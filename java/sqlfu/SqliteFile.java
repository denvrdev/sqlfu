package sqlfu;

import java.nio.channels.FileChannel;

public class SqliteFile {

  private final FileChannel f;

  public SqliteFile(FileChannel fileChannel) {
    this.f = fileChannel;
  }
}
