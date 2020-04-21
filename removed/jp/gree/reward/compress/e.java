package jp.gree.reward.compress;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.ads.AdActivity;
import jp.gree.reward.sdk.GreeRewardPromotionLayout;

public final class e implements View.OnClickListener {
    private /* synthetic */ GreeRewardPromotionLayout a;

    public e(GreeRewardPromotionLayout greeRewardPromotionLayout) {
        this.a = greeRewardPromotionLayout;
    }

    public final void onClick(View view) {
        ((InputMethodManager) GreeRewardPromotionLayout.a(this.a).getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        GreeRewardPromotionLayout.b(this.a).setVisibility(View.GONE);
        GreeRewardPromotionLayout.b(this.a).setVisibility(View.GONE);
        GreeRewardPromotionLayout.a(this.a).setVisibility(View.VISIBLE);
        GreeRewardPromotionLayout.a(this.a).setVisibility(View.VISIBLE);
        String a2 = this.a.a(AdActivity.INTENT_ACTION_PARAM);
        i.a("promotionUrl: ", a2);
        GreeRewardPromotionLayout.a(this.a).loadUrl(a2);
        GreeRewardPromotionLayout.a(this.a).setVisibility(View.VISIBLE);
    }
}
