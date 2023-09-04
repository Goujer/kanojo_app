package com.goujer.barcodekanojo.activity.top

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import com.goujer.barcodekanojo.activity.base.BaseActivity
import com.goujer.barcodekanojo.activity.setting.ServerConfigurationActivity
import com.goujer.barcodekanojo.activity.setting.UserModifyActivity
import com.goujer.barcodekanojo.preferences.ApplicationSetting
import com.goujer.barcodekanojo.BarcodeKanojoApp

import com.goujer.barcodekanojo.R
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response

import kotlinx.coroutines.*

import java.net.SocketException
import java.net.UnknownHostException
import kotlin.random.Random

class LaunchActivity : BaseActivity() {
	private lateinit var launch_root: RelativeLayout
	private lateinit var top_server_name: TextView
	private lateinit var top_log_in: Button
	private lateinit var top_sign_up: Button
	private lateinit var top_config_server: Button
	private lateinit var progressbar: ProgressBar

	private lateinit var settings: ApplicationSetting

	private var scope = CoroutineScope(Dispatchers.IO) + CoroutineName(TAG)
	private var loginJob: Job? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_boot)

		launch_root = findViewById(R.id.launch_root)
		top_server_name = findViewById(R.id.top_server_name)
		top_log_in = findViewById(R.id.top_log_in)
		top_sign_up = findViewById(R.id.top_sign_up)
		top_config_server = findViewById(R.id.top_config_server)
		progressbar = findViewById(R.id.progressbar)

		settings = (application as BarcodeKanojoApp).barcodeKanojo.settings
		if (Random.nextInt(0, 101) == 100) {
			launch_root.setBackgroundResource(R.drawable.top_bg_blue)
		}
	}

	override fun onStart() {
		super.onStart()

		//Set Button Listeners
		top_log_in.setOnClickListener {
			val logInIntent = Intent().setClass(this, LoginActivity::class.java)
			logInIntent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
			startActivityForResult(logInIntent, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
		}
		top_sign_up.setOnClickListener {
			val signUp = Intent().setClass(this, UserModifyActivity::class.java)
			signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
			startActivity(signUp)
		}
		top_config_server.setOnClickListener {
			startActivity(Intent(this, ServerConfigurationActivity::class.java))
		}

		//Set visibilities
		if (settings.getServerURL() == "") {
			progressbar.visibility = View.GONE
			top_log_in.visibility = View.INVISIBLE
			top_sign_up.visibility = View.INVISIBLE
			top_config_server.visibility = View.VISIBLE
		} else {
			progressbar.visibility = View.VISIBLE
			top_log_in.visibility = View.INVISIBLE
			top_sign_up.visibility = View.INVISIBLE
			top_config_server.visibility = View.INVISIBLE
			//Attempt log in / verify server
			loginJob = scope.launch {
				try {
					if (verifyUser()) {
						//Get Product Category List from Server
						(application as BarcodeKanojoApp).barcodeKanojo.init_product_category_list()
						finish()
						withContext(Dispatchers.Main) {
							startActivity(Intent().setClass(this@LaunchActivity, KanojosActivity::class.java))
						}
					} else {
						withContext(Dispatchers.Main) {
							progressbar.visibility = View.GONE
							top_log_in.visibility = View.VISIBLE
							top_sign_up.visibility = View.VISIBLE
							top_config_server.visibility = View.VISIBLE
						}
					}
				} catch (e: SocketException) {
					withContext(Dispatchers.Main) {
						showNoticeDialog(e.message)
						progressbar.visibility = View.GONE
						top_config_server.visibility = View.VISIBLE
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		top_server_name.text = settings.getServerURL()
	}

	override fun onStop() {
		super.onStop()
		Log.d(TAG, "Stop")
		if (loginJob != null && !loginJob!!.isCompleted) {
			loginJob!!.cancel()
		}

		//Set Button Listeners
		top_log_in.setOnClickListener(null)
		top_sign_up.setOnClickListener(null)
		top_config_server.setOnClickListener(null)

		progressbar.visibility = View.VISIBLE
		top_log_in.visibility = View.INVISIBLE
		top_sign_up.visibility = View.INVISIBLE
		top_config_server.visibility = View.INVISIBLE
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d(TAG, "Destroy")
		scope.cancel()
	}

	@Deprecated("Deprecated in Java")
	override fun onBackPressed() {

	}

	private fun verifyUser(): Boolean {
		val response: Response<BarcodeKanojoModel?>
		try {
			response = bootTaskProcess()
		} catch (e: BarcodeKanojoException) {
			if (e.message.equals("user not found", ignoreCase = true)) {
				runOnUiThread {
					showNoticeDialog(getString(R.string.error_user_not_found))
				}
			} else {
				Log.d(TAG, "Unknown error has occurred during verify")
				runOnUiThread {
					showNoticeDialog(e.localizedMessage)
				}
				e.printStackTrace()
			}
			return false
		} catch (e: UnknownHostException) {
			e.printStackTrace()
			runOnUiThread {
				showNoticeDialog(getString(R.string.error_unknown_host_exception))
			}
			return false
		}
		return if (response.code == Response.CODE_SUCCESS) {
			true
		} else {
			Log.d(TAG, "Unknown response code: " + response.code + "\n" + response.message)
			false
		}
	}

	private fun bootTaskProcess(): Response<BarcodeKanojoModel?> {
		val barcodeKanojoApp = (application as BarcodeKanojoApp)
		val barcodeKanojo = barcodeKanojoApp.barcodeKanojo

		//Log User in
		return barcodeKanojo.verify(barcodeKanojoApp.settings.getUUID(), "", null)
	}

	companion object {
		private const val TAG = "Launch Activity"
	}
}