package com.google.tagmanager.protobuf;

import java.util.Collection;
import java.util.List;

public interface LazyStringList extends List<String> {
    void add(ByteString byteString);

    void add(byte[] bArr);

    boolean addAllByteArray(Collection<byte[]> collection);

    boolean addAllByteString(Collection<? extends ByteString> collection);

    List<byte[]> asByteArrayList();

    byte[] getByteArray(int i);

    ByteString getByteString(int i);

    List<?> getUnderlyingElements();

    void mergeFrom(LazyStringList lazyStringList);

    void set(int i, ByteString byteString);

    void set(int i, byte[] bArr);
}
