package org.apache.james.mime4j.field.mimeversion.parser;

public interface MimeVersionParserConstants {
    public static final int ANY = 20;
    public static final int COMMENT = 5;
    public static final int DEFAULT = 0;
    public static final int DIGITS = 17;
    public static final int DOT = 18;
    public static final int EOF = 0;
    public static final int INCOMMENT = 1;
    public static final int INQUOTEDSTRING = 3;
    public static final int NESTED_COMMENT = 2;
    public static final int QUOTEDPAIR = 19;
    public static final int QUOTEDSTRING = 16;
    public static final int WS = 3;
    public static final String[] tokenImage = {"<EOF>", "\"\\r\"", "\"\\n\"", "<WS>", "\"(\"", "\")\"", "<token of kind 6>", "\"(\"", "<token of kind 8>", "<token of kind 9>", "\"(\"", "\")\"", "<token of kind 12>", "\"\\\"\"", "<token of kind 14>", "<token of kind 15>", "\"\\\"\"", "<DIGITS>", "\".\"", "<QUOTEDPAIR>", "<ANY>"};
}
