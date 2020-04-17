package jp.co.cybird.app.android.lib.commons.file.json.io;

public class StringBuilderOutputSource implements OutputSource {
    private final StringBuilder sb;

    public StringBuilderOutputSource() {
        this.sb = new StringBuilder(1000);
    }

    public StringBuilderOutputSource(StringBuilder sb2) {
        this.sb = sb2;
    }

    public void append(String text) {
        this.sb.append(text);
    }

    public void append(String text, int start, int end) {
        this.sb.append(text, start, end);
    }

    public void append(char c) {
        this.sb.append(c);
    }

    public void flush() {
    }

    public void clear() {
        this.sb.setLength(0);
    }

    public String toString() {
        return this.sb.toString();
    }
}
