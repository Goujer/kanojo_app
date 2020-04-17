package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.io.File;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Scanned;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;
import jp.co.cybird.barcodekanojoForGAM.view.ProductAndKanojoView;

public class KanojoEditActivity extends BaseEditActivity implements View.OnClickListener {
    private static final String TAG = "KanojoEditActivity";
    private Button btnClose;
    private Button btnSave;
    private EditItemView mCategoryName;
    private EditItemView mComment;
    private EditItemView mCompanyName;
    private Kanojo mKanojo;
    private EditItemView mKanojoName;
    private CustomLoadingView mLoadingView;
    private EditItemView mLocation;
    /* access modifiers changed from: private */
    public String mMessage;
    private EditItemView mPhoto;
    private Product mProduct;
    private ProductAndKanojoView mProductAndKanojo;
    private EditItemView mProductName;
    private Scanned mScanned;
    private boolean mShowMessage = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanojo_edit);
        this.btnClose = (Button) findViewById(R.id.edit_close);
        this.btnClose.setOnClickListener(this);
        this.mProductAndKanojo = (ProductAndKanojoView) findViewById(R.id.kanojo_edit_photo);
        this.mKanojoName = (EditItemView) findViewById(R.id.kanojo_edit_1_kanojo_name);
        this.mKanojoName.setOnClickListener(this);
        this.mCompanyName = (EditItemView) findViewById(R.id.kanojo_edit_2_company_name);
        this.mCompanyName.setOnClickListener(this);
        this.mProductName = (EditItemView) findViewById(R.id.kanojo_edit_3_product_name);
        this.mProductName.setOnClickListener(this);
        this.mCategoryName = (EditItemView) findViewById(R.id.kanojo_edit_4_category);
        this.mCategoryName.setOnClickListener(this);
        this.mLocation = (EditItemView) findViewById(R.id.kanojo_edit_5_location);
        this.mLocation.setOnClickListener(this);
        this.mPhoto = (EditItemView) findViewById(R.id.kanojo_edit_6_photo);
        this.mPhoto.setOnClickListener(this);
        this.mComment = (EditItemView) findViewById(R.id.kanojo_edit_7_comment);
        this.mComment.setOnClickListener(this);
        this.btnSave = (Button) findViewById(R.id.kanojo_edit_btn_save);
        this.btnSave.setOnClickListener(this);
        this.btnSave.setEnabled(false);
        Bundle bundle = getIntent().getExtras();
        this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
        this.mProduct = (Product) bundle.get(BaseInterface.EXTRA_PRODUCT);
        this.mScanned = (Scanned) bundle.get(BaseInterface.EXTRA_SCANNED);
        this.mMessage = bundle.getString(MessageModel.NOTIFY_AMENDMENT_INFORMATION);
        if (this.mKanojo != null && this.mProduct != null && this.mScanned != null) {
            this.mKanojoName.setValue(this.mKanojo.getName());
            this.mCompanyName.setValue(this.mProduct.getCompany_name());
            this.mProductName.setValue(this.mProduct.getName());
            this.mCategoryName.setValue(this.mProduct.getCategory());
            this.mLocation.setValue(this.mProduct.getLocation());
            this.mComment.setValue(this.mScanned.getComment());
            this.mLoadingView = (CustomLoadingView) findViewById(R.id.loadingView);
            if (!this.mCompanyName.isEmpty() && !this.mProductName.isEmpty()) {
                this.btnSave.setEnabled(true);
            }
            this.mProductAndKanojo.executeLoadImgTask(((BarcodeKanojoApp) getApplication()).getRemoteResourceManager(), this.mScanned.getProduct_image_url(), this.mKanojo);
            if (this.mMessage != null) {
                showNoticeDialog(this.mMessage, new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface arg0) {
                        KanojoEditActivity.this.mMessage = null;
                    }
                });
            }
        }
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_kanojo_edit, (ViewGroup) null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        bindEvent();
    }

    public void bindEvent() {
        this.btnClose.setOnClickListener(this);
        this.mKanojoName.setOnClickListener(this);
        this.mCompanyName.setOnClickListener(this);
        this.mProductName.setOnClickListener(this);
        this.mCategoryName.setOnClickListener(this);
        this.mLocation.setOnClickListener(this);
        this.mPhoto.setOnClickListener(this);
        this.mComment.setOnClickListener(this);
        this.btnSave.setOnClickListener(this);
    }

    public void unBindEvent() {
        this.btnClose.setOnClickListener((View.OnClickListener) null);
        this.mKanojoName.setOnClickListener((View.OnClickListener) null);
        this.mCompanyName.setOnClickListener((View.OnClickListener) null);
        this.mProductName.setOnClickListener((View.OnClickListener) null);
        this.mCategoryName.setOnClickListener((View.OnClickListener) null);
        this.mLocation.setOnClickListener((View.OnClickListener) null);
        this.mPhoto.setOnClickListener((View.OnClickListener) null);
        this.mComment.setOnClickListener((View.OnClickListener) null);
        this.btnSave.setOnClickListener((View.OnClickListener) null);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.btnClose.setOnClickListener((View.OnClickListener) null);
        this.mKanojoName.setOnClickListener((View.OnClickListener) null);
        this.mCompanyName.setOnClickListener((View.OnClickListener) null);
        this.mProductName.setOnClickListener((View.OnClickListener) null);
        this.mCategoryName.setOnClickListener((View.OnClickListener) null);
        this.mLocation.setOnClickListener((View.OnClickListener) null);
        this.mPhoto.setOnClickListener((View.OnClickListener) null);
        this.mComment.setOnClickListener((View.OnClickListener) null);
        this.btnSave.setOnClickListener((View.OnClickListener) null);
        this.mProductAndKanojo.clear();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File f = getFile();
        if (f != null && f.exists()) {
            setBitmapFromFile(this.mProductAndKanojo, f);
        }
    }

    public void onClick(View v) {
        unBindEvent();
        switch (v.getId()) {
            case R.id.edit_close:
                close();
                return;
            case R.id.kanojo_edit_1_kanojo_name:
            case R.id.kanojo_edit_5_location:
                bindEvent();
                return;
            case R.id.kanojo_edit_2_company_name:
                showEditTextDialog(this.r.getString(R.string.common_product_company), this.mCompanyName);
                return;
            case R.id.kanojo_edit_3_product_name:
                showEditTextDialog(this.r.getString(R.string.common_product_name), this.mProductName);
                return;
            case R.id.kanojo_edit_4_category:
                showListDialog(this.r.getString(R.string.common_product_category), this.mProduct, this.mCategoryName);
                return;
            case R.id.kanojo_edit_6_photo:
                showImagePickerDialog(this.r.getString(R.string.common_product_photo));
                return;
            case R.id.kanojo_edit_7_comment:
                showEditTextDialog(this.r.getString(R.string.common_product_comment), this.mComment, 4);
                return;
            case R.id.kanojo_edit_btn_save:
                executeInspectionAndUpdateTask(this.mProduct.getBarcode(), this.mCompanyName.getValue(), this.mProductName.getValue(), this.mProduct.getCategory_id(), this.mComment.getValue());
                return;
            default:
                return;
        }
    }

    public void onDismiss(DialogInterface dialog, int code) {
        if (this.mCompanyName.isEmpty() || this.mProductName.isEmpty()) {
            this.btnSave.setEnabled(false);
        } else {
            this.btnSave.setEnabled(true);
        }
        bindEvent();
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
        this.mLoadingView.dismiss();
    }
}
