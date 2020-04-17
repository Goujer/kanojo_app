package com.facebook.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.LegacyHelper;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.TokenCachingStrategy;
import com.facebook.internal.ServerProtocol;
import com.google.android.gcm.GCMConstants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;

public class Facebook {
    @Deprecated
    public static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    @Deprecated
    public static final Uri ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    @Deprecated
    public static final String CANCEL_URI = "fbconnect://cancel";
    private static final int DEFAULT_AUTH_ACTIVITY_CODE = 32665;
    @Deprecated
    protected static String DIALOG_BASE_URL = "https://m.facebook.com/dialog/";
    @Deprecated
    public static final String EXPIRES = "expires_in";
    @Deprecated
    public static final String FB_APP_SIGNATURE = "30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e3020170d3039303833313231353231365a180f32303530303932353231353231365a307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d010101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fab00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581ccfef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d0101040500038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a673149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd928a2";
    @Deprecated
    public static final int FORCE_DIALOG_AUTH = -1;
    @Deprecated
    protected static String GRAPH_BASE_URL = ServerProtocol.GRAPH_URL_BASE;
    private static final String LOGIN = "oauth";
    @Deprecated
    public static final String REDIRECT_URI = "fbconnect://success";
    @Deprecated
    protected static String RESTSERVER_URL = "https://api.facebook.com/restserver.php";
    @Deprecated
    public static final String SINGLE_SIGN_ON_DISABLED = "service_disabled";
    @Deprecated
    public static final String TOKEN = "access_token";
    private final long REFRESH_TOKEN_BARRIER = 86400000;
    /* access modifiers changed from: private */
    public long accessExpiresMillisecondsAfterEpoch = 0;
    /* access modifiers changed from: private */
    public String accessToken = null;
    /* access modifiers changed from: private */
    public long lastAccessUpdateMillisecondsAfterEpoch = 0;
    private final Object lock = new Object();
    private String mAppId;
    private Activity pendingAuthorizationActivity;
    /* access modifiers changed from: private */
    public String[] pendingAuthorizationPermissions;
    private Session pendingOpeningSession;
    /* access modifiers changed from: private */
    public volatile Session session;
    private boolean sessionInvalidated;
    private SetterTokenCachingStrategy tokenCache;
    private volatile Session userSetSession;

    public interface DialogListener {
        void onCancel();

        void onComplete(Bundle bundle);

        void onError(DialogError dialogError);

        void onFacebookError(FacebookError facebookError);
    }

    public interface ServiceListener {
        void onComplete(Bundle bundle);

        void onError(Error error);

        void onFacebookError(FacebookError facebookError);
    }

    @Deprecated
    public Facebook(String appId) {
        if (appId == null) {
            throw new IllegalArgumentException("You must specify your application ID when instantiating a Facebook object. See README for details.");
        }
        this.mAppId = appId;
    }

    @Deprecated
    public void authorize(Activity activity, DialogListener listener) {
        authorize(activity, new String[0], DEFAULT_AUTH_ACTIVITY_CODE, SessionLoginBehavior.SSO_WITH_FALLBACK, listener);
    }

    @Deprecated
    public void authorize(Activity activity, String[] permissions, DialogListener listener) {
        authorize(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, SessionLoginBehavior.SSO_WITH_FALLBACK, listener);
    }

    @Deprecated
    public void authorize(Activity activity, String[] permissions, int activityCode, DialogListener listener) {
        SessionLoginBehavior behavior;
        if (activityCode >= 0) {
            behavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
        } else {
            behavior = SessionLoginBehavior.SUPPRESS_SSO;
        }
        authorize(activity, permissions, activityCode, behavior, listener);
    }

