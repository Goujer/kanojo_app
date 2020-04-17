package com.google.zxing.client.android.share;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public final class BookmarkPickerActivity extends ListActivity {
    private static final String[] BOOKMARK_PROJECTION = {"title", "url"};
    private static final String BOOKMARK_SELECTION = "bookmark = 1 AND url IS NOT NULL";
    private static final String TAG = BookmarkPickerActivity.class.getSimpleName();
    static final int TITLE_COLUMN = 0;
    static final int URL_COLUMN = 1;
    private Cursor cursor = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.cursor = getContentResolver().query(Browser.BOOKMARKS_URI, BOOKMARK_PROJECTION, BOOKMARK_SELECTION, (String[]) null, (String) null);
        if (this.cursor == null) {
            Log.w(TAG, "No cursor returned for bookmark query");
            finish();
            return;
        }
        startManagingCursor(this.cursor);
        setListAdapter(new BookmarkAdapter(this, this.cursor));
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View view, int position, long id) {
        if (this.cursor.isClosed() || !this.cursor.moveToPosition(position)) {
            setResult(0);
        } else {
            Intent intent = new Intent();
            intent.addFlags(524288);
            intent.putExtra("title", this.cursor.getString(0));
            intent.putExtra("url", this.cursor.getString(1));
            setResult(-1, intent);
        }
        finish();
    }
}
