package org.eve.framework.raven.definition;

import org.eve.framework.raven.constant.LogLevel;

/**
 * @author yc_xia
 * @date 2018/6/22
 */
public class LogContext {
    private final Metadata metadata;
    private final LogLevel logLevel;
    private final String originalMsg;
    private final Object[] arguments;

    private String formattedMsg;
    private Throwable throwable;

    public LogContext(Metadata metadata, LogLevel logLevel, String originalMsg, Object[] arguments) {
        this.metadata = metadata;
        this.logLevel = logLevel;
        this.originalMsg = originalMsg;
        this.arguments = arguments;
    }

    public String renderLevel() {
        switch (this.logLevel) {
            case TRACE:
                return "TRACE";
            case DEBUG:
                return ("DEBUG");
            case INFO:
                return "INFO";
            case WARN:
                return this.metadata.getWarnLevelString();
            case ERROR:
                return "ERROR";
        }
        throw new IllegalStateException("Unrecognized level [" + this.logLevel + "]");
    }

    public String computeShortName() {
        return this.metadata.getLogName().substring(this.metadata.getLogName().lastIndexOf(".") + 1);
    }

    public void setResult(String formattedMsg, Throwable throwable) {
        this.formattedMsg = formattedMsg;
        this.throwable = throwable;
    }

    public String getFormattedMsg() {
        return formattedMsg;
    }

    public void setFormattedMsg(String formattedMsg) {
        this.formattedMsg = formattedMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getOriginalMsg() {
        return originalMsg;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
