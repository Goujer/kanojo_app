package jp.co.cybird.android.lib.gcm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Const.PREF_FILE_NAME);
        addPreferencesFromResource(ParameterLoader.getResourceIdForType("lib_gcm_prefs", "xml", this));
    }

    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("lib_gcm_willSendNotification")) {
            if (sharedPreferences.getBoolean(key, false)) {
                GCMUtilities.registerGCM(this);
            } else {
                GCMUtilities.unregisterGCM(this);
            }
        } else if (key.equals(Const.PREF_KEY_IS_TESTING)) {
            Const.switchIsTestingFLAG(sharedPreferences.getBoolean(key, false));
        } else if (key.equals(Const.PREF_KEY_DOES_INCLUDE_SANDBOX)) {
            Const.switchServerURL(sharedPreferences.getBoolean(key, false));
        }
    }
}
