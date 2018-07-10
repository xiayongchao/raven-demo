package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.BeanFactory;
import org.eve.framework.raven.definition.Configurator;
import org.eve.framework.raven.definition.Raven;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.util.VerifyUtils;

import java.util.Arrays;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class ConfiguratorManager {
    private ConfiguratorScheduler configuratorScheduler;
    private ConfiguratorListener configuratorListener;
    private ConfiguratorValidator configuratorValidator;
    private final BeanFactory beanFactory;

    public static final ConfiguratorManager getInstance(BeanFactory beanFactory) throws RavenException {
        return new ConfiguratorManager(beanFactory);
    }

    private ConfiguratorManager(BeanFactory beanFactory) throws RavenException {
        this.beanFactory = beanFactory;
    }

    /**
     * ConfiguratorListener和ConfiguratorValidator无法通过配置信息指定，因为他们两个早于配置信息
     *
     * @return
     * @throws RavenException
     */
    public Raven init() throws RavenException {
        this.configuratorListener = this.beanFactory.getBean(ConfiguratorListener.class);
        if (VerifyUtils.isNull(this.configuratorListener)) {
            throw new RavenException(String.format("获取%s实现类失败", ConfiguratorListener.class.getName()));
        }
        this.configuratorValidator = this.beanFactory.getBean(ConfiguratorValidator.class);
        if (VerifyUtils.isNull(this.configuratorValidator)) {
            throw new RavenException(String.format("获取%s实现类失败", ConfiguratorValidator.class.getName()));
        }

        Raven raven = this.configuratorListener.initialConfiguration();
        this.configuratorValidator.validate(raven);
        return raven;
    }

    /**
     * 开启定时器
     *
     * @param configurator
     * @throws RavenException
     */
    public void startScheduler(Configurator configurator) throws RavenException {
        if (VerifyUtils.isNull(configurator)) {
            return;
        }
        if (VerifyUtils.notBlank(configurator.getSchedulerClassName())) {
            this.configuratorScheduler = this.beanFactory.getBean(configurator.getSchedulerClassName());
        } else {
            this.configuratorScheduler = this.beanFactory.getBean(ConfiguratorScheduler.class);
        }

        BaseUpdateTask updateTask = this.beanFactory.getBean(BaseUpdateTask.class,
                Arrays.asList(ConfiguratorListener.class, ConfiguratorValidator.class, ConfigurationRefresher.class),
                Arrays.asList(this.configuratorListener, this.configuratorValidator, this.beanFactory));
        this.configuratorScheduler.start(updateTask, configurator.getScanPeriod());
    }
}
