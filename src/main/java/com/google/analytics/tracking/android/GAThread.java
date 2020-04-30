package com.google.analytics.tracking.android;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

class GAThread extends Thread implements AnalyticsThread {
    private static final String CLIENT_VERSION = "ma3.0.1";
    private static final int MAX_SAMPLE_RATE = 100;
    private static final int SAMPLE_RATE_MODULO = 10000;
    private static final int SAMPLE_RATE_MULTIPLIER = 100;
    private static GAThread sInstance;
    private volatile String mClientId;
    private volatile boolean mClosed = false;
    private volatile ArrayList<Command> mCommands;
    private final Context mContext;
    private volatile boolean mDisabled = false;
    private volatile String mInstallCampaign;
    private volatile ServiceProxy mServiceProxy;
    private final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    static GAThread getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new GAThread(ctx);
        }
        return sInstance;
    }

    private GAThread(Context ctx) {
        super("GAThread");
        if (ctx != null) {
            this.mContext = ctx.getApplicationContext();
        } else {
            this.mContext = ctx;
        }
        start();
    }

//    @VisibleForTesting
//    GAThread(Context ctx, ServiceProxy proxy) {
//        super("GAThread");
//        if (ctx != null) {
//            this.mContext = ctx.getApplicationContext();
//        } else {
//            this.mContext = ctx;
//        }
//        this.mServiceProxy = proxy;
//        start();
//    }

    @VisibleForTesting
    protected void init() {
        this.mServiceProxy.createService();
        this.mCommands = new ArrayList();
        this.mCommands.add(new Command(Command.APPEND_VERSION, "&_v".substring(1), CLIENT_VERSION));
        this.mCommands.add(new Command(Command.APPEND_QUEUE_TIME, "&qt".substring(1), (String) null));
        this.mCommands.add(new Command(Command.APPEND_CACHE_BUSTER, "&z".substring(1), (String) null));
    }

    public void sendHit(Map<String, String> hit) {
        final Map<String, String> hitCopy = new HashMap<>(hit);
        String hitTime = hit.get("&ht");
        if (hitTime != null) {
            try {
				Long.valueOf(hitTime);
            } catch (NumberFormatException e) {
                hitTime = null;
            }
        }
        if (hitTime == null) {
            hitCopy.put("&ht", Long.toString(System.currentTimeMillis()));
        }
        queueToThread(new Runnable() {
            public void run() {
                if (TextUtils.isEmpty(hitCopy.get(Fields.CLIENT_ID))) {
                    hitCopy.put(Fields.CLIENT_ID, GAThread.this.mClientId);
                }
                if (!GoogleAnalytics.getInstance(GAThread.this.mContext).getAppOptOut() && !GAThread.this.isSampledOut(hitCopy)) {
                    if (!TextUtils.isEmpty(GAThread.this.mInstallCampaign)) {
                        GAUsage.getInstance().setDisableUsage(true);
                        hitCopy.putAll(new MapBuilder().setCampaignParamsFromUrl(GAThread.this.mInstallCampaign).build());
                        GAUsage.getInstance().setDisableUsage(false);
                        String unused = GAThread.this.mInstallCampaign = null;
                    }
                    GAThread.this.fillAppParameters(hitCopy);
                    GAThread.this.mServiceProxy.putHit(HitBuilder.generateHitParams(hitCopy), Long.parseLong(hitCopy.get("&ht")), GAThread.this.getUrlScheme(hitCopy), GAThread.this.mCommands);
                }
            }
        });
    }

    private String getUrlScheme(Map<String, String> hit) {
        return (!hit.containsKey(Fields.USE_SECURE) || Utils.safeParseBoolean(hit.get(Fields.USE_SECURE), true)) ? "https:" : "http:";
    }

    private boolean isSampledOut(Map<String, String> hit) {
        if (hit.get(Fields.SAMPLE_RATE) == null) {
            return false;
        }
        double sampleRate = Utils.safeParseDouble(hit.get(Fields.SAMPLE_RATE), MAX_SAMPLE_RATE);
        if (sampleRate >= MAX_SAMPLE_RATE) {
            return false;
        }
        if (((double) (hashClientIdForSampling(hit.get(Fields.CLIENT_ID)) % SAMPLE_RATE_MODULO)) < SAMPLE_RATE_MULTIPLIER * sampleRate) {
            return false;
        }
        Log.v(String.format("%s hit sampled out", hit.get(Fields.HIT_TYPE) == null ? "unknown" : hit.get(Fields.HIT_TYPE)));
        return true;
    }

    @VisibleForTesting
	private static int hashClientIdForSampling(String clientId) {
        int hashVal = 1;
        if (!TextUtils.isEmpty(clientId)) {
            hashVal = 0;
            for (int charPos = clientId.length() - 1; charPos >= 0; charPos--) {
                char curChar = clientId.charAt(charPos);
                hashVal = ((hashVal << 6) & 65535) + curChar + (curChar << 14);
                int lefMost7 = hashVal & 266338304;
                if (lefMost7 != 0) {
                    hashVal ^= lefMost7 >> 21;
                }
            }
        }
        return hashVal;
    }

    private void fillAppParameters(Map<String, String> hit) {
        DefaultProvider appFieldsProvider = AppFieldsDefaultProvider.getProvider();
        Utils.putIfAbsent(hit, Fields.APP_NAME, appFieldsProvider.getValue(Fields.APP_NAME));
        Utils.putIfAbsent(hit, Fields.APP_VERSION, appFieldsProvider.getValue(Fields.APP_VERSION));
        Utils.putIfAbsent(hit, Fields.APP_ID, appFieldsProvider.getValue(Fields.APP_ID));
        Utils.putIfAbsent(hit, Fields.APP_INSTALLER_ID, appFieldsProvider.getValue(Fields.APP_INSTALLER_ID));
        hit.put("&v", "1");
    }

    public void dispatch() {
        queueToThread(new Runnable() {
            public void run() {
                GAThread.this.mServiceProxy.dispatch();
            }
        });
    }

    public void clearHits() {
        queueToThread(new Runnable() {
            public void run() {
                GAThread.this.mServiceProxy.clearHits();
            }
        });
    }

    public void setForceLocalDispatch() {
        queueToThread(new Runnable() {
            public void run() {
                GAThread.this.mServiceProxy.setForceLocalDispatch();
            }
        });
    }

    @VisibleForTesting
	private void queueToThread(Runnable r) {
        this.queue.add(r);
    }

    @VisibleForTesting
	private static String getAndClearCampaign(Context context) {
        try {
            FileInputStream input = context.openFileInput("gaInstallData");
            byte[] inputBytes = new byte[8192];
            int readLen = input.read(inputBytes, 0, 8192);
            if (input.available() > 0) {
                Log.e("Too much campaign data, ignoring it.");
                input.close();
                context.deleteFile("gaInstallData");
                return null;
            }
            input.close();
            context.deleteFile("gaInstallData");
            if (readLen <= 0) {
                Log.w("Campaign file is empty.");
                return null;
            }
            String campaignString = new String(inputBytes, 0, readLen);
            Log.i("Campaign found: " + campaignString);
            return campaignString;
        } catch (FileNotFoundException e) {
            Log.i("No campaign data found.");
            return null;
        } catch (IOException e2) {
            Log.e("Error reading campaign data.");
            context.deleteFile("gaInstallData");
            return null;
        }
    }

    private String printStackTrace(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);
        t.printStackTrace(stream);
        stream.flush();
        return new String(baos.toByteArray());
    }

    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.w("sleep interrupted in GAThread initialize");
        }
        try {
            if (this.mServiceProxy == null) {
                this.mServiceProxy = new GAServiceProxy(this.mContext, this);
            }
            init();
            this.mClientId = ClientIdDefaultProvider.getProvider().getValue(Fields.CLIENT_ID);
            this.mInstallCampaign = getAndClearCampaign(this.mContext);
        } catch (Throwable t) {
            Log.e("Error initializing the GAThread: " + printStackTrace(t));
            Log.e("Google Analytics will not start up.");
            this.mDisabled = true;
        }
        while (!this.mClosed) {
            try {
                Runnable r = this.queue.take();
                if (!this.mDisabled) {
                    r.run();
                }
            } catch (InterruptedException e2) {
                Log.i(e2.toString());
            } catch (Throwable t2) {
                Log.e("Error on GAThread: " + printStackTrace(t2));
                Log.e("Google Analytics is shutting down.");
                this.mDisabled = true;
            }
        }
    }

    public LinkedBlockingQueue<Runnable> getQueue() {
        return this.queue;
    }

    public Thread getThread() {
        return this;
    }

//    @VisibleForTesting
//    void close() {
//        this.mClosed = true;
//        interrupt();
//    }

//    @VisibleForTesting
//    boolean isDisabled() {
//        return this.mDisabled;
//    }
}
