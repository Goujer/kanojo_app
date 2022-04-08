package jp.co.cybird.app.android.lib.commons.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class BaseAgreementDialog implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
	private static boolean isShown = false;
	private String DIALOG_TITLE;
	private String LAYOUT_NAME;
	private String PREF_FILE_NAME;
	private String PREF_KEY_AGREEMENT;
	private Context mContext;
	private Dialog mDialog;
	private int mDisplayHeight;
	private String mEulaUrl;
	private int mEulaVer;
	protected SharedPreferences mPref;
	private WebView mWebview;

	public BaseAgreementDialog(Context context, int eulaVersion, String eulaUrl, String prefKey, String prefFileName, String layoutName, String title) {
		this.mContext = context;
		this.mEulaVer = eulaVersion;
		this.mEulaUrl = eulaUrl;
		this.PREF_KEY_AGREEMENT = prefKey;
		this.PREF_FILE_NAME = prefFileName == null ? "cy_agreement_dialog" : prefFileName;
		this.LAYOUT_NAME = layoutName == null ? "lib_gcm_agreement_dialog" : layoutName;
		this.DIALOG_TITLE = title == null ? ParameterLoader.getString("LIB_GCM_DIALOG_TITLE", this.mContext) : title;
		this.mPref = context.getSharedPreferences(this.PREF_FILE_NAME, 0);
		this.mDisplayHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		createDialog(context);
	}

	private void createDialog(Context context) {
		this.mDialog = new Dialog(context) {
			private ProgressBar mProgress;

			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setTitle(BaseAgreementDialog.this.DIALOG_TITLE);
				setContentView(ParameterLoader.getResourceIdForType(BaseAgreementDialog.this.LAYOUT_NAME, "layout", BaseAgreementDialog.this.mContext));
				findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_webview_frame", "id", BaseAgreementDialog.this.mContext)).setLayoutParams(new LinearLayout.LayoutParams(-1, BaseAgreementDialog.this.mDisplayHeight / 2));
				this.mProgress = findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_progress", "id", BaseAgreementDialog.this.mContext));
				BaseAgreementDialog.this.mWebview = findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_webview_agreement", "id", BaseAgreementDialog.this.mContext));
				BaseAgreementDialog.this.mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
				BaseAgreementDialog.this.mWebview.setWebViewClient(new LocalClient(this.mProgress));
				BaseAgreementDialog.this.mWebview.loadUrl(BaseAgreementDialog.this.mEulaUrl);
				findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_decline_button", "id", BaseAgreementDialog.this.mContext)).setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						BaseAgreementDialog.this.handleDecline();
						dismiss();
					}
				});
				findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_agree_button", "id", BaseAgreementDialog.this.mContext)).setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						BaseAgreementDialog.this.saveAgreement();
						BaseAgreementDialog.this.handleAgree();
						dismiss();
					}
				});
			}
		};
		this.mDialog.setOnCancelListener(this);
		this.mDialog.setOnDismissListener(this);
	}

	private void handleCancel() {
	}

	protected void handleDecline() {
	}

	protected void handleAgree() {
	}

	public void onPause() {
		if (this.mDialog != null && this.mDialog.isShowing()) {
			this.mDialog.dismiss();
			isShown = true;
		}
	}

	public void onResume() {
		if (isShown) {
			this.mDialog.show();
		}
	}

//	public void showAgreement() {
//		if (this.mDialog != null && !isAgreed()) {
//			this.mDialog.show();
//		}
//	}

	protected void saveAgreement() {
		SharedPreferences.Editor e = this.mPref.edit();
		e.putInt(this.PREF_KEY_AGREEMENT, this.mEulaVer);
		e.commit();
	}

//	public boolean isAgreed() {
//		return this.mEulaVer <= this.mPref.getInt(this.PREF_KEY_AGREEMENT, 0);
//	}

	public void onCancel(DialogInterface dialog) {
		isShown = false;
		handleCancel();
	}

	public void onDismiss(DialogInterface dialog) {
		isShown = false;
	}

	private static class LocalClient extends WebViewClient {
		private ProgressBar mProgress;

		LocalClient(ProgressBar progress) {
			this.mProgress = progress;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			this.mProgress.setVisibility(View.VISIBLE);
			view.setVisibility(View.INVISIBLE);
		}

		public void onPageFinished(WebView view, String url) {
			this.mProgress.setVisibility(View.INVISIBLE);
			view.setVisibility(View.VISIBLE);
		}
	}
}
