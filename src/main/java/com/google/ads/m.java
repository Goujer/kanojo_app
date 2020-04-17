package com.google.ads;

import android.os.Handler;
import android.os.Looper;
import com.google.ads.util.i;

public class m extends i {
    private static final m d = new m();
    public final i.c<String> a = new i.c<>("marketPackages", null);
    public final i.b<a> b = new i.b<>("constants", new a());
    public final i.b<Handler> c = new i.b<>("uiHandler", new Handler(Looper.getMainLooper()));

    public static final class a extends i {
        public final i.c<String> a = new i.c<>("ASDomains", null);
        public final i.c<Integer> b = new i.c<>("minHwAccelerationVersionBanner", 18);
        public final i.c<Integer> c = new i.c<>("minHwAccelerationVersionOverlay", 18);
        public final i.c<Integer> d = new i.c<>("minHwAccelerationVersionOverlay", 14);
        public final i.c<String> e = new i.c<>("mraidBannerPath", "http://media.admob.com/mraid/v1/mraid_app_banner.js");
        public final i.c<String> f = new i.c<>("mraidExpandedBannerPath", "http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js");
        public final i.c<String> g = new i.c<>("mraidInterstitialPath", "http://media.admob.com/mraid/v1/mraid_app_interstitial.js");
        public final i.c<String> h = new i.c<>("badAdReportPath", "https://badad.googleplex.com/s/reportAd");
        public final i.c<Long> i = new i.c<>("appCacheMaxSize", 0L);
        public final i.c<Long> j = new i.c<>("appCacheMaxSizePaddingInBytes", 131072L);
        public final i.c<Long> k = new i.c<>("maxTotalAppCacheQuotaInBytes", 5242880L);
        public final i.c<Long> l = new i.c<>("maxTotalDatabaseQuotaInBytes", 5242880L);
        public final i.c<Long> m = new i.c<>("maxDatabaseQuotaPerOriginInBytes", 1048576L);
        public final i.c<Long> n = new i.c<>("databaseQuotaIncreaseStepInBytes", 131072L);
        public final i.c<Boolean> o = new i.c<>("isInitialized", false);
    }

    public static m a() {
        return d;
    }

    private m() {
    }
}
