package com.google.ads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.ads.util.b;
import java.lang.ref.WeakReference;
import java.util.Date;

public final class at {
    private static final com.google.ads.internal.a a = com.google.ads.internal.a.a.b();

    private static class a implements Runnable {
        private final WeakReference<Activity> a;
        private final SharedPreferences.Editor b;

        public a(Activity activity) {
            this(activity, (SharedPreferences.Editor) null);
        }

        a(Activity activity, SharedPreferences.Editor editor) {
            this.a = new WeakReference<>(activity);
            this.b = editor;
        }

        private SharedPreferences.Editor a(Context context) {
            if (this.b == null) {
                return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit();
            }
            return this.b;
        }

        public void run() {
            String str;
            try {
                Activity activity = (Activity) this.a.get();
                if (activity == null) {
                    b.a("Activity was null while making a doritos cookie request.");
                    return;
                }
                Cursor query = activity.getContentResolver().query(as.a, as.b, (String) null, (String[]) null, (String) null);
                if (query == null || !query.moveToFirst() || query.getColumnNames().length <= 0) {
                    b.a("Google+ app not installed, not storing doritos cookie");
                    str = null;
                } else {
                    str = query.getString(query.getColumnIndex(query.getColumnName(0)));
                }
                SharedPreferences.Editor a2 = a(activity);
                if (!TextUtils.isEmpty(str)) {
                    a2.putString("drt", str);
                    a2.putLong("drt_ts", new Date().getTime());
                } else {
                    a2.putString("drt", "");
                    a2.putLong("drt_ts", 0);
                }
                a2.commit();
            } catch (Throwable th) {
                b.d("An unknown error occurred while sending a doritos request.", th);
            }
        }
    }

    public static boolean a(final Context context, long j) {
        if (!a(context, j, PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()))) {
            return false;
        }
        new Thread(new Runnable() {
            public void run() {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit();
                edit.putString("drt", "");
                edit.putLong("drt_ts", 0);
                edit.commit();
            }
        }).start();
        return true;
    }

    static boolean a(Context context, long j, SharedPreferences sharedPreferences) {
        if (!sharedPreferences.contains("drt") || !sharedPreferences.contains("drt_ts") || sharedPreferences.getLong("drt_ts", 0) < new Date().getTime() - j) {
            return true;
        }
        return false;
    }

    public static void a(Activity activity) {
        new Thread(new a(activity)).start();
    }
}
