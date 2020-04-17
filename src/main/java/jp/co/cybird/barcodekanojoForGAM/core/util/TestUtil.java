package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.util.Log;

public class TestUtil {
    private static long end;
    private static long start;

    public static void startLog(String tag, String method) {
        start = System.currentTimeMillis();
        Log.d(tag, String.valueOf(method) + ":start ");
    }

    public static void endLog(String tag, String method) {
        end = System.currentTimeMillis();
        Log.d(tag, String.valueOf(method) + ":end time = " + (end - start));
    }
}
