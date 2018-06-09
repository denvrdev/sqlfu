package sqlfu;

import javax.annotation.concurrent.ThreadSafe;

/** Main entry point for sqlfu. */
@ThreadSafe
public final class Sqlfu {

  /** Returns an instance of {@link SqliteFileHeaderReader.Factory}. */
  public static SqliteFileHeaderReader.Factory getSqliteFileHeaderReaderFactory() {
    return null;
  }

  private Sqlfu() {}
}
