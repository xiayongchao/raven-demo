package org.eve.framework.raven.definition;

import org.eve.framework.raven.configuration.EnableRavenConfiguration;
import org.springframework.util.Assert;

/**
 * @author xyc
 * @date 2018/4/2 0002
 */
public class RavenConfigurationDefinition {
    private String[] basePackages;
    private String resourcePattern = "**/*.class";

    public RavenConfigurationDefinition(String[] basePackages, String resourcePattern) {
        this.basePackages = basePackages;
        this.resourcePattern = resourcePattern;
    }

    public RavenConfigurationDefinition(EnableRavenConfiguration enableRavenConfiguration) {
        Assert.notNull(enableRavenConfiguration, "enableRavenConfiguration不能为空");
        this.basePackages = enableRavenConfiguration.basePackages();
        this.resourcePattern = enableRavenConfiguration.resourcePattern();
    }


    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public String getResourcePattern() {
        return resourcePattern;
    }

    public void setResourcePattern(String resourcePattern) {
        this.resourcePattern = resourcePattern;
    }
}
