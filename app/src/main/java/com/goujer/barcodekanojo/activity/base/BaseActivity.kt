package com.goujer.barcodekanojo.activity.base

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import jp.co.cybird.barcodekanojoForGAM.R

open class BaseActivity: Activity() {
	private var mCommondialog: AlertDialog? = null

	override fun onPause() {
		super.onPause()
		if (mCommondialog != null) {
			mCommondialog!!.dismiss()
		}
	}

	protected fun showNoticeDialog(message: String?) {
		showNoticeDialog(message, null)
	}

	protected fun showNoticeDialog(message: String?, listener: DialogInterface.OnDismissListener?) {
		if (mCommondialog == null) {
			mCommondialog = AlertDialog.Builder(this)
					.setTitle(R.string.app_name)
					.setIcon(R.drawable.icon_72)
					.setMessage(message)
					.setPositiveButton(R.string.common_dialog_ok) { dialog, which -> }.create()
		}
		mCommondialog?.setMessage(message)
		mCommondialog?.setCanceledOnTouchOutside(false)
		if (listener != null) {
			mCommondialog?.setOnDismissListener(listener)
		}
		mCommondialog?.show()
	}
}