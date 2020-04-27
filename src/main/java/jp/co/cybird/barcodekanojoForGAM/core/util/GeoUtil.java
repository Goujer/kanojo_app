package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GeoUtil {
    private static final boolean DEBUG = false;

    public static String geoToString(LatLng gp) {
        if (gp == null) {
            return null;
        }
        return gp.latitude + "," + gp.longitude;
    }

    public static LatLng stringToGeo(String geo) {
        if (geo == null) {
            return null;
        }
        String[] s = geo.split(",");
        if (s.length == 2) {
            try {
                return new LatLng(Float.parseFloat(s[0]), Float.parseFloat(s[1]));
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static double getLatitudeE6(LatLng geo) {
        if (geo != null) {
            return geo.latitude;
        }
        return 0;
    }

    public static double getLongitudeE6(LatLng geo) {
        if (geo != null) {
            return geo.longitude;
        }
        return 0;
    }

    public static void getToLocation(LatLng geo, Location location) {
        if (geo != null) {
            location.setLatitude(geo.latitude);
            location.setLongitude(geo.longitude);
        }
    }

    public static LatLng LocationToGeo(Location location) {
        if (location == null) {
            return null;
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
