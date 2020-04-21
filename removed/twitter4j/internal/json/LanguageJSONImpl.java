package twitter4j.internal.json;

import jp.co.cybird.barcodekanojoForGAM.gree.core.model.Inspection;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.api.HelpResources;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public class LanguageJSONImpl implements HelpResources.Language {
    private String code;
    private String name;
    private String status;

    LanguageJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            this.name = json.getString("name");
            this.code = json.getString("code");
            this.status = json.getString(Inspection.STATUS);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), (Throwable) jsone);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getStatus() {
        return this.status;
    }

    static ResponseList<HelpResources.Language> createLanguageList(HttpResponse res, Configuration conf) throws TwitterException {
        return createLanguageList(res.asJSONArray(), res, conf);
    }

    static ResponseList<HelpResources.Language> createLanguageList(JSONArray list, HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        try {
            int size = list.length();
            ResponseList<HelpResources.Language> languages = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                HelpResources.Language language = new LanguageJSONImpl(json);
                languages.add(language);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(language, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(languages, list);
            }
            return languages;
        } catch (JSONException jsone) {
            throw new TwitterException((Exception) jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }
}
