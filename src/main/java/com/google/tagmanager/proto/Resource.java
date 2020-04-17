package com.google.tagmanager.proto;

import com.google.analytics.containertag.proto.Serving;
import com.google.tagmanager.protobuf.AbstractParser;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.InvalidProtocolBufferException;
import com.google.tagmanager.protobuf.MessageLiteOrBuilder;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;

public final class Resource {

    public interface ResourceWithMetadataOrBuilder extends MessageLiteOrBuilder {
        Serving.Resource getResource();

        long getTimeStamp();

        boolean hasResource();

        boolean hasTimeStamp();
    }

    private Resource() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class ResourceWithMetadata extends GeneratedMessageLite implements ResourceWithMetadataOrBuilder {
        public static Parser<ResourceWithMetadata> PARSER = new AbstractParser<ResourceWithMetadata>() {
            public ResourceWithMetadata parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ResourceWithMetadata(input, extensionRegistry);
            }
        };
        public static final int RESOURCE_FIELD_NUMBER = 2;
        public static final int TIME_STAMP_FIELD_NUMBER = 1;
        private static final ResourceWithMetadata defaultInstance = new ResourceWithMetadata(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Serving.Resource resource_;
        /* access modifiers changed from: private */
        public long timeStamp_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;

        private ResourceWithMetadata(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private ResourceWithMetadata(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static ResourceWithMetadata getDefaultInstance() {
            return defaultInstance;
        }

        public ResourceWithMetadata getDefaultInstanceForType() {
            return defaultInstance;
        }

        private ResourceWithMetadata(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            initFields();
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance((OutputStream) unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
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
                            Serving.Resource.Builder subBuilder = (this.bitField0_ & 2) == 2 ? this.resource_.toBuilder() : null;
                            this.resource_ = (Serving.Resource) input.readMessage(Serving.Resource.PARSER, extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.resource_);
                                this.resource_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
                            break;
                        default:
                            if (parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                break;
                            } else {
                                done = true;
                                break;
                            }
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw e.setUnfinishedMessage(this);
                } catch (IOException e2) {
                    throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                } catch (Throwable th) {
                    try {
                        unknownFieldsCodedOutput.flush();
                    } catch (IOException e3) {
                    } finally {
                        this.unknownFields = unknownFieldsOutput.toByteString();
                    }
                    makeExtensionsImmutable();
                    throw th;
                }
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
                this.unknownFields = unknownFieldsOutput.toByteString();
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
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

        public boolean hasResource() {
            return (this.bitField0_ & 2) == 2;
        }

        public Serving.Resource getResource() {
            return this.resource_;
        }

        private void initFields() {
            this.timeStamp_ = 0;
            this.resource_ = Serving.Resource.getDefaultInstance();
        }

        public final boolean isInitialized() {
            boolean z = true;
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                if (isInitialized != 1) {
                    z = false;
                }
                return z;
            } else if (!hasTimeStamp()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasResource()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!getResource().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt64(1, this.timeStamp_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.resource_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                size2 = 0 + CodedOutputStream.computeInt64Size(1, this.timeStamp_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeMessageSize(2, this.resource_);
            }
            int size3 = size2 + this.unknownFields.size();
            this.memoizedSerializedSize = size3;
            return size3;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
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
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ResourceWithMetadata.class.hashCode() + 779;
            if (hasTimeStamp()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashLong(getTimeStamp());
            }
            if (hasResource()) {
                hash = (((hash * 37) + 2) * 53) + getResource().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.tagmanager.proto.MutableResource$ResourceWithMetadata");
            }
            return mutableDefault;
        }

        public static ResourceWithMetadata parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResourceWithMetadata parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResourceWithMetadata parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResourceWithMetadata parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResourceWithMetadata parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResourceWithMetadata parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ResourceWithMetadata parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ResourceWithMetadata parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ResourceWithMetadata parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResourceWithMetadata parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ResourceWithMetadata prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ResourceWithMetadata, Builder> implements ResourceWithMetadataOrBuilder {
            private int bitField0_;
            private Serving.Resource resource_ = Serving.Resource.getDefaultInstance();
            private long timeStamp_;

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.timeStamp_ = 0;
                this.bitField0_ &= -2;
                this.resource_ = Serving.Resource.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public ResourceWithMetadata getDefaultInstanceForType() {
                return ResourceWithMetadata.getDefaultInstance();
            }

            public ResourceWithMetadata build() {
                ResourceWithMetadata result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public ResourceWithMetadata buildPartial() {
                ResourceWithMetadata result = new ResourceWithMetadata((GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                long unused = result.timeStamp_ = this.timeStamp_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                Serving.Resource unused2 = result.resource_ = this.resource_;
                int unused3 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(ResourceWithMetadata other) {
                if (other != ResourceWithMetadata.getDefaultInstance()) {
                    if (other.hasTimeStamp()) {
                        setTimeStamp(other.getTimeStamp());
                    }
                    if (other.hasResource()) {
                        mergeResource(other.getResource());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasTimeStamp() && hasResource() && getResource().isInitialized()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    ResourceWithMetadata parsedMessage = ResourceWithMetadata.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    ResourceWithMetadata parsedMessage2 = (ResourceWithMetadata) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((ResourceWithMetadata) null);
                    }
                    throw th;
                }
            }

            public boolean hasTimeStamp() {
                return (this.bitField0_ & 1) == 1;
            }

            public long getTimeStamp() {
                return this.timeStamp_;
            }

            public Builder setTimeStamp(long value) {
                this.bitField0_ |= 1;
                this.timeStamp_ = value;
                return this;
            }

            public Builder clearTimeStamp() {
                this.bitField0_ &= -2;
                this.timeStamp_ = 0;
                return this;
            }

            public boolean hasResource() {
                return (this.bitField0_ & 2) == 2;
            }

            public Serving.Resource getResource() {
                return this.resource_;
            }

            public Builder setResource(Serving.Resource value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.resource_ = value;
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setResource(Serving.Resource.Builder builderForValue) {
                this.resource_ = builderForValue.build();
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeResource(Serving.Resource value) {
                if ((this.bitField0_ & 2) != 2 || this.resource_ == Serving.Resource.getDefaultInstance()) {
                    this.resource_ = value;
                } else {
                    this.resource_ = Serving.Resource.newBuilder(this.resource_).mergeFrom(value).buildPartial();
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearResource() {
                this.resource_ = Serving.Resource.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }
        }
    }
}
