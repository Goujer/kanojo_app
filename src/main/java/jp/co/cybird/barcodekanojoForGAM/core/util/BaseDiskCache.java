package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;

import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;

public final class BaseDiskCache implements DiskCache {

    private static final int EXTERNAL_MEMORY_AVAILABLE = 1;
    private static final int INTERNAL_MEMORY_AVAILABLE = 2;
    private static final int MIN_FILE_SIZE_IN_BYTES = 100;
    private static final String NOMEDIA = ".nomedia";
    private static final int STORAGE_NOT_AVAILABLE = 0;
    private static final String TAG = "BaseDiskCache";
    private Context mContext;
    private Digest mDigest;
    private File mStorageDirectory;

    public BaseDiskCache(Context context) {
        File storageDirectory = FileUtil.getCacheDirectory(context);
        this.mContext = context;
		if (FileUtil.isCacheDirectoryFull(context)) {
			context.sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
		}
        createDirectory(storageDirectory);
        this.mStorageDirectory = storageDirectory;
        cleanup();
    }

//    BaseDiskCache(String dirPath, String name, BaseDiskCallBack listener, Context context) {
//    	this(dirPath, name, context);
//        this.mListener = listener;
//    }

//    BaseDiskCache(File root, String dirPath, String name) {
//        File storageDirectory = new File(new File(root, dirPath), name);
//        createDirectory(storageDirectory);
//        this.mStorageDirectory = storageDirectory;
//        cleanupSimple();
//    }

    public boolean exists(String key) {
        return new File(createDirectory().toString() + File.separator + key).exists();
    }

	public File getFile(String key) throws IOException {
		File result = new File(createDirectory().toString() + File.separator + key);
		if (!result.exists()) {
			result.getParentFile().mkdirs();
			result.createNewFile();
		}
		return result;
	}

    public File getReadOnlyFile(String key) {
		return new File(createDirectory().toString() + File.separator + key);
	}

    public InputStream getInputStream(String key) throws IOException {
        return new FileInputStream(getReadOnlyFile(key));
    }

    public OutputStream getOutputStream(String key) throws IOException {
    	return new BufferedOutputStream(new FileOutputStream(getReadOnlyFile(key)));
	}

    public void store(String key, InputStream is) throws IOException {
        if (hasAvailableStorge(this.mContext) != 0) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
			OutputStream os = new BufferedOutputStream(new FileOutputStream(getFile(key)));
            try {
                byte[] b = new byte[2048];
                int total = 0;
                while (true) {
                    int count = bufferedInputStream.read(b);
                    if (count <= 0) {
                        break;
                    }
                    os.write(b, 0, count);
                    total += count;
                }
            } catch (IOException e) {
				if (Defs.DEBUG) {
					Log.e(TAG, "Error while saving cache file.");
					e.printStackTrace();
				}
				File deadFile = getReadOnlyFile(key);
				deadFile.delete();
			} finally {
            	os.close();
				bufferedInputStream.close();
			}
		}
    }

    public void invalidate(String key) {
		getReadOnlyFile(key).delete();
    }

    public void cleanup() {
    	cleanup(mStorageDirectory);
        //String[] children = this.mStorageDirectory.list();
        //if (children != null) {
        //    for (String file : children) {
        //        File child = new File(this.mStorageDirectory, file);
        //        if (!child.equals(new File(this.mStorageDirectory, NOMEDIA)) && child.length() <= MIN_FILE_SIZE_IN_BYTES) {
        //            child.delete();
        //        }
        //    }
        //}
    }

    private void cleanup(File parent) {
		String[] children = parent.list();
		if (children != null) {
			for (String file : children) {
				File child = new File(parent, file);
				if (child.isDirectory()) {
					if (child.list().length == 0) {
						child.delete();
					} else {
						cleanup(child);
					}
				} else if (child.isFile()) {
					if (!child.equals(new File(this.mStorageDirectory, NOMEDIA)) && child.length() <= MIN_FILE_SIZE_IN_BYTES) {
						child.delete();
					}
				}
			}
		}
	}

