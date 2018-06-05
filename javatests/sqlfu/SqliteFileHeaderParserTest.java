package sqlfu;

import static com.google.common.primitives.Bytes.asList;
import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static sqlfu.SqliteFile.HEADER_NUM_BYTES;
import static sqlfu.util.test.AssertThrows.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.MalformedInputException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SqliteFileHeaderParserTest {

  private ByteBuffer byteBuffer;

  @Before
  public void initializeInstanceVariables() {
    byteBuffer = ByteBuffer.allocate(HEADER_NUM_BYTES);
  }

  @Test
  public void constructor_throwsNullPointerExceptionOnNullArguments() {
    assertThrows(NullPointerException.class, () -> new SqliteFileHeaderParser(null));
  }

  @Test
  public void getMagicHeaderStringBytes_returnsCorrectBytesIfPositionIsZero() {
    final byte[] expected = new byte[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};
    byteBuffer.put(expected).put(new byte[] {9, 9, 9, 9, 9});
    byteBuffer.position(0).limit(byteBuffer.capacity());
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    final ByteBuffer actualByteBuffer = parser.getMagicHeaderStringBytes();

    final byte[] actualBytes = new byte[actualByteBuffer.remaining()];
    actualByteBuffer.get(actualBytes);
    assertThat(asList(actualBytes)).containsExactlyElementsIn(asList(expected)).inOrder();
  }

  @Test
  public void getMagicHeaderStringBytes_returnsCorrectBytesIfPositionIsNonZero() {
    final byte[] expected = new byte[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};
    final byte[] filler = new byte[] {9, 9, 9, 9};
    byteBuffer.put(filler).put(expected).put(filler);
    byteBuffer.position(filler.length).limit(byteBuffer.capacity());
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    final ByteBuffer actualByteBuffer = parser.getMagicHeaderStringBytes();

    final byte[] actualBytes = new byte[actualByteBuffer.remaining()];
    actualByteBuffer.get(actualBytes);
    assertThat(asList(actualBytes)).containsExactlyElementsIn(asList(expected)).inOrder();
  }

  @Test
  public void getMagicHeaderStringBytes_returnsCorrectBytesInSubsequentInvocation() {
    final byte[] expected = new byte[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};
    byteBuffer.put(expected);
    byteBuffer.position(0).limit(byteBuffer.capacity());
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    parser.getMagicHeaderStringBytes();
    final ByteBuffer actualByteBuffer = parser.getMagicHeaderStringBytes();

    final byte[] actualBytes = new byte[actualByteBuffer.remaining()];
    actualByteBuffer.get(actualBytes);
    assertThat(asList(actualBytes)).containsExactlyElementsIn(asList(expected)).inOrder();
  }

  @Test
  public void getMagicHeaderStringBytes_returnsExpectedHeaderFromSampleDb() {
    byteBuffer = loadSampleSqliteDb();
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    final ByteBuffer actualByteBuffer = parser.getMagicHeaderStringBytes();

    final byte[] actualBytes = new byte[actualByteBuffer.remaining()];
    actualByteBuffer.get(actualBytes);
    final byte[] expected = "SQLite format 3\u0000".getBytes(UTF_8);
    assertThat(asList(actualBytes)).containsExactlyElementsIn(asList(expected)).inOrder();
  }

  @Test
  public void getMagicHeaderString_returnsCorrectStringIfPositionIsZero() {
    final String expected = "SQLite format 3\u0000";
    byteBuffer.put(expected.getBytes(UTF_8)).put(new byte[] {9, 9, 9, 9, 9});
    byteBuffer.position(0).limit(byteBuffer.capacity());
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    final String actual = parser.getMagicHeaderString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void getMagicHeaderString_returnsCorrectStringIfPositionIsNonZero() {
    final String expected = "SQLite format 3\u0000";
    final byte[] filler = new byte[] {9, 9, 9, 9};
    byteBuffer.put(filler).put(expected.getBytes(UTF_8)).put(filler);
    byteBuffer.position(filler.length).limit(byteBuffer.capacity());
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    final String actual = parser.getMagicHeaderString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void getMagicHeaderString_returnsCorrectStringInSubsequentInvocation() {
    final String expected = "SQLite format 3\u0000";
    byteBuffer.put(expected.getBytes(UTF_8));
    byteBuffer.position(0).limit(byteBuffer.capacity());
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    parser.getMagicHeaderString();
    final String actual = parser.getMagicHeaderString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void getMagicHeaderString_returnsStringWithMalformedUtf8ByteSequencesReplaced() {
    final byte[] bytes = "aaaaaaaaaaaaaaaazzz".getBytes(UTF_8);
    bytes[2] = (byte) 0xFF;
    assertContainsMalformedUtf8Input(bytes); // sanity check
    byteBuffer.put(bytes);
    byteBuffer.position(0).limit(byteBuffer.capacity());
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    final String actual = parser.getMagicHeaderString();

    assertThat(actual).isEqualTo("aa_aaaaaaaaaaaaa");
  }

  @Test
  public void getMagicHeaderString_returnsExpectedHeaderFromSampleDb() {
    byteBuffer = loadSampleSqliteDb();
    SqliteFileHeaderParser parser = new SqliteFileHeaderParser(byteBuffer);

    final String actual = parser.getMagicHeaderString();

    assertThat(actual).isEqualTo("SQLite format 3\u0000");
  }

  private static void assertContainsMalformedUtf8Input(final byte[] bytes) {
    final CharsetDecoder decoder = UTF_8.newDecoder();
    decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
    decoder.onMalformedInput(CodingErrorAction.REPORT);
    final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    assertThrows(MalformedInputException.class, () -> decoder.decode(byteBuffer));
  }

  private ByteBuffer loadSampleSqliteDb() {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    try (final InputStream inputStream = getClass().getResourceAsStream("test.sqlite")) {
      while (true) {
        int value = inputStream.read();
        if (value < 0) {
          break;
        }
        byteArrayOutputStream.write(value);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
  }
}
