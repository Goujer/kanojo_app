package com.goujer.barcodekanojo.core.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.util.LruCache
import android.util.Log
import android.widget.ImageView
import com.goujer.barcodekanojo.core.http.HttpApi
import com.goujer.utils.encodeForUrl
import jp.co.cybird.barcodekanojoForGAM.Defs
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.lang.Runnable

class DynamicImageCache(cacheSize: Int, context: Context): LruCache<String, Bitmap>(cacheSize) {

    private val diskCache = ImageDiskCache(context)

    //Cache Section
    override fun create(key: String): Bitmap? {
        if (diskCache.exists(key)) {
            return BitmapFactory.decodeStream(diskCache.getInputStream(key))
        } else {
            return try {
                val connection = HttpApi.get().createHttpGet(encodeForUrl(key))
                HttpApi.get().executeHttpRequest(connection, diskCache, key)
                if (diskCache.exists(key)) {
                    BitmapFactory.decodeStream(diskCache.getInputStream(key))
                } else {
                    throw BarcodeKanojoException("Image $key could not be downloaded")
                }
            } catch (e: BarcodeKanojoException) {
                Log.e(TAG, "Image $key could not be loaded")
                if (Defs.DEBUG) {
                    e.printStackTrace()
                }
                null
            }
        }
    }

    override fun sizeOf(key: String, bitmap: Bitmap): Int {
        //The cache size will be measured in kilobytes rather than number of items.
        val sizeBytes: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount
        } else {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
		        bitmap.byteCount
	        } else {
		        bitmap.rowBytes * bitmap.height
	        }
        }
        return sizeBytes / 1024
    }

	// Needed because it is possible for the memory cache to have key missing, thus skipping entryRemoved() and not allowing the diskCache.remove() in there.
	fun evict(key: String) {
		super.remove(key)
		diskCache.remove(key)
	}

    override fun entryRemoved(evicted: Boolean, key: String, oldValue: Bitmap, newValue: Bitmap?) {
        super.entryRemoved(evicted, key, oldValue, newValue)
        if (!evicted && newValue == null) {
            diskCache.remove(key)
        }
    }

    override fun trimToSize(maxSize: Int) {
        super.trimToSize(maxSize)

        if (maxSize == -1) {
            // Clear all of disk cache
            diskCache.evictAll()
        } else {
            // Trim DiskCache to it's correct size
            diskCache.trimToSize(diskCache.maxSize)
        }
    }

	fun memEvictAll() {
		super.trimToSize(-1)
		diskCache.trimToSize(diskCache.maxSize)
	}

    //Helper Section

	/* loadBitmap
	memorycache -> return bitmap
	diskcache -> memorycache -> return bitmap
	server -> diskcache -> memorycache -> return bitmap */
	suspend fun loadBitmap(imageView: ImageView, key: String?, placeHolder: Int, endRunnable: Runnable? = null) {
		if (key == null) {
			endRunnable?.run()
		} else {
			//Setup
			var bitmap: Bitmap? = null

			withContext(Dispatchers.IO) {
				cacheMutex.withLock {
					bitmap = get(key)
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

	//Runs it in own coroutines, useful if class cannot cleanly manage its own coroutines.
	fun loadBitmapASync(imageView: ImageView, key: String?, placeHolder: Int): Job {
		return mScope.launch { loadBitmap(imageView, key, placeHolder) }
	}

	fun getFile(filename: String): File {
		return diskCache.getFileReadOnly(filename)
	}

	companion object {
		const val TAG = "DynamicImageCache"
		val cacheMutex = Mutex()
		val mScope = MainScope() + CoroutineName(TAG)
	}
}