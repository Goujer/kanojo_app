package jp.co.cybird.barcodekanojoForGAM.activity.top;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;

public class UserEntryActivity extends BaseEditActivity implements View.OnClickListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "UserEntryActivity";
    private Button btnClose;
    private Button btnSave;
    private ImageView imgPhoto;
    private EditItemView privacy_btn;
    private EditItemView terms_btn;
    private EditItemView txtBirthday;
    private EditItemView txtEmail;
    private EditItemView txtGender;
    private EditItemView txtIcon;
    private EditItemView txtName;
    private EditItemView txtPassword;
    private EditItemView txtRePassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_entry);
        this.btnClose = findViewById(R.id.kanojo_sign_up_close);
        this.btnClose.setOnClickListener(this);
        this.imgPhoto = findViewById(R.id.kanojo_user_entry_photo);
        this.txtName = findViewById(R.id.kanojo_user_entry_name);
        this.txtName.setOnClickListener(this);
        this.txtPassword = findViewById(R.id.kanojo_user_entry_password);
        this.txtPassword.setOnClickListener(this);
        this.txtPassword.hideText();
        this.txtRePassword = findViewById(R.id.kanojo_user_entry_re_password);
        this.txtRePassword.setOnClickListener(this);
        this.txtRePassword.hideText();
        this.txtEmail = findViewById(R.id.kanojo_user_entry_email);
        this.txtEmail.setOnClickListener(this);
        this.txtGender = findViewById(R.id.kanojo_user_entry_gender);
        this.txtGender.setOnClickListener(this);
        this.txtBirthday = findViewById(R.id.kanojo_user_entry_birthday);
        this.txtBirthday.setOnClickListener(this);
        this.txtIcon = findViewById(R.id.kanojo_user_entry_icon);
        this.txtIcon.setOnClickListener(this);
        this.privacy_btn = findViewById(R.id.kanojo_user_entry_privacy);
        this.privacy_btn.setOnClickListener(this);
        this.terms_btn = findViewById(R.id.kanojo_user_entry_terms);
        this.terms_btn.setOnClickListener(this);
        this.btnSave = findViewById(R.id.kanojo_sign_up_btn);
        this.btnSave.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        this.btnClose.setOnClickListener(null);
        this.txtName.setOnClickListener(null);
        this.txtPassword.setOnClickListener(null);
        this.txtRePassword.setOnClickListener(null);
        this.txtEmail.setOnClickListener(null);
        this.txtGender.setOnClickListener(null);
        this.txtBirthday.setOnClickListener(null);
        this.txtIcon.setOnClickListener(null);
        this.privacy_btn.setOnClickListener(null);
        this.terms_btn.setOnClickListener(null);
        this.btnSave.setOnClickListener(null);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File f = getFile();
        if (f != null && f.exists()) {
            setBitmapFromFile(this.imgPhoto, f);
        }
    }

    public void onClick(View v) {
        EditText input = new EditText(this);
        switch (v.getId()) {
            case R.id.kanojo_sign_up_close:
                close();
                return;
            case R.id.kanojo_user_entry_name:
                showEditTextDialog(this.r.getString(R.string.user_account_name), this.txtName);
                return;
            case R.id.kanojo_user_entry_password:
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                showEditTextDialog(this.r.getString(R.string.user_account_password), this.txtPassword, input);
                return;
            case R.id.kanojo_user_entry_re_password:
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                showEditTextDialog(this.r.getString(R.string.user_account_re_password), this.txtRePassword, input);
                return;
            case R.id.kanojo_user_entry_email:
                input.setInputType(33);
                showEditTextDialog(this.r.getString(R.string.user_account_email), this.txtEmail, input);
                return;
            case R.id.kanojo_user_entry_gender:
                showGenderDialog(this.r.getString(R.string.user_account_gender), this.txtGender);
                return;
            case R.id.kanojo_user_entry_birthday:
                showDatePickDialog(this.r.getString(R.string.user_account_birthday), this.txtBirthday);
                return;
            case R.id.kanojo_user_entry_icon:
                showImagePickerDialog(this.r.getString(R.string.user_account_icon));
                return;
            case R.id.kanojo_user_entry_privacy:
                showPrivacy();
                return;
            case R.id.kanojo_user_entry_terms:
                showTerms();
                return;
            case R.id.kanojo_sign_up_btn:
                User user = ((BarcodeKanojoApp) getApplication()).getUser();
                if (this.txtName.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_name));
                    return;
                } else if (this.txtPassword.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_password));
                    return;
                } else if (this.txtPassword.getValue().length() < 6 || 16 < this.txtPassword.getValue().length()) {
                    showNoticeDialog(this.r.getString(R.string.error_password_length));
                    return;
                } else if (!this.txtPassword.getValue().equals(this.txtRePassword.getValue())) {
                    showNoticeDialog(this.r.getString(R.string.error_unmatch_password));
                    return;
                } else if (this.txtEmail.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_email));
                    return;
                } else if (this.txtBirthday.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_birthday));
                    return;
                } else if (this.txtGender.getValue().equals("")) {
                    showNoticeDialog(this.r.getString(R.string.error_no_gender));
                    return;
                } else {
                    user.setName(this.txtName.getValue());
                    user.setPassword(this.txtPassword.getValue());
                    user.setEmail(this.txtEmail.getValue().replaceAll(" ", ""));
                    user.setSexFromText(this.txtGender.getValue());
                    user.setBirthFromText(this.txtBirthday.getValue());
                    Intent intent = new Intent();
                    intent.putExtra("photo", getFile());
                    setResult(105, intent);
                    close();
                    return;
                }
            default:
                return;
        }
    }

    protected void setBitmapFromFile(ImageView view, File file) {
        Bitmap setBitmap;
        Bitmap bitmap = loadBitmap(file, 200, 200);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width > height) {
                Matrix aMatrix = new Matrix();
                aMatrix.setRotate(90.0f);
                setBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, aMatrix, false);
            } else {
                setBitmap = bitmap;
            }
            this.imgPhoto.setImageBitmap(setBitmap);
        }
    }
}
