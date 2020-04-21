package jp.gree.reward.compress;

import android.view.View;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import jp.gree.reward.sdk.GreeRewardPromotionLayout;

public final class g implements View.OnClickListener {
    /* access modifiers changed from: private */
    public /* synthetic */ GreeRewardPromotionLayout a;

    public g(GreeRewardPromotionLayout greeRewardPromotionLayout) {
        this.a = greeRewardPromotionLayout;
    }

    public final void onClick(View view) {
        GreeRewardPromotionLayout.a(this.a).setVisibility(View.GONE);
        GreeRewardPromotionLayout.a(this.a).setVisibility(View.GONE);
        GreeRewardPromotionLayout.b(this.a).setVisibility(View.VISIBLE);
        GreeRewardPromotionLayout.b(this.a).setVisibility(View.VISIBLE);
        GreeRewardPromotionLayout.b(this.a).setWebViewClient(new h(this));
        GreeRewardPromotionLayout.b(this.a).getSettings().setBuiltInZoomControls(true);
        GreeRewardPromotionLayout.b(this.a).getSettings().setJavaScriptEnabled(true);
        GreeRewardPromotionLayout.b(this.a).requestFocus(130);
        String a2 = this.a.a(AppLauncherConsts.REQUEST_PARAM_GENERAL);
        i.a("inquiryUrl: ", a2);
        GreeRewardPromotionLayout.b(this.a).loadUrl(a2);
        GreeRewardPromotionLayout.b(this.a).setScrollBarStyle(33554432);
    }
}
