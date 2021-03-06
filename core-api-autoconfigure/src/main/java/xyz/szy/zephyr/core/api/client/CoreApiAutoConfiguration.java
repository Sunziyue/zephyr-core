package xyz.szy.zephyr.core.api.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.szy.zephyr.core.api.service.CacheOrderService;
import xyz.szy.zephyr.core.api.service.ProcessOrderService;
import xyz.szy.zephyr.dubbo.consumer.ServiceSubscriber;

@Configuration
public class CoreApiAutoConfiguration {

    @Value("${order.api.version:2.6.2}")
    private String version;

    private final ServiceSubscriber serviceSubscriber;

    @Autowired
    public CoreApiAutoConfiguration(ServiceSubscriber serviceSubscriber) {
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
