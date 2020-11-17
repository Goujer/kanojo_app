package jp.co.cybird.barcodekanojoForGAM.activity.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ZoomControls;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.util.FileUtil;
import jp.co.cybird.barcodekanojoForGAM.view.EditBitmapView;

public class EditBitmapActivity extends Activity implements BaseInterface, View.OnClickListener {

    private static final String FILENAME = "temp.jpg";
    private static final String NOMEDIA = ".nomedia";
    private static final String ROOT_DIR = (String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/barcodekanojo/");
    private static final String TAG = "EditBitmapActivity";
    private static final String TEMP_IMG = "camera_image.jpg";
    private Uri mImageUri;
    private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            EditBitmapActivity.this.finish();
        }
    };
    private File mRootDir;
    private String mRootPath;
    private EditBitmapView mView;
    private int mode = 0;

    public interface OnDialogDismissListener {
        void onDismiss(DialogInterface dialogInterface, int i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bitmap);
        findViewById(R.id.edit_bitmap_retake).setOnClickListener(this);
        findViewById(R.id.edit_bitmap_ok).setOnClickListener(this);
        if (FileUtil.isAvailableExternalSDMemory() || FileUtil.isAvailableInternalMemory()) {
            if (FileUtil.isAvailableExternalSDMemory()) {
                this.mRootPath = Environment.getExternalStorageDirectory().getPath() + "/barcodekanojo/";
            } else {
                this.mRootPath = getFilesDir().getAbsolutePath() + "/barcodekanojo/";
            }
            this.mRootDir = new File(this.mRootPath);
            createDirectory(this.mRootDir);
            this.mView = findViewById(R.id.edit_bitmap_view);
            this.mView.setDirPath(this.mRootPath);
            ZoomControls zoomCtl = findViewById(R.id.edit_bitmap_zoom_control);
            zoomCtl.setOnZoomInClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    EditBitmapActivity.this.mView.ZoomIn();
                }
            });
            zoomCtl.setOnZoomOutClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    EditBitmapActivity.this.mView.ZoomOut();
                }
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                this.mode = bundle.getInt(BaseInterface.EXTRA_REQUEST_CODE);
            }
            registerReceiver(this.mLoggedOutReceiver, new IntentFilter(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT));
            startPhotoActivity(this.mode);
            return;
        }
        sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
        finish();
    }

    protected void onDestroy() {
        try {
            unregisterReceiver(this.mLoggedOutReceiver);
        } catch (Exception e) {
        }
        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(R.id.common_top_menu_root);
        if (!(root == null || root.getChildCount() == 0)) {
            cleanupView(root.getChildAt(0));
        }
        super.onDestroy();
    }

    private void startPhotoActivity(int request_code) {
        if (request_code == 1100) {
            Intent intentGallery = new Intent();
            intentGallery.setType("image/*");
            intentGallery.setAction("android.intent.action.GET_CONTENT");
            startActivityForResult(intentGallery, 1100);
            return;
        }
        try {
            Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            this.mImageUri = Uri.fromFile(new File(this.mRootDir, TEMP_IMG));
            intentCamera.putExtra("output", this.mImageUri);
            startActivityForResult(intentCamera, BaseInterface.REQUEST_CAMERA);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 1100:
                    try {
                        Uri uriGallery = data.getData();
                        File file = new File(uriGallery.getPath());
                        if (!file.exists()) {
                            file = new File(FileUtil.getRealPathFromURI(this, uriGallery));
                        }
                        setBitmapFromFile(file);
                        return;
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        break;
                    }
                case BaseInterface.REQUEST_CAMERA:
                    setBitmapFromFile(new File(this.mRootDir, TEMP_IMG));
                    return;
            }
        }
        finish();
    }

    private void setBitmapFromFile(File file) {
        Bitmap set;
        Bitmap bitmap = loadBitmap(file, 480, 800);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width > height) {
                Matrix aMatrix = new Matrix();
                aMatrix.setRotate(90.0f);
                set = Bitmap.createBitmap(bitmap, 0, 0, width, height, aMatrix, false);
            } else {
                set = bitmap;
            }
            this.mView.setBitmap(set);
        }
    }

    protected final void cleanupView(View view) {
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback((Drawable.Callback) null);
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable((Drawable) null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable((Drawable) null);
        } else if (view instanceof SeekBar) {
            SeekBar sb = (SeekBar) view;
            sb.setProgressDrawable((Drawable) null);
            sb.setThumb((Drawable) null);
        } else if (view instanceof TextView) {
            ((TextView) view).setBackgroundDrawable((Drawable) null);
        } else if (view instanceof Button) {
            ((Button) view).setBackgroundDrawable((Drawable) null);
        }
        view.setBackgroundDrawable((Drawable) null);
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int size = vg.getChildCount();
            for (int i = 0; i < size; i++) {
                cleanupView(vg.getChildAt(i));
            }
        }
    }

    // JADX made a booboo
	public void onClick(View v) {
		Throwable th;
		switch (v.getId()) {
			case R.id.edit_bitmap_retake:
				startPhotoActivity(this.mode);
				return;
			case R.id.edit_bitmap_ok:
				try{
					this.mView.saveBitmap(FILENAME);
					OutputStream outputStream = null;
					try {
						File dir = new File(this.mRootDir.getPath());
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File file = new File(dir, FILENAME);
						if (file.exists()) {
							Bitmap resized = loadBitmap(file, 200, 200);
							OutputStream outputStream2 = new FileOutputStream(file);
							if (resized != null) {
								resized.compress(Bitmap.CompressFormat.JPEG, 100, outputStream2);
								outputStream = outputStream2;
							}
							outputStream = outputStream2;
						}
						if (outputStream != null) {
							try {
								outputStream.close();
							} catch (Throwable th5) {
							}
						}
					} catch (FileNotFoundException e2) {
						if (outputStream != null) {
							outputStream.close();
						}
						setResult(-1);
						finish();
						return;
					} catch (Throwable th6) {
						th = th6;
						if (outputStream != null) {
							outputStream.close();
						}
						throw th;
					}}
				catch(IOException e){
					e.printStackTrace();
				}
				catch (Throwable th2)
				{
					th2.printStackTrace();
				}
				setResult(-1);
				finish();
				return;
			default:
				return;
		}
	}

    public static File getEditedFile() {
        String mRootPath2;
        if (FileUtil.isAvailableExternalSDMemory()) {
            mRootPath2 = Environment.getExternalStorageDirectory().getPath() + "/barcodekanojo/";
        } else if (!FileUtil.isAvailableInternalMemory()) {
            return null;
        } else {
            mRootPath2 = Environment.getDataDirectory().getAbsolutePath() + "/data/jp.co.cybird.barcodekanojoForGAM/files/barcodekanojo/";
        }
        File dir = new File(mRootPath2);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, FILENAME);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    private Bitmap loadBitmap(File file, int view_width, int view_height) {
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

    private static final void createDirectory(File storageDirectory) {
        if (!storageDirectory.exists()) {
            Log.d(TAG, "Trying to create storageDirectory: " + String.valueOf(storageDirectory.mkdirs()));
            Log.d(TAG, "Exists: " + storageDirectory + " " + String.valueOf(storageDirectory.exists()));
            Log.d(TAG, "State: " + Environment.getExternalStorageState());
            Log.d(TAG, "Isdir: " + storageDirectory + " " + String.valueOf(storageDirectory.isDirectory()));
            Log.d(TAG, "Readable: " + storageDirectory + " " + String.valueOf(storageDirectory.canRead()));
            Log.d(TAG, "Writable: " + storageDirectory + " " + String.valueOf(storageDirectory.canWrite()));
            File tmp = storageDirectory.getParentFile();
            Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
            Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
            Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
            Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));
            File tmp2 = tmp.getParentFile();
            Log.d(TAG, "Exists: " + tmp2 + " " + String.valueOf(tmp2.exists()));
            Log.d(TAG, "Isdir: " + tmp2 + " " + String.valueOf(tmp2.isDirectory()));
            Log.d(TAG, "Readable: " + tmp2 + " " + String.valueOf(tmp2.canRead()));
            Log.d(TAG, "Writable: " + tmp2 + " " + String.valueOf(tmp2.canWrite()));
        }
        File nomediaFile = new File(storageDirectory, NOMEDIA);
        if (!nomediaFile.exists()) {
            try {
                Log.d(TAG, "Created file: " + nomediaFile + " " + String.valueOf(nomediaFile.createNewFile()));
            } catch (IOException e) {
                Log.d(TAG, "Unable to create .nomedia file for some reason.", e);
                throw new IllegalStateException("Unable to create nomedia file. PATH=" + storageDirectory.getAbsolutePath() + " Directory exists=" + storageDirectory.exists());
            }
        }
        if (!storageDirectory.isDirectory() || !nomediaFile.exists()) {
            throw new RuntimeException("Unable to create storage directory and nomedia file.");
        }
    }
}
