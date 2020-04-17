package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;

public class PrivacyInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "PrivacyInfoActivity";
    private Button btnNO;
    private Button btnOK;

    public void onCreate(Bundle savedInstanceState) {
        this.mBaseLoadingFinished = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_info);
        setAutoRefreshSession(false);
        this.btnOK = (Button) findViewById(R.id.kanojo_privacy_ok);
        this.btnOK.setOnClickListener(this);
        this.btnNO = (Button) findViewById(R.id.kanojo_privacy_no);
        this.btnNO.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.btnOK.setOnClickListener((View.OnClickListener) null);
        this.btnNO.setOnClickListener((View.OnClickListener) null);
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kanojo_privacy_ok:
                startSignUp();
                return;
            case R.id.kanojo_privacy_no:
                close();
                return;
            default:
                return;
        }
    }

    private void startSignUp() {
        setResult(BaseInterface.RESULT_PRIVACY_OK, (Intent) null);
        close();
    }
}
