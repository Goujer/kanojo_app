package org.apache.james.mime4j.field.datetime.parser;

public interface DateTimeParserConstants {
    public static final int ANY = 48;
    public static final int COMMENT = 38;
    public static final int DEFAULT = 0;
    public static final int DIGITS = 46;
    public static final int EOF = 0;
    public static final int INCOMMENT = 1;
    public static final int MILITARY_ZONE = 35;
    public static final int NESTED_COMMENT = 2;
    public static final int OFFSETDIR = 24;
    public static final int QUOTEDPAIR = 47;
    public static final int WS = 36;
    public static final String[] tokenImage = {"<EOF>", "\"\\r\"", "\"\\n\"", "\",\"", "\"Mon\"", "\"Tue\"", "\"Wed\"", "\"Thu\"", "\"Fri\"", "\"Sat\"", "\"Sun\"", "\"Jan\"", "\"Feb\"", "\"Mar\"", "\"Apr\"", "\"May\"", "\"Jun\"", "\"Jul\"", "\"Aug\"", "\"Sep\"", "\"Oct\"", "\"Nov\"", "\"Dec\"", "\":\"", "<OFFSETDIR>", "\"UT\"", "\"GMT\"", "\"EST\"", "\"EDT\"", "\"CST\"", "\"CDT\"", "\"MST\"", "\"MDT\"", "\"PST\"", "\"PDT\"", "<MILITARY_ZONE>", "<WS>", "\"(\"", "\")\"", "<token of kind 39>", "\"(\"", "<token of kind 41>", "<token of kind 42>", "\"(\"", "\")\"", "<token of kind 45>", "<DIGITS>", "<QUOTEDPAIR>", "<ANY>"};
}
