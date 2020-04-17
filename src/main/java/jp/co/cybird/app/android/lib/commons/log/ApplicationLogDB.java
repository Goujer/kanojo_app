package jp.co.cybird.app.android.lib.commons.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLogDB {
    public static final String COLUMN_MESSAGE = "applog_message";
    public static final String COLUMN_TAG = "applog_tag";
    public static final String COLUMN_TIMESTAMP = "applog_timestamp";
    public static final String COLUMN__ID = "_id";
    static final String DATABASE_NAME = "lib_applog.db";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_APPLOG = "applog";
    private static final String[] TABLE_TRANSACTIONS_COLUMNS = {COLUMN__ID, COLUMN_TIMESTAMP, COLUMN_MESSAGE, COLUMN_TAG};
    static Context mContext;
    private DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mDb = this.mDatabaseHelper.getWritableDatabase();

    public ApplicationLogDB(Context context) {
        mContext = context;
        this.mDatabaseHelper = new DatabaseHelper(context);
    }

    public void close() {
        this.mDatabaseHelper.close();
    }

    public void insert(ApplicationLog appLog) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, Long.valueOf(appLog.getTimestamp()));
        values.put(COLUMN_TAG, appLog.getTag());
        values.put(COLUMN_MESSAGE, appLog.getMessage());
        this.mDb.replace(TABLE_APPLOG, (String) null, values);
    }

    public Cursor queryTransactions() {
        return this.mDb.query(TABLE_APPLOG, TABLE_TRANSACTIONS_COLUMNS, (String) null, (String[]) null, (String) null, (String) null, (String) null);
    }

    public Cursor queryTransactions(String productId) {
        return this.mDb.query(TABLE_APPLOG, TABLE_TRANSACTIONS_COLUMNS, "applog_timestamp = ?", new String[]{productId}, (String) null, (String) null, (String) null);
    }

    protected static final ApplicationLog createTransaction(Cursor cursor) {
        return new ApplicationLog(cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP)), cursor.getString(cursor.getColumnIndex(COLUMN_TAG)), cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE)));
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, ApplicationLogDB.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            createTransactionsTable(db);
        }

        private void createTransactionsTable(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE applog(_id INTEGER PRIMARY KEY AUTOINCREMENT, applog_timestamp INTEGER, applog_tag TEXT, applog_message TEXT )");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public List<ApplicationLog> getAllLogs() {
        List<ApplicationLog> resultArray = new ArrayList<>();
        Cursor cursor = queryTransactions();
        if (cursor.moveToFirst()) {
            do {
                resultArray.add(createTransaction(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resultArray;
    }
}
