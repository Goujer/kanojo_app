package jp.co.cybird.barcodekanojoForGAM.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseKanojosActivity;

public class CustomWebViewActivity extends BaseKanojosActivity implements View.OnClickListener {
    private String extraWebViewURL;
    private RelativeLayout mProgressBar;
    private WebView mWebview;

    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_custom_webview);
        this.extraWebViewURL = getIntent().getStringExtra(BaseInterface.EXTRA_WEBVIEW_URL);
        this.mWebview = (WebView) findViewById(R.id.customwebview);
        this.mWebview.setWebViewClient(new MyWebViewClient(this, (MyWebViewClient) null));
        this.mWebview.getSettings().setJavaScriptEnabled(true);
        ((Button) findViewById(R.id.webview_close)).setOnClickListener(this);
        this.mProgressBar = (RelativeLayout) findViewById(R.id.progressbar);
        this.mProgressBar.setOnClickListener(this);
        showProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "CustomWebViewActivity onResume " + this.extraWebViewURL);
        if (this.extraWebViewURL != null) {
            this.mWebview.loadUrl(this.extraWebViewURL);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

	}

	@Override
    public void onClick(View v) {
		if (v.getId() == R.id.webview_close) {
			finish();
		}
	}

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        /* synthetic */ MyWebViewClient(CustomWebViewActivity customWebViewActivity, MyWebViewClient myWebViewClient) {
            this();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            CustomWebViewActivity.this.showProgressDialog();
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CustomWebViewActivity.this.dismissProgressDialog();
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i(TAG, "CustomWebViewActivity error code:" + errorCode);
            CustomWebViewActivity.this.showNoticeDialog(CustomWebViewActivity.this.getResources().getString(R.string.error_network));
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.mWebview.canGoBack()) {
                this.mWebview.goBack();
            } else if (keyCode == 4) {
                finish();
                overridePendingTransition(0, 0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public ProgressDialog showProgressDialog() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.VISIBLE);
        }
        this.mProgressBar.setTag("show");
        return this.mProgressDialog;
    }

    protected void dismissProgressDialog() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.INVISIBLE);
        }
        this.mProgressBar.setTag("hide");
    }
}
