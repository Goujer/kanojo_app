package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;

public class BaseDiskCache implements DiskCache {
    private static final boolean DEBUG = false;
    private static final int EXTERNAL_MEMORY_AVAILABLE = 1;
    private static final int INTERNAL_MEMORY_AVAILABLE = 2;
    private static final int MIN_FILE_SIZE_IN_BYTES = 100;
    private static final String NOMEDIA = ".nomedia";
    private static final int STORAGE_NOT_AVAILABLE = 0;
    private static final String TAG = "BaseDiskCache";
    private String mBaseDiskName;
    private Context mContext;
    private Digest mDigest;
    private String mDirPath;
    private BaseDiskCallBack mListener;
    private File mStorageDirectory;

    public interface BaseDiskCallBack {
        void onSaveFailed(String str);

        void onSaveFinished(String str);
    }

    BaseDiskCache(String dirPath, String name, Context context) {
        File baseDirectory;
        this.mContext = context;
        this.mBaseDiskName = name;
        this.mDirPath = dirPath;
        if (hasAvailableStorge(context) == 1) {
            baseDirectory = new File(Environment.getExternalStorageDirectory(), dirPath);
        } else if (hasAvailableStorge(context) == 2) {
            baseDirectory = new File(this.mContext.getFilesDir(), dirPath);
        } else {
            return;
        }
        File storageDirectory = new File(baseDirectory, name);
        createDirectory(storageDirectory);
        this.mStorageDirectory = storageDirectory;
        cleanup();
    }

    BaseDiskCache(String dirPath, String name, BaseDiskCallBack listener, Context context) {
        File storageDirectory = new File(new File(Environment.getExternalStorageDirectory(), dirPath), name);
        createDirectory(storageDirectory);
        this.mStorageDirectory = storageDirectory;
        cleanup();
        this.mListener = listener;
    }

    BaseDiskCache(File root, String dirPath, String name) {
        File storageDirectory = new File(new File(root, dirPath), name);
        createDirectory(storageDirectory);
        this.mStorageDirectory = storageDirectory;
        cleanupSimple();
    }

    public boolean exists(String key) {
        return getFile(key).exists();
    }

    public File getFile(String hash) {
        File result = getFile(createDirectory(false, this.mDirPath, this.mBaseDiskName), hash);
        if ((result == null || !result.exists()) && FileUtil.isAccessExternalMemory()) {
            return getFile(createDirectory(true, this.mDirPath, this.mBaseDiskName), hash);
        }
        return result;
    }

    public File getFile(File root, String hash) {
        File result = new File(String.valueOf(root.toString()) + File.separator + hash);
        if (result == null || !result.exists()) {
        }
        return result;
    }

    public File getFile(String hash, boolean isReadOnly) {
        File mTempStorageDirectory;
        if (isReadOnly) {
            return getFile(hash);
        }
        if (hasAvailableStorge(this.mContext) == 1) {
            mTempStorageDirectory = createDirectory(true, this.mDirPath, this.mBaseDiskName);
        } else if (hasAvailableStorge(this.mContext) != 2) {
            return null;
        } else {
            mTempStorageDirectory = createDirectory(false, this.mDirPath, this.mBaseDiskName);
        }
        return new File(String.valueOf(mTempStorageDirectory.toString()) + File.separator + hash);
    }

    public InputStream getInputStream(String hash) throws IOException {
        return new FileInputStream(getFile(hash));
    }

    public void store(String key, InputStream is) {
        if (hasAvailableStorge(this.mContext) != 0) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            try {
                OutputStream os = new BufferedOutputStream(new FileOutputStream(getFile(key, false)));
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
                os.close();
                if (this.mListener != null) {
                    this.mListener.onSaveFinished(key);
                }
                BufferedInputStream bufferedInputStream2 = bufferedInputStream;
            } catch (IOException e) {
                if (this.mListener != null) {
                    this.mListener.onSaveFailed(key);
                }
                BufferedInputStream bufferedInputStream3 = bufferedInputStream;
            }
        }
    }

    public void invalidate(String key) {
        getFile(key).delete();
    }

    public void cleanup() {
        String[] children = this.mStorageDirectory.list();
        if (children != null) {
            for (String file : children) {
                File child = new File(this.mStorageDirectory, file);
                if (!child.equals(new File(this.mStorageDirectory, NOMEDIA)) && child.length() <= 100) {
                    child.delete();
                }
            }
        }
    }

    public void cleanupSimple() {
        String[] children = this.mStorageDirectory.list();
        if (children != null && children.length > 500) {
            int i = children.length - 1;
            int m = i - 50;
            while (i > m) {
                new File(this.mStorageDirectory, children[i]).delete();
                i--;
            }
        }
    }

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
                throw new IllegalStateException("Unable to create nomedia file.");
            }
        }
        if (!storageDirectory.isDirectory() || !nomediaFile.exists()) {
            throw new RuntimeException("Unable to create storage directory and nomedia file.");
        }
    }

    public String encode(Uri uri) {
        return getDigest().hex(uri.toString());
    }

    private Digest getDigest() {
        if (this.mDigest == null) {
            this.mDigest = new Digest(Digest.SHA256);
        }
        return this.mDigest;
    }

    private int hasAvailableStorge(Context context) {
        boolean isAvailableExternalMemory = FileUtil.isAvailableExternalSDMemory();
        boolean isAvailableInternalMemory = FileUtil.isAvailableInternalMemory();
        if (isAvailableExternalMemory) {
            return 1;
        }
        if (isAvailableInternalMemory) {
            return 2;
        }
        context.sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
        return 0;
    }

    private File createDirectory(boolean isExternalMemeory, String dirPath, String name) {
        File baseDirectory;
        if (isExternalMemeory) {
            baseDirectory = new File(Environment.getExternalStorageDirectory(), this.mDirPath);
        } else {
            baseDirectory = new File(this.mContext.getFilesDir(), this.mDirPath);
        }
        File storageDirectory = new File(baseDirectory, this.mBaseDiskName);
        createDirectory(storageDirectory);
        return storageDirectory;
    }
}
