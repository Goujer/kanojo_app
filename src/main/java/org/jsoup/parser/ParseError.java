package org.jsoup.parser;

public class ParseError {
    private String errorMsg;
    private int pos;

    ParseError(int pos2, String errorMsg2) {
        this.pos = pos2;
        this.errorMsg = errorMsg2;
    }

    ParseError(int pos2, String errorFormat, Object... args) {
        this.errorMsg = String.format(errorFormat, args);
        this.pos = pos2;
    }

    public String getErrorMessage() {
        return this.errorMsg;
    }

    public int getPosition() {
        return this.pos;
    }

    public String toString() {
        return this.pos + ": " + this.errorMsg;
    }
}
