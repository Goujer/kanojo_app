package jp.co.cybird.barcodekanojoForGAM.activity.base;

public interface BaseInterface {
	//Errors
    public static final int ERROR_INTERNET = 500;
    public static final int ERROR_OUT_OF_MEMORY = 504;

    //Extras
    public static final String EXTRA_ALERT = "extra_alert";
    public static final String EXTRA_BARCODE = "extra_barcode";
    public static final String EXTRA_KANOJO = "extra_kanojo";
    public static final String EXTRA_KANOJO_ITEM = "extra_kanojo_item";
    public static final String EXTRA_KANOJO_ITEM_MODE = "extra_kanojo_item_mode";
    public static final String EXTRA_LEVEL = "extra_level";
    public static final String EXTRA_LOVE_INCREMENT = "extra_love_increment";
    public static final String EXTRA_MESSAGES = "extra_messages";
    public static final String EXTRA_PRODUCT = "extra_product";
    public static final String EXTRA_REQUEST_CODE = "extra_request_code";
    public static final String EXTRA_SCANNED = "extra_scanned";
    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_WEBVIEW_URL = "extra_webview_url";

    //Requests
	public static final int REQUEST_SIGN_UP = 804;
	public static final int REQUEST_LOG_IN = 805;
	public static final int REQUEST_MODIFY_USER = 806;
	public static final int REQUEST_CHANGE_PASWORD = 807;
    public static final int REQUEST_SHOW_PRIVACY = 809;
	public static final int REQUEST_DASHBOARD = 900;
	public static final int REQUEST_KANOJOS = 901;
	public static final int REQUEST_KANOJO = 1000;
	public static final int REQUEST_KANOJO_INFO = 1001;
	public static final int REQUEST_KANOJO_EDIT = 1002;
	public static final int REQUEST_BUY_TICKET = 1106;
	public static final int REQUEST_KANOJO_TICKETS = 1009;
	public static final int REQUEST_SCAN = 1010;
	public static final int REQUEST_SCAN_NEW = 1011;
	public static final int REQUEST_SCAN_ALREADY = 1012;
	public static final int REQUEST_SCAN_OTHERS = 1013;
	public static final int REQUEST_SCAN_OTHERS_EDIT = 1014;
	public static final int REQUEST_SCAN_ACTIVITY = 1019;
	public static final int REQUEST_SCAN_KANOJO_GENERATE = 1020;
	public static final int REQUEST_OPTION_ACTIVITY = 1050;
    public static final int REQUEST_USER_MODIFY_ACTIVITY = 1051;
	public static final int REQUEST_GALLERY = 1100;
	public static final int REQUEST_CAMERA = 1101;
	public static final int REQUEST_SOCIAL_CONFIG_FIRST = 1102;
	public static final int REQUEST_SOCIAL_CONFIG_SETTING = 1103;
	public static final int REQUEST_SOCIAL_SUKIYA_SETTING = 1104;
	public static final int REQUEST_SYNC_SETTING = 1105;
	public static final int REQUEST_KANOJO_ITEMS = 2114;

    //Results
	public static final int RESULT_SAVE_PRODUCT_INFO = 101;
	public static final int RESULT_GENERATE_KANOJO = 102;
	public static final int RESULT_ADD_FRIEND = 103;
	public static final int RESULT_SIGN_UP = 105;
    public static final int RESULT_LOG_IN = 106;
    public static final int RESULT_LOG_OUT = 107;
    public static final int RESULT_MODIFIED = 108;
	public static final int RESULT_CHANGED = 109;
	public static final int RESULT_PRIVACY_OK = 110;
	public static final int RESULT_DELETE_ACCOUNT = 111;
	public static final int RESULT_KANOJO_ROOM_EXIT = 201;
	public static final int RESULT_KANOJO_ITEM_USED = 204;
	public static final int RESULT_KANOJO_ITEM_PAYMENT_DONE = 207;
	public static final int RESULT_KANOJO_GOOD_BYE = 209;
    public static final int RESULT_MODIFIED_COMMON = 210;
    public static final int RESULT_MODIFIED_DEVICE = 211;
	public static final int RESULT_MODIFIED_ALL = 212;
	public static final int RESULT_KANOJO_MESSAGE_DIALOG = 213;
	public static final int RESULT_BUY_TICKET_SUCCESS = 214;
	public static final int RESULT_BUY_TICKET_FAIL = 215;

    public static final String TAG = "BarcodeKanojo";
}
