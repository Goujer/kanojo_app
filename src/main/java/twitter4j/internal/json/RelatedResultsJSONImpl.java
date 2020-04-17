package twitter4j.internal.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import twitter4j.RelatedResults;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

final class RelatedResultsJSONImpl extends TwitterResponseImpl implements RelatedResults, Serializable {
    private static final String TWEETS_FROM_USER = "TweetsFromUser";
    private static final String TWEETS_WITH_CONVERSATION = "TweetsWithConversation";
    private static final String TWEETS_WITH_REPLY = "TweetsWithReply";
    private static final long serialVersionUID = -7417061781993004083L;
    private Map<String, ResponseList<Status>> tweetsMap;

    RelatedResultsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        init(res.asJSONArray(), res, conf.isJSONStoreEnabled());
    }

    RelatedResultsJSONImpl(JSONArray jsonArray) throws TwitterException {
        init(jsonArray, (HttpResponse) null, false);
    }

    private void init(JSONArray jsonArray, HttpResponse res, boolean registerRawJSON) throws TwitterException {
        this.tweetsMap = new HashMap(2);
        try {
            int listLen = jsonArray.length();
            for (int i = 0; i < listLen; i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                if ("Tweet".equals(o.getString("resultType"))) {
                    String groupName = o.getString("groupName");
                    if (groupName.length() != 0 && (groupName.equals(TWEETS_WITH_CONVERSATION) || groupName.equals(TWEETS_WITH_REPLY) || groupName.equals(TWEETS_FROM_USER))) {
                        JSONArray results = o.getJSONArray("results");
                        ResponseList<Status> statuses = this.tweetsMap.get(groupName);
                        if (statuses == null) {
                            statuses = new ResponseListImpl<>(results.length(), res);
                            this.tweetsMap.put(groupName, statuses);
                        }
                        int resultsLen = results.length();
                        for (int j = 0; j < resultsLen; j++) {
                            JSONObject json = results.getJSONObject(j).getJSONObject("value");
                            Status status = new StatusJSONImpl(json);
                            if (registerRawJSON) {
                                DataObjectFactoryUtil.registerJSONObject(status, json);
                            }
                            statuses.add(status);
                        }
                        if (registerRawJSON) {
                            DataObjectFactoryUtil.registerJSONObject(statuses, results);
                        }
                    }
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        }
    }

    public ResponseList<Status> getTweetsWithConversation() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_WITH_CONVERSATION);
        return statuses != null ? statuses : new ResponseListImpl<>(0, (HttpResponse) null);
    }

    public ResponseList<Status> getTweetsWithReply() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_WITH_REPLY);
        return statuses != null ? statuses : new ResponseListImpl<>(0, (HttpResponse) null);
    }

    public ResponseList<Status> getTweetsFromUser() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_FROM_USER);
        return statuses != null ? statuses : new ResponseListImpl<>(0, (HttpResponse) null);
    }

    public int hashCode() {
        return this.tweetsMap.hashCode() + 31;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RelatedResultsJSONImpl)) {
            return false;
        }
        RelatedResultsJSONImpl other = (RelatedResultsJSONImpl) obj;
        if (this.tweetsMap == null) {
            if (other.tweetsMap != null) {
                return false;
            }
        } else if (this.tweetsMap.equals(other.tweetsMap)) {
            return true;
        } else {
            return false;
        }
        return true;
    }

    public String toString() {
        return "RelatedResultsJSONImpl {tweetsMap=" + this.tweetsMap + "}";
    }
}
