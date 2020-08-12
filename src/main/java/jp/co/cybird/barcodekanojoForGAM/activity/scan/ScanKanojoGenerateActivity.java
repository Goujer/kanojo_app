package jp.co.cybird.barcodekanojoForGAM.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.goujer.barcodekanojo.activity.base.BaseKanojoEditActivity;

import java.io.File;
import java.util.Arrays;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.model.Category;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;
import jp.co.cybird.barcodekanojoForGAM.view.ProductAndKanojoView;

public class ScanKanojoGenerateActivity extends BaseKanojoEditActivity implements View.OnClickListener, DialogInterface.OnDismissListener {
    private static final String TAG = "ScanKanojoGenerateActivity";
    private Button btnSave;
    private boolean isDetailByAmazon;
    private EditItemView mCategoryName;
    private EditItemView mComment;
    private EditItemView mCompanyName;
    private Kanojo mKanojo;
    private EditItemView mKanojoName;
    private Product mProduct;
    private ProductAndKanojoView mProductAndKanojo;
    private EditItemView mProductName;

	//TODO make arrows include generated icons and perhaps barcode image.
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_generate);
        findViewById(R.id.edit_close).setOnClickListener(this);
        this.mProductAndKanojo = findViewById(R.id.scan_generate_photo);
        this.mKanojoName = findViewById(R.id.scan_generate_1_kanojo_name);
        this.mKanojoName.setOnClickListener(this);
        this.mCompanyName = findViewById(R.id.scan_generate_2_company_name);
        this.mCompanyName.setOnClickListener(this);
        this.mProductName = findViewById(R.id.scan_generate_3_product_name);
        this.mProductName.setOnClickListener(this);
        this.mCategoryName = findViewById(R.id.scan_generate_4_category);
        this.mCategoryName.setOnClickListener(this);
        EditItemView mBarcode = findViewById(R.id.scan_generate_5_barcode);
        mBarcode.setOnClickListener(this);
        findViewById(R.id.scan_generate_6_photo).setOnClickListener(this);
        this.mComment = findViewById(R.id.scan_generate_7_comment);
        this.mComment.setOnClickListener(this);
        this.btnSave = findViewById(R.id.scan_generate_btn_save);
        this.btnSave.setOnClickListener(this);
        this.btnSave.setEnabled(false);
        Bundle bundle = getIntent().getExtras();
        this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
        this.mProduct = (Product) bundle.get(BaseInterface.EXTRA_PRODUCT);
        this.isDetailByAmazon = bundle.getBoolean("isDetailByAmazon");
        if (this.mKanojo != null && this.mProduct != null) {
            mBarcode.setValue(this.mKanojo.getBarcode());
            String leftimgurl = null;
            RemoteResourceManager rrm = ((BarcodeKanojoApp) getApplication()).getRemoteResourceManager();
            if (this.isDetailByAmazon) {
                this.mKanojoName.setValue(this.mProduct.getName());
                this.mCompanyName.setValue(this.mProduct.getCompany_name());
                this.mProductName.setValue(this.mProduct.getProduct());
                int category_id = checkCategoryID(this.mProduct.getCategory());
                this.mProduct.setCategory_id(category_id);
                this.mCategoryName.setValue(this.mCategoryList[category_id - 1]);
                leftimgurl = this.mProduct.getProduct_image_url();
                ImageCache.requestImage(leftimgurl, rrm);
                if (!this.mKanojoName.isEmpty() && !this.mCompanyName.isEmpty() && !this.mProductName.isEmpty()) {
                    this.btnSave.setEnabled(true);
                }
            } else {
                Category c = getDefaultCategory();
                if (c != null) {
                    this.mCategoryName.setValue(c.getName());
                    this.mProduct.setCategory_id(c.getId());
                    this.mProduct.setCategory(c.getName());
                }
            }
            this.mProductAndKanojo.executeLoadImgTask(rrm, leftimgurl, this.mKanojo);
        }
    }

    private int checkCategoryID(String mCategoryName2) {
        int id = Arrays.asList(this.mCategoryList).indexOf(mCategoryName2);
        if (id == -1) {
            return this.mCategoryList.length;
        }
        return id + 1;
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_scan_generate, null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    protected void onResume() {
        super.onResume();
        ((BarcodeKanojoApp) getApplication()).requestLocationUpdates(true);
    }

    protected void onPause() {
        ((BarcodeKanojoApp) getApplication()).removeLocationUpdates();
        super.onPause();
    }

    protected void onDestroy() {
        this.mProductAndKanojo.clear();
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File f = getFile();
        if (f != null && f.exists()) {
            setBitmapFromFile(this.mProductAndKanojo, f);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_close:
                close();
                return;
            case R.id.scan_generate_1_kanojo_name:
                showEditTextDialog(this.r.getString(R.string.common_product_kanojo_name), this.mKanojoName);
                return;
            case R.id.scan_generate_2_company_name:
                showEditTextDialog(this.r.getString(R.string.common_product_company), this.mCompanyName);
                return;
            case R.id.scan_generate_3_product_name:
                showEditTextDialog(this.r.getString(R.string.common_product_name), this.mProductName);
                return;
            case R.id.scan_generate_4_category:
                showListDialog(this.r.getString(R.string.common_product_category), this.mProduct, this.mCategoryName);
                return;
            case R.id.scan_generate_6_photo:
                showImagePickerDialog(this.r.getString(R.string.common_product_photo));
                return;
            case R.id.scan_generate_7_comment:
                showEditTextDialog(this.r.getString(R.string.common_product_comment), this.mComment, 4);
                return;
            case R.id.scan_generate_btn_save:
                if (this.isDetailByAmazon && getFile() == null) {
                    setFile(ImageCache.saveImageBitmap(((BarcodeKanojoApp) getApplication()).getRemoteResourceManager(), this.mProduct.getProduct_image_url()));
                }
                executeInspectionAndGenerateTask(this.mKanojo.getBarcode(), this.mCompanyName.getValue(), this.mKanojoName.getValue(), this.mProductName.getValue(), this.mProduct.getCategory_id(), this.mComment.getValue(), null, this.mKanojo);
        }
    }

    public void onDismiss(DialogInterface dialog, int code) {
        if (this.mKanojoName.isEmpty() || this.mCompanyName.isEmpty() || this.mProductName.isEmpty()) {
            this.btnSave.setEnabled(false);
        } else {
            this.btnSave.setEnabled(true);
        }
    }
}
