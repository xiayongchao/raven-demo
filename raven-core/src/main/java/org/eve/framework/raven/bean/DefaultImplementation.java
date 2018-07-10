package org.eve.framework.raven.bean;

import java.lang.annotation.*;

/**
 * 用于标志默认实现，同一个接口或者父类的多个实现或者子类中只能有一个使用此注解
 *
 * @author yc_xia
 * @date 2018/6/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultImplementation {
}
