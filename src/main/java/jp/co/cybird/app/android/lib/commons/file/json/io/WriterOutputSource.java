package jp.co.cybird.app.android.lib.commons.file.json.io;

import java.io.IOException;
import java.io.Writer;

public class WriterOutputSource implements OutputSource {
    private final char[] buf = new char[1000];
    private int pos = 0;
    private final Writer writer;

    public WriterOutputSource(Writer writer2) {
        this.writer = writer2;
    }

    public void append(String text) throws IOException {
        append(text, 0, text.length());
    }

    public void append(String text, int start, int end) throws IOException {
        int length = end - start;
        if (this.pos + length < this.buf.length) {
            text.getChars(start, end, this.buf, this.pos);
            this.pos += length;
            return;
        }
        this.writer.write(this.buf, 0, this.pos);
        if (length < this.buf.length) {
            text.getChars(start, end, this.buf, 0);
            this.pos = length;
            return;
        }
        this.writer.write(text, start, length);
        this.pos = 0;
    }

    public void append(char c) throws IOException {
        if (this.pos + 1 >= this.buf.length) {
            this.writer.write(this.buf, 0, this.pos);
            this.pos = 0;
        }
        char[] cArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        cArr[i] = c;
    }

    public void flush() throws IOException {
        if (this.pos > 0) {
            this.writer.write(this.buf, 0, this.pos);
        }
        this.writer.flush();
    }
}
