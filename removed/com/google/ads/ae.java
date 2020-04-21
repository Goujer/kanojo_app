package com.google.ads;

import android.content.Context;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ae implements Runnable {
    private final Context a;
    private final String b;

    public ae(String str, Context context) {
        this.b = str;
        this.a = context;
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection a(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void run() {
        HttpURLConnection a2;
        try {
            b.a("Pinging URL: " + this.b);
            a2 = a(new URL(this.b));
            AdUtil.a(a2, this.a);
            a2.setInstanceFollowRedirects(true);
            a2.connect();
            int responseCode = a2.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                b.e("Did not receive 2XX (got " + responseCode + ") from pinging URL: " + this.b);
            }
            a2.disconnect();
        } catch (Throwable th) {
            b.d("Unable to ping the URL: " + this.b, th);
        }
    }
}
