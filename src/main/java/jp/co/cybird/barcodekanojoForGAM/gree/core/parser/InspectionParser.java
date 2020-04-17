package jp.co.cybird.barcodekanojoForGAM.gree.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.parser.AbstractJSONParser;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.gree.core.model.Inspection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InspectionParser extends AbstractJSONParser<Inspection> {
    public Inspection parseInner(JSONObject json) throws BarcodeKanojoParseException {
        Inspection res = new Inspection();
        try {
            JSONObject object = ((JSONArray) json.get(GreeDefs.ENTRY)).getJSONObject(0);
            if (object.has(Inspection.TEXT_ID)) {
                res.setTextId(object.getString(Inspection.TEXT_ID));
            }
            if (object.has(Inspection.APP_ID)) {
                res.setAppId(object.getString(Inspection.APP_ID));
            }
            if (object.has(Inspection.AUTHOR_ID)) {
                res.setAuthorId(object.getString(Inspection.APP_ID));
            }
            if (object.has(Inspection.OWNER_ID)) {
                res.setOwnerId(object.getString(Inspection.APP_ID));
            }
            if (object.has("data")) {
                res.setData(object.getString("data"));
            }
            if (object.has(Inspection.STATUS)) {
            }
            res.setStatus(object.getInt(Inspection.STATUS));
            if (object.has(Inspection.CTIME)) {
                res.setCtime(object.getString(Inspection.CTIME));
            }
            if (object.has(Inspection.MTIME)) {
                res.setMtime(object.getString(Inspection.MTIME));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
