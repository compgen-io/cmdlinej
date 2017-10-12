package io.compgen.cmdline.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Call this method for all unannotated/unknown arguments
 * This allows you to process arguments that may not be known at compile-time.
 * 
 * @author mbreese
 *
 */

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UnknownArgs {
}
