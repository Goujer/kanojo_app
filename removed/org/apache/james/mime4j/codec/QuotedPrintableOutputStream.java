package org.apache.james.mime4j.codec;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QuotedPrintableOutputStream extends FilterOutputStream {
    private boolean closed = false;
    private QuotedPrintableEncoder encoder;

    public QuotedPrintableOutputStream(OutputStream out, boolean binary) {
        super(out);
        this.encoder = new QuotedPrintableEncoder(1024, binary);
        this.encoder.initEncoding(out);
    }

    public void close() throws IOException {
        if (!this.closed) {
            try {
                this.encoder.completeEncoding();
            } finally {
                this.closed = true;
            }
        }
    }

    public void flush() throws IOException {
        this.encoder.flushOutput();
    }

    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("QuotedPrintableOutputStream has been closed");
        }
        this.encoder.encodeChunk(b, off, len);
    }
}
