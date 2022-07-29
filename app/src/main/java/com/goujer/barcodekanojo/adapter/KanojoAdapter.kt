package com.goujer.barcodekanojo.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import com.goujer.barcodekanojo.R
import jp.co.cybird.barcodekanojoForGAM.adapter.base.BaseKanojoPairAdapter
import jp.co.cybird.barcodekanojoForGAM.adapter.base.ObservableAdapter
import com.goujer.barcodekanojo.core.model.Kanojo
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoPair
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList
import com.goujer.barcodekanojo.view.KanojoView
import java.util.*

class KanojoAdapter(context: Context?, private val mDic: DynamicImageCache) : BaseKanojoPairAdapter(context), ObservableAdapter {
	private val mHandler = Handler()
	private val mInflater = LayoutInflater.from(context)
	private var mListener: OnKanojoClickListener? = null
	private var mLoadedPhotoIndex = 0
	private val mNotifyThread = Runnable { notifyDataSetChanged() }
	private val mResourcesObserver = RemoteResourceManagerObserver(this, null)
	private val mRunnableLoadPhotos: Runnable = object : Runnable {
		override fun run() {
			if (mLoadedPhotoIndex < this@KanojoAdapter.count) {
				val kanojoAdapter = this@KanojoAdapter
				val access = kanojoAdapter.mLoadedPhotoIndex
				kanojoAdapter.mLoadedPhotoIndex = access + 1
				mHandler.postDelayed(this, 200)
			}
		}
	}

	interface OnKanojoClickListener {
		fun onKanojoClick(kanojo: Kanojo?)
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
		val holder: ViewHolder
		var view = convertView
		if (view == null) {
			view = mInflater.inflate(R.layout.row_kanojos, null)
			holder = ViewHolder()
			holder.left = view.findViewById(R.id.row_kanojos_left)
			holder.right = view.findViewById(R.id.row_kanojos_right)
			view.tag = holder
		} else {
			holder = view.tag as ViewHolder
		}
		val kanojoPair = getItem(position) as KanojoPair
		holder.left!!.setKanojo(kanojoPair.left, mDic)
		holder.right!!.setKanojo(kanojoPair.right, mDic)
		holder.left!!.isSelected = false
		holder.left!!.isPressed = false
		holder.right!!.isSelected = false
		holder.right!!.isPressed = false
		holder.left!!.setOnClickListener { mListener!!.onKanojoClick(kanojoPair.left) }
		holder.right!!.setOnClickListener { mListener!!.onKanojoClick(kanojoPair.right) }
		return view
	}

	fun setOnKanojoClickListener(l: OnKanojoClickListener?) {
		mListener = l
	}

	override fun setModelList(l: ModelList<KanojoPair>) {
		super.setModelList(l)
	}

	fun setKanojosModelList(kanojos: ModelList<Kanojo>) {
		setModelList(KanojoToPair(kanojos))
	}

	fun addKanojosModelList(kanojos: ModelList<Kanojo>?) {
		if (kanojos != null) {
			addModelList(KanojoToPair(kanojos))
		}
	}

	private fun KanojoToPair(kanojos: ModelList<Kanojo>): ModelList<KanojoPair> {
		val pairList = ModelList<KanojoPair>()
		val size = kanojos.size
		var i = 0
		while (i < size) {
			if (i + 1 < size) {
				pairList.add(KanojoPair(kanojos[i], kanojos[i + 1]))
			} else {
				pairList.add(KanojoPair(kanojos[i], null))
			}
			i += 2
		}
		return pairList
	}

	override fun removeObserver() {
		mHandler.removeCallbacks(mNotifyThread)
		mHandler.removeCallbacks(mRunnableLoadPhotos)
		//mDic.deleteObserver(mResourcesObserver)
	}

	private inner class RemoteResourceManagerObserver private constructor() : Observer {
		/* synthetic */
		internal constructor(kanojoAdapter: KanojoAdapter?, remoteResourceManagerObserver: RemoteResourceManagerObserver?) : this() {}

		override fun update(observable: Observable, data: Any) {
			if (data != null) {
				mHandler.removeCallbacks(mNotifyThread)
				mHandler.postDelayed(mNotifyThread, 400)
			}
		}
	}

	private class ViewHolder {
		var left: KanojoView? = null
		var right: KanojoView? = null
	}

	companion object {
		private const val TAG = "KanojoAdapter"
	}

	//init {
	//	mDic.addObserver(mResourcesObserver)
	//}
}