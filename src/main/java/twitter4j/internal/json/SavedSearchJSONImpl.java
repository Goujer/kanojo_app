package twitter4j.internal.json;

import java.util.Date;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class SavedSearchJSONImpl extends TwitterResponseImpl implements SavedSearch {
    private static final long serialVersionUID = 3083819860391598212L;
    private Date createdAt;
    private int id;
    private String name;
    private int position;
    private String query;

    SavedSearchJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    SavedSearchJSONImpl(JSONObject savedSearch) throws TwitterException {
        init(savedSearch);
    }

    static ResponseList<SavedSearch> createSavedSearchList(HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONArray json = res.asJSONArray();
        try {
            ResponseList<SavedSearch> savedSearches = new ResponseListImpl<>(json.length(), res);
            for (int i = 0; i < json.length(); i++) {
                JSONObject savedSearchesJSON = json.getJSONObject(i);
                SavedSearch savedSearch = new SavedSearchJSONImpl(savedSearchesJSON);
                savedSearches.add(savedSearch);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(savedSearch, savedSearchesJSON);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(savedSearches, json);
            }
            return savedSearches;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + res.asString(), (Throwable) jsone);
        }
    }

    private void init(JSONObject savedSearch) throws TwitterException {
        this.createdAt = z_T4JInternalParseUtil.getDate("created_at", savedSearch, "EEE MMM dd HH:mm:ss z yyyy");
        this.query = z_T4JInternalParseUtil.getUnescapedString("query", savedSearch);
        this.position = z_T4JInternalParseUtil.getInt("position", savedSearch);
        this.name = z_T4JInternalParseUtil.getUnescapedString("name", savedSearch);
        this.id = z_T4JInternalParseUtil.getInt("id", savedSearch);
    }

    public int compareTo(SavedSearch that) {
        return this.id - that.getId();
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public String getQuery() {
        return this.query;
    }

    public int getPosition() {
        return this.position;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SavedSearch)) {
            return false;
        }
        if (this.id != ((SavedSearch) o).getId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((((this.createdAt.hashCode() * 31) + this.query.hashCode()) * 31) + this.position) * 31) + this.name.hashCode()) * 31) + this.id;
    }

    public String toString() {
        return "SavedSearchJSONImpl{createdAt=" + this.createdAt + ", query='" + this.query + '\'' + ", position=" + this.position + ", name='" + this.name + '\'' + ", id=" + this.id + '}';
    }
}
