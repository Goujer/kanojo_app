package jp.gree.reward.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.io.IOException;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.gree.reward.compress.b;
import jp.gree.reward.compress.c;
import jp.gree.reward.compress.i;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import twitter4j.conf.PropertyConfiguration;

public final class GreeRewardAction {
    private int a = 0;

    /* renamed from: a  reason: collision with other field name */
    private Context f1a;

    /* renamed from: a  reason: collision with other field name */
    private String f2a;

    /* renamed from: a  reason: collision with other field name */
    private b f3a;
    private int b = 0;

    /* renamed from: b  reason: collision with other field name */
    private String f4b;
    private String c;
    private String d;
    private String e;

    GreeRewardAction(Context context) {
        this.f1a = context.getApplicationContext();
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            i.b(i.a(applicationInfo, "DEVELOP_MODE"));
            i.a(i.a(applicationInfo, "TEST_MODE"));
            if (!i.b()) {
                i.a("GreeRewarAction", "This OS version not supported!");
                return;
            }
            this.f2a = i.a(applicationInfo, "SITE_ID");
            this.f4b = i.a(applicationInfo, "SITE_KEY");
            this.f3a = (b) Enum.valueOf(b.class, i.a(applicationInfo, "MARKET"));
            a("SITE_KEY", this.f4b);
            a("SITE_ID", this.f2a);
            if (this.f3a == null) {
                i.a("GreeRewarAction", "MARKET is null.");
            }
        } catch (PackageManager.NameNotFoundException e2) {
            throw new c(e2);
        }
    }

    private static void a(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            i.a("GreeRewarAction", String.valueOf(str) + " is null or empty.");
        }
    }

    public final boolean sendAction(int i, String str) {
        boolean z;
        if (!i.b()) {
            i.a("GreeRewarAction", "This OS version not supported!");
            return false;
        } else if (i == 0 || str.length() == 0) {
            i.b("GreeRewarAction", "campaignId or advertisement is empty stirng.");
            return false;
        } else {
            this.a = i;
            this.c = str;
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.f1a);
            this.e = "_" + this.f2a + "_" + this.a + "_" + this.c + "_" + i.c();
            StringBuilder sb = new StringBuilder("greereward_");
            sb.append("action_success");
            sb.append(this.e);
            i.a("actionSuccessKey", sb.toString());
            if (defaultSharedPreferences.getBoolean(sb.toString(), false)) {
                i.a("actionInfo", "action data already send");
                return true;
            }
            if (this.f3a != b.GOOGLE) {
                z = false;
            } else {
                StringBuilder sb2 = new StringBuilder("greereward_");
                sb2.append("limit_of_return_app");
                sb2.append(this.e);
                i.a("limitKey", sb2.toString());
                String string = defaultSharedPreferences.getString(sb2.toString(), (String) null);
                z = string == null ? false : i.a(string).compareTo(new Date()) != -1;
            }
            if (z) {
                return false;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) this.f1a.getSystemService("connectivity");
            if (connectivityManager.getActiveNetworkInfo() != null ? connectivityManager.getActiveNetworkInfo().isConnected() : false) {
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                HttpParams params = defaultHttpClient.getParams();
                HttpClientParams.setRedirecting(params, false);
                HttpConnectionParams.setConnectionTimeout(params, 10000);
                HttpConnectionParams.setSoTimeout(params, 10000);
                TreeMap treeMap = new TreeMap();
                TelephonyManager telephonyManager = (TelephonyManager) this.f1a.getSystemService("phone");
                String b2 = i.b(telephonyManager.getNetworkOperatorName());
                String b3 = i.b(telephonyManager.getSimOperatorName());
                String a2 = i.a(telephonyManager);
                StringBuilder sb3 = new StringBuilder("greereward_");
                sb3.append("send_time");
                sb3.append(this.e);
                SharedPreferences defaultSharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(this.f1a);
                String string2 = defaultSharedPreferences2.getString(sb3.toString(), (String) null);
                if (string2 == null) {
                    string2 = i.b();
                    SharedPreferences.Editor edit = defaultSharedPreferences2.edit();
                    edit.putString(sb3.toString(), string2);
                    edit.commit();
                }
                treeMap.put("user", i.a(a2));
                treeMap.put("carrier", b2);
                treeMap.put("_spn", b3);
                treeMap.put("CAMPAIGN_ID".toLowerCase(), String.valueOf(this.a));
                treeMap.put("ADVERTISEMENT".toLowerCase(), this.c);
                treeMap.put("system", Build.VERSION.RELEASE);
                treeMap.put("model", Build.MODEL);
                treeMap.put("send_time", string2);
                if (i.a()) {
                    treeMap.put("privileged", GreeDefs.KANOJO_NAME);
                }
                if (this.d != null) {
                    treeMap.put("refresh", this.b + ";URL=" + this.d);
                }
                String str2 = "" + i.a() + GreeDefs.COMPANY_NAME + "." + GreeDefs.BARCODE + "." + this.f2a + "a?" + i.a((SortedMap) treeMap, this.f4b);
                i.a("actionUrl: ", str2);
                try {
                    HttpResponse execute = defaultHttpClient.execute(new HttpGet(str2));
                    int statusCode = execute.getStatusLine().getStatusCode();
                    i.a("action status: ", String.valueOf(statusCode));
                    if (statusCode >= 400) {
                        SharedPreferences.Editor edit2 = defaultSharedPreferences.edit();
                        edit2.putBoolean(sb.toString(), false);
                        edit2.commit();
                        return false;
                    } else if (statusCode == 307) {
                        String value = execute.getFirstHeader("Location").getValue();
                        i.a("GreeRewarAction", "307 redirect location " + value);
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(value));
                        intent.setFlags(268435456);
                        this.f1a.startActivity(intent);
                        return false;
                    } else {
                        Header firstHeader = execute.getFirstHeader("Retry-After");
                        if (firstHeader != null) {
                            int intValue = Integer.valueOf(firstHeader.getValue()).intValue();
                            StringBuilder sb4 = new StringBuilder("greereward_");
                            sb4.append("limit_of_return_app");
                            sb4.append(this.e);
                            i.a("limitKey", sb4.toString());
                            String a3 = i.a(new Date(new Date().getTime() + ((long) (intValue * 1000))));
                            SharedPreferences.Editor edit3 = defaultSharedPreferences.edit();
                            edit3.putString(sb4.toString(), a3);
                            edit3.commit();
                            i.a("retry-after: ", String.valueOf(intValue));
                            return false;
                        }
                        SharedPreferences.Editor edit4 = defaultSharedPreferences.edit();
                        edit4.putBoolean(sb.toString(), true);
                        edit4.commit();
                        return true;
                    }
                } catch (ClientProtocolException e2) {
                    i.a("GreeRewarAction", "sendAction", e2);
                    return false;
                } catch (IOException e3) {
                    i.a("GreeRewarAction", "sendAction", e3);
                    Toast.makeText(this.f1a, "Network unreachable.", 0).show();
                    return false;
                }
            } else {
                Toast.makeText(this.f1a, "Network disconnected.", 0).show();
                return false;
            }
        }
    }

    public final boolean sendAction(int i, String str, int i2, String str2) {
        this.b = i2;
        this.d = str2;
        return sendAction(i, str);
    }
}
