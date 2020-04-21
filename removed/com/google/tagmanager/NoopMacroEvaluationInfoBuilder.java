package com.google.tagmanager;

class NoopMacroEvaluationInfoBuilder implements MacroEvaluationInfoBuilder {
    NoopMacroEvaluationInfoBuilder() {
    }

    public ResolvedFunctionCallBuilder createResult() {
        return new NoopResolvedFunctionCallBuilder();
    }

    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new NoopRuleEvaluationStepInfoBuilder();
    }
}
