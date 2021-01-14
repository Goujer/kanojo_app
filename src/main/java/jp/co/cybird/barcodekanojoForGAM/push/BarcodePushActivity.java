package jp.co.cybird.barcodekanojoForGAM.push;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.activity.DashboardActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.WebViewTabActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseKanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.setting.UserModifyActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import com.goujer.barcodekanojo.preferences.ApplicationSetting;

public class BarcodePushActivity extends BaseKanojosActivity {
    private static final String PUSH_NOTIFICATION_P1 = "webview";
    private static final String PUSH_NOTIFICATION_P2 = "dashboard";
    private static final String PUSH_NOTIFICATION_P3 = "kanojo";
    private static final String PUSH_NOTIFICATION_REGISTER = "registered";
    private AutoLoginTask mAutoLoginTask;
    public final BroadcastReceiver mReciver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        }
    };
    final Handler mTaskEndHandler = new Handler() {
        public void handleMessage(Message msg) {
            StatusHolder next = BarcodePushActivity.this.getQueue().poll();
            if (next != null) {
                BarcodePushActivity.this.executePushTask(next);
            }
        }
    };
    private Queue<StatusHolder> mTaskQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        ApplicationSetting mSetting = new ApplicationSetting(this);
        String uuid = ((BarcodeKanojoApp) getApplication()).getUUID();
        String push_type = getIntent().getStringExtra("push_type");
        mSetting.commitUUID(uuid);
        if (push_type.equalsIgnoreCase(PUSH_NOTIFICATION_REGISTER)) {
            mSetting.commitDeviceToken(getIntent().getStringExtra("reg_id"));
            executePushListTask(PUSH_NOTIFICATION_REGISTER, 0);
        } else {
            String nextScreen = getIntent().getStringExtra("next_screen");
            if (nextScreen.equalsIgnoreCase(PUSH_NOTIFICATION_P1)) {
                executePushListTask(PUSH_NOTIFICATION_P1, 0);
            } else if (nextScreen.equalsIgnoreCase(PUSH_NOTIFICATION_P2)) {
                executePushListTask(PUSH_NOTIFICATION_P2, 0);
            } else if (nextScreen.equalsIgnoreCase(PUSH_NOTIFICATION_P3)) {
                executePushListTask(PUSH_NOTIFICATION_P3, Integer.parseInt(getIntent().getStringExtra("kanojo_id")));
            } else {
                finish();
                overridePendingTransition(0, 0);
            }
        }
        setAutoRefreshSession(false);
    }

    @Override
    protected void changeTab(Context packageContext, Class<?> cls) {
        if (!packageContext.getClass().getName().equalsIgnoreCase(cls.getName())) {
            Intent intent = new Intent().setClass(packageContext, cls);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            packageContext.startActivity(intent);
            finish();
            ((Activity) packageContext).overridePendingTransition(0, 0);
        }
    }

    protected void startKanojoActivity(Kanojo kanojo) {
        if (kanojo != null) {
            Intent intent = new Intent().setClass(this, KanojosActivity.class);
			intent.putExtra(BaseInterface.EXTRA_KANOJO, kanojo);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
        }
    }

    private boolean isLoading(StatusHolder status) {
        if (status.loading) {
            return true;
        }
        return false;
    }

    static class StatusHolder {
        public static final int DASHBOARD_PUSH = 2;
        public static final int KANOJO_PUSH = 3;
        public static final int LOGIN_TASK = 1;
        public static final int NO_PUSH = 4;
        public static final int PUSH_TASK = 1;
        public static final int REGISTER_DEVICE_TASK = 0;
        public static final int REGISTER_PUSH = 0;
        public static final int WEBVIEW_PUSH = 1;
        int kanojo_id = 0;
        int key;
        boolean loading = false;
        int mPushType;
        String type = "";

        StatusHolder() {
        }

        void updatePushType() {
            if (this.type.equalsIgnoreCase(BarcodePushActivity.PUSH_NOTIFICATION_P1)) {
                this.mPushType = 1;
            } else if (this.type.equalsIgnoreCase(BarcodePushActivity.PUSH_NOTIFICATION_P2)) {
                this.mPushType = 2;
            } else if (this.type.equalsIgnoreCase(BarcodePushActivity.PUSH_NOTIFICATION_P3)) {
                this.mPushType = 3;
            } else if (this.type.equalsIgnoreCase(BarcodePushActivity.PUSH_NOTIFICATION_REGISTER)) {
                this.mPushType = 0;
            } else {
                this.mPushType = 4;
            }
        }
    }

    private Queue<StatusHolder> getQueue() {
        if (this.mTaskQueue == null) {
            this.mTaskQueue = new LinkedList();
        }
        return this.mTaskQueue;
    }

    private synchronized void clearQueue() {
        getQueue().clear();
    }

    private synchronized boolean isQueueEmpty() {
        return this.mTaskQueue.isEmpty();
    }

    private synchronized void executePushListTask(String typePush, int kanojo_id) {
        clearQueue();
        StatusHolder mLoginHolder = new StatusHolder();
        mLoginHolder.key = 1;
        mLoginHolder.updatePushType();
        StatusHolder mRegisterHolder = new StatusHolder();
        mRegisterHolder.key = 0;
        mRegisterHolder.type = PUSH_NOTIFICATION_REGISTER;
        mRegisterHolder.updatePushType();
        StatusHolder mPushHolder = new StatusHolder();
        mPushHolder.key = 1;
        mPushHolder.type = typePush;
        mPushHolder.kanojo_id = kanojo_id;
        mPushHolder.updatePushType();
        if (typePush.equalsIgnoreCase(PUSH_NOTIFICATION_REGISTER)) {
            getQueue().offer(mRegisterHolder);
        } else {
            getQueue().offer(mLoginHolder);
            getQueue().offer(mPushHolder);
        }
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    private void executePushTask(StatusHolder list) {
        if (!isLoading(list)) {
            this.mAutoLoginTask = new AutoLoginTask();
            this.mAutoLoginTask.setList(list);
            this.mAutoLoginTask.execute();
        }
    }

    class AutoLoginTask extends AsyncTask<Void, Void, Response<?>> {
        private StatusHolder mList;
        private Exception mReason = null;

        AutoLoginTask() {
        }

        public void setList(StatusHolder list) {
            this.mList = list;
        }

        public void onPreExecute() {
            if (this.mList != null) {
                this.mList.loading = true;
            }
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process(this.mList);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
                if (this.mReason != null) {
                }
                if (response == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
				if (response.getCode() == 200) {
					if (BarcodePushActivity.this.isQueueEmpty()) {
						BarcodePushActivity.this.processPush(this.mList);
					} else {
						BarcodePushActivity.this.mTaskEndHandler.sendEmptyMessage(0);
					}
				}
            } catch (BarcodeKanojoException e) {
                if (this.mList.key == 1 && this.mReason.getMessage().equalsIgnoreCase("user not found")) {
                    Intent signUp = new Intent().setClass(BarcodePushActivity.this, UserModifyActivity.class);
                    signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
                    BarcodePushActivity.this.startActivity(signUp);
                }
                BarcodePushActivity.this.finish();
                BarcodePushActivity.this.overridePendingTransition(0, 0);
            } finally {
                BarcodePushActivity.this.overridePendingTransition(0, 0);
            }
        }

        @Override
        protected void onCancelled() {
        }

        Response<?> process(StatusHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) BarcodePushActivity.this.getApplication()).getBarcodeKanojo();
            ApplicationSetting setting = new ApplicationSetting(BarcodePushActivity.this);
            switch (list.key) {
                case 0:
                    return barcodeKanojo.android_register_device(setting.getUUID(), setting.getDeviceToken());
                case 1:
                    Response<BarcodeKanojoModel> android_verify = barcodeKanojo.verify("", "", ((BarcodeKanojoApp) getApplication()).getUUID());
                    if (android_verify == null) {
                        return android_verify;
                    }
                    barcodeKanojo.init_product_category_list();
                    return android_verify;
                default:
                    return null;
            }
        }
    }

    public void processPush(StatusHolder mList) {
        switch (mList.mPushType) {
            case 0:
                finish();
                overridePendingTransition(0, 0);
                return;
            case 1:
                changeTab(this, WebViewTabActivity.class);
                return;
            case 2:
                changeTab(this, DashboardActivity.class);
                return;
            case 3:
                Kanojo kanojo = new Kanojo();
                if (mList.kanojo_id > 0) {
                    kanojo.setId(mList.kanojo_id);
                    startKanojoActivity(kanojo);
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void startCheckSession() {
    }

    protected void endCheckSession() {
    }
}
