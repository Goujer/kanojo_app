package jp.co.cybird.barcodekanojoForGAM.activity.base;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.R;

import jp.co.cybird.barcodekanojoForGAM.activity.kanojo.KanojoRoomActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.util.ApiTask;
import jp.co.cybird.barcodekanojoForGAM.activity.util.EditBitmapActivity;
import com.goujer.barcodekanojo.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import com.goujer.barcodekanojo.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.util.FileUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;

import com.goujer.barcodekanojo.base.PermissionRequestCode;
import com.goujer.barcodekanojo.view.ProductAndKanojoView;

public abstract class BaseEditActivity extends BaseActivity implements BaseInterface {
    protected static final String TAG = "BaseEditActivity";
    protected File mFile;
    private KanojoGenerateAndUpdate mKanojoGenerateAndUpdateTask;
    protected DialogInterface.OnDismissListener mListener;
    private ProgressDialog mProgressDialog;
    private SignUpAndUpdateAccountTask mSignUpAndUpdateTask;
    protected Resources r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mListener = this;
        this.r = getResources();
    }

    @Override
    protected void onPause() {
        if (this.mKanojoGenerateAndUpdateTask != null) {
            this.mKanojoGenerateAndUpdateTask.cancel(true);
            this.mKanojoGenerateAndUpdateTask = null;
        }
        if (this.mSignUpAndUpdateTask != null) {
            this.mSignUpAndUpdateTask.cancel(true);
            this.mSignUpAndUpdateTask = null;
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BaseInterface.REQUEST_GALLERY:
                case BaseInterface.REQUEST_CAMERA:
                    this.mFile = EditBitmapActivity.getTempFile(this);
            }
        }
    }

    protected void setBitmapFromFile(ProductAndKanojoView view, File file) {
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
            view.setBitmap(setBitmap);
        }
    }

    protected Bitmap loadBitmap(File file, int view_width, int view_height) {
        int sample_size;
        if (file == null) {
            return null;
        }
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), option);
        if (option.outWidth * option.outHeight > 1048576) {
            sample_size = (int) (Math.sqrt(((double) (option.outWidth * option.outHeight)) / 1048576.0d) + 1.0d);
        } else {
            sample_size = 1;
        }
        option.inJustDecodeBounds = false;
        option.inSampleSize = sample_size;
        Bitmap src = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
        if (src == null) {
            return null;
        }
        int src_width = src.getWidth();
        int src_height = src.getHeight();
        float scale = getFitScale(view_width, view_height, src_width, src_height);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(src, 0, 0, src_width, src_height, matrix, true);
    }

    public static float getFitScale(int dest_width, int dest_height, int src_width, int src_height) {
        if (dest_width < dest_height) {
            if (src_width >= src_height) {
                return ((float) dest_width) / ((float) src_width);
            }
            float ret = ((float) dest_height) / ((float) src_height);
            return ((float) src_width) * ret > ((float) dest_width) ? ((float) dest_width) / ((float) src_width) : ret;
        } else if (src_width < src_height) {
            return ((float) dest_height) / ((float) src_height);
        } else {
            float ret2 = ((float) dest_width) / ((float) src_width);
            if (((float) src_height) * ret2 > ((float) dest_height)) {
                return ((float) dest_height) / ((float) src_height);
            }
            return ret2;
        }
    }

    protected void setFile(File file) {
        this.mFile = file;
    }

    protected File getFile() {
        return this.mFile;
    }

