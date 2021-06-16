package xyz.szy.zephyr.core.admin.starter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.szy.zephyr.core.admin.CoreAdminAutoConfiguration;


@Configuration
@ComponentScan
@Import({CoreAdminAutoConfiguration.class})
public class CoreAdminStarterAutoConfiguration {

}
