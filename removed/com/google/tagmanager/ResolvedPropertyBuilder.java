package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

interface ResolvedPropertyBuilder {
    ValueBuilder createPropertyValueBuilder(TypeSystem.Value value);
}
