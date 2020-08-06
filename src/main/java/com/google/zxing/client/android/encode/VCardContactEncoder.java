package com.google.zxing.client.android.encode;

import android.telephony.PhoneNumberUtils;
import java.util.regex.Pattern;

final class VCardContactEncoder extends ContactEncoder {
    private static final Pattern NEWLINE = Pattern.compile("\\n");
    private static final Pattern RESERVED_VCARD_CHARS = Pattern.compile("([\\\\,;])");
    private static final char TERMINATOR = '\n';
    private static final Formatter VCARD_FIELD_FORMATTER = new Formatter() {
        public String format(String source) {
            return VCardContactEncoder.NEWLINE.matcher(VCardContactEncoder.RESERVED_VCARD_CHARS.matcher(source).replaceAll("\\\\$1")).replaceAll("");
        }
    };

    VCardContactEncoder() {
    }

    public String[] encode(Iterable<String> names, String organization, Iterable<String> addresses, Iterable<String> phones, Iterable<String> emails, Iterable<String> urls, String note) {
        StringBuilder newContents = new StringBuilder(100);
        newContents.append("BEGIN:VCARD").append(TERMINATOR);
        newContents.append("VERSION:3.0").append(TERMINATOR);
        StringBuilder newDisplayContents = new StringBuilder(100);
        appendUpToUnique(newContents, newDisplayContents, "N", names, 1, (Formatter) null);
        append(newContents, newDisplayContents, "ORG", organization);
        appendUpToUnique(newContents, newDisplayContents, "ADR", addresses, 1, (Formatter) null);
        appendUpToUnique(newContents, newDisplayContents, "TEL", phones, Integer.MAX_VALUE, new Formatter() {
            public String format(String source) {
                return PhoneNumberUtils.formatNumber(source);
            }
        });
        appendUpToUnique(newContents, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, (Formatter) null);
        appendUpToUnique(newContents, newDisplayContents, "URL", urls, Integer.MAX_VALUE, (Formatter) null);
        append(newContents, newDisplayContents, "NOTE", note);
        newContents.append("END:VCARD").append(TERMINATOR);
        return new String[]{newContents.toString(), newDisplayContents.toString()};
    }

    private static void append(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, String value) {
        doAppend(newContents, newDisplayContents, prefix, value, VCARD_FIELD_FORMATTER, TERMINATOR);
    }

    private static void appendUpToUnique(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, Iterable<String> values, int max, Formatter formatter) {
        doAppendUpToUnique(newContents, newDisplayContents, prefix, values, max, formatter, VCARD_FIELD_FORMATTER, TERMINATOR);
    }
}
