package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityParser extends AbstractJSONParser<ActivityModel> {
    protected ActivityModel parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        JSONObject productObj;
        JSONObject kanojoObj;
        JSONObject userObj;
        JSONObject userObj2;
        ActivityModel res = new ActivityModel();
        try {
            if (object.has("id")) {
                res.setId(object.getInt("id"));
            }
            if (object.has("activity_type")) {
                res.setActivity_type(object.getInt("activity_type"));
            }
            if (object.has("created_timestamp")) {
                res.setCreated_timestamp(object.getInt("created_timestamp"));
            }
            if (object.has("activity")) {
                res.setActivity(object.getString("activity"));
            }
            if (object.has("user") && !object.isNull("user") && (userObj2 = object.getJSONObject("user")) != null) {
                res.setUser(new UserParser().parse(userObj2));
            }
            if (object.has("other_user") && !object.isNull("other_user") && (userObj = object.getJSONObject("other_user")) != null) {
                res.setOther_user(new UserParser().parse(userObj));
            }
            if (object.has("kanojo") && !object.isNull("kanojo") && (kanojoObj = object.getJSONObject("kanojo")) != null) {
                res.setKanojo(new KanojoParser().parse(kanojoObj));
            }
            if (object.has("product") && !object.isNull("product") && (productObj = object.getJSONObject("product")) != null) {
                res.setProduct(new ProductParser().parse(productObj));
            }
            return res;
        } catch (JSONException e) {
        	if (Defs.DEBUG) e.printStackTrace();
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
