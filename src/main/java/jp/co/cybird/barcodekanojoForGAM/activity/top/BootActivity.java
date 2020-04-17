package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;
import com.google.android.gcm.GCMRegistrar;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import jp.co.cybird.android.lib.gcm.GCMUtilities;
import jp.co.cybird.app.android.lib.cybirdid.CybirdCommonUserId;
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

public class BootActivity extends BaseKanojosActivity {
    private static final boolean DEBUG = false;
    private static final String TAG = BootActivity.class.getSimpleName();
    private volatile boolean authorizationDone;
    private BootTask mBootTask;
    /* access modifiers changed from: private */
    public RelativeLayout mProgressbar;
    final Handler mTaskEndHandler = new Handler() {
        public void handleMessage(Message msg) {
            StatusHolder next = (StatusHolder) BootActivity.this.getQueue().poll();
            if (next != null) {
                BootActivity.this.executeBootTask(next);
            }
        }
    };
    private Queue<StatusHolder> mTaskQueue;
    private boolean mTest = true;
    private long splashDelay = 1500;

    public void onCreate(Bundle savedInstanceState) {
        this.mBaseLoadingFinished = true;
        setAutoRefreshSession(false);
        super.onCreate(savedInstanceState);
        unregisterReceiver(this.mWarningFullSpaceReceiver);
        setContentView(R.layout.boot);
        this.mProgressbar = (RelativeLayout) findViewById(R.id.progressbar);
    }

    public void onResume() {
        super.onResume();
        Log.d("NguyenTT", "parametersString: " + GCMUtilities.parseParametersString(getIntent()));
        ApplicationSetting setting = new ApplicationSetting(this);
        String userAndroidID = setting.getUUID();
        if (userAndroidID == null) {
            userAndroidID = CybirdCommonUserId.get(this);
        }
        setting.commitUUID(userAndroidID);
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

    /* access modifiers changed from: protected */
    public void startDashboard() {
        logout();
        GCMUtilities.runGCM(this);
        startActivity(new Intent().setClass(this, KanojosActivity.class));
    }

    /* access modifiers changed from: protected */
    public void startConfig() {
        logout();
        Intent signUp = new Intent().setClass(this, SignUpActivity.class);
        signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
        startActivity(signUp);
    }

    private void startShowPrivacy() {
        startActivityForResult(new Intent().setClass(this, PrivacyInfoActivity.class), BaseInterface.REQUEST_SHOW_PRIVACY);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 809 && resultCode == 110) {
            startConfig();
        } else {
            logout();
        }
    }

    private boolean isLoading(StatusHolder status) {
        if (status.loading) {
            return true;
        }
        return false;
    }

    static class StatusHolder {
        public static final int LOGIN_TASK = 0;
        public static final int SIGNUP_TASK = 2;
        public static final int UUIDVERIFY_TASK = 1;
        int key;
        boolean loading = false;

        StatusHolder() {
        }
    }

    /* access modifiers changed from: private */
    public Queue<StatusHolder> getQueue() {
        if (this.mTaskQueue == null) {
            this.mTaskQueue = new LinkedList();
        }
        return this.mTaskQueue;
    }

    private synchronized void clearQueue() {
        getQueue().clear();
    }

    /* access modifiers changed from: private */
    public synchronized boolean isQueueEmpty() {
        return this.mTaskQueue.isEmpty();
    }

    private synchronized void executeBootListTask() {
        clearQueue();
        StatusHolder mLoginHolder = new StatusHolder();
        mLoginHolder.key = 0;
        getQueue().offer(mLoginHolder);
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: private */
    public void executeBootTask(StatusHolder list) {
        if (isLoading(list)) {
            Log.d("NguyenTT", "task " + list.key + " is running ");
            return;
        }
        this.mBootTask = new BootTask();
        this.mBootTask.setList(list);
        this.mProgressbar.setVisibility(8);
        this.mBootTask.execute(new Void[0]);
    }

    class BootTask extends AsyncTask<Void, Void, Response<?>> {
        private StatusHolder mList;
        private Exception mReason = null;

        BootTask() {
        }

        public void setList(StatusHolder list) {
            this.mList = list;
        }

        public void onPreExecute() {
            this.mList.loading = true;
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process(this.mList);
            } catch (Exception e) {
                this.mReason = e;
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
                    case 200:
                        if (!BootActivity.this.isQueueEmpty()) {
                            BootActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            return;
                        } else {
                            BootActivity.this.nextScreen(this.mList);
                            return;
                        }
                    case 403:
                        this.mList.key = 2;
                        BootActivity.this.nextScreen(this.mList);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                if (this.mList.key != 0 || !this.mReason.getMessage().equalsIgnoreCase("user not found")) {
                }
            }
            if (this.mList.key != 0 || !this.mReason.getMessage().equalsIgnoreCase("user not found")) {
                BootActivity.this.mProgressbar.setVisibility(4);
                BootActivity.this.showAlertDialog(new Alert(BootActivity.this.getResources().getString(R.string.error_internet)), new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        BootActivity.this.logout();
                    }
                });
                return;
            }
            this.mList.key = 2;
            BootActivity.this.nextScreen(this.mList);
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            BootActivity.this.mProgressbar.setVisibility(4);
        }

        /* access modifiers changed from: package-private */
        public Response<?> process(StatusHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) BootActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            ApplicationSetting setting = new ApplicationSetting(BootActivity.this);
            if (list == null) {
                throw new BarcodeKanojoException("process:StatusHolder is null!");
            }
            switch (list.key) {
                case 0:
                    Response<BarcodeKanojoModel> android_verify = barcodeKanojo.android_verify(((BarcodeKanojoApp) BootActivity.this.getApplication()).getUUID());
                    if (android_verify == null) {
                        return android_verify;
                    }
                    barcodeKanojo.init_product_category_list();
                    return android_verify;
                case 1:
                    return barcodeKanojo.android_uuid_verify(user.getEmail(), user.getPassword(), setting.getUUID());
                default:
                    return null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void nextScreen(StatusHolder list) {
        switch (list.key) {
            case 0:
                startDashboard();
                break;
            case 2:
                startShowPrivacy();
                break;
        }
        this.mProgressbar.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void logout() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void startCheckSession() {
    }

    /* access modifiers changed from: protected */
    public void endCheckSession() {
    }
}
