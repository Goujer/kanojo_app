package org.apache.james.mime4j.descriptor;

import org.apache.james.mime4j.parser.Field;

public interface MutableBodyDescriptor extends BodyDescriptor {
    void addField(Field field);
}
