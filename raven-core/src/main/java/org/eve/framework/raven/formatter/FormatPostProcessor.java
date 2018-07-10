package org.eve.framework.raven.formatter;

import org.eve.framework.raven.definition.LogContext;

/**
 * 格式化前置处理器
 *
 * @author yc_xia
 * @date 2018/6/1
 */
interface FormatPostProcessor {
    /**
     * 格式化前置处理
     *
     * @param logContext
     * @return
     */
    boolean postProcessBeforeFormat(LogContext logContext);
}
