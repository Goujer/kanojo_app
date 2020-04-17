package com.google.tagmanager;

class NoopDataLayerEventEvaluationInfoBuilder implements DataLayerEventEvaluationInfoBuilder {
    NoopDataLayerEventEvaluationInfoBuilder() {
    }

    public ResolvedFunctionCallBuilder createAndAddResult() {
        return new NoopResolvedFunctionCallBuilder();
    }

    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new NoopRuleEvaluationStepInfoBuilder();
    }
}
