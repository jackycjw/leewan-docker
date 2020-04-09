package com.leewan.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OperateAuth {

	String[] value() default "QUERY";
}