    private void authorize(Activity activity, String[] permissions, int activityCode, SessionLoginBehavior behavior, final DialogListener listener) {
        boolean z;
        checkUserSession("authorize");
        this.pendingOpeningSession = new Session.Builder(activity).setApplicationId(this.mAppId).setTokenCachingStrategy(getTokenCache()).build();
        this.pendingAuthorizationActivity = activity;
        this.pendingAuthorizationPermissions = permissions != null ? permissions : new String[0];
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).setCallback(new Session.StatusCallback() {
            public void call(Session callbackSession, SessionState state, Exception exception) {
                Facebook.this.onSessionCallback(callbackSession, state, exception, listener);
            }
        }).setLoginBehavior(behavior).setRequestCode(activityCode).setPermissions(Arrays.asList(permissions));
        Session session2 = this.pendingOpeningSession;
        if (this.pendingAuthorizationPermissions.length > 0) {
            z = true;
        } else {
            z = false;
        }
        openSession(session2, openRequest, z);
    }

    private void openSession(Session session2, Session.OpenRequest openRequest, boolean isPublish) {
        openRequest.setIsLegacy(true);
        if (isPublish) {
            session2.openForPublish(openRequest);
        } else {
            session2.openForRead(openRequest);
        }
    }

    /* access modifiers changed from: private */
    public void onSessionCallback(Session callbackSession, SessionState state, Exception exception, DialogListener listener) {
        Bundle extras = callbackSession.getAuthorizationBundle();
        if (state == SessionState.OPENED) {
            Session sessionToClose = null;
            synchronized (this.lock) {
                if (callbackSession != this.session) {
                    sessionToClose = this.session;
                    this.session = callbackSession;
                    this.sessionInvalidated = false;
                }
            }
            if (sessionToClose != null) {
                sessionToClose.close();
            }
            listener.onComplete(extras);
        } else if (exception == null) {
        } else {
            if (exception instanceof FacebookOperationCanceledException) {
                listener.onCancel();
            } else if (!(exception instanceof FacebookAuthorizationException) || extras == null || !extras.containsKey(Session.WEB_VIEW_ERROR_CODE_KEY) || !extras.containsKey(Session.WEB_VIEW_FAILING_URL_KEY)) {
                listener.onFacebookError(new FacebookError(exception.getMessage()));
            } else {
                listener.onError(new DialogError(exception.getMessage(), extras.getInt(Session.WEB_VIEW_ERROR_CODE_KEY), extras.getString(Session.WEB_VIEW_FAILING_URL_KEY)));
            }
        }
    }

    private boolean validateServiceIntent(Context context, Intent intent) {
        ResolveInfo resolveInfo = context.getPackageManager().resolveService(intent, 0);
        if (resolveInfo == null) {
            return false;
        }
        return validateAppSignatureForPackage(context, resolveInfo.serviceInfo.packageName);
    }

    private boolean validateAppSignatureForPackage(Context context, String packageName) {
        try {
            for (Signature signature : context.getPackageManager().getPackageInfo(packageName, 64).signatures) {
                if (signature.toCharsString().equals(FB_APP_SIGNATURE)) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Deprecated
    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
        checkUserSession("authorizeCallback");
        Session pending = this.pendingOpeningSession;
        if (pending != null && pending.onActivityResult(this.pendingAuthorizationActivity, requestCode, resultCode, data)) {
            this.pendingOpeningSession = null;
            this.pendingAuthorizationActivity = null;
            this.pendingAuthorizationPermissions = null;
        }
    }

    @Deprecated
    public boolean extendAccessToken(Context context, ServiceListener serviceListener) {
        checkUserSession("extendAccessToken");
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.platform.TokenRefreshService");
        if (!validateServiceIntent(context, intent)) {
            return false;
        }
        return context.bindService(intent, new TokenRefreshServiceConnection(context, serviceListener), 1);
    }

    @Deprecated
    public boolean extendAccessTokenIfNeeded(Context context, ServiceListener serviceListener) {
        checkUserSession("extendAccessTokenIfNeeded");
        if (shouldExtendAccessToken()) {
            return extendAccessToken(context, serviceListener);
        }
        return true;
    }

    @Deprecated
    public boolean shouldExtendAccessToken() {
        checkUserSession("shouldExtendAccessToken");
        return isSessionValid() && System.currentTimeMillis() - this.lastAccessUpdateMillisecondsAfterEpoch >= 86400000;
    }

    private class TokenRefreshServiceConnection implements ServiceConnection {
        final Context applicationsContext;
        final Messenger messageReceiver;
        Messenger messageSender = null;
        final ServiceListener serviceListener;

        public TokenRefreshServiceConnection(Context applicationsContext2, ServiceListener serviceListener2) {
            this.messageReceiver = new Messenger(new TokenRefreshConnectionHandler(Facebook.this, this));
            this.applicationsContext = applicationsContext2;
            this.serviceListener = serviceListener2;
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            this.messageSender = new Messenger(service);
            refreshToken();
        }

        public void onServiceDisconnected(ComponentName arg) {
            this.serviceListener.onError(new Error("Service disconnected"));
            this.applicationsContext.unbindService(this);
        }

        private void refreshToken() {
            Bundle requestData = new Bundle();
            requestData.putString("access_token", Facebook.this.accessToken);
            Message request = Message.obtain();
            request.setData(requestData);
            request.replyTo = this.messageReceiver;
            try {
                this.messageSender.send(request);
            } catch (RemoteException e) {
                this.serviceListener.onError(new Error("Service connection error"));
            }
        }
    }

    private static class TokenRefreshConnectionHandler extends Handler {
        WeakReference<TokenRefreshServiceConnection> connectionWeakReference;
        WeakReference<Facebook> facebookWeakReference;

        TokenRefreshConnectionHandler(Facebook facebook, TokenRefreshServiceConnection connection) {
            this.facebookWeakReference = new WeakReference<>(facebook);
            this.connectionWeakReference = new WeakReference<>(connection);
        }

        public void handleMessage(Message msg) {
            Facebook facebook = (Facebook) this.facebookWeakReference.get();
            TokenRefreshServiceConnection connection = (TokenRefreshServiceConnection) this.connectionWeakReference.get();
            if (facebook != null && connection != null) {
                String token = msg.getData().getString("access_token");
                long expiresAtMsecFromEpoch = msg.getData().getLong("expires_in") * 1000;
                if (token != null) {
                    facebook.setAccessToken(token);
                    facebook.setAccessExpires(expiresAtMsecFromEpoch);
                    Session refreshSession = facebook.session;
                    if (refreshSession != null) {
                        LegacyHelper.extendTokenCompleted(refreshSession, msg.getData());
                    }
                    if (connection.serviceListener != null) {
                        Bundle resultBundle = (Bundle) msg.getData().clone();
                        resultBundle.putLong("expires_in", expiresAtMsecFromEpoch);
                        connection.serviceListener.onComplete(resultBundle);
                    }
                } else if (connection.serviceListener != null) {
                    String error = msg.getData().getString(GCMConstants.EXTRA_ERROR);
                    if (msg.getData().containsKey("error_code")) {
                        connection.serviceListener.onFacebookError(new FacebookError(error, (String) null, msg.getData().getInt("error_code")));
                    } else {
                        ServiceListener serviceListener = connection.serviceListener;
                        if (error == null) {
                            error = "Unknown service error";
                        }
                        serviceListener.onError(new Error(error));
                    }
                }
                if (connection != null) {
                    connection.applicationsContext.unbindService(connection);
                }
            }
        }
    }

    @Deprecated
    public String logout(Context context) throws MalformedURLException, IOException {
        return logoutImpl(context);
    }

    /* access modifiers changed from: package-private */
    public String logoutImpl(Context context) throws MalformedURLException, IOException {
        Session sessionToClose;
        checkUserSession("logout");
        Bundle b = new Bundle();
        b.putString("method", "auth.expireSession");
        String response = request(b);
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.lock) {
            sessionToClose = this.session;
            this.session = null;
            this.accessToken = null;
            this.accessExpiresMillisecondsAfterEpoch = 0;
            this.lastAccessUpdateMillisecondsAfterEpoch = currentTimeMillis;
            this.sessionInvalidated = false;
        }
        if (sessionToClose != null) {
            sessionToClose.closeAndClearTokenInformation();
        }
        return response;
    }

    @Deprecated
    public String request(Bundle parameters) throws MalformedURLException, IOException {
        if (parameters.containsKey("method")) {
            return requestImpl((String) null, parameters, DownloadHelper.REQUEST_METHOD_GET);
        }
        throw new IllegalArgumentException("API method must be specified. (parameters must contain key \"method\" and value). See http://developers.facebook.com/docs/reference/rest/");
    }

    @Deprecated
    public String request(String graphPath) throws MalformedURLException, IOException {
        return requestImpl(graphPath, new Bundle(), DownloadHelper.REQUEST_METHOD_GET);
    }

    @Deprecated
    public String request(String graphPath, Bundle parameters) throws MalformedURLException, IOException {
        return requestImpl(graphPath, parameters, DownloadHelper.REQUEST_METHOD_GET);
    }

    @Deprecated
    public String request(String graphPath, Bundle params, String httpMethod) throws FileNotFoundException, MalformedURLException, IOException {
        return requestImpl(graphPath, params, httpMethod);
    }

    /* access modifiers changed from: package-private */
    public String requestImpl(String graphPath, Bundle params, String httpMethod) throws FileNotFoundException, MalformedURLException, IOException {
        params.putString("format", "json");
        if (isSessionValid()) {
            params.putString("access_token", getAccessToken());
        }
        return Util.openUrl(graphPath != null ? String.valueOf(GRAPH_BASE_URL) + graphPath : RESTSERVER_URL, httpMethod, params);
    }

    @Deprecated
    public void dialog(Context context, String action, DialogListener listener) {
        dialog(context, action, new Bundle(), listener);
    }

    @Deprecated
    public void dialog(Context context, String action, Bundle parameters, DialogListener listener) {
        parameters.putString(ServerProtocol.DIALOG_PARAM_DISPLAY, "touch");
        parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, REDIRECT_URI);
        if (action.equals(LOGIN)) {
            parameters.putString("type", "user_agent");
            parameters.putString("client_id", this.mAppId);
        } else {
            parameters.putString(Preferences.PREFERENCE_APP_ID, this.mAppId);
            if (isSessionValid()) {
                parameters.putString("access_token", getAccessToken());
            }
        }
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") != 0) {
            Util.showAlert(context, "Error", "Application requires permission to access the Internet");
        } else {
            new FbDialog(context, action, parameters, listener).show();
        }
    }

    @Deprecated
    public boolean isSessionValid() {
        return getAccessToken() != null && (getAccessExpires() == 0 || System.currentTimeMillis() < getAccessExpires());
    }

    @Deprecated
    public void setSession(Session session2) {
        if (session2 == null) {
            throw new IllegalArgumentException("session cannot be null");
        }
        synchronized (this.lock) {
            this.userSetSession = session2;
        }
    }

    private void checkUserSession(String methodName) {
        if (this.userSetSession != null) {
            throw new UnsupportedOperationException(String.format("Cannot call %s after setSession has been called.", new Object[]{methodName}));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0023, code lost:
        if (r0 != null) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0027, code lost:
        if (r3 == null) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0029, code lost:
        r5 = r3.getPermissions();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x002d, code lost:
        r2 = new com.facebook.Session.Builder(r11.pendingAuthorizationActivity).setApplicationId(r11.mAppId).setTokenCachingStrategy(getTokenCache()).build();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
        if (r2.getState() == com.facebook.SessionState.CREATED_TOKEN_LOADED) goto L_0x0060;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0052, code lost:
        if (r11.pendingAuthorizationPermissions == null) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0054, code lost:
        r5 = java.util.Arrays.asList(r11.pendingAuthorizationPermissions);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005b, code lost:
        r5 = java.util.Collections.emptyList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0060, code lost:
        r4 = new com.facebook.Session.OpenRequest(r11.pendingAuthorizationActivity).setPermissions((java.util.List) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x006f, code lost:
        if (r5.isEmpty() == false) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0071, code lost:
        r7 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0072, code lost:
        openSession(r2, r4, r7);
        r1 = null;
        r6 = null;
        r10 = r11.lock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0079, code lost:
        monitor-enter(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x007c, code lost:
        if (r11.sessionInvalidated != false) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0080, code lost:
        if (r11.session != null) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0082, code lost:
        r1 = r11.session;
        r11.session = r2;
        r6 = r2;
        r11.sessionInvalidated = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x008a, code lost:
        monitor-exit(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x008b, code lost:
        if (r1 == null) goto L_0x0090;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008d, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0094, code lost:
        r7 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0090, code lost:
        continue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        return null;
     */
    @Deprecated
    public final Session getSession() {
        Session returnSession;
        do {
            synchronized (this.lock) {
                if (this.userSetSession != null) {
                    Session session2 = this.userSetSession;
                    return session2;
                } else if (this.session != null || !this.sessionInvalidated) {
                    Session session3 = this.session;
                    return session3;
                } else {
                    String cachedToken = this.accessToken;
                    Session oldSession = this.session;
                }
            }
        } while (returnSession == null);
        return returnSession;
    }

    @Deprecated
    public String getAccessToken() {
        Session s = getSession();
        if (s != null) {
            return s.getAccessToken();
        }
        return null;
    }

    @Deprecated
    public long getAccessExpires() {
        Session s = getSession();
        if (s != null) {
            return s.getExpirationDate().getTime();
        }
        return this.accessExpiresMillisecondsAfterEpoch;
    }

    @Deprecated
    public long getLastAccessUpdate() {
        return this.lastAccessUpdateMillisecondsAfterEpoch;
    }

    @Deprecated
    public void setTokenFromCache(String accessToken2, long accessExpires, long lastAccessUpdate) {
        checkUserSession("setTokenFromCache");
        synchronized (this.lock) {
            this.accessToken = accessToken2;
            this.accessExpiresMillisecondsAfterEpoch = accessExpires;
            this.lastAccessUpdateMillisecondsAfterEpoch = lastAccessUpdate;
        }
    }

    @Deprecated
    public void setAccessToken(String token) {
        checkUserSession("setAccessToken");
        synchronized (this.lock) {
            this.accessToken = token;
            this.lastAccessUpdateMillisecondsAfterEpoch = System.currentTimeMillis();
            this.sessionInvalidated = true;
        }
    }

    @Deprecated
    public void setAccessExpires(long timestampInMsec) {
        checkUserSession("setAccessExpires");
        synchronized (this.lock) {
            this.accessExpiresMillisecondsAfterEpoch = timestampInMsec;
            this.lastAccessUpdateMillisecondsAfterEpoch = System.currentTimeMillis();
            this.sessionInvalidated = true;
        }
    }

    @Deprecated
    public void setAccessExpiresIn(String expiresInSecsFromNow) {
        long expires;
        checkUserSession("setAccessExpiresIn");
        if (expiresInSecsFromNow != null) {
            if (expiresInSecsFromNow.equals(GreeDefs.BARCODE)) {
                expires = 0;
            } else {
                expires = System.currentTimeMillis() + (Long.parseLong(expiresInSecsFromNow) * 1000);
            }
            setAccessExpires(expires);
        }
    }

    @Deprecated
    public String getAppId() {
        return this.mAppId;
    }

    @Deprecated
    public void setAppId(String appId) {
        checkUserSession("setAppId");
        synchronized (this.lock) {
            this.mAppId = appId;
            this.sessionInvalidated = true;
        }
    }

    private TokenCachingStrategy getTokenCache() {
        if (this.tokenCache == null) {
            this.tokenCache = new SetterTokenCachingStrategy(this, (SetterTokenCachingStrategy) null);
        }
        return this.tokenCache;
    }

    /* access modifiers changed from: private */
    public static String[] stringArray(List<String> list) {
        String[] array = new String[list.size()];
        if (list != null) {
            for (int i = 0; i < array.length; i++) {
                array[i] = list.get(i);
            }
        }
        return array;
    }

    /* access modifiers changed from: private */
    public static List<String> stringList(String[] array) {
        if (array != null) {
            return Arrays.asList(array);
        }
        return Collections.emptyList();
    }

    private class SetterTokenCachingStrategy extends TokenCachingStrategy {
        private SetterTokenCachingStrategy() {
        }

        /* synthetic */ SetterTokenCachingStrategy(Facebook facebook, SetterTokenCachingStrategy setterTokenCachingStrategy) {
            this();
        }

        public Bundle load() {
            Bundle bundle = new Bundle();
            if (Facebook.this.accessToken != null) {
                TokenCachingStrategy.putToken(bundle, Facebook.this.accessToken);
                TokenCachingStrategy.putExpirationMilliseconds(bundle, Facebook.this.accessExpiresMillisecondsAfterEpoch);
                TokenCachingStrategy.putPermissions(bundle, Facebook.stringList(Facebook.this.pendingAuthorizationPermissions));
                TokenCachingStrategy.putSource(bundle, AccessTokenSource.WEB_VIEW);
                TokenCachingStrategy.putLastRefreshMilliseconds(bundle, Facebook.this.lastAccessUpdateMillisecondsAfterEpoch);
            }
            return bundle;
        }

        public void save(Bundle bundle) {
            Facebook.this.accessToken = TokenCachingStrategy.getToken(bundle);
            Facebook.this.accessExpiresMillisecondsAfterEpoch = TokenCachingStrategy.getExpirationMilliseconds(bundle);
            Facebook.this.pendingAuthorizationPermissions = Facebook.stringArray(TokenCachingStrategy.getPermissions(bundle));
            Facebook.this.lastAccessUpdateMillisecondsAfterEpoch = TokenCachingStrategy.getLastRefreshMilliseconds(bundle);
        }

        public void clear() {
            Facebook.this.accessToken = null;
        }
    }

    @Deprecated
    public static String getAttributionId(ContentResolver contentResolver) {
        return Settings.getAttributionId(contentResolver);
    }

    @Deprecated
    public boolean getShouldAutoPublishInstall() {
        return Settings.getShouldAutoPublishInstall();
    }

    @Deprecated
    public void setShouldAutoPublishInstall(boolean value) {
        Settings.setShouldAutoPublishInstall(value);
    }

    @Deprecated
    public boolean publishInstall(Context context) {
        Settings.publishInstallAsync(context, this.mAppId);
        return false;
    }
}
