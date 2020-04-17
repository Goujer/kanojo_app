package jp.co.cybird.app.android.lib.commons.file.json.util;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExtendedDateFormat extends SimpleDateFormat {
    private static final long serialVersionUID = 1479111858272394806L;
    boolean escape = false;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ExtendedDateFormat(String pattern, Locale locale) {
        super(escape(pattern), locale);
        boolean z = false;
        this.escape = !pattern.equals(toPattern()) ? true : z;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ExtendedDateFormat(String pattern) {
        super(escape(pattern));
        boolean z = false;
        this.escape = !pattern.equals(toPattern()) ? true : z;
    }

    static String escape(String pattern) {
        boolean skip = false;
        int count = 0;
        StringBuilder sb = null;
        int last = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '\'') {
                skip = !skip;
            } else if (c != 'Z' || skip) {
                count = 0;
            } else {
                count++;
                if (count == 2) {
                    if (sb == null) {
                        sb = new StringBuilder(pattern.length() + 4);
                    }
                    sb.append(pattern, last, i - 1);
                    sb.append("Z\u0000");
                    last = i + 1;
                }
            }
        }
        if (sb == null) {
            return pattern;
        }
        if (last < pattern.length()) {
            sb.append(pattern, last, pattern.length());
        }
        return sb.toString();
    }

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        super.format(date, toAppendTo, pos);
        if (this.escape) {
            for (int i = 5; i < toAppendTo.length(); i++) {
                if (toAppendTo.charAt(i) == 0) {
                    toAppendTo.setCharAt(i, toAppendTo.charAt(i - 1));
                    toAppendTo.setCharAt(i - 1, toAppendTo.charAt(i - 2));
                    toAppendTo.setCharAt(i - 2, ':');
                }
            }
        }
        return toAppendTo;
    }
}
