package jp.co.cybird.android.lib.gcm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class AgreementDialog implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    private static boolean isShown = false;
    private final String PREF_KEY_AGREEMENT = "lib_gcm_agreement_version";
    /* access modifiers changed from: private */
    public Context mContext;
    private Dialog mDialog;
    /* access modifiers changed from: private */
    public int mDisplayHeight;
    /* access modifiers changed from: private */
    public String mEulaUrl;
    private int mEulaVer;
    private SharedPreferences mPref;
    /* access modifiers changed from: private */
    public WebView mWebview;

    @SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
    public AgreementDialog(Context context, int eulaVersion, String eulaUrl) {
        this.mContext = context;
        this.mEulaVer = eulaVersion;
        this.mEulaUrl = eulaUrl;
        this.mPref = context.getSharedPreferences(Const.PREF_FILE_NAME, 3);
        this.mDisplayHeight = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getHeight();
        createDialog(context);
    }

    private void createDialog(Context context) {
        this.mDialog = new Dialog(context) {
            private ProgressBar mProgress;

            /* access modifiers changed from: protected */
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setTitle(ParameterLoader.getString("LIB_GCM_DIALOG_TITLE", AgreementDialog.this.mContext));
                setContentView(ParameterLoader.getResourceIdForType("lib_gcm_agreement_dialog", "layout", AgreementDialog.this.mContext));
                ((FrameLayout) findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_webview_frame", "id", AgreementDialog.this.mContext))).setLayoutParams(new LinearLayout.LayoutParams(-1, AgreementDialog.this.mDisplayHeight / 2));
                this.mProgress = (ProgressBar) findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_progress", "id", AgreementDialog.this.mContext));
                AgreementDialog.this.mWebview = (WebView) findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_webview_agreement", "id", AgreementDialog.this.mContext));
                AgreementDialog.this.mWebview.getSettings().setCacheMode(2);
                AgreementDialog.this.mWebview.setWebViewClient(new LocalClient(this.mProgress));
                AgreementDialog.this.mWebview.loadUrl(AgreementDialog.this.mEulaUrl);
                ((Button) findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_decline_button", "id", AgreementDialog.this.mContext))).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        AgreementDialog.this.saveDecline();
                        AnonymousClass1.this.dismiss();
                    }
                });
                ((Button) findViewById(ParameterLoader.getResourceIdForType("lib_gcm_agreement_agree_button", "id", AgreementDialog.this.mContext))).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        AgreementDialog.this.saveAgreement();
                        AnonymousClass1.this.dismiss();
                    }
                });
            }
        };
        this.mDialog.setOnCancelListener(this);
        this.mDialog.setOnDismissListener(this);
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

    public void showAgreement() {
        if (this.mDialog != null && !isAgreed()) {
            this.mDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void saveDecline() {
        SharedPreferences.Editor e = this.mPref.edit();
        e.putInt("lib_gcm_agreement_version", this.mEulaVer);
        e.putBoolean("lib_gcm_willSendNotification", false);
        e.commit();
    }

    /* access modifiers changed from: private */
    public void saveAgreement() {
        SharedPreferences.Editor e = this.mPref.edit();
        e.putInt("lib_gcm_agreement_version", this.mEulaVer);
        e.putBoolean("lib_gcm_willSendNotification", true);
        e.putBoolean("lib_gcm_willPlaySound", true);
        e.putBoolean("lib_gcm_willVibrate", true);
        e.commit();
    }

    private boolean isAgreed() {
        return this.mEulaVer <= this.mPref.getInt("lib_gcm_agreement_version", 0);
    }

    public void onCancel(DialogInterface dialog) {
        isShown = false;
    }

    public void onDismiss(DialogInterface dialog) {
        isShown = false;
    }

    private class LocalClient extends WebViewClient {
        private ProgressBar mProgress;

        public LocalClient(ProgressBar progress) {
            this.mProgress = progress;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            this.mProgress.setVisibility(0);
            view.setVisibility(4);
        }

        public void onPageFinished(WebView view, String url) {
            this.mProgress.setVisibility(4);
            view.setVisibility(0);
        }
    }
}
