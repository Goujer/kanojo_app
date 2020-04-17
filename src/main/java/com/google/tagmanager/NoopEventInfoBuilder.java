package com.google.tagmanager;

class NoopEventInfoBuilder implements EventInfoBuilder {
    NoopEventInfoBuilder() {
    }

    public MacroEvaluationInfoBuilder createMacroEvaluationInfoBuilder() {
        return new NoopMacroEvaluationInfoBuilder();
    }

    public DataLayerEventEvaluationInfoBuilder createDataLayerEventEvaluationInfoBuilder() {
        return new NoopDataLayerEventEvaluationInfoBuilder();
    }

    public void processEventInfo() {
    }
}
