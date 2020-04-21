package jp.gree.reward.sdk;

import android.content.Context;
import jp.gree.reward.compress.c;
import jp.gree.reward.compress.i;

public class GreeRewardFactory {
    public static GreeRewardAction getActionInstance(Context context) {
        try {
            return new GreeRewardAction(context);
        } catch (c e) {
            i.a("GreeRewardFactory", "getActionInstance", e);
            return null;
        }
    }

    public static Class getPromotionClass(Context context) {
        try {
            return Class.forName("jp.gree.reward.sdk.GreeRewardPromotionActivity", true, context.getApplicationContext().getClassLoader());
        } catch (ClassNotFoundException e) {
            i.a("GreeRewardFactory", "getPromotionClass", e);
            return null;
        }
    }

    public static GreeRewardPromotionOption getPromotionOptionInstance(Context context) {
        try {
            return new GreeRewardPromotionOption(context);
        } catch (c e) {
            i.a("GreeRewardFactory", "getActionInstance", e);
            return null;
        }
    }
}
