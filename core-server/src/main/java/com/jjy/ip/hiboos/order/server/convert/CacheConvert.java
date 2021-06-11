package com.jjy.ip.hiboos.order.server.convert;

import com.google.common.collect.Maps;
import com.jjy.ip.hiboos.core.api.model.OnlineAppCode;
import com.jjy.ip.hiboos.core.api.service.OnlineAppCodeService;
import com.jjy.ip.hiboos.order.api.enums.ChannelEnum;
import com.jjy.ip.hiboos.order.api.enums.OrderEnum;
import com.jjy.ip.hiboos.order.api.exception.ConfigException;
import com.jjy.ip.hiboos.order.api.model.Body;
import com.jjy.ip.hiboos.order.api.model.OrderRequest;
import com.jjy.ip.hiboos.order.api.utils.date.DateUtils;
import com.jjy.ip.hiboos.order.server.model.CacheOrder;
import io.terminus.common.utils.Arguments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Sunziyue
 * @date 2020-09-17 10:10
 * @apiNote 订单缓存转换器
 */
@Slf4j
@Component
public class CacheConvert {

    private final OnlineAppCodeService onlineAppCodeService;

    @Autowired
    public CacheConvert(OnlineAppCodeService onlineAppCodeService) {
        this.onlineAppCodeService = onlineAppCodeService;
    }

    public CacheOrder orderToCache(OrderRequest request) throws Exception {
        Body body = request.body();
        ChannelEnum channelEnum = ChannelEnum.getByHibChannel(body.getChannel());
        OnlineAppCode onlineAppCode = this.buildOnlineAppCode(body.getOutStationNo());
        if (Arguments.isNull(onlineAppCode)) {
            throw new ConfigException("门店[" + body.getOutStationNo() + "]未配置, 或未启用.");
        }
        return CacheOrder.builder()
                .channel(channelEnum.jjyChannel())
                .posId(channelEnum.posId())
                .cash(channelEnum.cash())
                .payId(channelEnum.payId())
                .parentCode(channelEnum.parentCode())
                .transactionId(body.getTransactionId())
                .channelOrderId(body.getChannelOrderId())
                .orderType(body.getRefund() ? OrderEnum.RETURN_ORDER.statue() : OrderEnum.SUBMIT_ORDER.statue())
                .mktId(onlineAppCode.getDrp_id())
                .drpId(onlineAppCode.getDrpid())
                .serialNo(10000)
                .jsonCache(request.getBody())
                .createTime(DateUtils.date())
                .processDate(DateUtils.processDate())
                .processStatus(OrderEnum.UNTREATED.statue())
                .build();
    }

    private OnlineAppCode buildOnlineAppCode(String mkt_id) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("channel", "7");
        criteria.put("status", "1");
        criteria.put("drp_id", mkt_id);
        return this.onlineAppCodeService.findByCriteria(criteria);
    }


}
