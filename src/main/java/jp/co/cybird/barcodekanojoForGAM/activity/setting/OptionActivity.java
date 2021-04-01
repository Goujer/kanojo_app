package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.goujer.barcodekanojo.activity.setting.ServerConfigurationActivity;
import com.goujer.barcodekanojo.activity.setting.UserModifyActivity;
import com.goujer.barcodekanojo.activity.top.LaunchActivity;

import java.io.File;
import java.io.IOException;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;

public class OptionActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OptionActivity";
    private View server_btn;
    private EditItemView account_btn;
    private EditItemView bck_btn;
    private EditItemView kddi_btn;
    private LinearLayout mDashboard;
    private LinearLayout mKanojos;
    private BaseActivity.OnDialogDismissListener mListener;
    private OptionChangeDeviceTask mOptionChangeDeviceTask;
    private OptionDeleteTask mOptionDeleteTask;
    private OptionModifyTask mOptionModifyTask;
    private LinearLayout mScan;
    private LinearLayout mSetting;
    private LinearLayout mWebView;
    private EditItemView mail_btn;
    private File modifiedPhoto;
    private User modifiedUser;
    private EditItemView privacy_btn;
    private EditItemView rules_btn;
    private EditItemView team_btn;
    private EditItemView terms_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_option);
        this.server_btn = findViewById(R.id.kanojo_option_server_config);
        this.server_btn.setOnClickListener(this);
        this.account_btn = findViewById(R.id.kanojo_option_account_modify);
        this.account_btn.setOnClickListener(this);
        this.privacy_btn = findViewById(R.id.kanojo_option_privacy);
        this.privacy_btn.setOnClickListener(this);
        this.terms_btn = findViewById(R.id.kanojo_option_terms);
        this.terms_btn.setOnClickListener(this);
        this.rules_btn = findViewById(R.id.kanojo_option_rules);
        this.rules_btn.setOnClickListener(this);
        this.bck_btn = findViewById(R.id.kanojo_option_barcodekanojo);
        this.bck_btn.setOnClickListener(this);
        this.team_btn = findViewById(R.id.kanojo_option_team);
        this.team_btn.setOnClickListener(this);
        this.mail_btn = findViewById(R.id.kanojo_option_mail);
        this.mail_btn.setOnClickListener(this);
        this.kddi_btn = findViewById(R.id.kanojo_option_kddi);
        this.kddi_btn.setOnClickListener(this);
        this.mListener = new BaseActivity.OnDialogDismissListener() {
            public void onDismiss(DialogInterface dialog, int code) {
                OptionActivity.this.logout();
                OptionActivity.this.startActivity(new Intent().setClass(OptionActivity.this, LaunchActivity.class));
            }
        };
    }

    protected void onDestroy() {
		unBindEvent();
        super.onDestroy();
    }

    public void unBindEvent() {
		server_btn.setOnClickListener(null);
        this.account_btn.setOnClickListener(null);
        this.privacy_btn.setOnClickListener(null);
        this.terms_btn.setOnClickListener(null);
        this.rules_btn.setOnClickListener(null);
        this.bck_btn.setOnClickListener(null);
        this.team_btn.setOnClickListener(null);
        this.mail_btn.setOnClickListener(null);
        this.kddi_btn.setOnClickListener(null);
    }

    public void bindEvent() {
		server_btn.setOnClickListener(this);
        this.account_btn.setOnClickListener(this);
        this.privacy_btn.setOnClickListener(this);
        this.terms_btn.setOnClickListener(this);
        this.rules_btn.setOnClickListener(this);
        this.bck_btn.setOnClickListener(this);
        this.team_btn.setOnClickListener(this);
        this.mail_btn.setOnClickListener(this);
        this.kddi_btn.setOnClickListener(this);
    }

    public void onClick(View v) {
        unBindEvent();
		int id = v.getId();
		if (id == R.id.kanojo_option_server_config) {
			startServerConfig();
		} else if (id == R.id.kanojo_option_account_modify) {
			startAccountModify();
		} else if (id == R.id.kanojo_option_privacy) {
			showPrivacy();
		} else if (id == R.id.kanojo_option_terms) {
			showTerms();
		} else if (id == R.id.kanojo_option_rules) {
			showRules();
		} else if (id == R.id.kanojo_option_barcodekanojo) {
			showBarcodeKanojo();
		} else if (id == R.id.kanojo_option_team) {
			showTeam();
		} else if (id == R.id.kanojo_option_mail) {
			showMail();
		} else if (id == R.id.kanojo_option_kddi) {
			showKDDI();
		}
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bindEvent();
    }

    protected void fixUser() {
        checkAndCopyUser();
        showNoticeDialog(getString(R.string.edit_account_update_done));
    }

    private void startServerConfig() {
        startActivity(new Intent(this, ServerConfigurationActivity.class));
    }

    private void startAccountModify() {
        Intent intent = new Intent().setClass(this, UserModifyActivity.class);
        intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING);
        startActivityForResult(intent, BaseInterface.REQUEST_MODIFY_USER);
    }

    private void logout() {
        ((BarcodeKanojoApp) getApplication()).logged_out();
    }

    private void showAlertDialog(Alert alert) {
        super.showAlertDialog(alert, new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });
    }

    //private void executeOptionModifyTask() {
    //    if (this.mOptionModifyTask == null || this.mOptionModifyTask.getStatus() == AsyncTask.Status.FINISHED || this.mOptionModifyTask.cancel(true) || this.mOptionModifyTask.isCancelled()) {
    //        this.mOptionModifyTask = (OptionModifyTask) new OptionModifyTask().execute(new Void[0]);
    //    } else {
    //        Toast.makeText(this, "ttttttt", Toast.LENGTH_SHORT);
    //    }
    //}

    class OptionModifyTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        OptionModifyTask() {
        }

        public void onPreExecute() {
            ProgressDialog unused = OptionActivity.this.showProgressDialog();
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return modify_user();
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
                    OptionActivity.this.showAlertDialog(new Alert(OptionActivity.this.getResources().getString(R.string.error_internet)));
                    OptionActivity.this.dismissProgressDialog();
                } catch (Throwable th) {
                    OptionActivity.this.dismissProgressDialog();
                    throw th;
                }
            } else {
                switch (response.getCode()) {
                    case 200:
                        OptionActivity.this.executeOptionChangeDeviceTask();
                        break;
                    default:
                        Alert alert = response.getAlert();
                        if (alert != null) {
                            OptionActivity.this.showAlertDialog(alert);
                            break;
                        }
                        break;
                }
                OptionActivity.this.dismissProgressDialog();
            }
        }

        @Override
        protected void onCancelled() {
        }

        Response<?> modify_user() throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) OptionActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            if (OptionActivity.this.modifiedUser.getPassword().length == 0) {
                OptionActivity.this.modifiedUser.setPassword(user.getPassword());
            }
            Response<BarcodeKanojoModel> android_update = barcodeKanojo.update(OptionActivity.this.modifiedUser.getName(), user.getPassword(), OptionActivity.this.modifiedUser.getPassword(), OptionActivity.this.modifiedUser.getEmail(), OptionActivity.this.modifiedUser.getBirth_year(), OptionActivity.this.modifiedUser.getBirth_month(), OptionActivity.this.modifiedUser.getBirth_day(), OptionActivity.this.modifiedUser.getSex(), OptionActivity.this.modifiedPhoto);
            barcodeKanojo.init_product_category_list();
            User user2 = barcodeKanojo.getUser();
			((BarcodeKanojoApp) OptionActivity.this.getApplication()).getImageCache().requestBitmap(user2.getProfile_image_url());
            return android_update;
        }
    }

    //private void executeOptionDeleteTask() {
    //    if (this.mOptionDeleteTask == null || this.mOptionDeleteTask.getStatus() == AsyncTask.Status.FINISHED || this.mOptionDeleteTask.cancel(true) || this.mOptionDeleteTask.isCancelled()) {
    //        this.mOptionDeleteTask = (OptionDeleteTask) new OptionDeleteTask().execute(new Void[0]);
    //    }
    //}

    class OptionDeleteTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        OptionDeleteTask() {
        }

        public void onPreExecute() {
            ProgressDialog unused = OptionActivity.this.showProgressDialog();
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return delete_user();
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
                    OptionActivity.this.showAlertDialog(new Alert(OptionActivity.this.getResources().getString(R.string.error_internet)));
                    OptionActivity.this.dismissProgressDialog();
                } catch (Throwable th) {
                    OptionActivity.this.dismissProgressDialog();
                    throw th;
                }
            } else {
            	try {
					switch (OptionActivity.this.getCodeAndShowAlert(response, this.mReason, OptionActivity.this.mListener)) {
						case 200:
							break;
						default:
							Alert alert = response.getAlert();
							if (alert != null) {
								OptionActivity.this.showAlertDialog(alert);
								break;
							}
							break;
					}
					OptionActivity.this.dismissProgressDialog();
				} catch(BarcodeKanojoException e) {
            		e.printStackTrace();
				}
            }
        }

        protected void onCancelled() {
        }

       Response<?> delete_user() throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) OptionActivity.this.getApplication()).getBarcodeKanojo();
            return barcodeKanojo.android_delete_account(barcodeKanojo.getUser().getId());
        }
    }

    @Override
    protected void deleteUser() {
        showNoticeDialog(getString(R.string.delete_account_message));
    }

    private void executeOptionChangeDeviceTask() {
        if (this.mOptionChangeDeviceTask == null || this.mOptionChangeDeviceTask.getStatus() == AsyncTask.Status.FINISHED || this.mOptionChangeDeviceTask.cancel(true) || this.mOptionChangeDeviceTask.isCancelled()) {
            this.mOptionChangeDeviceTask = (OptionChangeDeviceTask) new OptionChangeDeviceTask().execute(new Void[0]);
        }
    }

    class OptionChangeDeviceTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        OptionChangeDeviceTask() {
        }

        @Override
        public void onPreExecute() {
            ProgressDialog unused = OptionActivity.this.showProgressDialog();
        }

        @Override
        public Response<?> doInBackground(Void... params) {
            try {
                return changeDevice();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        public void onPostExecute(Response<?> response) {
            if (response == null) {
                try {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                } catch (BarcodeKanojoException e) {
                    OptionActivity.this.showAlertDialog(new Alert(OptionActivity.this.getResources().getString(R.string.error_internet)));
                    OptionActivity.this.dismissProgressDialog();
                } catch (Throwable th) {
                    OptionActivity.this.dismissProgressDialog();
                    throw th;
                }
            } else {
                switch (response.getCode()) {
                    case 200:
                        OptionActivity.this.fixUser();
                        break;
                    default:
                        Alert alert = response.getAlert();
                        if (alert != null) {
                            OptionActivity.this.showAlertDialog(alert);
                            break;
                        }
                        break;
                }
                OptionActivity.this.dismissProgressDialog();
            }
        }

        @Override
        protected void onCancelled() {
        }

        Response<?> changeDevice() throws BarcodeKanojoException, IllegalStateException, IOException {
            return ((BarcodeKanojoApp) OptionActivity.this.getApplication()).getBarcodeKanojo().android_uuid_verify(OptionActivity.this.modifiedUser.getEmail(), OptionActivity.this.modifiedUser.getPassword(), ((BarcodeKanojoApp) OptionActivity.this.getApplication()).getUUID());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }
}
