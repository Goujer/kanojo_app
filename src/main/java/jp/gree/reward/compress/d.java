package jp.gree.reward.compress;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import jp.gree.reward.sdk.GreeRewardPromotionLayout;

public final class d extends WebViewClient {
    private /* synthetic */ GreeRewardPromotionLayout a;

    public d(GreeRewardPromotionLayout greeRewardPromotionLayout) {
        this.a = greeRewardPromotionLayout;
    }

    public final void onPageFinished(WebView webView, String str) {
        GreeRewardPromotionLayout.b(this.a).setVisibility(8);
        GreeRewardPromotionLayout.a(this.a).setVisibility(0);
        GreeRewardPromotionLayout.b(this.a).setText(GreeRewardPromotionLayout.a(this.a).getTitle());
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (!str.startsWith("greereward://") && !str.startsWith("greerewards://")) {
            return false;
        }
        i.a("GreeRewardPromotionLayout", str);
        String str2 = null;
        if (str.startsWith("greereward://")) {
            str2 = str.replace("greereward://", "http://");
        } else if (str.startsWith("greerewards://")) {
            str2 = str.replace("greerewards://", "https://");
        } else {
            i.b("Invalid URL: ", str);
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(str2));
        GreeRewardPromotionLayout.a(this.a).startActivity(intent);
        return true;
    }
}
