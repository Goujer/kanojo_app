package twitter4j.internal.json;

import java.io.Serializable;
import twitter4j.AccountSettings;
import twitter4j.Location;
import twitter4j.TimeZone;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

class AccountSettingsJSONImpl extends TwitterResponseImpl implements AccountSettings, Serializable {
    private static final long serialVersionUID = 7983363611306383416L;
    private final boolean ALWAYS_USE_HTTPS;
    private final boolean DISCOVERABLE_BY_EMAIL;
    private final boolean GEO_ENABLED;
    private final String LANGUAGE;
    private final String SLEEP_END_TIME;
    private final String SLEEP_START_TIME;
    private final boolean SLEEP_TIME_ENABLED;
    private final TimeZone TIMEZONE;
    private final Location[] TREND_LOCATION;

    private AccountSettingsJSONImpl(HttpResponse res, JSONObject json) throws TwitterException {
        super(res);
        try {
            JSONObject sleepTime = json.getJSONObject("sleep_time");
            this.SLEEP_TIME_ENABLED = z_T4JInternalParseUtil.getBoolean("enabled", sleepTime);
            this.SLEEP_START_TIME = sleepTime.getString("start_time");
            this.SLEEP_END_TIME = sleepTime.getString("end_time");
            if (json.isNull("trend_location")) {
                this.TREND_LOCATION = new Location[0];
            } else {
                JSONArray locations = json.getJSONArray("trend_location");
                this.TREND_LOCATION = new Location[locations.length()];
                for (int i = 0; i < locations.length(); i++) {
                    this.TREND_LOCATION[i] = new LocationJSONImpl(locations.getJSONObject(i));
                }
            }
            this.GEO_ENABLED = z_T4JInternalParseUtil.getBoolean("geo_enabled", json);
            this.LANGUAGE = json.getString("language");
            this.ALWAYS_USE_HTTPS = z_T4JInternalParseUtil.getBoolean("always_use_https", json);
            this.DISCOVERABLE_BY_EMAIL = z_T4JInternalParseUtil.getBoolean("discoverable_by_email", json);
            this.TIMEZONE = new TimeZoneJSONImpl(json.getJSONObject("time_zone"));
        } catch (JSONException e) {
            throw new TwitterException((Exception) e);
        }
    }

    AccountSettingsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
        }
    }

    AccountSettingsJSONImpl(JSONObject json) throws TwitterException {
        this((HttpResponse) null, json);
    }

    public boolean isSleepTimeEnabled() {
        return this.SLEEP_TIME_ENABLED;
    }

    public String getSleepStartTime() {
        return this.SLEEP_START_TIME;
    }

    public String getSleepEndTime() {
        return this.SLEEP_END_TIME;
    }

    public Location[] getTrendLocations() {
        return this.TREND_LOCATION;
    }

    public boolean isGeoEnabled() {
        return this.GEO_ENABLED;
    }

    public boolean isDiscoverableByEmail() {
        return this.DISCOVERABLE_BY_EMAIL;
    }

    public boolean isAlwaysUseHttps() {
        return this.ALWAYS_USE_HTTPS;
    }

    public String getLanguage() {
        return this.LANGUAGE;
    }

    public TimeZone getTimeZone() {
        return this.TIMEZONE;
    }
}
