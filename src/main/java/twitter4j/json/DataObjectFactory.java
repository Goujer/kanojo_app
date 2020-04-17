package twitter4j.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import jp.co.cybird.barcodekanojoForGAM.gree.core.model.Inspection;
import twitter4j.AccountTotals;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.OEmbed;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.RelatedResults;
import twitter4j.Relationship;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public final class DataObjectFactory {
    private static final Constructor<IDs> IDsConstructor;
    private static final Constructor<AccountTotals> accountTotalsConstructor;
    private static final Constructor<Category> categoryConstructor;
    private static final Constructor<DirectMessage> directMessageConstructor;
    private static final Constructor<Location> locationConstructor;
    private static final Constructor<OEmbed> oembedConstructor;
    private static final Constructor<Place> placeConstructor;
    private static final Method rateLimitStatusConstructor;
    private static final ThreadLocal<Map> rawJsonMap = new ThreadLocal<Map>() {
        /* access modifiers changed from: protected */
        public Map initialValue() {
            return new HashMap();
        }
    };
    private static final Constructor<RelatedResults> relatedResultsConstructor;
    private static final Constructor<Relationship> relationshipConstructor;
    private static final Constructor<SavedSearch> savedSearchConstructor;
    private static final Constructor<Status> statusConstructor;
    private static final Constructor<StatusDeletionNotice> statusDeletionNoticeConstructor;
    private static final Constructor<Trend> trendConstructor;
    private static final Constructor<Trends> trendsConstructor;
    private static final Constructor<User> userConstructor;
    private static final Constructor<UserList> userListConstructor;

    private DataObjectFactory() {
        throw new AssertionError("not intended to be instantiated.");
    }

    static {
        try {
            statusConstructor = Class.forName("twitter4j.internal.json.StatusJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            statusConstructor.setAccessible(true);
            userConstructor = Class.forName("twitter4j.internal.json.UserJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            userConstructor.setAccessible(true);
            relationshipConstructor = Class.forName("twitter4j.internal.json.RelationshipJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            relationshipConstructor.setAccessible(true);
            placeConstructor = Class.forName("twitter4j.internal.json.PlaceJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            placeConstructor.setAccessible(true);
            savedSearchConstructor = Class.forName("twitter4j.internal.json.SavedSearchJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            savedSearchConstructor.setAccessible(true);
            trendConstructor = Class.forName("twitter4j.internal.json.TrendJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            trendConstructor.setAccessible(true);
            trendsConstructor = Class.forName("twitter4j.internal.json.TrendsJSONImpl").getDeclaredConstructor(new Class[]{String.class});
            trendsConstructor.setAccessible(true);
            IDsConstructor = Class.forName("twitter4j.internal.json.IDsJSONImpl").getDeclaredConstructor(new Class[]{String.class});
            IDsConstructor.setAccessible(true);
            rateLimitStatusConstructor = Class.forName("twitter4j.internal.json.RateLimitStatusJSONImpl").getDeclaredMethod("createRateLimitStatuses", new Class[]{JSONObject.class});
            rateLimitStatusConstructor.setAccessible(true);
            categoryConstructor = Class.forName("twitter4j.internal.json.CategoryJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            categoryConstructor.setAccessible(true);
            directMessageConstructor = Class.forName("twitter4j.internal.json.DirectMessageJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            directMessageConstructor.setAccessible(true);
            locationConstructor = Class.forName("twitter4j.internal.json.LocationJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            locationConstructor.setAccessible(true);
            userListConstructor = Class.forName("twitter4j.internal.json.UserListJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            userListConstructor.setAccessible(true);
            relatedResultsConstructor = Class.forName("twitter4j.internal.json.RelatedResultsJSONImpl").getDeclaredConstructor(new Class[]{JSONArray.class});
            relatedResultsConstructor.setAccessible(true);
            statusDeletionNoticeConstructor = Class.forName("twitter4j.StatusDeletionNoticeImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            statusDeletionNoticeConstructor.setAccessible(true);
            accountTotalsConstructor = Class.forName("twitter4j.internal.json.AccountTotalsJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            accountTotalsConstructor.setAccessible(true);
            oembedConstructor = Class.forName("twitter4j.internal.json.OEmbedJSONImpl").getDeclaredConstructor(new Class[]{JSONObject.class});
            oembedConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        } catch (ClassNotFoundException e2) {
            throw new ExceptionInInitializerError(e2);
        }
    }

    public static String getRawJSON(Object obj) {
        Object json = rawJsonMap.get().get(obj);
        if (json instanceof String) {
            return (String) json;
        }
        if (json != null) {
            return json.toString();
        }
        return null;
    }

    public static Status createStatus(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return statusConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static User createUser(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return userConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static AccountTotals createAccountTotals(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return accountTotalsConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static Relationship createRelationship(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return relationshipConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static Place createPlace(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return placeConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static SavedSearch createSavedSearch(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return savedSearchConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static Trend createTrend(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return trendConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static Trends createTrends(String rawJSON) throws TwitterException {
        try {
            return trendsConstructor.newInstance(new Object[]{rawJSON});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new TwitterException((Exception) e2);
        } catch (InvocationTargetException e3) {
            throw new AssertionError(e3);
        }
    }

    public static IDs createIDs(String rawJSON) throws TwitterException {
        try {
            return IDsConstructor.newInstance(new Object[]{rawJSON});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        }
    }

    public static Map<String, RateLimitStatus> createRateLimitStatus(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return (Map) rateLimitStatusConstructor.invoke(Class.forName("twitter4j.internal.json.RateLimitStatusJSONImpl"), new Object[]{json});
        } catch (ClassNotFoundException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static Category createCategory(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return categoryConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static DirectMessage createDirectMessage(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return directMessageConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static Location createLocation(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return locationConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static UserList createUserList(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return userListConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static RelatedResults createRelatedResults(String rawJSON) throws TwitterException {
        try {
            JSONArray json = new JSONArray(rawJSON);
            return relatedResultsConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static OEmbed createOEmbed(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            return oembedConstructor.newInstance(new Object[]{json});
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    public static Object createObject(String rawJSON) throws TwitterException {
        try {
            JSONObject json = new JSONObject(rawJSON);
            switch (JSONObjectType.determine(json)) {
                case SENDER:
                    return registerJSONObject(directMessageConstructor.newInstance(new Object[]{json.getJSONObject("direct_message")}), json);
                case STATUS:
                    return registerJSONObject(statusConstructor.newInstance(new Object[]{json}), json);
                case DIRECT_MESSAGE:
                    return registerJSONObject(directMessageConstructor.newInstance(new Object[]{json.getJSONObject("direct_message")}), json);
                case DELETE:
                    return registerJSONObject(statusDeletionNoticeConstructor.newInstance(new Object[]{json.getJSONObject("delete").getJSONObject(Inspection.STATUS)}), json);
                default:
                    return json;
            }
        } catch (InstantiationException e) {
            throw new TwitterException((Exception) e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new TwitterException((Exception) e3);
        } catch (JSONException e4) {
            throw new TwitterException((Exception) e4);
        }
    }

    static void clearThreadLocalMap() {
        rawJsonMap.get().clear();
    }

    static <T> T registerJSONObject(T key, Object json) {
        rawJsonMap.get().put(key, json);
        return key;
    }
}
