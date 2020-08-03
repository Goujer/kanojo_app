package jp.co.cybird.barcodekanojoForGAM.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.io.File;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.util.ApiTask;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Scanned;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;
import jp.co.cybird.barcodekanojoForGAM.view.ProductAndKanojoView;

public class ScanOthersEditActivity extends BaseEditActivity implements View.OnClickListener {

    private static final String TAG = "ScanOthersEditActivity";
    private Button btnSave;
    private ApiTask mApiTask;
    private EditItemView mCategoryName;
    private EditItemView mComment;
    private EditItemView mCompanyName;
    private Kanojo mKanojo;
    private EditItemView mKanojoName;
    private String mMessage;
    private Product mProduct;
    private ProductAndKanojoView mProductAndKanojo;
    private EditItemView mProductName;
    private Scanned mScanned;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_others);
        findViewById(R.id.edit_close).setOnClickListener(this);
        this.mProductAndKanojo = findViewById(R.id.scan_others_photo);
        this.mKanojoName = findViewById(R.id.scan_others_1_kanojo_name);
        this.mKanojoName.setOnClickListener(this);
        this.mCompanyName = findViewById(R.id.scan_others_2_company_name);
        this.mCompanyName.setOnClickListener(this);
        this.mProductName = findViewById(R.id.scan_others_3_product_name);
        this.mProductName.setOnClickListener(this);
        this.mCategoryName = findViewById(R.id.scan_others_4_category);
        this.mCategoryName.setOnClickListener(this);
        findViewById(R.id.scan_others_5_barcode).setOnClickListener(this);
        EditItemView mCountry = findViewById(R.id.scan_others_6_country);
        mCountry.setOnClickListener(this);
        EditItemView mLocation = findViewById(R.id.scan_others_7_location);
        mLocation.setOnClickListener(this);
        findViewById(R.id.scan_others_8_photo).setOnClickListener(this);
        this.mComment = findViewById(R.id.scan_others_9_comment);
        this.mComment.setOnClickListener(this);
        this.btnSave = findViewById(R.id.scan_others_btn_save);
        this.btnSave.setOnClickListener(this);
        this.btnSave.setEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
            this.mProduct = (Product) bundle.get(BaseInterface.EXTRA_PRODUCT);
            this.mScanned = (Scanned) bundle.get(BaseInterface.EXTRA_SCANNED);
        }
        this.mMessage = bundle.getString(MessageModel.NOTIFY_AMENDMENT_INFORMATION);
        if (this.mKanojo != null && this.mProduct != null) {
            this.mKanojoName.setValue(this.mKanojo.getName());
            this.mCompanyName.setValue(this.mProduct.getCompany_name());
            this.mProductName.setValue(this.mProduct.getName());
            this.mCategoryName.setValue(this.mProduct.getCategory());
            mCountry.setValue(this.mProduct.getCountry());
            mLocation.setValue(this.mProduct.getLocation());
            if (this.mScanned != null) {
                this.mComment.setValue(this.mScanned.getComment());
            }
            if (!this.mKanojoName.isEmpty() && !this.mCompanyName.isEmpty() && !this.mProductName.isEmpty()) {
                this.btnSave.setEnabled(true);
            }
            this.mProductAndKanojo.executeLoadImgTask(((BarcodeKanojoApp) getApplication()).getRemoteResourceManager(), this.mProduct.getProduct_image_url(), this.mKanojo);
        }
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_scan_others, null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((BarcodeKanojoApp) getApplication()).requestLocationUpdates(true);
        if (this.mMessage != null) {
            showNoticeDialog(this.mMessage);
        }
    }

    @Override
    protected void onPause() {
        ((BarcodeKanojoApp) getApplication()).removeLocationUpdates();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        this.mProductAndKanojo.clear();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_close:
                close();
                return;
            case R.id.scan_others_2_company_name:
                showEditTextDialog(this.r.getString(R.string.common_product_company), this.mCompanyName);
                return;
            case R.id.scan_others_3_product_name:
                showEditTextDialog(this.r.getString(R.string.common_product_name), this.mProductName);
                return;
            case R.id.scan_others_4_category:
                showListDialog(this.r.getString(R.string.common_product_category), this.mProduct, this.mCategoryName);
                return;
            case R.id.scan_others_8_photo:
                showImagePickerDialog(this.r.getString(R.string.common_product_photo));
                return;
            case R.id.scan_others_9_comment:
                showEditTextDialog(this.r.getString(R.string.common_product_comment), this.mComment, 4);
                return;
            case R.id.scan_others_btn_save:
                this.mProduct.setCompany_name(this.mCompanyName.getValue());
                this.mProduct.setName(this.mProductName.getValue());
                this.mProduct.setComment(this.mComment.getValue());
                if (this.mKanojo.getRelation_status() == 1) {
                    this.mApiTask = new ApiTask(1, this.mProduct.getBarcode(), this.mProduct);
                    executeInspectionAndScanOthersTask(this.mApiTask);
                    return;
                }
                this.mApiTask = new ApiTask(2, this.mProduct.getBarcode(), this.mProduct);
                executeInspectionAndScanOthersTask(this.mApiTask);
                return;
            default:
		}
    }

    @Override
    public void onDismiss(DialogInterface dialog, int code) {
        if (this.mCompanyName.isEmpty() || this.mProductName.isEmpty()) {
            this.btnSave.setEnabled(false);
        } else {
            this.btnSave.setEnabled(true);
        }
		if (code == 200) {
			if (this.mApiTask == null) {
				return;
			}
			if (this.mApiTask.what == 2) {
				setResult(101);
				close();
			} else if (this.mApiTask.what == 1) {
				setResult(103);
				close();
			}
		}
	}
}
