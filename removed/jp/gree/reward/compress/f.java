package jp.gree.reward.compress;

import android.app.Activity;
import android.view.View;
import jp.gree.reward.sdk.GreeRewardPromotionActivity;
import jp.gree.reward.sdk.GreeRewardPromotionLayout;

public final class f implements View.OnClickListener {
    private /* synthetic */ GreeRewardPromotionLayout a;

    public f(GreeRewardPromotionLayout greeRewardPromotionLayout) {
        this.a = greeRewardPromotionLayout;
    }

    public final void onClick(View view) {
        if (GreeRewardPromotionLayout.a(this.a) instanceof GreeRewardPromotionActivity) {
            ((Activity) GreeRewardPromotionLayout.a(this.a)).finish();
        } else {
            this.a.hidePromotionView();
        }
    }
}
