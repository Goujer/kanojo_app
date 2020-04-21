package twitter4j.internal.json;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class PlaceJSONImpl extends TwitterResponseImpl implements Place, Serializable {
    private static final long serialVersionUID = -2873364341474633812L;
    private GeoLocation[][] boundingBoxCoordinates;
    private String boundingBoxType;
    private Place[] containedWithIn;
    private String country;
    private String countryCode;
    private String fullName;
    private GeoLocation[][] geometryCoordinates;
    private String geometryType;
    private String id;
    private String name;
    private String placeType;
    private String streetAddress;
    private String url;

    PlaceJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    PlaceJSONImpl(JSONObject json, HttpResponse res) throws TwitterException {
        super(res);
        init(json);
    }

    PlaceJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    PlaceJSONImpl() {
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            this.name = z_T4JInternalParseUtil.getUnescapedString("name", json);
            this.streetAddress = z_T4JInternalParseUtil.getUnescapedString("street_address", json);
            this.countryCode = z_T4JInternalParseUtil.getRawString("country_code", json);
            this.id = z_T4JInternalParseUtil.getRawString("id", json);
            this.country = z_T4JInternalParseUtil.getRawString("country", json);
            if (!json.isNull("place_type")) {
                this.placeType = z_T4JInternalParseUtil.getRawString("place_type", json);
            } else {
                this.placeType = z_T4JInternalParseUtil.getRawString("type", json);
            }
            this.url = z_T4JInternalParseUtil.getRawString("url", json);
            this.fullName = z_T4JInternalParseUtil.getRawString("full_name", json);
            if (!json.isNull("bounding_box")) {
                JSONObject boundingBoxJSON = json.getJSONObject("bounding_box");
                this.boundingBoxType = z_T4JInternalParseUtil.getRawString("type", boundingBoxJSON);
                this.boundingBoxCoordinates = z_T4JInternalJSONImplFactory.coordinatesAsGeoLocationArray(boundingBoxJSON.getJSONArray("coordinates"));
            } else {
                this.boundingBoxType = null;
                this.boundingBoxCoordinates = null;
            }
            if (!json.isNull("geometry")) {
                JSONObject geometryJSON = json.getJSONObject("geometry");
                this.geometryType = z_T4JInternalParseUtil.getRawString("type", geometryJSON);
                JSONArray array = geometryJSON.getJSONArray("coordinates");
                if (this.geometryType.equals("Point")) {
                    this.geometryCoordinates = (GeoLocation[][]) Array.newInstance(GeoLocation.class, new int[]{1, 1});
                    this.geometryCoordinates[0][0] = new GeoLocation(array.getDouble(0), array.getDouble(1));
                } else if (this.geometryType.equals("Polygon")) {
                    this.geometryCoordinates = z_T4JInternalJSONImplFactory.coordinatesAsGeoLocationArray(array);
                } else {
                    this.geometryType = null;
                    this.geometryCoordinates = null;
                }
            } else {
                this.geometryType = null;
                this.geometryCoordinates = null;
            }
            if (!json.isNull("contained_within")) {
                JSONArray containedWithInJSON = json.getJSONArray("contained_within");
                this.containedWithIn = new Place[containedWithInJSON.length()];
                for (int i = 0; i < containedWithInJSON.length(); i++) {
                    this.containedWithIn[i] = new PlaceJSONImpl(containedWithInJSON.getJSONObject(i));
                }
                return;
            }
            this.containedWithIn = null;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    public int compareTo(Place that) {
        return this.id.compareTo(that.getId());
    }

    static ResponseList<Place> createPlaceList(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = null;
        try {
            json = res.asJSONObject();
            return createPlaceList(json.getJSONObject("result").getJSONArray("places"), res, conf);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    static ResponseList<Place> createPlaceList(JSONArray list, HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        try {
            int size = list.length();
            ResponseList<Place> places = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Place place = new PlaceJSONImpl(json);
                places.add(place);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(place, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(places, list);
            }
            return places;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getId() {
        return this.id;
    }

    public String getCountry() {
        return this.country;
    }

    public String getPlaceType() {
        return this.placeType;
    }

    public String getURL() {
        return this.url;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getBoundingBoxType() {
        return this.boundingBoxType;
    }

    public GeoLocation[][] getBoundingBoxCoordinates() {
        return this.boundingBoxCoordinates;
    }

    public String getGeometryType() {
        return this.geometryType;
    }

    public GeoLocation[][] getGeometryCoordinates() {
        return this.geometryCoordinates;
    }

    public Place[] getContainedWithIn() {
        return this.containedWithIn;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Place) || !((Place) obj).getId().equals(this.id)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        List list = null;
        StringBuilder append = new StringBuilder().append("PlaceJSONImpl{name='").append(this.name).append('\'').append(", streetAddress='").append(this.streetAddress).append('\'').append(", countryCode='").append(this.countryCode).append('\'').append(", id='").append(this.id).append('\'').append(", country='").append(this.country).append('\'').append(", placeType='").append(this.placeType).append('\'').append(", url='").append(this.url).append('\'').append(", fullName='").append(this.fullName).append('\'').append(", boundingBoxType='").append(this.boundingBoxType).append('\'').append(", boundingBoxCoordinates=").append(this.boundingBoxCoordinates == null ? null : Arrays.asList(this.boundingBoxCoordinates)).append(", geometryType='").append(this.geometryType).append('\'').append(", geometryCoordinates=").append(this.geometryCoordinates == null ? null : Arrays.asList(this.geometryCoordinates)).append(", containedWithIn=");
        if (this.containedWithIn != null) {
            list = Arrays.asList(this.containedWithIn);
        }
        return append.append(list).append('}').toString();
    }
}
