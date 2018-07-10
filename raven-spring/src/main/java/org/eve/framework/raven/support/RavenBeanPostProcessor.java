package org.eve.framework.raven.support;

import org.eve.framework.raven.configuration.EnableRavenConfiguration;
import org.eve.framework.raven.definition.RavenComponentDefinition;
import org.eve.framework.raven.definition.RavenConfigurationDefinition;
import org.eve.framework.raven.exception.RavenException;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.util.Set;

/**
 * @author yc_xia
 * @date 2018/7/5
 */
public class RavenBeanPostProcessor implements MergedBeanDefinitionPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;
    private RavenComponentScanParser ravenComponentScanParser;
    private SpringRavenLoggerFactory loggerFactory = (SpringRavenLoggerFactory) StaticLoggerBinder.getRavenLoggerFactory();
    private RavenConfigurationDefinition ravenConfigurationDefinition;

    public RavenBeanPostProcessor(RavenComponentScanParser ravenComponentScanParser) {
        this.ravenComponentScanParser = ravenComponentScanParser;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        this.loggerFactory.setBeanFactory(this.beanFactory);
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (AnnotationUtils.findAnnotation(beanType, EnableRavenConfiguration.class) == null) {
            return;
        }
        Set<EnableRavenConfiguration> enableRavenConfigurations = AnnotationUtils.getRepeatableAnnotations(beanType, EnableRavenConfiguration.class);
        this.ravenConfigurationDefinition = new RavenConfigurationDefinition((EnableRavenConfiguration) enableRavenConfigurations.toArray()[0]);
        try {
            Set<RavenComponentDefinition> ravenComponentDefinitions = this.ravenComponentScanParser.parse(this.ravenConfigurationDefinition.getBasePackages(),
                    this.ravenConfigurationDefinition.getResourcePattern());
            this.ravenConfigurationDefinition = null;
            if (ravenComponentDefinitions == null || ravenComponentDefinitions.isEmpty()) {
                return;
            }

            for (RavenComponentDefinition ravenComponentDefinition : ravenComponentDefinitions) {
                String ravenBeanName = ravenComponentDefinition.getBeanName();
                String className = ravenComponentDefinition.getClassName();
                Object bean;
                if (this.beanFactory.containsBean(ravenBeanName)) {
                    bean = this.beanFactory.getBean(ravenBeanName);
                } else {
                    bean = this.loggerFactory.getBean(className, false);
                    this.beanFactory.registerSingleton(ravenBeanName, bean);
                }
                if ("org.eve.framework.log.MyPrintPostProcessor".equals(className)) {
                    System.out.println("向spring注册org.eve.framework.log.MyPrintPostProcessor");
                }
                this.loggerFactory.registerObject(className, ravenBeanName, bean);
            }
        } catch (IOException e) {
            throw new RavenException(e);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
