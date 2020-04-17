package org.apache.james.mime4j.field;

import org.apache.james.mime4j.parser.Field;

public interface ParsedField extends Field {
    ParseException getParseException();

    boolean isValidField();
}
