package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

class GreaterEqualsPredicate extends NumberPredicate {
    private static final String ID = FunctionType.GREATER_EQUALS.toString();

    public static String getFunctionId() {
        return ID;
    }

    public GreaterEqualsPredicate() {
        super(ID);
    }

    /* access modifiers changed from: protected */
    public boolean evaluateNumber(TypedNumber arg0, TypedNumber arg1, Map<String, TypeSystem.Value> map) {
        return arg0.compareTo(arg1) >= 0;
    }
}
