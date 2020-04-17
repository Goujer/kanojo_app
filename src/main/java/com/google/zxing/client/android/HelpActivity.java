package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import jp.co.cybird.barcodekanojoForGAM.R;

public final class HelpActivity extends Activity {
    private static final String BASE_URL = ("file:///android_asset/html-" + LocaleManager.getTranslatedAssetLanguage() + '/');
    public static final String DEFAULT_PAGE = "index.html";
    public static final String REQUESTED_PAGE_KEY = "requested_page_key";
    private static final String WEBVIEW_STATE_PRESENT = "webview_state_present";
    public static final String WHATS_NEW_PAGE = "whatsnew.html";
    /* access modifiers changed from: private */
    public Button backButton;
    private final View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View view) {
            HelpActivity.this.webView.goBack();
        }
    };
    private final View.OnClickListener doneListener = new View.OnClickListener() {
        public void onClick(View view) {
            HelpActivity.this.finish();
        }
    };
    /* access modifiers changed from: private */
    public WebView webView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.help);
        this.webView = (WebView) findViewById(R.id.help_contents);
        this.webView.setWebViewClient(new HelpClient(this, (HelpClient) null));
        Intent intent = getIntent();
        if (icicle != null && icicle.getBoolean(WEBVIEW_STATE_PRESENT, false)) {
            this.webView.restoreState(icicle);
        } else if (intent != null) {
            String page = intent.getStringExtra(REQUESTED_PAGE_KEY);
            if (page == null || page.length() <= 0) {
                this.webView.loadUrl(String.valueOf(BASE_URL) + DEFAULT_PAGE);
            } else {
                this.webView.loadUrl(String.valueOf(BASE_URL) + page);
            }
        } else {
            this.webView.loadUrl(String.valueOf(BASE_URL) + DEFAULT_PAGE);
        }
        this.backButton = (Button) findViewById(R.id.back_button);
        this.backButton.setOnClickListener(this.backListener);
        findViewById(R.id.done_button).setOnClickListener(this.doneListener);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle state) {
        String url = this.webView.getUrl();
        if (url != null && url.length() > 0) {
            this.webView.saveState(state);
            state.putBoolean(WEBVIEW_STATE_PRESENT, true);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.webView.canGoBack()) {
            return super.onKeyDown(keyCode, event);
        }
        this.webView.goBack();
        return true;
    }

    private final class HelpClient extends WebViewClient {
        private HelpClient() {
        }

        /* synthetic */ HelpClient(HelpActivity helpActivity, HelpClient helpClient) {
            this();
        }

        public void onPageFinished(WebView view, String url) {
            HelpActivity.this.setTitle(view.getTitle());
            HelpActivity.this.backButton.setEnabled(view.canGoBack());
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("file")) {
                return false;
            }
            HelpActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            return true;
        }
    }
}
