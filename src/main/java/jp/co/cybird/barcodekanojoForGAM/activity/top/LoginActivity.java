package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.view.EditTextView;

public class LoginActivity extends BaseEditActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private Button btnClose;
    private Button btnLogin;
    private EditTextView txtEmail;
    private EditTextView txtPassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.btnClose = (Button) findViewById(R.id.kanojo_log_in_close);
        this.btnClose.setOnClickListener(this);
        this.txtEmail = (EditTextView) findViewById(R.id.kanojo_log_in_email);
        this.txtEmail.setOnClickListener(this);
        this.txtPassword = (EditTextView) findViewById(R.id.kanojo_log_in_password);
        this.txtPassword.setOnClickListener(this);
        this.txtPassword.setTypeToPassword();
        this.btnLogin = (Button) findViewById(R.id.kanojo_log_in_btn);
        this.btnLogin.setOnClickListener(this);
        User user = ((BarcodeKanojoApp) getApplication()).getUser();
        if (user.getEmail() != null) {
            this.txtEmail.setValue(user.getEmail());
        }
        if (user.getPassword() != null) {
            this.txtPassword.setValue(user.getPassword());
        }
        setAutoRefreshSession(false);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.btnClose.setOnClickListener((View.OnClickListener) null);
        this.txtEmail.setOnClickListener((View.OnClickListener) null);
        this.txtPassword.setOnClickListener((View.OnClickListener) null);
        this.btnLogin.setOnClickListener((View.OnClickListener) null);
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kanojo_log_in_close:
                close();
                return;
            case R.id.kanojo_log_in_btn:
                User user = ((BarcodeKanojoApp) getApplication()).getUser();
                if (this.txtPassword.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_password));
                    return;
                } else if (this.txtEmail.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_email));
                    return;
                } else {
                    user.setPassword(this.txtPassword.getValue());
                    user.setEmail(this.txtEmail.getValue().replaceAll(" ", ""));
                    setResult(BaseInterface.RESULT_LOG_IN, (Intent) null);
                    close();
                    return;
                }
            default:
                return;
        }
    }
}
