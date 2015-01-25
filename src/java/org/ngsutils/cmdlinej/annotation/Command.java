package org.ngsutils.cmdlinej.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Command {
	String name();

	String desc() default "";
	String category() default "";
    
    boolean experimental() default false;
    boolean deprecated() default false;
	boolean hidden() default false;
}
