package sqlfu;

import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static sqlfu.SqliteFile.HEADER_NUM_BYTES;
import static sqlfu.util.test.AssertThrows.assertThrows;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.google.common.primitives.Bytes;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SqliteFileTest {

  private SqliteFileFactory sqliteFileFactory;

  @Before
  public void initVariables() {
    sqliteFileFactory = new SqliteFileFactory();
  }

  @Test
  public void HEADER_NUM_BYTES_should_be_100() {
    assertThat(HEADER_NUM_BYTES).isEqualTo(100);
  }

  @Test
  public void readHeader_throwsEOFExceptionIfFileIsEmpty() {
    SqliteFile sqliteFile = sqliteFileFactory.forEmptyFile();

    EOFException exception = assertThrows(EOFException.class, sqliteFile::readHeader);

    assertThat(exception)
        .hasMessageThat()
        .isEqualTo("unexpected end of file: found 0 bytes but expected at least 100 bytes");
  }

  @Test
  public void readHeader_throwsEOFExceptionIfFileContains1FewBytes() {
    byte[] bytes = createSampleByteArray(HEADER_NUM_BYTES - 1);
    SqliteFile sqliteFile = sqliteFileFactory.forFileContaining(bytes);

    EOFException exception = assertThrows(EOFException.class, sqliteFile::readHeader);

    assertThat(exception)
        .hasMessageThat()
        .isEqualTo("unexpected end of file: found 99 bytes but expected at least 100 bytes");
  }

  @Test
  public void readHeader_shouldNotThrowExceptionIfFileIsExactly100Bytes() throws IOException {
    byte[] bytes = createSampleByteArray(HEADER_NUM_BYTES);
    SqliteFile sqliteFile = sqliteFileFactory.forFileContaining(bytes);

    sqliteFile.readHeader();
  }

  @Test
  public void readHeader_shouldNotThrowExceptionIfFileIsGreaterThan100Bytes() throws IOException {
    byte[] bytes = createSampleByteArray(HEADER_NUM_BYTES * 2);
    SqliteFile sqliteFile = sqliteFileFactory.forFileContaining(bytes);

    sqliteFile.readHeader();
  }

  @Test
  public void readHeader_shouldReturnAll100BytesIfFileIsExactly100Bytes() throws IOException {
    byte[] bytes = createSampleByteArray(HEADER_NUM_BYTES);
    byte[] expected = Arrays.copyOf(bytes, bytes.length);
    SqliteFile sqliteFile = sqliteFileFactory.forFileContaining(bytes);

    ByteBuffer byteBuffer = sqliteFile.readHeader();

    byte[] actual = new byte[byteBuffer.remaining()];
    byteBuffer.get(actual);
    assertThat(actual).asList().containsExactlyElementsIn(Bytes.asList(expected)).inOrder();
  }

  @Test
  public void readHeader_shouldReturnFirst100BytesIfFileIsGreaterThan100Bytes() throws IOException {
    byte[] bytes = createSampleByteArray(HEADER_NUM_BYTES * 2);
    byte[] expected = Arrays.copyOf(bytes, HEADER_NUM_BYTES);
    SqliteFile sqliteFile = sqliteFileFactory.forFileContaining(bytes);

    ByteBuffer byteBuffer = sqliteFile.readHeader();

    byte[] actual = new byte[byteBuffer.remaining()];
    byteBuffer.get(actual);
    assertThat(actual).asList().containsExactlyElementsIn(Bytes.asList(expected)).inOrder();
  }

  @Test
  public void readHeader_shouldReturnFirst100BytesOnSecondInvocation() throws IOException {
    byte[] bytes = createSampleByteArray(HEADER_NUM_BYTES * 2);
    byte[] expected = Arrays.copyOf(bytes, HEADER_NUM_BYTES);
    SqliteFile sqliteFile = sqliteFileFactory.forFileContaining(bytes);

    sqliteFile.readHeader();
    ByteBuffer byteBuffer = sqliteFile.readHeader();

    assertThat(byteBuffer.remaining()).isEqualTo(HEADER_NUM_BYTES);
    byte[] actual = new byte[byteBuffer.remaining()];
    byteBuffer.get(actual);
    assertThat(actual).asList().containsExactlyElementsIn(Bytes.asList(expected)).inOrder();
  }

  @Test
  public void readHeader_shouldThrowIOExceptionIfFileIsClosed() throws IOException {
    SqliteFile sqliteFile = sqliteFileFactory.forClosedChannel();

    assertThrows(IOException.class, sqliteFile::readHeader);
  }

  private static byte[] createSampleByteArray(final int size) {
    final byte[] seedData = "This is sample data".getBytes(UTF_8);
    assertThat(size % seedData.length).isNotEqualTo(0);

    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(size);
    while (byteArrayOutputStream.size() < size) {
      try {
        byteArrayOutputStream.write(seedData);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    return Arrays.copyOf(byteArrayOutputStream.toByteArray(), size);
  }

  private static final class SqliteFileFactory {

    private final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

    SqliteFile forClosedChannel() {
      Path path = createEmptyFile();
      final SeekableByteChannel channel;
      try {
        channel = Files.newByteChannel(path);
        channel.close();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
      return new SqliteFile(channel);
    }

    SqliteFile forPath(final Path path) {
      final SeekableByteChannel channel;
      try {
        channel = Files.newByteChannel(path);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
      return new SqliteFile(channel);
    }

    SqliteFile forEmptyFile() {
      return forPath(createEmptyFile());
    }

    SqliteFile forFileContaining(final byte[] bytes) {
      Path path = createEmptyFile();
      try (OutputStream outputStream = Files.newOutputStream(path)) {
        outputStream.write(bytes);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
      return forPath(path);
    }

    Path createEmptyFile() {
      Path path = fileSystem.getPath("empty.txt");
      try {
        Files.newOutputStream(path).close();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
      return path;
    }
  }
}
