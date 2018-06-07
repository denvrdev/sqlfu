package sqlfu.api;

import java.nio.ByteBuffer;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import sqlfu.annotations.ReturnValuePreparedForRead;
import sqlfu.annotations.ReturnsNewObject;
import sqlfu.annotations.TransientReturnValue;

/** Splits the bytes of an SQLite file header into its component parts. */
@NotThreadSafe
public interface SqliteFileHeaderParser {

  /**
   * Returns the bytes of the "magic header string".
   *
   * @return a {@link ByteBuffer} that contains the bytes of the "magic header string".
   */
  @ReturnValuePreparedForRead
  @TransientReturnValue
  ByteBuffer getMagicHeaderString();

  /** Creates instances of {@link SqliteFileHeaderParser}. */
  @ThreadSafe
  interface Factory {

    /**
     * Creates and returns a new {@link SqliteFileHeaderParser} that gets the header bytes from the
     * given reader.
     */
    @ReturnsNewObject
    SqliteFileHeaderParser create(SqliteFileHeaderReader reader);
  }
}
