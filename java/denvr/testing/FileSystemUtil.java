package denvr.testing;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;

/** Utility methods for unit tests. */
public final class FileSystemUtil {

  private final FileSystem fileSystem;

  @Inject
  FileSystemUtil(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  public Path createFile() {
    try {
      return Files.createTempFile("FileSystemUtil.", ".dat");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public Path getNonExistentPath() {
    Path path = createFile();
    deleteFile(path);
    return path;
  }

  public void deleteFile(Path path) {
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
