package jp.co.cybird.barcodekanojoForGAM.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import com.goujer.barcodekanojo.activity.scan.ScanKanojoGenerateActivity;
import com.goujer.barcodekanojo.base.PermissionRequestCode;

import jp.co.cybird.barcodekanojoForGAM.activity.scan.ScanOthersEditActivity;
import com.goujer.barcodekanojo.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Barcode;
import com.goujer.barcodekanojo.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import com.goujer.barcodekanojo.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.Live2dUtil;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;

public class ScanActivity extends BaseActivity implements View.OnClickListener {
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
    private int code;
    private LayoutInflater inflater;
    private Barcode mBarcode;
    private Bitmap mBitmap;
    private ImageView mBitmapView;
    private Kanojo mKanojo;
    private RelativeLayout mLayoutThumb;
    private CustomLoadingView mLoadingView;
    private MessageModel mMessages;
    private Product mProduct;
    private ProgressBar mProgressBar;
    private ScanApiTask mScanApiTask;
    private ScanQueryTask mScanQueryTask;
    private String mStringMessage = "";
    private int result = 0;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        this.mLayoutThumb = findViewById(R.id.scan_result_layout_thumb);
        this.txtMessage = findViewById(R.id.scan_result_message);
        this.mProgressBar = findViewById(R.id.scan_result_progressbar);
        this.btnClose = findViewById(R.id.scan_result_close);
        this.btnClose.setOnClickListener(this);
        this.btn01 = findViewById(R.id.scan_result_btn_01);
        this.btn01.setOnClickListener(this);
        this.btn02 = findViewById(R.id.scan_result_btn_02);
        this.btn02.setOnClickListener(this);
        this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.mLoadingView = findViewById(R.id.loadingView);
		if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
			requestPermissions(new String[] { Manifest.permission.CAMERA }, PermissionRequestCode.SCAN_ACTIVITY);
		} else {
			startCaptureActivity();
		}
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((BarcodeKanojoApp) getApplication()).requestLocationUpdates(true);
    }

    @Override
    protected void onPause() {
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
            this.mBitmapView.setImageDrawable(null);
            this.mBitmapView.setBackgroundDrawable(null);
        }
        if (this.mBitmap != null && isFinishing()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.scan_result_close) {
            startCaptureActivity();
        }
        switch (this.result) {
            case SCAN_RESULT_GENERATE:
                if (id == R.id.scan_result_btn_01) {
                    executeScanApiTask(new ApiTask(2, this.mBarcode.getBarcode(), null));
                    return;
                } else if (id == R.id.scan_result_btn_02) {
                    startCaptureActivity();
                    return;
                } else {
                    return;
                }
            case SCAN_RESULT_KANOJO:
            case SCAN_RESULT_FRIEND:
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
                    if (this.mMessages != null) {
                        intent.putExtra(MessageModel.NOTIFY_AMENDMENT_INFORMATION, this.mMessages.get(MessageModel.NOTIFY_AMENDMENT_INFORMATION));
                    }
                    startActivityForResult(intent, BaseInterface.REQUEST_SCAN_OTHERS_EDIT);
                    return;
                } else {
                    return;
                }
            case SCAN_RESULT_OTHERS:
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
                    if (this.mMessages != null) {
                        intent2.putExtra(MessageModel.NOTIFY_AMENDMENT_INFORMATION, this.mMessages.get(MessageModel.NOTIFY_AMENDMENT_INFORMATION));
                    }
                    startActivityForResult(intent2, BaseInterface.REQUEST_SCAN_OTHERS_EDIT);
                    return;
                } else {
                    return;
                }
            default:
		}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseInterface.REQUEST_SCAN && resultCode == -1) {
            String contents = data.getStringExtra(Intents.Scan.RESULT);
            String format = data.getStringExtra(Intents.Scan.RESULT_FORMAT);
            switch (format) {
				case "UPC_A":
				case "UPC_E":
					switch (contents.charAt(6)) {
						case '0':
						case '1':
						case '2': {
							contents = contents.substring(0, 3) + contents.charAt(6) + "0000" + contents.substring(3, 6) + contents.charAt(7);
							break;
						}
						case '3': {
							contents = contents.substring(0, 4) + "00000" + contents.substring(4, 6) + contents.charAt(7);
							break;
						}
						case '4': {
							contents = contents.substring(0, 5) + "00000" + contents.charAt(5) + contents.charAt(7);
							break;
						}
						case '5':
						case '6':
						case '7':
						case '8':
						case '9': {
							contents = contents.substring(0, 6) + "0000" + contents.charAt(6) + contents.charAt(7);
							break;
						}
					}
				case "EAN_8":
					contents = "00000" + contents;
				case "EAN_13":
					executeScanQueryTask(contents);
					break;
				default:
					showNoticeDialog(getResources().getString(R.string.error_barcode_format)+" Found: "+format, dialog -> startCaptureActivity());
			}

        } else if (requestCode == BaseInterface.REQUEST_SCAN_KANOJO_GENERATE) {
			if (resultCode == BaseInterface.RESULT_GENERATE_KANOJO) {
				setResult(BaseInterface.RESULT_GENERATE_KANOJO);
				finish();
			} else {
				startCaptureActivity();
			}
		} else if (requestCode == BaseInterface.REQUEST_SCAN_OTHERS_EDIT) {
            switch (resultCode) {
				case BaseInterface.RESULT_SAVE_PRODUCT_INFO:
					setResult(BaseInterface.RESULT_SAVE_PRODUCT_INFO);
                    finish();
                    return;
				case BaseInterface.RESULT_ADD_FRIEND:
					setResult(BaseInterface.RESULT_ADD_FRIEND);
                    finish();
                    return;
                default:
                    startCaptureActivity();
			}
        } else {
            finish();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog, int code2) {
        //startCaptureActivity();
		Log.d(TAG, "OnDismiss()");
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    	switch (requestCode) {
			case PermissionRequestCode.SCAN_ACTIVITY:
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					startCaptureActivity();
				}  else {
					// Explain to the user that the feature is unavailable because
					// the features requires a permission that the user has denied.
					// At the same time, respect the user's decision. Don't link to
					// system settings in an effort to convince the user to change
					// their decision.
					showNoticeDialog("Scanning functionality will not be avaliable.", new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							finish();
						}});
					//TODO: Make this proper string.
				}
				return;
		}
	}

	@Override
	protected View getClientView() {
		View layout = getLayoutInflater().inflate(R.layout.activity_scan_result, null);
		LinearLayout appLayoutRoot = new LinearLayout(this);
		appLayoutRoot.addView(layout);
		return appLayoutRoot;
	}

    private void updateViews() {
        switch (this.result) {
            case SCAN_RESULT_GENERATE:
                this.mLayoutThumb.setVisibility(View.VISIBLE);
                this.mProgressBar.setVisibility(View.GONE);
                this.txtMessage.setText(this.mStringMessage);
                View viewNewKanojo = this.inflater.inflate(R.layout.scan_result_new_kanojo, null);
                this.mBitmapView = viewNewKanojo.findViewById(R.id.scan_result_photo);
                if (this.mBitmapView != null) {
                    this.mBitmap = Live2dUtil.createSilhouette(getApplicationContext(), this.mKanojo, 75);
                    if (this.mBitmap != null) {
                        this.mBitmapView.setImageBitmap(this.mBitmap);
                    }
                }
                this.mLayoutThumb.addView(viewNewKanojo);
                break;
            case SCAN_RESULT_KANOJO:
            case SCAN_RESULT_FRIEND:
            case SCAN_RESULT_OTHERS:
                this.mLayoutThumb.setVisibility(View.VISIBLE);
                this.mProgressBar.setVisibility(View.GONE);
                this.txtMessage.setText(this.mStringMessage);
                View view = this.inflater.inflate(R.layout.scan_result_others, null);
                this.mBitmapView = view.findViewById(R.id.scan_result_others_photo);
                if (this.mBitmapView != null) {
                    this.mBitmap = Live2dUtil.createNormalIcon(getApplicationContext(), this.mKanojo, this.mKanojo.getEmotion_status());
                    if (this.mBitmap != null) {
                        this.mBitmapView.setImageBitmap(this.mBitmap);
                    }
                }
                TextView txtName = view.findViewById(R.id.scan_result_name);
                if (!(txtName == null || this.mKanojo == null)) {
                    txtName.setText(this.mKanojo.getName());
                }
                TextView txtProduct = view.findViewById(R.id.scan_result_product_name);
                if (!(txtProduct == null || this.mProduct == null)) {
                    txtProduct.setText(this.mProduct.getName());
                }
                TextView txtBarcode = view.findViewById(R.id.scan_result_barcode);
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
            case SCAN_RESULT_GENERATE:
                this.btnClose.setVisibility(View.GONE);
                this.btn01.setVisibility(View.VISIBLE);
                this.btn02.setVisibility(View.VISIBLE);
                this.btn01.setText(R.string.scan_result_yes);
                this.btn02.setText(R.string.scan_result_cancel);
                return;
            case SCAN_RESULT_KANOJO:
            case SCAN_RESULT_FRIEND:
                this.btn01.setVisibility(View.VISIBLE);
                this.btn02.setVisibility(View.VISIBLE);
                this.btn01.setText(R.string.scan_result_ok);
                this.btn02.setText(R.string.scan_result_edit_productinfo);
                return;
            case SCAN_RESULT_OTHERS:
                this.btn01.setVisibility(View.VISIBLE);
                this.btn02.setVisibility(View.VISIBLE);
                this.btn01.setText(R.string.scan_result_add_friend);
                this.btn02.setText(R.string.scan_result_edit_productinfo);
                return;
            default:
                this.btnClose.setVisibility(View.GONE);
                this.btn01.setVisibility(View.GONE);
                this.btn02.setVisibility(View.GONE);
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

		@Override
        public void onPreExecute() {
            ScanActivity.this.showProgressDialog();
        }

        @Override
        protected Response<?> doInBackground(String... params) {
            if (params == null || params.length == 0) {
                return null;
            }
            try {
	            BarcodeKanojoApp barcodeKanojoApp = (BarcodeKanojoApp) ScanActivity.this.getApplication();
	            BarcodeKanojo barcodeKanojo = barcodeKanojoApp.getBarcodeKanojo();
	            return barcodeKanojo.query(params[0]);  //Check if Kanojo exists with this Barcode.
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<?> response) {
            try {
                switch (ScanActivity.this.getCodeAndShowDialog(response, this.mReason)) {
                    case Response.CODE_SUCCESS:
                        ScanActivity.this.mKanojo = (Kanojo) response.get(Kanojo.class);
                        ScanActivity.this.mBarcode = (Barcode) response.get(Barcode.class);
                        ScanActivity.this.mProduct = (Product) response.get(Product.class);
                        ScanActivity.this.mMessages = (MessageModel) response.get(MessageModel.class);
                        if (ScanActivity.this.mKanojo == null) {
                            if (ScanActivity.this.mBarcode != null) {
                                ScanActivity.this.mKanojo = new Kanojo(ScanActivity.this.mBarcode);
                                ScanActivity.this.mKanojo.setLove_gauge(85);
                                ScanActivity.this.mProduct = new Product();
                                ScanActivity.this.result = 1;
                                ScanActivity.this.mStringMessage = ScanActivity.this.mMessages.get(MessageModel.DO_GENERATE_KANOJO);
                                break;
                            } else {
                                throw new BarcodeKanojoException("Barcode Model is null");
                            }
                        } else {
                            switch (ScanActivity.this.mKanojo.getRelation_status()) {
								case Kanojo.RELATION_OTHER:
                                    ScanActivity.this.result = 4;
                                    ScanActivity.this.mStringMessage = ScanActivity.this.mMessages.get(MessageModel.DO_ADD_FRIEND);
                                    break;
								case Kanojo.RELATION_KANOJO:
                                    ScanActivity.this.result = 2;
                                    ScanActivity.this.mStringMessage = ScanActivity.this.mMessages.get(MessageModel.INFORM_GIRLFRIEND);
                                    break;
								case Kanojo.RELATION_FRIEND:
                                    ScanActivity.this.result = 3;
                                    ScanActivity.this.mStringMessage = ScanActivity.this.mMessages.get(MessageModel.INFORM_FRIEND);
                                    break;
                                default:
                                    ScanActivity.this.result = 0;
                                    break;
                            }
                        }
                        break;
                    case Response.CODE_ERROR_NETWORK:
                        ScanActivity.this.showToast(ScanActivity.this.getResources().getString(R.string.error_internet));
                        TimerTask task = new TimerTask() {
                            public void run() {
                                ScanActivity.this.close();
                            }
                        };
                        Timer timer = new Timer();
                        Log.d(TAG, "time: 3500");
                        timer.schedule(task, 3500);
                        break;
                }
            } catch (BarcodeKanojoException e) {
            	if (Defs.DEBUG) e.printStackTrace();
            } finally {
                ScanActivity.this.dismissProgressDialog();
                ScanActivity.this.updateViews();
            }
        }

        @Override
        protected void onCancelled() {
            ScanActivity.this.dismissProgressDialog();
        }
    }

    private void executeScanApiTask(ApiTask task) {
        if (this.mScanApiTask == null || this.mScanApiTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mScanApiTask = new ScanApiTask();
            this.mScanApiTask.setTask(task);
            this.mScanApiTask.execute();
        }
    }

    class ScanApiTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;
        private ApiTask mTask;

        void setTask(ApiTask task) {
            this.mTask = task;
        }

        @Override
        protected void onPreExecute() {
            ScanActivity.this.showProgressDialog();
        }

        @Override
        protected Response<?> doInBackground(Void... params) {
            try {
				BarcodeKanojoApp barcodeKanojoApp = (BarcodeKanojoApp) ScanActivity.this.getApplication();
				BarcodeKanojo barcodeKanojo = barcodeKanojoApp.getBarcodeKanojo();
				Location loc = barcodeKanojoApp.getLastKnownLocation();
				if (loc == null) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException ignored) {
					}
					loc = barcodeKanojoApp.getLastKnownLocation();
				}
				if (this.mTask == null) {
					throw new BarcodeKanojoException("Task is null");
				}
				switch (this.mTask.what) {
					case ApiTask.API_SCAN:
						if (this.mTask.barcode == null || this.mTask.product == null) {
							return null;
						}
						return barcodeKanojo.scan(this.mTask.barcode, this.mTask.product.getCompany_name(), this.mTask.product.getName(), this.mTask.product.getCategory_id(), null, null, loc);
					case ApiTask.API_DECREASE:
						if (this.mTask.barcode != null) {
							return barcodeKanojo.decrease_generating(this.mTask.barcode);
						}
						return null;
					default:
						return null;
				}
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<?> response) {
            try {
                if (response.getCode() != Response.CODE_SUCCESS || response.getAlert() == null) {
                    ScanActivity.this.code = ScanActivity.this.getCodeAndShowAlert(response, this.mReason);
                } else {
                    ScanActivity.this.code = ScanActivity.this.getCodeAndShowAlert(response, this.mReason, (dialog, code) -> finish());
                }
                switch (ScanActivity.this.code) {
					case Response.CODE_SUCCESS:
                        User user = (User) response.get(User.class);
                        if (user != null) {
                            ((BarcodeKanojoApp) ScanActivity.this.getApplication()).getBarcodeKanojo().setUser(user);
                        }
                        ScanActivity.this.dismissProgressDialog();
                        if (this.mTask.what != ApiTask.API_DECREASE) {
                            if (this.mTask.what == ApiTask.API_SCAN) {
                                ScanActivity.this.setResult(BaseInterface.RESULT_ADD_FRIEND);
                                if (response.getAlert() == null) {
                                    finish();
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
            } catch (BarcodeKanojoException ignored) {
            } finally {
                ScanActivity.this.dismissProgressDialog();
            }
        }

        @Override
        protected void onCancelled() {
            ScanActivity.this.dismissProgressDialog();
        }
    }

    static class ApiTask {
        public static final int API_DECREASE = 2;
        public static final int API_SCAN = 1;
        String barcode;
        Product product;
        int what;

        ApiTask(int what2, String barcode2, Product product2) {
            this.what = what2;
            this.barcode = barcode2;
            this.product = product2;
        }
    }

    private int getCodeAndShowDialog(Response<?> response, Exception e) throws BarcodeKanojoException {
        if (response != null) {
            return getCodeAndShowAlert(response, e);
        }
        this.code = Response.CODE_ERROR_NETWORK;
        return this.code;
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK || !this.mLoadingView.isShow()) {
            return super.onKeyDown(keyCode, event);
        }
        this.mLoadingView.setMessage(getString(R.string.requesting_cant_cancel));
        return true;
    }

    public void showProgressDialog() {
        this.mLoadingView.show();
    }

    protected void dismissProgressDialog() {
        this.mLoadingView.dismiss();
    }
}
