package jp.co.cybird.barcodekanojoForGAM.core.parser;

import android.util.Log;

import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Category;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryParser extends AbstractJSONParser<ModelList<Category>> {
    public static final String TAG = "CategoryParser";

    protected ModelList<Category> parseInner(JSONObject object) throws BarcodeKanojoException {
        ModelList<Category> res = new ModelList<>();
        try {
            if (!object.has("categories") || object.isNull("categories")) {
                if (BarcodeKanojo.DEBUG) {
                    Log.d(TAG, "Categories is null");
                }
                return null;
            }
            JSONArray jsonArray = object.getJSONArray("categories");
            if (jsonArray == null) {
                return null;
            }
            int l = jsonArray.length();
            for (int i = 0; i < l; i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Category category = new Category();
                if (jsonObj.has("id")) {
                    category.setId(jsonObj.getInt("id"));
                }
                if (jsonObj.has("name")) {
                    category.setName(jsonObj.getString("name"));
                }
                res.add(category);
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
