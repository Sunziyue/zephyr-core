package com.jjy.ip.hiboos.order.server.starter;

import com.jjy.ip.hiboos.order.server.OrderServerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderServerAutoConfiguration.class})
public class OrderServerStarterAutoConfiguration {

}
