package org.apache.james.mime4j.field.contenttype.parser;

public class Token {
    public int beginColumn;
    public int beginLine;
    public int endColumn;
    public int endLine;
    public String image;
    public int kind;
    public Token next;
    public Token specialToken;

    public String toString() {
        return this.image;
    }

    public static final Token newToken(int ofKind) {
        return new Token();
    }
}
