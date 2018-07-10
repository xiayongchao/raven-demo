package org.eve.framework.raven.definition;

import org.eve.framework.raven.constant.LogLevel;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class LoggerDefinition extends AbstractExpansion {
    private Integer id;
    private Integer printerRef;
    private Integer formatterRef;
    private Boolean transmit;
    private String levelName;
    private LogLevel level;
    private String scope;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrinterRef() {
        return printerRef;
    }

    public void setPrinterRef(Integer printerRef) {
        this.printerRef = printerRef;
    }

    public Integer getFormatterRef() {
        return formatterRef;
    }

    public void setFormatterRef(Integer formatterRef) {
        this.formatterRef = formatterRef;
    }

    public Boolean getTransmit() {
        return transmit;
    }

    public void setTransmit(Boolean transmit) {
        this.transmit = transmit;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 只要id或者scope中一项相等则整个对象相等
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

        LoggerDefinition that = (LoggerDefinition) o;

        if (id != null && id.equals(that.id)) {
            return true;
        }
        if (id == null && that.id == null) {
            return true;
        }
        if (scope != null && scope.equals(that.scope)) {
            return true;
        }
        if (scope == null && that.scope == null) {
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
