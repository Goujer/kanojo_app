package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

class RuntimeVersionMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.RUNTIME_VERSION.toString();
    public static final long VERSION_NUMBER = 50788154;

    public static String getFunctionId() {
        return ID;
    }

    public RuntimeVersionMacro() {
        super(ID, new String[0]);
    }

    public boolean isCacheable() {
        return true;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> map) {
        return Types.objectToValue(Long.valueOf(VERSION_NUMBER));
    }
}
