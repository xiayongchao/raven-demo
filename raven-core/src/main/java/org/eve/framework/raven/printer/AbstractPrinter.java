package org.eve.framework.raven.printer;

import org.eve.framework.raven.bean.ScanBean;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
@ScanBean
public abstract class AbstractPrinter extends BaseChainPrinter implements Printer {
    @Override
    boolean doPrint(String message, Throwable throwable) {
        this.print(message, throwable);
        return true;
    }
}
