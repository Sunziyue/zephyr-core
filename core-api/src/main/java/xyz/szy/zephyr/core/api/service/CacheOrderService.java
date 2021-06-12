package xyz.szy.zephyr.core.api.service;


import xyz.szy.zephyr.core.api.model.OrderRequest;
import xyz.szy.zephyr.core.api.model.OrderResponse;

/**
 * @author Sunziyue
 * @date 2020-09-16 16:26
 * @apiNote 缓存订单核销
 */
public interface CacheOrderService {

    /**
     * 缓存订单核销
     * @param request 订单核销接口入参
     * @return 订单核销接口返回值
     */
    OrderResponse cache(OrderRequest request);
}
