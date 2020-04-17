package com.facebook.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.android.R;
import com.facebook.internal.SessionAuthorizationType;
import com.facebook.internal.SessionTracker;
import com.facebook.internal.Utility;
import com.facebook.model.GraphUser;
import java.util.Collections;
import java.util.List;

public class LoginButton extends Button {
    /* access modifiers changed from: private */
    public static final String TAG = LoginButton.class.getName();
    /* access modifiers changed from: private */
    public String applicationId = null;
    /* access modifiers changed from: private */
    public boolean confirmLogout;
    private boolean fetchUserInfo;
    private String loginText;
    private String logoutText;
    /* access modifiers changed from: private */
    public Fragment parentFragment;
    /* access modifiers changed from: private */
    public LoginButtonProperties properties = new LoginButtonProperties();
    /* access modifiers changed from: private */
    public SessionTracker sessionTracker;
    /* access modifiers changed from: private */
    public GraphUser user = null;
    /* access modifiers changed from: private */
    public UserInfoChangedCallback userInfoChangedCallback;
    private Session userInfoSession = null;

    public interface OnErrorListener {
        void onError(FacebookException facebookException);
    }

    public interface UserInfoChangedCallback {
        void onUserInfoFetched(GraphUser graphUser);
    }

    static class LoginButtonProperties {
        /* access modifiers changed from: private */
        public SessionAuthorizationType authorizationType = null;
        /* access modifiers changed from: private */
        public SessionDefaultAudience defaultAudience = SessionDefaultAudience.FRIENDS;
        /* access modifiers changed from: private */
        public SessionLoginBehavior loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
        /* access modifiers changed from: private */
        public OnErrorListener onErrorListener;
        /* access modifiers changed from: private */
        public List<String> permissions = Collections.emptyList();
        /* access modifiers changed from: private */
        public Session.StatusCallback sessionStatusCallback;

        LoginButtonProperties() {
        }

        public void setOnErrorListener(OnErrorListener onErrorListener2) {
            this.onErrorListener = onErrorListener2;
        }

        public OnErrorListener getOnErrorListener() {
            return this.onErrorListener;
        }

        public void setDefaultAudience(SessionDefaultAudience defaultAudience2) {
            this.defaultAudience = defaultAudience2;
        }

        public SessionDefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }

        public void setReadPermissions(List<String> permissions2, Session session) {
            if (SessionAuthorizationType.PUBLISH.equals(this.authorizationType)) {
                throw new UnsupportedOperationException("Cannot call setReadPermissions after setPublishPermissions has been called.");
            } else if (validatePermissions(permissions2, SessionAuthorizationType.READ, session)) {
                this.permissions = permissions2;
                this.authorizationType = SessionAuthorizationType.READ;
            }
        }

        public void setPublishPermissions(List<String> permissions2, Session session) {
            if (SessionAuthorizationType.READ.equals(this.authorizationType)) {
                throw new UnsupportedOperationException("Cannot call setPublishPermissions after setReadPermissions has been called.");
            } else if (validatePermissions(permissions2, SessionAuthorizationType.PUBLISH, session)) {
                this.permissions = permissions2;
                this.authorizationType = SessionAuthorizationType.PUBLISH;
            }
        }

        private boolean validatePermissions(List<String> permissions2, SessionAuthorizationType authType, Session currentSession) {
            if (SessionAuthorizationType.PUBLISH.equals(authType) && Utility.isNullOrEmpty(permissions2)) {
                throw new IllegalArgumentException("Permissions for publish actions cannot be null or empty.");
            } else if (currentSession == null || !currentSession.isOpened() || Utility.isSubset(permissions2, currentSession.getPermissions())) {
                return true;
            } else {
                Log.e(LoginButton.TAG, "Cannot set additional permissions when session is already open.");
                return false;
            }
        }

        /* access modifiers changed from: package-private */
        public List<String> getPermissions() {
            return this.permissions;
        }

        public void clearPermissions() {
            this.permissions = null;
            this.authorizationType = null;
        }

        public void setLoginBehavior(SessionLoginBehavior loginBehavior2) {
            this.loginBehavior = loginBehavior2;
        }

