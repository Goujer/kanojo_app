package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.ResourceUtil;
import java.util.List;
import java.util.Map;

class DebugResolvedRuleBuilder implements ResolvedRuleBuilder {
    ResolvedFunctionCallTranslatorList addMacrosHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableAddMacrosList());
    ResolvedFunctionCallTranslatorList addTagsHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableAddTagsList());
    ResolvedFunctionCallTranslatorList removeMacrosHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableRemoveMacrosList());
    ResolvedFunctionCallTranslatorList removeTagsHolder = new DebugResolvedFunctionCallListTranslator(this.resolvedRule.getMutableRemoveTagsList());
    MutableDebug.ResolvedRule resolvedRule;

    public DebugResolvedRuleBuilder(MutableDebug.ResolvedRule rule) {
        this.resolvedRule = rule;
    }

    public ResolvedFunctionCallBuilder createNegativePredicate() {
        return new DebugResolvedFunctionCallBuilder(this.resolvedRule.addNegativePredicates());
    }

    public ResolvedFunctionCallBuilder createPositivePredicate() {
        return new DebugResolvedFunctionCallBuilder(this.resolvedRule.addPositivePredicates());
    }

    public ResolvedFunctionCallTranslatorList getAddedMacroFunctions() {
        return this.addMacrosHolder;
    }

    public ResolvedFunctionCallTranslatorList getRemovedMacroFunctions() {
        return this.removeMacrosHolder;
    }

    public ResolvedFunctionCallTranslatorList getAddedTagFunctions() {
        return this.addTagsHolder;
    }

    public ResolvedFunctionCallTranslatorList getRemovedTagFunctions() {
        return this.removeTagsHolder;
    }

    public void setValue(TypeSystem.Value result) {
        this.resolvedRule.setResult(DebugValueBuilder.copyImmutableValue(result));
    }

    public static MutableDebug.ResolvedFunctionCall translateExpandedFunctionCall(ResourceUtil.ExpandedFunctionCall f) {
        MutableDebug.ResolvedFunctionCall result = MutableDebug.ResolvedFunctionCall.newMessage();
        for (Map.Entry<String, TypeSystem.Value> originalParam : f.getProperties().entrySet()) {
            MutableDebug.ResolvedProperty prop = MutableDebug.ResolvedProperty.newMessage();
            prop.setKey(originalParam.getKey());
            prop.setValue(DebugValueBuilder.copyImmutableValue(originalParam.getValue()));
            result.addProperties(prop);
        }
        return result;
    }

    class DebugResolvedFunctionCallListTranslator implements ResolvedFunctionCallTranslatorList {
        @VisibleForTesting
        List<MutableDebug.ResolvedFunctionCall> toBuild;

        DebugResolvedFunctionCallListTranslator(List<MutableDebug.ResolvedFunctionCall> result) {
            this.toBuild = result;
        }

        public void translateAndAddAll(List<ResourceUtil.ExpandedFunctionCall> functions, List<String> ruleNames) {
            for (int i = 0; i < functions.size(); i++) {
                this.toBuild.add(DebugResolvedRuleBuilder.translateExpandedFunctionCall(functions.get(i)));
                if (i < ruleNames.size()) {
                    this.toBuild.get(i).setAssociatedRuleName(ruleNames.get(i));
                } else {
                    this.toBuild.get(i).setAssociatedRuleName("Unknown");
                }
            }
        }
    }
}
