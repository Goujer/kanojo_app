package jp.co.cybird.barcodekanojoForGAM.adapter.base;

import android.content.Context;
import android.widget.BaseAdapter;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;

public abstract class AbsModelListAdapter<T extends BarcodeKanojoModel> extends BaseAdapter {
    protected ModelList<T> list = null;

    public AbsModelListAdapter(Context context) {
    }

    public int getCount() {
        if (this.list == null) {
            return 0;
        }
        return this.list.size();
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public boolean isEmpty() {
        if (this.list == null) {
            return true;
        }
        return this.list.isEmpty();
    }

    public void clear() {
        if (this.list != null) {
            this.list.clear();
        }
    }

    public void setModelList(ModelList<T> l) {
        this.list = l;
        notifyDataSetInvalidated();
    }

    public void addModelList(ModelList<T> l) {
        if (this.list == null) {
            this.list = new ModelList<>();
        }
        this.list.addAll(l);
        notifyDataSetChanged();
    }
}
