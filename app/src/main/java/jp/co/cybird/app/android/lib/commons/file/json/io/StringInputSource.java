package jp.co.cybird.app.android.lib.commons.file.json.io;

public class StringInputSource extends CharSequenceInputSource {
    private final String str;

    public StringInputSource(String str2) {
        super(str2);
        this.str = str2;
    }

    public void copy(StringBuilder sb, int len) {
        if (this.mark == -1) {
            throw new IllegalStateException("no mark");
        }
        sb.append(this.str, this.mark, this.mark + len);
    }
}
