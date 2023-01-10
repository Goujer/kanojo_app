package jp.co.cybird.barcodekanojoForGAM.adapter.base;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;

public class SeparatedListAdapter extends BaseAdapter implements ObservableAdapter {
    public static final int MODE_EXTEND_DATE = 4;
    public static final int MODE_EXTEND_GIFT = 5;
    public static final int TYPE_SECTION_HEADER = 0;
    public List<Boolean> flag;
    public final ArrayAdapter<String> headers;
    public int mCount;
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        public void onChanged() {
            SeparatedListAdapter.this.notifyDataSetChanged();
        }
    };
    private int mode;
    public final Map<String, Adapter> sections = new LinkedHashMap();
    private int userLevel;

    public SeparatedListAdapter(Context context) {
        this.headers = new ArrayAdapter<>(context, R.layout.list_header);
        this.mCount = 0;
    }

    public SeparatedListAdapter(Context context, int layoutId) {
        this.headers = new ArrayAdapter<>(context, layoutId);
        this.flag = new ArrayList();
        this.mCount = 0;
    }

    public void setUserLevel(int userLevel2) {
        this.userLevel = userLevel2;
    }

    public void setMode(int mode2) {
        this.mode = mode2;
    }

    public void addSection(String section, boolean flag, Adapter adapter) {
        this.headers.add(section);
        this.sections.put(section, adapter);
        this.flag.add(flag);
        adapter.registerDataSetObserver(this.mDataSetObserver);
        notifyDataSetChanged();
    }

    public void removeObserver() {
        for (Map.Entry<String, Adapter> it : this.sections.entrySet()) {
            if (it.getValue() instanceof ObservableAdapter) {
                ((ObservableAdapter) it.getValue()).removeObserver();
            }
        }
    }

    public void clear() {
        this.headers.clear();
        this.sections.clear();
        this.flag.clear();
        notifyDataSetInvalidated();
    }

    public Object getItem(int position) {
        for (Object section : this.sections.keySet()) {
            Adapter adapter = this.sections.get(section);
            int size = adapter.getCount() + 1;
            if (position == 0) {
                return section;
            }
            if (position < size) {
                return adapter.getItem(position - 1);
            }
            position -= size;
        }
        return null;
    }

    public int getCount() {
        int total = 0;
        for (Adapter adapter : this.sections.values()) {
            total += adapter.getCount() + 1;
        }
        return total;
    }

    public int getViewTypeCount() {
        int total = 1;
        for (Adapter adapter : this.sections.values()) {
            total += adapter.getViewTypeCount();
        }
        return total;
    }

    public int getItemViewType(int position) {
        int type = 1;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = this.sections.get(section);
            int size = adapter.getCount() + 1;
            if (position == 0) {
                return 0;
            }
            if (position < size) {
                return adapter.getItemViewType(position - 1) + type;
            }
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

	@Override
    public boolean isEnabled(int position) {
        if (this.mode == MODE_EXTEND_DATE || this.mode == MODE_EXTEND_GIFT) {
            if (getItemViewType(position) == 0 || !checkLevel(position)) {
                return false;
            }
            return true;
        } else if (getItemViewType(position) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkLevel(int position) {
        KanojoItem item = (KanojoItem) getItem(position);
        if (item == null || this.userLevel == 0 || (item.getPurchasable_level() != null && Integer.parseInt(item.getPurchasable_level()) > this.userLevel)) {
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = this.sections.get(section);
            int size = adapter.getCount() + 1;
            if (position == 0) {
                View header = this.headers.getView(sectionnum, convertView, parent);
                if (this.flag.get(sectionnum)) {
                    header.setBackgroundResource(R.drawable.common_sectionheaderbg_blue);
                    return header;
                }
                header.setBackgroundResource(R.drawable.common_sectionheaderbg);
                return header;
            } else if (position < size) {
                return adapter.getView(position - 1, convertView, parent);
            } else {
                position -= size;
                sectionnum++;
            }
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public boolean hasStableIds() {
        return false;
    }
}
