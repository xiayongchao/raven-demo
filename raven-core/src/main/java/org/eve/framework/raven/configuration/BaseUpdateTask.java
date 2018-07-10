package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.ScanBean;

/**
 * @author yc_xia
 * @date 2018/7/2
 */
@ScanBean
public abstract class BaseUpdateTask implements Runnable {
    protected final ConfiguratorListener configuratorListener;
    protected final ConfiguratorValidator configuratorValidator;
    protected final ConfigurationRefresher contextUpdater;

    public BaseUpdateTask(ConfiguratorListener configuratorListener, ConfiguratorValidator configuratorValidator, ConfigurationRefresher contextUpdater) {
        this.configuratorListener = configuratorListener;
        this.configuratorValidator = configuratorValidator;
        this.contextUpdater = contextUpdater;
    }
}
