package org.eve.framework;

import org.eve.framework.raven.support.RavenBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * @author yc_xia
 */
@SpringBootApplication
public class SpringBootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApplication.class, args);
    }

/*
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RavenBeanPostProcessor ravenBeanPostProcessor() {
        return new RavenBeanPostProcessor();
    }
*/
}
