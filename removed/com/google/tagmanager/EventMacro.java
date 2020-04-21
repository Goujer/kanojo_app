package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

class EventMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.EVENT.toString();
    private final Runtime mRuntime;

    public static String getFunctionId() {
        return ID;
    }

    public EventMacro(Runtime runtime) {
        super(ID, new String[0]);
        this.mRuntime = runtime;
    }

    public boolean isCacheable() {
        return false;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> map) {
        String currentEventName = this.mRuntime.getCurrentEventName();
        return currentEventName == null ? Types.getDefaultValue() : Types.objectToValue(currentEventName);
    }
}
