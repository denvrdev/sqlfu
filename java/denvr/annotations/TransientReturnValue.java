package denvr.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The method to which this annotation applies returns or may return a <em>transient</em> value.
 *
 * <p>A reference to the object that the method returns is (or may be) stored internally in the
 * object on which the method is invoked. Therefore, the caller <em>must</em> never use the returned
 * object after invoking any other method on the object from which the object was obtained.
 *
 * <p>For example, a "read" method in a "reader" class may return a byte array containing the bytes
 * read. If the method is annotated with this annotation then a subsequent "read" method invocation
 * may store the newly-read bytes in the <em>same</em> array and return the <em>same</em> array
 * again. This may be done as an optimization to avoid creating a new byte array for every "read"
 * method invocation. The caller must be aware of this fact because the bytes in the returned array
 * may change unexpectedly when another method is invoked on the object from which it was returned.
 * Instead, the caller should copy the data from the byte array to another byte array and release
 * the reference to the returned byte array before invoking any other methods on the object.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface TransientReturnValue {}
