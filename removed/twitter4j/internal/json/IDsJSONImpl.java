package twitter4j.internal.json;

import java.util.Arrays;
import twitter4j.IDs;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class IDsJSONImpl extends TwitterResponseImpl implements IDs {
    private static final long serialVersionUID = -6585026560164704953L;
    private long[] ids;
    private long nextCursor = -1;
    private long previousCursor = -1;

    IDsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        String json = res.asString();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    IDsJSONImpl(String json) throws TwitterException {
        init(json);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    private void init(String jsonStr) throws TwitterException {
        JSONArray idList;
        JSONObject json;
        try {
            if (jsonStr.startsWith("{")) {
                json = new JSONObject(jsonStr);
                JSONArray idList2 = json.getJSONArray("ids");
                this.ids = new long[idList2.length()];
                for (int i = 0; i < idList2.length(); i++) {
                    this.ids[i] = Long.parseLong(idList2.getString(i));
                }
                this.previousCursor = z_T4JInternalParseUtil.getLong("previous_cursor", json);
                this.nextCursor = z_T4JInternalParseUtil.getLong("next_cursor", json);
                return;
            }
            idList = new JSONArray(jsonStr);
            this.ids = new long[idList.length()];
            for (int i2 = 0; i2 < idList.length(); i2++) {
                this.ids[i2] = Long.parseLong(idList.getString(i2));
            }
        } catch (NumberFormatException nfe) {
            throw new TwitterException("Twitter API returned malformed response: " + idList, (Throwable) nfe);
        } catch (NumberFormatException nfe2) {
            throw new TwitterException("Twitter API returned malformed response: " + json, (Throwable) nfe2);
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public long[] getIDs() {
        return this.ids;
    }

    public boolean hasPrevious() {
        return 0 != this.previousCursor;
    }

    public long getPreviousCursor() {
        return this.previousCursor;
    }

    public boolean hasNext() {
        return 0 != this.nextCursor;
    }

    public long getNextCursor() {
        return this.nextCursor;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IDs)) {
            return false;
        }
        if (!Arrays.equals(this.ids, ((IDs) o).getIDs())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.ids != null) {
            return Arrays.hashCode(this.ids);
        }
        return 0;
    }

    public String toString() {
        return "IDsJSONImpl{ids=" + Arrays.toString(this.ids) + ", previousCursor=" + this.previousCursor + ", nextCursor=" + this.nextCursor + '}';
    }
}
