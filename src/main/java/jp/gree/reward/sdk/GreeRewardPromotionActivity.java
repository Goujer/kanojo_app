package jp.gree.reward.sdk;

import android.app.Activity;
import android.os.Bundle;

public class GreeRewardPromotionActivity extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        Bundle extras = getIntent().getExtras();
        GreeRewardPromotionLayout greeRewardPromotionLayout = new GreeRewardPromotionLayout(this);
        if (extras != null) {
            if (extras.getInt("MEDIA_ID") != 0) {
                greeRewardPromotionLayout.setMediaId(extras.getInt("MEDIA_ID"));
            }
            if (extras.getInt("CAMPAIGN_ID", 0) != 0) {
                greeRewardPromotionLayout.setCampaignId(extras.getInt("CAMPAIGN_ID"));
                if (extras.getBoolean("CLICK_CAMPAIGN", false)) {
                    greeRewardPromotionLayout.setClickCampaign(extras.getBoolean("CLICK_CAMPAIGN"));
                }
            }
            if (extras.getString("IDENTIFIER") != null) {
                greeRewardPromotionLayout.setIdentifier(extras.getString("IDENTIFIER"));
            }
            if (extras.getString("ITEM_IDENTIFIER") != null) {
                greeRewardPromotionLayout.setItemIdentifier(extras.getString("ITEM_IDENTIFIER"));
                if (extras.getInt("ITEM_PRICE", 0) != 0) {
                    greeRewardPromotionLayout.setItemPrice(extras.getInt("ITEM_PRICE"));
                }
                if (extras.getString("ITEM_NAME") != null) {
                    greeRewardPromotionLayout.setItemName(extras.getString("ITEM_NAME"));
                }
                if (extras.getString("ITEM_IMAGE") != null) {
                    greeRewardPromotionLayout.setItemImage(extras.getString("ITEM_IMAGE"));
                }
            }
        }
        if (!greeRewardPromotionLayout.getClickCampaign()) {
            greeRewardPromotionLayout.setOrientation(1);
            setContentView(greeRewardPromotionLayout);
        }
        greeRewardPromotionLayout.showPromotionView();
        if (greeRewardPromotionLayout.getClickCampaign()) {
            finish();
        }
    }
}
