package twitter4j.internal.json;

import java.io.Serializable;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterResponse;
import twitter4j.internal.http.HttpResponse;

abstract class TwitterResponseImpl implements TwitterResponse, Serializable {
    private static final long serialVersionUID = -7284708239736552059L;
    private transient int accessLevel;
    private transient RateLimitStatus rateLimitStatus;

    public TwitterResponseImpl() {
        this.rateLimitStatus = null;
        this.accessLevel = 0;
    }

    public TwitterResponseImpl(HttpResponse res) {
        this.rateLimitStatus = null;
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        this.accessLevel = z_T4JInternalParseUtil.toAccessLevel(res);
    }

    public RateLimitStatus getRateLimitStatus() {
        return this.rateLimitStatus;
    }

    public int getAccessLevel() {
        return this.accessLevel;
    }
}
