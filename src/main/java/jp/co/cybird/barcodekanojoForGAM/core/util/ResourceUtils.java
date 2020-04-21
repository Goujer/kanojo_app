package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public final class ResourceUtils {
    private static final String DEV_RES_SUFFIX = "_dev";
    private static final String TAG = ResourceUtils.class.getSimpleName();
    private static boolean isDebuggable;
    private static String packageName;
    private static Resources res;

    private ResourceUtils() {
    }

    public static void configure(Context context) {
        packageName = context.getPackageName();
        res = context.getResources();
    }

    public static boolean isTrue(int resId) {
        return Boolean.parseBoolean(getString(resId));
    }

    public static Drawable getDrawable(int resId) {
        if (isDebuggable) {
            return getDrawable(res.getResourceName(resId));
        }
        return res.getDrawable(resId);
    }

    public static String getString(int resId) {
        if (isDebuggable) {
            return getDevString(res.getResourceName(resId), new Object[0]);
        }
        return res.getString(resId);
    }

    public static String getString(int resId, Object... args) {
        if (isDebuggable) {
            return getDevString(res.getResourceName(resId), args);
        }
        return res.getString(resId, args);
    }

    public static String getQuantityString(int resId, int quantity) {
        if (isDebuggable) {
            return getDevQuantityString(res.getResourceName(resId), quantity, new Object[0]);
        }
        return res.getQuantityString(resId, quantity);
    }

    public static String getQuantityString(int resId, int quantity, Object... args) {
        if (isDebuggable) {
            return getDevQuantityString(res.getResourceName(resId), quantity, args);
        }
        return res.getQuantityString(resId, quantity, args);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = res.getIdentifier(r4.concat(DEV_RES_SUFFIX), r5, packageName);
     */
    private static int getDevResourceId(String name, String defType) {
        int resId = res.getIdentifier(name.concat(DEV_RES_SUFFIX), defType, packageName);
        return (!isDebuggable || resId == 0) ? res.getIdentifier(name, defType, packageName) : resId;
    }

    private static String getDevString(String name, Object... args) {
        return res.getString(getDevResourceId(name, "string"), args);
    }

    private static String getDevQuantityString(String name, int quantity, Object... args) {
        return res.getQuantityString(getDevResourceId(name, "plurals"), quantity, args);
    }

    private static Drawable getDrawable(String name) {
        return res.getDrawable(getDevResourceId(name, "drawable"));
    }
}
