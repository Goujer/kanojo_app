package jp.co.cybird.barcodekanojoForGAM.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.scan.ScanKanojoGenerateActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.scan.ScanOthersEditActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Barcode;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.Scanned;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.Live2dUtil;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;

public class ScanActivity extends BaseActivity implements View.OnClickListener {
    private static final boolean DEBUG = false;
    private static final int LONG_DELAY = 3500;
    private static final String TAG = "ScanActivity";
    private final int SCAN_RESULT_DEFAULT = 0;
    private final int SCAN_RESULT_FRIEND = 3;
    private final int SCAN_RESULT_GENERATE = 1;
    private final int SCAN_RESULT_KANOJO = 2;
    private final int SCAN_RESULT_OTHERS = 4;
    private Button btn01;
    private Button btn02;
    private Button btnClose;
    /* access modifiers changed from: private */
    public int code;
    private LayoutInflater inflater;
    /* access modifiers changed from: private */
    public Barcode mBarcode;
    private Bitmap mBitmap;
    private ImageView mBitmapView;
    /* access modifiers changed from: private */
    public Kanojo mKanojo;
    private RelativeLayout mLayoutThumb;
    private CustomLoadingView mLoadingView;
    /* access modifiers changed from: private */
    public MessageModel mMessages;
    /* access modifiers changed from: private */
    public Product mProduct;
    private ProgressBar mProgressBar;
    private ScanApiTask mScanApiTask;
    private ScanQueryTask mScanQueryTask;
    /* access modifiers changed from: private */
    public Scanned mScanned;
    /* access modifiers changed from: private */
    public String mStringMessage = "";
    /* access modifiers changed from: private */
    public int result = 0;
    private TextView txtMessage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        this.mLayoutThumb = (RelativeLayout) findViewById(R.id.scan_result_layout_thumb);
        this.txtMessage = (TextView) findViewById(R.id.scan_result_message);
        this.mProgressBar = (ProgressBar) findViewById(R.id.scan_result_progressbar);
        this.btnClose = (Button) findViewById(R.id.scan_result_close);
        this.btnClose.setOnClickListener(this);
        this.btn01 = (Button) findViewById(R.id.scan_result_btn_01);
        this.btn01.setOnClickListener(this);
        this.btn02 = (Button) findViewById(R.id.scan_result_btn_02);
        this.btn02.setOnClickListener(this);
        this.inflater = (LayoutInflater) getSystemService("layout_inflater");
        this.mLoadingView = (CustomLoadingView) findViewById(R.id.loadingView);
        startCaptureActivity();
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_scan_result, (ViewGroup) null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        ((BarcodeKanojoApp) getApplication()).requestLocationUpdates(true);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        ((BarcodeKanojoApp) getApplication()).removeLocationUpdates();
        if (this.mScanQueryTask != null) {
            this.mScanQueryTask.cancel(true);
            this.mScanQueryTask = null;
        }
        if (this.mScanApiTask != null) {
            this.mScanApiTask.cancel(true);
            this.mScanApiTask = null;
        }
        if (this.mBitmapView != null && isFinishing()) {
            this.mBitmapView.setImageDrawable((Drawable) null);
            this.mBitmapView.setBackgroundDrawable((Drawable) null);
        }
        if (this.mBitmap != null && isFinishing()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
        super.onPause();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.scan_result_close) {
            startCaptureActivity();
        }
        switch (this.result) {
            case 1:
                if (id == R.id.scan_result_btn_01) {
                    executeScanApiTask(new ApiTask(2, this.mBarcode.getBarcode(), (Product) null));
                    return;
                } else if (id == R.id.scan_result_btn_02) {
                    startCaptureActivity();
                    return;
                } else {
                    return;
                }
            case 2:
            case 3:
                if (id == R.id.scan_result_btn_01) {
                    startCaptureActivity();
                    return;
                } else if (id == R.id.scan_result_btn_02) {
                    Intent intent = new Intent().setClass(this, ScanOthersEditActivity.class);
                    if (this.mKanojo != null) {
                        intent.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                    }
                    if (this.mProduct != null) {
                        intent.putExtra(BaseInterface.EXTRA_PRODUCT, this.mProduct);
                    }
                    if (this.mScanned != null) {
                        intent.putExtra(BaseInterface.EXTRA_SCANNED, this.mScanned);
                    }
                    if (this.mMessages != null) {
                        intent.putExtra(MessageModel.NOTIFY_AMENDMENT_INFORMATION, (String) this.mMessages.get(MessageModel.NOTIFY_AMENDMENT_INFORMATION));
                    }
                    startActivityForResult(intent, BaseInterface.REQUEST_SCAN_OTHERS_EDIT);
                    return;
                } else {
                    return;
                }
            case 4:
                if (id == R.id.scan_result_btn_01) {
                    executeScanApiTask(new ApiTask(1, this.mBarcode.getBarcode(), this.mProduct));
                    return;
                } else if (id == R.id.scan_result_btn_02) {
                    Intent intent2 = new Intent().setClass(this, ScanOthersEditActivity.class);
                    if (this.mKanojo != null) {
                        intent2.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                    }
                    if (this.mProduct != null) {
                        intent2.putExtra(BaseInterface.EXTRA_PRODUCT, this.mProduct);
                    }
                    if (this.mScanned != null) {
                        intent2.putExtra(BaseInterface.EXTRA_SCANNED, this.mScanned);
                    }
                    if (this.mMessages != null) {
                        intent2.putExtra(MessageModel.NOTIFY_AMENDMENT_INFORMATION, (String) this.mMessages.get(MessageModel.NOTIFY_AMENDMENT_INFORMATION));
                    }
                    startActivityForResult(intent2, BaseInterface.REQUEST_SCAN_OTHERS_EDIT);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode == -1) {
            String contents = data.getStringExtra(Intents.Scan.RESULT);
            String stringExtra = data.getStringExtra(Intents.Scan.RESULT_FORMAT);
            executeScanQueryTask(contents);
        } else if (requestCode == 1020) {
            switch (resultCode) {
                case 102:
                    setResult(102);
                    close();
                    return;
                default:
                    startCaptureActivity();
                    return;
            }
        } else if (requestCode == 1014) {
            switch (resultCode) {
                case 101:
                    setResult(101);
                    close();
                    return;
                case 103:
                    setResult(103);
                    close();
                    return;
                default:
                    startCaptureActivity();
                    return;
            }
        } else {
            close();
        }
    }

