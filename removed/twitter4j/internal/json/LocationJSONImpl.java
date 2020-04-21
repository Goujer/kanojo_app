package twitter4j.internal.json;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class LocationJSONImpl implements Location {
    private static final long serialVersionUID = 7095092358530897222L;
    private final String countryCode;
    private final String countryName;
    private final String name;
    private final int placeCode;
    private final String placeName;
    private final String url;
    private final int woeid;

    LocationJSONImpl(JSONObject location) throws TwitterException {
        try {
            this.woeid = z_T4JInternalParseUtil.getInt("woeid", location);
            this.countryName = z_T4JInternalParseUtil.getUnescapedString("country", location);
            this.countryCode = z_T4JInternalParseUtil.getRawString("countryCode", location);
            if (!location.isNull("placeType")) {
                JSONObject placeJSON = location.getJSONObject("placeType");
                this.placeName = z_T4JInternalParseUtil.getUnescapedString("name", placeJSON);
                this.placeCode = z_T4JInternalParseUtil.getInt("code", placeJSON);
            } else {
                this.placeName = null;
                this.placeCode = -1;
            }
            this.name = z_T4JInternalParseUtil.getUnescapedString("name", location);
            this.url = z_T4JInternalParseUtil.getUnescapedString("url", location);
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    static ResponseList<Location> createLocationList(HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        return createLocationList(res.asJSONArray(), conf.isJSONStoreEnabled());
    }

    static ResponseList<Location> createLocationList(JSONArray list, boolean storeJSON) throws TwitterException {
        try {
            int size = list.length();
            ResponseList<Location> locations = new ResponseListImpl<>(size, (HttpResponse) null);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Location location = new LocationJSONImpl(json);
                locations.add(location);
                if (storeJSON) {
                    DataObjectFactoryUtil.registerJSONObject(location, json);
                }
            }
            if (storeJSON) {
                DataObjectFactoryUtil.registerJSONObject(locations, list);
            }
            return locations;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public int getWoeid() {
        return this.woeid;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public int getPlaceCode() {
        return this.placeCode;
    }

    public String getName() {
        return this.name;
    }

    public String getURL() {
        return this.url;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationJSONImpl)) {
            return false;
        }
        if (this.woeid != ((LocationJSONImpl) o).woeid) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.woeid;
    }

    public String toString() {
        return "LocationJSONImpl{woeid=" + this.woeid + ", countryName='" + this.countryName + '\'' + ", countryCode='" + this.countryCode + '\'' + ", placeName='" + this.placeName + '\'' + ", placeCode='" + this.placeCode + '\'' + ", name='" + this.name + '\'' + ", url='" + this.url + '\'' + '}';
    }
}
