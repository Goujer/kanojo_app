package twitter4j.internal.json;

import twitter4j.PagableResponseList;
import twitter4j.RateLimitStatus;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONObject;

class PagableResponseListImpl<T> extends ResponseListImpl implements PagableResponseList {
    private static final long serialVersionUID = 1531950333538983361L;
    private final long nextCursor;
    private final long previousCursor;

    PagableResponseListImpl(RateLimitStatus rateLimitStatus, int accessLevel) {
        super(rateLimitStatus, accessLevel);
        this.previousCursor = 0;
        this.nextCursor = 0;
    }

    PagableResponseListImpl(int size, JSONObject json, HttpResponse res) {
        super(size, res);
        this.previousCursor = z_T4JInternalParseUtil.getLong("previous_cursor", json);
        this.nextCursor = z_T4JInternalParseUtil.getLong("next_cursor", json);
    }

    public boolean hasPrevious() {
        return 0 != this.previousCursor;
    }

    public long getPreviousCursor() {
        return this.previousCursor;
    }

    public boolean hasNext() {
        return 0 != this.nextCursor;
    }

    public long getNextCursor() {
        return this.nextCursor;
    }
}
