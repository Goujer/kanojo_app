package jp.co.cybird.barcodekanojoForGAM.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import jp.co.cybird.barcodekanojoForGAM.core.util.PhoneInfo;

public class ApplicationSetting {
    private Keys keys = new Keys();
    private Context mContext;
    private SharedPreferences setting;

    public static final class Keys {
        final String appRegistrationId = Preferences.PREFERENCE_APP_ID;
        final String dataVersion = Preferences.PREFERENCE_VERSION;
        final String deviceToken = Preferences.PREFERENCE_DEVICE_TOKEN;
        final String facebookEmail = Preferences.PREFERENCE_FACEBOOK_EMAIL;
        final String facebookExpired = "expires_in";
        final String facebookFirstName = Preferences.PREFERENCE_FACEBOOK_LAST_NAME;
        final String facebookID = Preferences.PREFERENCE_FACEBOOK_ID;
        final String facebookKey = Preferences.PREFERENCE_FACEBOOK_KEY;
        final String facebookLastName = Preferences.PREFERENCE_FACEBOOK_FIRST_NAME;
        final String facebookLastUpdate = Preferences.PREFERENCE_FACEBOOK_LAST_UPDATE;
        final String facebookToken = "access_token";
        final String isSyncFaceBook = Preferences.PREFERENCE_IS_SYNC_FACEBOOK;
        final String isSyncTwitter = Preferences.PREFERENCE_IS_SYNC_TWITTER;
        final String twitterAccessToken = Preferences.PREFERENCE_TWITTER_ACCESS_TOKEN;
        final String twitterAccessTokenSecret = Preferences.PREFERENCE_TWITTER_ACCESS_TOKEN_SECRET;
        final String userAndroidId = Preferences.PREFERENCE_ANDROID_ID;
        final String userGoogleAccount = Preferences.PREFERENCE_GOOGLE_ACCOUNT;
        final String userICCID = Preferences.PREFERENCE_USER_ICCID;
        final String userImei = Preferences.PREFERENCE_USER_IMEI;
    }

    public ApplicationSetting(Context context) {
        this.mContext = context;
        this.setting = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Keys getPreferenceKeys() {
        return this.keys;
    }

    public boolean getServerHttps() {
    	return this.setting.getBoolean(Preferences.SERVER_HTTPS, false);
	}

	public void commitServerHttps(boolean useHttps) {
		SharedPreferences.Editor editor = this.setting.edit();
		editor.putBoolean(Preferences.SERVER_HTTPS, useHttps);
		editor.apply();
	}

	public void clearServerHttps() {
		SharedPreferences.Editor editor = this.setting.edit();
		editor.remove(Preferences.SERVER_HTTPS);
		editor.apply();
	}

    public String getServerURL() {
    	return this.setting.getString(Preferences.SERVER_URL, "");
	}

	public void commitServerURL(String url) {
		SharedPreferences.Editor editor = this.setting.edit();
		editor.putString(Preferences.SERVER_URL, url);
		editor.apply();
	}

	public void clearServerURL() {
		SharedPreferences.Editor editor = this.setting.edit();
		editor.remove(Preferences.SERVER_URL);
		editor.apply();
	}

	public int getServerPort() {
		return this.setting.getInt(Preferences.SERVER_PORT, 0);
	}

	public void commitServerPort(int port) {
		SharedPreferences.Editor editor = this.setting.edit();
		editor.putInt(Preferences.SERVER_PORT, port);
		editor.apply();
	}

	public void clearServerPort() {
		SharedPreferences.Editor editor = this.setting.edit();
		editor.remove(Preferences.SERVER_PORT);
		editor.apply();
	}

    public String getUserGoogleAccount() {
        return this.setting.getString(this.keys.userGoogleAccount, "");
    }

    public void commitUserGoogleAccount() {
        PhoneInfo p = new PhoneInfo(this.mContext);
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.userGoogleAccount, p.getGoogleAccount());
        editor.apply();
    }

