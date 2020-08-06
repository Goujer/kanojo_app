package jp.co.cybird.barcodekanojoForGAM.core.parser;

import java.io.IOException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class KanojoMessageParser extends AbstractJSONParser<KanojoMessage> {
    protected KanojoMessage parseInner(JSONObject object) throws IOException, BarcodeKanojoException, BarcodeKanojoParseException {
        KanojoMessage item = new KanojoMessage();
        try {
            if (object.has("text")) {
                item.setMessage(object.getString("text"));
            }
            if (object.has("btn_text")) {
                item.setButtonText(object.getString("btn_text"));
            }
            if (object.has("url")) {
                item.setSiteURL(object.getString("url"));
            }
            if (object.has("next_screen")) {
                item.setNextScreen(object.getString("next_screen"));
            }
            if (object.has("kanojo_id")) {
                item.setKanojo_id(object.getInt("kanojo_id"));
            }
            return item;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
