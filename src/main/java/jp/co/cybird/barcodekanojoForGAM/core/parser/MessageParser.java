package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageParser extends AbstractJSONParser<MessageModel> {
    private String[] mNames;

    public MessageParser(String... names) {
        this.mNames = names;
    }

    protected MessageModel parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        MessageModel res = new MessageModel();
        if (object.has("message") && !object.isNull("message")) {
            try {
                res.put("message", object.getString("message"));
                return res;
            } catch (JSONException e) {
            }
        }
        if (!object.has("messages") || object.isNull("messages")) {
            return res;
        }
        JSONObject jsonObj = null;
        try {
            jsonObj = object.getJSONObject("messages");
        } catch (JSONException e2) {
        }
        if (jsonObj == null) {
            return null;
        }
        if (this.mNames == null || this.mNames.length == 0) {
            return null;
        }
        for (String name : this.mNames) {
            try {
                if (jsonObj.has(name) && !jsonObj.isNull(name)) {
                    res.put(name, jsonObj.getString(name));
                }
            } catch (JSONException e3) {
            }
        }
        return res;
    }
}
