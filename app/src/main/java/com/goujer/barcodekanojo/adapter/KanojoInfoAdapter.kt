package com.goujer.barcodekanojo.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import com.goujer.barcodekanojo.R
import jp.co.cybird.barcodekanojoForGAM.adapter.base.BaseActivityModelAdapter
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*

class KanojoInfoAdapter(context: Context?, private val mDic: DynamicImageCache) : BaseActivityModelAdapter(context) {
	private val mHandler = Handler()
	private val mInflater = LayoutInflater.from(context)
	private val mNotifyThread = Runnable { notifyDataSetChanged() }
	private val mResourcesObserver = RemoteResourceManagerObserver(this, null as RemoteResourceManagerObserver?)

	fun removeObserver() {
		mHandler.removeCallbacks(mNotifyThread)
		//mDic.deleteObserver(mResourcesObserver)
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
		val holder: ViewHolder
		var view = convertView
		if (view == null) {
			view = mInflater.inflate(R.layout.row_kanojo_info_activities, null as ViewGroup?)
			holder = ViewHolder(null as ViewHolder?)
			holder.imgLeft = view.findViewById<View>(R.id.row_kanojo_info_activities_img) as ImageView
			holder.txtActivity = view.findViewById<View>(R.id.row_kanojo_info_activities_txt) as TextView
			view.tag = holder
		} else {
			holder = view.tag as ViewHolder
			holder.mJob?.cancel()
		}
		val act = getItem(position) as ActivityModel
		if (act != null) {
			val user = act.user
			if (user != null) {
				holder.mJob = mDic.loadBitmapASync(holder.imgLeft!!, user.profile_image_url, R.drawable.common_noimage)
			}
			if (holder.txtActivity != null) {
				holder.txtActivity!!.text = act.activity
			}
		}
		return view
	}

	private inner class RemoteResourceManagerObserver private constructor() : Observer {
		/* synthetic */
		internal constructor(kanojoInfoAdapter: KanojoInfoAdapter?, remoteResourceManagerObserver: RemoteResourceManagerObserver?) : this() {}

		@Deprecated("Deprecated in Java")
		override fun update(observable: Observable, data: Any) {
			if (data != null) {
				mHandler.removeCallbacks(mNotifyThread)
				mHandler.postDelayed(mNotifyThread, 400)
			}
		}
	}

	private class ViewHolder private constructor() {
		var imgLeft: ImageView? = null
		var txtActivity: TextView? = null
		var mJob: Job? = null

		/* synthetic */
		internal constructor(viewHolder: ViewHolder?) : this() {}
	}

	companion object {
		private const val TAG = "KanojoInfoAdapter"
	}

	//init {
	//	mDic.addObserver(mResourcesObserver)
	//}
}