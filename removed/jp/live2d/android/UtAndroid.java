package jp.live2d.android;

import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class UtAndroid {
    static boolean initial = true;

    public static void setup() {
        if (initial) {
            initial = false;
            System.setErr(new PrintStream(new LogOutputStream(System.err, true)));
            System.setOut(new PrintStream(new LogOutputStream(System.out, false)));
        }
    }

    private static class LogOutputStream extends OutputStream {
        PrintStream defaultOut;
        boolean isError;

        public LogOutputStream(PrintStream curOut, boolean isError2) {
            this.defaultOut = curOut;
            this.isError = isError2;
        }

        public void write(int b) throws IOException {
            this.defaultOut.write(b);
            if (this.isError) {
                Log.e("System.err", new StringBuilder().append((char) b).toString());
            } else {
                Log.v("System.out", new StringBuilder().append((char) b).toString());
            }
        }

        public void write(byte[] b, int off, int len) throws IOException {
            this.defaultOut.write(b);
            String str = new String(b, off, len);
            if (this.isError) {
                Log.e("Sys.err", str);
            } else {
                Log.v("Sys.out", str);
            }
        }

        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }
    }
}
