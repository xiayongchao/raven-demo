package org.eve.framework.raven.printer;

import org.eve.framework.raven.bean.DefaultImplementation;

import java.io.PrintStream;

/**
 * 默认日志输出打印器
 *
 * @author yc_xia
 * @date 2018/6/22
 */
@DefaultImplementation
public class DefaultPrinter extends AbstractPrinter {
    private final PrintStream printStream;

    public DefaultPrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void print(String message, Throwable throwable) {
        this.printStream.println(message);
        if (throwable != null) {
            throwable.printStackTrace(this.printStream);
        }
        this.printStream.flush();
    }
}
