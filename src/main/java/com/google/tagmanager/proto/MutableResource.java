package com.google.tagmanager.proto;

import com.google.analytics.containertag.proto.MutableServing;
import com.google.tagmanager.protobuf.AbstractMutableMessageLite;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMutableMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.MessageLite;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.OutputStream;

public final class MutableResource {
    private MutableResource() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class ResourceWithMetadata extends GeneratedMutableMessageLite<ResourceWithMetadata> implements MutableMessageLite {
        public static Parser<ResourceWithMetadata> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
        public static final int RESOURCE_FIELD_NUMBER = 2;
        public static final int TIME_STAMP_FIELD_NUMBER = 1;
        private static final ResourceWithMetadata defaultInstance = new ResourceWithMetadata(true);
        private static volatile MessageLite immutableDefault = null;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private MutableServing.Resource resource_;
        private long timeStamp_;

        private ResourceWithMetadata() {
            initFields();
        }

        private ResourceWithMetadata(boolean noInit) {
        }

        public ResourceWithMetadata newMessageForType() {
            return new ResourceWithMetadata();
        }

        public static ResourceWithMetadata newMessage() {
            return new ResourceWithMetadata();
        }

        private void initFields() {
            this.resource_ = MutableServing.Resource.getDefaultInstance();
        }

        public static ResourceWithMetadata getDefaultInstance() {
            return defaultInstance;
        }

        public final ResourceWithMetadata getDefaultInstanceForType() {
            return defaultInstance;
        }

        public Parser<ResourceWithMetadata> getParserForType() {
            return PARSER;
        }

        public boolean hasTimeStamp() {
            return (this.bitField0_ & 1) == 1;
        }

        public long getTimeStamp() {
            return this.timeStamp_;
        }

        public ResourceWithMetadata setTimeStamp(long value) {
            assertMutable();
            this.bitField0_ |= 1;
            this.timeStamp_ = value;
            return this;
        }

        public ResourceWithMetadata clearTimeStamp() {
            assertMutable();
            this.bitField0_ &= -2;
            this.timeStamp_ = 0;
            return this;
        }

        private void ensureResourceInitialized() {
            if (this.resource_ == MutableServing.Resource.getDefaultInstance()) {
                this.resource_ = MutableServing.Resource.newMessage();
            }
        }

        public boolean hasResource() {
            return (this.bitField0_ & 2) == 2;
        }

        public MutableServing.Resource getResource() {
            return this.resource_;
        }

        public MutableServing.Resource getMutableResource() {
            assertMutable();
            ensureResourceInitialized();
            this.bitField0_ |= 2;
            return this.resource_;
        }

        public ResourceWithMetadata setResource(MutableServing.Resource value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 2;
            this.resource_ = value;
            return this;
        }

        public ResourceWithMetadata clearResource() {
            assertMutable();
            this.bitField0_ &= -3;
            if (this.resource_ != MutableServing.Resource.getDefaultInstance()) {
                this.resource_.clear();
            }
            return this;
        }

        public final boolean isInitialized() {
            if (hasTimeStamp() && hasResource() && getResource().isInitialized()) {
                return true;
            }
            return false;
        }

        public ResourceWithMetadata clone() {
            return newMessageForType().mergeFrom(this);
        }

        public ResourceWithMetadata mergeFrom(ResourceWithMetadata other) {
            if (this == other) {
                throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
            }
            assertMutable();
            if (other != getDefaultInstance()) {
                if (other.hasTimeStamp()) {
                    setTimeStamp(other.getTimeStamp());
                }
                if (other.hasResource()) {
                    ensureResourceInitialized();
                    this.resource_.mergeFrom(other.getResource());
                    this.bitField0_ |= 2;
                }
                this.unknownFields = this.unknownFields.concat(other.unknownFields);
            }
            return this;
        }

        public boolean mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
            assertMutable();
            try {
                ByteString.Output unknownFieldsOutput = ByteString.newOutput();
                CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        case 8:
                            this.bitField0_ |= 1;
                            this.timeStamp_ = input.readInt64();
                            break;
                        case 18:
                            if (this.resource_ == MutableServing.Resource.getDefaultInstance()) {
                                this.resource_ = MutableServing.Resource.newMessage();
                            }
                            this.bitField0_ |= 2;
                            input.readMessage((MutableMessageLite) this.resource_, extensionRegistry);
                            break;
                        default:
                            if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                break;
                            } else {
                                done = true;
                                break;
                            }
                    }
                }
                unknownFieldsCodedOutput.flush();
                this.unknownFields = unknownFieldsOutput.toByteString();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        public void writeToWithCachedSizes(CodedOutputStream output) throws IOException {
            int bytesWrittenBefore = output.getTotalBytesWritten();
            output.writeInt64(1, this.timeStamp_);
            output.writeMessageWithCachedSizes(2, this.resource_);
            output.writeRawBytes(this.unknownFields);
            if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
                throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
            }
        }

        public int getSerializedSize() {
            int size = 0 + CodedOutputStream.computeInt64Size(1, this.timeStamp_) + CodedOutputStream.computeMessageSize(2, this.resource_) + this.unknownFields.size();
            this.cachedSize = size;
            return size;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public ResourceWithMetadata clear() {
            assertMutable();
            super.clear();
            this.timeStamp_ = 0;
            this.bitField0_ &= -2;
            if (this.resource_ != MutableServing.Resource.getDefaultInstance()) {
                this.resource_.clear();
            }
            this.bitField0_ &= -3;
            return this;
        }

        public boolean equals(Object obj) {
            boolean result;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ResourceWithMetadata)) {
                return super.equals(obj);
            }
            ResourceWithMetadata other = (ResourceWithMetadata) obj;
            boolean result2 = 1 != 0 && hasTimeStamp() == other.hasTimeStamp();
            if (hasTimeStamp()) {
                result2 = result2 && getTimeStamp() == other.getTimeStamp();
            }
            if (!result2 || hasResource() != other.hasResource()) {
                result = false;
            } else {
                result = true;
            }
            if (hasResource()) {
                if (!result || !getResource().equals(other.getResource())) {
                    result = false;
                } else {
                    result = true;
                }
            }
            return result;
        }

        public int hashCode() {
            int hash = 41;
            if (hasTimeStamp()) {
                int hash2 = 1517 + 1;
                hash = 80454 + Internal.hashLong(getTimeStamp());
            }
            if (hasResource()) {
                hash = (((hash * 37) + 2) * 53) + getResource().hashCode();
            }
            return (hash * 29) + this.unknownFields.hashCode();
        }

        static {
            defaultInstance.initFields();
            defaultInstance.makeImmutable();
        }

        /* access modifiers changed from: protected */
        public MessageLite internalImmutableDefault() {
            if (immutableDefault == null) {
                immutableDefault = internalImmutableDefault("com.google.tagmanager.proto.Resource$ResourceWithMetadata");
            }
            return immutableDefault;
        }
    }
}
