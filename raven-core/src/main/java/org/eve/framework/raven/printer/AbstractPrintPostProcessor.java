package org.eve.framework.raven.printer;

import org.eve.framework.raven.bean.ScanBean;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
@ScanBean
public abstract class AbstractPrintPostProcessor extends BaseChainPrinter implements PrintPostProcessor {
    @Override
    boolean doPrint(String message, Throwable throwable) {
        return this.postProcessBeforePrint(message, throwable);
    }
}
