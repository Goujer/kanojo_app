package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import jp.co.cybird.android.lib.gcm.GCMUtilities;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.setting.UserModifyActivity;
import jp.co.cybird.barcodekanojoForGAM.billing.TestBillingActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.FaceBookProfile;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.twitter.TwitterDialog;
import jp.co.cybird.barcodekanojoForGAM.core.util.FaceBookUtil;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;
import twitter4j.auth.AccessToken;
import twitter4j.conf.PropertyConfiguration;

public class SignUpActivity extends BaseEditActivity implements View.OnClickListener, FaceBookUtil.OnFaceBoookListener, TwitterDialog.Listener {
    public static final boolean DEBUG = false;
    public static final String TAG = "SignUpActivity";
    private Button btnBack;
    EditItemView btnFaceBook;
    EditItemView btnTwitter;
    private EditItemView btnoldID;
    private AutoLoginTask mAutoLoginTask;
    /* access modifiers changed from: private */
    public boolean mBlockClick = false;
    /* access modifiers changed from: private */
    public FaceBookUtil mFb;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public CustomLoadingView mLoadingView;
    private int mRequestCode;
    /* access modifiers changed from: private */
    public ApplicationSetting mSetting;
    private LinearLayout mSkipLayout;
    private LinearLayout mSocialLayout;
    private LinearLayout mSukiyaLayout;
    final Handler mTaskEndHandler = new Handler() {
        public void handleMessage(Message msg) {
            StatusHolder next = (StatusHolder) SignUpActivity.this.getQueue().poll();
            if (next != null) {
                SignUpActivity.this.executeAutoLoginTask(next);
            }
        }
    };
    private Queue<StatusHolder> mTaskQueue;
    private boolean mTwitterOn = false;
    /* access modifiers changed from: private */
    public TwitterDialog mTwitterdialog;
    private File modifiedPhoto;
    /* access modifiers changed from: private */
    public User modifiedUser;
    private boolean oauthStarted;
    private Resources r;
    private TextView txtSettingTitle;
    private EditItemView txtSukiyaConfirmPassword;
    private EditItemView txtSukiyaEmail;
    private EditItemView txtSukiyaID;
    private EditItemView txtSukiyaPassword;

    public void onCreate(Bundle savedInstanceState) {
        this.mBaseLoadingFinished = true;
        super.onCreate(savedInstanceState);
        initlayout();
    }

