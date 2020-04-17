package twitter4j.internal.json;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class RateLimitStatusJSONImpl implements RateLimitStatus, Serializable {
    private static final long serialVersionUID = 1625565652687304084L;
    private int limit;
    private int remaining;
    private int resetTimeInSeconds;
    private int secondsUntilReset;

    static Map<String, RateLimitStatus> createRateLimitStatuses(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = res.asJSONObject();
        Map<String, RateLimitStatus> map = createRateLimitStatuses(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(map, json);
        }
        return map;
    }

    static Map<String, RateLimitStatus> createRateLimitStatuses(JSONObject json) throws TwitterException {
        Map<String, RateLimitStatus> map = new HashMap<>();
        try {
            JSONObject resources = json.getJSONObject("resources");
            Iterator resourceKeys = resources.keys();
            while (resourceKeys.hasNext()) {
                JSONObject resource = resources.getJSONObject((String) resourceKeys.next());
                Iterator endpointKeys = resource.keys();
                while (endpointKeys.hasNext()) {
                    String endpoint = (String) endpointKeys.next();
                    map.put(endpoint, new RateLimitStatusJSONImpl(resource.getJSONObject(endpoint)));
                }
            }
            return Collections.unmodifiableMap(map);
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    private RateLimitStatusJSONImpl(int limit2, int remaining2, int resetTimeInSeconds2) {
        this.limit = limit2;
        this.remaining = remaining2;
        this.resetTimeInSeconds = resetTimeInSeconds2;
        this.secondsUntilReset = (int) (((((long) resetTimeInSeconds2) * 1000) - System.currentTimeMillis()) / 1000);
    }

    RateLimitStatusJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    /* access modifiers changed from: package-private */
    public void init(JSONObject json) throws TwitterException {
        this.limit = z_T4JInternalParseUtil.getInt("limit", json);
        this.remaining = z_T4JInternalParseUtil.getInt("remaining", json);
        this.resetTimeInSeconds = z_T4JInternalParseUtil.getInt("reset", json);
        this.secondsUntilReset = (int) (((((long) this.resetTimeInSeconds) * 1000) - System.currentTimeMillis()) / 1000);
    }

    static RateLimitStatus createFromResponseHeader(HttpResponse res) {
        String strLimit;
        if (res == null || (strLimit = res.getResponseHeader("X-Rate-Limit-Limit")) == null) {
            return null;
        }
        int limit2 = Integer.parseInt(strLimit);
        String remaining2 = res.getResponseHeader("X-Rate-Limit-Remaining");
        if (remaining2 == null) {
            return null;
        }
        int remainingHits = Integer.parseInt(remaining2);
        String reset = res.getResponseHeader("X-Rate-Limit-Reset");
        if (reset != null) {
            return new RateLimitStatusJSONImpl(limit2, remainingHits, (int) Long.parseLong(reset));
        }
        return null;
    }

    public int getRemaining() {
        return this.remaining;
    }

    public int getRemainingHits() {
        return getRemaining();
    }

    public int getLimit() {
        return this.limit;
    }

    public int getResetTimeInSeconds() {
        return this.resetTimeInSeconds;
    }

    public int getSecondsUntilReset() {
        return this.secondsUntilReset;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RateLimitStatusJSONImpl that = (RateLimitStatusJSONImpl) o;
        if (this.limit != that.limit) {
            return false;
        }
        if (this.remaining != that.remaining) {
            return false;
        }
        if (this.resetTimeInSeconds != that.resetTimeInSeconds) {
            return false;
        }
        if (this.secondsUntilReset != that.secondsUntilReset) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((this.remaining * 31) + this.limit) * 31) + this.resetTimeInSeconds) * 31) + this.secondsUntilReset;
    }

    public String toString() {
        return "RateLimitStatusJSONImpl{remaining=" + this.remaining + ", limit=" + this.limit + ", resetTimeInSeconds=" + this.resetTimeInSeconds + ", secondsUntilReset=" + this.secondsUntilReset + '}';
    }
}
