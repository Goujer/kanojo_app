package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem;
import com.google.analytics.midtier.proto.containertag.TypeSystem;

class DebugValueBuilder implements ValueBuilder {
    private MutableTypeSystem.Value value;

    public DebugValueBuilder(MutableTypeSystem.Value value2) {
        this.value = value2;
    }

    private void validateType(MutableTypeSystem.Value.Type expected, MutableTypeSystem.Value.Type actual, String message) {
        if (!expected.equals(actual)) {
            throw new TypeMismatchException(message, actual);
        }
    }

    public static MutableTypeSystem.Value copyImmutableValue(TypeSystem.Value value2) {
        MutableTypeSystem.Value result = MutableTypeSystem.Value.newMessage();
        if (!result.mergeFrom(value2.toByteArray())) {
            Log.e("Failed to copy runtime value into debug value");
        }
        return result;
    }

    public ValueBuilder getListItem(int index) {
        validateType(MutableTypeSystem.Value.Type.LIST, this.value.getType(), "add new list item");
        return new DebugValueBuilder(this.value.getListItem(index));
    }

    public ValueBuilder getMapKey(int index) {
        validateType(MutableTypeSystem.Value.Type.MAP, this.value.getType(), "add new map key");
        return new DebugValueBuilder(this.value.getMapKey(index));
    }

    public ValueBuilder getMapValue(int index) {
        validateType(MutableTypeSystem.Value.Type.MAP, this.value.getType(), "add new map value");
        return new DebugValueBuilder(this.value.getMapValue(index));
    }

    public ValueBuilder getTemplateToken(int index) {
        validateType(MutableTypeSystem.Value.Type.TEMPLATE, this.value.getType(), "add template token");
        return new DebugValueBuilder(this.value.getTemplateToken(index));
    }

    public MacroEvaluationInfoBuilder createValueMacroEvaluationInfoExtension() {
        validateType(MutableTypeSystem.Value.Type.MACRO_REFERENCE, this.value.getType(), "set macro evaluation extension");
        return new DebugMacroEvaluationInfoBuilder((MutableDebug.MacroEvaluationInfo) this.value.getMutableExtension(MutableDebug.MacroEvaluationInfo.macro));
    }

    private static class TypeMismatchException extends IllegalStateException {
        public TypeMismatchException(String operation, MutableTypeSystem.Value.Type t) {
            super("Attempted operation: " + operation + " on object of type: " + t);
        }
    }
}
