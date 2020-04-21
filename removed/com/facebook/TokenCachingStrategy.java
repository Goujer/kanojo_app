package com.facebook;

import android.os.Bundle;
import com.facebook.internal.Validate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class TokenCachingStrategy {
    public static final String EXPIRATION_DATE_KEY = "com.facebook.TokenCachingStrategy.ExpirationDate";
    private static final long INVALID_BUNDLE_MILLISECONDS = Long.MIN_VALUE;
    private static final String IS_SSO_KEY = "com.facebook.TokenCachingStrategy.IsSSO";
    public static final String LAST_REFRESH_DATE_KEY = "com.facebook.TokenCachingStrategy.LastRefreshDate";
    public static final String PERMISSIONS_KEY = "com.facebook.TokenCachingStrategy.Permissions";
    public static final String TOKEN_KEY = "com.facebook.TokenCachingStrategy.Token";
    public static final String TOKEN_SOURCE_KEY = "com.facebook.TokenCachingStrategy.AccessTokenSource";
    public static final String USER_FBID_KEY = "com.facebook.TokenCachingStrategy.UserFBID";

    public abstract void clear();

    public abstract Bundle load();

    public abstract void save(Bundle bundle);

    public static boolean hasTokenInformation(Bundle bundle) {
        String token;
        if (bundle == null || (token = bundle.getString(TOKEN_KEY)) == null || token.length() == 0 || bundle.getLong(EXPIRATION_DATE_KEY, 0) == 0) {
            return false;
        }
        return true;
    }

    public static String getToken(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return bundle.getString(TOKEN_KEY);
    }

    public static void putToken(Bundle bundle, String value) {
        Validate.notNull(bundle, "bundle");
        Validate.notNull(value, "value");
        bundle.putString(TOKEN_KEY, value);
    }

    public static Date getExpirationDate(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return getDate(bundle, EXPIRATION_DATE_KEY);
    }

    public static void putExpirationDate(Bundle bundle, Date value) {
        Validate.notNull(bundle, "bundle");
        Validate.notNull(value, "value");
        putDate(bundle, EXPIRATION_DATE_KEY, value);
    }

    public static long getExpirationMilliseconds(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return bundle.getLong(EXPIRATION_DATE_KEY);
    }

    public static void putExpirationMilliseconds(Bundle bundle, long value) {
        Validate.notNull(bundle, "bundle");
        bundle.putLong(EXPIRATION_DATE_KEY, value);
    }

    public static List<String> getPermissions(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return bundle.getStringArrayList(PERMISSIONS_KEY);
    }

    public static void putPermissions(Bundle bundle, List<String> value) {
        ArrayList<String> arrayList;
        Validate.notNull(bundle, "bundle");
        Validate.notNull(value, "value");
        if (value instanceof ArrayList) {
            arrayList = (ArrayList) value;
        } else {
            arrayList = new ArrayList<>(value);
        }
        bundle.putStringArrayList(PERMISSIONS_KEY, arrayList);
    }

    public static AccessTokenSource getSource(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        if (bundle.containsKey(TOKEN_SOURCE_KEY)) {
            return (AccessTokenSource) bundle.getSerializable(TOKEN_SOURCE_KEY);
        }
        return bundle.getBoolean(IS_SSO_KEY) ? AccessTokenSource.FACEBOOK_APPLICATION_WEB : AccessTokenSource.WEB_VIEW;
    }

    public static void putSource(Bundle bundle, AccessTokenSource value) {
        Validate.notNull(bundle, "bundle");
        bundle.putSerializable(TOKEN_SOURCE_KEY, value);
    }

    public static Date getLastRefreshDate(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return getDate(bundle, LAST_REFRESH_DATE_KEY);
    }

    public static void putLastRefreshDate(Bundle bundle, Date value) {
        Validate.notNull(bundle, "bundle");
        Validate.notNull(value, "value");
        putDate(bundle, LAST_REFRESH_DATE_KEY, value);
    }

    public static long getLastRefreshMilliseconds(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return bundle.getLong(LAST_REFRESH_DATE_KEY);
    }

    public static void putLastRefreshMilliseconds(Bundle bundle, long value) {
        Validate.notNull(bundle, "bundle");
        bundle.putLong(LAST_REFRESH_DATE_KEY, value);
    }

    static Date getDate(Bundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        long n = bundle.getLong(key, INVALID_BUNDLE_MILLISECONDS);
        if (n != INVALID_BUNDLE_MILLISECONDS) {
            return new Date(n);
        }
        return null;
    }

    static void putDate(Bundle bundle, String key, Date date) {
        bundle.putLong(key, date.getTime());
    }
}
