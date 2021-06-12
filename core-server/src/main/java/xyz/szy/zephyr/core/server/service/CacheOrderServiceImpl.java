package xyz.szy.zephyr.core.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.szy.zephyr.core.api.model.OrderRequest;
import xyz.szy.zephyr.core.api.model.OrderResponse;
import xyz.szy.zephyr.core.api.service.CacheOrderService;

@Slf4j
@Service
public class CacheOrderServiceImpl implements CacheOrderService {
    @Override
    public OrderResponse cache(OrderRequest request) {
        System.out.println(request);
        return OrderResponse.OK();
    }
}
