package org.eve.framework.raven.definition;

import org.eve.framework.raven.util.VerifyUtils;

import java.util.List;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class FormatterDefinition extends AbstractExpansion {
    private Integer id;
    private String className;
    private String dateTimeFormat;
    private Boolean showAppId;
    private Boolean showAppName;
    private Boolean showLoggerId;
    private Boolean showDateTime;
    private Boolean showThreadName;
    private Boolean showLogName;
    private Boolean showShortLogName;
    private Boolean levelInBrackets;
    private String warnLevelString;
    private String description;
    private List<Integer> processorRef;

    private static FormatterDefinition defaultFormatterDefinition;

    public static FormatterDefinition getDefault() {
        if (VerifyUtils.isNull(defaultFormatterDefinition)) {
            defaultFormatterDefinition = new FormatterDefinition();
            defaultFormatterDefinition.setDateTimeFormat("yyyy-MM-dd HH:ss:mm.SSS");
            defaultFormatterDefinition.setShowAppId(false);
            defaultFormatterDefinition.setShowAppName(false);
            defaultFormatterDefinition.setShowLoggerId(false);
            defaultFormatterDefinition.setShowDateTime(true);
            defaultFormatterDefinition.setShowThreadName(true);
            defaultFormatterDefinition.setShowLogName(true);
            defaultFormatterDefinition.setShowShortLogName(false);
            defaultFormatterDefinition.setLevelInBrackets(true);
            defaultFormatterDefinition.setWarnLevelString("WARN");
        }
        return defaultFormatterDefinition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public Boolean getShowAppId() {
        return showAppId;
    }

    public void setShowAppId(Boolean showAppId) {
        this.showAppId = showAppId;
    }

    public Boolean getShowAppName() {
        return showAppName;
    }

    public void setShowAppName(Boolean showAppName) {
        this.showAppName = showAppName;
    }

    public Boolean getShowLoggerId() {
        return showLoggerId;
    }

    public void setShowLoggerId(Boolean showLoggerId) {
        this.showLoggerId = showLoggerId;
    }

    public Boolean getShowDateTime() {
        return showDateTime;
    }

    public void setShowDateTime(Boolean showDateTime) {
        this.showDateTime = showDateTime;
    }

    public Boolean getShowThreadName() {
        return showThreadName;
    }

    public void setShowThreadName(Boolean showThreadName) {
        this.showThreadName = showThreadName;
    }

    public Boolean getShowLogName() {
        return showLogName;
    }

    public void setShowLogName(Boolean showLogName) {
        this.showLogName = showLogName;
    }

    public Boolean getShowShortLogName() {
        return showShortLogName;
    }

    public void setShowShortLogName(Boolean showShortLogName) {
        this.showShortLogName = showShortLogName;
    }

    public Boolean getLevelInBrackets() {
        return levelInBrackets;
    }

    public void setLevelInBrackets(Boolean levelInBrackets) {
        this.levelInBrackets = levelInBrackets;
    }

    public String getWarnLevelString() {
        return warnLevelString;
    }

    public void setWarnLevelString(String warnLevelString) {
        this.warnLevelString = warnLevelString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getProcessorRef() {
        return processorRef;
    }

    public void setProcessorRef(List<Integer> processorRef) {
        this.processorRef = processorRef;
    }

    /**
     * 只要id或者className中一项相等则整个对象相等
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FormatterDefinition that = (FormatterDefinition) o;

        if (id != null && id.equals(that.id)) {
            return true;
        }
        if (id == null && that.id == null) {
            return true;
        }
        if (className != null && className.equals(that.className)) {
            return true;
        }
        if (className == null && that.className == null) {
            return true;
        }

        return false;
    }

    /**
     * hashCode返回值一致，将比较是否相等交给equals方法进行判断
     *
     * @return
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
