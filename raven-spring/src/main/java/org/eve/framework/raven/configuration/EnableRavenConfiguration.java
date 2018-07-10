package org.eve.framework.raven.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RavenConfiguration.class})
public @interface EnableRavenConfiguration {
    String[] basePackages() default {};

    String resourcePattern() default "**/*.class";
}
