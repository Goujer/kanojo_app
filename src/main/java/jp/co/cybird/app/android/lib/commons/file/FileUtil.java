package jp.co.cybird.app.android.lib.commons.file;

import android.content.Context;
import android.os.Environment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import jp.co.cybird.app.android.lib.commons.log.DLog;

public class FileUtil {
    public static final String EXTERNAL_STORAGE_DATA_DIR = "/Android/data";

    public static void saveFileInAppDirectory(Context context, InputStream is, String fileName) throws FileNotFoundException, IOException {
        if (is == null) {
            DLog.d("CAC", "InputStream is NULL.");
            return;
        }
        FileOutputStream os = context.getApplicationContext().openFileOutput(fileName, 0);
        DataInputStream dis = new DataInputStream(is);
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
        byte[] buff = new byte[1024];
        while (true) {
            try {
                int bLen = dis.read(buff);
                if (bLen <= 0) {
                    break;
                }
                dos.write(buff, 0, bLen);
            } finally {
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
            }
        }
        dos.flush();
    }

    public static void saveFileInAppDirectory(Context context, String str, String fileName, boolean utf) throws FileNotFoundException, IOException {
        if (str == null) {
            DLog.d("CAC", "InputStream is NULL.");
            return;
        }
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(context.getApplicationContext().openFileOutput(fileName, 0)));
        dos.write(str.getBytes());
        dos.flush();
        dos.close();
    }

    public static void saveFileInExternalStorage(Context context, InputStream is, String filePath) throws IOException {
        makeCommonIconCacheDir(filePath);
        FileOutputStream os = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(is);
        while (true) {
            int bufferLength = bis.read(buffer);
            if (bufferLength <= 0) {
                bis.close();
                os.close();
                is.close();
                DLog.d("FU", "After save");
                return;
            }
            DLog.d("FU", "before write");
            os.write(buffer, 0, bufferLength);
            DLog.d("FU", "bufferLength: " + bufferLength);
        }
    }

    public static void makeCommonIconCacheDir(String filePath) {
        String externalPath = getExternalStorageDataDirPath();
        if (!filePath.startsWith(externalPath)) {
            if (!filePath.startsWith(File.separator)) {
                filePath = String.valueOf(File.separator) + filePath;
            }
            filePath = externalPath.concat(filePath);
        }
        File dir = new File(new File(filePath).getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static File getExternalStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public static String getExternalStoragePath() {
        return getExternalStorage().getPath();
    }

    public static String getExternalStorageDataDirPath() {
        return getExternalStoragePath().concat(EXTERNAL_STORAGE_DATA_DIR);
    }

    public static String getExternalStoragePackageDataDir(Context context, boolean mkdir) {
        String dirPath = getExternalStorageDataDirPath().concat(String.valueOf(File.separator) + context.getPackageName());
        if (mkdir) {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return dirPath;
    }

    public static File getDownloadCacheDir() {
        return Environment.getDownloadCacheDirectory();
    }

    public static String getDownloadCacheDirPath() {
        return getDownloadCacheDir().getPath();
    }
}
