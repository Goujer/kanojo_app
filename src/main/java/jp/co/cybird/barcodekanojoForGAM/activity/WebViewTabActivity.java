package jp.co.cybird.barcodekanojoForGAM.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseKanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.WebViewData;
import com.goujer.barcodekanojo.preferences.ApplicationSetting;

public class WebViewTabActivity extends BaseKanojosActivity implements View.OnClickListener {
    private String extraWebViewURL;
    private RelativeLayout mProgressBar;
    private WebView webview;

    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_webview);
        this.webview = findViewById(R.id.webview);
        this.webview.setWebViewClient(new MyWebViewClient(this, null));
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.mProgressBar = findViewById(R.id.progressbar);
        this.mProgressBar.setOnClickListener(this);
        showProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.extraWebViewURL = getIntent().getStringExtra(BaseInterface.EXTRA_WEBVIEW_URL);
        Log.d(TAG, "WebViewTabActivity onResume " + this.extraWebViewURL);
        if (this.extraWebViewURL == null) {
            new GetURLWebView().execute();
        } else {
            this.webview.loadUrl(this.extraWebViewURL);
        }
    }

    class GetURLWebView extends AsyncTask<Void, Void, Response<?>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebViewTabActivity.this.showProgressDialog();
        }

        @Override
        protected Response<?> doInBackground(Void... params) {
            try {
                getURLWebView();
            } catch (BarcodeKanojoException e) {
                e.printStackTrace();
            }
			return null;
        }

        @Override
        protected void onPostExecute(Response<?> result) {
            WebViewTabActivity.this.dismissProgressDialog();
            super.onPostExecute(result);
        }

        private void getURLWebView() throws BarcodeKanojoException {
            Response<BarcodeKanojoModel> uRLWebView = ((BarcodeKanojoApp) WebViewTabActivity.this.getApplication()).getBarcodeKanojo().getURLWebView(new ApplicationSetting(WebViewTabActivity.this).getUUID());
            if (uRLWebView == null) {
                throw new BarcodeKanojoException("Error: URL webview not found");
            }
            int code = uRLWebView.getCode();
            switch (code) {
                case 200:
                    WebViewTabActivity.this.webview.loadUrl("https://" + ((WebViewData) uRLWebView.get(WebViewData.class)).getUrl());
                    return;
                case 400:
                case 401:
                case 403:
                case 404:
                case 500:
                case 503:
                    WebViewTabActivity.this.dismissProgressDialog();
                    throw new BarcodeKanojoException("Error: Code: " + code + " WebView not initialized!");
                default:
                    return;
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        /* synthetic */ MyWebViewClient(WebViewTabActivity webViewTabActivity, MyWebViewClient myWebViewClient) {
            this();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
        }
    }

    @Override
    public void onClick(View v) {
        v.getId();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.webview.canGoBack()) {
                this.webview.goBack();
            } else {
                finish();
                overridePendingTransition(0, 0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public ProgressDialog showProgressDialog() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.VISIBLE);
        }
        this.mProgressBar.setTag("show");
        return this.mProgressDialog;
    }

    @Override
    protected void dismissProgressDialog() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.INVISIBLE);
        }
        this.mProgressBar.setTag("hide");
    }
}
