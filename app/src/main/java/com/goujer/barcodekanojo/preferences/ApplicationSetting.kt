package com.goujer.barcodekanojo.preferences

import android.content.Context
import android.content.SharedPreferences
import com.goujer.barcodekanojo.core.Password
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences
import java.util.*

class ApplicationSetting(context: Context) {
	private var setting: SharedPreferences = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)

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

	fun getServerURL(): String {
		return setting.getString(Preferences.SERVER_URL, "")!!
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

	fun setUUID(uuid: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.DEVICE_UUID, uuid)
		editor.apply()
	}

	fun getUUID(): String {
		var uuid = setting.getString(Preferences.DEVICE_UUID, null)
		if (uuid == null)
			uuid = UUID.randomUUID().toString()
			setUUID(uuid)
		return uuid
	}

	fun clearUUID() {
		val editor = setting.edit()
		editor.remove(Preferences.DEVICE_UUID)
		editor.apply()
	}

	fun setEmail(email: String) {
		val editor = setting.edit()
		editor.putString(Preferences.USER_EMAIL, email.replace(" ", "").lowercase())
		editor.apply()
	}

	fun getEmail(): String {
		return setting.getString(Preferences.USER_EMAIL, "")!!
	}

	fun clearEmail() {
		val editor = setting.edit()
		editor.remove(Preferences.USER_EMAIL)
		editor.apply()
	}

	fun setPassword(password: Password) {
		val editor = setting.edit()
		editor.putString(Preferences.USER_PASSWORD_HASH, password.hashedPassword)
		editor.apply()
	}

	fun getPassword(): Password {
		return Password.saveHashedPassword(setting.getString(Preferences.USER_PASSWORD_HASH, "") ?: "")
	}

	fun clearPassword() {
		val editor = setting.edit()
		editor.remove(Preferences.USER_PASSWORD_HASH)
		editor.apply()
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

	fun logout() {
		clearEmail()
		clearPassword()
		clearUUID()
	}

	fun reset() {
		clearServerHttps()
		clearServerURL()
		clearServerPort()
		clearUUID()
		clearEmail()
		clearPassword()
		clearDeviceToken()
	}
}