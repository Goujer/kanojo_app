package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;

class DebugDataLayerEventEvaluationInfoBuilder implements DataLayerEventEvaluationInfoBuilder {
    private MutableDebug.DataLayerEventEvaluationInfo dataLayerEvent;

    public DebugDataLayerEventEvaluationInfoBuilder(MutableDebug.DataLayerEventEvaluationInfo dataLayerEvent2) {
        this.dataLayerEvent = dataLayerEvent2;
    }

    public ResolvedFunctionCallBuilder createAndAddResult() {
        return new DebugResolvedFunctionCallBuilder(this.dataLayerEvent.addResults());
    }

    public RuleEvaluationStepInfoBuilder createRulesEvaluation() {
        return new DebugRuleEvaluationStepInfoBuilder(this.dataLayerEvent.getMutableRulesEvaluation());
    }
}
