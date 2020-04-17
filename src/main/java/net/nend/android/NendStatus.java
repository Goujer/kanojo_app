package net.nend.android;

import java.util.Locale;

enum NendStatus {
    SUCCESS(200, "Success!"),
    INITIAL(800, "Initial state"),
    ERR_INVALID_ATTRIBUTE_SET(811, "AttributeSet is invalid."),
    ERR_INVALID_API_KEY(812, "Api key is invalid."),
    ERR_INVALID_SPOT_ID(813, "Spot id is invalid."),
    ERR_INVALID_CONTEXT(814, "Context is invalid."),
    ERR_INVALID_URL(815, "Url is invalid."),
    ERR_INVALID_RESPONSE(814, "Response is invalid."),
    ERR_INVALID_AD_STATUS(815, "Ad status is invalid."),
    ERR_INVALID_RESPONSE_TYPE(816, "Response type is invalid."),
    ERR_HTTP_REQUEST(820, "Failed to http request."),
    ERR_FAILED_TO_PARSE(821, "Failed to parse response."),
    ERR_OUT_OF_STOCK(830, "Ad is out of stock."),
    ERR_UNEXPECTED(899, "Unexpected error.");
    
    private static final String STATUS_PREFIX = "AE";
    private final int code;
    private final String msg;

    private NendStatus(int code2, String msg2) {
        this.code = code2;
        this.msg = msg2;
    }

    /* access modifiers changed from: package-private */
    public int getCode() {
        return this.code;
    }

    /* access modifiers changed from: package-private */
    public String getMsg() {
        return String.format(Locale.US, "[%s%d] %s", new Object[]{STATUS_PREFIX, Integer.valueOf(this.code), this.msg});
    }

    /* access modifiers changed from: package-private */
    public String getMsg(String customMsg) {
        return String.format(Locale.US, "[%s%d] %s %s", new Object[]{STATUS_PREFIX, Integer.valueOf(this.code), this.msg, customMsg});
    }
}
