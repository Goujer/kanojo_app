package jp.co.cybird.app.android.lib.commons.tracker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import java.util.Map;

public final class TrackerWrapper {
    private static final String CAMPAIGN_SOURCE_PARAM = "utm_source";
    private static boolean isTrackerValid = false;
    private static Context mContext = null;
    private static final String senderIdKey = "ga_trackingId";

    public static void init(Context c) {
        int resId;
        if (!isTrackerValid && c != null && (resId = c.getResources().getIdentifier(senderIdKey, "string", c.getPackageName())) != 0 && !TextUtils.isEmpty(c.getResources().getString(resId))) {
            isTrackerValid = true;
            mContext = c;
            EasyTracker.getInstance(mContext);
        }
    }

    public static void sendEvent(String category, String action, String label, long value) {
        if (isTrackerValid) {
            EasyTracker.getInstance(mContext).send(MapBuilder.createEvent(category, action, label, Long.valueOf(value)).build());
        }
    }

    public static void setView(String viewName) {
        if (isTrackerValid) {
            EasyTracker.getInstance(mContext).set("&cd", viewName);
        }
    }

    public static void sendView() {
        if (isTrackerValid) {
            EasyTracker.getInstance(mContext).send(MapBuilder.createAppView().build());
        }
    }

    public static void sendView(String viewName) {
        if (isTrackerValid) {
            EasyTracker.getInstance(mContext).set("&cd", viewName);
            EasyTracker.getInstance(mContext).send(MapBuilder.createAppView().build());
        }
    }

    public static void autoActivityStart(Activity a) {
        if (isTrackerValid) {
            EasyTracker.getInstance(mContext).activityStart(a);
        }
    }

    public static void autoActivityStop(Activity a) {
        if (isTrackerValid) {
            EasyTracker.getInstance(mContext).activityStop(a);
        }
    }

    public static void setCampaign(Uri uri) {
        if (isTrackerValid) {
            EasyTracker.getInstance(mContext).send(MapBuilder.createAppView().setAll(getReferrerMapFromUri(uri)).build());
        }
    }

    private static Map<String, String> getReferrerMapFromUri(Uri uri) {
        MapBuilder paramMap = new MapBuilder();
        if (uri == null) {
            return paramMap.build();
        }
        if (uri.getQueryParameter(CAMPAIGN_SOURCE_PARAM) != null) {
            paramMap.setCampaignParamsFromUrl(uri.toString());
        } else if (uri.getAuthority() != null) {
            paramMap.set(Fields.CAMPAIGN_MEDIUM, "referral");
            paramMap.set(Fields.CAMPAIGN_SOURCE, uri.getAuthority());
        }
        return paramMap.build();
    }

    public static void sendTransaction(String orderId, double cost, String affiliation, double tax, double shipping, String SKU, String productName, long quantity, String productCategory, String currencyCode) {
        if (isTrackerValid) {
            EasyTracker easyTracker = EasyTracker.getInstance(mContext);
            easyTracker.send(MapBuilder.createTransaction(orderId, affiliation, Double.valueOf(cost), Double.valueOf(tax), Double.valueOf(shipping), currencyCode).build());
            easyTracker.send(MapBuilder.createItem(orderId, productName, SKU, productCategory, Double.valueOf(cost), Long.valueOf(quantity), currencyCode).build());
        }
    }

    public static boolean isAppOptOut() {
        if (isTrackerValid) {
            return GoogleAnalytics.getInstance(mContext).getAppOptOut();
        }
        return false;
    }

    public static void setAppOptOut(boolean optOut) {
        if (isTrackerValid) {
            GoogleAnalytics.getInstance(mContext).setAppOptOut(optOut);
        }
    }
}
