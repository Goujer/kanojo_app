package com.facebook;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.AuthorizationClient;
import com.facebook.internal.SessionAuthorizationType;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Session implements Serializable {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$facebook$SessionState = null;
    public static final String ACTION_ACTIVE_SESSION_CLOSED = "com.facebook.sdk.ACTIVE_SESSION_CLOSED";
    public static final String ACTION_ACTIVE_SESSION_OPENED = "com.facebook.sdk.ACTIVE_SESSION_OPENED";
    public static final String ACTION_ACTIVE_SESSION_SET = "com.facebook.sdk.ACTIVE_SESSION_SET";
    public static final String ACTION_ACTIVE_SESSION_UNSET = "com.facebook.sdk.ACTIVE_SESSION_UNSET";
    public static final String APPLICATION_ID_PROPERTY = "com.facebook.sdk.ApplicationId";
    private static final String AUTH_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.authBundleKey";
    public static final int DEFAULT_AUTHORIZE_ACTIVITY_CODE = 64206;
    private static final String MANAGE_PERMISSION_PREFIX = "manage";
    private static final Set<String> OTHER_PUBLISH_PERMISSIONS = new HashSet<String>() {
        {
            add("ads_management");
            add("create_event");
            add("rsvp_event");
        }
    };
    private static final String PUBLISH_PERMISSION_PREFIX = "publish";
    private static final String SESSION_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.saveSessionKey";
    private static final Object STATIC_LOCK = new Object();
    public static final String TAG = Session.class.getCanonicalName();
    private static final int TOKEN_EXTEND_RETRY_SECONDS = 3600;
    private static final int TOKEN_EXTEND_THRESHOLD_SECONDS = 86400;
    public static final String WEB_VIEW_ERROR_CODE_KEY = "com.facebook.sdk.WebViewErrorCode";
    public static final String WEB_VIEW_FAILING_URL_KEY = "com.facebook.sdk.FailingUrl";
    private static Session activeSession = null;
    private static final long serialVersionUID = 1;
    /* access modifiers changed from: private */
    public static volatile Context staticContext;
    private String applicationId;
    private volatile Bundle authorizationBundle;
    private AuthorizationClient authorizationClient;
    /* access modifiers changed from: private */
    public AutoPublishAsyncTask autoPublishAsyncTask;
    /* access modifiers changed from: private */
    public final List<StatusCallback> callbacks;
    /* access modifiers changed from: private */
    public volatile TokenRefreshRequest currentTokenRefreshRequest;
    /* access modifiers changed from: private */
    public Handler handler;
    private Date lastAttemptedTokenExtendDate;
    private final Object lock;
    private AuthorizationRequest pendingRequest;
    private SessionState state;
    private TokenCachingStrategy tokenCachingStrategy;
    private AccessToken tokenInfo;

    interface StartActivityDelegate {
        Activity getActivityContext();

        void startActivityForResult(Intent intent, int i);
    }

    public interface StatusCallback {
        void call(Session session, SessionState sessionState, Exception exc);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$facebook$SessionState() {
        int[] iArr = $SWITCH_TABLE$com$facebook$SessionState;
        if (iArr == null) {
            iArr = new int[SessionState.values().length];
            try {
                iArr[SessionState.CLOSED.ordinal()] = 7;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SessionState.CLOSED_LOGIN_FAILED.ordinal()] = 6;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SessionState.CREATED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[SessionState.CREATED_TOKEN_LOADED.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[SessionState.OPENED.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[SessionState.OPENED_TOKEN_UPDATED.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[SessionState.OPENING.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            $SWITCH_TABLE$com$facebook$SessionState = iArr;
        }
        return iArr;
    }

    private static class SerializationProxyV1 implements Serializable {
        private static final long serialVersionUID = 7663436173185080063L;
        private final String applicationId;
        private final Date lastAttemptedTokenExtendDate;
        private final AuthorizationRequest pendingRequest;
        private final boolean shouldAutoPublish;
        private final SessionState state;
        private final AccessToken tokenInfo;

        SerializationProxyV1(String applicationId2, SessionState state2, AccessToken tokenInfo2, Date lastAttemptedTokenExtendDate2, boolean shouldAutoPublish2, AuthorizationRequest pendingRequest2) {
            this.applicationId = applicationId2;
            this.state = state2;
            this.tokenInfo = tokenInfo2;
            this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate2;
            this.shouldAutoPublish = shouldAutoPublish2;
            this.pendingRequest = pendingRequest2;
        }

        private Object readResolve() {
            return new Session(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, this.shouldAutoPublish, this.pendingRequest, (Session) null);
        }
    }

    private Session(String applicationId2, SessionState state2, AccessToken tokenInfo2, Date lastAttemptedTokenExtendDate2, boolean shouldAutoPublish, AuthorizationRequest pendingRequest2) {
        this.lastAttemptedTokenExtendDate = new Date(0);
        this.lock = new Object();
        this.applicationId = applicationId2;
        this.state = state2;
        this.tokenInfo = tokenInfo2;
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate2;
        this.pendingRequest = pendingRequest2;
        this.handler = new Handler(Looper.getMainLooper());
        this.currentTokenRefreshRequest = null;
        this.tokenCachingStrategy = null;
        this.callbacks = new ArrayList();
    }

    /* synthetic */ Session(String str, SessionState sessionState, AccessToken accessToken, Date date, boolean z, AuthorizationRequest authorizationRequest, Session session) {
        this(str, sessionState, accessToken, date, z, authorizationRequest);
    }

    public Session(Context currentContext) {
        this(currentContext, (String) null, (TokenCachingStrategy) null, true);
    }

    Session(Context context, String applicationId2, TokenCachingStrategy tokenCachingStrategy2) {
        this(context, applicationId2, tokenCachingStrategy2, true);
    }

    Session(Context context, String applicationId2, TokenCachingStrategy tokenCachingStrategy2, boolean loadTokenFromCache) {
        Bundle tokenState = null;
        this.lastAttemptedTokenExtendDate = new Date(0);
        this.lock = new Object();
        if (context != null && applicationId2 == null) {
            applicationId2 = Utility.getMetadataApplicationId(context);
        }
        Validate.notNull(applicationId2, "applicationId");
        initializeStaticContext(context);
        tokenCachingStrategy2 = tokenCachingStrategy2 == null ? new SharedPreferencesTokenCachingStrategy(staticContext) : tokenCachingStrategy2;
        this.applicationId = applicationId2;
        this.tokenCachingStrategy = tokenCachingStrategy2;
        this.state = SessionState.CREATED;
        this.pendingRequest = null;
        this.callbacks = new ArrayList();
        this.handler = new Handler(Looper.getMainLooper());
        tokenState = loadTokenFromCache ? tokenCachingStrategy2.load() : tokenState;
        if (TokenCachingStrategy.hasTokenInformation(tokenState)) {
            Date cachedExpirationDate = TokenCachingStrategy.getDate(tokenState, TokenCachingStrategy.EXPIRATION_DATE_KEY);
            Date now = new Date();
            if (cachedExpirationDate == null || cachedExpirationDate.before(now)) {
                tokenCachingStrategy2.clear();
                this.tokenInfo = AccessToken.createEmptyToken(Collections.emptyList());
                return;
            }
            this.tokenInfo = AccessToken.createFromCache(tokenState);
            this.state = SessionState.CREATED_TOKEN_LOADED;
            return;
        }
        this.tokenInfo = AccessToken.createEmptyToken(Collections.emptyList());
    }

    public final Bundle getAuthorizationBundle() {
        Bundle bundle;
        synchronized (this.lock) {
            bundle = this.authorizationBundle;
        }
        return bundle;
    }

    public final boolean isOpened() {
        boolean isOpened;
        synchronized (this.lock) {
            isOpened = this.state.isOpened();
        }
        return isOpened;
    }

    public final boolean isClosed() {
        boolean isClosed;
        synchronized (this.lock) {
            isClosed = this.state.isClosed();
        }
        return isClosed;
    }

    public final SessionState getState() {
        SessionState sessionState;
        synchronized (this.lock) {
            sessionState = this.state;
        }
        return sessionState;
    }

    public final String getApplicationId() {
        return this.applicationId;
    }

    public final String getAccessToken() {
        String token;
        synchronized (this.lock) {
            token = this.tokenInfo == null ? null : this.tokenInfo.getToken();
        }
        return token;
    }

    public final Date getExpirationDate() {
        Date expires;
        synchronized (this.lock) {
            expires = this.tokenInfo == null ? null : this.tokenInfo.getExpires();
        }
        return expires;
    }

    public final List<String> getPermissions() {
        List<String> permissions;
        synchronized (this.lock) {
            permissions = this.tokenInfo == null ? null : this.tokenInfo.getPermissions();
        }
        return permissions;
    }

    public final void openForRead(OpenRequest openRequest) {
        open(openRequest, SessionAuthorizationType.READ);
    }

    public final void openForPublish(OpenRequest openRequest) {
        open(openRequest, SessionAuthorizationType.PUBLISH);
    }

    public final void open(AccessToken accessToken, StatusCallback callback) {
        synchronized (this.lock) {
            if (this.pendingRequest != null) {
                throw new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request.");
            } else if (this.state == SessionState.CREATED || this.state == SessionState.CREATED_TOKEN_LOADED) {
                if (callback != null) {
                    addCallback(callback);
                }
                this.tokenInfo = accessToken;
                if (this.tokenCachingStrategy != null) {
                    this.tokenCachingStrategy.save(accessToken.toCacheBundle());
                }
                SessionState oldState = this.state;
                this.state = SessionState.OPENED;
                postStateChange(oldState, this.state, (Exception) null);
            } else {
                throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
            }
        }
        autoPublishAsync();
    }

    public final void requestNewReadPermissions(NewPermissionsRequest newPermissionsRequest) {
        requestNewPermissions(newPermissionsRequest, SessionAuthorizationType.READ);
    }

    public final void requestNewPublishPermissions(NewPermissionsRequest newPermissionsRequest) {
        requestNewPermissions(newPermissionsRequest, SessionAuthorizationType.PUBLISH);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
        if (r10 == null) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
        r2 = (com.facebook.AuthorizationClient.Result) r10.getSerializableExtra("com.facebook.LoginActivity:Result");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0028, code lost:
        if (r2 == null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
        handleAuthorizationResult(r9, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0033, code lost:
        if (r6.authorizationClient == null) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0035, code lost:
        r6.authorizationClient.onActivityResult(r8, r9, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        if (r9 != 0) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        r0 = new com.facebook.FacebookOperationCanceledException("User canceled operation.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0044, code lost:
        finishAuthOrReauth((com.facebook.AccessToken) null, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
        return false;
     */
    public final boolean onActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data) {
        Validate.notNull(currentActivity, "currentActivity");
        initializeStaticContext(currentActivity);
        synchronized (this.lock) {
            if (this.pendingRequest == null || requestCode != this.pendingRequest.getRequestCode()) {
            }
        }
    }

    public final void close() {
        synchronized (this.lock) {
            SessionState oldState = this.state;
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 1:
                case 3:
                    this.state = SessionState.CLOSED_LOGIN_FAILED;
                    postStateChange(oldState, this.state, new FacebookException("Log in attempt aborted."));
                    break;
                case 2:
                case 4:
                case 5:
                    this.state = SessionState.CLOSED;
                    postStateChange(oldState, this.state, (Exception) null);
                    break;
            }
        }
    }

    public final void closeAndClearTokenInformation() {
        if (this.tokenCachingStrategy != null) {
            this.tokenCachingStrategy.clear();
        }
        Utility.clearFacebookCookies(staticContext);
        close();
    }

    public final void addCallback(StatusCallback callback) {
        synchronized (this.callbacks) {
            if (callback != null) {
                if (!this.callbacks.contains(callback)) {
                    this.callbacks.add(callback);
                }
            }
        }
    }

    public final void removeCallback(StatusCallback callback) {
        synchronized (this.callbacks) {
            this.callbacks.remove(callback);
        }
    }

    public String toString() {
        return "{Session" + " state:" + this.state + ", token:" + (this.tokenInfo == null ? "null" : this.tokenInfo) + ", appId:" + (this.applicationId == null ? "null" : this.applicationId) + "}";
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    public void extendTokenCompleted(Bundle bundle) {
        synchronized (this.lock) {
            SessionState oldState = this.state;
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 4:
                    this.state = SessionState.OPENED_TOKEN_UPDATED;
                    postStateChange(oldState, this.state, (Exception) null);
                    break;
                case 5:
                    break;
                default:
                    Log.d(TAG, "refreshToken ignored in state " + this.state);
                    return;
            }
            this.tokenInfo = AccessToken.createFromRefresh(this.tokenInfo, bundle);
            if (this.tokenCachingStrategy != null) {
                this.tokenCachingStrategy.save(this.tokenInfo.toCacheBundle());
            }
        }
    }

    private Object writeReplace() {
        return new SerializationProxyV1(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, false, this.pendingRequest);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Cannot readObject, serialization proxy required");
    }

    public static final void saveSession(Session session, Bundle bundle) {
        if (bundle != null && session != null && !bundle.containsKey(SESSION_BUNDLE_SAVE_KEY)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                new ObjectOutputStream(outputStream).writeObject(session);
                bundle.putByteArray(SESSION_BUNDLE_SAVE_KEY, outputStream.toByteArray());
                bundle.putBundle(AUTH_BUNDLE_SAVE_KEY, session.authorizationBundle);
            } catch (IOException e) {
                throw new FacebookException("Unable to save session.", e);
            }
        }
    }

    public static final Session restoreSession(Context context, TokenCachingStrategy cachingStrategy, StatusCallback callback, Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        byte[] data = bundle.getByteArray(SESSION_BUNDLE_SAVE_KEY);
        if (data != null) {
            try {
                Session session = (Session) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
                initializeStaticContext(context);
                if (cachingStrategy != null) {
                    session.tokenCachingStrategy = cachingStrategy;
                } else {
                    session.tokenCachingStrategy = new SharedPreferencesTokenCachingStrategy(context);
                }
                if (callback != null) {
                    session.addCallback(callback);
                }
                session.authorizationBundle = bundle.getBundle(AUTH_BUNDLE_SAVE_KEY);
                return session;
            } catch (ClassNotFoundException e) {
                Log.w(TAG, "Unable to restore session", e);
            } catch (IOException e2) {
                Log.w(TAG, "Unable to restore session.", e2);
            }
        }
        return null;
    }

    public static final Session getActiveSession() {
        Session session;
        synchronized (STATIC_LOCK) {
            session = activeSession;
        }
        return session;
    }

    public static final void setActiveSession(Session session) {
        synchronized (STATIC_LOCK) {
            if (session != activeSession) {
                Session oldSession = activeSession;
                if (oldSession != null) {
                    oldSession.close();
                }
                activeSession = session;
                if (oldSession != null) {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_UNSET);
                }
                if (session != null) {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_SET);
                    if (session.isOpened()) {
                        postActiveSessionAction(ACTION_ACTIVE_SESSION_OPENED);
                    }
                }
            }
        }
    }

    public static Session openActiveSessionFromCache(Context context) {
        return openActiveSession(context, false, (OpenRequest) null);
    }

    public static Session openActiveSession(Activity activity, boolean allowLoginUI, StatusCallback callback) {
        return openActiveSession((Context) activity, allowLoginUI, new OpenRequest(activity).setCallback(callback));
    }

    public static Session openActiveSession(Context context, Fragment fragment, boolean allowLoginUI, StatusCallback callback) {
        return openActiveSession(context, allowLoginUI, new OpenRequest(fragment).setCallback(callback));
    }

    public static Session openActiveSessionWithAccessToken(Context context, AccessToken accessToken, StatusCallback callback) {
        Session session = new Session(context, (String) null, (TokenCachingStrategy) null, false);
        setActiveSession(session);
        session.open(accessToken, callback);
        return session;
    }

    private static Session openActiveSession(Context context, boolean allowLoginUI, OpenRequest openRequest) {
        Session session = new Builder(context).build();
        if (!SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) && !allowLoginUI) {
            return null;
        }
        setActiveSession(session);
        session.openForRead(openRequest);
        return session;
    }

    static Context getStaticContext() {
        return staticContext;
    }

    static void initializeStaticContext(Context currentContext) {
        if (currentContext != null && staticContext == null) {
            Context applicationContext = currentContext.getApplicationContext();
            if (applicationContext == null) {
                applicationContext = currentContext;
            }
            staticContext = applicationContext;
        }
    }

    /* access modifiers changed from: package-private */
    public void authorize(AuthorizationRequest request) {
        request.setApplicationId(this.applicationId);
        autoPublishAsync();
        boolean started = tryLoginActivity(request);
        if (!started && request.isLegacy) {
            started = tryLegacyAuth(request);
        }
        if (!started) {
            synchronized (this.lock) {
                SessionState oldState = this.state;
                switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                    case 6:
                    case 7:
                        return;
                    default:
                        this.state = SessionState.CLOSED_LOGIN_FAILED;
                        postStateChange(oldState, this.state, new FacebookException("Log in attempt failed."));
                        return;
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0059, code lost:
        if (r0 != com.facebook.SessionState.OPENING) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005b, code lost:
        authorize(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        return;
     */
    private void open(OpenRequest openRequest, SessionAuthorizationType authType) {
        SessionState newState;
        validatePermissions(openRequest, authType);
        validateLoginBehavior(openRequest);
        synchronized (this.lock) {
            if (this.pendingRequest != null) {
                postStateChange(this.state, this.state, new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request."));
                return;
            }
            SessionState oldState = this.state;
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 1:
                    newState = SessionState.OPENING;
                    this.state = newState;
                    if (openRequest != null) {
                        this.pendingRequest = openRequest;
                        break;
                    } else {
                        throw new IllegalArgumentException("openRequest cannot be null when opening a new Session");
                    }
                case 2:
                    if (openRequest != null) {
                        if (!Utility.isNullOrEmpty(openRequest.getPermissions()) && !Utility.isSubset(openRequest.getPermissions(), getPermissions())) {
                            this.pendingRequest = openRequest;
                        }
                    }
                    if (this.pendingRequest != null) {
                        newState = SessionState.OPENING;
                        this.state = newState;
                        break;
                    } else {
                        newState = SessionState.OPENED;
                        this.state = newState;
                        break;
                    }
                default:
                    throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
            }
            if (openRequest != null) {
                addCallback(openRequest.getCallback());
            }
            postStateChange(oldState, newState, (Exception) null);
        }
    }

    private void requestNewPermissions(NewPermissionsRequest newPermissionsRequest, SessionAuthorizationType authType) {
        validatePermissions(newPermissionsRequest, authType);
        validateLoginBehavior(newPermissionsRequest);
        if (newPermissionsRequest != null) {
            synchronized (this.lock) {
                if (this.pendingRequest != null) {
                    throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has a pending request.");
                }
                switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                    case 4:
                    case 5:
                        this.pendingRequest = newPermissionsRequest;
                        break;
                    default:
                        throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that is not currently open.");
                }
            }
            newPermissionsRequest.setValidateSameFbidAsToken(getAccessToken());
            authorize(newPermissionsRequest);
        }
    }

    private void validateLoginBehavior(AuthorizationRequest request) {
        if (request != null && !request.isLegacy) {
            Intent intent = new Intent();
            intent.setClass(getStaticContext(), LoginActivity.class);
            if (!resolveIntent(intent)) {
                throw new FacebookException(String.format("Cannot use SessionLoginBehavior %s when %s is not declared as an activity in AndroidManifest.xml", new Object[]{request.getLoginBehavior(), LoginActivity.class.getName()}));
            }
        }
    }

    private void validatePermissions(AuthorizationRequest request, SessionAuthorizationType authType) {
        if (request != null && !Utility.isNullOrEmpty(request.getPermissions())) {
            for (String permission : request.getPermissions()) {
                if (isPublishPermission(permission)) {
                    if (SessionAuthorizationType.READ.equals(authType)) {
                        throw new FacebookException(String.format("Cannot pass a publish or manage permission (%s) to a request for read authorization", new Object[]{permission}));
                    }
                } else if (SessionAuthorizationType.PUBLISH.equals(authType)) {
                    Log.w(TAG, String.format("Should not pass a read permission (%s) to a request for publish or manage authorization", new Object[]{permission}));
                }
            }
        } else if (SessionAuthorizationType.PUBLISH.equals(authType)) {
            throw new FacebookException("Cannot request publish or manage authorization with no permissions.");
        }
    }

    static boolean isPublishPermission(String permission) {
        return permission != null && (permission.startsWith(PUBLISH_PERMISSION_PREFIX) || permission.startsWith(MANAGE_PERMISSION_PREFIX) || OTHER_PUBLISH_PERMISSIONS.contains(permission));
    }

    /* access modifiers changed from: private */
    public void handleAuthorizationResult(int resultCode, AuthorizationClient.Result result) {
        AccessToken newToken = null;
        Exception exception = null;
        Log.d("NguyenTT", "Session: resultCode " + resultCode + " result: " + result.code + " " + result.errorMessage);
        if (resultCode == -1) {
            if (result.code == AuthorizationClient.Result.Code.SUCCESS) {
                newToken = result.token;
            } else if (result.code == AuthorizationClient.Result.Code.CANCEL) {
                exception = new FacebookOperationCanceledException(result.errorMessage);
            } else if (result.code == AuthorizationClient.Result.Code.ERROR) {
                exception = new FacebookDialogException(result.errorMessage, -200, "");
            }
        } else if (resultCode == 0) {
            exception = new FacebookOperationCanceledException(result.errorMessage);
        }
        this.authorizationClient = null;
        finishAuthOrReauth(newToken, exception);
    }

    private boolean tryLoginActivity(AuthorizationRequest request) {
        Intent intent = getLoginActivityIntent(request);
        if (!resolveIntent(intent)) {
            return false;
        }
        try {
            request.getStartActivityDelegate().startActivityForResult(intent, request.getRequestCode());
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private boolean resolveIntent(Intent intent) {
        if (getStaticContext().getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        }
        return true;
    }

    private Intent getLoginActivityIntent(AuthorizationRequest request) {
        Intent intent = new Intent();
        intent.setClass(getStaticContext(), LoginActivity.class);
        intent.setAction(request.getLoginBehavior().toString());
        intent.putExtras(LoginActivity.populateIntentExtras(request.getAuthorizationClientRequest()));
        return intent;
    }

    private boolean tryLegacyAuth(AuthorizationRequest request) {
        this.authorizationClient = new AuthorizationClient();
        this.authorizationClient.setOnCompletedListener(new AuthorizationClient.OnCompletedListener() {
            public void onCompleted(AuthorizationClient.Result result) {
                Session.this.handleAuthorizationResult(-1, result);
            }
        });
        this.authorizationClient.setContext(getStaticContext());
        this.authorizationClient.startOrContinueAuth(request.getAuthorizationClientRequest());
        return true;
    }

    /* access modifiers changed from: package-private */
    public void finishAuthOrReauth(AccessToken newToken, Exception exception) {
        if (newToken != null && newToken.isInvalid()) {
            newToken = null;
            exception = new FacebookException("Invalid access token.");
        }
        synchronized (this.lock) {
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 3:
                    finishAuthorization(newToken, exception);
                    break;
                case 4:
                case 5:
                    finishReauthorization(newToken, exception);
                    break;
            }
        }
    }

    private void finishAuthorization(AccessToken newToken, Exception exception) {
        SessionState oldState = this.state;
        if (newToken != null) {
            this.tokenInfo = newToken;
            saveTokenToCache(newToken);
            this.state = SessionState.OPENED;
        } else if (exception != null) {
            this.state = SessionState.CLOSED_LOGIN_FAILED;
        }
        this.pendingRequest = null;
        postStateChange(oldState, this.state, exception);
    }

    private void finishReauthorization(AccessToken newToken, Exception exception) {
        SessionState oldState = this.state;
        if (newToken != null) {
            this.tokenInfo = newToken;
            saveTokenToCache(newToken);
            this.state = SessionState.OPENED_TOKEN_UPDATED;
        }
        this.pendingRequest = null;
        postStateChange(oldState, this.state, exception);
    }

    private void saveTokenToCache(AccessToken newToken) {
        if (newToken != null && this.tokenCachingStrategy != null) {
            this.tokenCachingStrategy.save(newToken.toCacheBundle());
        }
    }

    /* access modifiers changed from: package-private */
    public void postStateChange(SessionState oldState, final SessionState newState, final Exception exception) {
        if (oldState != newState || exception != null) {
            if (newState.isClosed()) {
                this.tokenInfo = AccessToken.createEmptyToken(Collections.emptyList());
            }
            synchronized (this.callbacks) {
                runWithHandlerOrExecutor(this.handler, new Runnable() {
                    public void run() {
                        for (final StatusCallback callback : Session.this.callbacks) {
                            final SessionState sessionState = newState;
                            final Exception exc = exception;
                            Session.runWithHandlerOrExecutor(Session.this.handler, new Runnable() {
                                public void run() {
                                    callback.call(Session.this, sessionState, exc);
                                }
                            });
                        }
                    }
                });
            }
            if (this == activeSession && oldState.isOpened() != newState.isOpened()) {
                if (newState.isOpened()) {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_OPENED);
                } else {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_CLOSED);
                }
            }
        }
    }

    static void postActiveSessionAction(String action) {
        LocalBroadcastManager.getInstance(getStaticContext()).sendBroadcast(new Intent(action));
    }

    /* access modifiers changed from: private */
    public static void runWithHandlerOrExecutor(Handler handler2, Runnable runnable) {
        if (handler2 != null) {
            handler2.post(runnable);
        } else {
            Settings.getExecutor().execute(runnable);
        }
    }

    /* access modifiers changed from: package-private */
    public void extendAccessTokenIfNeeded() {
        if (shouldExtendAccessToken()) {
            extendAccessToken();
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0011, code lost:
        if (r0 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0013, code lost:
        r0.bind();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    public void extendAccessToken() {
        TokenRefreshRequest newTokenRefreshRequest = null;
        synchronized (this.lock) {
            if (this.currentTokenRefreshRequest == null) {
                TokenRefreshRequest newTokenRefreshRequest2 = new TokenRefreshRequest();
                try {
                    this.currentTokenRefreshRequest = newTokenRefreshRequest2;
                    newTokenRefreshRequest = newTokenRefreshRequest2;
                } catch (Throwable th) {
                    th = th;
                    TokenRefreshRequest tokenRefreshRequest = newTokenRefreshRequest2;
                    throw th;
                }
            }
            try {
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldExtendAccessToken() {
        if (this.currentTokenRefreshRequest != null) {
            return false;
        }
        Date now = new Date();
        if (!this.state.isOpened() || !this.tokenInfo.getSource().canExtendToken() || now.getTime() - this.lastAttemptedTokenExtendDate.getTime() <= 3600000 || now.getTime() - this.tokenInfo.getLastRefresh().getTime() <= 86400000) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public AccessToken getTokenInfo() {
        return this.tokenInfo;
    }

    /* access modifiers changed from: package-private */
    public void setTokenInfo(AccessToken tokenInfo2) {
        this.tokenInfo = tokenInfo2;
    }

    /* access modifiers changed from: package-private */
    public Date getLastAttemptedTokenExtendDate() {
        return this.lastAttemptedTokenExtendDate;
    }

    /* access modifiers changed from: package-private */
    public void setLastAttemptedTokenExtendDate(Date lastAttemptedTokenExtendDate2) {
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate2;
    }

    /* access modifiers changed from: package-private */
    public void setCurrentTokenRefreshRequest(TokenRefreshRequest request) {
        this.currentTokenRefreshRequest = request;
    }

    class TokenRefreshRequest implements ServiceConnection {
        final Messenger messageReceiver;
        Messenger messageSender = null;

        TokenRefreshRequest() {
            this.messageReceiver = new Messenger(new TokenRefreshRequestHandler(Session.this, this));
        }

        public void bind() {
            Intent intent = NativeProtocol.createTokenRefreshIntent(Session.getStaticContext());
            if (intent == null || !Session.staticContext.bindService(intent, new TokenRefreshRequest(), 1)) {
                cleanup();
            } else {
                Session.this.setLastAttemptedTokenExtendDate(new Date());
            }
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            this.messageSender = new Messenger(service);
            refreshToken();
        }

        public void onServiceDisconnected(ComponentName arg) {
            cleanup();
            Session.staticContext.unbindService(this);
        }

        /* access modifiers changed from: private */
        public void cleanup() {
            if (Session.this.currentTokenRefreshRequest == this) {
                Session.this.currentTokenRefreshRequest = null;
            }
        }

        private void refreshToken() {
            Bundle requestData = new Bundle();
            requestData.putString("access_token", Session.this.getTokenInfo().getToken());
            Message request = Message.obtain();
            request.setData(requestData);
            request.replyTo = this.messageReceiver;
            try {
                this.messageSender.send(request);
            } catch (RemoteException e) {
                cleanup();
            }
        }
    }

    static class TokenRefreshRequestHandler extends Handler {
        private WeakReference<TokenRefreshRequest> refreshRequestWeakReference;
        private WeakReference<Session> sessionWeakReference;

        TokenRefreshRequestHandler(Session session, TokenRefreshRequest refreshRequest) {
            super(Looper.getMainLooper());
            this.sessionWeakReference = new WeakReference<>(session);
            this.refreshRequestWeakReference = new WeakReference<>(refreshRequest);
        }

        public void handleMessage(Message msg) {
            String token = msg.getData().getString("access_token");
            Session session = (Session) this.sessionWeakReference.get();
            if (!(session == null || token == null)) {
                session.extendTokenCompleted(msg.getData());
            }
            TokenRefreshRequest request = (TokenRefreshRequest) this.refreshRequestWeakReference.get();
            if (request != null) {
                Session.staticContext.unbindService(request);
                request.cleanup();
            }
        }
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object otherObj) {
        if (!(otherObj instanceof Session)) {
            return false;
        }
        Session other = (Session) otherObj;
        if (!areEqual(other.applicationId, this.applicationId) || !areEqual(other.authorizationBundle, this.authorizationBundle) || !areEqual(other.state, this.state) || !areEqual(other.getExpirationDate(), getExpirationDate())) {
            return false;
        }
        return true;
    }

    private static boolean areEqual(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    public static final class Builder {
        private String applicationId;
        private final Context context;
        private TokenCachingStrategy tokenCachingStrategy;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setApplicationId(String applicationId2) {
            this.applicationId = applicationId2;
            return this;
        }

        public Builder setTokenCachingStrategy(TokenCachingStrategy tokenCachingStrategy2) {
            this.tokenCachingStrategy = tokenCachingStrategy2;
            return this;
        }

        public Session build() {
            return new Session(this.context, this.applicationId, this.tokenCachingStrategy);
        }
    }

    private void autoPublishAsync() {
        String applicationId2;
        AutoPublishAsyncTask asyncTask = null;
        synchronized (this) {
            if (this.autoPublishAsyncTask == null && Settings.getShouldAutoPublishInstall() && (applicationId2 = this.applicationId) != null) {
                AutoPublishAsyncTask asyncTask2 = new AutoPublishAsyncTask(applicationId2, staticContext);
                this.autoPublishAsyncTask = asyncTask2;
                asyncTask = asyncTask2;
            }
        }
        if (asyncTask != null) {
            asyncTask.execute(new Void[0]);
        }
    }

    private class AutoPublishAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Context mApplicationContext;
        private final String mApplicationId;

        public AutoPublishAsyncTask(String applicationId, Context context) {
            this.mApplicationId = applicationId;
            this.mApplicationContext = context.getApplicationContext();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voids) {
            try {
                Settings.publishInstallAndWait(this.mApplicationContext, this.mApplicationId);
                return null;
            } catch (Exception e) {
                Utility.logd("Facebook-publish", e.getMessage());
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            synchronized (Session.this) {
                Session.this.autoPublishAsyncTask = null;
            }
        }
    }

    public static class AuthorizationRequest implements Serializable {
        private static final long serialVersionUID = 1;
        private String applicationId;
        private SessionDefaultAudience defaultAudience;
        /* access modifiers changed from: private */
        public boolean isLegacy;
        private SessionLoginBehavior loginBehavior;
        private List<String> permissions;
        private int requestCode;
        /* access modifiers changed from: private */
        public final StartActivityDelegate startActivityDelegate;
        private StatusCallback statusCallback;
        private String validateSameFbidAsToken;

        AuthorizationRequest(final Activity activity) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.startActivityDelegate = new StartActivityDelegate() {
                public void startActivityForResult(Intent intent, int requestCode) {
                    activity.startActivityForResult(intent, requestCode);
                }

                public Activity getActivityContext() {
                    return activity;
                }
            };
        }

        AuthorizationRequest(final Fragment fragment) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.startActivityDelegate = new StartActivityDelegate() {
                public void startActivityForResult(Intent intent, int requestCode) {
                    fragment.startActivityForResult(intent, requestCode);
                }

                public Activity getActivityContext() {
                    return fragment.getActivity();
                }
            };
        }

        private AuthorizationRequest(SessionLoginBehavior loginBehavior2, int requestCode2, List<String> permissions2, String defaultAudience2, boolean isLegacy2, String applicationId2, String validateSameFbidAsToken2) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.startActivityDelegate = new StartActivityDelegate() {
                public void startActivityForResult(Intent intent, int requestCode) {
                    throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
                }

                public Activity getActivityContext() {
                    throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
                }
            };
            this.loginBehavior = loginBehavior2;
            this.requestCode = requestCode2;
            this.permissions = permissions2;
            this.defaultAudience = SessionDefaultAudience.valueOf(defaultAudience2);
            this.isLegacy = isLegacy2;
            this.applicationId = applicationId2;
            this.validateSameFbidAsToken = validateSameFbidAsToken2;
        }

        /* synthetic */ AuthorizationRequest(SessionLoginBehavior sessionLoginBehavior, int i, List list, String str, boolean z, String str2, String str3, AuthorizationRequest authorizationRequest) {
            this(sessionLoginBehavior, i, list, str, z, str2, str3);
        }

        public void setIsLegacy(boolean isLegacy2) {
            this.isLegacy = isLegacy2;
        }

        /* access modifiers changed from: package-private */
        public boolean isLegacy() {
            return this.isLegacy;
        }

        /* access modifiers changed from: package-private */
        public AuthorizationRequest setCallback(StatusCallback statusCallback2) {
            this.statusCallback = statusCallback2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public StatusCallback getCallback() {
            return this.statusCallback;
        }

        /* access modifiers changed from: package-private */
        public AuthorizationRequest setLoginBehavior(SessionLoginBehavior loginBehavior2) {
            if (loginBehavior2 != null) {
                this.loginBehavior = loginBehavior2;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public SessionLoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }

        /* access modifiers changed from: package-private */
        public AuthorizationRequest setRequestCode(int requestCode2) {
            if (requestCode2 >= 0) {
                this.requestCode = requestCode2;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public int getRequestCode() {
            return this.requestCode;
        }

        /* access modifiers changed from: package-private */
        public AuthorizationRequest setPermissions(List<String> permissions2) {
            if (permissions2 != null) {
                this.permissions = permissions2;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public List<String> getPermissions() {
            return this.permissions;
        }

        /* access modifiers changed from: package-private */
        public AuthorizationRequest setDefaultAudience(SessionDefaultAudience defaultAudience2) {
            if (defaultAudience2 != null) {
                this.defaultAudience = defaultAudience2;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public SessionDefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }

        /* access modifiers changed from: package-private */
        public StartActivityDelegate getStartActivityDelegate() {
            return this.startActivityDelegate;
        }

        /* access modifiers changed from: package-private */
        public String getApplicationId() {
            return this.applicationId;
        }

        /* access modifiers changed from: package-private */
        public void setApplicationId(String applicationId2) {
            this.applicationId = applicationId2;
        }

        /* access modifiers changed from: package-private */
        public String getValidateSameFbidAsToken() {
            return this.validateSameFbidAsToken;
        }

        /* access modifiers changed from: package-private */
        public void setValidateSameFbidAsToken(String validateSameFbidAsToken2) {
            this.validateSameFbidAsToken = validateSameFbidAsToken2;
        }

        /* access modifiers changed from: package-private */
        public AuthorizationClient.AuthorizationRequest getAuthorizationClientRequest() {
            return new AuthorizationClient.AuthorizationRequest(this.loginBehavior, this.requestCode, this.isLegacy, this.permissions, this.defaultAudience, this.applicationId, this.validateSameFbidAsToken, new AuthorizationClient.StartActivityDelegate() {
                public void startActivityForResult(Intent intent, int requestCode) {
                    AuthorizationRequest.this.startActivityDelegate.startActivityForResult(intent, requestCode);
                }

                public Activity getActivityContext() {
                    return AuthorizationRequest.this.startActivityDelegate.getActivityContext();
                }
            });
        }

        /* access modifiers changed from: package-private */
        public Object writeReplace() {
            return new AuthRequestSerializationProxyV1(this.loginBehavior, this.requestCode, this.permissions, this.defaultAudience.name(), this.isLegacy, this.applicationId, this.validateSameFbidAsToken, (AuthRequestSerializationProxyV1) null);
        }

        /* access modifiers changed from: package-private */
        public void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Cannot readObject, serialization proxy required");
        }

        private static class AuthRequestSerializationProxyV1 implements Serializable {
            private static final long serialVersionUID = -8748347685113614927L;
            private final String applicationId;
            private final String defaultAudience;
            private boolean isLegacy;
            private final SessionLoginBehavior loginBehavior;
            private final List<String> permissions;
            private final int requestCode;
            private final String validateSameFbidAsToken;

            private AuthRequestSerializationProxyV1(SessionLoginBehavior loginBehavior2, int requestCode2, List<String> permissions2, String defaultAudience2, boolean isLegacy2, String applicationId2, String validateSameFbidAsToken2) {
                this.loginBehavior = loginBehavior2;
                this.requestCode = requestCode2;
                this.permissions = permissions2;
                this.defaultAudience = defaultAudience2;
                this.isLegacy = isLegacy2;
                this.applicationId = applicationId2;
                this.validateSameFbidAsToken = validateSameFbidAsToken2;
            }

            /* synthetic */ AuthRequestSerializationProxyV1(SessionLoginBehavior sessionLoginBehavior, int i, List list, String str, boolean z, String str2, String str3, AuthRequestSerializationProxyV1 authRequestSerializationProxyV1) {
                this(sessionLoginBehavior, i, list, str, z, str2, str3);
            }

            private Object readResolve() {
                return new AuthorizationRequest(this.loginBehavior, this.requestCode, this.permissions, this.defaultAudience, this.isLegacy, this.applicationId, this.validateSameFbidAsToken, (AuthorizationRequest) null);
            }
        }
    }

    public static final class OpenRequest extends AuthorizationRequest {
        private static final long serialVersionUID = 1;

        public OpenRequest(Activity activity) {
            super(activity);
        }

        public OpenRequest(Fragment fragment) {
            super(fragment);
        }

        public final OpenRequest setCallback(StatusCallback statusCallback) {
            super.setCallback(statusCallback);
            return this;
        }

        public final OpenRequest setLoginBehavior(SessionLoginBehavior loginBehavior) {
            super.setLoginBehavior(loginBehavior);
            return this;
        }

        public final OpenRequest setRequestCode(int requestCode) {
            super.setRequestCode(requestCode);
            return this;
        }

        public final OpenRequest setPermissions(List<String> permissions) {
            super.setPermissions(permissions);
            return this;
        }

        public final OpenRequest setDefaultAudience(SessionDefaultAudience defaultAudience) {
            super.setDefaultAudience(defaultAudience);
            return this;
        }
    }

    public static final class NewPermissionsRequest extends AuthorizationRequest {
        private static final long serialVersionUID = 1;

        public NewPermissionsRequest(Activity activity, List<String> permissions) {
            super(activity);
            setPermissions(permissions);
        }

        public NewPermissionsRequest(Fragment fragment, List<String> permissions) {
            super(fragment);
            setPermissions(permissions);
        }

        public final NewPermissionsRequest setCallback(StatusCallback statusCallback) {
            super.setCallback(statusCallback);
            return this;
        }

        public final NewPermissionsRequest setLoginBehavior(SessionLoginBehavior loginBehavior) {
            super.setLoginBehavior(loginBehavior);
            return this;
        }

        public final NewPermissionsRequest setRequestCode(int requestCode) {
            super.setRequestCode(requestCode);
            return this;
        }

        public final NewPermissionsRequest setDefaultAudience(SessionDefaultAudience defaultAudience) {
            super.setDefaultAudience(defaultAudience);
            return this;
        }
    }
}
