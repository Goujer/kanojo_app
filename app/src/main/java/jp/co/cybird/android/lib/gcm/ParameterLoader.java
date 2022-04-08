package jp.co.cybird.android.lib.gcm;

import android.content.Context;

public class ParameterLoader {
    public static int getResourceIdForType(String key, String type, Context ctx) {
        if (ctx == null) {
            return 0;
        }
        return ctx.getResources().getIdentifier(key, type, ctx.getPackageName());
    }

    public static String getString(String key, Context ctx) {
        int id = getResourceIdForType(key, "string", ctx);
        if (id == 0) {
            return "";
        }
        return ctx.getString(id);
    }

    public static boolean getBool(String key, Context ctx) {
        int id = getResourceIdForType(key, "bool", ctx);
        if (id == 0) {
            return false;
        }
        return ctx.getResources().getBoolean(id);
    }
}
