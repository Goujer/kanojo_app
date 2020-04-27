package jp.co.cybird.barcodekanojoForGAM.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import jp.co.cybird.barcodekanojoForGAM.core.util.PhoneInfo;

public class ApplicationSetting {
    private Keys keys = new Keys();
    private Context mContext;
    private SharedPreferences setting;

    public static final class Keys {
        public final String appRegistrationId = Preferences.PREFERENCE_APP_ID;
        public final String dataVersion = Preferences.PREFERENCE_VERSION;
        public final String deviceToken = Preferences.PREFERENCE_DEVICE_TOKEN;
        public final String facebookEmail = Preferences.PREFERENCE_FACEBOOK_EMAIL;
        public final String facebookExpired = "expires_in";
        public final String facebookFirstName = Preferences.PREFERENCE_FACEBOOK_LAST_NAME;
        public final String facebookID = Preferences.PREFERENCE_FACEBOOK_ID;
        public final String facebookKey = Preferences.PREFERENCE_FACEBOOK_KEY;
        public final String facebookLastName = Preferences.PREFERENCE_FACEBOOK_FIRST_NAME;
        public final String facebookLastUpdate = Preferences.PREFERENCE_FACEBOOK_LAST_UPDATE;
        public final String facebookToken = "access_token";
        public final String isSyncFaceBook = Preferences.PREFERENCE_IS_SYNC_FACEBOOK;
        public final String isSyncTwitter = Preferences.PREFERENCE_IS_SYNC_TWITTER;
        public final String twitterAccessToken = Preferences.PREFERENCE_TWITTER_ACCESS_TOKEN;
        public final String twitterAccessTokenSecret = Preferences.PREFERENCE_TWITTER_ACCESS_TOKEN_SECRET;
        public final String userAndroidId = Preferences.PREFERENCE_ANDROID_ID;
        public final String userGoogleAccount = Preferences.PREFERENCE_GOOGLE_ACCOUNT;
        public final String userICCID = Preferences.PREFERENCE_USER_ICCID;
        public final String userImei = Preferences.PREFERENCE_USER_IMEI;
    }

    public ApplicationSetting(Context context) {
        this.mContext = context;
        this.setting = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Keys getPreferenceKeys() {
        return this.keys;
    }

    public String getUserGoogleAccount() {
        return this.setting.getString(this.keys.userGoogleAccount, "");
    }

    public void commitUserGoogleAccount() {
        PhoneInfo p = new PhoneInfo(this.mContext);
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.userGoogleAccount, p.getGoogleAccount());
        editor.commit();
    }

    public void commitICCID(String iccid) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.userICCID, iccid);
        editor.commit();
    }

    public String getICCID() {
        return this.setting.getString(this.keys.userICCID, null);
    }

    public void commitUUID(String uuid) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.userAndroidId, uuid);
        editor.commit();
    }

    public String getUUID() {
        return this.setting.getString(this.keys.userAndroidId, null);
    }

    public void clearUUID() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.userAndroidId);
        editor.commit();
    }

    public void clearDataVersion() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.dataVersion);
        editor.commit();
    }

    public void removeUser() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.userICCID);
        editor.remove(this.keys.userAndroidId);
        editor.remove(this.keys.userImei);
        editor.commit();
    }

    public String getDataVersion() {
        return this.setting.getString(this.keys.dataVersion, (String) null);
    }

    public void commitFacebookExpired(Long faceBookExpired) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putLong(this.keys.facebookExpired, faceBookExpired.longValue());
        editor.commit();
    }

    public Long getFaceBookExpired() {
        return Long.valueOf(this.setting.getLong(this.keys.facebookExpired, 0));
    }

    public void clearFaceBookExpired() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookExpired);
        editor.commit();
    }

    public void commitFaceBookToken(String faceBookToken) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.facebookToken, faceBookToken);
        editor.commit();
    }

    public String getFaceBookToken() {
        return this.setting.getString(this.keys.facebookToken, null);
    }

    public void clearFaceBookToken() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookToken);
        editor.commit();
    }

    public void commitFacebookLastUpdate(Long faceBookLastUpdate) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putLong(this.keys.facebookLastUpdate, faceBookLastUpdate);
        editor.commit();
    }

    public Long getFaceBookLastUpdate() {
        return this.setting.getLong(this.keys.facebookLastUpdate, 0);
    }

    public void clearFaceBookLastUpdate() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookLastUpdate);
        editor.commit();
    }

    public void commitFacebookID(String faceBookID) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.facebookID, faceBookID);
        editor.commit();
    }

    public String getFaceBookID() {
        return this.setting.getString(this.keys.facebookID, "");
    }

    public void clearFaceBookID() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookID);
        editor.commit();
    }

    public void commitFaceBookEmail(String faceBookEmail) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.facebookEmail, faceBookEmail);
        editor.commit();
    }

    public String getFaceBookEmail() {
        return this.setting.getString(this.keys.facebookEmail, (String) null);
    }

    public void clearFaceBookEmail() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookEmail);
        editor.commit();
    }

    public void commitDeviceToken(String deviceToken) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.deviceToken, deviceToken);
        editor.commit();
    }

    public String getDeviceToken() {
        return this.setting.getString(this.keys.deviceToken, (String) null);
    }

    public void clearDeviceToken() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.deviceToken);
        editor.commit();
    }

    public void commitTwitterAccessToken(String token) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.twitterAccessToken, token);
        editor.commit();
    }

    public String getTwitterAccessToken() {
        return this.setting.getString(this.keys.twitterAccessToken, (String) null);
    }

    public void clearTwitterAccessToken() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.twitterAccessToken);
        editor.commit();
    }

    public void commitTwitterAccessTokenSecret(String tokenSecret) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.twitterAccessTokenSecret, tokenSecret);
        editor.commit();
    }

    public String getTwitterAccessTokenSecret() {
        return this.setting.getString(this.keys.twitterAccessTokenSecret, (String) null);
    }

    public void clearTwitterAccessTokenSecret() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.twitterAccessTokenSecret);
        editor.commit();
    }

    public void commitSyncFaceBook(boolean sync) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putBoolean(this.keys.isSyncFaceBook, sync);
        editor.commit();
    }

    public void commitSyncTwitter(boolean sync) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putBoolean(this.keys.isSyncTwitter, sync);
        editor.commit();
    }

    public boolean isSyncFaceBook() {
        return this.setting.getBoolean(this.keys.isSyncFaceBook, false);
    }

    public boolean isSyncTwitter() {
        return this.setting.getBoolean(this.keys.isSyncTwitter, false);
    }

    public void clearSync() {
        commitSyncFaceBook(false);
        commitSyncTwitter(false);
    }

    public void reset() {
        clearDataVersion();
        clearFaceBookEmail();
        clearFaceBookExpired();
        clearFaceBookID();
        clearFaceBookLastUpdate();
        clearFaceBookToken();
        clearTwitterAccessToken();
        clearTwitterAccessTokenSecret();
        clearSync();
    }
}
