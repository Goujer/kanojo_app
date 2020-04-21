package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class LineReaderInputStreamAdaptor extends LineReaderInputStream {
    private final LineReaderInputStream bis;
    private boolean eof;
    private final int maxLineLen;
    private boolean used;

    public LineReaderInputStreamAdaptor(InputStream is, int maxLineLen2) {
        super(is);
        this.used = false;
        this.eof = false;
        if (is instanceof LineReaderInputStream) {
            this.bis = (LineReaderInputStream) is;
        } else {
            this.bis = null;
        }
        this.maxLineLen = maxLineLen2;
    }

    public LineReaderInputStreamAdaptor(InputStream is) {
        this(is, -1);
    }

    public int read() throws IOException {
        int i = this.in.read();
        this.eof = i == -1;
        this.used = true;
        return i;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int i = this.in.read(b, off, len);
        this.eof = i == -1;
        this.used = true;
        return i;
    }

    public int readLine(ByteArrayBuffer dst) throws IOException {
        int i;
        if (this.bis != null) {
            i = this.bis.readLine(dst);
        } else {
            i = doReadLine(dst);
        }
        this.eof = i == -1;
        this.used = true;
        return i;
    }

    private int doReadLine(ByteArrayBuffer dst) throws IOException {
        int ch;
        int total = 0;
        do {
            ch = this.in.read();
            if (ch == -1) {
                break;
            }
            dst.append(ch);
            total++;
            if (this.maxLineLen > 0 && dst.length() >= this.maxLineLen) {
                throw new MaxLineLimitException("Maximum line length limit exceeded");
            }
        } while (ch != 10);
        if (total == 0 && ch == -1) {
            return -1;
        }
        return total;
    }

    public boolean eof() {
        return this.eof;
    }

    public boolean isUsed() {
        return this.used;
    }

    public String toString() {
        return "[LineReaderInputStreamAdaptor: " + this.bis + "]";
    }
}
