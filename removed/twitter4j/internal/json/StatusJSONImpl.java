package twitter4j.internal.json;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class StatusJSONImpl extends TwitterResponseImpl implements Status, Serializable {
    private static final Logger logger = Logger.getLogger(StatusJSONImpl.class);
    private static final long serialVersionUID = 7548618898682727465L;
    private long[] contributorsIDs;
    private Date createdAt;
    private long currentUserRetweetId = -1;
    private GeoLocation geoLocation = null;
    private HashtagEntity[] hashtagEntities;
    private long id;
    private String inReplyToScreenName;
    private long inReplyToStatusId;
    private long inReplyToUserId;
    private boolean isFavorited;
    private boolean isPossiblySensitive;
    private boolean isTruncated;
    private MediaEntity[] mediaEntities;
    private Place place = null;
    private long retweetCount;
    private Status retweetedStatus;
    private String source;
    private String text;
    private URLEntity[] urlEntities;
    private User user = null;
    private UserMentionEntity[] userMentionEntities;

    StatusJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    StatusJSONImpl(JSONObject json, Configuration conf) throws TwitterException {
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    StatusJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    StatusJSONImpl() {
    }

    private void init(JSONObject json) throws TwitterException {
        this.id = z_T4JInternalParseUtil.getLong("id", json);
        this.source = z_T4JInternalParseUtil.getUnescapedString("source", json);
        this.createdAt = z_T4JInternalParseUtil.getDate("created_at", json);
        this.isTruncated = z_T4JInternalParseUtil.getBoolean("truncated", json);
        this.inReplyToStatusId = z_T4JInternalParseUtil.getLong("in_reply_to_status_id", json);
        this.inReplyToUserId = z_T4JInternalParseUtil.getLong("in_reply_to_user_id", json);
        this.isFavorited = z_T4JInternalParseUtil.getBoolean("favorited", json);
        this.inReplyToScreenName = z_T4JInternalParseUtil.getUnescapedString("in_reply_to_screen_name", json);
        this.retweetCount = z_T4JInternalParseUtil.getLong("retweet_count", json);
        this.isPossiblySensitive = z_T4JInternalParseUtil.getBoolean("possibly_sensitive", json);
        try {
            if (!json.isNull(PropertyConfiguration.USER)) {
                this.user = new UserJSONImpl(json.getJSONObject(PropertyConfiguration.USER));
            }
            this.geoLocation = z_T4JInternalJSONImplFactory.createGeoLocation(json);
            if (!json.isNull("place")) {
                this.place = new PlaceJSONImpl(json.getJSONObject("place"));
            }
            if (!json.isNull("retweeted_status")) {
                this.retweetedStatus = new StatusJSONImpl(json.getJSONObject("retweeted_status"));
            }
            if (!json.isNull("contributors")) {
                JSONArray contributorsArray = json.getJSONArray("contributors");
                this.contributorsIDs = new long[contributorsArray.length()];
                for (int i = 0; i < contributorsArray.length(); i++) {
                    this.contributorsIDs[i] = Long.parseLong(contributorsArray.getString(i));
                }
            } else {
                this.contributorsIDs = new long[0];
            }
            if (!json.isNull("entities")) {
                JSONObject entities = json.getJSONObject("entities");
                if (!entities.isNull("user_mentions")) {
                    JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                    int len = userMentionsArray.length();
                    this.userMentionEntities = new UserMentionEntity[len];
                    for (int i2 = 0; i2 < len; i2++) {
                        this.userMentionEntities[i2] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i2));
                    }
                }
                if (!entities.isNull("urls")) {
                    JSONArray urlsArray = entities.getJSONArray("urls");
                    int len2 = urlsArray.length();
                    this.urlEntities = new URLEntity[len2];
                    for (int i3 = 0; i3 < len2; i3++) {
                        this.urlEntities[i3] = new URLEntityJSONImpl(urlsArray.getJSONObject(i3));
                    }
                }
                if (!entities.isNull("hashtags")) {
                    JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                    int len3 = hashtagsArray.length();
                    this.hashtagEntities = new HashtagEntity[len3];
                    for (int i4 = 0; i4 < len3; i4++) {
                        this.hashtagEntities[i4] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i4));
                    }
                }
                if (!entities.isNull("media")) {
                    JSONArray mediaArray = entities.getJSONArray("media");
                    int len4 = mediaArray.length();
                    this.mediaEntities = new MediaEntity[len4];
                    for (int i5 = 0; i5 < len4; i5++) {
                        this.mediaEntities[i5] = new MediaEntityJSONImpl(mediaArray.getJSONObject(i5));
                    }
                }
            }
            this.userMentionEntities = this.userMentionEntities == null ? new UserMentionEntity[0] : this.userMentionEntities;
            this.urlEntities = this.urlEntities == null ? new URLEntity[0] : this.urlEntities;
            this.hashtagEntities = this.hashtagEntities == null ? new HashtagEntity[0] : this.hashtagEntities;
            this.mediaEntities = this.mediaEntities == null ? new MediaEntity[0] : this.mediaEntities;
            this.text = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"), this.userMentionEntities, this.urlEntities, this.hashtagEntities, this.mediaEntities);
            if (!json.isNull("current_user_retweet")) {
                this.currentUserRetweetId = json.getJSONObject("current_user_retweet").getLong("id");
            }
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public int compareTo(Status that) {
        long delta = this.id - that.getId();
        if (delta < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        if (delta > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public String getSource() {
        return this.source;
    }

    public boolean isTruncated() {
        return this.isTruncated;
    }

    public long getInReplyToStatusId() {
        return this.inReplyToStatusId;
    }

    public long getInReplyToUserId() {
        return this.inReplyToUserId;
    }

    public String getInReplyToScreenName() {
        return this.inReplyToScreenName;
    }

    public GeoLocation getGeoLocation() {
        return this.geoLocation;
    }

    public Place getPlace() {
        return this.place;
    }

    public long[] getContributors() {
        return this.contributorsIDs;
    }

    public boolean isFavorited() {
        return this.isFavorited;
    }

    public User getUser() {
        return this.user;
    }

    public boolean isRetweet() {
        return this.retweetedStatus != null;
    }

    public Status getRetweetedStatus() {
        return this.retweetedStatus;
    }

    public long getRetweetCount() {
        return this.retweetCount;
    }

    public boolean isRetweetedByMe() {
        return this.currentUserRetweetId != -1;
    }

    public long getCurrentUserRetweetId() {
        return this.currentUserRetweetId;
    }

    public boolean isPossiblySensitive() {
        return this.isPossiblySensitive;
    }

    public UserMentionEntity[] getUserMentionEntities() {
        return this.userMentionEntities;
    }

    public URLEntity[] getURLEntities() {
        return this.urlEntities;
    }

    public HashtagEntity[] getHashtagEntities() {
        return this.hashtagEntities;
    }

    public MediaEntity[] getMediaEntities() {
        return this.mediaEntities;
    }

    static ResponseList<Status> createStatusList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Status> statuses = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Status status = new StatusJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(status, json);
                }
                statuses.add(status);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(statuses, list);
            }
            return statuses;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public int hashCode() {
        return (int) this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Status) || ((Status) obj).getId() != this.id) {
            return false;
        }
        return true;
    }

    public String toString() {
        List list = null;
        StringBuilder append = new StringBuilder().append("StatusJSONImpl{createdAt=").append(this.createdAt).append(", id=").append(this.id).append(", text='").append(this.text).append('\'').append(", source='").append(this.source).append('\'').append(", isTruncated=").append(this.isTruncated).append(", inReplyToStatusId=").append(this.inReplyToStatusId).append(", inReplyToUserId=").append(this.inReplyToUserId).append(", isFavorited=").append(this.isFavorited).append(", inReplyToScreenName='").append(this.inReplyToScreenName).append('\'').append(", geoLocation=").append(this.geoLocation).append(", place=").append(this.place).append(", retweetCount=").append(this.retweetCount).append(", isPossiblySensitive=").append(this.isPossiblySensitive).append(", contributorsIDs=").append(this.contributorsIDs).append(", retweetedStatus=").append(this.retweetedStatus).append(", userMentionEntities=").append(this.userMentionEntities == null ? null : Arrays.asList(this.userMentionEntities)).append(", urlEntities=").append(this.urlEntities == null ? null : Arrays.asList(this.urlEntities)).append(", hashtagEntities=").append(this.hashtagEntities == null ? null : Arrays.asList(this.hashtagEntities)).append(", mediaEntities=");
        if (this.mediaEntities != null) {
            list = Arrays.asList(this.mediaEntities);
        }
        return append.append(list).append(", currentUserRetweetId=").append(this.currentUserRetweetId).append(", user=").append(this.user).append('}').toString();
    }
}
