package org.eve.framework.raven.formatter;

import org.eve.framework.raven.definition.LogContext;
import org.eve.framework.raven.util.VerifyUtils;

import java.util.List;

/**
 * 格式化调用链
 *
 * @author yc_xia
 * @date 2018/6/22
 */
public class FormatChain {
    private BaseChainFormatter baseChainFormatter;
    private boolean hasProcessor;

    public boolean hasProcessor() {
        return this.hasProcessor;
    }

    public FormatChain(Formatter formatter, List<AbstractFormatPostProcessor> formatPostProcessors) {
        if (VerifyUtils.notEmpty(formatPostProcessors)) {
            for (FormatPostProcessor formatPostProcessor : formatPostProcessors) {
                if (VerifyUtils.isNull(formatPostProcessor)) {
                    continue;
                }
                if (VerifyUtils.isNull(this.baseChainFormatter)) {
                    this.baseChainFormatter = (BaseChainFormatter) formatPostProcessor;
                } else {
                    this.baseChainFormatter.setNextChainPrinter((BaseChainFormatter) formatPostProcessor);
                }
            }
        }
        if (VerifyUtils.isNull(this.baseChainFormatter)) {
            this.baseChainFormatter = (BaseChainFormatter) formatter;
        } else {
            this.hasProcessor = true;
            this.baseChainFormatter.setNextChainPrinter((BaseChainFormatter) formatter);
        }
    }

    public boolean format(LogContext logContext) {
        if (VerifyUtils.isNull(this.baseChainFormatter)) {
            return false;
        }
        return this.baseChainFormatter.doChainFormat(logContext);
    }
}
