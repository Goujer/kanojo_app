package net.nend.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.SparseArray;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import net.nend.android.AdParameter;
import net.nend.android.NendAdResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class NendAdResponseParser {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$NendAdResponseParser$ResponseType = null;
    static final /* synthetic */ boolean $assertionsDisabled = (!NendAdResponseParser.class.desiredAssertionStatus());
    private static final String RESPONSE_ENCODING = "UTF-8";
    private final PackageManager mPackageManager;

    static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$NendAdResponseParser$ResponseType() {
        int[] iArr = $SWITCH_TABLE$net$nend$android$NendAdResponseParser$ResponseType;
        if (iArr == null) {
            iArr = new int[ResponseType.values().length];
            try {
                iArr[ResponseType.APP_TARGETING.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ResponseType.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ResponseType.UNSUPPORTED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ResponseType.WEB_VIEW.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$net$nend$android$NendAdResponseParser$ResponseType = iArr;
        }
        return iArr;
    }

    private static final class JsonParam {
        private static final String CLICK_URL = "click_url";
        private static final String CONDITIONS = "conditions";
        private static final String DEFAULT_AD = "default_ad";
        private static final String HEIGHT = "height";
        private static final String IMAGE_URL = "image_url";
        private static final String LOGICAL_OPERATOR = "logical_operator";
        private static final String MESSAGE = "message";
        private static final String PACKAGE_NAME = "url_scheme";
        private static final String RELOAD = "reload";
        private static final String RESPONSE_TYPE = "response_type";
        private static final String STATUS_CODE = "status_code";
        private static final String TARGETING_ADS = "targeting_ads";
        private static final String WEB_VIEW_URL = "web_view_url";
        private static final String WIDTH = "width";

        private JsonParam() {
        }
    }

    private enum ResponseType {
        UNSUPPORTED(0),
        NORMAL(1),
        WEB_VIEW(2),
        APP_TARGETING(3);

        private static final SparseArray<ResponseType> intToEnum = new SparseArray<>();
        private int type;

        static {
            int i;
            for (ResponseType responseType : values()) {
                intToEnum.put(responseType.type, responseType);
            }
        }

        private ResponseType(int type2) {
            this.type = type2;
        }

        private static ResponseType valueOf(int type2) {
            return intToEnum.get(type2, UNSUPPORTED);
        }
    }

    NendAdResponseParser(Context context) {
        if (context == null) {
            throw new NullPointerException(NendStatus.ERR_INVALID_CONTEXT.getMsg());
        }
        this.mPackageManager = context.getPackageManager();
    }

    AdParameter parseResponse(String responseStr) {
        if (responseStr != null) {
            try {
                if (responseStr.length() != 0) {
                    JSONObject responseJson = new JSONObject(URLDecoder.decode(responseStr, RESPONSE_ENCODING));
                    if (responseJson.getInt("status_code") != NendStatus.SUCCESS.getCode()) {
                        throw new NendException(NendStatus.ERR_INVALID_AD_STATUS, "Ad status : " + responseJson.getInt("status_code") + ", Message : " + responseJson.getString("message"));
                    }
                    switch ($SWITCH_TABLE$net$nend$android$NendAdResponseParser$ResponseType()[ResponseType.valueOf(responseJson.getInt("response_type")).ordinal()]) {
                        case 2:
                            return getNormalAd(responseJson);
                        case 3:
                            return getWebViewAd(responseJson);
                        case 4:
                            return getAppTargetingAd(responseJson);
                        default:
                            throw new NendException(NendStatus.ERR_INVALID_RESPONSE_TYPE);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return null;
            } catch (JSONException e2) {
                NendLog.w(NendStatus.ERR_FAILED_TO_PARSE, (Throwable) e2);
                return null;
            } catch (NendException e3) {
                NendLog.w(NendStatus.ERR_FAILED_TO_PARSE, (Throwable) e3);
                return null;
            } catch (IllegalArgumentException e4) {
                NendLog.w(NendStatus.ERR_FAILED_TO_PARSE, (Throwable) e4);
                return null;
            }
        }
        throw new IllegalArgumentException(NendStatus.ERR_INVALID_RESPONSE.getMsg());
    }

    private AdParameter getNormalAd(JSONObject response) throws JSONException {
        JSONObject defaultAd = response.getJSONObject("default_ad");
        NendAdResponse.Builder builder = new NendAdResponse.Builder().setViewType(AdParameter.ViewType.ADVIEW).setImageUrl(defaultAd.getString("image_url")).setClickUrl(defaultAd.getString("click_url")).setHeight(response.getInt("height")).setWidth(response.getInt("width"));
        if (!response.isNull("reload")) {
            builder.setReloadIntervalInSeconds(response.getInt("reload"));
        }
        return builder.build();
    }

    private AdParameter getWebViewAd(JSONObject response) throws JSONException {
        return new NendAdResponse.Builder().setViewType(AdParameter.ViewType.WEBVIEW).setWebViewUrl(response.getString("web_view_url")).setHeight(response.getInt("height")).setWidth(response.getInt("width")).build();
    }

    private AdParameter getAppTargetingAd(JSONObject response) throws JSONException, NendException {
        JSONArray targetingAdArray = response.getJSONArray("targeting_ads");
        int iEnd = targetingAdArray.length();
        for (int i = 0; i < iEnd; i++) {
            JSONObject targetingAd = targetingAdArray.getJSONObject(i);
            JSONArray conditionArray = targetingAd.getJSONArray("conditions");
            int jEnd = conditionArray.length();
            for (int j = 0; j < jEnd; j++) {
                if (isTarget(conditionArray.getJSONArray(j))) {
                    NendAdResponse.Builder builder = new NendAdResponse.Builder().setViewType(AdParameter.ViewType.ADVIEW).setImageUrl(targetingAd.getString("image_url")).setClickUrl(targetingAd.getString("click_url")).setHeight(response.getInt("height")).setWidth(response.getInt("width"));
                    if (!response.isNull("reload")) {
                        builder.setReloadIntervalInSeconds(response.getInt("reload"));
                    }
                    return builder.build();
                }
            }
        }
        if (!response.isNull("default_ad")) {
            return getNormalAd(response);
        }
        throw new NendException(NendStatus.ERR_OUT_OF_STOCK);
    }

    private boolean isTarget(JSONArray ruleArray) throws JSONException {
        if ($assertionsDisabled || ruleArray != null) {
            int end = ruleArray.length();
            for (int i = 0; i < end; i++) {
                JSONObject rule = ruleArray.getJSONObject(i);
                int logicalOperator = rule.getInt("logical_operator");
                if (logicalOperator == 1) {
                    try {
                        this.mPackageManager.getPackageInfo(rule.getString("url_scheme"), 1);
                    } catch (PackageManager.NameNotFoundException e) {
                        return false;
                    }
                } else if (logicalOperator != 2) {
                    return false;
                } else {
                    try {
                        this.mPackageManager.getPackageInfo(rule.getString("url_scheme"), 1);
                        return false;
                    } catch (PackageManager.NameNotFoundException e2) {
                    }
                }
            }
            return true;
        }
        throw new AssertionError();
    }
}
