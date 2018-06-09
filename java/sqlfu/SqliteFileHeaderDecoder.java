package sqlfu.api;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import denvr.annotations.ReturnsNewObject;

/** Decodes the raw bytes of an SQLite file header into friendly objects. */
@NotThreadSafe
public interface SqliteFileHeaderDecoder {

  /** Returns the "magic header string". */
  String getMagicHeaderString();

  /** Creates instances of {@link SqliteFileHeaderDecoder}. */
  @ThreadSafe
  interface Factory {

    /**
     * Creates and returns a new {@link SqliteFileHeaderDecoder} that gets the header bytes from the
     * given parser.
     */
    @ReturnsNewObject
    SqliteFileHeaderDecoder create(SqliteFileHeaderParser parser);
  }
}
