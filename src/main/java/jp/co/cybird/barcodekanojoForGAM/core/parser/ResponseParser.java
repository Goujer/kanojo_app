package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseParser extends AbstractJSONParser<Response<BarcodeKanojoModel>> {

    private static final String TAG = "ResponseParser";
    private JSONParser<? extends BarcodeKanojoModel>[] mSubParser;

    public ResponseParser(JSONParser<? extends BarcodeKanojoModel>... subParser) {
        this.mSubParser = subParser;
    }

    protected Response<BarcodeKanojoModel> parseInner(JSONObject object) throws BarcodeKanojoException {
        Response<BarcodeKanojoModel> response = new Response<>();
        try {
            if (!object.has("code") || object.isNull("code")) {
                throw new BarcodeKanojoException("Message from the server does not contain code!");
            }
            response.setCode(object.getInt("code"));
            for (JSONParser<? extends BarcodeKanojoModel> parse : this.mSubParser) {
				BarcodeKanojoModel parse2 = parse.parse(object);
                if (parse2 != null) {
                    response.add(parse2);
                }
            }
            return response;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
