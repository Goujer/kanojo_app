package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class EOLConvertingInputStream extends InputStream {
    public static final int CONVERT_BOTH = 3;
    public static final int CONVERT_CR = 1;
    public static final int CONVERT_LF = 2;
    private int flags;
    private PushbackInputStream in;
    private int previous;

    public EOLConvertingInputStream(InputStream in2) {
        this(in2, 3);
    }

    public EOLConvertingInputStream(InputStream in2, int flags2) {
        this.in = null;
        this.previous = 0;
        this.flags = 3;
        this.in = new PushbackInputStream(in2, 2);
        this.flags = flags2;
    }

    public void close() throws IOException {
        this.in.close();
    }

    public int read() throws IOException {
        int b = this.in.read();
        if (b == -1) {
            return -1;
        }
        if ((this.flags & 1) != 0 && b == 13) {
            int c = this.in.read();
            if (c != -1) {
                this.in.unread(c);
            }
            if (c != 10) {
                this.in.unread(10);
            }
        } else if (!((this.flags & 2) == 0 || b != 10 || this.previous == 13)) {
            b = 13;
            this.in.unread(10);
        }
        this.previous = b;
        return b;
    }
}
