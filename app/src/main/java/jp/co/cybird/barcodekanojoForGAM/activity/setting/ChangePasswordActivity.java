package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.goujer.barcodekanojo.activity.base.BaseEditActivity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;

public class ChangePasswordActivity extends BaseEditActivity implements View.OnClickListener {

    private static final String TAG = "ChangePasswordActivity";
    private Button btnClose;
    private Button btnSave;
    private byte[] encodedCurrentPassword;
    private boolean isNewEmail;
    private EditItemView txtCurrent;
    private EditItemView txtPassword;
    private EditItemView txtRePassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.isNewEmail = bundle.getBoolean("new_email", false);
            this.encodedCurrentPassword = bundle.getByteArray("encodedCurrentPassword");
            if (this.encodedCurrentPassword != null && this.encodedCurrentPassword.length == 0) {
                this.encodedCurrentPassword = null;
            }
        }
        this.btnClose = findViewById(R.id.kanojo_password_change_close);
        this.btnClose.setOnClickListener(this);
        this.txtCurrent = findViewById(R.id.kanojo_password_change_current);
        this.txtCurrent.setOnClickListener(this);
        this.txtCurrent.hideText();
        this.txtPassword = findViewById(R.id.kanojo_password_change_password);
        this.txtPassword.setOnClickListener(this);
        this.txtPassword.hideText();
        this.txtRePassword = findViewById(R.id.kanojo_password_change_re_password);
        this.txtRePassword.setOnClickListener(this);
        this.txtRePassword.hideText();
        this.btnSave = findViewById(R.id.kanojo_password_change_btn);
        this.btnSave.setOnClickListener(this);
        if (this.isNewEmail) {
            this.txtCurrent.setVisibility(View.GONE);
            this.txtPassword.setBackgroundResource(R.drawable.row_kanojo_edit_bg_top);
            return;
        }
        this.txtCurrent.setVisibility(View.VISIBLE);
        this.txtPassword.setBackgroundResource(R.drawable.row_kanojo_edit_bg_middle);
    }

    @Override
    protected void onDestroy() {
        this.btnClose.setOnClickListener(null);
        this.txtCurrent.setOnClickListener(null);
        this.txtPassword.setOnClickListener(null);
        this.txtRePassword.setOnClickListener(null);
        this.btnSave.setOnClickListener(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        EditText input = new EditText(this);
        if (v.getId() == R.id.kanojo_password_change_close) {
			finish();
		} else if (v.getId() == R.id.kanojo_password_change_current) {
			input.setInputType(129);
			showEditTextDialog(getResources().getString(R.string.password_change_current), this.txtCurrent, input);
		} else if (v.getId() == R.id.kanojo_password_change_password) {
			input.setInputType(129);
			showEditTextDialog(getResources().getString(R.string.password_change_password), this.txtPassword, input);
		} else if (v.getId() == R.id.kanojo_password_change_re_password) {
			input.setInputType(129);
			showEditTextDialog(getResources().getString(R.string.password_change_re_password), this.txtRePassword, input);
		} else if (v.getId() == R.id.kanojo_password_change_btn) {
			if (!this.isNewEmail && this.txtCurrent.getValue().equals("")) {
				showNoticeDialog(getResources().getString(R.string.error_no_old_password));
			} else if (!this.isNewEmail && !checkCurrentPassword()) {
				showNoticeDialog(getResources().getString(R.string.error_unmatch_current_password));
			} else if (this.txtPassword.getValue().equals("")) {
				showNoticeDialog(getResources().getString(R.string.error_no_new_password));
			} else if (this.txtPassword.getValue().length() < 6 || 16 < this.txtPassword.getValue().length()) {
				showNoticeDialog(getResources().getString(R.string.error_password_length));
			} else if (!this.txtPassword.getValue().equals(this.txtRePassword.getValue())) {
				showNoticeDialog(getResources().getString(R.string.error_unmatch_new_password));
			} else {
				try {
					Intent intent = new Intent();
					byte[] current_password;
					byte[] new_password;
					if (Build.VERSION.SDK_INT < 19) {
						current_password = MessageDigest.getInstance("SHA-512").digest(this.txtCurrent.getValue().getBytes("UTF-8"));
						new_password = MessageDigest.getInstance("SHA-512").digest(this.txtPassword.getValue().getBytes("UTF-8"));
					} else {
						current_password = MessageDigest.getInstance("SHA-512").digest(this.txtCurrent.getValue().getBytes(StandardCharsets.UTF_8));
						new_password = MessageDigest.getInstance("SHA-512").digest(this.txtPassword.getValue().getBytes(StandardCharsets.UTF_8));
					}
					intent.putExtra("new_password", new_password);
					if (!this.isNewEmail) {
						intent.putExtra("currentPassword", current_password);
					}
					setResult(BaseInterface.RESULT_CHANGED, intent);
					finish();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
    }

    private boolean checkCurrentPassword() {
        if (this.encodedCurrentPassword != null) {
            try {
				byte[] encodeString;
				if (Build.VERSION.SDK_INT < 19) {
					encodeString = MessageDigest.getInstance("SHA-512").digest(this.txtCurrent.getValue().getBytes("UTF-8"));
				} else {
					encodeString = MessageDigest.getInstance("SHA-512").digest(this.txtCurrent.getValue().getBytes(StandardCharsets.UTF_8));
				}
                return Arrays.equals(this.encodedCurrentPassword, encodeString);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
