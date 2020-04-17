package twitter4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.gree.core.model.Inspection;
import twitter4j.api.HelpResources;
import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.util.z_T4JInternalStringUtil;

class TwitterImpl extends TwitterBaseImpl implements Twitter {
    private static final Map<Configuration, HttpParameter[]> implicitParamsMap = new HashMap();
    private static final Map<Configuration, String> implicitParamsStrMap = new HashMap();
    private static final long serialVersionUID = -1486360080128882436L;
    private final HttpParameter[] IMPLICIT_PARAMS;
    private final String IMPLICIT_PARAMS_STR;
    private final HttpParameter INCLUDE_MY_RETWEET;

    TwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        this.INCLUDE_MY_RETWEET = new HttpParameter("include_my_retweet", conf.isIncludeMyRetweetEnabled());
        HttpParameter[] implicitParams = implicitParamsMap.get(conf);
        String implicitParamsStr = implicitParamsStrMap.get(conf);
        if (implicitParams == null) {
            String includeEntities = conf.isIncludeEntitiesEnabled() ? GreeDefs.KANOJO_NAME : GreeDefs.BARCODE;
            String includeRTs = conf.isIncludeRTsEnabled() ? GreeDefs.KANOJO_NAME : GreeDefs.BARCODE;
            boolean contributorsEnabled = conf.getContributingTo() != -1;
            implicitParamsStr = "include_entities=" + includeEntities + "&include_rts=" + includeRTs + (contributorsEnabled ? "&contributingto=" + conf.getContributingTo() : "");
            implicitParamsStrMap.put(conf, implicitParamsStr);
            List<HttpParameter> params = new ArrayList<>();
            params.add(new HttpParameter("include_entities", includeEntities));
            params.add(new HttpParameter("include_rts", includeRTs));
            if (contributorsEnabled) {
                params.add(new HttpParameter("contributingto", conf.getContributingTo()));
            }
            implicitParams = (HttpParameter[]) params.toArray(new HttpParameter[params.size()]);
            implicitParamsMap.put(conf, implicitParams);
        }
        this.IMPLICIT_PARAMS = implicitParams;
        this.IMPLICIT_PARAMS_STR = implicitParamsStr;
    }

    public ResponseList<Status> getMentions() throws TwitterException {
        return getMentionsTimeline();
    }

    public ResponseList<Status> getMentionsTimeline() throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/mentions_timeline.json"));
    }

    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        return getMentionsTimeline(paging);
    }

    public ResponseList<Status> getMentionsTimeline(Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/mentions_timeline.json", paging.asPostParameterArray()));
    }

    public ResponseList<Status> getHomeTimeline() throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/home_timeline.json", new HttpParameter[]{this.INCLUDE_MY_RETWEET}));
    }

    public ResponseList<Status> getHomeTimeline(Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/home_timeline.json", mergeParameters(paging.asPostParameterArray(), new HttpParameter[]{this.INCLUDE_MY_RETWEET})));
    }

    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/retweets_of_me.json"));
    }

    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/retweets_of_me.json", paging.asPostParameterArray()));
    }

    public ResponseList<Status> getUserTimeline(String screenName, Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName), this.INCLUDE_MY_RETWEET}, paging.asPostParameterArray())));
    }

    public ResponseList<Status> getUserTimeline(long userId, Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId), this.INCLUDE_MY_RETWEET}, paging.asPostParameterArray())));
    }

    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, new Paging());
    }

    public ResponseList<Status> getUserTimeline(long userId) throws TwitterException {
        return getUserTimeline(userId, new Paging());
    }

    public ResponseList<Status> getUserTimeline() throws TwitterException {
        return getUserTimeline(new Paging());
    }

    public ResponseList<Status> getUserTimeline(Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/user_timeline.json", mergeParameters(new HttpParameter[]{this.INCLUDE_MY_RETWEET}, paging.asPostParameterArray())));
    }

    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "statuses/retweets/" + statusId + ".json?count=100"));
    }

    public Status showStatus(long id) throws TwitterException {
        return this.factory.createStatus(get(this.conf.getRestBaseURL() + "statuses/show/" + id + ".json"));
    }

    public Status destroyStatus(long statusId) throws TwitterException {
        return this.factory.createStatus(post(this.conf.getRestBaseURL() + "statuses/destroy/" + statusId + ".json"));
    }

    public Status updateStatus(String status) throws TwitterException {
        return this.factory.createStatus(post(this.conf.getRestBaseURL() + "statuses/update.json", new HttpParameter[]{new HttpParameter((String) Inspection.STATUS, status)}));
    }

    public Status updateStatus(StatusUpdate status) throws TwitterException {
        return this.factory.createStatus(post(this.conf.getRestBaseURL() + (status.isWithMedia() ? "statuses/update_with_media.json" : "statuses/update.json"), status.asHttpParameterArray()));
    }

    public Status retweetStatus(long statusId) throws TwitterException {
        return this.factory.createStatus(post(this.conf.getRestBaseURL() + "statuses/retweet/" + statusId + ".json"));
    }

    public OEmbed getOEmbed(OEmbedRequest req) throws TwitterException {
        return this.factory.createOEmbed(get(this.conf.getRestBaseURL() + "statuses/oembed.json", req.asHttpParameterArray()));
    }

    public QueryResult search(Query query) throws TwitterException {
        if (query.nextPage() != null) {
            return this.factory.createQueryResult(get(this.conf.getRestBaseURL() + "search/tweets.json" + query.nextPage()), query);
        }
        return this.factory.createQueryResult(get(this.conf.getRestBaseURL() + "search/tweets.json", query.asHttpParameterArray()), query);
    }

    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        return this.factory.createDirectMessageList(get(this.conf.getRestBaseURL() + "direct_messages.json"));
    }

    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        return this.factory.createDirectMessageList(get(this.conf.getRestBaseURL() + "direct_messages.json", paging.asPostParameterArray()));
    }

    public ResponseList<DirectMessage> getSentDirectMessages() throws TwitterException {
        return this.factory.createDirectMessageList(get(this.conf.getRestBaseURL() + "direct_messages/sent.json"));
    }

    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws TwitterException {
        return this.factory.createDirectMessageList(get(this.conf.getRestBaseURL() + "direct_messages/sent.json", paging.asPostParameterArray()));
    }

    public DirectMessage showDirectMessage(long id) throws TwitterException {
        return this.factory.createDirectMessage(get(this.conf.getRestBaseURL() + "direct_messages/show.json?id=" + id));
    }

    public DirectMessage destroyDirectMessage(long id) throws TwitterException {
        return this.factory.createDirectMessage(post(this.conf.getRestBaseURL() + "direct_messages/destroy.json?id=" + id));
    }

    public DirectMessage sendDirectMessage(long userId, String text) throws TwitterException {
        return this.factory.createDirectMessage(post(this.conf.getRestBaseURL() + "direct_messages/new.json", new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("text", text)}));
    }

    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        return this.factory.createDirectMessage(post(this.conf.getRestBaseURL() + "direct_messages/new.json", new HttpParameter[]{new HttpParameter("screen_name", screenName), new HttpParameter("text", text)}));
    }

    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "friends/ids.json?cursor=" + cursor));
    }

    public IDs getFriendsIDs(long userId, long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "friends/ids.json?user_id=" + userId + "&cursor=" + cursor));
    }

    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "friends/ids.json?screen_name=" + screenName + "&cursor=" + cursor));
    }

    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "followers/ids.json?cursor=" + cursor));
    }

    public IDs getFollowersIDs(long userId, long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "followers/ids.json?user_id=" + userId + "&cursor=" + cursor));
    }

    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "followers/ids.json?screen_name=" + screenName + "&cursor=" + cursor));
    }

    public ResponseList<Friendship> lookupFriendships(long[] ids) throws TwitterException {
        return this.factory.createFriendshipList(get(this.conf.getRestBaseURL() + "friendships/lookup.json?user_id=" + z_T4JInternalStringUtil.join(ids)));
    }

    public ResponseList<Friendship> lookupFriendships(String[] screenNames) throws TwitterException {
        return this.factory.createFriendshipList(get(this.conf.getRestBaseURL() + "friendships/lookup.json?screen_name=" + z_T4JInternalStringUtil.join(screenNames)));
    }

    public IDs getIncomingFriendships(long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "friendships/incoming.json?cursor=" + cursor));
    }

    public IDs getOutgoingFriendships(long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "friendships/outgoing.json?cursor=" + cursor));
    }

    public User createFriendship(long userId) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId));
    }

    public User createFriendship(String screenName) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName));
    }

    public User createFriendship(long userId, boolean follow) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId + "&follow=" + follow));
    }

    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName + "&follow=" + follow));
    }

    public User destroyFriendship(long userId) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "friendships/destroy.json?user_id=" + userId));
    }

    public User destroyFriendship(String screenName) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "friendships/destroy.json?screen_name=" + screenName));
    }

    public Relationship updateFriendship(long userId, boolean enableDeviceNotification, boolean retweets) throws TwitterException {
        return this.factory.createRelationship(post(this.conf.getRestBaseURL() + "friendships/update.json", new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", retweets)}));
    }

    public Relationship updateFriendship(String screenName, boolean enableDeviceNotification, boolean retweets) throws TwitterException {
        return this.factory.createRelationship(post(this.conf.getRestBaseURL() + "friendships/update.json", new HttpParameter[]{new HttpParameter("screen_name", screenName), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", enableDeviceNotification)}));
    }

    public Relationship showFriendship(long sourceId, long targetId) throws TwitterException {
        return this.factory.createRelationship(get(this.conf.getRestBaseURL() + "friendships/show.json", new HttpParameter[]{new HttpParameter("source_id", sourceId), new HttpParameter("target_id", targetId)}));
    }

    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return this.factory.createRelationship(get(this.conf.getRestBaseURL() + "friendships/show.json", HttpParameter.getParameterArray("source_screen_name", sourceScreenName, "target_screen_name", targetScreenName)));
    }

    public PagableResponseList<User> getFriendsList(long userId, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "friends/list.json?user_id=" + userId + "&cursor=" + cursor));
    }

    public PagableResponseList<User> getFriendsList(String screenName, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "friends/list.json?screen_name=" + screenName + "&cursor=" + cursor));
    }

    public PagableResponseList<User> getFollowersList(long userId, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "followers/list.json?user_id=" + userId + "&cursor=" + cursor));
    }

    public PagableResponseList<User> getFollowersList(String screenName, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "followers/list.json?screen_name=" + screenName + "&cursor=" + cursor));
    }

    public AccountSettings getAccountSettings() throws TwitterException {
        return this.factory.createAccountSettings(get(this.conf.getRestBaseURL() + "account/settings.json"));
    }

    public User verifyCredentials() throws TwitterException {
        return super.fillInIDAndScreenName();
    }

    public AccountSettings updateAccountSettings(Integer trend_locationWoeid, Boolean sleep_timeEnabled, String start_sleepTime, String end_sleepTime, String time_zone, String lang) throws TwitterException {
        List<HttpParameter> profile = new ArrayList<>(6);
        if (trend_locationWoeid != null) {
            profile.add(new HttpParameter("trend_location_woeid", trend_locationWoeid.intValue()));
        }
        if (sleep_timeEnabled != null) {
            profile.add(new HttpParameter("sleep_time_enabled", sleep_timeEnabled.toString()));
        }
        if (start_sleepTime != null) {
            profile.add(new HttpParameter("start_sleep_time", start_sleepTime));
        }
        if (end_sleepTime != null) {
            profile.add(new HttpParameter("end_sleep_time", end_sleepTime));
        }
        if (time_zone != null) {
            profile.add(new HttpParameter("time_zone", time_zone));
        }
        if (lang != null) {
            profile.add(new HttpParameter("lang", lang));
        }
        return this.factory.createAccountSettings(post(this.conf.getRestBaseURL() + "account/settings.json", (HttpParameter[]) profile.toArray(new HttpParameter[profile.size()])));
    }

    public User updateProfile(String name, String url, String location, String description) throws TwitterException {
        List<HttpParameter> profile = new ArrayList<>(4);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "account/update_profile.json", (HttpParameter[]) profile.toArray(new HttpParameter[profile.size()])));
    }

    public User updateProfileBackgroundImage(File image, boolean tile) throws TwitterException {
        checkFileValidity(image);
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "account/update_profile_background_image.json", new HttpParameter[]{new HttpParameter("image", image), new HttpParameter("tile", tile)}));
    }

    public User updateProfileBackgroundImage(InputStream image, boolean tile) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "account/update_profile_background_image.json", new HttpParameter[]{new HttpParameter("image", "image", image), new HttpParameter("tile", tile)}));
    }

    public User updateProfileColors(String profileBackgroundColor, String profileTextColor, String profileLinkColor, String profileSidebarFillColor, String profileSidebarBorderColor) throws TwitterException {
        List<HttpParameter> colors = new ArrayList<>(6);
        addParameterToList(colors, "profile_background_color", profileBackgroundColor);
        addParameterToList(colors, "profile_text_color", profileTextColor);
        addParameterToList(colors, "profile_link_color", profileLinkColor);
        addParameterToList(colors, "profile_sidebar_fill_color", profileSidebarFillColor);
        addParameterToList(colors, "profile_sidebar_border_color", profileSidebarBorderColor);
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "account/update_profile_colors.json", (HttpParameter[]) colors.toArray(new HttpParameter[colors.size()])));
    }

    private void addParameterToList(List<HttpParameter> colors, String paramName, String color) {
        if (color != null) {
            colors.add(new HttpParameter(paramName, color));
        }
    }

    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "account/update_profile_image.json", new HttpParameter[]{new HttpParameter("image", image)}));
    }

    public User updateProfileImage(InputStream image) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "account/update_profile_image.json", new HttpParameter[]{new HttpParameter("image", "image", image)}));
    }

    private void checkFileValidity(File image) throws TwitterException {
        if (!image.exists()) {
            throw new TwitterException((Exception) new FileNotFoundException(image + " is not found."));
        } else if (!image.isFile()) {
            throw new TwitterException((Exception) new IOException(image + " is not a file."));
        }
    }

    public PagableResponseList<User> getBlocksList() throws TwitterException {
        return getBlocksList(-1);
    }

    public PagableResponseList<User> getBlocksList(long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "blocks/list.json?cursor=" + cursor));
    }

    public IDs getBlocksIDs() throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "blocks/ids.json"));
    }

    public IDs getBlocksIDs(long cursor) throws TwitterException {
        return this.factory.createIDs(get(this.conf.getRestBaseURL() + "blocks/ids.json?cursor=" + cursor));
    }

    public User createBlock(long userId) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "blocks/create.json?user_id=" + userId));
    }

    public User createBlock(String screenName) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "blocks/create.json?screen_name=" + screenName));
    }

    public User destroyBlock(long userId) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "blocks/destroy.json?user_id=" + userId));
    }

    public User destroyBlock(String screen_name) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "blocks/destroy.json?screen_name=" + screen_name));
    }

    public ResponseList<User> lookupUsers(long[] ids) throws TwitterException {
        return this.factory.createUserList(post(this.conf.getRestBaseURL() + "users/lookup.json", new HttpParameter[]{new HttpParameter("user_id", z_T4JInternalStringUtil.join(ids))}));
    }

    public ResponseList<User> lookupUsers(String[] screenNames) throws TwitterException {
        return this.factory.createUserList(post(this.conf.getRestBaseURL() + "users/lookup.json", new HttpParameter[]{new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    public User showUser(long userId) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "users/show.json?user_id=" + userId));
    }

    public User showUser(String screenName) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "users/show.json?screen_name=" + screenName));
    }

    public ResponseList<User> searchUsers(String query, int page) throws TwitterException {
        return this.factory.createUserList(get(this.conf.getRestBaseURL() + "users/search.json", new HttpParameter[]{new HttpParameter((String) AppLauncherConsts.REQUEST_PARAM_GENERAL, query), new HttpParameter("per_page", 20), new HttpParameter("page", page)}));
    }

    public ResponseList<User> getContributees(long userId) throws TwitterException {
        return this.factory.createUserList(get(this.conf.getRestBaseURL() + "users/contributees.json?user_id=" + userId));
    }

    public ResponseList<User> getContributees(String screenName) throws TwitterException {
        return this.factory.createUserList(get(this.conf.getRestBaseURL() + "users/contributees.json?screen_name=" + screenName));
    }

    public ResponseList<User> getContributors(long userId) throws TwitterException {
        return this.factory.createUserList(get(this.conf.getRestBaseURL() + "users/contributors.json?user_id=" + userId));
    }

    public ResponseList<User> getContributors(String screenName) throws TwitterException {
        return this.factory.createUserList(get(this.conf.getRestBaseURL() + "users/contributors.json?screen_name=" + screenName));
    }

    public void removeProfileBanner() throws TwitterException {
        post(this.conf.getRestBaseURL() + "account/remove_profile_banner.json");
    }

    public void updateProfileBanner(File image) throws TwitterException {
        checkFileValidity(image);
        post(this.conf.getRestBaseURL() + "account/update_profile_banner.json", new HttpParameter[]{new HttpParameter((String) AppLauncherConsts.REQUEST_PARAM_GENERAL_TYPE_BANNER, image)});
    }

    public void updateProfileBanner(InputStream image) throws TwitterException {
        post(this.conf.getRestBaseURL() + "account/update_profile_banner.json", new HttpParameter[]{new HttpParameter(AppLauncherConsts.REQUEST_PARAM_GENERAL_TYPE_BANNER, AppLauncherConsts.REQUEST_PARAM_GENERAL_TYPE_BANNER, image)});
    }

    public ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException {
        try {
            return this.factory.createUserListFromJSONArray_Users(get(this.conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + ".json"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
        return this.factory.createCategoryList(get(this.conf.getRestBaseURL() + "users/suggestions.json"));
    }

    public ResponseList<User> getMemberSuggestions(String categorySlug) throws TwitterException {
        try {
            return this.factory.createUserListFromJSONArray(get(this.conf.getRestBaseURL() + "users/suggestions/" + URLEncoder.encode(categorySlug, "UTF-8") + "/members.json"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseList<Status> getFavorites() throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "favorites/list.json"));
    }

    public ResponseList<Status> getFavorites(long userId) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "favorites/list.json?user_id=" + userId));
    }

    public ResponseList<Status> getFavorites(String screenName) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "favorites/list.json?screen_name=" + screenName));
    }

    public ResponseList<Status> getFavorites(Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "favorites/list.json", paging.asPostParameterArray()));
    }

    public ResponseList<Status> getFavorites(long userId, Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "favorites/list.json", mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId)}, paging.asPostParameterArray())));
    }

    public ResponseList<Status> getFavorites(String screenName, Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "favorites/list.json", mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName)}, paging.asPostParameterArray())));
    }

    public Status destroyFavorite(long id) throws TwitterException {
        return this.factory.createStatus(post(this.conf.getRestBaseURL() + "favorites/destroy.json?id=" + id));
    }

    public Status createFavorite(long id) throws TwitterException {
        return this.factory.createStatus(post(this.conf.getRestBaseURL() + "favorites/create.json?id=" + id));
    }

    public ResponseList<UserList> getUserLists(String listOwnerScreenName) throws TwitterException {
        return this.factory.createUserListList(get(this.conf.getRestBaseURL() + "lists/list.json?screen_name=" + listOwnerScreenName));
    }

    public ResponseList<UserList> getUserLists(long listOwnerUserId) throws TwitterException {
        return this.factory.createUserListList(get(this.conf.getRestBaseURL() + "lists/list.json?user_id=" + listOwnerUserId));
    }

    public ResponseList<Status> getUserListStatuses(int listId, Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "lists/statuses.json", mergeParameters(paging.asPostParameterArray(Paging.SMCP, "count"), new HttpParameter("list_id", listId))));
    }

    public ResponseList<Status> getUserListStatuses(long ownerId, String slug, Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "lists/statuses.json", mergeParameters(paging.asPostParameterArray(Paging.SMCP, "count"), new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)})));
    }

    public ResponseList<Status> getUserListStatuses(String ownerScreenName, String slug, Paging paging) throws TwitterException {
        return this.factory.createStatusList(get(this.conf.getRestBaseURL() + "lists/statuses.json", mergeParameters(paging.asPostParameterArray(Paging.SMCP, "count"), new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)})));
    }

    public UserList destroyUserListMember(int listId, long userId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id", userId)}));
    }

    public UserList deleteUserListMember(int listId, long userId) throws TwitterException {
        return destroyUserListMember(listId, userId);
    }

    public UserList destroyUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)}));
    }

    public UserList destroyUserListMember(String ownerScreenName, String slug, long userId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/destroy.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)}));
    }

    public UserList deleteUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return destroyUserListMember(ownerId, slug, userId);
    }

    public PagableResponseList<UserList> getUserListMemberships(long cursor) throws TwitterException {
        return this.factory.createPagableUserListList(get(this.conf.getRestBaseURL() + "lists/memberships.json?cursor=" + cursor));
    }

    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, cursor, false);
    }

    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberId, cursor, false);
    }

    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return this.factory.createPagableUserListList(get(this.conf.getRestBaseURL() + "lists/memberships.json?user_id=" + listMemberId + "&cursor=" + cursor + "&filter_to_owned_lists=" + filterToOwnedLists));
    }

    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return this.factory.createPagableUserListList(get(this.conf.getRestBaseURL() + "lists/memberships.json?screen_name=" + listMemberScreenName + "&cursor=" + cursor + "&filter_to_owned_lists=" + filterToOwnedLists));
    }

    public PagableResponseList<User> getUserListSubscribers(int listId, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "lists/subscribers.json?list_id=" + listId + "&cursor=" + cursor));
    }

    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "lists/subscribers.json?owner_id=" + ownerId + "&slug=" + slug + "&cursor=" + cursor));
    }

    public PagableResponseList<User> getUserListSubscribers(String ownerScreenName, String slug, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "lists/subscribers.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug + "&cursor=" + cursor));
    }

    public UserList createUserListSubscription(int listId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    public UserList createUserListSubscription(long ownerId, String slug) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    public UserList createUserListSubscription(String ownerScreenName, String slug) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/subscribers/create.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    public User showUserListSubscription(int listId, long userId) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "lists/subscribers/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    public User showUserListSubscription(long ownerId, String slug, long userId) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "lists/subscribers/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    public User showUserListSubscription(String ownerScreenName, String slug, long userId) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "lists/subscribers/show.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug + "&user_id=" + userId));
    }

    public UserList destroyUserListSubscription(int listId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/subscribers/destroy.json", new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    public UserList destroyUserListSubscription(long ownerId, String slug) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/subscribers/destroy.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    public UserList destroyUserListSubscription(String ownerScreenName, String slug) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/subscribers/destroy.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    public UserList createUserListMembers(int listId, long[] userIds) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id", z_T4JInternalStringUtil.join(userIds))}));
    }

    public UserList addUserListMembers(int listId, long[] userIds) throws TwitterException {
        return createUserListMembers(listId, userIds);
    }

    public UserList createUserListMembers(long ownerId, String slug, long[] userIds) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("user_id", z_T4JInternalStringUtil.join(userIds))}));
    }

    public UserList createUserListMembers(String ownerScreenName, String slug, long[] userIds) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", z_T4JInternalStringUtil.join(userIds))}));
    }

    public UserList addUserListMembers(long ownerId, String slug, long[] userIds) throws TwitterException {
        return createUserListMembers(ownerId, slug, userIds);
    }

    public UserList createUserListMembers(int listId, String[] screenNames) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    public UserList addUserListMembers(int listId, String[] screenNames) throws TwitterException {
        return createUserListMembers(listId, screenNames);
    }

    public UserList createUserListMembers(long ownerId, String slug, String[] screenNames) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    public UserList createUserListMembers(String ownerScreenName, String slug, String[] screenNames) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create_all.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("screen_name", z_T4JInternalStringUtil.join(screenNames))}));
    }

    public UserList addUserListMembers(long ownerId, String slug, String[] screenNames) throws TwitterException {
        return createUserListMembers(ownerId, slug, screenNames);
    }

    public User showUserListMembership(int listId, long userId) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "lists/members/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    public User showUserListMembership(long ownerId, String slug, long userId) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "lists/members/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    public User showUserListMembership(String ownerScreenName, String slug, long userId) throws TwitterException {
        return this.factory.createUser(get(this.conf.getRestBaseURL() + "lists/members/show.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug + "&user_id=" + userId));
    }

    public PagableResponseList<User> getUserListMembers(int listId, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "lists/members.json?list_id=" + listId + "&cursor=" + cursor));
    }

    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "lists/members.json?owner_id=" + ownerId + "&slug=" + slug + "&cursor=" + cursor));
    }

    public PagableResponseList<User> getUserListMembers(String ownerScreenName, String slug, long cursor) throws TwitterException {
        return this.factory.createPagableUserList(get(this.conf.getRestBaseURL() + "lists/members.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug + "&cursor=" + cursor));
    }

    public UserList createUserListMember(int listId, long userId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("list_id", listId)}));
    }

    public UserList addUserListMember(int listId, long userId) throws TwitterException {
        return null;
    }

    public UserList createUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    public UserList createUserListMember(String ownerScreenName, String slug, long userId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/members/create.json", new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    public UserList addUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return null;
    }

    public UserList destroyUserList(int listId) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/destroy.json", new HttpParameter[]{new HttpParameter("list_id", listId)}));
    }

    public UserList destroyUserList(long ownerId, String slug) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/destroy.json", new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)}));
    }

    public UserList destroyUserList(String ownerScreenName, String slug) throws TwitterException {
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/destroy.json", new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)}));
    }

    public UserList updateUserList(int listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("list_id", listId));
    }

    public UserList updateUserList(long ownerId, String slug, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug));
    }

    public UserList updateUserList(String ownerScreenName, String slug, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug));
    }

    private UserList updateUserList(String newListName, boolean isPublicList, String newDescription, HttpParameter... params) throws TwitterException {
        List<HttpParameter> httpParams = new ArrayList<>();
        Collections.addAll(httpParams, params);
        if (newListName != null) {
            httpParams.add(new HttpParameter("name", newListName));
        }
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            httpParams.add(new HttpParameter("description", newDescription));
        }
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/update.json", (HttpParameter[]) httpParams.toArray(new HttpParameter[httpParams.size()])));
    }

    public UserList createUserList(String listName, boolean isPublicList, String description) throws TwitterException {
        List<HttpParameter> httpParams = new ArrayList<>();
        httpParams.add(new HttpParameter("name", listName));
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            httpParams.add(new HttpParameter("description", description));
        }
        return this.factory.createAUserList(post(this.conf.getRestBaseURL() + "lists/create.json", (HttpParameter[]) httpParams.toArray(new HttpParameter[httpParams.size()])));
    }

    public UserList showUserList(int listId) throws TwitterException {
        return this.factory.createAUserList(get(this.conf.getRestBaseURL() + "lists/show.json?list_id=" + listId));
    }

    public UserList showUserList(long ownerId, String slug) throws TwitterException {
        return this.factory.createAUserList(get(this.conf.getRestBaseURL() + "lists/show.json?owner_id=" + ownerId + "&slug=" + slug));
    }

    public UserList showUserList(String ownerScreenName, String slug) throws TwitterException {
        return this.factory.createAUserList(get(this.conf.getRestBaseURL() + "lists/show.json?owner_screen_name=" + ownerScreenName + "&slug=" + slug));
    }

    public PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor) throws TwitterException {
        return this.factory.createPagableUserListList(get(this.conf.getRestBaseURL() + "lists/subscriptions.json?screen_name=" + listOwnerScreenName + "&cursor=" + cursor));
    }

    public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
        return this.factory.createSavedSearchList(get(this.conf.getRestBaseURL() + "saved_searches/list.json"));
    }

    public SavedSearch showSavedSearch(int id) throws TwitterException {
        return this.factory.createSavedSearch(get(this.conf.getRestBaseURL() + "saved_searches/show/" + id + ".json"));
    }

    public SavedSearch createSavedSearch(String query) throws TwitterException {
        return this.factory.createSavedSearch(post(this.conf.getRestBaseURL() + "saved_searches/create.json", new HttpParameter[]{new HttpParameter("query", query)}));
    }

    public SavedSearch destroySavedSearch(int id) throws TwitterException {
        return this.factory.createSavedSearch(post(this.conf.getRestBaseURL() + "saved_searches/destroy/" + id + ".json"));
    }

    public Place getGeoDetails(String placeId) throws TwitterException {
        return this.factory.createPlace(get(this.conf.getRestBaseURL() + "geo/id/" + placeId + ".json"));
    }

    public ResponseList<Place> reverseGeoCode(GeoQuery query) throws TwitterException {
        try {
            return this.factory.createPlaceList(get(this.conf.getRestBaseURL() + "geo/reverse_geocode.json", query.asHttpParameterArray()));
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return this.factory.createEmptyResponseList();
            }
            throw te;
        }
    }

    public ResponseList<Place> searchPlaces(GeoQuery query) throws TwitterException {
        return this.factory.createPlaceList(get(this.conf.getRestBaseURL() + "geo/search.json", query.asHttpParameterArray()));
    }

    public SimilarPlaces getSimilarPlaces(GeoLocation location, String name, String containedWithin, String streetAddress) throws TwitterException {
        List<HttpParameter> params = new ArrayList<>(3);
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        params.add(new HttpParameter("name", name));
        if (containedWithin != null) {
            params.add(new HttpParameter("contained_within", containedWithin));
        }
        if (streetAddress != null) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return this.factory.createSimilarPlaces(get(this.conf.getRestBaseURL() + "geo/similar_places.json", (HttpParameter[]) params.toArray(new HttpParameter[params.size()])));
    }

    public Place createPlace(String name, String containedWithin, String token, GeoLocation location, String streetAddress) throws TwitterException {
        List<HttpParameter> params = new ArrayList<>(3);
        params.add(new HttpParameter("name", name));
        params.add(new HttpParameter("contained_within", containedWithin));
        params.add(new HttpParameter("token", token));
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        if (streetAddress != null) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return this.factory.createPlace(post(this.conf.getRestBaseURL() + "geo/place.json", (HttpParameter[]) params.toArray(new HttpParameter[params.size()])));
    }

    public Trends getLocationTrends(int woeid) throws TwitterException {
        return getPlaceTrends(woeid);
    }

    public Trends getPlaceTrends(int woeid) throws TwitterException {
        return this.factory.createTrends(get(this.conf.getRestBaseURL() + "trends/place.json?id=" + woeid));
    }

    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return this.factory.createLocationList(get(this.conf.getRestBaseURL() + "trends/available.json"));
    }

    public ResponseList<Location> getAvailableTrends(GeoLocation location) throws TwitterException {
        return getClosestTrends(location);
    }

    public ResponseList<Location> getClosestTrends(GeoLocation location) throws TwitterException {
        return this.factory.createLocationList(get(this.conf.getRestBaseURL() + "trends/closest.json", new HttpParameter[]{new HttpParameter("lat", location.getLatitude()), new HttpParameter("long", location.getLongitude())}));
    }

    public User reportSpam(long userId) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "users/report_spam.json?user_id=" + userId));
    }

    public User reportSpam(String screenName) throws TwitterException {
        return this.factory.createUser(post(this.conf.getRestBaseURL() + "users/report_spam.json?screen_name=" + screenName));
    }

    public TwitterAPIConfiguration getAPIConfiguration() throws TwitterException {
        return this.factory.createTwitterAPIConfiguration(get(this.conf.getRestBaseURL() + "help/configuration.json"));
    }

    public ResponseList<HelpResources.Language> getLanguages() throws TwitterException {
        return this.factory.createLanguageList(get(this.conf.getRestBaseURL() + "help/languages.json"));
    }

    public String getPrivacyPolicy() throws TwitterException {
        try {
            return get(this.conf.getRestBaseURL() + "help/privacy.json").asJSONObject().getString("privacy");
        } catch (JSONException e) {
            throw new TwitterException((Exception) e);
        }
    }

    public String getTermsOfService() throws TwitterException {
        try {
            return get(this.conf.getRestBaseURL() + "help/tos.json").asJSONObject().getString("tos");
        } catch (JSONException e) {
            throw new TwitterException((Exception) e);
        }
    }

    public Map<String, RateLimitStatus> getRateLimitStatus() throws TwitterException {
        return this.factory.createRateLimitStatuses(get(this.conf.getRestBaseURL() + "application/rate_limit_status.json"));
    }

    public Map<String, RateLimitStatus> getRateLimitStatus(String... resources) throws TwitterException {
        return this.factory.createRateLimitStatuses(get(this.conf.getRestBaseURL() + "application/rate_limit_status.json?resources=" + z_T4JInternalStringUtil.join(resources)));
    }

    public RelatedResults getRelatedResults(long statusId) throws TwitterException {
        return this.factory.createRelatedResults(get("https://api.twitter.com/1/related_results/show.json?id=" + Long.toString(statusId)));
    }

    private HttpResponse get(String url) throws TwitterException {
        String url2;
        ensureAuthorizationEnabled();
        if (url.contains("?")) {
            url2 = url + "&" + this.IMPLICIT_PARAMS_STR;
        } else {
            url2 = url + "?" + this.IMPLICIT_PARAMS_STR;
        }
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url2, this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.get(url2, this.auth);
            return response;
        } finally {
            TwitterAPIMonitor.getInstance().methodCalled(url2, System.currentTimeMillis() - start, isOk(response));
        }
    }

    private HttpResponse get(String url, HttpParameter[] params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url, mergeImplicitParams(params), this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.get(url, mergeImplicitParams(params), this.auth);
            return response;
        } finally {
            TwitterAPIMonitor.getInstance().methodCalled(url, System.currentTimeMillis() - start, isOk(response));
        }
    }

    private HttpResponse post(String url) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, this.IMPLICIT_PARAMS, this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.post(url, this.IMPLICIT_PARAMS, this.auth);
            return response;
        } finally {
            TwitterAPIMonitor.getInstance().methodCalled(url, System.currentTimeMillis() - start, isOk(response));
        }
    }

    private HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, mergeImplicitParams(params), this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.post(url, mergeImplicitParams(params), this.auth);
            return response;
        } finally {
            TwitterAPIMonitor.getInstance().methodCalled(url, System.currentTimeMillis() - start, isOk(response));
        }
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter[] params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[(params1.length + params2.length)];
            System.arraycopy(params1, 0, params, 0, params1.length);
            System.arraycopy(params2, 0, params, params1.length, params2.length);
            return params;
        } else if (params1 == null && params2 == null) {
            return new HttpParameter[0];
        } else {
            if (params1 != null) {
                return params1;
            }
            return params2;
        }
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[(params1.length + 1)];
            System.arraycopy(params1, 0, params, 0, params1.length);
            params[params.length - 1] = params2;
            return params;
        } else if (params1 == null && params2 == null) {
            return new HttpParameter[0];
        } else {
            if (params1 != null) {
                return params1;
            }
            return new HttpParameter[]{params2};
        }
    }

    private HttpParameter[] mergeImplicitParams(HttpParameter[] params) {
        return mergeParameters(params, this.IMPLICIT_PARAMS);
    }

    private boolean isOk(HttpResponse response) {
        return response != null && response.getStatusCode() < 300;
    }

    public String toString() {
        return "TwitterImpl{INCLUDE_MY_RETWEET=" + this.INCLUDE_MY_RETWEET + '}';
    }
}
