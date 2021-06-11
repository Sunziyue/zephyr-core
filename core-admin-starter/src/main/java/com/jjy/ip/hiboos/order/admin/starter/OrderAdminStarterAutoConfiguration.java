package com.jjy.ip.hiboos.order.admin.starter;

import com.jjy.ip.hiboos.order.admin.OrderAdminAutoConfiguration;
import io.terminus.common.swagger.autoconfig.SwaggerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan
@Import({OrderAdminAutoConfiguration.class, SwaggerAutoConfiguration.class})
public class OrderAdminStarterAutoConfiguration {

}
