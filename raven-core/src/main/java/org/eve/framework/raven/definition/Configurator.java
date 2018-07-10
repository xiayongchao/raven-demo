package org.eve.framework.raven.definition;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class Configurator extends AbstractExpansion {
    /**
     * 是否开启配置定时扫描更新功能
     */
    private Boolean scan;
    /**
     * 扫描更新时间间隔
     */
    private Long scanPeriod;
    /**
     * 用户自定义实现的定时器类名，用于加载
     */
    private String schedulerClassName;
    /**
     * 描述
     */
    private String description;

    public Boolean getScan() {
        return scan;
    }

    public void setScan(Boolean scan) {
        this.scan = scan;
    }

    public Long getScanPeriod() {
        return scanPeriod;
    }

    public void setScanPeriod(Long scanPeriod) {
        this.scanPeriod = scanPeriod;
    }

    public String getSchedulerClassName() {
        return schedulerClassName;
    }

    public void setSchedulerClassName(String schedulerClassName) {
        this.schedulerClassName = schedulerClassName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
