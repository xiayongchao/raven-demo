package org.eve.framework.raven.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author xiayc
 * @date 2018/7/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RavenBean {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
