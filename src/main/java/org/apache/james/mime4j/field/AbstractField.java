package org.apache.james.mime4j.field;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;
import org.apache.james.mime4j.util.MimeUtil;

public abstract class AbstractField implements ParsedField {
    private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("^([\\x21-\\x39\\x3b-\\x7e]+):");
    private static final DefaultFieldParser parser = new DefaultFieldParser();
    private final String body;
    private final String name;
    private final ByteSequence raw;

    protected AbstractField(String name2, String body2, ByteSequence raw2) {
        this.name = name2;
        this.body = body2;
        this.raw = raw2;
    }

    public static ParsedField parse(ByteSequence raw2) throws MimeException {
        return parse(raw2, ContentUtil.decode(raw2));
    }

    public static ParsedField parse(String rawStr) throws MimeException {
        return parse(ContentUtil.encode(rawStr), rawStr);
    }

    public static DefaultFieldParser getParser() {
        return parser;
    }

    public String getName() {
        return this.name;
    }

    public ByteSequence getRaw() {
        return this.raw;
    }

    public String getBody() {
        return this.body;
    }

    public boolean isValidField() {
        return getParseException() == null;
    }

    public ParseException getParseException() {
        return null;
    }

    public String toString() {
        return this.name + ": " + this.body;
    }

    private static ParsedField parse(ByteSequence raw2, String rawStr) throws MimeException {
        String unfolded = MimeUtil.unfold(rawStr);
        Matcher fieldMatcher = FIELD_NAME_PATTERN.matcher(unfolded);
        if (!fieldMatcher.find()) {
            throw new MimeException("Invalid field in string");
        }
        String name2 = fieldMatcher.group(1);
        String body2 = unfolded.substring(fieldMatcher.end());
        if (body2.length() > 0 && body2.charAt(0) == ' ') {
            body2 = body2.substring(1);
        }
        return parser.parse(name2, body2, raw2);
    }
}
