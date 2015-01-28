package org.ngsutils.cmdlinej.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Option {
	String desc() default "";

	String charName() default "";
	String name() default "";

	String defaultValue() default "";
	boolean required() default false;
	
	boolean hide() default false;
	boolean showHelp() default false;
}
