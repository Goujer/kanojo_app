package twitter4j.internal.json;

import twitter4j.TimeZone;
import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public class TimeZoneJSONImpl implements TimeZone {
    private final String NAME;
    private final String TZINFO_NAME;
    private final int UTC_OFFSET;

    TimeZoneJSONImpl(JSONObject jSONObject) throws TwitterException {
        try {
            this.UTC_OFFSET = z_T4JInternalParseUtil.getInt("utc_offset", jSONObject);
            this.NAME = jSONObject.getString("name");
            this.TZINFO_NAME = jSONObject.getString("tzinfo_name");
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public String getName() {
        return this.NAME;
    }

    public String tzinfoName() {
        return this.TZINFO_NAME;
    }

    public int utcOffset() {
        return this.UTC_OFFSET;
    }
}
