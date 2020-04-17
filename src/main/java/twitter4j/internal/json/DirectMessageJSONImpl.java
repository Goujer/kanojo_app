package twitter4j.internal.json;

import com.google.android.gcm.GCMConstants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import twitter4j.DirectMessage;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class DirectMessageJSONImpl extends TwitterResponseImpl implements DirectMessage, Serializable {
    private static final long serialVersionUID = -7104233663827757577L;
    private Date createdAt;
    private HashtagEntity[] hashtagEntities;
    private long id;
    private MediaEntity[] mediaEntities;
    private User recipient;
    private long recipientId;
    private String recipientScreenName;
    private User sender;
    private long senderId;
    private String senderScreenName;
    private String text;
    private URLEntity[] urlEntities;
    private UserMentionEntity[] userMentionEntities;

    DirectMessageJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    DirectMessageJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        this.id = z_T4JInternalParseUtil.getLong("id", json);
        this.senderId = z_T4JInternalParseUtil.getLong("sender_id", json);
        this.recipientId = z_T4JInternalParseUtil.getLong("recipient_id", json);
        this.createdAt = z_T4JInternalParseUtil.getDate("created_at", json);
        this.senderScreenName = z_T4JInternalParseUtil.getUnescapedString("sender_screen_name", json);
        this.recipientScreenName = z_T4JInternalParseUtil.getUnescapedString("recipient_screen_name", json);
        try {
            this.sender = new UserJSONImpl(json.getJSONObject(GCMConstants.EXTRA_SENDER));
            this.recipient = new UserJSONImpl(json.getJSONObject("recipient"));
            if (!json.isNull("entities")) {
                JSONObject entities = json.getJSONObject("entities");
                if (!entities.isNull("user_mentions")) {
                    JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                    int len = userMentionsArray.length();
                    this.userMentionEntities = new UserMentionEntity[len];
                    for (int i = 0; i < len; i++) {
                        this.userMentionEntities[i] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i));
                    }
                }
                if (!entities.isNull("urls")) {
                    JSONArray urlsArray = entities.getJSONArray("urls");
                    int len2 = urlsArray.length();
                    this.urlEntities = new URLEntity[len2];
                    for (int i2 = 0; i2 < len2; i2++) {
                        this.urlEntities[i2] = new URLEntityJSONImpl(urlsArray.getJSONObject(i2));
                    }
                }
                if (!entities.isNull("hashtags")) {
                    JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                    int len3 = hashtagsArray.length();
                    this.hashtagEntities = new HashtagEntity[len3];
                    for (int i3 = 0; i3 < len3; i3++) {
                        this.hashtagEntities[i3] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i3));
                    }
                }
                if (!entities.isNull("media")) {
                    JSONArray mediaArray = entities.getJSONArray("media");
                    int len4 = mediaArray.length();
                    this.mediaEntities = new MediaEntity[len4];
                    for (int i4 = 0; i4 < len4; i4++) {
                        this.mediaEntities[i4] = new MediaEntityJSONImpl(mediaArray.getJSONObject(i4));
                    }
                }
            }
            this.userMentionEntities = this.userMentionEntities == null ? new UserMentionEntity[0] : this.userMentionEntities;
            this.urlEntities = this.urlEntities == null ? new URLEntity[0] : this.urlEntities;
            this.hashtagEntities = this.hashtagEntities == null ? new HashtagEntity[0] : this.hashtagEntities;
            this.mediaEntities = this.mediaEntities == null ? new MediaEntity[0] : this.mediaEntities;
            this.text = HTMLEntity.unescapeAndSlideEntityIncdices(json.getString("text"), this.userMentionEntities, this.urlEntities, this.hashtagEntities, this.mediaEntities);
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public long getRecipientId() {
        return this.recipientId;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public String getSenderScreenName() {
        return this.senderScreenName;
    }

    public String getRecipientScreenName() {
        return this.recipientScreenName;
    }

    public User getSender() {
        return this.sender;
    }

    public User getRecipient() {
        return this.recipient;
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

    static ResponseList<DirectMessage> createDirectMessageList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<DirectMessage> directMessages = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                DirectMessage directMessage = new DirectMessageJSONImpl(json);
                directMessages.add(directMessage);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(directMessage, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(directMessages, list);
            }
            return directMessages;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
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
        if (!(obj instanceof DirectMessage) || ((DirectMessage) obj).getId() != this.id) {
            return false;
        }
        return true;
    }

    public String toString() {
        List list = null;
        StringBuilder append = new StringBuilder().append("DirectMessageJSONImpl{id=").append(this.id).append(", text='").append(this.text).append('\'').append(", sender_id=").append(this.senderId).append(", recipient_id=").append(this.recipientId).append(", created_at=").append(this.createdAt).append(", userMentionEntities=").append(this.userMentionEntities == null ? null : Arrays.asList(this.userMentionEntities)).append(", urlEntities=").append(this.urlEntities == null ? null : Arrays.asList(this.urlEntities)).append(", hashtagEntities=").append(this.hashtagEntities == null ? null : Arrays.asList(this.hashtagEntities)).append(", sender_screen_name='").append(this.senderScreenName).append('\'').append(", recipient_screen_name='").append(this.recipientScreenName).append('\'').append(", sender=").append(this.sender).append(", recipient=").append(this.recipient).append(", userMentionEntities=").append(this.userMentionEntities == null ? null : Arrays.asList(this.userMentionEntities)).append(", urlEntities=").append(this.urlEntities == null ? null : Arrays.asList(this.urlEntities)).append(", hashtagEntities=");
        if (this.hashtagEntities != null) {
            list = Arrays.asList(this.hashtagEntities);
        }
        return append.append(list).append('}').toString();
    }
}
