package xyz.szy.zephyr.core.admin.starter;

import xyz.szy.zephyr.core.admin.CoreAdminAutoConfiguration;
import io.terminus.common.swagger.autoconfig.SwaggerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan
@Import({CoreAdminAutoConfiguration.class, SwaggerAutoConfiguration.class})
public class CoreAdminStarterAutoConfiguration {

}
