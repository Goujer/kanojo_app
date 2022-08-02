package jp.co.cybird.barcodekanojoForGAM.activity.util;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ZoomControls;

import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.BuildConfig;
import com.goujer.barcodekanojo.R;
import com.goujer.barcodekanojo.activity.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.util.FileUtil;
import jp.co.cybird.barcodekanojoForGAM.view.EditBitmapView;

public class EditBitmapActivity extends BaseActivity implements BaseInterface, View.OnClickListener {
    private static final String FILENAME = "temp.jpg";
    private static final String NOMEDIA = ".nomedia";
    //private static final String ROOT_DIR = (String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/barcodekanojo/");
    private static final String TAG = "EditBitmapActivity";
    private String mPhotoPath;
	private final BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            EditBitmapActivity.this.finish();
        }
    };
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
        //TODO Is this needed?
        //if (FileUtil.isAvailableExternalSDMemory() || FileUtil.isAvailableInternalMemory()) {
            //if (FileUtil.isAvailableExternalSDMemory()) {
            //    this.mRootPath = Environment.getExternalStorageDirectory().getPath() + "/barcodekanojo/";
            //} else {
            //    this.mRootPath = getFilesDir().getAbsolutePath() + "/barcodekanojo/";
            //}
            //this.mRootDir = new File(this.mRootPath);
            //createDirectory(this.mRootDir);
            this.mView = findViewById(R.id.edit_bitmap_view);
            //this.mView.setDirPath(this.mRootPath);

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
        //}
        //sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
        //finish();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(this.mLoggedOutReceiver);
        } catch (Exception ignored) {
        }
        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(R.id.common_top_menu_root);
        if (!(root == null || root.getChildCount() == 0)) {
            cleanupView(root.getChildAt(0));
        }

        super.onDestroy();
    }

    private void startPhotoActivity(int request_code) {
        if (request_code == BaseInterface.REQUEST_GALLERY) {
			startGalleryActivity();
        	return;
        }
		if (Build.VERSION.SDK_INT >= 23
				&& this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
			requestPermissions(new String[] { Manifest.permission.CAMERA }, 69);
			if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
				startCameraActivity();
			} else {
				finish();
			}
		} else {
			startCameraActivity();
		}
    }

    private void startGalleryActivity() {
		Intent intentGallery = new Intent();
		intentGallery.setType("image/*");
		intentGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentGallery, BaseInterface.REQUEST_GALLERY);
	}

	//TODO Ask to use memory if storage not available
	private void startCameraActivity() {
		try {
			Uri mImageUri = null;
			mPhotoPath = null;
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				if (FileUtil.isExternalStorageAvailable()) {
					if (FileUtil.doesExternalStorageHaveSpace()) {
						if (Build.VERSION.SDK_INT >= 29) {
							final ContentValues contentValues = new ContentValues();
							contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".jpg");
							//contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
							contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + "BarcodeKANOJO");
							mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
							mPhotoPath = mImageUri.getPath();
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
						} else if (Build.VERSION.SDK_INT < 23 || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
							File mRootDir;
							if (Build.VERSION.SDK_INT >= 8) {
								mRootDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "BarcodeKANOJO");
							} else {
								mRootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.pathSeparator + "DCIM" + File.separator + "BarcodeKANOJO");
							}
							//TODO ensure directory creation
							File imageFile = new File(mRootDir, new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".jpg");
							mPhotoPath = imageFile.getAbsolutePath();
							try {
								imageFile.getParentFile().mkdirs();
								//imageFile.createNewFile();
								if (Build.VERSION.SDK_INT >= 16) {
									mImageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", imageFile);
									if (Build.VERSION.SDK_INT <= 22) {
										takePictureIntent.setClipData(ClipData.newRawUri("", mImageUri));
									}
									takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
								} else {
									mImageUri = Uri.fromFile(imageFile);
								}
								takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
							} catch (NullPointerException e) {
								e.printStackTrace();
							}
						}
					} else {
						showNoticeDialog(getString(R.string.error_cache_directory));
					}
				} else {
					showNoticeDialog(getString(R.string.error_external_storage));
				}
				startActivityForResult(takePictureIntent, BaseInterface.REQUEST_CAMERA);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BaseInterface.REQUEST_GALLERY:
                    try {
                        //Uri uriGallery = data.getData();
                        //File file = new File(uriGallery.getPath());
                        //if (!file.exists()) {
                        //    file = new File(FileUtil.getRealPathFromURI(this, uriGallery));
                        //}
                        //setBitmapFromFile(file);
						setBitmapFromUri(data.getData());
                        return;
                    } catch (Exception e) {
                    	if (e.getMessage() != null) {
							Log.e(TAG, e.getMessage());
						}
                    	e.printStackTrace();
                        break;
                    }
                case BaseInterface.REQUEST_CAMERA:
                	if (this.mPhotoPath != null) {
						//setBitmapFromFile(new File(mImageUri.toString()));
						Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
						File f = new File(mPhotoPath);
						Uri contentUri = Uri.fromFile(f);
						mediaScanIntent.setData(contentUri);
						this.sendBroadcast(mediaScanIntent);
						try {
							setBitmapFromUri(Uri.fromFile(new File(mPhotoPath)));
						} catch (Exception e) {
							if (e.getMessage() != null) {
								Log.e(TAG, e.getMessage());
							}
							e.printStackTrace();
							break;
						}
					} else {
						this.mView.setBitmap((Bitmap) data.getExtras().get("data"));
					}
                    return;
            }
        }
        finish();
    }

	private void setBitmapFromUri(Uri uri) throws IOException {
		Bitmap set;
		Bitmap bitmap;

		if (Build.VERSION.SDK_INT >= 28) {
			bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), uri));
		} else {
			bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
		}
		if (bitmap != null) {
			//Scaling
			DisplayMetrics displayMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			int size = Math.min(displayMetrics.heightPixels, displayMetrics.widthPixels);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if (width > height) {
				double ratio = (double)(width)/(double)(height);
				bitmap = Bitmap.createScaledBitmap(bitmap, (int)(size*ratio), size, true);
			} else if (width < height) {
				double ratio = (double)(height)/(double)(width);
				bitmap = Bitmap.createScaledBitmap(bitmap, size, (int)(size*ratio),true);
			} else {
				bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
			}

			//Rotation
			int imgRotation = getImageRotationDegrees(uri);
			int endRotation = (imgRotation < 0) ? -imgRotation : imgRotation;
			endRotation %= 360;
			endRotation = 90 * (endRotation / 90);
			if (endRotation > 0 && bitmap != null) {
				Matrix m = new Matrix();
				m.setRotate(endRotation);
				Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (tmp != null) {
					bitmap.recycle();
					bitmap = tmp;
				}
			}
			this.mView.setBitmap(bitmap);
		}
	}

	public int getImageRotationDegrees(@NotNull Uri imgUri) {
		int photoRotation = ExifInterface.ORIENTATION_UNDEFINED;

		try {
			boolean hasRotation = false;
			//If image comes from the gallery and is not in the folder DCIM (Scheme: content://)
			String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
			Cursor cursor = this.getContentResolver().query(imgUri, projection, null, null, null);
			if (cursor != null) {
				if (cursor.getColumnCount() > 0 && cursor.moveToFirst()) {
					photoRotation = cursor.getInt(cursor.getColumnIndex(projection[0]));
					hasRotation = photoRotation != 0;
					Log.d(TAG, "Cursor orientation: " + photoRotation);
				}
				cursor.close();
			}

			//If image comes from the camera (Scheme: file://) or is from the folder DCIM (Scheme: content://)
			if (!hasRotation) {
				ExifInterface exif = new ExifInterface(getAbsolutePath(imgUri));
				int exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
				switch (exifRotation) {
					case ExifInterface.ORIENTATION_ROTATE_90: {
						photoRotation = 90;
						break;
					}
					case ExifInterface.ORIENTATION_ROTATE_180: {
						photoRotation = 180;
						break;
					}
					case ExifInterface.ORIENTATION_ROTATE_270: {
						photoRotation = 270;
						break;
					}
				}
				Log.d(TAG, "Exif orientation: "+ photoRotation);
			}
		} catch (IOException e) {
			Log.e(TAG, "Error determining rotation for image"+ imgUri, e);
		}
		return photoRotation;
	}

	private String getAbsolutePath(Uri uri) {
		String filePath = uri.getPath();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
			// Will return "image:x*"
			String[] wholeID = TextUtils.split(DocumentsContract.getDocumentId(uri), ":");
			// Split at colon, use second item in the array
			String type = wholeID[0];
			if (type.equalsIgnoreCase("image")) {
				String id = wholeID[1];
				String[] column = {MediaStore.Images.Media.DATA};
				// where id is equal to
				String sel = MediaStore.Images.Media._ID + "=?";
				Cursor cursor = this.getContentResolver().
						query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								column, sel, new String[]{id}, null);
				if (cursor != null) {
					int columnIndex = cursor.getColumnIndex(column[0]);
					if (cursor.moveToFirst()) {
						filePath = cursor.getString(columnIndex);
					}
					cursor.close();
				}
				Log.d(TAG, "Fetched absolute path for uri" + uri);
			}
		}
		return filePath;
	}

    protected final void cleanupView(View view) {
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback(null);
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable(null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(null);
        } else if (view instanceof SeekBar) {
            SeekBar sb = (SeekBar) view;
            sb.setProgressDrawable(null);
            sb.setThumb(null);
        } else if (view instanceof TextView) {
            ((TextView) view).setBackgroundDrawable(null);
        } else if (view instanceof Button) {
            ((Button) view).setBackgroundDrawable(null);
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.edit_bitmap_retake) {
			this.mView.setBitmap(null);
			startPhotoActivity(this.mode);
		} else if (id == R.id.edit_bitmap_ok) {
			//Can't pass in intent, too fat.
			Intent data = new Intent();
			Bitmap bitmap = this.mView.getBitmap();
			//setResult(RESULT_OK, data);
			//finish();

			File file;
			if (Build.VERSION.SDK_INT >= 8 && FileUtil.isAvailableExternalSDMemory()) {
				file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
			} else if (FileUtil.isAvailableInternalMemory()) {
				file = new File(getFilesDir(), FILENAME);
			} else {
				showNoticeDialog(getResources().getString(R.string.error_external_storage), dialog -> {
					finish();
				});
				return;
			}
			try {
				file.createNewFile();
				OutputStream outputStream = null;
				try {
					if (file.exists()) {
						outputStream = new FileOutputStream(file);
						if (bitmap != null) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
						}
					}
					data.putExtra("data", file.getAbsolutePath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (outputStream != null) {
						outputStream.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				setResult(RESULT_OK);
				finish();
			}
		}
//			try {
//				this.mView.saveBitmap(FILENAME);
//				OutputStream outputStream = null;
//				try {
//					File dir = new File(this.mRootDir.getPath());
//					if (!dir.exists()) {
//						dir.mkdirs();
//					}
//					File file = new File(dir, FILENAME);
//					if (file.exists()) {
//						Bitmap resized = loadBitmap(file, 200, 200);
//						outputStream = new FileOutputStream(file);
//						if (resized != null) {
//							resized.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//						}
//					}
//					if (outputStream != null) {
//						outputStream.close();
//					}
//				} catch (FileNotFoundException e) {
//					if (outputStream != null) {
//						outputStream.close();
//					}
//					setResult(RESULT_OK);
//					finish();
//					return;
//				} catch (Throwable th) {
//					if (outputStream != null) {
//						outputStream.close();
//					}
//					throw th;
//				}
//			} catch (Throwable th) {
//				th.printStackTrace();
//			}
//			setResult(RESULT_OK);
//			finish();
//		}
	}

    public static File getTempFile(Context context) {
		if (Build.VERSION.SDK_INT >= 8 && FileUtil.isAvailableExternalSDMemory()) {
			return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
		} else if (FileUtil.isAvailableInternalMemory()) {
			return new File(context.getFilesDir(), FILENAME);
		} else {
			return null;
		}

//        String mRootPath2;
//        if (FileUtil.isAvailableExternalSDMemory()) {
//            mRootPath2 = Environment.getExternalStorageDirectory().getPath() + "/barcodekanojo/";
//        } else if (!FileUtil.isAvailableInternalMemory()) {
//            return null;
//        } else {
//            mRootPath2 = Environment.getDataDirectory().getAbsolutePath() + "/data/jp.co.cybird.barcodekanojoForGAM/files/barcodekanojo/";
//        }
//        File dir = new File(mRootPath2);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        File file = new File(dir, FILENAME);
//        if (file.exists()) {
//            return file;
//        }
//        return null;
    }

    //private Bitmap loadBitmap(File file, int view_width, int view_height) {
    //    int sample_size;
    //    if (file == null) {
    //        return null;
    //    }
    //    BitmapFactory.Options option = new BitmapFactory.Options();
    //    option.inJustDecodeBounds = true;
    //    BitmapFactory.decodeFile(file.getAbsolutePath(), option);
    //    if (option.outWidth * option.outHeight > 1048576) {
    //        sample_size = (int) (Math.sqrt(((double) (option.outWidth * option.outHeight)) / 1048576.0d) + 1.0d);
    //    } else {
    //        sample_size = 1;
    //    }
    //    option.inJustDecodeBounds = false;
    //    option.inSampleSize = sample_size;
    //    Bitmap src = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
    //    if (src == null) {
    //        return null;
    //    }
    //    int src_width = src.getWidth();
    //    int src_height = src.getHeight();
    //    float scale = getFitScale(view_width, view_height, src_width, src_height);
    //    Matrix matrix = new Matrix();
    //    matrix.postScale(scale, scale);
    //    return Bitmap.createBitmap(src, 0, 0, src_width, src_height, matrix, true);
    //}

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

    //private static final void createDirectory(File storageDirectory) {
    //    if (!storageDirectory.exists()) {
    //        Log.d(TAG, "Trying to create storageDirectory: " + String.valueOf(storageDirectory.mkdirs()));
    //        Log.d(TAG, "Exists: " + storageDirectory + " " + String.valueOf(storageDirectory.exists()));
    //        Log.d(TAG, "State: " + Environment.getExternalStorageState());
    //        Log.d(TAG, "Isdir: " + storageDirectory + " " + String.valueOf(storageDirectory.isDirectory()));
    //        Log.d(TAG, "Readable: " + storageDirectory + " " + String.valueOf(storageDirectory.canRead()));
    //        Log.d(TAG, "Writable: " + storageDirectory + " " + String.valueOf(storageDirectory.canWrite()));
    //        File tmp = storageDirectory.getParentFile();
    //        Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
    //        Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
    //        Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
    //        Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));
    //        File tmp2 = tmp.getParentFile();
    //        Log.d(TAG, "Exists: " + tmp2 + " " + String.valueOf(tmp2.exists()));
    //        Log.d(TAG, "Isdir: " + tmp2 + " " + String.valueOf(tmp2.isDirectory()));
    //        Log.d(TAG, "Readable: " + tmp2 + " " + String.valueOf(tmp2.canRead()));
    //        Log.d(TAG, "Writable: " + tmp2 + " " + String.valueOf(tmp2.canWrite()));
    //    }
    //    File nomediaFile = new File(storageDirectory, NOMEDIA);
    //    if (!nomediaFile.exists()) {
    //        try {
    //            Log.d(TAG, "Created file: " + nomediaFile + " " + nomediaFile.createNewFile());
    //        } catch (IOException e) {
    //            Log.d(TAG, "Unable to create .nomedia file for some reason.", e);
    //            throw new IllegalStateException("Unable to create nomedia file. PATH=" + storageDirectory.getAbsolutePath() + " Directory exists=" + storageDirectory.exists());
    //        }
    //    }
    //    if (!storageDirectory.isDirectory() || !nomediaFile.exists()) {
    //        throw new RuntimeException("Unable to create storage directory and nomedia file.");
    //    }
    //}
}
