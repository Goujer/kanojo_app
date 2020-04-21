package twitter4j.internal.json;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class QueryResultJSONImpl extends TwitterResponseImpl implements QueryResult, Serializable {
    static Method queryFactoryMethod = null;
    private static final long serialVersionUID = -6781654399437121238L;
    private double completedIn;
    private int count;
    private long maxId;
    private String nextResults;
    private String query;
    private String refreshUrl;
    private long sinceId;
    private List<Status> tweets;

    static {
        Method[] arr$ = Query.class.getDeclaredMethods();
        int len$ = arr$.length;
        int i$ = 0;
        while (true) {
            if (i$ >= len$) {
                break;
            }
            Method method = arr$[i$];
            if (method.getName().equals("createWithNextPageQuery")) {
                queryFactoryMethod = method;
                queryFactoryMethod.setAccessible(true);
                break;
            }
            i$++;
        }
        if (queryFactoryMethod == null) {
            throw new ExceptionInInitializerError(new NoSuchMethodException("twitter4j.Query.createWithNextPageQuery(java.lang.String)"));
        }
    }

    QueryResultJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        try {
            JSONObject searchMetaData = json.getJSONObject("search_metadata");
            this.completedIn = z_T4JInternalParseUtil.getDouble("completed_in", searchMetaData);
            this.count = z_T4JInternalParseUtil.getInt("count", searchMetaData);
            this.maxId = z_T4JInternalParseUtil.getLong("max_id", searchMetaData);
            this.nextResults = searchMetaData.has("next_results") ? searchMetaData.getString("next_results") : null;
            this.query = z_T4JInternalParseUtil.getURLDecodedString("query", searchMetaData);
            this.refreshUrl = z_T4JInternalParseUtil.getUnescapedString("refresh_url", searchMetaData);
            this.sinceId = z_T4JInternalParseUtil.getLong("since_id", searchMetaData);
            JSONArray array = json.getJSONArray("statuses");
            this.tweets = new ArrayList(array.length());
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            for (int i = 0; i < array.length(); i++) {
                this.tweets.add(new StatusJSONImpl(array.getJSONObject(i), conf));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    QueryResultJSONImpl(Query query2) {
        this.sinceId = query2.getSinceId();
        this.count = query2.getCount();
        this.tweets = new ArrayList(0);
    }

    public long getSinceId() {
        return this.sinceId;
    }

    public long getMaxId() {
        return this.maxId;
    }

    public String getRefreshUrl() {
        return getRefreshURL();
    }

    public String getRefreshURL() {
        return this.refreshUrl;
    }

    public int getCount() {
        return this.count;
    }

    public double getCompletedIn() {
        return this.completedIn;
    }

    public String getQuery() {
        return this.query;
    }

    public List<Status> getTweets() {
        return this.tweets;
    }

    public Query nextQuery() {
        if (this.nextResults == null) {
            return null;
        }
        try {
            return (Query) queryFactoryMethod.invoke((Object) null, new String[]{this.nextResults});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2);
        }
    }

    public boolean hasNext() {
        return this.nextResults != null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryResult that = (QueryResult) o;
        if (Double.compare(that.getCompletedIn(), this.completedIn) != 0) {
            return false;
        }
        if (this.maxId != that.getMaxId()) {
            return false;
        }
        if (this.count != that.getCount()) {
            return false;
        }
        if (this.sinceId != that.getSinceId()) {
            return false;
        }
        if (!this.query.equals(that.getQuery())) {
            return false;
        }
        if (this.refreshUrl == null ? that.getRefreshUrl() != null : !this.refreshUrl.equals(that.getRefreshUrl())) {
            return false;
        }
        if (this.tweets != null) {
            if (this.tweets.equals(that.getTweets())) {
                return true;
            }
        } else if (that.getTweets() == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int i3 = ((((int) (this.sinceId ^ (this.sinceId >>> 32))) * 31) + ((int) (this.maxId ^ (this.maxId >>> 32)))) * 31;
        if (this.refreshUrl != null) {
            i = this.refreshUrl.hashCode();
        } else {
            i = 0;
        }
        int result = ((i3 + i) * 31) + this.count;
        long temp = this.completedIn != 0.0d ? Double.doubleToLongBits(this.completedIn) : 0;
        int hashCode = ((((result * 31) + ((int) ((temp >>> 32) ^ temp))) * 31) + this.query.hashCode()) * 31;
        if (this.tweets != null) {
            i2 = this.tweets.hashCode();
        }
        return hashCode + i2;
    }

    public String toString() {
        return "QueryResultJSONImpl{sinceId=" + this.sinceId + ", maxId=" + this.maxId + ", refreshUrl='" + this.refreshUrl + '\'' + ", count=" + this.count + ", completedIn=" + this.completedIn + ", query='" + this.query + '\'' + ", tweets=" + this.tweets + '}';
    }
}
