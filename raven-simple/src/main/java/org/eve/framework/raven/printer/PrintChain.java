package org.eve.framework.raven.printer;

import org.eve.framework.raven.definition.LogContext;
import org.eve.framework.raven.util.VerifyUtils;

import java.util.List;

/**
 * 打印调用链
 *
 * @author yc_xia
 * @date 2018/6/22
 */
public class PrintChain {
    private BaseChainPrinter baseChainPrinter;

    public PrintChain(Printer printer, List<AbstractPrintPostProcessor> printPostProcessors) {
        if (VerifyUtils.notEmpty(printPostProcessors)) {
            for (PrintPostProcessor printPostProcessor : printPostProcessors) {
                if (VerifyUtils.isNull(printPostProcessor)) {
                    continue;
                }
                if (VerifyUtils.isNull(this.baseChainPrinter)) {
                    this.baseChainPrinter = (BaseChainPrinter) printPostProcessor;
                } else {
                    this.baseChainPrinter.setNextChainPrinter((BaseChainPrinter) printPostProcessor);
                }
            }
        }
        if (VerifyUtils.isNull(this.baseChainPrinter)) {
            this.baseChainPrinter = (BaseChainPrinter) printer;
        } else {
            this.baseChainPrinter.setNextChainPrinter((BaseChainPrinter) printer);
        }
    }

    public void print(LogContext logContext) {
        if (VerifyUtils.isNull(this.baseChainPrinter)) {
            return;
        }
        this.baseChainPrinter.doChainPrint(logContext);
    }
}
