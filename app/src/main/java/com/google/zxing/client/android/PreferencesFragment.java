/*
 * Copyright (C) 2013 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import java.util.ArrayList;
import java.util.Collection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.goujer.barcodekanojo.R;

/**
 * Implements support for barcode scanning preferences.
 *
 * @see PreferencesActivity
 */
public final class PreferencesFragment
		extends PreferenceFragment
		implements SharedPreferences.OnSharedPreferenceChangeListener {

	private CheckBoxPreference[] checkBoxPrefs;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.preferences);

		PreferenceScreen preferences = getPreferenceScreen();
		preferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		checkBoxPrefs = findDecodePrefs(preferences);
		disableLastCheckedPref();
	}

	private static CheckBoxPreference[] findDecodePrefs(PreferenceScreen preferences, String... keys) {
		CheckBoxPreference[] prefs = new CheckBoxPreference[keys.length];
		for (int i = 0; i < keys.length; i++) {
			prefs[i] = (CheckBoxPreference) preferences.findPreference(keys[i]);
		}
		return prefs;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		disableLastCheckedPref();
	}

	private void disableLastCheckedPref() {
		Collection<CheckBoxPreference> checked = new ArrayList<>(checkBoxPrefs.length);
		for (CheckBoxPreference pref : checkBoxPrefs) {
			if (pref.isChecked()) {
				checked.add(pref);
			}
		}
		boolean disable = checked.size() <= 1;
		for (CheckBoxPreference pref : checkBoxPrefs) {
			pref.setEnabled(!(disable && checked.contains(pref)));
		}
	}
}
