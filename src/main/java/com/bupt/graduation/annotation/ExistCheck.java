package com.bupt.graduation.annotation;

import java.lang.annotation.*;

/**
 * @author wanz
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExistCheck {
}
