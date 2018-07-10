package org.eve.framework.raven.logger;

import org.eve.framework.raven.constant.LogLevel;
import org.eve.framework.raven.definition.LogContext;
import org.eve.framework.raven.definition.Metadata;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.formatter.FormatChain;
import org.eve.framework.raven.printer.PrintChain;
import org.eve.framework.raven.util.VerifyUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.Util;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author yc_xia
 * @date 2018/6/21
 */
public class RavenLogger implements Logger {
    private Metadata metadata;
    private FormatChain formatChain;
    private PrintChain printChain;
    private RavenLogger transmitLogger;
    private final ReadWriteLock updateLock;

    public void setTransmitLogger(RavenLogger transmitLogger) {
        this.transmitLogger = transmitLogger;
    }

    public RavenLogger(Metadata metadata, FormatChain formatChain, PrintChain printChain, ReadWriteLock updateLock) throws RavenException {
        if (VerifyUtils.isNull(metadata)) {
            throw new RavenException("实例化RavenLogger失败,metadata不能为空");
        }
        if (VerifyUtils.isNull(formatChain)) {
            throw new RavenException("实例化RavenLogger失败,formatChain不能为空");
        }
        if (VerifyUtils.isNull(printChain)) {
            throw new RavenException("实例化RavenLogger失败,printChain不能为空");
        }
        if (VerifyUtils.isNull(updateLock)) {
            throw new RavenException("实例化RavenLogger失败,updateLock不能为空");
        }

        this.metadata = metadata;
        this.formatChain = formatChain;
        this.printChain = printChain;
        this.updateLock = updateLock;
    }

    private void log(LogLevel logLevel, String message) {
        this.log(logLevel, message, null, null);
    }

    private void log(LogLevel logLevel, String message, Throwable throwable) {
        this.log(logLevel, message, null, null);
    }

    private void log(LogLevel logLevel, String message, Object arg1, Object arg2) {
        this.log(logLevel, message, new Object[]{arg1, arg2});
    }

    private void log(LogLevel logLevel, String message, Object... arguments) {
        this.log(logLevel, message, null, arguments);
    }

    private void log(LogLevel logLevel, String message, Throwable throwable, Object... arguments) {
        this.execute(logLevel, message, throwable, arguments);
        if (VerifyUtils.notNull(this.transmitLogger)) {
            this.transmitLogger.execute(logLevel, message, throwable, arguments);
        }
    }

    private void execute(LogLevel logLevel, String message, Throwable throwable, Object... arguments) {
        this.updateLock.readLock().lock();
        try {
            if (!this.metadata.isLevelEnabled(logLevel)) {
                return;
            }
            LogContext logContext = new LogContext(this.metadata.deepCopy(!this.formatChain.hasProcessor()),
                    logLevel, message, arguments);
            if (this.formatChain.format(logContext)) {
                this.printChain.print(logContext);
            }
        } catch (IOException | ClassNotFoundException e) {
            Util.report("深度拷贝LogContext对象失败", e);
        } finally {
            this.updateLock.readLock().unlock();
        }
    }

    /**
     * 更新logger
     *
     * @param metadata
     * @param formatChain
     * @param printChain
     * @throws RavenException
     */
    public void refresh(Metadata metadata, FormatChain formatChain, PrintChain printChain) throws RavenException {
        if (VerifyUtils.isNull(metadata)) {
            throw new RavenException("更新RavenLogger失败,metadata不能为空");
        }
        if (VerifyUtils.isNull(formatChain)) {
            throw new RavenException("更新RavenLogger失败,formatChain不能为空");
        }
        if (VerifyUtils.isNull(printChain)) {
            throw new RavenException("更新RavenLogger失败,printChain不能为空");
        }

        this.metadata = metadata;
        this.formatChain = formatChain;
        this.printChain = printChain;
    }

    @Override
    public String getName() {
        return this.metadata.getLogName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.metadata.isLevelEnabled(LogLevel.TRACE);
    }

    @Override
    public void trace(String s) {
        this.log(LogLevel.TRACE, s);
    }

    @Override
    public void trace(String s, Object o) {
        this.log(LogLevel.TRACE, s, o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        this.log(LogLevel.TRACE, s, o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        this.log(LogLevel.TRACE, s, objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        this.log(LogLevel.TRACE, s, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.isTraceEnabled();
    }

    @Override
    public void trace(Marker marker, String s) {
        this.trace(s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        this.trace(s, o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        this.trace(s, o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        this.trace(s, objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        this.trace(s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.metadata.isLevelEnabled(LogLevel.DEBUG);
    }

    @Override
    public void debug(String s) {
        this.log(LogLevel.DEBUG, s);
    }

    @Override
    public void debug(String s, Object o) {
        this.log(LogLevel.DEBUG, s, o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        this.log(LogLevel.DEBUG, s, o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        this.log(LogLevel.DEBUG, s, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        this.log(LogLevel.DEBUG, s, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String s) {
        this.debug(s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        this.debug(s, o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        this.debug(s, o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        this.debug(s, objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        this.debug(s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.metadata.isLevelEnabled(LogLevel.INFO);
    }

    @Override
    public void info(String s) {
        this.log(LogLevel.INFO, s);
    }

    @Override
    public void info(String s, Object o) {
        this.log(LogLevel.INFO, s, o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        this.log(LogLevel.INFO, s, o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        this.log(LogLevel.INFO, s, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        this.log(LogLevel.INFO, s, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String s) {
        this.info(s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        this.info(s, o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        this.info(s, o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        this.info(s, objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        this.info(s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.metadata.isLevelEnabled(LogLevel.WARN);
    }

    @Override
    public void warn(String s) {
        this.log(LogLevel.WARN, s);
    }

    @Override
    public void warn(String s, Object o) {
        this.log(LogLevel.WARN, s, o);
    }

    @Override
    public void warn(String s, Object... objects) {
        this.log(LogLevel.WARN, s, objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        this.log(LogLevel.WARN, s, o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        this.log(LogLevel.WARN, s, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String s) {
        this.warn(s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        this.warn(s, o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        this.warn(s, o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        this.warn(s, objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        this.warn(s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.metadata.isLevelEnabled(LogLevel.ERROR);
    }

    @Override
    public void error(String s) {
        this.log(LogLevel.ERROR, s);
    }

    @Override
    public void error(String s, Object o) {
        this.log(LogLevel.ERROR, s, o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        this.log(LogLevel.ERROR, s, o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        this.log(LogLevel.ERROR, s, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        this.log(LogLevel.ERROR, s, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String s) {
        this.error(s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        this.error(s, o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        this.error(s, o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        this.error(s, objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        this.error(s, throwable);
    }
}
