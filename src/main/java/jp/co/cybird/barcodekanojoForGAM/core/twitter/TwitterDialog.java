package jp.co.cybird.barcodekanojoForGAM.core.twitter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.R;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterDialog extends Dialog {
    private static final String CALLBACK_URL = "http://www.barcodekanojo.com/dashboard/setting/twitter/callback";
    public static final int DEFAULT_THEME = 16973840;
    private static final String DISPLAY_TOUCH = "touch";
    private static final boolean DUMMY_CALLBACK_URL = false;
    private static final String LOG_TAG = "FacebookSDK.WebDialog";
    private static final String TAG = "WebDialog";
    private static final String USER_AGENT = "user_agent";
    /* access modifiers changed from: private */
    public FrameLayout contentFrameLayout;
    /* access modifiers changed from: private */
    public ImageView crossImageView;
    /* access modifiers changed from: private */
    public boolean isDetached;
    private boolean listenerCalled;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private OnCompleteListener onCompleteListener;
    private String url;
    /* access modifiers changed from: private */
    public WebView webView;

    public interface Listener {
        void onFailure(TwitterDialog twitterDialog, Result result);

        void onSuccess(TwitterDialog twitterDialog, AccessToken accessToken);
    }

    public interface OnCompleteListener {
        void onComplete(Bundle bundle, FacebookException facebookException);
    }

    public enum Result {
        SUCCESS,
        CANCELLATION,
        REQUEST_TOKEN_ERROR,
        AUTHORIZATION_ERROR,
        ACCESS_TOKEN_ERROR
    }

    public TwitterDialog(Context context, String url2) {
        this(context, url2, 16973840);
    }

    public TwitterDialog(Context context, String url2, int theme) {
        super(context, theme);
        this.listenerCalled = false;
        this.isDetached = false;
        this.mContext = context;
        this.url = url2;
    }

    public TwitterDialog(Context context, String action, Bundle parameters, int theme, OnCompleteListener listener) {
        super(context, theme);
        this.listenerCalled = false;
        this.isDetached = false;
        this.mContext = context;
        parameters = parameters == null ? new Bundle() : parameters;
        parameters.putString(ServerProtocol.DIALOG_PARAM_DISPLAY, DISPLAY_TOUCH);
        parameters.putString("type", USER_AGENT);
        this.url = Utility.buildUri("m.facebook.com", ServerProtocol.DIALOG_PATH + action, parameters).toString();
        this.onCompleteListener = listener;
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.onCompleteListener = listener;
    }

    public OnCompleteListener getOnCompleteListener() {
        return this.onCompleteListener;
    }

    public void dismiss() {
        if (this.webView != null) {
            this.webView.stopLoading();
        }
        if (!this.isDetached) {
            if (this.mProgressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
            }
            super.dismiss();
        }
    }

    public void onDetachedFromWindow() {
        this.isDetached = true;
        super.onDetachedFromWindow();
    }

    public void onAttachedToWindow() {
        this.isDetached = false;
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                TwitterDialog.this.sendCancelToListener();
            }
        });
        this.mProgressDialog = new ProgressDialog(getContext());
        this.mProgressDialog.requestWindowFeature(1);
        this.mProgressDialog.setMessage(getContext().getString(R.string.com_facebook_loading));
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                TwitterDialog.this.sendCancelToListener();
                TwitterDialog.this.dismiss();
            }
        });
        requestWindowFeature(1);
        this.contentFrameLayout = new FrameLayout(getContext());
        createCrossImage();
        setUpWebView(this.crossImageView.getDrawable().getIntrinsicWidth() / 2);
        this.contentFrameLayout.addView(this.crossImageView, new ViewGroup.LayoutParams(-2, -2));
        addContentView(this.contentFrameLayout, new ViewGroup.LayoutParams(-1, -1));
    }

    private void sendSuccessToListener(Bundle values) {
        if (this.onCompleteListener != null && !this.listenerCalled) {
            this.listenerCalled = true;
            this.onCompleteListener.onComplete(values, (FacebookException) null);
        }
    }

    private void sendErrorToListener(Throwable error) {
        FacebookException facebookException;
        if (this.onCompleteListener != null && !this.listenerCalled) {
            this.listenerCalled = true;
            if (error instanceof FacebookException) {
                facebookException = (FacebookException) error;
            } else {
                facebookException = new FacebookException(error);
            }
            this.onCompleteListener.onComplete((Bundle) null, facebookException);
        }
    }

    private void sendCancelToListener() {
        sendErrorToListener(new FacebookOperationCanceledException());
    }

    private void createCrossImage() {
        this.crossImageView = new ImageView(getContext());
        this.crossImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TwitterDialog.this.sendCancelToListener();
                TwitterDialog.this.dismiss();
            }
        });
        this.crossImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.com_facebook_close));
        this.crossImageView.setVisibility(View.INVISIBLE);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void setUpWebView(int margin) {
        LinearLayout webViewContainer = new LinearLayout(getContext());
        this.webView = new WebView(getContext());
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(this.url);
        this.webView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.webView.setVisibility(View.INVISIBLE);
        this.webView.getSettings().setSavePassword(false);
        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(this.webView);
        this.contentFrameLayout.addView(webViewContainer);
    }

    public User getuserProfile(AccessToken accessToken) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(Defs.USER_TWITTER_CONSUMER_KEY());
        configurationBuilder.setOAuthConsumerSecret(Defs.USER_TWITTER_CONSUMER_SECRET());
        Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
        try {
            twitter.setOAuthAccessToken(accessToken);
            return twitter.showUser(twitter.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void logout() {
        CookieSyncManager.createInstance(this.mContext);
        CookieManager.getInstance().removeAllCookie();
        if (this.webView != null) {
            this.webView.clearCache(true);
        }
    }

    public void start(String consumerKey, String consumerSecret, String callbackUrl, boolean dummyCallbackUrl, Listener listener) {
        if (consumerKey == null || consumerSecret == null || callbackUrl == null || listener == null) {
            throw new IllegalArgumentException();
        }
        Boolean dummy = Boolean.valueOf(dummyCallbackUrl);
        new TwitterOAuthTask(this, (TwitterOAuthTask) null).execute(new Object[]{consumerKey, consumerSecret, callbackUrl, dummy, listener});
    }

    public void start(Listener listener) {
        start(Defs.USER_TWITTER_CONSUMER_KEY(), Defs.USER_TWITTER_CONSUMER_SECRET(), CALLBACK_URL, false, listener);
    }

    /* access modifiers changed from: protected */
    public ProgressDialog showProgressDialog() {
        if (this.mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this.mContext);
            dialog.setTitle(R.string.app_name);
            dialog.setMessage(this.mContext.getResources().getString(R.string.kanojo_room_dialog_progress));
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            this.mProgressDialog = dialog;
        }
        this.mProgressDialog.show();
        return this.mProgressDialog;
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        try {
            if (this.mProgressDialog != null) {
                this.mProgressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
        }
    }

    private class TwitterOAuthTask extends AsyncTask<Object, Void, Result> {
        private AccessToken accessToken;
        private volatile boolean authorizationDone;
        /* access modifiers changed from: private */
        public String callbackUrl;
        /* access modifiers changed from: private */
        public boolean dummyCallbackUrl;
        private Listener listener;
        private RequestToken requestToken;
        private Twitter twitter;
        /* access modifiers changed from: private */
        public volatile String verifier;

        private TwitterOAuthTask() {
        }

        /* synthetic */ TwitterOAuthTask(TwitterDialog twitterDialog, TwitterOAuthTask twitterOAuthTask) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            TwitterDialog.this.webView.setWebViewClient(new LocalWebViewClient());
        }

        /* access modifiers changed from: protected */
        public Result doInBackground(Object... args) {
            String consumerKey = (String) args[0];
            String consumerSecret = (String) args[1];
            this.callbackUrl = (String) args[2];
            this.dummyCallbackUrl = (Boolean) args[3];
            this.listener = (Listener) args[4];
            Log.d(TwitterDialog.TAG, "CONSUMER KEY = " + consumerKey);
            Log.d(TwitterDialog.TAG, "CONSUMER SECRET = " + consumerSecret);
            Log.d(TwitterDialog.TAG, "CALLBACK URL = " + this.callbackUrl);
            Log.d(TwitterDialog.TAG, "DUMMY CALLBACK URL = " + this.dummyCallbackUrl);
            System.setProperty("twitter4j.debug", "true");
            this.twitter = new TwitterFactory().getInstance();
            this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
            this.requestToken = getRequestToken();
            if (this.requestToken == null) {
                return Result.REQUEST_TOKEN_ERROR;
            }
            authorize();
            waitForAuthorization();
            if (this.verifier == null) {
                return Result.AUTHORIZATION_ERROR;
            }
            this.accessToken = getAccessToken();
            if (this.accessToken == null) {
                return Result.ACCESS_TOKEN_ERROR;
            }
            return Result.SUCCESS;
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Void... values) {
            String url = this.requestToken.getAuthorizationURL();
            Log.d(TwitterDialog.TAG, "Loading the authorization URL: " + url);
            TwitterDialog.this.webView.loadUrl(url);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Result result) {
            Log.d(TwitterDialog.TAG, "onPostExecute: result = " + result);
            if (result == null) {
                result = Result.CANCELLATION;
            }
            if (result == Result.SUCCESS) {
                this.listener.onSuccess(TwitterDialog.this, this.accessToken);
                TwitterDialog.this.dismiss();
                return;
            }
            this.listener.onFailure(TwitterDialog.this, result);
            TwitterDialog.this.dismiss();
        }

        private RequestToken getRequestToken() {
            try {
                RequestToken token = this.twitter.getOAuthRequestToken(this.callbackUrl);
                Log.d(TwitterDialog.TAG, "Got a request token.");
                return token;
            } catch (TwitterException e) {
                e.printStackTrace();
                Log.e(TwitterDialog.TAG, "Failed to get a request token.", e);
                return null;
            }
        }

        private void authorize() {
            publishProgress(new Void[0]);
        }

        private void waitForAuthorization() {
            while (!this.authorizationDone) {
                synchronized (this) {
                    try {
                        Log.d(TwitterDialog.TAG, "Waiting for the authorization step to be done.");
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            Log.d(TwitterDialog.TAG, "Finished waiting for the authorization step to be done.");
        }

        private void notifyAuthorization() {
            this.authorizationDone = true;
            synchronized (this) {
                Log.d(TwitterDialog.TAG, "Notifying that the authorization step was done.");
                notify();
            }
        }

        protected class LocalWebViewClient extends WebViewClient {
            protected LocalWebViewClient() {
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TwitterDialog.TAG, "onReceivedError: [" + errorCode + "] " + description);
                TwitterOAuthTask.this.notifyAuthorization();
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!TwitterDialog.this.isDetached) {
                    TwitterDialog.this.showProgressDialog();
                }
                if (Build.VERSION.SDK_INT < 11 && shouldOverrideUrlLoading(view, url)) {
                    TwitterDialog.this.webView.stopLoading();
                }
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith(TwitterOAuthTask.this.callbackUrl)) {
                    return false;
                }
                Log.d(TwitterDialog.TAG, "Detected the callback URL: " + url);
                TwitterOAuthTask.this.verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
                Log.d(TwitterDialog.TAG, "oauth_verifier = " + TwitterOAuthTask.this.verifier);
                TwitterOAuthTask.this.notifyAuthorization();
                return TwitterOAuthTask.this.dummyCallbackUrl;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!TwitterDialog.this.isDetached) {
                    TwitterDialog.this.dismissProgressDialog();
                }
                TwitterDialog.this.contentFrameLayout.setBackgroundColor(0);
                TwitterDialog.this.webView.setVisibility(View.VISIBLE);
                TwitterDialog.this.crossImageView.setVisibility(View.VISIBLE);
            }
        }

        private AccessToken getAccessToken() {
            try {
                AccessToken token = this.twitter.getOAuthAccessToken(this.requestToken, this.verifier);
                Log.d(TwitterDialog.TAG, "Got an access token for " + token.getScreenName());
                return token;
            } catch (TwitterException e) {
                e.printStackTrace();
                Log.e(TwitterDialog.TAG, "Failed to get an access token.", e);
                return null;
            }
        }
    }
}
