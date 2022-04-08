package jp.co.cybird.app.android.lib.commons.net;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connMng = (ConnectivityManager) context.getSystemService("connectivity");
        if (connMng.getActiveNetworkInfo() != null) {
            return connMng.getActiveNetworkInfo().isConnected();
        }
        return false;
    }
}
