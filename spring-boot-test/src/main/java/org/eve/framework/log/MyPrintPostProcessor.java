package org.eve.framework.log;

import org.eve.framework.raven.annotation.RavenBean;
import org.eve.framework.raven.printer.AbstractPrintPostProcessor;
import org.eve.framework.service.TestService;

import javax.annotation.Resource;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
@RavenBean("hgfsdgd")
public class MyPrintPostProcessor extends AbstractPrintPostProcessor {
    @Resource
    private TestService testService;

    /**
     * 打印前置处理
     *
     * @param message
     * @param throwable
     * @return
     */
    @Override
    public boolean postProcessBeforePrint(String message, Throwable throwable) {
        this.testService.print("我是spring容器管理的MyPrintPostProcessor，我被调用了");
        return true;
    }
}
