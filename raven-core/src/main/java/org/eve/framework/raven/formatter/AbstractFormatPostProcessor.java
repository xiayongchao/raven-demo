package org.eve.framework.raven.formatter;

import org.eve.framework.raven.bean.ScanBean;
import org.eve.framework.raven.definition.LogContext;

/**
 *
 * @author yc_xia
 * @date 2018/7/3
 */
@ScanBean
public abstract class AbstractFormatPostProcessor extends BaseChainFormatter implements FormatPostProcessor {
    @Override
    boolean doFormat(LogContext logContext) {
        return this.postProcessBeforeFormat(logContext);
    }
}
