package org.eve.framework.raven.definition;

/**
 * @author xyc
 * @date 2018/2/11 0011
 */
public class RavenComponentDefinition {
    private String beanName;
    private String className;

    public RavenComponentDefinition(String beanName, String className) {
        this.beanName = beanName;
        this.className = className;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
