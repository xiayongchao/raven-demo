package org.eve.framework.raven.bean;

import java.lang.annotation.*;

/**
 * 用于标记需要扫描的接口或者父类
 *
 * @author yc_xia
 * @date 2018/6/21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScanBean {
}
