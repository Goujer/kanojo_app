package com.google.zxing.client.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import java.util.ArrayList;
import java.util.Collection;
import com.goujer.barcodekanojo.R;

public final class PreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
    public static final String KEY_BULK_MODE = "preferences_bulk_mode";
    public static final String KEY_COPY_TO_CLIPBOARD = "preferences_copy_to_clipboard";
    public static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";
    public static final String KEY_DECODE_1D = "preferences_decode_1D";
    public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
    public static final String KEY_DECODE_QR = "preferences_decode_QR";
    public static final String KEY_DISABLE_CONTINUOUS_FOCUS = "preferences_disable_continuous_focus";
    public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";
    public static final String KEY_HELP_VERSION_SHOWN = "preferences_help_version_shown";
    public static final String KEY_INVERT_SCAN = "preferences_invert_scan";
    public static final String KEY_PLAY_BEEP = "preferences_play_beep";
    public static final String KEY_REMEMBER_DUPLICATES = "preferences_remember_duplicates";
    public static final String KEY_SEARCH_COUNTRY = "preferences_search_country";
    public static final String KEY_SUPPLEMENTAL = "preferences_supplemental";
    public static final String KEY_VIBRATE = "preferences_vibrate";
    private CheckBoxPreference decode1D;
    private CheckBoxPreference decodeDataMatrix;
    private CheckBoxPreference decodeQR;

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceScreen preferences = getPreferenceScreen();
        preferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        this.decode1D = (CheckBoxPreference) preferences.findPreference(KEY_DECODE_1D);
        this.decodeQR = (CheckBoxPreference) preferences.findPreference(KEY_DECODE_QR);
        this.decodeDataMatrix = (CheckBoxPreference) preferences.findPreference(KEY_DECODE_DATA_MATRIX);
        disableLastCheckedPref();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        disableLastCheckedPref();
    }

    private void disableLastCheckedPref() {
        boolean disable;
        boolean z;
        Collection<CheckBoxPreference> checked = new ArrayList<>(3);
        if (this.decode1D.isChecked()) {
            checked.add(this.decode1D);
        }
        if (this.decodeQR.isChecked()) {
            checked.add(this.decodeQR);
        }
        if (this.decodeDataMatrix.isChecked()) {
            checked.add(this.decodeDataMatrix);
        }
        if (checked.size() < 2) {
            disable = true;
        } else {
            disable = false;
        }
        for (CheckBoxPreference pref : new CheckBoxPreference[]{this.decode1D, this.decodeQR, this.decodeDataMatrix}) {
            if (!disable || !checked.contains(pref)) {
                z = true;
            } else {
                z = false;
            }
            pref.setEnabled(z);
        }
    }
}
