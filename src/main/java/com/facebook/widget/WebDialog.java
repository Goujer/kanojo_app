package com.facebook.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.facebook.FacebookDialogException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookServiceException;
import com.facebook.Session;
import com.facebook.android.R;
import com.facebook.android.Util;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.google.android.gcm.GCMConstants;

public class WebDialog extends Dialog {
    static final String CANCEL_URI = "fbconnect://cancel";
    public static final int DEFAULT_THEME = 16973840;
    private static final String DISPLAY_TOUCH = "touch";
    private static final String LOG_TAG = "FacebookSDK.WebDialog";
    static final String REDIRECT_URI = "fbconnect://success";
    private static final String USER_AGENT = "user_agent";
    /* access modifiers changed from: private */
    public FrameLayout contentFrameLayout;
    /* access modifiers changed from: private */
    public ImageView crossImageView;
    /* access modifiers changed from: private */
    public boolean isDetached;
    private boolean listenerCalled;
    private OnCompleteListener onCompleteListener;
    /* access modifiers changed from: private */
    public ProgressDialog spinner;
    private String url;
    /* access modifiers changed from: private */
    public WebView webView;

    public interface OnCompleteListener {
        void onComplete(Bundle bundle, FacebookException facebookException);
    }

    public WebDialog(Context context, String url2) {
        this(context, url2, 16973840);
    }

    public WebDialog(Context context, String url2, int theme) {
        super(context, theme);
        this.listenerCalled = false;
        this.isDetached = false;
        this.url = url2;
    }

