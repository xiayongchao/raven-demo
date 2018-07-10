package org.eve.framework.raven.configuration;

import org.eve.framework.raven.support.RavenBeanPostProcessor;
import org.eve.framework.raven.support.RavenComponentScanParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
@Configuration
public class RavenConfiguration {
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean("org.eve.framework.raven.support.RavenBeanPostProcessor")
    public RavenBeanPostProcessor postmanBeanPostProcessor(RavenComponentScanParser ravenComponentScanParser) {
        return new RavenBeanPostProcessor(ravenComponentScanParser);
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean("org.eve.framework.raven.support.RavenComponentScanParser")
    public RavenComponentScanParser ravenComponentScanParser() {
        return new RavenComponentScanParser();
    }
}
