package jp.co.cybird.app.android.lib.commons.file.json.io;

import java.io.IOException;

public class AppendableOutputSource implements OutputSource {
    private final Appendable ap;

    public AppendableOutputSource(Appendable ap2) {
        this.ap = ap2;
    }

    public void append(String text) throws IOException {
        this.ap.append(text);
    }

    public void append(String text, int start, int end) throws IOException {
        this.ap.append(text, start, end);
    }

    public void append(char c) throws IOException {
        this.ap.append(c);
    }

    public void flush() throws IOException {
    }

    public String toString() {
        return this.ap.toString();
    }
}
