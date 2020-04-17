package jp.co.cybird.android.lib.gcm;

import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class Const {
    static String GA_NOTIFICATION_ACTION_DISPLAY = "通知を表示";
    static String GA_NOTIFICATION_ACTION_TAP = "通知をタッチ";
    static String GA_NOTIFICATION_APP_LABEL = "アプリ起動通知";
    static String GA_NOTIFICATION_CATEGORY = "通知";
    static String GA_NOTIFICATION_DISPLAY_LABEL = "通知を用意";
    static String GA_NOTIFICATION_MARKET_LABEL = "Google Play通知";
    static String GA_NOTIFICATION_NONE_LABEL = "ヌール通知";
    static String GA_NOTIFICATION_URL_LABEL = "ブラウザ通知";
    static final String GCM_BUNDLE_NAME = "push";
    static String IS_TESTING = GreeDefs.BARCODE;
    static final String PARAM_ACTION = "act";
    static final String PARAM_ACTION_APP = "app";
    static final String PARAM_ACTION_BROWSER = "browser";
    static final String PARAM_ACTION_MARKET = "market";
    static final String PARAM_ACTION_NONE = "none";
    static final String PARAM_MESSAGE = "msg";
    static final String PARAM_NID = "nid";
    static final String PARAM_NUMBER = "num";
    static final String PARAM_SND = "snd";
    static final String PARAM_URL = "url";
    public static final String PREF_FILE_NAME = "lib_gcm";
    public static final String PREF_KEY_DOES_INCLUDE_SANDBOX = "lib_gcm_does_include_sandbox";
    static final String PREF_KEY_IS_NORMAL = "lib_gcm_is_normal";
    public static final String PREF_KEY_IS_TESTING = "lib_gcm_is_testing";
    static final String PREF_KEY_REGISTRATION_ID = "lib_gcm_registration_ID";
    static final String PREF_KEY_WILLPLAYSOUND = "lib_gcm_willPlaySound";
    static final String PREF_KEY_WILLSENDNOTIFICATION = "lib_gcm_willSendNotification";
    static final String PREF_KEY_WILLVIBRATE = "lib_gcm_willVibrate";
    static String REGISTER_URL = "https://push.cybird.ne.jp/register";
    static final String TAG = "JP.CO.CYBIRD.ANDROID.GCM";
    static final String TRACK_CATEGORY_RECEIVE = "receive";
    static final String TRACK_CATEGORY_TAP = "tap";
    static String TRACK_URL = "https://push.cybird.ne.jp/track";
    static String UNREGISTER_URL = "https://push.cybird.ne.jp/unregister";

    public static void switchIsTestingFLAG(boolean isTesting) {
        if (isTesting) {
            IS_TESTING = GreeDefs.KANOJO_NAME;
        } else {
            IS_TESTING = GreeDefs.BARCODE;
        }
    }

    public static void switchServerURL(boolean doesIncludeSandbox) {
        if (doesIncludeSandbox) {
            UNREGISTER_URL = "https://sandbox.push.cybird.ne.jp/unregister";
            REGISTER_URL = "https://sandbox.push.cybird.ne.jp/register";
            TRACK_URL = "https://sandbox.push.cybird.ne.jp/track";
            return;
        }
        UNREGISTER_URL = "https://push.cybird.ne.jp/unregister";
        REGISTER_URL = "https://push.cybird.ne.jp/register";
        TRACK_URL = "https://push.cybird.ne.jp/track";
    }
}
