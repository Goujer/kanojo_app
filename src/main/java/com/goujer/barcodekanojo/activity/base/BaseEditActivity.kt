package com.goujer.barcodekanojo.activity.base

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.EditText
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView

open class BaseEditActivity : BaseActivity() {
	protected fun showEditTextDialog(title: String?, value: EditItemView, edit: EditText) {
		val dialog: AlertDialog = AlertDialog.Builder(this).setTitle(title).setView(edit).setPositiveButton(R.string.common_dialog_ok, DialogInterface.OnClickListener { dialog, which -> value.value = edit.text.toString() }).setNegativeButton(R.string.common_dialog_cancel, DialogInterface.OnClickListener { dialog, which -> }).create()
		dialog.show()
	}
}