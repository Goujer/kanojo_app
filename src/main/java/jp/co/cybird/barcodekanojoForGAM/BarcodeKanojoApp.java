package jp.co.cybird.barcodekanojoForGAM;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import jp.co.cybird.barcodekanojoForGAM.billing.util.PurchaseApi;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.location.BestLocationListener;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;

import com.goujer.barcodekanojo.core.util.DynamicImageCache;
import com.goujer.barcodekanojo.preferences.ApplicationSetting;

public class BarcodeKanojoApp extends Application {
    public static final String INTENT_ACTION_FULL_STORAGE = "jp.co.cybird.barcodekanojoForGAM.intent.action.FULL_STORAGE";
    public static final String INTENT_ACTION_LOGGED_OUT = "jp.co.cybird.barcodekanojoForGAM.intent.action.LOGGED_OUT";
    public static final String PACKAGE_NAME = "jp.co.cybird.barcodekanojoForGAM";
    private static final String TAG = "BarcodeKanojo";
    private BarcodeKanojo mBarcodeKanojo;
    private ApplicationSetting settings;
    private DynamicImageCache imageCache;
    private BestLocationListener mBestLocationListener = new BestLocationListener();
    private PurchaseApi mPurchaseApi;
    private String[] mUserGenderList;

	@Override
    public void onCreate() {
        super.onCreate();
        this.mBarcodeKanojo = new BarcodeKanojo();
        this.mBarcodeKanojo.setUser(new User());
        this.mPurchaseApi = new PurchaseApi(getApplicationContext());
        this.mUserGenderList = getResources().getStringArray(R.array.user_account_gender_list);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
		settings = new ApplicationSetting(this);
		mBarcodeKanojo.createHttpApi(settings.getServerHttps(), settings.getServerURL(), settings.getServerPort(), Defs.USER_AGENT(), Defs.USER_LANGUAGE());
		imageCache = new DynamicImageCache(settings.getCacheSize(), getBaseContext(), mBarcodeKanojo.getHttpApi());
    }

    public void changeLocate() {
        this.mBarcodeKanojo = new BarcodeKanojo();
        this.mUserGenderList = getResources().getStringArray(R.array.user_account_gender_list);
    }

    public String getUDID() {
        return "au_barcodekanojo";
    }

    public String getUUID() {
        return settings.getUUID();
    }

    public User getUser() {
        return this.mBarcodeKanojo.getUser();
    }

    public BarcodeKanojo getBarcodeKanojo() {
        return this.mBarcodeKanojo;
    }

    public PurchaseApi getPurchaseApi() {
        if (this.mPurchaseApi.getHelper() == null) {
            this.mPurchaseApi = new PurchaseApi(getApplicationContext());
        }
        return this.mPurchaseApi;
    }

	public ApplicationSetting getSettings() {
		return settings;
	}

    public DynamicImageCache getImageCache() {
		return imageCache;
	}

    public void logged_out() {
        sendBroadcast(new Intent(INTENT_ACTION_LOGGED_OUT));
    }

    public BestLocationListener requestLocationUpdates(boolean gps) {
        this.mBestLocationListener.register((LocationManager) getSystemService(LOCATION_SERVICE), gps);
        return this.mBestLocationListener;
    }

//    public BestLocationListener requestLocationUpdates(Observer observer) {
//        this.mBestLocationListener.addObserver(observer);
//        this.mBestLocationListener.register((LocationManager) getSystemService(LOCATION_SERVICE), true);
//        return this.mBestLocationListener;
//    }

    public void removeLocationUpdates() {
        this.mBestLocationListener.unregister((LocationManager) getSystemService(LOCATION_SERVICE));
    }

//    public void removeLocationUpdates(Observer observer) {
//        this.mBestLocationListener.deleteObserver(observer);
//        removeLocationUpdates();
//    }

	//TODO This is a quick fix, but a real in depth consensual location approximation should be used eventually.
    public Location getLastKnownLocation() {
        return null;
    	//return this.mBestLocationListener.getLastKnownLocation();
    }

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

    public String[] getUserGenderList() {
        this.mUserGenderList = getResources().getStringArray(R.array.user_account_gender_list);
        return this.mUserGenderList;
    }

    public void updateBCKApi(boolean useHttps, String newUrl, int port) {
		mBarcodeKanojo.createHttpApi(useHttps, newUrl, port, Defs.USER_AGENT(), Defs.USER_LANGUAGE());
	}

	public String getmApiBaseUrl() {
    	return mBarcodeKanojo.getmApiBaseUrl();
	}
}
