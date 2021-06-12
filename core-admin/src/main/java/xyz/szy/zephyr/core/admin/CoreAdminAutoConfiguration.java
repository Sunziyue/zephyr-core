package xyz.szy.zephyr.core.admin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.szy.zephyr.core.api.client.CoreApiAutoConfiguration;

@Configuration
@ComponentScan
@Import({CoreApiAutoConfiguration.class})
public class CoreAdminAutoConfiguration implements WebMvcConfigurer {

}
