package org.apache.james.mime4j.parser;

import java.io.InputStream;
import org.apache.james.mime4j.descriptor.BodyDescriptor;

public class RawEntity implements EntityStateMachine {
    private int state = 2;
    private final InputStream stream;

    RawEntity(InputStream stream2) {
        this.stream = stream2;
    }

    public int getState() {
        return this.state;
    }

    public void setRecursionMode(int recursionMode) {
    }

    public EntityStateMachine advance() {
        this.state = -1;
        return null;
    }

    public InputStream getContentStream() {
        return this.stream;
    }

    public BodyDescriptor getBodyDescriptor() {
        return null;
    }

    public Field getField() {
        return null;
    }

    public String getFieldName() {
        return null;
    }

    public String getFieldValue() {
        return null;
    }
}
