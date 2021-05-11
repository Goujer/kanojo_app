package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.location.Location;

public class GeoUtil {
    public static String geoToString(Location gp) {
        if (gp == null) {
            return "0,0";
        }
        return gp.getLatitude() + "," + gp.getLongitude();
    }

    public static Location stringToGeo(String geo) {
        if (geo == null) {
            return null;
        }
        String[] s = geo.split(",");
        if (s.length == 2) {
            try {
            	Location returning = new Location("BarcodeKANOJO");
            	returning.setLatitude(Float.parseFloat(s[0]));
            	returning.setLongitude(Float.parseFloat(s[1]));
                return returning;
            } catch (Exception e) {
            }
        }
        return null;
    }

	public static Location doublesToGeo(double latitude, double longitude) {
		Location returning = new Location("BarcodeKANOJO");
		returning.setLatitude(latitude);
		returning.setLongitude(longitude);
		return returning;
	}

    public static double getLatitudeE6(Location geo) {
        if (geo != null) {
            return geo.getLatitude();
        }
        return 0;
    }

    public static double getLongitudeE6(Location geo) {
        if (geo != null) {
            return geo.getLongitude();
        }
        return 0;
    }

    //public static void geoToLocation(LatLng geo, Location location) {
    //    if (geo != null) {
    //        location.setLatitude(geo.latitude);
    //        location.setLongitude(geo.longitude);
    //    }
    //}

    //public static LatLng LocationToGeo(Location location) {
    //    if (location == null) {
    //        return null;
    //    }
    //    return new LatLng(location.getLatitude(), location.getLongitude());
    //}
}
