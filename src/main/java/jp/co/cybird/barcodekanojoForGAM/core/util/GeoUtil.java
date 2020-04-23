package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.location.Location;
import com.google.android.maps.GeoPoint;

public class GeoUtil {
    private static final boolean DEBUG = false;

    public static String geoToString(GeoPoint gp) {
        if (gp == null) {
            return null;
        }
        return ((double) gp.getLatitudeE6()) / 1000000.0d + "," + (((double) gp.getLongitudeE6()) / 1000000.0d);
    }

    public static GeoPoint stringToGeo(String geo) {
        if (geo == null) {
            return null;
        }
        String[] s = geo.split(",");
        if (s.length == 2) {
            try {
                return new GeoPoint((int) (((double) Float.parseFloat(s[0])) * 1000000.0d), (int) (((double) Float.parseFloat(s[1])) * 1000000.0d));
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static int getLatitudeE6(GeoPoint geo) {
        if (geo != null) {
            return geo.getLatitudeE6();
        }
        return 0;
    }

    public static int getLongitudeE6(GeoPoint geo) {
        if (geo != null) {
            return geo.getLongitudeE6();
        }
        return 0;
    }

    public static void getToLoaction(GeoPoint geo, Location location) {
        if (geo != null) {
            location.setLatitude(((double) geo.getLatitudeE6()) / 1000000.0d);
            location.setLongitude(((double) geo.getLongitudeE6()) / 1000000.0d);
        }
    }

    public static GeoPoint LocationToGeo(Location location) {
        if (location == null) {
            return null;
        }
        return new GeoPoint((int) (location.getLatitude() * 1000000.0d), (int) (location.getLongitude() * 1000000.0d));
    }
}
