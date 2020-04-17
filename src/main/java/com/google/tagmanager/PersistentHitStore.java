package com.google.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.SimpleNetworkDispatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.impl.client.DefaultHttpClient;

class PersistentHitStore implements HitStore {
    /* access modifiers changed from: private */
    public static final String CREATE_HITS_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL,'%s' INTEGER NOT NULL);", new Object[]{HITS_TABLE, HIT_ID, HIT_TIME, HIT_URL, HIT_FIRST_DISPATCH_TIME});
    private static final String DATABASE_FILENAME = "gtm_urls.db";
    @VisibleForTesting
    static final String HITS_TABLE = "gtm_hits";
    static final long HIT_DISPATCH_RETRY_WINDOW = 14400000;
    @VisibleForTesting
    static final String HIT_FIRST_DISPATCH_TIME = "hit_first_send_time";
    @VisibleForTesting
    static final String HIT_ID = "hit_id";
    private static final String HIT_ID_WHERE_CLAUSE = "hit_id=?";
    @VisibleForTesting
    static final String HIT_TIME = "hit_time";
    @VisibleForTesting
    static final String HIT_URL = "hit_url";
    /* access modifiers changed from: private */
    public Clock mClock;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final String mDatabaseName;
    private final UrlDatabaseHelper mDbHelper;
    private volatile Dispatcher mDispatcher;
    private long mLastDeleteStaleHitsTime;
    private final HitStoreStateListener mListener;

    PersistentHitStore(HitStoreStateListener listener, Context ctx) {
        this(listener, ctx, DATABASE_FILENAME);
    }

    @VisibleForTesting
    PersistentHitStore(HitStoreStateListener listener, Context ctx, String databaseName) {
        this.mContext = ctx.getApplicationContext();
        this.mDatabaseName = databaseName;
        this.mListener = listener;
        this.mClock = new Clock() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.mDbHelper = new UrlDatabaseHelper(this.mContext, this.mDatabaseName);
        this.mDispatcher = new SimpleNetworkDispatcher(new DefaultHttpClient(), this.mContext, new StoreDispatchListener());
        this.mLastDeleteStaleHitsTime = 0;
    }

    @VisibleForTesting
    public void setClock(Clock clock) {
        this.mClock = clock;
    }

    @VisibleForTesting
    public UrlDatabaseHelper getDbHelper() {
        return this.mDbHelper;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setDispatcher(Dispatcher dispatcher) {
        this.mDispatcher = dispatcher;
    }

    public void putHit(long hitTimeInMilliseconds, String path) {
        deleteStaleHits();
        removeOldHitIfFull();
        writeHitToDatabase(hitTimeInMilliseconds, path);
    }

    private void removeOldHitIfFull() {
        int hitsOverLimit = (getNumStoredHits() - 2000) + 1;
        if (hitsOverLimit > 0) {
            List<String> hitsToDelete = peekHitIds(hitsOverLimit);
            Log.v("Store full, deleting " + hitsToDelete.size() + " hits to make room.");
            deleteHits((String[]) hitsToDelete.toArray(new String[0]));
        }
    }

    private void writeHitToDatabase(long hitTimeInMilliseconds, String path) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for putHit");
        if (db != null) {
            ContentValues content = new ContentValues();
            content.put(HIT_TIME, Long.valueOf(hitTimeInMilliseconds));
            content.put(HIT_URL, path);
            content.put(HIT_FIRST_DISPATCH_TIME, 0);
            try {
                db.insert(HITS_TABLE, (String) null, content);
                this.mListener.reportStoreIsEmpty(false);
            } catch (SQLiteException e) {
                Log.w("Error storing hit");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public List<String> peekHitIds(int maxHits) {
        List<String> hitIds = new ArrayList<>();
        if (maxHits <= 0) {
            Log.w("Invalid maxHits specified. Skipping");
        } else {
            SQLiteDatabase db = getWritableDatabase("Error opening database for peekHitIds.");
            if (db != null) {
                Cursor cursor = null;
                try {
                    Cursor cursor2 = db.query(HITS_TABLE, new String[]{HIT_ID}, (String) null, (String[]) null, (String) null, (String) null, String.format("%s ASC", new Object[]{HIT_ID}), Integer.toString(maxHits));
                    if (cursor2.moveToFirst()) {
                        do {
                            hitIds.add(String.valueOf(cursor2.getLong(0)));
                        } while (cursor2.moveToNext());
                    }
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                } catch (SQLiteException e) {
                    Log.w("Error in peekHits fetching hitIds: " + e.getMessage());
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
        }
        return hitIds;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00f7  */
    public List<Hit> peekHits(int maxHits) {
        List<Hit> hits = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase("Error opening database for peekHits");
        if (db == null) {
            return hits;
        }
        Cursor cursor = null;
        try {
            cursor = db.query(HITS_TABLE, new String[]{HIT_ID, HIT_TIME, HIT_FIRST_DISPATCH_TIME}, (String) null, (String[]) null, (String) null, (String) null, String.format("%s ASC", new Object[]{HIT_ID}), Integer.toString(maxHits));
            List<Hit> hits2 = new ArrayList<>();
            try {
                if (cursor.moveToFirst()) {
                    do {
                        hits2.add(new Hit(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2)));
                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
                int count = 0;
                try {
                    SQLiteDatabase sQLiteDatabase = db;
                    cursor = sQLiteDatabase.query(HITS_TABLE, new String[]{HIT_ID, HIT_URL}, (String) null, (String[]) null, (String) null, (String) null, String.format("%s ASC", new Object[]{HIT_ID}), Integer.toString(maxHits));
                    if (cursor.moveToFirst()) {
                        do {
                            if (((SQLiteCursor) cursor).getWindow().getNumRows() > 0) {
                                hits2.get(count).setHitUrl(cursor.getString(1));
                            } else {
                                Log.w(String.format("HitString for hitId %d too large.  Hit will be deleted.", new Object[]{Long.valueOf(hits2.get(count).getHitId())}));
                            }
                            count++;
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    List<Hit> list = hits2;
                    return hits2;
                } catch (SQLiteException e) {
                    Log.w("Error in peekHits fetching hit url: " + e.getMessage());
                    List<Hit> partialHits = new ArrayList<>();
                    boolean foundOneBadHit = false;
                    for (Hit hit : hits2) {
                        if (TextUtils.isEmpty(hit.getHitUrl())) {
                            if (foundOneBadHit) {
                                break;
                            }
                            foundOneBadHit = true;
                        }
                        partialHits.add(hit);
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    List<Hit> list2 = hits2;
                    return partialHits;
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (SQLiteException e2) {
                e = e2;
                hits = hits2;
                try {
                    Log.w("Error in peekHits fetching hitIds: " + e.getMessage());
                    if (cursor != null) {
                        cursor.close();
                    }
                    return hits;
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                List<Hit> list3 = hits2;
                if (cursor != null) {
                }
                throw th;
            }
        } catch (SQLiteException e3) {
            e = e3;
            Log.w("Error in peekHits fetching hitIds: " + e.getMessage());
            if (cursor != null) {
            }
            return hits;
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setLastDeleteStaleHitsTime(long timeInMilliseconds) {
        this.mLastDeleteStaleHitsTime = timeInMilliseconds;
    }

    /* access modifiers changed from: package-private */
    public int deleteStaleHits() {
        boolean z = true;
        long now = this.mClock.currentTimeMillis();
        if (now <= this.mLastDeleteStaleHitsTime + 86400000) {
            return 0;
        }
        this.mLastDeleteStaleHitsTime = now;
        SQLiteDatabase db = getWritableDatabase("Error opening database for deleteStaleHits.");
        if (db == null) {
            return 0;
        }
        int rslt = db.delete(HITS_TABLE, "HIT_TIME < ?", new String[]{Long.toString(this.mClock.currentTimeMillis() - 2592000000L)});
        HitStoreStateListener hitStoreStateListener = this.mListener;
        if (getNumStoredHits() != 0) {
            z = false;
        }
        hitStoreStateListener.reportStoreIsEmpty(z);
        return rslt;
    }

    /* access modifiers changed from: package-private */
    public void deleteHits(String[] hitIds) {
        SQLiteDatabase db;
        boolean z = true;
        if (hitIds != null && hitIds.length != 0 && (db = getWritableDatabase("Error opening database for deleteHits.")) != null) {
            try {
                db.delete(HITS_TABLE, String.format("HIT_ID in (%s)", new Object[]{TextUtils.join(",", Collections.nCopies(hitIds.length, "?"))}), hitIds);
                HitStoreStateListener hitStoreStateListener = this.mListener;
                if (getNumStoredHits() != 0) {
                    z = false;
                }
                hitStoreStateListener.reportStoreIsEmpty(z);
            } catch (SQLiteException e) {
                Log.w("Error deleting hits");
            }
        }
    }

    /* access modifiers changed from: private */
    public void deleteHit(long hitId) {
        deleteHits(new String[]{String.valueOf(hitId)});
    }

    /* access modifiers changed from: private */
    public void setHitFirstDispatchTime(long hitId, long firstDispatchTime) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db != null) {
            ContentValues cv = new ContentValues();
            cv.put(HIT_FIRST_DISPATCH_TIME, Long.valueOf(firstDispatchTime));
            try {
                db.update(HITS_TABLE, cv, HIT_ID_WHERE_CLAUSE, new String[]{String.valueOf(hitId)});
            } catch (SQLiteException e) {
                Log.w("Error setting HIT_FIRST_DISPATCH_TIME for hitId: " + hitId);
                deleteHit(hitId);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getNumStoredHits() {
        int numStoredHits = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            Cursor cursor2 = db.rawQuery("SELECT COUNT(*) from gtm_hits", (String[]) null);
            if (cursor2.moveToFirst()) {
                numStoredHits = (int) cursor2.getLong(0);
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        } catch (SQLiteException e) {
            Log.w("Error getting numStoredHits");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return numStoredHits;
    }

    /* access modifiers changed from: package-private */
    public int getNumStoredUntriedHits() {
        int numStoredHits = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredHits.");
        if (db == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            cursor = db.query(HITS_TABLE, new String[]{HIT_ID, HIT_FIRST_DISPATCH_TIME}, "hit_first_send_time=0", (String[]) null, (String) null, (String) null, (String) null);
            numStoredHits = cursor.getCount();
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLiteException e) {
            Log.w("Error getting num untried hits");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return numStoredHits;
    }

    public void dispatch() {
        Log.v("GTM Dispatch running...");
        if (this.mDispatcher.okToDispatch()) {
            List<Hit> hits = peekHits(40);
            if (hits.isEmpty()) {
                Log.v("...nothing to dispatch");
                this.mListener.reportStoreIsEmpty(true);
                return;
            }
            this.mDispatcher.dispatchHits(hits);
            if (getNumStoredUntriedHits() > 0) {
                ServiceManagerImpl.getInstance().dispatch();
            }
        }
    }

    @VisibleForTesting
    class StoreDispatchListener implements SimpleNetworkDispatcher.DispatchListener {
        StoreDispatchListener() {
        }

        public void onHitDispatched(Hit hit) {
            PersistentHitStore.this.deleteHit(hit.getHitId());
        }

        public void onHitPermanentDispatchFailure(Hit hit) {
            PersistentHitStore.this.deleteHit(hit.getHitId());
            Log.v("Permanent failure dispatching hitId: " + hit.getHitId());
        }

        public void onHitTransientDispatchFailure(Hit hit) {
            long firstDispatchTime = hit.getHitFirstDispatchTime();
            if (firstDispatchTime == 0) {
                PersistentHitStore.this.setHitFirstDispatchTime(hit.getHitId(), PersistentHitStore.this.mClock.currentTimeMillis());
            } else if (PersistentHitStore.HIT_DISPATCH_RETRY_WINDOW + firstDispatchTime < PersistentHitStore.this.mClock.currentTimeMillis()) {
                PersistentHitStore.this.deleteHit(hit.getHitId());
                Log.v("Giving up on failed hitId: " + hit.getHitId());
            }
        }
    }

    public Dispatcher getDispatcher() {
        return this.mDispatcher;
    }

    public void close() {
        try {
            this.mDbHelper.getWritableDatabase().close();
            this.mDispatcher.close();
        } catch (SQLiteException e) {
            Log.w("Error opening database for close");
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public UrlDatabaseHelper getHelper() {
        return this.mDbHelper;
    }

    private SQLiteDatabase getWritableDatabase(String errorMessage) {
        try {
            return this.mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.w(errorMessage);
            return null;
        }
    }

    @VisibleForTesting
    class UrlDatabaseHelper extends SQLiteOpenHelper {
        private boolean mBadDatabase;
        private long mLastDatabaseCheckTime = 0;

        /* access modifiers changed from: package-private */
        public boolean isBadDatabase() {
            return this.mBadDatabase;
        }

        /* access modifiers changed from: package-private */
        public void setBadDatabase(boolean badDatabase) {
            this.mBadDatabase = badDatabase;
        }

        UrlDatabaseHelper(Context context, String databaseName) {
            super(context, databaseName, (SQLiteDatabase.CursorFactory) null, 1);
        }

        private boolean tablePresent(String table, SQLiteDatabase db) {
            Cursor cursor = null;
            try {
                cursor = db.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{table}, (String) null, (String) null, (String) null);
                boolean moveToFirst = cursor.moveToFirst();
                if (cursor == null) {
                    return moveToFirst;
                }
                cursor.close();
                return moveToFirst;
            } catch (SQLiteException e) {
                Log.w("Error querying for table " + table);
                if (cursor != null) {
                    cursor.close();
                }
                return false;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }

        public SQLiteDatabase getWritableDatabase() {
            if (!this.mBadDatabase || this.mLastDatabaseCheckTime + 3600000 <= PersistentHitStore.this.mClock.currentTimeMillis()) {
                SQLiteDatabase db = null;
                this.mBadDatabase = true;
                this.mLastDatabaseCheckTime = PersistentHitStore.this.mClock.currentTimeMillis();
                try {
                    db = super.getWritableDatabase();
                } catch (SQLiteException e) {
                    PersistentHitStore.this.mContext.getDatabasePath(PersistentHitStore.this.mDatabaseName).delete();
                }
                if (db == null) {
                    db = super.getWritableDatabase();
                }
                this.mBadDatabase = false;
                return db;
            }
            throw new SQLiteException("Database creation failed");
        }

        public void onOpen(SQLiteDatabase db) {
            if (Build.VERSION.SDK_INT < 15) {
                Cursor cursor = db.rawQuery("PRAGMA journal_mode=memory", (String[]) null);
                try {
                    cursor.moveToFirst();
                } finally {
                    cursor.close();
                }
            }
            if (!tablePresent(PersistentHitStore.HITS_TABLE, db)) {
                db.execSQL(PersistentHitStore.CREATE_HITS_TABLE);
            } else {
                validateColumnsPresent(db);
            }
        }

        /* JADX INFO: finally extract failed */
        private void validateColumnsPresent(SQLiteDatabase db) {
            Cursor c = db.rawQuery("SELECT * FROM gtm_hits WHERE 0", (String[]) null);
            Set<String> columns = new HashSet<>();
            try {
                String[] columnNames = c.getColumnNames();
                for (String add : columnNames) {
                    columns.add(add);
                }
                c.close();
                if (!columns.remove(PersistentHitStore.HIT_ID) || !columns.remove(PersistentHitStore.HIT_URL) || !columns.remove(PersistentHitStore.HIT_TIME) || !columns.remove(PersistentHitStore.HIT_FIRST_DISPATCH_TIME)) {
                    throw new SQLiteException("Database column missing");
                } else if (!columns.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                }
            } catch (Throwable th) {
                c.close();
                throw th;
            }
        }

        public void onCreate(SQLiteDatabase db) {
            FutureApis.setOwnerOnlyReadWrite(db.getPath());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
