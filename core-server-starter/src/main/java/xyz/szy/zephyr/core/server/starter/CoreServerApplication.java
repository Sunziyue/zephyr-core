package xyz.szy.zephyr.core.server.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreServerApplication.class, args);

        // 这块考虑移动到 dubbo starter
        HoldProcessor holdProcessor = new HoldProcessor();
        holdProcessor.startAwait();
        Runtime.getRuntime().addShutdownHook(new Thread(holdProcessor::stopAwait));
    }
}
