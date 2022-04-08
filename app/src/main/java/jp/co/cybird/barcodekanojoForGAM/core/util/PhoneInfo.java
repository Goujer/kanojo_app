package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;

public class PhoneInfo {
    private final String TAG = PhoneInfo.class.getSimpleName();
    String androidid;
    private Context context;
    String googleAccount;
    String iccid;
    String imei;
    String imsi;
    String model;
    private TelephonyManager telephonyManager;

    public PhoneInfo(Context context2) {
        this.context = context2;
        this.telephonyManager = (TelephonyManager) this.context.getSystemService("phone");
    }

    public String getIMEI() {
        String encodeimei = "";
        this.imei = this.telephonyManager.getDeviceId();
        if (this.imei == null || this.imei == "") {
            return "";
        }
        try {
            encodeimei = URLEncoder.encode(this.imei, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(this.TAG, e.getMessage());
        }
        return encodeimei;
    }

    public String getICCID() {
        this.iccid = this.telephonyManager.getSimSerialNumber();
        if (this.iccid == null || this.iccid == "") {
            return "";
        }
        try {
            return URLEncoder.encode(this.iccid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(this.TAG, e.getMessage());
            return "";
        }
    }

    public String getAndroidID() {
        this.androidid = Settings.Secure.getString(this.context.getContentResolver(), Preferences.PREFERENCE_ANDROID_ID);
        if (this.androidid == null) {
            return "";
        }
        try {
            return URLEncoder.encode(this.androidid, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(this.TAG, e.getMessage());
            return "";
        }
    }

    public String getModel() {
        this.model = Build.MODEL;
        try {
            return URLEncoder.encode(this.model, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(this.TAG, e.getMessage());
            return "";
        }
    }

    public String getIMSI() {
        String encodeModel = "";
        this.imsi = this.telephonyManager.getSubscriberId();
        if (this.imsi == null) {
            return "";
        }
        try {
            encodeModel = URLEncoder.encode(this.imsi, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(this.TAG, e.getMessage());
        }
        return encodeModel;
    }

    public String getGoogleAccount() {
        String encodeGoogleAccount = "";
        Account[] accounts = AccountManager.get(this.context).getAccountsByType("com.google");
        if (accounts != null && accounts.length > 0) {
            this.googleAccount = accounts[0].name;
        }
        if (this.googleAccount == null) {
            return "";
        }
        try {
            encodeGoogleAccount = URLEncoder.encode(this.googleAccount, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(this.TAG, e.getMessage());
        }
        return encodeGoogleAccount;
    }
}
