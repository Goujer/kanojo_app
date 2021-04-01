package com.goujer.barcodekanojo.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.ImageView
import com.goujer.android.support.util.LruCache
import com.goujer.barcodekanojo.core.http.HttpApi
import com.goujer.barcodekanojo.core.http.NameStringPair
import com.goujer.utils.encodeForUrl
import com.goujer.utils.getByteCount
import jp.co.cybird.barcodekanojoForGAM.Defs
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.util.BaseDiskCache
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.lang.Runnable
import java.util.*

class DynamicImageCache(cacheSize: Int, context: Context, httpApi: HttpApi): Observable() {

	private val memoryCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(cacheSize) {
		override fun sizeOf(key: String, bitmap: Bitmap): Int {
			// The cache size will be measured in kilobytes rather than
			// number of items.
			val sizeBytes: Int
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				sizeBytes = bitmap.allocationByteCount
			} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
				sizeBytes = bitmap.byteCount
			} else {
				sizeBytes = getByteCount(bitmap)
			}
			return sizeBytes / 1024
		}
	}
	private val diskCache = BaseDiskCache(context)
	private val mHttpApi = httpApi

	/* requestBitmap
	memorycache
	diskcache -> memorycache
	server -> diskcache -> memorycache */
	fun requestBitmap(key: String) {
		mScope.launch(Dispatchers.IO) {
			cacheMutex.withLock {
				if (memoryCache[key] == null) {
					if (diskCache.exists(key)) {
						memoryCache.put(key, BitmapFactory.decodeStream(diskCache.getInputStream(key)))
					} else {
						try {
							val connection = mHttpApi.createHttpGet(encodeForUrl(key))
							mHttpApi.executeHttpRequest(connection, diskCache, key)
							if (diskCache.exists(key)) {
								memoryCache.put(key, BitmapFactory.decodeStream(diskCache.getInputStream(key)))
							} else {
								throw BarcodeKanojoException("Image $key could not be downloaded")
							}
						} catch (e: BarcodeKanojoException) {
							Log.w(TAG, "Image $key could not be loaded")
							if (Defs.DEBUG) {
								e.printStackTrace()
							}
						}
					}
				}
			}
		}
	}

	/* loadBitmap
	sized memorycache -> return bitmap
	sized diskcache -> memorycache -> return bitmap
	full memorycache -> resize -> diskcache -> memorycache -> return bitmap
	full diskcache -> resize -> diskcache -> memorycache -> return bitmap
	sized server -> diskcache -> memorycache -> return bitmap */
	suspend fun loadBitmap(imageView: ImageView, key: String?, placeHolder: Int, endRunnable: Runnable? = null) {
		if (key == null) {
			endRunnable?.run()
		} else {
			//Setup
			val size = if (imageView.height > 0) { imageView.height.toString() } else ""
			val sizeKey = key + size
			//Initial Check
			var bitmap: Bitmap? = memoryCache[sizeKey]
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap)
				endRunnable?.run()
			} else {
				withContext(Dispatchers.IO) {
					cacheMutex.withLock {
						bitmap = memoryCache[sizeKey]
						if (bitmap == null) {
							when {
								diskCache.exists(sizeKey) -> {
									bitmap = BitmapFactory.decodeStream(diskCache.getInputStream(sizeKey))
									memoryCache.put(sizeKey, bitmap)
								}
								memoryCache[key] != null -> {
									bitmap = Bitmap.createScaledBitmap(memoryCache[key], imageView.height, imageView.height, true)
									val os = diskCache.getOutputStream(sizeKey)
									bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, os)
									os.close()
									memoryCache.put(sizeKey, bitmap)
								}
								diskCache.exists(key) -> {
									bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(diskCache.getInputStream(key)), imageView.height, imageView.height, true)
									val os = diskCache.getOutputStream(sizeKey)
									bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, os)
									os.close()
									memoryCache.put(sizeKey, bitmap)
								}
								else -> {
									try {
										val connection = mHttpApi.createHttpGet(encodeForUrl(key),
												NameStringPair("size", size))
										mHttpApi.executeHttpRequest(connection, diskCache, sizeKey)
										if (diskCache.exists(sizeKey)) {
											memoryCache.put(sizeKey, BitmapFactory.decodeStream(diskCache.getInputStream(sizeKey)))
											bitmap = memoryCache[sizeKey]
										} else {
											throw BarcodeKanojoException("Image $sizeKey could not be downloaded")
										}
									} catch (e: BarcodeKanojoException) {
										Log.e(TAG, "Image $sizeKey could not be loaded")
										if (Defs.DEBUG) {
											e.printStackTrace()
										}
									}
								}
							}
						}
					}
				}
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap)
				} else {
					imageView.setImageResource(placeHolder)
				}
				endRunnable?.run()
			}
		}
	}

	//Runs it in own coroutines, useful if class cannot cleany manage its own coroutines.
	fun loadBitmapASync(imageView: ImageView, key: String?, placeHolder: Int): Job {
		return mScope.launch { loadBitmap(imageView, key, placeHolder) }
	}

	fun getFile(filename: String): File {
		return diskCache.getReadOnlyFile(filename)
	}

	companion object {
		const val TAG = "DynamicImageCache"
		val cacheMutex = Mutex();
		val mScope = MainScope() + CoroutineName(TAG)
	}
}