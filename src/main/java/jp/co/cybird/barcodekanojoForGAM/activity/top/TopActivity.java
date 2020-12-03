package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.DashboardActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;

public class TopActivity extends BaseActivity {

    private static final String TAG = "TopActivity";
    private Button login_btn;
    private ProgressBar mProgressBar;
    private TopLogInTask mTopLogInTask;
    private TopSignUpTask mTopSignUpTask;
    private File modifiedPhoto;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        this.mProgressBar = findViewById(R.id.top_progressbar);
        this.mProgressBar.setVisibility(View.INVISIBLE);
        this.login_btn = findViewById(R.id.top_log_in);
        this.login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TopActivity.this.startLogin();
            }
        });
        this.signup_btn = findViewById(R.id.top_sign_up);
        this.signup_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TopActivity.this.startShowPrivacy();
            }
        });
    }

    @Override
    protected void onDestroy() {
        this.login_btn.setOnClickListener(null);
        this.signup_btn.setOnClickListener(null);
        super.onDestroy();
    }

    @Override
    protected View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_top, (ViewGroup) null);
        RelativeLayout appLayoutRoot = new RelativeLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.login_btn.setVisibility(View.VISIBLE);
        this.signup_btn.setVisibility(View.VISIBLE);
        BarcodeKanojoApp barcodeKanojoApp = (BarcodeKanojoApp) getApplication();
        barcodeKanojoApp.requestLocationUpdates(false);
        if (isSdCardWriteable()) {
            barcodeKanojoApp.loadResourceManagers();
        } else {
            showNoticeDialog(getResources().getString(R.string.error_external_storage), new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    TopActivity.this.close();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        ((BarcodeKanojoApp) getApplication()).removeLocationUpdates();
        if (this.mTopLogInTask != null) {
            this.mTopLogInTask.cancel(true);
            this.mTopLogInTask = null;
        }
        if (this.mTopSignUpTask != null) {
            this.mTopSignUpTask.cancel(true);
            this.mTopSignUpTask = null;
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 805 && resultCode == 106) {
            backupUser(((BarcodeKanojoApp) getApplication()).getUser());
            executeTopLogInTask();
        }
        if (requestCode == 809 && resultCode == 110) {
            startSignUp();
        }
        if (requestCode == 804 && resultCode == 105) {
            backupUser(((BarcodeKanojoApp) getApplication()).getUser());
            this.modifiedPhoto = (File) data.getExtras().get("photo");
            executeTopSignUpTask();
        }
        if (requestCode == 900 && resultCode == 107) {
            logout();
        }
    }

    private void showAlertDialog(Alert alert) {
        super.showAlertDialog(alert, new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });
    }

    private void startDashboard() {
        checkAndCopyUser();
        finish();
        startActivityForResult(new Intent().setClass(this, DashboardActivity.class), BaseInterface.REQUEST_DASHBOARD);
    }

    private void startShowPrivacy() {
        startActivityForResult(new Intent().setClass(this, PrivacyInfoActivity.class), BaseInterface.REQUEST_SHOW_PRIVACY);
    }

    private void startSignUp() {
        startActivityForResult(new Intent().setClass(this, UserEntryActivity.class), BaseInterface.REQUEST_SIGN_UP);
    }

    private void startLogin() {
        startActivityForResult(new Intent().setClass(this, LoginActivity.class), BaseInterface.REQUEST_LOG_IN);
    }

    private void executeTopLogInTask() {
        if (this.mTopLogInTask == null || this.mTopLogInTask.getStatus() == AsyncTask.Status.FINISHED || this.mTopLogInTask.cancel(true) || this.mTopLogInTask.isCancelled()) {
            this.mTopLogInTask = (TopLogInTask) new TopLogInTask().execute(new Void[0]);
        } else {
            Toast.makeText(this, "ttttttt", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        this.login_btn.setVisibility(View.VISIBLE);
        this.signup_btn.setVisibility(View.VISIBLE);
    }

    private void executeTopSignUpTask() {
        if (this.mTopSignUpTask == null || this.mTopSignUpTask.getStatus() == AsyncTask.Status.FINISHED || this.mTopSignUpTask.cancel(true) || this.mTopSignUpTask.isCancelled()) {
            this.mTopSignUpTask = (TopSignUpTask) new TopSignUpTask().execute(new Void[0]);
        } else {
            Toast.makeText(this, "ttttttt", Toast.LENGTH_SHORT).show();
        }
    }

    class TopLogInTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        TopLogInTask() {
        }

        @Override
        protected void onPreExecute() {
            TopActivity.this.mProgressBar.setVisibility(View.VISIBLE);
            TopActivity.this.login_btn.setVisibility(View.INVISIBLE);
            TopActivity.this.signup_btn.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Response<?> doInBackground(Void... params) {
            try {
                return login();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<?> response) {
            if (response == null) {
                try {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                } catch (BarcodeKanojoException e) {
                    if (this.mReason.getMessage().equals("user not found")) {
                        TopActivity.this.showAlertDialog(new Alert(TopActivity.this.getResources().getString(R.string.error_login)));
                    } else {
                        TopActivity.this.showAlertDialog(new Alert(TopActivity.this.getResources().getString(R.string.error_internet)));
                        TopActivity.this.close();
                    }
                    TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                    TopActivity.this.login_btn.setVisibility(View.VISIBLE);
                    TopActivity.this.signup_btn.setVisibility(View.VISIBLE);
                } catch (Throwable th) {
                    TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                    TopActivity.this.login_btn.setVisibility(View.VISIBLE);
                    TopActivity.this.signup_btn.setVisibility(View.VISIBLE);
                    throw th;
                }
            } else {
                switch (response.getCode()) {
                    case 200:
                        TopActivity.this.startDashboard();
                        break;
                    case 401:
                    case 403:
                    case 404:
                        TopActivity.this.startSignUp();
                        break;
                    default:
                        Alert alert = response.getAlert();
                        if (alert != null) {
                            TopActivity.this.showAlertDialog(alert);
                            break;
                        }
                        break;
                }
                TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                TopActivity.this.login_btn.setVisibility(View.VISIBLE);
                TopActivity.this.signup_btn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onCancelled() {
            TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
            TopActivity.this.login_btn.setVisibility(View.VISIBLE);
            TopActivity.this.signup_btn.setVisibility(View.VISIBLE);
        }

        Response<?> login() throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) TopActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            Response<BarcodeKanojoModel> iphone_verify = barcodeKanojo.verify(user.getEmail(), user.getPassword(), ((BarcodeKanojoApp) TopActivity.this.getApplication()).getUDID());
            barcodeKanojo.init_product_category_list();
            return iphone_verify;
        }
    }

    class TopSignUpTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        TopSignUpTask() {
        }

        @Override
        protected void onPreExecute() {
            TopActivity.this.mProgressBar.setVisibility(View.VISIBLE);
            TopActivity.this.login_btn.setVisibility(View.INVISIBLE);
            TopActivity.this.signup_btn.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Response<?> doInBackground(Void... params) {
            try {
                return signup();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            if (response == null) {
                try {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                } catch (BarcodeKanojoException e) {
                    TopActivity.this.showAlertDialog(new Alert(TopActivity.this.getResources().getString(R.string.error_internet)));
                    TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                } catch (Throwable th) {
                    TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                    throw th;
                }
            } else {
                switch (response.getCode()) {
                    case 200:
                        TopActivity.this.startDashboard();
                        break;
                    default:
                        Alert alert = response.getAlert();
                        if (alert != null) {
                            TopActivity.this.showAlertDialog(alert);
                            break;
                        }
                        break;
                }
                TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onCancelled() {
            TopActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
            TopActivity.this.login_btn.setVisibility(View.VISIBLE);
            TopActivity.this.signup_btn.setVisibility(View.VISIBLE);
        }

        Response<?> signup() throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) TopActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            Response<BarcodeKanojoModel> iphone_signup = barcodeKanojo.signup(user.getName(), user.getPassword(), user.getEmail(), user.getBirth_year(), user.getBirth_month(), user.getBirth_day(), user.getSex(), TopActivity.this.modifiedPhoto);
            barcodeKanojo.init_product_category_list();
            return iphone_signup;
        }
    }
}
