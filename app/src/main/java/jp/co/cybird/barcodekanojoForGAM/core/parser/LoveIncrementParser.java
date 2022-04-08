package jp.co.cybird.barcodekanojoForGAM.core.parser;

import android.util.Log;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.LoveIncrement;
import org.json.JSONException;
import org.json.JSONObject;

public class LoveIncrementParser extends AbstractJSONParser<LoveIncrement> {
    protected LoveIncrement parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        LoveIncrement res = new LoveIncrement();
        Log.d(LoveIncrement.TAG, object.toString());
        try {
            if (object.has("increase_love")) {
                res.setIncrease_love(object.getString("increase_love"));
            }
            if (object.has("decrement_love")) {
                res.setDecrement_love(object.getString("decrement_love"));
            }
            if (object.has("alertShow")) {
                res.setAlertShow(object.getString("alertShow"));
            }
            if (object.has("reaction_word")) {
                res.setReaction_word(object.getString("reaction_word"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
