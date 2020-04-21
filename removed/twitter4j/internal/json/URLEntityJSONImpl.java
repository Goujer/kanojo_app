package twitter4j.internal.json;

import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class URLEntityJSONImpl extends EntityIndex implements URLEntity {
    private static final long serialVersionUID = -8948472760821379376L;
    private String displayURL;
    private String expandedURL;
    private String url;

    URLEntityJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    URLEntityJSONImpl(int start, int end, String url2, String expandedURL2, String displayURL2) {
        setStart(start);
        setEnd(end);
        this.url = url2;
        this.expandedURL = expandedURL2;
        this.displayURL = displayURL2;
    }

    URLEntityJSONImpl() {
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            setStart(indicesArray.getInt(0));
            setEnd(indicesArray.getInt(1));
            this.url = json.getString("url");
            if (!json.isNull("expanded_url")) {
                this.expandedURL = json.getString("expanded_url");
            } else {
                this.expandedURL = this.url;
            }
            if (!json.isNull("display_url")) {
                this.displayURL = json.getString("display_url");
            } else {
                this.displayURL = this.url;
            }
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public String getURL() {
        return this.url;
    }

    public String getExpandedURL() {
        return this.expandedURL;
    }

    public String getDisplayURL() {
        return this.displayURL;
    }

    public int getStart() {
        return super.getStart();
    }

    public int getEnd() {
        return super.getEnd();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        URLEntityJSONImpl that = (URLEntityJSONImpl) o;
        if (this.displayURL == null ? that.displayURL != null : !this.displayURL.equals(that.displayURL)) {
            return false;
        }
        if (this.expandedURL == null ? that.expandedURL != null : !this.expandedURL.equals(that.expandedURL)) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(that.url)) {
                return true;
            }
        } else if (that.url == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.url != null) {
            result = this.url.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.expandedURL != null) {
            i = this.expandedURL.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.displayURL != null) {
            i2 = this.displayURL.hashCode();
        }
        return i4 + i2;
    }

    public String toString() {
        return "URLEntityJSONImpl{url='" + this.url + '\'' + ", expandedURL='" + this.expandedURL + '\'' + ", displayURL='" + this.displayURL + '\'' + '}';
    }
}
