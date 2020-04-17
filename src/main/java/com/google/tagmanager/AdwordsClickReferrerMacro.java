package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

class AdwordsClickReferrerMacro extends FunctionCallImplementation {
    private static final String COMPONENT = Key.COMPONENT.toString();
    private static final String CONVERSION_ID = Key.CONVERSION_ID.toString();
    private static final String ID = FunctionType.ADWORDS_CLICK_REFERRER.toString();
    private final Context context;

    public static String getFunctionId() {
        return ID;
    }

    public AdwordsClickReferrerMacro(Context context2) {
        super(ID, CONVERSION_ID);
        this.context = context2;
    }

    public boolean isCacheable() {
        return true;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        TypeSystem.Value conversionIdValue = parameters.get(CONVERSION_ID);
        if (conversionIdValue == null) {
            return Types.getDefaultValue();
        }
        String conversionId = Types.valueToString(conversionIdValue);
        TypeSystem.Value componentValue = parameters.get(COMPONENT);
        String referrer = InstallReferrerUtil.getClickReferrer(this.context, conversionId, componentValue != null ? Types.valueToString(componentValue) : null);
        return referrer != null ? Types.objectToValue(referrer) : Types.getDefaultValue();
    }
}
