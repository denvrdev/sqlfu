package sqlfu;

import static java.nio.file.Files.newByteChannel;
import static java.nio.file.StandardOpenOption.READ;

import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.concurrent.ThreadSafe;

/** Main entry point for sqlfu. */
@ThreadSafe
public final class Sqlfu {

  private Sqlfu() {}

  /**
   * Creates and returns a new {@link SqliteFileHeaderReader} object that read from the given file.
   *
   * @throws IOException if opening the file for reading fails.
   */
  public static SqliteFileHeaderReader newSqliteFileHeaderReader(Path path) throws IOException {
    return new SqliteFileHeaderReaderImpl(newByteChannel(path, READ));
  }
}
