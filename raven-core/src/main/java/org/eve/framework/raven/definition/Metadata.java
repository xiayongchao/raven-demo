package org.eve.framework.raven.definition;

import org.eve.framework.raven.constant.LogLevel;

import java.io.IOException;
import java.text.DateFormat;

/**
 * @author yc_xia
 * @date 2018/6/22
 */
public class Metadata extends AbstractExpansion {
    private final long startTime;
    private final Integer appId;
    private final String appName;
    private Integer loggerId;
    private String logName;
    private String shortLogName;
    private LogLevel currentLogLevel;
    private String dateTimeFormat;
    private DateFormat dateTimeFormatter;
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

    public Metadata(long startTime, Integer appId, String appName) {
        this.startTime = startTime;
        this.appId = appId;
        this.appName = appName;
    }

    @Override
    public Metadata deepCopy(boolean useSelf) throws IOException, ClassNotFoundException {
        if (useSelf) {
            return this;
        }
        Metadata target = new Metadata(this.getStartTime(), this.getAppId(), this.getAppName());
        target.setLoggerId(this.getLoggerId());
        target.setLogName(this.getLogName());
        target.setCurrentLogLevel(this.getCurrentLogLevel());
        target.setDateTimeFormat(this.getDateTimeFormat());
        target.setDateTimeFormatter(this.getDateTimeFormatter());
        target.setLevelInBrackets(this.getLevelInBrackets());
        target.setWarnLevelString(this.getWarnLevelString());
        target.setShowAppId(this.getShowAppId());
        target.setShowAppName(this.getShowAppName());
        target.setShowLoggerId(this.getShowLoggerId());
        target.setShowThreadName(this.getShowThreadName());
        target.setShowDateTime(this.getShowDateTime());
        target.setShowShortLogName(this.getShowShortLogName());
        target.setShowLogName(this.getShowLogName());
        target.setExpansionMap(super.deepCopyExpansionMap());
        return target;
    }

    public boolean isLevelEnabled(LogLevel logLevel) {
        return logLevel.ge(this.currentLogLevel);
    }

    public long getStartTime() {
        return startTime;
    }

    public Integer getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public Integer getLoggerId() {
        return loggerId;
    }

    public void setLoggerId(Integer loggerId) {
        this.loggerId = loggerId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getShortLogName() {
        return shortLogName;
    }

    public void setShortLogName(String shortLogName) {
        this.shortLogName = shortLogName;
    }

    public LogLevel getCurrentLogLevel() {
        return currentLogLevel;
    }

    public void setCurrentLogLevel(LogLevel currentLogLevel) {
        this.currentLogLevel = currentLogLevel;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public DateFormat getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateFormat dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
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
}
