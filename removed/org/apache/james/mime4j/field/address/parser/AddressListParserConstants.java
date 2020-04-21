package org.apache.james.mime4j.field.address.parser;

public interface AddressListParserConstants {
    public static final int ALPHA = 11;
    public static final int ANY = 33;
    public static final int ATEXT = 13;
    public static final int COMMENT = 20;
    public static final int DEFAULT = 0;
    public static final int DIGIT = 12;
    public static final int DOMAINLITERAL = 18;
    public static final int DOTATOM = 14;
    public static final int EOF = 0;
    public static final int INCOMMENT = 2;
    public static final int INDOMAINLITERAL = 1;
    public static final int INQUOTEDSTRING = 4;
    public static final int NESTED_COMMENT = 3;
    public static final int QUOTEDPAIR = 32;
    public static final int QUOTEDSTRING = 31;
    public static final int WS = 10;
    public static final String[] tokenImage = {"<EOF>", "\"\\r\"", "\"\\n\"", "\",\"", "\":\"", "\";\"", "\"<\"", "\">\"", "\"@\"", "\".\"", "<WS>", "<ALPHA>", "<DIGIT>", "<ATEXT>", "<DOTATOM>", "\"[\"", "<token of kind 16>", "<token of kind 17>", "\"]\"", "\"(\"", "\")\"", "<token of kind 21>", "\"(\"", "<token of kind 23>", "<token of kind 24>", "\"(\"", "\")\"", "<token of kind 27>", "\"\\\"\"", "<token of kind 29>", "<token of kind 30>", "\"\\\"\"", "<QUOTEDPAIR>", "<ANY>"};
}
