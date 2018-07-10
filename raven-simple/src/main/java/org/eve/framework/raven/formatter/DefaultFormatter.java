package org.eve.framework.raven.formatter;

import org.eve.framework.raven.bean.DefaultImplementation;
import org.eve.framework.raven.definition.LogContext;
import org.eve.framework.raven.definition.Metadata;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;

/**
 * @author yc_xia
 * @date 2018/6/22
 */
@DefaultImplementation
public class DefaultFormatter extends AbstractFormatter {
    @Override
    public boolean format(LogContext logContext) {
        String message = logContext.getOriginalMsg();
        Throwable throwable = null;
        if (logContext.getArguments() != null) {
            FormattingTuple tp;
            if (logContext.getArguments().length > 2) {
                tp = MessageFormatter.arrayFormat(message, logContext.getArguments());
            } else {
                tp = MessageFormatter.format(message, logContext.getArguments().length >= 1 ? logContext.getArguments()[0] : null,
                        logContext.getArguments().length >= 2 ? logContext.getArguments()[1] : null);
            }
            message = tp.getMessage();
            throwable = tp.getThrowable();
        }

        StringBuilder buf = new StringBuilder(32);

        Metadata metadata = logContext.getMetadata();
        // Append date-time if so configured
        if (metadata.getShowDateTime()) {
            if (metadata.getDateTimeFormatter() != null) {

                Date now = new Date();
                String dateText;
                synchronized (metadata.getDateTimeFormatter()) {
                    dateText = metadata.getDateTimeFormatter().format(now);
                }

                buf.append(dateText);
                buf.append(' ');
            } else {
                buf.append(System.currentTimeMillis() - metadata.getStartTime());
                buf.append(' ');
            }
        }

        if (metadata.getShowAppId()) {
            buf.append('<');
            buf.append(metadata.getAppId());
            buf.append("> ");
        }

        if (metadata.getShowAppName()) {
            buf.append('<');
            buf.append(metadata.getAppName());
            buf.append("> ");
        }

        if (metadata.getShowLoggerId()) {
            buf.append('<');
            buf.append(metadata.getLoggerId());
            buf.append("> | ");
        }

        // Append current thread name if so configured
        if (metadata.getShowThreadName()) {
            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
        }

        if (metadata.getLevelInBrackets()) {
            buf.append('[');
        }

        // Append a readable representation of the log level
        String levelStr = logContext.renderLevel();
        buf.append(levelStr);
        if (metadata.getLevelInBrackets()) {
            buf.append(']');
        }
        buf.append(' ');

        // Append the name of the log instance if so configured
        if (metadata.getShowShortLogName()) {
            if (metadata.getShortLogName() == null) {
                metadata.setShortLogName(logContext.computeShortName());
            }
            buf.append(String.valueOf(metadata.getShortLogName())).append(" - ");
        } else if (metadata.getShowLogName()) {
            buf.append(String.valueOf(metadata.getLogName())).append(" - ");
        }

        // Append the message
        buf.append(message);
        logContext.setResult(buf.toString(), throwable);
        return true;
    }
}
