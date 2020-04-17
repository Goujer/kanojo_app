package com.facebook;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import com.facebook.GetTokenClient;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.android.R;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.google.android.gcm.GCMConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class AuthorizationClient implements Serializable {
    private static final long serialVersionUID = 1;
    transient BackgroundProcessingListener backgroundProcessingListener;
    transient boolean checkedInternetPermission;
    transient Context context;
    AuthHandler currentHandler;
    List<AuthHandler> handlersToTry;
    transient OnCompletedListener onCompletedListener;
    AuthorizationRequest pendingRequest;
    transient StartActivityDelegate startActivityDelegate;

    interface BackgroundProcessingListener {
        void onBackgroundProcessingStarted();

        void onBackgroundProcessingStopped();
    }

    interface OnCompletedListener {
        void onCompleted(Result result);
    }

    interface StartActivityDelegate {
        Activity getActivityContext();

        void startActivityForResult(Intent intent, int i);
    }

    AuthorizationClient() {
    }

    /* access modifiers changed from: package-private */
    public void setContext(Context context2) {
        this.context = context2;
        this.startActivityDelegate = null;
    }

    /* access modifiers changed from: package-private */
    public void setContext(final Activity activity) {
        this.context = activity;
        this.startActivityDelegate = new StartActivityDelegate() {
            public void startActivityForResult(Intent intent, int requestCode) {
                activity.startActivityForResult(intent, requestCode);
            }

            public Activity getActivityContext() {
                return activity;
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void startOrContinueAuth(AuthorizationRequest request) {
        if (getInProgress()) {
            continueAuth();
        } else {
            authorize(request);
        }
    }

    /* access modifiers changed from: package-private */
    public void authorize(AuthorizationRequest request) {
        if (request != null) {
            if (this.pendingRequest != null) {
                throw new FacebookException("Attempted to authorize while a request is pending.");
            } else if (!request.needsNewTokenValidation() || checkInternetPermission()) {
                this.pendingRequest = request;
                this.handlersToTry = getHandlerTypes(request);
                tryNextHandler();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void continueAuth() {
        if (this.pendingRequest == null || this.currentHandler == null) {
            throw new FacebookException("Attempted to continue authorization without a pending request.");
        } else if (this.currentHandler.needsRestart()) {
            this.currentHandler.cancel();
            tryCurrentHandler();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean getInProgress() {
        return (this.pendingRequest == null || this.currentHandler == null) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public void cancelCurrentHandler() {
        if (this.currentHandler != null) {
            this.currentHandler.cancel();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.pendingRequest.getRequestCode()) {
            return this.currentHandler.onActivityResult(requestCode, resultCode, data);
        }
        return false;
    }

    private List<AuthHandler> getHandlerTypes(AuthorizationRequest request) {
        ArrayList<AuthHandler> handlers = new ArrayList<>();
        SessionLoginBehavior behavior = request.getLoginBehavior();
        if (behavior.allowsKatanaAuth()) {
            if (!request.isLegacy()) {
                handlers.add(new GetTokenAuthHandler());
                handlers.add(new KatanaLoginDialogAuthHandler());
            }
            handlers.add(new KatanaProxyAuthHandler());
        }
        if (behavior.allowsWebViewAuth()) {
            handlers.add(new WebViewAuthHandler());
        }
        return handlers;
    }

    /* access modifiers changed from: package-private */
    public boolean checkInternetPermission() {
        if (this.checkedInternetPermission) {
            return true;
        }
        if (checkPermission("android.permission.INTERNET") != 0) {
            complete(Result.createErrorResult(this.context.getString(R.string.com_facebook_internet_permission_error_title), this.context.getString(R.string.com_facebook_internet_permission_error_message)));
            return false;
        }
        this.checkedInternetPermission = true;
        return true;
    }

    /* access modifiers changed from: package-private */
    public void tryNextHandler() {
        while (this.handlersToTry != null && !this.handlersToTry.isEmpty()) {
            this.currentHandler = this.handlersToTry.remove(0);
            if (tryCurrentHandler()) {
                return;
            }
        }
        if (this.pendingRequest != null) {
            completeWithFailure();
        }
    }

    private void completeWithFailure() {
        complete(Result.createErrorResult("Login attempt failed.", (String) null));
    }

    /* access modifiers changed from: package-private */
    public boolean tryCurrentHandler() {
        if (!this.currentHandler.needsInternetPermission() || checkInternetPermission()) {
            return this.currentHandler.tryAuthorize(this.pendingRequest);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void completeAndValidate(Result outcome) {
        if (outcome.token == null || !this.pendingRequest.needsNewTokenValidation()) {
            complete(outcome);
        } else {
            validateSameFbidAndFinish(outcome);
        }
    }

    /* access modifiers changed from: package-private */
    public void complete(Result outcome) {
        this.handlersToTry = null;
        this.currentHandler = null;
        this.pendingRequest = null;
        notifyOnCompleteListener(outcome);
    }

    /* access modifiers changed from: package-private */
    public OnCompletedListener getOnCompletedListener() {
        return this.onCompletedListener;
    }

    /* access modifiers changed from: package-private */
    public void setOnCompletedListener(OnCompletedListener onCompletedListener2) {
        this.onCompletedListener = onCompletedListener2;
    }

    /* access modifiers changed from: package-private */
    public BackgroundProcessingListener getBackgroundProcessingListener() {
        return this.backgroundProcessingListener;
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundProcessingListener(BackgroundProcessingListener backgroundProcessingListener2) {
        this.backgroundProcessingListener = backgroundProcessingListener2;
    }

    /* access modifiers changed from: package-private */
    public StartActivityDelegate getStartActivityDelegate() {
        if (this.startActivityDelegate != null) {
            return this.startActivityDelegate;
        }
        if (this.pendingRequest != null) {
            return new StartActivityDelegate() {
                public void startActivityForResult(Intent intent, int requestCode) {
                    AuthorizationClient.this.pendingRequest.getStartActivityDelegate().startActivityForResult(intent, requestCode);
                }

                public Activity getActivityContext() {
                    return AuthorizationClient.this.pendingRequest.getStartActivityDelegate().getActivityContext();
                }
            };
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public int checkPermission(String permission) {
        return this.context.checkCallingOrSelfPermission(permission);
    }

    /* access modifiers changed from: package-private */
    public void validateSameFbidAndFinish(Result pendingResult) {
        if (pendingResult.token == null) {
            throw new FacebookException("Can't validate without a token");
        }
        RequestBatch batch = createReauthValidationBatch(pendingResult);
        notifyBackgroundProcessingStart();
        batch.executeAsync();
    }

    /* access modifiers changed from: package-private */
    public RequestBatch createReauthValidationBatch(final Result pendingResult) {
        final ArrayList<String> fbids = new ArrayList<>();
        final ArrayList<String> tokenPermissions = new ArrayList<>();
        String newToken = pendingResult.token.getToken();
        Request.Callback meCallback = new Request.Callback() {
            public void onCompleted(Response response) {
                try {
                    GraphUser user = (GraphUser) response.getGraphObjectAs(GraphUser.class);
                    if (user != null) {
                        fbids.add(user.getId());
                    }
                } catch (Exception e) {
                }
            }
        };
        String validateSameFbidAsToken = this.pendingRequest.getPreviousAccessToken();
        Request requestCurrentTokenMe = createGetProfileIdRequest(validateSameFbidAsToken);
        requestCurrentTokenMe.setCallback(meCallback);
        Request requestNewTokenMe = createGetProfileIdRequest(newToken);
        requestNewTokenMe.setCallback(meCallback);
        Request requestCurrentTokenPermissions = createGetPermissionsRequest(validateSameFbidAsToken);
        requestCurrentTokenPermissions.setCallback(new Request.Callback() {
            public void onCompleted(Response response) {
                GraphObjectList<GraphObject> data;
                try {
                    GraphMultiResult result = (GraphMultiResult) response.getGraphObjectAs(GraphMultiResult.class);
                    if (result != null && (data = result.getData()) != null && data.size() == 1) {
                        tokenPermissions.addAll(((GraphObject) data.get(0)).asMap().keySet());
                    }
                } catch (Exception e) {
                }
            }
        });
        RequestBatch batch = new RequestBatch(requestCurrentTokenMe, requestNewTokenMe, requestCurrentTokenPermissions);
        batch.setBatchApplicationId(this.pendingRequest.getApplicationId());
        batch.addCallback(new RequestBatch.Callback() {
            public void onBatchCompleted(RequestBatch batch) {
                Result result;
                try {
                    if (fbids.size() != 2 || fbids.get(0) == null || fbids.get(1) == null || !((String) fbids.get(0)).equals(fbids.get(1))) {
                        result = Result.createErrorResult("User logged in as different Facebook user.", (String) null);
                    } else {
                        result = Result.createTokenResult(AccessToken.createFromTokenWithRefreshedPermissions(pendingResult.token, tokenPermissions));
                    }
                    AuthorizationClient.this.complete(result);
                } catch (Exception ex) {
                    AuthorizationClient.this.complete(Result.createErrorResult("Caught exception", ex.getMessage()));
                } finally {
                    AuthorizationClient.this.notifyBackgroundProcessingStop();
                }
            }
        });
        return batch;
    }

    /* access modifiers changed from: package-private */
    public Request createGetPermissionsRequest(String accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        parameters.putString("access_token", accessToken);
        return new Request((Session) null, "me/permissions", parameters, HttpMethod.GET, (Request.Callback) null);
    }

    /* access modifiers changed from: package-private */
    public Request createGetProfileIdRequest(String accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        parameters.putString("access_token", accessToken);
        return new Request((Session) null, "me", parameters, HttpMethod.GET, (Request.Callback) null);
    }

    private void notifyOnCompleteListener(Result outcome) {
        if (this.onCompletedListener != null) {
            this.onCompletedListener.onCompleted(outcome);
        }
    }

    /* access modifiers changed from: private */
    public void notifyBackgroundProcessingStart() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStarted();
        }
    }

    /* access modifiers changed from: private */
    public void notifyBackgroundProcessingStop() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStopped();
        }
    }

    abstract class AuthHandler implements Serializable {
        private static final long serialVersionUID = 1;

        /* access modifiers changed from: package-private */
        public abstract boolean tryAuthorize(AuthorizationRequest authorizationRequest);

        AuthHandler() {
        }

        /* access modifiers changed from: package-private */
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean needsRestart() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean needsInternetPermission() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
        }
    }

    class WebViewAuthHandler extends AuthHandler {
        private static final long serialVersionUID = 1;
        private transient WebDialog loginDialog;

        WebViewAuthHandler() {
            super();
        }

        /* access modifiers changed from: package-private */
        public boolean needsRestart() {
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean needsInternetPermission() {
            return true;
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            if (this.loginDialog != null) {
                this.loginDialog.dismiss();
                this.loginDialog = null;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean tryAuthorize(final AuthorizationRequest request) {
            String applicationId = request.getApplicationId();
            Bundle parameters = new Bundle();
            if (!Utility.isNullOrEmpty(request.getPermissions())) {
                parameters.putString("scope", TextUtils.join(",", request.getPermissions()));
            }
            Utility.clearFacebookCookies(AuthorizationClient.this.context);
            this.loginDialog = ((WebDialog.Builder) new AuthDialogBuilder(AuthorizationClient.this.getStartActivityDelegate().getActivityContext(), applicationId, parameters).setOnCompleteListener(new WebDialog.OnCompleteListener() {
                public void onComplete(Bundle values, FacebookException error) {
                    WebViewAuthHandler.this.onWebDialogComplete(request, values, error);
                }
            })).build();
            this.loginDialog.show();
            return true;
        }

        /* access modifiers changed from: package-private */
        public void onWebDialogComplete(AuthorizationRequest request, Bundle values, FacebookException error) {
            Result outcome;
            if (values != null) {
                CookieSyncManager.createInstance(AuthorizationClient.this.context).sync();
                outcome = Result.createTokenResult(AccessToken.createFromWebBundle(request.getPermissions(), values, AccessTokenSource.WEB_VIEW));
            } else if (error instanceof FacebookOperationCanceledException) {
                outcome = Result.createCancelResult("User canceled log in.");
            } else {
                outcome = Result.createErrorResult(error.getMessage(), (String) null);
            }
            AuthorizationClient.this.completeAndValidate(outcome);
        }
    }

    class GetTokenAuthHandler extends AuthHandler {
        private static final long serialVersionUID = 1;
        private transient GetTokenClient getTokenClient;

        GetTokenAuthHandler() {
            super();
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            if (this.getTokenClient != null) {
                this.getTokenClient.cancel();
                this.getTokenClient = null;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean tryAuthorize(final AuthorizationRequest request) {
            this.getTokenClient = new GetTokenClient(AuthorizationClient.this.context, request.getApplicationId());
            if (!this.getTokenClient.start()) {
                return false;
            }
            AuthorizationClient.this.notifyBackgroundProcessingStart();
            this.getTokenClient.setCompletedListener(new GetTokenClient.CompletedListener() {
                public void completed(Bundle result) {
                    GetTokenAuthHandler.this.getTokenCompleted(request, result);
                }
            });
            return true;
        }

        /* access modifiers changed from: package-private */
        public void getTokenCompleted(AuthorizationRequest request, Bundle result) {
            this.getTokenClient = null;
            AuthorizationClient.this.notifyBackgroundProcessingStop();
            if (result != null) {
                ArrayList<String> currentPermissions = result.getStringArrayList("com.facebook.platform.extra.PERMISSIONS");
                List<String> permissions = request.getPermissions();
                if (currentPermissions == null || (permissions != null && !currentPermissions.containsAll(permissions))) {
                    ArrayList<String> newPermissions = new ArrayList<>();
                    for (String permission : permissions) {
                        if (!currentPermissions.contains(permission)) {
                            newPermissions.add(permission);
                        }
                    }
                    request.setPermissions(newPermissions);
                } else {
                    AuthorizationClient.this.completeAndValidate(Result.createTokenResult(AccessToken.createFromNativeLogin(result, AccessTokenSource.FACEBOOK_APPLICATION_SERVICE)));
                    return;
                }
            }
            AuthorizationClient.this.tryNextHandler();
        }
    }

    abstract class KatanaAuthHandler extends AuthHandler {
        private static final long serialVersionUID = 1;

        KatanaAuthHandler() {
            super();
        }

        /* access modifiers changed from: protected */
        public boolean tryIntent(Intent intent, int requestCode) {
            if (intent == null) {
                return false;
            }
            try {
                AuthorizationClient.this.getStartActivityDelegate().startActivityForResult(intent, requestCode);
                return true;
            } catch (ActivityNotFoundException e) {
                return false;
            }
        }
    }

    class KatanaLoginDialogAuthHandler extends KatanaAuthHandler {
        private static final long serialVersionUID = 1;

        KatanaLoginDialogAuthHandler() {
            super();
        }

        /* access modifiers changed from: package-private */
        public boolean tryAuthorize(AuthorizationRequest request) {
            return tryIntent(NativeProtocol.createLoginDialog20121101Intent(AuthorizationClient.this.context, request.getApplicationId(), new ArrayList(request.getPermissions()), request.getDefaultAudience().getNativeProtocolAudience()), request.getRequestCode());
        }

        /* access modifiers changed from: package-private */
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            Result outcome;
            if (NativeProtocol.isServiceDisabledResult20121101(data)) {
                AuthorizationClient.this.tryNextHandler();
                return true;
            }
            if (resultCode == 0) {
                outcome = Result.createCancelResult(data.getStringExtra("com.facebook.platform.status.ERROR_DESCRIPTION"));
            } else if (resultCode != -1) {
                outcome = Result.createErrorResult("Unexpected resultCode from authorization.", (String) null);
            } else {
                outcome = handleResultOk(data);
            }
            if (outcome != null) {
                AuthorizationClient.this.completeAndValidate(outcome);
                return true;
            }
            AuthorizationClient.this.tryNextHandler();
            return true;
        }

        private Result handleResultOk(Intent data) {
            Bundle extras = data.getExtras();
            String errorType = extras.getString("com.facebook.platform.status.ERROR_TYPE");
            if (errorType == null) {
                return Result.createTokenResult(AccessToken.createFromNativeLogin(extras, AccessTokenSource.FACEBOOK_APPLICATION_NATIVE));
            }
            if ("ServiceDisabled".equals(errorType)) {
                return null;
            }
            if ("UserCanceled".equals(errorType)) {
                return Result.createCancelResult((String) null);
            }
            return Result.createErrorResult(errorType, extras.getString("error_description"));
        }
    }

    class KatanaProxyAuthHandler extends KatanaAuthHandler {
        private static final long serialVersionUID = 1;

        KatanaProxyAuthHandler() {
            super();
        }

        /* access modifiers changed from: package-private */
        public boolean tryAuthorize(AuthorizationRequest request) {
            return tryIntent(NativeProtocol.createProxyAuthIntent(AuthorizationClient.this.context, request.getApplicationId(), request.getPermissions()), request.getRequestCode());
        }

        /* access modifiers changed from: package-private */
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            Result outcome;
            if (resultCode == 0) {
                outcome = Result.createCancelResult(data.getStringExtra(GCMConstants.EXTRA_ERROR));
            } else if (resultCode != -1) {
                outcome = Result.createErrorResult("Unexpected resultCode from authorization.", (String) null);
            } else {
                outcome = handleResultOk(data);
            }
            if (outcome != null) {
                AuthorizationClient.this.completeAndValidate(outcome);
                return true;
            }
            AuthorizationClient.this.tryNextHandler();
            return true;
        }

        private Result handleResultOk(Intent data) {
            Bundle extras = data.getExtras();
            String error = extras.getString(GCMConstants.EXTRA_ERROR);
            if (error == null) {
                error = extras.getString("error_type");
            }
            if (error == null) {
                return Result.createTokenResult(AccessToken.createFromWebBundle(AuthorizationClient.this.pendingRequest.getPermissions(), extras, AccessTokenSource.FACEBOOK_APPLICATION_WEB));
            }
            if (ServerProtocol.errorsProxyAuthDisabled.contains(error)) {
                return null;
            }
            if (ServerProtocol.errorsUserCanceled.contains(error)) {
                return Result.createCancelResult((String) null);
            }
            return Result.createErrorResult(error, extras.getString("error_description"));
        }
    }

    static class AuthDialogBuilder extends WebDialog.Builder {
        private static final String OAUTH_DIALOG = "oauth";
        static final String REDIRECT_URI = "fbconnect://success";

        public AuthDialogBuilder(Context context, String applicationId, Bundle parameters) {
            super(context, applicationId, OAUTH_DIALOG, parameters);
        }

        public WebDialog build() {
            Bundle parameters = getParameters();
            parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, "fbconnect://success");
            parameters.putString("client_id", getApplicationId());
            return new WebDialog(getContext(), OAUTH_DIALOG, parameters, getTheme(), getListener());
        }
    }

    static class AuthorizationRequest implements Serializable {
        private static final long serialVersionUID = 1;
        private String applicationId;
        private SessionDefaultAudience defaultAudience;
        private boolean isLegacy = false;
        private SessionLoginBehavior loginBehavior;
        private List<String> permissions;
        private String previousAccessToken;
        private int requestCode;
        private final transient StartActivityDelegate startActivityDelegate;

        AuthorizationRequest(SessionLoginBehavior loginBehavior2, int requestCode2, boolean isLegacy2, List<String> permissions2, SessionDefaultAudience defaultAudience2, String applicationId2, String validateSameFbidAsToken, StartActivityDelegate startActivityDelegate2) {
            this.loginBehavior = loginBehavior2;
            this.requestCode = requestCode2;
            this.isLegacy = isLegacy2;
            this.permissions = permissions2;
            this.defaultAudience = defaultAudience2;
            this.applicationId = applicationId2;
            this.previousAccessToken = validateSameFbidAsToken;
            this.startActivityDelegate = startActivityDelegate2;
        }

        /* access modifiers changed from: package-private */
        public StartActivityDelegate getStartActivityDelegate() {
            return this.startActivityDelegate;
        }

        /* access modifiers changed from: package-private */
        public List<String> getPermissions() {
            return this.permissions;
        }

        /* access modifiers changed from: package-private */
        public void setPermissions(List<String> permissions2) {
            this.permissions = permissions2;
        }

        /* access modifiers changed from: package-private */
        public SessionLoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }

        /* access modifiers changed from: package-private */
        public int getRequestCode() {
            return this.requestCode;
        }

        /* access modifiers changed from: package-private */
        public SessionDefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }

        /* access modifiers changed from: package-private */
        public String getApplicationId() {
            return this.applicationId;
        }

        /* access modifiers changed from: package-private */
        public boolean isLegacy() {
            return this.isLegacy;
        }

        /* access modifiers changed from: package-private */
        public void setIsLegacy(boolean isLegacy2) {
            this.isLegacy = isLegacy2;
        }

        /* access modifiers changed from: package-private */
        public String getPreviousAccessToken() {
            return this.previousAccessToken;
        }

        /* access modifiers changed from: package-private */
        public boolean needsNewTokenValidation() {
            return this.previousAccessToken != null && !this.isLegacy;
        }
    }

    static class Result implements Serializable {
        private static final long serialVersionUID = 1;
        final Code code;
        final String errorMessage;
        final AccessToken token;

        enum Code {
            SUCCESS,
            CANCEL,
            ERROR
        }

        private Result(Code code2, AccessToken token2, String errorMessage2) {
            this.token = token2;
            this.errorMessage = errorMessage2;
            this.code = code2;
        }

        static Result createTokenResult(AccessToken token2) {
            return new Result(Code.SUCCESS, token2, (String) null);
        }

        static Result createCancelResult(String message) {
            return new Result(Code.CANCEL, (AccessToken) null, message);
        }

        static Result createErrorResult(String errorType, String errorDescription) {
            String message = errorType;
            if (errorDescription != null) {
                message = String.valueOf(message) + ": " + errorDescription;
            }
            return new Result(Code.ERROR, (AccessToken) null, message);
        }
    }
}
