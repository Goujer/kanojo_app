package org.apache.james.mime4j.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PositionInputStream extends FilterInputStream {
    private long markedPosition = 0;
    protected long position = 0;

    public PositionInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public long getPosition() {
        return this.position;
    }

    public int available() throws IOException {
        return this.in.available();
    }

    public int read() throws IOException {
        int b = this.in.read();
        if (b != -1) {
            this.position++;
        }
        return b;
    }

    public void close() throws IOException {
        this.in.close();
    }

    public void reset() throws IOException {
        this.in.reset();
        this.position = this.markedPosition;
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    public void mark(int readlimit) {
        this.in.mark(readlimit);
        this.markedPosition = this.position;
    }

    public long skip(long n) throws IOException {
        long c = this.in.skip(n);
        if (c > 0) {
            this.position += c;
        }
        return c;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int c = this.in.read(b, off, len);
        if (c > 0) {
            this.position += (long) c;
        }
        return c;
    }
}
