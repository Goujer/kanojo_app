package jp.co.cybird.app.android.lib.commons.file.json;

public enum JSONEventType {
    START_OBJECT,
    END_OBJECT,
    START_ARRAY,
    END_ARRAY,
    NAME,
    STRING,
    NUMBER,
    BOOLEAN,
    NULL,
    WHITESPACE,
    COMMENT
}
