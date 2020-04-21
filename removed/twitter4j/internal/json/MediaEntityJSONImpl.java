package twitter4j.internal.json;

import java.util.HashMap;
import java.util.Map;
import twitter4j.MediaEntity;
import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public class MediaEntityJSONImpl extends EntityIndex implements MediaEntity {
    private static final long serialVersionUID = 224487082931268487L;
    private String displayURL;
    private String expandedURL;
    private long id;
    private String mediaURL;
    private String mediaURLHttps;
    private Map<Integer, MediaEntity.Size> sizes;
    private String type;
    private String url;

    public /* bridge */ /* synthetic */ int compareTo(EntityIndex x0) {
        return super.compareTo(x0);
    }

    MediaEntityJSONImpl(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            setStart(indicesArray.getInt(0));
            setEnd(indicesArray.getInt(1));
            this.id = z_T4JInternalParseUtil.getLong("id", json);
            this.url = json.getString("url");
            this.expandedURL = json.getString("expanded_url");
            this.mediaURL = json.getString("media_url");
            this.mediaURLHttps = json.getString("media_url_https");
            this.displayURL = json.getString("display_url");
            JSONObject sizes2 = json.getJSONObject("sizes");
            this.sizes = new HashMap(4);
            addMediaEntitySizeIfNotNull(this.sizes, sizes2, MediaEntity.Size.LARGE, "large");
            addMediaEntitySizeIfNotNull(this.sizes, sizes2, MediaEntity.Size.MEDIUM, "medium");
            addMediaEntitySizeIfNotNull(this.sizes, sizes2, MediaEntity.Size.SMALL, "small");
            addMediaEntitySizeIfNotNull(this.sizes, sizes2, MediaEntity.Size.THUMB, "thumb");
            if (!json.isNull("type")) {
                this.type = json.getString("type");
            }
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    private void addMediaEntitySizeIfNotNull(Map<Integer, MediaEntity.Size> sizes2, JSONObject sizesJSON, Integer size, String key) throws JSONException {
        if (!sizesJSON.isNull(key)) {
            sizes2.put(size, new Size(sizesJSON.getJSONObject(key)));
        }
    }

    MediaEntityJSONImpl() {
    }

    public long getId() {
        return this.id;
    }

    public String getMediaURL() {
        return this.mediaURL;
    }

    public String getMediaURLHttps() {
        return this.mediaURLHttps;
    }

    public String getURL() {
        return this.url;
    }

    public String getDisplayURL() {
        return this.displayURL;
    }

    public String getExpandedURL() {
        return this.expandedURL;
    }

    public Map<Integer, MediaEntity.Size> getSizes() {
        return this.sizes;
    }

    public String getType() {
        return this.type;
    }

    public int getStart() {
        return super.getStart();
    }

    public int getEnd() {
        return super.getEnd();
    }

    static class Size implements MediaEntity.Size {
        private static final long serialVersionUID = 8681853416159361581L;
        int height;
        int resize;
        int width;

        Size(JSONObject json) throws JSONException {
            this.width = json.getInt("w");
            this.height = json.getInt("h");
            this.resize = "fit".equals(json.getString("resize")) ? 100 : 101;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public int getResize() {
            return this.resize;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Size)) {
                return false;
            }
            Size size = (Size) o;
            if (this.height != size.height) {
                return false;
            }
            if (this.resize != size.resize) {
                return false;
            }
            if (this.width != size.width) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((this.width * 31) + this.height) * 31) + this.resize;
        }

        public String toString() {
            return "Size{width=" + this.width + ", height=" + this.height + ", resize=" + this.resize + '}';
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaEntityJSONImpl)) {
            return false;
        }
        if (this.id != ((MediaEntityJSONImpl) o).id) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (int) (this.id ^ (this.id >>> 32));
    }

    public String toString() {
        return "MediaEntityJSONImpl{id=" + this.id + ", url=" + this.url + ", mediaURL=" + this.mediaURL + ", mediaURLHttps=" + this.mediaURLHttps + ", expandedURL=" + this.expandedURL + ", displayURL='" + this.displayURL + '\'' + ", sizes=" + this.sizes + ", type=" + this.type + '}';
    }
}
