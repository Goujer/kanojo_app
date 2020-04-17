package jp.co.cybird.barcodekanojoForGAM.core.model;

import java.util.ArrayList;
import java.util.Collection;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;

public class ModelList<T extends BarcodeKanojoModel> extends ArrayList<T> implements BarcodeKanojoModel {
    public static final String TAG = "ModelList";
    private static final long serialVersionUID = 1;

    public ModelList() {
    }

    public ModelList(Collection<T> collection) {
        super(collection);
    }

    public BarcodeKanojoModel get(Class<?> cls) {
        if (isEmpty()) {
            return null;
        }
        int size = size();
        for (int i = 0; i < size; i++) {
            BarcodeKanojoModel m = (BarcodeKanojoModel) get(i);
            if (m.getClass().isAssignableFrom(cls)) {
                return m;
            }
        }
        return null;
    }
}
