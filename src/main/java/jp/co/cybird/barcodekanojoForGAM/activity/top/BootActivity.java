package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.gcm.GCMRegistrar;
import com.goujer.barcodekanojo.activity.setting.ServerConfigurationActivity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import jp.co.cybird.android.lib.gcm.GCMUtilities;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseKanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;

public final class BootActivity extends BaseKanojosActivity {
    private static final String TAG = BootActivity.class.getSimpleName();
    private volatile boolean authorizationDone;
    private RelativeLayout mProgressbar;
    final Handler mTaskEndHandler = new Handler() {
		@Override
    	public void handleMessage(Message msg) {
            StatusHolder next = BootActivity.this.getQueue().poll();
            if (next != null) {
                BootActivity.this.executeBootTask(next);
            }
        }
    };
    private LinkedList<StatusHolder> mTaskQueue;
    private boolean mTest = true;
    private long splashDelay = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mBaseLoadingFinished = true;
        setAutoRefreshSession(false);
        super.onCreate(savedInstanceState);
        unregisterReceiver(this.mWarningFullSpaceReceiver);
        setContentView(R.layout.boot);
        this.mProgressbar = findViewById(R.id.progressbar);
		if (new ApplicationSetting(this).getServerURL().equals("")) {
			startActivity(new Intent(this, ServerConfigurationActivity.class));
		}
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NguyenTT", "parametersString: " + GCMUtilities.parseParametersString(getIntent()));
        ApplicationSetting setting = new ApplicationSetting(this);
        String userAndroidID = setting.getUUID();
        if (userAndroidID == null) {
            userAndroidID = UUID.randomUUID().toString();
			setting.commitUUID(userAndroidID);
        }
        setting.commitDeviceToken(GCMRegistrar.getRegistrationId(this));
        setting.commitUserGoogleAccount();
        Log.d("NguyenTT", "UUID " + userAndroidID);
        Log.d("NguyenTT", "Start BootActivity");
        Log.d("NguyenTT", "reg_id: " + GCMRegistrar.getRegistrationId(this));
        Log.d("NguyenTT", "facebook_token: " + setting.getFaceBookToken());
        Log.d("NguyenTT", "facebook_id: " + setting.getFaceBookID());
		try {
			executeBootListTask();
		} catch (Exception e) {
			e.printStackTrace();
			showNoticeDialog(e.getMessage());
		}
    }

    protected void startDashboard() {
        logout();
        GCMUtilities.runGCM(this);
        startActivity(new Intent().setClass(this, KanojosActivity.class));
    }

    protected void startConfig() {
        logout();
        Intent signUp = new Intent().setClass(this, SignUpActivity.class);
        signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
        startActivity(signUp);
    }

    private void startShowPrivacy() {
        startActivityForResult(new Intent().setClass(this, PrivacyInfoActivity.class), BaseInterface.REQUEST_SHOW_PRIVACY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 809 && resultCode == 110) {
            startConfig();
        } else {
            logout();
        }
    }

    private boolean isLoading(StatusHolder status) {
		return status.loading;
	}

    static class StatusHolder {
        static final int LOGIN_TASK = 0;
        static final int SIGNUP_TASK = 2;
        static final int UUIDVERIFY_TASK = 1;
        int key;
        boolean loading = false;
    }

    private Queue<StatusHolder> getQueue() {
        if (this.mTaskQueue == null) {
            this.mTaskQueue = new LinkedList<>();
        }
        return this.mTaskQueue;
    }

    private synchronized void clearQueue() {
        getQueue().clear();
    }

    private synchronized boolean isQueueEmpty() {
        return this.mTaskQueue.isEmpty();
    }

    private synchronized void executeBootListTask() {
		ApplicationSetting setting = new ApplicationSetting(this);
		((BarcodeKanojoApp) getApplication()).updateBCKApi(setting.getServerHttps(), setting.getServerURL(), setting.getServerPort());
        clearQueue();
        StatusHolder mLoginHolder = new StatusHolder();
        mLoginHolder.key = StatusHolder.LOGIN_TASK;
        getQueue().offer(mLoginHolder);
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    private void executeBootTask(StatusHolder status) {
        if (isLoading(status)) {
            Log.d("NguyenTT", "task " + status.key + " is running ");
            return;
        }
		BootTask mBootTask = new BootTask();
        mBootTask.setList(status);
        this.mProgressbar.setVisibility(View.VISIBLE);
        mBootTask.execute();
    }

    class BootTask extends AsyncTask<Void, Void, Response<?>> {
        private StatusHolder mStatus;
        private Exception mReason = null;

        BootTask() {
        }

        public void setList(StatusHolder status) {
            this.mStatus = status;
        }

        public void onPreExecute() {
            this.mStatus.loading = true;
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process(this.mStatus);
            } catch (Exception e) {
                this.mReason = e;
                e.printStackTrace();
                return null;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x0033  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0054  */
        public void onPostExecute(Response<?> response) {
            try {
                if (this.mReason != null) {
                }
                switch (response.getCode()) {
                    case Response.CODE_SUCCESS:
                        if (!BootActivity.this.isQueueEmpty()) {
                            BootActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            return;
                        } else {
                            BootActivity.this.nextScreen(this.mStatus);
                            return;
                        }
                    case Response.CODE_ERROR_FORBIDDEN:
                        this.mStatus.key = StatusHolder.SIGNUP_TASK;
                        BootActivity.this.nextScreen(this.mStatus);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
            	e.printStackTrace();
			}
            if (this.mStatus.key != StatusHolder.LOGIN_TASK || !this.mReason.getMessage().equalsIgnoreCase("user not found")) {
                BootActivity.this.mProgressbar.setVisibility(View.INVISIBLE);
                BootActivity.this.showAlertDialog(new Alert(BootActivity.this.getResources().getString(R.string.error_internet)), new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
						startActivity(new Intent(BootActivity.this, ServerConfigurationActivity.class));
                    	//BootActivity.this.logout();
                    }
                });
                return;
            }
            this.mStatus.key = StatusHolder.SIGNUP_TASK;
            BootActivity.this.nextScreen(this.mStatus);
        }

        protected void onCancelled() {
            BootActivity.this.mProgressbar.setVisibility(View.INVISIBLE);
        }

        Response<?> process(StatusHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) BootActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            ApplicationSetting setting = new ApplicationSetting(BootActivity.this);
            if (list == null) {
                throw new BarcodeKanojoException("process:StatusHolder is null!");
            }
            switch (list.key) {
                case StatusHolder.LOGIN_TASK:
                    Response<BarcodeKanojoModel> android_verify = barcodeKanojo.android_verify(((BarcodeKanojoApp) BootActivity.this.getApplication()).getUUID());
                    if (android_verify == null) {
                        return null;
                    }
                    barcodeKanojo.init_product_category_list();
                    return android_verify;
                case StatusHolder.UUIDVERIFY_TASK:
                    return barcodeKanojo.android_uuid_verify(user.getEmail(), user.getPassword(), setting.getUUID());
                default:
                    return null;
            }
        }
    }

    void nextScreen(StatusHolder list) {
        switch (list.key) {
            case StatusHolder.LOGIN_TASK:
                startDashboard();
                break;
            case StatusHolder.SIGNUP_TASK:
                startShowPrivacy();
                break;
        }
        this.mProgressbar.setVisibility(View.GONE);
    }

    private void logout() {
        finish();
    }

    protected void startCheckSession() {
    }

    protected void endCheckSession() {
    }
}
