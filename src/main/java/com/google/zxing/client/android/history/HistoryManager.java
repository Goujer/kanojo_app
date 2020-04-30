package com.google.zxing.client.android.history;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import com.facebook.internal.ServerProtocol;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.PreferencesActivity;
import com.google.zxing.client.android.result.ResultHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public final class HistoryManager {
	private static final String[] COLUMNS = {"text", ServerProtocol.DIALOG_PARAM_DISPLAY, "format", "timestamp", "details"};
	private static final String[] COUNT_COLUMN = {"COUNT(1)"};
	private static final DateFormat EXPORT_DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(2, 2);
	private static final String[] ID_COL_PROJECTION = {"id"};
	private static final String[] ID_DETAIL_COL_PROJECTION = {"id", "details"};
	private static final int MAX_ITEMS = 2000;
	private static final String TAG = HistoryManager.class.getSimpleName();
	private final Activity activity;
	private static final Pattern DOUBLE_QUOTE = Pattern.compile("\"", Pattern.LITERAL);

	public HistoryManager(Activity activity2) {
		this.activity = activity2;
	}

	public boolean hasHistoryItems() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = new DBHelper(this.activity).getReadableDatabase();
			cursor = db.query("history", COUNT_COLUMN, null, null, (String) null, (String) null, (String) null);
			cursor.moveToFirst();
			return cursor.getInt(0) > 0;
		} finally {
			close(cursor, db);
		}
	}

	public List<HistoryItem> buildHistoryItems() {
		SQLiteOpenHelper helper = new DBHelper(this.activity);
		List<HistoryItem> items = new ArrayList<>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.query("history", COLUMNS, (String) null, (String[]) null, (String) null, (String) null, "timestamp DESC");
			while (cursor.moveToNext()) {
				String text = cursor.getString(0);
				String display = cursor.getString(1);
				String format = cursor.getString(2);
				long timestamp = cursor.getLong(3);
				items.add(new HistoryItem(new Result(text, (byte[]) null, (ResultPoint[]) null, BarcodeFormat.valueOf(format), timestamp), display, cursor.getString(4)));
			}
			return items;
		} finally {
			close(cursor, db);
		}
	}

	public HistoryItem buildHistoryItem(int number) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = new DBHelper(this.activity).getReadableDatabase();
			cursor = db.query("history", COLUMNS, (String) null, (String[]) null, (String) null, (String) null, "timestamp DESC");
			cursor.move(number + 1);
			String text = cursor.getString(0);
			String display = cursor.getString(1);
			String format = cursor.getString(2);
			long timestamp = cursor.getLong(3);
			return new HistoryItem(new Result(text, (byte[]) null, (ResultPoint[]) null, BarcodeFormat.valueOf(format), timestamp), display, cursor.getString(4));
		} finally {
			close(cursor, db);
		}
	}

	public void deleteHistoryItem(int number) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = new DBHelper(this.activity).getWritableDatabase();
			cursor = db.query("history", ID_COL_PROJECTION, (String) null, (String[]) null, (String) null, (String) null, "timestamp DESC");
			cursor.move(number + 1);
			db.delete("history", "id=" + cursor.getString(0), (String[]) null);
		} finally {
			close(cursor, db);
		}
	}

	public void addHistoryItem(Result result, ResultHandler handler) {
		if (this.activity.getIntent().getBooleanExtra(Intents.Scan.SAVE_HISTORY, true) && !handler.areContentsSecure()) {
			if (!PreferenceManager.getDefaultSharedPreferences(this.activity).getBoolean(PreferencesActivity.KEY_REMEMBER_DUPLICATES, false)) {
				deletePrevious(result.getText());
			}
			ContentValues values = new ContentValues();
			values.put("text", result.getText());
			values.put("format", result.getBarcodeFormat().toString());
			values.put(ServerProtocol.DIALOG_PARAM_DISPLAY, handler.getDisplayContents().toString());
			values.put("timestamp", Long.valueOf(System.currentTimeMillis()));
			SQLiteDatabase db = null;
			try {
				db = new DBHelper(this.activity).getWritableDatabase();
				db.insert("history", "timestamp", values);
			} finally {
				close((Cursor) null, db);
			}
		}
	}

	public void addHistoryItemDetails(String itemID, String itemDetails) {
		String newDetails;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = new DBHelper(this.activity).getWritableDatabase();
			cursor = db.query("history", ID_DETAIL_COL_PROJECTION, "text=?", new String[]{itemID}, (String) null, (String) null, "timestamp DESC", GreeDefs.KANOJO_NAME);
			String oldID = null;
			String oldDetails = null;
			if (cursor.moveToNext()) {
				oldID = cursor.getString(0);
				oldDetails = cursor.getString(1);
			}
			if (oldID != null) {
				if (oldDetails == null) {
					newDetails = itemDetails;
				} else if (oldDetails.contains(itemDetails)) {
					newDetails = null;
				} else {
					newDetails = String.valueOf(oldDetails) + " : " + itemDetails;
				}
				if (newDetails != null) {
					ContentValues values = new ContentValues();
					values.put("details", newDetails);
					db.update("history", values, "id=?", new String[]{oldID});
				}
			}
		} finally {
			close(cursor, db);
		}
	}

	private void deletePrevious(String text) {
		SQLiteDatabase db = null;
		try {
			db = new DBHelper(this.activity).getWritableDatabase();
			db.delete("history", "text=?", new String[]{text});
		} finally {
			close((Cursor) null, db);
		}
	}

	public void trimHistory() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = new DBHelper(this.activity).getWritableDatabase();
			cursor = db.query("history", ID_COL_PROJECTION, (String) null, (String[]) null, (String) null, (String) null, "timestamp DESC");
			cursor.move(MAX_ITEMS);
			while (cursor.moveToNext()) {
				String id = cursor.getString(0);
				Log.i(TAG, "Deleting scan history ID " + id);
				db.delete("history", "id=" + id, (String[]) null);
			}
		} catch (SQLiteException sqle) {
			Log.w(TAG, sqle);
		} finally {
			close(cursor, db);
		}
	}

	/* access modifiers changed from: package-private */
	public CharSequence buildHistory() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = new DBHelper(this.activity).getWritableDatabase();
			cursor = db.query("history", COLUMNS, (String) null, (String[]) null, (String) null, (String) null, "timestamp DESC");
			StringBuilder historyText = new StringBuilder(1000);
			while (cursor.moveToNext()) {
				historyText.append('\"').append(massageHistoryField(cursor.getString(0))).append("\",");
				historyText.append('\"').append(massageHistoryField(cursor.getString(1))).append("\",");
				historyText.append('\"').append(massageHistoryField(cursor.getString(2))).append("\",");
				historyText.append('\"').append(massageHistoryField(cursor.getString(3))).append("\",");
				historyText.append('\"').append(massageHistoryField(EXPORT_DATE_TIME_FORMAT.format(new Date(cursor.getLong(3))))).append("\",");
				historyText.append('\"').append(massageHistoryField(cursor.getString(4))).append("\"\r\n");
			}
			return historyText;
		} finally {
			close(cursor, db);
		}
	}

	/* access modifiers changed from: package-private */
	public void clearHistory() {
		SQLiteDatabase db = null;
		try {
			db = new DBHelper(this.activity).getWritableDatabase();
			db.delete("history", (String) null, (String[]) null);
		} finally {
			close((Cursor) null, db);
		}
	}

	static Uri saveHistory(String history) {
		File bsRoot = new File(Environment.getExternalStorageDirectory(), "BarcodeScanner");
		File historyRoot = new File(bsRoot, "History");
		if (!historyRoot.exists() && !historyRoot.mkdirs()) {
			Log.w(TAG, "Couldn't make dir " + historyRoot);
			return null;
		}
		File historyFile = new File(historyRoot, "history-" + System.currentTimeMillis() + ".csv");
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(historyFile), Charset.forName("UTF-8"));
			out.write(history);
			return Uri.parse("file://" + historyFile.getAbsolutePath());
		} catch (IOException ioe) {
			Log.w(TAG, "Couldn't access file " + historyFile + " due to " + ioe);
			return null;
		}
	}

	private static String massageHistoryField(String value) {
		return value == null ? "" : DOUBLE_QUOTE.matcher(value).replaceAll("\"\"");
	}

	private static void close(Cursor cursor, SQLiteDatabase database) {
		if (cursor != null) {
			cursor.close();
		}
		if (database != null) {
			database.close();
		}
	}
}
