package org.apache.james.mime4j.util;

public interface ByteSequence {
    public static final ByteSequence EMPTY = new EmptyByteSequence();

    byte byteAt(int i);

    int length();

    byte[] toByteArray();
}
