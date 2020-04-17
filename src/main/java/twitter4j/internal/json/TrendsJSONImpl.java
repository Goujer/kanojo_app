package twitter4j.internal.json;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class TrendsJSONImpl extends TwitterResponseImpl implements Trends, Serializable {
    private static final long serialVersionUID = -7151479143843312309L;
    private Date asOf;
    private Location location;
    private Date trendAt;
    private Trend[] trends;

    public int compareTo(Trends that) {
        return this.trendAt.compareTo(that.getTrendAt());
    }

    TrendsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        init(res.asString(), conf.isJSONStoreEnabled());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asString());
        }
    }

    TrendsJSONImpl(String jsonStr) throws TwitterException {
        this(jsonStr, false);
    }

    TrendsJSONImpl(String jsonStr, boolean storeJSON) throws TwitterException {
        init(jsonStr, storeJSON);
    }

    /* access modifiers changed from: package-private */
    public void init(String jsonStr, boolean storeJSON) throws TwitterException {
        JSONObject json;
        try {
            if (jsonStr.startsWith("[")) {
                JSONArray array = new JSONArray(jsonStr);
                if (array.length() > 0) {
                    json = array.getJSONObject(0);
                } else {
                    throw new TwitterException("No trends found on the specified woeid");
                }
            } else {
                json = new JSONObject(jsonStr);
            }
            this.asOf = z_T4JInternalParseUtil.parseTrendsDate(json.getString("as_of"));
            this.location = extractLocation(json, storeJSON);
            JSONArray array2 = json.getJSONArray("trends");
            this.trendAt = this.asOf;
            this.trends = jsonArrayToTrendArray(array2, storeJSON);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage(), (Throwable) jsone);
        }
    }

    TrendsJSONImpl(Date asOf2, Location location2, Date trendAt2, Trend[] trends2) {
        this.asOf = asOf2;
        this.location = location2;
        this.trendAt = trendAt2;
        this.trends = trends2;
    }

    static ResponseList<Trends> createTrendsList(HttpResponse res, boolean storeJSON) throws TwitterException {
        JSONObject json = res.asJSONObject();
        try {
            Date asOf2 = z_T4JInternalParseUtil.parseTrendsDate(json.getString("as_of"));
            JSONObject trendsJson = json.getJSONObject("trends");
            Location location2 = extractLocation(json, storeJSON);
            ResponseList<Trends> trends2 = new ResponseListImpl<>(trendsJson.length(), res);
            Iterator ite = trendsJson.keys();
            while (ite.hasNext()) {
                String key = (String) ite.next();
                Trend[] trendsArray = jsonArrayToTrendArray(trendsJson.getJSONArray(key), storeJSON);
                if (key.length() == 19) {
                    trends2.add(new TrendsJSONImpl(asOf2, location2, z_T4JInternalParseUtil.getDate(key, "yyyy-MM-dd HH:mm:ss"), trendsArray));
                } else if (key.length() == 16) {
                    trends2.add(new TrendsJSONImpl(asOf2, location2, z_T4JInternalParseUtil.getDate(key, "yyyy-MM-dd HH:mm"), trendsArray));
                } else if (key.length() == 10) {
                    trends2.add(new TrendsJSONImpl(asOf2, location2, z_T4JInternalParseUtil.getDate(key, "yyyy-MM-dd"), trendsArray));
                }
            }
            Collections.sort(trends2);
            return trends2;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + res.asString(), (Throwable) jsone);
        }
    }

    private static Location extractLocation(JSONObject json, boolean storeJSON) throws TwitterException {
        if (json.isNull("locations")) {
            return null;
        }
        try {
            ResponseList<Location> locations = LocationJSONImpl.createLocationList(json.getJSONArray("locations"), storeJSON);
            if (locations.size() != 0) {
                return (Location) locations.get(0);
            }
            return null;
        } catch (JSONException e) {
            throw new AssertionError("locations can't be null");
        }
    }

    private static Trend[] jsonArrayToTrendArray(JSONArray array, boolean storeJSON) throws JSONException {
        Trend[] trends2 = new Trend[array.length()];
        for (int i = 0; i < array.length(); i++) {
            trends2[i] = new TrendJSONImpl(array.getJSONObject(i), storeJSON);
        }
        return trends2;
    }

    public Trend[] getTrends() {
        return this.trends;
    }

    public Location getLocation() {
        return this.location;
    }

    public Date getAsOf() {
        return this.asOf;
    }

    public Date getTrendAt() {
        return this.trendAt;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trends)) {
            return false;
        }
        Trends trends1 = (Trends) o;
        if (this.asOf == null ? trends1.getAsOf() != null : !this.asOf.equals(trends1.getAsOf())) {
            return false;
        }
        if (this.trendAt == null ? trends1.getTrendAt() != null : !this.trendAt.equals(trends1.getTrendAt())) {
            return false;
        }
        if (!Arrays.equals(this.trends, trends1.getTrends())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.asOf != null) {
            result = this.asOf.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.trendAt != null) {
            i = this.trendAt.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.trends != null) {
            i2 = Arrays.hashCode(this.trends);
        }
        return i4 + i2;
    }

    public String toString() {
        return "TrendsJSONImpl{asOf=" + this.asOf + ", trendAt=" + this.trendAt + ", trends=" + (this.trends == null ? null : Arrays.asList(this.trends)) + '}';
    }
}
