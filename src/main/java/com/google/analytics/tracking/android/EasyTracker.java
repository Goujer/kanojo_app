package com.google.analytics.tracking.android;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.google.analytics.tracking.android.GAUsage;
import com.google.analytics.tracking.android.Logger;
import com.google.android.gms.common.util.VisibleForTesting;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class EasyTracker extends Tracker {
    private static final int DEFAULT_SAMPLE_RATE = 100;
    private static final String EASY_TRACKER_NAME = "easy_tracker";
    static final int NUM_MILLISECONDS_TO_WAIT_FOR_OPEN_ACTIVITY = 1000;
    private static EasyTracker sInstance;
    private static String sResourcePackageName;
    private int mActivitiesActive;
    private final Map<String, String> mActivityNameMap;
    private Clock mClock;
    private Context mContext;
    private final GoogleAnalytics mGoogleAnalytics;
    private boolean mIsAutoActivityTracking;
    private boolean mIsInForeground;
    private boolean mIsReportUncaughtExceptionsEnabled;
    private long mLastOnStopTime;
    private ParameterLoader mParameterFetcher;
    private ServiceManager mServiceManager;
    private long mSessionTimeout;
    private boolean mStartSessionOnNextSend;
    private Timer mTimer;
    private TimerTask mTimerTask;

    private EasyTracker(Context ctx) {
        this(ctx, new ParameterLoaderImpl(ctx), GoogleAnalytics.getInstance(ctx), GAServiceManager.getInstance(), null);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    private EasyTracker(Context ctx, ParameterLoader parameterLoader, GoogleAnalytics ga, ServiceManager serviceManager, TrackerHandler handler) {
        super(EASY_TRACKER_NAME, null, handler == null ? ga : handler);
        this.mIsAutoActivityTracking = false;
        this.mActivitiesActive = 0;
        this.mActivityNameMap = new HashMap();
        this.mIsInForeground = false;
        this.mStartSessionOnNextSend = false;
        if (sResourcePackageName != null) {
            parameterLoader.setResourcePackageName(sResourcePackageName);
        }
        this.mGoogleAnalytics = ga;
        setContext(ctx, parameterLoader, serviceManager);
        this.mClock = new Clock() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
    }

    public static EasyTracker getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new EasyTracker(ctx);
        }
        return sInstance;
    }

//    @VisibleForTesting
//    static EasyTracker getNewInstance(Context ctx, ParameterLoader parameterLoader, GoogleAnalytics ga, ServiceManager serviceManager, TrackerHandler handler) {
//        sInstance = new EasyTracker(ctx, parameterLoader, ga, serviceManager, handler);
//        return sInstance;
//    }

