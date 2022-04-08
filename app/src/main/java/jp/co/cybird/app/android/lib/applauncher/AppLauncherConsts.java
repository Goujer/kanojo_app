package jp.co.cybird.app.android.lib.applauncher;

import java.io.File;

public class AppLauncherConsts {
    public static final String APPLAUNCHER_CACHE_DIRECTORY = "debfe33f954902cf907ea34bca3eb467";
    public static final String APPLAUNCHER_ICON_DIRECTORY = (APPLAUNCHER_CACHE_DIRECTORY + File.separator + "icons");
    public static final boolean DEBUGGABLE = false;
    public static final String DEFAULT_SCHEME_DATA_CATEGORY = "default";
    public static final String FORMAT_FILENAME_SCHEME_DATA = "scheme_%s.json";
    static String GA_LAUNCHER_CATEGORY = "ランチャー";
    static String GA_LAUNCHER_INITIATION_ACTION = "初期化";
    static String GA_LAUNCHER_LOAD_SCHEME_LABEL = "データロード";
    static String GA_LAUNCHER_SHOW_LAUNCHER_ACTION = "ランチャー起動";
    static String GA_LAUNCHER_SHOW_LAUNCHER_LABEL = "一覧表示";
    static String GA_LAUNCHER_TAP_INSTALLED_APP_ACTION = "アプリ起動";
    static String GA_LAUNCHER_TAP_NEW_APP_ACTION = "マーケット起動";
    public static final String REQUEST_PARAM_GENERAL = "q";
    public static final String REQUEST_PARAM_GENERAL_CATEGORY = "uuid";
    public static final String REQUEST_PARAM_GENERAL_PID = "pid";
    public static final String REQUEST_PARAM_GENERAL_REFFERER = "referrer";
    public static final String REQUEST_PARAM_GENERAL_TID = "tid";
    public static final String REQUEST_PARAM_GENERAL_TYPE = "type";
    public static final String REQUEST_PARAM_GENERAL_TYPE_BANNER = "banner";
    public static final String REQUEST_PARAM_GENERAL_TYPE_LIST = "list";
    public static final String REQUEST_PARAM_GENERAL_UUID = "uuid";
    public static final String REQUEST_PARAM_SCHEME_APIVERSION = "v";
    public static final String REQUEST_PARAM_TRACK_APIVERSION = "v";
    public static final String REQUEST_URL_SCHEME_DATA = "http://app.sf.cybird.ne.jp/scheme";
    public static final String REQUEST_URL_SCHEME_DATA_DEV = "http://210.149.187.57/scheme";
    public static final String REQUEST_URL_SCHEME_DATA_REL = "http://app.sf.cybird.ne.jp/scheme";
    public static final String REQUEST_URL_TRACK = "http://app.sf.cybird.ne.jp/track";
    public static final String REQUEST_URL_TRACK_DEV = "http://210.149.187.57/track";
    public static final String REQUEST_URL_TRACK_REL = "http://app.sf.cybird.ne.jp/track";
    public static final String TAG = "ALauncher";
    private static String USER_AGENT = null;

    public enum LAUNCHER_TYPE {
        list,
        banner
    }

    public static String getUserAgent() {
        return USER_AGENT;
    }

    public static void setUserAgent(String userAgent) {
        USER_AGENT = userAgent;
    }
}
