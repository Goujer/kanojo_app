package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.mediation.nend.NendAdapterExtras;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.adapter.KanojoInfoAdapter;
import jp.co.cybird.barcodekanojoForGAM.adapter.KanojoInfoImgAdapter;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.Scanned;
import jp.co.cybird.barcodekanojoForGAM.core.util.Digest;
import jp.co.cybird.barcodekanojoForGAM.core.util.HttpUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.view.MoreBtnView;

public class KanojoInfoActivity extends BaseActivity implements View.OnClickListener, MoreBtnView.OnMoreClickListener {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_LIMIT = 6;
    private static final String TAG = "KanojoInfoActivity";
    private final int MORE_ACTIVITIES = 11;
    private AdView adView;
    private Button btnClose;
    private Button btnEdit;
    /* access modifiers changed from: private */
    public ModelList<ActivityModel> mActivities;
    /* access modifiers changed from: private */
    public int mActivityCount = 0;
    private KanojoInfoAdapter mAdapter;
    /* access modifiers changed from: private */
    public MoreBtnView mFooter;
    private Gallery mGallery;
    /* access modifiers changed from: private */
    public KanojoInfoImgAdapter mImgAdapter;
    private Kanojo mKanojo;
    private KanojoInfoTask mKanojoInfoTask;
    /* access modifiers changed from: private */
    public int mLimit = 6;
    private ListView mListView;
    private String mMessage;
    /* access modifiers changed from: private */
    public Product mProduct;
    /* access modifiers changed from: private */
    public ImageView mProductImg;
    /* access modifiers changed from: private */
    public RemoteResourceManager mRrm;
    private Scanned mScanned;
    private Resources r;
    /* access modifiers changed from: private */
    public boolean readAllFlg = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanojo_info);
        this.r = getResources();
        setBanner();
        this.mRrm = ((BarcodeKanojoApp) getApplication()).getRemoteResourceManager();
        this.btnClose = (Button) findViewById(R.id.kanojo_info_close);
        this.btnClose.setOnClickListener(this);
        this.btnEdit = (Button) findViewById(R.id.kanojo_info_edit);
        this.btnEdit.setOnClickListener(this);
        this.mListView = (ListView) findViewById(R.id.kanojo_info_list);
        this.mFooter = new MoreBtnView(this);
        this.mFooter.setOnMoreClickListener(11, this);
        this.mAdapter = new KanojoInfoAdapter(this, this.mRrm);
        this.mListView.addFooterView(this.mFooter);
        this.mListView.setAdapter(this.mAdapter);
        this.mActivities = new ModelList<>();
        Bundle bundle = getIntent().getExtras();
        this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
        this.mProduct = (Product) bundle.get(BaseInterface.EXTRA_PRODUCT);
        this.mScanned = (Scanned) bundle.get(BaseInterface.EXTRA_SCANNED);
        this.mMessage = bundle.getString(MessageModel.NOTIFY_AMENDMENT_INFORMATION);
        if (this.mKanojo != null && this.mProduct != null && this.mScanned != null) {
            if (this.mKanojo.getRelation_status() == 1) {
                this.btnEdit.setVisibility(8);
                this.btnEdit.setEnabled(false);
            }
            initProductView();
            initGalleryView();
            ImageCache.setImageAndRequest(getApplicationContext(), this.mProductImg, this.mProduct.getProduct_image_url(), this.mRrm, R.drawable.common_noimage_product);
            String strUrl = this.mProduct.getProduct_image_url();
            if (strUrl != null && HttpUtil.isUrl(strUrl)) {
                this.mImgAdapter.addImgUrl(strUrl);
            }
            executeKanojoInfoTask();
        }
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_kanojo_info, (ViewGroup) null);
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
        this.btnEdit.setOnClickListener(this);
    }

    public void unBindEvent() {
        this.btnClose.setOnClickListener((View.OnClickListener) null);
        this.btnEdit.setOnClickListener((View.OnClickListener) null);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mKanojoInfoTask != null) {
            this.mKanojoInfoTask.cancel(true);
            this.mKanojoInfoTask = null;
        }
        if (isFinishing()) {
            this.mAdapter.removeObserver();
            if (this.mImgAdapter != null) {
                this.mImgAdapter.removeObserver();
            }
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.adView != null) {
            this.adView.destroy();
        }
        this.mImgAdapter.removeObserver();
        this.mImgAdapter = null;
        this.mAdapter.removeObserver();
        this.mAdapter.clear();
        this.mAdapter = null;
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kanojo_info_close:
                close();
                return;
            case R.id.kanojo_info_edit:
                unBindEvent();
                Intent intent = new Intent(this, KanojoEditActivity.class);
                if (this.mKanojo != null) {
                    intent.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                }
                if (this.mProduct != null) {
                    intent.putExtra(BaseInterface.EXTRA_PRODUCT, this.mProduct);
                }
                if (this.mScanned != null) {
                    intent.putExtra(BaseInterface.EXTRA_SCANNED, this.mScanned);
                }
                if (this.mMessage != null) {
                    intent.putExtra(MessageModel.NOTIFY_AMENDMENT_INFORMATION, this.mMessage);
                }
                startActivityForResult(intent, BaseInterface.REQUEST_KANOJO_EDIT);
                return;
            default:
                return;
        }
    }

    public void onMoreClick(int id) {
        if (id == 11) {
            executeKanojoInfoTask();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            switch (requestCode) {
                case BaseInterface.REQUEST_KANOJO_EDIT:
                    setResult(101, data);
                    close();
                    return;
                default:
                    return;
            }
        }
    }

    private void initProductView() {
        TextView txtName = (TextView) findViewById(R.id.kanojo_info_name);
        if (txtName != null) {
            txtName.setText(this.mProduct.getName());
        }
        TextView txtComapnyName = (TextView) findViewById(R.id.kanojo_info_company_name);
        if (txtComapnyName != null) {
            txtComapnyName.setText(this.mProduct.getCompany_name());
        }
        TextView txtCountry = (TextView) findViewById(R.id.kanojo_info_country);
        if (txtCountry != null) {
            txtCountry.setText(String.valueOf(this.r.getString(R.string.kanojo_info_country)) + this.mProduct.getCountry());
        }
        TextView txtBarcode = (TextView) findViewById(R.id.kanojo_info_barcode);
        if (txtBarcode != null) {
            txtBarcode.setText(String.valueOf(this.r.getString(R.string.kanojo_info_barcode)) + "************");
        }
        TextView txtCategory = (TextView) findViewById(R.id.kanojo_info_category);
        if (txtCategory != null) {
            txtCategory.setText(String.valueOf(this.r.getString(R.string.kanojo_info_category)) + this.mProduct.getCategory());
        }
        TextView txtScanned = (TextView) findViewById(R.id.kanojo_info_scanned);
        if (txtScanned != null) {
            txtScanned.setText(String.valueOf(this.r.getString(R.string.kanojo_info_scanned)) + this.mProduct.getScan_count());
        }
    }

    private void initGalleryView() {
        this.mProductImg = (ImageView) findViewById(R.id.kanojo_info_product_img);
        this.mGallery = new Gallery(this);
        Display disp = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        this.mGallery.setSpacing(10);
        int width = disp.getWidth();
        int h = (int) this.r.getDimension(R.dimen.kanojo_info_gallery_height);
        int p = (int) this.r.getDimension(R.dimen.kanojo_info_padding);
        int d = (int) this.r.getDimension(R.dimen.kanojo_info_gallery_left_margin);
        int w = width - (p * 2);
        ((FrameLayout) findViewById(R.id.kanojo_info_layout_gallery)).addView(this.mGallery, w * 2, h);
        this.mGallery.scrollTo(w - d, 0);
        this.mImgAdapter = new KanojoInfoImgAdapter(this, this.mRrm);
        this.mGallery.setAdapter(this.mImgAdapter);
        this.mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View parent, int position, long id) {
                ImageCache.setImage(KanojoInfoActivity.this.mProductImg, KanojoInfoActivity.this.mImgAdapter.getItemUrl(position), KanojoInfoActivity.this.mRrm, R.drawable.common_noimage_product);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateListItem() {
        this.mAdapter.setModelList(this.mActivities);
        if (this.readAllFlg) {
            this.mListView.removeFooterView(this.mFooter);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    private void executeKanojoInfoTask() {
        if (!this.readAllFlg) {
            if (this.mKanojoInfoTask == null || this.mKanojoInfoTask.getStatus() == AsyncTask.Status.FINISHED) {
                this.mLimit = 6;
                this.mKanojoInfoTask = (KanojoInfoTask) new KanojoInfoTask().execute(new Void[0]);
            }
        }
    }

    class KanojoInfoTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        KanojoInfoTask() {
        }

        public void onPreExecute() {
            KanojoInfoActivity.this.mFooter.setLoading(true);
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
                switch (KanojoInfoActivity.this.getCodeAndShowAlert(response, this.mReason)) {
                    case 200:
                        ModelList<ActivityModel> temp = response.getActivityModelList();
                        if (temp != null) {
                            int size = temp.size();
                            if (size != 0) {
                                if (size < KanojoInfoActivity.this.mLimit) {
                                    KanojoInfoActivity.this.readAllFlg = true;
                                }
                                KanojoInfoActivity kanojoInfoActivity = KanojoInfoActivity.this;
                                kanojoInfoActivity.mActivityCount = kanojoInfoActivity.mActivityCount + size;
                                KanojoInfoActivity.this.mActivities.addAll(temp);
                                KanojoInfoActivity.this.addImgToGallery(temp);
                            } else {
                                KanojoInfoActivity.this.readAllFlg = true;
                            }
                        } else {
                            KanojoInfoActivity.this.readAllFlg = true;
                        }
                        KanojoInfoActivity.this.updateListItem();
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
                KanojoInfoActivity.this.mFooter.setLoading(false);
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            KanojoInfoActivity.this.mFooter.setLoading(false);
        }

        /* access modifiers changed from: package-private */
        public Response<?> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            return ((BarcodeKanojoApp) KanojoInfoActivity.this.getApplication()).getBarcodeKanojo().scanned_timeline(KanojoInfoActivity.this.mProduct.getBarcode(), 0, KanojoInfoActivity.this.mActivityCount, KanojoInfoActivity.this.mLimit);
        }
    }

    /* access modifiers changed from: private */
    public void addImgToGallery(ModelList<ActivityModel> l) {
        Iterator it = l.iterator();
        while (it.hasNext()) {
            String strUrl = ((ActivityModel) it.next()).getScanned().getProduct_image_url();
            if (strUrl != null && HttpUtil.isUrl(strUrl)) {
                this.mImgAdapter.addImgUrl(strUrl);
            }
        }
    }

    @SuppressLint({"DefaultLocale"})
    private void setBanner() {
        this.adView = new AdView((Activity) this, AdSize.BANNER, getResources().getString(R.string.admob_mediation_id));
        ((LinearLayout) findViewById(R.id.adView)).addView(this.adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setNetworkExtras(new NendAdapterExtras());
        this.adView.loadAd(adRequest);
    }

    public static final String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance(Digest.MD5);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(b & 255);
                while (h.length() < 2) {
                    h = GreeDefs.BARCODE + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.v(TAG, e.toString());
            return "";
        }
    }
}
