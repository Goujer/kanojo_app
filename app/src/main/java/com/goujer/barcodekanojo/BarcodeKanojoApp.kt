package com.goujer.barcodekanojo

import android.app.Application
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import androidx.multidex.MultiDexApplication
import com.google.android.gms.security.ProviderInstaller
import com.goujer.barcodekanojo.core.BarcodeKanojo
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import com.goujer.barcodekanojo.core.model.User
import com.goujer.barcodekanojo.preferences.ApplicationSetting

import jp.co.cybird.barcodekanojoForGAM.core.location.BestLocationListener
import org.conscrypt.Conscrypt
import java.security.Security
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager


class BarcodeKanojoApp : MultiDexApplication() {
	lateinit var barcodeKanojo: BarcodeKanojo
		private set
	lateinit var imageCache: DynamicImageCache
		private set
	private val mBestLocationListener = BestLocationListener()

	private lateinit var mUserGenderList: Array<String>

	override fun onCreate() {
		super.onCreate()

		//Security Setup (Should allow newer TLS stuff)
		Security.insertProviderAt(Conscrypt.newProvider(), 1)

		initServerConnection()
		mUserGenderList = resources.getStringArray(R.array.user_account_gender_list)
		imageCache = DynamicImageCache((Runtime.getRuntime().maxMemory() / 1024L).toInt() / 6, baseContext)
	}

	@Deprecated("Deprecated in Java")
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
		barcodeKanojo = BarcodeKanojo(this)
		mUserGenderList = resources.getStringArray(R.array.user_account_gender_list)
	}

	val uDID: String
		get() = "au_barcodekanojo"

	val settings: ApplicationSetting
		get() = barcodeKanojo.settings

	val user: User?
		get() = barcodeKanojo.user

	fun initServerConnection() {
		barcodeKanojo = BarcodeKanojo(this)
		barcodeKanojo.user = User()
	}

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
	}
}