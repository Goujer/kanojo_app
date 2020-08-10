package jp.co.cybird.barcodekanojoForGAM.core.parser;

import java.io.IOException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.WebViewData;
import org.json.JSONException;
import org.json.JSONObject;

public class WebViewParser extends AbstractJSONParser<WebViewData> {
    protected WebViewData parseInner(JSONObject object) throws IOException, BarcodeKanojoException, BarcodeKanojoParseException {
        WebViewData webviewData = new WebViewData();
        if (object.has("url")) {
            try {
                webviewData.setUrl(object.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return webviewData;
    }
}
