package com.goujer.barcodekanojo.adapter

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Gallery
import android.widget.ImageView
import com.goujer.barcodekanojo.core.util.DynamicImageCache
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.adapter.base.ObservableAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*

class KanojoInfoImgAdapter(private val context: Context, private val mDic: DynamicImageCache) : BaseAdapter(), ObservableAdapter {
	private val galleryIconSize = context.resources.getDimension(R.dimen.kanojo_info_gallery_height).toInt()
	private var imgUrls: MutableList<String> = ArrayList<String>()
	private val mHandler = Handler()
	private val mNotifyThread = Runnable { notifyDataSetChanged() }
	private val mResourcesObserver = RemoteResourceManagerObserver(this, null as RemoteResourceManagerObserver?)
	private val mScope = MainScope()

	//fun setImgUrls(l: MutableList<String>) {
	//	imgUrls = l
	//	for (url in l) {
	//		mDic.requestBitmap(url)
	//	}
	//	notifyDataSetChanged()
	//	mHandler.removeCallbacks(mNotifyThread)
	//	mHandler.postDelayed(mNotifyThread, 200)
	//}

	fun addImgUrl(l: String) {
		imgUrls.add(l)
		notifyDataSetChanged()

		mDic.requestBitmap(l)
		mHandler.removeCallbacks(mNotifyThread)
		mHandler.postDelayed(mNotifyThread, 200)
	}

	fun destroy() {
		imgUrls.clear()
		mScope.cancel()
	}

	override fun getCount(): Int {
		return imgUrls.size
	}

	override fun getItem(position: Int): Any {
		return imgUrls[position]
	}

	override fun hasStableIds(): Boolean {
		return true
	}

	fun getItemUrl(position: Int): String {
		return imgUrls[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getView(position: Int, view: View?, parent: ViewGroup): View? {
		val imageView: ImageView
		if (view == null) {
			imageView = ImageView(context)
			imageView.scaleType = ImageView.ScaleType.FIT_CENTER
			imageView.layoutParams = Gallery.LayoutParams(galleryIconSize, galleryIconSize)
		} else {
			imageView = view as ImageView
		}
		mScope.launch { mDic.loadBitmap(imageView, imgUrls[position], R.drawable.common_noimage_product, null) }
		return imageView
	}

	override fun removeObserver() {
		mHandler.removeCallbacks(mNotifyThread)
		mDic.deleteObserver(mResourcesObserver)
	}

	private inner class RemoteResourceManagerObserver private constructor() : Observer {
		/* synthetic */
		internal constructor(kanojoInfoImgAdapter: KanojoInfoImgAdapter?, remoteResourceManagerObserver: RemoteResourceManagerObserver?) : this() {}

		override fun update(observable: Observable, data: Any) {
			if (data != null) {
				mHandler.removeCallbacks(mNotifyThread)
				mHandler.postDelayed(mNotifyThread, 300)
			}
		}
	}

	companion object {
		private const val TAG = "KanojoInfoImgAdapter"
	}

	init {
		context.obtainStyledAttributes(R.styleable.KanojoInfoGallery).recycle()
		mDic.addObserver(mResourcesObserver)
	}
}