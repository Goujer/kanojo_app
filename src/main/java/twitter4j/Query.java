package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import twitter4j.internal.http.HttpParameter;

public final class Query implements Serializable {
    public static final String KILOMETERS = "km";
    public static final String MILES = "mi";
    public static final String MIXED = "mixed";
    public static final String POPULAR = "popular";
    public static final String RECENT = "recent";
    private static HttpParameter WITH_TWITTER_USER_ID = new HttpParameter("with_twitter_user_id", "true");
    private static final long serialVersionUID = -8108425822233599808L;
    private int count = -1;
    private String geocode = null;
    private String lang = null;
    private String locale = null;
    private long maxId = -1;
    private String nextPageQuery = null;
    private String query = null;
    private String resultType = null;
    private String since = null;
    private long sinceId = -1;
    private String until = null;

    public Query() {
    }

    public Query(String query2) {
        this.query = query2;
    }

    private static Query createWithNextPageQuery(String nextPageQuery2) {
        Query query2 = new Query();
        query2.nextPageQuery = nextPageQuery2;
        return query2;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query2) {
        this.query = query2;
    }

    public Query query(String query2) {
        setQuery(query2);
        return this;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang2) {
        this.lang = lang2;
    }

    public Query lang(String lang2) {
        setLang(lang2);
        return this;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale2) {
        this.locale = locale2;
    }

    public Query locale(String locale2) {
        setLocale(locale2);
        return this;
    }

    public long getMaxId() {
        return this.maxId;
    }

    public void setMaxId(long maxId2) {
        this.maxId = maxId2;
    }

    public Query maxId(long maxId2) {
        setMaxId(maxId2);
        return this;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count2) {
        this.count = count2;
    }

    public Query count(int count2) {
        setCount(count2);
        return this;
    }

    public String getSince() {
        return this.since;
    }

    public void setSince(String since2) {
        this.since = since2;
    }

    public Query since(String since2) {
        setSince(since2);
        return this;
    }

    public long getSinceId() {
        return this.sinceId;
    }

    public void setSinceId(long sinceId2) {
        this.sinceId = sinceId2;
    }

    public Query sinceId(long sinceId2) {
        setSinceId(sinceId2);
        return this;
    }

    public String getGeocode() {
        return this.geocode;
    }

    public void setGeoCode(GeoLocation location, double radius, String unit) {
        this.geocode = location.getLatitude() + "," + location.getLongitude() + "," + radius + unit;
    }

    public Query geoCode(GeoLocation location, double radius, String unit) {
        setGeoCode(location, radius, unit);
        return this;
    }

    public String getUntil() {
        return this.until;
    }

    public void setUntil(String until2) {
        this.until = until2;
    }

    public Query until(String until2) {
        setUntil(until2);
        return this;
    }

    public String getResultType() {
        return this.resultType;
    }

    public void setResultType(String resultType2) {
        this.resultType = resultType2;
    }

    public Query resultType(String resultType2) {
        setResultType(resultType2);
        return this;
    }

    /* access modifiers changed from: package-private */
    public HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>(12);
        appendParameter(AppLauncherConsts.REQUEST_PARAM_GENERAL, this.query, (List<HttpParameter>) params);
        appendParameter("lang", this.lang, (List<HttpParameter>) params);
        appendParameter("locale", this.locale, (List<HttpParameter>) params);
        appendParameter("max_id", this.maxId, (List<HttpParameter>) params);
        appendParameter("count", (long) this.count, (List<HttpParameter>) params);
        appendParameter("since", this.since, (List<HttpParameter>) params);
        appendParameter("since_id", this.sinceId, (List<HttpParameter>) params);
        appendParameter("geocode", this.geocode, (List<HttpParameter>) params);
        appendParameter("until", this.until, (List<HttpParameter>) params);
        appendParameter("result_type", this.resultType, (List<HttpParameter>) params);
        params.add(WITH_TWITTER_USER_ID);
        return (HttpParameter[]) params.toArray(new HttpParameter[params.size()]);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        if (0 <= value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    /* access modifiers changed from: package-private */
    public String nextPage() {
        return this.nextPageQuery;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Query query1 = (Query) o;
        if (this.maxId != query1.maxId) {
            return false;
        }
        if (this.count != query1.count) {
            return false;
        }
        if (this.sinceId != query1.sinceId) {
            return false;
        }
        if (this.geocode == null ? query1.geocode != null : !this.geocode.equals(query1.geocode)) {
            return false;
        }
        if (this.lang == null ? query1.lang != null : !this.lang.equals(query1.lang)) {
            return false;
        }
        if (this.locale == null ? query1.locale != null : !this.locale.equals(query1.locale)) {
            return false;
        }
        if (this.nextPageQuery == null ? query1.nextPageQuery != null : !this.nextPageQuery.equals(query1.nextPageQuery)) {
            return false;
        }
        if (this.query == null ? query1.query != null : !this.query.equals(query1.query)) {
            return false;
        }
        if (this.resultType == null ? query1.resultType != null : !this.resultType.equals(query1.resultType)) {
            return false;
        }
        if (this.since == null ? query1.since != null : !this.since.equals(query1.since)) {
            return false;
        }
        if (this.until != null) {
            if (this.until.equals(query1.until)) {
                return true;
            }
        } else if (query1.until == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = 0;
        if (this.query != null) {
            result = this.query.hashCode();
        } else {
            result = 0;
        }
        int i8 = result * 31;
        if (this.lang != null) {
            i = this.lang.hashCode();
        } else {
            i = 0;
        }
        int i9 = (i8 + i) * 31;
        if (this.locale != null) {
            i2 = this.locale.hashCode();
        } else {
            i2 = 0;
        }
        int i10 = (((((i9 + i2) * 31) + ((int) (this.maxId ^ (this.maxId >>> 32)))) * 31) + this.count) * 31;
        if (this.since != null) {
            i3 = this.since.hashCode();
        } else {
            i3 = 0;
        }
        int i11 = (((i10 + i3) * 31) + ((int) (this.sinceId ^ (this.sinceId >>> 32)))) * 31;
        if (this.geocode != null) {
            i4 = this.geocode.hashCode();
        } else {
            i4 = 0;
        }
        int i12 = (i11 + i4) * 31;
        if (this.until != null) {
            i5 = this.until.hashCode();
        } else {
            i5 = 0;
        }
        int i13 = (i12 + i5) * 31;
        if (this.resultType != null) {
            i6 = this.resultType.hashCode();
        } else {
            i6 = 0;
        }
        int i14 = (i13 + i6) * 31;
        if (this.nextPageQuery != null) {
            i7 = this.nextPageQuery.hashCode();
        }
        return i14 + i7;
    }

    public String toString() {
        return "Query{query='" + this.query + '\'' + ", lang='" + this.lang + '\'' + ", locale='" + this.locale + '\'' + ", maxId=" + this.maxId + ", count=" + this.count + ", since='" + this.since + '\'' + ", sinceId=" + this.sinceId + ", geocode='" + this.geocode + '\'' + ", until='" + this.until + '\'' + ", resultType='" + this.resultType + '\'' + ", nextPageQuery='" + this.nextPageQuery + '\'' + '}';
    }
}
