package xyz.szy.zephyr.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class)
public class TestApplication {
    @Test
    public void test(){
        System.out.println("this is test case");
    }
}
