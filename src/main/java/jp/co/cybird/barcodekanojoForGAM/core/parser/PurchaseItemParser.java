package jp.co.cybird.barcodekanojoForGAM.core.parser;

import java.io.IOException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.PurchaseItem;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseItemParser extends AbstractJSONParser<PurchaseItem> {
    /* access modifiers changed from: protected */
    public PurchaseItem parseInner(JSONObject object) throws IOException, BarcodeKanojoException, BarcodeKanojoParseException {
        PurchaseItem item = new PurchaseItem();
        try {
            if (object.has("google_transaction_id")) {
                item.setTransactionId(object.getInt("google_transaction_id"));
            }
            return item;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
