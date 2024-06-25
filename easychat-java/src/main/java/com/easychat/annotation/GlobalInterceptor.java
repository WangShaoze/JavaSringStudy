package com.easychat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GlobalInterceptor
 * 全局拦截器注解
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {

    // 校验登录
    boolean checkLogin() default true;

    // 校验管理员
    boolean checkAdmin() default false;

}
