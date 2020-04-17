package jp.co.cybird.android.lib.gcm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.webkit.WebView;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import java.util.regex.Pattern;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import jp.co.cybird.app.android.lib.commons.http.RequestParams;
import jp.co.cybird.app.android.lib.commons.http.ThreadHttpClient;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.app.android.lib.commons.security.popgate.Codec;
import jp.co.cybird.app.android.lib.cybirdid.CybirdCommonUserId;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class GCMUtilities {
    public static final String CYLIB_GCM_PARAM_DATA = "cylib_gcm_data";
    public static final String CYLIB_GCM_PARAM_ISPUSH = "cylib_gcm_ispush";
    private static boolean allowCustomizationEveryLaunch = false;
    private static SharedPreferences mPrefs;
    private static Runnable mRegistrationRunnable;
    private static Runnable mUnregistrationRunnable;
    private static String mUserAgent;

    public static void setRegistrationRunnable(Runnable r) {
        mRegistrationRunnable = r;
    }

    public static void setUnRegistrationRunnable(Runnable r) {
        mUnregistrationRunnable = r;
    }

    public static void launchPerfActivity(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        String possibleEmail = "";
        for (Account account : AccountManager.get(context).getAccounts()) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                DLog.d("JP.CO.CYBIRD.ANDROID.GCM", possibleEmail);
                context.startActivity(new Intent(context, PrefsActivity.class));
            }
        }
        if ("".equals(possibleEmail)) {
            Toast.makeText(context, ParameterLoader.getString("LIB_GCM_GOOGLE_ACCOUNT_MISSING_INFO", context), 1).show();
        }
    }

    public static String getSendID(Context context) {
        String sender_id = ParameterLoader.getString("LIB_GCM_SENDERID", context);
        if (sender_id.equals("")) {
            DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "Failed to load string.LIB_GCM_SENDERID.");
        }
        DLog.d("JP.CO.CYBIRD.ANDROID.GCM", "sender_id is " + sender_id);
        return sender_id;
    }

    public static void registerGCM(Context context) {
        String sender_id = getSendID(context);
        if (sender_id.equals("")) {
            DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "SEND_ID not exist.");
            return;
        }
        GCMRegistrar.register(context, sender_id);
    }

    public static void unregisterGCM(Context context) {
        GCMRegistrar.unregister(context);
    }

    public static String parseParametersString(Intent intent) {
        int j;
        String parametersString = "";
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Boolean isFromPush = Boolean.valueOf(extras.getBoolean("push", false));
            DLog.e("JP.CO.CYBIRD.ANDROID.GCM", String.valueOf(isFromPush));
            if (isFromPush.booleanValue()) {
                DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "Get parameters from bundle");
                Intent pushIntent = intent;
                if (!(pushIntent == null || pushIntent.getData() == null)) {
                    try {
                        String uriWithParametersString = pushIntent.getData().toString();
                        if (uriWithParametersString == null) {
                            uriWithParametersString = "";
                        }
                        if (uriWithParametersString.startsWith("app://") && (j = uriWithParametersString.indexOf("/", 6)) > 5) {
                            parametersString = uriWithParametersString.substring(j + 1);
                            DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "Parameters  is " + parametersString);
                        }
                        DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "Parameters String is " + uriWithParametersString);
                    } catch (IndexOutOfBoundsException e) {
                        DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "ERROR.");
                    }
                }
            }
        }
        return parametersString;
    }

    public static void runGCM(Context c) {
        mPrefs = c.getSharedPreferences(Const.PREF_FILE_NAME, 0);
        if (mPrefs.getBoolean("lib_gcm_willSendNotification", true)) {
            try {
                setUserAgent(new WebView(c).getSettings().getUserAgentString());
                initGCM(c);
            } catch (UnsupportedOperationException e) {
            }
        }
    }

    public static int isPush(Activity activity) {
        Context context = activity.getApplicationContext();
        return context.getSharedPreferences(context.getPackageName(), 0).getInt(CYLIB_GCM_PARAM_ISPUSH, 0);
    }

    public static String parseParametersString(Activity activity) {
        SharedPreferences packagePrefs = activity.getSharedPreferences(activity.getPackageName(), 0);
        String returnString = packagePrefs.getString(CYLIB_GCM_PARAM_DATA, "");
        SharedPreferences.Editor editor = packagePrefs.edit();
        editor.remove(CYLIB_GCM_PARAM_ISPUSH);
        editor.remove(CYLIB_GCM_PARAM_DATA);
        editor.commit();
        return returnString;
    }

    public static void sendRegistrationInfo(Context context, boolean doesAllowCustmization) {
        String registrationId = getRegistrationID(context);
        if (!"".equals(registrationId)) {
            registrationId = Codec.decode(registrationId);
        }
        DLog.d("JP.CO.CYBIRD.ANDROID.GCM", "sendRegistrationInfo() registrationId: " + registrationId);
        if (!"".equals(registrationId)) {
            Boolean willSendMessage = true;
            String UUID = CybirdCommonUserId.get(context);
            if (UUID == null || "".equals(UUID)) {
                willSendMessage = false;
                DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "CAN NOT GET UUID WHEN REGISTER!");
            }
            String packageName = context.getApplicationContext().getPackageName();
            if (packageName == null || "".equals(packageName)) {
                willSendMessage = false;
                DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "CAN NOT GET PACKAGE NAME WHEN REGISTER!");
            }
            String test = Const.IS_TESTING;
            if (willSendMessage.booleanValue()) {
                RequestParams params = new RequestParams();
                String query = "uuid=" + UUID + "&device_id=" + registrationId + "&product_id=" + packageName + "&user_agent=" + getUserAgent(context) + "&test=" + test;
                DLog.v("JP.CO.CYBIRD.ANDROID.GCM", "q=" + query);
                params.put("v", GreeDefs.KANOJO_NAME);
                params.put(AppLauncherConsts.REQUEST_PARAM_GENERAL, Codec.encode(query));
                ThreadHttpClient threadClient = new ThreadHttpClient();
                threadClient.setUserAgent(getUserAgent(context));
                threadClient.post(Const.REGISTER_URL, params);
            }
            if (mRegistrationRunnable != null && doesAllowCustmization) {
                new Thread(mRegistrationRunnable).start();
            }
        }
    }

    public static void sendUnregistrationInfo(Context context) {
        String registrationId = getRegistrationID(context);
        if (!"".equals(registrationId)) {
            registrationId = Codec.decode(registrationId);
        }
        if (!"".equals(registrationId)) {
            String UUID = CybirdCommonUserId.get(context);
            if (UUID == null || "".equals(UUID)) {
                DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "CAN NOT GET UUID WHEN UNREGISTER!");
            }
            String packageName = context.getApplicationContext().getPackageName();
            if (packageName == null || "".equals(packageName)) {
                DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "CAN NOT GET PACKAGE NAME WHEN UNREGISTER!");
            }
            String test = Const.IS_TESTING;
            RequestParams params = new RequestParams();
            String query = "uuid=" + UUID + "&device_id=" + registrationId + "&product_id=" + packageName + "&test=" + test;
            DLog.v("JP.CO.CYBIRD.ANDROID.GCM", "q=" + query);
            params.put("v", GreeDefs.KANOJO_NAME);
            params.put(AppLauncherConsts.REQUEST_PARAM_GENERAL, Codec.encode(query));
            ThreadHttpClient threadClient = new ThreadHttpClient();
            threadClient.setUserAgent(getUserAgent(context));
            threadClient.post(Const.UNREGISTER_URL, params);
            if (mUnregistrationRunnable != null) {
                new Thread(mUnregistrationRunnable).start();
            }
        }
    }

    public static String getRegistrationID(Context c) {
        return c.getSharedPreferences(Const.PREF_FILE_NAME, 0).getString("lib_gcm_registration_ID", "");
    }

    public static String getUserAgent(Context context) {
        return mUserAgent == null ? "GCM lib" : mUserAgent;
    }

    public static void setUserAgent(String userAgent) {
        mUserAgent = userAgent;
    }

    public static boolean isUnity(Context context) {
        return ParameterLoader.getBool("LIB_GCM_IS_UNITY", context);
    }

    public static void setWillSendNotification(boolean willSendNotification) {
        if (mPrefs != null) {
            mPrefs.edit().putBoolean("lib_gcm_willSendNotification", willSendNotification).commit();
        }
    }

    public static boolean willSendNotification() {
        if (mPrefs == null) {
            return false;
        }
        return mPrefs.getBoolean("lib_gcm_willSendNotification", true);
    }

    public static void setWillPlaySound(boolean willPlaySound) {
        if (mPrefs != null) {
            mPrefs.edit().putBoolean("lib_gcm_willPlaySound", willPlaySound).commit();
        }
    }

    public static boolean willPlaySound() {
        if (mPrefs == null) {
            return false;
        }
        return mPrefs.getBoolean("lib_gcm_willPlaySound", false);
    }

    public static void setWillVibrate(boolean willVibrate) {
        if (mPrefs != null) {
            mPrefs.edit().putBoolean("lib_gcm_willVibrate", willVibrate).commit();
        }
    }

    public static boolean willVibrate() {
        if (mPrefs == null) {
            return false;
        }
        return mPrefs.getBoolean("lib_gcm_willVibrate", false);
    }

    public static void allowCustomizationEveryLaunch() {
        allowCustomizationEveryLaunch = true;
    }

    public static String getRegistrationId(Context c) {
        return GCMRegistrar.getRegistrationId(c);
    }

    private static void initGCM(Context context) {
        try {
            Bundle b = ((Activity) context).getIntent().getExtras();
            if (b != null && Boolean.valueOf(b.getBoolean("push", false)).booleanValue()) {
                return;
            }
        } catch (Exception e) {
        }
        GCMRegistrar.checkDevice(context);
        GCMRegistrar.checkManifest(context);
        String regId = GCMRegistrar.getRegistrationId(context);
        if (regId.equals("")) {
            String sender_id = getSendID(context);
            if (sender_id.equals("")) {
                DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "SEND_ID not exist.");
                return;
            }
            GCMRegistrar.register(context, sender_id);
            return;
        }
        DLog.e("JP.CO.CYBIRD.ANDROID.GCM", "RegisterID is " + regId);
        SharedPreferences prefs = context.getSharedPreferences(Const.PREF_FILE_NAME, 0);
        String savedRegistrationId = prefs.getString("lib_gcm_registration_ID", "");
        if (!"".equals(savedRegistrationId)) {
            savedRegistrationId = Codec.decode(savedRegistrationId);
        }
        if (!savedRegistrationId.equals(regId)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("lib_gcm_registration_ID", Codec.encode(regId));
            editor.commit();
        }
        sendRegistrationInfo(context, allowCustomizationEveryLaunch);
    }
}
