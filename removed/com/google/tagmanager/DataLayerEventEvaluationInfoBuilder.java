package com.google.tagmanager;

interface DataLayerEventEvaluationInfoBuilder {
    ResolvedFunctionCallBuilder createAndAddResult();

    RuleEvaluationStepInfoBuilder createRulesEvaluation();
}
