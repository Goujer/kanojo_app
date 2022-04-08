package jp.co.cybird.barcodekanojoForGAM.core.parser;

import android.util.Log;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import org.json.JSONException;
import org.json.JSONObject;

public class KanojoItemParser extends AbstractJSONParser<KanojoItem> {
    private int item_class;

    public KanojoItemParser(int item_class2) {
        this.item_class = item_class2;
    }

    protected KanojoItem parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        KanojoItem res = new KanojoItem(this.item_class);
        try {
            if (object.has("item_category_id") && !object.isNull("item_category_id")) {
                res.setItem_category_id(object.getInt("item_category_id"));
                res.setCategory(true);
            }
            if (object.has("item_id") && !object.isNull("item_id")) {
                res.setItem_id(object.getInt("item_id"));
                res.setCategory(false);
            }
            if (object.has("expand_flag") && !object.isNull("expand_flag") && object.getInt("expand_flag") == 1) {
                res.setExpand_flag(true);
            }
            if (object.has("title")) {
                res.setTitle(object.getString("title"));
            }
            Log.d("KanojoItem", "Title : " + res.getTitle());
            if (object.has("description")) {
                res.setDescription(object.getString("description"));
            }
            if (object.has("image_thumbnail_url")) {
                res.setImage_thumbnail_url(object.getString("image_thumbnail_url"));
            }
            if (object.has("image_url")) {
                res.setImage_url(object.getString("image_url"));
            }
            if (object.has("price")) {
                res.setPrice(object.getString("price"));
            }
            if (object.has("confirm_purchase_message")) {
                res.setConfirm_purchase_message(object.getString("confirm_purchase_message"));
            }
            if (object.has("confirm_use_message")) {
                res.setConfirm_use_message(object.getString("confirm_use_message"));
            }
            if (object.has("has_units")) {
                res.setHas_units(object.getString("has_units"));
            }
            if (object.has("purchase_product_id")) {
                res.setItem_purchase_product_id(object.getString("purchase_product_id"));
            }
            if (object.has("purchasable_level") && !object.getString("purchasable_level").equals("null")) {
                res.setPurchasable_level(object.getString("purchasable_level"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
