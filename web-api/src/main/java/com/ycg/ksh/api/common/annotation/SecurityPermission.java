package com.ycg.ksh.api.common.annotation;

import java.lang.annotation.*;

/**
 * 权限注解
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/23
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityPermission {

    String code() default "";//权限CODE

}
