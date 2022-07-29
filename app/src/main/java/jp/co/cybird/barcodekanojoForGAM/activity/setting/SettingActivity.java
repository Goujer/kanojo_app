package jp.co.cybird.barcodekanojoForGAM.activity.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.ViewGroup;

import com.goujer.barcodekanojo.activity.setting.UserModifyActivity;
import com.goujer.barcodekanojo.core.http.HttpApi;

import com.goujer.barcodekanojo.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.util.ViewUtil;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;

public class SettingActivity extends PreferenceActivity implements BaseInterface {

    public static final String KEY_PREF_ABOUT = "about_preference";
    public static final String KEY_PREF_TWITTER = "twitter_preference";
    public static final String KEY_PREF_WALLPAPER = "list_preference";
    public static final String KEY_TWEET_FOLLOW = "tweet_txt_follow_me";
    public static final String KEY_TWEET_URL = "tweet_url_public";
    private static final String TAG = "SettingActivity";
    private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            SettingActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
		setTheme(android.R.style.Theme_DeviceDefault_Light);
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.preferences);
        registerReceiver(this.mLoggedOutReceiver, new IntentFilter(BarcodeKanojoApp.INTENT_ACTION_LOGGED_OUT));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(this.mLoggedOutReceiver);
        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        if (!(root == null || root.getChildCount() == 0)) {
            ViewUtil.cleanupView(root.getChildAt(0));
        }
        super.onDestroy();
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if (Preferences.PREFERENCE_ACCOUNT_CHANGE.equals(key)) {
            startActivity(new Intent(this, UserModifyActivity.class));
            return true;
        } else if (Preferences.PREFERENCE_ABOUT_RULES.equals(key)) {
            startWebViewActivity(Defs.URL_ABOUT_RULES);
            return true;
        } else if (Preferences.PREFERENCE_ABOUT_BARCODEKANOJO.equals(key)) {
            startWebViewActivity(Defs.URL_ABOUT_BARCODEKANOJO);
            return true;
        } else if (!Preferences.PREFERENCE_ABOUT_TEAM.equals(key)) {
            return true;
        } else {
            startWebViewActivity(Defs.URL_ABOUT_TEAM);
            return true;
        }
    }

    private void startWebViewActivity(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, HttpApi.Companion.get().getMApiBaseUrl() + url);
        startActivity(intent);
    }

//    private void startMailClient(String email) {
//        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:" + email));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
}
