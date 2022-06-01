package com.goujer.barcodekanojo.activity.setting

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.*
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.LinearLayout

import com.goujer.barcodekanojo.activity.top.LaunchActivity
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import com.goujer.barcodekanojo.preferences.ApplicationSetting

import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.core.Password
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity.OnDialogDismissListener
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface
import jp.co.cybird.barcodekanojoForGAM.activity.setting.ChangePasswordActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import com.goujer.barcodekanojo.core.model.User
import jp.co.cybird.barcodekanojoForGAM.databinding.ActivityUserModifyBinding
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView.EditItemViewCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class UserModifyActivity : BaseEditActivity(), View.OnClickListener {
	private lateinit var app: BarcodeKanojoApp
	private lateinit var imgAvatar: ImageView
	private var mAutoLoginTask: AutoLoginTask? = null
	private var mChangeDeviceLayout: LinearLayout? = null
	private var mListener2: OnDialogDismissListener? = null
	private var mLoadingView: CustomLoadingView? = null
	private var mRequestCode = 0
	private var mResultCode = 0
	private lateinit var mDic: DynamicImageCache
	private var imageJob: Job? = null
	val mTaskEndHandler: Handler = object : Handler() {
		override fun handleMessage(msg: Message) {
			executeAutoLoginTask(queue!!.poll())
		}
	}
	private var mTaskQueue: Queue<StatusHolder?>? = null
	private var mTextChangeListener: EditItemViewCallback? = null
	private var modifiedPhoto: File? = null
	private var modifiedUser: User? = null
	private var user: User? = null
	private var password: Password? = null
	private var currentPassword: Password? = null

    private lateinit var binding: ActivityUserModifyBinding
	private val mScope = MainScope()

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
        binding = ActivityUserModifyBinding.inflate(layoutInflater)
		requestWindowFeature(1)
		setContentView(binding.root)
		app = application as BarcodeKanojoApp
		val bundle2 = intent.extras
		if (bundle2 != null) {
			mRequestCode = bundle2.getInt(EXTRA_REQUEST_CODE, REQUEST_SOCIAL_CONFIG_SETTING)
			if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
				modifiedUser = bundle2.getParcelable("user")
				user = modifiedUser
				setAutoRefreshSession(false)
			}
		}
		if (savedInstanceState != null && user == null) {
			mRequestCode = savedInstanceState.getInt(EXTRA_REQUEST_CODE, REQUEST_SOCIAL_CONFIG_SETTING)
			modifiedUser = savedInstanceState.getParcelable("user")
			user = modifiedUser
		}
		if (user == null) {
			user = app.user
		}
		mDic = app.imageCache

        binding.kanojoUserUpdateBtn.isEnabled = true
		if (mRequestCode == 1103) {
            binding.kanojoUserUpdateBtn.setText(R.string.edit_account_update_btn)
		} else if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
            binding.kanojoUserUpdateBtn.setText(R.string.user_register_btn)
		}

		binding.kanojoUserModifyName.setTextChangeListner(mTextChangeListener)
		if (user!!.name != null && user!!.name != "null") {
            binding.kanojoUserModifyName.value = user!!.name
		} else if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
            binding.kanojoUserModifyName.setHoverDescription(getString(R.string.blank_name_L012))
		} else {
            binding.kanojoUserModifyName.setHoverDescription(getString(R.string.blank_name))
		}

		binding.kanojoUserModifyPassword.setTextChangeListner(mTextChangeListener)
        binding.kanojoUserModifyPassword.hideText()

		binding.kanojoUserModifyEmail.setTextChangeListner(mTextChangeListener)
        binding.kanojoUserModifyEmail.value = user!!.email

		binding.kanojoUserModifyGender.setTextChangeListner(mTextChangeListener)
		if (user!!.sex != null) {
            binding.kanojoUserModifyGender.value = user!!.getSexText(app.userGenderList)
		}

		binding.kanojoUserModifyBirthday.setTextChangeListner(mTextChangeListener)
        binding.kanojoUserModifyBirthday.value = user!!.birthText
		imgAvatar = binding.kanojoUserModifyIcon.avatar
		imgAvatar.visibility = View.VISIBLE
		mChangeDeviceLayout = findViewById(R.id.kanojo_user_account_device_layout)
		//TODO: This Imagejob may not need to be run during a registration as there will be no profile picture to get from the server.
		imageJob = mScope.launch { mDic.loadBitmap(imgAvatar, user!!.profile_image_url, R.drawable.common_noimage, null) }
		mListener2 = OnDialogDismissListener { dialogInterface, code ->
			if (code == 200) {
				deleteUser()
				logout()
				val signUp = Intent().setClass(this@UserModifyActivity, LaunchActivity::class.java)
				signUp.putExtra(EXTRA_REQUEST_CODE, REQUEST_SOCIAL_CONFIG_FIRST)
				this@UserModifyActivity.startActivity(signUp)
			}
		}
		mTextChangeListener = EditItemViewCallback { v, value ->
            binding.kanojoUserUpdateBtn.isEnabled = isReadyForUpdate
            binding.kanojoUserUpdateBtn.isEnabled = !(binding.kanojoUserModifyName.value.isEmpty() && binding.kanojoUserModifyEmail.value.isEmpty() && mRequestCode != BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST)
		}
		switchLayout()
		mLoadingView = findViewById(R.id.loadingView)
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putInt(EXTRA_REQUEST_CODE, mRequestCode)
		user!!.name = binding.kanojoUserModifyName.value
		user!!.setBirthFromText(binding.kanojoUserModifyBirthday.value)
		user!!.setSexFromText(binding.kanojoUserModifyGender.value)
		//if ((mRequestCode != BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST || user!!.profile_image_url == null) && file != null) {
		//    user.setProfile_image_url(file.absolutePath);
		//}
		outState.putParcelable("user", user)
		super.onSaveInstanceState(outState)
	}

	override fun onDismiss(dialog: DialogInterface, code: Int) {
		super.onDismiss(dialog, code)
		when (code) {
			200 -> updateAndClose()
			400 -> if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
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
		mScope.cancel()
		super.onDestroy()
	}

	fun bindEvent() {
        binding.kanojoUserModifyClose.setOnClickListener(this)
        binding.kanojoUserModifyName.setOnClickListener(this)
        binding.kanojoUserModifyPassword.setOnClickListener(this)
        binding.kanojoUserModifyEmail.setOnClickListener(this)
        binding.kanojoUserModifyGender.setOnClickListener(this)
        binding.kanojoUserModifyBirthday.setOnClickListener(this)
        binding.kanojoUserModifyIcon.setOnClickListener(this)
        binding.kanojoUserUpdateBtn.setOnClickListener(this)
		binding.kanojoUserDeleteBtn.setOnClickListener(this)
	}

	private fun unBindEvent() {
        binding.kanojoUserModifyClose.setOnClickListener(null)
        binding.kanojoUserModifyName.setOnClickListener(null)
        binding.kanojoUserModifyPassword.setOnClickListener(null)
        binding.kanojoUserModifyEmail.setOnClickListener(null)
        binding.kanojoUserModifyGender.setOnClickListener(null)
        binding.kanojoUserModifyBirthday.setOnClickListener(null)
        binding.kanojoUserModifyIcon.setOnClickListener(null)
        binding.kanojoUserUpdateBtn.setOnClickListener(null)
        binding.kanojoUserDeleteBtn.setOnClickListener(null)
	}

	override fun onClick(v: View) {
		unBindEvent()
		Log.d(TAG, "View Clicked: " + v.id)
		val id = v.id
		when (id) {
			R.id.kanojo_user_modify_close -> {
				close()
			}
			R.id.kanojo_user_modify_name -> {
				showEditTextDialog(r.getString(R.string.user_account_name), binding.kanojoUserModifyName)
			}
			R.id.kanojo_user_modify_gender -> {
				showGenderDialog(r.getString(R.string.user_account_gender), binding.kanojoUserModifyGender)
			}
			R.id.kanojo_user_modify_birthday -> {
				showDatePickDialog(r.getString(R.string.user_account_birthday), binding.kanojoUserModifyBirthday)
			}
			R.id.kanojo_user_modify_icon -> {
				showImagePickerDialog(r.getString(R.string.user_account_icon))
			}
			R.id.kanojo_user_modify_email -> {
				showEditTextDialog(r.getString(R.string.user_account_email), binding.kanojoUserModifyEmail)
			}
			R.id.kanojo_user_modify_password -> {
				startPasswordChangeActivity()
			}
			R.id.kanojo_user_update_btn -> {
				mResultCode = RESULT_MODIFIED
				if (binding.kanojoUserModifyName.value != "" || binding.kanojoUserModifyGender.value != "" || binding.kanojoUserModifyBirthday.value != "" || imgAvatar.drawable != null) {
					mResultCode = RESULT_MODIFIED_COMMON
				}
				if (binding.kanojoUserModifyEmail.value != "") {
					if (binding.kanojoUserModifyPassword.value == "" && user!!.email == null) {
						showNoticeDialog(r.getString(R.string.error_password_length))
						return
					} else if (mResultCode == RESULT_MODIFIED_COMMON) {
						mResultCode = RESULT_MODIFIED_COMMON
					} else {
						mResultCode = RESULT_MODIFIED_DEVICE
					}
				} else if (user!!.email == null && binding.kanojoUserModifyPassword.value != "") {
					showNoticeDialog(r.getString(R.string.error_no_email))
					return
				}
				processData()
			}
			R.id.kanojo_user_delete_btn -> {
				mResultCode = RESULT_DELETE_ACCOUNT
				showConfirmDeleteDialog(resources.getString(R.string.delete_account_warning_message))
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		val f = file
		if (f != null && f.exists()) {
			setBitmapFromFile(imgAvatar, f, 30, 30)
            binding.kanojoUserUpdateBtn.isEnabled = true
		}
		if (requestCode == BaseInterface.REQUEST_CHANGE_PASWORD && resultCode == BaseInterface.RESULT_CHANGED) {
            binding.kanojoUserModifyPassword.value = "********"
			password = data?.getParcelableExtra("new_password")
			currentPassword = data?.getParcelableExtra("current_Password")
			binding.kanojoUserUpdateBtn.isEnabled = true
		}
	}

	protected fun setBitmapFromFile(view: ImageView?, file: File?, width: Int, height: Int) {
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

	private fun startPasswordChangeActivity() {
		val intent = Intent().setClass(this, ChangePasswordActivity::class.java)
		if (user!!.email == null || !(application as BarcodeKanojoApp).barcodeKanojo.isUserLoggedIn) {
			intent.putExtra("new_email", true)
			intent.putExtra("current_password", Password.emptyPassword())
		} else {
			intent.putExtra("new_email", false)
			intent.putExtra("current_password", user!!.currentPassword)
		}
		startActivityForResult(intent, REQUEST_CHANGE_PASWORD)
	}

	private fun switchLayout() {
		when (mRequestCode) {
			REQUEST_SOCIAL_CONFIG_FIRST -> {
				mChangeDeviceLayout!!.visibility = View.GONE
                binding.kanojoUserDeleteBtn.visibility = View.GONE
                binding.kanojoUserUpdateBtn.isEnabled = true
				return
			}
			REQUEST_SOCIAL_CONFIG_SETTING -> {
				mChangeDeviceLayout!!.visibility = View.GONE
                binding.kanojoUserDeleteBtn.visibility = View.VISIBLE
				return
			}
			else -> return
		}
	}

	protected fun showConfirmDeleteDialog(message: String?) {
		val dialog = AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(message).setPositiveButton(R.string.common_dialog_ok) { dialog, which -> processData() }.setNegativeButton(R.string.common_dialog_cancel) { dialog, which ->
			bindEvent()
			dialog.dismiss()
		}.create()
		dialog.setCanceledOnTouchOutside(false)
		dialog.setOnDismissListener { bindEvent() }
		dialog.show()
	}

	private fun processData() {
		if (mResultCode == RESULT_DELETE_ACCOUNT) {
			executeOptionDeleteTask()
		} else if (mResultCode == RESULT_MODIFIED_ALL || mResultCode == RESULT_MODIFIED_COMMON || mResultCode == RESULT_MODIFIED_DEVICE || mResultCode == RESULT_MODIFIED) {
			modifiedUser = User()
			modifiedUser!!.name = binding.kanojoUserModifyName.value
			modifiedUser!!.password = password
			modifiedUser!!.email = binding.kanojoUserModifyEmail.value.replace(" ".toRegex(), "")
			modifiedUser!!.setSexFromText(binding.kanojoUserModifyGender.value, app!!.userGenderList)
			val birthday = binding.kanojoUserModifyBirthday.value
			if (birthday == "") {
				modifiedUser!!.setBirth(1, 1, 1990)
			} else {
				modifiedUser!!.setBirthFromText(binding.kanojoUserModifyBirthday.value)
			}
			backupUser(modifiedUser)
			if (mRequestCode != BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
				modifiedPhoto = file
				user?.let { mDic.evict(it.profile_image_url) }
			} /*else {
				modifiedPhoto = mDic.getFile(user!!.profile_image_url)
			}*/
			executeAutoLoginListTask()
		} else {
			updateAndClose()
		}
	}

	private fun executeOptionDeleteTask() {
		val mDeleteUserHolder = StatusHolder()
		mDeleteUserHolder.key = 8
		queue!!.offer(mDeleteUserHolder)
		mTaskEndHandler.sendEmptyMessage(0)
	}

	private fun updateAndClose() {
		setResult(RESULT_MODIFIED)
		finish()
	}

	private fun logout() {
		(application as BarcodeKanojoApp).logged_out()
	}

	private fun isLoading(status: StatusHolder?): Boolean {
		return status!!.loading
	}

	internal class StatusHolder {
		var key = 0
		var loading = false

		companion object {
			const val SIGNUP_TASK = 0
			const val SAVING_COMMON_INFO_TASK = 1
			const val SAVING_DEVICE_ACCOUNT_TASK = 2
			//public static final int REGISTER_TOKEN_TASK = 3;
			const val UPDATE_TASK = 7
			const val DELETE_USER_TASK = 8
			const val VERIFY_TASK = 9
		}
	}

	private val queue: Queue<StatusHolder?>?
		get() {
			if (mTaskQueue == null) {
				mTaskQueue = LinkedList<StatusHolder?>()
			}
			return mTaskQueue
		}

	@Synchronized
	private fun clearQueue() {
		queue!!.clear()
	}

	@get:Synchronized
	private val isQueueEmpty: Boolean
		private get() = mTaskQueue!!.isEmpty()

	//protected void startDashboard() {
	//    finish();
	//    startActivity(new Intent().setClass(this, KanojosActivity.class));
	//}

	@Synchronized
	private fun executeAutoLoginListTask() {
		clearQueue()
		val mSignUpHolder = StatusHolder()
		mSignUpHolder.key = StatusHolder.SIGNUP_TASK
		StatusHolder().key = StatusHolder.UPDATE_TASK
		//new StatusHolder().key = StatusHolder.REGISTER_TOKEN_TASK;
		val mSaveCommonInfoHolder = StatusHolder()
		mSaveCommonInfoHolder.key = StatusHolder.SAVING_COMMON_INFO_TASK
		val mSaveDeviceHolder = StatusHolder()
		mSaveDeviceHolder.key = StatusHolder.SAVING_DEVICE_ACCOUNT_TASK
		StatusHolder().key = StatusHolder.VERIFY_TASK
		if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
			queue!!.offer(mSignUpHolder)
		}
		if (mResultCode == BaseInterface.RESULT_MODIFIED_COMMON || mResultCode == BaseInterface.RESULT_MODIFIED_ALL) {
			queue!!.offer(mSaveCommonInfoHolder)
		}
		if (mResultCode == BaseInterface.RESULT_MODIFIED_DEVICE || mResultCode == BaseInterface.RESULT_MODIFIED_ALL) {
			queue!!.offer(mSaveDeviceHolder)
		}
		if (!isQueueEmpty) {
			mTaskEndHandler.sendEmptyMessage(0)
		} else {
			updateAndClose()
		}
	}

	private fun executeAutoLoginTask(list: StatusHolder?) {
		if (isLoading(list)) {
			Log.d(TAG, "task " + list!!.key + " is running ")
			return
		}
		mAutoLoginTask = AutoLoginTask()
		mAutoLoginTask!!.setList(list)
		showProgressDialog()
		mAutoLoginTask!!.execute()
	}

	internal inner class AutoLoginTask : AsyncTask<Void?, Void?, Response<*>?>() {
		private var mList: StatusHolder? = null
		private var mReason: Exception? = null
		fun setList(list: StatusHolder?) {
			mList = list
		}

		public override fun onPreExecute() {
			mList!!.loading = true
		}

		override fun doInBackground(vararg params: Void?): Response<*>? {
			return try {
				val barcodeKanojo = (this@UserModifyActivity.application as BarcodeKanojoApp).barcodeKanojo
				val user = barcodeKanojo.user
				val setting = ApplicationSetting(this@UserModifyActivity)
				if (mList == null) {
					throw BarcodeKanojoException("process:StatusHolder is null!")
				}
				var cPassword = currentPassword
				when (mList!!.key) {
					StatusHolder.SIGNUP_TASK -> barcodeKanojo.signup((application as BarcodeKanojoApp).uUID, modifiedUser!!.name, modifiedUser!!.password, modifiedUser!!.email, modifiedUser!!.birth_year, modifiedUser!!.birth_month, modifiedUser!!.birth_day, modifiedUser!!.sex, modifiedPhoto)
					StatusHolder.SAVING_COMMON_INFO_TASK -> {
						if (modifiedUser!!.password != null && (modifiedUser!!.password?.hashedPassword ?: "") == "") {
							modifiedUser!!.password = user.password
						}
						if (cPassword == null) {
							cPassword = user.password
						}
						barcodeKanojo.update(modifiedUser!!.name, cPassword, modifiedUser!!.password, modifiedUser!!.email, modifiedUser!!.birth_year, modifiedUser!!.birth_month, modifiedUser!!.birth_day, modifiedUser!!.sex, modifiedPhoto)
					}
					StatusHolder.SAVING_DEVICE_ACCOUNT_TASK -> barcodeKanojo.verify(modifiedUser!!.email, modifiedUser!!.password, (this@UserModifyActivity.application as BarcodeKanojoApp).uUID)
					StatusHolder.UPDATE_TASK -> {
						if (cPassword == null) {
							cPassword = user.password
						}
						barcodeKanojo.update(modifiedUser!!.name, cPassword, modifiedUser!!.password, modifiedUser!!.email, modifiedUser!!.birth_year, modifiedUser!!.birth_month, modifiedUser!!.birth_day, modifiedUser!!.sex, modifiedPhoto)
					}
					StatusHolder.DELETE_USER_TASK -> barcodeKanojo.android_delete_account(user.id)
					StatusHolder.VERIFY_TASK -> barcodeKanojo.verify("", null, (application as BarcodeKanojoApp).uUID)
					else -> null
				}
			} catch (e: Exception) {
				mReason = e
				null
			}
		}

		/* JADX INFO: finally extract failed */
		public override fun onPostExecute(response: Response<*>?) {
			val code: Int
			try {
				if (mReason != null) {
					mReason!!.printStackTrace()
				}
				if (response == null) {
					throw BarcodeKanojoException("""response is null! \n${mReason}""".trimIndent())
				}
				code = if (mList!!.key == StatusHolder.DELETE_USER_TASK) {
						getCodeAndShowAlert(response, mReason, mListener2)
					} else {
						getCodeAndShowAlert(response, mReason)
					}
				when (code) {
					Response.CODE_SUCCESS -> {
						if (mList!!.key == StatusHolder.SIGNUP_TASK) {
							(application as BarcodeKanojoApp).barcodeKanojo.user = response[User::class.java] as User
						}
						if (mList!!.key == StatusHolder.DELETE_USER_TASK) {
							finish()
							startActivity(Intent().setClass(this@UserModifyActivity, LaunchActivity::class.java))
						}
						if (!isQueueEmpty) {
							mTaskEndHandler.sendEmptyMessage(0)
						}
					}
					Response.CODE_ERROR_BAD_REQUEST, Response.CODE_ERROR_UNAUTHORIZED, Response.CODE_ERROR_FORBIDDEN, Response.CODE_ERROR_NOT_FOUND, Response.CODE_ERROR_SERVER, Response.CODE_ERROR_SERVICE_UNAVAILABLE -> {
						dismissProgressDialog()
						bindEvent()
						clearQueue()
					}
				}
				dismissProgressDialog()
			} catch (e: BarcodeKanojoException) {
				dismissProgressDialog()
				bindEvent()
				clearQueue()
				if (mAutoLoginTask != null) {
					mAutoLoginTask!!.cancel(true)
					mAutoLoginTask = null
				}
				this@UserModifyActivity.showNoticeDialog(this@UserModifyActivity.getString(R.string.slow_network))
				dismissProgressDialog()
			} catch (th: Throwable) {
				dismissProgressDialog()
				throw th
			}
		}

		override fun onCancelled() {
			dismissProgressDialog()
			bindEvent()
		}
	}

	//    void nextScreen(StatusHolder list) {
	//        setResult(BaseInterface.RESULT_MODIFIED);
	//        close();
	//        dismissProgressDialog();
	//    }

	private val isReadyForUpdate: Boolean
		get() {
			var mCount = 0
			if (binding.kanojoUserModifyName.value != "" && binding.kanojoUserModifyName.value.equals(user!!.name, ignoreCase = true)) {
				mCount++
			}
			if (binding.kanojoUserModifyGender.value != "" && binding.kanojoUserModifyGender.value.equals(user!!.sex, ignoreCase = true)) {
				mCount++
			}
			if (binding.kanojoUserModifyBirthday.value != "" && binding.kanojoUserModifyBirthday.value.equals(user!!.birthText, ignoreCase = true)) {
				mCount++
			}
			if (imgAvatar.drawable != null) {
				mCount++
			}
			if (binding.kanojoUserModifyEmail.value != "" && binding.kanojoUserModifyEmail.value.equals(user!!.email, ignoreCase = true) && binding.kanojoUserModifyPassword.value != "" && password == user!!.password) {
				mCount++
			}
			if (mRequestCode == BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
				mCount++
			}
			return mCount > 0
		}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (keyCode != KeyEvent.KEYCODE_BACK || !mLoadingView!!.isShow) {
			return super.onKeyDown(keyCode, event)
		}
		mLoadingView!!.setMessage(getString(R.string.requesting_cant_cancel))
		return true
	}

	public override fun showProgressDialog(): ProgressDialog {
		mLoadingView!!.show()
		return ProgressDialog(this)
	}

	override fun dismissProgressDialog() {
		if (mLoadingView != null) {
			mLoadingView!!.dismiss()
		}
	}

	override fun startCheckSession() {
		if (mRequestCode != BaseInterface.REQUEST_SOCIAL_CONFIG_FIRST) {
			super.startCheckSession()
			showProgressDialog()
		}
	}

	override fun endCheckSession() {
		dismissProgressDialog()
	}

    private fun showGenderDialog(title: String?, value: EditItemView) {
        val genderList = resources.getStringArray(R.array.user_account_gender_list)
        var selected = -1
        if (genderList != null) {
            val size = genderList.size
            for (i in 0 until size) {
                if (genderList[i] == value.value) {
                    selected = i
                }
            }
        }
        val dialog = AlertDialog.Builder(this).setTitle(title).setSingleChoiceItems(genderList, selected) { dialog, position -> value.value = genderList[position] }.setPositiveButton(R.string.common_dialog_ok) { dialog, which -> }.create()
        if (this.mListener != null) {
            dialog.setOnDismissListener(this.mListener)
        }
        dialog.show()
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
            } catch (e: java.lang.Exception) {
            }
        }
        val dialog = DatePickerDialog(this, { view: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int -> value.value = String.format("%02d", monthOfYear + 1) + "." + String.format("%02d", dayOfMonth) + "." + String.format("%04d", year1) }, year, month, day)
        if (mListener != null) {
            dialog.setOnDismissListener(mListener)
        }
        dialog.show()
    }

	companion object {
		//TODO Rewrite all of this?
		private const val TAG = "UserModifyActivity"
	}
}