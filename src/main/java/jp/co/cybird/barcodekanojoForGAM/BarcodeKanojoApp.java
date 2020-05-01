package jp.co.cybird.barcodekanojoForGAM;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import java.util.Observer;
import jp.co.cybird.barcodekanojoForGAM.billing.util.PurchaseApi;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.location.BestLocationListener;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.BaseDiskCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;

public class BarcodeKanojoApp extends Application {
    private static final boolean DEBUG = false;
    public static final String INTENT_ACTION_FULL_STORAGE = "jp.co.cybird.barcodekanojoForGAM.intent.action.FULL_STORAGE";
    public static final String INTENT_ACTION_LOGGED_OUT = "jp.co.cybird.barcodekanojoForGAM.intent.action.LOGGED_OUT";
    public static final String PACKAGE_NAME = "jp.co.cybird.barcodekanojoForGAM";
    private static final String TAG = "BarcodeKanojo";
    private BarcodeKanojo mBarcodeKanojo;
    private BestLocationListener mBestLocationListener = new BestLocationListener();
    private PurchaseApi mPurchaseApi;
    private RemoteResourceManager mRemoteResourceManager;
    private BaseDiskCache.BaseDiskCallBack mSaveListener;
    private String[] mUserGenderList;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mBarcodeKanojo = new BarcodeKanojo(BarcodeKanojo.createHttpApi(Defs.USER_AGENT(), Defs.USER_LANGUAGE()));
        this.mBarcodeKanojo.setUser(new User());
        loadResourceManagers();
        this.mPurchaseApi = new PurchaseApi(getApplicationContext());
        this.mUserGenderList = getResources().getStringArray(R.array.user_account_gender_list);
    }

    public void changeLocate() {
        this.mBarcodeKanojo = new BarcodeKanojo(BarcodeKanojo.createHttpApi(Defs.USER_AGENT(), Defs.USER_LANGUAGE()));
        this.mUserGenderList = getResources().getStringArray(R.array.user_account_gender_list);
    }

    public String getUDID() {
        return "au_barcodekanojo";
    }

    public String getUUID() {
        return new ApplicationSetting(this).getUUID();
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

    public RemoteResourceManager getRemoteResourceManager() {
        return this.mRemoteResourceManager;
    }

    public void loadResourceManagers() {
        try {
            if (this.mSaveListener != null) {
                this.mRemoteResourceManager = new RemoteResourceManager("cache", this.mSaveListener, getApplicationContext());
            } else {
                this.mRemoteResourceManager = new RemoteResourceManager("cache", getApplicationContext());
            }
        } catch (IllegalStateException e) {
            if (this.mSaveListener != null) {
                this.mRemoteResourceManager = new RemoteResourceManager("cache", this.mSaveListener, getApplicationContext());
            } else {
                this.mRemoteResourceManager = new RemoteResourceManager("cache", getApplicationContext());
            }
        }
    }

    public void logged_out() {
        sendBroadcast(new Intent(INTENT_ACTION_LOGGED_OUT));
    }

    public BestLocationListener requestLocationUpdates(boolean gps) {
        this.mBestLocationListener.register((LocationManager) getSystemService(LOCATION_SERVICE), gps);
        return this.mBestLocationListener;
    }

    public BestLocationListener requestLocationUpdates(Observer observer) {
        this.mBestLocationListener.addObserver(observer);
        this.mBestLocationListener.register((LocationManager) getSystemService(LOCATION_SERVICE), true);
        return this.mBestLocationListener;
    }

    public void removeLocationUpdates() {
        this.mBestLocationListener.unregister((LocationManager) getSystemService(LOCATION_SERVICE));
    }

    public void removeLocationUpdates(Observer observer) {
        this.mBestLocationListener.deleteObserver(observer);
        removeLocationUpdates();
    }

    public Location getLastKnownLocation() {
        return this.mBestLocationListener.getLastKnownLocation();
    }

    public Location getLastKnownLocationOrThrow() {
        Location location = this.mBestLocationListener.getLastKnownLocation();
        if (location == null) {
            return null;
        }
        return location;
    }

    public void clearLastKnownLocation() {
        this.mBestLocationListener.clearLastKnownLocation();
    }

    public void setSavingListener(BaseDiskCache.BaseDiskCallBack listener) {
        this.mSaveListener = listener;
        loadResourceManagers();
    }

    public void removeSaveListener() {
        this.mSaveListener = null;
        loadResourceManagers();
    }

    public String[] getUserGenderList() {
        this.mUserGenderList = getResources().getStringArray(R.array.user_account_gender_list);
        return this.mUserGenderList;
    }
}