    public void onDismiss(DialogInterface dialog, int code2) {
        startCaptureActivity();
    }

    /* access modifiers changed from: private */
    public void updateViews() {
        switch (this.result) {
            case 1:
                this.mLayoutThumb.setVisibility(View.VISIBLE);
                this.mProgressBar.setVisibility(View.GONE);
                this.txtMessage.setText(this.mStringMessage);
                View viewNewKanojo = this.inflater.inflate(R.layout.scan_result_new_kanojo, (ViewGroup) null);
                this.mBitmapView = (ImageView) viewNewKanojo.findViewById(R.id.scan_result_photo);
                if (this.mBitmapView != null) {
                    this.mBitmap = Live2dUtil.createSilhouette(getApplicationContext(), this.mKanojo, 75);
                    if (this.mBitmap != null) {
                        this.mBitmapView.setImageBitmap(this.mBitmap);
                    }
                }
                this.mLayoutThumb.addView(viewNewKanojo);
                break;
            case 2:
            case 3:
            case 4:
                this.mLayoutThumb.setVisibility(View.VISIBLE);
                this.mProgressBar.setVisibility(View.GONE);
                this.txtMessage.setText(this.mStringMessage);
                View view = this.inflater.inflate(R.layout.scan_result_others, (ViewGroup) null);
                this.mBitmapView = (ImageView) view.findViewById(R.id.scan_result_others_photo);
                if (this.mBitmapView != null) {
                    this.mBitmap = Live2dUtil.createNormalIcon(getApplicationContext(), this.mKanojo, this.mKanojo.getEmotion_status());
                    if (this.mBitmap != null) {
                        this.mBitmapView.setImageBitmap(this.mBitmap);
                    }
                }
                TextView txtName = (TextView) view.findViewById(R.id.scan_result_name);
                if (!(txtName == null || this.mKanojo == null)) {
                    txtName.setText(this.mKanojo.getName());
                }
                TextView txtProduct = (TextView) view.findViewById(R.id.scan_result_product_name);
                if (!(txtProduct == null || this.mProduct == null)) {
                    txtProduct.setText(this.mProduct.getName());
                }
                TextView txtBarcode = (TextView) view.findViewById(R.id.scan_result_barcode);
                if (!(txtBarcode == null || this.mBarcode == null)) {
                    txtBarcode.setText(this.mBarcode.getBarcode());
                }
                this.mLayoutThumb.addView(view);
                break;
            default:
                if (this.mLayoutThumb.getChildCount() > 0) {
                    this.mLayoutThumb.removeAllViews();
                }
                this.mLayoutThumb.setVisibility(View.GONE);
                this.mProgressBar.setVisibility(View.GONE);
                this.txtMessage.setText("");
                break;
        }
        switch (this.result) {
            case 1:
                this.btnClose.setVisibility(View.GONE);
                this.btn01.setVisibility(View.VISIBLE);
                this.btn02.setVisibility(View.VISIBLE);
                this.btn01.setText(R.string.scan_result_yes);
                this.btn02.setText(R.string.scan_result_cancel);
                return;
            case 2:
            case 3:
                this.btn01.setVisibility(View.VISIBLE);
                this.btn02.setVisibility(View.VISIBLE);
                this.btn01.setText(R.string.scan_result_ok);
                this.btn02.setText(R.string.scan_result_edit_productinfo);
                return;
            case 4:
                this.btn01.setVisibility(View.VISIBLE);
                this.btn02.setVisibility(View.VISIBLE);
                this.btn01.setText(R.string.scan_result_add_friend);
                this.btn02.setText(R.string.scan_result_edit_productinfo);
                return;
            default:
                this.btnClose.setVisibility(View.GONE);
                this.btn01.setVisibility(View.GONE);
                this.btn02.setVisibility(View.GONE);
                return;
        }
    }

