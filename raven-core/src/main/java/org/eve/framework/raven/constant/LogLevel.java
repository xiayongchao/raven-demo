package org.eve.framework.raven.constant;

import org.eve.framework.raven.util.VerifyUtils;
import org.slf4j.spi.LocationAwareLogger;

/**
 * 日志等级
 *
 * @author yc_xia
 * @date 2018/5/25
 */
public enum LogLevel {
    DEBUG(LocationAwareLogger.DEBUG_INT),

    INFO(LocationAwareLogger.INFO_INT),

    WARN(LocationAwareLogger.WARN_INT),

    ERROR(LocationAwareLogger.ERROR_INT),

    TRACE(LocationAwareLogger.TRACE_INT),

    OFF(LocationAwareLogger.ERROR_INT + 10);

    private final int value;

    LogLevel(int value) {
        this.value = value;
    }

    public static boolean contains(String logType) {
        if (VerifyUtils.isNull(logType)) {
            return false;
        }
        logType = logType.toUpperCase();
        for (LogLevel type : LogLevel.values()) {
            if (logType.equals(type.toString())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(LogLevel.DEBUG.toString());
        System.out.println(LogLevel.valueOf("DEBUG"));
    }

    /**
     * 大于等于
     *
     * @param logLevel
     * @return
     */
    public boolean ge(LogLevel logLevel) {
        return this.value >= logLevel.value;
    }
}
