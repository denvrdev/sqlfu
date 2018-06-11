package sqlfu;

import static com.google.common.truth.Truth.assertThat;

import com.google.guiceberry.GuiceBerryModule;
import com.google.guiceberry.junit4.GuiceBerryRule;
import denvr.testing.InMemoryFileSystemProvider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SqlfuTest {

  @Rule public final GuiceBerryRule guiceBerryRule = new GuiceBerryRule(MyGuiceBerryModule.class);

  @Inject private FileSystem inMemoryFileSystem;

  @Test
  public void newSqliteFileHeaderReader_returnsNonNull() throws IOException {
    Path path = createSampleFile();

    SqliteFileHeaderReader reader = Sqlfu.newSqliteFileHeaderReader(path);

    assertThat(reader).isNotNull();
  }

  @Test
  public void newSqliteFileHeaderReader_returnsNewObject() throws IOException {
    Path path = createSampleFile();

    SqliteFileHeaderReader reader1 = Sqlfu.newSqliteFileHeaderReader(path);
    SqliteFileHeaderReader reader2 = Sqlfu.newSqliteFileHeaderReader(path);

    assertThat(reader1).isNotSameAs(reader2);
  }

  private Path createSampleFile() {
    Path path = inMemoryFileSystem.getPath("SampleFile.sqlite");
    createEmptyFile(path);
    return path;
  }

  private static void createEmptyFile(Path path) {
    try {
      Files.newOutputStream(path).close();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static final class MyGuiceBerryModule extends GuiceBerryModule {

    @Override
    protected void configure() {
      super.configure();
      bind(FileSystem.class).toProvider(InMemoryFileSystemProvider.class);
    }
  }
}
