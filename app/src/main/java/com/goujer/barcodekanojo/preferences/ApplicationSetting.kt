package com.goujer.barcodekanojo.preferences

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.content.SharedPreferencesCompat.EditorCompat
import com.goujer.barcodekanojo.core.Password
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences
import java.util.*

class ApplicationSetting(context: Context) {
	private var setting: SharedPreferences = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
	private val editorCompat = EditorCompat.getInstance()

	fun getServerHttps(): Boolean {
		return setting.getBoolean(Preferences.SERVER_HTTPS, false)
	}

	fun commitServerHttps(useHttps: Boolean) {
		val editor = setting.edit()
		editor.putBoolean(Preferences.SERVER_HTTPS, useHttps)
		editorCompat.apply(editor)
	}

	fun clearServerHttps() {
		val editor = setting.edit()
		editor.remove(Preferences.SERVER_HTTPS)
		editorCompat.apply(editor)
	}

	fun getServerURL(): String {
		return setting.getString(Preferences.SERVER_URL, "")!!
	}

	fun commitServerURL(url: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.SERVER_URL, url)
		editorCompat.apply(editor)
	}

	fun clearServerURL() {
		val editor = setting.edit()
		editor.remove(Preferences.SERVER_URL)
		editorCompat.apply(editor)
	}

	fun getServerPort(): Int {
		return setting.getInt(Preferences.SERVER_PORT, 0)
	}

	fun commitServerPort(port: Int) {
		val editor = setting.edit()
		editor.putInt(Preferences.SERVER_PORT, port)
		editorCompat.apply(editor)
	}

	fun clearServerPort() {
		val editor = setting.edit()
		editor.remove(Preferences.SERVER_PORT)
		editorCompat.apply(editor)
	}

	fun setUUID(uuid: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.DEVICE_UUID, uuid)
		editorCompat.apply(editor)
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
		editorCompat.apply(editor)
	}

	fun setEmail(email: String) {
		val editor = setting.edit()
		editor.putString(Preferences.USER_EMAIL, email.replace(" ", "").lowercase())
		editorCompat.apply(editor)
	}

	fun getEmail(): String {
		return setting.getString(Preferences.USER_EMAIL, "")!!
	}

	fun clearEmail() {
		val editor = setting.edit()
		editor.remove(Preferences.USER_EMAIL)
		editorCompat.apply(editor)
	}

	fun setPassword(password: Password) {
		val editor = setting.edit()
		editor.putString(Preferences.USER_PASSWORD_HASH, password.hashedPassword)
		editorCompat.apply(editor)
	}

	fun getPassword(): Password {
		return Password.saveHashedPassword(setting.getString(Preferences.USER_PASSWORD_HASH, "") ?: "")
	}

	fun clearPassword() {
		val editor = setting.edit()
		editor.remove(Preferences.USER_PASSWORD_HASH)
		editorCompat.apply(editor)
	}

	fun commitDeviceToken(deviceToken: String?) {
		val editor = setting.edit()
		editor.putString(Preferences.PREFERENCE_DEVICE_TOKEN, deviceToken)
		editorCompat.apply(editor)
	}

	fun getDeviceToken(): String? {
		return setting.getString(Preferences.PREFERENCE_DEVICE_TOKEN, null as String?)
	}

	fun clearDeviceToken() {
		val editor = setting.edit()
		editor.remove(Preferences.PREFERENCE_DEVICE_TOKEN)
		editorCompat.apply(editor)
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