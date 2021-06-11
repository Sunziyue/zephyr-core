package com.jjy.ip.hiboos.order.server.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jjy.ip.hiboos.order.server.model.CacheOrder;
import io.terminus.common.mysql.dao.MyBatisDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sunziyue
 */
@Slf4j
@Repository
public class CacheOrderDao extends MyBatisDao<CacheOrder> {

    /**
     * 根据 baidu_shop_id_List 查询出
     */
    public Map<String, List<CacheOrder>> findByBaiduShopId(Map<String, Object> criteria) {
        List<CacheOrder> cacheOrderList = this.sqlSession.selectList(this.sqlId("findByBaiduShopId"), criteria);
        return cacheOrderList.stream().collect(Collectors.toMap(
                CacheOrder::getMktId,
                cacheOrder -> {
                    List<CacheOrder> cacheOrders = Lists.newArrayList();
                    cacheOrders.add(cacheOrder);
                    return cacheOrders;
                },
                (List<CacheOrder> target, List<CacheOrder> increment) -> {
                    target.addAll(increment);
                    return target;
                }
        ));
    }

    /**
     * 获取当天最大流水号
     */
    public Integer largestSerialNo(String mktId, String today) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("mktId", mktId);
        criteria.put("processDate", today);
        return this.sqlSession.selectOne(this.sqlId("largestSerialNo"), criteria);
    }

    /**
     * @param cacheOrderList
     */
    public void updateProcessStatus(List<CacheOrder> cacheOrderList) {
        cacheOrderList.forEach(cacheOrder -> this.sqlSession.update(this.sqlId("updateProcessStatus"), cacheOrder));
    }

    /**
     * @param criteria
     * @return
     */
    public Map<String, CacheOrder> findByChannelOrderIds(Map<String, Object> criteria) {
        List<CacheOrder> cacheOrderList = this.sqlSession.selectList(this.sqlId("findByChannelOrderIds"), criteria);
        return cacheOrderList.stream().collect(Collectors.toMap(CacheOrder::getChannelOrderId, cacheOrder -> cacheOrder));
    }
}
