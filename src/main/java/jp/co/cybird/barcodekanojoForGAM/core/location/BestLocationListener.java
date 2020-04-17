package jp.co.cybird.barcodekanojoForGAM.core.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import java.util.Date;
import java.util.List;
import java.util.Observable;

public class BestLocationListener extends Observable implements LocationListener {
    private static final boolean DEBUG = false;
    public static final long LOCATION_UPDATE_MAX_DELTA_THRESHOLD = 300000;
    public static final long LOCATION_UPDATE_MIN_DISTANCE = 0;
    public static final long LOCATION_UPDATE_MIN_TIME = 0;
    public static final float REQUESTED_FIRST_SEARCH_ACCURACY_IN_METERS = 100.0f;
    public static final int REQUESTED_FIRST_SEARCH_MAX_DELTA_THRESHOLD = 300000;
    public static final long SLOW_LOCATION_UPDATE_MIN_DISTANCE = 50;
    public static final long SLOW_LOCATION_UPDATE_MIN_TIME = 300000;
    private static final String TAG = "BestLocationListener";
    private Location mLastLocation;

    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public synchronized void onBestLocationChanged(Location location) {
        this.mLastLocation = location;
        setChanged();
        notifyObservers(location);
    }

    public synchronized Location getLastKnownLocation() {
        return this.mLastLocation;
    }

    public synchronized void clearLastKnownLocation() {
        this.mLastLocation = null;
    }

    public void updateLocation(Location location) {
        if (location != null && this.mLastLocation == null) {
            onBestLocationChanged(location);
        } else if (location != null) {
            long now = new Date().getTime();
            long locationUpdateDelta = now - location.getTime();
            long lastLocationUpdateDelta = now - this.mLastLocation.getTime();
            boolean locationIsInTimeThreshold = locationUpdateDelta <= 300000;
            boolean lastLocationIsInTimeThreshold = lastLocationUpdateDelta <= 300000;
            if (locationUpdateDelta <= lastLocationUpdateDelta) {
            }
            boolean accuracyComparable = location.hasAccuracy() || this.mLastLocation.hasAccuracy();
            boolean locationIsMostAccurate = false;
            if (accuracyComparable) {
                if (location.hasAccuracy() && !this.mLastLocation.hasAccuracy()) {
                    locationIsMostAccurate = true;
                } else if (location.hasAccuracy() || !this.mLastLocation.hasAccuracy()) {
                    locationIsMostAccurate = location.getAccuracy() <= this.mLastLocation.getAccuracy();
                } else {
                    locationIsMostAccurate = false;
                }
            }
            if (accuracyComparable && locationIsMostAccurate && locationIsInTimeThreshold) {
                onBestLocationChanged(location);
            } else if (locationIsInTimeThreshold && !lastLocationIsInTimeThreshold) {
                onBestLocationChanged(location);
            }
        }
    }

    public boolean isAccurateEnough(Location location) {
        if (location == null || !location.hasAccuracy() || location.getAccuracy() > 100.0f || new Date().getTime() - location.getTime() >= 300000) {
            return false;
        }
        return true;
    }

    public void register(LocationManager locationManager, boolean gps) {
        long updateMinTime = 300000;
        long updateMinDistance = 50;
        if (gps) {
            updateMinTime = 0;
            updateMinDistance = 0;
        }
        List<String> providers = locationManager.getProviders(true);
        int providersCount = providers.size();
        for (int i = 0; i < providersCount; i++) {
            String providerName = providers.get(i);
            if (locationManager.isProviderEnabled(providerName)) {
                updateLocation(locationManager.getLastKnownLocation(providerName));
            }
            if (gps || !"gps".equals(providerName)) {
                locationManager.requestLocationUpdates(providerName, updateMinTime, (float) updateMinDistance, this);
            }
        }
    }

    public void unregister(LocationManager locationManager) {
        locationManager.removeUpdates(this);
    }

    public synchronized void updateLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        int providersCount = providers.size();
        for (int i = 0; i < providersCount; i++) {
            String providerName = providers.get(i);
            if (locationManager.isProviderEnabled(providerName)) {
                updateLocation(locationManager.getLastKnownLocation(providerName));
            }
        }
    }
}
