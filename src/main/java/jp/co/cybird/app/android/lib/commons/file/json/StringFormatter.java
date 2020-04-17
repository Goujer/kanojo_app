package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class StringFormatter implements Formatter {
    private static final int[] ESCAPE_CHARS = new int[128];
    public static final StringFormatter INSTANCE = new StringFormatter();

    StringFormatter() {
    }

    static {
        for (int i = 0; i < 32; i++) {
            ESCAPE_CHARS[i] = -1;
        }
        ESCAPE_CHARS[8] = 98;
        ESCAPE_CHARS[9] = 116;
        ESCAPE_CHARS[10] = 110;
        ESCAPE_CHARS[12] = 102;
        ESCAPE_CHARS[13] = 114;
        ESCAPE_CHARS[34] = 34;
        ESCAPE_CHARS[92] = 92;
        ESCAPE_CHARS[60] = -2;
        ESCAPE_CHARS[62] = -2;
        ESCAPE_CHARS[127] = -1;
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        serialize(context, o.toString(), out);
        return false;
    }

    static void serialize(JSON.JSONContext context, String s, OutputSource out) throws Exception {
        out.append('\"');
        int start = 0;
        int length = s.length();
        for (int i = 0; i < length; i++) {
            int c = s.charAt(i);
            if (c < ESCAPE_CHARS.length) {
                int x = ESCAPE_CHARS[c];
                if (x != 0) {
                    if (x > 0) {
                        if (start < i) {
                            out.append(s, start, i);
                        }
                        out.append('\\');
                        out.append((char) x);
                        start = i + 1;
                    } else if (x == -1 || (x == -2 && context.getMode() != JSON.Mode.STRICT)) {
                        if (start < i) {
                            out.append(s, start, i);
                        }
                        out.append("\\u00");
                        out.append("0123456789ABCDEF".charAt(c / 16));
                        out.append("0123456789ABCDEF".charAt(c % 16));
                        start = i + 1;
                    }
                }
            } else if (c == 8232) {
                if (start < i) {
                    out.append(s, start, i);
                }
                out.append("\\u2028");
                start = i + 1;
            } else if (c == 8233) {
                if (start < i) {
                    out.append(s, start, i);
                }
                out.append("\\u2029");
                start = i + 1;
            }
        }
        if (start < length) {
            out.append(s, start, length);
        }
        out.append('\"');
    }
}
