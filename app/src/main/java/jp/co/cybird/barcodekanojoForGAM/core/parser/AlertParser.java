package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlertParser extends AbstractJSONParser<ModelList<Alert>> {
    protected ModelList<Alert> parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        ModelList<Alert> res = new ModelList<>();
        try {
            if (object.has("alert")) {
                JSONObject jsonObj = object.getJSONObject("alert");
                Alert alert = new Alert();
                if (jsonObj.has("title")) {
                    alert.setTitle(jsonObj.getString("title"));
                }
                if (jsonObj.has("body")) {
                    alert.setBody(jsonObj.getString("body"));
                }
                res.add(alert);
                return res;
            } else if (!object.has("alerts")) {
                return null;
            } else {
                JSONArray jsonArray = object.getJSONArray("alerts");
                if (jsonArray == null) {
                    return null;
                }
                int l = jsonArray.length();
                for (int i = 0; i < l; i++) {
                    JSONObject jsonObj2 = jsonArray.getJSONObject(i);
                    Alert alert2 = new Alert();
                    if (jsonObj2.has("title")) {
                        alert2.setTitle(jsonObj2.getString("title"));
                    }
                    if (jsonObj2.has("body")) {
                        alert2.setBody(jsonObj2.getString("body"));
                    }
                    res.add(alert2);
                }
                return res;
            }
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
