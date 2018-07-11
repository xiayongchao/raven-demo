package org.eve.framework.raven.definition;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class Raven extends AbstractExpansion {
    private String appId;
    private String appName;
    private Configurator configurator;
    private Configuration configuration;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Configurator getConfigurator() {
        return configurator;
    }

    public void setConfigurator(Configurator configurator) {
        this.configurator = configurator;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
