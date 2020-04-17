package com.google.tagmanager;

interface MacroEvaluationInfoBuilder {
    ResolvedFunctionCallBuilder createResult();

    RuleEvaluationStepInfoBuilder createRulesEvaluation();
}
