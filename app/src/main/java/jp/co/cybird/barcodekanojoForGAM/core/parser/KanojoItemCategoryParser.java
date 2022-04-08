package jp.co.cybird.barcodekanojoForGAM.core.parser;

import android.util.Log;
import java.util.Iterator;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItemCategory;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import org.json.JSONException;
import org.json.JSONObject;

public class KanojoItemCategoryParser extends AbstractJSONParser<KanojoItemCategory> {
    private int item_class;

    public KanojoItemCategoryParser(int item_class2) {
        this.item_class = item_class2;
    }

    protected KanojoItemCategory parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        KanojoItemCategory res = new KanojoItemCategory();
        try {
            if (object.has("title")) {
                res.setTitle(object.getString("title"));
            }
            if (object.has("flag")) {
                res.setFlag(object.getString("flag"));
            }
            if (object.has("level")) {
                res.setLevel(object.getString("level"));
            }
            ModelList<?> list = (ModelList) new ModelListParser("items", new KanojoItemParser(this.item_class)).parse(object);
            if (list != null) {
                ModelList<KanojoItem> items = new ModelList<>();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    BarcodeKanojoModel item = (BarcodeKanojoModel) it.next();
                    if (item instanceof KanojoItem) {
                        items.add((KanojoItem) item);
                    }
                }
                Log.e("KanojoItem list", "List size: " + list.size());
                Log.e("KanojoItem item", "item size: " + items.size());
                res.setItems(items);
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
