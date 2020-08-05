package com.goujer.barcodekanojo.activity.setting

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView
import java.net.CookieHandler
import java.net.CookieManager

class ServerConfigurationActivity : BaseEditActivity(), View.OnClickListener {
	private lateinit var btnClose: Button
	private lateinit var switchHttp: Switch
	private lateinit var txtURL: EditItemView
	private lateinit var txtPort: EditItemView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_server_configuration)
		btnClose = findViewById(R.id.setting_server_close)
		btnClose.setOnClickListener(this)
		switchHttp = findViewById(R.id.setting_server_https)
		switchHttp.isChecked = ApplicationSetting(this).serverHttps
		txtURL = findViewById(R.id.setting_server_url)
		txtURL.setOnClickListener(this)
		txtURL.value = ApplicationSetting(this).getServerURL()
		txtPort = findViewById(R.id.setting_server_port)
		txtPort.setOnClickListener(this)
		txtPort.value = ApplicationSetting(this).getServerPort().toString()
	}

	override fun onDestroy() {
		btnClose.setOnClickListener(null)
		txtURL.setOnClickListener(null)
		txtPort.setOnClickListener(null)
		super.onDestroy()
	}

	override fun onClick(v: View) {
		val input = EditText(this)
		when (v.id) {
			R.id.setting_server_close -> {
				saveData()
				finish()
			}
			R.id.setting_server_url -> {
				input.inputType = InputType.TYPE_CLASS_TEXT
				showEditTextDialog(r.getString(R.string.server_url), txtURL, input)
			}
			R.id.setting_server_port -> {
				input.inputType = InputType.TYPE_CLASS_NUMBER
				showEditTextDialog(r.getString(R.string.server_port), txtPort, input)
			}
		}
	}

	override fun onBackPressed() {
		saveData()
		super.onBackPressed()
	}

	private fun saveData() {
		(application as BarcodeKanojoApp).updateBCKApi(switchHttp.isChecked, txtURL.value, Integer.parseInt(txtPort.value))
		ApplicationSetting(this).commitServerHttps(switchHttp.isChecked)
		ApplicationSetting(this).commitServerURL(txtURL.value)
		ApplicationSetting(this).commitServerPort(Integer.parseInt(txtPort.value))
	}
}