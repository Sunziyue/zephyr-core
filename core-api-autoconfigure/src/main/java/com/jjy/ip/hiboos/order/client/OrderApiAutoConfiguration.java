package com.jjy.ip.hiboos.order.client;

import com.jjy.ip.hiboos.order.api.service.CacheOrderService;
import com.jjy.ip.hiboos.order.api.service.ProcessOrderService;
import io.terminus.boot.rpc.dubbo.light.consumer.ServiceSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApiAutoConfiguration {

    @Value("${order.api.version:2.6.2}")
    private String version;

    private final ServiceSubscriber serviceSubscriber;

    @Autowired
    public OrderApiAutoConfiguration(ServiceSubscriber serviceSubscriber) {
        this.serviceSubscriber = serviceSubscriber;
    }

    @Bean
    public CacheOrderService cacheOrderFacade() {
        return this.serviceSubscriber.consumer(CacheOrderService.class).timeout(60000).version(version).subscribe();
    }

    @Bean
    public ProcessOrderService processOrderFacade() {
        return this.serviceSubscriber.consumer(ProcessOrderService.class).timeout(60000).version(version).subscribe();
    }
}
