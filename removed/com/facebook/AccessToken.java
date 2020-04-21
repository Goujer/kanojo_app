package com.facebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class AccessToken implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = (!AccessToken.class.desiredAssertionStatus());
    static final String ACCESS_TOKEN_KEY = "access_token";
    private static final Date ALREADY_EXPIRED_EXPIRATION_TIME = MIN_DATE;
    private static final AccessTokenSource DEFAULT_ACCESS_TOKEN_SOURCE = AccessTokenSource.FACEBOOK_APPLICATION_WEB;
    private static final Date DEFAULT_EXPIRATION_TIME = MAX_DATE;
    private static final Date DEFAULT_LAST_REFRESH_TIME = new Date();
    static final String EXPIRES_IN_KEY = "expires_in";
    private static final Date MAX_DATE = new Date(Long.MAX_VALUE);
    private static final Date MIN_DATE = new Date(Long.MIN_VALUE);
    private static final long serialVersionUID = 1;
    private final Date expires;
    private final Date lastRefresh;
    private final List<String> permissions;
    private final AccessTokenSource source;
    private final String token;

    AccessToken(String token2, Date expires2, List<String> permissions2, AccessTokenSource source2, Date lastRefresh2) {
        permissions2 = permissions2 == null ? Collections.emptyList() : permissions2;
        this.expires = expires2;
        this.permissions = Collections.unmodifiableList(permissions2);
        this.token = token2;
        this.source = source2;
        this.lastRefresh = lastRefresh2;
    }

    public String getToken() {
        return this.token;
    }

    public Date getExpires() {
        return this.expires;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public AccessTokenSource getSource() {
        return this.source;
    }

    public Date getLastRefresh() {
        return this.lastRefresh;
    }

    public static AccessToken createFromExistingAccessToken(String accessToken, Date expirationTime, Date lastRefreshTime, AccessTokenSource accessTokenSource, List<String> permissions2) {
        if (expirationTime == null) {
            expirationTime = DEFAULT_EXPIRATION_TIME;
        }
        if (lastRefreshTime == null) {
            lastRefreshTime = DEFAULT_LAST_REFRESH_TIME;
        }
        if (accessTokenSource == null) {
            accessTokenSource = DEFAULT_ACCESS_TOKEN_SOURCE;
        }
        return new AccessToken(accessToken, expirationTime, permissions2, accessTokenSource, lastRefreshTime);
    }

    public static AccessToken createFromNativeLinkingIntent(Intent intent) {
        Validate.notNull(intent, "intent");
        if (intent.getExtras() == null) {
            return null;
        }
        return createFromBundle((List<String>) null, intent.getExtras(), AccessTokenSource.FACEBOOK_APPLICATION_WEB, new Date());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{AccessToken");
        builder.append(" token:").append(tokenToString());
        appendPermissions(builder);
        builder.append("}");
        return builder.toString();
    }

    static AccessToken createEmptyToken(List<String> permissions2) {
        return new AccessToken("", ALREADY_EXPIRED_EXPIRATION_TIME, permissions2, AccessTokenSource.NONE, DEFAULT_LAST_REFRESH_TIME);
    }

    static AccessToken createFromString(String token2, List<String> permissions2, AccessTokenSource source2) {
        return new AccessToken(token2, DEFAULT_EXPIRATION_TIME, permissions2, source2, DEFAULT_LAST_REFRESH_TIME);
    }

    static AccessToken createFromNativeLogin(Bundle bundle, AccessTokenSource source2) {
        return createNew(bundle.getStringArrayList("com.facebook.platform.extra.PERMISSIONS"), bundle.getString("com.facebook.platform.extra.ACCESS_TOKEN"), getBundleLongAsDate(bundle, "com.facebook.platform.extra.EXPIRES_SECONDS_SINCE_EPOCH", new Date(0)), source2);
    }

    static AccessToken createFromWebBundle(List<String> requestedPermissions, Bundle bundle, AccessTokenSource source2) {
        return createNew(requestedPermissions, bundle.getString("access_token"), getBundleLongAsDate(bundle, "expires_in", new Date()), source2);
    }

    @SuppressLint({"FieldGetter"})
    static AccessToken createFromRefresh(AccessToken current, Bundle bundle) {
        if ($assertionsDisabled || current.source == AccessTokenSource.FACEBOOK_APPLICATION_WEB || current.source == AccessTokenSource.FACEBOOK_APPLICATION_NATIVE || current.source == AccessTokenSource.FACEBOOK_APPLICATION_SERVICE) {
            Date expires2 = getBundleLongAsDate(bundle, "expires_in", new Date(0));
            return createNew(current.getPermissions(), bundle.getString("access_token"), expires2, current.source);
        }
        throw new AssertionError();
    }

    static AccessToken createFromTokenWithRefreshedPermissions(AccessToken token2, List<String> permissions2) {
        return new AccessToken(token2.token, token2.expires, permissions2, token2.source, token2.lastRefresh);
    }

    private static AccessToken createNew(List<String> requestedPermissions, String accessToken, Date expires2, AccessTokenSource source2) {
        if (Utility.isNullOrEmpty(accessToken) || expires2 == null) {
            return createEmptyToken(requestedPermissions);
        }
        return new AccessToken(accessToken, expires2, requestedPermissions, source2, new Date());
    }

    static AccessToken createFromCache(Bundle bundle) {
        List<String> permissions2;
        List<String> originalPermissions = bundle.getStringArrayList(TokenCachingStrategy.PERMISSIONS_KEY);
        if (originalPermissions == null) {
            permissions2 = Collections.emptyList();
        } else {
            permissions2 = Collections.unmodifiableList(new ArrayList(originalPermissions));
        }
        return new AccessToken(bundle.getString(TokenCachingStrategy.TOKEN_KEY), TokenCachingStrategy.getDate(bundle, TokenCachingStrategy.EXPIRATION_DATE_KEY), permissions2, TokenCachingStrategy.getSource(bundle), TokenCachingStrategy.getDate(bundle, TokenCachingStrategy.LAST_REFRESH_DATE_KEY));
    }

    /* access modifiers changed from: package-private */
    public Bundle toCacheBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(TokenCachingStrategy.TOKEN_KEY, this.token);
        TokenCachingStrategy.putDate(bundle, TokenCachingStrategy.EXPIRATION_DATE_KEY, this.expires);
        bundle.putStringArrayList(TokenCachingStrategy.PERMISSIONS_KEY, new ArrayList(this.permissions));
        bundle.putSerializable(TokenCachingStrategy.TOKEN_SOURCE_KEY, this.source);
        TokenCachingStrategy.putDate(bundle, TokenCachingStrategy.LAST_REFRESH_DATE_KEY, this.lastRefresh);
        return bundle;
    }

    /* access modifiers changed from: package-private */
    public boolean isInvalid() {
        return Utility.isNullOrEmpty(this.token) || new Date().after(this.expires);
    }

    private static AccessToken createFromBundle(List<String> requestedPermissions, Bundle bundle, AccessTokenSource source2, Date expirationBase) {
        String token2 = bundle.getString("access_token");
        Date expires2 = getBundleLongAsDate(bundle, "expires_in", expirationBase);
        if (Utility.isNullOrEmpty(token2) || expires2 == null) {
            return null;
        }
        return new AccessToken(token2, expires2, requestedPermissions, source2, new Date());
    }

    private String tokenToString() {
        if (this.token == null) {
            return "null";
        }
        if (Settings.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
            return this.token;
        }
        return "ACCESS_TOKEN_REMOVED";
    }

    private void appendPermissions(StringBuilder builder) {
        builder.append(" permissions:");
        if (this.permissions == null) {
            builder.append("null");
            return;
        }
        builder.append("[");
        builder.append(TextUtils.join(", ", this.permissions));
        builder.append("]");
    }

    private static class SerializationProxyV1 implements Serializable {
        private static final long serialVersionUID = -2488473066578201069L;
        private final Date expires;
        private final Date lastRefresh;
        private final List<String> permissions;
        private final AccessTokenSource source;
        private final String token;

        private SerializationProxyV1(String token2, Date expires2, List<String> permissions2, AccessTokenSource source2, Date lastRefresh2) {
            this.expires = expires2;
            this.permissions = permissions2;
            this.token = token2;
            this.source = source2;
            this.lastRefresh = lastRefresh2;
        }

        /* synthetic */ SerializationProxyV1(String str, Date date, List list, AccessTokenSource accessTokenSource, Date date2, SerializationProxyV1 serializationProxyV1) {
            this(str, date, list, accessTokenSource, date2);
        }

        private Object readResolve() {
            return new AccessToken(this.token, this.expires, this.permissions, this.source, this.lastRefresh);
        }
    }

    private Object writeReplace() {
        return new SerializationProxyV1(this.token, this.expires, this.permissions, this.source, this.lastRefresh, (SerializationProxyV1) null);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Cannot readObject, serialization proxy required");
    }

    private static Date getBundleLongAsDate(Bundle bundle, String key, Date dateBase) {
        long secondsFromBase;
        if (bundle == null) {
            return null;
        }
        Object secondsObject = bundle.get(key);
        if (secondsObject instanceof Long) {
            secondsFromBase = ((Long) secondsObject).longValue();
        } else if (!(secondsObject instanceof String)) {
            return null;
        } else {
            try {
                secondsFromBase = Long.parseLong((String) secondsObject);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (secondsFromBase == 0) {
            return new Date(Long.MAX_VALUE);
        }
        return new Date(dateBase.getTime() + (1000 * secondsFromBase));
    }
}
