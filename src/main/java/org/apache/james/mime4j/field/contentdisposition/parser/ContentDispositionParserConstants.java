package org.apache.james.mime4j.field.contentdisposition.parser;

public interface ContentDispositionParserConstants {
    public static final int ANY = 22;
    public static final int ATOKEN = 20;
    public static final int COMMENT = 7;
    public static final int DEFAULT = 0;
    public static final int DIGITS = 19;
    public static final int EOF = 0;
    public static final int INCOMMENT = 1;
    public static final int INQUOTEDSTRING = 3;
    public static final int NESTED_COMMENT = 2;
    public static final int QUOTEDPAIR = 21;
    public static final int QUOTEDSTRING = 18;
    public static final int WS = 5;
    public static final String[] tokenImage = {"<EOF>", "\"\\r\"", "\"\\n\"", "\";\"", "\"=\"", "<WS>", "\"(\"", "\")\"", "<token of kind 8>", "\"(\"", "<token of kind 10>", "<token of kind 11>", "\"(\"", "\")\"", "<token of kind 14>", "\"\\\"\"", "<token of kind 16>", "<token of kind 17>", "\"\\\"\"", "<DIGITS>", "<ATOKEN>", "<QUOTEDPAIR>", "<ANY>"};
}
