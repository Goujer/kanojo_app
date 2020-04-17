package com.google.ads;

import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.ActivationOverlay;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.d;
import com.google.ads.m;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.g;
import com.google.ads.util.i;
import java.util.HashMap;

public class x implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        boolean z;
        n i = dVar.i();
        m.a a = i.d.a().b.a();
        c(hashMap, "as_domains", a.a);
        c(hashMap, "bad_ad_report_path", a.h);
        a(hashMap, "min_hwa_banner", a.b);
        a(hashMap, "min_hwa_activation_overlay", a.c);
        a(hashMap, "min_hwa_overlay", a.d);
        c(hashMap, "mraid_banner_path", a.e);
        c(hashMap, "mraid_expanded_banner_path", a.f);
        c(hashMap, "mraid_interstitial_path", a.g);
        b(hashMap, "ac_max_size", a.i);
        b(hashMap, "ac_padding", a.j);
        b(hashMap, "ac_total_quota", a.k);
        b(hashMap, "db_total_quota", a.l);
        b(hashMap, "db_quota_per_origin", a.m);
        b(hashMap, "db_quota_step_size", a.n);
        AdWebView l = dVar.l();
        if (AdUtil.a >= 11) {
            g.a(l.getSettings(), i);
            g.a(webView.getSettings(), i);
        }
        if (!i.g.a().a()) {
            boolean k = l.k();
            boolean z2 = AdUtil.a < a.b.a().intValue();
            if (!z2 && k) {
                b.a("Re-enabling hardware acceleration for a banner after reading constants.");
                l.h();
            } else if (z2 && !k) {
                b.a("Disabling hardware acceleration for a banner after reading constants.");
                l.g();
            }
        }
        ActivationOverlay a2 = i.e.a();
        if (!i.g.a().b() && a2 != null) {
            boolean k2 = a2.k();
            if (AdUtil.a < a.c.a().intValue()) {
                z = true;
            } else {
                z = false;
            }
            if (!z && k2) {
                b.a("Re-enabling hardware acceleration for an activation overlay after reading constants.");
                a2.h();
            } else if (z && !k2) {
                b.a("Disabling hardware acceleration for an activation overlay after reading constants.");
                a2.g();
            }
        }
        String a3 = a.a.a();
        al a4 = i.s.a();
        if (a4 != null && !TextUtils.isEmpty(a3)) {
            a4.a(a3);
        }
        a.o.a(true);
    }

    private void a(HashMap<String, String> hashMap, String str, i.c<Integer> cVar) {
        try {
            String str2 = hashMap.get(str);
            if (!TextUtils.isEmpty(str2)) {
                cVar.a(Integer.valueOf(str2));
            }
        } catch (NumberFormatException e) {
            b.a("Could not parse \"" + str + "\" constant.");
        }
    }

    private void b(HashMap<String, String> hashMap, String str, i.c<Long> cVar) {
        try {
            String str2 = hashMap.get(str);
            if (!TextUtils.isEmpty(str2)) {
                cVar.a(Long.valueOf(str2));
            }
        } catch (NumberFormatException e) {
            b.a("Could not parse \"" + str + "\" constant.");
        }
    }

    private void c(HashMap<String, String> hashMap, String str, i.c<String> cVar) {
        String str2 = hashMap.get(str);
        if (!TextUtils.isEmpty(str2)) {
            cVar.a(str2);
        }
    }
}
