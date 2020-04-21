package com.google.tagmanager;

import com.google.tagmanager.ResourceUtil;
import java.util.Set;

interface RuleEvaluationStepInfoBuilder {
    ResolvedRuleBuilder createResolvedRuleBuilder();

    void setEnabledFunctions(Set<ResourceUtil.ExpandedFunctionCall> set);
}
