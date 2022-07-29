package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.goujer.barcodekanojo.activity.base.BaseKanojoEditActivity;

import java.io.File;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import com.goujer.barcodekanojo.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;
import com.goujer.barcodekanojo.view.ProductAndKanojoView;

public class KanojoEditActivity extends BaseKanojoEditActivity implements View.OnClickListener {
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
    private String mMessage;
    private EditItemView mPhoto;
    private Product mProduct;
    private ProductAndKanojoView mProductAndKanojo;
    private EditItemView mProductName;
    private boolean mShowMessage = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanojo_edit);
        this.btnClose = findViewById(R.id.edit_close);
        this.btnClose.setOnClickListener(this);
        this.mProductAndKanojo = findViewById(R.id.kanojo_edit_photo);
        this.mKanojoName = findViewById(R.id.kanojo_edit_1_kanojo_name);
        this.mKanojoName.setOnClickListener(this);
        this.mCompanyName = findViewById(R.id.kanojo_edit_2_company_name);
        this.mCompanyName.setOnClickListener(this);
        this.mProductName = findViewById(R.id.kanojo_edit_3_product_name);
        this.mProductName.setOnClickListener(this);
        this.mCategoryName = findViewById(R.id.kanojo_edit_4_category);
        this.mCategoryName.setOnClickListener(this);
        this.mLocation = findViewById(R.id.kanojo_edit_5_location);
        this.mLocation.setOnClickListener(this);
        this.mPhoto = findViewById(R.id.kanojo_edit_6_photo);
        this.mPhoto.setOnClickListener(this);
        this.mComment = findViewById(R.id.kanojo_edit_7_comment);
        this.mComment.setOnClickListener(this);
        this.btnSave = findViewById(R.id.kanojo_edit_btn_save);
        this.btnSave.setOnClickListener(this);
        this.btnSave.setEnabled(false);
        Bundle bundle = getIntent().getExtras();
        this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
        this.mProduct = (Product) bundle.get(BaseInterface.EXTRA_PRODUCT);
        this.mMessage = bundle.getString(MessageModel.NOTIFY_AMENDMENT_INFORMATION);
        if (this.mKanojo != null && this.mProduct != null) {
            this.mKanojoName.setValue(this.mKanojo.getName());
            this.mCompanyName.setValue(this.mProduct.getCompany_name());
            this.mProductName.setValue(this.mProduct.getName());
            this.mCategoryName.setValue(this.mProduct.getCategory());
            this.mLocation.setValue(this.mProduct.getLocation());
            this.mComment.setValue(this.mProduct.getComment());
            this.mLoadingView = findViewById(R.id.loadingView);
            if (!this.mCompanyName.isEmpty() && !this.mProductName.isEmpty()) {
                this.btnSave.setEnabled(true);
            }
            this.mProductAndKanojo.executeLoadImgTask(((BarcodeKanojoApp) getApplication()).getImageCache(), this.mProduct.getProduct_image_url(), this.mKanojo);
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
        View layout = getLayoutInflater().inflate(R.layout.activity_kanojo_edit, null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(layout);
        return appLayoutRoot;
    }

    @Override
    protected void onResume() {
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
        this.btnClose.setOnClickListener(null);
        this.mKanojoName.setOnClickListener(null);
        this.mCompanyName.setOnClickListener(null);
        this.mProductName.setOnClickListener(null);
        this.mCategoryName.setOnClickListener(null);
        this.mLocation.setOnClickListener(null);
        this.mPhoto.setOnClickListener(null);
        this.mComment.setOnClickListener(null);
        this.btnSave.setOnClickListener(null);
    }

    @Override
    protected void onDestroy() {
        unBindEvent();
        this.mProductAndKanojo.destroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File f = getFile();
        if (f != null && f.exists()) {
            setBitmapFromFile(this.mProductAndKanojo, f);
        }
    }

    public void onClick(View v) {
        unBindEvent();
		int id = v.getId();
		if (id == R.id.edit_close) {
			close();
			return;
		} else if (id == R.id.kanojo_edit_1_kanojo_name || id == R.id.kanojo_edit_5_location) {
			bindEvent();
			return;
		} else if (id == R.id.kanojo_edit_2_company_name) {
			showEditTextDialog(this.r.getString(R.string.common_product_company), this.mCompanyName);
			return;
		} else if (id == R.id.kanojo_edit_3_product_name) {
			showEditTextDialog(this.r.getString(R.string.common_product_name), this.mProductName);
			return;
		} else if (id == R.id.kanojo_edit_4_category) {
			showListDialog(this.r.getString(R.string.common_product_category), this.mProduct, this.mCategoryName);
			return;
		} else if (id == R.id.kanojo_edit_6_photo) {
			showImagePickerDialog(this.r.getString(R.string.common_product_photo));
			return;
		} else if (id == R.id.kanojo_edit_7_comment) {
			showEditTextDialog(this.r.getString(R.string.common_product_comment), this.mComment, 4);
			return;
		} else if (id == R.id.kanojo_edit_btn_save) {
			executeInspectionAndUpdateTask(this.mProduct.getBarcode(), this.mCompanyName.getValue(), this.mProductName.getValue(), this.mProduct.getCategory_id(), this.mComment.getValue());
			return;
		}
		return;
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

    protected void dismissProgressDialog() {
        this.mLoadingView.dismiss();
    }
}
