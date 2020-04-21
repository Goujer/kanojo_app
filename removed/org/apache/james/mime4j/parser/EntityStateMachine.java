package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.descriptor.BodyDescriptor;

public interface EntityStateMachine {
    EntityStateMachine advance() throws IOException, MimeException;

    BodyDescriptor getBodyDescriptor() throws IllegalStateException;

    InputStream getContentStream() throws IllegalStateException;

    Field getField() throws IllegalStateException;

    int getState();

    void setRecursionMode(int i);
}