    public void initlayout() {
        setContentView(R.layout.activity_sign_up);
        this.mSetting = new ApplicationSetting(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mRequestCode = bundle.getInt(BaseInterface.EXTRA_REQUEST_CODE);
        }
        if (this.mRequestCode == 1102) {
            setAutoRefreshSession(false);
            if (this.mSetting.getDeviceToken() == null || this.mSetting.getDeviceToken().length() <= 0) {
                GCMUtilities.runGCM(this);
            } else {
                executeRegisterPushTask();
            }
        }
        this.r = getResources();
        this.mHandler = new Handler();
        this.modifiedUser = new User();
        this.mFb = new FaceBookUtil(this, this);
        this.mFb.setListener(this);
        this.btnFaceBook = (EditItemView) findViewById(R.id.social_setting_facebook);
        this.btnFaceBook.setOnClickListener(this);
        this.mTwitterdialog = new TwitterDialog(this, "file:///android_asset/whitepage.html");
        this.mTwitterdialog.setOnDismissListener(this);
        this.btnoldID = (EditItemView) findViewById(R.id.social_setting_oldID);
        this.btnoldID.setOnClickListener(this);
        if (!this.mSetting.isSyncFaceBook() || this.mRequestCode != 1103) {
            this.btnFaceBook.hideIconAdd();
        } else {
            this.btnFaceBook.showIconAdd();
        }
        this.btnTwitter = (EditItemView) findViewById(R.id.social_setting_twitter);
        this.btnTwitter.setOnClickListener(this);
        if (!this.mSetting.isSyncTwitter() || this.mRequestCode != 1103) {
            this.mTwitterdialog.logout();
            this.btnTwitter.hideIconAdd();
        } else {
            this.btnTwitter.showIconAdd();
        }
        ((EditItemView) findViewById(R.id.social_setting_sukiya)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.social_setting_skip)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.social_setting_privacy_policy)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.social_setting_terms_of_use)).setOnClickListener(this);
        this.mSkipLayout = (LinearLayout) findViewById(R.id.social_setting_skip_config_layout);
        this.btnBack = (Button) findViewById(R.id.signup_option_close);
        this.btnBack.setOnClickListener(this);
        EditItemView editItemView = (EditItemView) findViewById(R.id.social_sukiya_id);
        this.mSocialLayout = (LinearLayout) findViewById(R.id.social_setting_layout);
        this.mSukiyaLayout = (LinearLayout) findViewById(R.id.social_setting_sukiya_layout);
        this.txtSettingTitle = (TextView) findViewById(R.id.social_setting_title);
        this.txtSukiyaID = (EditItemView) findViewById(R.id.social_sukiya_id);
        this.txtSukiyaID.setOnClickListener(this);
        this.txtSukiyaEmail = (EditItemView) findViewById(R.id.social_sukiya_email);
        this.txtSukiyaEmail.setOnClickListener(this);
        this.txtSukiyaPassword = (EditItemView) findViewById(R.id.social_sukiya_passord);
        this.txtSukiyaPassword.hideText();
        this.txtSukiyaPassword.setOnClickListener(this);
        this.txtSukiyaConfirmPassword = (EditItemView) findViewById(R.id.social_sukiya_confirm_password);
        this.txtSukiyaConfirmPassword.setOnClickListener(this);
        this.txtSukiyaConfirmPassword.hideText();
        switchLayout();
        this.mLoadingView = (CustomLoadingView) findViewById(R.id.loadingView);
        CookieSyncManager.createInstance(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
        if (!this.mTwitterOn) {
            this.mBlockClick = false;
        }
        if (!this.oauthStarted) {
            this.oauthStarted = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mAutoLoginTask != null) {
            this.mAutoLoginTask.cancel(true);
            this.mAutoLoginTask = null;
        }
        dismissProgressDialog();
        clearQueue();
        CookieSyncManager.getInstance().stopSync();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        this.mLoadingView.show();
        EditText input = new EditText(this);
        if (!this.mBlockClick) {
            this.mBlockClick = true;
            switch (v.getId()) {
                case R.id.signup_option_close:
                    setResult(BaseInterface.RESULT_CHANGED);
                    close();
                    return;
                case R.id.social_setting_facebook:
                    if (this.mRequestCode == 1102 || !this.mSetting.isSyncFaceBook()) {
                        this.btnFaceBook.hideIconAdd();
                        this.mSetting.clearFaceBookID();
                        this.mSetting.clearFaceBookToken();
                        this.mSetting.clearFaceBookLastUpdate();
                        this.mFb.clear();
                        this.mFb.login(true);
                        return;
                    } else if (this.mSetting.isSyncFaceBook()) {
                        showConfirmDisconnectDialog(String.format(getString(R.string.comfirm_disconnect_social), new Object[]{"FaceBook"}), true);
                        return;
                    } else {
                        this.mFb.login(false);
                        return;
                    }
                case R.id.social_setting_twitter:
                    if (this.mRequestCode == 1102 || !this.mSetting.isSyncTwitter()) {
                        this.btnTwitter.hideIconAdd();
                        this.mTwitterdialog.logout();
                        this.mSetting.commitSyncTwitter(false);
                        showTwitterDialog("", (DialogInterface.OnDismissListener) null);
                        this.mTwitterOn = true;
                        return;
                    } else if (this.mSetting.isSyncTwitter()) {
                        showConfirmDisconnectDialog(String.format(getString(R.string.comfirm_disconnect_social), new Object[]{"Twitter"}), false);
                        return;
                    } else {
                        showTwitterDialog("", (DialogInterface.OnDismissListener) null);
                        return;
                    }
                case R.id.social_setting_oldID:
                    Intent intent = new Intent().setClass(this, LoginActivity.class);
                    intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
                    startActivityForResult(intent, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
                    return;
                case R.id.social_setting_sukiya:
                    Intent intent2 = new Intent().setClass(this, SignUpActivity.class);
                    intent2.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_SUKIYA_SETTING);
                    startActivityForResult(intent2, BaseInterface.REQUEST_SOCIAL_SUKIYA_SETTING);
                    return;
                case R.id.social_setting_skip:
                    Intent dashboard = new Intent().setClass(this, UserModifyActivity.class);
                    dashboard.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
                    startActivityForResult(dashboard, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
                    return;
                case R.id.social_setting_privacy_policy:
                    showPrivacy();
                    return;
                case R.id.social_setting_terms_of_use:
                    showTerms();
                    return;
                case R.id.social_sukiya_id:
                    showEditTextDialog(this.r.getString(R.string.social_sukiya_id), this.txtSukiyaID);
                    return;
                case R.id.social_sukiya_email:
                    showEditTextDialog(this.r.getString(R.string.social_sukiya_email), this.txtSukiyaEmail);
                    return;
                case R.id.social_sukiya_passord:
                    input.setInputType(129);
                    showEditTextDialog(this.r.getString(R.string.social_sukiya_password), this.txtSukiyaPassword, input);
                    return;
                case R.id.social_sukiya_confirm_password:
                    input.setInputType(129);
                    showEditTextDialog(this.r.getString(R.string.social_sukiya_confirm_password), this.txtSukiyaConfirmPassword, input);
                    return;
                case R.id.social_sukiya_submit_btn:
                    Intent intent3 = new Intent().setClass(this, TestBillingActivity.class);
                    intent3.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_SUKIYA_SETTING);
                    startActivityForResult(intent3, BaseInterface.REQUEST_SOCIAL_SUKIYA_SETTING);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 108) {
            if (requestCode == 1102 || requestCode == 1103) {
                startDashboard();
            }
        } else if (resultCode == -100) {
            FaceBookUtil.mFb.authorizeCallback(requestCode, resultCode, data);
        } else if (requestCode == 1102 && resultCode == 106) {
            User user = ((BarcodeKanojoApp) getApplication()).getUser();
            executeLoginWithOldIDListTask(user.getEmail(), user.getPassword());
        } else {
            dismissProgressDialog();
            this.btnFaceBook.hideIconAdd();
            this.mSetting.clearFaceBookID();
            this.mSetting.clearFaceBookToken();
            this.mSetting.clearFaceBookLastUpdate();
            this.mFb.clear();
            this.btnTwitter.hideIconAdd();
            this.mTwitterdialog.logout();
        }
    }

    public void onDismiss(DialogInterface dialog, int code) {
        super.onDismiss(dialog, code);
        switch (code) {
            case 200:
                if (isQueueEmpty()) {
                }
                dismissProgressDialog();
                break;
        }
        this.mBlockClick = false;
        this.mLoadingView.dismiss();
    }

    /* access modifiers changed from: protected */
    public void exectuteInspectionAndSignUpTask(String txtName) {
        HashMap<String, String> param = new HashMap<>();
        param.put(GreeDefs.USER_NAME, txtName);
        inspectionAndUpdateByAction(param, 0, (HashMap<String, Object>) null);
    }

    public void switchLayout() {
        switch (this.mRequestCode) {
            case BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST:
                this.mSocialLayout.setVisibility(View.VISIBLE);
                this.mSkipLayout.setVisibility(View.VISIBLE);
                this.mSukiyaLayout.setVisibility(View.GONE);
                this.btnBack.setVisibility(View.INVISIBLE);
                this.btnoldID.setVisibility(View.VISIBLE);
                this.txtSettingTitle.setText(getResources().getString(R.string.social_setting_singup));
                this.btnFaceBook.setKey(getString(R.string.social_setting_facebook));
                this.btnTwitter.setKey(getString(R.string.social_setting_twister));
                this.btnTwitter.setBackgroundResource(R.drawable.row_kanojo_edit_bg_middle);
                return;
            case BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING:
                this.mSocialLayout.setVisibility(View.VISIBLE);
                this.mSkipLayout.setVisibility(View.GONE);
                this.mSukiyaLayout.setVisibility(View.GONE);
                this.btnBack.setVisibility(View.VISIBLE);
                this.txtSettingTitle.setText(getResources().getString(R.string.social_setting_common_setting));
                this.btnFaceBook.setKey(getString(R.string.common_setting_facebook));
                this.btnTwitter.setKey(getString(R.string.common_setting_twister));
                this.btnoldID.setVisibility(View.GONE);
                this.btnTwitter.setBackgroundResource(R.drawable.row_kanojo_edit_bg_bottom);
                return;
            case BaseInterface.REQUEST_SOCIAL_SUKIYA_SETTING:
                this.mSocialLayout.setVisibility(View.GONE);
                this.mSkipLayout.setVisibility(View.GONE);
                this.mSukiyaLayout.setVisibility(View.VISIBLE);
                this.btnBack.setVisibility(View.VISIBLE);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void fixUser() {
        checkAndCopyUser();
        showNoticeDialog(getString(R.string.edit_account_update_done));
    }

    /* access modifiers changed from: private */
    public void showAlertDialog(Alert alert) {
        super.showAlertDialog(alert, new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.mLoadingView.isShow()) {
                this.mLoadingView.setMessage(getString(R.string.requesting_cant_cancel));
                return true;
            } else if (!(this.mAutoLoginTask == null || this.mAutoLoginTask.getStatus() == AsyncTask.Status.FINISHED) || !getQueue().isEmpty()) {
                dismissProgressDialog();
                this.mAutoLoginTask.cancel(true);
                this.mAutoLoginTask = null;
                clearQueue();
                this.mBlockClick = false;
                return true;
            }
        }
        this.mBlockClick = false;
        return super.onKeyDown(keyCode, event);
    }

    private boolean isLoading(StatusHolder status) {
        if (status.loading) {
            return true;
        }
        return false;
    }

    static class StatusHolder {
        public static final int DISCONNECT_FACEBOOK_TASK = 3;
        public static final int DISCONNECT_TWITTER_TASK = 4;
        public static final int LOGIN_TASK = 6;
        public static final int REGISTER_DEVICE_TASK = 8;
        public static final int REGISTER_FB_TASK = 0;
        public static final int REGISTER_SUKIYA_TASK = 2;
        public static final int REGISTER_TWITTER_TASK = 1;
        public static final int SIGNUP_TASK = 7;
        public static final int UUID_VERIFY_TASK = 5;
        String email;
        int key;
        boolean loading = false;
        String password;

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

    /* access modifiers changed from: private */
    public synchronized void clearQueue() {
        getQueue().clear();
    }

    /* access modifiers changed from: private */
    public synchronized boolean isQueueEmpty() {
        return this.mTaskQueue.isEmpty();
    }

    private synchronized void executeRegisterPushTask() {
        clearQueue();
        StatusHolder mRegisterTokenHolder = new StatusHolder();
        mRegisterTokenHolder.key = 8;
        getQueue().offer(mRegisterTokenHolder);
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    private synchronized void executeLoginWithOldIDListTask(String email, String password) {
        clearQueue();
        StatusHolder mVerifyHolder = new StatusHolder();
        mVerifyHolder.key = 5;
        mVerifyHolder.email = email;
        mVerifyHolder.password = password;
        StatusHolder mLoginHolder = new StatusHolder();
        mLoginHolder.key = 6;
        StatusHolder mSignUpHolder = new StatusHolder();
        mSignUpHolder.key = 7;
        mSignUpHolder.email = email;
        mSignUpHolder.password = password;
        getQueue().offer(mVerifyHolder);
        getQueue().offer(mLoginHolder);
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    private synchronized void executeAutoLoginListTask(boolean fbFlag) {
        clearQueue();
        StatusHolder mFbHolder = new StatusHolder();
        mFbHolder.key = 0;
        StatusHolder mTwitterHolder = new StatusHolder();
        mTwitterHolder.key = 1;
        if (fbFlag) {
            getQueue().offer(mFbHolder);
        } else {
            getQueue().offer(mTwitterHolder);
        }
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: private */
    public synchronized void executeDisconnectListTask(boolean fbFlag) {
        clearQueue();
        StatusHolder mFbHolder = new StatusHolder();
        mFbHolder.key = 3;
        StatusHolder mTwitterHolder = new StatusHolder();
        mTwitterHolder.key = 4;
        if (fbFlag) {
            getQueue().offer(mFbHolder);
        } else {
            getQueue().offer(mTwitterHolder);
        }
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: private */
    public void executeAutoLoginTask(StatusHolder list) {
        if (isLoading(list)) {
            Log.d("NguyenTT", "task " + list.key + " is running ");
            return;
        }
        this.mAutoLoginTask = new AutoLoginTask();
        this.mAutoLoginTask.setList(list);
        showProgressDialog();
        this.mAutoLoginTask.execute(new Void[0]);
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

        public void onPostExecute(Response<?> response) {
            int code;
            try {
                if (this.mReason != null) {
                }
                if (response == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
                if (SignUpActivity.this.isQueueEmpty()) {
                    code = SignUpActivity.this.getCodeAndShowAlert(response, this.mReason);
                    if (this.mList.key == 3) {
                        SignUpActivity.this.btnFaceBook.hideIconAdd();
                        SignUpActivity.this.mSetting.commitSyncFaceBook(false);
                        SignUpActivity.this.mFb.login(false);
                    } else if (this.mList.key == 4) {
                        SignUpActivity.this.btnTwitter.hideIconAdd();
                        SignUpActivity.this.mTwitterdialog.logout();
                        SignUpActivity.this.mSetting.commitSyncTwitter(false);
                        SignUpActivity.this.modifiedUser.setTwitter_connect(false);
                    } else if (this.mList.key == 0) {
                        SignUpActivity.this.mSetting.commitSyncFaceBook(true);
                        SignUpActivity.this.btnFaceBook.showIconAdd();
                        SignUpActivity.this.modifiedUser.setFacebook_connect(true);
                    } else if (this.mList.key == 1) {
                        SignUpActivity.this.mSetting.commitSyncTwitter(true);
                        SignUpActivity.this.btnTwitter.showIconAdd();
                        SignUpActivity.this.modifiedUser.setTwitter_connect(true);
                    } else if (this.mList.key == 8) {
                        SignUpActivity.this.dismissProgressDialog();
                    }
                } else {
                    code = response.getCode();
                }
                switch (code) {
                    case 200:
                        if (!SignUpActivity.this.isQueueEmpty()) {
                            SignUpActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            return;
                        } else if (this.mList.key == 6) {
                            SignUpActivity.this.startDashboard();
                            SignUpActivity.this.dismissProgressDialog();
                            return;
                        } else {
                            return;
                        }
                    case 400:
                        if (this.mList.key == 5) {
                            SignUpActivity.this.dismissProgressDialog();
                            SignUpActivity.this.clearQueue();
                            SignUpActivity.this.showAlertDialog(response.getAlert());
                            return;
                        }
                        return;
                    case 401:
                    case 404:
                        return;
                    case 403:
                        if (this.mList.key == 5) {
                            SignUpActivity.this.showAlertDialog(response.getAlert(), SignUpActivity.this);
                            SignUpActivity.this.clearQueue();
                            ((BarcodeKanojoApp) SignUpActivity.this.getApplication()).getBarcodeKanojo().resetUser();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } catch (BarcodeKanojoException e) {
                SignUpActivity.this.dismissProgressDialog();
                SignUpActivity.this.clearQueue();
                SignUpActivity.this.showNoticeDialog(SignUpActivity.this.getResources().getString(R.string.slow_network));
                SignUpActivity.this.mBlockClick = false;
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            SignUpActivity.this.dismissProgressDialog();
            SignUpActivity.this.mBlockClick = false;
        }

        Response<?> process(StatusHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) SignUpActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            if (list == null) {
                throw new BarcodeKanojoException("process:StatusHolder is null!");
            }
            switch (list.key) {
                case 0:
                    return barcodeKanojo.android_register_fb(SignUpActivity.this.mSetting.getFaceBookID(), SignUpActivity.this.mSetting.getFaceBookToken());
                case 1:
                    return barcodeKanojo.android_register_twitter(SignUpActivity.this.mSetting.getTwitterAccessToken(), SignUpActivity.this.mSetting.getTwitterAccessTokenSecret());
                case 3:
                    return barcodeKanojo.android_disconnect_fb();
                case 4:
                    return barcodeKanojo.android_disconnect_twitter();
                case 5:
                    return barcodeKanojo.android_uuid_verify(list.email, list.password, SignUpActivity.this.mSetting.getUUID());
                case 6:
                    return barcodeKanojo.android_verify(SignUpActivity.this.mSetting.getUUID());
                case 7:
                    Response<BarcodeKanojoModel> iphone_signup = barcodeKanojo.iphone_signup("test", list.password, list.email, 5, 5, (String) null, (String) null, (File) null, (String) null);
                    break;
                case 8:
                    break;
                default:
                    return null;
            }
            return barcodeKanojo.android_register_device(SignUpActivity.this.mSetting.getUUID(), SignUpActivity.this.mSetting.getDeviceToken());
        }
    }

    /* access modifiers changed from: protected */
    public void startDashboard() {
        finish();
        startActivity(new Intent().setClass(this, KanojosActivity.class));
    }

    /* access modifiers changed from: protected */
    public void showTwitterDialog(String message, DialogInterface.OnDismissListener listener) {
        if (this.mTwitterdialog == null) {
            this.mTwitterdialog = new TwitterDialog(this, "http://google.com");
        }
        this.mTwitterdialog.show();
        this.mTwitterdialog.start(this);
    }

    public void onSuccess(TwitterDialog view, AccessToken accessToken) {
        Log.d("NguyenTT", "Twitter token: " + accessToken.getToken());
        Log.d("NguyenTT", "Twitter token secret: " + accessToken.getTokenSecret());
        this.mSetting.commitTwitterAccessToken(accessToken.getToken());
        this.mSetting.commitTwitterAccessTokenSecret(accessToken.getTokenSecret());
        twitter4j.User user = view.getuserProfile(accessToken);
        this.modifiedUser = new User();
        this.modifiedUser.setSex((String) null);
        if (this.mRequestCode == 1102) {
            this.mSetting.commitSyncTwitter(true);
            this.modifiedUser.setTwitter_connect(true);
            gotoNextStep();
            return;
        }
        executeAutoLoginListTask(false);
    }

    public void onFailure(TwitterDialog view, TwitterDialog.Result result) {
        Log.d("NguyenTT", "Register Twitter fail: " + result.name());
        if (!(result == TwitterDialog.Result.AUTHORIZATION_ERROR || result == TwitterDialog.Result.CANCELLATION)) {
            showNoticeDialog(getString(R.string.slow_network));
        }
        this.mSetting.commitSyncTwitter(false);
        this.btnTwitter.hideIconAdd();
        this.modifiedUser.setTwitter_connect(false);
        this.mTwitterOn = false;
    }

    public void onLoginSuccess() {
        showProgressDialog();
    }

    public void onGetProfileSuccess(FaceBookProfile fbHodler) {
        this.modifiedUser.setSex(fbHodler.getGender());
        this.modifiedUser.setBirthFromText(fbHodler.getBirthday().replace("/", "."));
        if (this.mRequestCode == 1102) {
            this.mSetting.commitSyncFaceBook(true);
            this.modifiedUser.setFacebook_connect(true);
            gotoNextStep();
            return;
        }
        executeAutoLoginListTask(true);
    }

    public void onGetProfileFail(FaceBookProfile fbHodler) {
        FaceBookProfile faceBookProfile = fbHodler;
        dismissProgressDialog();
        this.modifiedUser = new User();
        this.mHandler.post(new Runnable() {
            public void run() {
                SignUpActivity.this.showNoticeDialog(SignUpActivity.this.getString(R.string.slow_network));
                SignUpActivity.this.mSetting.commitSyncFaceBook(false);
                SignUpActivity.this.btnFaceBook.hideIconAdd();
                SignUpActivity.this.modifiedUser.setFacebook_connect(false);
            }
        });
    }

    public void onFail(String message, int errorCode, String errorType) {
        dismissProgressDialog();
        this.mHandler.post(new Runnable() {
            public void run() {
                SignUpActivity.this.showNoticeDialog(SignUpActivity.this.getString(R.string.slow_network));
            }
        });
    }

    public void onLogoutSuccess() {
        Log.d("NguyenTT", "logout successful");
        this.mSetting.commitSyncFaceBook(false);
        if (this.mRequestCode != 1102) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SignUpActivity.this.btnFaceBook.hideIconAdd();
                }
            });
        } else {
            this.mBlockClick = false;
        }
    }

    public void onCancel(String message) {
        this.mBlockClick = false;
        this.mLoadingView.dismiss();
    }

    public void gotoNextStep() {
        if (this.mRequestCode == 1102) {
            Intent intent = new Intent(this, UserModifyActivity.class);
            intent.putExtra("user", this.modifiedUser);
            intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
            startActivityForResult(intent, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
        }
    }

    /* access modifiers changed from: protected */
    public void showConfirmDisconnectDialog(String message, final boolean isFaceBook) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(message).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (isFaceBook) {
                    SignUpActivity.this.executeDisconnectListTask(true);
                } else {
                    SignUpActivity.this.executeDisconnectListTask(false);
                }
            }
        }).setNegativeButton(R.string.common_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SignUpActivity.this.mBlockClick = false;
                SignUpActivity.this.mLoadingView.dismiss();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                SignUpActivity.this.mBlockClick = false;
                SignUpActivity.this.mLoadingView.dismiss();
            }
        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public ProgressDialog showProgressDialog() {
        this.mLoadingView.show();
        return new ProgressDialog(this);
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        this.mLoadingView.dismiss();
    }

    /* access modifiers changed from: protected */
    public void startCheckSession() {
        if (this.mRequestCode != 1102) {
            super.startCheckSession();
            showProgressDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void endCheckSession() {
        dismissProgressDialog();
    }
}
