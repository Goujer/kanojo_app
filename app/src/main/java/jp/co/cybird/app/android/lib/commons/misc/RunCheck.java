package jp.co.cybird.app.android.lib.commons.misc;

import android.content.Context;
import android.content.SharedPreferences;

public class RunCheck {
    private static String PREF_KEY_FIRSTRUN = "firstrun";

    public static boolean isFirstRun(Context context) {
        SharedPreferences mPref = context.getSharedPreferences("lib_commons_runcheck_" + context.getPackageName(), 0);
        if (!mPref.getBoolean(PREF_KEY_FIRSTRUN, true)) {
            return false;
        }
        SharedPreferences.Editor e = mPref.edit();
        e.putBoolean(PREF_KEY_FIRSTRUN, false);
        e.commit();
        return true;
    }
}
