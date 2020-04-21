package twitter4j.internal.json;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

class UserListJSONImpl extends TwitterResponseImpl implements UserList, Serializable {
    private static final long serialVersionUID = -6345893237975349030L;
    private String description;
    private boolean following;
    private String fullName;
    private int id;
    private int memberCount;
    private boolean mode;
    private String name;
    private String slug;
    private int subscriberCount;
    private String uri;
    private User user;

    UserListJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
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

    UserListJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        this.id = z_T4JInternalParseUtil.getInt("id", json);
        this.name = z_T4JInternalParseUtil.getRawString("name", json);
        this.fullName = z_T4JInternalParseUtil.getRawString("full_name", json);
        this.slug = z_T4JInternalParseUtil.getRawString("slug", json);
        this.description = z_T4JInternalParseUtil.getRawString("description", json);
        this.subscriberCount = z_T4JInternalParseUtil.getInt("subscriber_count", json);
        this.memberCount = z_T4JInternalParseUtil.getInt("member_count", json);
        this.uri = z_T4JInternalParseUtil.getRawString("uri", json);
        this.mode = "public".equals(z_T4JInternalParseUtil.getRawString("mode", json));
        this.following = z_T4JInternalParseUtil.getBoolean("following", json);
        try {
            if (!json.isNull(PropertyConfiguration.USER)) {
                this.user = new UserJSONImpl(json.getJSONObject(PropertyConfiguration.USER));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    public int compareTo(UserList that) {
        return this.id - that.getId();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getSlug() {
        return this.slug;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSubscriberCount() {
        return this.subscriberCount;
    }

    public int getMemberCount() {
        return this.memberCount;
    }

    public URI getURI() {
        try {
            return new URI(this.uri);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public boolean isPublic() {
        return this.mode;
    }

    public boolean isFollowing() {
        return this.following;
    }

    public User getUser() {
        return this.user;
    }

    static PagableResponseList<UserList> createPagableUserListList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("lists");
            int size = list.length();
            PagableResponseList<UserList> users = new PagableResponseListImpl<>(size, json, res);
            for (int i = 0; i < size; i++) {
                JSONObject userListJson = list.getJSONObject(i);
                UserList userList = new UserListJSONImpl(userListJson);
                users.add(userList);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(userList, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, json);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    static ResponseList<UserList> createUserListList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<UserList> users = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject userListJson = list.getJSONObject(i);
                UserList userList = new UserListJSONImpl(userListJson);
                users.add(userList);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(userList, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, list);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserList) || ((UserList) obj).getId() != this.id) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "UserListJSONImpl{id=" + this.id + ", name='" + this.name + '\'' + ", fullName='" + this.fullName + '\'' + ", slug='" + this.slug + '\'' + ", description='" + this.description + '\'' + ", subscriberCount=" + this.subscriberCount + ", memberCount=" + this.memberCount + ", uri='" + this.uri + '\'' + ", mode=" + this.mode + ", user=" + this.user + ", following=" + this.following + '}';
    }
}
