package twitter4j.internal.json;

import com.google.ads.AdActivity;
import java.io.Serializable;
import twitter4j.OEmbed;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public class OEmbedJSONImpl extends TwitterResponseImpl implements OEmbed, Serializable {
    private static final long serialVersionUID = -675438169712979958L;
    private String authorName;
    private String authorURL;
    private long cacheAge;
    private String html;
    private String url;
    private String version;
    private int width;

    public /* bridge */ /* synthetic */ int getAccessLevel() {
        return super.getAccessLevel();
    }

    public /* bridge */ /* synthetic */ RateLimitStatus getRateLimitStatus() {
        return super.getRateLimitStatus();
    }

    OEmbedJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    OEmbedJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            this.html = json.getString(AdActivity.HTML_PARAM);
            this.authorName = json.getString("author_name");
            this.url = json.getString("url");
            this.version = json.getString("version");
            this.cacheAge = json.getLong("cache_age");
            this.authorURL = json.getString("author_url");
            this.width = json.getInt("width");
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public String getHtml() {
        return this.html;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getURL() {
        return this.url;
    }

    public String getVersion() {
        return this.version;
    }

    public long getCacheAge() {
        return this.cacheAge;
    }

    public String getAuthorURL() {
        return this.authorURL;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OEmbedJSONImpl that = (OEmbedJSONImpl) o;
        if (this.cacheAge != that.cacheAge) {
            return false;
        }
        if (this.width != that.width) {
            return false;
        }
        if (this.authorName == null ? that.authorName != null : !this.authorName.equals(that.authorName)) {
            return false;
        }
        if (this.authorURL == null ? that.authorURL != null : !this.authorURL.equals(that.authorURL)) {
            return false;
        }
        if (this.html == null ? that.html != null : !this.html.equals(that.html)) {
            return false;
        }
        if (this.url == null ? that.url != null : !this.url.equals(that.url)) {
            return false;
        }
        if (this.version != null) {
            if (this.version.equals(that.version)) {
                return true;
            }
        } else if (that.version == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (this.html != null) {
            result = this.html.hashCode();
        } else {
            result = 0;
        }
        int i5 = result * 31;
        if (this.authorName != null) {
            i = this.authorName.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.url != null) {
            i2 = this.url.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.version != null) {
            i3 = this.version.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (((i7 + i3) * 31) + ((int) (this.cacheAge ^ (this.cacheAge >>> 32)))) * 31;
        if (this.authorURL != null) {
            i4 = this.authorURL.hashCode();
        }
        return ((i8 + i4) * 31) + this.width;
    }

    public String toString() {
        return "OEmbedJSONImpl{html='" + this.html + '\'' + ", authorName='" + this.authorName + '\'' + ", url='" + this.url + '\'' + ", version='" + this.version + '\'' + ", cacheAge=" + this.cacheAge + ", authorURL='" + this.authorURL + '\'' + ", width=" + this.width + '}';
    }
}
