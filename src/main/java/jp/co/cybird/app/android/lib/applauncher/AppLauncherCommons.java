package jp.co.cybird.app.android.lib.applauncher;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import jp.co.cybird.app.android.lib.InstallReferrerReceiver;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.app.android.lib.commons.security.popgate.Codec;
import jp.co.cybird.app.android.lib.cybirdid.CybirdCommonUserId;

public class AppLauncherCommons {
    public static String getQueryString(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        if (sb.charAt(sb.length() - 1) == '&') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String getEncodedParamsString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        if (sb.charAt(sb.length() - 1) == '&') {
            sb.deleteCharAt(sb.length() - 1);
        }
        DLog.d(AppLauncherConsts.TAG, sb.toString());
        return Codec.encode(sb.toString());
    }

    public static String getGeneralParamsString(Context context, AppLauncherConsts.LAUNCHER_TYPE type, String category) {
        return getGeneralParamsString(context, type, category, (String) null);
    }

    public static String getGeneralParamsString(Context context, AppLauncherConsts.LAUNCHER_TYPE type, String category, String tid) {
        String pid = context.getPackageName();
        String launcherType = null;
        if (type == null) {
            launcherType = AppLauncherConsts.REQUEST_PARAM_GENERAL_TYPE_LIST;
        } else if (type.equals(AppLauncherConsts.LAUNCHER_TYPE.list)) {
            launcherType = AppLauncherConsts.REQUEST_PARAM_GENERAL_TYPE_LIST;
        } else if (type.equals(AppLauncherConsts.LAUNCHER_TYPE.banner)) {
            launcherType = AppLauncherConsts.REQUEST_PARAM_GENERAL_TYPE_BANNER;
        }
        String userId = CybirdCommonUserId.get(context);
        Map<String, String> params = new HashMap<>();
        params.put(AppLauncherConsts.REQUEST_PARAM_GENERAL_PID, pid);
        if (tid != null) {
            params.put(AppLauncherConsts.REQUEST_PARAM_GENERAL_TID, tid);
        }
        params.put("type", launcherType);
        params.put("uuid", userId);
        if (category != null && category.equals("")) {
            params.put("uuid", category);
        }
        String installReferrer = InstallReferrerReceiver.getAndClearCampaign(context);
        if (installReferrer != null) {
            params.put(AppLauncherConsts.REQUEST_PARAM_GENERAL_REFFERER, installReferrer);
        }
        return getEncodedParamsString(params);
    }
}
