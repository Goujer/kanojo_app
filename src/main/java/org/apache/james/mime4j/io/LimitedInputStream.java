package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends PositionInputStream {
    private final long limit;

    public LimitedInputStream(InputStream instream, long limit2) {
        super(instream);
        if (limit2 < 0) {
            throw new IllegalArgumentException("Limit may not be negative");
        }
        this.limit = limit2;
    }

    private void enforceLimit() throws IOException {
        if (this.position >= this.limit) {
            throw new IOException("Input stream limit exceeded");
        }
    }

    public int read() throws IOException {
        enforceLimit();
        return super.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        enforceLimit();
        return super.read(b, off, Math.min(len, getBytesLeft()));
    }

    public long skip(long n) throws IOException {
        enforceLimit();
        return super.skip(Math.min(n, (long) getBytesLeft()));
    }

    private int getBytesLeft() {
        return (int) Math.min(2147483647L, this.limit - this.position);
    }
}
