package com.goujer.barcodekanojo.activity.top

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar

import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.R
import com.goujer.barcodekanojo.core.Password.Companion.hashPassword

import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.view.EditTextView

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext

import java.net.SocketException
import java.net.UnknownHostException

class LoginActivity : BaseEditActivity(), View.OnClickListener {

	private lateinit var kanojoLogInClose: Button
	private lateinit var kanojoLogInEmail: EditTextView
	private lateinit var kanojoLogInPassword: EditTextView
	private lateinit var kanojoLogInBtn: Button
	private lateinit var logInProgressBar: ProgressBar

	private var scope = CoroutineScope(Dispatchers.IO) + CoroutineName(TAG)
	private var loginJob: Job? = null

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		kanojoLogInClose = findViewById<View>(R.id.kanojo_log_in_close) as Button
		kanojoLogInEmail = findViewById<View>(R.id.kanojo_log_in_email) as EditTextView
		kanojoLogInPassword = findViewById<View>(R.id.kanojo_log_in_password) as EditTextView
		kanojoLogInBtn = findViewById<View>(R.id.kanojo_log_in_btn) as Button
		logInProgressBar = findViewById(R.id.log_in_progressbar)

		kanojoLogInPassword.setTypeToPassword()
		kanojoLogInEmail.value = (application as BarcodeKanojoApp).barcodeKanojo.settings.getEmail()
		kanojoLogInClose.setOnClickListener(this)
		kanojoLogInPassword.setOnClickListener(this)
		kanojoLogInBtn.setOnClickListener(this)
		logInProgressBar.visibility = View.GONE
		setAutoRefreshSession(false)
	}

	override fun onDestroy() {
		super.onDestroy()
		kanojoLogInClose.setOnClickListener(null)
		kanojoLogInPassword.setOnClickListener(null)
		kanojoLogInBtn.setOnClickListener(null)
	}

	override fun onClick(v: View) {
		val id = v.id
		if (id == R.id.kanojo_log_in_close) {
			finish()
		} else if (id == R.id.kanojo_log_in_btn) {
			if (kanojoLogInPassword.value.isEmpty()) {
				showNoticeDialog(getString(R.string.error_no_password))
			} else if (kanojoLogInEmail.value.isEmpty()) {
				showNoticeDialog(getString(R.string.error_no_email))
			} else {
				kanojoLogInClose.isEnabled = false
				kanojoLogInEmail.isEnabled = false
				kanojoLogInPassword.isEnabled = false
				kanojoLogInBtn.isEnabled = false
				logInProgressBar.visibility = View.VISIBLE

				//Attempt log in / verify server
				loginJob = scope.launch {
					if (verifyUser()) {
						val barcodeKanojo = (application as BarcodeKanojoApp).barcodeKanojo
						val applicationSetting = barcodeKanojo.settings
						barcodeKanojo.init_product_category_list()
						applicationSetting.setPassword(hashPassword(kanojoLogInPassword.value, ""))
						applicationSetting.setEmail(kanojoLogInEmail.value)
						setResult(RESULT_LOG_IN, null as Intent?)
						finish()
					} else {
						withContext(Dispatchers.Main) {
							logInProgressBar.visibility = View.GONE
							kanojoLogInClose.isEnabled = true
							kanojoLogInEmail.isEnabled = true
							kanojoLogInPassword.isEnabled = true
							kanojoLogInBtn.isEnabled = true
						}
					}
				}
			}
		}
	}

	private fun verifyUser(): Boolean {
		val response: Response<BarcodeKanojoModel?>
		try {
			val barcodeKanojo = (application as BarcodeKanojoApp).barcodeKanojo
			response = barcodeKanojo.verify(barcodeKanojo.settings.getUUID(), kanojoLogInEmail.value, hashPassword(kanojoLogInPassword.value, ""))
		} catch (e: BarcodeKanojoException) {
			if (e.message.equals("user not found", ignoreCase = true)) {
				runOnUiThread {
					showNoticeDialog(getString(R.string.error_user_not_found));
				}
			} else {
				Log.d(TAG, "Unknown error has occurred during verify")
				runOnUiThread {
					showNoticeDialog(e.localizedMessage);
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
		} catch (e: SocketException) {
			runOnUiThread {
				showNoticeDialog(e.localizedMessage)
			}
			return false
		}
		runOnUiThread {
			getCodeAndShowAlert(response, null)
		}
		return response.code == Response.CODE_SUCCESS
	}

	companion object {
		private const val TAG = "LoginActivity"
	}
}