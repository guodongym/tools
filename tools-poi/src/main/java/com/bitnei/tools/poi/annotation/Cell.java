package com.bitnei.tools.poi.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Cell {

	String name();

	String defaultValue() default "";

	int order() default 0;

	String format() default "yyyy-MM-dd HH:mm:ss";
}
