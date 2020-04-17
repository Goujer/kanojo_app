package com.google.ads.internal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.ads.AdActivity;
import com.google.ads.AdSize;
import com.google.ads.ak;
import com.google.ads.n;
import com.google.ads.util.AdUtil;
import com.google.ads.util.IcsUtil;
import com.google.ads.util.b;
import com.google.ads.util.g;
import com.google.ads.util.h;
import java.lang.ref.WeakReference;

public class AdWebView extends WebView {
    protected final n a;
    private WeakReference<AdActivity> b = null;
    private AdSize c;
    private boolean d = false;
    private boolean e = false;
    private boolean f = false;

    public AdWebView(n slotState, AdSize adSize) {
        super(slotState.f.a());
        this.a = slotState;
        this.c = adSize;
        setBackgroundColor(0);
        AdUtil.a((WebView) this);
        WebSettings settings = getSettings();
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.parse(url), mimeType);
                    AdActivity i = AdWebView.this.i();
                    if (i != null && AdUtil.a(intent, (Context) i)) {
                        i.startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    b.a("Couldn't find an Activity to view url/mimetype: " + url + " / " + mimeType);
                } catch (Throwable th) {
                    b.b("Unknown error trying to start activity to view URL: " + url, th);
                }
            }
        });
        if (AdUtil.a >= 17) {
            h.a(settings, slotState);
        } else if (AdUtil.a >= 11) {
            g.a(settings, slotState);
        }
        setScrollBarStyle(33554432);
        if (AdUtil.a >= 14) {
            setWebChromeClient(new IcsUtil.a(slotState));
        } else if (AdUtil.a >= 11) {
            setWebChromeClient(new g.a(slotState));
        }
    }

    public void f() {
        AdActivity i = i();
        if (i != null) {
            i.finish();
        }
    }

    public void g() {
        if (AdUtil.a >= 11) {
            g.a((View) this);
        }
        this.e = true;
    }

    public void h() {
        if (this.e && AdUtil.a >= 11) {
            g.b(this);
        }
        this.e = false;
    }

    public AdActivity i() {
        if (this.b != null) {
            return (AdActivity) this.b.get();
        }
        return null;
    }

    public boolean j() {
        return this.f;
    }

    public boolean k() {
        return this.e;
    }

    public void setAdActivity(AdActivity adActivity) {
        this.b = new WeakReference<>(adActivity);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        try {
            super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        } catch (Throwable th) {
            b.d("An error occurred while loading data in AdWebView:", th);
        }
    }

    public void loadUrl(String url) {
        try {
            super.loadUrl(url);
        } catch (Throwable th) {
            b.d("An error occurred while loading a URL in AdWebView:", th);
        }
    }

    public void stopLoading() {
        try {
            super.stopLoading();
        } catch (Throwable th) {
            b.d("An error occurred while stopping loading in AdWebView:", th);
        }
    }

    public void destroy() {
        try {
            super.destroy();
        } catch (Throwable th) {
            b.d("An error occurred while destroying an AdWebView:", th);
        }
        try {
            setWebViewClient(new WebViewClient());
        } catch (Throwable th2) {
        }
    }

    public synchronized void setAdSize(AdSize adSize) {
        this.c = adSize;
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int i2 = Integer.MAX_VALUE;
        synchronized (this) {
            if (isInEditMode()) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else if (this.c == null || this.d) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                int mode = View.MeasureSpec.getMode(widthMeasureSpec);
                int size = View.MeasureSpec.getSize(widthMeasureSpec);
                int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
                int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
                float f2 = getContext().getResources().getDisplayMetrics().density;
                int width = (int) (((float) this.c.getWidth()) * f2);
                int height = (int) (((float) this.c.getHeight()) * f2);
                if (mode == Integer.MIN_VALUE || mode == 1073741824) {
                    i = size;
                } else {
                    i = Integer.MAX_VALUE;
                }
                if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                    i2 = size2;
                }
                if (((float) width) - (f2 * 6.0f) > ((float) i) || height > i2) {
                    b.b("Not enough space to show ad! Wants: <" + width + ", " + height + ">, Has: <" + size + ", " + size2 + ">");
                    setVisibility(8);
                    setMeasuredDimension(size, size2);
                } else {
                    setMeasuredDimension(width, height);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        ak a2 = this.a.r.a();
        if (a2 != null) {
            a2.a(event);
        }
        return super.onTouchEvent(event);
    }

    public void setCustomClose(boolean useCustomClose) {
        AdActivity adActivity;
        this.f = useCustomClose;
        if (this.b != null && (adActivity = (AdActivity) this.b.get()) != null) {
            adActivity.setCustomClose(useCustomClose);
        }
    }

    public void setIsExpandedMraid(boolean isCurrentlyExpandedMraid) {
        this.d = isCurrentlyExpandedMraid;
    }

    public void a(boolean z) {
        if (z) {
            setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getAction() == 2;
                }
            });
        } else {
            setOnTouchListener((View.OnTouchListener) null);
        }
    }
}
