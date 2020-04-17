package jp.co.cybird.app.android.lib.applauncher;

import android.content.Context;

public class ParamLoader {
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
}
