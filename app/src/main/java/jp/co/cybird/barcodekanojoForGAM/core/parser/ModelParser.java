package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelParser extends AbstractJSONParser<BarcodeKanojoModel> {

    private String mName;
    private JSONParser<? extends BarcodeKanojoModel> mSubParser;

    public ModelParser(String name, JSONParser<? extends BarcodeKanojoModel> subParser) {
        this.mName = name;
        this.mSubParser = subParser;
    }

    protected BarcodeKanojoModel parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        try {
            if (!object.has(this.mName) || object.isNull(this.mName) || object.getString(this.mName).equals("")) {
                return null;
            }
            return this.mSubParser.parse(object.getJSONObject(this.mName));
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
