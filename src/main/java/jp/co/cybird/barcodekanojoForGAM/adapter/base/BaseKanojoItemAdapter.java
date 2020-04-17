package jp.co.cybird.barcodekanojoForGAM.adapter.base;

import android.content.Context;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;

public abstract class BaseKanojoItemAdapter extends AbsModelListAdapter<KanojoItem> {
    public BaseKanojoItemAdapter(Context context) {
        super(context);
    }
}
