package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.util.ByteSequence;

public interface Field {
    String getBody();

    String getName();

    ByteSequence getRaw();
}
