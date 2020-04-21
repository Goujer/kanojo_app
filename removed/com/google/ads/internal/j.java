package com.google.ads.internal;

import android.content.Context;
import com.google.ads.m;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class j implements Runnable {
    private String a;
    private Context b;

    public j(String str, Context context) {
        this.a = str;
        this.b = context;
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection a(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setInstanceFollowRedirects(true);
        AdUtil.a(httpURLConnection, this.b);
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        return httpURLConnection;
    }

    /* access modifiers changed from: protected */
    public BufferedOutputStream a(HttpURLConnection httpURLConnection) throws IOException {
        return new BufferedOutputStream(httpURLConnection.getOutputStream());
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void run() {
        HttpURLConnection a2;
        try {
            a2 = a(new URL(m.a().b.a().h.a()));
            byte[] bytes = new a(this.a).a().toString().getBytes();
            a2.setFixedLengthStreamingMode(bytes.length);
            BufferedOutputStream a3 = a(a2);
            a3.write(bytes);
            a3.close();
            if (a2.getResponseCode() != 200) {
                b.b("Got error response from BadAd backend: " + a2.getResponseMessage());
            }
            a2.disconnect();
        } catch (IOException e) {
            b.b("Error reporting bad ad.", e);
        } catch (Throwable th) {
            a2.disconnect();
            throw th;
        }
    }

    public static class a {
        private final String a;

        public a(String str) {
            this.a = str;
        }

        public JSONObject a() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("debugHeader", this.a);
            } catch (JSONException e) {
                b.b("Could not build ReportAdJson from inputs.", e);
            }
            return jSONObject;
        }
    }
}
