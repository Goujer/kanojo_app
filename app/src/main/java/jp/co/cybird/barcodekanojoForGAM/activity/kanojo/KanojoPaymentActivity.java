package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import com.goujer.barcodekanojo.core.model.User;

public class KanojoPaymentActivity extends BaseEditActivity implements View.OnClickListener {

    private static final String TAG = "KanojoPaymentActivity";
    private Button btnClose;
    private KanojoItem mKanojoItem;
    private WebView wv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        this.btnClose = (Button) findViewById(R.id.kanojo_payment_close);
        this.btnClose.setOnClickListener(this);
        this.wv = (WebView) findViewById(R.id.kanojo_payment_view);
        this.wv.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.addJavascriptInterface(new Object() {
            public void performClick() {
                KanojoPaymentActivity.this.paymentSuccessed();
            }
        }, "ok");
        this.wv.addJavascriptInterface(new Object() {
            public void performClick() {
                KanojoPaymentActivity.this.paymentFailed();
            }
        }, "cancel");
        User user = ((BarcodeKanojoApp) getApplication()).getUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mKanojoItem = (KanojoItem) bundle.get(BaseInterface.EXTRA_KANOJO_ITEM);
        }
        this.wv.postUrl(Defs.URL_PAYMENT(), ("user_id=" + Integer.toString(user.getId()) + "&store_item_id=" + Integer.toString(this.mKanojoItem.getItem_id())).getBytes());
    }

    protected void paymentSuccessed() {
        setResult(BaseInterface.RESULT_KANOJO_ITEM_PAYMENT_DONE, (Intent) null);
        close();
    }

    protected void paymentFailed() {
        close();
    }

    protected void onDestroy() {
        this.btnClose.setOnClickListener((View.OnClickListener) null);
        this.wv.setWebViewClient((WebViewClient) null);
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kanojo_payment_close:
                close();
                return;
            default:
                return;
        }
    }
}
