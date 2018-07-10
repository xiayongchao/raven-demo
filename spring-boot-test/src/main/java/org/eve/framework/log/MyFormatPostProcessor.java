package org.eve.framework.log;

import org.eve.framework.raven.definition.LogContext;
import org.eve.framework.raven.formatter.AbstractFormatPostProcessor;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
public class MyFormatPostProcessor extends AbstractFormatPostProcessor {
    /**
     * 格式化前置处理
     *
     * @param logContext
     * @return
     */
    @Override
    public boolean postProcessBeforeFormat(LogContext logContext) {
        logContext.getMetadata().setShowAppId(false);
        return true;
    }
}
