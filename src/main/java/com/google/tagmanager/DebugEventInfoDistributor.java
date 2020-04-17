package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;

class DebugEventInfoDistributor implements EventInfoDistributor {
    private String containerId;
    private String containerVersion;
    private DebugInformationHandler handler;

    public DebugEventInfoDistributor(DebugInformationHandler handler2, String containerVersion2, String containerId2) {
        this.handler = handler2;
        this.containerVersion = containerVersion2;
        this.containerId = containerId2;
    }

    public EventInfoBuilder createMacroEvalutionEventInfo(String key) {
        return new DebugEventInfoBuilder(MutableDebug.EventInfo.EventType.MACRO_REFERENCE, this.containerVersion, this.containerId, key, this.handler);
    }

    public EventInfoBuilder createDataLayerEventEvaluationEventInfo(String event) {
        return new DebugEventInfoBuilder(MutableDebug.EventInfo.EventType.DATA_LAYER_EVENT, this.containerVersion, this.containerId, event, this.handler);
    }

    public boolean debugMode() {
        return true;
    }
}
