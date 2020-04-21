package twitter4j.internal.json;

import java.io.Serializable;
import java.util.Map;
import twitter4j.AccountSettings;
import twitter4j.AccountTotals;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.Friendship;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.OEmbed;
import twitter4j.PagableResponseList;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.RelatedResults;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.SimilarPlaces;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.api.HelpResources;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONObject;

public interface z_T4JInternalFactory extends Serializable {
    UserList createAUserList(HttpResponse httpResponse) throws TwitterException;

    UserList createAUserList(JSONObject jSONObject) throws TwitterException;

    AccountSettings createAccountSettings(HttpResponse httpResponse) throws TwitterException;

    AccountTotals createAccountTotals(HttpResponse httpResponse) throws TwitterException;

    ResponseList<Category> createCategoryList(HttpResponse httpResponse) throws TwitterException;

    DirectMessage createDirectMessage(HttpResponse httpResponse) throws TwitterException;

    DirectMessage createDirectMessage(JSONObject jSONObject) throws TwitterException;

    ResponseList<DirectMessage> createDirectMessageList(HttpResponse httpResponse) throws TwitterException;

    <T> ResponseList<T> createEmptyResponseList();

    ResponseList<Friendship> createFriendshipList(HttpResponse httpResponse) throws TwitterException;

    IDs createIDs(HttpResponse httpResponse) throws TwitterException;

    ResponseList<HelpResources.Language> createLanguageList(HttpResponse httpResponse) throws TwitterException;

    ResponseList<Location> createLocationList(HttpResponse httpResponse) throws TwitterException;

    OEmbed createOEmbed(HttpResponse httpResponse) throws TwitterException;

    PagableResponseList<User> createPagableUserList(HttpResponse httpResponse) throws TwitterException;

    PagableResponseList<UserList> createPagableUserListList(HttpResponse httpResponse) throws TwitterException;

    Place createPlace(HttpResponse httpResponse) throws TwitterException;

    ResponseList<Place> createPlaceList(HttpResponse httpResponse) throws TwitterException;

    QueryResult createQueryResult(HttpResponse httpResponse, Query query) throws TwitterException;

    Map<String, RateLimitStatus> createRateLimitStatuses(HttpResponse httpResponse) throws TwitterException;

    RelatedResults createRelatedResults(HttpResponse httpResponse) throws TwitterException;

    Relationship createRelationship(HttpResponse httpResponse) throws TwitterException;

    SavedSearch createSavedSearch(HttpResponse httpResponse) throws TwitterException;

    ResponseList<SavedSearch> createSavedSearchList(HttpResponse httpResponse) throws TwitterException;

    SimilarPlaces createSimilarPlaces(HttpResponse httpResponse) throws TwitterException;

    Status createStatus(HttpResponse httpResponse) throws TwitterException;

    Status createStatus(JSONObject jSONObject) throws TwitterException;

    ResponseList<Status> createStatusList(HttpResponse httpResponse) throws TwitterException;

    Trends createTrends(HttpResponse httpResponse) throws TwitterException;

    TwitterAPIConfiguration createTwitterAPIConfiguration(HttpResponse httpResponse) throws TwitterException;

    User createUser(HttpResponse httpResponse) throws TwitterException;

    User createUser(JSONObject jSONObject) throws TwitterException;

    ResponseList<User> createUserList(HttpResponse httpResponse) throws TwitterException;

    ResponseList<User> createUserListFromJSONArray(HttpResponse httpResponse) throws TwitterException;

    ResponseList<User> createUserListFromJSONArray_Users(HttpResponse httpResponse) throws TwitterException;

    ResponseList<UserList> createUserListList(HttpResponse httpResponse) throws TwitterException;
}
