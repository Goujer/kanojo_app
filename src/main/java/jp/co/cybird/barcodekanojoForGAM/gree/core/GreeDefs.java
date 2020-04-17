package jp.co.cybird.barcodekanojoForGAM.gree.core;

public class GreeDefs {
    public static final String BARCODE = "0";
    public static final String COMPANY_NAME = "3";
    public static final String COMPANY_NAME_TEXTID = "4";
    public static final String DELETE_SUCCESS = "202";
    public static final int EDIT_ACCOUNT = 1;
    public static final int EDIT_KANOJO = 4;
    public static final String ENTRY = "entry";
    public static final String GEOPOINT = "10";
    public static final String GET_SUCCESS = "200";
    public static final String INSPECTION_REQUEST = "/inspection/@app";
    public static final String KANOJO_NAME = "1";
    public static final String KANOJO_NAME_TEXTID = "2";
    public static final String PARAM_NAME_DATA = "data";
    public static final String POST = "post";
    public static final String POST_AND_PUT_SUCCESS = "201";
    public static final String PRODUCT_COMMENT = "8";
    public static final String PRODUCT_COMMENT_TEXTID = "9";
    public static final String PRODUCT_CUTEGORY_ID = "7";
    public static final String PRODUCT_NAME = "5";
    public static final String PRODUCT_NAME_TEXTID = "6";
    public static final int SCAN_AND_GENERATE = 2;
    public static final int SCAN_OTHERS = 3;
    public static final int SIGN_UP = 0;
    public static final String USER_NAME = "11";
    public static final String USER_NAME_TEXTID = "12";

    public static String getTextIdParamCode(String key) {
        if (COMPANY_NAME.equals(key)) {
            return COMPANY_NAME_TEXTID;
        }
        if (PRODUCT_NAME.equals(key)) {
            return PRODUCT_NAME_TEXTID;
        }
        if (PRODUCT_COMMENT.equals(key)) {
            return PRODUCT_COMMENT_TEXTID;
        }
        if (USER_NAME.equals(key)) {
            return USER_NAME_TEXTID;
        }
        if (KANOJO_NAME.equals(key)) {
            return KANOJO_NAME_TEXTID;
        }
        return null;
    }
}
