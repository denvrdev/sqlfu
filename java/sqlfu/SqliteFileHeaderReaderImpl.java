package sqlfu;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

final class SqliteFileHeaderReaderImpl implements SqliteFileHeaderReader {

  private final SeekableByteChannel byteChannel;

  SqliteFileHeaderReaderImpl(final SeekableByteChannel byteChannel) {
    this.byteChannel = checkNotNull(byteChannel, "byteChannel==null");
  }

  @Override
  public void close() throws IOException {
    byteChannel.close();
  }

  @Override
  public ReadResult read(final ByteBuffer byteBuffer) throws IOException {
    throw new UnsupportedOperationException();
  }
}
