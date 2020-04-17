package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

abstract class StringPredicate extends Predicate {
    /* access modifiers changed from: protected */
    public abstract boolean evaluateString(String str, String str2, Map<String, TypeSystem.Value> map);

    public StringPredicate(String functionId) {
        super(functionId);
    }

    /* access modifiers changed from: protected */
    public boolean evaluateNoDefaultValues(TypeSystem.Value arg0, TypeSystem.Value arg1, Map<String, TypeSystem.Value> parameters) {
        String stringArg0 = Types.valueToString(arg0);
        String stringArg1 = Types.valueToString(arg1);
        if (stringArg0 == Types.getDefaultString() || stringArg1 == Types.getDefaultString()) {
            return false;
        }
        return evaluateString(stringArg0, stringArg1, parameters);
    }
}
