package org.apache.james.mime4j.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LineNumberInputStream extends FilterInputStream implements LineNumberSource {
    private int lineNumber = 1;

    public LineNumberInputStream(InputStream is) {
        super(is);
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int read() throws IOException {
        int b = this.in.read();
        if (b == 10) {
            this.lineNumber++;
        }
        return b;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int n = this.in.read(b, off, len);
        for (int i = off; i < off + n; i++) {
            if (b[i] == 10) {
                this.lineNumber++;
            }
        }
        return n;
    }
}
