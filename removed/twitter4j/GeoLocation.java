package twitter4j;

import java.io.Serializable;

public class GeoLocation implements Serializable {
    private static final long serialVersionUID = -4847567157651889935L;
    protected double latitude;
    protected double longitude;

    public GeoLocation(double latitude2, double longitude2) {
        this.latitude = latitude2;
        this.longitude = longitude2;
    }

    GeoLocation() {
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoLocation)) {
            return false;
        }
        GeoLocation that = (GeoLocation) o;
        if (Double.compare(that.getLatitude(), this.latitude) != 0) {
            return false;
        }
        if (Double.compare(that.getLongitude(), this.longitude) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp;
        long temp2;
        if (this.latitude != 0.0d) {
            temp = Double.doubleToLongBits(this.latitude);
        } else {
            temp = 0;
        }
        int result = (int) ((temp >>> 32) ^ temp);
        if (this.longitude != 0.0d) {
            temp2 = Double.doubleToLongBits(this.longitude);
        } else {
            temp2 = 0;
        }
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    public String toString() {
        return "GeoLocation{latitude=" + this.latitude + ", longitude=" + this.longitude + '}';
    }
}