//    private boolean checkForNewSession() {
//        return this.mSessionTimeout == 0 || (this.mSessionTimeout > 0 && this.mClock.currentTimeMillis() > this.mLastOnStopTime + this.mSessionTimeout);
//    }

    private void loadParameters() {
        Logger.LogLevel logLevel;
        Log.v("Starting EasyTracker.");
        String trackingId = this.mParameterFetcher.getString("ga_trackingId");
        if (TextUtils.isEmpty(trackingId)) {
            trackingId = this.mParameterFetcher.getString("ga_api_key");
        }
        set(Fields.TRACKING_ID, trackingId);
        Log.v("[EasyTracker] trackingId loaded: " + trackingId);
        String appName = this.mParameterFetcher.getString("ga_appName");
        if (!TextUtils.isEmpty(appName)) {
            Log.v("[EasyTracker] app name loaded: " + appName);
            set(Fields.APP_NAME, appName);
        }
        String appVersion = this.mParameterFetcher.getString("ga_appVersion");
        if (appVersion != null) {
            Log.v("[EasyTracker] app version loaded: " + appVersion);
            set(Fields.APP_VERSION, appVersion);
        }
        String logLevelString = this.mParameterFetcher.getString("ga_logLevel");
        if (!(logLevelString == null || (logLevel = getLogLevelFromString(logLevelString)) == null)) {
            Log.v("[EasyTracker] log level loaded: " + logLevel);
            this.mGoogleAnalytics.getLogger().setLogLevel(logLevel);
        }
        Double sampleRate = this.mParameterFetcher.getDoubleFromString("ga_sampleFrequency");
        if (sampleRate == null) {
            sampleRate = (double) this.mParameterFetcher.getInt("ga_sampleRate", 100);
        }
        if (sampleRate != 100.0d) {
            set(Fields.SAMPLE_RATE, Double.toString(sampleRate));
        }
        Log.v("[EasyTracker] sample rate loaded: " + sampleRate);
        int dispatchPeriod = this.mParameterFetcher.getInt("ga_dispatchPeriod", 1800);
        Log.v("[EasyTracker] dispatch period loaded: " + dispatchPeriod);
        this.mServiceManager.setLocalDispatchPeriod(dispatchPeriod);
        this.mSessionTimeout = (long) (this.mParameterFetcher.getInt("ga_sessionTimeout", 30) * 1000);
        Log.v("[EasyTracker] session timeout loaded: " + this.mSessionTimeout);
        this.mIsAutoActivityTracking = this.mParameterFetcher.getBoolean("ga_autoActivityTracking") || this.mParameterFetcher.getBoolean("ga_auto_activity_tracking");
        Log.v("[EasyTracker] auto activity tracking loaded: " + this.mIsAutoActivityTracking);
        boolean isAnonymizeIpEnabled = this.mParameterFetcher.getBoolean("ga_anonymizeIp");
        if (isAnonymizeIpEnabled) {
            set(Fields.ANONYMIZE_IP, GreeDefs.KANOJO_NAME);
            Log.v("[EasyTracker] anonymize ip loaded: " + isAnonymizeIpEnabled);
        }
        this.mIsReportUncaughtExceptionsEnabled = this.mParameterFetcher.getBoolean("ga_reportUncaughtExceptions");
        if (this.mIsReportUncaughtExceptionsEnabled) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionReporter(this, this.mServiceManager, Thread.getDefaultUncaughtExceptionHandler(), this.mContext));
            Log.v("[EasyTracker] report uncaught exceptions loaded: " + this.mIsReportUncaughtExceptionsEnabled);
        }
        this.mGoogleAnalytics.setDryRun(this.mParameterFetcher.getBoolean("ga_dryRun"));
    }

    private Logger.LogLevel getLogLevelFromString(String logLevelString) {
        try {
            return Logger.LogLevel.valueOf(logLevelString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

//    @VisibleForTesting
//    void overrideUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
//        if (this.mIsReportUncaughtExceptionsEnabled) {
//            Thread.setDefaultUncaughtExceptionHandler(handler);
//        }
//    }

    private void setContext(Context ctx, ParameterLoader parameterLoader, ServiceManager serviceManager) {
        if (ctx == null) {
            Log.e("Context cannot be null");
        }
        this.mContext = ctx.getApplicationContext();
        this.mServiceManager = serviceManager;
        this.mParameterFetcher = parameterLoader;
        loadParameters();
    }

//    public void activityStart(Activity activity) {
//        GAUsage.getInstance().setUsage(GAUsage.Field.EASY_TRACKER_ACTIVITY_START);
//        clearExistingTimer();
//        if (!this.mIsInForeground && this.mActivitiesActive == 0 && checkForNewSession()) {
//            this.mStartSessionOnNextSend = true;
//        }
//        this.mIsInForeground = true;
//        this.mActivitiesActive++;
//        if (this.mIsAutoActivityTracking) {
//            Map<String, String> params = new HashMap<>();
//            params.put(Fields.HIT_TYPE, HitTypes.APP_VIEW);
//            GAUsage.getInstance().setDisableUsage(true);
//            set("&cd", getActivityName(activity));
//            send(params);
//            GAUsage.getInstance().setDisableUsage(false);
//        }
//    }

//    public void activityStop(Activity activity) {
//        GAUsage.getInstance().setUsage(GAUsage.Field.EASY_TRACKER_ACTIVITY_STOP);
//        this.mActivitiesActive--;
//        this.mActivitiesActive = Math.max(0, this.mActivitiesActive);
//        this.mLastOnStopTime = this.mClock.currentTimeMillis();
//        if (this.mActivitiesActive == 0) {
//            clearExistingTimer();
//            this.mTimerTask = new NotInForegroundTimerTask();
//            this.mTimer = new Timer("waitForActivityStart");
//            this.mTimer.schedule(this.mTimerTask, 1000);
//        }
//    }

    @Deprecated
    public void dispatchLocalHits() {
        this.mServiceManager.dispatchLocalHits();
    }

//    private synchronized void clearExistingTimer() {
//        if (this.mTimer != null) {
//            this.mTimer.cancel();
//            this.mTimer = null;
//        }
//    }

//    private String getActivityName(Activity activity) {
//        String canonicalName = activity.getClass().getCanonicalName();
//        if (this.mActivityNameMap.containsKey(canonicalName)) {
//            return this.mActivityNameMap.get(canonicalName);
//        }
//        String name = this.mParameterFetcher.getString(canonicalName);
//        if (name == null) {
//            name = canonicalName;
//        }
//        this.mActivityNameMap.put(canonicalName, name);
//        return name;
//    }
//
//    @VisibleForTesting
//    void setClock(Clock clock) {
//        this.mClock = clock;
//    }
//
//    @VisibleForTesting
//    public int getActivitiesActive() {
//        return this.mActivitiesActive;
//    }

    public void send(Map<String, String> params) {
        if (this.mStartSessionOnNextSend) {
            params.put(Fields.SESSION_CONTROL, "start");
            this.mStartSessionOnNextSend = false;
        }
        super.send(params);
    }

//    public static void setResourcePackageName(String resourcePackageName) {
//        sResourcePackageName = resourcePackageName;
//    }
//
//    private class NotInForegroundTimerTask extends TimerTask {
//        private NotInForegroundTimerTask() {
//        }
//
//        public void run() {
//            boolean unused = EasyTracker.this.mIsInForeground = false;
//        }
//    }
}
