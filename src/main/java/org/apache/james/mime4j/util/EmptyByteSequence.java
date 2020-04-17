package org.apache.james.mime4j.util;

final class EmptyByteSequence implements ByteSequence {
    private static final byte[] EMPTY_BYTES = new byte[0];

    EmptyByteSequence() {
    }

    public int length() {
        return 0;
    }

    public byte byteAt(int index) {
        throw new IndexOutOfBoundsException();
    }

    public byte[] toByteArray() {
        return EMPTY_BYTES;
    }
}
