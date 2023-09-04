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
import android.widget.Button
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
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView.EditItemViewCallback
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class UserModifyActivity : BaseEditActivity(), View.OnClickListener {
	private lateinit var app: BarcodeKanojoApp

	private lateinit var userModifyClose: Button
	private lateinit var userModifyLogout: Button
	private lateinit var userModifyName: EditItemView
	private lateinit var userModifyEmail: EditItemView
	private lateinit var userModifyBirthday: EditItemView
	private lateinit var userModifyIcon: EditItemView
	private lateinit var imgAvatar: ImageView
	private lateinit var passwordChangeCurrent: EditItemView
	private lateinit var passwordChangeNew: EditItemView
	private lateinit var passwordChangeReNew: EditItemView
	private lateinit var userUpdateBtn: Button
	private lateinit var userDeleteBtn: Button
	private lateinit var loadingView: CustomLoadingView

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

	private val mScope = MainScope()
	private var ioScope = CoroutineScope(Dispatchers.IO) + CoroutineName(TAG)

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(R.layout.activity_user_modify)

		userModifyClose = findViewById(R.id.user_modify_close)
		userModifyLogout = findViewById(R.id.user_modify_logout)
		userModifyName = findViewById(R.id.user_modify_name)
		userModifyEmail = findViewById(R.id.user_modify_email)
		userModifyBirthday = findViewById(R.id.user_modify_birthday)
		userModifyIcon = findViewById(R.id.user_modify_icon)
		imgAvatar = userModifyIcon.avatar
		passwordChangeCurrent = findViewById(R.id.password_change_current)
		passwordChangeNew = findViewById(R.id.password_change_new)
		passwordChangeReNew = findViewById(R.id.password_change_re_new)
		userUpdateBtn = findViewById(R.id.user_update_btn)
		userDeleteBtn = findViewById(R.id.user_delete_btn)
		loadingView = findViewById(R.id.loadingView)

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
			userModifyEmail.value = savedInstanceState.getString("email")
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
            userModifyName.value = user!!.name
		}

		//Email Field Setup
		if (userModifyEmail.value == "") {
			val email = app.barcodeKanojo.settings.getEmail()
			userModifyEmail.value = app.barcodeKanojo.settings.getEmail()
		}

		//Birthday Field Setup
        userModifyBirthday.value = user!!.birthText
		userModifyIcon.avatar.visibility = View.VISIBLE

		//Password Fields setup
		passwordChangeCurrent.hideText()
		passwordChangeNew.hideText()
		passwordChangeReNew.hideText()

		switchLayout()

		//Listener for determining if Update/Register Button can be pressed.
		mTextChangeListener = EditItemViewCallback { _, _ ->
            userUpdateBtn.isEnabled = isReadyForUpdate
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putInt(EXTRA_REQUEST_CODE, mRequestCode)
		user!!.name = userModifyName.value
		user!!.setBirthFromText(userModifyBirthday.value)
		outState.putParcelable("user", user)
		outState.putString("email", userModifyName.value)
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
		userModifyName.setTextChangeListner(null)
		userModifyEmail.setTextChangeListner(null)
		userModifyBirthday.setTextChangeListner(null)
		passwordChangeCurrent.setTextChangeListner(null)
		passwordChangeNew.setTextChangeListner(null)
		passwordChangeReNew.setTextChangeListner(null)
		mScope.cancel()
		loadingView.dismiss()
		super.onDestroy()
	}

	fun bindEvent() {
        userModifyClose.setOnClickListener(this)
		userModifyLogout.setOnClickListener(this)
        userModifyName.setOnClickListener(this)
        userModifyEmail.setOnClickListener(this)
        userModifyBirthday.setOnClickListener(this)
        userModifyIcon.setOnClickListener(this)
		passwordChangeCurrent.setOnClickListener(this)
		passwordChangeNew.setOnClickListener(this)
		passwordChangeReNew.setOnClickListener(this)
        userUpdateBtn.setOnClickListener(this)
		userDeleteBtn.setOnClickListener(this)

		userModifyName.setTextChangeListner(mTextChangeListener)
		userModifyEmail.setTextChangeListner(mTextChangeListener)
		userModifyBirthday.setTextChangeListner(mTextChangeListener)
		passwordChangeCurrent.setTextChangeListner(mTextChangeListener)
		passwordChangeNew.setTextChangeListner(mTextChangeListener)
		passwordChangeReNew.setTextChangeListner(mTextChangeListener)
	}

	private fun unBindEvent() {
        userModifyClose.setOnClickListener(null)
		userModifyLogout.setOnClickListener(null)
        userModifyName.setOnClickListener(null)
        userModifyEmail.setOnClickListener(null)
        userModifyBirthday.setOnClickListener(null)
        userModifyIcon.setOnClickListener(null)
		passwordChangeCurrent.setOnClickListener(null)
		passwordChangeNew.setOnClickListener(null)
		passwordChangeReNew.setOnClickListener(null)
        userUpdateBtn.setOnClickListener(null)
        userDeleteBtn.setOnClickListener(null)
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
			showEditTextDialog(r.getString(R.string.user_account_name), userModifyName)
		} else if (id == R.id.user_modify_birthday) {
			//TODO probably a good idea to add a warning or block to those under 13
			showDatePickDialog(r.getString(R.string.user_account_birthday), userModifyBirthday)
		} else if (id == R.id.user_modify_icon) {
			showImagePickerDialog(r.getString(R.string.user_account_icon))
		} else if (id == R.id.user_modify_email) {
			showEditTextDialog(r.getString(R.string.user_account_email), userModifyEmail)
		} else if (v.id == R.id.password_change_current) {
			val input = EditText(this)
			input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
			showEditTextDialog(resources.getString(R.string.password_change_current), passwordChangeCurrent, input)
		} else if (v.id == R.id.password_change_new) {
			val input = EditText(this)
			input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
			showEditTextDialog(resources.getString(R.string.password_change_password), passwordChangeNew, input)
		} else if (v.id == R.id.password_change_re_new) {
			val input = EditText(this)
			input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
			showEditTextDialog(resources.getString(R.string.password_change_re_password), passwordChangeReNew, input)
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
						setBitmapFromFile(userModifyIcon.avatar, modifiedPhoto, 30, 30)
						userUpdateBtn.isEnabled = isReadyForUpdate
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
				userModifyName.setHoverDescription(getString(R.string.blank_name_L012))
				passwordChangeCurrent.visibility = View.GONE
				passwordChangeNew.setBackgroundResource(R.drawable.row_kanojo_edit_bg_top)
                userUpdateBtn.isEnabled = true
				userUpdateBtn.setText(R.string.user_register_btn)
				userDeleteBtn.visibility = View.GONE
				userModifyLogout.visibility = View.GONE
			}
			REQUEST_SOCIAL_CONFIG_SETTING -> {
				userModifyName.setHoverDescription(getString(R.string.blank_name))
				imageJob = mScope.launch {
					mDic.loadBitmap(userModifyIcon.avatar, user!!.profile_image_url, R.drawable.common_noimage, null)
				}
                userDeleteBtn.visibility = View.VISIBLE
				userUpdateBtn.setText(R.string.edit_account_update_btn)
			}
			else -> return
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
		loadingView.show()

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
				loadingView.dismiss()
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
					loadingView.dismiss()
					bindEvent()
					showNoticeDialog(getString(R.string.slow_network))
				}
			}
		}
	}

	private fun executeUserModifyTask() {
		//Email Validation
		if (userModifyEmail.value.isEmpty()) {
			showNoticeDialog(r.getString(R.string.error_no_email))
			modifiedEmail = userModifyEmail.value.replace(" ".toRegex(), "").lowercase()
			return
		}

		//Password validation
		if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST && passwordChangeNew.value.isEmpty() && passwordChangeReNew.value.isEmpty()){
			showNoticeDialog(resources.getString(R.string.error_no_password))
			return
		}
		if (passwordChangeNew.value.isNotEmpty() || passwordChangeReNew.value.isNotEmpty()) {
			if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING && passwordChangeCurrent.value.isEmpty()) { //No Empty Current Password
				showNoticeDialog(resources.getString(R.string.error_no_old_password))
				return
			} else if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_SETTING && !checkCurrentPassword()) {                //Check Input Password against app stored
				showNoticeDialog(resources.getString(R.string.error_unmatch_current_password))
				return
			} else if (passwordChangeNew.value.equals("")) {
				showNoticeDialog(resources.getString(R.string.error_no_new_password))
				return
			} else if (passwordChangeNew.value.length < 6 || 16 < passwordChangeNew.value.length) {
				showNoticeDialog(resources.getString(R.string.error_password_length))
				return
			} else if (!passwordChangeNew.value.equals(passwordChangeReNew.value)) {
				showNoticeDialog(resources.getString(R.string.error_unmatch_new_password))
				return
			} else {
				//We good, password change good to send
				mCurrentPassword = hashPassword(passwordChangeCurrent.value, "")
				mNewPassword = hashPassword(passwordChangeNew.value, "")
			}
		}

		//ProcessData()
		val modifiedUser = User()
		modifiedName = userModifyName.value
		modifiedBirthday = userModifyBirthday.value
		modifiedEmail = userModifyEmail.value
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
					loadingView.dismiss()
				}
			}
		} else {
			//User Signup
			loadingView.show()
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
					loadingView.dismiss()
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
			if (userModifyEmail.value.isEmpty()) {
				return false
			} else if (mRequestCode == REQUEST_SOCIAL_CONFIG_SETTING) {
				if (userModifyEmail.value.isNotEmpty()) {
					return true
				}
				if (userModifyName.value.isNotEmpty() && userModifyName.value != user!!.name) {
					return true
				}
				if (userModifyBirthday.value.isNotEmpty() && !userModifyBirthday.value.equals(user!!.birthText, ignoreCase = true)) {
					return true
				}
				if (modifiedPhoto != null) {
					return true
				}
				if (passwordChangeNew.value.isNotEmpty()) {
					return !(passwordChangeCurrent.isEmpty || passwordChangeNew.value != passwordChangeReNew.value)
				}
			} else if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
				return true
			}
			return false
		}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (keyCode != KeyEvent.KEYCODE_BACK || !loadingView.isShow) {
			return super.onKeyDown(keyCode, event)
		}
		loadingView.setMessage(getString(R.string.requesting_cant_cancel))
		return true
	}

	override fun startCheckSession() {
		if (mRequestCode != BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
			super.startCheckSession()
			loadingView.show()
		}
	}

	override fun endCheckSession() {
		loadingView.dismiss()
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
		val inputCurrentPassword = hashPassword(passwordChangeCurrent.value, currentPassword.mSalt)
		return currentPassword == inputCurrentPassword
	}

	companion object {
		private const val TAG = "UserModifyActivity"
	}
}