        public SessionLoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }

        public void setSessionStatusCallback(Session.StatusCallback callback) {
            this.sessionStatusCallback = callback;
        }

        public Session.StatusCallback getSessionStatusCallback() {
            return this.sessionStatusCallback;
        }
    }

    public LoginButton(Context context) {
        super(context);
        initializeActiveSessionWithCachedToken(context);
        finishInit();
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs.getStyleAttribute() == 0) {
            setTextColor(getResources().getColor(R.color.com_facebook_loginview_text_color));
            setTextSize(0, getResources().getDimension(R.dimen.com_facebook_loginview_text_size));
            setPadding(getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_left), getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_top), getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_right), getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_padding_bottom));
            setWidth(getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_width));
            setHeight(getResources().getDimensionPixelSize(R.dimen.com_facebook_loginview_height));
            setGravity(17);
            parseAttributes(attrs);
            if (isInEditMode()) {
                setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
                this.loginText = "Log in";
                return;
            }
            setBackgroundResource(R.drawable.com_facebook_loginbutton_blue);
            initializeActiveSessionWithCachedToken(context);
        }
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(attrs);
        initializeActiveSessionWithCachedToken(context);
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.properties.setOnErrorListener(onErrorListener);
    }

    public OnErrorListener getOnErrorListener() {
        return this.properties.getOnErrorListener();
    }

    public void setDefaultAudience(SessionDefaultAudience defaultAudience) {
        this.properties.setDefaultAudience(defaultAudience);
    }

    public SessionDefaultAudience getDefaultAudience() {
        return this.properties.getDefaultAudience();
    }

    public void setReadPermissions(List<String> permissions) {
        this.properties.setReadPermissions(permissions, this.sessionTracker.getSession());
    }

    public void setPublishPermissions(List<String> permissions) {
        this.properties.setPublishPermissions(permissions, this.sessionTracker.getSession());
    }

    public void clearPermissions() {
        this.properties.clearPermissions();
    }

    public void setLoginBehavior(SessionLoginBehavior loginBehavior) {
        this.properties.setLoginBehavior(loginBehavior);
    }

    public SessionLoginBehavior getLoginBehavior() {
        return this.properties.getLoginBehavior();
    }

    public void setApplicationId(String applicationId2) {
        this.applicationId = applicationId2;
    }

    public UserInfoChangedCallback getUserInfoChangedCallback() {
        return this.userInfoChangedCallback;
    }

    public void setUserInfoChangedCallback(UserInfoChangedCallback userInfoChangedCallback2) {
        this.userInfoChangedCallback = userInfoChangedCallback2;
    }

    public void setSessionStatusCallback(Session.StatusCallback callback) {
        this.properties.setSessionStatusCallback(callback);
    }

    public Session.StatusCallback getSessionStatusCallback() {
        return this.properties.getSessionStatusCallback();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Session session = this.sessionTracker.getSession();
        if (session != null) {
            return session.onActivityResult((Activity) getContext(), requestCode, resultCode, data);
        }
        return false;
    }

    public void setSession(Session newSession) {
        this.sessionTracker.setSession(newSession);
        fetchUserInfo();
        setButtonText();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        finishInit();
    }

    private void finishInit() {
        setOnClickListener(new LoginClickListener(this, (LoginClickListener) null));
        setButtonText();
        if (!isInEditMode()) {
            this.sessionTracker = new SessionTracker(getContext(), new LoginButtonCallback(this, (LoginButtonCallback) null), (Session) null, false);
            fetchUserInfo();
        }
    }

    public void setFragment(Fragment fragment) {
        this.parentFragment = fragment;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.sessionTracker != null && !this.sessionTracker.isTracking()) {
            this.sessionTracker.startTracking();
            fetchUserInfo();
            setButtonText();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.sessionTracker != null) {
            this.sessionTracker.stopTracking();
        }
    }

    /* access modifiers changed from: package-private */
    public List<String> getPermissions() {
        return this.properties.getPermissions();
    }

    /* access modifiers changed from: package-private */
    public void setProperties(LoginButtonProperties properties2) {
        this.properties = properties2;
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.com_facebook_login_view);
        this.confirmLogout = a.getBoolean(0, true);
        this.fetchUserInfo = a.getBoolean(1, true);
        this.loginText = a.getString(2);
        this.logoutText = a.getString(3);
        a.recycle();
    }

    /* access modifiers changed from: private */
    public void setButtonText() {
        String string;
        String string2;
        if (this.sessionTracker == null || this.sessionTracker.getOpenSession() == null) {
            if (this.loginText != null) {
                string = this.loginText;
            } else {
                string = getResources().getString(R.string.com_facebook_loginview_log_in_button);
            }
            setText(string);
            return;
        }
        if (this.logoutText != null) {
            string2 = this.logoutText;
        } else {
            string2 = getResources().getString(R.string.com_facebook_loginview_log_out_button);
        }
        setText(string2);
    }

    private boolean initializeActiveSessionWithCachedToken(Context context) {
        if (context == null) {
            return false;
        }
        Session session = Session.getActiveSession();
        if (session != null) {
            return session.isOpened();
        }
        if (Utility.getMetadataApplicationId(context) == null || Session.openActiveSessionFromCache(context) == null) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void fetchUserInfo() {
        if (this.fetchUserInfo) {
            final Session currentSession = this.sessionTracker.getOpenSession();
            if (currentSession == null) {
                this.user = null;
                if (this.userInfoChangedCallback != null) {
                    this.userInfoChangedCallback.onUserInfoFetched(this.user);
                }
            } else if (currentSession != this.userInfoSession) {
                Request.executeBatchAsync(Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
                    public void onCompleted(GraphUser me, Response response) {
                        if (currentSession == LoginButton.this.sessionTracker.getOpenSession()) {
                            LoginButton.this.user = me;
                            if (LoginButton.this.userInfoChangedCallback != null) {
                                LoginButton.this.userInfoChangedCallback.onUserInfoFetched(LoginButton.this.user);
                            }
                        }
                        if (response.getError() != null) {
                            LoginButton.this.handleError(response.getError().getException());
                        }
                    }
                }));
                this.userInfoSession = currentSession;
            }
        }
    }

    private class LoginClickListener implements View.OnClickListener {
        private LoginClickListener() {
        }

        /* synthetic */ LoginClickListener(LoginButton loginButton, LoginClickListener loginClickListener) {
            this();
        }

        public void onClick(View v) {
            String message;
            Context context = LoginButton.this.getContext();
            final Session openSession = LoginButton.this.sessionTracker.getOpenSession();
            if (openSession == null) {
                Session currentSession = LoginButton.this.sessionTracker.getSession();
                if (currentSession == null || currentSession.getState().isClosed()) {
                    LoginButton.this.sessionTracker.setSession((Session) null);
                    Session session = new Session.Builder(context).setApplicationId(LoginButton.this.applicationId).build();
                    Session.setActiveSession(session);
                    currentSession = session;
                }
                if (!currentSession.isOpened()) {
                    Session.OpenRequest openRequest = null;
                    if (LoginButton.this.parentFragment != null) {
                        openRequest = new Session.OpenRequest(LoginButton.this.parentFragment);
                    } else if (context instanceof Activity) {
                        openRequest = new Session.OpenRequest((Activity) context);
                    }
                    if (openRequest != null) {
                        openRequest.setDefaultAudience(LoginButton.this.properties.defaultAudience);
                        openRequest.setPermissions(LoginButton.this.properties.permissions);
                        openRequest.setLoginBehavior(LoginButton.this.properties.loginBehavior);
                        if (SessionAuthorizationType.PUBLISH.equals(LoginButton.this.properties.authorizationType)) {
                            currentSession.openForPublish(openRequest);
                        } else {
                            currentSession.openForRead(openRequest);
                        }
                    }
                }
            } else if (LoginButton.this.confirmLogout) {
                String logout = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_log_out_action);
                String cancel = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_cancel_action);
                if (LoginButton.this.user == null || LoginButton.this.user.getName() == null) {
                    message = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_logged_in_using_facebook);
                } else {
                    message = String.format(LoginButton.this.getResources().getString(R.string.com_facebook_loginview_logged_in_as), new Object[]{LoginButton.this.user.getName()});
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message).setCancelable(true).setPositiveButton(logout, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openSession.closeAndClearTokenInformation();
                    }
                }).setNegativeButton(cancel, (DialogInterface.OnClickListener) null);
                builder.create().show();
            } else {
                openSession.closeAndClearTokenInformation();
            }
        }
    }

    private class LoginButtonCallback implements Session.StatusCallback {
        private LoginButtonCallback() {
        }

        /* synthetic */ LoginButtonCallback(LoginButton loginButton, LoginButtonCallback loginButtonCallback) {
            this();
        }

        public void call(Session session, SessionState state, Exception exception) {
            LoginButton.this.fetchUserInfo();
            LoginButton.this.setButtonText();
            if (exception != null) {
                LoginButton.this.handleError(exception);
            }
            if (LoginButton.this.properties.sessionStatusCallback != null) {
                LoginButton.this.properties.sessionStatusCallback.call(session, state, exception);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handleError(Exception exception) {
        if (this.properties.onErrorListener == null) {
            return;
        }
        if (exception instanceof FacebookException) {
            this.properties.onErrorListener.onError((FacebookException) exception);
        } else {
            this.properties.onErrorListener.onError(new FacebookException((Throwable) exception));
        }
    }
}
