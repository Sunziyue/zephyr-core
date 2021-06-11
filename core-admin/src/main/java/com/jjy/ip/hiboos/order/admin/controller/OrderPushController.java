package com.jjy.ip.hiboos.order.admin.controller;

import com.jjy.ip.hiboos.order.api.model.OrderRequest;
import com.jjy.ip.hiboos.order.api.model.OrderResponse;
import com.jjy.ip.hiboos.order.api.service.CacheOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sunziyue
 * @date 2020-09-16 16:26
 * @apiNote 订单核销接口
 */
@Slf4j
@RestController("/order")
@Api(tags = "海博订单核销接口")
public class OrderPushController {

    private final CacheOrderService cacheOrderService;

    @Autowired
    public OrderPushController(CacheOrderService cacheOrderService) {
        this.cacheOrderService = cacheOrderService;
    }

    @ApiOperation("海博订单核销接口")
    @PostMapping("/push")
    public OrderResponse orderPush(OrderRequest request) {
        return this.cacheOrderService.cache(request);
    }

}
