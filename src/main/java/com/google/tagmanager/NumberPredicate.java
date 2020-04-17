package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

abstract class NumberPredicate extends Predicate {
    /* access modifiers changed from: protected */
    public abstract boolean evaluateNumber(TypedNumber typedNumber, TypedNumber typedNumber2, Map<String, TypeSystem.Value> map);

    public NumberPredicate(String functionId) {
        super(functionId);
    }

    /* access modifiers changed from: protected */
    public boolean evaluateNoDefaultValues(TypeSystem.Value arg0, TypeSystem.Value arg1, Map<String, TypeSystem.Value> parameters) {
        TypedNumber numberArg0 = Types.valueToNumber(arg0);
        TypedNumber numberArg1 = Types.valueToNumber(arg1);
        if (numberArg0 == Types.getDefaultNumber() || numberArg1 == Types.getDefaultNumber()) {
            return false;
        }
        return evaluateNumber(numberArg0, numberArg1, parameters);
    }
}
