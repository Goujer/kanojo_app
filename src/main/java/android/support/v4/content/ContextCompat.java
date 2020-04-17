package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class ContextCompat {
    public static boolean startActivities(Context context, Intent[] intents) {
        return startActivities(context, intents, (Bundle) null);
    }

    public static boolean startActivities(Context context, Intent[] intents, Bundle options) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 16) {
            ContextCompatJellybean.startActivities(context, intents, options);
            return true;
        } else if (version < 11) {
            return false;
        } else {
            ContextCompatHoneycomb.startActivities(context, intents);
            return true;
        }
    }
}
