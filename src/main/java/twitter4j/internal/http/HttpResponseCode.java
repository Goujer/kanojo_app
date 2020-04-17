package twitter4j.internal.http;

public interface HttpResponseCode {
    public static final int BAD_GATEWAY = 502;
    public static final int BAD_REQUEST = 400;
    public static final int ENHANCE_YOUR_CLAIM = 420;
    public static final int FORBIDDEN = 403;
    public static final int FOUND = 302;
    public static final int GATEWAY_TIMEOUT = 504;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int MULTIPLE_CHOICES = 300;
    public static final int NOT_ACCEPTABLE = 406;
    public static final int NOT_FOUND = 404;
    public static final int NOT_MODIFIED = 304;
    public static final int OK = 200;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int TOO_MANY_REQUESTS = 429;
    public static final int UNAUTHORIZED = 401;
    public static final int UNPROCESSABLE_ENTITY = 422;
}
