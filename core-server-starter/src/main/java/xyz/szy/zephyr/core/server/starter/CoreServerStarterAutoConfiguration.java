package xyz.szy.zephyr.core.server.starter;

import xyz.szy.zephyr.core.server.CoreServerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreServerAutoConfiguration.class})
public class CoreServerStarterAutoConfiguration {

}
