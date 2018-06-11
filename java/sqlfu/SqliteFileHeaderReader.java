package sqlfu;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.annotation.concurrent.NotThreadSafe;

/** Reads the SQLite file header bytes from a file. */
@NotThreadSafe
public interface SqliteFileHeaderReader extends AutoCloseable {

  /**
   * Reads the bytes of the file header of an Sqlite database file.
   *
   * <p>The bytes of the header are read from the underlying file and written into the given {@link
   * ByteBuffer} starting at {@link ByteBuffer#position()} up to {@link ByteBuffer#limit()}.
   *
   * <p>When this method returns, {@link ByteBuffer#position()} will be set to the first byte of the
   * header and {@link ByteBuffer#limit()} will be set to after the last byte. If this method throws
   * an exception then the position and limit values are undefined.
   *
   * <p>The {@link ByteBuffer#mark()} will not be affected in any way by this method's invocation.
   *
   * <p>If this amount of space (i.e. {@link ByteBuffer#remaining()}) is insufficient to store the
   * entire header then only the bytes that fit will be written. Since this method returns the
   * number of bytes remaining in the header that did not fit, this value can be used to determine
   * the required minimum size of a {@link ByteBuffer} that can be specified to a subsequent
   * invocation of this method to read the entire header.
   *
   * @param byteBuffer the buffer into which to write the bytes of the Sqlite file header.
   * @return {@code 0} (zero) if all bytes of the Sqlite file header were successfully written into
   *     the given {@link ByteBuffer}; otherwise, returns the number of bytes remaining in the
   *     header that were not able to be written into the {@link ByteBuffer} due to insufficient
   *     {@link ByteBuffer#remaining()}.
   * @throws IOException if reading from the underlying file throws it.
   * @throws EOFException if the underlying file has too few bytes to contain a complete Sqlite file
   *     header.
   */
  int read(ByteBuffer byteBuffer) throws IOException, EOFException;

  /**
   * Close any underlying connections to files and release the resources.
   *
   * @throws IOException if thrown when closing the underlying file.
   */
  @Override
  void close() throws IOException;
}
