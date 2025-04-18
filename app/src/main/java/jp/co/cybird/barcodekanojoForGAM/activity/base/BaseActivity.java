package jp.co.cybird.barcodekanojoForGAM.activity.base;

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

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.Locale;

import com.goujer.barcodekanojo.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.CustomWebViewActivity;
import com.goujer.barcodekanojo.core.BarcodeKanojo;

import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import com.goujer.barcodekanojo.core.model.User;

import com.goujer.barcodekanojo.preferences.ApplicationSetting;

public abstract class BaseActivity extends GreeBaseActivity implements BaseInterface, DialogInterface.OnDismissListener {
    protected static final String TAG = "BaseActivity";
    public static int mActivityCount;
    private int code = 0;
    private boolean mAutoRefresh = true;
    public boolean mBaseLoadingFinished = false;

    private CheckSessionTask mCheckSessionTask;
	private RefreshTask mRefreshTask;

    AlertDialog mCommondialog;
    private AlertDialog mFullStorageDialog;
	protected ProgressDialog mProgressDialog;

    private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Defs.DEBUG) {
                Log.d(TAG, "Current On Recieve: " + BaseActivity.mActivityCount);
            }
            if (Defs.DEBUG) {
                Log.d(BaseActivity.TAG, "onReceive: " + intent + ", at " + this);
            }
            BaseActivity.this.finish();
        }
    };

    public BroadcastReceiver mWarningFullSpaceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Defs.DEBUG) {
                Log.d(BaseActivity.TAG, "mWarningFullSpaceReceiver: " + intent + ", at " + this);
            }
            BaseActivity.this.showAlertFullStorageDialog(BaseActivity.this.getString(R.string.error_out_of_memory));
        }
    };
    private User tmpUser;

    public interface OnDialogDismissListener {
        void onDismiss(DialogInterface dialogInterface, int i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCount++;
        String name = getLocalClassName();
        if (Defs.DEBUG) {
            Log.d(TAG, "Start Activity: " + name + " Current: " + mActivityCount);
        }
        registerReceiver(this.mLoggedOutReceiver, new IntentFilter(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT));
        registerReceiver(this.mWarningFullSpaceReceiver, new IntentFilter(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mAutoRefresh) {
            if (Defs.DEBUG) {
                Log.d(TAG, "Check session for Activity " + getLocalClassName());
            }
            startCheckSession();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissNoticeDialog();
    }

    @Override
    protected void onDestroy() {
        if (Defs.DEBUG) {
            mActivityCount--;
            Log.d(TAG, "End Activity: " + getLocalClassName() + " Current: " + mActivityCount);
        }
        try {
            unregisterReceiver(this.mLoggedOutReceiver);
        } catch (Exception e) {}
	    try {
		    unregisterReceiver(this.mWarningFullSpaceReceiver);
	    } catch (Exception e) {}

        ViewGroup root = getWindow().getDecorView().findViewById(R.id.common_top_menu_root);
        if (!(root == null || root.getChildCount() == 0)) {
            cleanupView(root.getChildAt(0));
        }
        super.onDestroy();
    }

    protected Dialog onCreateDialog(int id) {
        return null;
    }

    protected void backupUser(User user) {
        this.tmpUser = user;
    }

    protected void checkAndCopyUser() {
        User user = ((BarcodeKanojoApp) getApplication()).getUser();
        if (user.getName() == null) {
            if (Defs.DEBUG) {
                Log.d(TAG, "Name copied:" + this.tmpUser.getName());
            }
            user.setName(this.tmpUser.getName());
        }
        if (user.getBirthText().equals("")) {
            if (Defs.DEBUG) {
                Log.d(TAG, "Birthday copied:" + this.tmpUser.getBirthText());
            }
            user.setBirthFromText(this.tmpUser.getBirthText());
        }
    }

    protected void onUserUpdated() {
    }

    protected void updateUser(Response<?> response) {
        User user = (User) response.get(User.class);
        if (user != null) {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo();
            User old = barcodeKanojo.getUser();
            if (old == null) {
                if (Defs.DEBUG) {
                    Log.d(TAG, "new user id: " + user.getId());
                }
                barcodeKanojo.setUser(user);
            } else if (user.getId() == old.getId()) {
                if (Defs.DEBUG) {
                    Log.d(TAG, "update user id: " + user.getId() + " , stamina:" + user.getStamina());
                }
                backupUser(old);
                barcodeKanojo.setUser(user);
                checkAndCopyUser();
                onUserUpdated();
            }
        }
    }

    protected int getCodeAndShowAlert(Response<?> response, Exception e) throws BarcodeKanojoException {
        return getCodeAndShowAlert(response, e, null);
    }

    protected int getCodeAndShowAlert(Response<?> response, Exception e, final OnDialogDismissListener listener) throws BarcodeKanojoException {
        if (response == null) {
            showToast(getResources().getString(R.string.error_internet));
            if (!Defs.DEBUG) {
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
                showAlertDialog(alert, dialog -> listener.onDismiss(dialog, BaseActivity.this.code));
            }
        } else if (listener != null) {
			listener.onDismiss(null, this.code);
        }
		if (this.code == Response.CODE_SUCCESS) {
			updateUser(response);
		} else {
			if (Defs.DEBUG) {
				showToast("code: " + this.code);
			} else {
				showToast(getResources().getString(R.string.error_internet));
			}
		}
        return this.code;
    }

    protected void showAlertDialog(Alert alert, DialogInterface.OnDismissListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setMessage(alert.getBody())
				.setPositiveButton(R.string.common_dialog_ok, (dialog1, which) -> {})
				.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(listener);
        if (!isFinishing()) {
            dialog.show();
        }
    }

    protected void showProgressDialog(DialogInterface.OnCancelListener listener) {
        if (this.mProgressDialog == null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(R.string.app_name);
            dialog.setMessage(getString(R.string.common_progress_dialog_message));
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(listener);
            this.mProgressDialog = dialog;
        }
		if (!this.mProgressDialog.isShowing()) {
			this.mProgressDialog.show();
		}
    }

    protected void showProgressDialog() {
        showProgressDialog(null);
    }

    protected void dismissProgressDialog() {
        try {
            this.mProgressDialog.dismiss();
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onDismiss(dialog, this.code);
    }

    public void onDismiss(DialogInterface dialog, int code2) {
		if (code2 == 401) {
			executeRefreshSession();
		}
	}

    protected void showNoticeDialog(String message) {
        showNoticeDialog(message, this);
    }

    protected void showNoticeDialog(String message, DialogInterface.OnDismissListener listener) {
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

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected final void cleanupView(View view) {
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback(null);
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable(null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(null);
        } else if (view instanceof SeekBar) {
            SeekBar sb = (SeekBar) view;
            sb.setProgressDrawable(null);
            sb.setThumb(null);
        } else if (view instanceof TextView) {
            view.setBackgroundDrawable(null);
        } else if (view instanceof Button) {
            view.setBackgroundDrawable(null);
        }
        view.setBackgroundDrawable(null);
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

    public void showMail() {
        Intent intent = new Intent(getApplicationContext(), CustomWebViewActivity.class);
        String locale = Locale.getDefault().getLanguage();
        String uuid = ((BarcodeKanojoApp) getApplication()).getSettings().getUUID();
        if (Defs.DEBUG) {
            Log.e("lang", locale);
        }
        if (locale.equals(Locale.JAPANESE.toString())) {
            if (Defs.DEBUG) {
                Log.e("access lang JA", getGeneralUrl(Defs.URL_GENERAL_JA, uuid));
            }
            intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, getGeneralUrl(Defs.URL_GENERAL_JA, uuid));
        } else if (locale.equals(Locale.CHINESE.toString())) {
            if (Defs.DEBUG) {
                Log.e("access lang ZH", getGeneralUrl(Defs.URL_GENERAL_ZH, uuid));
            }
            intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, getGeneralUrl(Defs.URL_GENERAL_ZH, uuid));
        } else {
            if (Defs.DEBUG) {
                Log.e("access lang EN", getGeneralUrl(Defs.URL_GENERAL_EN, uuid));
            }
            intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, getGeneralUrl(Defs.URL_GENERAL_EN, uuid));
        }
        startActivity(intent);
    }

    private String getGeneralUrl(String url, String uuid) {
    	try {
			return url + "?id=" + URLEncoder.encode(uuid, "UTF-8");
		} catch (UnsupportedEncodingException e) {
    		if (Defs.DEBUG) {
				e.printStackTrace();
			}
			return url + "?id=" + uuid;
		}
    }

    public void showKDDI() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Defs.URL_KDDI_SERVICE)));
    }

    public void executeRefreshSession() {
        if (this.mRefreshTask == null || this.mRefreshTask.getStatus() == AsyncTask.Status.FINISHED) {
            if (Defs.DEBUG) {
                Log.e(TAG, "Start Run " + this.mRefreshTask);
            }
            this.mRefreshTask = (RefreshTask) new RefreshTask().execute(new Void[0]);
        } else if (Defs.DEBUG) {
            Log.e(TAG, "Query already running attempting to cancel: " + this.mRefreshTask);
        }
    }

    class RefreshTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        RefreshTask() {
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        protected Response<?> doInBackground(Void... params) {
            try {
				return ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().verify(((BarcodeKanojoApp) getApplication()).getSettings().getUUID(), "", null);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<?> response) {
            try {
                if (this.mReason != null && Defs.DEBUG) {
                    Log.d(TAG, "Error message: " + this.mReason.getMessage());
                }
                if (response == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
                if (Defs.DEBUG) {
                    Response.checkResponse(response);
                }
                switch (response.getCode()) {
					case Response.CODE_SUCCESS:
                        BaseActivity.this.mBaseLoadingFinished = true;
                        BaseActivity.this.endCheckSession();
                        return;
                    case Response.CODE_ERROR_BAD_REQUEST:
					case Response.CODE_ERROR_FORBIDDEN:
					case Response.CODE_ERROR_NOT_FOUND:
                        return;
					case Response.CODE_ERROR_UNAUTHORIZED:
                        BaseActivity.this.showNoticeDialog("lalallalal");
                }
            } catch (BarcodeKanojoException e) {
				BaseActivity.this.endCheckSession();
                if (Defs.DEBUG) {
                    e.printStackTrace();
                }
                BaseActivity.this.showNoticeDialog(BaseActivity.this.getResources().getString(R.string.slow_network));
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    public void executeCheckSession() {
        if (this.mCheckSessionTask == null || this.mCheckSessionTask.getStatus() == AsyncTask.Status.FINISHED) {
            if (Defs.DEBUG) {
                Log.e(TAG, "Start Run " + this.mCheckSessionTask);
            }
            this.mCheckSessionTask = (CheckSessionTask) new CheckSessionTask(this).execute(new Void[0]);
        } else if (Defs.DEBUG) {
            Log.e(TAG, "Query already running attempting to cancel: " + this.mCheckSessionTask);
        }
    }

    private static class CheckSessionTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

	    private final WeakReference<BaseActivity> activityRef;

	    CheckSessionTask(BaseActivity activity) {
		    super();
		    activityRef = new WeakReference<>(activity);
	    }

        @Override
        protected Response<?> doInBackground(Void... params) {
	        BaseActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return null;
	        }

            try {
                return ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo().account_show();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<?> response) {
	        BaseActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            try {
                if (this.mReason != null && Defs.DEBUG) {
                    Log.d(TAG, "Error message: " + this.mReason.getMessage());
                    mReason.printStackTrace();
                }
                if (response == null) {
                    throw new BarcodeKanojoException("Response is null! \n" + this.mReason);
                }
                if (Defs.DEBUG) {
                    Response.checkResponse(response);
                }
                switch (response.getCode()) {
                    case Response.CODE_SUCCESS:
                        activity.mBaseLoadingFinished = true;
                        activity.endCheckSession();
                        return;
                    case Response.CODE_ERROR_BAD_REQUEST:
                    case Response.CODE_ERROR_FORBIDDEN:
					case Response.CODE_ERROR_NOT_FOUND:
                        return;
                    case Response.CODE_ERROR_UNAUTHORIZED:
	                    activity.executeRefreshSession();
                        return;
                    default:
				}
            } catch (BarcodeKanojoException e) {
                if (Defs.DEBUG) {
                    e.printStackTrace();
                }
                activity.endCheckSession();
                activity.showNoticeDialog(e.getMessage());
            }
        }
    }

    protected void showAlertFullStorageDialog(String message) {
        if (this.mFullStorageDialog == null) {
            this.mFullStorageDialog = new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(message).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
        }
        this.mFullStorageDialog.setOnDismissListener(dialog -> BaseActivity.this.sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT)));
        this.mFullStorageDialog.setCanceledOnTouchOutside(false);
        if (!isFinishing() && !this.mFullStorageDialog.isShowing()) {
            this.mFullStorageDialog.show();
        }
    }

    protected void startCheckSession() {
        executeCheckSession();
    }

    protected void endCheckSession() {
    }

    protected void setAutoRefreshSession(boolean autoRefresh) {
        this.mAutoRefresh = autoRefresh;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        if (Defs.DEBUG) {
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
