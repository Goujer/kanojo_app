package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.BuildConfig;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;

public class FileUtil {
    public static final int AT_LEAST_AVAILABLE_SPACE = 10;
    private static final int CHUNK_SIZE = 32768;
    public static final int ICON_QUALITY = 100;
    public static final String ICON_TEMP = "icon_temp.png";
    public static final String PRODUCT_TEMP = "";
    public static final String PROFILE_TEMP = "";
    public static final String TAG = FileUtil.class.getSimpleName();
    private static final byte[] _fileIOBuffer = new byte[32768];

	public static String getRealPathFromURI(Activity activity, Uri contentUri) {
		Cursor cursor = activity.managedQuery(contentUri, new String[]{"_data"}, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow("_data");
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static File createIconCache(Context context, Kanojo kanojo) throws BarcodeKanojoException {
		Bitmap bitmap = Live2dUtil.createNormalIcon(context, kanojo);
		if (bitmap == null) {
			throw new BarcodeKanojoException("cant crate icon. bitmap is null");
		}
		try {
			writeDataFile(context, ICON_TEMP, bmpToByte(bitmap, Bitmap.CompressFormat.PNG, 100));
			bitmap.recycle();
			return new File(context.getFilesDir(), ICON_TEMP);
		} catch (Exception e) {
			throw new BarcodeKanojoException("cant create icon cache:");
		}
	}

	public static byte[] bmpToByte(Bitmap src, Bitmap.CompressFormat format, int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);
		return os.toByteArray();
	}

	public static void writeDataFile(Context context, String name, byte[] w) throws Exception {
		OutputStream out = null;
		try {
			out = context.openFileOutput(name, 0);
			out.write(w, 0, w.length);
			out.close();
		} catch (Exception e) {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
			throw e;
		}
	}

//	public static Bitmap ByteTobmp(byte[] data) {
//		return BitmapFactory.decodeByteArray(data, 0, data.length);
//	}

//	public static byte[] readDataFile(Context context, String name) throws Exception {
//		InputStream in = null;
//		try {
//			in = context.openFileInput(name);
//			byte[] w = new byte[102400];
//			int size = in.read(w);
//			in.close();
//			byte[] result = new byte[size];
//			System.arraycopy(w, 0, result, 0, size);
//			return result;
//		} catch (Exception e) {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (Exception e2) {
//				}
//			}
//			throw e;
//		}
//	}

	//JADX Error
	/* JADX WARNING: Removed duplicated region for block: B:24:0x0047  */
	/* JADX WARNING: Removed duplicated region for block: B:42:0x0082  */
	/* JADX WARNING: Removed duplicated region for block: B:44:0x0087  */
	/* JADX WARNING: Removed duplicated region for block: B:47:0x008e  */
	/* JADX WARNING: Removed duplicated region for block: B:49:0x0093  */
	/* JADX WARNING: Removed duplicated region for block: B:68:? A[RETURN, SYNTHETIC] */
	/* JADX WARNING: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
	public static void unzipFile(File zipFile, String directory) throws IOException {
		ZipInputStream in = null;
		FileOutputStream os = null;
		try {
			ZipInputStream in2 = new ZipInputStream(new FileInputStream(zipFile));
			FileOutputStream os2 = null;
			while (true) {
				try {
					ZipEntry entry = in2.getNextEntry();
					if (entry == null) {
						break;
					}
					String entryName = entry.getName();
					if (entry.isDirectory()) {
						new File(directory, entryName).mkdirs();
					} else {
						File file = new File(directory, entryName);
						if (file.exists()) {
							file.delete();
						}
						os = new FileOutputStream(file);
						int unzippedSize = 0;
						while (true) {
							try {
								int bytesRead = in2.read(_fileIOBuffer);
								if (bytesRead == -1) {
									break;
								}
								os.write(_fileIOBuffer, 0, bytesRead);
								unzippedSize += bytesRead;
							} catch (FileNotFoundException e) {
								e = e;
								in = in2;
								try {
									Log.v("unzip", e.getMessage());
									if (in != null) {
									}
									if (os == null) {
									}
								} catch (Throwable th) {
									th = th;
									if (in != null) {
									}
									if (os != null) {
									}
									throw th;
								}
							} catch (IOException e2) {
								in = in2;
								Log.v("unzip", e2.getMessage());
								if (in != null) {
								}
								if (os != null) {
								}
							} catch (Throwable th2) {
								in = in2;
								if (in != null) {
									in.close();
								}
								if (os != null) {
									os.close();
								}
								throw th2;
							}
						}
						os.close();
						os2 = os;
					}
				} catch (FileNotFoundException e3) {
					os = os2;
					in = in2;
				} catch (IOException e4) {
					os = os2;
					in = in2;
					Log.v("unzip", e4.getMessage());
					if (in != null) {
					}
					if (os != null) {
					}
				} catch (Throwable th3) {
					os = os2;
					in = in2;
					if (in != null) {
					}
					if (os != null) {
					}
					th3.printStackTrace();
				}
			}
			if (in2 != null) {
				in2.close();
			}
			if (os2 != null) {
				os2.close();
			}
			FileOutputStream fileOutputStream = os2;
			ZipInputStream zipInputStream = in2;
		} catch (FileNotFoundException e5) {
			Log.v("unzip", e5.getMessage());
			if (in != null) {
				in.close();
			}
			if (os == null) {
				os.close();
			}
		} catch (IOException e6) {
			Log.v("unzip", e6.getMessage());
			if (in != null) {
				in.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}

//	public static void WriteToFile(String fileName, String content) throws IOException {
//		FileOutputStream fos = new FileOutputStream(new File(String.valueOf(String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/barcodekanojo/") + Preferences.PREFERENCE_DEVICE_TOKEN));
//		OutputStreamWriter writer = new OutputStreamWriter(fos);
//		writer.append(content);
//		writer.close();
//		fos.close();
//	}

//	public static File GetFile(String fileName) {
//		File tokenFile = new File(String.valueOf(String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/barcodekanojo/") + Preferences.PREFERENCE_DEVICE_TOKEN);
//		if (tokenFile == null || !tokenFile.exists()) {
//			try {
//				tokenFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return tokenFile;
//	}

	public static boolean isAvailableExternalSDMemory() {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			return false;
		}
		StatFs stats = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
		int availableBytes = stats.getAvailableBlocks() * stats.getBlockSize();
		if (((float) (availableBytes / 1048576)) >= 10.0f || availableBytes < 1000) {
			return true;
		}
		return false;
	}

	public static boolean isAvailableInternalSDMemory() {
		try {
			if (Environment.getExternalStorageState().equals("mounted")) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

//	public static boolean isAccessExternalMemory() {
//		if (Environment.getExternalStorageState().equals("mounted")) {
//			return true;
//		}
//		return false;
//	}

	public static boolean isAvailableInternalMemory() {
		StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
		if ((((float) statFs.getAvailableBlocks()) * ((float) statFs.getBlockSize())) / 1048576.0f >= 10.0f) {
			return true;
		}
		return false;
	}

	public static boolean isCacheDirectoryFull(Context context) {
		String path;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (Build.VERSION.SDK_INT <= 8) {
				path = Environment.getExternalStorageDirectory().getAbsolutePath();
			} else {
				path = context.getExternalCacheDir().getAbsolutePath();
			}
		} else {
			path = context.getCacheDir().getAbsolutePath();
		}
		StatFs statFs = new StatFs(path);
		return ((((float) statFs.getAvailableBlocks()) * ((float) statFs.getBlockSize())) / 1048576.0f < 10.0f);
	}

	public static File getCacheDirectory(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (Build.VERSION.SDK_INT <= 8) {
				return new File(Environment.getExternalStorageDirectory(), "Android" + File.pathSeparator + "data" + File.pathSeparator + BuildConfig.APPLICATION_ID + File.pathSeparator + "cache");
			} else {
				return context.getExternalCacheDir();
			}
		} else {
			return context.getCacheDir();
		}
	}
}
