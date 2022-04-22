package com.goujer.barcodekanojo

import android.app.Application
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo
import com.goujer.barcodekanojo.preferences.ApplicationSetting
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import jp.co.cybird.barcodekanojoForGAM.core.location.BestLocationListener
import jp.co.cybird.barcodekanojoForGAM.billing.util.PurchaseApi
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import jp.co.cybird.barcodekanojoForGAM.Defs
import jp.co.cybird.barcodekanojoForGAM.R
import com.goujer.barcodekanojo.core.model.User

class BarcodeKanojoApp : Application() {
	lateinit var barcodeKanojo: BarcodeKanojo
		private set
	lateinit var settings: ApplicationSetting
		private set
	lateinit var imageCache: DynamicImageCache
		private set
	private val mBestLocationListener = BestLocationListener()
	lateinit var mPurchaseApi: PurchaseApi
		private set
	private lateinit var mUserGenderList: Array<String>

	override fun onCreate() {
		super.onCreate()
		barcodeKanojo = BarcodeKanojo()
		barcodeKanojo.user = User()
		mPurchaseApi = PurchaseApi(applicationContext)
		mUserGenderList = resources.getStringArray(R.array.user_account_gender_list)
		settings = ApplicationSetting(this)
		barcodeKanojo.createHttpApi(settings.getServerHttps(), settings.getServerURL(), settings.getServerPort(), Defs.USER_AGENT(), Defs.USER_LANGUAGE())
		imageCache = DynamicImageCache((Runtime.getRuntime().maxMemory() / 1024L).toInt() / 6, baseContext)
	}

	override fun onLowMemory() {
		super.onLowMemory()
		imageCache.evictAll()
	}

	override fun onTrimMemory(level: Int) {
		super.onTrimMemory(level)
        when (level) {
            TRIM_MEMORY_RUNNING_MODERATE -> imageCache.trimToSize(imageCache.size()/2)
            TRIM_MEMORY_RUNNING_LOW -> imageCache.trimToSize(imageCache.size()/3)
            TRIM_MEMORY_RUNNING_CRITICAL -> imageCache.trimToSize(imageCache.size()/4)
            TRIM_MEMORY_UI_HIDDEN,
            TRIM_MEMORY_BACKGROUND,
            TRIM_MEMORY_MODERATE,
            TRIM_MEMORY_COMPLETE -> imageCache.memEvictAll()
        }
	}

	fun changeLocate() {
		barcodeKanojo = BarcodeKanojo()
		mUserGenderList = resources.getStringArray(R.array.user_account_gender_list)
	}

	val uDID: String
		get() = "au_barcodekanojo"

	val uUID: String?
		get() = settings.getUUID()

	val user: User
		get() = barcodeKanojo.user

	fun logged_out() {
		sendBroadcast(Intent(INTENT_ACTION_LOGGED_OUT))
	}

	fun requestLocationUpdates(gps: Boolean): BestLocationListener {
		mBestLocationListener.register(getSystemService(LOCATION_SERVICE) as LocationManager, gps)
		return mBestLocationListener
	}

	//    public BestLocationListener requestLocationUpdates(Observer observer) {
	//        this.mBestLocationListener.addObserver(observer);
	//        this.mBestLocationListener.register((LocationManager) getSystemService(LOCATION_SERVICE), true);
	//        return this.mBestLocationListener;
	//    }

	fun removeLocationUpdates() {
		mBestLocationListener.unregister(getSystemService(LOCATION_SERVICE) as LocationManager)
        //return this.mBestLocationListener.getLastKnownLocation();
	}

	//    public void removeLocationUpdates(Observer observer) {
	//        this.mBestLocationListener.deleteObserver(observer);
	//        removeLocationUpdates();
	//    }

	//TODO This is a quick fix, but a real in depth consensual location approximation should be used eventually.
	val lastKnownLocation: Location?
		get() = null

	//return this.mBestLocationListener.getLastKnownLocation();
	//    public Location getLastKnownLocationOrThrow() {
	//        Location location = this.mBestLocationListener.getLastKnownLocation();
	//        if (location == null) {
	//            return null;
	//        }
	//        return location;
	//    }

	//    public void clearLastKnownLocation() {
	//        this.mBestLocationListener.clearLastKnownLocation();
	//    }

	//    public void setSavingListener(BaseDiskCache.BaseDiskCallBack listener) {
	//        this.mSaveListener = listener;
	//        loadResourceManagers();
	//    }

	//    public void removeSaveListener() {
	//        this.mSaveListener = null;
	//        loadResourceManagers();
	//    }

	val userGenderList: Array<String>
		get() {
			mUserGenderList = resources.getStringArray(R.array.user_account_gender_list)
			return mUserGenderList
		}

	companion object {
		const val INTENT_ACTION_FULL_STORAGE = "jp.co.cybird.barcodekanojoForGAM.intent.action.FULL_STORAGE"
		const val INTENT_ACTION_LOGGED_OUT = "jp.co.cybird.barcodekanojoForGAM.intent.action.LOGGED_OUT"
		const val PACKAGE_NAME = "jp.co.cybird.barcodekanojoForGAM"
		private const val TAG = "BarcodeKanojo"
	}
}