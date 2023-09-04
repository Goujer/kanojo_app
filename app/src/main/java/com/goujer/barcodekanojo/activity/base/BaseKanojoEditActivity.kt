package com.goujer.barcodekanojo.activity.base

import android.app.AlertDialog
import android.os.Bundle
import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.R
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseEditActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.Category
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList
import jp.co.cybird.barcodekanojoForGAM.core.model.Product
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView
import java.io.IOException

open class BaseKanojoEditActivity : BaseEditActivity() {
	private lateinit var mCategories: ModelList<Category>
	protected lateinit var mCategoryList: Array<String?>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		try {
			mCategories = (application as BarcodeKanojoApp).barcodeKanojo.categoryList
			val size: Int = mCategories.size
			mCategoryList = arrayOfNulls(size)
			for (i in 0 until size) {
				mCategoryList[i] = mCategories[i].name
			}
		} catch (e: BarcodeKanojoException) {
			Thread {
				try {
					val barcodeKanojo = (application as BarcodeKanojoApp).barcodeKanojo
					barcodeKanojo.init_product_category_list()
					mCategories = barcodeKanojo.categoryList
					val size: Int = mCategories.size
					mCategoryList = arrayOfNulls(size)
					var i = 0
					while (i < size) {
						mCategoryList[i] = mCategories[i].name
						i++
					}
				} catch (e: IOException) {
				} catch (e: BarcodeKanojoException) {
				}
			}.start()
		}
	}

	protected fun getDefaultCategory(): Category? {
		return if (mCategories.size == 0) {
			null
		} else mCategories[0]
	}

	protected fun showListDialog(title: String?, product: Product, value: EditItemView) {
		var selected = 0
		val size: Int = mCategories.size
		for (i in 0 until size) {
			if (mCategories[i].id == product.category_id) {
				selected = i
			}
		}
		val dialog: AlertDialog = AlertDialog.Builder(this).setTitle(title).setSingleChoiceItems(mCategoryList, selected) { dialog, position ->
			product.category = mCategoryList[position]
			product.category_id = mCategories[position].id
			value.value = mCategories[position].name
		}.setPositiveButton(R.string.common_dialog_ok) { dialog, which -> }.create()
		if (mListener != null) {
			dialog.setOnDismissListener(mListener)
		}
		dialog.show()
	}
}