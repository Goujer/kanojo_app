package com.goujer.barcodekanojo.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goujer.barcodekanojo.core.util.DynamicImageCache
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.adapter.base.BaseActivityModelAdapter
import jp.co.cybird.barcodekanojoForGAM.adapter.base.ObservableAdapter
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*

class DashboardAdapter(context: Context?, private val mDic: DynamicImageCache, observer: Observer?) : BaseActivityModelAdapter(context), ObservableAdapter {
	private val isFirst = false
	private val mHandler = Handler()
	private val mInflater = LayoutInflater.from(context)
	private var mListener: OnKanojoClickListener? = null
	private var mLoadedPhotoIndex = 0
	private val mNotifyThread = Runnable { superNotifyDataSetChanged() }
	private val mResourcesObserver = RemoteResourceManagerObserver(this, null as RemoteResourceManagerObserver?)
	private val mRunnableLoadPhotos: Runnable = object : Runnable {
		override fun run() {
			if (mLoadedPhotoIndex < this@DashboardAdapter.count) {
				val dashboardAdapter = this@DashboardAdapter
				val dashboardAdapter2 = this@DashboardAdapter
				val access0 = dashboardAdapter2.mLoadedPhotoIndex
				dashboardAdapter2.mLoadedPhotoIndex = access0 + 1
				val a = dashboardAdapter.getItem(access0) as ActivityModel
				mDic.requestBitmap(a.leftImgUrl)
				mDic.requestBitmap(a.rightImgUrl)
				mHandler.postDelayed(this, 200)
			}
		}
	}
	private val mScope = MainScope()

	fun destroy() {
		mScope.cancel()
	}

	interface OnKanojoClickListener {
		fun onKanojoClick(kanojo: Kanojo?)
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
		val holder: ViewHolder
		var view = convertView
		if (view == null) {
			view = mInflater.inflate(R.layout.row_activities, null)
			holder = ViewHolder()
			holder.imgLeft = view.findViewById(R.id.row_activities_left_img)
			holder.imgRight = view.findViewById(R.id.row_activities_right_img)
			holder.imgRightCover = view.findViewById(R.id.row_activities_right_img_cover)
			holder.txtActivity = view.findViewById(R.id.row_activities_txt)
			view.tag = holder
		} else {
			holder = view.tag as ViewHolder
			holder.reset()
		}
		val act = getItem(position) as ActivityModel
		if (act != null) {
			holder.leftImageJob = mScope.launch { mDic.loadBitmap(holder.imgLeft!!, act.leftImgUrl, R.drawable.common_noimage_product, null) }
			holder.rightImageJob = mScope.launch { mDic.loadBitmap(holder.imgRight!!, act.rightImgUrl, R.drawable.common_noimage_product, null) }
			if (holder.txtActivity != null) {
				holder.txtActivity!!.text = act.activity
			}
			val kanojo = act.kanojo
			holder.imgLeft!!.setOnClickListener(null)
			holder.imgRight!!.setOnClickListener(null)
			holder.imgRight!!.visibility = View.VISIBLE
			holder.imgRightCover!!.visibility = View.VISIBLE
			when (act.activity_type) {
				2, 9 -> holder.imgLeft!!.setOnClickListener {
					if (mListener != null) {
						mListener!!.onKanojoClick(kanojo)
					}
				}
				5, 7, 8, 10 -> holder.imgRight!!.setOnClickListener {
					if (mListener != null) {
						mListener!!.onKanojoClick(kanojo)
					}
				}
				11 -> {
					holder.imgRight!!.visibility = View.GONE
					holder.imgRightCover!!.visibility = View.GONE
				}
			}
		}
		return view
	}

	fun setOnKanojoClickListener(l: OnKanojoClickListener?) {
		mListener = l
	}

	override fun setModelList(l: ModelList<ActivityModel>) {
		super.setModelList(l)
		mHandler.removeCallbacks(mRunnableLoadPhotos)
		mHandler.postDelayed(mRunnableLoadPhotos, 100)
	}

	override fun addModelList(l: ModelList<ActivityModel>) {
		super.addModelList(l)
		for (a in l) {
			mDic.requestBitmap(a.leftImgUrl)
			mDic.requestBitmap(a.rightImgUrl)
		}
	}

	override fun removeObserver() {
		mHandler.removeCallbacks(mNotifyThread)
		mHandler.removeCallbacks(mRunnableLoadPhotos)
		mDic.deleteObserver(mResourcesObserver)
	}

	private inner class RemoteResourceManagerObserver private constructor() : Observer {
		/* synthetic */
		internal constructor(dashboardAdapter: DashboardAdapter?, remoteResourceManagerObserver: RemoteResourceManagerObserver?) : this() {}

		override fun update(observable: Observable, data: Any) {
			if (data != null) {
				mHandler.removeCallbacks(mNotifyThread)
				mHandler.postDelayed(mNotifyThread, 400)
			}
		}
	}

	override fun notifyDataSetChanged() {
		super.notifyDataSetChanged()
		mHandler.removeCallbacks(mNotifyThread)
		mHandler.postDelayed(mNotifyThread, 400)
	}

	fun superNotifyDataSetChanged() {
		if (isFirst) {
			super.notifyDataSetInvalidated()
		} else {
			super.notifyDataSetChanged()
		}
	}

	private class ViewHolder {
		var imgLeft: ImageView? = null
		var imgRight: ImageView? = null
		var imgRightCover: ImageView? = null
		var txtActivity: TextView? = null
		var rightImageJob: Job? = null
		var leftImageJob: Job? = null
		fun reset() {
			if (leftImageJob != null && !leftImageJob!!.isCompleted) {
				leftImageJob!!.cancel(null)
			}
			if (rightImageJob != null && !rightImageJob!!.isCompleted) {
				rightImageJob!!.cancel(null)
			}
		}
	}

	companion object {
		private const val TAG = "DashboardAdapter"
	}

	init {
		mDic.addObserver(mResourcesObserver)
	}
}