package com.goujer.barcodekanojo.activity.kanojo

import android.content.Intent
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Gallery
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.R
import com.goujer.barcodekanojo.adapter.KanojoInfoAdapter
import com.goujer.barcodekanojo.adapter.KanojoInfoImgAdapter
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import com.goujer.barcodekanojo.core.model.Kanojo
import jp.co.cybird.barcodekanojoForGAM.Defs
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity
import jp.co.cybird.barcodekanojoForGAM.activity.kanojo.KanojoEditActivity
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList
import jp.co.cybird.barcodekanojoForGAM.core.model.Product
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.core.util.HttpUtil
import jp.co.cybird.barcodekanojoForGAM.view.MoreBtnView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class KanojoInfoActivity : BaseActivity(), View.OnClickListener, MoreBtnView.OnMoreClickListener {
	private var btnClose: Button? = null
	private var btnEdit: Button? = null
	private var mActivities: ModelList<ActivityModel>? = null
	private var mActivityCount = 0
	private var mAdapter: KanojoInfoAdapter? = null
	private var mFooter: MoreBtnView? = null
	private var mGallery: Gallery? = null
	private var mImgAdapter: KanojoInfoImgAdapter? = null
	private var mKanojo: Kanojo? = null
	private var mKanojoInfoTask: KanojoInfoTask? = null
	private var mLimit = DEFAULT_LIMIT
	private var mListView: ListView? = null
	private var mMessage: String? = null
	private var mProduct: Product? = null
	private var mProductImg: ImageView? = null
	private lateinit var mDic: DynamicImageCache
	private var r: Resources? = null
	private var readAllFlg = false
	private val mScope = MainScope()
	private var mJob: Job? = null

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_kanojo_info)
		r = resources
		mDic = (application as BarcodeKanojoApp).imageCache
		btnClose = findViewById(R.id.kanojo_info_close)
		btnClose?.setOnClickListener(this)

		btnEdit = findViewById(R.id.kanojo_info_edit)
		btnEdit?.setOnClickListener(this)

		//List View
		mListView = findViewById(R.id.kanojo_info_list)
		mFooter = MoreBtnView(this)
		mFooter!!.setOnMoreClickListener(MORE_ACTIVITIES, this)
		mAdapter = KanojoInfoAdapter(this, mDic)
		mListView?.addFooterView(mFooter)
		mListView?.adapter = mAdapter

		mActivities = ModelList()
		val bundle = intent.extras
		mKanojo = bundle!![EXTRA_KANOJO] as Kanojo?
		mProduct = bundle[EXTRA_PRODUCT] as Product?
		mMessage = bundle.getString(MessageModel.NOTIFY_AMENDMENT_INFORMATION)
		if (mKanojo != null && mProduct != null) {
			if (mKanojo!!.relation_status == Kanojo.RELATION_OTHER) {
				btnEdit?.visibility = View.GONE
				btnEdit?.isEnabled = false
			}
			initProductView()
			initGalleryView()
			mJob = mScope.launch { mDic.loadBitmap(mProductImg!!, mProduct!!.product_image_url, R.drawable.common_noimage_product, null) }
			val strUrl = mProduct!!.product_image_url
			if (HttpUtil.isUrl(strUrl)) {
				mImgAdapter!!.addImgUrl(strUrl)
			}
			executeKanojoInfoTask()
		}
	}

	public override fun getClientView(): View {
		val layout = layoutInflater.inflate(R.layout.activity_kanojo_info, null)
		val appLayoutRoot = LinearLayout(this)
		appLayoutRoot.addView(layout)
		return appLayoutRoot
	}

	override fun onResume() {
		super.onResume()
		bindEvent()
	}

	override fun onPause() {
		if (mKanojoInfoTask != null) {
			mKanojoInfoTask!!.cancel(true)
			mKanojoInfoTask = null
		}

		if (isFinishing) {
			mAdapter!!.removeObserver()
			if (mImgAdapter != null) {
				mImgAdapter!!.removeObserver()
			}
		}
		super.onPause()
	}

	override fun onStop() {
		mJob?.cancel()
		super.onStop()
	}

	override fun onDestroy() {
		mScope.cancel()
		mImgAdapter!!.destroy()
		mImgAdapter!!.removeObserver()
		mImgAdapter = null
		mAdapter!!.removeObserver()
		mAdapter!!.clear()
		mAdapter = null
		super.onDestroy()
	}

	private fun bindEvent() {
		btnClose!!.setOnClickListener(this)
		btnEdit!!.setOnClickListener(this)
	}

	private fun unBindEvent() {
		btnClose!!.setOnClickListener(null)
		btnEdit!!.setOnClickListener(null)
	}

	override fun onClick(v: View) {
		val id = v.id
		if (id == R.id.kanojo_info_close) {
			close()
		} else if (id == R.id.kanojo_info_edit) {
			unBindEvent()
			val intent = Intent(this, KanojoEditActivity::class.java)
			if (mKanojo != null) {
				intent.putExtra(EXTRA_KANOJO, mKanojo)
			}
			if (mProduct != null) {
				intent.putExtra(EXTRA_PRODUCT, mProduct)
			}
			if (mMessage != null) {
				intent.putExtra(MessageModel.NOTIFY_AMENDMENT_INFORMATION, mMessage)
			}
			startActivityForResult(intent, REQUEST_KANOJO_EDIT)
		}
	}

	override fun onMoreClick(id: Int) {
		if (id == MORE_ACTIVITIES) {
			executeKanojoInfoTask()
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == 101) {
			if (requestCode == REQUEST_KANOJO_EDIT) {
				setResult(101, data)
				close()
			}
		}
	}

	private fun initProductView() {
		val txtName = findViewById<TextView>(R.id.kanojo_info_name)
		if (txtName != null) {
			txtName.text = mProduct!!.name
		}
		val txtComapnyName = findViewById<TextView>(R.id.kanojo_info_company_name)
		if (txtComapnyName != null) {
			txtComapnyName.text = mProduct!!.company_name
		}
		val txtCountry = findViewById<TextView>(R.id.kanojo_info_country)
		if (txtCountry != null) {
			txtCountry.text = r!!.getString(R.string.kanojo_info_country) + mProduct!!.country
		}
		val txtBarcode = findViewById<TextView>(R.id.kanojo_info_barcode)
		if (txtBarcode != null) {
			txtBarcode.text = r!!.getString(R.string.kanojo_info_barcode) + "************"
		}
		val txtCategory = findViewById<TextView>(R.id.kanojo_info_category)
		if (txtCategory != null) {
			txtCategory.text = r!!.getString(R.string.kanojo_info_category) + mProduct!!.category
		}
		val txtScanned = findViewById<TextView>(R.id.kanojo_info_scanned)
		if (txtScanned != null) {
			txtScanned.text = r!!.getString(R.string.kanojo_info_scanned) + mProduct!!.scan_count
		}
	}

	private fun initGalleryView() {
		mProductImg = findViewById(R.id.kanojo_info_product_img)
		mGallery = Gallery(this)
		val disp = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
		mGallery!!.setSpacing(10)
		val width = disp.width
		val h = r!!.getDimension(R.dimen.kanojo_info_gallery_height).toInt()
		val p = r!!.getDimension(R.dimen.kanojo_info_padding).toInt()
		val d = r!!.getDimension(R.dimen.kanojo_info_gallery_left_margin).toInt()
		val w = width - p * 2
		(findViewById<View>(R.id.kanojo_info_layout_gallery) as FrameLayout).addView(mGallery, w * 2, h)
		mGallery!!.scrollTo(w - d, 0)
		mImgAdapter = KanojoInfoImgAdapter(this, mDic)
		mGallery!!.adapter = mImgAdapter
		mGallery!!.onItemSelectedListener = object : OnItemSelectedListener {
			override fun onItemSelected(adapterView: AdapterView<*>?, parent: View, position: Int, id: Long) {
				mJob = mScope.launch { mDic.loadBitmap(mProductImg!!, mImgAdapter!!.getItemUrl(position), R.drawable.common_noimage_product) }
			}

			override fun onNothingSelected(adapterView: AdapterView<*>?) {}
		}
	}

	private fun updateListItem() {
		mAdapter!!.setModelList(mActivities!!)
		if (readAllFlg) {
			mListView!!.removeFooterView(mFooter)
		}
		mAdapter!!.notifyDataSetChanged()
	}

	private fun executeKanojoInfoTask() {
		if (!readAllFlg) {
			if (mKanojoInfoTask == null || mKanojoInfoTask!!.status == AsyncTask.Status.FINISHED) {
				mLimit = DEFAULT_LIMIT
				mKanojoInfoTask = KanojoInfoTask(this).execute(*arrayOfNulls<Void>(0)) as KanojoInfoTask
			}
		}
	}

	class KanojoInfoTask(activity: KanojoInfoActivity) : AsyncTask<Void?, Void?, Response<*>?>() {
		private val activityRef: WeakReference<KanojoInfoActivity> = WeakReference(activity)
		private var mReason: Exception? = null
		@Deprecated("Deprecated in Java")
		public override fun onPreExecute() {
			val activity = activityRef.get();
			if (activity == null || activity.isFinishing) {
				return
			}

			activity.mFooter!!.setLoading(true)
		}

		@Deprecated("Deprecated in Java")
		override fun doInBackground(vararg params: Void?): Response<*>? {
			val activity = activityRef.get();
			if (activity == null || activity.isFinishing) {
				return null
			}

			return try {
				return (activity.application as BarcodeKanojoApp).barcodeKanojo.kanojo_timeline(activity.mKanojo!!.id, activity.mActivityCount, activity.mLimit)
			} catch (e: Exception) {
				mReason = e
				null
			}
		}

		@Deprecated("Deprecated in Java")
		public override fun onPostExecute(response: Response<*>?) {
			val activity = activityRef.get();
			if (activity == null || activity.isFinishing) {
				return
			}

			try {
				when (activity.getCodeAndShowAlert(response, mReason)) {
					Response.CODE_SUCCESS -> {
						val temp = response!!.activityModelList
						if (temp != null) {
							val size = temp.size
							if (size != 0) {
								if (size < activity.mLimit) {
									activity.readAllFlg = true
								}

								activity.mActivityCount += size
								activity.mActivities!!.addAll(temp)
								activity.addImgToGallery(temp)
							} else {
								activity.readAllFlg = true
							}
						} else {
							activity.readAllFlg = true
						}
						activity.updateListItem()
					}
				}
			} catch (e: BarcodeKanojoException) {
				if (Defs.DEBUG) e.printStackTrace()
			} finally {
				activity.mFooter!!.setLoading(false)
			}
		}

		@Deprecated("Deprecated in Java")
		override fun onCancelled() {
			val activity = activityRef.get();
			if (activity == null || activity.isFinishing) {
				return
			}

			activity.mFooter!!.setLoading(false)
		}
	}

	private fun addImgToGallery(l: ModelList<ActivityModel>) {
		for (activityModel in l) {
			val strUrl = activityModel.product.product_image_url
			if (strUrl != null && HttpUtil.isUrl(strUrl)) {
				mImgAdapter!!.addImgUrl(strUrl)
			}
		}
	}
	//public static final String md5(String s) {
	//    try {
	//        MessageDigest digest = MessageDigest.getInstance(Digest.MD5);
	//        digest.update(s.getBytes());
	//        byte[] messageDigest = digest.digest();
	//        StringBuilder hexString = new StringBuilder();
	//        for (byte b : messageDigest) {
	//            String h = Integer.toHexString(b & 255);
	//            while (h.length() < 2) {
	//                h = GreeDefs.BARCODE + h;
	//            }
	//            hexString.append(h);
	//        }
	//        return hexString.toString();
	//    } catch (NoSuchAlgorithmException e) {
	//        Log.v(TAG, e.toString());
	//        return "";
	//    }
	//}
	companion object {
		private const val DEFAULT_LIMIT = 6
		private const val MORE_ACTIVITIES = 11
	}
}