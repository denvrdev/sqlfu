package sqlfu;

import denvr.annotations.ReturnValuePreparedForRead;
import denvr.annotations.ReturnsNewObject;
import denvr.annotations.TransientReturnValue;
import java.nio.ByteBuffer;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

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
