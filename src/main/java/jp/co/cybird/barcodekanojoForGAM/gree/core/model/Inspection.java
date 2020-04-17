package jp.co.cybird.barcodekanojoForGAM.gree.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class Inspection implements BarcodeKanojoModel, Parcelable {
    public static final String APP_ID = "appId";
    public static final String AUTHOR_ID = "authorId";
    public static final String CTIME = "ctime";
    public static final String DATA = "data";
    public static final boolean DEBUG = false;
    public static final String[] INSPECTION_EDIT_ACCOUNT_STRING = {GreeDefs.USER_NAME};
    public static final String[] INSPECTION_PRODUCT_EDIT_STRINGS = {GreeDefs.COMPANY_NAME, GreeDefs.PRODUCT_NAME, GreeDefs.PRODUCT_COMMENT};
    public static final String[] INSPECTION_SCAN_AND_GENERATE_STRINGS = {GreeDefs.KANOJO_NAME, GreeDefs.COMPANY_NAME, GreeDefs.PRODUCT_NAME, GreeDefs.PRODUCT_COMMENT};
    public static final String[] INSPECTION_SCAN_OTHERS_STRINGS = {GreeDefs.COMPANY_NAME, GreeDefs.PRODUCT_NAME, GreeDefs.PRODUCT_COMMENT};
    public static final String[] INSPECTION_SIGN_UP_STRINGS = {GreeDefs.USER_NAME};
    public static final String MTIME = "mtime";
    public static final String OWNER_ID = "ownerId";
    public static final String STATUS = "status";
    public static final String TAG = "Inspection";
    public static final String TEXT_ID = "textId";
    private String appId;
    private String authorId;
    private String ctime;
    private String data;
    private String mtime;
    private String ownerId;
    private int responseCode;
    private int status;
    private String textId;

    public static final String[] getKeys(int action) {
        switch (action) {
            case 0:
                return INSPECTION_SIGN_UP_STRINGS;
            case 1:
                return INSPECTION_EDIT_ACCOUNT_STRING;
            case 2:
                return INSPECTION_SCAN_AND_GENERATE_STRINGS;
            case 3:
                return INSPECTION_PRODUCT_EDIT_STRINGS;
            case 4:
                return INSPECTION_PRODUCT_EDIT_STRINGS;
            default:
                return null;
        }
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode2) {
        this.responseCode = responseCode2;
    }

    public String getTextId() {
        return this.textId;
    }

    public void setTextId(String textId2) {
        this.textId = textId2;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId2) {
        this.appId = appId2;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(String authorId2) {
        this.authorId = authorId2;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId2) {
        this.ownerId = ownerId2;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data2) {
        this.data = data2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public String getCtime() {
        return this.ctime;
    }

    public void setCtime(String ctime2) {
        this.ctime = ctime2;
    }

    public String getMtime() {
        return this.mtime;
    }

    public void setMtime(String mtime2) {
        this.mtime = mtime2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }
}
