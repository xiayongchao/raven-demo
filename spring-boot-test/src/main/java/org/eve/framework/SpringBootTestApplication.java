package org.eve.framework;

import org.eve.framework.raven.configuration.EnableRavenConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yc_xia
 */
@SpringBootApplication
@EnableRavenConfiguration(basePackages = "org.eve.framework.log")
public class SpringBootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApplication.class, args);
    }
}
