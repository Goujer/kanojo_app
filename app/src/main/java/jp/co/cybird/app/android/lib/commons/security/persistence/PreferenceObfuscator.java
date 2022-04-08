package jp.co.cybird.app.android.lib.commons.security.persistence;

import android.content.SharedPreferences;
import android.util.Log;
import jp.co.cybird.app.android.lib.commons.security.Obfuscator;
import jp.co.cybird.app.android.lib.commons.security.ValidationException;

public class PreferenceObfuscator {
    private static final String TAG = "PreferenceObfuscator";
    private SharedPreferences.Editor mEditor = null;
    private final Obfuscator mObfuscator;
    private final SharedPreferences mPreferences;

    public PreferenceObfuscator(SharedPreferences sp, Obfuscator o) {
        this.mPreferences = sp;
        this.mObfuscator = o;
    }

    public void putString(String key, String value) {
        if (this.mEditor == null) {
            this.mEditor = this.mPreferences.edit();
        }
        this.mEditor.putString(key, this.mObfuscator.obfuscate(value, key));
    }

    public String getString(String key, String defValue) {
        String value = this.mPreferences.getString(key, (String) null);
        if (value == null) {
            return defValue;
        }
        try {
            return this.mObfuscator.unobfuscate(value, key);
        } catch (ValidationException e) {
            Log.w(TAG, "Validation error");
            return defValue;
        }
    }

    public void commit() {
        if (this.mEditor != null) {
            this.mEditor.commit();
            this.mEditor = null;
        }
    }
}
