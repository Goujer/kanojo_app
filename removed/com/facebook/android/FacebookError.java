package com.facebook.android;

public class FacebookError extends RuntimeException {
    private static final long serialVersionUID = 1;
    private int mErrorCode = 0;
    private String mErrorType;

    @Deprecated
    public FacebookError(String message) {
        super(message);
    }

    @Deprecated
    public FacebookError(String message, String type, int code) {
        super(message);
        this.mErrorType = type;
        this.mErrorCode = code;
    }

    @Deprecated
    public int getErrorCode() {
        return this.mErrorCode;
    }

    @Deprecated
    public String getErrorType() {
        return this.mErrorType;
    }
}
