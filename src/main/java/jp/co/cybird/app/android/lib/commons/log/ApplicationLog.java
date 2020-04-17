package jp.co.cybird.app.android.lib.commons.log;

public class ApplicationLog {
    String mMessage;
    String mTag;
    long mTimestamp;

    public ApplicationLog(String tag, String message) {
        this.mTimestamp = System.currentTimeMillis();
        this.mTag = tag;
        this.mMessage = message;
    }

    public ApplicationLog(long timestamp, String tag, String message) {
        this.mTimestamp = timestamp;
        this.mTag = tag;
        this.mMessage = message;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public String getTag() {
        return this.mTag;
    }

    public String getMessage() {
        return this.mMessage;
    }
}