    public void commitICCID(String iccid) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.userICCID, iccid);
        editor.apply();
    }

    public String getICCID() {
        return this.setting.getString(this.keys.userICCID, null);
    }

    public void commitUUID(String uuid) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.userAndroidId, uuid);
        editor.apply();
    }

    public String getUUID() {
    	return this.setting.getString(this.keys.userAndroidId, "");
    }

    public void clearUUID() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.userAndroidId);
        editor.apply();
    }

    public void clearDataVersion() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.dataVersion);
        editor.apply();
    }

    public void removeUser() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.userICCID);
        editor.remove(this.keys.userAndroidId);
        editor.remove(this.keys.userImei);
        editor.apply();
    }

    public String getDataVersion() {
        return this.setting.getString(this.keys.dataVersion, null);
    }

    public void commitFacebookExpired(Long faceBookExpired) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putLong(this.keys.facebookExpired, faceBookExpired);
        editor.apply();
    }

    public Long getFaceBookExpired() {
        return this.setting.getLong(this.keys.facebookExpired, 0);
    }

    private void clearFaceBookExpired() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookExpired);
        editor.apply();
    }

    public void commitFaceBookToken(String faceBookToken) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.facebookToken, faceBookToken);
        editor.apply();
    }

    public String getFaceBookToken() {
        return this.setting.getString(this.keys.facebookToken, null);
    }

    public void clearFaceBookToken() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookToken);
        editor.apply();
    }

    public void commitFacebookLastUpdate(Long faceBookLastUpdate) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putLong(this.keys.facebookLastUpdate, faceBookLastUpdate);
        editor.apply();
    }

    public Long getFaceBookLastUpdate() {
        return this.setting.getLong(this.keys.facebookLastUpdate, 0);
    }

    public void clearFaceBookLastUpdate() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookLastUpdate);
        editor.apply();
    }

    public void commitFacebookID(String faceBookID) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.facebookID, faceBookID);
        editor.apply();
    }

    public String getFaceBookID() {
        return this.setting.getString(this.keys.facebookID, "");
    }

    public void clearFaceBookID() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookID);
        editor.apply();
    }

    public void commitFaceBookEmail(String faceBookEmail) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.facebookEmail, faceBookEmail);
        editor.apply();
    }

    public String getFaceBookEmail() {
        return this.setting.getString(this.keys.facebookEmail, null);
    }

    public void clearFaceBookEmail() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.facebookEmail);
        editor.apply();
    }

    public void commitDeviceToken(String deviceToken) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.deviceToken, deviceToken);
        editor.apply();
    }

    public String getDeviceToken() {
        return this.setting.getString(this.keys.deviceToken, (String) null);
    }

    public void clearDeviceToken() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.deviceToken);
        editor.apply();
    }

    public void commitTwitterAccessToken(String token) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.twitterAccessToken, token);
        editor.apply();
    }

    public String getTwitterAccessToken() {
        return this.setting.getString(this.keys.twitterAccessToken, (String) null);
    }

    public void clearTwitterAccessToken() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.twitterAccessToken);
        editor.apply();
    }

    public void commitTwitterAccessTokenSecret(String tokenSecret) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putString(this.keys.twitterAccessTokenSecret, tokenSecret);
        editor.apply();
    }

    public String getTwitterAccessTokenSecret() {
        return this.setting.getString(this.keys.twitterAccessTokenSecret, (String) null);
    }

    public void clearTwitterAccessTokenSecret() {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.remove(this.keys.twitterAccessTokenSecret);
        editor.apply();
    }

    public void commitSyncFaceBook(boolean sync) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putBoolean(this.keys.isSyncFaceBook, sync);
        editor.apply();
    }

    public void commitSyncTwitter(boolean sync) {
        SharedPreferences.Editor editor = this.setting.edit();
        editor.putBoolean(this.keys.isSyncTwitter, sync);
        editor.apply();
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
    	clearServerHttps();
    	clearServerURL();
    	clearServerPort();
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
