package com.google.zxing.client.android.share;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.zxing.client.android.Intents;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is only needed because I can't successfully send an ACTION_PICK intent to
 * com.android.browser.BrowserBookmarksPage. It can go away if that starts working in the future.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class BookmarkPickerActivity extends ListActivity {

    private static final String TAG = BookmarkPickerActivity.class.getSimpleName();

    private static final String[] BOOKMARK_PROJECTION = {
            "title", // Browser.BookmarkColumns.TITLE
            "url", // Browser.BookmarkColumns.URL
    };
    // Copied from android.provider.Browser.BOOKMARKS_URI:
    private static final Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");

    private static final String BOOKMARK_SELECTION = "bookmark = 1 AND url IS NOT NULL";

    private final List<String[]> titleURLs = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        titleURLs.clear();
        try {
            Cursor cursor = getContentResolver().query(BOOKMARKS_URI, BOOKMARK_PROJECTION,
                    BOOKMARK_SELECTION, null, null);
            if (cursor == null) {
                Log.w(TAG, "No cursor returned for bookmark query");
                finish();
                return;
            }
            while (cursor.moveToNext()) {
                titleURLs.add(new String[] { cursor.getString(0), cursor.getString(1) });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListAdapter(new BookmarkAdapter(this, titleURLs));
    }

    @Override
    protected void onListItemClick(ListView l, View view, int position, long id) {
        String[] titleURL = titleURLs.get(position);
        Intent intent = new Intent();
        intent.addFlags(Intents.FLAG_NEW_DOC);
        intent.putExtra("title", titleURL[0]); // Browser.BookmarkColumns.TITLE
        intent.putExtra("url", titleURL[1]); // Browser.BookmarkColumns.URL
        setResult(RESULT_OK, intent);
        finish();
    }
}
