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
		if (this.mCommondialog == null) {
			this.mCommondialog = AlertDialog.Builder(this).setTitle(R.string.app_name).setIcon(R.drawable.icon_72).setMessage(message).setPositiveButton(R.string.common_dialog_ok, DialogInterface.OnClickListener { dialog, which -> }).create()
		}
		this.mCommondialog?.setMessage(message)
		this.mCommondialog?.setCanceledOnTouchOutside(false)
		this.mCommondialog?.show()
	}
}