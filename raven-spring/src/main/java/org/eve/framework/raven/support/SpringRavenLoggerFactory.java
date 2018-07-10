package org.eve.framework.raven.support;

import org.eve.framework.raven.RavenLoggerFactory;
import org.eve.framework.raven.util.VerifyUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author xiayc
 * @date 2018/7/10
 */
public class SpringRavenLoggerFactory extends RavenLoggerFactory {
    private DefaultListableBeanFactory beanFactory;

    void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanName
     * @return
     */
    @Override
    protected Object getSpringBean(String beanName) {
        if (VerifyUtils.isNull(beanName) || VerifyUtils.isNull(this.beanFactory)) {
            return null;
        }
        try {
            return this.beanFactory.getBean(beanName);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanName
     * @param args
     * @return
     */
    @Override
    protected Object getSpringBean(String beanName, Object... args) {
        if (VerifyUtils.isNull(beanName) || VerifyUtils.isNull(this.beanFactory)) {
            return null;
        }
        try {
            return this.beanFactory.getBean(beanName, args);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanType
     * @return
     */
    @Override
    protected <T> T getSpringBean(Class<T> beanType) {
        if (VerifyUtils.isNull(beanType) || VerifyUtils.isNull(this.beanFactory)) {
            return null;
        }
        try {
            return this.beanFactory.getBean(beanType);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanType
     * @param args
     * @return
     */
    @Override
    protected <T> T getSpringBean(Class<T> beanType, Object... args) {
        if (VerifyUtils.isNull(beanType) || VerifyUtils.isNull(this.beanFactory)) {
            return null;
        }
        try {
            return this.beanFactory.getBean(beanType, args);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
