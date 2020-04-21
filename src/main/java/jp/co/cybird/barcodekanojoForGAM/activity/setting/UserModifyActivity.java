package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.gcm.GCMRegistrar;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import jp.co.cybird.app.android.lib.cybirdid.CybirdCommonUserId;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.top.SignUpActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;
import twitter4j.conf.PropertyConfiguration;

public class UserModifyActivity extends BaseEditActivity implements View.OnClickListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "UserModifyActivity";
    private BarcodeKanojoApp app;
    private Button btnClose;
    private Button btnDelete;
    /* access modifiers changed from: private */
    public Button btnSave;
    /* access modifiers changed from: private */
    public String currentPassword;
    private ImageView imgAvarta;
    /* access modifiers changed from: private */
    public AutoLoginTask mAutoLoginTask;
    private LinearLayout mChangeDeviceLayout;
    /* access modifiers changed from: private */
    public BaseActivity.OnDialogDismissListener mListener;
    private CustomLoadingView mLoadingView;
    /* access modifiers changed from: private */
    public int mRequestCode;
    private int mResultCode;
    private RemoteResourceManager mRrm;
    final Handler mTaskEndHandler = new Handler() {
        public void handleMessage(Message msg) {
            UserModifyActivity.this.executeAutoLoginTask((StatusHolder) UserModifyActivity.this.getQueue().poll());
        }
    };
    private Queue<StatusHolder> mTaskQueue;
    private EditItemView.EditItemViewCallback mTextChangeListener;
    /* access modifiers changed from: private */
    public File modifiedPhoto;
    /* access modifiers changed from: private */
    public User modifiedUser;
    private EditItemView txtBirthday;
    /* access modifiers changed from: private */
    public EditItemView txtEmail;
    private EditItemView txtGender;
    private EditItemView txtIcon;
    /* access modifiers changed from: private */
    public EditItemView txtName;
    private EditItemView txtPassword;
    private User user = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = savedInstanceState;
        requestWindowFeature(1);
        setContentView(R.layout.activity_user_modify);
        this.app = (BarcodeKanojoApp) getApplication();
        Bundle bundle2 = getIntent().getExtras();
        if (bundle2 != null) {
            this.mRequestCode = bundle2.getInt(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING);
            if (this.mRequestCode == 1102) {
                this.modifiedUser = (User) bundle2.getParcelable("user");
                this.user = this.modifiedUser;
                setAutoRefreshSession(false);
            }
        }
        if (savedInstanceState != null && this.user == null) {
            this.mRequestCode = savedInstanceState.getInt(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING);
            this.modifiedUser = (User) savedInstanceState.getParcelable("user");
            this.user = this.modifiedUser;
        }
        if (this.user == null) {
            this.user = this.app.getUser();
        }
        this.mRrm = this.app.getRemoteResourceManager();
        this.btnClose = (Button) findViewById(R.id.kanojo_user_modify_close);
        this.btnClose.setOnClickListener(this);
        this.btnSave = (Button) findViewById(R.id.kanojo_user_update_btn);
        this.btnSave.setOnClickListener(this);
        this.btnSave.setEnabled(true);
        if (this.mRequestCode == 1103) {
            this.btnSave.setText(R.string.edit_account_update_btn);
        } else if (this.mRequestCode == 1102) {
            this.btnSave.setText(R.string.user_register_btn);
        }
        this.txtName = (EditItemView) findViewById(R.id.kanojo_user_modify_name);
        this.txtName.setOnClickListener(this);
        this.txtName.setTextChangeListner(this.mTextChangeListener);
        if (this.user.getName() != null && this.user.getName() != "null") {
            this.txtName.setValue(this.user.getName());
        } else if (this.mRequestCode == 1102) {
            this.txtName.setHoverDescription(getString(R.string.blank_name_L012));
        } else {
            this.txtName.setHoverDescription(getString(R.string.blank_name));
        }
        this.txtPassword = (EditItemView) findViewById(R.id.kanojo_user_modify_password);
        this.txtPassword.setOnClickListener(this);
        this.txtPassword.setTextChangeListner(this.mTextChangeListener);
        this.txtPassword.hideText();
        this.txtEmail = (EditItemView) findViewById(R.id.kanojo_user_modify_email);
        this.txtEmail.setOnClickListener(this);
        this.txtEmail.setTextChangeListner(this.mTextChangeListener);
        this.txtEmail.setValue(this.user.getEmail());
        this.txtGender = (EditItemView) findViewById(R.id.kanojo_user_modify_gender);
        this.txtGender.setOnClickListener(this);
        this.txtGender.setTextChangeListner(this.mTextChangeListener);
        if (this.user.getSex() != null) {
            this.txtGender.setValue(this.user.getSexText(this.app.getUserGenderList()));
        }
        this.txtBirthday = (EditItemView) findViewById(R.id.kanojo_user_modify_birthday);
        this.txtBirthday.setOnClickListener(this);
        this.txtBirthday.setTextChangeListner(this.mTextChangeListener);
        this.txtBirthday.setValue(this.user.getBirthText());
        this.txtIcon = (EditItemView) findViewById(R.id.kanojo_user_modify_icon);
        this.txtIcon.setOnClickListener(this);
        this.btnDelete = (Button) findViewById(R.id.kanojo_user_delete_btn);
        this.btnDelete.setOnClickListener(this);
        this.imgAvarta = this.txtIcon.getAvarta();
        this.imgAvarta.setVisibility(View.VISIBLE);
        this.mChangeDeviceLayout = (LinearLayout) findViewById(R.id.kanojo_user_account_device_layout);
        if (this.user.getProfile_image_url() != null) {
            ImageCache.setImageAndRequest(this, this.imgAvarta, this.user.getProfile_image_url(), this.mRrm, R.drawable.common_noimage);
        }
        this.mListener = new BaseActivity.OnDialogDismissListener() {
            public void onDismiss(DialogInterface dialog, int code) {
                if (code == 200) {
                    UserModifyActivity.this.deleteUser();
                    UserModifyActivity.this.logout();
                    Intent signUp = new Intent().setClass(UserModifyActivity.this, SignUpActivity.class);
                    signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST);
                    UserModifyActivity.this.startActivity(signUp);
                }
            }
        };
        this.mTextChangeListener = new EditItemView.EditItemViewCallback() {
            public void onTextChange(View v, String value) {
                if (UserModifyActivity.this.isReadyForUpdate()) {
                    UserModifyActivity.this.btnSave.setEnabled(true);
                } else {
                    UserModifyActivity.this.btnSave.setEnabled(false);
                }
                if (UserModifyActivity.this.txtName.getValue().length() == 0 && UserModifyActivity.this.txtEmail.getValue().length() == 0 && UserModifyActivity.this.mRequestCode != 1102) {
                    UserModifyActivity.this.btnSave.setEnabled(false);
                } else {
                    UserModifyActivity.this.btnSave.setEnabled(true);
                }
            }
        };
        switchLayout();
        this.mLoadingView = (CustomLoadingView) findViewById(R.id.loadingView);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BaseInterface.EXTRA_REQUEST_CODE, this.mRequestCode);
        this.user.setName(this.txtName.getValue());
        this.user.setBirthFromText(this.txtBirthday.getValue());
        this.user.setSexFromText(this.txtGender.getValue());
        if (this.mRequestCode == 1102 && this.user.getProfile_image_url() != null) {
            this.user.setProfile_image_url(this.user.getProfile_image_url());
        } else if (getFile() != null) {
            this.user.setProfile_image_url(getFile().getAbsolutePath());
        }
        outState.putParcelable("user", this.user);
        super.onSaveInstanceState(outState);
    }

    public void onDismiss(DialogInterface dialog, int code) {
        super.onDismiss(dialog, code);
        switch (code) {
            case 200:
                updateAndClose();
                break;
            case 400:
                if (this.mRequestCode == 1102) {
                    updateAndClose();
                    break;
                }
                break;
        }
        bindEvent();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.btnClose.setOnClickListener((View.OnClickListener) null);
        this.txtName.setOnClickListener((View.OnClickListener) null);
        this.txtPassword.setOnClickListener((View.OnClickListener) null);
        this.txtEmail.setOnClickListener((View.OnClickListener) null);
        this.txtGender.setOnClickListener((View.OnClickListener) null);
        this.txtBirthday.setOnClickListener((View.OnClickListener) null);
        this.txtIcon.setOnClickListener((View.OnClickListener) null);
        this.btnSave.setOnClickListener((View.OnClickListener) null);
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        bindEvent();
    }

    public void bindEvent() {
        this.btnClose.setOnClickListener(this);
        this.txtName.setOnClickListener(this);
        this.txtPassword.setOnClickListener(this);
        this.txtEmail.setOnClickListener(this);
        this.txtGender.setOnClickListener(this);
        this.txtBirthday.setOnClickListener(this);
        this.txtIcon.setOnClickListener(this);
        this.btnSave.setOnClickListener(this);
        this.btnDelete.setOnClickListener(this);
    }

    public void unBindEvent() {
        this.btnClose.setOnClickListener((View.OnClickListener) null);
        this.txtName.setOnClickListener((View.OnClickListener) null);
        this.txtPassword.setOnClickListener((View.OnClickListener) null);
        this.txtEmail.setOnClickListener((View.OnClickListener) null);
        this.txtGender.setOnClickListener((View.OnClickListener) null);
        this.txtBirthday.setOnClickListener((View.OnClickListener) null);
        this.txtIcon.setOnClickListener((View.OnClickListener) null);
        this.btnSave.setOnClickListener((View.OnClickListener) null);
        this.btnDelete.setOnClickListener((View.OnClickListener) null);
    }

    public void onClick(View v) {
        unBindEvent();
        Log.d("Nguyen", "View Clicked: " + v.getId());
        switch (v.getId()) {
            case R.id.kanojo_user_modify_close:
                close();
                return;
            case R.id.kanojo_user_modify_name:
                showEditTextDialog(this.r.getString(R.string.user_account_name), this.txtName);
                return;
            case R.id.kanojo_user_modify_gender:
                showGenderDialog(this.r.getString(R.string.user_account_gender), this.txtGender);
                return;
            case R.id.kanojo_user_modify_birthday:
                showDatePickDialog(this.r.getString(R.string.user_account_birthday), this.txtBirthday);
                return;
            case R.id.kanojo_user_modify_icon:
                showImagePickerDialog(this.r.getString(R.string.user_account_icon));
                return;
            case R.id.kanojo_user_modify_email:
                showEditTextDialog(this.r.getString(R.string.user_account_email), this.txtEmail);
                return;
            case R.id.kanojo_user_modify_password:
                startPasswordChangeActivity();
                return;
            case R.id.kanojo_user_update_btn:
                this.mResultCode = BaseInterface.RESULT_MODIFIED;
                if (!this.txtName.getValue().equals("") || !this.txtGender.getValue().equals("") || !this.txtBirthday.getValue().equals("") || this.imgAvarta.getDrawable() != null) {
                    this.mResultCode = BaseInterface.RESULT_MODIFIED_COMMON;
                }
                if (!this.txtEmail.getValue().equals("")) {
                    if (this.txtPassword.getValue().equals("") && this.user.getEmail() == null) {
                        showNoticeDialog(this.r.getString(R.string.error_password_length));
                        return;
                    } else if (this.mResultCode == 210) {
                        this.mResultCode = BaseInterface.RESULT_MODIFIED_COMMON;
                    } else {
                        this.mResultCode = BaseInterface.RESULT_MODIFIED_DEVICE;
                    }
                } else if (this.user.getEmail() == null && !this.txtPassword.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_email));
                    return;
                }
                processData();
                return;
            case R.id.kanojo_user_delete_btn:
                this.mResultCode = BaseInterface.RESULT_DELETE_ACCOUNT;
                showConfirmDeleteDialog(getResources().getString(R.string.delete_account_warning_message));
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File f = getFile();
        if (f != null && f.exists()) {
            setBitmapFromFile(this.imgAvarta, f, 30, 30);
            this.btnSave.setEnabled(true);
        }
        if (requestCode == 807 && resultCode == 109) {
            this.txtPassword.setValue(data.getStringExtra("new_password"));
            this.currentPassword = data.getStringExtra("currentPassword");
        }
    }

    /* access modifiers changed from: protected */
    public void setBitmapFromFile(ImageView view, File file, int width, int height) {
        Bitmap setBitmap;
        Bitmap bitmap = loadBitmap(file, width, height);
        if (bitmap != null) {
            if (width > height) {
                Matrix aMatrix = new Matrix();
                aMatrix.setRotate(90.0f);
                setBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, aMatrix, false);
            } else {
                setBitmap = bitmap;
            }
            view.setImageBitmap(setBitmap);
        }
    }

    private void startPasswordChangeActivity() {
        Intent intent = new Intent().setClass(this, ChangePasswordActivity.class);
        if (this.user.getEmail() == null) {
            intent.putExtra("new_email", true);
            intent.putExtra("encodedCurrentPassword", "");
        } else {
            intent.putExtra("new_email", false);
            intent.putExtra("encodedCurrentPassword", this.user.getCurrentPassword());
        }
        startActivityForResult(intent, BaseInterface.REQUEST_CHANGE_PASWORD);
    }

    private void switchLayout() {
        switch (this.mRequestCode) {
            case BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST:
                this.mChangeDeviceLayout.setVisibility(View.GONE);
                this.btnDelete.setVisibility(View.GONE);
                this.btnSave.setEnabled(true);
                return;
            case BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING:
                this.mChangeDeviceLayout.setVisibility(View.GONE);
                this.btnDelete.setVisibility(View.VISIBLE);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void showConfirmDeleteDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(message).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UserModifyActivity.this.processData();
            }
        }).setNegativeButton(R.string.common_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UserModifyActivity.this.bindEvent();
                dialog.dismiss();
            }
        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                UserModifyActivity.this.bindEvent();
            }
        });
        dialog.show();
    }

    public void processData() {
        if (this.mResultCode == 111) {
            executeOptionDeleteTask();
        } else if (this.mResultCode == 212 || this.mResultCode == 210 || this.mResultCode == 211 || this.mResultCode == 108) {
            this.modifiedUser = new User();
            this.modifiedUser.setName(this.txtName.getValue());
            this.modifiedUser.setPassword(this.txtPassword.getValue());
            this.modifiedUser.setEmail(this.txtEmail.getValue().replaceAll(" ", ""));
            this.modifiedUser.setSexFromText(this.txtGender.getValue(), this.app.getUserGenderList());
            this.modifiedUser.setBirthFromText(this.txtBirthday.getValue());
            backupUser(this.modifiedUser);
            if (this.mRequestCode != 1102 || this.user.getProfile_image_url() == null) {
                this.modifiedPhoto = getFile();
            } else {
                this.modifiedPhoto = this.mRrm.getFile(Uri.parse(this.user.getProfile_image_url()));
            }
            executeAutoLoginListTask(this.user.isFacebook_connect());
        } else {
            updateAndClose();
        }
    }

    private void executeOptionDeleteTask() {
        StatusHolder mDeleteUserHolder = new StatusHolder();
        mDeleteUserHolder.key = 8;
        getQueue().offer(mDeleteUserHolder);
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: protected */
    public void updateAndClose() {
        setResult(BaseInterface.RESULT_MODIFIED);
        close();
    }

    /* access modifiers changed from: private */
    public void logout() {
        ((BarcodeKanojoApp) getApplication()).logged_out();
    }

    private boolean isLoading(StatusHolder status) {
        if (status.loading) {
            return true;
        }
        return false;
    }

    static class StatusHolder {
        public static final int DELETE_USER_TASK = 8;
        public static final int REGISTER_FB_TASK = 4;
        public static final int REGISTER_SUKIYA_TASK = 6;
        public static final int REGISTER_TOKEN_TASK = 3;
        public static final int REGISTER_TWITTER_TASK = 5;
        public static final int SAVING_COMMON_INFO_TASK = 1;
        public static final int SAVING_DEVICE_ACCOUNT_TASK = 2;
        public static final int SIGNUP_TASK = 0;
        public static final int UPDATE_TASK = 7;
        public static final int VERIFY_TASK = 9;
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

    /* access modifiers changed from: private */
    public synchronized void clearQueue() {
        getQueue().clear();
    }

    /* access modifiers changed from: private */
    public synchronized boolean isQueueEmpty() {
        return this.mTaskQueue.isEmpty();
    }

    /* access modifiers changed from: protected */
    public void startDashboard() {
        finish();
        startActivity(new Intent().setClass(this, KanojosActivity.class));
    }

    private synchronized void executeAutoLoginListTask(boolean fbFlag) {
        clearQueue();
        StatusHolder mSignUpHolder = new StatusHolder();
        mSignUpHolder.key = 0;
        StatusHolder mFbHolder = new StatusHolder();
        mFbHolder.key = 4;
        StatusHolder mTwitterHolder = new StatusHolder();
        mTwitterHolder.key = 5;
        new StatusHolder().key = 7;
        new StatusHolder().key = 3;
        StatusHolder mSaveCommonInfoHolder = new StatusHolder();
        mSaveCommonInfoHolder.key = 1;
        StatusHolder mSaveDeviceHolder = new StatusHolder();
        mSaveDeviceHolder.key = 2;
        new StatusHolder().key = 9;
        if (this.mRequestCode == 1102) {
            getQueue().offer(mSignUpHolder);
            if (this.user.isFacebook_connect()) {
                getQueue().offer(mFbHolder);
            } else if (this.user.isTwitter_connect()) {
                getQueue().offer(mTwitterHolder);
            }
        }
        if (this.mResultCode == 210 || this.mResultCode == 212) {
            getQueue().offer(mSaveCommonInfoHolder);
        }
        if (this.mResultCode == 211 || this.mResultCode == 212) {
            getQueue().offer(mSaveDeviceHolder);
        }
        if (!isQueueEmpty()) {
            this.mTaskEndHandler.sendEmptyMessage(0);
        } else {
            updateAndClose();
        }
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

        /* JADX INFO: finally extract failed */
        public void onPostExecute(Response<?> response) {
            int code;
            try {
                if (this.mReason != null) {
                }
                if (response == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
                if (!UserModifyActivity.this.isQueueEmpty()) {
                    code = response.getCode();
                } else if (this.mList.key == 8) {
                    code = UserModifyActivity.this.getCodeAndShowAlert(response, this.mReason, UserModifyActivity.this.mListener);
                } else {
                    code = UserModifyActivity.this.getCodeAndShowAlert(response, this.mReason);
                }
                switch (code) {
                    case 200:
                        if (!UserModifyActivity.this.isQueueEmpty()) {
                            UserModifyActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            break;
                        }
                        break;
                    case 400:
                    case 401:
                    case 403:
                    case 404:
                    case 500:
                    case 503:
                        UserModifyActivity.this.dismissProgressDialog();
                        UserModifyActivity.this.bindEvent();
                        UserModifyActivity.this.clearQueue();
                        break;
                }
                UserModifyActivity.this.dismissProgressDialog();
            } catch (BarcodeKanojoException e) {
                UserModifyActivity.this.dismissProgressDialog();
                UserModifyActivity.this.bindEvent();
                UserModifyActivity.this.clearQueue();
                if (UserModifyActivity.this.mAutoLoginTask != null) {
                    UserModifyActivity.this.mAutoLoginTask.cancel(true);
                    UserModifyActivity.this.mAutoLoginTask = null;
                }
                UserModifyActivity.this.showNoticeDialog(UserModifyActivity.this.getString(R.string.slow_network));
                UserModifyActivity.this.dismissProgressDialog();
            } catch (Throwable th) {
                UserModifyActivity.this.dismissProgressDialog();
                throw th;
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            UserModifyActivity.this.dismissProgressDialog();
            UserModifyActivity.this.bindEvent();
        }

        /* access modifiers changed from: package-private */
        public Response<?> process(StatusHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            String cPassword;
            String cPassword2;
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) UserModifyActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            ApplicationSetting setting = new ApplicationSetting(UserModifyActivity.this);
            if (list == null) {
                throw new BarcodeKanojoException("process:StatusHolder is null!");
            }
            String cPassword3 = UserModifyActivity.this.currentPassword;
            switch (list.key) {
                case 0:
                    String str = cPassword3;
                    return barcodeKanojo.android_signup(1990, 5, 20, Defs.DEBUG_SEX, ((BarcodeKanojoApp) UserModifyActivity.this.getApplication()).getUUID());
                case 1:
                    if (UserModifyActivity.this.modifiedUser.getPassword().equals("")) {
                        UserModifyActivity.this.modifiedUser.setPassword(user.getPassword());
                    }
                    if (UserModifyActivity.this.currentPassword == null) {
                        cPassword = user.getPassword();
                    } else {
                        cPassword = cPassword3;
                    }
                    return barcodeKanojo.android_update(UserModifyActivity.this.modifiedUser.getName(), cPassword, UserModifyActivity.this.modifiedUser.getPassword(), UserModifyActivity.this.modifiedUser.getEmail(), UserModifyActivity.this.modifiedUser.getBirth_month(), UserModifyActivity.this.modifiedUser.getBirth_day(), UserModifyActivity.this.modifiedUser.getBirth_year(), UserModifyActivity.this.modifiedUser.getSex(), UserModifyActivity.this.modifiedUser.getDescription(), UserModifyActivity.this.modifiedPhoto);
                case 2:
                    String str2 = cPassword3;
                    return barcodeKanojo.android_uuid_verify(UserModifyActivity.this.modifiedUser.getEmail(), UserModifyActivity.this.modifiedUser.getPassword(), ((BarcodeKanojoApp) UserModifyActivity.this.getApplication()).getUUID());
                case 3:
                    String str3 = cPassword3;
                    return barcodeKanojo.android_register_device(setting.getUUID(), GCMRegistrar.getRegistrationId(UserModifyActivity.this));
                case 4:
                    String str4 = cPassword3;
                    return barcodeKanojo.android_register_fb(setting.getFaceBookID(), setting.getFaceBookToken());
                case 5:
                    String str5 = cPassword3;
                    return barcodeKanojo.android_register_twitter(setting.getTwitterAccessToken(), setting.getTwitterAccessTokenSecret());
                case 6:
                    return null;
                case 7:
                    if (UserModifyActivity.this.currentPassword == null) {
                        cPassword2 = user.getPassword();
                    } else {
                        cPassword2 = cPassword3;
                    }
                    return barcodeKanojo.android_update(UserModifyActivity.this.modifiedUser.getName(), cPassword2, UserModifyActivity.this.modifiedUser.getPassword(), UserModifyActivity.this.modifiedUser.getEmail(), UserModifyActivity.this.modifiedUser.getBirth_month(), UserModifyActivity.this.modifiedUser.getBirth_day(), UserModifyActivity.this.modifiedUser.getBirth_year(), UserModifyActivity.this.modifiedUser.getSex(), UserModifyActivity.this.modifiedUser.getDescription(), UserModifyActivity.this.modifiedPhoto);
                case 8:
                    String str6 = cPassword3;
                    return barcodeKanojo.android_delete_account(user.getId());
                case 9:
                    String str7 = cPassword3;
                    return barcodeKanojo.android_verify(CybirdCommonUserId.get(UserModifyActivity.this.getApplicationContext()));
                default:
                    String str8 = cPassword3;
                    return null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void nextScreen(StatusHolder list) {
        setResult(BaseInterface.RESULT_MODIFIED);
        close();
        dismissProgressDialog();
    }

    public boolean isReadyForUpdate() {
        int mCount = 0;
        if (this.txtName.getValue() != "" && this.txtName.getValue().equalsIgnoreCase(this.user.getName())) {
            mCount = 0 + 1;
        }
        if (this.txtGender.getValue() != "" && this.txtGender.getValue().equalsIgnoreCase(this.user.getSex())) {
            mCount++;
        }
        if (this.txtBirthday.getValue() != "" && this.txtBirthday.getValue().equalsIgnoreCase(this.user.getBirthText())) {
            mCount++;
        }
        if (this.imgAvarta.getDrawable() != null) {
            mCount++;
        }
        if (this.txtEmail.getValue() != "" && this.txtEmail.getValue().equalsIgnoreCase(this.user.getEmail()) && this.txtPassword.getValue() != "" && this.txtPassword.getValue().equalsIgnoreCase(this.user.getPassword())) {
            mCount++;
        }
        if (this.mRequestCode == 1102) {
            mCount++;
        }
        return mCount > 0;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mLoadingView.isShow()) {
            return super.onKeyDown(keyCode, event);
        }
        this.mLoadingView.setMessage(getString(R.string.requesting_cant_cancel));
        return true;
    }

    public ProgressDialog showProgressDialog() {
        this.mLoadingView.show();
        return new ProgressDialog(this);
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        if (this.mLoadingView != null) {
            this.mLoadingView.dismiss();
        }
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
