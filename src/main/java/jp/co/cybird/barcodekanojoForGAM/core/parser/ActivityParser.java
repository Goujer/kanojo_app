package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.Scanned;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.PropertyConfiguration;

public class ActivityParser extends AbstractJSONParser<ActivityModel> {
    /* access modifiers changed from: protected */
    public ActivityModel parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        JSONObject scannedObj;
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
                res.setUser((User) new UserParser().parse(userObj2));
            }
            if (object.has("other_user") && !object.isNull("other_user") && (userObj = object.getJSONObject("other_user")) != null) {
                res.setOther_user((User) new UserParser().parse(userObj));
            }
            if (object.has("kanojo") && !object.isNull("kanojo") && (kanojoObj = object.getJSONObject("kanojo")) != null) {
                res.setKanojo((Kanojo) new KanojoParser().parse(kanojoObj));
            }
            if (object.has("scanned") && !object.isNull("scanned") && (scannedObj = object.getJSONObject("scanned")) != null) {
                res.setScanned((Scanned) new ScannedParser().parse(scannedObj));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
