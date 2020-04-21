package com.facebook.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.android.R;
import com.facebook.model.GraphUser;
import com.facebook.widget.ImageRequest;
import com.facebook.widget.LoginButton;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class UserSettingsFragment extends FacebookFragment {
    private static final String FIELDS = "fields";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PICTURE = "picture";
    private static final String REQUEST_FIELDS = TextUtils.join(",", new String[]{ID, NAME, PICTURE});
    private TextView connectedStateLabel;
    /* access modifiers changed from: private */
    public LoginButton loginButton;
    private LoginButton.LoginButtonProperties loginButtonProperties = new LoginButton.LoginButtonProperties();
    private Session.StatusCallback sessionStatusCallback;
    /* access modifiers changed from: private */
    public GraphUser user;
    private Session userInfoSession;
    private Drawable userProfilePic;
    private String userProfilePicID;

    public /* bridge */ /* synthetic */ void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public /* bridge */ /* synthetic */ void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public /* bridge */ /* synthetic */ void onDestroy() {
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.com_facebook_usersettingsfragment, container, false);
        this.loginButton = (LoginButton) view.findViewById(R.id.com_facebook_usersettingsfragment_login_button);
        this.loginButton.setProperties(this.loginButtonProperties);
        this.loginButton.setFragment(this);
        Session session = getSession();
        if (session != null && !session.equals(Session.getActiveSession())) {
            this.loginButton.setSession(session);
        }
        this.connectedStateLabel = (TextView) view.findViewById(R.id.com_facebook_usersettingsfragment_profile_name);
        if (view.getBackground() == null) {
            view.setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
        } else {
            view.getBackground().setDither(true);
        }
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void onResume() {
        super.onResume();
        fetchUserInfo();
        updateUI();
    }

    public void setSession(Session newSession) {
        super.setSession(newSession);
        if (this.loginButton != null) {
            this.loginButton.setSession(newSession);
        }
        fetchUserInfo();
        updateUI();
    }

    public void setDefaultAudience(SessionDefaultAudience defaultAudience) {
        this.loginButtonProperties.setDefaultAudience(defaultAudience);
    }

    public SessionDefaultAudience getDefaultAudience() {
        return this.loginButtonProperties.getDefaultAudience();
    }

    public void setReadPermissions(List<String> permissions) {
        this.loginButtonProperties.setReadPermissions(permissions, getSession());
    }

    public void setPublishPermissions(List<String> permissions) {
        this.loginButtonProperties.setPublishPermissions(permissions, getSession());
    }

    public void clearPermissions() {
        this.loginButtonProperties.clearPermissions();
    }

    public void setLoginBehavior(SessionLoginBehavior loginBehavior) {
        this.loginButtonProperties.setLoginBehavior(loginBehavior);
    }

    public SessionLoginBehavior getLoginBehavior() {
        return this.loginButtonProperties.getLoginBehavior();
    }

    public void setOnErrorListener(LoginButton.OnErrorListener onErrorListener) {
        this.loginButtonProperties.setOnErrorListener(onErrorListener);
    }

    public LoginButton.OnErrorListener getOnErrorListener() {
        return this.loginButtonProperties.getOnErrorListener();
    }

    public void setSessionStatusCallback(Session.StatusCallback callback) {
        this.sessionStatusCallback = callback;
    }

    public Session.StatusCallback getSessionStatusCallback() {
        return this.sessionStatusCallback;
    }

    /* access modifiers changed from: protected */
    public void onSessionStateChange(SessionState state, Exception exception) {
        fetchUserInfo();
        updateUI();
        if (this.sessionStatusCallback != null) {
            this.sessionStatusCallback.call(getSession(), state, exception);
        }
    }

    /* access modifiers changed from: package-private */
    public List<String> getPermissions() {
        return this.loginButtonProperties.getPermissions();
    }

    private void fetchUserInfo() {
        final Session currentSession = getSession();
        if (currentSession == null || !currentSession.isOpened()) {
            this.user = null;
        } else if (currentSession != this.userInfoSession) {
            Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
                public void onCompleted(GraphUser me, Response response) {
                    if (currentSession == UserSettingsFragment.this.getSession()) {
                        UserSettingsFragment.this.user = me;
                        UserSettingsFragment.this.updateUI();
                    }
                    if (response.getError() != null) {
                        UserSettingsFragment.this.loginButton.handleError(response.getError().getException());
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString(FIELDS, REQUEST_FIELDS);
            request.setParameters(parameters);
            Request.executeBatchAsync(request);
            this.userInfoSession = currentSession;
        }
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        if (isAdded()) {
            if (isSessionOpen()) {
                this.connectedStateLabel.setTextColor(getResources().getColor(R.color.com_facebook_usersettingsfragment_connected_text_color));
                this.connectedStateLabel.setShadowLayer(1.0f, 0.0f, -1.0f, getResources().getColor(R.color.com_facebook_usersettingsfragment_connected_shadow_color));
                if (this.user != null) {
                    ImageRequest request = getImageRequest();
                    if (request != null) {
                        URL requestUrl = request.getImageUrl();
                        if (!requestUrl.equals(this.connectedStateLabel.getTag())) {
                            if (this.user.getId().equals(this.userProfilePicID)) {
                                this.connectedStateLabel.setCompoundDrawables((Drawable) null, this.userProfilePic, (Drawable) null, (Drawable) null);
                                this.connectedStateLabel.setTag(requestUrl);
                            } else {
                                ImageDownloader.downloadAsync(request);
                            }
                        }
                    }
                    this.connectedStateLabel.setText(this.user.getName());
                    return;
                }
                this.connectedStateLabel.setText(getResources().getString(R.string.com_facebook_usersettingsfragment_logged_in));
                Drawable noProfilePic = getResources().getDrawable(R.drawable.com_facebook_profile_default_icon);
                noProfilePic.setBounds(0, 0, getResources().getDimensionPixelSize(R.dimen.com_facebook_usersettingsfragment_profile_picture_width), getResources().getDimensionPixelSize(R.dimen.com_facebook_usersettingsfragment_profile_picture_height));
                this.connectedStateLabel.setCompoundDrawables((Drawable) null, noProfilePic, (Drawable) null, (Drawable) null);
                return;
            }
            int textColor = getResources().getColor(R.color.com_facebook_usersettingsfragment_not_connected_text_color);
            this.connectedStateLabel.setTextColor(textColor);
            this.connectedStateLabel.setShadowLayer(0.0f, 0.0f, 0.0f, textColor);
            this.connectedStateLabel.setText(getResources().getString(R.string.com_facebook_usersettingsfragment_not_logged_in));
            this.connectedStateLabel.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            this.connectedStateLabel.setTag((Object) null);
        }
    }

    private ImageRequest getImageRequest() {
        try {
            return new ImageRequest.Builder(getActivity(), ImageRequest.getProfilePictureUrl(this.user.getId(), getResources().getDimensionPixelSize(R.dimen.com_facebook_usersettingsfragment_profile_picture_width), getResources().getDimensionPixelSize(R.dimen.com_facebook_usersettingsfragment_profile_picture_height))).setCallerTag(this).setCallback(new ImageRequest.Callback() {
                public void onCompleted(ImageResponse response) {
                    UserSettingsFragment.this.processImageResponse(UserSettingsFragment.this.user.getId(), response);
                }
            }).build();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void processImageResponse(String id, ImageResponse response) {
        Bitmap bitmap;
        if (response != null && (bitmap = response.getBitmap()) != null) {
            BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
            drawable.setBounds(0, 0, getResources().getDimensionPixelSize(R.dimen.com_facebook_usersettingsfragment_profile_picture_width), getResources().getDimensionPixelSize(R.dimen.com_facebook_usersettingsfragment_profile_picture_height));
            this.userProfilePic = drawable;
            this.userProfilePicID = id;
            this.connectedStateLabel.setCompoundDrawables((Drawable) null, drawable, (Drawable) null, (Drawable) null);
            this.connectedStateLabel.setTag(response.getRequest().getImageUrl());
        }
    }
}
