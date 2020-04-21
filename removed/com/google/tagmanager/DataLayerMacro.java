package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

class DataLayerMacro extends FunctionCallImplementation {
    private static final String DEFAULT_VALUE = Key.DEFAULT_VALUE.toString();
    private static final String ID = FunctionType.CUSTOM_VAR.toString();
    private static final String NAME = Key.NAME.toString();
    private final DataLayer mDataLayer;

    public static String getFunctionId() {
        return ID;
    }

    public DataLayerMacro(DataLayer dataLayer) {
        super(ID, NAME);
        this.mDataLayer = dataLayer;
    }

    public static String getNameKey() {
        return NAME;
    }

    public static String getDefaultValueKey() {
        return DEFAULT_VALUE;
    }

    public boolean isCacheable() {
        return false;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        Object dataLayerValue = this.mDataLayer.get(Types.valueToString(parameters.get(NAME)));
        if (dataLayerValue != null) {
            return Types.objectToValue(dataLayerValue);
        }
        TypeSystem.Value defaultValue = parameters.get(DEFAULT_VALUE);
        if (defaultValue != null) {
            return defaultValue;
        }
        return Types.getDefaultValue();
    }
}
