package twitter4j.internal.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import twitter4j.MediaEntity;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.MediaEntityJSONImpl;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

class TwitterAPIConfigurationJSONImpl extends TwitterResponseImpl implements TwitterAPIConfiguration {
    private static final long serialVersionUID = 5786291660087491465L;
    private int charactersReservedPerMedia;
    private int maxMediaPerUpload;
    private String[] nonUsernamePaths;
    private int photoSizeLimit;
    private Map<Integer, MediaEntity.Size> photoSizes;
    private int shortURLLength;
    private int shortURLLengthHttps;

    TwitterAPIConfigurationJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject medium;
        try {
            JSONObject json = res.asJSONObject();
            this.photoSizeLimit = z_T4JInternalParseUtil.getInt("photo_size_limit", json);
            this.shortURLLength = z_T4JInternalParseUtil.getInt("short_url_length", json);
            this.shortURLLengthHttps = z_T4JInternalParseUtil.getInt("short_url_length_https", json);
            this.charactersReservedPerMedia = z_T4JInternalParseUtil.getInt("characters_reserved_per_media", json);
            JSONObject sizes = json.getJSONObject("photo_sizes");
            this.photoSizes = new HashMap(4);
            this.photoSizes.put(MediaEntity.Size.LARGE, new MediaEntityJSONImpl.Size(sizes.getJSONObject("large")));
            if (sizes.isNull("med")) {
                medium = sizes.getJSONObject("medium");
            } else {
                medium = sizes.getJSONObject("med");
            }
            this.photoSizes.put(MediaEntity.Size.MEDIUM, new MediaEntityJSONImpl.Size(medium));
            this.photoSizes.put(MediaEntity.Size.SMALL, new MediaEntityJSONImpl.Size(sizes.getJSONObject("small")));
            this.photoSizes.put(MediaEntity.Size.THUMB, new MediaEntityJSONImpl.Size(sizes.getJSONObject("thumb")));
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
                DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
            }
            JSONArray nonUsernamePathsJSONArray = json.getJSONArray("non_username_paths");
            this.nonUsernamePaths = new String[nonUsernamePathsJSONArray.length()];
            for (int i = 0; i < nonUsernamePathsJSONArray.length(); i++) {
                this.nonUsernamePaths[i] = nonUsernamePathsJSONArray.getString(i);
            }
            this.maxMediaPerUpload = z_T4JInternalParseUtil.getInt("max_media_per_upload", json);
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public int getPhotoSizeLimit() {
        return this.photoSizeLimit;
    }

    public int getShortURLLength() {
        return this.shortURLLength;
    }

    public int getShortURLLengthHttps() {
        return this.shortURLLengthHttps;
    }

    public int getCharactersReservedPerMedia() {
        return this.charactersReservedPerMedia;
    }

    public Map<Integer, MediaEntity.Size> getPhotoSizes() {
        return this.photoSizes;
    }

    public String[] getNonUsernamePaths() {
        return this.nonUsernamePaths;
    }

    public int getMaxMediaPerUpload() {
        return this.maxMediaPerUpload;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterAPIConfigurationJSONImpl)) {
            return false;
        }
        TwitterAPIConfigurationJSONImpl that = (TwitterAPIConfigurationJSONImpl) o;
        if (this.charactersReservedPerMedia != that.charactersReservedPerMedia) {
            return false;
        }
        if (this.maxMediaPerUpload != that.maxMediaPerUpload) {
            return false;
        }
        if (this.photoSizeLimit != that.photoSizeLimit) {
            return false;
        }
        if (this.shortURLLength != that.shortURLLength) {
            return false;
        }
        if (this.shortURLLengthHttps != that.shortURLLengthHttps) {
            return false;
        }
        if (!Arrays.equals(this.nonUsernamePaths, that.nonUsernamePaths)) {
            return false;
        }
        if (this.photoSizes != null) {
            if (this.photoSizes.equals(that.photoSizes)) {
                return true;
            }
        } else if (that.photoSizes == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int i3 = ((((((this.photoSizeLimit * 31) + this.shortURLLength) * 31) + this.shortURLLengthHttps) * 31) + this.charactersReservedPerMedia) * 31;
        if (this.photoSizes != null) {
            i = this.photoSizes.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.nonUsernamePaths != null) {
            i2 = Arrays.hashCode(this.nonUsernamePaths);
        }
        return ((i4 + i2) * 31) + this.maxMediaPerUpload;
    }

    public String toString() {
        return "TwitterAPIConfigurationJSONImpl{photoSizeLimit=" + this.photoSizeLimit + ", shortURLLength=" + this.shortURLLength + ", shortURLLengthHttps=" + this.shortURLLengthHttps + ", charactersReservedPerMedia=" + this.charactersReservedPerMedia + ", photoSizes=" + this.photoSizes + ", nonUsernamePaths=" + (this.nonUsernamePaths == null ? null : Arrays.asList(this.nonUsernamePaths)) + ", maxMediaPerUpload=" + this.maxMediaPerUpload + '}';
    }
}