//    public void cleanupSimple() {
//        String[] children = this.mStorageDirectory.list();
//        if (children != null && children.length > 500) {
//            int i = children.length - 1;
//            int m = i - 50;
//            while (i > m) {
//                new File(this.mStorageDirectory, children[i]).delete();
//                i--;
//            }
//        }
//    }

    public void clear() {
        String[] children = this.mStorageDirectory.list();
        if (children != null) {
            for (String file : children) {
                File child = new File(this.mStorageDirectory, file);
                if (!child.equals(new File(this.mStorageDirectory, NOMEDIA))) {
                    child.delete();
                }
            }
        }
        this.mStorageDirectory.delete();
    }

    private static final void createDirectory(File storageDirectory) {
        if (!storageDirectory.exists()) {
            Log.d(TAG, "Trying to create storageDirectory: " + storageDirectory.mkdirs());
            Log.d(TAG, "Exists: " + storageDirectory + " " + storageDirectory.exists());
            Log.d(TAG, "State: " + Environment.getExternalStorageState());
            Log.d(TAG, "Isdir: " + storageDirectory + " " + storageDirectory.isDirectory());
            Log.d(TAG, "Readable: " + storageDirectory + " " + storageDirectory.canRead());
            Log.d(TAG, "Writable: " + storageDirectory + " " + storageDirectory.canWrite());
            File tmp = storageDirectory.getParentFile();
            Log.d(TAG, "Exists: " + tmp + " " + tmp.exists());
            Log.d(TAG, "Isdir: " + tmp + " " + tmp.isDirectory());
            Log.d(TAG, "Readable: " + tmp + " " + tmp.canRead());
            Log.d(TAG, "Writable: " + tmp + " " + tmp.canWrite());
            File tmp2 = tmp.getParentFile();
            Log.d(TAG, "Exists: " + tmp2 + " " + tmp2.exists());
            Log.d(TAG, "Isdir: " + tmp2 + " " + tmp2.isDirectory());
            Log.d(TAG, "Readable: " + tmp2 + " " + tmp2.canRead());
            Log.d(TAG, "Writable: " + tmp2 + " " + tmp2.canWrite());
        }
        File nomediaFile = new File(storageDirectory, NOMEDIA);
        if (!nomediaFile.exists()) {
            try {
                Log.d(TAG, "Created file: " + nomediaFile + " " + nomediaFile.createNewFile());
            } catch (IOException e) {
                Log.d(TAG, "Unable to create .nomedia file for some reason.", e);
                throw new IllegalStateException("Unable to create nomedia file.");
            }
        }
        if (!storageDirectory.isDirectory() || !nomediaFile.exists()) {
            throw new RuntimeException("Unable to create storage directory and nomedia file.");
        }
    }

    //public String encode(Uri uri) {
    //    return getDigest().hex(uri.toString());
    //}

    //private Digest getDigest() {
    //    if (this.mDigest == null) {
    //        this.mDigest = new Digest(Digest.SHA256);
    //    }
    //    return this.mDigest;
    //}

    private int hasAvailableStorge(Context context) {
        boolean isAvailableExternalMemory = FileUtil.isAvailableExternalSDMemory();
        boolean isAvailableInternalMemory = FileUtil.isAvailableInternalMemory();
        if (isAvailableExternalMemory) {
            return EXTERNAL_MEMORY_AVAILABLE;
        }
        if (isAvailableInternalMemory) {
            return INTERNAL_MEMORY_AVAILABLE;
        }
        context.sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
        return STORAGE_NOT_AVAILABLE;
    }

    private File createDirectory() {
		File storageDirectory = FileUtil.getCacheDirectory(this.mContext);
        createDirectory(storageDirectory);
        return storageDirectory;
    }
}
