package jp.co.cybird.barcodekanojoForGAM.core.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import com.facebook.android.Facebook;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;

public class SessionStore {
    private static final String EXPIRES = "expires_in";
    private static final String KEY = "facebook-session";
    private static final String LAST_UPDATE = "last_update";
    private static final String TOKEN = "access_token";

    public static boolean save(Facebook session, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("facebook-session", 0).edit();
        editor.putString("access_token", session.getAccessToken());
        editor.putLong("expires_in", session.getAccessExpires());
        editor.putLong("last_update", session.getLastAccessUpdate());
        ApplicationSetting setting = new ApplicationSetting(context);
        setting.commitFaceBookToken(session.getAccessToken());
        setting.commitFacebookExpired(Long.valueOf(session.getAccessExpires()));
        setting.commitFacebookLastUpdate(Long.valueOf(session.getLastAccessUpdate()));
        return editor.commit();
    }

    public static boolean restore(Facebook session, Context context) {
        SharedPreferences savedSession = context.getSharedPreferences("facebook-session", 0);
        session.setTokenFromCache(savedSession.getString("access_token", (String) null), savedSession.getLong("expires_in", 0), savedSession.getLong("last_update", 0));
        return session.isSessionValid();
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("facebook-session", 0).edit();
        editor.clear();
        editor.commit();
    }
}
