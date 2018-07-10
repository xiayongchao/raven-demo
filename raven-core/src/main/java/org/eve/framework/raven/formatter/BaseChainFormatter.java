package org.eve.framework.raven.formatter;

import org.eve.framework.raven.definition.LogContext;
import org.eve.framework.raven.util.VerifyUtils;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
abstract class BaseChainFormatter {
    private BaseChainFormatter nextChainFormatter;

    void setNextChainPrinter(BaseChainFormatter nextChainPrinter) {
        this.nextChainFormatter = nextChainPrinter;
    }

    boolean doChainFormat(LogContext logContext) {
        if (!this.doFormat(logContext)) {
            return false;
        }
        if (VerifyUtils.notNull(this.nextChainFormatter)) {
            return this.nextChainFormatter.doChainFormat(logContext);
        }
        return true;
    }

    abstract boolean doFormat(LogContext logContext);
}
