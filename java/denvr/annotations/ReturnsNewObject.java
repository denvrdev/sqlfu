package denvr.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The method to which this annotation applies creates and returns a new object.
 *
 * <p>This annotation is typically applied to "factory" methods whose sole responsibility is to
 * create an instance of a class and return that newly-created instance. The method must not retain
 * an instance to the created object.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ReturnsNewObject {}
