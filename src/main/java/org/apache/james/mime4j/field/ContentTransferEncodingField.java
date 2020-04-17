package org.apache.james.mime4j.field;

import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.MimeUtil;

public class ContentTransferEncodingField extends AbstractField {
    static final FieldParser PARSER = new FieldParser() {
        public ParsedField parse(String name, String body, ByteSequence raw) {
            return new ContentTransferEncodingField(name, body, raw);
        }
    };
    private String encoding;

    ContentTransferEncodingField(String name, String body, ByteSequence raw) {
        super(name, body, raw);
        this.encoding = body.trim().toLowerCase();
    }

    public String getEncoding() {
        return this.encoding;
    }

    public static String getEncoding(ContentTransferEncodingField f) {
        if (f == null || f.getEncoding().length() == 0) {
            return MimeUtil.ENC_7BIT;
        }
        return f.getEncoding();
    }
}
