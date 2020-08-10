package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductParser extends AbstractJSONParser<Product> {
    protected Product parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        Product res = new Product();
        try {
            if (object.has("barcode")) {
                res.setBarcode(object.getString("barcode"));
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
            if (object.has("geo")) {
                res.setGeo(object.getString("geo"));
            }
            if (object.has("location")) {
                res.setLocation(object.getString("location"));
            }
            if (object.has("product_image_url")) {
                res.setProduct_image_url(object.getString("product_image_url"));
            }
            if (object.has("scan_count")) {
                res.setScan_count(object.getInt("scan_count"));
            }
            if (object.has("company_name")) {
                res.setCompany_name(object.getString("company_name"));
            }
            if (object.has("country")) {
                res.setCountry(object.getString("country"));
            }
            if (object.has("price")) {
                res.setPrice(object.getString("price"));
            }
            if (object.has("product")) {
                res.setProduct(object.getString("product"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
