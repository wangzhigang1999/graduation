package com.bupt.graduation.annotation;

import java.lang.annotation.*;

/**
 * 检查当前请求上下文的合照是否存在
 *
 * @author wanz
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExistCheck {
}
