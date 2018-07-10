package org.eve.framework.raven.printer;

import org.eve.framework.raven.definition.LogContext;
import org.eve.framework.raven.definition.LogContextAware;
import org.eve.framework.raven.util.VerifyUtils;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
abstract class BaseChainPrinter {
    private BaseChainPrinter nextChainPrinter;

    void setNextChainPrinter(BaseChainPrinter nextChainPrinter) {
        this.nextChainPrinter = nextChainPrinter;
    }

    boolean doChainPrint(LogContext logContext) {
        if (this instanceof LogContextAware) {
            ((LogContextAware) this).setLogContext(logContext);
        }
        if (!this.doPrint(logContext.getFormattedMsg(), logContext.getThrowable())) {
            return false;
        }
        if (VerifyUtils.notNull(this.nextChainPrinter)) {
            return this.nextChainPrinter.doChainPrint(logContext);
        }
        return true;
    }

    abstract boolean doPrint(String message, Throwable throwable);
}
