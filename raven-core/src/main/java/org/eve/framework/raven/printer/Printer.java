package org.eve.framework.raven.printer;

/**
 * @author yc_xia
 * @date 2018/6/21
 */
interface Printer {
    void print(String message, Throwable throwable);
}
