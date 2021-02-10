package com.goujer.barcodekanojo.activity.setting

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp
import jp.co.cybird.barcodekanojoForGAM.R
import com.goujer.barcodekanojo.preferences.ApplicationSetting
import jp.co.cybird.barcodekanojoForGAM.Defs
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView

class ServerConfigurationActivity : Activity(), View.OnClickListener {
	private lateinit var btnClose: Button
	private lateinit var switchHttp: CompoundButton
	private lateinit var txtURL: EditItemView
	private lateinit var txtPort: EditItemView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_server_configuration)
		btnClose = findViewById(R.id.setting_server_close)
		btnClose.setOnClickListener(this)
		switchHttp = findViewById(R.id.setting_server_https)
		switchHttp.isChecked = ApplicationSetting(this).getServerHttps()
		if (!Defs.DEBUG) {
			switchHttp.visibility = View.GONE
		}
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
				input.setText(txtURL.value)
				input.inputType = InputType.TYPE_CLASS_TEXT
				showEditTextDialog(resources.getString(R.string.server_url), txtURL, input)
			}
			R.id.setting_server_port -> {
				input.setText(txtPort.value)
				input.inputType = InputType.TYPE_CLASS_NUMBER
				showEditTextDialog(resources.getString(R.string.server_port), txtPort, input)
			}
		}
	}

	protected fun showEditTextDialog(title: String?, value: EditItemView, edit: EditText) {
		val dialog = AlertDialog.Builder(this).setTitle(title).setView(edit).setPositiveButton(R.string.common_dialog_ok) { dialog, which -> value.value = edit.text.toString() }.setNegativeButton(R.string.common_dialog_cancel) { dialog, which -> }.create()
		dialog.show()
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