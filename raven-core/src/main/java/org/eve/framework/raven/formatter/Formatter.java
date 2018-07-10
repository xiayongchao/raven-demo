package org.eve.framework.raven.formatter;

import org.eve.framework.raven.definition.LogContext;

/**
 * @author yc_xia
 * @date 2018/6/7
 */
interface Formatter {
    boolean format(LogContext logContext);
}
