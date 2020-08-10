package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.util.EditBitmapActivity;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class EditAccountActivity extends BaseEditActivity implements BaseInterface, View.OnClickListener {
    public static final String TAG = "LogInActivity";
    private Resources r;
    private TextView txtName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        this.r = getResources();
        ((Button) findViewById(R.id.edit_acount_close)).setOnClickListener(this);
        ((Button) findViewById(R.id.edit_account_ok)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.edit_account_name)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.edit_account_photo)).setOnClickListener(this);
        this.txtName = (TextView) findViewById(R.id.edit_account_name_value);
        this.txtName.setText(((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser().getName());
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_edit_account, (ViewGroup) null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_acount_close:
                finish();
                return;
            case R.id.edit_account_name:
                final EditText edit = new EditText(this);
                edit.setInputType(1);
                edit.setText(this.txtName.getText().toString());
                new AlertDialog.Builder(this).setTitle(R.string.sign_up_name).setView(edit).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditAccountActivity.this.txtName.setText(edit.getText().toString());
                    }
                }).setNegativeButton(R.string.common_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
                return;
            case R.id.edit_account_photo:
                showImagePickerDialog(this.r.getString(R.string.sign_up_photo));
                return;
            case R.id.edit_account_ok:
                executeInspectionAndEditAccountTask(this.txtName.getText().toString());
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 1100:
                case BaseInterface.REQUEST_CAMERA:
                    this.mFile = EditBitmapActivity.getEditedFile();
                    return;
                default:
                    return;
            }
        }
    }

    public void onDismiss(DialogInterface dialog, int code) {
        switch (code) {
            case 200:
                finish();
                return;
            default:
                return;
        }
    }

    protected void executeInspectionAndEditAccountTask(String txt) {
        HashMap<String, String> param = new HashMap<>();
        param.put(GreeDefs.USER_NAME, txt);
        inspectionAndUpdateByAction(param, 1, (HashMap<String, Object>) null);
    }
}
