package twitter4j.internal.json;

import twitter4j.Friendship;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

class FriendshipJSONImpl implements Friendship {
    private static final long serialVersionUID = 7724410837770709741L;
    private boolean followedBy = false;
    private boolean following = false;
    private final long id;
    private final String name;
    private final String screenName;

    FriendshipJSONImpl(JSONObject json) throws TwitterException {
        try {
            this.id = z_T4JInternalParseUtil.getLong("id", json);
            this.name = json.getString("name");
            this.screenName = json.getString("screen_name");
            JSONArray connections = json.getJSONArray("connections");
            for (int i = 0; i < connections.length(); i++) {
                String connection = connections.getString(i);
                if ("following".equals(connection)) {
                    this.following = true;
                } else if ("followed_by".equals(connection)) {
                    this.followedBy = true;
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    static ResponseList<Friendship> createFriendshipList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Friendship> friendshipList = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Friendship friendship = new FriendshipJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(friendship, json);
                }
                friendshipList.add(friendship);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(friendshipList, list);
            }
            return friendshipList;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public boolean isFollowing() {
        return this.following;
    }

    public boolean isFollowedBy() {
        return this.followedBy;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendshipJSONImpl that = (FriendshipJSONImpl) o;
        if (this.followedBy != that.followedBy) {
            return false;
        }
        if (this.following != that.following) {
            return false;
        }
        if (this.id != that.id) {
            return false;
        }
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (!this.screenName.equals(that.screenName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 1;
        int i5 = ((int) (this.id ^ (this.id >>> 32))) * 31;
        if (this.name != null) {
            i = this.name.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.screenName != null) {
            i2 = this.screenName.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.following) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (!this.followedBy) {
            i4 = 0;
        }
        return i8 + i4;
    }

    public String toString() {
        return "FriendshipJSONImpl{id=" + this.id + ", name='" + this.name + '\'' + ", screenName='" + this.screenName + '\'' + ", following=" + this.following + ", followedBy=" + this.followedBy + '}';
    }
}
