package com.google.tagmanager;

class NoopValueBuilder implements ValueBuilder {
    NoopValueBuilder() {
    }

    public ValueBuilder getListItem(int index) {
        return new NoopValueBuilder();
    }

    public ValueBuilder getMapKey(int index) {
        return new NoopValueBuilder();
    }

    public ValueBuilder getMapValue(int index) {
        return new NoopValueBuilder();
    }

    public ValueBuilder getTemplateToken(int index) {
        return new NoopValueBuilder();
    }

    public MacroEvaluationInfoBuilder createValueMacroEvaluationInfoExtension() {
        return new NoopMacroEvaluationInfoBuilder();
    }
}
