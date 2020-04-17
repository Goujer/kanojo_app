package com.google.tagmanager;

class NoopEventInfoDistributor implements EventInfoDistributor {
    NoopEventInfoDistributor() {
    }

    public EventInfoBuilder createMacroEvalutionEventInfo(String key) {
        return new NoopEventInfoBuilder();
    }

    public EventInfoBuilder createDataLayerEventEvaluationEventInfo(String event) {
        return new NoopEventInfoBuilder();
    }

    public boolean debugMode() {
        return false;
    }
}
