package jp.co.cybird.barcodekanojoForGAM.adapter.base;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import java.util.LinkedHashMap;
import java.util.Map;

public class SeparatedListHeaderAdapter extends BaseAdapter implements ObservableAdapter {
    private static final int EXTRA_HEADER = 1;
    private static final int EXTRA_HEADER_FOOTER = 2;

    public static final int TYPE_SECTION_FOOTER = -1;
    public static final int TYPE_SECTION_HEADER = 0;
    public final Map<String, View> footers = new LinkedHashMap<>();
    public final Map<String, View> headers = new LinkedHashMap<>();
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        public void onChanged() {
            SeparatedListHeaderAdapter.this.notifyDataSetChanged();
        }
    };
    private Map<String, Integer> mExtraItemCount = new LinkedHashMap<>();
    public final Map<String, Adapter> sections = new LinkedHashMap<>();

    public SeparatedListHeaderAdapter(Context context) {
    }

    public SeparatedListHeaderAdapter(Context context, int layoutId) {
    }

    public void addSection(String key, View header, Adapter adapter, View footer) {
        this.headers.put(key, header);
        this.sections.put(key, adapter);
        this.footers.put(key, footer);
        this.mExtraItemCount.put(key, 2);
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
        this.footers.clear();
        notifyDataSetInvalidated();
    }

    public Object getItem(int position) {
        for (String section : this.sections.keySet()) {
            Adapter adapter = this.sections.get(section);
            int extraCount = this.mExtraItemCount.get(section);
            int size = adapter.getCount() + extraCount;
            if (extraCount == 2) {
                if (position == 0) {
                    return this.headers.get(section);
                }
                if (position < size - 1) {
                    return adapter.getItem(position - 1);
                }
                if (position == size - 1) {
                    return this.footers.get(section);
                }
            } else if (extraCount != 1) {
                continue;
            } else if (position == 0) {
                return this.headers.get(section);
            } else {
                if (position < size) {
                    return adapter.getItem(position - 1);
                }
            }
            position -= size;
        }
        return null;
    }

    public int getCount() {
        int total = 0;
        for (Object section : this.sections.keySet()) {
            total += this.mExtraItemCount.get(section).intValue() + this.sections.get(section).getCount();
        }
        return total;
    }

    public int getViewTypeCount() {
        int total = 0;
        for (Object section : this.sections.keySet()) {
            total += this.mExtraItemCount.get(section).intValue() + this.sections.get(section).getViewTypeCount();
        }
        return total;
    }

    public int getItemViewType(int position) {
        int type = 1;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = this.sections.get(section);
            int extraCount = this.mExtraItemCount.get(section).intValue();
            int size = adapter.getCount() + extraCount;
            if (extraCount == 2) {
                if (position == 0) {
                    return 0;
                }
                if (position < size - 1) {
                    return adapter.getItemViewType(position - 1) + type;
                }
                if (position == size - 1) {
                    return -1;
                }
            } else if (extraCount != 1) {
                continue;
            } else if (position == 0) {
                return 0;
            } else {
                if (position < size) {
                    return adapter.getItemViewType(position - 1) + type;
                }
            }
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        return (getItemViewType(position) == 0 || getItemViewType(position) == -1) ? false : true;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = this.sections.get(section);
            int extraCount = this.mExtraItemCount.get(section);
            int size = adapter.getCount() + extraCount;
            if (extraCount == 2) {
                if (position == 0) {
                    return this.headers.get(section);
                }
                if (position < size - 1) {
                    return adapter.getView(position - 1, convertView, parent);
                }
                if (position == size - 1) {
                    return this.footers.get(section);
                }
            } else if (extraCount != 1) {
                continue;
            } else if (position == 0) {
                return this.headers.get(section);
            } else {
                if (position < size) {
                    return adapter.getView(position - 1, convertView, parent);
                }
            }
            position -= size;
            sectionnum++;
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return false;
    }

    public void removeFooter(String section) {
        if (this.footers.containsKey(section)) {
            this.footers.remove(section);
            this.mExtraItemCount.remove(section);
            this.mExtraItemCount.put(section, 1);
            notifyDataSetChanged();
        }
    }
}
