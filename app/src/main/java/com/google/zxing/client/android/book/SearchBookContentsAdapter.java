package com.google.zxing.client.android.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;
import com.goujer.barcodekanojo.R;

final class SearchBookContentsAdapter extends ArrayAdapter<SearchBookContentsResult> {
    SearchBookContentsAdapter(Context context, List<SearchBookContentsResult> items) {
        super(context, R.layout.search_book_contents_list_item, 0, items);
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        SearchBookContentsListItem listItem;
        if (view == null) {
            listItem = (SearchBookContentsListItem) LayoutInflater.from(getContext()).inflate(R.layout.search_book_contents_list_item, viewGroup, false);
        } else if (!(view instanceof SearchBookContentsListItem)) {
            return view;
        } else {
            listItem = (SearchBookContentsListItem) view;
        }
        listItem.set((SearchBookContentsResult) getItem(position));
        return listItem;
    }
}
