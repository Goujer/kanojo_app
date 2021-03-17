package com.goujer.barcodekanojo.activity.top

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.goujer.barcodekanojo.activity.base.BaseActivity
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
import kotlinx.coroutines.*
import java.net.ConnectException

class LaunchActivity : BaseActivity() {
	private lateinit var mProgressbar: View
	private lateinit var mLogIn: View
	private lateinit var mSignUp: View
	private lateinit var mServerConfig: View
	private lateinit var mServerName: TextView
	private lateinit var settings: ApplicationSetting

	private var scope = CoroutineScope(Dispatchers.IO) + CoroutineName(TAG)
	private var loginJob: Job? = null

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

		//Set Button Listeners
		mLogIn.setOnClickListener {
			val logInIntent = Intent().setClass(this, LoginActivity::class.java)
			logInIntent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
			startActivityForResult(logInIntent, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
		}
		mSignUp.setOnClickListener {
			val signUp = Intent().setClass(this, UserModifyActivity::class.java)
			signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
			startActivity(signUp)
		}
		mServerConfig.setOnClickListener {
			startActivity(Intent(this, ServerConfigurationActivity::class.java))
		}

		//Set visibilities
		if (settings.getServerURL() == "") {
			mProgressbar.visibility = View.GONE
			mLogIn.visibility = View.INVISIBLE
			mSignUp.visibility = View.INVISIBLE
			mServerConfig.visibility = View.VISIBLE
		} else {
			mProgressbar.visibility = View.VISIBLE
			mLogIn.visibility = View.INVISIBLE
			mSignUp.visibility = View.INVISIBLE
			mServerConfig.visibility = View.INVISIBLE
			//Attempt log in / verify server
			loginJob = scope.launch {
				try {
					if (verifyUser()) {
						finish()
						withContext(Dispatchers.Main) {
							startActivity(Intent().setClass(this@LaunchActivity, KanojosActivity::class.java))
						}
					} else {
						withContext(Dispatchers.Main) {
							mProgressbar.visibility = View.GONE
							mLogIn.visibility = View.VISIBLE
							mSignUp.visibility = View.VISIBLE
							mServerConfig.visibility = View.VISIBLE
						}
					}
				} catch (e: ConnectException) {
					withContext(Dispatchers.Main) {
						mProgressbar.visibility = View.GONE
						mServerConfig.visibility = View.VISIBLE
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		mServerName.text = settings.getServerURL()
	}

	override fun onStop() {
		super.onStop()
		if (loginJob != null && !loginJob!!.isCompleted) {
			loginJob!!.cancel()
		}

		//Set Button Listeners
		mLogIn.setOnClickListener(null)
		mSignUp.setOnClickListener(null)
		mServerConfig.setOnClickListener(null)

		mProgressbar.visibility = View.VISIBLE
		mLogIn.visibility = View.INVISIBLE
		mSignUp.visibility = View.INVISIBLE
		mServerConfig.visibility = View.INVISIBLE
	}

	override fun onDestroy() {
		super.onDestroy()
		scope.cancel()
	}

	override fun onBackPressed() {

	}

	private fun verifyUser(): Boolean {
		val response: Response<BarcodeKanojoModel>
		try {
			response = bootTaskProcess()
		} catch (e: BarcodeKanojoException) {
			if (e.message.equals("user not found", ignoreCase = true)) {
				runOnUiThread {
					showNoticeDialog(getString(R.string.error_user_not_found));
				}
				return false
			} else {
				Log.d(TAG, "Unknown error has occurred during verify")
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

	private fun bootTaskProcess(): Response<BarcodeKanojoModel> {
		val barcodeKanojo = (application as BarcodeKanojoApp).barcodeKanojo
		val user = barcodeKanojo.user
		val android_verify = barcodeKanojo.verify(user.email, user.password, (application as BarcodeKanojoApp).uuid)
		barcodeKanojo.init_product_category_list()
		return android_verify
	}

	companion object {
		private const val TAG = "Launch Activity"
	}
}