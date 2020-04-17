package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;

public class WebViewActivity extends Activity {
    public static final boolean DEBUG = false;
    public static final String INTENT_EXTRA_URL = "jp.co.cybird.barcodekanojoForGAM.WebViewActivity.INTENT_EXTRA_URL";
    private static final String TAG = "WebViewActivity";
    private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            WebViewActivity.this.finish();
        }
    };
    private WebView mWebView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(5);
        registerReceiver(this.mLoggedOutReceiver, new IntentFilter(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT));
        this.mWebView = new WebView(this);
        this.mWebView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setWebViewClient(new EmbeddedWebViewClient(this, (EmbeddedWebViewClient) null));
        if (getIntent().getStringExtra(INTENT_EXTRA_URL) != null) {
            this.mWebView.loadUrl(getIntent().getStringExtra(INTENT_EXTRA_URL));
            setContentView(this.mWebView);
            return;
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mLoggedOutReceiver);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mWebView.canGoBack()) {
            return super.onKeyDown(keyCode, event);
        }
        this.mWebView.goBack();
        return true;
    }

    private class EmbeddedWebViewClient extends WebViewClient {
        private EmbeddedWebViewClient() {
        }

        /* synthetic */ EmbeddedWebViewClient(WebViewActivity webViewActivity, EmbeddedWebViewClient embeddedWebViewClient) {
            this();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WebViewActivity.this.setProgressBarIndeterminateVisibility(true);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebViewActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }
}
