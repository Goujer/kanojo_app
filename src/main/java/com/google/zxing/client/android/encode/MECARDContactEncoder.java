package com.google.zxing.client.android.encode;

import android.telephony.PhoneNumberUtils;
import java.util.regex.Pattern;

final class MECARDContactEncoder extends ContactEncoder {
    /* access modifiers changed from: private */
    public static final Pattern COMMA = Pattern.compile(",");
    private static final Formatter MECARD_FIELD_FORMATTER = new Formatter() {
        public String format(String source) {
            return MECARDContactEncoder.NEWLINE.matcher(MECARDContactEncoder.RESERVED_MECARD_CHARS.matcher(source).replaceAll("\\\\$1")).replaceAll("");
        }
    };
    /* access modifiers changed from: private */
    public static final Pattern NEWLINE = Pattern.compile("\\n");
    private static final Pattern NOT_DIGITS = Pattern.compile("[^0-9]+");
    /* access modifiers changed from: private */
    public static final Pattern RESERVED_MECARD_CHARS = Pattern.compile("([\\\\:;])");
    private static final char TERMINATOR = ';';

    MECARDContactEncoder() {
    }

    public String[] encode(Iterable<String> names, String organization, Iterable<String> addresses, Iterable<String> phones, Iterable<String> emails, Iterable<String> urls, String note) {
        StringBuilder newContents = new StringBuilder(100);
        newContents.append("MECARD:");
        StringBuilder newDisplayContents = new StringBuilder(100);
        appendUpToUnique(newContents, newDisplayContents, "N", names, 1, new Formatter() {
            public String format(String source) {
                if (source == null) {
                    return null;
                }
                return MECARDContactEncoder.COMMA.matcher(source).replaceAll("");
            }
        });
        append(newContents, newDisplayContents, "ORG", organization);
        appendUpToUnique(newContents, newDisplayContents, "ADR", addresses, 1, (Formatter) null);
        appendUpToUnique(newContents, newDisplayContents, "TEL", phones, Integer.MAX_VALUE, new Formatter() {
            public String format(String source) {
                return MECARDContactEncoder.keepOnlyDigits(PhoneNumberUtils.formatNumber(source));
            }
        });
        appendUpToUnique(newContents, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, (Formatter) null);
        appendUpToUnique(newContents, newDisplayContents, "URL", urls, Integer.MAX_VALUE, (Formatter) null);
        append(newContents, newDisplayContents, "NOTE", note);
        newContents.append(TERMINATOR);
        return new String[]{newContents.toString(), newDisplayContents.toString()};
    }

    /* access modifiers changed from: private */
    public static String keepOnlyDigits(CharSequence s) {
        if (s == null) {
            return null;
        }
        return NOT_DIGITS.matcher(s).replaceAll("");
    }

    private static void append(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, String value) {
        doAppend(newContents, newDisplayContents, prefix, value, MECARD_FIELD_FORMATTER, TERMINATOR);
    }

    private static void appendUpToUnique(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, Iterable<String> values, int max, Formatter formatter) {
        doAppendUpToUnique(newContents, newDisplayContents, prefix, values, max, formatter, MECARD_FIELD_FORMATTER, TERMINATOR);
    }
}
