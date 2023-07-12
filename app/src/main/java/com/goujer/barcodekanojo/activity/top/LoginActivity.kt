package com.goujer.barcodekanojo.activity.top

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.goujer.barcodekanojo.core.Password.Companion.hashPassword
import jp.co.cybird.barcodekanojoForGAM.view.EditTextView
import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.R
import com.goujer.barcodekanojo.databinding.ActivityBootBinding
import com.goujer.barcodekanojo.databinding.ActivityLoginBinding
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import kotlinx.coroutines.*
import java.net.SocketException
import java.net.UnknownHostException

class LoginActivity : BaseEditActivity(), View.OnClickListener {
	private lateinit var binding: ActivityLoginBinding

	private var scope = CoroutineScope(Dispatchers.IO) + CoroutineName(TAG)
	private var loginJob: Job? = null

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLoginBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.kanojoLogInPassword.setTypeToPassword()
		binding.kanojoLogInEmail.value = (application as BarcodeKanojoApp).barcodeKanojo.settings.getEmail()
		binding.kanojoLogInClose.setOnClickListener(this)
		binding.kanojoLogInPassword.setOnClickListener(this)
		binding.kanojoLogInBtn.setOnClickListener(this)
		binding.progressbar.visibility = View.GONE
		setAutoRefreshSession(false)
	}

	override fun onDestroy() {
		super.onDestroy()
		binding.kanojoLogInClose.setOnClickListener(null)
		binding.kanojoLogInPassword.setOnClickListener(null)
		binding.kanojoLogInBtn.setOnClickListener(null)
	}

	override fun onClick(v: View) {
		val id = v.id
		if (id == R.id.kanojo_log_in_close) {
			finish()
		} else if (id == R.id.kanojo_log_in_btn) {
			if (binding.kanojoLogInPassword.value.isEmpty()) {
				showNoticeDialog(getString(R.string.error_no_password))
			} else if (binding.kanojoLogInEmail.value.isEmpty()) {
				showNoticeDialog(getString(R.string.error_no_email))
			} else {
				binding.kanojoLogInClose.isEnabled = false
				binding.kanojoLogInEmail.isEnabled = false
				binding.kanojoLogInPassword.isEnabled = false
				binding.kanojoLogInBtn.isEnabled = false
				binding.progressbar.visibility = View.VISIBLE

				//Attempt log in / verify server
				loginJob = scope.launch {
					if (verifyUser()) {
						val barcodeKanojo = (application as BarcodeKanojoApp).barcodeKanojo
						val applicationSetting = barcodeKanojo.settings
						barcodeKanojo.init_product_category_list()
						applicationSetting.setPassword(hashPassword(binding.kanojoLogInPassword.value, ""))
						applicationSetting.setEmail(binding.kanojoLogInEmail.value)
						setResult(RESULT_LOG_IN, null as Intent?)
						finish()
					} else {
						withContext(Dispatchers.Main) {
							binding.progressbar.visibility = View.GONE
							binding.kanojoLogInClose.isEnabled = true
							binding.kanojoLogInEmail.isEnabled = true
							binding.kanojoLogInPassword.isEnabled = true
							binding.kanojoLogInBtn.isEnabled = true
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
			response = barcodeKanojo.verify(barcodeKanojo.settings.getUUID(), binding.kanojoLogInEmail.value, hashPassword(binding.kanojoLogInPassword.value, ""))
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