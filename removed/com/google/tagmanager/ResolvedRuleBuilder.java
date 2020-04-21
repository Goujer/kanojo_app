package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

interface ResolvedRuleBuilder {
    ResolvedFunctionCallBuilder createNegativePredicate();

    ResolvedFunctionCallBuilder createPositivePredicate();

    ResolvedFunctionCallTranslatorList getAddedMacroFunctions();

    ResolvedFunctionCallTranslatorList getAddedTagFunctions();

    ResolvedFunctionCallTranslatorList getRemovedMacroFunctions();

    ResolvedFunctionCallTranslatorList getRemovedTagFunctions();

    void setValue(TypeSystem.Value value);
}
