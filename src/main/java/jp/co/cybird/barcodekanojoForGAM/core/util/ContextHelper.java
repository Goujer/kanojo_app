package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;

public final class ContextHelper {
    private static TelephonyContext telephonyContext;

    public static class TelephonyContext {
        private Context context;
        private TelephonyManager telephonyManager;

        public TelephonyContext(Context context2) {
            this.telephonyManager = (TelephonyManager) context2.getSystemService(Context.TELEPHONY_SERVICE);
            this.context = context2;
        }

        public Account getGoogleAccount() throws Exception {
            Account[] accounts = AccountManager.get(this.context).getAccountsByType("com.google");
            if (accounts != null && accounts.length != 0) {
                return accounts[0];
            }
            throw new Exception("google account not found.");
        }

        public String getGoogleAuthToken(String service) throws Exception {
            AccountManager am = AccountManager.get(this.context);
            Account[] accounts = am.getAccountsByType("com.google");
            if (accounts == null || accounts.length == 0) {
                throw new Exception("google account not found.");
            }
            try {
                return am.getAuthToken(accounts[0], service, false, null, null).getResult().getString("authtoken");
            } catch (Exception e) {
                throw new Exception("get auth token failed.", e);
            }
        }

        public String getAndroidId() {
            return Settings.Secure.getString(this.context.getContentResolver(), Preferences.PREFERENCE_ANDROID_ID);
        }

        public String getICCID() {
            return "8981100000301831371";
        }

        public String getIMEI() {
            return this.telephonyManager.getDeviceId();
        }

        public String getIMSI() {
            return this.telephonyManager.getSubscriberId();
        }

        public String getModelName() {
            return "P-05D";
        }
    }

    private ContextHelper() {
    }

    public static void configure(Context context) {
        telephonyContext = new TelephonyContext(context);
    }

    public static void configure(TelephonyContext telephonyContext2) {
        telephonyContext = telephonyContext2;
    }

    public static String getAndroidId() {
        return telephonyContext.getAndroidId();
    }

    public static String getICCID() {
        return telephonyContext.getICCID();
    }

    public static String getEncryptICCID() {
        if (telephonyContext.getICCID() != null) {
            return telephonyContext.getICCID();
        }
        return null;
    }

    public static String getModelName() {
        return telephonyContext.getModelName();
    }

    public static String getIMEI() {
        return telephonyContext.getIMEI();
    }

    public static String getEncryptIMEI() {
        if (telephonyContext.getIMEI() != null) {
            return telephonyContext.getIMEI();
        }
        return null;
    }

    public static String getIMSI() {
        return telephonyContext.getIMSI();
    }

    public static Account getGoogleAccount() throws Exception {
        Account account = telephonyContext.getGoogleAccount();
        if (account != null) {
            return account;
        }
        throw new Exception("google account not found.");
    }

    public static String getGoogleAuthToken(String service) throws Exception {
        return telephonyContext.getGoogleAuthToken(service);
    }
}
