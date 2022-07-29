package com.goujer.barcodekanojo.activity.scan

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.goujer.barcodekanojo.activity.base.BaseKanojoEditActivity
import com.goujer.barcodekanojo.view.ProductAndKanojoView
import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.R
import com.goujer.barcodekanojo.core.model.Kanojo
import jp.co.cybird.barcodekanojoForGAM.core.model.Product
import jp.co.cybird.barcodekanojoForGAM.view.EditItemView

class ScanKanojoGenerateActivity : BaseKanojoEditActivity(), View.OnClickListener, DialogInterface.OnDismissListener {
	private lateinit var btnSave: Button
	private var isDetailByAmazon = false
	private lateinit var mCategoryName: EditItemView
	private lateinit var mComment: EditItemView
	private lateinit var mCompanyName: EditItemView
	private var mKanojo: Kanojo? = null
	private lateinit var mKanojoName: EditItemView
	private var mProduct: Product? = null
	private lateinit var mProductAndKanojo: ProductAndKanojoView
	private lateinit var mProductName: EditItemView

	//TODO make arrows include generated icons and perhaps barcode image.
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_scan_generate)
		findViewById<View>(R.id.edit_close).setOnClickListener(this)
		mProductAndKanojo = findViewById(R.id.scan_generate_photo)
		mKanojoName = findViewById(R.id.scan_generate_1_kanojo_name)
		mKanojoName.setOnClickListener(this)
		mCompanyName = findViewById(R.id.scan_generate_2_company_name)
		mCompanyName.setOnClickListener(this)
		mProductName = findViewById(R.id.scan_generate_3_product_name)
		mProductName.setOnClickListener(this)
		mCategoryName = findViewById(R.id.scan_generate_4_category)
		mCategoryName.setOnClickListener(this)
		val mBarcode: EditItemView = findViewById(R.id.scan_generate_5_barcode)
		mBarcode.setOnClickListener(this)
		findViewById<View>(R.id.scan_generate_6_photo).setOnClickListener(this)
		mComment = findViewById(R.id.scan_generate_7_comment)
		mComment.setOnClickListener(this)
		btnSave = findViewById(R.id.scan_generate_btn_save)
		btnSave.setOnClickListener(this)
		btnSave.setEnabled(false)
		val bundle = intent.extras
		mKanojo = bundle!![EXTRA_KANOJO] as Kanojo?
		mProduct = bundle[EXTRA_PRODUCT] as Product?
		isDetailByAmazon = bundle.getBoolean("isDetailByAmazon")
		if (mKanojo != null && mProduct != null) {
			mBarcode.value = mKanojo!!.barcode
			var leftimgurl: String? = null
			val dic = (application as BarcodeKanojoApp).imageCache
			if (isDetailByAmazon) {
				mKanojoName.value = mKanojo!!.name
				mCompanyName.value = mProduct!!.company_name
				mProductName.value = mProduct!!.name
				val category_id = checkCategoryID(mProduct!!.category)
				mProduct!!.category_id = category_id
				mCategoryName.value = mCategoryList[category_id - 1]
				leftimgurl = mProduct!!.product_image_url
				if (!mKanojoName.isEmpty && !mCompanyName.isEmpty && !mProductName.isEmpty) {
					btnSave.isEnabled = true
				}
			} else {
				val c = getDefaultCategory()
				if (c != null) {
					mCategoryName.value = c.name
					mProduct!!.category_id = c.id
					mProduct!!.category = c.name
				}
			}
			mProductAndKanojo.executeLoadImgTask(dic, leftimgurl!!, mKanojo!!)
		}
	}

	private fun checkCategoryID(mCategoryName2: String): Int {
		val id = listOf(*mCategoryList).indexOf(mCategoryName2)
		return if (id == -1) {
			mCategoryList.size
		} else id + 1
	}

	public override fun getClientView(): View {
		val layout = layoutInflater.inflate(R.layout.activity_scan_generate, null)
		val appLayoutRoot = LinearLayout(this)
		appLayoutRoot.addView(layout)
		return appLayoutRoot
	}

	override fun onResume() {
		super.onResume()
		(application as BarcodeKanojoApp).requestLocationUpdates(true)
	}

	override fun onPause() {
		(application as BarcodeKanojoApp).removeLocationUpdates()
		super.onPause()
	}

	override fun onDestroy() {
		mProductAndKanojo.destroy()
		super.onDestroy()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
		super.onActivityResult(requestCode, resultCode, data)
		val f = file
		if (f != null && f.exists()) {
			setBitmapFromFile(mProductAndKanojo, f)
		}
	}

	override fun onClick(v: View) {
		val id = v.id
		if (id == R.id.edit_close) {
			close()
		} else if (id == R.id.scan_generate_1_kanojo_name) {
			showEditTextDialog(r.getString(R.string.common_product_kanojo_name), mKanojoName)
		} else if (id == R.id.scan_generate_2_company_name) {
			showEditTextDialog(r.getString(R.string.common_product_company), mCompanyName)
		} else if (id == R.id.scan_generate_3_product_name) {
			showEditTextDialog(r.getString(R.string.common_product_name), mProductName)
		} else if (id == R.id.scan_generate_4_category) {
			showListDialog(r.getString(R.string.common_product_category), mProduct!!, mCategoryName!!)
		} else if (id == R.id.scan_generate_6_photo) {
			showImagePickerDialog(r.getString(R.string.common_product_photo))
		} else if (id == R.id.scan_generate_7_comment) {
			showEditTextDialog(r.getString(R.string.common_product_comment), mComment, 4)
		} else if (id == R.id.scan_generate_btn_save) {
			if (isDetailByAmazon && file == null) {
				val dic = (application as BarcodeKanojoApp).imageCache
				file = dic.getFile(mProduct!!.product_image_url)
			}
			executeInspectionAndGenerateTask(mKanojo!!.barcode, mCompanyName.value, mKanojoName.value, mProductName.value, mProduct!!.category_id, mComment.value, null, mKanojo)
		}
	}

	override fun onDismiss(dialog: DialogInterface, code: Int) {
		btnSave.isEnabled = !(mKanojoName.isEmpty || mCompanyName.isEmpty || mProductName.isEmpty)
	}

	companion object {
		private const val TAG = "ScanKanojoGenerateActivity"
	}
}