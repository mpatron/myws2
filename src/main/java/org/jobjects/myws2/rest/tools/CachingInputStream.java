package org.jobjects.myws2.rest.tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachingInputStream extends BufferedInputStream {
  public CachingInputStream(InputStream source) {
    super(new PostCloseProtection(source));
    super.mark(Integer.MAX_VALUE);
  }

  @Override
  public synchronized void close() throws IOException {
    if (!((PostCloseProtection) in).decoratedClosed) {
      in.close();
    }
    super.reset();
  }

  private static class PostCloseProtection extends InputStream {
    private volatile boolean decoratedClosed = false;
    private final InputStream source;

    public PostCloseProtection(InputStream source) {
      this.source = source;
    }

    @Override
    public int read() throws IOException {
      return decoratedClosed ? -1 : source.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
      return decoratedClosed ? -1 : source.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      return decoratedClosed ? -1 : source.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
      return decoratedClosed ? 0 : source.skip(n);
    }

    @Override
    public int available() throws IOException {
      return source.available();
    }

    @Override
    public void close() throws IOException {
      decoratedClosed = true;
      source.close();
    }

    @Override
    public void mark(int readLimit) {
      source.mark(readLimit);
    }

    @Override
    public void reset() throws IOException {
      source.reset();
    }

    @Override
    public boolean markSupported() {
      return source.markSupported();
    }
  }
}
