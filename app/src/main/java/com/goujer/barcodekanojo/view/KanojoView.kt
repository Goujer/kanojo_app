package com.goujer.barcodekanojo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import jp.co.cybird.barcodekanojoForGAM.R
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo
import kotlinx.coroutines.Job

class KanojoView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
	private val barLoveGauge: ProgressBar
	private val imgBarCover: ImageView
	private val imgKanojo: ImageView
	private val imgKanojoCover: ImageView
	private val imgRate: ImageView
	private val txtName: TextView
	private val viewCover: View
	private var mJob: Job? = null
	private var kId: Int = -1

//	fun destroy() {
//		imgKanojo.setImageDrawable(null)
//		imgKanojo.setBackgroundDrawable(null)
//		imgKanojoCover.setImageDrawable(null)
//		imgKanojoCover.setBackgroundDrawable(null)
//		imgRate.setImageDrawable(null)
//		imgRate.setBackgroundDrawable(null)
//		imgBarCover.setImageDrawable(null)
//		imgBarCover.setBackgroundDrawable(null)
//		barLoveGauge.progressDrawable = null
//		barLoveGauge.setBackgroundDrawable(null)
//		txtName.setBackgroundDrawable(null)
//		viewCover.setBackgroundDrawable(null)
//		setBackgroundDrawable(null)
//	}

	override fun setPressed(pressed: Boolean) {
		super.setPressed(pressed)
		imgKanojoCover.isPressed = pressed
		imgBarCover.isPressed = pressed
	}

	override fun setSelected(pressed: Boolean) {
		super.setSelected(pressed)
		imgKanojoCover.isSelected = pressed
		imgBarCover.isSelected = pressed
	}

	fun setKanojo(kanojo: Kanojo?, dic: DynamicImageCache) {
		if (kanojo != null) {
			viewCover.visibility = GONE
			if (kId != kanojo.id) {
				if (mJob != null && !mJob!!.isCompleted) {
					mJob!!.cancel(null)
				}
				imgKanojo.setImageResource(R.drawable.common_noimage)
				mJob = dic.loadBitmapASync(imgKanojo, kanojo.profile_image_icon_url, R.drawable.common_noimage)
				kId = kanojo.id
			} else {
				Log.d(TAG, "Match")
			}
			txtName.text = kanojo.name
			val lovegauge = kanojo.love_gauge
			barLoveGauge.progress = 0
			val bounds = barLoveGauge.progressDrawable.bounds
			if (lovegauge <= 30) {
				barLoveGauge.progressDrawable = resources.getDrawable(R.drawable.secondary_progress_red)
			} else {
				barLoveGauge.progressDrawable = resources.getDrawable(R.drawable.secondary_progress_blue)
			}
			barLoveGauge.progressDrawable.bounds = bounds
			barLoveGauge.progress = lovegauge
			setRateImg(imgRate, kanojo.like_rate)
			if (kanojo.mascotEnable == 1) {
				setBackgroundResource(R.drawable.row_kanojos_permanent_bg)
			} else {
				setBackgroundResource(R.drawable.row_kanojos_bg)
			}
		} else {
			viewCover.visibility = VISIBLE
		}
	}

	private fun setRateImg(img: ImageView?, rate: Int) {
		if (img != null) {
			when (rate) {
				1 -> {
					img.setImageResource(R.drawable.row_kanojos_star1)
					return
				}
				2 -> {
					img.setImageResource(R.drawable.row_kanojos_star2)
					return
				}
				3 -> {
					img.setImageResource(R.drawable.row_kanojos_star3)
					return
				}
				4 -> {
					img.setImageResource(R.drawable.row_kanojos_star4)
					return
				}
				5 -> {
					img.setImageResource(R.drawable.row_kanojos_star5)
					return
				}
				else -> img.setImageResource(R.drawable.row_kanojos_star0)
			}
		}
	}

	init {
		gravity = 16
		setBackgroundResource(R.drawable.row_kanojos_bg)
		LayoutInflater.from(context).inflate(R.layout.view_kanojo, this, true)
		barLoveGauge = findViewById(R.id.view_kanojo_bar)
		imgBarCover = findViewById(R.id.view_kanojo_bar_cover)
		imgKanojo = findViewById(R.id.view_kanojo_img)
		imgKanojoCover = findViewById(R.id.view_kanojo_img_cover)
		imgRate = findViewById(R.id.view_kanojo_rate)
		txtName = findViewById(R.id.view_kanojo_name)
		viewCover = findViewById(R.id.view_kanojo_cover)
	}

	companion object {
		const val TAG = "KanojoView"
	}
}