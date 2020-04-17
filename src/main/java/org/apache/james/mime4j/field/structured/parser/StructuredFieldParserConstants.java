package org.apache.james.mime4j.field.structured.parser;

public interface StructuredFieldParserConstants {
    public static final int ANY = 17;
    public static final int CONTENT = 15;
    public static final int DEFAULT = 0;
    public static final int EOF = 0;
    public static final int FOLD = 12;
    public static final int INCOMMENT = 1;
    public static final int INQUOTEDSTRING = 3;
    public static final int NESTED_COMMENT = 2;
    public static final int QUOTEDPAIR = 16;
    public static final int QUOTEDSTRING = 13;
    public static final int STRING_CONTENT = 11;
    public static final int WS = 14;
    public static final String[] tokenImage = {"<EOF>", "\"(\"", "\")\"", "\"(\"", "<token of kind 4>", "\"(\"", "\")\"", "<token of kind 7>", "<token of kind 8>", "\"\\\"\"", "<token of kind 10>", "<STRING_CONTENT>", "<FOLD>", "\"\\\"\"", "<WS>", "<CONTENT>", "<QUOTEDPAIR>", "<ANY>"};
}
