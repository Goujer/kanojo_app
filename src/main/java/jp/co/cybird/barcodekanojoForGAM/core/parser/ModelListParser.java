package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelListParser extends AbstractJSONParser<ModelList<BarcodeKanojoModel>> {
    private static final boolean DEBUG = false;
    private static final String TAG = "ModelListParser";
    private String mName;
    private JSONParser<? extends BarcodeKanojoModel> mSubParser;

    public ModelListParser(String name, JSONParser<? extends BarcodeKanojoModel> subParser) {
        this.mName = name;
        this.mSubParser = subParser;
    }

    /* access modifiers changed from: protected */
    public ModelList<BarcodeKanojoModel> parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        ModelList<BarcodeKanojoModel> ModelList = new ModelList<>();
        try {
            if (!object.has(this.mName)) {
                return null;
            }
            if (object.isNull(this.mName)) {
                return null;
            }
            JSONArray jsonArray = object.getJSONArray(this.mName);
            int l = jsonArray.length();
            for (int i = 0; i < l; i++) {
                Object parse = this.mSubParser.parse(jsonArray.getJSONObject(i));
                if (parse != null) {
                    ModelList.add(parse);
                }
            }
            return ModelList;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
