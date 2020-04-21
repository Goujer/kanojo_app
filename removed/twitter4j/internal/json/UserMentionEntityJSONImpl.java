package twitter4j.internal.json;

import twitter4j.TwitterException;
import twitter4j.UserMentionEntity;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

class UserMentionEntityJSONImpl extends EntityIndex implements UserMentionEntity {
    private static final long serialVersionUID = 6580431141350059702L;
    private long id;
    private String name;
    private String screenName;

    UserMentionEntityJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    UserMentionEntityJSONImpl(int start, int end, String name2, String screenName2, long id2) {
        setStart(start);
        setEnd(end);
        this.name = name2;
        this.screenName = screenName2;
        this.id = id2;
    }

    UserMentionEntityJSONImpl() {
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            setStart(indicesArray.getInt(0));
            setEnd(indicesArray.getInt(1));
            if (!json.isNull("name")) {
                this.name = json.getString("name");
            }
            if (!json.isNull("screen_name")) {
                this.screenName = json.getString("screen_name");
            }
            this.id = z_T4JInternalParseUtil.getLong("id", json);
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public long getId() {
        return this.id;
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
        UserMentionEntityJSONImpl that = (UserMentionEntityJSONImpl) o;
        if (this.id != that.id) {
            return false;
        }
        if (this.name == null ? that.name != null : !this.name.equals(that.name)) {
            return false;
        }
        if (this.screenName != null) {
            if (this.screenName.equals(that.screenName)) {
                return true;
            }
        } else if (that.screenName == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.name != null) {
            result = this.name.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.screenName != null) {
            i = this.screenName.hashCode();
        }
        return ((i2 + i) * 31) + ((int) (this.id ^ (this.id >>> 32)));
    }

    public String toString() {
        return "UserMentionEntityJSONImpl{name='" + this.name + '\'' + ", screenName='" + this.screenName + '\'' + ", id=" + this.id + '}';
    }
}
