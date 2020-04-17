package jp.co.cybird.barcodekanojoForGAM.activity.base;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import com.google.android.maps.GeoPoint;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.util.ApiTask;
import jp.co.cybird.barcodekanojoForGAM.activity.util.EditBitmapActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.Category;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.util.FileUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView;
import jp.co.cybird.barcodekanojoForGAM.view.ProductAndKanojoView;

public abstract class BaseEditActivity extends BaseActivity implements BaseInterface {
    protected static final boolean DEBUG = false;
    protected static final String TAG = "BaseEditActivity";
    /* access modifiers changed from: private */
    public ModelList<Category> mCategories;
    protected String[] mCategoryList = {""};
    protected File mFile;
    private KanojoGenerateAndUpdate mKanojoGenerateAndUpdateTask;
    private DialogInterface.OnDismissListener mListener;
    private ProgressDialog mProgressDialog;
    private SignUpAndUpdateAccountTask mSignUpAndUpdateTask;
    protected Resources r;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mListener = this;
        this.r = getResources();
        try {
            this.mCategories = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getCategoryList();
            int size = this.mCategories.size();
            this.mCategoryList = new String[size];
            for (int i = 0; i < size; i++) {
                this.mCategoryList[i] = ((Category) this.mCategories.get(i)).getName();
            }
        } catch (BarcodeKanojoException e) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) BaseEditActivity.this.getApplication()).getBarcodeKanojo();
                        barcodeKanojo.init_product_category_list();
                        BaseEditActivity.this.mCategories = barcodeKanojo.getCategoryList();
                        int size = BaseEditActivity.this.mCategories.size();
                        BaseEditActivity.this.mCategoryList = new String[size];
                        for (int i = 0; i < size; i++) {
                            BaseEditActivity.this.mCategoryList[i] = ((Category) BaseEditActivity.this.mCategories.get(i)).getName();
                        }
                    } catch (IOException | BarcodeKanojoException e) {
                    }
                }
            }).start();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
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

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    /* access modifiers changed from: protected */
    public void setBitmapFromFile(ProductAndKanojoView view, File file) {
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

    /* access modifiers changed from: protected */
    public Bitmap loadBitmap(File file, int view_width, int view_height) {
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

    /* access modifiers changed from: protected */
    public void setFile(File file) {
        this.mFile = file;
    }

    /* access modifiers changed from: protected */
    public File getFile() {
        return this.mFile;
    }

    /* access modifiers changed from: protected */
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: protected */
    public Category getDefaultCategory() {
        if (this.mCategories == null || this.mCategories.size() == 0) {
            return null;
        }
        return (Category) this.mCategories.get(0);
    }

    /* access modifiers changed from: protected */
    public void showListDialog(String title, final Product product, final EditItemView value) {
        int selected = 0;
        if (this.mCategories != null) {
            int size = this.mCategories.size();
            for (int i = 0; i < size; i++) {
                if (((Category) this.mCategories.get(i)).getId() == product.getCategory_id()) {
                    selected = i;
                }
            }
        }
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title).setSingleChoiceItems(this.mCategoryList, selected, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                product.setCategory(BaseEditActivity.this.mCategoryList[position]);
                if (BaseEditActivity.this.mCategories != null) {
                    product.setCategory_id(((Category) BaseEditActivity.this.mCategories.get(position)).getId());
                    value.setValue(((Category) BaseEditActivity.this.mCategories.get(position)).getName());
                }
            }
        }).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener);
        }
        dialog.show();
    }

    /* access modifiers changed from: protected */
    public void showGenderDialog(String title, final EditItemView value) {
        final String[] genderList = getResources().getStringArray(R.array.user_account_gender_list);
        int selected = -1;
        if (genderList != null) {
            int size = genderList.length;
            for (int i = 0; i < size; i++) {
                if (genderList[i].equals(value.getValue())) {
                    selected = i;
                }
            }
        }
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title).setSingleChoiceItems(genderList, selected, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                value.setValue(genderList[position]);
            }
        }).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener);
        }
        dialog.show();
    }

    /* access modifiers changed from: protected */
    public void showDatePickDialog(String title, final EditItemView value) {
        Calendar cal = Calendar.getInstance();
        String birthText = value.getValue();
        int year = cal.get(1);
        int month = cal.get(2);
        int day = cal.get(5);
        if (!(birthText == null || birthText == "")) {
            try {
                String[] arr = birthText.split("\\.");
                if (arr.length == 3) {
                    month = Integer.parseInt(arr[0]) - 1;
                    day = Integer.parseInt(arr[1]);
                    year = Integer.parseInt(arr[2]);
                }
            } catch (Exception e) {
            }
        }
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                value.setValue(String.valueOf(String.format("%02d", new Object[]{Integer.valueOf(monthOfYear + 1)})) + "." + String.format("%02d", new Object[]{Integer.valueOf(dayOfMonth)}) + "." + String.format("%04d", new Object[]{Integer.valueOf(year)}));
            }
        }, year, month, day);
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener);
        }
        dialog.show();
    }

    /* access modifiers changed from: protected */
    public void showEditTextDialog(String title, EditItemView value) {
        EditText edit = new EditText(this);
        edit.setInputType(1);
        edit.setText(value.getValue());
        showEditTextDialog(title, value, edit);
    }

    /* access modifiers changed from: protected */
    public void showEditTextDialog(String title, EditItemView value, int lines) {
        EditText edit = new EditText(this);
        edit.setText(value.getValue());
        edit.setLines(lines);
        edit.setGravity(48);
        showEditTextDialog(title, value, edit);
    }

    /* access modifiers changed from: protected */
    public void showEditTextDialog(String title, final EditItemView value, final EditText edit) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title).setView(edit).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                value.setValue(edit.getText().toString());
            }
        }).setNegativeButton(R.string.common_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener);
        }
        dialog.show();
    }

    /* access modifiers changed from: protected */
    public void showImagePickerDialog(String title) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(title).setItems(this.r.getStringArray(R.array.common_product_photo_dialog), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(BaseEditActivity.this, EditBitmapActivity.class);
                        intent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, 1100);
                        BaseEditActivity.this.startActivityForResult(intent, 1100);
                        return;
                    case 1:
                        Intent intent2 = new Intent(BaseEditActivity.this, EditBitmapActivity.class);
                        intent2.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_CAMERA);
                        BaseEditActivity.this.startActivityForResult(intent2, BaseInterface.REQUEST_CAMERA);
                        return;
                    default:
                        return;
                }
            }
        }).setNegativeButton(R.string.common_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener);
        }
        dialog.show();
    }

    /* access modifiers changed from: protected */
    public ProgressDialog showProgressDialog() {
        try {
            if (this.mProgressDialog == null) {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle(R.string.app_name);
                dialog.setMessage(getString(R.string.kanojo_room_dialog_progress));
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                this.mProgressDialog = dialog;
            }
            if (!this.mProgressDialog.isShowing()) {
                this.mProgressDialog.show();
            }
        } catch (Exception e) {
        }
        return this.mProgressDialog;
    }

    /* access modifiers changed from: protected */
    public void dismissProgressDialog() {
        try {
            if (this.mProgressDialog != null) {
                this.mProgressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void executeInspectionAndUpdateTask(String barcode, String company_name, String product_name, int product_category_id, String product_comment) {
        HashMap<String, String> param = new HashMap<>();
        param.put(GreeDefs.BARCODE, barcode);
        param.put(GreeDefs.COMPANY_NAME, company_name);
        param.put(GreeDefs.PRODUCT_NAME, product_name);
        param.put(GreeDefs.PRODUCT_CUTEGORY_ID, new StringBuilder().append(product_category_id).toString());
        param.put(GreeDefs.PRODUCT_COMMENT, product_comment);
        inspectionAndUpdateByAction(param, 4, (HashMap<String, Object>) null);
    }

    public void executeUpdateTask(HashMap<String, String> map) {
        if (this.mKanojoGenerateAndUpdateTask == null || this.mKanojoGenerateAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mKanojoGenerateAndUpdateTask = new KanojoGenerateAndUpdate(map, (Object) null, 4);
            this.mKanojoGenerateAndUpdateTask.execute(new String[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void executeInspectionAndScanOthersTask(ApiTask task) {
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
            this.mKanojoGenerateAndUpdateTask = new KanojoGenerateAndUpdate(param, task, 3);
            this.mKanojoGenerateAndUpdateTask.execute(new String[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void executeInspectionAndGenerateTask(String barcode, String company_name, String kanojo_name, String product_name, int product_category_id, String product_comment, GeoPoint product_geo, Kanojo kanojo) {
        HashMap<String, String> param = new HashMap<>();
        param.put(GreeDefs.BARCODE, barcode);
        param.put(GreeDefs.KANOJO_NAME, kanojo_name);
        param.put(GreeDefs.COMPANY_NAME, company_name);
        param.put(GreeDefs.PRODUCT_NAME, product_name);
        param.put(GreeDefs.PRODUCT_CUTEGORY_ID, new StringBuilder().append(product_category_id).toString());
        param.put(GreeDefs.PRODUCT_COMMENT, product_comment);
        HashMap<String, Object> options = new HashMap<>();
        options.put(Kanojo.TAG, kanojo);
        inspectionAndUpdateByAction(param, 2, options);
    }

    public void exectuteGenerateTask(HashMap<String, String> params, Kanojo kanojo) {
        if (this.mKanojoGenerateAndUpdateTask == null || this.mKanojoGenerateAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mKanojoGenerateAndUpdateTask = new KanojoGenerateAndUpdate(params, kanojo, 2);
            this.mKanojoGenerateAndUpdateTask.execute(new String[0]);
        }
    }

    protected class KanojoGenerateAndUpdate extends AsyncTask<String, Void, Response<?>> {
        private int mAction;
        private Object mOption = null;
        private HashMap<String, String> mParam;
        private Exception mReason = null;

        public KanojoGenerateAndUpdate(HashMap<String, String> param, Object opton, int action) {
            this.mParam = param;
            this.mOption = opton;
            this.mAction = action;
        }

        public void onPreExecute() {
            BaseEditActivity.this.showProgressDialog();
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
            int code = 0;
            try {
                if (this.mAction == 2) {
                    code = BaseEditActivity.this.getCodeAndShowAlert(response, this.mReason, new BaseActivity.OnDialogDismissListener() {
                        public void onDismiss(DialogInterface dialog, int code) {
                            if (code == 200) {
                                BaseEditActivity.this.setResult(102);
                                BaseEditActivity.this.close();
                            }
                        }
                    });
                } else if (this.mAction == 3) {
                    code = BaseEditActivity.this.getCodeAndShowAlert(response, this.mReason);
                } else if (this.mAction == 4) {
                    if (response == null) {
                        throw new BarcodeKanojoException(this.mReason.toString());
                    }
                    code = response.getCode();
                }
                switch (code) {
                    case 200:
                        if (this.mAction != 2) {
                            if (this.mAction != 4) {
                                if (this.mAction == 3) {
                                    BaseEditActivity.this.dismissProgressDialog();
                                    ApiTask task = (ApiTask) this.mOption;
                                    task.result = 1;
                                    if (task.what != 2) {
                                        if (task.what == 1) {
                                            BaseEditActivity.this.setResult(103);
                                            BaseEditActivity.this.close();
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
                                    BaseEditActivity.this.dismissProgressDialog();
                                    BaseEditActivity.this.showNoticeDialog(alert.getBody(), new DialogInterface.OnDismissListener() {
                                        public void onDismiss(DialogInterface dialog) {
                                            Intent data = new Intent();
                                            data.putExtra(BaseInterface.EXTRA_PRODUCT, product);
                                            BaseEditActivity.this.setResult(101, data);
                                            BaseEditActivity.this.close();
                                        }
                                    });
                                    break;
                                }
                            }
                        } else {
                            BaseEditActivity.this.setResult(102);
                            BaseEditActivity.this.close();
                            break;
                        }
                        break;
                    case 500:
                    case 503:
                        BaseEditActivity.this.dismissProgressDialog();
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
                BaseEditActivity.this.dismissProgressDialog();
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            BaseEditActivity.this.dismissProgressDialog();
        }

        /* access modifiers changed from: package-private */
        public Response<?> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojoApp barcodeKanojoApp = (BarcodeKanojoApp) BaseEditActivity.this.getApplication();
            BarcodeKanojo barcodeKanojo = barcodeKanojoApp.getBarcodeKanojo();
            if (this.mAction == 2) {
                File iconFile = FileUtil.createIconCache(BaseEditActivity.this, (Kanojo) this.mOption);
                Location loc = barcodeKanojoApp.getLastKnownLocation();
                if (loc == null) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }
                    loc = barcodeKanojoApp.getLastKnownLocation();
                }
                return barcodeKanojo.scan_and_generate(this.mParam.get(GreeDefs.BARCODE), this.mParam.get(GreeDefs.COMPANY_NAME), this.mParam.get(GreeDefs.COMPANY_NAME_TEXTID), this.mParam.get(GreeDefs.KANOJO_NAME), this.mParam.get(GreeDefs.KANOJO_NAME_TEXTID), iconFile, this.mParam.get(GreeDefs.PRODUCT_NAME), this.mParam.get(GreeDefs.PRODUCT_NAME_TEXTID), Integer.valueOf(this.mParam.get(GreeDefs.PRODUCT_CUTEGORY_ID)).intValue(), this.mParam.get(GreeDefs.PRODUCT_COMMENT), this.mParam.get(GreeDefs.PRODUCT_COMMENT_TEXTID), BaseEditActivity.this.getFile(), GeoUtil.LocationToGeo(loc));
            } else if (this.mAction == 4) {
                return barcodeKanojo.update(this.mParam.get(GreeDefs.BARCODE), this.mParam.get(GreeDefs.COMPANY_NAME), this.mParam.get(GreeDefs.COMPANY_NAME_TEXTID), this.mParam.get(GreeDefs.PRODUCT_NAME), this.mParam.get(GreeDefs.PRODUCT_NAME_TEXTID), Integer.valueOf(this.mParam.get(GreeDefs.PRODUCT_CUTEGORY_ID)).intValue(), this.mParam.get(GreeDefs.PRODUCT_COMMENT), this.mParam.get(GreeDefs.PRODUCT_COMMENT_TEXTID), BaseEditActivity.this.getFile(), GeoUtil.stringToGeo(this.mParam.get(GreeDefs.GEOPOINT)));
            } else if (this.mAction == 3) {
                Location loc2 = barcodeKanojoApp.getLastKnownLocation();
                if (loc2 == null) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e2) {
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
                        return barcodeKanojo.scan(task.barcode, task.product.getCompany_name(), task.companyNameTextId, task.product.getName(), task.productNameTextId, task.product.getCategory_id(), task.product.getComment(), task.productCommentTextId, BaseEditActivity.this.getFile(), GeoUtil.LocationToGeo(loc2));
                    case 2:
                        if (task.barcode == null || task.product == null) {
                            return null;
                        }
                        return barcodeKanojo.update(task.barcode, task.product.getCompany_name(), task.companyNameTextId, task.product.getName(), task.productNameTextId, task.product.getCategory_id(), task.product.getComment(), task.productCommentTextId, BaseEditActivity.this.getFile(), GeoUtil.LocationToGeo(loc2));
                    default:
                        return null;
                }
            } else {
                throw new IllegalStateException("Action:" + this.mAction);
            }
        }
    }

    protected class SignUpAndUpdateAccountTask extends AsyncTask<Void, Void, Response<?>> {
        private static final boolean DEBUG = false;
        private static final String TAG = "SignUpAndUpdateAccountTask";
        private int mAction;
        private Exception mReason;
        private HashMap<String, String> param;

        public SignUpAndUpdateAccountTask(HashMap<String, String> param2, int action) {
            this.param = param2;
            this.mAction = action;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            BaseEditActivity.this.showProgressDialog();
        }

        /* access modifiers changed from: protected */
        public Response<?> doInBackground(Void... params) {
            try {
                BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) BaseEditActivity.this.getApplication()).getBarcodeKanojo();
                Log.e(TAG, "doInBackground() cunnot be used !!!");
                String udid = ((BarcodeKanojoApp) BaseEditActivity.this.getApplication()).getUDID();
                return null;
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Response<?> response) {
            try {
                switch (BaseEditActivity.this.getCodeAndShowAlert(response, this.mReason)) {
                    case 200:
                        if (this.mAction == 1) {
                            BaseEditActivity.this.updateUser(response);
                            break;
                        }
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
                BaseEditActivity.this.dismissProgressDialog();
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            BaseEditActivity.this.dismissProgressDialog();
        }
    }

    public void executeSignUpTask(HashMap<String, String> param) {
        if (this.mSignUpAndUpdateTask == null || this.mSignUpAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mSignUpAndUpdateTask = new SignUpAndUpdateAccountTask(param, 0);
            this.mSignUpAndUpdateTask.execute(new Void[0]);
        }
    }

    public void executeEditAccountTask(HashMap<String, String> map) {
        if (this.mSignUpAndUpdateTask == null || this.mSignUpAndUpdateTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mSignUpAndUpdateTask = (SignUpAndUpdateAccountTask) new SignUpAndUpdateAccountTask(map, 1).execute(new Void[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void inspectionAndUpdateByAction(HashMap<String, String> param, int action, HashMap<String, Object> options) {
        new HashMap();
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
                exectuteGenerateTask(param, kanojo);
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
                return;
        }
    }
}
