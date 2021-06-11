package com.jjy.ip.hiboos.order.server.service;

import com.google.common.base.Throwables;
import com.jjy.ip.hiboos.order.api.exception.ChannelUnKnowException;
import com.jjy.ip.hiboos.order.api.exception.ConfigException;
import com.jjy.ip.hiboos.order.api.model.OrderRequest;
import com.jjy.ip.hiboos.order.api.model.OrderResponse;
import com.jjy.ip.hiboos.order.api.service.CacheOrderService;
import com.jjy.ip.hiboos.order.server.convert.CacheConvert;
import com.jjy.ip.hiboos.order.server.dao.CacheOrderDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author Sunziyue
 * @date 2020-09-16 16:26
 * @apiNote 缓存订单核销
 */
@Slf4j
@Service
public class CacheOrderServiceImpl implements CacheOrderService {

    private final CacheOrderDao cacheOrderDao;
    private final CacheConvert convert;

    @Autowired
    public CacheOrderServiceImpl(CacheOrderDao cacheOrderDao,
                                 CacheConvert convert) {
        this.cacheOrderDao = cacheOrderDao;
        this.convert = convert;
    }

    /**
     * 缓存订单核销
     * @param request 订单核销接口入参
     * @return 订单核销接口返回值
     */
    @Override
    public OrderResponse cache(OrderRequest request) {
        try {
            this.cacheOrderDao.create(this.convert.orderToCache(request));
            return OrderResponse.OK();
        } catch (DuplicateKeyException e) {
            log.warn("WARN : {} 此销售单已存在", request.body().getTransactionId());
            return OrderResponse.OK();
        } catch (ConfigException | ChannelUnKnowException e) {
            log.error("ERROR : {}", Throwables.getStackTraceAsString(e));
            return OrderResponse.NG(e.getMessage());
        } catch (Exception e) {
            log.error("ERROR : {}", Throwables.getStackTraceAsString(e));
            return OrderResponse.NG();
        }
    }
}
