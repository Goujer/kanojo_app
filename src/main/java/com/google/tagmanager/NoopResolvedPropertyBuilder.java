package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

class NoopResolvedPropertyBuilder implements ResolvedPropertyBuilder {
    NoopResolvedPropertyBuilder() {
    }

    public ValueBuilder createPropertyValueBuilder(TypeSystem.Value propertyValue) {
        return new NoopValueBuilder();
    }
}
