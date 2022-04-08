package jp.co.cybird.app.android.lib.commons.file.json;

public class JSONException extends RuntimeException {
    public static final int FORMAT_ERROR = 100;
    public static final int PARSE_ERROR = 200;
    public static final int POSTPARSE_ERROR = 250;
    public static final int PREFORMAT_ERROR = 150;
    private static final long serialVersionUID = -8323989588488596436L;
    private long columnNumber = -1;
    private int errorID;
    private long lineNumber = -1;
    private long offset = -1;

    public JSONException(String message, int id, long lineNumber2, long columnNumber2, long offset2) {
        super(message);
        this.errorID = id;
        this.lineNumber = lineNumber2;
        this.columnNumber = columnNumber2;
        this.offset = offset2;
    }

    public JSONException(String message, int id, Throwable cause) {
        super(message, cause);
        this.errorID = id;
    }

    public JSONException(String message, int id) {
        super(message);
        this.errorID = id;
    }

    public int getErrorCode() {
        return this.errorID;
    }

    public long getLineNumber() {
        return this.lineNumber;
    }

    public long getColumnNumber() {
        return this.columnNumber;
    }

    public long getErrorOffset() {
        return this.offset;
    }
}
