package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

class GreaterThanPredicate extends NumberPredicate {
    private static final String ID = FunctionType.GREATER_THAN.toString();

    public static String getFunctionId() {
        return ID;
    }

    public GreaterThanPredicate() {
        super(ID);
    }

    /* access modifiers changed from: protected */
    public boolean evaluateNumber(TypedNumber arg0, TypedNumber arg1, Map<String, TypeSystem.Value> map) {
        return arg0.compareTo(arg1) > 0;
    }
}
