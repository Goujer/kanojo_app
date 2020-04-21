package twitter4j.internal.json;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import jp.co.cybird.barcodekanojoForGAM.gree.core.model.Inspection;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class UserJSONImpl extends TwitterResponseImpl implements User, Serializable {
    private static final long serialVersionUID = -6345893237975349030L;
    private Date createdAt;
    private String description;
    private URLEntity[] descriptionURLEntities;
    private int favouritesCount;
    private int followersCount;
    private int friendsCount;
    private long id;
    private boolean isContributorsEnabled;
    private boolean isFollowRequestSent;
    private boolean isGeoEnabled;
    private boolean isProtected;
    private boolean isVerified;
    private String lang;
    private int listedCount;
    private String location;
    private String name;
    private String profileBackgroundColor;
    private String profileBackgroundImageUrl;
    private String profileBackgroundImageUrlHttps;
    private boolean profileBackgroundTiled;
    private String profileBannerImageUrl;
    private String profileImageUrl;
    private String profileImageUrlHttps;
    private String profileLinkColor;
    private String profileSidebarBorderColor;
    private String profileSidebarFillColor;
    private String profileTextColor;
    private boolean profileUseBackgroundImage;
    private String screenName;
    private boolean showAllInlineMedia;
    private Status status;
    private int statusesCount;
    private String timeZone;
    private boolean translator;
    private String url;
    private URLEntity urlEntity;
    private int utcOffset;

    UserJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
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

    UserJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    UserJSONImpl() {
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            this.id = z_T4JInternalParseUtil.getLong("id", json);
            this.name = z_T4JInternalParseUtil.getRawString("name", json);
            this.screenName = z_T4JInternalParseUtil.getRawString("screen_name", json);
            this.location = z_T4JInternalParseUtil.getRawString("location", json);
            this.descriptionURLEntities = getURLEntitiesFromJSON(json, "description");
            this.descriptionURLEntities = this.descriptionURLEntities == null ? new URLEntity[0] : this.descriptionURLEntities;
            URLEntity[] urlEntities = getURLEntitiesFromJSON(json, "url");
            if (urlEntities != null && urlEntities.length > 0) {
                this.urlEntity = urlEntities[0];
            }
            this.description = z_T4JInternalParseUtil.getRawString("description", json);
            if (this.description != null) {
                this.description = HTMLEntity.unescapeAndSlideEntityIncdices(this.description, (UserMentionEntity[]) null, this.descriptionURLEntities, (HashtagEntity[]) null, (MediaEntity[]) null);
            }
            this.isContributorsEnabled = z_T4JInternalParseUtil.getBoolean("contributors_enabled", json);
            this.profileImageUrl = z_T4JInternalParseUtil.getRawString("profile_image_url", json);
            this.profileImageUrlHttps = z_T4JInternalParseUtil.getRawString("profile_image_url_https", json);
            this.url = z_T4JInternalParseUtil.getRawString("url", json);
            this.isProtected = z_T4JInternalParseUtil.getBoolean("protected", json);
            this.isGeoEnabled = z_T4JInternalParseUtil.getBoolean("geo_enabled", json);
            this.isVerified = z_T4JInternalParseUtil.getBoolean("verified", json);
            this.translator = z_T4JInternalParseUtil.getBoolean("is_translator", json);
            this.followersCount = z_T4JInternalParseUtil.getInt("followers_count", json);
            this.profileBackgroundColor = z_T4JInternalParseUtil.getRawString("profile_background_color", json);
            this.profileTextColor = z_T4JInternalParseUtil.getRawString("profile_text_color", json);
            this.profileLinkColor = z_T4JInternalParseUtil.getRawString("profile_link_color", json);
            this.profileSidebarFillColor = z_T4JInternalParseUtil.getRawString("profile_sidebar_fill_color", json);
            this.profileSidebarBorderColor = z_T4JInternalParseUtil.getRawString("profile_sidebar_border_color", json);
            this.profileUseBackgroundImage = z_T4JInternalParseUtil.getBoolean("profile_use_background_image", json);
            this.showAllInlineMedia = z_T4JInternalParseUtil.getBoolean("show_all_inline_media", json);
            this.friendsCount = z_T4JInternalParseUtil.getInt("friends_count", json);
            this.createdAt = z_T4JInternalParseUtil.getDate("created_at", json, "EEE MMM dd HH:mm:ss z yyyy");
            this.favouritesCount = z_T4JInternalParseUtil.getInt("favourites_count", json);
            this.utcOffset = z_T4JInternalParseUtil.getInt("utc_offset", json);
            this.timeZone = z_T4JInternalParseUtil.getRawString("time_zone", json);
            this.profileBackgroundImageUrl = z_T4JInternalParseUtil.getRawString("profile_background_image_url", json);
            this.profileBackgroundImageUrlHttps = z_T4JInternalParseUtil.getRawString("profile_background_image_url_https", json);
            this.profileBannerImageUrl = z_T4JInternalParseUtil.getRawString("profile_banner_url", json);
            this.profileBackgroundTiled = z_T4JInternalParseUtil.getBoolean("profile_background_tile", json);
            this.lang = z_T4JInternalParseUtil.getRawString("lang", json);
            this.statusesCount = z_T4JInternalParseUtil.getInt("statuses_count", json);
            this.listedCount = z_T4JInternalParseUtil.getInt("listed_count", json);
            this.isFollowRequestSent = z_T4JInternalParseUtil.getBoolean("follow_request_sent", json);
            if (!json.isNull(Inspection.STATUS)) {
                this.status = new StatusJSONImpl(json.getJSONObject(Inspection.STATUS));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    private static URLEntity[] getURLEntitiesFromJSON(JSONObject json, String category) throws JSONException, TwitterException {
        if (!json.isNull("entities")) {
            JSONObject entitiesJSON = json.getJSONObject("entities");
            if (!entitiesJSON.isNull(category)) {
                JSONObject descriptionEntitiesJSON = entitiesJSON.getJSONObject(category);
                if (!descriptionEntitiesJSON.isNull("urls")) {
                    JSONArray urlsArray = descriptionEntitiesJSON.getJSONArray("urls");
                    int len = urlsArray.length();
                    URLEntity[] urlEntities = new URLEntity[len];
                    for (int i = 0; i < len; i++) {
                        urlEntities[i] = new URLEntityJSONImpl(urlsArray.getJSONObject(i));
                    }
                    return urlEntities;
                }
            }
        }
        return null;
    }

    public int compareTo(User that) {
        return (int) (this.id - that.getId());
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

    public String getLocation() {
        return this.location;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isContributorsEnabled() {
        return this.isContributorsEnabled;
    }

    public String getProfileImageURL() {
        return this.profileImageUrl;
    }

    public String getBiggerProfileImageURL() {
        return toResizedURL(this.profileImageUrl, "_bigger");
    }

    public String getMiniProfileImageURL() {
        return toResizedURL(this.profileImageUrl, "_mini");
    }

    public String getOriginalProfileImageURL() {
        return toResizedURL(this.profileImageUrl, "");
    }

    private String toResizedURL(String originalURL, String sizeSuffix) {
        if (originalURL == null) {
            return null;
        }
        int index = originalURL.lastIndexOf("_");
        int suffixIndex = originalURL.lastIndexOf(".");
        String url2 = originalURL.substring(0, index) + sizeSuffix;
        if (suffixIndex > originalURL.lastIndexOf("/")) {
            return url2 + originalURL.substring(suffixIndex);
        }
        return url2;
    }

    public URL getProfileImageUrlHttps() {
        try {
            return new URL(this.profileImageUrlHttps);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String getProfileImageURLHttps() {
        return this.profileImageUrlHttps;
    }

    public String getBiggerProfileImageURLHttps() {
        return toResizedURL(this.profileImageUrlHttps, "_bigger");
    }

    public String getMiniProfileImageURLHttps() {
        return toResizedURL(this.profileImageUrlHttps, "_mini");
    }

    public String getOriginalProfileImageURLHttps() {
        return toResizedURL(this.profileImageUrlHttps, "");
    }

    public String getURL() {
        return this.url;
    }

    public boolean isProtected() {
        return this.isProtected;
    }

    public int getFollowersCount() {
        return this.followersCount;
    }

    public String getProfileBackgroundColor() {
        return this.profileBackgroundColor;
    }

    public String getProfileTextColor() {
        return this.profileTextColor;
    }

    public String getProfileLinkColor() {
        return this.profileLinkColor;
    }

    public String getProfileSidebarFillColor() {
        return this.profileSidebarFillColor;
    }

    public String getProfileSidebarBorderColor() {
        return this.profileSidebarBorderColor;
    }

    public boolean isProfileUseBackgroundImage() {
        return this.profileUseBackgroundImage;
    }

    public boolean isShowAllInlineMedia() {
        return this.showAllInlineMedia;
    }

    public int getFriendsCount() {
        return this.friendsCount;
    }

    public Status getStatus() {
        return this.status;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public int getFavouritesCount() {
        return this.favouritesCount;
    }

    public int getUtcOffset() {
        return this.utcOffset;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public String getProfileBackgroundImageUrl() {
        return getProfileBackgroundImageURL();
    }

    public String getProfileBackgroundImageURL() {
        return this.profileBackgroundImageUrl;
    }

    public String getProfileBackgroundImageUrlHttps() {
        return this.profileBackgroundImageUrlHttps;
    }

    public String getProfileBannerURL() {
        if (this.profileBannerImageUrl != null) {
            return this.profileBannerImageUrl + "/web";
        }
        return null;
    }

    public String getProfileBannerRetinaURL() {
        if (this.profileBannerImageUrl != null) {
            return this.profileBannerImageUrl + "/web_retina";
        }
        return null;
    }

    public String getProfileBannerIPadURL() {
        if (this.profileBannerImageUrl != null) {
            return this.profileBannerImageUrl + "/ipad";
        }
        return null;
    }

    public String getProfileBannerIPadRetinaURL() {
        if (this.profileBannerImageUrl != null) {
            return this.profileBannerImageUrl + "/ipad_retina";
        }
        return null;
    }

    public String getProfileBannerMobileURL() {
        if (this.profileBannerImageUrl != null) {
            return this.profileBannerImageUrl + "/mobile";
        }
        return null;
    }

    public String getProfileBannerMobileRetinaURL() {
        if (this.profileBannerImageUrl != null) {
            return this.profileBannerImageUrl + "/ipad_retina";
        }
        return null;
    }

    public boolean isProfileBackgroundTiled() {
        return this.profileBackgroundTiled;
    }

    public String getLang() {
        return this.lang;
    }

    public int getStatusesCount() {
        return this.statusesCount;
    }

    public boolean isGeoEnabled() {
        return this.isGeoEnabled;
    }

    public boolean isVerified() {
        return this.isVerified;
    }

    public boolean isTranslator() {
        return this.translator;
    }

    public int getListedCount() {
        return this.listedCount;
    }

    public boolean isFollowRequestSent() {
        return this.isFollowRequestSent;
    }

    public URLEntity[] getDescriptionURLEntities() {
        return this.descriptionURLEntities;
    }

    public URLEntity getURLEntity() {
        if (this.urlEntity == null) {
            String plainURL = this.url == null ? "" : this.url;
            this.urlEntity = new URLEntityJSONImpl(0, plainURL.length(), plainURL, plainURL, plainURL);
        }
        return this.urlEntity;
    }

    static PagableResponseList<User> createPagableUserList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("users");
            int size = list.length();
            PagableResponseList<User> users = new PagableResponseListImpl<>(size, json, res);
            for (int i = 0; i < size; i++) {
                JSONObject userJson = list.getJSONObject(i);
                User user = new UserJSONImpl(userJson);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(user, userJson);
                }
                users.add(user);
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

    static ResponseList<User> createUserList(HttpResponse res, Configuration conf) throws TwitterException {
        return createUserList(res.asJSONArray(), res, conf);
    }

    static ResponseList<User> createUserList(JSONArray list, HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            int size = list.length();
            ResponseList<User> users = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                User user = new UserJSONImpl(json);
                users.add(user);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(user, json);
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
        return (int) this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User) || ((User) obj).getId() != this.id) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "UserJSONImpl{id=" + this.id + ", name='" + this.name + '\'' + ", screenName='" + this.screenName + '\'' + ", location='" + this.location + '\'' + ", description='" + this.description + '\'' + ", isContributorsEnabled=" + this.isContributorsEnabled + ", profileImageUrl='" + this.profileImageUrl + '\'' + ", profileImageUrlHttps='" + this.profileImageUrlHttps + '\'' + ", url='" + this.url + '\'' + ", isProtected=" + this.isProtected + ", followersCount=" + this.followersCount + ", status=" + this.status + ", profileBackgroundColor='" + this.profileBackgroundColor + '\'' + ", profileTextColor='" + this.profileTextColor + '\'' + ", profileLinkColor='" + this.profileLinkColor + '\'' + ", profileSidebarFillColor='" + this.profileSidebarFillColor + '\'' + ", profileSidebarBorderColor='" + this.profileSidebarBorderColor + '\'' + ", profileUseBackgroundImage=" + this.profileUseBackgroundImage + ", showAllInlineMedia=" + this.showAllInlineMedia + ", friendsCount=" + this.friendsCount + ", createdAt=" + this.createdAt + ", favouritesCount=" + this.favouritesCount + ", utcOffset=" + this.utcOffset + ", timeZone='" + this.timeZone + '\'' + ", profileBackgroundImageUrl='" + this.profileBackgroundImageUrl + '\'' + ", profileBackgroundImageUrlHttps='" + this.profileBackgroundImageUrlHttps + '\'' + ", profileBackgroundTiled=" + this.profileBackgroundTiled + ", lang='" + this.lang + '\'' + ", statusesCount=" + this.statusesCount + ", isGeoEnabled=" + this.isGeoEnabled + ", isVerified=" + this.isVerified + ", translator=" + this.translator + ", listedCount=" + this.listedCount + ", isFollowRequestSent=" + this.isFollowRequestSent + '}';
    }
}
