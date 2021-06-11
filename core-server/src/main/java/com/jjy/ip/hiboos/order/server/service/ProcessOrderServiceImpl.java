package com.jjy.ip.hiboos.order.server.service;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jjy.ip.hiboos.core.api.model.OnlineAppCode;
import com.jjy.ip.hiboos.core.api.service.OnlineAppCodeService;
import com.jjy.ip.hiboos.order.api.enums.OrderEnum;
import com.jjy.ip.hiboos.order.api.service.ProcessOrderService;
import com.jjy.ip.hiboos.order.server.component.CreateReturnOrderHdtComponent;
import com.jjy.ip.hiboos.order.server.component.CreateSubmitOrderHdtComponent;
import com.jjy.ip.hiboos.order.server.dao.CacheOrderDao;
import com.jjy.ip.hiboos.order.server.model.CacheOrder;
import io.terminus.common.utils.Arguments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sunziyue
 * @date 2020/09/14 18:03
 * @apiNote 处理订单总入口
 */
@Slf4j
@Service
public class ProcessOrderServiceImpl implements ProcessOrderService {

    private final OnlineAppCodeService onlineAppCodeService;
    private final CacheOrderDao cacheOrderDao;
    private final CreateSubmitOrderHdtComponent createSubmitOrderHdtComponent;
    private final CreateReturnOrderHdtComponent createReturnOrderHdtComponent;

    @Autowired
    public ProcessOrderServiceImpl(OnlineAppCodeService onlineAppCodeService, CacheOrderDao cacheOrderDao,
                                   CreateSubmitOrderHdtComponent createSubmitOrderHdtComponent,
                                   CreateReturnOrderHdtComponent createReturnOrderHdtComponent) {
        this.onlineAppCodeService = onlineAppCodeService;
        this.cacheOrderDao = cacheOrderDao;
        this.createSubmitOrderHdtComponent = createSubmitOrderHdtComponent;
        this.createReturnOrderHdtComponent = createReturnOrderHdtComponent;
    }

    /**
     * 处理订单总入口
     *
     * @param subnetId 数据库节点
     * @return 数据库节点 处理情况
     */
    // @Monitor(id = "20")
    @Override
    public void processOrderBySubnetId(String subnetId) {
        try {
            // 通过 subnetId 去online_app_code 查出 对应的 门店信息
            List<OnlineAppCode> onlineList = this.onlineAppCodeService.findBySubnetId(subnetId);
            if (Arguments.notEmpty(onlineList)) {
                // 收集线上门店号
                List<String> drp_idList = onlineList.stream().map(OnlineAppCode::getDrp_id).collect(Collectors.toList());
                // 创建缓存表查询条件
                Map<String, Object> criteria = Maps.newHashMap();
                criteria.put("processStatus", OrderEnum.UNTREATED.statue());
                criteria.put("drp_idList", drp_idList);
                // 使用门店信息查询 缓存表
                Map<String, List<CacheOrder>> cacheOrderMap = this.cacheOrderDao.findByBaiduShopId(criteria);
                // 缓存处理完的订单
                List<CacheOrder> cacheOrderList = Lists.newArrayList();
                // 按门店生成 jjy_bill_id
                cacheOrderMap.forEach((mktId, cacheList) -> {
                    Map<String, List<CacheOrder>> groupMap = cacheList.stream().collect(Collectors.groupingBy(CacheOrder::getProcessDate));
                    groupMap.forEach((processDate, groupList) -> {
                        if (Arguments.notEmpty(cacheList)) {
                            // 查询当前门店最大的流水号,当天第一次执行获取到的是"10000"
                            Integer largestSerialNo = this.cacheOrderDao.largestSerialNo(mktId, processDate);
                            int flag = 0;
                            for (CacheOrder cache : groupList) {
                                try {
                                    // 之前已经生成过 bill 的 cache 不会重新生成了, 延用之前的 流水号 和 bill,防止断号.
                                    if (Arguments.isEmpty(cache.getJjyBillId())) {
                                        String serialNo = String.valueOf(largestSerialNo + (++flag));
                                        String jjy_bill_id = Joiner.on("").join(mktId, cache.getPosId(), cache.getProcessDate(), serialNo);
                                        cache.setSerialNo(Integer.parseInt(serialNo));
                                        cache.setJjyBillId(jjy_bill_id);
                                    }
                                } catch (Exception e) {
                                    cache.NG("生成 bill 失败");
                                    log.error("生成 bill 失败 订单号: {}\n ERROR: {}", cache.getTransactionId(), Throwables.getStackTraceAsString(e));
                                }
                                cacheOrderList.add(cache);
                            }
                        }
                    });
                });
                // 开始处理订单真实数据 生成 HDT 发送DRP 返回hdt的状态
                List<CacheOrder> cacheList = this.processJson(cacheOrderList, subnetId);
                // 这里判断非空是因为在 processJson::create[Submit/Return]OrderHdtService 中可能会因为数据库错误被清空
                if (Arguments.notEmpty(cacheList)) {
                    // 当前分片处理结束后，更新数据库中的状态。
                    this.cacheOrderDao.updateProcessStatus(cacheList);
                }
            } else {
                log.warn("subnetId : [{}], 下没有配置门店或没有生效的门店", subnetId);
            }
        } catch (Exception e) {
            log.error("ERROR:\n{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 开始处理订单真实数据 生成 HDT 发送DRP
     *
     * @param cacheOrderList
     */
    private List<CacheOrder> processJson(List<CacheOrder> cacheOrderList, String subnetId) {
        this.createSubmitOrderHdtComponent.create(cacheOrderList, subnetId);
        this.createReturnOrderHdtComponent.create(cacheOrderList, subnetId);
        return cacheOrderList;
    }
}
