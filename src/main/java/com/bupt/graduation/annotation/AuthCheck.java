package com.bupt.graduation.annotation;

import java.lang.annotation.*;

/**
 * 用来对用户鉴权的注解，当前业务暂未使用
 *
 * @author wanz
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AuthCheck {
    //所操作的业务对应的权限ID
    String[] role();
}