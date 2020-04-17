package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem;
import com.google.analytics.midtier.proto.containertag.TypeSystem;

class DebugResolvedPropertyBuilder implements ResolvedPropertyBuilder {
    private MutableDebug.ResolvedProperty resolvedProperty;

    public DebugResolvedPropertyBuilder(MutableDebug.ResolvedProperty resolvedProperty2) {
        this.resolvedProperty = resolvedProperty2;
    }

    public ValueBuilder createPropertyValueBuilder(TypeSystem.Value propertyValue) {
        MutableTypeSystem.Value val = DebugValueBuilder.copyImmutableValue(propertyValue);
        this.resolvedProperty.setValue(val);
        return new DebugValueBuilder(val);
    }
}
