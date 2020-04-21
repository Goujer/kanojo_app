package twitter4j.internal.json;

import java.util.ArrayList;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.internal.http.HttpResponse;

class ResponseListImpl<T> extends ArrayList<T> implements ResponseList<T> {
    private static final long serialVersionUID = 5646617841989265312L;
    private transient int accessLevel;
    private transient RateLimitStatus rateLimitStatus = null;

    ResponseListImpl(HttpResponse res) {
        init(res);
    }

    ResponseListImpl(int size, HttpResponse res) {
        super(size);
        init(res);
    }

    ResponseListImpl(RateLimitStatus rateLimitStatus2, int accessLevel2) {
        this.rateLimitStatus = rateLimitStatus2;
        this.accessLevel = accessLevel2;
    }

    private void init(HttpResponse res) {
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
