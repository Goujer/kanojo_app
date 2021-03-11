package com.goujer.utils

import android.graphics.Bitmap
import android.util.Log

const val TAG = "BitmapUtil"

//Get Bitmap.ByteCount for API < 12
fun getByteCount(bitmap: Bitmap): Int {
	if (bitmap.isRecycled) {
		Log.w(TAG, "Called getByteCount() on a recycle()'d bitmap! "
				+ "This is undefined behavior!")
		return 0
	}
	// int result permits bitmaps up to 46,340 x 46,340
	// int result permits bitmaps up to 46,340 x 46,340
	return bitmap.rowBytes * bitmap.height
}