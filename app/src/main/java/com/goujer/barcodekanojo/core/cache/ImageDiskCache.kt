package com.goujer.barcodekanojo.core.cache

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.BuildConfig
import jp.co.cybird.barcodekanojoForGAM.Defs
import jp.co.cybird.barcodekanojoForGAM.core.util.FileUtil
import java.io.*

/*  Very Lazily cleaned LRU like Cache on Disk
	Works as subsystem of DynamicImageCache
	It is designed to not really trim until an Application.trimMemory() is called.
	This keeps trims from blocking runtime when the cache is being used.*/
class ImageDiskCache(val mContext: Context) {
	private var mStorageDirectory: File
	internal val maxSize: Long

	init {
		val storageDirectory = getCacheDirectory(mContext)
		if (FileUtil.isCacheDirectoryFull(mContext)) {
			mContext.sendBroadcast(Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE))
		}
		FileUtil.createDirectory(storageDirectory)
		mStorageDirectory = storageDirectory

		maxSize = if (Build.VERSION.SDK_INT > 26) {
				val storageManager = mContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
				storageManager.getCacheQuotaBytes(storageManager.getUuidForPath(mStorageDirectory))
			} else {
				Runtime.getRuntime().maxMemory() / 4L
			}
	}

	//    BaseDiskCache(String dirPath, String name, BaseDiskCallBack listener, Context context) {
	//    	this(dirPath, name, context);
	//        this.mListener = listener;
	//    }

	//    BaseDiskCache(File root, String dirPath, String name) {
	//        File storageDirectory = new File(new File(root, dirPath), name);
	//        createDirectory(storageDirectory);
	//        this.mStorageDirectory = storageDirectory;
	//        cleanupSimple();
	//    }

	@Throws(IOException::class)
	fun put(key: String?, `is`: InputStream?) {
		if (hasAvailableStorage(mContext) != 0) {
			try {
				BufferedInputStream(`is`).use { bufferedInputStream ->
					getOutputStream(key).use { os ->
						val b = ByteArray(2048)
						var total = 0
						while (true) {
							val count = bufferedInputStream.read(b)
							if (count <= 0) {
								break
							}
							os.write(b, 0, count)
							total += count
						}
					}
				}
			} catch (e: IOException) {
				if (Defs.DEBUG) {
					Log.e(TAG, "Error while saving cache file.")
					e.printStackTrace()
				}
				val deadFile = getFileReadOnly(key)
				deadFile.delete()
			}
		}
	}

	//Removes older files first till meeting System cache sizes
	fun trimToSize(maxSize: Long) {
		val allChildren = ArrayList<File>()
		if (mStorageDirectory.isDirectory && mStorageDirectory.listFiles()!!.isNotEmpty()) {
			allChildren.addAll(listAllChildren(mStorageDirectory))
		}

		allChildren.sortByDescending { list -> list.lastModified() }
		allChildren.reverse()
		while (allChildren.sumOf { it.length() } >= maxSize) {
			val child = allChildren.removeLastOrNull()
			if (child == null) {
				return
			} else {
				child.delete()
			}
		}
	}

	private fun listAllChildren(parent: File): ArrayList<File> {
		val allChildren = ArrayList<File>()
		for (child in parent.listFiles()!!) {
			if (child.isDirectory) {
				if (child.listFiles()!!.isEmpty()) {
					child.delete()
				} else {
					allChildren.addAll(listAllChildren(child))
				}
			} else if (child.isFile) {
				if (child != File(this.mStorageDirectory, FileUtil.NOMEDIA)) {
					if (child.length() == 0L) {
						child.delete()
					} else {
						allChildren.add(child)
					}
				}
			}
		}
		return allChildren
	}

	fun remove(key: String?) {
		if (!getFileReadOnly(key).delete()) {
			Log.i(TAG, "Unable to remove key from disk cache")
		}
	}

	//Removes all files in all cache directories
	fun evictAll() {
		if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
			evictAll(mContext.externalCacheDir!!)
		}
		evictAll(mContext.cacheDir)
	}

	private fun evictAll(parent: File) {
		val children = parent.list()
		if (children != null) {
			for (file in children) {
				val child = File(parent, file)
				if (child.isDirectory) {
					if (child.list()!!.isEmpty()) {
						child.delete()
					} else {
						evictAll(child)
					}
				} else if (child.isFile) {
					if (child != File(this.mStorageDirectory, FileUtil.NOMEDIA)) {
						child.delete()
					}
				}
			}
		}
	}

	fun exists(key: String?): Boolean {
		return File(createDirectory().toString() + File.separator + key).exists()
	}

	@Throws(IOException::class)
	fun getFile(key: String?): File {
		val result = File(createDirectory().toString() + File.separator + key)
		if (!result.exists()) {
			result.parentFile?.mkdirs()
			result.createNewFile()
		}
		return result
	}

	fun getFileReadOnly(key: String?): File {
		return File(createDirectory().toString() + File.separator + key)
	}

	@Throws(IOException::class)
	fun getInputStream(key: String?): InputStream {
		return FileInputStream(getFileReadOnly(key))
	}

	@Throws(IOException::class)
	fun getOutputStream(key: String?): OutputStream {
		return BufferedOutputStream(FileOutputStream(getFile(key)))
	}

	private fun hasAvailableStorage(context: Context): Int {
		val isAvailableExternalMemory = FileUtil.isAvailableExternalSDMemory()
		val isAvailableInternalMemory = FileUtil.isAvailableInternalMemory()
		if (isAvailableExternalMemory) {
			return EXTERNAL_MEMORY_AVAILABLE
		}
		if (isAvailableInternalMemory) {
			return INTERNAL_MEMORY_AVAILABLE
		}
		context.sendBroadcast(Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE))
		return STORAGE_NOT_AVAILABLE
	}

	private fun createDirectory(): File {
		val storageDirectory = getCacheDirectory(mContext)
		FileUtil.createDirectory(storageDirectory)
		return storageDirectory
	}

	companion object {
		private const val STORAGE_NOT_AVAILABLE = 0
		private const val EXTERNAL_MEMORY_AVAILABLE = 1
		private const val INTERNAL_MEMORY_AVAILABLE = 2
		private const val TAG = "BaseDiskCache"

		@JvmStatic
		private fun getCacheDirectory(context: Context): File {
			if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
				//If Media (external storage) is mounted, try to use it for the cache area.
				return if (Build.VERSION.SDK_INT >= 8) {
					if (Build.VERSION.SDK_INT >= 11) {
						//In APIs greater than 11 external storage may be emulated, in that situation we do not want to use the eternal storage as there is no benefit.
						if (!Environment.isExternalStorageEmulated()) {
							context.externalCacheDir ?: context.cacheDir
						} else {
							context.cacheDir
						}
					} else {
						context.externalCacheDir ?: context.cacheDir
					}
				} else {
					//On APIs less than 8 context.externalCacheDir is not accessible, so we create the path to the location ourselves.
					File(Environment.getExternalStorageDirectory(), "Android" + File.pathSeparator + "data" + File.pathSeparator + BuildConfig.APPLICATION_ID + File.pathSeparator + "cache" + File.pathSeparator)
				}
			} else {
				return context.cacheDir
			}
		}
	}


}