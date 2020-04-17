package twitter4j.json;

import com.google.analytics.tracking.android.HitTypes;
import com.google.android.gcm.GCMConstants;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public final class JSONObjectType {
    private static final Logger logger = Logger.getLogger(JSONObjectType.class);

    public enum Type {
        SENDER,
        STATUS,
        DIRECT_MESSAGE,
        DELETE,
        LIMIT,
        STALL_WARNING,
        SCRUB_GEO,
        FRIENDS,
        FAVORITE,
        UNFAVORITE,
        FOLLOW,
        UNFOLLOW,
        USER_LIST_MEMBER_ADDED,
        USER_LIST_MEMBER_DELETED,
        USER_LIST_SUBSCRIBED,
        USER_LIST_UNSUBSCRIBED,
        USER_LIST_CREATED,
        USER_LIST_UPDATED,
        USER_LIST_DESTROYED,
        USER_UPDATE,
        BLOCK,
        UNBLOCK,
        DISCONNECTION,
        UNKNOWN
    }

    public static Type determine(JSONObject json) {
        if (!json.isNull(GCMConstants.EXTRA_SENDER)) {
            return Type.SENDER;
        }
        if (!json.isNull("text")) {
            return Type.STATUS;
        }
        if (!json.isNull("direct_message")) {
            return Type.DIRECT_MESSAGE;
        }
        if (!json.isNull("delete")) {
            return Type.DELETE;
        }
        if (!json.isNull("limit")) {
            return Type.LIMIT;
        }
        if (!json.isNull("warning")) {
            return Type.STALL_WARNING;
        }
        if (!json.isNull("scrub_geo")) {
            return Type.SCRUB_GEO;
        }
        if (!json.isNull("friends")) {
            return Type.FRIENDS;
        }
        if (!json.isNull(HitTypes.EVENT)) {
            try {
                String event = json.getString(HitTypes.EVENT);
                if ("favorite".equals(event)) {
                    return Type.FAVORITE;
                }
                if ("unfavorite".equals(event)) {
                    return Type.UNFAVORITE;
                }
                if ("follow".equals(event)) {
                    return Type.FOLLOW;
                }
                if ("unfollow".equals(event)) {
                    return Type.UNFOLLOW;
                }
                if (event.startsWith(AppLauncherConsts.REQUEST_PARAM_GENERAL_TYPE_LIST)) {
                    if ("list_member_added".equals(event)) {
                        return Type.USER_LIST_MEMBER_ADDED;
                    }
                    if ("list_member_removed".equals(event)) {
                        return Type.USER_LIST_MEMBER_DELETED;
                    }
                    if ("list_user_subscribed".equals(event)) {
                        return Type.USER_LIST_SUBSCRIBED;
                    }
                    if ("list_user_unsubscribed".equals(event)) {
                        return Type.USER_LIST_UNSUBSCRIBED;
                    }
                    if ("list_created".equals(event)) {
                        return Type.USER_LIST_CREATED;
                    }
                    if ("list_updated".equals(event)) {
                        return Type.USER_LIST_UPDATED;
                    }
                    if ("list_destroyed".equals(event)) {
                        return Type.USER_LIST_DESTROYED;
                    }
                } else if ("user_update".equals(event)) {
                    return Type.USER_UPDATE;
                } else {
                    if ("block".equals(event)) {
                        return Type.BLOCK;
                    }
                    if ("unblock".equals(event)) {
                        return Type.UNBLOCK;
                    }
                }
            } catch (JSONException e) {
                try {
                    logger.warn("Failed to get event element: ", json.toString(2));
                } catch (JSONException e2) {
                }
            }
        } else if (!json.isNull("disconnect")) {
            return Type.DISCONNECTION;
        }
        return Type.UNKNOWN;
    }
}
