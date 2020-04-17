package jp.co.cybird.app.android.lib.commons.file.json.io;

public class CharSequenceInputSource implements InputSource {
    private int columns = 0;
    private final CharSequence cs;
    private int lines = 1;
    int mark = -1;
    private int offset = 0;
    private int start = 0;

    public CharSequenceInputSource(CharSequence cs2) {
        if (cs2 == null) {
            throw new NullPointerException();
        }
        this.cs = cs2;
    }

    public int next() {
        if (this.start < this.cs.length()) {
            CharSequence charSequence = this.cs;
            int i = this.start;
            this.start = i + 1;
            int n = charSequence.charAt(i);
            this.offset++;
            if (n == 13) {
                this.lines++;
                this.columns = 0;
            } else if (n != 10) {
                this.columns++;
            } else if (this.offset < 2 || this.cs.charAt(this.offset - 2) != 13) {
                this.lines++;
                this.columns = 0;
            }
            return n;
        }
        this.start++;
        return -1;
    }

    public void back() {
        if (this.start == 0) {
            throw new IllegalStateException("no backup charcter");
        }
        this.start--;
        if (this.start < this.cs.length()) {
            this.offset--;
            this.columns--;
        }
    }

    public int mark() {
        this.mark = this.start;
        return this.cs.length() - this.mark;
    }

    public void copy(StringBuilder sb, int len) {
        if (this.mark == -1) {
            throw new IllegalStateException("no mark");
        } else if (this.mark + len > this.cs.length()) {
            throw new IndexOutOfBoundsException();
        } else {
            sb.append(this.cs, this.mark, this.mark + len);
        }
    }

    public String copy(int len) {
        if (this.mark == -1) {
            throw new IllegalStateException("no mark");
        } else if (this.mark + len > this.cs.length()) {
            throw new IndexOutOfBoundsException();
        } else {
            char[] array = new char[len];
            for (int i = 0; i < len; i++) {
                array[i] = this.cs.charAt(this.mark + i);
            }
            return String.valueOf(array);
        }
    }

    public long getLineNumber() {
        return (long) this.lines;
    }

    public long getColumnNumber() {
        return (long) this.columns;
    }

    public long getOffset() {
        return (long) this.offset;
    }

    public String toString() {
        int spos = 0;
        int max = Math.min(this.start - 1, this.cs.length() - 1);
        int charCount = 0;
        int i = 0;
        while (i < max + 1 && i < 20) {
            char c = this.cs.charAt(max - i);
            if (c == 13 || (c == 10 && ((max - i) - 1 < 0 || this.cs.charAt((max - i) - 1) != 13))) {
                if (charCount > 0) {
                    break;
                }
            } else if (c != 10) {
                spos = max - i;
                charCount++;
            }
            i++;
        }
        if (spos <= max) {
            return this.cs.subSequence(spos, max + 1).toString();
        }
        return "";
    }
}
