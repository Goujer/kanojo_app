package org.apache.james.mime4j.field;

import java.util.HashMap;
import java.util.Map;
import org.apache.james.mime4j.util.ByteSequence;

public class DelegatingFieldParser implements FieldParser {
    private FieldParser defaultParser = UnstructuredField.PARSER;
    private Map<String, FieldParser> parsers = new HashMap();

    public void setFieldParser(String name, FieldParser parser) {
        this.parsers.put(name.toLowerCase(), parser);
    }

    public FieldParser getParser(String name) {
        FieldParser field = this.parsers.get(name.toLowerCase());
        if (field == null) {
            return this.defaultParser;
        }
        return field;
    }

    public ParsedField parse(String name, String body, ByteSequence raw) {
        return getParser(name).parse(name, body, raw);
    }
}
