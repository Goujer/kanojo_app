package jp.live2d.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UtFile {
    public static byte[] loadFile(String path) throws IOException {
        return loadFile(new File(path));
    }

    public static byte[] loadFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        int len = (int) file.length();
        byte[] bin = new byte[len];
        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file), 8192);
        int off = 0;
        while (true) {
            int read = fin.read(bin, off, len - off);
            if (read <= 0) {
                return bin;
            }
            off += read;
        }
    }

    public static byte[] load(InputStream in) throws IOException {
        ByteArrayOutputStream bo = null;
        try {
            BufferedInputStream bin = in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in, 8192);
            ByteArrayOutputStream bo2 = new ByteArrayOutputStream();
            try {
                byte[] buf = new byte[1000];
                while (true) {
                    int load = bin.read(buf);
                    if (load <= 0) {
                        break;
                    }
                    bo2.write(buf, 0, load);
                }
                byte[] byteArray = bo2.toByteArray();
                if (in != null) {
                    in.close();
                }
                if (bo2 != null) {
                    bo2.flush();
                    bo2.close();
                }
                return byteArray;
            } catch (Throwable th) {
                th = th;
                bo = bo2;
            }
        } catch (Throwable th2) {
            if (in != null) {
                in.close();
            }
            if (bo != null) {
                bo.flush();
                bo.close();
            }
            throw th2;
        }
    }
}
