package com.google.tagmanager.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface MutableMessageLite extends MessageLite, Cloneable {
    MutableMessageLite clear();

    MutableMessageLite clone();

    int getCachedSize();

    MessageLite getDefaultInstanceForType();

    MessageLite immutableCopy();

    boolean mergeDelimitedFrom(InputStream inputStream);

    boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite);

    boolean mergeFrom(ByteString byteString);

    boolean mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite);

    boolean mergeFrom(CodedInputStream codedInputStream);

    boolean mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite);

    boolean mergeFrom(InputStream inputStream);

    boolean mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite);

    boolean mergeFrom(ByteBuffer byteBuffer);

    boolean mergeFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite);

    boolean mergeFrom(byte[] bArr);

    boolean mergeFrom(byte[] bArr, int i, int i2);

    boolean mergeFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite);

    boolean mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite);

    boolean mergePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite);

    MutableMessageLite newMessageForType();

    boolean parseDelimitedFrom(InputStream inputStream);

    boolean parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite);

    boolean parseFrom(ByteString byteString);

    boolean parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite);

    boolean parseFrom(InputStream inputStream);

    boolean parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite);

    boolean parseFrom(ByteBuffer byteBuffer);

    boolean parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite);

    boolean parseFrom(byte[] bArr);

    boolean parseFrom(byte[] bArr, int i, int i2);

    boolean parseFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite);

    boolean parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite);

    void writeToWithCachedSizes(CodedOutputStream codedOutputStream) throws IOException;
}
