package org.eve.framework.raven.support;

import org.eve.framework.raven.exception.RavenException;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author yc_xia
 * @date 2018/7/5
 */
@Component
public class RavenBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;
    private SpringBeanFactory loggerFactory = StaticLoggerBinder.getRavenLoggerFactory();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        this.loggerFactory.setBeanFactory(this.beanFactory);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanType = bean.getClass();
        if (!beanType.isAnnotationPresent(RavenBean.class)) {
            return bean;
        }
        String className = beanType.getName();
        Object object;
        if (this.beanFactory.containsBean(beanName)) {
            object = this.beanFactory.getBean(beanName);
        } else {
            try {
                object = this.loggerFactory.getBean(className, false);
                this.beanFactory.registerSingleton(beanName, object);
            } catch (RavenException e) {
                throw new RuntimeException(e);
            }
        }
        this.loggerFactory.registerObject(className, beanName, object);
        return bean;
    }
}
