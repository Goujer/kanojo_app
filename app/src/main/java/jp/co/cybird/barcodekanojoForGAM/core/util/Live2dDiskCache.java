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

public class Live2dDiskCache {

    private static final String TAG = "Live2dDiskCache";

	private final File mStorageDirectory;

    public Live2dDiskCache(Context context, String dirPath, String name) {
        File storageDirectory = new File(new File(Environment.getExternalStorageDirectory(), dirPath), name);
        FileUtil.createDirectory(storageDirectory);
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

    public void put(String key, InputStream is) {
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
            if (key.endsWith(".zip")) {
                File parent = null;
                try {
                    FileUtil.unzipFile(getFile(key), this.mStorageDirectory.toString());
                    parent = getFile(key).getParentFile();
                    getFile(key).delete();
                } catch (IOException ignored) {
                }
                if (parent != null) {
                    createNoMediaFile(parent);
                }
                if (parent != null) {
                    try {
                        File gomi = new File(parent, "__MACOSX");
						delete(gomi);
					} catch (Exception ignored) {
                    }
                }
            }
        } catch (IOException ignored) {
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

    public void remove(String key) {
        getFile(key).delete();
    }

    private static final void createNoMediaFile(File rootDirectory) {
        if (rootDirectory.exists() && !rootDirectory.isFile() && rootDirectory.isDirectory()) {
            File[] files = rootDirectory.listFiles();
            for (File createNoMediaFile : files) {
                createNoMediaFile(createNoMediaFile);
            }
            File nomediaFile = new File(rootDirectory, FileUtil.NOMEDIA);
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
