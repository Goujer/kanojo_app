package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.tagmanager.ResourceUtil;
import java.util.List;

class NoopResolvedRuleBuilder implements ResolvedRuleBuilder {
    NoopResolvedRuleBuilder() {
    }

    public ResolvedFunctionCallBuilder createNegativePredicate() {
        return new NoopResolvedFunctionCallBuilder();
    }

    public ResolvedFunctionCallBuilder createPositivePredicate() {
        return new NoopResolvedFunctionCallBuilder();
    }

    public ResolvedFunctionCallTranslatorList getAddedMacroFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    public ResolvedFunctionCallTranslatorList getRemovedMacroFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    public ResolvedFunctionCallTranslatorList getAddedTagFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    public ResolvedFunctionCallTranslatorList getRemovedTagFunctions() {
        return new NoopResolvedFunctionCallTranslatorList();
    }

    public void setValue(TypeSystem.Value result) {
    }

    public class NoopResolvedFunctionCallTranslatorList implements ResolvedFunctionCallTranslatorList {
        public NoopResolvedFunctionCallTranslatorList() {
        }

        public void translateAndAddAll(List<ResourceUtil.ExpandedFunctionCall> list, List<String> list2) {
        }
    }
}
