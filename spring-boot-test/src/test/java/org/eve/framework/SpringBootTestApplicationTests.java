package org.eve.framework;

import org.eve.framework.service.TestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTestApplicationTests {
    @Test
    public void contextLoads() throws InterruptedException {
        LoggerFactory.getLogger(TestServiceImpl.class).error("B{}2{}431242", "@", "*");
//        Thread.sleep(15000);
//        LoggerFactory.getLogger(TestServiceImpl.class).error("发发{}大{}发的萨芬撒", "@", "*");
    }

    public static void main(String[] args) throws InterruptedException {
        LoggerFactory.getLogger(SpringBootTestApplicationTests.class).info("SpringBootTestApplicationTests{}fasdfdas{}431242", "哈哈哈哈");
    }
}
