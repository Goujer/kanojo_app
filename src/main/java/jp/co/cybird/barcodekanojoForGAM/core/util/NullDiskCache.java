package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class NullDiskCache implements DiskCache {
    public boolean exists(String key) {
        return false;
    }

    public File getFile(String key) {
        return null;
    }

    public InputStream getInputStream(String key) throws IOException {
        throw new FileNotFoundException();
    }

    public void store(String key, InputStream is) {
    }

    public void cleanup() {
    }

    public void invalidate(String key) {
    }

    public void clear() {
    }

    public String encode(Uri uri) {
        return "";
    }
}
