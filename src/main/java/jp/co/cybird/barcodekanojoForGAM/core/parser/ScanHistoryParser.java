package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.ScanHistory;
import org.json.JSONException;
import org.json.JSONObject;

public class ScanHistoryParser extends AbstractJSONParser<ScanHistory> {
    /* access modifiers changed from: protected */
    public ScanHistory parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        ScanHistory res = new ScanHistory();
        try {
            if (object.has("barcode")) {
                res.setBarcode(object.getString("barcode"));
            }
            if (object.has("total_count")) {
                res.setTotal_count(object.getInt("total_count"));
            }
            if (object.has("kanojo_count")) {
                res.setKanojo_count(object.getInt("kanojo_count"));
            }
            if (object.has("friend_count")) {
                res.setFriend_count(object.getInt("friend_count"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
