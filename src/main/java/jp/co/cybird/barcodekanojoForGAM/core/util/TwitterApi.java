package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterApi {
    private static final String PREF_MEDIA_PROViDER = "pictureService";
    private static final String TAG = "TwitterApi";
    private String CALLBACK_URL = "tweet-android-barcode:///";
    private String CONSUMER_KEY = "7lULtdlUSws8xJoha8HXA";
    private String CONSUMER_SECRET = "WxvPM5sx0TaiI7DmnzcnJvOuFNl8XMBeo37Hq57QyE";
    private boolean _authetication = false;
    private Context _ctx;
    private int _layout;
    private OnTwitterListener mListener;
    private RequestToken mReqToken;
    private Twitter mTwitter;
    private ApplicationSetting setting;

    public interface OnTwitterListener {
        void onTwitterFail();

        void onTwitterSuccess();
    }

    public TwitterApi(Context ctx, int layout) {
        this._ctx = ctx;
        this._authetication = false;
        this._layout = layout;
        this.setting = new ApplicationSetting(this._ctx);
        Log.i(TAG, "Got Preferences");
        this.mTwitter = new TwitterFactory().getInstance();
        Log.i(TAG, "Got Twitter4j");
        this.mTwitter.setOAuthConsumer(this.CONSUMER_KEY, this.CONSUMER_SECRET);
        Log.i(TAG, "Inflated Twitter4j");
    }

    public void setListener(OnTwitterListener listener) {
        this.mListener = listener;
    }

    public void login() {
        if (this.setting.getTwitterAccessToken() != null) {
            Log.i(TAG, "Repeat User");
            loginAuthorisedUser();
            return;
        }
        Log.i(TAG, "New User");
        loginNewUser();
    }

    public void loginNewUser() {
        try {
            Log.i(TAG, "Request App Authentication");
            this.mReqToken = this.mTwitter.getOAuthRequestToken(this.CALLBACK_URL);
            Log.i(TAG, "Starting Webview to login to twitter");
            WebView twitterSite = new WebView(this._ctx);
            twitterSite.loadUrl(this.mReqToken.getAuthenticationURL());
            ((Activity) this._ctx).setContentView(twitterSite);
        } catch (TwitterException e) {
            Log.d("NguyenTT", e.getMessage());
            Toast.makeText(this._ctx, "Twitter Login error, try again later", 0).show();
        }
    }

    public void loginAuthorisedUser() {
        this.mTwitter.setOAuthAccessToken(new AccessToken(this.setting.getTwitterAccessToken(), this.setting.getTwitterAccessTokenSecret()));
        Toast.makeText(this._ctx, "Welcome back", 0).show();
        this._authetication = true;
    }

    public void dealWithTwitterResponse(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith(this.CALLBACK_URL)) {
            authoriseNewUser(uri.getQueryParameter("oauth_verifier"));
        }
    }

    private void authoriseNewUser(String oauthVerifier) {
        try {
            AccessToken at = this.mTwitter.getOAuthAccessToken(this.mReqToken, oauthVerifier);
            this.mTwitter.setOAuthAccessToken(at);
            saveAccessToken(at);
            ((Activity) this._ctx).setContentView(this._layout);
            if (this.mListener != null) {
                this.mListener.onTwitterSuccess();
            }
        } catch (TwitterException e) {
            Toast.makeText(this._ctx, "Twitter auth error x01, try again later", 0).show();
            if (this.mListener != null) {
                this.mListener.onTwitterFail();
            }
        }
    }

    private void saveAccessToken(AccessToken at) {
        String token = at.getToken();
        String secret = at.getTokenSecret();
        this.setting.commitTwitterAccessToken(token);
        this.setting.commitTwitterAccessTokenSecret(secret);
    }

    public boolean canPostTwitter() {
        return this._authetication;
    }

    public boolean tweetMessage(String txtMessage) {
        if (!this._authetication) {
            Log.d(TAG, "Authencation failed");
            return false;
        }
        try {
            this.mTwitter.updateStatus(txtMessage);
            return true;
        } catch (TwitterException e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
    }

    public void UpdateStatus(StatusUpdate s) {
        try {
            this.mTwitter.updateStatus(s);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
