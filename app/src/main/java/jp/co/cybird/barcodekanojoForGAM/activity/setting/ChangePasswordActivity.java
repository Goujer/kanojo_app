package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.goujer.barcodekanojo.R;
import com.goujer.barcodekanojo.activity.base.BaseEditActivity;
import com.goujer.barcodekanojo.core.Password;

import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;

public class ChangePasswordActivity extends BaseEditActivity implements View.OnClickListener {

    private static final String TAG = "ChangePasswordActivity";
    private Password currentPassword;
    private boolean isNewEmail;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.isNewEmail = bundle.getBoolean("new_email", false);
            this.currentPassword = bundle.getParcelable("current_password");
            if (this.currentPassword != null && this.currentPassword.getHashedPassword().equals("")) {
                this.currentPassword = null;
            }
        }
        findViewById(R.id.password_change_close_btn).setOnClickListener(this);

        findViewById(R.id.password_change_current).setOnClickListener(this);
		((EditItemView) findViewById(R.id.password_change_current)).hideText();

		findViewById(R.id.password_change_new).setOnClickListener(this);
		((EditItemView) findViewById(R.id.password_change_new)).hideText();

		findViewById(R.id.password_change_re_new).setOnClickListener(this);
		((EditItemView) findViewById(R.id.password_change_re_new)).hideText();

        findViewById(R.id.password_change_check_btn).setOnClickListener(this);
        if (this.isNewEmail) {
            findViewById(R.id.password_change_current).setVisibility(View.GONE);
			findViewById(R.id.password_change_new).setBackgroundResource(R.drawable.row_kanojo_edit_bg_top);
            return;
        }
        findViewById(R.id.password_change_current).setVisibility(View.VISIBLE);
		findViewById(R.id.password_change_new).setBackgroundResource(R.drawable.row_kanojo_edit_bg_middle);
    }

    @Override
    protected void onDestroy() {
		findViewById(R.id.password_change_close_btn).setOnClickListener(null);
        findViewById(R.id.password_change_current).setOnClickListener(null);
		findViewById(R.id.password_change_new).setOnClickListener(null);
		findViewById(R.id.password_change_re_new).setOnClickListener(null);
		findViewById(R.id.password_change_check_btn).setOnClickListener(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        EditText input = new EditText(this);
        if (v.getId() == R.id.password_change_close_btn) {
			finish();
		} else if (v.getId() == R.id.password_change_current) {
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			showEditTextDialog(getResources().getString(R.string.password_change_current), findViewById(R.id.password_change_current), input);
		} else if (v.getId() == R.id.password_change_new) {
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			showEditTextDialog(getResources().getString(R.string.password_change_password), findViewById(R.id.password_change_new), input);
		} else if (v.getId() == R.id.password_change_re_new) {
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			showEditTextDialog(getResources().getString(R.string.password_change_re_password), findViewById(R.id.password_change_re_new), input);
		} else if (v.getId() == R.id.password_change_check_btn) {
			if (!this.isNewEmail && ((EditItemView) findViewById(R.id.password_change_current)).getValue().equals("")) {
				showNoticeDialog(getResources().getString(R.string.error_no_old_password));
			} else if (!this.isNewEmail && !checkCurrentPassword()) {
				showNoticeDialog(getResources().getString(R.string.error_unmatch_current_password));
			} else if (((EditItemView) findViewById(R.id.password_change_new)).getValue().equals("")) {
				showNoticeDialog(getResources().getString(R.string.error_no_new_password));
			} else if (((EditItemView) findViewById(R.id.password_change_new)).getValue().length() < 6 || 16 < ((EditItemView) findViewById(R.id.password_change_new)).getValue().length()) {
				showNoticeDialog(getResources().getString(R.string.error_password_length));
			} else if (!((EditItemView) findViewById(R.id.password_change_new)).getValue().equals(((EditItemView) findViewById(R.id.password_change_re_new)).getValue())) {
				showNoticeDialog(getResources().getString(R.string.error_unmatch_new_password));
			} else {
				Intent intent = new Intent();
				Password current_password = Password.Companion.hashPassword(((EditItemView) findViewById(R.id.password_change_current)).getValue(), "");
				Password new_password = Password.Companion.hashPassword(((EditItemView) findViewById(R.id.password_change_new)).getValue(), "");

				intent.putExtra("new_password", new_password);
				if (!this.isNewEmail) {
					intent.putExtra("current_Password", current_password);
				}
				setResult(BaseInterface.RESULT_CHANGED, intent);
				finish();
			}
		}
    }

    private boolean checkCurrentPassword() {
        if (this.currentPassword != null) {
        	Password inputCurrentPassword = Password.Companion.hashPassword(((EditItemView)findViewById(R.id.password_change_current)).getValue(), currentPassword.getMSalt());
        	return this.currentPassword.equals(inputCurrentPassword);
        }
        return false;
    }

    //public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    //    MessageDigest md = MessageDigest.getInstance(Digest.SHA1);
    //    if (Build.VERSION.SDK_INT < 19) {
	//		md.update(text.getBytes("iso-8859-1"), 0, text.length());
	//	} else {
	//		md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
	//	}
    //    return convertToHex(md.digest());
    //}

    //private static String convertToHex(byte[] data) {
    //    char c;
    //    StringBuilder buf = new StringBuilder();
    //    for (byte b : data) {
    //        int halfbyte = (b >>> 4) & 15;
    //        int two_halfs = 0;
    //        while (true) {
    //            if (halfbyte < 0 || halfbyte > 9) {
    //                c = (char) ((halfbyte - 10) + 97);
    //            } else {
    //                c = (char) (halfbyte + 48);
    //            }
    //            buf.append(c);
    //            halfbyte = b & 15;
    //            int two_halfs2 = two_halfs + 1;
    //            if (two_halfs >= 1) {
    //                break;
    //            }
    //            two_halfs = two_halfs2;
    //        }
    //    }
    //    return buf.toString();
    //}
}
