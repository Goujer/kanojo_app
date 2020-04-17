package jp.gree.reward.compress;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import jp.gree.reward.sdk.GreeRewardPromotionLayout;

final class h extends WebViewClient {
    private /* synthetic */ g a;

    h(g gVar) {
        this.a = gVar;
    }

    public final void onPageFinished(WebView webView, String str) {
        GreeRewardPromotionLayout.a(this.a.a).setVisibility(8);
        GreeRewardPromotionLayout.a(this.a.a).setText(GreeRewardPromotionLayout.b(this.a.a).getTitle());
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
        webView.loadUrl(str);
        return false;
    }
}
