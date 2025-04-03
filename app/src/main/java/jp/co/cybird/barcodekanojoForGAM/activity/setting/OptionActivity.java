package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.PreferencesActivity;
import com.goujer.barcodekanojo.activity.setting.CreditsActivity;
import com.goujer.barcodekanojo.activity.setting.UserModifyActivity;
import com.goujer.barcodekanojo.activity.top.LaunchActivity;
import com.goujer.barcodekanojo.core.http.HttpApi;

import java.io.File;

import com.goujer.barcodekanojo.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;

import com.goujer.barcodekanojo.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;

public class OptionActivity extends BaseActivity implements View.OnClickListener {
    private EditItemView account_btn;
	private EditItemView prefs_btn;
    private EditItemView bck_btn;
    private LinearLayout mDashboard;
    private LinearLayout mKanojos;
    private BaseActivity.OnDialogDismissListener mListener;
    //private OptionChangeDeviceTask mOptionChangeDeviceTask;
    //private OptionDeleteTask mOptionDeleteTask;
    //private OptionModifyTask mOptionModifyTask;
    private LinearLayout mScan;
    private LinearLayout mSetting;
    private LinearLayout mWebView;
    private EditItemView bluesky_btn;
	private EditItemView kofi_btn;
    private File modifiedPhoto;
    private User modifiedUser;
    private EditItemView privacy_btn;
    private EditItemView rules_btn;
    private EditItemView team_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_option);

        this.account_btn = findViewById(R.id.kanojo_option_account_modify);
		this.prefs_btn = findViewById(R.id.kanojo_option_app_preferences);
        this.privacy_btn = findViewById(R.id.kanojo_option_privacy);
        this.rules_btn = findViewById(R.id.kanojo_option_rules);
        this.bck_btn = findViewById(R.id.kanojo_option_barcodekanojo);
        this.team_btn = findViewById(R.id.kanojo_option_team);
        this.bluesky_btn = findViewById(R.id.kanojo_option_support_bluesky);
	    this.kofi_btn = findViewById(R.id.kanojo_option_support_kofi);

        this.mListener = (dialog, code) -> {
			OptionActivity.this.logout();
			OptionActivity.this.startActivity(new Intent().setClass(OptionActivity.this, LaunchActivity.class));
		};
    }

	@Override
	protected void onResume() {
		super.onResume();
		bindEvent();
	}

    protected void onDestroy() {
		unBindEvent();
        super.onDestroy();
    }

    public void unBindEvent() {
        this.account_btn.setOnClickListener(null);
		this.prefs_btn.setOnClickListener(null);
        this.privacy_btn.setOnClickListener(null);
        this.rules_btn.setOnClickListener(null);
        this.bck_btn.setOnClickListener(null);
        this.team_btn.setOnClickListener(null);
        this.bluesky_btn.setOnClickListener(null);
	    this.kofi_btn.setOnClickListener(null);
    }

    public void bindEvent() {
        this.account_btn.setOnClickListener(this);
	    this.prefs_btn.setOnClickListener(this);
        this.privacy_btn.setOnClickListener(this);
        this.rules_btn.setOnClickListener(this);
        this.bck_btn.setOnClickListener(this);
        this.team_btn.setOnClickListener(this);
        this.bluesky_btn.setOnClickListener(this);
	    this.kofi_btn.setOnClickListener(this);
    }

    public void onClick(View v) {
        unBindEvent();
		int id = v.getId();
		if (id == R.id.kanojo_option_account_modify) {
			startAccountModify();
		} else if (id == R.id.kanojo_option_app_preferences) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intents.FLAG_NEW_DOC);
			intent.setClassName(this, PreferencesActivity.class.getName());
			startActivity(intent);
		} else if (id == R.id.kanojo_option_privacy) {
			startOtherWebViewActivity(Defs.URL_LEGAL_PRIVACY);
		} else if (id == R.id.kanojo_option_rules) {
			startWebViewActivity(Defs.URL_ABOUT_RULES);
		} else if (id == R.id.kanojo_option_barcodekanojo) {
			startWebViewActivity(Defs.URL_ABOUT_BARCODEKANOJO);
		} else if (id == R.id.kanojo_option_team) {
			showCredits();
		} else if (id == R.id.kanojo_option_support_bluesky) {
			showBluesky();
		} else if (id == R.id.kanojo_option_support_kofi) {
			showKofi();
		}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bindEvent();
    }

    protected void fixUser() {
        checkAndCopyUser();
        showNoticeDialog(getString(R.string.edit_account_update_done));
    }

    private void startAccountModify() {
        Intent intent = new Intent().setClass(this, UserModifyActivity.class);
        intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING);
        startActivityForResult(intent, BaseInterface.REQUEST_MODIFY_USER);
    }

    private void logout() {
        ((BarcodeKanojoApp) getApplication()).logged_out();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

	private void showBluesky() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Defs.URL_GOUJER_BLUESKY));
		startActivity(browserIntent);
	}

	private void showKofi() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Defs.URL_GOUJER_KOFI));
		startActivity(browserIntent);
	}

	private void showCredits() {
		Intent intent = new Intent(this, CreditsActivity.class);
		startActivity(intent);
	}

	private void startWebViewActivity(String url) {
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, HttpApi.Companion.get().getMApiBaseUrl() + url);
		startActivity(intent);
	}

	private void startOtherWebViewActivity(String url) {
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, url);
		startActivity(intent);
	}
}
