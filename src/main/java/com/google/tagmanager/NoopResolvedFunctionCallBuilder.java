package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

class NoopResolvedFunctionCallBuilder implements ResolvedFunctionCallBuilder {
    NoopResolvedFunctionCallBuilder() {
    }

    public ResolvedPropertyBuilder createResolvedPropertyBuilder(String key) {
        return new NoopResolvedPropertyBuilder();
    }

    public void setFunctionResult(TypeSystem.Value functionResult) {
    }
}
