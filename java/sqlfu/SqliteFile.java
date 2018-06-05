package sqlfu;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SeekableByteChannel;

/**
 * Wraps a channel and provides read/write access to the SQLite database metadata that are stored in
 * the file referenced by the given channel.
 *
 * <p>Some methods of this class perform exclusively <em>read</em> operations on the channel and are
 * therefore safe to invoke when the given {@link ByteChannel} was opened in read-only mode. Each
 * method documents explicitly if it performs only read operations or contains at least one write
 * operation.
 *
 * <p>This class is <em>not</em> thread-safe; the behaviour of all methods in this class if invoked
 * concurrently from multiple threads is undefined.
 */
public final class SqliteFile {

  /** The number of bytes in the SQLite file header. */
  public static final int HEADER_NUM_BYTES = 100;

  private final SeekableByteChannel channel;
  private ByteBuffer buffer;

  public SqliteFile(final SeekableByteChannel channel) {
    this.channel = checkNotNull(channel, "channel==null");
  }

  /**
   * Reads the bytes of the SQLite header.
   *
   * <p>This method performs only <em>read</em> operations on the file; therefore, it is safe to
   * invoked this method when the encapsulated {@link ByteChannel} is read-only.
   *
   * <p>Note that the returned buffer is used internally by this class when various methods are
   * invoked; therefore, it <em>must</em> not be used after invoking any subsequent methods on this
   * object.
   *
   * @return a (potentially) shared buffer containing the header bytes; the position will be at the
   *     first byte of the header and the limit will be set to the end of the header bytes.
   * @throws IOException if throws when reading from the encapsulated file.
   * @throws EOFException if the file's contents are too small to contain even an SQLite header.
   */
  public ByteBuffer readHeader() throws IOException, EOFException {
    channel.position(0);
    return readExact(HEADER_NUM_BYTES);
  }

  private ByteBuffer readExact(int size) throws IOException, EOFException {
    checkArgument(size >= 0, "size==%s", size);

    ByteBuffer buffer = getOrCreateBuffer(size);

    int numBytesRead = 0;
    while (numBytesRead < size) {
      final int curNumBytesRead = channel.read(buffer);
      if (curNumBytesRead < 0) {
        throw new EOFException(
            "unexpected end of file: found "
                + numBytesRead
                + " bytes but expected at least "
                + size
                + " bytes");
      }
      numBytesRead += curNumBytesRead;
    }

    buffer.flip();
    return buffer;
  }

  private ByteBuffer getOrCreateBuffer(final int minSize) {
    checkArgument(minSize >= 0, "minSize==%s", minSize);
    if (buffer == null || buffer.capacity() < minSize) {
      buffer = ByteBuffer.allocate(minSize);
    }
    buffer.clear();
    return buffer;
  }
}