    public WebDialog(Context context, String action, Bundle parameters, int theme, OnCompleteListener listener) {
        super(context, theme);
        this.listenerCalled = false;
        this.isDetached = false;
        parameters = parameters == null ? new Bundle() : parameters;
        parameters.putString(ServerProtocol.DIALOG_PARAM_DISPLAY, DISPLAY_TOUCH);
        parameters.putString("type", USER_AGENT);
        this.url = Utility.buildUri(ServerProtocol.DIALOG_AUTHORITY, ServerProtocol.DIALOG_PATH + action, parameters).toString();
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
            if (this.spinner.isShowing()) {
                this.spinner.dismiss();
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
                WebDialog.this.sendCancelToListener();
            }
        });
        this.spinner = new ProgressDialog(getContext());
        this.spinner.requestWindowFeature(1);
        this.spinner.setMessage(getContext().getString(R.string.com_facebook_loading));
        this.spinner.setCanceledOnTouchOutside(false);
        this.spinner.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                WebDialog.this.sendCancelToListener();
                WebDialog.this.dismiss();
            }
        });
        requestWindowFeature(1);
        this.contentFrameLayout = new FrameLayout(getContext());
        createCrossImage();
        setUpWebView(this.crossImageView.getDrawable().getIntrinsicWidth() / 2);
        this.contentFrameLayout.addView(this.crossImageView, new ViewGroup.LayoutParams(-2, -2));
        addContentView(this.contentFrameLayout, new ViewGroup.LayoutParams(-1, -1));
    }

    /* access modifiers changed from: private */
    public void sendSuccessToListener(Bundle values) {
        if (this.onCompleteListener != null && !this.listenerCalled) {
            this.listenerCalled = true;
            this.onCompleteListener.onComplete(values, (FacebookException) null);
        }
    }

    /* access modifiers changed from: private */
    public void sendErrorToListener(Throwable error) {
        FacebookException facebookException;
        if (this.onCompleteListener != null && !this.listenerCalled) {
            this.listenerCalled = true;
            if (error instanceof FacebookDialogException) {
                facebookException = (FacebookDialogException) error;
            } else if (error instanceof FacebookException) {
                facebookException = (FacebookException) error;
            } else {
                facebookException = new FacebookException(error);
            }
            this.onCompleteListener.onComplete((Bundle) null, facebookException);
        }
    }

    /* access modifiers changed from: private */
    public void sendCancelToListener() {
        sendErrorToListener(new FacebookOperationCanceledException());
    }

    private void createCrossImage() {
        this.crossImageView = new ImageView(getContext());
        this.crossImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebDialog.this.sendCancelToListener();
                WebDialog.this.dismiss();
            }
        });
        this.crossImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.com_facebook_close));
        this.crossImageView.setVisibility(4);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void setUpWebView(int margin) {
        LinearLayout webViewContainer = new LinearLayout(getContext());
        this.webView = new WebView(getContext());
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.setWebViewClient(new DialogWebViewClient(this, (DialogWebViewClient) null));
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.loadUrl(this.url);
        this.webView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.webView.setVisibility(4);
        this.webView.getSettings().setSavePassword(false);
        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(this.webView);
        this.contentFrameLayout.addView(webViewContainer);
    }

    private class DialogWebViewClient extends WebViewClient {
        private DialogWebViewClient() {
        }

        /* synthetic */ DialogWebViewClient(WebDialog webDialog, DialogWebViewClient dialogWebViewClient) {
            this();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Utility.logd(WebDialog.LOG_TAG, "Redirect URL: " + url);
            if (url.startsWith("fbconnect://success")) {
                Bundle values = Util.parseUrl(url);
                String error = values.getString(GCMConstants.EXTRA_ERROR);
                if (error == null) {
                    error = values.getString("error_type");
                }
                String errorMessage = values.getString("error_msg");
                if (errorMessage == null) {
                    errorMessage = values.getString("error_description");
                }
                String errorCodeString = values.getString("error_code");
                int errorCode = -1;
                if (!Utility.isNullOrEmpty(errorCodeString)) {
                    try {
                        errorCode = Integer.parseInt(errorCodeString);
                    } catch (NumberFormatException e) {
                        errorCode = -1;
                    }
                }
                if (Utility.isNullOrEmpty(error) && Utility.isNullOrEmpty(errorMessage) && errorCode == -1) {
                    WebDialog.this.sendSuccessToListener(values);
                } else if (error == null || (!error.equals("access_denied") && !error.equals("OAuthAccessDeniedException"))) {
                    WebDialog.this.sendErrorToListener(new FacebookServiceException(new FacebookRequestError(errorCode, error, errorMessage), errorMessage));
                } else {
                    WebDialog.this.sendCancelToListener();
                }
                WebDialog.this.dismiss();
                return true;
            } else if (url.startsWith("fbconnect://cancel")) {
                WebDialog.this.sendCancelToListener();
                WebDialog.this.dismiss();
                return true;
            } else if (url.contains(WebDialog.DISPLAY_TOUCH)) {
                return false;
            } else {
                WebDialog.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                return true;
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Utility.logd(WebDialog.LOG_TAG, "Webview onReceivedError: " + errorCode);
            super.onReceivedError(view, errorCode, description, failingUrl);
            WebDialog.this.webView.stopLoading();
            if (!WebDialog.this.isDetached) {
                WebDialog.this.spinner.dismiss();
            }
            WebDialog.this.sendErrorToListener(new FacebookDialogException(description, errorCode, failingUrl));
            WebDialog.this.dismiss();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Utility.logd(WebDialog.LOG_TAG, "Webview onReceivedError: " + error.getPrimaryError());
            super.onReceivedSslError(view, handler, error);
            WebDialog.this.sendErrorToListener(new FacebookDialogException((String) null, -11, (String) null));
            handler.cancel();
            WebDialog.this.dismiss();
            if (!WebDialog.this.isDetached) {
                WebDialog.this.spinner.dismiss();
            }
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utility.logd(WebDialog.LOG_TAG, "Webview loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            if (!WebDialog.this.isDetached) {
                WebDialog.this.spinner.show();
            }
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!WebDialog.this.isDetached) {
                WebDialog.this.spinner.dismiss();
            }
            WebDialog.this.contentFrameLayout.setBackgroundColor(0);
            WebDialog.this.webView.setVisibility(0);
            WebDialog.this.crossImageView.setVisibility(0);
        }
    }

    private static class BuilderBase<CONCRETE extends BuilderBase<?>> {
        public static final String ACCESS_TOKEN = "access_token";
        private static final String APP_ID_PARAM = "app_id";
        private String action;
        private String applicationId;
        private Context context;
        private OnCompleteListener listener;
        private Bundle parameters;
        private Session session;
        private int theme = 16973840;

        protected BuilderBase(Context context2, Session session2, String action2, Bundle parameters2) {
            Validate.notNull(session2, "session");
            if (!session2.isOpened()) {
                throw new FacebookException("Attempted to use a Session that was not open.");
            }
            this.session = session2;
            finishInit(context2, action2, parameters2);
        }

        protected BuilderBase(Context context2, String applicationId2, String action2, Bundle parameters2) {
            Validate.notNullOrEmpty(applicationId2, "applicationId");
            this.applicationId = applicationId2;
            finishInit(context2, action2, parameters2);
        }

        public CONCRETE setTheme(int theme2) {
            this.theme = theme2;
            return this;
        }

        public CONCRETE setOnCompleteListener(OnCompleteListener listener2) {
            this.listener = listener2;
            return this;
        }

        public WebDialog build() {
            if (this.session == null || !this.session.isOpened()) {
                this.parameters.putString("app_id", this.applicationId);
            } else {
                this.parameters.putString("app_id", this.session.getApplicationId());
                this.parameters.putString("access_token", this.session.getAccessToken());
            }
            if (!this.parameters.containsKey(ServerProtocol.DIALOG_PARAM_REDIRECT_URI)) {
                this.parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, "fbconnect://success");
            }
            return new WebDialog(this.context, this.action, this.parameters, this.theme, this.listener);
        }

        /* access modifiers changed from: protected */
        public String getApplicationId() {
            return this.applicationId;
        }

        /* access modifiers changed from: protected */
        public Context getContext() {
            return this.context;
        }

        /* access modifiers changed from: protected */
        public int getTheme() {
            return this.theme;
        }

        /* access modifiers changed from: protected */
        public Bundle getParameters() {
            return this.parameters;
        }

        /* access modifiers changed from: protected */
        public OnCompleteListener getListener() {
            return this.listener;
        }

        private void finishInit(Context context2, String action2, Bundle parameters2) {
            this.context = context2;
            this.action = action2;
            if (parameters2 != null) {
                this.parameters = parameters2;
            } else {
                this.parameters = new Bundle();
            }
        }
    }

    public static class Builder extends BuilderBase<Builder> {
        public /* bridge */ /* synthetic */ WebDialog build() {
            return super.build();
        }

        public /* bridge */ /* synthetic */ BuilderBase setOnCompleteListener(OnCompleteListener onCompleteListener) {
            return super.setOnCompleteListener(onCompleteListener);
        }

        public /* bridge */ /* synthetic */ BuilderBase setTheme(int i) {
            return super.setTheme(i);
        }

        public Builder(Context context, Session session, String action, Bundle parameters) {
            super(context, session, action, parameters);
        }

        public Builder(Context context, String applicationId, String action, Bundle parameters) {
            super(context, applicationId, action, parameters);
        }
    }

    public static class FeedDialogBuilder extends BuilderBase<FeedDialogBuilder> {
        private static final String CAPTION_PARAM = "caption";
        private static final String DESCRIPTION_PARAM = "description";
        private static final String FEED_DIALOG = "feed";
        private static final String FROM_PARAM = "from";
        private static final String LINK_PARAM = "link";
        private static final String NAME_PARAM = "name";
        private static final String PICTURE_PARAM = "picture";
        private static final String SOURCE_PARAM = "source";
        private static final String TO_PARAM = "to";

        public /* bridge */ /* synthetic */ WebDialog build() {
            return super.build();
        }

        public /* bridge */ /* synthetic */ BuilderBase setOnCompleteListener(OnCompleteListener onCompleteListener) {
            return super.setOnCompleteListener(onCompleteListener);
        }

        public /* bridge */ /* synthetic */ BuilderBase setTheme(int i) {
            return super.setTheme(i);
        }

        public FeedDialogBuilder(Context context, Session session) {
            super(context, session, FEED_DIALOG, (Bundle) null);
        }

        public FeedDialogBuilder(Context context, Session session, Bundle parameters) {
            super(context, session, FEED_DIALOG, parameters);
        }

        public FeedDialogBuilder setFrom(String id) {
            getParameters().putString(FROM_PARAM, id);
            return this;
        }

        public FeedDialogBuilder setTo(String id) {
            getParameters().putString(TO_PARAM, id);
            return this;
        }

        public FeedDialogBuilder setLink(String link) {
            getParameters().putString(LINK_PARAM, link);
            return this;
        }

        public FeedDialogBuilder setPicture(String picture) {
            getParameters().putString(PICTURE_PARAM, picture);
            return this;
        }

        public FeedDialogBuilder setSource(String source) {
            getParameters().putString(SOURCE_PARAM, source);
            return this;
        }

        public FeedDialogBuilder setName(String name) {
            getParameters().putString(NAME_PARAM, name);
            return this;
        }

        public FeedDialogBuilder setCaption(String caption) {
            getParameters().putString(CAPTION_PARAM, caption);
            return this;
        }

        public FeedDialogBuilder setDescription(String description) {
            getParameters().putString(DESCRIPTION_PARAM, description);
            return this;
        }
    }

    public static class RequestsDialogBuilder extends BuilderBase<RequestsDialogBuilder> {
        private static final String APPREQUESTS_DIALOG = "apprequests";
        private static final String DATA_PARAM = "data";
        private static final String MESSAGE_PARAM = "message";
        private static final String TITLE_PARAM = "title";
        private static final String TO_PARAM = "to";

        public /* bridge */ /* synthetic */ WebDialog build() {
            return super.build();
        }

        public /* bridge */ /* synthetic */ BuilderBase setOnCompleteListener(OnCompleteListener onCompleteListener) {
            return super.setOnCompleteListener(onCompleteListener);
        }

        public /* bridge */ /* synthetic */ BuilderBase setTheme(int i) {
            return super.setTheme(i);
        }

        public RequestsDialogBuilder(Context context, Session session) {
            super(context, session, APPREQUESTS_DIALOG, (Bundle) null);
        }

        public RequestsDialogBuilder(Context context, Session session, Bundle parameters) {
            super(context, session, APPREQUESTS_DIALOG, parameters);
        }

        public RequestsDialogBuilder setMessage(String message) {
            getParameters().putString(MESSAGE_PARAM, message);
            return this;
        }

        public RequestsDialogBuilder setTo(String id) {
            getParameters().putString(TO_PARAM, id);
            return this;
        }

        public RequestsDialogBuilder setData(String data) {
            getParameters().putString("data", data);
            return this;
        }

        public RequestsDialogBuilder setTitle(String title) {
            getParameters().putString(TITLE_PARAM, title);
            return this;
        }
    }
}
