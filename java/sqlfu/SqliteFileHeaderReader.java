package sqlfu;

import denvr.annotations.ReturnsNewObject;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

/** Reads the SQLite file header bytes from a file. */
@NotThreadSafe
public interface SqliteFileHeaderReader extends AutoCloseable {

  /**
   * Reads some or all of the SQLite header bytes from the encapsulated file and writes them into
   * the given {@link ByteBuffer}.
   *
   * <p>Each invocation of this method will read a chunk of bytes and write them into the given
   * {@link ByteBuffer}, at most {@link ByteBuffer#remaining()} bytes. When this method returns, the
   * {@link ByteBuffer#position()} will be after the last byte written.
   *
   * <p>If {@link ReadResult#NO_MORE_BYTES_AVAILBLE} is returned then all bytes of the SQLite file
   * header have been read and returned. All subsequent invocations of this method on this object
   * will then write zero bytes into the {@link ByteBuffer} and return {@link
   * ReadResult#NO_MORE_BYTES_AVAILBLE}.
   *
   * <p>If {@link ReadResult#MORE_BYTES_AVAILBLE} is returned then only <em>some</em> of the bytes
   * of the SQLite file header were written into the given {@link ByteBuffer}. A subsequent
   * invocation will be required to get the next chunk of bytes.
   *
   * @throws IOException if reading from the encapsulated file throws it or if {@link #close()} has
   *     been invoked.
   * @throws EOFException if the file is too small to contain a complete SQLite file header.
   */
  ReadResult read(ByteBuffer byteBuffer) throws IOException;

  /**
   * Close any underlying connections to files and release the resources.
   *
   * @throws IOException if thrown when closing the underlying file.
   */
  @Override
  void close() throws IOException;

  /** The result of invoking {@link #read}. */
  enum ReadResult {

    /** There are more bytes available to be read. */
    MORE_BYTES_AVAILBLE,

    /** There are no more bytes available to be read. */
    NO_MORE_BYTES_AVAILBLE,
  }

  /** Creates instances of {@link SqliteFileHeaderReader}. */
  @ThreadSafe
  interface Factory {

    /** Creates and returns a new {@link SqliteFileHeaderReader} that reads from the given file. */
    @ReturnsNewObject
    SqliteFileHeaderReader create(Path path);
  }
}
