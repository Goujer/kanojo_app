package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Scanned;
import org.json.JSONException;
import org.json.JSONObject;

public class ScannedParser extends AbstractJSONParser<Scanned> {
    protected Scanned parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        Scanned res = new Scanned();
        try {
            if (object.has("id")) {
                res.setId(object.getInt("id"));
            }
            if (object.has("barcode")) {
                res.setBarcode(object.getString("barcode"));
            }
            if (object.has("user_id")) {
                res.setUser_id(object.getInt("user_id"));
            }
            if (object.has("geo")) {
                res.setGeo(object.getString("geo"));
            }
            if (object.has("location")) {
                res.setLocation(object.getString("location"));
            }
            if (object.has("name")) {
                res.setName(object.getString("name"));
            }
            if (object.has("category_id")) {
                res.setCategory_id(object.getInt("category_id"));
            }
            if (object.has("category")) {
                res.setCategory(object.getString("category"));
            }
            if (object.has("comment")) {
                res.setComment(object.getString("comment"));
            }
            if (object.has("product_image_url")) {
                res.setProduct_image_url(object.getString("product_image_url"));
            }
            if (object.has("nationality")) {
                res.setNationality(object.getString("nationality"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
