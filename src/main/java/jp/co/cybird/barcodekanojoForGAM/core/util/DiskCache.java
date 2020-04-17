package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.net.Uri;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface DiskCache {
    void cleanup();

    void clear();

    String encode(Uri uri);

    boolean exists(String str);

    File getFile(String str);

    InputStream getInputStream(String str) throws IOException;

    void invalidate(String str);

    void store(String str, InputStream inputStream);
}
