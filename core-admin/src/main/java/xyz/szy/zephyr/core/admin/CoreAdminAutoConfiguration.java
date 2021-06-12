package xyz.szy.zephyr.core.admin;

import xyz.szy.zephyr.core.api.client.CoreApiAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
@Import({CoreApiAutoConfiguration.class})
public class CoreAdminAutoConfiguration extends WebMvcConfigurerAdapter {

}
