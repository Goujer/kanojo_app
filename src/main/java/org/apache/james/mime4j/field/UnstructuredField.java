package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.util.ByteSequence;

public class UnstructuredField extends AbstractField {
    static final FieldParser PARSER = new FieldParser() {
        public ParsedField parse(String name, String body, ByteSequence raw) {
            return new UnstructuredField(name, body, raw);
        }
    };
    private boolean parsed = false;
    private String value;

    UnstructuredField(String name, String body, ByteSequence raw) {
        super(name, body, raw);
    }

    public String getValue() {
        if (!this.parsed) {
            parse();
        }
        return this.value;
    }

    private void parse() {
        this.value = DecoderUtil.decodeEncodedWords(getBody());
        this.parsed = true;
    }
}
