package com.google.ads.util;

import android.annotation.TargetApi;
import android.view.View;
import android.webkit.WebChromeClient;
import com.google.ads.AdSize;
import com.google.ads.internal.AdWebView;
import com.google.ads.n;
import com.google.ads.util.g;

@TargetApi(14)
public class IcsUtil {

    public static class a extends g.a {
        public a(n nVar) {
            super(nVar);
        }

        public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
            callback.onCustomViewHidden();
        }
    }

    public static class IcsAdWebView extends AdWebView {
        public IcsAdWebView(n slotState, AdSize adSize) {
            super(slotState, adSize);
        }

        public boolean canScrollHorizontally(int direction) {
            if (this.a.e.a() != null) {
                return !this.a.e.a().b();
            }
            return super.canScrollHorizontally(direction);
        }

        public boolean canScrollVertically(int direction) {
            if (this.a.e.a() != null) {
                return !this.a.e.a().b();
            }
            return super.canScrollVertically(direction);
        }
    }
}
