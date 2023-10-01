package com.goujer.barcodekanojo.activity.top

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller

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
import com.goujer.barcodekanojo.databinding.ActivityBootBinding

import kotlinx.coroutines.*

import java.net.SocketException
import java.net.UnknownHostException
import kotlin.random.Random

class LaunchActivity : BaseActivity() {
	private lateinit var settings: ApplicationSetting

	private lateinit var binding: ActivityBootBinding

	private var scope = CoroutineScope(Dispatchers.IO) + CoroutineName(TAG)
	private var loginJob: Job? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityBootBinding.inflate(layoutInflater)
		setContentView(binding.root)
		settings = (application as BarcodeKanojoApp).barcodeKanojo.settings
		if (Random.nextInt(0, 101) == 100) {
			binding.root.setBackgroundResource(R.drawable.top_bg_blue)
		}
	}

	override fun onStart() {
		super.onStart()
		//Set Button Listeners
		binding.topLogIn.setOnClickListener {
			val logInIntent = Intent().setClass(this, LoginActivity::class.java)
			logInIntent.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
			startActivityForResult(logInIntent, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
		}
		binding.topSignUp.setOnClickListener {
			val signUp = Intent().setClass(this, UserModifyActivity::class.java)
			signUp.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
			startActivity(signUp)
		}
		binding.topConfigServer.setOnClickListener {
			startActivity(Intent(this, ServerConfigurationActivity::class.java))
		}

		//Set visibilities
		if (settings.getServerURL() == "") {
			binding.progressbar.visibility = View.GONE
			binding.topLogIn.visibility = View.INVISIBLE
			binding.topSignUp.visibility = View.INVISIBLE
			binding.topConfigServer.visibility = View.VISIBLE
		} else {
			binding.progressbar.visibility = View.VISIBLE
			binding.topLogIn.visibility = View.INVISIBLE
			binding.topSignUp.visibility = View.INVISIBLE
			binding.topConfigServer.visibility = View.INVISIBLE
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
							binding.progressbar.visibility = View.GONE
							binding.topLogIn.visibility = View.VISIBLE
							binding.topSignUp.visibility = View.VISIBLE
							binding.topConfigServer.visibility = View.VISIBLE
						}
					}
				} catch (e: SocketException) {
					withContext(Dispatchers.Main) {
						showNoticeDialog(e.message)
						binding.progressbar.visibility = View.GONE
						binding.topConfigServer.visibility = View.VISIBLE
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		binding.topServerName.text = settings.getServerURL()
		updateAndroidSecurityProvider(this)
	}

	override fun onStop() {
		super.onStop()
		Log.d(TAG, "Stop")
		if (loginJob != null && !loginJob!!.isCompleted) {
			loginJob!!.cancel()
		}

		//Set Button Listeners
		binding.topLogIn.setOnClickListener(null)
		binding.topSignUp.setOnClickListener(null)
		binding.topConfigServer.setOnClickListener(null)

		binding.progressbar.visibility = View.VISIBLE
		binding.topLogIn.visibility = View.INVISIBLE
		binding.topSignUp.visibility = View.INVISIBLE
		binding.topConfigServer.visibility = View.INVISIBLE
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

	private fun updateAndroidSecurityProvider(callingActivity: Activity) { //Attempt to update connection security (helps older devices with ssl and tls)
		try {
			ProviderInstaller.installIfNeeded(this)
		} catch (e: GooglePlayServicesRepairableException) {
			// Thrown when Google Play Services is not installed, up-to-date, or enabled
			// Show dialog to allow users to install, update, or otherwise enable Google Play services.
			GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)?.show()
		} catch (e: GooglePlayServicesNotAvailableException) {
			Log.e("SecurityException", "Google Play Services not available.")
		}
	}

	companion object {
		private const val TAG = "Launch Activity"
	}
}