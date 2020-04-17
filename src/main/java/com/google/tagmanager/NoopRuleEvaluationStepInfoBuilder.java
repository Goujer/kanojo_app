package com.google.tagmanager;

import com.google.tagmanager.ResourceUtil;
import java.util.Set;

class NoopRuleEvaluationStepInfoBuilder implements RuleEvaluationStepInfoBuilder {
    NoopRuleEvaluationStepInfoBuilder() {
    }

    public void setEnabledFunctions(Set<ResourceUtil.ExpandedFunctionCall> set) {
    }

    public ResolvedRuleBuilder createResolvedRuleBuilder() {
        return new NoopResolvedRuleBuilder();
    }
}
