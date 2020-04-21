package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.tagmanager.ResourceUtil;
import java.util.Set;

class DebugRuleEvaluationStepInfoBuilder implements RuleEvaluationStepInfoBuilder {
    private MutableDebug.RuleEvaluationStepInfo ruleEvaluationStepInfo;

    public DebugRuleEvaluationStepInfoBuilder(MutableDebug.RuleEvaluationStepInfo ruleEvaluationStepInfo2) {
        this.ruleEvaluationStepInfo = ruleEvaluationStepInfo2;
    }

    public void setEnabledFunctions(Set<ResourceUtil.ExpandedFunctionCall> enabledFunctions) {
        for (ResourceUtil.ExpandedFunctionCall enabledFunction : enabledFunctions) {
            this.ruleEvaluationStepInfo.addEnabledFunctions(DebugResolvedRuleBuilder.translateExpandedFunctionCall(enabledFunction));
        }
    }

    public ResolvedRuleBuilder createResolvedRuleBuilder() {
        return new DebugResolvedRuleBuilder(this.ruleEvaluationStepInfo.addRules());
    }
}
