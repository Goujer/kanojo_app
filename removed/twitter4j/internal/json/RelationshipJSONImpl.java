package twitter4j.internal.json;

import java.io.Serializable;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

class RelationshipJSONImpl extends TwitterResponseImpl implements Relationship, Serializable {
    private static final long serialVersionUID = 7725021608907856360L;
    private final boolean sourceBlockingTarget;
    private final boolean sourceFollowedByTarget;
    private final boolean sourceFollowingTarget;
    private final boolean sourceNotificationsEnabled;
    private final long sourceUserId;
    private final String sourceUserScreenName;
    private final long targetUserId;
    private final String targetUserScreenName;
    private boolean wantRetweets;

    RelationshipJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
        }
    }

    RelationshipJSONImpl(JSONObject json) throws TwitterException {
        this((HttpResponse) null, json);
    }

    RelationshipJSONImpl(HttpResponse res, JSONObject json) throws TwitterException {
        super(res);
        try {
            JSONObject relationship = json.getJSONObject("relationship");
            JSONObject sourceJson = relationship.getJSONObject("source");
            JSONObject targetJson = relationship.getJSONObject("target");
            this.sourceUserId = z_T4JInternalParseUtil.getLong("id", sourceJson);
            this.targetUserId = z_T4JInternalParseUtil.getLong("id", targetJson);
            this.sourceUserScreenName = z_T4JInternalParseUtil.getUnescapedString("screen_name", sourceJson);
            this.targetUserScreenName = z_T4JInternalParseUtil.getUnescapedString("screen_name", targetJson);
            this.sourceBlockingTarget = z_T4JInternalParseUtil.getBoolean("blocking", sourceJson);
            this.sourceFollowingTarget = z_T4JInternalParseUtil.getBoolean("following", sourceJson);
            this.sourceFollowedByTarget = z_T4JInternalParseUtil.getBoolean("followed_by", sourceJson);
            this.sourceNotificationsEnabled = z_T4JInternalParseUtil.getBoolean("notifications_enabled", sourceJson);
            this.wantRetweets = z_T4JInternalParseUtil.getBoolean("want_retweets", sourceJson);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    static ResponseList<Relationship> createRelationshipList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Relationship> relationships = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Relationship relationship = new RelationshipJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(relationship, json);
                }
                relationships.add(relationship);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(relationships, list);
            }
            return relationships;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public long getSourceUserId() {
        return this.sourceUserId;
    }

    public long getTargetUserId() {
        return this.targetUserId;
    }

    public boolean isSourceBlockingTarget() {
        return this.sourceBlockingTarget;
    }

    public String getSourceUserScreenName() {
        return this.sourceUserScreenName;
    }

    public String getTargetUserScreenName() {
        return this.targetUserScreenName;
    }

    public boolean isSourceFollowingTarget() {
        return this.sourceFollowingTarget;
    }

    public boolean isTargetFollowingSource() {
        return this.sourceFollowedByTarget;
    }

    public boolean isSourceFollowedByTarget() {
        return this.sourceFollowedByTarget;
    }

    public boolean isTargetFollowedBySource() {
        return this.sourceFollowingTarget;
    }

    public boolean isSourceNotificationsEnabled() {
        return this.sourceNotificationsEnabled;
    }

    public boolean isSourceWantRetweets() {
        return this.wantRetweets;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Relationship)) {
            return false;
        }
        Relationship that = (Relationship) o;
        if (this.sourceUserId != that.getSourceUserId()) {
            return false;
        }
        if (this.targetUserId != that.getTargetUserId()) {
            return false;
        }
        if (!this.sourceUserScreenName.equals(that.getSourceUserScreenName())) {
            return false;
        }
        if (!this.targetUserScreenName.equals(that.getTargetUserScreenName())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 1;
        int i5 = 0;
        int hashCode = ((((int) (this.targetUserId ^ (this.targetUserId >>> 32))) * 31) + (this.targetUserScreenName != null ? this.targetUserScreenName.hashCode() : 0)) * 31;
        if (this.sourceBlockingTarget) {
            i = 1;
        } else {
            i = 0;
        }
        int i6 = (hashCode + i) * 31;
        if (this.sourceNotificationsEnabled) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.sourceFollowingTarget) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (!this.sourceFollowedByTarget) {
            i4 = 0;
        }
        int i9 = (((i8 + i4) * 31) + ((int) (this.sourceUserId ^ (this.sourceUserId >>> 32)))) * 31;
        if (this.sourceUserScreenName != null) {
            i5 = this.sourceUserScreenName.hashCode();
        }
        return i9 + i5;
    }

    public String toString() {
        return "RelationshipJSONImpl{sourceUserId=" + this.sourceUserId + ", targetUserId=" + this.targetUserId + ", sourceUserScreenName='" + this.sourceUserScreenName + '\'' + ", targetUserScreenName='" + this.targetUserScreenName + '\'' + ", sourceFollowingTarget=" + this.sourceFollowingTarget + ", sourceFollowedByTarget=" + this.sourceFollowedByTarget + ", sourceNotificationsEnabled=" + this.sourceNotificationsEnabled + '}';
    }
}
