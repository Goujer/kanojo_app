package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;

class DebugMacroEvaluationInfoBuilder implements MacroEvaluationInfoBuilder {
    private MutableDebug.MacroEvaluationInfo macroEvaluationInfo;

    public DebugMacroEvaluationInfoBuilder(MutableDebug.MacroEvaluationInfo macroEvaluationInfo2) {
        this.macroEvaluationInfo = macroEvaluationInfo2;
    }

    public ResolvedFunctionCallBuilder createResult() {
        return new DebugResolvedFunctionCallBuilder(this.macroEvaluationInfo.getMutableResult());
    }

    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new DebugRuleEvaluationStepInfoBuilder(this.macroEvaluationInfo.getMutableRulesEvaluation());
    }
}
