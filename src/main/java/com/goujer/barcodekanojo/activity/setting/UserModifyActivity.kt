package com.goujer.barcodekanojo.activity.setting

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.goujer.barcodekanojo.activity.setting.UserModifyActivity
import com.goujer.barcodekanojo.activity.top.LaunchActivity
import com.goujer.barcodekanojo.core.util.DynamicImageCache
import com.goujer.barcodekanojo.preferences.ApplicationSetting
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity.OnDialogDismissListener
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.activity.setting.ChangePasswordActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.core.model.User
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView.EditItemViewCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

//import com.google.android.gcm.GCMRegistrar;
class UserModifyActivity : BaseEditActivity(), View.OnClickListener {
	private var app: BarcodeKanojoApp? = null
	private lateinit var btnClose: Button
	private lateinit var btnDelete: Button
	private lateinit var btnSave: Button
	private var currentPassword: ByteArray? = null
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
	private lateinit var txtBirthday: EditItemView
	private lateinit var txtEmail: EditItemView
	private lateinit var txtGender: EditItemView
	private lateinit var txtIcon: EditItemView
	private lateinit var txtName: EditItemView
	private lateinit var txtPassword: EditItemView
	private var user: User? = null
	private var password: ByteArray? = null

	private val mScope = MainScope()

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWindowFeature(1)
		setContentView(R.layout.activity_user_modify)
		app = application as BarcodeKanojoApp
		val bundle2 = intent.extras
		if (bundle2 != null) {
			mRequestCode = bundle2.getInt(EXTRA_REQUEST_CODE, REQUEST_SOCIAL_CONFIG_SETTING)
			if (mRequestCode == 1102) {
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
			user = app!!.user
		}
		mDic = app!!.imageCache
		btnClose = findViewById(R.id.kanojo_user_modify_close)
		btnClose.setOnClickListener(this)
		btnSave = findViewById(R.id.kanojo_user_update_btn)
		btnSave.setOnClickListener(this)
		btnSave.isEnabled = true
		if (mRequestCode == 1103) {
			btnSave.setText(R.string.edit_account_update_btn)
		} else if (mRequestCode == 1102) {
			btnSave.setText(R.string.user_register_btn)
		}
		txtName = findViewById(R.id.kanojo_user_modify_name)
		txtName.setOnClickListener(this)
		txtName.setTextChangeListner(mTextChangeListener)
		if (user!!.name != null && user!!.name != "null") {
			txtName.value = user!!.name
		} else if (mRequestCode == 1102) {
			txtName.setHoverDescription(getString(R.string.blank_name_L012))
		} else {
			txtName.setHoverDescription(getString(R.string.blank_name))
		}
		txtPassword = findViewById(R.id.kanojo_user_modify_password)
		txtPassword.setOnClickListener(this)
		txtPassword.setTextChangeListner(mTextChangeListener)
		txtPassword.hideText()
		txtEmail = findViewById(R.id.kanojo_user_modify_email)
		txtEmail.setOnClickListener(this)
		txtEmail.setTextChangeListner(mTextChangeListener)
		txtEmail.value = user!!.email
		txtGender = findViewById(R.id.kanojo_user_modify_gender)
		txtGender.setOnClickListener(this)
		txtGender.setTextChangeListner(mTextChangeListener)
		if (user!!.sex != null) {
			txtGender.value = user!!.getSexText(app!!.userGenderList)
		}
		txtBirthday = findViewById(R.id.kanojo_user_modify_birthday)
		txtBirthday.setOnClickListener(this)
		txtBirthday.setTextChangeListner(mTextChangeListener)
		txtBirthday.value = user!!.birthText
		txtIcon = findViewById(R.id.kanojo_user_modify_icon)
		txtIcon.setOnClickListener(this)
		btnDelete = findViewById(R.id.kanojo_user_delete_btn)
		btnDelete.setOnClickListener(this)
		imgAvatar = txtIcon.avatar
		imgAvatar.visibility = View.VISIBLE
		mChangeDeviceLayout = findViewById(R.id.kanojo_user_account_device_layout)
		if (user!!.profile_image_url != null) {
			imageJob = mScope.launch { mDic.loadBitmap(imgAvatar, user!!.profile_image_url, R.drawable.common_noimage, null) }
		}
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
			btnSave.isEnabled = isReadyForUpdate
			btnSave.isEnabled = !(txtName.value.isEmpty() && txtEmail.value.isEmpty() && mRequestCode != 1102)
		}
		switchLayout()
		mLoadingView = findViewById(R.id.loadingView)
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putInt(EXTRA_REQUEST_CODE, mRequestCode)
		user!!.name = txtName.value
		user!!.setBirthFromText(txtBirthday.value)
		user!!.setSexFromText(txtGender.value)
		//if ((mRequestCode != 1102 || user!!.profile_image_url == null) && file != null) {
		//    user.setProfile_image_url(file.absolutePath);
		//}
		outState.putParcelable("user", user)
		super.onSaveInstanceState(outState)
	}

	override fun onDismiss(dialog: DialogInterface, code: Int) {
		super.onDismiss(dialog, code)
		when (code) {
			200 -> updateAndClose()
			400 -> if (mRequestCode == 1102) {
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
		btnClose.setOnClickListener(this)
		txtName.setOnClickListener(this)
		txtPassword.setOnClickListener(this)
		txtEmail.setOnClickListener(this)
		txtGender.setOnClickListener(this)
		txtBirthday.setOnClickListener(this)
		txtIcon.setOnClickListener(this)
		btnSave.setOnClickListener(this)
		btnDelete.setOnClickListener(this)
	}

	private fun unBindEvent() {
		btnClose.setOnClickListener(null)
		txtName.setOnClickListener(null)
		txtPassword.setOnClickListener(null)
		txtEmail.setOnClickListener(null)
		txtGender.setOnClickListener(null)
		txtBirthday.setOnClickListener(null)
		txtIcon.setOnClickListener(null)
		btnSave.setOnClickListener(null)
		btnDelete.setOnClickListener(null)
	}

	override fun onClick(v: View) {
		unBindEvent()
		Log.d(TAG, "View Clicked: " + v.id)
		val id = v.id
		if (id == R.id.kanojo_user_modify_close) {
			close()
		} else if (id == R.id.kanojo_user_modify_name) {
			showEditTextDialog(r.getString(R.string.user_account_name), txtName)
		} else if (id == R.id.kanojo_user_modify_gender) {
			showGenderDialog(r.getString(R.string.user_account_gender), txtGender)
		} else if (id == R.id.kanojo_user_modify_birthday) {
			showDatePickDialog(r.getString(R.string.user_account_birthday), txtBirthday)
		} else if (id == R.id.kanojo_user_modify_icon) {
			showImagePickerDialog(r.getString(R.string.user_account_icon))
		} else if (id == R.id.kanojo_user_modify_email) {
			showEditTextDialog(r.getString(R.string.user_account_email), txtEmail)
		} else if (id == R.id.kanojo_user_modify_password) {
			startPasswordChangeActivity()
		} else if (id == R.id.kanojo_user_update_btn) {
			mResultCode = RESULT_MODIFIED
			if (txtName.value != "" || txtGender.value != "" || txtBirthday.value != "" || imgAvatar.drawable != null) {
				mResultCode = RESULT_MODIFIED_COMMON
			}
			if (txtEmail.value != "") {
				if (txtPassword.value == "" && user!!.email == null) {
					showNoticeDialog(r.getString(R.string.error_password_length))
					return
				} else if (mResultCode == RESULT_MODIFIED_COMMON) {
					mResultCode = RESULT_MODIFIED_COMMON
				} else {
					mResultCode = RESULT_MODIFIED_DEVICE
				}
			} else if (user!!.email == null && txtPassword.value != "") {
				showNoticeDialog(r.getString(R.string.error_no_email))
				return
			}
			processData()
		} else if (id == R.id.kanojo_user_delete_btn) {
			mResultCode = RESULT_DELETE_ACCOUNT
			showConfirmDeleteDialog(resources.getString(R.string.delete_account_warning_message))
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		val f = file
		if (f != null && f.exists()) {
			setBitmapFromFile(imgAvatar, f, 30, 30)
			btnSave.isEnabled = true
		}
		if (requestCode == 807 && resultCode == 109) {
			txtPassword.value = "********"
			password = data?.getByteArrayExtra("new_password")
			currentPassword = data?.getByteArrayExtra("currentPassword")
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
			intent.putExtra("encodedCurrentPassword", ByteArray(0))
		} else {
			intent.putExtra("new_email", false)
			intent.putExtra("encodedCurrentPassword", user!!.currentPassword)
		}
		startActivityForResult(intent, REQUEST_CHANGE_PASWORD)
	}

	private fun switchLayout() {
		when (mRequestCode) {
			REQUEST_SOCIAL_CONFIG_FIRST -> {
				mChangeDeviceLayout!!.visibility = View.GONE
				btnDelete.visibility = View.GONE
				btnSave.isEnabled = true
				return
			}
			REQUEST_SOCIAL_CONFIG_SETTING -> {
				mChangeDeviceLayout!!.visibility = View.GONE
				btnDelete.visibility = View.VISIBLE
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

	fun processData() {
		if (mResultCode == 111) {
			executeOptionDeleteTask()
		} else if (mResultCode == 212 || mResultCode == 210 || mResultCode == 211 || mResultCode == 108) {
			modifiedUser = User()
			modifiedUser!!.name = txtName.value
			modifiedUser!!.password = password
			modifiedUser!!.email = txtEmail.value.replace(" ".toRegex(), "")
			modifiedUser!!.setSexFromText(txtGender.value, app!!.userGenderList)
			val birthday = txtBirthday.value
			if (birthday == "") {
				modifiedUser!!.setBirth(1, 1, 1990)
			} else {
				modifiedUser!!.setBirthFromText(txtBirthday.value)
			}
			backupUser(modifiedUser)
			if (mRequestCode != 1102 || user!!.profile_image_url == null) {
				modifiedPhoto = file
			} else {
				modifiedPhoto = mDic.getFile(user!!.profile_image_url)
			}
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

	protected fun updateAndClose() {
		setResult(RESULT_MODIFIED)
		close()
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
			const val DELETE_USER_TASK = 8
			const val REGISTER_FB_TASK = 4
			const val REGISTER_SUKIYA_TASK = 6

			//public static final int REGISTER_TOKEN_TASK = 3;
			const val REGISTER_TWITTER_TASK = 5
			const val SAVING_COMMON_INFO_TASK = 1
			const val SAVING_DEVICE_ACCOUNT_TASK = 2
			const val SIGNUP_TASK = 0
			const val UPDATE_TASK = 7
			const val VERIFY_TASK = 9
		}
	}

	private val queue: Queue<StatusHolder?>?
		private get() {
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
		mSignUpHolder.key = 0
		StatusHolder().key = 7
		//new StatusHolder().key = StatusHolder.REGISTER_TOKEN_TASK;
		val mSaveCommonInfoHolder = StatusHolder()
		mSaveCommonInfoHolder.key = 1
		val mSaveDeviceHolder = StatusHolder()
		mSaveDeviceHolder.key = 2
		StatusHolder().key = 9
		if (mRequestCode == 1102) {
			queue!!.offer(mSignUpHolder)
		}
		if (mResultCode == 210 || mResultCode == 212) {
			queue!!.offer(mSaveCommonInfoHolder)
		}
		if (mResultCode == 211 || mResultCode == 212) {
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
				var cPassword: ByteArray? = currentPassword
				when (mList!!.key) {
					StatusHolder.SIGNUP_TASK -> barcodeKanojo.signup((application as BarcodeKanojoApp).uuid, modifiedUser!!.name, modifiedUser!!.password, modifiedUser!!.email, modifiedUser!!.birth_year, modifiedUser!!.birth_month, modifiedUser!!.birth_day, modifiedUser!!.sex, modifiedPhoto)
					StatusHolder.SAVING_COMMON_INFO_TASK -> {
						if (modifiedUser!!.password.size == 0) {
							modifiedUser!!.password = user.password
						}
						if (cPassword == null) {
							cPassword = user.password
						}
						barcodeKanojo.update(modifiedUser!!.name, cPassword, modifiedUser!!.password, modifiedUser!!.email, modifiedUser!!.birth_year, modifiedUser!!.birth_month, modifiedUser!!.birth_day, modifiedUser!!.sex, modifiedPhoto)
					}
					2 -> barcodeKanojo.android_uuid_verify(modifiedUser!!.email, modifiedUser!!.password, (this@UserModifyActivity.application as BarcodeKanojoApp).uuid)
					6 -> null
					StatusHolder.UPDATE_TASK -> {
						if (cPassword == null) {
							cPassword = user.password
						}
						barcodeKanojo.update(modifiedUser!!.name, cPassword, modifiedUser!!.password, modifiedUser!!.email, modifiedUser!!.birth_year, modifiedUser!!.birth_month, modifiedUser!!.birth_day, modifiedUser!!.sex, modifiedPhoto)
					}
					StatusHolder.DELETE_USER_TASK -> barcodeKanojo.android_delete_account(user.id)
					9 -> barcodeKanojo.verify("", ByteArray(0), (application as BarcodeKanojoApp).uuid)
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
				code = if (!isQueueEmpty) {
					response.code
				} else if (mList!!.key == StatusHolder.DELETE_USER_TASK) {
					this@UserModifyActivity.getCodeAndShowAlert(response, mReason, this@UserModifyActivity.mListener2)
				} else {
					this@UserModifyActivity.getCodeAndShowAlert(response, mReason)
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
					Response.CODE_ERROR_BAD_REQUEST, Response.CODE_ERROR_UNAUTHORIZED, 403, 404, 500, 503 -> {
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
	val isReadyForUpdate: Boolean
		get() {
			var mCount = 0
			if (txtName.value != "" && txtName.value.equals(user!!.name, ignoreCase = true)) {
				mCount++
			}
			if (txtGender.value != "" && txtGender.value.equals(user!!.sex, ignoreCase = true)) {
				mCount++
			}
			if (txtBirthday.value != "" && txtBirthday.value.equals(user!!.birthText, ignoreCase = true)) {
				mCount++
			}
			if (imgAvatar.drawable != null) {
				mCount++
			}
			if (txtEmail.value != "" && txtEmail.value.equals(user!!.email, ignoreCase = true) && txtPassword.value != "" && Arrays.equals(password, user!!.password)) {
				mCount++
			}
			if (mRequestCode == 1102) {
				mCount++
			}
			return mCount > 0
		}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (keyCode != 4 || !mLoadingView!!.isShow) {
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
		if (mRequestCode != 1102) {
			super.startCheckSession()
			showProgressDialog()
		}
	}

	override fun endCheckSession() {
		dismissProgressDialog()
	}

	companion object {
		//TODO Rewrite all of this?
		private const val TAG = "UserModifyActivity"
	}
}