package com.google.tagmanager;

interface EventInfoDistributor {
    EventInfoBuilder createDataLayerEventEvaluationEventInfo(String str);

    EventInfoBuilder createMacroEvalutionEventInfo(String str);

    boolean debugMode();
}
