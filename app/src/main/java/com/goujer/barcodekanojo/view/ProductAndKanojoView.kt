package com.goujer.barcodekanojo.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import jp.co.cybird.barcodekanojoForGAM.R
import com.goujer.barcodekanojo.core.model.Kanojo
import jp.co.cybird.barcodekanojoForGAM.core.util.Live2dUtil
import kotlinx.coroutines.*

class ProductAndKanojoView(private var mContext: Context?, attrs: AttributeSet?) : RelativeLayout(mContext, attrs) {
	private val imgLeft: ImageView
	private val imgRight: ImageView
	private val scope = MainScope()

	fun destroy() {
		scope.cancel()
		mContext = null
	}

	fun setBitmap(bitmap: Bitmap?) {
		if (imgLeft != null) {
			imgLeft.setImageBitmap(bitmap)
			invalidate()
		}
	}

	fun executeLoadImgTask(dic: DynamicImageCache, leftImgUrl: String, kanojo: Kanojo) {
		scope.launch {
			dic.loadBitmap(imgLeft!!, leftImgUrl, R.drawable.common_noimage_product, Runnable { invalidate() })
		}
		scope.launch(Dispatchers.Default) {
			val bitmap = Live2dUtil.createNormalIcon(mContext, kanojo)
			withContext(Dispatchers.Main) {
				imgRight.setImageBitmap(bitmap)
				invalidate()
			}
		}
	}

	companion object {
		private const val TAG = "ProductAndKanojoView"
	}

	init {
		gravity = 16
		setPadding(0, 8, 0, 8)
		LayoutInflater.from(mContext).inflate(R.layout.view_product_and_kanojo, this, true)
		imgLeft = findViewById(R.id.product_and_kanojo_left)
		imgRight = findViewById(R.id.product_and_kanojo_right)
	}
}