package com.facebook;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.facebook.Request;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONException;

public final class Settings {
    private static final String ANALYTICS_EVENT = "event";
    private static final String APPLICATION_FIELDS = "fields";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final Uri ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    private static final String ATTRIBUTION_KEY = "attribution";
    private static final String ATTRIBUTION_PREFERENCES = "com.facebook.sdk.attributionTracking";
    private static final int DEFAULT_CORE_POOL_SIZE = 5;
    private static final int DEFAULT_KEEP_ALIVE = 1;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 128;
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(0);

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "FacebookSdk #" + this.counter.incrementAndGet());
        }
    };
    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new LinkedBlockingQueue(10);
    private static final Object LOCK = new Object();
    private static final String MOBILE_INSTALL_EVENT = "MOBILE_APP_INSTALL";
    private static final String PUBLISH_ACTIVITY_PATH = "%s/activities";
    private static final String SUPPORTS_ATTRIBUTION = "supports_attribution";
    private static volatile Executor executor;
    private static final HashSet<LoggingBehavior> loggingBehaviors = new HashSet<>();
    private static volatile boolean shouldAutoPublishInstall;

    public static final Set<LoggingBehavior> getLoggingBehaviors() {
        Set<LoggingBehavior> unmodifiableSet;
        synchronized (loggingBehaviors) {
            unmodifiableSet = Collections.unmodifiableSet(new HashSet(loggingBehaviors));
        }
        return unmodifiableSet;
    }

    public static final void addLoggingBehavior(LoggingBehavior behavior) {
        synchronized (loggingBehaviors) {
            loggingBehaviors.add(behavior);
        }
    }

    public static final void removeLoggingBehavior(LoggingBehavior behavior) {
        synchronized (loggingBehaviors) {
            loggingBehaviors.remove(behavior);
        }
    }

    public static final void clearLoggingBehaviors() {
        synchronized (loggingBehaviors) {
            loggingBehaviors.clear();
        }
    }

    public static final boolean isLoggingBehaviorEnabled(LoggingBehavior behavior) {
        synchronized (loggingBehaviors) {
        }
        return false;
    }

    public static Executor getExecutor() {
        synchronized (LOCK) {
            if (executor == null) {
                Executor executor2 = getAsyncTaskExecutor();
                if (executor2 == null) {
                    executor2 = new ThreadPoolExecutor(5, 128, 1, TimeUnit.SECONDS, DEFAULT_WORK_QUEUE, DEFAULT_THREAD_FACTORY);
                }
                executor = executor2;
            }
        }
        return executor;
    }

    public static void setExecutor(Executor executor2) {
        Validate.notNull(executor2, "executor");
        synchronized (LOCK) {
            executor = executor2;
        }
    }

    private static Executor getAsyncTaskExecutor() {
        try {
            Field executorField = AsyncTask.class.getField("THREAD_POOL_EXECUTOR");
            if (executorField == null) {
                return null;
            }
            try {
                Object executorObject = executorField.get((Object) null);
                if (executorObject == null) {
                    return null;
                }
                if (!(executorObject instanceof Executor)) {
                    return null;
                }
                return (Executor) executorObject;
            } catch (IllegalAccessException e) {
                return null;
            }
        } catch (NoSuchFieldException e2) {
            return null;
        }
    }

    public static void publishInstallAsync(Context context, final String applicationId) {
        final Context applicationContext = context.getApplicationContext();
        getExecutor().execute(new Runnable() {
            public void run() {
                Settings.publishInstallAndWait(applicationContext, applicationId);
            }
        });
    }

    public static void setShouldAutoPublishInstall(boolean shouldAutoPublishInstall2) {
        shouldAutoPublishInstall = shouldAutoPublishInstall2;
    }

    public static boolean getShouldAutoPublishInstall() {
        return shouldAutoPublishInstall;
    }

    public static boolean publishInstallAndWait(Context context, String applicationId) {
        if (applicationId == null) {
            return false;
        }
        try {
            String attributionId = getAttributionId(context.getContentResolver());
            SharedPreferences preferences = context.getSharedPreferences(ATTRIBUTION_PREFERENCES, 0);
            String pingKey = String.valueOf(applicationId) + "ping";
            long lastPing = preferences.getLong(pingKey, 0);
            if (lastPing == 0 && attributionId != null) {
                Bundle supportsAttributionParams = new Bundle();
                supportsAttributionParams.putString(APPLICATION_FIELDS, SUPPORTS_ATTRIBUTION);
                Request pingRequest = Request.newGraphPathRequest((Session) null, applicationId, (Request.Callback) null);
                pingRequest.setParameters(supportsAttributionParams);
                Object doesSupportAttribution = pingRequest.executeAndWait().getGraphObject().getProperty(SUPPORTS_ATTRIBUTION);
                if (!(doesSupportAttribution instanceof Boolean)) {
                    throw new JSONException(String.format("%s contains %s instead of a Boolean", new Object[]{SUPPORTS_ATTRIBUTION, doesSupportAttribution}));
                } else if (((Boolean) doesSupportAttribution).booleanValue()) {
                    GraphObject publishParams = GraphObject.Factory.create();
                    publishParams.setProperty("event", MOBILE_INSTALL_EVENT);
                    publishParams.setProperty(ATTRIBUTION_KEY, attributionId);
                    Request.newPostRequest((Session) null, String.format(PUBLISH_ACTIVITY_PATH, new Object[]{applicationId}), publishParams, (Request.Callback) null).executeAndWait();
                    SharedPreferences.Editor editor = preferences.edit();
                    lastPing = System.currentTimeMillis();
                    editor.putLong(pingKey, lastPing);
                    editor.commit();
                }
            }
            return lastPing != 0;
        } catch (Exception e) {
            Utility.logd("Facebook-publish", e.getMessage());
            return false;
        }
    }

    public static String getAttributionId(ContentResolver contentResolver) {
        ContentResolver contentResolver2 = contentResolver;
        Cursor c = contentResolver2.query(ATTRIBUTION_ID_CONTENT_URI, new String[]{"aid"}, (String) null, (String[]) null, (String) null);
        if (c == null || !c.moveToFirst()) {
            return null;
        }
        String string = c.getString(c.getColumnIndex("aid"));
        c.close();
        return string;
    }
}
