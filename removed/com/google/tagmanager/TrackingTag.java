package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

abstract class TrackingTag extends FunctionCallImplementation {
    public abstract void evaluateTrackingTag(Map<String, TypeSystem.Value> map);

    public TrackingTag(String functionId, String... requiredKeys) {
        super(functionId, requiredKeys);
    }

    public boolean isCacheable() {
        return false;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        evaluateTrackingTag(parameters);
        return Types.getDefaultValue();
    }
}
