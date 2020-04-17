package jp.co.cybird.app.android.lib.utils;

import android.content.Context;
import com.google.ads.AdActivity;

public class b {
    public static native String a(Context context);

    static {
        System.loadLibrary(AdActivity.URL_PARAM);
    }
}
