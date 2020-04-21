package twitter4j.internal.json;

import twitter4j.HashtagEntity;
import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

class HashtagEntityJSONImpl extends EntityIndex implements HashtagEntity {
    private static final long serialVersionUID = 4068992372784813200L;
    private String text;

    HashtagEntityJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    HashtagEntityJSONImpl(int start, int end, String text2) {
        setStart(start);
        setEnd(end);
        this.text = text2;
    }

    HashtagEntityJSONImpl() {
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            setStart(indicesArray.getInt(0));
            setEnd(indicesArray.getInt(1));
            if (!json.isNull("text")) {
                this.text = json.getString("text");
            }
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public String getText() {
        return this.text;
    }

    public int getStart() {
        return super.getStart();
    }

    public int getEnd() {
        return super.getEnd();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashtagEntityJSONImpl that = (HashtagEntityJSONImpl) o;
        if (this.text != null) {
            if (this.text.equals(that.text)) {
                return true;
            }
        } else if (that.text == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.text != null) {
            return this.text.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "HashtagEntityJSONImpl{text='" + this.text + '\'' + '}';
    }
}
