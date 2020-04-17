package com.google.tagmanager;

interface EventInfoBuilder {
    DataLayerEventEvaluationInfoBuilder createDataLayerEventEvaluationInfoBuilder();

    MacroEvaluationInfoBuilder createMacroEvaluationInfoBuilder();

    void processEventInfo();
}