//    protected void setOnDismissListener(DialogInterface.OnDismissListener listener) {
//		this.mListener = listener;
//	}

    protected void showEditTextDialog(String title, EditItemView value) {
        EditText edit = new EditText(this);
        edit.setInputType(1);
        edit.setText(value.getValue());
        showEditTextDialog(title, value, edit);
    }

    protected void showEditTextDialog(String title, EditItemView value, int lines) {
        EditText edit = new EditText(this);
        edit.setText(value.getValue());
        edit.setLines(lines);
        edit.setGravity(48);
        showEditTextDialog(title, value, edit);
    }

    protected void showEditTextDialog(String title, final EditItemView value, final EditText edit) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title).setView(edit).setPositiveButton(R.string.common_dialog_ok, (dialog1, which) -> value.setValue(edit.getText().toString())).setNegativeButton(R.string.common_dialog_cancel, (dialog12, which) -> {
		}).create();
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener);
        }
        dialog.show();
    }

    protected void showImagePickerDialog(String title) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title).setItems(this.r.getStringArray(R.array.common_product_photo_dialog), (dialog12, position) -> {
            switch (position) {
                case 0:
						//Request Storage
						if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
							requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionRequestCode.IMAGE_PICKER_GALLERY);
						} else {
							Intent intent = new Intent(BaseEditActivity.this, EditBitmapActivity.class);
							intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_GALLERY);
							startActivityForResult(intent, BaseInterface.REQUEST_GALLERY);
							return;
						}
                case 1:
						//Request Storage and Camera
						if (Build.VERSION.SDK_INT >= 23 && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)) {
							requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PermissionRequestCode.IMAGE_PICKER_CAMERA);
						} else {
							Intent intent = new Intent(BaseEditActivity.this, EditBitmapActivity.class);
							intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_CAMERA);
							startActivityForResult(intent, BaseInterface.REQUEST_CAMERA);
							return;
						}
            }
        }).setNegativeButton(R.string.common_dialog_cancel, (dialog1, which) -> {}).create();
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener);
        }
        dialog.show();
    }

    protected void dismissProgressDialog() {
        try {
            if (this.mProgressDialog != null) {
                this.mProgressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
        }
    }

    protected void executeInspectionAndUpdateTask(String barcode, String company_name, String product_name, int product_category_id, String product_comment) {
        HashMap<String, String> param = new HashMap<>();
        param.put(GreeDefs.BARCODE, barcode);
        param.put(GreeDefs.COMPANY_NAME, company_name);
        param.put(GreeDefs.PRODUCT_NAME, product_name);
        param.put(GreeDefs.PRODUCT_CUTEGORY_ID, String.valueOf(product_category_id));
        param.put(GreeDefs.PRODUCT_COMMENT, product_comment);
        inspectionAndUpdateByAction(param, 4, null);
    }

    public void executeUpdateTask(HashMap<String, String> map) {
        if (this.mKanojoGenerateAndUpdateTask == null || this.mKanojoGenerateAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mKanojoGenerateAndUpdateTask = new KanojoGenerateAndUpdate(this, map, null, 4);
            this.mKanojoGenerateAndUpdateTask.execute();
        }
    }

    protected void executeInspectionAndScanOthersTask(ApiTask task) {
        HashMap<String, String> param = new HashMap<>();
        param.put(GreeDefs.COMPANY_NAME, task.product.getCompany_name());
        param.put(GreeDefs.PRODUCT_NAME, task.product.getName());
        param.put(GreeDefs.PRODUCT_COMMENT, task.product.getComment());
        HashMap<String, Object> options = new HashMap<>();
        options.put(ApiTask.TAG, task);
        inspectionAndUpdateByAction(param, 3, options);
    }

    public void executeScanOthersApiTask(HashMap<String, String> param, ApiTask task) {
        if (this.mKanojoGenerateAndUpdateTask == null || this.mKanojoGenerateAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            if (task.product.getComment() != null && task.product.getComment().equals("")) {
                task.productCommentTextId = param.get(GreeDefs.PRODUCT_COMMENT_TEXTID);
            }
            task.productNameTextId = param.get(GreeDefs.PRODUCT_NAME_TEXTID);
            task.companyNameTextId = param.get(GreeDefs.COMPANY_NAME_TEXTID);
            this.mKanojoGenerateAndUpdateTask = new KanojoGenerateAndUpdate(this, param, task, 3);
            this.mKanojoGenerateAndUpdateTask.execute();
        }
    }

    protected void executeInspectionAndGenerateTask(String barcode, String company_name, String kanojo_name, String product_name, int product_category_id, String product_comment, Location product_geo, Kanojo kanojo) {
        HashMap<String, String> param = new HashMap<>();
        param.put(GreeDefs.BARCODE, barcode);
        param.put(GreeDefs.KANOJO_NAME, kanojo_name);
        param.put(GreeDefs.COMPANY_NAME, company_name);
        param.put(GreeDefs.PRODUCT_NAME, product_name);
        param.put(GreeDefs.PRODUCT_CUTEGORY_ID, String.valueOf(product_category_id));
        param.put(GreeDefs.PRODUCT_COMMENT, product_comment);
        HashMap<String, Object> options = new HashMap<>();
        options.put(Kanojo.TAG, kanojo);
        inspectionAndUpdateByAction(param, 2, options);
    }

    public void executeGenerateTask(HashMap<String, String> params, Kanojo kanojo) {
        if (this.mKanojoGenerateAndUpdateTask == null || this.mKanojoGenerateAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mKanojoGenerateAndUpdateTask = new KanojoGenerateAndUpdate(this, params, kanojo, 2);
            this.mKanojoGenerateAndUpdateTask.execute();
        }
    }

    protected static class KanojoGenerateAndUpdate extends AsyncTask<String, Void, Response<?>> {
        private int mAction;
        private Object mOption;
        private HashMap<String, String> mParam;
        private Exception mReason = null;
        private WeakReference<BaseEditActivity> activityRef;

        KanojoGenerateAndUpdate(BaseEditActivity activity, HashMap<String, String> param, Object option, int action) {
        	this.activityRef = new WeakReference<>(activity);
            this.mParam = param;
            this.mOption = option;
            this.mAction = action;
        }

        public void onPreExecute() {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

			activity.showProgressDialog();
        }

        public Response<?> doInBackground(String... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            int code = 0;
            try {
                if (this.mAction == 2) {
					code = activity.getCodeAndShowAlert(response, this.mReason, (dialog, code1) -> {
					    if (code1 == Response.CODE_SUCCESS) {
													activity.setResult(BaseInterface.RESULT_GENERATE_KANOJO);
													activity.close();
					    }
					});
                } else if (this.mAction == 3) {
                    code = activity.getCodeAndShowAlert(response, this.mReason);
                } else if (this.mAction == 4) {
                    if (response == null) {
                        throw new BarcodeKanojoException(this.mReason.toString());
                    }
                    code = response.getCode();
                }
                switch (code) {
					case Response.CODE_SUCCESS:
                        if (this.mAction != 2) {
                            if (this.mAction != 4) {
                                if (this.mAction == 3) {
									activity.dismissProgressDialog();
                                    ApiTask task = (ApiTask) this.mOption;
                                    task.result = 1;
                                    if (task.what != 2) {
                                        if (task.what == 1) {
											activity.setResult(103);
											activity.close();
                                            break;
                                        }
                                    } else {
                                        task.result = 1;
                                        break;
                                    }
                                }
                            } else {
                                Alert alert = response.getAlert();
                                final Product product = (Product) response.get(Product.class);
                                if (alert != null) {
	                                activity.dismissProgressDialog();
	                                activity.showNoticeDialog(alert.getBody(), new DialogInterface.OnDismissListener() {
                                        public void onDismiss(DialogInterface dialog) {
                                            Intent data = new Intent();
                                            data.putExtra(BaseInterface.EXTRA_PRODUCT, product);
											activity.setResult(101, data);
											activity.close();
                                        }
                                    });
                                    break;
                                }
                            }
                        } else {
							activity.setResult(BaseInterface.RESULT_GENERATE_KANOJO);
							activity.close();
                            break;
                        }
                        break;
                    case Response.CODE_ERROR_SERVER:
                    case Response.CODE_ERROR_SERVICE_UNAVAILABLE:
	                    activity.dismissProgressDialog();
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
	            activity.dismissProgressDialog();
            }
        }

        protected void onCancelled() {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

			activity.dismissProgressDialog();
        }

        Response<?> process() throws BarcodeKanojoException, IllegalStateException, IOException {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return null;
	        }

            BarcodeKanojoApp barcodeKanojoApp = (BarcodeKanojoApp) activity.getApplication();
            BarcodeKanojo barcodeKanojo = barcodeKanojoApp.getBarcodeKanojo();
            if (this.mAction == 2) {
                File iconFile = FileUtil.createIconCache(activity, (Kanojo) this.mOption);
                Location loc = barcodeKanojoApp.getLastKnownLocation();
                if (loc == null) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }
                    loc = barcodeKanojoApp.getLastKnownLocation();
                }
                return barcodeKanojo.scan_and_generate(this.mParam.get(GreeDefs.BARCODE), this.mParam.get(GreeDefs.COMPANY_NAME), this.mParam.get(GreeDefs.KANOJO_NAME), iconFile, this.mParam.get(GreeDefs.PRODUCT_NAME), Integer.parseInt(this.mParam.get(GreeDefs.PRODUCT_CUTEGORY_ID)), this.mParam.get(GreeDefs.PRODUCT_COMMENT), activity.getFile(), loc);
            } else if (this.mAction == 4) {
                return barcodeKanojo.update(this.mParam.get(GreeDefs.BARCODE), this.mParam.get(GreeDefs.COMPANY_NAME), this.mParam.get(GreeDefs.PRODUCT_NAME), Integer.parseInt(this.mParam.get(GreeDefs.PRODUCT_CUTEGORY_ID)), this.mParam.get(GreeDefs.PRODUCT_COMMENT), activity.getFile(), GeoUtil.stringToGeo(this.mParam.get(GreeDefs.GEOPOINT)));
            } else if (this.mAction == 3) {
                Location loc2 = barcodeKanojoApp.getLastKnownLocation();
                if (loc2 == null) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                    loc2 = barcodeKanojoApp.getLastKnownLocation();
                }
                ApiTask task = (ApiTask) this.mOption;
                if (task == null) {
                    throw new BarcodeKanojoException("Task is null");
                }
                switch (task.what) {
                    case 1:
                        if (task.barcode == null || task.product == null) {
                            return null;
                        }
                        return barcodeKanojo.scan(task.barcode, task.product.getCompany_name(), task.product.getName(), task.product.getCategory_id(), task.product.getComment(), activity.getFile(), loc2);
                    case 2:
                        if (task.barcode == null || task.product == null) {
                            return null;
                        }
                        return barcodeKanojo.update(task.barcode, task.product.getCompany_name(), task.product.getName(), task.product.getCategory_id(), task.product.getComment(), activity.getFile(), loc2);
                    default:
                        return null;
                }
            } else {
                throw new IllegalStateException("Action:" + this.mAction);
            }
        }
    }

    protected static class SignUpAndUpdateAccountTask extends AsyncTask<Void, Void, Response<?>> {
        private static final String TAG = "SignUpUpdateAccountTask";
        private final int mAction;
        private Exception mReason;
		private WeakReference<BaseEditActivity> activityRef;

		public SignUpAndUpdateAccountTask(HashMap<String, String> param2, int action) {
			this.mAction = action;
        }

        protected void onPreExecute() {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            activity.showProgressDialog();
        }

        protected Response<?> doInBackground(Void... params) {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return null;
	        }

            try {
                BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo();
                Log.e(TAG, "doInBackground() cunnot be used !!!");
                String udid = ((BarcodeKanojoApp) activity.getApplication()).getUDID();
                return null;
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        protected void onPostExecute(Response<?> response) {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            try {
                switch (activity.getCodeAndShowAlert(response, this.mReason)) {
					case Response.CODE_SUCCESS:
                        if (this.mAction == 1) {
                            activity.updateUser(response);
                            break;
                        }
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
                activity.dismissProgressDialog();
            }
        }

        protected void onCancelled() {
	        BaseEditActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            activity.dismissProgressDialog();
        }
    }

    public void executeSignUpTask(HashMap<String, String> param) {
        if (this.mSignUpAndUpdateTask == null || this.mSignUpAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mSignUpAndUpdateTask = new SignUpAndUpdateAccountTask(param, 0);
            this.mSignUpAndUpdateTask.execute();
        }
    }

    public void executeEditAccountTask(HashMap<String, String> map) {
        if (this.mSignUpAndUpdateTask == null || this.mSignUpAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mSignUpAndUpdateTask = (SignUpAndUpdateAccountTask) new SignUpAndUpdateAccountTask(map, 1).execute(new Void[0]);
        }
    }

    protected void inspectionAndUpdateByAction(HashMap<String, String> param, int action, HashMap<String, Object> options) {
        switch (action) {
            case 0:
                executeSignUpTask(param);
                return;
            case 1:
                executeEditAccountTask(param);
                return;
            case 2:
                Kanojo kanojo = null;
                if (options != null) {
                    kanojo = (Kanojo) options.get(Kanojo.TAG);
                }
                executeGenerateTask(param, kanojo);
                return;
            case 3:
                ApiTask task = null;
                if (options != null) {
                    task = (ApiTask) options.get(ApiTask.TAG);
                }
                executeScanOthersApiTask(param, task);
                return;
            case 4:
                executeUpdateTask(param);
                return;
            default:
		}
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case PermissionRequestCode.IMAGE_PICKER_GALLERY:
				if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Intent intent = new Intent(BaseEditActivity.this, EditBitmapActivity.class);
					intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_GALLERY);
					startActivityForResult(intent, BaseInterface.REQUEST_GALLERY);
				} else {
					finish();
				}
				return;
			case PermissionRequestCode.IMAGE_PICKER_CAMERA:
				if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
					Intent intent = new Intent(BaseEditActivity.this, EditBitmapActivity.class);
					intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_CAMERA);
					startActivityForResult(intent, BaseInterface.REQUEST_CAMERA);
				} else {
					finish();
				}
				return;
		}
	}
}
