package sqlfu;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

/** Parses data from the bytes of the header of an Sqlite database. */
public final class SqliteFileHeaderParser {

  private static final int MAGIC_HEADER_STRING_OFFSET = 0;
  private static final int MAGIC_HEADER_STRING_LENGTH = 16;

  private final ByteBuffer byteBuffer;
  private final int startPosition;

  /**
   * Creates a new {@link SqliteFileHeaderParser} that parses the Sqlite header from the given
   * {@link ByteBuffer}.
   *
   * <p>The {@link ByteBuffer#remaining()} method of the given {@link ByteBuffer} should return at
   * least {@link SqliteFile#HEADER_NUM_BYTES}; otherwise, methods in this class may throw
   * undocumented exceptions.
   *
   * <p>A reference to the given {@link ByteBuffer} will be stored internally and re-used often;
   * therefore, it is important to ensure that its contents never change throughout the lifetime of
   * this object.
   */
  public SqliteFileHeaderParser(final ByteBuffer byteBuffer) {
    this.byteBuffer = checkNotNull(byteBuffer, "byteBuffer==null");
    this.startPosition = byteBuffer.position();
  }

  /**
   * Returns a {@link ByteBuffer} containing the "magic header string" from the encapsulated {@link
   * ByteBuffer}.
   *
   * <p>Note that the returned {@link ByteBuffer} may be an internally-cached object that is re-used
   * for subsequent method invocations on this object; therefore, the caller must never use the
   * returned object after invoking any other method on this object.
   *
   * @return a {@link ByteBuffer} where the bytes starting at the position and up to the limit are
   *     the bytes of the magic header string from the encapsulated {@link ByteBuffer}.
   * @see #getMagicHeaderString
   */
  public ByteBuffer getMagicHeaderStringBytes() {
    return configureByteBufferForSliceRead(MAGIC_HEADER_STRING_OFFSET, MAGIC_HEADER_STRING_LENGTH);
  }

  /**
   * Gets the "magic header string" from the encapsulated {@link ByteBuffer}, decoded as a UTF-8
   * string.
   *
   * <p>Any characters from the "magic header string" bytes that cannot be successfully decoded are
   * replaced with placeholder values.
   *
   * @return a string whose value is the bytes returned from {@link #getMagicHeaderStringBytes()}
   *     decoded as a UTF-8 string.
   * @see #getMagicHeaderStringBytes()
   */
  public String getMagicHeaderString() {
    final ByteBuffer magicHeaderStringByteBuffer = getMagicHeaderStringBytes();

    CharsetDecoder decoder = UTF_8.newDecoder();
    decoder.onMalformedInput(CodingErrorAction.REPLACE);
    decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
    decoder.replaceWith("_");

    try {
      return decoder.decode(magicHeaderStringByteBuffer).toString();
    } catch (CharacterCodingException e) {
      throw new AssertionError(e); // impossible, since CodingErrorAction.REPLACE was set on decoder
    }
  }

  private ByteBuffer configureByteBufferForSliceRead(int offset, int length) {
    byteBuffer.position(startPosition + offset);
    byteBuffer.limit(startPosition + offset + length);
    return byteBuffer;
  }

  public static final class ParseException extends Exception {

    public ParseException(final String message) {
      super(message);
    }
  }
}
