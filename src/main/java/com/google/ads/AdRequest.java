package com.google.ads;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import com.google.ads.doubleclick.DfpExtras;
import com.google.ads.mediation.NetworkExtras;
import com.google.ads.mediation.admob.AdMobAdapterExtras;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdRequest {
    public static final String LOGTAG = "Ads";
    public static final String TEST_EMULATOR = AdUtil.b("emulator");
    public static final String VERSION = "6.4.1";
    private static final SimpleDateFormat a = new SimpleDateFormat("yyyyMMdd");
    private static Method b;
    private static Method c;
    private Gender d = null;
    private Date e = null;
    private Set<String> f = null;
    private Map<String, Object> g = null;
    private final Map<Class<?>, NetworkExtras> h = new HashMap();
    private Location i = null;
    private boolean j = false;
    private boolean k = false;
    private Set<String> l = null;

    public enum Gender {
        UNKNOWN,
        MALE,
        FEMALE
    }

    static {
        b = null;
        c = null;
        try {
            for (Method method : Class.forName("com.google.analytics.tracking.android.AdMobInfo").getMethods()) {
                if (method.getName().equals("getInstance") && method.getParameterTypes().length == 0) {
                    b = method;
                } else if (method.getName().equals("getJoinIds") && method.getParameterTypes().length == 0) {
                    c = method;
                }
            }
            if (b == null || c == null) {
                b = null;
                c = null;
                b.e("No Google Analytics: Library Incompatible.");
            }
        } catch (ClassNotFoundException e2) {
            b.a("No Google Analytics: Library Not Found.");
        } catch (Throwable th) {
            b.a("No Google Analytics: Error Loading Library");
        }
    }

    public enum ErrorCode {
        INVALID_REQUEST("Invalid Ad request."),
        NO_FILL("Ad request successful, but no ad returned due to lack of ad inventory."),
        NETWORK_ERROR("A network error occurred."),
        INTERNAL_ERROR("There was an internal error.");
        
        private final String a;

        private ErrorCode(String description) {
            this.a = description;
        }

        public String toString() {
            return this.a;
        }
    }

    public AdRequest setGender(Gender gender) {
        this.d = gender;
        return this;
    }

    public Gender getGender() {
        return this.d;
    }

    @Deprecated
    public AdRequest setBirthday(String birthday) {
        if (birthday == "" || birthday == null) {
            this.e = null;
        } else {
            try {
                this.e = a.parse(birthday);
            } catch (ParseException e2) {
                b.e("Birthday format invalid.  Expected 'YYYYMMDD' where 'YYYY' is a 4 digit year, 'MM' is a two digit month, and 'DD' is a two digit day.  Birthday value ignored");
                this.e = null;
            }
        }
        return this;
    }

    public AdRequest setBirthday(Date birthday) {
        if (birthday == null) {
            this.e = null;
        } else {
            this.e = new Date(birthday.getTime());
        }
        return this;
    }

    public AdRequest setBirthday(Calendar calendar) {
        if (calendar == null) {
            this.e = null;
        } else {
            setBirthday(calendar.getTime());
        }
        return this;
    }

    public Date getBirthday() {
        return this.e;
    }

    public AdRequest clearBirthday() {
        this.e = null;
        return this;
    }

    @Deprecated
    public AdRequest setPlusOneOptOut(boolean plusOneOptOut) {
        a().setPlusOneOptOut(plusOneOptOut);
        return this;
    }

    @Deprecated
    public boolean getPlusOneOptOut() {
        return a().getPlusOneOptOut();
    }

    public AdRequest setKeywords(Set<String> keywords) {
        this.f = keywords;
        return this;
    }

    public AdRequest addKeyword(String keyword) {
        if (this.f == null) {
            this.f = new HashSet();
        }
        this.f.add(keyword);
        return this;
    }

    public AdRequest addKeywords(Set<String> keywords) {
        if (this.f == null) {
            this.f = new HashSet();
        }
        this.f.addAll(keywords);
        return this;
    }

    public Set<String> getKeywords() {
        if (this.f == null) {
            return null;
        }
        return Collections.unmodifiableSet(this.f);
    }

    private synchronized AdMobAdapterExtras a() {
        if (getNetworkExtras(AdMobAdapterExtras.class) == null) {
            setNetworkExtras(new AdMobAdapterExtras());
        }
        return (AdMobAdapterExtras) getNetworkExtras(AdMobAdapterExtras.class);
    }

    @Deprecated
    public AdRequest setExtras(Map<String, Object> extras) {
        a().setExtras(extras);
        return this;
    }

    @Deprecated
    public AdRequest addExtra(String key, Object value) {
        AdMobAdapterExtras a2 = a();
        if (a2.getExtras() == null) {
            a2.setExtras(new HashMap());
        }
        a2.getExtras().put(key, value);
        return this;
    }

    public AdRequest setNetworkExtras(NetworkExtras extras) {
        if (extras != null) {
            this.h.put(extras.getClass(), extras);
        }
        return this;
    }

    public AdRequest removeNetworkExtras(Class<?> extrasClass) {
        this.h.remove(extrasClass);
        return this;
    }

    public <T> T getNetworkExtras(Class<T> extrasClass) {
        return this.h.get(extrasClass);
    }

    public AdRequest setMediationExtras(Map<String, Object> mediationExtras) {
        this.g = mediationExtras;
        return this;
    }

    public AdRequest addMediationExtra(String key, Object value) {
        if (this.g == null) {
            this.g = new HashMap();
        }
        this.g.put(key, value);
        return this;
    }

    public AdRequest setLocation(Location location) {
        this.i = location;
        return this;
    }

    public Location getLocation() {
        return this.i;
    }

    @Deprecated
    public AdRequest setTesting(boolean testing) {
        this.j = testing;
        return this;
    }

    public Map<String, Object> getRequestMap(Context context) {
        String str;
        HashMap hashMap = new HashMap();
        if (this.f != null) {
            hashMap.put("kw", this.f);
        }
        if (this.d != null) {
            hashMap.put("cust_gender", Integer.valueOf(this.d.ordinal()));
        }
        if (this.e != null) {
            hashMap.put("cust_age", a.format(this.e));
        }
        if (this.i != null) {
            hashMap.put("uule", AdUtil.a(this.i));
        }
        if (this.j) {
            hashMap.put("testing", 1);
        }
        if (isTestDevice(context)) {
            hashMap.put("adtest", "on");
        } else if (!this.k) {
            if (AdUtil.c()) {
                str = "AdRequest.TEST_EMULATOR";
            } else {
                str = "\"" + AdUtil.a(context) + "\"";
            }
            b.c("To get test ads on this device, call adRequest.addTestDevice(" + str + ");");
            this.k = true;
        }
        AdMobAdapterExtras adMobAdapterExtras = (AdMobAdapterExtras) getNetworkExtras(AdMobAdapterExtras.class);
        DfpExtras dfpExtras = (DfpExtras) getNetworkExtras(DfpExtras.class);
        if (dfpExtras != null && dfpExtras.getExtras() != null && !dfpExtras.getExtras().isEmpty()) {
            hashMap.put("extras", dfpExtras.getExtras());
        } else if (!(adMobAdapterExtras == null || adMobAdapterExtras.getExtras() == null || adMobAdapterExtras.getExtras().isEmpty())) {
            hashMap.put("extras", adMobAdapterExtras.getExtras());
        }
        if (dfpExtras != null) {
            String publisherProvidedId = dfpExtras.getPublisherProvidedId();
            if (!TextUtils.isEmpty(publisherProvidedId)) {
                hashMap.put("ppid", publisherProvidedId);
            }
        }
        if (this.g != null) {
            hashMap.put("mediation_extras", this.g);
        }
        try {
            if (b != null) {
                Map map = (Map) c.invoke(b.invoke((Object) null, new Object[0]), new Object[0]);
                if (map != null && map.size() > 0) {
                    hashMap.put("analytics_join_id", map);
                }
            }
        } catch (Throwable th) {
            b.c("Internal Analytics Error:", th);
        }
        return hashMap;
    }

    public AdRequest addTestDevice(String testDevice) {
        if (this.l == null) {
            this.l = new HashSet();
        }
        this.l.add(testDevice);
        return this;
    }

    public AdRequest setTestDevices(Set<String> testDevices) {
        this.l = testDevices;
        return this;
    }

    public boolean isTestDevice(Context context) {
        String a2;
        if (this.l == null || (a2 = AdUtil.a(context)) == null || !this.l.contains(a2)) {
            return false;
        }
        return true;
    }
}
