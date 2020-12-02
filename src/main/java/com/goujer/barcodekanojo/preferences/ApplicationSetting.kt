package com.goujer.barcodekanojo.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import jp.co.cybird.barcodekanojoForGAM.core.util.PhoneInfo
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences

class ApplicationSetting(context: Context) {
	private var mContext: Context = context
	private var setting: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

	fun getServerHttps(): Boolean {
		return setting.getBoolean(Preferences.SERVER_HTTPS, false)
	}

	fun commitServerHttps(useHttps: Boolean) {
		val editor = setting.edit()
		editor.putBoolean(Preferences.SERVER_HTTPS, useHttps)
		editor.apply()
	}

	fun clearServerHttps() {
		val editor = setting.edit()
		editor.remove(Preferences.SERVER_HTTPS)
		editor.apply()
	}

	fun getServerURL(): String? {
		return setting.getString(Preferences.SERVER_URL, "")
	}

	fun commitServerURL(url: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.SERVER_URL, url)
		editor.apply()
	}

	fun clearServerURL() {
		val editor = setting.edit()
		editor.remove(Preferences.SERVER_URL)
		editor.apply()
	}

	fun getServerPort(): Int {
		return setting.getInt(Preferences.SERVER_PORT, 0)
	}

	fun commitServerPort(port: Int) {
		val editor = setting.edit()
		editor.putInt(Preferences.SERVER_PORT, port)
		editor.apply()
	}

	fun clearServerPort() {
		val editor = setting.edit()
		editor.remove(Preferences.SERVER_PORT)
		editor.apply()
	}

	fun getUserGoogleAccount(): String? {
		return setting.getString(Preferences.PREFERENCE_GOOGLE_ACCOUNT, "")
	}

	fun commitUserGoogleAccount() {
		val p = PhoneInfo(mContext)
		val editor = setting.edit()
		editor.putString(Preferences.PREFERENCE_GOOGLE_ACCOUNT, p.googleAccount)
		editor.apply()
	}

	fun commitICCID(iccid: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.PREFERENCE_USER_ICCID, iccid)
		editor.apply()
	}

	fun getICCID(): String? {
		return setting.getString(Preferences.PREFERENCE_USER_ICCID, "")
	}

	fun commitUUID(uuid: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.PREFERENCE_ANDROID_ID, uuid)
		editor.apply()
	}

	fun getUUID(): String? {
		return setting.getString(Preferences.PREFERENCE_ANDROID_ID, "")
	}

	fun clearUUID() {
		val editor = setting.edit()
		editor.remove(Preferences.PREFERENCE_ANDROID_ID)
		editor.apply()
	}

	fun clearDataVersion() {
		val editor = setting.edit()
		editor.remove(Preferences.PREFERENCE_ANDROID_ID)
		editor.apply()
	}

	fun removeUser() {
		val editor = setting.edit()
		editor.remove(Preferences.PREFERENCE_USER_ICCID)
		editor.remove(Preferences.PREFERENCE_ANDROID_ID)
		editor.remove(Preferences.PREFERENCE_USER_IMEI)
		editor.apply()
	}

	fun getDataVersion(): String? {
		return setting.getString(Preferences.PREFERENCE_ANDROID_ID, null)
	}

	fun commitDeviceToken(deviceToken: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.PREFERENCE_DEVICE_TOKEN, deviceToken)
		editor.apply()
	}

	fun getDeviceToken(): String? {
		return setting.getString(Preferences.PREFERENCE_DEVICE_TOKEN, null as String?)
	}

	fun clearDeviceToken() {
		val editor = setting.edit()
		editor.remove(Preferences.PREFERENCE_DEVICE_TOKEN)
		editor.apply()
	}

	fun reset() {
		clearServerHttps()
		clearServerURL()
		clearServerPort()
		clearDataVersion()
	}
}