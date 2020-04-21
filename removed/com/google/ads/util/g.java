package com.google.ads.util;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.ads.AdActivity;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.c;
import com.google.ads.internal.d;
import com.google.ads.internal.i;
import com.google.ads.m;
import com.google.ads.n;
import com.google.ads.o;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@TargetApi(11)
public class g {

    public static class b extends i {
        public b(d dVar, Map<String, o> map, boolean z, boolean z2) {
            super(dVar, map, z, z2);
        }

        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            try {
                if ("mraid.js".equalsIgnoreCase(new File(url).getName())) {
                    c k = this.a.k();
                    if (k != null) {
                        k.c(true);
                    } else {
                        this.a.a(true);
                    }
                    m.a a = this.a.i().d.a().b.a();
                    if (this.a.i().b()) {
                        String a2 = a.g.a();
                        b.a("shouldInterceptRequest(" + a2 + ")");
                        return a(a2, view.getContext());
                    } else if (this.b) {
                        String a3 = a.f.a();
                        b.a("shouldInterceptRequest(" + a3 + ")");
                        return a(a3, view.getContext());
                    } else {
                        String a4 = a.e.a();
                        b.a("shouldInterceptRequest(" + a4 + ")");
                        return a(a4, view.getContext());
                    }
                }
            } catch (IOException e) {
                b.d("IOException fetching MRAID JS.", e);
            } catch (Throwable th) {
                b.d("An unknown error occurred fetching MRAID JS.", th);
            }
            return super.shouldInterceptRequest(view, url);
        }

        private static WebResourceResponse a(String str, Context context) throws IOException {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                AdUtil.a(httpURLConnection, context.getApplicationContext());
                httpURLConnection.connect();
                return new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(AdUtil.a((Readable) new InputStreamReader(httpURLConnection.getInputStream())).getBytes("UTF-8")));
            } finally {
                httpURLConnection.disconnect();
            }
        }
    }

    public static class a extends WebChromeClient {
        private final n a;

        public a(n nVar) {
            this.a = nVar;
        }

        public void onCloseWindow(WebView webView) {
            if (webView instanceof AdWebView) {
                ((AdWebView) webView).f();
            }
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String str = "JS: " + consoleMessage.message() + " (" + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + ")";
            switch (AnonymousClass1.a[consoleMessage.messageLevel().ordinal()]) {
                case 1:
                    b.b(str);
                    break;
                case 2:
                    b.e(str);
                    break;
                case 3:
                case 4:
                    b.c(str);
                    break;
                case 5:
                    b.a(str);
                    break;
            }
            return super.onConsoleMessage(consoleMessage);
        }

        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            m.a a2 = this.a.d.a().b.a();
            long longValue = a2.l.a().longValue() - totalUsedQuota;
            if (longValue <= 0) {
                quotaUpdater.updateQuota(currentQuota);
                return;
            }
            if (currentQuota == 0) {
                if (estimatedSize > longValue || estimatedSize > a2.m.a().longValue()) {
                    estimatedSize = 0;
                }
            } else if (estimatedSize == 0) {
                estimatedSize = Math.min(Math.min(a2.n.a().longValue(), longValue) + currentQuota, a2.m.a().longValue());
            } else {
                if (estimatedSize <= Math.min(a2.m.a().longValue() - currentQuota, longValue)) {
                    currentQuota += estimatedSize;
                }
                estimatedSize = currentQuota;
            }
            quotaUpdater.updateQuota(estimatedSize);
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return a(view, url, message, (String) null, result, (JsPromptResult) null, false);
        }

        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return a(view, url, message, (String) null, result, (JsPromptResult) null, false);
        }

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return a(view, url, message, (String) null, result, (JsPromptResult) null, false);
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return a(view, url, message, defaultValue, (JsResult) null, result, true);
        }

        public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            m.a a2 = this.a.d.a().b.a();
            long longValue = a2.j.a().longValue() + spaceNeeded;
            if (a2.k.a().longValue() - totalUsedQuota < longValue) {
                quotaUpdater.updateQuota(0);
            } else {
                quotaUpdater.updateQuota(longValue);
            }
        }

        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            callback.onCustomViewHidden();
        }

        private static boolean a(WebView webView, String str, String str2, String str3, JsResult jsResult, JsPromptResult jsPromptResult, boolean z) {
            AdActivity i;
            if (!(webView instanceof AdWebView) || (i = ((AdWebView) webView).i()) == null) {
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(i);
            builder.setTitle(str);
            if (z) {
                a(builder, i, str2, str3, jsPromptResult);
            } else {
                a(builder, str2, jsResult);
            }
            return true;
        }

        private static void a(AlertDialog.Builder builder, String str, final JsResult jsResult) {
            builder.setMessage(str).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    jsResult.confirm();
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    jsResult.cancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    jsResult.cancel();
                }
            }).create().show();
        }

        private static void a(AlertDialog.Builder builder, Context context, String str, String str2, final JsPromptResult jsPromptResult) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            TextView textView = new TextView(context);
            textView.setText(str);
            final EditText editText = new EditText(context);
            editText.setText(str2);
            linearLayout.addView(textView);
            linearLayout.addView(editText);
            builder.setView(linearLayout).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    jsPromptResult.confirm(editText.getText().toString());
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    jsPromptResult.cancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    jsPromptResult.cancel();
                }
            }).create().show();
        }
    }

    /* renamed from: com.google.ads.util.g$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a = new int[ConsoleMessage.MessageLevel.values().length];

        static {
            try {
                a[ConsoleMessage.MessageLevel.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[ConsoleMessage.MessageLevel.WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[ConsoleMessage.MessageLevel.LOG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[ConsoleMessage.MessageLevel.TIP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[ConsoleMessage.MessageLevel.DEBUG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static void a(WebSettings webSettings, n nVar) {
        Context a2 = nVar.f.a();
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(nVar.d.a().b.a().i.a().longValue());
        webSettings.setAppCachePath(new File(a2.getCacheDir(), "admob").getAbsolutePath());
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(a2.getDatabasePath("admob").getAbsolutePath());
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
    }

    public static void a(View view) {
        view.setLayerType(1, (Paint) null);
    }

    public static void b(View view) {
        view.setLayerType(0, (Paint) null);
    }

    public static void a(Window window) {
        window.setFlags(16777216, 16777216);
    }
}
