package jp.co.cybird.barcodekanojoForGAM.core.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KanojoMessageParser extends AbstractJSONParser<KanojoMessage> {
    protected KanojoMessage parseInner(JSONObject object) throws IOException, BarcodeKanojoException, BarcodeKanojoParseException {
        KanojoMessage item = new KanojoMessage();
        try {
            if (object.has("text")) {
	            JSONArray jSONTextArray = object.getJSONArray("text");
	            int size =  jSONTextArray.length();
	            String[] textArray = new String[size];

				for (int i = 0; i < size; i++) {
					textArray[i] = jSONTextArray.getString(i);  //Todo fix unicode escape characters
				}
                item.setMessages(textArray);
            }
            return item;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
