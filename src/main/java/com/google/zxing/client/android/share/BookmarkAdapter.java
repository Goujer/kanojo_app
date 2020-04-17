package com.google.zxing.client.android.share;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;

final class BookmarkAdapter extends BaseAdapter {
    private final Context context;
    private final Cursor cursor;

    BookmarkAdapter(Context context2, Cursor cursor2) {
        this.context = context2;
        this.cursor = cursor2;
    }

    public int getCount() {
        return this.cursor.getCount();
    }

    public Object getItem(int index) {
        return null;
    }

    public long getItemId(int index) {
        return (long) index;
    }

    public View getView(int index, View view, ViewGroup viewGroup) {
        LinearLayout layout;
        if (view instanceof LinearLayout) {
            layout = (LinearLayout) view;
        } else {
            layout = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.bookmark_picker_list_item, viewGroup, false);
        }
        if (!this.cursor.isClosed()) {
            this.cursor.moveToPosition(index);
            ((TextView) layout.findViewById(R.id.bookmark_title)).setText(this.cursor.getString(0));
            ((TextView) layout.findViewById(R.id.bookmark_url)).setText(this.cursor.getString(1));
        }
        return layout;
    }
}
