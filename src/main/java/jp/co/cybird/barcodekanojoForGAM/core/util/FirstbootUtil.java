package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.content.Context;
import android.content.SharedPreferences;

public class FirstbootUtil {

    private static final String PREF_KEY = "BarcodeKANOJO_firstboot";

    public static boolean isShowed(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, 0);
        boolean ret = false;
        if (pref.contains(key)) {
            ret = pref.getBoolean(key, false);
        }
        if (!ret) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(key, true);
            editor.commit();
        }
        return ret;
    }
}
