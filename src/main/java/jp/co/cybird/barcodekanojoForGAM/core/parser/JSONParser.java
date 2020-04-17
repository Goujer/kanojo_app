package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import org.json.JSONObject;

public interface JSONParser<T extends BarcodeKanojoModel> {
    T parse(JSONObject jSONObject) throws BarcodeKanojoException, BarcodeKanojoParseException;
}
