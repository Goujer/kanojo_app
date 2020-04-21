package com.google.ads.internal;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.l;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public final class f implements Runnable {
    private final l a;
    private final a b;
    private volatile boolean c;
    private boolean d;
    private String e;
    private Thread f;

    public interface a {
        HttpURLConnection a(URL url) throws IOException;
    }

    f(l lVar) {
        this(lVar, new a() {
            public HttpURLConnection a(URL url) throws IOException {
                return (HttpURLConnection) url.openConnection();
            }
        });
    }

    f(l lVar, a aVar) {
        this.f = null;
        this.a = lVar;
        this.b = aVar;
    }

    /* access modifiers changed from: package-private */
    public void a() {
        this.c = true;
    }

    private void a(HttpURLConnection httpURLConnection) {
        b(httpURLConnection);
        f(httpURLConnection);
        g(httpURLConnection);
        h(httpURLConnection);
        i(httpURLConnection);
        e(httpURLConnection);
        j(httpURLConnection);
        k(httpURLConnection);
        l(httpURLConnection);
        d(httpURLConnection);
        c(httpURLConnection);
        m(httpURLConnection);
        n(httpURLConnection);
    }

    private void b(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Debug-Dialog");
        if (!TextUtils.isEmpty(headerField)) {
            this.a.b.a().f(headerField);
        }
    }

    private void c(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("Content-Type");
        if (!TextUtils.isEmpty(headerField)) {
            this.a.b.a().b(headerField);
        }
    }

    private void d(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Mediation");
        if (!TextUtils.isEmpty(headerField)) {
            this.a.b.a().b(Boolean.valueOf(headerField).booleanValue());
        }
    }

    private void e(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Interstitial-Timeout");
        if (!TextUtils.isEmpty(headerField)) {
            try {
                this.a.a.a().b.a().a((long) (Float.parseFloat(headerField) * 1000.0f));
            } catch (NumberFormatException e2) {
                b.d("Could not get timeout value: " + headerField, e2);
            }
        }
    }

    private void f(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Tracking-Urls");
        if (!TextUtils.isEmpty(headerField)) {
            for (String b2 : headerField.trim().split("\\s+")) {
                this.a.a.a().b.a().b(b2);
            }
        }
    }

    private void g(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Manual-Tracking-Urls");
        if (!TextUtils.isEmpty(headerField)) {
            for (String c2 : headerField.trim().split("\\s+")) {
                this.a.a.a().b.a().c(c2);
            }
        }
    }

    private void h(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Click-Tracking-Urls");
        if (!TextUtils.isEmpty(headerField)) {
            for (String a2 : headerField.trim().split("\\s+")) {
                this.a.b.a().a(a2);
            }
        }
    }

    private void i(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Refresh-Rate");
        if (!TextUtils.isEmpty(headerField)) {
            try {
                float parseFloat = Float.parseFloat(headerField);
                d a2 = this.a.a.a().b.a();
                if (parseFloat > 0.0f) {
                    a2.a(parseFloat);
                    if (!a2.t()) {
                        a2.g();
                    }
                } else if (a2.t()) {
                    a2.f();
                }
            } catch (NumberFormatException e2) {
                b.d("Could not get refresh value: " + headerField, e2);
            }
        }
    }

    private void j(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Orientation");
        if (TextUtils.isEmpty(headerField)) {
            return;
        }
        if (headerField.equals("portrait")) {
            this.a.b.a().a(AdUtil.b());
        } else if (headerField.equals("landscape")) {
            this.a.b.a().a(AdUtil.a());
        }
    }

    private void k(HttpURLConnection httpURLConnection) {
        if (!TextUtils.isEmpty(httpURLConnection.getHeaderField("X-Afma-Doritos-Cache-Life"))) {
            try {
                this.a.a.a().b.a().b(Long.parseLong(httpURLConnection.getHeaderField("X-Afma-Doritos-Cache-Life")));
            } catch (NumberFormatException e2) {
                b.e("Got bad value of Doritos cookie cache life from header: " + httpURLConnection.getHeaderField("X-Afma-Doritos-Cache-Life") + ". Using default value instead.");
            }
        }
    }

    public void a(boolean z) {
        this.d = z;
    }

    private void l(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("Cache-Control");
        if (!TextUtils.isEmpty(headerField)) {
            this.a.b.a().c(headerField);
        }
    }

    private void m(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Ad-Size");
        if (!TextUtils.isEmpty(headerField)) {
            try {
                String[] split = headerField.split("x", 2);
                if (split.length != 2) {
                    b.e("Could not parse size header: " + headerField);
                    return;
                }
                this.a.b.a().a(new AdSize(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            } catch (NumberFormatException e2) {
                b.e("Could not parse size header: " + headerField);
            }
        }
    }

    private void n(HttpURLConnection httpURLConnection) {
        String headerField = httpURLConnection.getHeaderField("X-Afma-Disable-Activation-And-Scroll");
        if (!TextUtils.isEmpty(headerField)) {
            this.a.b.a().a(headerField.equals(GreeDefs.KANOJO_NAME));
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void a(String str) {
        if (this.f == null) {
            this.e = str;
            this.c = false;
            this.f = new Thread(this);
            this.f.start();
        }
    }

    private void a(HttpURLConnection httpURLConnection, int i) throws IOException {
        if (300 <= i && i < 400) {
            String headerField = httpURLConnection.getHeaderField("Location");
            if (headerField == null) {
                b.c("Could not get redirect location from a " + i + " redirect.");
                this.a.b.a().a(AdRequest.ErrorCode.INTERNAL_ERROR);
                a();
                return;
            }
            a(httpURLConnection);
            this.e = headerField;
        } else if (i == 200) {
            a(httpURLConnection);
            String trim = AdUtil.a((Readable) new InputStreamReader(httpURLConnection.getInputStream())).trim();
            b.a("Response content is: " + trim);
            if (TextUtils.isEmpty(trim)) {
                b.a("Response message is null or zero length: " + trim);
                this.a.b.a().a(AdRequest.ErrorCode.NO_FILL);
                a();
                return;
            }
            this.a.b.a().a(trim, this.e);
            a();
        } else if (i == 400) {
            b.c("Bad request");
            this.a.b.a().a(AdRequest.ErrorCode.INVALID_REQUEST);
            a();
        } else {
            b.c("Invalid response code: " + i);
            this.a.b.a().a(AdRequest.ErrorCode.INTERNAL_ERROR);
            a();
        }
    }

    public void run() {
        try {
            b();
        } catch (MalformedURLException e2) {
            b.b("Received malformed ad url from javascript.", e2);
            this.a.b.a().a(AdRequest.ErrorCode.INTERNAL_ERROR);
        } catch (IOException e3) {
            b.b("IOException connecting to ad url.", e3);
            this.a.b.a().a(AdRequest.ErrorCode.NETWORK_ERROR);
        } catch (Throwable th) {
            b.b("An unknown error occurred in AdResponseLoader.", th);
            this.a.b.a().a(AdRequest.ErrorCode.INTERNAL_ERROR);
        }
    }

    private void b() throws MalformedURLException, IOException {
        while (!this.c) {
            HttpURLConnection a2 = this.b.a(new URL(this.e));
            try {
                a(this.a.a.a().f.a(), a2);
                AdUtil.a(a2, this.a.a.a().f.a());
                a2.setInstanceFollowRedirects(false);
                a2.connect();
                a(a2, a2.getResponseCode());
            } finally {
                a2.disconnect();
            }
        }
    }

    private void a(Context context, HttpURLConnection httpURLConnection) {
        String string = PreferenceManager.getDefaultSharedPreferences(context).getString("drt", "");
        if (this.d && !TextUtils.isEmpty(string)) {
            if (AdUtil.a == 8) {
                httpURLConnection.addRequestProperty("X-Afma-drt-Cookie", string);
            } else {
                httpURLConnection.addRequestProperty("Cookie", string);
            }
        }
    }
}
