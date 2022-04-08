package jp.co.cybird.app.android.lib.commons.file.json.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReaderInputSource implements InputSource {
    private static int BACK = 20;
    private int back = BACK;
    private final char[] buf = new char[(BACK + 256)];
    private long columns = 0;
    private int end = (BACK - 1);
    private InputStream in;
    private long lines = 1;
    private int mark = -1;
    private long offset = 0;
    private Reader reader;
    private int start = BACK;

    public ReaderInputSource(InputStream in2) {
        if (in2 == null) {
            throw new NullPointerException();
        }
        this.in = in2;
    }

    public ReaderInputSource(Reader reader2) {
        if (reader2 == null) {
            throw new NullPointerException();
        }
        this.reader = reader2;
    }

    public int next() throws IOException {
        int n = get();
        if (n != -1) {
            this.offset++;
            if (n == 13) {
                this.lines++;
                this.columns = 0;
            } else if (n != 10) {
                this.columns++;
            } else if (this.start < 2 || this.buf[this.start - 2] != 13) {
                this.lines++;
                this.columns = 0;
            }
        }
        return n;
    }

    private int get() throws IOException {
        int i = -1;
        if (this.start > this.end) {
            if (this.end > BACK) {
                int len = Math.min(BACK, (this.end - BACK) + 1);
                System.arraycopy(this.buf, (this.end + 1) - len, this.buf, BACK - len, len);
                this.back = BACK - len;
            }
            if (this.in != null) {
                if (!this.in.markSupported()) {
                    this.in = new BufferedInputStream(this.in);
                }
                this.reader = new InputStreamReader(this.in, determineEncoding(this.in));
                this.in = null;
            }
            int size = this.reader.read(this.buf, BACK, this.buf.length - BACK);
            if (size != -1) {
                if (this.mark > this.end - BACK) {
                    i = BACK - ((this.end - this.mark) + 1);
                }
                this.mark = i;
                this.start = BACK;
                this.end = (BACK + size) - 1;
            } else {
                this.start++;
                return -1;
            }
        }
        char[] cArr = this.buf;
        int i2 = this.start;
        this.start = i2 + 1;
        return cArr[i2];
    }

    public void back() {
        if (this.start <= this.back) {
            throw new IllegalStateException("no backup charcter");
        }
        this.start--;
        if (this.start <= this.end) {
            this.offset--;
            this.columns--;
        }
    }

    public int mark() throws IOException {
        if (this.start > this.end) {
            int c = get();
            back();
            if (c == -1) {
                this.mark = -1;
                return 0;
            }
        }
        this.mark = this.start;
        return (this.end - this.mark) + 1;
    }

    public void copy(StringBuilder sb, int len) {
        if (this.mark == -1) {
            throw new IllegalStateException("no mark");
        } else if (this.mark + len > this.end + 1) {
            throw new IndexOutOfBoundsException();
        } else {
            sb.append(this.buf, this.mark, len);
        }
    }

    public String copy(int len) {
        if (this.mark == -1) {
            throw new IllegalStateException("no mark");
        } else if (this.mark + len <= this.end + 1) {
            return String.valueOf(this.buf, this.mark, len);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public long getLineNumber() {
        return this.lines;
    }

    public long getColumnNumber() {
        return this.columns;
    }

    public long getOffset() {
        return this.offset;
    }

    private static String determineEncoding(InputStream in2) throws IOException {
        String encoding = "UTF-8";
        in2.mark(4);
        byte[] check = new byte[4];
        int size = in2.read(check);
        if (size == 2) {
            if (((check[0] & 255) == 0 && (check[1] & 255) != 0) || ((check[0] & 255) == 254 && (check[1] & 255) == 255)) {
                encoding = "UTF-16BE";
            } else if (((check[0] & 255) != 0 && (check[1] & 255) == 0) || ((check[0] & 255) == 255 && (check[1] & 255) == 254)) {
                encoding = "UTF-16LE";
            }
        } else if (size == 4) {
            if ((check[0] & 255) == 0 && (check[1] & 255) == 0) {
                encoding = "UTF-32BE";
            } else if ((check[2] & 255) == 0 && (check[3] & 255) == 0) {
                encoding = "UTF-32LE";
            } else if (((check[0] & 255) == 0 && (check[1] & 255) != 0) || ((check[0] & 255) == 254 && (check[1] & 255) == 255)) {
                encoding = "UTF-16BE";
            } else if (((check[0] & 255) != 0 && (check[1] & 255) == 0) || ((check[0] & 255) == 255 && (check[1] & 255) == 254)) {
                encoding = "UTF-16LE";
            }
        }
        in2.reset();
        return encoding;
    }

    public String toString() {
        int spos = this.back;
        int max = Math.min(this.start - 1, this.end);
        int charCount = 0;
        int i = 0;
        while (i < (max + 1) - this.back && i < BACK) {
            char c = this.buf[max - i];
            if (c == 13 || (c == 10 && ((max - i) - 1 < 0 || this.buf[(max - i) - 1] != 13))) {
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
            return String.valueOf(this.buf, spos, (max - spos) + 1);
        }
        return "";
    }
}
