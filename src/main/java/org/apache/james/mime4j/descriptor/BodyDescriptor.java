package org.apache.james.mime4j.descriptor;

public interface BodyDescriptor extends ContentDescriptor {
    String getBoundary();
}
