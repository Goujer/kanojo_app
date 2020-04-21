package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

interface ResolvedFunctionCallBuilder {
    ResolvedPropertyBuilder createResolvedPropertyBuilder(String str);

    void setFunctionResult(TypeSystem.Value value);
}
