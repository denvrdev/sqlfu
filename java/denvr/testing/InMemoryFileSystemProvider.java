package denvr.testing;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.nio.file.FileSystem;
import javax.inject.Provider;

/** A provider that provides {@link FileSystem} instances for an in-memory file system. */
public final class InMemoryFileSystemProvider implements Provider<FileSystem> {

  @Override
  public FileSystem get() {
    return Jimfs.newFileSystem(Configuration.unix());
  }
}
