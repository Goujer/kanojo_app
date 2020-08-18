package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.content.Context;
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

public class Live2dDiskCache implements DiskCache {

    private static final int MIN_FILE_SIZE_IN_BYTES = 100;
    private static final String NOMEDIA = ".nomedia";
    private static final String TAG = "Live2dDiskCache";
    private File mStorageDirectory;

    public Live2dDiskCache(Context context, String dirPath, String name) {
        File storageDirectory = new File(new File(Environment.getExternalStorageDirectory(), dirPath), name);
        createDirectory(storageDirectory);
        this.mStorageDirectory = storageDirectory;
    }

    public boolean exists(String key) {
        return getFile(key).exists();
    }

    public File getFile(String hash) {
        return new File(this.mStorageDirectory.toString() + File.separator + hash);
    }

    public InputStream getInputStream(String hash) throws IOException {
        return new FileInputStream(getFile(hash));
    }

    public void store(String key, InputStream is) {
        InputStream is2 = new BufferedInputStream(is);
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getFile(key)));
            byte[] b = new byte[2048];
            int total = 0;
            while (true) {
                int count = is2.read(b);
                if (count <= 0) {
                    break;
                }
                os.write(b, 0, count);
                total += count;
            }
            os.close();
            if (key.substring(key.length() - 4, key.length()).equals(".zip")) {
                File parent = null;
                try {
                    FileUtil.unzipFile(getFile(key), this.mStorageDirectory.toString());
                    parent = getFile(key).getParentFile();
                    getFile(key).delete();
                } catch (IOException e) {
                }
                if (parent != null) {
                    createNoMediaFile(parent);
                }
                if (parent != null) {
                    try {
                        File gomi = new File(parent, "__MACOSX");
                        if (gomi != null) {
                            delete(gomi);
                        }
                    } catch (Exception e2) {
                    }
                }
            }
        } catch (IOException e3) {
        }
    }

    private static void delete(File f) {
        if (f.exists()) {
            if (f.isFile()) {
                f.delete();
            }
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File delete : files) {
                    delete(delete);
                }
                f.delete();
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
                if (!child.equals(new File(this.mStorageDirectory, NOMEDIA)) && child.length() <= MIN_FILE_SIZE_IN_BYTES) {
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

    private static final void createNoMediaFile(File rootDirectory) {
        if (rootDirectory.exists() && !rootDirectory.isFile() && rootDirectory.isDirectory()) {
            File[] files = rootDirectory.listFiles();
            for (File createNoMediaFile : files) {
                createNoMediaFile(createNoMediaFile);
            }
            File nomediaFile = new File(rootDirectory, NOMEDIA);
            if (!nomediaFile.exists()) {
                try {
                    Log.d(TAG, "Created file: " + nomediaFile + " " + nomediaFile.createNewFile());
                } catch (IOException e) {
                    Log.d(TAG, "Unable to create .nomedia file for some reason.", e);
                    throw new IllegalStateException("Unable to create nomedia file.");
                }
            }
            if (!rootDirectory.isDirectory() || !nomediaFile.exists()) {
                throw new RuntimeException("Unable to create storage directory and nomedia file.");
            }
        }
    }

    public String encode(Uri uri) {
        return Uri.encode(uri.toString());
    }
}
