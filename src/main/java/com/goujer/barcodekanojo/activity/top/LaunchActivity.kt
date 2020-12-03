package com.goujer.barcodekanojo.activity.top

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.goujer.barcodekanojo.activity.setting.ServerConfigurationActivity
import com.goujer.barcodekanojo.preferences.ApplicationSetting
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface
import jp.co.cybird.barcodekanojoForGAM.activity.setting.UserModifyActivity
import jp.co.cybird.barcodekanojoForGAM.activity.top.LoginActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.ConnectException

class LaunchActivity : Activity() {
	private lateinit var mProgressbar: View
	private lateinit var mLogIn: View
	private lateinit var mSignUp: View
	private lateinit var mServerConfig: View
	private lateinit var mServerName: TextView
	private lateinit var settings: ApplicationSetting
	private lateinit var scope: CoroutineScope

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_boot)
		settings = (application as BarcodeKanojoApp).settings
		mProgressbar = findViewById(R.id.progressbar)
		mLogIn = findViewById(R.id.top_log_in)
		mSignUp = findViewById(R.id.top_sign_up)
		mServerConfig = findViewById(R.id.top_config_server)
		mServerName = findViewById(R.id.top_server_name)
	}

	override fun onStart() {
		super.onStart()
		scope = CoroutineScope(Dispatchers.Main)
		mServerName.text = settings.getServerURL()
		mLogIn.visibility = View.INVISIBLE
		mSignUp.visibility = View.INVISIBLE
		mServerConfig.visibility = View.INVISIBLE
	}

	override fun onResume() {
		super.onResume()
		if (settings.getServerURL() == "") {
			mProgressbar.visibility = View.GONE
			mServerConfig.visibility = View.VISIBLE
		} else {
			//Attempt log in / verify server
			scope.launch(Dispatchers.IO) {
				try {
					if (verifyUser()) {
						finish()
						startActivity(Intent().setClass(this@LaunchActivity, KanojosActivity::class.java))
					} else {
						runOnUiThread {
							mProgressbar.visibility = View.GONE
							mLogIn.visibility = View.VISIBLE
							mSignUp.visibility = View.VISIBLE
							mServerConfig.visibility = View.VISIBLE
						}
					}
				} catch (e: ConnectException) {
					runOnUiThread {
						mProgressbar.visibility = View.GONE
						mServerConfig.visibility = View.VISIBLE
					}
				}
			}
		}
	}

	override fun onPause() {
		super.onPause()
		mProgressbar.visibility = View.VISIBLE
		mLogIn.visibility = View.INVISIBLE
		mSignUp.visibility = View.INVISIBLE
		mServerConfig.visibility = View.INVISIBLE
	}

	override fun onStop() {
		super.onStop()
		scope.cancel()
	}

	fun configServer(v: View) {
		startActivity(Intent(this, ServerConfigurationActivity::class.java))
	}

	fun logIn(v: View) {
		val dashboard = Intent().setClass(this, LoginActivity::class.java)
		dashboard.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
		startActivityForResult(dashboard, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
	}

	fun signUp(v: View) {
		val signUp = Intent().setClass(this, UserModifyActivity::class.java)
		signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
		startActivity(signUp)
	}

	private suspend fun verifyUser(): Boolean {
		val response: Response<BarcodeKanojoModel>
		try {
			response = bootTaskProcess()
		} catch (e: BarcodeKanojoException) {
			if (e.message.equals("user not found", ignoreCase = true)) {
				return false
			} else {
				Log.d(TAG, "Unknown error has occured during verify")
				e.printStackTrace()
				return false
			}
		}
		if (response.code == Response.CODE_SUCCESS) {
			return true
		} else {
			Log.d(TAG, "Unknown response code: " + response.code + "\n" + response.message)
			return false
		}
	}

	private suspend fun bootTaskProcess(): Response<BarcodeKanojoModel>{
		val barcodeKanojo = (application as BarcodeKanojoApp).barcodeKanojo
		val android_verify = barcodeKanojo.verify("", "", (application as BarcodeKanojoApp).uuid)
		barcodeKanojo.init_product_category_list()
		return android_verify
	}

	companion object {
		private val TAG = "Launch Activity"
	}
}