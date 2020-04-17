package twitter4j.internal.json;

import java.io.Serializable;
import twitter4j.Trend;
import twitter4j.internal.org.json.JSONObject;

final class TrendJSONImpl implements Trend, Serializable {
    private static final long serialVersionUID = 1925956704460743946L;
    private String name;
    private String query;
    private String url;

    TrendJSONImpl(JSONObject json, boolean storeJSON) {
        this.url = null;
        this.query = null;
        this.name = z_T4JInternalParseUtil.getRawString("name", json);
        this.url = z_T4JInternalParseUtil.getRawString("url", json);
        this.query = z_T4JInternalParseUtil.getRawString("query", json);
        if (storeJSON) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    TrendJSONImpl(JSONObject json) {
        this(json, false);
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return getURL();
    }

    public String getURL() {
        return this.url;
    }

    public String getQuery() {
        return this.query;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trend)) {
            return false;
        }
        Trend trend = (Trend) o;
        if (!this.name.equals(trend.getName())) {
            return false;
        }
        if (this.query == null ? trend.getQuery() != null : !this.query.equals(trend.getQuery())) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(trend.getURL())) {
                return true;
            }
        } else if (trend.getURL() == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int hashCode = this.name.hashCode() * 31;
        if (this.url != null) {
            i = this.url.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 31;
        if (this.query != null) {
            i2 = this.query.hashCode();
        }
        return i3 + i2;
    }

    public String toString() {
        return "TrendJSONImpl{name='" + this.name + '\'' + ", url='" + this.url + '\'' + ", query='" + this.query + '\'' + '}';
    }
}
