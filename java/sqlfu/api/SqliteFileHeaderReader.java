package sqlfu.api;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import sqlfu.annotations.ReturnValuePreparedForRead;
import sqlfu.annotations.ReturnsNewObject;
import sqlfu.annotations.TransientReturnValue;

/** Reads the SQLite file header bytes from a file. */
@NotThreadSafe
public interface SqliteFileHeaderReader {

  /** The size of the SQLite file header, in bytes. */
  int SQLITE_FILE_HEADER_NUM_BYTES = 100;

  /**
   * Reads the SQLite header bytes from the encapsulated file and returns them.
   *
   * <p>The position of the encapsulated file may be changed by invoking this method. For example,
   * the position could be set to the beginning of the header bytes prior to reading them and then
   * left at the end of the header bytes after reading them; however, since an implementation is
   * free to implement this functionality however it wants this may not actually occur (e.g. if the
   * header bytes are cached and returned from memeory in a future invocation).
   *
   * <p>A reference to the returned {@link ByteBuffer} may be stored internally and re-used during a
   * future invocation of a method on this object; therefore, the caller must not use the returned
   * {@link ByteBuffer} after any future invocation of a method on this object.
   *
   * @return a {@link ByteBuffer} containing the SQLite header bytes read from the encapsulated
   *     file; the position will be set to the first byte of the header and the limit will be set
   *     after the last byte.
   * @throws IOException if reading from the encapsulated file throws it.
   * @throws EOFException if the number of SQLite header bytes available in the encapsulated file is
   *     less than {@link #SQLITE_FILE_HEADER_NUM_BYTES}.
   */
  @ReturnValuePreparedForRead
  @TransientReturnValue
  ByteBuffer read() throws IOException;

  /** Creates instances of {@link SqliteFileHeaderReader}. */
  @ThreadSafe
  interface Factory {

    /**
     * Creates and returns a new {@link SqliteFileHeaderReader} that reads the bytes from the given
     * channel. It assumes that the header bytes are at the standard byte offset of a typical SQLite
     * database.
     */
    @ReturnsNewObject
    SqliteFileHeaderReader create(SeekableByteChannel channel);

    /**
     * Creates and returns a new {@link SqliteFileHeaderReader} that reads the bytes from the given
     * buffer. It assumes that the header bytes are at the standard byte offset of a typical SQLite
     * database. Only bytes between the position and limit are read, treating the position at the
     * time when this method was invoked as the first byte of the file.
     */
    @ReturnsNewObject
    SqliteFileHeaderReader create(ByteBuffer byteBuffer);
  }
}
