package jp.gree.reward.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedMap;
import java.util.TreeMap;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.gree.reward.compress.c;
import jp.gree.reward.compress.i;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import twitter4j.conf.PropertyConfiguration;

public class GreeRewardPromotionOption {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private Context f19a;

    /* renamed from: a  reason: collision with other field name */
    private String f20a;
    private int b = 0;

    /* renamed from: b  reason: collision with other field name */
    private String f21b;

    GreeRewardPromotionOption(Context context) {
        this.f19a = context.getApplicationContext();
        try {
            ApplicationInfo applicationInfo = this.f19a.getPackageManager().getApplicationInfo(this.f19a.getPackageName(), 128);
            i.b(i.a(applicationInfo, "DEVELOP_MODE"));
            i.a(i.a(applicationInfo, "TEST_MODE"));
            this.f20a = i.a(applicationInfo, "SITE_ID");
            this.f21b = i.a(applicationInfo, "SITE_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            throw new c(e);
        }
    }

    public Integer getActionStatus(int i) {
        this.b = i;
        Integer num = new Integer(-1);
        if (i.a(this.f19a)) {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpParams params = defaultHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            TreeMap treeMap = new TreeMap();
            String a2 = i.a((TelephonyManager) this.f19a.getSystemService("phone"));
            treeMap.put("MEDIA_ID".toLowerCase(), String.valueOf(this.a));
            treeMap.put(PropertyConfiguration.USER, i.a(a2));
            treeMap.put("CAMPAIGN_ID".toLowerCase(), String.valueOf(this.b));
            String str = "" + i.a() + GreeDefs.COMPANY_NAME + "." + GreeDefs.BARCODE + "." + this.f20a + "la?" + i.a((SortedMap) treeMap, this.f21b);
            i.a("actionStatusUrl: ", str);
            try {
                HttpResponse execute = defaultHttpClient.execute(new HttpGet(str));
                int statusCode = execute.getStatusLine().getStatusCode();
                i.a("action status: ", String.valueOf(statusCode));
                if (statusCode == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(execute.getEntity().getContent()));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        sb.append(readLine);
                    }
                    String sb2 = sb.toString();
                    if (sb2.length() == 0) {
                        return null;
                    }
                    try {
                        return Integer.valueOf(sb2);
                    } catch (NumberFormatException e) {
                        i.a("GreeRewardPromotionOption", "getActionStatus", e);
                        return num;
                    }
                }
            } catch (ClientProtocolException e2) {
                i.a("GreeRewardPromotionOption", "getActionStatus", e2);
                return num;
            } catch (IOException e3) {
                i.a("GreeRewardPromotionOption", "getActionStatus", e3);
            }
        }
        return num;
    }

    public void setMediaId(int i) {
        this.a = i;
    }
}
