package org.eve.framework.raven.printer;

/**
 * 打印前置处理器
 *
 * @author yc_xia
 * @date 2018/6/1
 */
interface PrintPostProcessor {
    /**
     * 打印前置处理
     *
     * @param message
     * @param throwable
     * @return
     */
    boolean postProcessBeforePrint(String message, Throwable throwable);
}
