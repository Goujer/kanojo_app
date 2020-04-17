package jp.co.cybird.barcodekanojoForGAM.activity.base;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Locale;
import jp.co.cybird.app.android.lib.commons.security.popgate.Codec;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.CustomWebViewActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.PhoneInfo;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;

public abstract class BaseActivity extends GreeBaseActivity implements BaseInterface, DialogInterface.OnDismissListener {
    /* access modifiers changed from: private */
    public static boolean DEBUG = false;
    protected static final String TAG = "BaseActivity";
    public static int mActivityCount;
    /* access modifiers changed from: private */
    public int code = 0;
    private boolean mAutoRefresh = true;
    public boolean mBaseLoadingFinished = false;
    final OnAccountsUpdateListener mChangeAccountListener = new OnAccountsUpdateListener() {
        public void onAccountsUpdated(Account[] accounts) {
            if (!new ApplicationSetting(BaseActivity.this.getApplicationContext()).getUserGoogleAccount().equalsIgnoreCase(new PhoneInfo(BaseActivity.this.getApplicationContext()).getGoogleAccount())) {
                if (BaseActivity.DEBUG) {
                    Log.d(BaseActivity.TAG, "Google Account has changed, will call verify api");
                }
                try {
                    BaseActivity.this.unregisterReceiver(BaseActivity.this.mChangeGmailReceiver);
                } catch (Exception e) {
                }
                BaseActivity.this.sendBroadcast(new Intent("android.accounts.LOGIN_ACCOUNTS_CHANGED"));
            }
        }
    };
    public BroadcastReceiver mChangeGmailReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (BaseActivity.DEBUG) {
                Log.d(BaseActivity.TAG, "mChangeGmailReceiver: " + intent + ", at " + this);
            }
            String saveGoogle = new ApplicationSetting(context).getUserGoogleAccount();
            String curGoogle = new PhoneInfo(context).getGoogleAccount();
            if (BaseActivity.DEBUG) {
                Log.d(BaseActivity.TAG, "saved Google Account: " + saveGoogle);
                Log.d(BaseActivity.TAG, "current Google Account: " + curGoogle);
            }
            if (!saveGoogle.equalsIgnoreCase(curGoogle)) {
                if (BaseActivity.DEBUG) {
                    Log.d(BaseActivity.TAG, "Google Account has changed, will call verify api");
                }
                BaseActivity.this.showAlertFullStorageDialog("gmail has changed....");
            }
        }
    };
    private CheckSessionTask mCheckSessionTask;
    AlertDialog mCommondialog;
    private AlertDialog mFullStorageDialog;
    private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (BaseActivity.DEBUG) {
                Log.d("NguyenTT", "Current On Recieve: " + BaseActivity.mActivityCount);
            }
            if (BaseActivity.DEBUG) {
                Log.d(BaseActivity.TAG, "onReceive: " + intent + ", at " + this);
            }
            BaseActivity.this.finish();
        }
    };
    protected ProgressDialog mProgressDialog;
    private RefreshTask mRefreshTask;
    public BroadcastReceiver mWarningFullSpaceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (BaseActivity.DEBUG) {
                Log.d(BaseActivity.TAG, "mWarningFullSpaceReceiver: " + intent + ", at " + this);
            }
            BaseActivity.this.showAlertFullStorageDialog(BaseActivity.this.getString(R.string.error_out_of_memory));
        }
    };
    private User tmpUser;

    public interface OnDialogDismissListener {
        void onDismiss(DialogInterface dialogInterface, int i);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCount++;
        String name = getLocalClassName();
        if (DEBUG) {
            Log.d("NguyenTT", "Start Activity: " + name + " Current: " + mActivityCount);
        }
        registerReceiver(this.mLoggedOutReceiver, new IntentFilter(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT));
        registerReceiver(this.mWarningFullSpaceReceiver, new IntentFilter(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mAutoRefresh) {
            if (DEBUG) {
                Log.d(TAG, "Check session for Activity " + getLocalClassName());
            }
            startCheckSession();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        dismissNoticeDialog();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (DEBUG) {
            mActivityCount--;
            Log.d("NguyenTT", "End Activity: " + getLocalClassName() + " Current: " + mActivityCount);
        }
        try {
            unregisterReceiver(this.mLoggedOutReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(this.mWarningFullSpaceReceiver);
        } catch (Exception e2) {
        }
        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(16908290);
        if (!(root == null || root.getChildCount() == 0)) {
            cleanupView(root.getChildAt(0));
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void backupUser(User user) {
        this.tmpUser = user;
    }

    /* access modifiers changed from: protected */
    public void checkAndCopyUser() {
        User user = ((BarcodeKanojoApp) getApplication()).getUser();
        if (user.getName() == null) {
            if (DEBUG) {
                Log.d("TAG", "Name copied:" + this.tmpUser.getName());
            }
            user.setName(this.tmpUser.getName());
        }
        if (user.getDescription() == null) {
            if (DEBUG) {
                Log.d("TAG", "Description copied:" + this.tmpUser.getDescription());
            }
            user.setDescription(this.tmpUser.getDescription());
        }
        if (user.getBirthText() == null) {
            if (DEBUG) {
                Log.d("TAG", "Birthday copied:" + this.tmpUser.getBirthText());
            }
            user.setBirthFromText(this.tmpUser.getBirthText());
        }
        if (user.getSex() == null) {
            if (DEBUG) {
                Log.d("TAG", "Sex copied:" + this.tmpUser.getSex());
            }
            user.setSex(this.tmpUser.getSex());
        }
        if (user.getEmail() == null) {
            if (DEBUG) {
                Log.d("TAG", "Email copied:" + this.tmpUser.getEmail());
            }
            user.setEmail(this.tmpUser.getEmail());
        }
        if (user.getPassword() == null) {
            if (DEBUG) {
                Log.d("TAG", "Password copied:" + this.tmpUser.getPassword());
            }
            user.setPassword(this.tmpUser.getPassword());
        }
    }

    /* access modifiers changed from: protected */
    public void onUserUpdated() {
    }

    /* access modifiers changed from: protected */
    public void updateUser(Response<?> response) {
        User user = (User) response.get(User.class);
        if (user != null) {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo();
            User old = barcodeKanojo.getUser();
            if (old == null) {
                if (DEBUG) {
                    Log.d(TAG, "new user id: " + user.getId());
                }
                barcodeKanojo.setUser(user);
            } else if (user.getId() == old.getId()) {
                if (DEBUG) {
                    Log.d(TAG, "update user id: " + user.getId() + " , stamina:" + user.getStamina());
                }
                backupUser(old);
                barcodeKanojo.setUser(user);
                checkAndCopyUser();
                onUserUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getCodeAndShowAlert(Response<?> response, Exception e) throws BarcodeKanojoException {
        return getCodeAndShowAlert(response, e, (OnDialogDismissListener) null);
    }

    /* access modifiers changed from: protected */
    public int getCodeAndShowAlert(Response<?> response, Exception e, final OnDialogDismissListener listener) throws BarcodeKanojoException {
        if (response == null) {
            showToast(getResources().getString(R.string.error_internet));
            if (!DEBUG) {
                return 0;
            }
            if (e != null) {
                throw new BarcodeKanojoException(e.toString());
            }
            throw new BarcodeKanojoException("response is null & reason is null");
        }
        this.code = response.getCode();
        Alert alert = response.getAlert();
        if (alert != null) {
            if (isFinishing()) {
                Log.d(TAG, "thread has finished!!");
            } else if (listener == null) {
                showAlertDialog(alert, this);
            } else {
                showAlertDialog(alert, new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        listener.onDismiss(dialog, BaseActivity.this.code);
                    }
                });
            }
        }
        switch (this.code) {
            case 200:
                updateUser(response);
                break;
            case Response.CODE_ERROR_NOT_ENOUGH_TICKET /*202*/:
                if (DEBUG) {
                    Toast.makeText(this, "code:" + this.code + " ", 1).show();
                    break;
                }
                break;
            case 400:
                if (DEBUG) {
                    Toast.makeText(this, "code:" + this.code + " ", 1).show();
                    break;
                }
                break;
            case 401:
                if (DEBUG) {
                    Toast.makeText(this, "code:" + this.code + " ", 1).show();
                    break;
                }
                break;
            case 403:
                if (DEBUG) {
                    Toast.makeText(this, "code:" + this.code + " ", 1).show();
                    break;
                }
                break;
            case 404:
                if (DEBUG) {
                    Toast.makeText(this, "code:" + this.code + " ", 1).show();
                    break;
                }
                break;
            case 500:
            case 503:
                if (DEBUG) {
                    Toast.makeText(this, "code:" + this.code + " ", 1).show();
                    break;
                }
                break;
            default:
                showToast(getResources().getString(R.string.error_internet));
                break;
        }
        return this.code;
    }

    /* access modifiers changed from: protected */
    public void showAlertDialog(Alert alert, DialogInterface.OnDismissListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(alert.getBody()).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(listener);
        if (!isFinishing()) {
            dialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public ProgressDialog showProgressDialog(DialogInterface.OnCancelListener listener) {
        if (this.mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(R.string.app_name);
            dialog.setMessage(getString(R.string.common_progress_dialog_message));
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(listener);
            this.mProgressDialog = dialog;
        }
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.show();
        return this.mProgressDialog;
    }

    /* access modifiers changed from: protected */
    public ProgressDialog showProgressDialog() {
        return showProgressDialog((DialogInterface.OnCancelListener) null);
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        try {
            this.mProgressDialog.dismiss();
        } catch (IllegalArgumentException e) {
        }
    }

    public void onDismiss(DialogInterface dialog) {
        onDismiss(dialog, this.code);
    }

    public void onDismiss(DialogInterface dialog, int code2) {
        switch (code2) {
            case 401:
                executeRefreshSession();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void showNoticeDialog(String message) {
        showNoticeDialog(message, this);
    }

    /* access modifiers changed from: protected */
    public void showNoticeDialog(String message, DialogInterface.OnDismissListener listener) {
        if (this.mCommondialog == null) {
            this.mCommondialog = new AlertDialog.Builder(this).setTitle(R.string.app_name).setIcon(R.drawable.icon_72).setMessage(message).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
        }
        this.mCommondialog.setMessage(message);
        this.mCommondialog.setCanceledOnTouchOutside(false);
        if (listener != null) {
            this.mCommondialog.setOnDismissListener(listener);
        }
        this.mCommondialog.show();
    }

    /* access modifiers changed from: protected */
    public void showToast(String message) {
        Toast.makeText(this, message, 1).show();
    }

    /* access modifiers changed from: protected */
    public final void cleanupView(View view) {
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback((Drawable.Callback) null);
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable((Drawable) null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable((Drawable) null);
        } else if (view instanceof SeekBar) {
            SeekBar sb = (SeekBar) view;
            sb.setProgressDrawable((Drawable) null);
            sb.setThumb((Drawable) null);
        } else if (view instanceof TextView) {
            ((TextView) view).setBackgroundDrawable((Drawable) null);
        } else if (view instanceof Button) {
            ((Button) view).setBackgroundDrawable((Drawable) null);
        }
        view.setBackgroundDrawable((Drawable) null);
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int size = vg.getChildCount();
            for (int i = 0; i < size; i++) {
                cleanupView(vg.getChildAt(i));
            }
        }
    }

    protected static boolean isSdCardWriteable() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public void showPrivacy() {
        Intent intent = new Intent(getApplicationContext(), CustomWebViewActivity.class);
        intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, String.valueOf(Defs.URL_BASE()) + Defs.URL_LEGAL_PRIVACY);
        startActivity(intent);
    }

    public void showTerms() {
        Intent intent = new Intent(getApplicationContext(), CustomWebViewActivity.class);
        intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, String.valueOf(Defs.URL_BASE()) + Defs.URL_LEGAL_TERMS);
        startActivity(intent);
    }

    public void showRules() {
        Intent intent = new Intent(getApplicationContext(), CustomWebViewActivity.class);
        intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, String.valueOf(Defs.URL_BASE()) + Defs.URL_ABOUT_RULES);
        startActivity(intent);
    }

    public void showBarcodeKanojo() {
        Intent intent = new Intent(getApplicationContext(), CustomWebViewActivity.class);
        intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, String.valueOf(Defs.URL_BASE()) + Defs.URL_ABOUT_BARCODEKANOJO);
        startActivity(intent);
    }

    public void showTeam() {
        Intent intent = new Intent(getApplicationContext(), CustomWebViewActivity.class);
        intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, String.valueOf(Defs.URL_BASE()) + Defs.URL_ABOUT_TEAM);
        startActivity(intent);
    }

    public void showMail() {
        Intent intent = new Intent(getApplicationContext(), CustomWebViewActivity.class);
        String locale = Locale.getDefault().getLanguage();
        String uuid = ((BarcodeKanojoApp) getApplicationContext()).getUUID();
        if (DEBUG) {
            Log.e("lang", locale.toString());
        }
        if (locale.equals(Locale.JAPANESE.toString())) {
            if (DEBUG) {
                Log.e("access lang JA", getGeneralUrl(Defs.URL_GENERAL_JA, uuid));
            }
            intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, getGeneralUrl(Defs.URL_GENERAL_JA, uuid));
        } else if (locale.equals(Locale.CHINESE.toString())) {
            if (DEBUG) {
                Log.e("access lang ZH", getGeneralUrl(Defs.URL_GENERAL_ZH, uuid));
            }
            intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, getGeneralUrl(Defs.URL_GENERAL_ZH, uuid));
        } else {
            if (DEBUG) {
                Log.e("access lang EN", getGeneralUrl(Defs.URL_GENERAL_EN, uuid));
            }
            intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, getGeneralUrl(Defs.URL_GENERAL_EN, uuid));
        }
        startActivity(intent);
    }

    private String getGeneralUrl(String url, String uuid) {
        return String.valueOf(url) + "?id=" + Codec.encode(uuid);
    }

    public void showKDDI() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Defs.URL_KDDI_SERVICE)));
    }

    /* access modifiers changed from: protected */
    public void deleteUser() {
        ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().resetUser();
        new ApplicationSetting(getApplicationContext()).reset();
    }

    public void executeRefreshSession() {
        if (this.mRefreshTask == null || this.mRefreshTask.getStatus() == AsyncTask.Status.FINISHED) {
            if (DEBUG) {
                Log.e("NguyenTT", "Start Run " + this.mRefreshTask);
            }
            this.mRefreshTask = (RefreshTask) new RefreshTask().execute(new Void[0]);
        } else if (DEBUG) {
            Log.e(TAG, "Query already running attempting to cancel: " + this.mRefreshTask);
        }
    }

    class RefreshTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        RefreshTask() {
        }

        public void onPreExecute() {
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
                if (this.mReason != null && BaseActivity.DEBUG) {
                    Log.d("NguyenTT", "Error message: " + this.mReason.getMessage());
                }
                if (response == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
                if (BaseActivity.DEBUG) {
                    Response.checkResponse(response);
                }
                switch (response.getCode()) {
                    case 200:
                        BaseActivity.this.mBaseLoadingFinished = true;
                        BaseActivity.this.endCheckSession();
                        return;
                    case 400:
                    case 403:
                    case 404:
                        return;
                    case 401:
                        BaseActivity.this.showNoticeDialog("lalallalal");
                        return;
                    default:
                        return;
                }
            } catch (BarcodeKanojoException e) {
                if (BaseActivity.DEBUG) {
                    e.printStackTrace();
                }
                BaseActivity.this.endCheckSession();
                BaseActivity.this.showNoticeDialog(BaseActivity.this.getResources().getString(R.string.slow_network));
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
        }

        /* access modifiers changed from: package-private */
        public Response<?> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            return ((BarcodeKanojoApp) BaseActivity.this.getApplication()).getBarcodeKanojo().android_verify(((BarcodeKanojoApp) BaseActivity.this.getApplication()).getUUID());
        }
    }

    public void executeCheckSession() {
        if (this.mCheckSessionTask == null || this.mCheckSessionTask.getStatus() == AsyncTask.Status.FINISHED) {
            if (DEBUG) {
                Log.e("NguyenTT", "Start Run " + this.mCheckSessionTask);
            }
            this.mCheckSessionTask = (CheckSessionTask) new CheckSessionTask().execute(new Void[0]);
        } else if (DEBUG) {
            Log.e(TAG, "Query already running attempting to cancel: " + this.mCheckSessionTask);
        }
    }

    class CheckSessionTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        CheckSessionTask() {
        }

        public void onPreExecute() {
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
                if (this.mReason != null && BaseActivity.DEBUG) {
                    Log.d("NguyenTT", "Error message: " + this.mReason.getMessage());
                }
                if (response == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
                if (BaseActivity.DEBUG) {
                    Response.checkResponse(response);
                }
                switch (response.getCode()) {
                    case 200:
                        BaseActivity.this.mBaseLoadingFinished = true;
                        BaseActivity.this.endCheckSession();
                        return;
                    case 400:
                    case 403:
                    case 404:
                        return;
                    case 401:
                        BaseActivity.this.executeRefreshSession();
                        return;
                    default:
                        return;
                }
            } catch (BarcodeKanojoException e) {
                if (BaseActivity.DEBUG) {
                    e.printStackTrace();
                }
                BaseActivity.this.endCheckSession();
                BaseActivity.this.showNoticeDialog(BaseActivity.this.getResources().getString(R.string.slow_network));
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
        }

        /* access modifiers changed from: package-private */
        public Response<?> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            return ((BarcodeKanojoApp) BaseActivity.this.getApplication()).getBarcodeKanojo().account_show();
        }
    }

    /* access modifiers changed from: protected */
    public void showAlertFullStorageDialog(String message) {
        if (this.mFullStorageDialog == null) {
            this.mFullStorageDialog = new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(message).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
        }
        this.mFullStorageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                BaseActivity.this.sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT));
            }
        });
        this.mFullStorageDialog.setCanceledOnTouchOutside(false);
        if (!isFinishing() && !this.mFullStorageDialog.isShowing()) {
            this.mFullStorageDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void startCheckSession() {
        executeCheckSession();
    }

    /* access modifiers changed from: protected */
    public void endCheckSession() {
    }

    /* access modifiers changed from: protected */
    public void setAutoRefreshSession(boolean autoRefresh) {
        this.mAutoRefresh = autoRefresh;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        if (DEBUG) {
            Log.d(TAG, "locate changed, refresh http...");
        }
        ((BarcodeKanojoApp) getApplication()).changeLocate();
    }

    public void dismissNoticeDialog() {
        if (this.mCommondialog != null) {
            this.mCommondialog.dismiss();
        }
    }
}