    private void startCaptureActivity() {
        this.result = 0;
        updateViews();
        Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setAction(Intents.Scan.ACTION);
        startActivityForResult(intent, BaseInterface.REQUEST_SCAN);
    }

    private void executeScanQueryTask(String barcode) {
        if (barcode != null && !barcode.equals("")) {
            if (this.mScanQueryTask == null || this.mScanQueryTask.getStatus() == AsyncTask.Status.FINISHED || this.mScanQueryTask.cancel(true) || this.mScanQueryTask.isCancelled()) {
                this.mScanQueryTask = (ScanQueryTask) new ScanQueryTask().execute(new String[]{barcode});
            }
        }
    }

    class ScanQueryTask extends AsyncTask<String, Void, Response<?>> {
        private Exception mReason = null;

        ScanQueryTask() {
        }

        public void onPreExecute() {
            ScanActivity.this.showProgressDialog();
        }

        public Response<?> doInBackground(String... params) {
            if (params == null || params.length == 0) {
                return null;
            }
            try {
                return process(params[0]);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
                switch (ScanActivity.this.getCodeAndShowDialog(response, this.mReason)) {
                    case 200:
                        ScanActivity.this.mKanojo = (Kanojo) response.get(Kanojo.class);
                        ScanActivity.this.mBarcode = (Barcode) response.get(Barcode.class);
                        ScanActivity.this.mProduct = (Product) response.get(Product.class);
                        ScanActivity.this.mScanned = (Scanned) response.get(Scanned.class);
                        ScanActivity.this.mMessages = (MessageModel) response.get(MessageModel.class);
                        if (ScanActivity.this.mKanojo == null) {
                            if (ScanActivity.this.mBarcode != null) {
                                ScanActivity.this.mKanojo = new Kanojo(ScanActivity.this.mBarcode);
                                ScanActivity.this.mKanojo.setLove_gauge(85);
                                ScanActivity.this.mProduct = new Product();
                                ScanActivity.this.result = 1;
                                ScanActivity.this.mStringMessage = (String) ScanActivity.this.mMessages.get(MessageModel.DO_GENERATE_KANOJO);
                                break;
                            } else {
                                throw new BarcodeKanojoException("Barcode Model is null");
                            }
                        } else {
                            switch (ScanActivity.this.mKanojo.getRelation_status()) {
                                case 1:
                                    ScanActivity.this.result = 4;
                                    ScanActivity.this.mStringMessage = (String) ScanActivity.this.mMessages.get(MessageModel.DO_ADD_FRIEND);
                                    break;
                                case 2:
                                    ScanActivity.this.result = 2;
                                    ScanActivity.this.mStringMessage = (String) ScanActivity.this.mMessages.get(MessageModel.INFORM_GIRLFRIEND);
                                    break;
                                case 3:
                                    ScanActivity.this.result = 3;
                                    ScanActivity.this.mStringMessage = (String) ScanActivity.this.mMessages.get(MessageModel.INFORM_FRIEND);
                                    break;
                                default:
                                    ScanActivity.this.result = 0;
                                    break;
                            }
                        }
                    case 502:
                        ScanActivity.this.showToast(ScanActivity.this.getResources().getString(R.string.error_internet));
                        TimerTask task = new TimerTask() {
                            public void run() {
                                ScanActivity.this.close();
                            }
                        };
                        Timer timer = new Timer();
                        Log.d("NguyenTT", "time: 3500");
                        timer.schedule(task, 3500);
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
                ScanActivity.this.dismissProgressDialog();
                ScanActivity.this.updateViews();
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            ScanActivity.this.dismissProgressDialog();
        }

        /* access modifiers changed from: package-private */
        public Response<?> process(String barcode) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojoApp barcodeKanojoApp = (BarcodeKanojoApp) ScanActivity.this.getApplication();
            BarcodeKanojo barcodeKanojo = barcodeKanojoApp.getBarcodeKanojo();
            Location loc = barcodeKanojoApp.getLastKnownLocation();
            if (loc == null) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                loc = barcodeKanojoApp.getLastKnownLocation();
            }
            return barcodeKanojo.query(barcode, GeoUtil.LocationToGeo(loc));
        }
    }

