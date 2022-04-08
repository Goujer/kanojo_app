package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.SearchResult;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultParser extends AbstractJSONParser<SearchResult> {
    protected SearchResult parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        SearchResult res = new SearchResult();
        try {
            if (object.has("hit_count")) {
                res.setHit_count(object.getInt("hit_count"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
