package org.apache.james.mime4j.field.contenttype.parser;

public interface ContentTypeParserConstants {
    public static final int ANY = 23;
    public static final int ATOKEN = 21;
    public static final int COMMENT = 8;
    public static final int DEFAULT = 0;
    public static final int DIGITS = 20;
    public static final int EOF = 0;
    public static final int INCOMMENT = 1;
    public static final int INQUOTEDSTRING = 3;
    public static final int NESTED_COMMENT = 2;
    public static final int QUOTEDPAIR = 22;
    public static final int QUOTEDSTRING = 19;
    public static final int WS = 6;
    public static final String[] tokenImage = {"<EOF>", "\"\\r\"", "\"\\n\"", "\"/\"", "\";\"", "\"=\"", "<WS>", "\"(\"", "\")\"", "<token of kind 9>", "\"(\"", "<token of kind 11>", "<token of kind 12>", "\"(\"", "\")\"", "<token of kind 15>", "\"\\\"\"", "<token of kind 17>", "<token of kind 18>", "\"\\\"\"", "<DIGITS>", "<ATOKEN>", "<QUOTEDPAIR>", "<ANY>"};
}
