package twitter4j;

import java.io.IOException;
import java.util.List;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;
import twitter4j.internal.json.z_T4JInternalJSONImplFactory;
import twitter4j.internal.json.z_T4JInternalParseUtil;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public class TwitterException extends Exception implements TwitterResponse, HttpResponseCode {
    private static final String[] FILTER = {"twitter4j"};
    private static final long serialVersionUID = -2623309261327598087L;
    private int errorCode;
    private String errorMessage;
    private ExceptionDiagnosis exceptionDiagnosis;
    boolean nested;
    private HttpResponse response;
    private int statusCode;

    public TwitterException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.errorCode = -1;
        this.exceptionDiagnosis = null;
        this.errorMessage = null;
        this.nested = false;
        decode(message);
    }

    public TwitterException(String message) {
        this(message, (Throwable) null);
    }

    public TwitterException(Exception cause) {
        this(cause.getMessage(), (Throwable) cause);
        if (cause instanceof TwitterException) {
            ((TwitterException) cause).setNested();
        }
    }

    public TwitterException(String message, HttpResponse res) {
        this(message);
        this.response = res;
        this.statusCode = res.getStatusCode();
    }

    public TwitterException(String message, Exception cause, int statusCode2) {
        this(message, (Throwable) cause);
        this.statusCode = statusCode2;
    }

    public String getMessage() {
        StringBuilder value = new StringBuilder();
        if (this.errorMessage == null || this.errorCode == -1) {
            value.append(super.getMessage());
        } else {
            value.append("message - ").append(this.errorMessage).append("\n");
            value.append("code - ").append(this.errorCode).append("\n");
        }
        if (this.statusCode != -1) {
            return getCause(this.statusCode) + "\n" + value.toString();
        }
        return value.toString();
    }

    private void decode(String str) {
        if (str != null && str.startsWith("{")) {
            try {
                JSONObject json = new JSONObject(str);
                if (!json.isNull("errors")) {
                    JSONObject error = json.getJSONArray("errors").getJSONObject(0);
                    this.errorMessage = error.getString("message");
                    this.errorCode = z_T4JInternalParseUtil.getInt("code", error);
                }
            } catch (JSONException e) {
            }
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getResponseHeader(String name) {
        if (this.response == null) {
            return null;
        }
        List<String> header = this.response.getResponseHeaderFields().get(name);
        if (header.size() > 0) {
            return header.get(0);
        }
        return null;
    }

    public RateLimitStatus getRateLimitStatus() {
        if (this.response == null) {
            return null;
        }
        return z_T4JInternalJSONImplFactory.createRateLimitStatusFromResponseHeader(this.response);
    }

    public int getAccessLevel() {
        return z_T4JInternalParseUtil.toAccessLevel(this.response);
    }

    public int getRetryAfter() {
        if (this.statusCode == 400) {
            RateLimitStatus rateLimitStatus = getRateLimitStatus();
            if (rateLimitStatus != null) {
                return rateLimitStatus.getSecondsUntilReset();
            }
            return -1;
        } else if (this.statusCode != 420) {
            return -1;
        } else {
            try {
                String retryAfterStr = this.response.getResponseHeader("Retry-After");
                if (retryAfterStr != null) {
                    return Integer.valueOf(retryAfterStr).intValue();
                }
                return -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    public boolean isCausedByNetworkIssue() {
        return getCause() instanceof IOException;
    }

    public boolean exceededRateLimitation() {
        return (this.statusCode == 400 && getRateLimitStatus() != null) || this.statusCode == 420 || this.statusCode == 429;
    }

    public boolean resourceNotFound() {
        return this.statusCode == 404;
    }

    public String getExceptionCode() {
        return getExceptionDiagnosis().asHexString();
    }

    private ExceptionDiagnosis getExceptionDiagnosis() {
        if (this.exceptionDiagnosis == null) {
            this.exceptionDiagnosis = new ExceptionDiagnosis(this, FILTER);
        }
        return this.exceptionDiagnosis;
    }

    /* access modifiers changed from: package-private */
    public void setNested() {
        this.nested = true;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isErrorMessageAvailable() {
        return this.errorMessage != null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterException that = (TwitterException) o;
        if (this.errorCode != that.errorCode) {
            return false;
        }
        if (this.nested != that.nested) {
            return false;
        }
        if (this.statusCode != that.statusCode) {
            return false;
        }
        if (this.errorMessage == null ? that.errorMessage != null : !this.errorMessage.equals(that.errorMessage)) {
            return false;
        }
        if (this.exceptionDiagnosis == null ? that.exceptionDiagnosis != null : !this.exceptionDiagnosis.equals(that.exceptionDiagnosis)) {
            return false;
        }
        if (this.response != null) {
            if (this.response.equals(that.response)) {
                return true;
            }
        } else if (that.response == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        int i5 = ((this.statusCode * 31) + this.errorCode) * 31;
        if (this.exceptionDiagnosis != null) {
            i = this.exceptionDiagnosis.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.response != null) {
            i2 = this.response.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.errorMessage != null) {
            i3 = this.errorMessage.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (this.nested) {
            i4 = 1;
        }
        return i8 + i4;
    }

    public String toString() {
        return getMessage() + (this.nested ? "" : "\nRelevant discussions can be found on the Internet at:\n\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getStackLineHashAsHex() + " or\n\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getLineNumberHashAsHex()) + "\nTwitterException{" + (this.nested ? "" : "exceptionCode=[" + getExceptionCode() + "], ") + "statusCode=" + this.statusCode + ", message=" + this.errorMessage + ", code=" + this.errorCode + ", retryAfter=" + getRetryAfter() + ", rateLimitStatus=" + getRateLimitStatus() + ", version=" + Version.getVersion() + '}';
    }

    private static String getCause(int statusCode2) {
        String cause;
        switch (statusCode2) {
            case HttpResponseCode.NOT_MODIFIED /*304*/:
                cause = "There was no new data to return.";
                break;
            case 400:
                cause = "The request was invalid. An accompanying error message will explain why. This is the status code will be returned during version 1.0 rate limiting(https://dev.twitter.com/pages/rate-limiting). In API v1.1, a request without authentication is considered invalid and you will get this response.";
                break;
            case 401:
                cause = "Authentication credentials (https://dev.twitter.com/pages/auth) were missing or incorrect. Ensure that you have set valid consumer key/secret, access token/secret, and the system clock is in sync.";
                break;
            case 403:
                cause = "The request is understood, but it has been refused. An accompanying error message will explain why. This code is used when requests are being denied due to update limits (https://support.twitter.com/articles/15364-about-twitter-limits-update-api-dm-and-following).";
                break;
            case 404:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists. Also returned when the requested format is not supported by the requested method.";
                break;
            case HttpResponseCode.NOT_ACCEPTABLE /*406*/:
                cause = "Returned by the Search API when an invalid format is specified in the request.\nReturned by the Streaming API when one or more of the parameters are not suitable for the resource. The track parameter, for example, would throw this error if:\n The track keyword is too long or too short.\n The bounding box specified is invalid.\n No predicates defined for filtered resource, for example, neither track nor follow parameter defined.\n Follow userid cannot be read.";
                break;
            case HttpResponseCode.ENHANCE_YOUR_CLAIM /*420*/:
                cause = "Returned by the Search and Trends API when you are being rate limited (https://dev.twitter.com/docs/rate-limiting).\nReturned by the Streaming API:\n Too many login attempts in a short period of time.\n Running too many copies of the same application authenticating with the same account name.";
                break;
            case HttpResponseCode.UNPROCESSABLE_ENTITY /*422*/:
                cause = "Returned when an image uploaded to POST account/update_profile_banner(https://dev.twitter.com/docs/api/1/post/account/update_profile_banner) is unable to be processed.";
                break;
            case HttpResponseCode.TOO_MANY_REQUESTS /*429*/:
                cause = "Returned in API v1.1 when a request cannot be served due to the application's rate limit having been exhausted for the resource. See Rate Limiting in API v1.1.(https://dev.twitter.com/docs/rate-limiting/1.1)";
                break;
            case 500:
                cause = "Something is broken. Please post to the group (https://dev.twitter.com/docs/support) so the Twitter team can investigate.";
                break;
            case 502:
                cause = "Twitter is down or being upgraded.";
                break;
            case 503:
                cause = "The Twitter servers are up, but overloaded with requests. Try again later.";
                break;
            case 504:
                cause = "The Twitter servers are up, but the request couldn't be serviced due to some failure within our stack. Try again later.";
                break;
            default:
                cause = "";
                break;
        }
        return statusCode2 + ":" + cause;
    }
}
