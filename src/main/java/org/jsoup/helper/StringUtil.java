package org.jsoup.helper;

import java.util.Collection;
import java.util.Iterator;

public final class StringUtil {
    private static final String[] padding = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          "};

    public static String join(Collection strings, String sep) {
        return join(strings.iterator(), sep);
    }

    public static String join(Iterator strings, String sep) {
        if (!strings.hasNext()) {
            return "";
        }
        String start = strings.next().toString();
        if (!strings.hasNext()) {
            return start;
        }
        StringBuilder sb = new StringBuilder(64).append(start);
        while (strings.hasNext()) {
            sb.append(sep);
            sb.append(strings.next());
        }
        return sb.toString();
    }

    public static String padding(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("width must be > 0");
        } else if (width < padding.length) {
            return padding[width];
        } else {
            char[] out = new char[width];
            for (int i = 0; i < width; i++) {
                out[i] = ' ';
            }
            return String.valueOf(out);
        }
    }

    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!isWhitespace(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String string) {
        if (string == null || string.length() == 0) {
            return false;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isDigit(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhitespace(int c) {
        return c == 32 || c == 9 || c == 10 || c == 12 || c == 13;
    }

    public static String normaliseWhitespace(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        boolean lastWasWhite = false;
        boolean modified = false;
        int l = string.length();
        int i = 0;
        while (i < l) {
            int c = string.codePointAt(i);
            if (!isWhitespace(c)) {
                sb.appendCodePoint(c);
                lastWasWhite = false;
            } else if (lastWasWhite) {
                modified = true;
            } else {
                if (c != 32) {
                    modified = true;
                }
                sb.append(' ');
                lastWasWhite = true;
            }
            i += Character.charCount(c);
        }
        return modified ? sb.toString() : string;
    }

    public static boolean in(String needle, String... haystack) {
        for (String hay : haystack) {
            if (hay.equals(needle)) {
                return true;
            }
        }
        return false;
    }
}
