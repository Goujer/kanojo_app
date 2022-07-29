package com.goujer.barcodekanojo.activity.top

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View

import com.goujer.barcodekanojo.activity.base.BaseActivity
import com.goujer.barcodekanojo.activity.setting.ServerConfigurationActivity
import com.goujer.barcodekanojo.activity.setting.UserModifyActivity
import com.goujer.barcodekanojo.preferences.ApplicationSetting
import com.goujer.barcodekanojo.BarcodeKanojoApp

import com.goujer.barcodekanojo.R
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface
import jp.co.cybird.barcodekanojoForGAM.activity.top.LoginActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import com.goujer.barcodekanojo.databinding.ActivityBootBinding

import kotlinx.coroutines.*

import java.net.SocketException
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
		settings = (application as BarcodeKanojoApp).settings
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
		return if (response.code == Response.CODE_SUCCESS) {
			true
		} else {
			Log.d(TAG, "Unknown response code: " + response.code + "\n" + response.message)
			false
		}
	}

	private fun bootTaskProcess(): Response<BarcodeKanojoModel> {
		val barcodeKanojo = (application as BarcodeKanojoApp).barcodeKanojo
		val user = barcodeKanojo.user
		val android_verify = barcodeKanojo.verify(user.email, user.password, (application as BarcodeKanojoApp).uUID)
		barcodeKanojo.init_product_category_list()
		return android_verify
	}

	companion object {
		private const val TAG = "Launch Activity"
	}
}