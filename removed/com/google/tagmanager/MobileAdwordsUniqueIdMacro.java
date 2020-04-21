package com.google.tagmanager;

import android.content.Context;
import android.provider.Settings;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Map;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;

class MobileAdwordsUniqueIdMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.MOBILE_ADWORDS_UNIQUE_ID.toString();
    private final Context mContext;

    public static String getFunctionId() {
        return ID;
    }

    public MobileAdwordsUniqueIdMacro(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    public boolean isCacheable() {
        return true;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> map) {
        String androidId = getAndroidId(this.mContext);
        return androidId == null ? Types.getDefaultValue() : Types.objectToValue(androidId);
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Preferences.PREFERENCE_ANDROID_ID);
    }
}
