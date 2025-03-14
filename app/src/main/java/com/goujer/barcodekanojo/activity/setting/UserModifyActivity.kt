package com.goujer.barcodekanojo.activity.setting

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.R
import com.goujer.barcodekanojo.activity.top.LaunchActivity
import com.goujer.barcodekanojo.core.Password
import com.goujer.barcodekanojo.core.Password.Companion.hashPassword
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import com.goujer.barcodekanojo.core.model.User
import com.goujer.barcodekanojo.databinding.ActivityUserModifyBinding
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView.EditItemViewCallback
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class UserModifyActivity : BaseEditActivity(), View.OnClickListener {
	private lateinit var app: BarcodeKanojoApp

	private var mRequestCode = 0
	private var mResultCode = 0
	private var mTextChangeListener: EditItemViewCallback? = null

	private var user: User? = null

	private var modifiedName: String? = null
	private var modifiedGender: String? = null
	private var modifiedBirthday: String? = null
	private var modifiedPhoto: File? = null
	private var modifiedEmail: String? = null
	private var mCurrentPassword: Password? = null
	private var mNewPassword: Password? = null

	private lateinit var mDic: DynamicImageCache
	private var imageJob: Job? = null
	private var accountJob: Job? = null

    private lateinit var binding: ActivityUserModifyBinding
	private val mScope = MainScope()
	private var ioScope = CoroutineScope(Dispatchers.IO) + CoroutineName(TAG)

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
        binding = ActivityUserModifyBinding.inflate(layoutInflater)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(binding.root)
		app = application as BarcodeKanojoApp

		val bundle = intent.extras
		if (bundle != null) {
			mRequestCode = bundle.getInt(EXTRA_REQUEST_CODE, REQUEST_SOCIAL_CONFIG_SETTING)
			if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
				setAutoRefreshSession(false)
			}
		}
		if (savedInstanceState != null) {
			mRequestCode = savedInstanceState.getInt(EXTRA_REQUEST_CODE, REQUEST_SOCIAL_CONFIG_SETTING)
			user = savedInstanceState.getParcelable("user")
			binding.userModifyEmail.value = savedInstanceState.getString("email")
		}
		if (user == null) {
			user = app.user
		}
		if (user == null) {
			user = User()
		}

		//Get Image Cache
		mDic = app.imageCache

		//Name Field Setup
		if (user!!.name != null && user!!.name!!.isNotEmpty() && user!!.name != "null") {
            binding.userModifyName.value = user!!.name
		}

		//Email Field Setup
		if (binding.userModifyEmail.value == "") {
			val email = app.barcodeKanojo.settings.getEmail()
			binding.userModifyEmail.value = app.barcodeKanojo.settings.getEmail()
		}

		//Birthday Field Setup
        binding.userModifyBirthday.value = user!!.birthText
		binding.userModifyIcon.avatar.visibility = View.VISIBLE

		//Password Fields setup
		binding.passwordChangeCurrent.hideText()
		binding.passwordChangeNew.hideText()
		binding.passwordChangeReNew.hideText()

		switchLayout()

		//Listener for determining if Update/Register Button can be pressed.
		mTextChangeListener = EditItemViewCallback { _, _ ->
            binding.userUpdateBtn.isEnabled = isReadyForUpdate
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putInt(EXTRA_REQUEST_CODE, mRequestCode)
		user!!.name = binding.userModifyName.value
		user!!.setBirthFromText(binding.userModifyBirthday.value)
		outState.putParcelable("user", user)
		outState.putString("email", binding.userModifyName.value)
		super.onSaveInstanceState(outState)
	}

	override fun onDismiss(dialog: DialogInterface?, code: Int) {
		super.onDismiss(dialog, code)
		when (code) {
			Response.CODE_SUCCESS -> updateAndClose()
			Response.CODE_ERROR_BAD_REQUEST -> if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
				updateAndClose()
			}
		}
		bindEvent()
	}

	override fun onResume() {
		super.onResume()
		bindEvent()
	}

	override fun onStop() {
		if (imageJob != null && !imageJob!!.isCompleted) {
			imageJob!!.cancel(null)
		}
		super.onStop()
	}

	override fun onDestroy() {
		unBindEvent()
		binding.userModifyName.setTextChangeListner(null)
		binding.userModifyEmail.setTextChangeListner(null)
		binding.userModifyBirthday.setTextChangeListner(null)
		binding.passwordChangeCurrent.setTextChangeListner(null)
		binding.passwordChangeNew.setTextChangeListner(null)
		binding.passwordChangeReNew.setTextChangeListner(null)
		mScope.cancel()
		binding.loadingView.dismiss()
		super.onDestroy()
	}

	fun bindEvent() {
        binding.userModifyClose.setOnClickListener(this)
		binding.userModifyLogout.setOnClickListener(this)
        binding.userModifyName.setOnClickListener(this)
        binding.userModifyEmail.setOnClickListener(this)
        binding.userModifyBirthday.setOnClickListener(this)
        binding.userModifyIcon.setOnClickListener(this)
		binding.passwordChangeCurrent.setOnClickListener(this)
		binding.passwordChangeNew.setOnClickListener(this)
		binding.passwordChangeReNew.setOnClickListener(this)
        binding.userUpdateBtn.setOnClickListener(this)
		binding.userDeleteBtn.setOnClickListener(this)

		binding.userModifyName.setTextChangeListner(mTextChangeListener)
		binding.userModifyEmail.setTextChangeListner(mTextChangeListener)
		binding.userModifyBirthday.setTextChangeListner(mTextChangeListener)
		binding.passwordChangeCurrent.setTextChangeListner(mTextChangeListener)
		binding.passwordChangeNew.setTextChangeListner(mTextChangeListener)
		binding.passwordChangeReNew.setTextChangeListner(mTextChangeListener)
	}

	private fun unBindEvent() {
        binding.userModifyClose.setOnClickListener(null)
		binding.userModifyLogout.setOnClickListener(null)
        binding.userModifyName.setOnClickListener(null)
        binding.userModifyEmail.setOnClickListener(null)
        binding.userModifyBirthday.setOnClickListener(null)
        binding.userModifyIcon.setOnClickListener(null)
		binding.passwordChangeCurrent.setOnClickListener(null)
		binding.passwordChangeNew.setOnClickListener(null)
		binding.passwordChangeReNew.setOnClickListener(null)
        binding.userUpdateBtn.setOnClickListener(null)
        binding.userDeleteBtn.setOnClickListener(null)
	}

	override fun onClick(v: View) {
		unBindEvent()
		Log.d(TAG, "View Clicked: " + v.id)
		val id = v.id
		if (id == R.id.user_modify_close) {
			close()
		} else if (id == R.id.user_modify_logout) {
			logout()
			finish()
			startActivity(Intent().setClass(this@UserModifyActivity, LaunchActivity::class.java))
		} else if (id == R.id.user_modify_name) {
			showEditTextDialog(r.getString(R.string.user_account_name), binding.userModifyName)
		} else if (id == R.id.user_modify_birthday) {
			//TODO probably a good idea to add a warning or block to those under 13
			showDatePickDialog(r.getString(R.string.user_account_birthday), binding.userModifyBirthday)
		} else if (id == R.id.user_modify_icon) {
			showImagePickerDialog(r.getString(R.string.user_account_icon))
		} else if (id == R.id.user_modify_email) {
			showEditTextDialog(r.getString(R.string.user_account_email), binding.userModifyEmail)
		} else if (v.id == R.id.password_change_current) {
			val input = EditText(this)
			input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
			showEditTextDialog(resources.getString(R.string.password_change_current), binding.passwordChangeCurrent, input)
		} else if (v.id == R.id.password_change_new) {
			val input = EditText(this)
			input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
			showEditTextDialog(resources.getString(R.string.password_change_password), binding.passwordChangeNew, input)
		} else if (v.id == R.id.password_change_re_new) {
			val input = EditText(this)
			input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
			showEditTextDialog(resources.getString(R.string.password_change_re_password), binding.passwordChangeReNew, input)
		} else if (id == R.id.user_update_btn) {
			executeUserModifyTask()
		} else if (id == R.id.user_delete_btn) {
			mResultCode = RESULT_DELETE_ACCOUNT
			showConfirmDeleteDialog(resources.getString(R.string.delete_account_warning_message))
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == RESULT_OK) {
			when (requestCode) {
				REQUEST_GALLERY, REQUEST_CAMERA ->
					if (mFile != null && mFile.exists()) {
						modifiedPhoto = mFile
						setBitmapFromFile(binding.userModifyIcon.avatar, modifiedPhoto, 30, 30)
						binding.userUpdateBtn.isEnabled = isReadyForUpdate
					}
			}
		}
	}

	private fun setBitmapFromFile(view: ImageView?, file: File?, width: Int, height: Int) {
		val setBitmap: Bitmap
		val bitmap = loadBitmap(file, width, height)
		if (bitmap != null) {
			setBitmap = if (width > height) {
				val aMatrix = Matrix()
				aMatrix.setRotate(90.0f)
				Bitmap.createBitmap(bitmap, 0, 0, width, height, aMatrix, false)
			} else {
				bitmap
			}
			view!!.setImageBitmap(setBitmap)
		}
	}

	// Switches between register and signup layout
	private fun switchLayout() {
		when (mRequestCode) {
			REQUEST_SOCIAL_CONFIG_FIRST -> {
				binding.userModifyName.setHoverDescription(getString(R.string.blank_name_L012))
				binding.passwordChangeCurrent.visibility = View.GONE
				binding.passwordChangeNew.setBackgroundResource(R.drawable.row_kanojo_edit_bg_top)
                binding.userUpdateBtn.isEnabled = true
				binding.userUpdateBtn.setText(R.string.user_register_btn)
				binding.userDeleteBtn.visibility = View.GONE
				binding.userModifyLogout.visibility = View.GONE
			}
			REQUEST_SOCIAL_CONFIG_SETTING -> {
				binding.userModifyName.setHoverDescription(getString(R.string.blank_name))
				imageJob = mScope.launch {
					mDic.loadBitmap(binding.userModifyIcon.avatar, user!!.profile_image_url, R.drawable.common_noimage, null)
				}
                binding.userDeleteBtn.visibility = View.VISIBLE
				binding.userUpdateBtn.setText(R.string.edit_account_update_btn)
			}
		}
	}

	private fun showConfirmDeleteDialog(message: String?) {
		val dialog = AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(R.string.common_dialog_ok) { dialog, which ->
					executeOptionDeleteTask()
				}
				.setNegativeButton(R.string.common_dialog_cancel) { dialog, which ->
					bindEvent()
					dialog.dismiss()
				}
				.create()
		dialog.setCanceledOnTouchOutside(false)
		dialog.setOnDismissListener { bindEvent() }
		dialog.show()
	}

	private fun executeOptionDeleteTask() {
		binding.loadingView.show()

		var response: Response<BarcodeKanojoModel?>? = null
		var mReason: Exception? = null

		val barcodeKanojo = app.barcodeKanojo
		val user = barcodeKanojo.user!!
		val settings = barcodeKanojo.settings
		accountJob = ioScope.launch {
			try {
				response = barcodeKanojo.android_delete_account(user.id)
			} catch (e: Exception) {
				mReason = e
				mReason!!.printStackTrace()
			}
			withContext(Dispatchers.Main){
				binding.loadingView.dismiss()
				try {
					if (response == null) {
						throw BarcodeKanojoException("""response is null! \n${mReason}""".trimIndent())
					}
					getCodeAndShowAlert(response, mReason) { _, codeIn ->
						when (codeIn) {
							Response.CODE_SUCCESS -> {
								logout()
								finish()
								startActivity(Intent().setClass(this@UserModifyActivity, LaunchActivity::class.java))
							}
							Response.CODE_ERROR_BAD_REQUEST, Response.CODE_ERROR_UNAUTHORIZED, Response.CODE_ERROR_FORBIDDEN, Response.CODE_ERROR_NOT_FOUND, Response.CODE_ERROR_SERVER, Response.CODE_ERROR_SERVICE_UNAVAILABLE -> {
								bindEvent()
							}
						}
					}
				} catch (e: BarcodeKanojoException) {
					binding.loadingView.dismiss()
					bindEvent()
					showNoticeDialog(getString(R.string.slow_network))
				}
			}
		}
	}

	private fun executeUserModifyTask() {
		//Email Validation
		if (binding.userModifyEmail.value.isEmpty()) {
			showNoticeDialog(r.getString(R.string.error_no_email))
			modifiedEmail = binding.userModifyEmail.value.replace(" ".toRegex(), "").lowercase()
			return
		}

		//Password validation
		if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST && binding.passwordChangeNew.value.isEmpty() && binding.passwordChangeReNew.value.isEmpty()){
			showNoticeDialog(resources.getString(R.string.error_no_password))
			return
		}
		if (binding.passwordChangeNew.value.isNotEmpty() || binding.passwordChangeReNew.value.isNotEmpty()) {
			if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING && binding.passwordChangeCurrent.value.isEmpty()) { //No Empty Current Password
				showNoticeDialog(resources.getString(R.string.error_no_old_password))
				return
			} else if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING && !checkCurrentPassword()) {                //Check Input Password against app stored
				showNoticeDialog(resources.getString(R.string.error_unmatch_current_password))
				return
			} else if (binding.passwordChangeNew.value.equals("")) {
				showNoticeDialog(resources.getString(R.string.error_no_new_password))
				return
			} else if (binding.passwordChangeNew.value.length < 6 || 16 < binding.passwordChangeNew.value.length) {
				showNoticeDialog(resources.getString(R.string.error_password_length))
				return
			} else if (!binding.passwordChangeNew.value.equals(binding.passwordChangeReNew.value)) {
				showNoticeDialog(resources.getString(R.string.error_unmatch_new_password))
				return
			} else {
				//We good, password change good to send
				mCurrentPassword = hashPassword(binding.passwordChangeCurrent.value, "")
				mNewPassword = hashPassword(binding.passwordChangeNew.value, "")
			}
		}

		//ProcessData()
		val modifiedUser = User()
		modifiedName = binding.userModifyName.value
		modifiedBirthday = binding.userModifyBirthday.value
		modifiedEmail = binding.userModifyEmail.value
		backupUser(modifiedUser)    //TODO Do we need this?
		if (mRequestCode != BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
			//Profile Update
			user?.let { mDic.evict(it.profile_image_url) }
			var mReason: Exception? = null
			var response: Response<BarcodeKanojoModel?>? = null
			accountJob = ioScope.launch {
				val tempUser = User()
				tempUser.setBirthFromText(modifiedBirthday!!)
				try {
					response = app.barcodeKanojo.update(modifiedName, mCurrentPassword, mNewPassword, modifiedEmail, tempUser.birth_year, tempUser.birth_month, tempUser.birth_day, modifiedGender, modifiedPhoto)
				} catch (e: Exception) {
					mReason = e
					mReason!!.printStackTrace()
				}
				withContext(Dispatchers.Main) {
					try {
						if (response == null) {
							throw BarcodeKanojoException("""response is null! \n${mReason}""".trimIndent())
						}
						getCodeAndShowAlert(response, mReason) { _, codeIn ->
							if (codeIn == Response.CODE_SUCCESS) {
								(application as BarcodeKanojoApp).barcodeKanojo.settings.setEmail(modifiedEmail!!)
								if (mNewPassword != null) {
									(application as BarcodeKanojoApp).barcodeKanojo.settings.setPassword(mNewPassword!!)
								}
								updateAndClose()
							} else {
								bindEvent()
							}
						}
					} catch (e: BarcodeKanojoException) {
						bindEvent()
						//clearQueue()
						showNoticeDialog(getString(R.string.slow_network))
					}
					binding.loadingView.dismiss()
				}
			}
		} else {
			//User Signup
			binding.loadingView.show()
			var mReason: Exception? = null
			var response: Response<BarcodeKanojoModel?>? = null
			accountJob = ioScope.launch {
				val tempUser = User()
				tempUser.setBirthFromText(modifiedBirthday!!)
				try {
					response = app.barcodeKanojo.signup(app.settings.getUUID(), modifiedName, mNewPassword!!, modifiedEmail!!, tempUser.birth_year, tempUser.birth_month, tempUser.birth_day, modifiedGender, modifiedPhoto)
				} catch (e: Exception) {
					mReason = e
					mReason!!.printStackTrace()
				}
				withContext(Dispatchers.Main) {
					try {
						val code = getCodeAndShowAlert(response, mReason)
						if (code == Response.CODE_SUCCESS) {
							(application as BarcodeKanojoApp).barcodeKanojo.settings.setEmail(modifiedEmail!!)
							if (mNewPassword != null) {
								(application as BarcodeKanojoApp).barcodeKanojo.settings.setPassword(mNewPassword!!)
							}
							updateAndClose()
						} else {
							bindEvent()
						}
					} catch (e: BarcodeKanojoException) {
						bindEvent()
						showNoticeDialog(getString(R.string.slow_network))
					}
					binding.loadingView.dismiss()
				}
			}
		}
	}

	private fun updateAndClose() {
		setResult(RESULT_MODIFIED)
		finish()
	}

	private fun logout() {
		app.barcodeKanojo.resetUser()
		app.barcodeKanojo.settings.logout()
		app.logged_out()
	}

	private val isReadyForUpdate: Boolean
		get() {
			if (binding.userModifyEmail.value.isEmpty()) {
				return false
			} else if (mRequestCode == REQUEST_SOCIAL_CONFIG_SETTING) {
				if (binding.userModifyEmail.value.isNotEmpty()) {
					return true
				}
				if (binding.userModifyName.value.isNotEmpty() && binding.userModifyName.value != user!!.name) {
					return true
				}
				if (binding.userModifyBirthday.value.isNotEmpty() && !binding.userModifyBirthday.value.equals(user!!.birthText, ignoreCase = true)) {
					return true
				}
				if (modifiedPhoto != null) {
					return true
				}
				if (binding.passwordChangeNew.value.isNotEmpty()) {
					return !(binding.passwordChangeCurrent.isEmpty || binding.passwordChangeNew.value != binding.passwordChangeReNew.value)
				}
			} else if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
				return true
			}
			return false
		}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (keyCode != KeyEvent.KEYCODE_BACK || !binding.loadingView.isShow) {
			return super.onKeyDown(keyCode, event)
		}
		binding.loadingView.setMessage(getString(R.string.requesting_cant_cancel))
		return true
	}

	override fun startCheckSession() {
		if (mRequestCode != BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
			super.startCheckSession()
			binding.loadingView.show()
		}
	}

	override fun endCheckSession() {
		binding.loadingView.dismiss()
	}

    private fun showDatePickDialog(title: String?, value: EditItemView) {
        val cal = Calendar.getInstance()
        val birthText = value.value
        var year = cal[1]
        var month = cal[2]
        var day = cal[5]
        if (!(birthText == null || birthText == "")) {
            try {
                val arr = birthText.split("\\.").toTypedArray()
                if (arr.size == 3) {
                    month = arr[0].toInt() - 1
                    day = arr[1].toInt()
                    year = arr[2].toInt()
                }
            } catch (e: Exception) {
            }
        }
        val dialog = DatePickerDialog(this, { view: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int -> value.value = String.format("%02d", monthOfYear + 1) + "." + String.format("%02d", dayOfMonth) + "." + String.format("%04d", year1) }, year, month, day)
        if (mListener != null) {
            dialog.setOnDismissListener(mListener)
        }
        dialog.show()
    }

	private fun checkCurrentPassword(): Boolean {
		val currentPassword = app.settings.getPassword()
		val inputCurrentPassword = hashPassword(binding.passwordChangeCurrent.value, currentPassword.mSalt)
		return currentPassword == inputCurrentPassword
	}

	companion object {
	}
}