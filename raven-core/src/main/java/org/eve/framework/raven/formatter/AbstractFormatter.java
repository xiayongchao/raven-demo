package org.eve.framework.raven.formatter;

import org.eve.framework.raven.bean.ScanBean;
import org.eve.framework.raven.definition.LogContext;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
@ScanBean
public abstract class AbstractFormatter extends BaseChainFormatter implements Formatter {
    @Override
    boolean doFormat(LogContext logContext) {
        return this.format(logContext);
    }
}
