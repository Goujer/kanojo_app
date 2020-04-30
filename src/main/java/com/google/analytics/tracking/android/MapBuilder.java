package com.google.analytics.tracking.android;

import android.text.TextUtils;
import com.google.analytics.tracking.android.GAUsage;
import java.util.HashMap;
import java.util.Map;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class MapBuilder {
    private Map<String, String> map = new HashMap();

    public MapBuilder set(String paramName, String paramValue) {
        GAUsage.getInstance().setUsage(GAUsage.Field.MAP_BUILDER_SET);
        if (paramName != null) {
            this.map.put(paramName, paramValue);
        } else {
            Log.w(" MapBuilder.set() called with a null paramName.");
        }
        return this;
    }

//    public MapBuilder setAll(Map<String, String> params) {
//        GAUsage.getInstance().setUsage(GAUsage.Field.MAP_BUILDER_SET_ALL);
//        if (params != null) {
//            this.map.putAll(new HashMap(params));
//        }
//        return this;
//    }

    public String get(String paramName) {
        GAUsage.getInstance().setUsage(GAUsage.Field.MAP_BUILDER_GET);
        return this.map.get(paramName);
    }

    public Map<String, String> build() {
        return new HashMap(this.map);
    }

//    public static MapBuilder createAppView() {
//        GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_APP_VIEW);
//        MapBuilder builder = new MapBuilder();
//        builder.set(Fields.HIT_TYPE, HitTypes.APP_VIEW);
//        return builder;
//    }

    public static MapBuilder createEvent(String category, String action, String label, Long value) {
        GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_EVENT);
        MapBuilder builder = new MapBuilder();
        builder.set(Fields.HIT_TYPE, HitTypes.EVENT);
        builder.set(Fields.EVENT_CATEGORY, category);
        builder.set(Fields.EVENT_ACTION, action);
        builder.set(Fields.EVENT_LABEL, label);
        builder.set(Fields.EVENT_VALUE, value == null ? null : Long.toString(value.longValue()));
        return builder;
    }

//    public static MapBuilder createTransaction(String transactionId, String affiliation, Double revenue, Double tax, Double shipping, String currencyCode) {
//        String str = null;
//        GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_TRANSACTION);
//        MapBuilder builder = new MapBuilder();
//        builder.set(Fields.HIT_TYPE, HitTypes.TRANSACTION);
//        builder.set(Fields.TRANSACTION_ID, transactionId);
//        builder.set(Fields.TRANSACTION_AFFILIATION, affiliation);
//        builder.set(Fields.TRANSACTION_REVENUE, revenue == null ? null : Double.toString(revenue.doubleValue()));
//        builder.set(Fields.TRANSACTION_TAX, tax == null ? null : Double.toString(tax.doubleValue()));
//        if (shipping != null) {
//            str = Double.toString(shipping.doubleValue());
//        }
//        builder.set(Fields.TRANSACTION_SHIPPING, str);
//        builder.set(Fields.CURRENCY_CODE, currencyCode);
//        return builder;
//    }

//    public static MapBuilder createItem(String transactionId, String name, String sku, String category, Double price, Long quantity, String currencyCode) {
//        String str = null;
//        GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_ITEM);
//        MapBuilder builder = new MapBuilder();
//        builder.set(Fields.HIT_TYPE, HitTypes.ITEM);
//        builder.set(Fields.TRANSACTION_ID, transactionId);
//        builder.set(Fields.ITEM_SKU, sku);
//        builder.set(Fields.ITEM_NAME, name);
//        builder.set(Fields.ITEM_CATEGORY, category);
//        builder.set(Fields.ITEM_PRICE, price == null ? null : Double.toString(price.doubleValue()));
//        if (quantity != null) {
//            str = Long.toString(quantity.longValue());
//        }
//        builder.set(Fields.ITEM_QUANTITY, str);
//        builder.set(Fields.CURRENCY_CODE, currencyCode);
//        return builder;
//    }

    public static MapBuilder createException(String exceptionDescription, Boolean fatal) {
        GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_EXCEPTION);
        MapBuilder builder = new MapBuilder();
        builder.set(Fields.HIT_TYPE, HitTypes.EXCEPTION);
        builder.set(Fields.EX_DESCRIPTION, exceptionDescription);
        builder.set(Fields.EX_FATAL, booleanToString(fatal));
        return builder;
    }

    public static MapBuilder createTiming(String category, Long intervalInMilliseconds, String name, String label) {
        GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_TIMING);
        MapBuilder builder = new MapBuilder();
        builder.set(Fields.HIT_TYPE, HitTypes.TIMING);
        builder.set(Fields.TIMING_CATEGORY, category);
        String interval = null;
        if (intervalInMilliseconds != null) {
            interval = Long.toString(intervalInMilliseconds.longValue());
        }
        builder.set(Fields.TIMING_VALUE, interval);
        builder.set(Fields.TIMING_VAR, name);
        builder.set(Fields.TIMING_LABEL, label);
        return builder;
    }

    public static MapBuilder createSocial(String network, String action, String target) {
        GAUsage.getInstance().setUsage(GAUsage.Field.CONSTRUCT_SOCIAL);
        MapBuilder builder = new MapBuilder();
        builder.set(Fields.HIT_TYPE, HitTypes.SOCIAL);
        builder.set(Fields.SOCIAL_NETWORK, network);
        builder.set(Fields.SOCIAL_ACTION, action);
        builder.set(Fields.SOCIAL_TARGET, target);
        return builder;
    }

    public MapBuilder setCampaignParamsFromUrl(String utmParams) {
        GAUsage.getInstance().setUsage(GAUsage.Field.MAP_BUILDER_SET_CAMPAIGN_PARAMS);
        String filteredCampaign = Utils.filterCampaign(utmParams);
        if (!TextUtils.isEmpty(filteredCampaign)) {
            Map<String, String> paramsMap = Utils.parseURLParameters(filteredCampaign);
            set(Fields.CAMPAIGN_CONTENT, paramsMap.get("utm_content"));
            set(Fields.CAMPAIGN_MEDIUM, paramsMap.get("utm_medium"));
            set(Fields.CAMPAIGN_NAME, paramsMap.get("utm_campaign"));
            set(Fields.CAMPAIGN_SOURCE, paramsMap.get("utm_source"));
            set(Fields.CAMPAIGN_KEYWORD, paramsMap.get("utm_term"));
            set(Fields.CAMPAIGN_ID, paramsMap.get("utm_id"));
            set("&gclid", paramsMap.get("gclid"));
            set("&dclid", paramsMap.get("dclid"));
            set("&gmob_t", paramsMap.get("gmob_t"));
        }
        return this;
    }

    static String booleanToString(Boolean b) {
        if (b == null) {
            return null;
        }
        return b.booleanValue() ? GreeDefs.KANOJO_NAME : GreeDefs.BARCODE;
    }
}
