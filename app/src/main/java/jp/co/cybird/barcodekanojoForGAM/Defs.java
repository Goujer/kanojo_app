package jp.co.cybird.barcodekanojoForGAM;

import java.util.Locale;

public class Defs {
	public static final boolean DEBUG = true; //Global DEBUG variable, TODO put this in gradle or something.
    public static final String DEBUG_BIRTH = "0101";
    public static final String DEBUG_EMAIL = "mizuba@netspeed.jp";
    public static final boolean DEBUG_ENCRYPTED = false;
    public static final boolean DEBUG_IPHONE = false;
    public static final boolean DEBUG_LOCATION = false;
    public static final boolean DEBUG_LOGIN = false;
    public static final String DEBUG_NAME = "MIZUBA";
    public static final boolean DEBUG_PARSER = false;
    public static final String DEBUG_PASSWORD = "123456789";
    public static final boolean DEBUG_PURCHASE = false;
    public static final boolean DEBUG_RESOURCE = false;
    public static final boolean DEBUG_RESPONSE = false;
    private static final boolean DEBUG_SERVER = false;
    public static final String DEBUG_SEX = "male";
    public static final String MAIL_BUSINESS = "tieup@barcodekanojo.com";
    public static final String MAIL_SUPPORT = "help@barcodekanojo.com";
    private static final String TAG = "Defs";
    public static final String URL_ABOUT_BARCODEKANOJO = "/resource/about_barcodekanojo.html";
    public static final String URL_ABOUT_RULES = "/resource/about_rules.html";
    public static final String URL_ABOUT_TEAM = "/resource/about_team.html";
    private static final String URL_BASE_DEBUG = "http://api.test.barcodekanojo.com/2";
    public static final String URL_BASE_LIVE2D_EXTPARTS = "http://storage.barcodekanojo.com/avatar";
    public static final String URL_GENERAL_EN = "https://my.cybird.ne.jp/sp-inq/BCK006";
    public static final String URL_GENERAL_JA = "https://my.cybird.ne.jp/sp-inq/BCK004";
    public static final String URL_GENERAL_ZH = "https://my.cybird.ne.jp/sp-inq/BCK005";
    public static final String URL_KDDI_SERVICE = "https://connect.auone.jp/net/id/kessai_service.html";
    public static final String URL_LEGAL_PRIVACY = "/resource/legal_privacy.html";
    public static final String URL_LEGAL_TERMS = "/resource/legal_terms.html";
    public static final String URL_PAYMENT_DEBUG = "https://api.test.barcodekanojo.com/au/transaction";
    public static final String URL_PAYMENT_RELEASE = "https://api.barcodekanojo.com/au/transaction";
    public static final String URL_SUPPORT = "http://www.barcodekanojo.com/contact";
    private static final String USER_AGENT_DEBUG = "BarcodeKanojo/2.4.2 CFNetwork/485.12.7 Darwin/10.4.0";
    private static final String USER_AGENT_PREFIX = "BarcodeKanojo/2.4.2 ";

    public static String URL_PAYMENT() {
        return URL_PAYMENT_RELEASE;
    }

    public static String USER_AGENT() {
        return USER_AGENT_PREFIX + System.getProperties().getProperty("http.agent");
    }

    public static String USER_LANGUAGE() {
        return Locale.getDefault().toString().substring(0, 2);
    }
}
