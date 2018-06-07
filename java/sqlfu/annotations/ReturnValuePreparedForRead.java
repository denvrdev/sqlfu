package sqlfu.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;

/**
 * The method to which this annotation applies returns an object that is prepared for being read.
 *
 * <p>A method annotated with this annotation must have the purpose of "reading" data and returning
 * the data that was read in some sort of "buffer"-like object that can be used for both reading and
 * writing data. If such a method is annotated with this annotation then the caller can assume that
 * the returned object is prepared for or "configured" such that the requested data, and only the
 * requested data, will be returned when it is retrieved from the object.
 *
 * <p>For example, a "read" method may return a {@link ByteBuffer} that contains the data that was
 * read. In this case, the {@link ByteBuffer#position()} would be set to the first byte that was
 * read and the {@link ByteBuffer#limit()} would be set after the last byte. The caller then could
 * immediately invoke {@link ByteBuffer#get()} to retrieve the first byte that was read.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ReturnValuePreparedForRead {}
