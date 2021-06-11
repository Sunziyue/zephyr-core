package com.jjy.ip.hiboos.order.admin;

import com.jjy.ip.hiboos.order.client.OrderApiAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
@Import({OrderApiAutoConfiguration.class})
public class OrderAdminAutoConfiguration extends WebMvcConfigurerAdapter {

}
