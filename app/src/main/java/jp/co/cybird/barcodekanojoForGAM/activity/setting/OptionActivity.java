package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.goujer.barcodekanojo.activity.setting.ServerConfigurationActivity;
import com.goujer.barcodekanojo.activity.setting.UserModifyActivity;
import com.goujer.barcodekanojo.activity.top.LaunchActivity;
import com.goujer.barcodekanojo.core.http.HttpApi;

import java.io.File;
import java.io.IOException;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import com.goujer.barcodekanojo.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import com.goujer.barcodekanojo.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;

public class OptionActivity extends BaseActivity implements View.OnClickListener {
    private EditItemView account_btn;
    private EditItemView bck_btn;
    private EditItemView kddi_btn;
    private LinearLayout mDashboard;
    private LinearLayout mKanojos;
    private BaseActivity.OnDialogDismissListener mListener;
    //private OptionChangeDeviceTask mOptionChangeDeviceTask;
    private OptionDeleteTask mOptionDeleteTask;
    //private OptionModifyTask mOptionModifyTask;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_option);
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
        this.mListener = (dialog, code) -> {
			OptionActivity.this.logout();
			OptionActivity.this.startActivity(new Intent().setClass(OptionActivity.this, LaunchActivity.class));
		};
    }

    protected void onDestroy() {
		unBindEvent();
        super.onDestroy();
    }

    public void unBindEvent() {
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
		if (id == R.id.kanojo_option_account_modify) {
			startAccountModify();
		} else if (id == R.id.kanojo_option_privacy) {
			startWebViewActivity(Defs.URL_LEGAL_PRIVACY);
		} else if (id == R.id.kanojo_option_terms) {
			startWebViewActivity(Defs.URL_LEGAL_TERMS);
		} else if (id == R.id.kanojo_option_rules) {
			startWebViewActivity(Defs.URL_ABOUT_RULES);
		} else if (id == R.id.kanojo_option_barcodekanojo) {
			startWebViewActivity(Defs.URL_ABOUT_BARCODEKANOJO);
		} else if (id == R.id.kanojo_option_team) {
			startWebViewActivity(Defs.URL_ABOUT_TEAM);
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

    //class OptionModifyTask extends AsyncTask<Void, Void, Response<?>> {
    //    private Exception mReason = null;

    //    public void onPreExecute() {
    //        showProgressDialog();
    //    }

    //    public Response<?> doInBackground(Void... params) {
    //        try {
    //            return modify_user();
    //        } catch (Exception e) {
    //            this.mReason = e;
    //            return null;
    //        }
    //    }

    //    public void onPostExecute(Response<?> response) {
    //        if (response == null) {
    //            try {
    //                throw new BarcodeKanojoException("response is null! \n" + this.mReason);
    //            } catch (BarcodeKanojoException e) {
    //                showAlertDialog(new Alert(OptionActivity.this.getResources().getString(R.string.error_internet)));
    //                dismissProgressDialog();
    //            } catch (Throwable th) {
    //                dismissProgressDialog();
    //                throw th;
    //            }
    //        } else {
	//			if (response.getCode() == 200) {
	//				executeOptionChangeDeviceTask();
	//			} else {
	//				Alert alert = response.getAlert();
	//				if (alert != null) {
	//					showAlertDialog(alert);
	//				}
	//			}
    //            dismissProgressDialog();
    //        }
    //    }

    //    @Override
    //    protected void onCancelled() {
    //    }

    //    Response<?> modify_user() throws BarcodeKanojoException, IllegalStateException, IOException {
    //        BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) OptionActivity.this.getApplication()).getBarcodeKanojo();
    //        User user = barcodeKanojo.getUser();
    //        if (modifiedUser.getPassword().getHashedPassword().length() == 0) {
    //            modifiedUser.setPassword(user.getPassword());
    //        }
    //        Response<BarcodeKanojoModel> android_update = barcodeKanojo.update(OptionActivity.this.modifiedUser.getName(), user.getPassword(), OptionActivity.this.modifiedUser.getPassword(), OptionActivity.this.modifiedUser.getEmail(), OptionActivity.this.modifiedUser.getBirth_year(), OptionActivity.this.modifiedUser.getBirth_month(), OptionActivity.this.modifiedUser.getBirth_day(), OptionActivity.this.modifiedUser.getSex(), OptionActivity.this.modifiedPhoto);
    //        barcodeKanojo.init_product_category_list();
    //        User user2 = barcodeKanojo.getUser();
	//		((BarcodeKanojoApp) OptionActivity.this.getApplication()).getImageCache().evict(user2.getProfile_image_url());
    //        return android_update;
    //    }
    //}

    //private void executeOptionDeleteTask() {
    //    if (this.mOptionDeleteTask == null || this.mOptionDeleteTask.getStatus() == AsyncTask.Status.FINISHED || this.mOptionDeleteTask.cancel(true) || this.mOptionDeleteTask.isCancelled()) {
    //        this.mOptionDeleteTask = (OptionDeleteTask) new OptionDeleteTask().execute(new Void[0]);
    //    }
    //}

    class OptionDeleteTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        public void onPreExecute() {
            showProgressDialog();
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
					if (OptionActivity.this.getCodeAndShowAlert(response, this.mReason, OptionActivity.this.mListener) != 200) {
						Alert alert = response.getAlert();
						if (alert != null) {
							OptionActivity.this.showAlertDialog(alert);
						}
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

    //@Override
    //protected void deleteUser() {
    //    showNoticeDialog(getString(R.string.delete_account_message));
    //}

//    private void executeOptionChangeDeviceTask() {
//        if (this.mOptionChangeDeviceTask == null || this.mOptionChangeDeviceTask.getStatus() == AsyncTask.Status.FINISHED || this.mOptionChangeDeviceTask.cancel(true) || this.mOptionChangeDeviceTask.isCancelled()) {
//            this.mOptionChangeDeviceTask = (OptionChangeDeviceTask) new OptionChangeDeviceTask().execute(new Void[0]);
//        }
//    }

//    class OptionChangeDeviceTask extends AsyncTask<Void, Void, Response<?>> {
//        private Exception mReason = null;
//
//        @Override
//        public void onPreExecute() {
//            showProgressDialog();
//        }
//
//        @Override
//        public Response<?> doInBackground(Void... params) {
//            try {
//                return changeDevice();
//            } catch (Exception e) {
//                this.mReason = e;
//                return null;
//            }
//        }
//
//        @Override
//        public void onPostExecute(Response<?> response) {
//            if (response == null) {
//                try {
//                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
//                } catch (BarcodeKanojoException e) {
//                    OptionActivity.this.showAlertDialog(new Alert(OptionActivity.this.getResources().getString(R.string.error_internet)));
//                    OptionActivity.this.dismissProgressDialog();
//                } catch (Throwable th) {
//                    OptionActivity.this.dismissProgressDialog();
//                    throw th;
//                }
//            } else {
//				if (response.getCode() == Response.CODE_SUCCESS) {
//					OptionActivity.this.fixUser();
//				} else {
//					Alert alert = response.getAlert();
//					if (alert != null) {
//						OptionActivity.this.showAlertDialog(alert);
//					}
//				}
//                OptionActivity.this.dismissProgressDialog();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//        }
//
//        Response<?> changeDevice() throws BarcodeKanojoException, IllegalStateException {
//            return ((BarcodeKanojoApp) OptionActivity.this.getApplication()).getBarcodeKanojo().verify(OptionActivity.this.modifiedUser.getEmail(), OptionActivity.this.modifiedUser.getPassword(), ((BarcodeKanojoApp) OptionActivity.this.getApplication()).getSettings().getUUID());
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

	private void startWebViewActivity(String url) {
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, HttpApi.Companion.get().getMApiBaseUrl() + url);
		startActivity(intent);
	}
}
