package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.core.Password;

import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import com.goujer.barcodekanojo.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.view.EditTextView;

public class LoginActivity extends BaseEditActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private Button btnClose;
    private Button btnLogin;
    private EditTextView txtEmail;
    private EditTextView txtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.btnClose = (Button) findViewById(R.id.kanojo_log_in_close);
        this.txtEmail = (EditTextView) findViewById(R.id.kanojo_log_in_email);
        this.txtPassword = (EditTextView) findViewById(R.id.kanojo_log_in_password);
        this.txtPassword.setTypeToPassword();
        this.btnLogin = (Button) findViewById(R.id.kanojo_log_in_btn);
        User user = ((BarcodeKanojoApp) getApplication()).getUser();
        if (user.getEmail() != null) {
            this.txtEmail.setValue(user.getEmail());
        }
        //if (user.getPassword() != null) {
        //    this.txtPassword.setValue(user.getPassword());
        //}
		this.btnClose.setOnClickListener(this);
		this.txtPassword.setOnClickListener(this);
		this.btnLogin.setOnClickListener(this);

        setAutoRefreshSession(false);
    }

    @Override
    protected void onDestroy() {
		super.onDestroy();
		this.btnClose.setOnClickListener(null);
        this.txtPassword.setOnClickListener(null);
        this.btnLogin.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.kanojo_log_in_close) {
			finish();
		} else if (id == R.id.kanojo_log_in_btn) {
			User user = ((BarcodeKanojoApp) getApplication()).getUser();
			/*if (this.txtPassword.getValue().equals("")) {
				showNoticeDialog(this.r.getString(R.string.error_no_password));
			} else*/ if (this.txtEmail.getValue().equals("")) {
				showNoticeDialog(this.r.getString(R.string.error_no_email));
			} else {
				user.setPassword(Password.Companion.hashPassword(this.txtPassword.getValue(), ""));
				user.setEmail(this.txtEmail.getValue().replaceAll(" ", ""));
				setResult(BaseInterface.RESULT_LOG_IN, (Intent) null);
				finish();
			}
		}
	}
}