    private void executeScanApiTask(ApiTask task) {
        if (this.mScanApiTask == null || this.mScanApiTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mScanApiTask = new ScanApiTask();
            this.mScanApiTask.setTask(task);
            this.mScanApiTask.execute(new Void[0]);
        }
    }

    class ScanApiTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;
        private ApiTask mTask;

        ScanApiTask() {
        }

        public void setTask(ApiTask task) {
            this.mTask = task;
        }

        public void onPreExecute() {
            ScanActivity.this.showProgressDialog();
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process(this.mTask);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
                Log.d(ScanActivity.TAG, new StringBuilder().append(response.getCode()).toString());
                Log.d(ScanActivity.TAG, new StringBuilder().append(response.getAlert()).toString());
                if (response.getCode() != 200 || response.getAlert() == null) {
                    ScanActivity.this.code = ScanActivity.this.getCodeAndShowAlert(response, this.mReason);
                } else {
                    ScanActivity.this.code = ScanActivity.this.getCodeAndShowAlert(response, this.mReason, new BaseActivity.OnDialogDismissListener() {
                        public void onDismiss(DialogInterface dialog, int code) {
                            ScanActivity.this.close();
                        }
                    });
                }
                switch (ScanActivity.this.code) {
                    case 200:
                        User user = (User) response.get(User.class);
                        if (user != null) {
                            ((BarcodeKanojoApp) ScanActivity.this.getApplication()).getBarcodeKanojo().setUser(user);
                        }
                        ScanActivity.this.dismissProgressDialog();
                        if (this.mTask.what != 2) {
                            if (this.mTask.what == 1) {
                                ScanActivity.this.setResult(103);
                                if (response.getAlert() == null) {
                                    ScanActivity.this.close();
                                    break;
                                }
                            }
                        } else {
                            Product product = (Product) response.get(Product.class);
                            Intent intent = new Intent().setClass(ScanActivity.this, ScanKanojoGenerateActivity.class);
                            if (ScanActivity.this.mKanojo != null) {
                                intent.putExtra(BaseInterface.EXTRA_KANOJO, ScanActivity.this.mKanojo);
                            }
                            if (product != null) {
                                intent.putExtra(BaseInterface.EXTRA_PRODUCT, product);
                                intent.putExtra("isDetailByAmazon", true);
                            } else if (ScanActivity.this.mProduct != null) {
                                intent.putExtra(BaseInterface.EXTRA_PRODUCT, ScanActivity.this.mProduct);
                                intent.putExtra("isDetailByAmazon", false);
                            }
                            ScanActivity.this.startActivityForResult(intent, BaseInterface.REQUEST_SCAN_KANOJO_GENERATE);
                            break;
                        }
                        break;
                    case 400:
                    case 401:
                    case 403:
                    case 404:
                    case 500:
                    case 503:
                        ScanActivity.this.dismissProgressDialog();
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
                ScanActivity.this.dismissProgressDialog();
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            ScanActivity.this.dismissProgressDialog();
        }

        /* access modifiers changed from: package-private */
        public Response<?> process(ApiTask task) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojoApp barcodeKanojoApp = (BarcodeKanojoApp) ScanActivity.this.getApplication();
            BarcodeKanojo barcodeKanojo = barcodeKanojoApp.getBarcodeKanojo();
            Location loc = barcodeKanojoApp.getLastKnownLocation();
            if (loc == null) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                loc = barcodeKanojoApp.getLastKnownLocation();
            }
            if (task == null) {
                throw new BarcodeKanojoException("Task is null");
            }
            switch (task.what) {
                case 1:
                    if (task.barcode == null || task.product == null) {
                        return null;
                    }
                    return barcodeKanojo.scan(task.barcode, task.product.getCompany_name(), task.product.getName(), task.product.getCategory_id(), (String) null, (File) null, GeoUtil.LocationToGeo(loc));
                case 2:
                    if (task.barcode != null) {
                        return barcodeKanojo.decrease_generating(task.barcode);
                    }
                    return null;
                default:
                    return null;
            }
        }
    }

    static class ApiTask {
        public static final int API_DECREASE = 2;
        public static final int API_SCAN = 1;
        String barcode;
        Product product;
        int what;

        public ApiTask(int what2, String barcode2, Product product2) {
            this.what = what2;
            this.barcode = barcode2;
            this.product = product2;
        }
    }

    /* access modifiers changed from: protected */
    public int getCodeAndShowDialog(Response<?> response, Exception e) throws BarcodeKanojoException {
        if (response != null) {
            return getCodeAndShowAlert(response, e);
        }
        this.code = 502;
        return this.code;
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
