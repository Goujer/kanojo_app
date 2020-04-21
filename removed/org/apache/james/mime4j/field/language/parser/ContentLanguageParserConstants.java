package org.apache.james.mime4j.field.language.parser;

public interface ContentLanguageParserConstants {
    public static final int ALPHA = 18;
    public static final int ALPHANUM = 19;
    public static final int ANY = 22;
    public static final int COMMENT = 5;
    public static final int DEFAULT = 0;
    public static final int DIGITS = 17;
    public static final int DOT = 20;
    public static final int EOF = 0;
    public static final int INCOMMENT = 1;
    public static final int INQUOTEDSTRING = 3;
    public static final int NESTED_COMMENT = 2;
    public static final int QUOTEDPAIR = 21;
    public static final int QUOTEDSTRING = 16;
    public static final int WS = 3;
    public static final String[] tokenImage = {"<EOF>", "\",\"", "\"-\"", "<WS>", "\"(\"", "\")\"", "<token of kind 6>", "\"(\"", "<token of kind 8>", "<token of kind 9>", "\"(\"", "\")\"", "<token of kind 12>", "\"\\\"\"", "<token of kind 14>", "<token of kind 15>", "\"\\\"\"", "<DIGITS>", "<ALPHA>", "<ALPHANUM>", "\".\"", "<QUOTEDPAIR>", "<ANY>"};
}
