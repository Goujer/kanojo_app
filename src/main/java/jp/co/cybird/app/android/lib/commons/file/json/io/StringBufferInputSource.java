package jp.co.cybird.app.android.lib.commons.file.json.io;

public class StringBufferInputSource extends CharSequenceInputSource {
    private final StringBuffer sb;

    public StringBufferInputSource(StringBuffer sb2) {
        super(sb2);
        this.sb = sb2;
    }

    public void copy(StringBuilder sb2, int len) {
        if (this.mark == -1) {
            throw new IllegalStateException("no mark");
        } else if (this.mark + len > this.sb.length()) {
            throw new IndexOutOfBoundsException();
        } else {
            sb2.append(this.sb, this.mark, this.mark + len);
        }
    }

    public String copy(int len) {
        if (this.mark == -1) {
            throw new IllegalStateException("no mark");
        } else if (this.mark + len <= this.sb.length()) {
            return this.sb.substring(this.mark, this.mark + len);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
