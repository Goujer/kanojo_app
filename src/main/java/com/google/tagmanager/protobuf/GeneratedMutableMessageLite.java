package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.GeneratedMutableMessageLite;
import com.google.tagmanager.protobuf.MessageLite;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class GeneratedMutableMessageLite<MessageType extends GeneratedMutableMessageLite<MessageType>> extends AbstractMutableMessageLite implements Serializable {
    private static final long serialVersionUID = 1;
    protected ByteString unknownFields = ByteString.EMPTY;

    public abstract MessageType getDefaultInstanceForType();

    /* access modifiers changed from: protected */
    public abstract MessageLite internalImmutableDefault();

    public abstract MessageType mergeFrom(MessageType messagetype);

    public Parser<MessageType> getParserForType() {
        throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
    }

    public MessageType clear() {
        assertMutable();
        this.unknownFields = ByteString.EMPTY;
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        return input.skipField(tag, unknownFieldsCodedOutput);
    }

    static MessageLite.Builder internalCopyToBuilder(MutableMessageLite fromMessage, MessageLite toMessagePrototype) {
        MessageLite.Builder builder = toMessagePrototype.newBuilderForType();
        try {
            builder.mergeFrom(fromMessage.toByteArray());
            return builder;
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Failed to parse serialized bytes (should not happen)");
        }
    }

    protected static MessageLite internalImmutableDefault(String name) {
        try {
            return (MessageLite) GeneratedMessageLite.invokeOrDie(GeneratedMessageLite.getMethodOrDie(Class.forName(name), "getDefaultInstance", new Class[0]), (Object) null, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Cannot load the corresponding immutable class. Please add necessary dependencies.");
        }
    }

    public MessageLite immutableCopy() {
        MessageLite immutableDefaultInstance = internalImmutableDefault();
        return this == getDefaultInstanceForType() ? immutableDefaultInstance : internalCopyToBuilder(this, immutableDefaultInstance).buildPartial();
    }

    public static abstract class ExtendableMutableMessage<MessageType extends ExtendableMutableMessage<MessageType>> extends GeneratedMutableMessageLite<MessageType> {
        /* access modifiers changed from: private */
        public FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions = FieldSet.emptySet();

        public /* bridge */ /* synthetic */ MessageLite getDefaultInstanceForType() {
            return GeneratedMutableMessageLite.super.getDefaultInstanceForType();
        }

        protected ExtendableMutableMessage() {
        }

        /* access modifiers changed from: package-private */
        public void internalSetExtensionSet(FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions2) {
            this.extensions = extensions2;
        }

        public MessageType clear() {
            assertMutable();
            this.extensions = FieldSet.emptySet();
            return (ExtendableMutableMessage) GeneratedMutableMessageLite.super.clear();
        }

        private void ensureExtensionsIsMutable() {
            if (this.extensions.isImmutable()) {
                this.extensions = this.extensions.clone();
            }
        }

        private void verifyExtensionContainingType(GeneratedMessageLite.GeneratedExtension<MessageType, ?> extension) {
            if (extension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            return this.extensions.hasField(extension.descriptor);
        }

        public final <Type> int getExtensionCount(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> extension) {
            verifyExtensionContainingType(extension);
            return this.extensions.getRepeatedFieldCount(extension.descriptor);
        }

        public final <Type> Type getExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            Object value = this.extensions.getField(extension.descriptor);
            if (value == null) {
                return extension.defaultValue;
            }
            if (extension.descriptor.isRepeated) {
                return Collections.unmodifiableList((List) extension.fromFieldSetType(value));
            }
            return extension.fromFieldSetType(value);
        }

        public final <Type> Type getExtension(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> extension, int index) {
            verifyExtensionContainingType(extension);
            return extension.singularFromFieldSetType(this.extensions.getRepeatedField(extension.descriptor, index));
        }

        public final <Type extends MutableMessageLite> Type getMutableExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> extension) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            GeneratedMessageLite.ExtensionDescriptor descriptor = extension.descriptor;
            if (descriptor.getLiteJavaType() != WireFormat.JavaType.MESSAGE) {
                throw new UnsupportedOperationException("getMutableExtension() called on a non-Message type.");
            } else if (descriptor.isRepeated()) {
                throw new UnsupportedOperationException("getMutableExtension() called on a repeated type.");
            } else {
                Type value = this.extensions.getField(extension.descriptor);
                if (value != null) {
                    return (MutableMessageLite) value;
                }
                Type message = ((MutableMessageLite) extension.defaultValue).newMessageForType();
                this.extensions.setField(extension.descriptor, message);
                return message;
            }
        }

        public final <Type> MessageType setExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> extension, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.setField(extension.descriptor, extension.toFieldSetType(value));
            return this;
        }

        public final <Type> MessageType setExtension(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> extension, int index, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.setRepeatedField(extension.descriptor, index, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> MessageType addExtension(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> extension, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> MessageType clearExtension(GeneratedMessageLite.GeneratedExtension<MessageType, ?> extension) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.clearField(extension.descriptor);
            return this;
        }

        /* access modifiers changed from: protected */
        public boolean extensionsAreInitialized() {
            return this.extensions.isInitialized();
        }

        /* access modifiers changed from: protected */
        public boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
            ensureExtensionsIsMutable();
            return GeneratedMutableMessageLite.parseUnknownField(this.extensions, getDefaultInstanceForType(), input, unknownFieldsCodedOutput, extensionRegistry, tag);
        }

        public MessageLite immutableCopy() {
            GeneratedMessageLite.ExtendableBuilder builder = (GeneratedMessageLite.ExtendableBuilder) internalCopyToBuilder(this, internalImmutableDefault());
            builder.internalSetExtensionSet(this.extensions.cloneWithAllFieldsToImmutable());
            return builder.buildPartial();
        }

        protected class ExtensionWriter {
            private final Iterator<Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object>> iter;
            private final boolean messageSetWireFormat;
            private Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object> next;

            private ExtensionWriter(boolean messageSetWireFormat2) {
                this.iter = ExtendableMutableMessage.this.extensions.iterator();
                if (this.iter.hasNext()) {
                    this.next = this.iter.next();
                }
                this.messageSetWireFormat = messageSetWireFormat2;
            }

            public void writeUntil(int end, CodedOutputStream output) throws IOException {
                while (this.next != null && this.next.getKey().getNumber() < end) {
                    GeneratedMessageLite.ExtensionDescriptor extension = this.next.getKey();
                    if (!this.messageSetWireFormat || extension.getLiteJavaType() != WireFormat.JavaType.MESSAGE || extension.isRepeated()) {
                        FieldSet.writeField(extension, this.next.getValue(), output);
                    } else {
                        output.writeMessageSetExtension(extension.getNumber(), (MessageLite) this.next.getValue());
                    }
                    if (this.iter.hasNext()) {
                        this.next = this.iter.next();
                    } else {
                        this.next = null;
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public ExtendableMutableMessage<MessageType>.ExtensionWriter newExtensionWriter() {
            return new ExtensionWriter(false);
        }

        /* access modifiers changed from: protected */
        public ExtendableMutableMessage<MessageType>.ExtensionWriter newMessageSetExtensionWriter() {
            return new ExtensionWriter(true);
        }

        /* access modifiers changed from: protected */
        public int extensionsSerializedSize() {
            return this.extensions.getSerializedSize();
        }

        /* access modifiers changed from: protected */
        public int extensionsSerializedSizeAsMessageSet() {
            return this.extensions.getMessageSetSerializedSize();
        }

        /* access modifiers changed from: protected */
        public final void mergeExtensionFields(MessageType other) {
            ensureExtensionsIsMutable();
            this.extensions.mergeFrom(other.extensions);
        }
    }

    static <MessageType extends MutableMessageLite> boolean parseUnknownField(FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions, MessageType defaultInstance, CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        Object findValueByNumber;
        int wireType = WireFormat.getTagWireType(tag);
        GeneratedMessageLite.GeneratedExtension<MessageType, ?> extension = extensionRegistry.findLiteExtensionByNumber(defaultInstance, WireFormat.getTagFieldNumber(tag));
        boolean unknown = false;
        boolean packed = false;
        if (extension == null) {
            unknown = true;
        } else if (wireType == FieldSet.getWireFormatForFieldType(extension.descriptor.getLiteType(), false)) {
            packed = false;
        } else if (!extension.descriptor.isRepeated || !extension.descriptor.type.isPackable() || wireType != FieldSet.getWireFormatForFieldType(extension.descriptor.getLiteType(), true)) {
            unknown = true;
        } else {
            packed = true;
        }
        if (unknown) {
            return input.skipField(tag, unknownFieldsCodedOutput);
        }
        if (packed) {
            int limit = input.pushLimit(input.readRawVarint32());
            if (extension.descriptor.getLiteType() == WireFormat.FieldType.ENUM) {
                while (input.getBytesUntilLimit() > 0) {
                    Object findValueByNumber2 = extension.descriptor.getEnumType().findValueByNumber(input.readEnum());
                    if (findValueByNumber2 == null) {
                        return true;
                    }
                    extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(findValueByNumber2));
                }
            } else {
                while (input.getBytesUntilLimit() > 0) {
                    extensions.addRepeatedField(extension.descriptor, FieldSet.readPrimitiveFieldForMutable(input, extension.descriptor.getLiteType(), false));
                }
            }
            input.popLimit(limit);
        } else {
            switch (extension.descriptor.getLiteJavaType()) {
                case MESSAGE:
                    MutableMessageLite message = ((MutableMessageLite) extension.messageDefaultInstance).newMessageForType();
                    if (extension.descriptor.getLiteType() == WireFormat.FieldType.GROUP) {
                        input.readGroup(extension.getNumber(), message, extensionRegistry);
                    } else {
                        input.readMessage(message, extensionRegistry);
                    }
                    findValueByNumber = message;
                    break;
                case ENUM:
                    int rawValue = input.readEnum();
                    findValueByNumber = extension.descriptor.getEnumType().findValueByNumber(rawValue);
                    if (findValueByNumber == null) {
                        unknownFieldsCodedOutput.writeRawVarint32(tag);
                        unknownFieldsCodedOutput.writeUInt32NoTag(rawValue);
                        return true;
                    }
                    break;
                default:
                    findValueByNumber = FieldSet.readPrimitiveFieldForMutable(input, extension.descriptor.getLiteType(), false);
                    break;
            }
            if (extension.descriptor.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(findValueByNumber));
            } else {
                extensions.setField(extension.descriptor, extension.singularToFieldSetType(findValueByNumber));
            }
        }
        return true;
    }

    static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private byte[] asBytes;
        private String messageClassName;

        SerializedForm(MutableMessageLite regularForm) {
            this.messageClassName = regularForm.getClass().getName();
            this.asBytes = regularForm.toByteArray();
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            try {
                MutableMessageLite message = (MutableMessageLite) Class.forName(this.messageClassName).getMethod("newMessage", new Class[0]).invoke((Object) null, new Object[0]);
                if (message.mergeFrom(CodedInputStream.newInstance(this.asBytes))) {
                    return message;
                }
                throw new RuntimeException("Unable to understand proto buffer");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find proto buffer class", e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException("Unable to find newMessage method", e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException("Unable to call newMessage method", e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Error calling newMessage", e4.getCause());
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }
}
