package com.google.tagmanager;

import android.annotation.TargetApi;
import android.os.Build;
import java.io.File;

class FutureApis {
    private FutureApis() {
    }

    public static int version() {
        try {
            return Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            Log.e("Invalid version number: " + Build.VERSION.SDK);
            return 0;
        }
    }

    @TargetApi(9)
    static boolean setOwnerOnlyReadWrite(String path) {
        if (version() < 9) {
            return false;
        }
        File file = new File(path);
        file.setReadable(false, false);
        file.setWritable(false, false);
        file.setReadable(true, true);
        file.setWritable(true, true);
        return true;
    }
}
