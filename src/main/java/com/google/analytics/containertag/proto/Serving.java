package com.google.analytics.containertag.proto;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.tagmanager.protobuf.AbstractMessageLite;
import com.google.tagmanager.protobuf.AbstractParser;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.InvalidProtocolBufferException;
import com.google.tagmanager.protobuf.LazyStringArrayList;
import com.google.tagmanager.protobuf.LazyStringList;
import com.google.tagmanager.protobuf.MessageLiteOrBuilder;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import com.google.tagmanager.protobuf.UnmodifiableLazyStringList;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;

public final class Serving {

    public interface CacheOptionOrBuilder extends MessageLiteOrBuilder {
        int getExpirationSeconds();

        int getGcacheExpirationSeconds();

        CacheOption.CacheLevel getLevel();

        boolean hasExpirationSeconds();

        boolean hasGcacheExpirationSeconds();

        boolean hasLevel();
    }

    public interface ContainerOrBuilder extends MessageLiteOrBuilder {
        String getContainerId();

        ByteString getContainerIdBytes();

        Resource getJsResource();

        ResourceState getState();

        String getVersion();

        ByteString getVersionBytes();

        boolean hasContainerId();

        boolean hasJsResource();

        boolean hasState();

        boolean hasVersion();
    }

    public interface FunctionCallOrBuilder extends MessageLiteOrBuilder {
        int getFunction();

        boolean getLiveOnly();

        int getName();

        int getProperty(int i);

        int getPropertyCount();

        List<Integer> getPropertyList();

        boolean getServerSide();

        boolean hasFunction();

        boolean hasLiveOnly();

        boolean hasName();

        boolean hasServerSide();
    }

    public interface OptionalResourceOrBuilder extends MessageLiteOrBuilder {
        Resource getResource();

        boolean hasResource();
    }

    public interface PropertyOrBuilder extends MessageLiteOrBuilder {
        int getKey();

        int getValue();

        boolean hasKey();

        boolean hasValue();
    }

    public interface ResourceOrBuilder extends MessageLiteOrBuilder {
        boolean getEnableAutoEventTracking();

        String getKey(int i);

        ByteString getKeyBytes(int i);

        int getKeyCount();

        List<String> getKeyList();

        CacheOption getLiveJsCacheOption();

        FunctionCall getMacro(int i);

        int getMacroCount();

        List<FunctionCall> getMacroList();

        String getMalwareScanAuthCode();

        ByteString getMalwareScanAuthCodeBytes();

        FunctionCall getPredicate(int i);

        int getPredicateCount();

        List<FunctionCall> getPredicateList();

        String getPreviewAuthCode();

        ByteString getPreviewAuthCodeBytes();

        Property getProperty(int i);

        int getPropertyCount();

        List<Property> getPropertyList();

        float getReportingSampleRate();

        int getResourceFormatVersion();

        Rule getRule(int i);

        int getRuleCount();

        List<Rule> getRuleList();

        FunctionCall getTag(int i);

        int getTagCount();

        List<FunctionCall> getTagList();

        String getTemplateVersionSet();

        ByteString getTemplateVersionSetBytes();

        String getUsageContext(int i);

        ByteString getUsageContextBytes(int i);

        int getUsageContextCount();

        List<String> getUsageContextList();

        TypeSystem.Value getValue(int i);

        int getValueCount();

        List<TypeSystem.Value> getValueList();

        String getVersion();

        ByteString getVersionBytes();

        boolean hasEnableAutoEventTracking();

        boolean hasLiveJsCacheOption();

        boolean hasMalwareScanAuthCode();

        boolean hasPreviewAuthCode();

        boolean hasReportingSampleRate();

        boolean hasResourceFormatVersion();

        boolean hasTemplateVersionSet();

        boolean hasVersion();
    }

    public interface RuleOrBuilder extends MessageLiteOrBuilder {
        int getAddMacro(int i);

        int getAddMacroCount();

        List<Integer> getAddMacroList();

        int getAddMacroRuleName(int i);

        int getAddMacroRuleNameCount();

        List<Integer> getAddMacroRuleNameList();

        int getAddTag(int i);

        int getAddTagCount();

        List<Integer> getAddTagList();

        int getAddTagRuleName(int i);

        int getAddTagRuleNameCount();

        List<Integer> getAddTagRuleNameList();

        int getNegativePredicate(int i);

        int getNegativePredicateCount();

        List<Integer> getNegativePredicateList();

        int getPositivePredicate(int i);

        int getPositivePredicateCount();

        List<Integer> getPositivePredicateList();

        int getRemoveMacro(int i);

        int getRemoveMacroCount();

        List<Integer> getRemoveMacroList();

        int getRemoveMacroRuleName(int i);

        int getRemoveMacroRuleNameCount();

        List<Integer> getRemoveMacroRuleNameList();

        int getRemoveTag(int i);

        int getRemoveTagCount();

        List<Integer> getRemoveTagList();

        int getRemoveTagRuleName(int i);

        int getRemoveTagRuleNameCount();

        List<Integer> getRemoveTagRuleNameList();
    }

    public interface ServingValueOrBuilder extends MessageLiteOrBuilder {
        int getListItem(int i);

        int getListItemCount();

        List<Integer> getListItemList();

        int getMacroNameReference();

        int getMacroReference();

        int getMapKey(int i);

        int getMapKeyCount();

        List<Integer> getMapKeyList();

        int getMapValue(int i);

        int getMapValueCount();

        List<Integer> getMapValueList();

        int getTemplateToken(int i);

        int getTemplateTokenCount();

        List<Integer> getTemplateTokenList();

        boolean hasMacroNameReference();

        boolean hasMacroReference();
    }

    private Serving() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
        registry.add(ServingValue.ext);
    }

    public enum ResourceType implements Internal.EnumLite {
        JS_RESOURCE(0, 1),
        NS_RESOURCE(1, 2),
        PIXEL_COLLECTION(2, 3),
        SET_COOKIE(3, 4),
        GET_COOKIE(4, 5),
        CLEAR_CACHE(5, 6),
        RAW_PROTO(6, 7);
        
        public static final int CLEAR_CACHE_VALUE = 6;
        public static final int GET_COOKIE_VALUE = 5;
        public static final int JS_RESOURCE_VALUE = 1;
        public static final int NS_RESOURCE_VALUE = 2;
        public static final int PIXEL_COLLECTION_VALUE = 3;
        public static final int RAW_PROTO_VALUE = 7;
        public static final int SET_COOKIE_VALUE = 4;
        private static Internal.EnumLiteMap<ResourceType> internalValueMap;
        private final int value;

        static {
            internalValueMap = new Internal.EnumLiteMap<ResourceType>() {
                public ResourceType findValueByNumber(int number) {
                    return ResourceType.valueOf(number);
                }
            };
        }

        public final int getNumber() {
            return this.value;
        }

        public static ResourceType valueOf(int value2) {
            switch (value2) {
                case 1:
                    return JS_RESOURCE;
                case 2:
                    return NS_RESOURCE;
                case 3:
                    return PIXEL_COLLECTION;
                case 4:
                    return SET_COOKIE;
                case 5:
                    return GET_COOKIE;
                case 6:
                    return CLEAR_CACHE;
                case 7:
                    return RAW_PROTO;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<ResourceType> internalGetValueMap() {
            return internalValueMap;
        }

        private ResourceType(int index, int value2) {
            this.value = value2;
        }
    }

    public enum ResourceState implements Internal.EnumLite {
        PREVIEW(0, 1),
        LIVE(1, 2);
        
        public static final int LIVE_VALUE = 2;
        public static final int PREVIEW_VALUE = 1;
        private static Internal.EnumLiteMap<ResourceState> internalValueMap;
        private final int value;

        static {
            internalValueMap = new Internal.EnumLiteMap<ResourceState>() {
                public ResourceState findValueByNumber(int number) {
                    return ResourceState.valueOf(number);
                }
            };
        }

        public final int getNumber() {
            return this.value;
        }

        public static ResourceState valueOf(int value2) {
            switch (value2) {
                case 1:
                    return PREVIEW;
                case 2:
                    return LIVE;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<ResourceState> internalGetValueMap() {
            return internalValueMap;
        }

        private ResourceState(int index, int value2) {
            this.value = value2;
        }
    }

    public static final class Container extends GeneratedMessageLite implements ContainerOrBuilder {
        public static final int CONTAINER_ID_FIELD_NUMBER = 3;
        public static final int JS_RESOURCE_FIELD_NUMBER = 1;
        public static Parser<Container> PARSER = new AbstractParser<Container>() {
            public Container parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Container(input, extensionRegistry);
            }
        };
        public static final int STATE_FIELD_NUMBER = 4;
        public static final int VERSION_FIELD_NUMBER = 5;
        private static final Container defaultInstance = new Container(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public Object containerId_;
        /* access modifiers changed from: private */
        public Resource jsResource_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public ResourceState state_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;
        /* access modifiers changed from: private */
        public Object version_;

        private Container(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Container(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Container getDefaultInstance() {
            return defaultInstance;
        }

        public Container getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Container(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                        case 10:
                            Resource.Builder subBuilder = (this.bitField0_ & 1) == 1 ? this.jsResource_.toBuilder() : null;
                            this.jsResource_ = (Resource) input.readMessage(Resource.PARSER, extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.jsResource_);
                                this.jsResource_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
                            break;
                        case 26:
                            ByteString bs = input.readBytes();
                            this.bitField0_ |= 2;
                            this.containerId_ = bs;
                            break;
                        case 32:
                            int rawValue = input.readEnum();
                            ResourceState value = ResourceState.valueOf(rawValue);
                            if (value != null) {
                                this.bitField0_ |= 4;
                                this.state_ = value;
                                break;
                            } else {
                                unknownFieldsCodedOutput.writeRawVarint32(tag);
                                unknownFieldsCodedOutput.writeRawVarint32(rawValue);
                                break;
                            }
                        case 42:
                            ByteString bs2 = input.readBytes();
                            this.bitField0_ |= 8;
                            this.version_ = bs2;
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

        public Parser<Container> getParserForType() {
            return PARSER;
        }

        public boolean hasJsResource() {
            return (this.bitField0_ & 1) == 1;
        }

        public Resource getJsResource() {
            return this.jsResource_;
        }

        public boolean hasContainerId() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getContainerId() {
            Object ref = this.containerId_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.containerId_ = s;
            }
            return s;
        }

        public ByteString getContainerIdBytes() {
            Object ref = this.containerId_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.containerId_ = b;
            return b;
        }

        public boolean hasState() {
            return (this.bitField0_ & 4) == 4;
        }

        public ResourceState getState() {
            return this.state_;
        }

        public boolean hasVersion() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getVersion() {
            Object ref = this.version_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.version_ = s;
            }
            return s;
        }

        public ByteString getVersionBytes() {
            Object ref = this.version_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.version_ = b;
            return b;
        }

        private void initFields() {
            this.jsResource_ = Resource.getDefaultInstance();
            this.containerId_ = "";
            this.state_ = ResourceState.PREVIEW;
            this.version_ = "";
        }

        public final boolean isInitialized() {
            boolean z = true;
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                if (isInitialized != 1) {
                    z = false;
                }
                return z;
            } else if (!hasJsResource()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasContainerId()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasState()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!getJsResource().isInitialized()) {
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
                output.writeMessage(1, this.jsResource_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(3, getContainerIdBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeEnum(4, this.state_.getNumber());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeBytes(5, getVersionBytes());
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
                size2 = 0 + CodedOutputStream.computeMessageSize(1, this.jsResource_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(3, getContainerIdBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeEnumSize(4, this.state_.getNumber());
            }
            if ((this.bitField0_ & 8) == 8) {
                size2 += CodedOutputStream.computeBytesSize(5, getVersionBytes());
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
            boolean result2;
            boolean result3;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Container)) {
                return super.equals(obj);
            }
            Container other = (Container) obj;
            boolean result4 = 1 != 0 && hasJsResource() == other.hasJsResource();
            if (hasJsResource()) {
                result4 = result4 && getJsResource().equals(other.getJsResource());
            }
            if (!result4 || hasContainerId() != other.hasContainerId()) {
                result = false;
            } else {
                result = true;
            }
            if (hasContainerId()) {
                if (!result || !getContainerId().equals(other.getContainerId())) {
                    result = false;
                } else {
                    result = true;
                }
            }
            if (!result || hasState() != other.hasState()) {
                result2 = false;
            } else {
                result2 = true;
            }
            if (hasState()) {
                if (!result2 || getState() != other.getState()) {
                    result2 = false;
                } else {
                    result2 = true;
                }
            }
            if (!result2 || hasVersion() != other.hasVersion()) {
                result3 = false;
            } else {
                result3 = true;
            }
            if (hasVersion()) {
                if (!result3 || !getVersion().equals(other.getVersion())) {
                    result3 = false;
                } else {
                    result3 = true;
                }
            }
            return result3;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Container.class.hashCode() + 779;
            if (hasJsResource()) {
                hash = (((hash * 37) + 1) * 53) + getJsResource().hashCode();
            }
            if (hasContainerId()) {
                hash = (((hash * 37) + 3) * 53) + getContainerId().hashCode();
            }
            if (hasState()) {
                hash = (((hash * 37) + 4) * 53) + Internal.hashEnum(getState());
            }
            if (hasVersion()) {
                hash = (((hash * 37) + 5) * 53) + getVersion().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Container");
            }
            return mutableDefault;
        }

        public static Container parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Container parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Container parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Container parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Container parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Container parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Container parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Container parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Container parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Container parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Container prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Container, Builder> implements ContainerOrBuilder {
            private int bitField0_;
            private Object containerId_ = "";
            private Resource jsResource_ = Resource.getDefaultInstance();
            private ResourceState state_ = ResourceState.PREVIEW;
            private Object version_ = "";

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
                this.jsResource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                this.containerId_ = "";
                this.bitField0_ &= -3;
                this.state_ = ResourceState.PREVIEW;
                this.bitField0_ &= -5;
                this.version_ = "";
                this.bitField0_ &= -9;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public Container getDefaultInstanceForType() {
                return Container.getDefaultInstance();
            }

            public Container build() {
                Container result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public Container buildPartial() {
                Container result = new Container((GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                Resource unused = result.jsResource_ = this.jsResource_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                Object unused2 = result.containerId_ = this.containerId_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                ResourceState unused3 = result.state_ = this.state_;
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ |= 8;
                }
                Object unused4 = result.version_ = this.version_;
                int unused5 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(Container other) {
                if (other != Container.getDefaultInstance()) {
                    if (other.hasJsResource()) {
                        mergeJsResource(other.getJsResource());
                    }
                    if (other.hasContainerId()) {
                        this.bitField0_ |= 2;
                        this.containerId_ = other.containerId_;
                    }
                    if (other.hasState()) {
                        setState(other.getState());
                    }
                    if (other.hasVersion()) {
                        this.bitField0_ |= 8;
                        this.version_ = other.version_;
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasJsResource() && hasContainerId() && hasState() && getJsResource().isInitialized()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    Container parsedMessage = Container.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    Container parsedMessage2 = (Container) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((Container) null);
                    }
                    throw th;
                }
            }

            public boolean hasJsResource() {
                return (this.bitField0_ & 1) == 1;
            }

            public Resource getJsResource() {
                return this.jsResource_;
            }

            public Builder setJsResource(Resource value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.jsResource_ = value;
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setJsResource(Resource.Builder builderForValue) {
                this.jsResource_ = builderForValue.build();
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeJsResource(Resource value) {
                if ((this.bitField0_ & 1) != 1 || this.jsResource_ == Resource.getDefaultInstance()) {
                    this.jsResource_ = value;
                } else {
                    this.jsResource_ = Resource.newBuilder(this.jsResource_).mergeFrom(value).buildPartial();
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearJsResource() {
                this.jsResource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }

            public boolean hasContainerId() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getContainerId() {
                Object ref = this.containerId_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.containerId_ = s;
                return s;
            }

            public ByteString getContainerIdBytes() {
                Object ref = this.containerId_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.containerId_ = b;
                return b;
            }

            public Builder setContainerId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.containerId_ = value;
                return this;
            }

            public Builder clearContainerId() {
                this.bitField0_ &= -3;
                this.containerId_ = Container.getDefaultInstance().getContainerId();
                return this;
            }

            public Builder setContainerIdBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.containerId_ = value;
                return this;
            }

            public boolean hasState() {
                return (this.bitField0_ & 4) == 4;
            }

            public ResourceState getState() {
                return this.state_;
            }

            public Builder setState(ResourceState value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.state_ = value;
                return this;
            }

            public Builder clearState() {
                this.bitField0_ &= -5;
                this.state_ = ResourceState.PREVIEW;
                return this;
            }

            public boolean hasVersion() {
                return (this.bitField0_ & 8) == 8;
            }

            public String getVersion() {
                Object ref = this.version_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.version_ = s;
                return s;
            }

            public ByteString getVersionBytes() {
                Object ref = this.version_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.version_ = b;
                return b;
            }

            public Builder setVersion(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.version_ = value;
                return this;
            }

            public Builder clearVersion() {
                this.bitField0_ &= -9;
                this.version_ = Container.getDefaultInstance().getVersion();
                return this;
            }

            public Builder setVersionBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.version_ = value;
                return this;
            }
        }
    }

    public static final class ServingValue extends GeneratedMessageLite implements ServingValueOrBuilder {
        public static final int EXT_FIELD_NUMBER = 101;
        public static final int LIST_ITEM_FIELD_NUMBER = 1;
        public static final int MACRO_NAME_REFERENCE_FIELD_NUMBER = 6;
        public static final int MACRO_REFERENCE_FIELD_NUMBER = 4;
        public static final int MAP_KEY_FIELD_NUMBER = 2;
        public static final int MAP_VALUE_FIELD_NUMBER = 3;
        public static Parser<ServingValue> PARSER = new AbstractParser<ServingValue>() {
            public ServingValue parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ServingValue(input, extensionRegistry);
            }
        };
        public static final int TEMPLATE_TOKEN_FIELD_NUMBER = 5;
        private static final ServingValue defaultInstance = new ServingValue(true);
        public static final GeneratedMessageLite.GeneratedExtension<TypeSystem.Value, ServingValue> ext = GeneratedMessageLite.newSingularGeneratedExtension(TypeSystem.Value.getDefaultInstance(), getDefaultInstance(), getDefaultInstance(), (Internal.EnumLiteMap<?>) null, 101, WireFormat.FieldType.MESSAGE, ServingValue.class);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public List<Integer> listItem_;
        /* access modifiers changed from: private */
        public int macroNameReference_;
        /* access modifiers changed from: private */
        public int macroReference_;
        /* access modifiers changed from: private */
        public List<Integer> mapKey_;
        /* access modifiers changed from: private */
        public List<Integer> mapValue_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public List<Integer> templateToken_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;

        private ServingValue(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private ServingValue(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static ServingValue getDefaultInstance() {
            return defaultInstance;
        }

        public ServingValue getDefaultInstanceForType() {
            return defaultInstance;
        }

        private ServingValue(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int mutable_bitField0_ = 0;
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
                            if ((mutable_bitField0_ & 1) != 1) {
                                this.listItem_ = new ArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            this.listItem_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 10:
                            int limit = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 1) != 1 && input.getBytesUntilLimit() > 0) {
                                this.listItem_ = new ArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.listItem_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit);
                            break;
                        case 16:
                            if ((mutable_bitField0_ & 2) != 2) {
                                this.mapKey_ = new ArrayList();
                                mutable_bitField0_ |= 2;
                            }
                            this.mapKey_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 18:
                            int limit2 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 2) != 2 && input.getBytesUntilLimit() > 0) {
                                this.mapKey_ = new ArrayList();
                                mutable_bitField0_ |= 2;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.mapKey_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit2);
                            break;
                        case 24:
                            if ((mutable_bitField0_ & 4) != 4) {
                                this.mapValue_ = new ArrayList();
                                mutable_bitField0_ |= 4;
                            }
                            this.mapValue_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 26:
                            int limit3 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 4) != 4 && input.getBytesUntilLimit() > 0) {
                                this.mapValue_ = new ArrayList();
                                mutable_bitField0_ |= 4;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.mapValue_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit3);
                            break;
                        case 32:
                            this.bitField0_ |= 1;
                            this.macroReference_ = input.readInt32();
                            break;
                        case 40:
                            if ((mutable_bitField0_ & 16) != 16) {
                                this.templateToken_ = new ArrayList();
                                mutable_bitField0_ |= 16;
                            }
                            this.templateToken_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 42:
                            int limit4 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 16) != 16 && input.getBytesUntilLimit() > 0) {
                                this.templateToken_ = new ArrayList();
                                mutable_bitField0_ |= 16;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.templateToken_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit4);
                            break;
                        case DateTimeParserConstants.ANY /*48*/:
                            this.bitField0_ |= 2;
                            this.macroNameReference_ = input.readInt32();
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
                    if ((mutable_bitField0_ & 1) == 1) {
                        this.listItem_ = Collections.unmodifiableList(this.listItem_);
                    }
                    if ((mutable_bitField0_ & 2) == 2) {
                        this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                    }
                    if ((mutable_bitField0_ & 4) == 4) {
                        this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                    }
                    if ((mutable_bitField0_ & 16) == 16) {
                        this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
                    }
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
            if ((mutable_bitField0_ & 1) == 1) {
                this.listItem_ = Collections.unmodifiableList(this.listItem_);
            }
            if ((mutable_bitField0_ & 2) == 2) {
                this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
            }
            if ((mutable_bitField0_ & 4) == 4) {
                this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
            }
            if ((mutable_bitField0_ & 16) == 16) {
                this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
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

        public Parser<ServingValue> getParserForType() {
            return PARSER;
        }

        public List<Integer> getListItemList() {
            return this.listItem_;
        }

        public int getListItemCount() {
            return this.listItem_.size();
        }

        public int getListItem(int index) {
            return this.listItem_.get(index).intValue();
        }

        public List<Integer> getMapKeyList() {
            return this.mapKey_;
        }

        public int getMapKeyCount() {
            return this.mapKey_.size();
        }

        public int getMapKey(int index) {
            return this.mapKey_.get(index).intValue();
        }

        public List<Integer> getMapValueList() {
            return this.mapValue_;
        }

        public int getMapValueCount() {
            return this.mapValue_.size();
        }

        public int getMapValue(int index) {
            return this.mapValue_.get(index).intValue();
        }

        public boolean hasMacroReference() {
            return (this.bitField0_ & 1) == 1;
        }

        public int getMacroReference() {
            return this.macroReference_;
        }

        public List<Integer> getTemplateTokenList() {
            return this.templateToken_;
        }

        public int getTemplateTokenCount() {
            return this.templateToken_.size();
        }

        public int getTemplateToken(int index) {
            return this.templateToken_.get(index).intValue();
        }

        public boolean hasMacroNameReference() {
            return (this.bitField0_ & 2) == 2;
        }

        public int getMacroNameReference() {
            return this.macroNameReference_;
        }

        private void initFields() {
            this.listItem_ = Collections.emptyList();
            this.mapKey_ = Collections.emptyList();
            this.mapValue_ = Collections.emptyList();
            this.macroReference_ = 0;
            this.templateToken_ = Collections.emptyList();
            this.macroNameReference_ = 0;
        }

        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == -1) {
                this.memoizedIsInitialized = 1;
                return true;
            } else if (isInitialized == 1) {
                return true;
            } else {
                return false;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.listItem_.size(); i++) {
                output.writeInt32(1, this.listItem_.get(i).intValue());
            }
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                output.writeInt32(2, this.mapKey_.get(i2).intValue());
            }
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                output.writeInt32(3, this.mapValue_.get(i3).intValue());
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt32(4, this.macroReference_);
            }
            for (int i4 = 0; i4 < this.templateToken_.size(); i4++) {
                output.writeInt32(5, this.templateToken_.get(i4).intValue());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(6, this.macroNameReference_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int dataSize = 0;
            for (int i = 0; i < this.listItem_.size(); i++) {
                dataSize += CodedOutputStream.computeInt32SizeNoTag(this.listItem_.get(i).intValue());
            }
            int size2 = 0 + dataSize + (getListItemList().size() * 1);
            int dataSize2 = 0;
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.mapKey_.get(i2).intValue());
            }
            int size3 = size2 + dataSize2 + (getMapKeyList().size() * 1);
            int dataSize3 = 0;
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                dataSize3 += CodedOutputStream.computeInt32SizeNoTag(this.mapValue_.get(i3).intValue());
            }
            int size4 = size3 + dataSize3 + (getMapValueList().size() * 1);
            if ((this.bitField0_ & 1) == 1) {
                size4 += CodedOutputStream.computeInt32Size(4, this.macroReference_);
            }
            int dataSize4 = 0;
            for (int i4 = 0; i4 < this.templateToken_.size(); i4++) {
                dataSize4 += CodedOutputStream.computeInt32SizeNoTag(this.templateToken_.get(i4).intValue());
            }
            int size5 = size4 + dataSize4 + (getTemplateTokenList().size() * 1);
            if ((this.bitField0_ & 2) == 2) {
                size5 += CodedOutputStream.computeInt32Size(6, this.macroNameReference_);
            }
            int size6 = size5 + this.unknownFields.size();
            this.memoizedSerializedSize = size6;
            return size6;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            boolean result;
            boolean result2;
            boolean result3;
            boolean result4;
            boolean result5;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ServingValue)) {
                return super.equals(obj);
            }
            ServingValue other = (ServingValue) obj;
            if (!(1 != 0 && getListItemList().equals(other.getListItemList())) || !getMapKeyList().equals(other.getMapKeyList())) {
                result = false;
            } else {
                result = true;
            }
            if (!result || !getMapValueList().equals(other.getMapValueList())) {
                result2 = false;
            } else {
                result2 = true;
            }
            if (!result2 || hasMacroReference() != other.hasMacroReference()) {
                result3 = false;
            } else {
                result3 = true;
            }
            if (hasMacroReference()) {
                if (!result3 || getMacroReference() != other.getMacroReference()) {
                    result3 = false;
                } else {
                    result3 = true;
                }
            }
            if (!result3 || !getTemplateTokenList().equals(other.getTemplateTokenList())) {
                result4 = false;
            } else {
                result4 = true;
            }
            if (!result4 || hasMacroNameReference() != other.hasMacroNameReference()) {
                result5 = false;
            } else {
                result5 = true;
            }
            if (hasMacroNameReference()) {
                if (!result5 || getMacroNameReference() != other.getMacroNameReference()) {
                    result5 = false;
                } else {
                    result5 = true;
                }
            }
            return result5;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ServingValue.class.hashCode() + 779;
            if (getListItemCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getListItemList().hashCode();
            }
            if (getMapKeyCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getMapKeyList().hashCode();
            }
            if (getMapValueCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getMapValueList().hashCode();
            }
            if (hasMacroReference()) {
                hash = (((hash * 37) + 4) * 53) + getMacroReference();
            }
            if (getTemplateTokenCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getTemplateTokenList().hashCode();
            }
            if (hasMacroNameReference()) {
                hash = (((hash * 37) + 6) * 53) + getMacroNameReference();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$ServingValue");
            }
            return mutableDefault;
        }

        public static ServingValue parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServingValue parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServingValue parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServingValue parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServingValue parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServingValue parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ServingValue parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ServingValue parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ServingValue parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServingValue parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ServingValue prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ServingValue, Builder> implements ServingValueOrBuilder {
            private int bitField0_;
            private List<Integer> listItem_ = Collections.emptyList();
            private int macroNameReference_;
            private int macroReference_;
            private List<Integer> mapKey_ = Collections.emptyList();
            private List<Integer> mapValue_ = Collections.emptyList();
            private List<Integer> templateToken_ = Collections.emptyList();

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
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -3;
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.macroReference_ = 0;
                this.bitField0_ &= -9;
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.macroNameReference_ = 0;
                this.bitField0_ &= -33;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public ServingValue getDefaultInstanceForType() {
                return ServingValue.getDefaultInstance();
            }

            public ServingValue build() {
                ServingValue result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public ServingValue buildPartial() {
                ServingValue result = new ServingValue((GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.listItem_ = Collections.unmodifiableList(this.listItem_);
                    this.bitField0_ &= -2;
                }
                List unused = result.listItem_ = this.listItem_;
                if ((this.bitField0_ & 2) == 2) {
                    this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                    this.bitField0_ &= -3;
                }
                List unused2 = result.mapKey_ = this.mapKey_;
                if ((this.bitField0_ & 4) == 4) {
                    this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                    this.bitField0_ &= -5;
                }
                List unused3 = result.mapValue_ = this.mapValue_;
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ = 0 | 1;
                }
                int unused4 = result.macroReference_ = this.macroReference_;
                if ((this.bitField0_ & 16) == 16) {
                    this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
                    this.bitField0_ &= -17;
                }
                List unused5 = result.templateToken_ = this.templateToken_;
                if ((from_bitField0_ & 32) == 32) {
                    to_bitField0_ |= 2;
                }
                int unused6 = result.macroNameReference_ = this.macroNameReference_;
                int unused7 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(ServingValue other) {
                if (other != ServingValue.getDefaultInstance()) {
                    if (!other.listItem_.isEmpty()) {
                        if (this.listItem_.isEmpty()) {
                            this.listItem_ = other.listItem_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureListItemIsMutable();
                            this.listItem_.addAll(other.listItem_);
                        }
                    }
                    if (!other.mapKey_.isEmpty()) {
                        if (this.mapKey_.isEmpty()) {
                            this.mapKey_ = other.mapKey_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureMapKeyIsMutable();
                            this.mapKey_.addAll(other.mapKey_);
                        }
                    }
                    if (!other.mapValue_.isEmpty()) {
                        if (this.mapValue_.isEmpty()) {
                            this.mapValue_ = other.mapValue_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureMapValueIsMutable();
                            this.mapValue_.addAll(other.mapValue_);
                        }
                    }
                    if (other.hasMacroReference()) {
                        setMacroReference(other.getMacroReference());
                    }
                    if (!other.templateToken_.isEmpty()) {
                        if (this.templateToken_.isEmpty()) {
                            this.templateToken_ = other.templateToken_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureTemplateTokenIsMutable();
                            this.templateToken_.addAll(other.templateToken_);
                        }
                    }
                    if (other.hasMacroNameReference()) {
                        setMacroNameReference(other.getMacroNameReference());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                return true;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    ServingValue parsedMessage = ServingValue.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    ServingValue parsedMessage2 = (ServingValue) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((ServingValue) null);
                    }
                    throw th;
                }
            }

            private void ensureListItemIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.listItem_ = new ArrayList(this.listItem_);
                    this.bitField0_ |= 1;
                }
            }

            public List<Integer> getListItemList() {
                return Collections.unmodifiableList(this.listItem_);
            }

            public int getListItemCount() {
                return this.listItem_.size();
            }

            public int getListItem(int index) {
                return this.listItem_.get(index).intValue();
            }

            public Builder setListItem(int index, int value) {
                ensureListItemIsMutable();
                this.listItem_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addListItem(int value) {
                ensureListItemIsMutable();
                this.listItem_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllListItem(Iterable<? extends Integer> values) {
                ensureListItemIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.listItem_);
                return this;
            }

            public Builder clearListItem() {
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            private void ensureMapKeyIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.mapKey_ = new ArrayList(this.mapKey_);
                    this.bitField0_ |= 2;
                }
            }

            public List<Integer> getMapKeyList() {
                return Collections.unmodifiableList(this.mapKey_);
            }

            public int getMapKeyCount() {
                return this.mapKey_.size();
            }

            public int getMapKey(int index) {
                return this.mapKey_.get(index).intValue();
            }

            public Builder setMapKey(int index, int value) {
                ensureMapKeyIsMutable();
                this.mapKey_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addMapKey(int value) {
                ensureMapKeyIsMutable();
                this.mapKey_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllMapKey(Iterable<? extends Integer> values) {
                ensureMapKeyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapKey_);
                return this;
            }

            public Builder clearMapKey() {
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            private void ensureMapValueIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.mapValue_ = new ArrayList(this.mapValue_);
                    this.bitField0_ |= 4;
                }
            }

            public List<Integer> getMapValueList() {
                return Collections.unmodifiableList(this.mapValue_);
            }

            public int getMapValueCount() {
                return this.mapValue_.size();
            }

            public int getMapValue(int index) {
                return this.mapValue_.get(index).intValue();
            }

            public Builder setMapValue(int index, int value) {
                ensureMapValueIsMutable();
                this.mapValue_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addMapValue(int value) {
                ensureMapValueIsMutable();
                this.mapValue_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllMapValue(Iterable<? extends Integer> values) {
                ensureMapValueIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapValue_);
                return this;
            }

            public Builder clearMapValue() {
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            public boolean hasMacroReference() {
                return (this.bitField0_ & 8) == 8;
            }

            public int getMacroReference() {
                return this.macroReference_;
            }

            public Builder setMacroReference(int value) {
                this.bitField0_ |= 8;
                this.macroReference_ = value;
                return this;
            }

            public Builder clearMacroReference() {
                this.bitField0_ &= -9;
                this.macroReference_ = 0;
                return this;
            }

            private void ensureTemplateTokenIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.templateToken_ = new ArrayList(this.templateToken_);
                    this.bitField0_ |= 16;
                }
            }

            public List<Integer> getTemplateTokenList() {
                return Collections.unmodifiableList(this.templateToken_);
            }

            public int getTemplateTokenCount() {
                return this.templateToken_.size();
            }

            public int getTemplateToken(int index) {
                return this.templateToken_.get(index).intValue();
            }

            public Builder setTemplateToken(int index, int value) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addTemplateToken(int value) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllTemplateToken(Iterable<? extends Integer> values) {
                ensureTemplateTokenIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.templateToken_);
                return this;
            }

            public Builder clearTemplateToken() {
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            public boolean hasMacroNameReference() {
                return (this.bitField0_ & 32) == 32;
            }

            public int getMacroNameReference() {
                return this.macroNameReference_;
            }

            public Builder setMacroNameReference(int value) {
                this.bitField0_ |= 32;
                this.macroNameReference_ = value;
                return this;
            }

            public Builder clearMacroNameReference() {
                this.bitField0_ &= -33;
                this.macroNameReference_ = 0;
                return this;
            }
        }
    }

    public static final class Property extends GeneratedMessageLite implements PropertyOrBuilder {
        public static final int KEY_FIELD_NUMBER = 1;
        public static Parser<Property> PARSER = new AbstractParser<Property>() {
            public Property parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Property(input, extensionRegistry);
            }
        };
        public static final int VALUE_FIELD_NUMBER = 2;
        private static final Property defaultInstance = new Property(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public int key_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;
        /* access modifiers changed from: private */
        public int value_;

        private Property(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Property(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Property getDefaultInstance() {
            return defaultInstance;
        }

        public Property getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Property(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                            this.key_ = input.readInt32();
                            break;
                        case 16:
                            this.bitField0_ |= 2;
                            this.value_ = input.readInt32();
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

        public Parser<Property> getParserForType() {
            return PARSER;
        }

        public boolean hasKey() {
            return (this.bitField0_ & 1) == 1;
        }

        public int getKey() {
            return this.key_;
        }

        public boolean hasValue() {
            return (this.bitField0_ & 2) == 2;
        }

        public int getValue() {
            return this.value_;
        }

        private void initFields() {
            this.key_ = 0;
            this.value_ = 0;
        }

        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                if (isInitialized == 1) {
                    return true;
                }
                return false;
            } else if (!hasKey()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasValue()) {
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
                output.writeInt32(1, this.key_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(2, this.value_);
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
                size2 = 0 + CodedOutputStream.computeInt32Size(1, this.key_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeInt32Size(2, this.value_);
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
            if (!(obj instanceof Property)) {
                return super.equals(obj);
            }
            Property other = (Property) obj;
            boolean result2 = 1 != 0 && hasKey() == other.hasKey();
            if (hasKey()) {
                result2 = result2 && getKey() == other.getKey();
            }
            if (!result2 || hasValue() != other.hasValue()) {
                result = false;
            } else {
                result = true;
            }
            if (hasValue()) {
                if (!result || getValue() != other.getValue()) {
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
            int hash = Property.class.hashCode() + 779;
            if (hasKey()) {
                hash = (((hash * 37) + 1) * 53) + getKey();
            }
            if (hasValue()) {
                hash = (((hash * 37) + 2) * 53) + getValue();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Property");
            }
            return mutableDefault;
        }

        public static Property parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Property parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Property parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Property parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Property parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Property parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Property parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Property parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Property parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Property parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Property prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Property, Builder> implements PropertyOrBuilder {
            private int bitField0_;
            private int key_;
            private int value_;

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
                this.key_ = 0;
                this.bitField0_ &= -2;
                this.value_ = 0;
                this.bitField0_ &= -3;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public Property getDefaultInstanceForType() {
                return Property.getDefaultInstance();
            }

            public Property build() {
                Property result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public Property buildPartial() {
                Property result = new Property((GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                int unused = result.key_ = this.key_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                int unused2 = result.value_ = this.value_;
                int unused3 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(Property other) {
                if (other != Property.getDefaultInstance()) {
                    if (other.hasKey()) {
                        setKey(other.getKey());
                    }
                    if (other.hasValue()) {
                        setValue(other.getValue());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasKey() && hasValue()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    Property parsedMessage = Property.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    Property parsedMessage2 = (Property) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((Property) null);
                    }
                    throw th;
                }
            }

            public boolean hasKey() {
                return (this.bitField0_ & 1) == 1;
            }

            public int getKey() {
                return this.key_;
            }

            public Builder setKey(int value) {
                this.bitField0_ |= 1;
                this.key_ = value;
                return this;
            }

            public Builder clearKey() {
                this.bitField0_ &= -2;
                this.key_ = 0;
                return this;
            }

            public boolean hasValue() {
                return (this.bitField0_ & 2) == 2;
            }

            public int getValue() {
                return this.value_;
            }

            public Builder setValue(int value) {
                this.bitField0_ |= 2;
                this.value_ = value;
                return this;
            }

            public Builder clearValue() {
                this.bitField0_ &= -3;
                this.value_ = 0;
                return this;
            }
        }
    }

    public static final class FunctionCall extends GeneratedMessageLite implements FunctionCallOrBuilder {
        public static final int FUNCTION_FIELD_NUMBER = 2;
        public static final int LIVE_ONLY_FIELD_NUMBER = 6;
        public static final int NAME_FIELD_NUMBER = 4;
        public static Parser<FunctionCall> PARSER = new AbstractParser<FunctionCall>() {
            public FunctionCall parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new FunctionCall(input, extensionRegistry);
            }
        };
        public static final int PROPERTY_FIELD_NUMBER = 3;
        public static final int SERVER_SIDE_FIELD_NUMBER = 1;
        private static final FunctionCall defaultInstance = new FunctionCall(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public int function_;
        /* access modifiers changed from: private */
        public boolean liveOnly_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public int name_;
        /* access modifiers changed from: private */
        public List<Integer> property_;
        /* access modifiers changed from: private */
        public boolean serverSide_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;

        private FunctionCall(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private FunctionCall(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static FunctionCall getDefaultInstance() {
            return defaultInstance;
        }

        public FunctionCall getDefaultInstanceForType() {
            return defaultInstance;
        }

        private FunctionCall(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int mutable_bitField0_ = 0;
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
                            this.bitField0_ |= 8;
                            this.serverSide_ = input.readBool();
                            break;
                        case 16:
                            this.bitField0_ |= 1;
                            this.function_ = input.readInt32();
                            break;
                        case 24:
                            if ((mutable_bitField0_ & 1) != 1) {
                                this.property_ = new ArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            this.property_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 26:
                            int limit = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 1) != 1 && input.getBytesUntilLimit() > 0) {
                                this.property_ = new ArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.property_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit);
                            break;
                        case 32:
                            this.bitField0_ |= 2;
                            this.name_ = input.readInt32();
                            break;
                        case DateTimeParserConstants.ANY /*48*/:
                            this.bitField0_ |= 4;
                            this.liveOnly_ = input.readBool();
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
                    if ((mutable_bitField0_ & 1) == 1) {
                        this.property_ = Collections.unmodifiableList(this.property_);
                    }
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
            if ((mutable_bitField0_ & 1) == 1) {
                this.property_ = Collections.unmodifiableList(this.property_);
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

        public Parser<FunctionCall> getParserForType() {
            return PARSER;
        }

        public List<Integer> getPropertyList() {
            return this.property_;
        }

        public int getPropertyCount() {
            return this.property_.size();
        }

        public int getProperty(int index) {
            return this.property_.get(index).intValue();
        }

        public boolean hasFunction() {
            return (this.bitField0_ & 1) == 1;
        }

        public int getFunction() {
            return this.function_;
        }

        public boolean hasName() {
            return (this.bitField0_ & 2) == 2;
        }

        public int getName() {
            return this.name_;
        }

        public boolean hasLiveOnly() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean getLiveOnly() {
            return this.liveOnly_;
        }

        public boolean hasServerSide() {
            return (this.bitField0_ & 8) == 8;
        }

        public boolean getServerSide() {
            return this.serverSide_;
        }

        private void initFields() {
            this.property_ = Collections.emptyList();
            this.function_ = 0;
            this.name_ = 0;
            this.liveOnly_ = false;
            this.serverSide_ = false;
        }

        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                if (isInitialized == 1) {
                    return true;
                }
                return false;
            } else if (!hasFunction()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 8) == 8) {
                output.writeBool(1, this.serverSide_);
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt32(2, this.function_);
            }
            for (int i = 0; i < this.property_.size(); i++) {
                output.writeInt32(3, this.property_.get(i).intValue());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(4, this.name_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeBool(6, this.liveOnly_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if ((this.bitField0_ & 8) == 8) {
                size2 = 0 + CodedOutputStream.computeBoolSize(1, this.serverSide_);
            }
            if ((this.bitField0_ & 1) == 1) {
                size2 += CodedOutputStream.computeInt32Size(2, this.function_);
            }
            int dataSize = 0;
            for (int i = 0; i < this.property_.size(); i++) {
                dataSize += CodedOutputStream.computeInt32SizeNoTag(this.property_.get(i).intValue());
            }
            int size3 = size2 + dataSize + (getPropertyList().size() * 1);
            if ((this.bitField0_ & 2) == 2) {
                size3 += CodedOutputStream.computeInt32Size(4, this.name_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size3 += CodedOutputStream.computeBoolSize(6, this.liveOnly_);
            }
            int size4 = size3 + this.unknownFields.size();
            this.memoizedSerializedSize = size4;
            return size4;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            boolean result;
            boolean result2;
            boolean result3;
            boolean result4;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof FunctionCall)) {
                return super.equals(obj);
            }
            FunctionCall other = (FunctionCall) obj;
            if (!(1 != 0 && getPropertyList().equals(other.getPropertyList())) || hasFunction() != other.hasFunction()) {
                result = false;
            } else {
                result = true;
            }
            if (hasFunction()) {
                if (!result || getFunction() != other.getFunction()) {
                    result = false;
                } else {
                    result = true;
                }
            }
            if (!result || hasName() != other.hasName()) {
                result2 = false;
            } else {
                result2 = true;
            }
            if (hasName()) {
                if (!result2 || getName() != other.getName()) {
                    result2 = false;
                } else {
                    result2 = true;
                }
            }
            if (!result2 || hasLiveOnly() != other.hasLiveOnly()) {
                result3 = false;
            } else {
                result3 = true;
            }
            if (hasLiveOnly()) {
                if (!result3 || getLiveOnly() != other.getLiveOnly()) {
                    result3 = false;
                } else {
                    result3 = true;
                }
            }
            if (!result3 || hasServerSide() != other.hasServerSide()) {
                result4 = false;
            } else {
                result4 = true;
            }
            if (hasServerSide()) {
                if (!result4 || getServerSide() != other.getServerSide()) {
                    result4 = false;
                } else {
                    result4 = true;
                }
            }
            return result4;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = FunctionCall.class.hashCode() + 779;
            if (getPropertyCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getPropertyList().hashCode();
            }
            if (hasFunction()) {
                hash = (((hash * 37) + 2) * 53) + getFunction();
            }
            if (hasName()) {
                hash = (((hash * 37) + 4) * 53) + getName();
            }
            if (hasLiveOnly()) {
                hash = (((hash * 37) + 6) * 53) + Internal.hashBoolean(getLiveOnly());
            }
            if (hasServerSide()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashBoolean(getServerSide());
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$FunctionCall");
            }
            return mutableDefault;
        }

        public static FunctionCall parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static FunctionCall parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static FunctionCall parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static FunctionCall parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static FunctionCall parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static FunctionCall parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static FunctionCall parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static FunctionCall parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static FunctionCall parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static FunctionCall parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(FunctionCall prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<FunctionCall, Builder> implements FunctionCallOrBuilder {
            private int bitField0_;
            private int function_;
            private boolean liveOnly_;
            private int name_;
            private List<Integer> property_ = Collections.emptyList();
            private boolean serverSide_;

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
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.function_ = 0;
                this.bitField0_ &= -3;
                this.name_ = 0;
                this.bitField0_ &= -5;
                this.liveOnly_ = false;
                this.bitField0_ &= -9;
                this.serverSide_ = false;
                this.bitField0_ &= -17;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public FunctionCall getDefaultInstanceForType() {
                return FunctionCall.getDefaultInstance();
            }

            public FunctionCall build() {
                FunctionCall result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public FunctionCall buildPartial() {
                FunctionCall result = new FunctionCall((GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.property_ = Collections.unmodifiableList(this.property_);
                    this.bitField0_ &= -2;
                }
                List unused = result.property_ = this.property_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ = 0 | 1;
                }
                int unused2 = result.function_ = this.function_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 2;
                }
                int unused3 = result.name_ = this.name_;
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ |= 4;
                }
                boolean unused4 = result.liveOnly_ = this.liveOnly_;
                if ((from_bitField0_ & 16) == 16) {
                    to_bitField0_ |= 8;
                }
                boolean unused5 = result.serverSide_ = this.serverSide_;
                int unused6 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(FunctionCall other) {
                if (other != FunctionCall.getDefaultInstance()) {
                    if (!other.property_.isEmpty()) {
                        if (this.property_.isEmpty()) {
                            this.property_ = other.property_;
                            this.bitField0_ &= -2;
                        } else {
                            ensurePropertyIsMutable();
                            this.property_.addAll(other.property_);
                        }
                    }
                    if (other.hasFunction()) {
                        setFunction(other.getFunction());
                    }
                    if (other.hasName()) {
                        setName(other.getName());
                    }
                    if (other.hasLiveOnly()) {
                        setLiveOnly(other.getLiveOnly());
                    }
                    if (other.hasServerSide()) {
                        setServerSide(other.getServerSide());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                if (!hasFunction()) {
                    return false;
                }
                return true;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    FunctionCall parsedMessage = FunctionCall.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    FunctionCall parsedMessage2 = (FunctionCall) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((FunctionCall) null);
                    }
                    throw th;
                }
            }

            private void ensurePropertyIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.property_ = new ArrayList(this.property_);
                    this.bitField0_ |= 1;
                }
            }

            public List<Integer> getPropertyList() {
                return Collections.unmodifiableList(this.property_);
            }

            public int getPropertyCount() {
                return this.property_.size();
            }

            public int getProperty(int index) {
                return this.property_.get(index).intValue();
            }

            public Builder setProperty(int index, int value) {
                ensurePropertyIsMutable();
                this.property_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addProperty(int value) {
                ensurePropertyIsMutable();
                this.property_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllProperty(Iterable<? extends Integer> values) {
                ensurePropertyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.property_);
                return this;
            }

            public Builder clearProperty() {
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public boolean hasFunction() {
                return (this.bitField0_ & 2) == 2;
            }

            public int getFunction() {
                return this.function_;
            }

            public Builder setFunction(int value) {
                this.bitField0_ |= 2;
                this.function_ = value;
                return this;
            }

            public Builder clearFunction() {
                this.bitField0_ &= -3;
                this.function_ = 0;
                return this;
            }

            public boolean hasName() {
                return (this.bitField0_ & 4) == 4;
            }

            public int getName() {
                return this.name_;
            }

            public Builder setName(int value) {
                this.bitField0_ |= 4;
                this.name_ = value;
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -5;
                this.name_ = 0;
                return this;
            }

            public boolean hasLiveOnly() {
                return (this.bitField0_ & 8) == 8;
            }

            public boolean getLiveOnly() {
                return this.liveOnly_;
            }

            public Builder setLiveOnly(boolean value) {
                this.bitField0_ |= 8;
                this.liveOnly_ = value;
                return this;
            }

            public Builder clearLiveOnly() {
                this.bitField0_ &= -9;
                this.liveOnly_ = false;
                return this;
            }

            public boolean hasServerSide() {
                return (this.bitField0_ & 16) == 16;
            }

            public boolean getServerSide() {
                return this.serverSide_;
            }

            public Builder setServerSide(boolean value) {
                this.bitField0_ |= 16;
                this.serverSide_ = value;
                return this;
            }

            public Builder clearServerSide() {
                this.bitField0_ &= -17;
                this.serverSide_ = false;
                return this;
            }
        }
    }

    public static final class Rule extends GeneratedMessageLite implements RuleOrBuilder {
        public static final int ADD_MACRO_FIELD_NUMBER = 7;
        public static final int ADD_MACRO_RULE_NAME_FIELD_NUMBER = 9;
        public static final int ADD_TAG_FIELD_NUMBER = 3;
        public static final int ADD_TAG_RULE_NAME_FIELD_NUMBER = 5;
        public static final int NEGATIVE_PREDICATE_FIELD_NUMBER = 2;
        public static Parser<Rule> PARSER = new AbstractParser<Rule>() {
            public Rule parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Rule(input, extensionRegistry);
            }
        };
        public static final int POSITIVE_PREDICATE_FIELD_NUMBER = 1;
        public static final int REMOVE_MACRO_FIELD_NUMBER = 8;
        public static final int REMOVE_MACRO_RULE_NAME_FIELD_NUMBER = 10;
        public static final int REMOVE_TAG_FIELD_NUMBER = 4;
        public static final int REMOVE_TAG_RULE_NAME_FIELD_NUMBER = 6;
        private static final Rule defaultInstance = new Rule(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public List<Integer> addMacroRuleName_;
        /* access modifiers changed from: private */
        public List<Integer> addMacro_;
        /* access modifiers changed from: private */
        public List<Integer> addTagRuleName_;
        /* access modifiers changed from: private */
        public List<Integer> addTag_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public List<Integer> negativePredicate_;
        /* access modifiers changed from: private */
        public List<Integer> positivePredicate_;
        /* access modifiers changed from: private */
        public List<Integer> removeMacroRuleName_;
        /* access modifiers changed from: private */
        public List<Integer> removeMacro_;
        /* access modifiers changed from: private */
        public List<Integer> removeTagRuleName_;
        /* access modifiers changed from: private */
        public List<Integer> removeTag_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;

        private Rule(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Rule(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Rule getDefaultInstance() {
            return defaultInstance;
        }

        public Rule getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Rule(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int mutable_bitField0_ = 0;
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
                            if ((mutable_bitField0_ & 1) != 1) {
                                this.positivePredicate_ = new ArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            this.positivePredicate_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 10:
                            int limit = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 1) != 1 && input.getBytesUntilLimit() > 0) {
                                this.positivePredicate_ = new ArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.positivePredicate_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit);
                            break;
                        case 16:
                            if ((mutable_bitField0_ & 2) != 2) {
                                this.negativePredicate_ = new ArrayList();
                                mutable_bitField0_ |= 2;
                            }
                            this.negativePredicate_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 18:
                            int limit2 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 2) != 2 && input.getBytesUntilLimit() > 0) {
                                this.negativePredicate_ = new ArrayList();
                                mutable_bitField0_ |= 2;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.negativePredicate_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit2);
                            break;
                        case 24:
                            if ((mutable_bitField0_ & 4) != 4) {
                                this.addTag_ = new ArrayList();
                                mutable_bitField0_ |= 4;
                            }
                            this.addTag_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 26:
                            int limit3 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 4) != 4 && input.getBytesUntilLimit() > 0) {
                                this.addTag_ = new ArrayList();
                                mutable_bitField0_ |= 4;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.addTag_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit3);
                            break;
                        case 32:
                            if ((mutable_bitField0_ & 8) != 8) {
                                this.removeTag_ = new ArrayList();
                                mutable_bitField0_ |= 8;
                            }
                            this.removeTag_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 34:
                            int limit4 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 8) != 8 && input.getBytesUntilLimit() > 0) {
                                this.removeTag_ = new ArrayList();
                                mutable_bitField0_ |= 8;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.removeTag_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit4);
                            break;
                        case 40:
                            if ((mutable_bitField0_ & 16) != 16) {
                                this.addTagRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 16;
                            }
                            this.addTagRuleName_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 42:
                            int limit5 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 16) != 16 && input.getBytesUntilLimit() > 0) {
                                this.addTagRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 16;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.addTagRuleName_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit5);
                            break;
                        case DateTimeParserConstants.ANY /*48*/:
                            if ((mutable_bitField0_ & 32) != 32) {
                                this.removeTagRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 32;
                            }
                            this.removeTagRuleName_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 50:
                            int limit6 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 32) != 32 && input.getBytesUntilLimit() > 0) {
                                this.removeTagRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 32;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.removeTagRuleName_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit6);
                            break;
                        case 56:
                            if ((mutable_bitField0_ & 64) != 64) {
                                this.addMacro_ = new ArrayList();
                                mutable_bitField0_ |= 64;
                            }
                            this.addMacro_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 58:
                            int limit7 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 64) != 64 && input.getBytesUntilLimit() > 0) {
                                this.addMacro_ = new ArrayList();
                                mutable_bitField0_ |= 64;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.addMacro_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit7);
                            break;
                        case AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS:
                            if ((mutable_bitField0_ & 128) != 128) {
                                this.removeMacro_ = new ArrayList();
                                mutable_bitField0_ |= 128;
                            }
                            this.removeMacro_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 66:
                            int limit8 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 128) != 128 && input.getBytesUntilLimit() > 0) {
                                this.removeMacro_ = new ArrayList();
                                mutable_bitField0_ |= 128;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.removeMacro_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit8);
                            break;
                        case 72:
                            if ((mutable_bitField0_ & 256) != 256) {
                                this.addMacroRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 256;
                            }
                            this.addMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 74:
                            int limit9 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 256) != 256 && input.getBytesUntilLimit() > 0) {
                                this.addMacroRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 256;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.addMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit9);
                            break;
                        case 80:
                            if ((mutable_bitField0_ & 512) != 512) {
                                this.removeMacroRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 512;
                            }
                            this.removeMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                            break;
                        case 82:
                            int limit10 = input.pushLimit(input.readRawVarint32());
                            if ((mutable_bitField0_ & 512) != 512 && input.getBytesUntilLimit() > 0) {
                                this.removeMacroRuleName_ = new ArrayList();
                                mutable_bitField0_ |= 512;
                            }
                            while (input.getBytesUntilLimit() > 0) {
                                this.removeMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                            }
                            input.popLimit(limit10);
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
                    if ((mutable_bitField0_ & 1) == 1) {
                        this.positivePredicate_ = Collections.unmodifiableList(this.positivePredicate_);
                    }
                    if ((mutable_bitField0_ & 2) == 2) {
                        this.negativePredicate_ = Collections.unmodifiableList(this.negativePredicate_);
                    }
                    if ((mutable_bitField0_ & 4) == 4) {
                        this.addTag_ = Collections.unmodifiableList(this.addTag_);
                    }
                    if ((mutable_bitField0_ & 8) == 8) {
                        this.removeTag_ = Collections.unmodifiableList(this.removeTag_);
                    }
                    if ((mutable_bitField0_ & 16) == 16) {
                        this.addTagRuleName_ = Collections.unmodifiableList(this.addTagRuleName_);
                    }
                    if ((mutable_bitField0_ & 32) == 32) {
                        this.removeTagRuleName_ = Collections.unmodifiableList(this.removeTagRuleName_);
                    }
                    if ((mutable_bitField0_ & 64) == 64) {
                        this.addMacro_ = Collections.unmodifiableList(this.addMacro_);
                    }
                    if ((mutable_bitField0_ & 128) == 128) {
                        this.removeMacro_ = Collections.unmodifiableList(this.removeMacro_);
                    }
                    if ((mutable_bitField0_ & 256) == 256) {
                        this.addMacroRuleName_ = Collections.unmodifiableList(this.addMacroRuleName_);
                    }
                    if ((mutable_bitField0_ & 512) == 512) {
                        this.removeMacroRuleName_ = Collections.unmodifiableList(this.removeMacroRuleName_);
                    }
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
            if ((mutable_bitField0_ & 1) == 1) {
                this.positivePredicate_ = Collections.unmodifiableList(this.positivePredicate_);
            }
            if ((mutable_bitField0_ & 2) == 2) {
                this.negativePredicate_ = Collections.unmodifiableList(this.negativePredicate_);
            }
            if ((mutable_bitField0_ & 4) == 4) {
                this.addTag_ = Collections.unmodifiableList(this.addTag_);
            }
            if ((mutable_bitField0_ & 8) == 8) {
                this.removeTag_ = Collections.unmodifiableList(this.removeTag_);
            }
            if ((mutable_bitField0_ & 16) == 16) {
                this.addTagRuleName_ = Collections.unmodifiableList(this.addTagRuleName_);
            }
            if ((mutable_bitField0_ & 32) == 32) {
                this.removeTagRuleName_ = Collections.unmodifiableList(this.removeTagRuleName_);
            }
            if ((mutable_bitField0_ & 64) == 64) {
                this.addMacro_ = Collections.unmodifiableList(this.addMacro_);
            }
            if ((mutable_bitField0_ & 128) == 128) {
                this.removeMacro_ = Collections.unmodifiableList(this.removeMacro_);
            }
            if ((mutable_bitField0_ & 256) == 256) {
                this.addMacroRuleName_ = Collections.unmodifiableList(this.addMacroRuleName_);
            }
            if ((mutable_bitField0_ & 512) == 512) {
                this.removeMacroRuleName_ = Collections.unmodifiableList(this.removeMacroRuleName_);
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

        public Parser<Rule> getParserForType() {
            return PARSER;
        }

        public List<Integer> getPositivePredicateList() {
            return this.positivePredicate_;
        }

        public int getPositivePredicateCount() {
            return this.positivePredicate_.size();
        }

        public int getPositivePredicate(int index) {
            return this.positivePredicate_.get(index).intValue();
        }

        public List<Integer> getNegativePredicateList() {
            return this.negativePredicate_;
        }

        public int getNegativePredicateCount() {
            return this.negativePredicate_.size();
        }

        public int getNegativePredicate(int index) {
            return this.negativePredicate_.get(index).intValue();
        }

        public List<Integer> getAddTagList() {
            return this.addTag_;
        }

        public int getAddTagCount() {
            return this.addTag_.size();
        }

        public int getAddTag(int index) {
            return this.addTag_.get(index).intValue();
        }

        public List<Integer> getRemoveTagList() {
            return this.removeTag_;
        }

        public int getRemoveTagCount() {
            return this.removeTag_.size();
        }

        public int getRemoveTag(int index) {
            return this.removeTag_.get(index).intValue();
        }

        public List<Integer> getAddTagRuleNameList() {
            return this.addTagRuleName_;
        }

        public int getAddTagRuleNameCount() {
            return this.addTagRuleName_.size();
        }

        public int getAddTagRuleName(int index) {
            return this.addTagRuleName_.get(index).intValue();
        }

        public List<Integer> getRemoveTagRuleNameList() {
            return this.removeTagRuleName_;
        }

        public int getRemoveTagRuleNameCount() {
            return this.removeTagRuleName_.size();
        }

        public int getRemoveTagRuleName(int index) {
            return this.removeTagRuleName_.get(index).intValue();
        }

        public List<Integer> getAddMacroList() {
            return this.addMacro_;
        }

        public int getAddMacroCount() {
            return this.addMacro_.size();
        }

        public int getAddMacro(int index) {
            return this.addMacro_.get(index).intValue();
        }

        public List<Integer> getRemoveMacroList() {
            return this.removeMacro_;
        }

        public int getRemoveMacroCount() {
            return this.removeMacro_.size();
        }

        public int getRemoveMacro(int index) {
            return this.removeMacro_.get(index).intValue();
        }

        public List<Integer> getAddMacroRuleNameList() {
            return this.addMacroRuleName_;
        }

        public int getAddMacroRuleNameCount() {
            return this.addMacroRuleName_.size();
        }

        public int getAddMacroRuleName(int index) {
            return this.addMacroRuleName_.get(index).intValue();
        }

        public List<Integer> getRemoveMacroRuleNameList() {
            return this.removeMacroRuleName_;
        }

        public int getRemoveMacroRuleNameCount() {
            return this.removeMacroRuleName_.size();
        }

        public int getRemoveMacroRuleName(int index) {
            return this.removeMacroRuleName_.get(index).intValue();
        }

        private void initFields() {
            this.positivePredicate_ = Collections.emptyList();
            this.negativePredicate_ = Collections.emptyList();
            this.addTag_ = Collections.emptyList();
            this.removeTag_ = Collections.emptyList();
            this.addTagRuleName_ = Collections.emptyList();
            this.removeTagRuleName_ = Collections.emptyList();
            this.addMacro_ = Collections.emptyList();
            this.removeMacro_ = Collections.emptyList();
            this.addMacroRuleName_ = Collections.emptyList();
            this.removeMacroRuleName_ = Collections.emptyList();
        }

        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == -1) {
                this.memoizedIsInitialized = 1;
                return true;
            } else if (isInitialized == 1) {
                return true;
            } else {
                return false;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.positivePredicate_.size(); i++) {
                output.writeInt32(1, this.positivePredicate_.get(i).intValue());
            }
            for (int i2 = 0; i2 < this.negativePredicate_.size(); i2++) {
                output.writeInt32(2, this.negativePredicate_.get(i2).intValue());
            }
            for (int i3 = 0; i3 < this.addTag_.size(); i3++) {
                output.writeInt32(3, this.addTag_.get(i3).intValue());
            }
            for (int i4 = 0; i4 < this.removeTag_.size(); i4++) {
                output.writeInt32(4, this.removeTag_.get(i4).intValue());
            }
            for (int i5 = 0; i5 < this.addTagRuleName_.size(); i5++) {
                output.writeInt32(5, this.addTagRuleName_.get(i5).intValue());
            }
            for (int i6 = 0; i6 < this.removeTagRuleName_.size(); i6++) {
                output.writeInt32(6, this.removeTagRuleName_.get(i6).intValue());
            }
            for (int i7 = 0; i7 < this.addMacro_.size(); i7++) {
                output.writeInt32(7, this.addMacro_.get(i7).intValue());
            }
            for (int i8 = 0; i8 < this.removeMacro_.size(); i8++) {
                output.writeInt32(8, this.removeMacro_.get(i8).intValue());
            }
            for (int i9 = 0; i9 < this.addMacroRuleName_.size(); i9++) {
                output.writeInt32(9, this.addMacroRuleName_.get(i9).intValue());
            }
            for (int i10 = 0; i10 < this.removeMacroRuleName_.size(); i10++) {
                output.writeInt32(10, this.removeMacroRuleName_.get(i10).intValue());
            }
            output.writeRawBytes(this.unknownFields);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int dataSize = 0;
            for (int i = 0; i < this.positivePredicate_.size(); i++) {
                dataSize += CodedOutputStream.computeInt32SizeNoTag(this.positivePredicate_.get(i).intValue());
            }
            int size2 = 0 + dataSize + (getPositivePredicateList().size() * 1);
            int dataSize2 = 0;
            for (int i2 = 0; i2 < this.negativePredicate_.size(); i2++) {
                dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.negativePredicate_.get(i2).intValue());
            }
            int size3 = size2 + dataSize2 + (getNegativePredicateList().size() * 1);
            int dataSize3 = 0;
            for (int i3 = 0; i3 < this.addTag_.size(); i3++) {
                dataSize3 += CodedOutputStream.computeInt32SizeNoTag(this.addTag_.get(i3).intValue());
            }
            int size4 = size3 + dataSize3 + (getAddTagList().size() * 1);
            int dataSize4 = 0;
            for (int i4 = 0; i4 < this.removeTag_.size(); i4++) {
                dataSize4 += CodedOutputStream.computeInt32SizeNoTag(this.removeTag_.get(i4).intValue());
            }
            int size5 = size4 + dataSize4 + (getRemoveTagList().size() * 1);
            int dataSize5 = 0;
            for (int i5 = 0; i5 < this.addTagRuleName_.size(); i5++) {
                dataSize5 += CodedOutputStream.computeInt32SizeNoTag(this.addTagRuleName_.get(i5).intValue());
            }
            int size6 = size5 + dataSize5 + (getAddTagRuleNameList().size() * 1);
            int dataSize6 = 0;
            for (int i6 = 0; i6 < this.removeTagRuleName_.size(); i6++) {
                dataSize6 += CodedOutputStream.computeInt32SizeNoTag(this.removeTagRuleName_.get(i6).intValue());
            }
            int size7 = size6 + dataSize6 + (getRemoveTagRuleNameList().size() * 1);
            int dataSize7 = 0;
            for (int i7 = 0; i7 < this.addMacro_.size(); i7++) {
                dataSize7 += CodedOutputStream.computeInt32SizeNoTag(this.addMacro_.get(i7).intValue());
            }
            int size8 = size7 + dataSize7 + (getAddMacroList().size() * 1);
            int dataSize8 = 0;
            for (int i8 = 0; i8 < this.removeMacro_.size(); i8++) {
                dataSize8 += CodedOutputStream.computeInt32SizeNoTag(this.removeMacro_.get(i8).intValue());
            }
            int size9 = size8 + dataSize8 + (getRemoveMacroList().size() * 1);
            int dataSize9 = 0;
            for (int i9 = 0; i9 < this.addMacroRuleName_.size(); i9++) {
                dataSize9 += CodedOutputStream.computeInt32SizeNoTag(this.addMacroRuleName_.get(i9).intValue());
            }
            int size10 = size9 + dataSize9 + (getAddMacroRuleNameList().size() * 1);
            int dataSize10 = 0;
            for (int i10 = 0; i10 < this.removeMacroRuleName_.size(); i10++) {
                dataSize10 += CodedOutputStream.computeInt32SizeNoTag(this.removeMacroRuleName_.get(i10).intValue());
            }
            int size11 = size10 + dataSize10 + (getRemoveMacroRuleNameList().size() * 1) + this.unknownFields.size();
            this.memoizedSerializedSize = size11;
            return size11;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            boolean result;
            boolean result2;
            boolean result3;
            boolean result4;
            boolean result5;
            boolean result6;
            boolean result7;
            boolean result8;
            boolean result9;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Rule)) {
                return super.equals(obj);
            }
            Rule other = (Rule) obj;
            if (!(1 != 0 && getPositivePredicateList().equals(other.getPositivePredicateList())) || !getNegativePredicateList().equals(other.getNegativePredicateList())) {
                result = false;
            } else {
                result = true;
            }
            if (!result || !getAddTagList().equals(other.getAddTagList())) {
                result2 = false;
            } else {
                result2 = true;
            }
            if (!result2 || !getRemoveTagList().equals(other.getRemoveTagList())) {
                result3 = false;
            } else {
                result3 = true;
            }
            if (!result3 || !getAddTagRuleNameList().equals(other.getAddTagRuleNameList())) {
                result4 = false;
            } else {
                result4 = true;
            }
            if (!result4 || !getRemoveTagRuleNameList().equals(other.getRemoveTagRuleNameList())) {
                result5 = false;
            } else {
                result5 = true;
            }
            if (!result5 || !getAddMacroList().equals(other.getAddMacroList())) {
                result6 = false;
            } else {
                result6 = true;
            }
            if (!result6 || !getRemoveMacroList().equals(other.getRemoveMacroList())) {
                result7 = false;
            } else {
                result7 = true;
            }
            if (!result7 || !getAddMacroRuleNameList().equals(other.getAddMacroRuleNameList())) {
                result8 = false;
            } else {
                result8 = true;
            }
            if (!result8 || !getRemoveMacroRuleNameList().equals(other.getRemoveMacroRuleNameList())) {
                result9 = false;
            } else {
                result9 = true;
            }
            return result9;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Rule.class.hashCode() + 779;
            if (getPositivePredicateCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getPositivePredicateList().hashCode();
            }
            if (getNegativePredicateCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getNegativePredicateList().hashCode();
            }
            if (getAddTagCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getAddTagList().hashCode();
            }
            if (getRemoveTagCount() > 0) {
                hash = (((hash * 37) + 4) * 53) + getRemoveTagList().hashCode();
            }
            if (getAddTagRuleNameCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getAddTagRuleNameList().hashCode();
            }
            if (getRemoveTagRuleNameCount() > 0) {
                hash = (((hash * 37) + 6) * 53) + getRemoveTagRuleNameList().hashCode();
            }
            if (getAddMacroCount() > 0) {
                hash = (((hash * 37) + 7) * 53) + getAddMacroList().hashCode();
            }
            if (getRemoveMacroCount() > 0) {
                hash = (((hash * 37) + 8) * 53) + getRemoveMacroList().hashCode();
            }
            if (getAddMacroRuleNameCount() > 0) {
                hash = (((hash * 37) + 9) * 53) + getAddMacroRuleNameList().hashCode();
            }
            if (getRemoveMacroRuleNameCount() > 0) {
                hash = (((hash * 37) + 10) * 53) + getRemoveMacroRuleNameList().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Rule");
            }
            return mutableDefault;
        }

        public static Rule parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Rule parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Rule parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Rule parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Rule parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Rule parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Rule parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Rule parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Rule parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Rule parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Rule prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Rule, Builder> implements RuleOrBuilder {
            private List<Integer> addMacroRuleName_ = Collections.emptyList();
            private List<Integer> addMacro_ = Collections.emptyList();
            private List<Integer> addTagRuleName_ = Collections.emptyList();
            private List<Integer> addTag_ = Collections.emptyList();
            private int bitField0_;
            private List<Integer> negativePredicate_ = Collections.emptyList();
            private List<Integer> positivePredicate_ = Collections.emptyList();
            private List<Integer> removeMacroRuleName_ = Collections.emptyList();
            private List<Integer> removeMacro_ = Collections.emptyList();
            private List<Integer> removeTagRuleName_ = Collections.emptyList();
            private List<Integer> removeTag_ = Collections.emptyList();

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
                this.positivePredicate_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.negativePredicate_ = Collections.emptyList();
                this.bitField0_ &= -3;
                this.addTag_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.removeTag_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.addTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.removeTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -33;
                this.addMacro_ = Collections.emptyList();
                this.bitField0_ &= -65;
                this.removeMacro_ = Collections.emptyList();
                this.bitField0_ &= -129;
                this.addMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -257;
                this.removeMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -513;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public Rule getDefaultInstanceForType() {
                return Rule.getDefaultInstance();
            }

            public Rule build() {
                Rule result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public Rule buildPartial() {
                Rule result = new Rule((GeneratedMessageLite.Builder) this);
                int i = this.bitField0_;
                if ((this.bitField0_ & 1) == 1) {
                    this.positivePredicate_ = Collections.unmodifiableList(this.positivePredicate_);
                    this.bitField0_ &= -2;
                }
                List unused = result.positivePredicate_ = this.positivePredicate_;
                if ((this.bitField0_ & 2) == 2) {
                    this.negativePredicate_ = Collections.unmodifiableList(this.negativePredicate_);
                    this.bitField0_ &= -3;
                }
                List unused2 = result.negativePredicate_ = this.negativePredicate_;
                if ((this.bitField0_ & 4) == 4) {
                    this.addTag_ = Collections.unmodifiableList(this.addTag_);
                    this.bitField0_ &= -5;
                }
                List unused3 = result.addTag_ = this.addTag_;
                if ((this.bitField0_ & 8) == 8) {
                    this.removeTag_ = Collections.unmodifiableList(this.removeTag_);
                    this.bitField0_ &= -9;
                }
                List unused4 = result.removeTag_ = this.removeTag_;
                if ((this.bitField0_ & 16) == 16) {
                    this.addTagRuleName_ = Collections.unmodifiableList(this.addTagRuleName_);
                    this.bitField0_ &= -17;
                }
                List unused5 = result.addTagRuleName_ = this.addTagRuleName_;
                if ((this.bitField0_ & 32) == 32) {
                    this.removeTagRuleName_ = Collections.unmodifiableList(this.removeTagRuleName_);
                    this.bitField0_ &= -33;
                }
                List unused6 = result.removeTagRuleName_ = this.removeTagRuleName_;
                if ((this.bitField0_ & 64) == 64) {
                    this.addMacro_ = Collections.unmodifiableList(this.addMacro_);
                    this.bitField0_ &= -65;
                }
                List unused7 = result.addMacro_ = this.addMacro_;
                if ((this.bitField0_ & 128) == 128) {
                    this.removeMacro_ = Collections.unmodifiableList(this.removeMacro_);
                    this.bitField0_ &= -129;
                }
                List unused8 = result.removeMacro_ = this.removeMacro_;
                if ((this.bitField0_ & 256) == 256) {
                    this.addMacroRuleName_ = Collections.unmodifiableList(this.addMacroRuleName_);
                    this.bitField0_ &= -257;
                }
                List unused9 = result.addMacroRuleName_ = this.addMacroRuleName_;
                if ((this.bitField0_ & 512) == 512) {
                    this.removeMacroRuleName_ = Collections.unmodifiableList(this.removeMacroRuleName_);
                    this.bitField0_ &= -513;
                }
                List unused10 = result.removeMacroRuleName_ = this.removeMacroRuleName_;
                return result;
            }

            public Builder mergeFrom(Rule other) {
                if (other != Rule.getDefaultInstance()) {
                    if (!other.positivePredicate_.isEmpty()) {
                        if (this.positivePredicate_.isEmpty()) {
                            this.positivePredicate_ = other.positivePredicate_;
                            this.bitField0_ &= -2;
                        } else {
                            ensurePositivePredicateIsMutable();
                            this.positivePredicate_.addAll(other.positivePredicate_);
                        }
                    }
                    if (!other.negativePredicate_.isEmpty()) {
                        if (this.negativePredicate_.isEmpty()) {
                            this.negativePredicate_ = other.negativePredicate_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureNegativePredicateIsMutable();
                            this.negativePredicate_.addAll(other.negativePredicate_);
                        }
                    }
                    if (!other.addTag_.isEmpty()) {
                        if (this.addTag_.isEmpty()) {
                            this.addTag_ = other.addTag_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureAddTagIsMutable();
                            this.addTag_.addAll(other.addTag_);
                        }
                    }
                    if (!other.removeTag_.isEmpty()) {
                        if (this.removeTag_.isEmpty()) {
                            this.removeTag_ = other.removeTag_;
                            this.bitField0_ &= -9;
                        } else {
                            ensureRemoveTagIsMutable();
                            this.removeTag_.addAll(other.removeTag_);
                        }
                    }
                    if (!other.addTagRuleName_.isEmpty()) {
                        if (this.addTagRuleName_.isEmpty()) {
                            this.addTagRuleName_ = other.addTagRuleName_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureAddTagRuleNameIsMutable();
                            this.addTagRuleName_.addAll(other.addTagRuleName_);
                        }
                    }
                    if (!other.removeTagRuleName_.isEmpty()) {
                        if (this.removeTagRuleName_.isEmpty()) {
                            this.removeTagRuleName_ = other.removeTagRuleName_;
                            this.bitField0_ &= -33;
                        } else {
                            ensureRemoveTagRuleNameIsMutable();
                            this.removeTagRuleName_.addAll(other.removeTagRuleName_);
                        }
                    }
                    if (!other.addMacro_.isEmpty()) {
                        if (this.addMacro_.isEmpty()) {
                            this.addMacro_ = other.addMacro_;
                            this.bitField0_ &= -65;
                        } else {
                            ensureAddMacroIsMutable();
                            this.addMacro_.addAll(other.addMacro_);
                        }
                    }
                    if (!other.removeMacro_.isEmpty()) {
                        if (this.removeMacro_.isEmpty()) {
                            this.removeMacro_ = other.removeMacro_;
                            this.bitField0_ &= -129;
                        } else {
                            ensureRemoveMacroIsMutable();
                            this.removeMacro_.addAll(other.removeMacro_);
                        }
                    }
                    if (!other.addMacroRuleName_.isEmpty()) {
                        if (this.addMacroRuleName_.isEmpty()) {
                            this.addMacroRuleName_ = other.addMacroRuleName_;
                            this.bitField0_ &= -257;
                        } else {
                            ensureAddMacroRuleNameIsMutable();
                            this.addMacroRuleName_.addAll(other.addMacroRuleName_);
                        }
                    }
                    if (!other.removeMacroRuleName_.isEmpty()) {
                        if (this.removeMacroRuleName_.isEmpty()) {
                            this.removeMacroRuleName_ = other.removeMacroRuleName_;
                            this.bitField0_ &= -513;
                        } else {
                            ensureRemoveMacroRuleNameIsMutable();
                            this.removeMacroRuleName_.addAll(other.removeMacroRuleName_);
                        }
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                return true;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    Rule parsedMessage = Rule.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    Rule parsedMessage2 = (Rule) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((Rule) null);
                    }
                    throw th;
                }
            }

            private void ensurePositivePredicateIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.positivePredicate_ = new ArrayList(this.positivePredicate_);
                    this.bitField0_ |= 1;
                }
            }

            public List<Integer> getPositivePredicateList() {
                return Collections.unmodifiableList(this.positivePredicate_);
            }

            public int getPositivePredicateCount() {
                return this.positivePredicate_.size();
            }

            public int getPositivePredicate(int index) {
                return this.positivePredicate_.get(index).intValue();
            }

            public Builder setPositivePredicate(int index, int value) {
                ensurePositivePredicateIsMutable();
                this.positivePredicate_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addPositivePredicate(int value) {
                ensurePositivePredicateIsMutable();
                this.positivePredicate_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllPositivePredicate(Iterable<? extends Integer> values) {
                ensurePositivePredicateIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.positivePredicate_);
                return this;
            }

            public Builder clearPositivePredicate() {
                this.positivePredicate_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            private void ensureNegativePredicateIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.negativePredicate_ = new ArrayList(this.negativePredicate_);
                    this.bitField0_ |= 2;
                }
            }

            public List<Integer> getNegativePredicateList() {
                return Collections.unmodifiableList(this.negativePredicate_);
            }

            public int getNegativePredicateCount() {
                return this.negativePredicate_.size();
            }

            public int getNegativePredicate(int index) {
                return this.negativePredicate_.get(index).intValue();
            }

            public Builder setNegativePredicate(int index, int value) {
                ensureNegativePredicateIsMutable();
                this.negativePredicate_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addNegativePredicate(int value) {
                ensureNegativePredicateIsMutable();
                this.negativePredicate_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllNegativePredicate(Iterable<? extends Integer> values) {
                ensureNegativePredicateIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.negativePredicate_);
                return this;
            }

            public Builder clearNegativePredicate() {
                this.negativePredicate_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            private void ensureAddTagIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.addTag_ = new ArrayList(this.addTag_);
                    this.bitField0_ |= 4;
                }
            }

            public List<Integer> getAddTagList() {
                return Collections.unmodifiableList(this.addTag_);
            }

            public int getAddTagCount() {
                return this.addTag_.size();
            }

            public int getAddTag(int index) {
                return this.addTag_.get(index).intValue();
            }

            public Builder setAddTag(int index, int value) {
                ensureAddTagIsMutable();
                this.addTag_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddTag(int value) {
                ensureAddTagIsMutable();
                this.addTag_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddTag(Iterable<? extends Integer> values) {
                ensureAddTagIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addTag_);
                return this;
            }

            public Builder clearAddTag() {
                this.addTag_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            private void ensureRemoveTagIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.removeTag_ = new ArrayList(this.removeTag_);
                    this.bitField0_ |= 8;
                }
            }

            public List<Integer> getRemoveTagList() {
                return Collections.unmodifiableList(this.removeTag_);
            }

            public int getRemoveTagCount() {
                return this.removeTag_.size();
            }

            public int getRemoveTag(int index) {
                return this.removeTag_.get(index).intValue();
            }

            public Builder setRemoveTag(int index, int value) {
                ensureRemoveTagIsMutable();
                this.removeTag_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveTag(int value) {
                ensureRemoveTagIsMutable();
                this.removeTag_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveTag(Iterable<? extends Integer> values) {
                ensureRemoveTagIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeTag_);
                return this;
            }

            public Builder clearRemoveTag() {
                this.removeTag_ = Collections.emptyList();
                this.bitField0_ &= -9;
                return this;
            }

            private void ensureAddTagRuleNameIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.addTagRuleName_ = new ArrayList(this.addTagRuleName_);
                    this.bitField0_ |= 16;
                }
            }

            public List<Integer> getAddTagRuleNameList() {
                return Collections.unmodifiableList(this.addTagRuleName_);
            }

            public int getAddTagRuleNameCount() {
                return this.addTagRuleName_.size();
            }

            public int getAddTagRuleName(int index) {
                return this.addTagRuleName_.get(index).intValue();
            }

            public Builder setAddTagRuleName(int index, int value) {
                ensureAddTagRuleNameIsMutable();
                this.addTagRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddTagRuleName(int value) {
                ensureAddTagRuleNameIsMutable();
                this.addTagRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddTagRuleName(Iterable<? extends Integer> values) {
                ensureAddTagRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addTagRuleName_);
                return this;
            }

            public Builder clearAddTagRuleName() {
                this.addTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            private void ensureRemoveTagRuleNameIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.removeTagRuleName_ = new ArrayList(this.removeTagRuleName_);
                    this.bitField0_ |= 32;
                }
            }

            public List<Integer> getRemoveTagRuleNameList() {
                return Collections.unmodifiableList(this.removeTagRuleName_);
            }

            public int getRemoveTagRuleNameCount() {
                return this.removeTagRuleName_.size();
            }

            public int getRemoveTagRuleName(int index) {
                return this.removeTagRuleName_.get(index).intValue();
            }

            public Builder setRemoveTagRuleName(int index, int value) {
                ensureRemoveTagRuleNameIsMutable();
                this.removeTagRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveTagRuleName(int value) {
                ensureRemoveTagRuleNameIsMutable();
                this.removeTagRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveTagRuleName(Iterable<? extends Integer> values) {
                ensureRemoveTagRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeTagRuleName_);
                return this;
            }

            public Builder clearRemoveTagRuleName() {
                this.removeTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -33;
                return this;
            }

            private void ensureAddMacroIsMutable() {
                if ((this.bitField0_ & 64) != 64) {
                    this.addMacro_ = new ArrayList(this.addMacro_);
                    this.bitField0_ |= 64;
                }
            }

            public List<Integer> getAddMacroList() {
                return Collections.unmodifiableList(this.addMacro_);
            }

            public int getAddMacroCount() {
                return this.addMacro_.size();
            }

            public int getAddMacro(int index) {
                return this.addMacro_.get(index).intValue();
            }

            public Builder setAddMacro(int index, int value) {
                ensureAddMacroIsMutable();
                this.addMacro_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddMacro(int value) {
                ensureAddMacroIsMutable();
                this.addMacro_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddMacro(Iterable<? extends Integer> values) {
                ensureAddMacroIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addMacro_);
                return this;
            }

            public Builder clearAddMacro() {
                this.addMacro_ = Collections.emptyList();
                this.bitField0_ &= -65;
                return this;
            }

            private void ensureRemoveMacroIsMutable() {
                if ((this.bitField0_ & 128) != 128) {
                    this.removeMacro_ = new ArrayList(this.removeMacro_);
                    this.bitField0_ |= 128;
                }
            }

            public List<Integer> getRemoveMacroList() {
                return Collections.unmodifiableList(this.removeMacro_);
            }

            public int getRemoveMacroCount() {
                return this.removeMacro_.size();
            }

            public int getRemoveMacro(int index) {
                return this.removeMacro_.get(index).intValue();
            }

            public Builder setRemoveMacro(int index, int value) {
                ensureRemoveMacroIsMutable();
                this.removeMacro_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveMacro(int value) {
                ensureRemoveMacroIsMutable();
                this.removeMacro_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveMacro(Iterable<? extends Integer> values) {
                ensureRemoveMacroIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeMacro_);
                return this;
            }

            public Builder clearRemoveMacro() {
                this.removeMacro_ = Collections.emptyList();
                this.bitField0_ &= -129;
                return this;
            }

            private void ensureAddMacroRuleNameIsMutable() {
                if ((this.bitField0_ & 256) != 256) {
                    this.addMacroRuleName_ = new ArrayList(this.addMacroRuleName_);
                    this.bitField0_ |= 256;
                }
            }

            public List<Integer> getAddMacroRuleNameList() {
                return Collections.unmodifiableList(this.addMacroRuleName_);
            }

            public int getAddMacroRuleNameCount() {
                return this.addMacroRuleName_.size();
            }

            public int getAddMacroRuleName(int index) {
                return this.addMacroRuleName_.get(index).intValue();
            }

            public Builder setAddMacroRuleName(int index, int value) {
                ensureAddMacroRuleNameIsMutable();
                this.addMacroRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddMacroRuleName(int value) {
                ensureAddMacroRuleNameIsMutable();
                this.addMacroRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddMacroRuleName(Iterable<? extends Integer> values) {
                ensureAddMacroRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addMacroRuleName_);
                return this;
            }

            public Builder clearAddMacroRuleName() {
                this.addMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -257;
                return this;
            }

            private void ensureRemoveMacroRuleNameIsMutable() {
                if ((this.bitField0_ & 512) != 512) {
                    this.removeMacroRuleName_ = new ArrayList(this.removeMacroRuleName_);
                    this.bitField0_ |= 512;
                }
            }

            public List<Integer> getRemoveMacroRuleNameList() {
                return Collections.unmodifiableList(this.removeMacroRuleName_);
            }

            public int getRemoveMacroRuleNameCount() {
                return this.removeMacroRuleName_.size();
            }

            public int getRemoveMacroRuleName(int index) {
                return this.removeMacroRuleName_.get(index).intValue();
            }

            public Builder setRemoveMacroRuleName(int index, int value) {
                ensureRemoveMacroRuleNameIsMutable();
                this.removeMacroRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveMacroRuleName(int value) {
                ensureRemoveMacroRuleNameIsMutable();
                this.removeMacroRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveMacroRuleName(Iterable<? extends Integer> values) {
                ensureRemoveMacroRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeMacroRuleName_);
                return this;
            }

            public Builder clearRemoveMacroRuleName() {
                this.removeMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -513;
                return this;
            }
        }
    }

    public static final class CacheOption extends GeneratedMessageLite implements CacheOptionOrBuilder {
        public static final int EXPIRATION_SECONDS_FIELD_NUMBER = 2;
        public static final int GCACHE_EXPIRATION_SECONDS_FIELD_NUMBER = 3;
        public static final int LEVEL_FIELD_NUMBER = 1;
        public static Parser<CacheOption> PARSER = new AbstractParser<CacheOption>() {
            public CacheOption parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new CacheOption(input, extensionRegistry);
            }
        };
        private static final CacheOption defaultInstance = new CacheOption(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public int expirationSeconds_;
        /* access modifiers changed from: private */
        public int gcacheExpirationSeconds_;
        /* access modifiers changed from: private */
        public CacheLevel level_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;

        private CacheOption(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private CacheOption(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static CacheOption getDefaultInstance() {
            return defaultInstance;
        }

        public CacheOption getDefaultInstanceForType() {
            return defaultInstance;
        }

        private CacheOption(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                            int rawValue = input.readEnum();
                            CacheLevel value = CacheLevel.valueOf(rawValue);
                            if (value != null) {
                                this.bitField0_ |= 1;
                                this.level_ = value;
                                break;
                            } else {
                                unknownFieldsCodedOutput.writeRawVarint32(tag);
                                unknownFieldsCodedOutput.writeRawVarint32(rawValue);
                                break;
                            }
                        case 16:
                            this.bitField0_ |= 2;
                            this.expirationSeconds_ = input.readInt32();
                            break;
                        case 24:
                            this.bitField0_ |= 4;
                            this.gcacheExpirationSeconds_ = input.readInt32();
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

        public Parser<CacheOption> getParserForType() {
            return PARSER;
        }

        public enum CacheLevel implements Internal.EnumLite {
            NO_CACHE(0, 1),
            PRIVATE(1, 2),
            PUBLIC(2, 3);
            
            public static final int NO_CACHE_VALUE = 1;
            public static final int PRIVATE_VALUE = 2;
            public static final int PUBLIC_VALUE = 3;
            private static Internal.EnumLiteMap<CacheLevel> internalValueMap;
            private final int value;

            static {
                internalValueMap = new Internal.EnumLiteMap<CacheLevel>() {
                    public CacheLevel findValueByNumber(int number) {
                        return CacheLevel.valueOf(number);
                    }
                };
            }

            public final int getNumber() {
                return this.value;
            }

            public static CacheLevel valueOf(int value2) {
                switch (value2) {
                    case 1:
                        return NO_CACHE;
                    case 2:
                        return PRIVATE;
                    case 3:
                        return PUBLIC;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<CacheLevel> internalGetValueMap() {
                return internalValueMap;
            }

            private CacheLevel(int index, int value2) {
                this.value = value2;
            }
        }

        public boolean hasLevel() {
            return (this.bitField0_ & 1) == 1;
        }

        public CacheLevel getLevel() {
            return this.level_;
        }

        public boolean hasExpirationSeconds() {
            return (this.bitField0_ & 2) == 2;
        }

        public int getExpirationSeconds() {
            return this.expirationSeconds_;
        }

        public boolean hasGcacheExpirationSeconds() {
            return (this.bitField0_ & 4) == 4;
        }

        public int getGcacheExpirationSeconds() {
            return this.gcacheExpirationSeconds_;
        }

        private void initFields() {
            this.level_ = CacheLevel.NO_CACHE;
            this.expirationSeconds_ = 0;
            this.gcacheExpirationSeconds_ = 0;
        }

        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == -1) {
                this.memoizedIsInitialized = 1;
                return true;
            } else if (isInitialized == 1) {
                return true;
            } else {
                return false;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeEnum(1, this.level_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(2, this.expirationSeconds_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeInt32(3, this.gcacheExpirationSeconds_);
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
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.level_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeInt32Size(2, this.expirationSeconds_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeInt32Size(3, this.gcacheExpirationSeconds_);
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
            boolean result2;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof CacheOption)) {
                return super.equals(obj);
            }
            CacheOption other = (CacheOption) obj;
            boolean result3 = 1 != 0 && hasLevel() == other.hasLevel();
            if (hasLevel()) {
                result3 = result3 && getLevel() == other.getLevel();
            }
            if (!result3 || hasExpirationSeconds() != other.hasExpirationSeconds()) {
                result = false;
            } else {
                result = true;
            }
            if (hasExpirationSeconds()) {
                if (!result || getExpirationSeconds() != other.getExpirationSeconds()) {
                    result = false;
                } else {
                    result = true;
                }
            }
            if (!result || hasGcacheExpirationSeconds() != other.hasGcacheExpirationSeconds()) {
                result2 = false;
            } else {
                result2 = true;
            }
            if (hasGcacheExpirationSeconds()) {
                if (!result2 || getGcacheExpirationSeconds() != other.getGcacheExpirationSeconds()) {
                    result2 = false;
                } else {
                    result2 = true;
                }
            }
            return result2;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = CacheOption.class.hashCode() + 779;
            if (hasLevel()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashEnum(getLevel());
            }
            if (hasExpirationSeconds()) {
                hash = (((hash * 37) + 2) * 53) + getExpirationSeconds();
            }
            if (hasGcacheExpirationSeconds()) {
                hash = (((hash * 37) + 3) * 53) + getGcacheExpirationSeconds();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$CacheOption");
            }
            return mutableDefault;
        }

        public static CacheOption parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static CacheOption parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static CacheOption parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static CacheOption parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static CacheOption parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static CacheOption parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static CacheOption parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static CacheOption parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static CacheOption parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static CacheOption parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(CacheOption prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<CacheOption, Builder> implements CacheOptionOrBuilder {
            private int bitField0_;
            private int expirationSeconds_;
            private int gcacheExpirationSeconds_;
            private CacheLevel level_ = CacheLevel.NO_CACHE;

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
                this.level_ = CacheLevel.NO_CACHE;
                this.bitField0_ &= -2;
                this.expirationSeconds_ = 0;
                this.bitField0_ &= -3;
                this.gcacheExpirationSeconds_ = 0;
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public CacheOption getDefaultInstanceForType() {
                return CacheOption.getDefaultInstance();
            }

            public CacheOption build() {
                CacheOption result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public CacheOption buildPartial() {
                CacheOption result = new CacheOption((GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                CacheLevel unused = result.level_ = this.level_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                int unused2 = result.expirationSeconds_ = this.expirationSeconds_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                int unused3 = result.gcacheExpirationSeconds_ = this.gcacheExpirationSeconds_;
                int unused4 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(CacheOption other) {
                if (other != CacheOption.getDefaultInstance()) {
                    if (other.hasLevel()) {
                        setLevel(other.getLevel());
                    }
                    if (other.hasExpirationSeconds()) {
                        setExpirationSeconds(other.getExpirationSeconds());
                    }
                    if (other.hasGcacheExpirationSeconds()) {
                        setGcacheExpirationSeconds(other.getGcacheExpirationSeconds());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                return true;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    CacheOption parsedMessage = CacheOption.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    CacheOption parsedMessage2 = (CacheOption) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((CacheOption) null);
                    }
                    throw th;
                }
            }

            public boolean hasLevel() {
                return (this.bitField0_ & 1) == 1;
            }

            public CacheLevel getLevel() {
                return this.level_;
            }

            public Builder setLevel(CacheLevel value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.level_ = value;
                return this;
            }

            public Builder clearLevel() {
                this.bitField0_ &= -2;
                this.level_ = CacheLevel.NO_CACHE;
                return this;
            }

            public boolean hasExpirationSeconds() {
                return (this.bitField0_ & 2) == 2;
            }

            public int getExpirationSeconds() {
                return this.expirationSeconds_;
            }

            public Builder setExpirationSeconds(int value) {
                this.bitField0_ |= 2;
                this.expirationSeconds_ = value;
                return this;
            }

            public Builder clearExpirationSeconds() {
                this.bitField0_ &= -3;
                this.expirationSeconds_ = 0;
                return this;
            }

            public boolean hasGcacheExpirationSeconds() {
                return (this.bitField0_ & 4) == 4;
            }

            public int getGcacheExpirationSeconds() {
                return this.gcacheExpirationSeconds_;
            }

            public Builder setGcacheExpirationSeconds(int value) {
                this.bitField0_ |= 4;
                this.gcacheExpirationSeconds_ = value;
                return this;
            }

            public Builder clearGcacheExpirationSeconds() {
                this.bitField0_ &= -5;
                this.gcacheExpirationSeconds_ = 0;
                return this;
            }
        }
    }

    public static final class Resource extends GeneratedMessageLite implements ResourceOrBuilder {
        public static final int ENABLE_AUTO_EVENT_TRACKING_FIELD_NUMBER = 18;
        public static final int KEY_FIELD_NUMBER = 1;
        public static final int LIVE_JS_CACHE_OPTION_FIELD_NUMBER = 14;
        public static final int MACRO_FIELD_NUMBER = 4;
        public static final int MALWARE_SCAN_AUTH_CODE_FIELD_NUMBER = 10;
        public static Parser<Resource> PARSER = new AbstractParser<Resource>() {
            public Resource parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Resource(input, extensionRegistry);
            }
        };
        public static final int PREDICATE_FIELD_NUMBER = 6;
        public static final int PREVIEW_AUTH_CODE_FIELD_NUMBER = 9;
        public static final int PROPERTY_FIELD_NUMBER = 3;
        public static final int REPORTING_SAMPLE_RATE_FIELD_NUMBER = 15;
        public static final int RESOURCE_FORMAT_VERSION_FIELD_NUMBER = 17;
        public static final int RULE_FIELD_NUMBER = 7;
        public static final int TAG_FIELD_NUMBER = 5;
        public static final int TEMPLATE_VERSION_SET_FIELD_NUMBER = 12;
        public static final int USAGE_CONTEXT_FIELD_NUMBER = 16;
        public static final int VALUE_FIELD_NUMBER = 2;
        public static final int VERSION_FIELD_NUMBER = 13;
        private static final Resource defaultInstance = new Resource(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public boolean enableAutoEventTracking_;
        /* access modifiers changed from: private */
        public LazyStringList key_;
        /* access modifiers changed from: private */
        public CacheOption liveJsCacheOption_;
        /* access modifiers changed from: private */
        public List<FunctionCall> macro_;
        /* access modifiers changed from: private */
        public Object malwareScanAuthCode_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public List<FunctionCall> predicate_;
        /* access modifiers changed from: private */
        public Object previewAuthCode_;
        /* access modifiers changed from: private */
        public List<Property> property_;
        /* access modifiers changed from: private */
        public float reportingSampleRate_;
        /* access modifiers changed from: private */
        public int resourceFormatVersion_;
        /* access modifiers changed from: private */
        public List<Rule> rule_;
        /* access modifiers changed from: private */
        public List<FunctionCall> tag_;
        /* access modifiers changed from: private */
        public Object templateVersionSet_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;
        /* access modifiers changed from: private */
        public LazyStringList usageContext_;
        /* access modifiers changed from: private */
        public List<TypeSystem.Value> value_;
        /* access modifiers changed from: private */
        public Object version_;

        private Resource(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Resource(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Resource getDefaultInstance() {
            return defaultInstance;
        }

        public Resource getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Resource(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int mutable_bitField0_ = 0;
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
                        case 10:
                            ByteString bs = input.readBytes();
                            if ((mutable_bitField0_ & 1) != 1) {
                                this.key_ = new LazyStringArrayList();
                                mutable_bitField0_ |= 1;
                            }
                            this.key_.add(bs);
                            break;
                        case 18:
                            if ((mutable_bitField0_ & 2) != 2) {
                                this.value_ = new ArrayList();
                                mutable_bitField0_ |= 2;
                            }
                            this.value_.add(input.readMessage(TypeSystem.Value.PARSER, extensionRegistry));
                            break;
                        case 26:
                            if ((mutable_bitField0_ & 4) != 4) {
                                this.property_ = new ArrayList();
                                mutable_bitField0_ |= 4;
                            }
                            this.property_.add(input.readMessage(Property.PARSER, extensionRegistry));
                            break;
                        case 34:
                            if ((mutable_bitField0_ & 8) != 8) {
                                this.macro_ = new ArrayList();
                                mutable_bitField0_ |= 8;
                            }
                            this.macro_.add(input.readMessage(FunctionCall.PARSER, extensionRegistry));
                            break;
                        case 42:
                            if ((mutable_bitField0_ & 16) != 16) {
                                this.tag_ = new ArrayList();
                                mutable_bitField0_ |= 16;
                            }
                            this.tag_.add(input.readMessage(FunctionCall.PARSER, extensionRegistry));
                            break;
                        case 50:
                            if ((mutable_bitField0_ & 32) != 32) {
                                this.predicate_ = new ArrayList();
                                mutable_bitField0_ |= 32;
                            }
                            this.predicate_.add(input.readMessage(FunctionCall.PARSER, extensionRegistry));
                            break;
                        case 58:
                            if ((mutable_bitField0_ & 64) != 64) {
                                this.rule_ = new ArrayList();
                                mutable_bitField0_ |= 64;
                            }
                            this.rule_.add(input.readMessage(Rule.PARSER, extensionRegistry));
                            break;
                        case 74:
                            ByteString bs2 = input.readBytes();
                            this.bitField0_ |= 1;
                            this.previewAuthCode_ = bs2;
                            break;
                        case 82:
                            ByteString bs3 = input.readBytes();
                            this.bitField0_ |= 2;
                            this.malwareScanAuthCode_ = bs3;
                            break;
                        case 98:
                            ByteString bs4 = input.readBytes();
                            this.bitField0_ |= 4;
                            this.templateVersionSet_ = bs4;
                            break;
                        case BaseInterface.RESULT_LOG_IN:
                            ByteString bs5 = input.readBytes();
                            this.bitField0_ |= 8;
                            this.version_ = bs5;
                            break;
                        case 114:
                            CacheOption.Builder subBuilder = (this.bitField0_ & 16) == 16 ? this.liveJsCacheOption_.toBuilder() : null;
                            this.liveJsCacheOption_ = (CacheOption) input.readMessage(CacheOption.PARSER, extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.liveJsCacheOption_);
                                this.liveJsCacheOption_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 16;
                            break;
                        case 125:
                            this.bitField0_ |= 32;
                            this.reportingSampleRate_ = input.readFloat();
                            break;
                        case 130:
                            ByteString bs6 = input.readBytes();
                            if ((mutable_bitField0_ & 16384) != 16384) {
                                this.usageContext_ = new LazyStringArrayList();
                                mutable_bitField0_ |= 16384;
                            }
                            this.usageContext_.add(bs6);
                            break;
                        case 136:
                            this.bitField0_ |= 128;
                            this.resourceFormatVersion_ = input.readInt32();
                            break;
                        case 144:
                            this.bitField0_ |= 64;
                            this.enableAutoEventTracking_ = input.readBool();
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
                    if ((mutable_bitField0_ & 1) == 1) {
                        this.key_ = new UnmodifiableLazyStringList(this.key_);
                    }
                    if ((mutable_bitField0_ & 2) == 2) {
                        this.value_ = Collections.unmodifiableList(this.value_);
                    }
                    if ((mutable_bitField0_ & 4) == 4) {
                        this.property_ = Collections.unmodifiableList(this.property_);
                    }
                    if ((mutable_bitField0_ & 8) == 8) {
                        this.macro_ = Collections.unmodifiableList(this.macro_);
                    }
                    if ((mutable_bitField0_ & 16) == 16) {
                        this.tag_ = Collections.unmodifiableList(this.tag_);
                    }
                    if ((mutable_bitField0_ & 32) == 32) {
                        this.predicate_ = Collections.unmodifiableList(this.predicate_);
                    }
                    if ((mutable_bitField0_ & 64) == 64) {
                        this.rule_ = Collections.unmodifiableList(this.rule_);
                    }
                    if ((mutable_bitField0_ & 16384) == 16384) {
                        this.usageContext_ = new UnmodifiableLazyStringList(this.usageContext_);
                    }
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
            if ((mutable_bitField0_ & 1) == 1) {
                this.key_ = new UnmodifiableLazyStringList(this.key_);
            }
            if ((mutable_bitField0_ & 2) == 2) {
                this.value_ = Collections.unmodifiableList(this.value_);
            }
            if ((mutable_bitField0_ & 4) == 4) {
                this.property_ = Collections.unmodifiableList(this.property_);
            }
            if ((mutable_bitField0_ & 8) == 8) {
                this.macro_ = Collections.unmodifiableList(this.macro_);
            }
            if ((mutable_bitField0_ & 16) == 16) {
                this.tag_ = Collections.unmodifiableList(this.tag_);
            }
            if ((mutable_bitField0_ & 32) == 32) {
                this.predicate_ = Collections.unmodifiableList(this.predicate_);
            }
            if ((mutable_bitField0_ & 64) == 64) {
                this.rule_ = Collections.unmodifiableList(this.rule_);
            }
            if ((mutable_bitField0_ & 16384) == 16384) {
                this.usageContext_ = new UnmodifiableLazyStringList(this.usageContext_);
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

        public Parser<Resource> getParserForType() {
            return PARSER;
        }

        public List<String> getKeyList() {
            return this.key_;
        }

        public int getKeyCount() {
            return this.key_.size();
        }

        public String getKey(int index) {
            return (String) this.key_.get(index);
        }

        public ByteString getKeyBytes(int index) {
            return this.key_.getByteString(index);
        }

        public List<TypeSystem.Value> getValueList() {
            return this.value_;
        }

        public List<? extends TypeSystem.ValueOrBuilder> getValueOrBuilderList() {
            return this.value_;
        }

        public int getValueCount() {
            return this.value_.size();
        }

        public TypeSystem.Value getValue(int index) {
            return this.value_.get(index);
        }

        public TypeSystem.ValueOrBuilder getValueOrBuilder(int index) {
            return this.value_.get(index);
        }

        public List<Property> getPropertyList() {
            return this.property_;
        }

        public List<? extends PropertyOrBuilder> getPropertyOrBuilderList() {
            return this.property_;
        }

        public int getPropertyCount() {
            return this.property_.size();
        }

        public Property getProperty(int index) {
            return this.property_.get(index);
        }

        public PropertyOrBuilder getPropertyOrBuilder(int index) {
            return this.property_.get(index);
        }

        public List<FunctionCall> getMacroList() {
            return this.macro_;
        }

        public List<? extends FunctionCallOrBuilder> getMacroOrBuilderList() {
            return this.macro_;
        }

        public int getMacroCount() {
            return this.macro_.size();
        }

        public FunctionCall getMacro(int index) {
            return this.macro_.get(index);
        }

        public FunctionCallOrBuilder getMacroOrBuilder(int index) {
            return this.macro_.get(index);
        }

        public List<FunctionCall> getTagList() {
            return this.tag_;
        }

        public List<? extends FunctionCallOrBuilder> getTagOrBuilderList() {
            return this.tag_;
        }

        public int getTagCount() {
            return this.tag_.size();
        }

        public FunctionCall getTag(int index) {
            return this.tag_.get(index);
        }

        public FunctionCallOrBuilder getTagOrBuilder(int index) {
            return this.tag_.get(index);
        }

        public List<FunctionCall> getPredicateList() {
            return this.predicate_;
        }

        public List<? extends FunctionCallOrBuilder> getPredicateOrBuilderList() {
            return this.predicate_;
        }

        public int getPredicateCount() {
            return this.predicate_.size();
        }

        public FunctionCall getPredicate(int index) {
            return this.predicate_.get(index);
        }

        public FunctionCallOrBuilder getPredicateOrBuilder(int index) {
            return this.predicate_.get(index);
        }

        public List<Rule> getRuleList() {
            return this.rule_;
        }

        public List<? extends RuleOrBuilder> getRuleOrBuilderList() {
            return this.rule_;
        }

        public int getRuleCount() {
            return this.rule_.size();
        }

        public Rule getRule(int index) {
            return this.rule_.get(index);
        }

        public RuleOrBuilder getRuleOrBuilder(int index) {
            return this.rule_.get(index);
        }

        public boolean hasPreviewAuthCode() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getPreviewAuthCode() {
            Object ref = this.previewAuthCode_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.previewAuthCode_ = s;
            }
            return s;
        }

        public ByteString getPreviewAuthCodeBytes() {
            Object ref = this.previewAuthCode_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.previewAuthCode_ = b;
            return b;
        }

        public boolean hasMalwareScanAuthCode() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getMalwareScanAuthCode() {
            Object ref = this.malwareScanAuthCode_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.malwareScanAuthCode_ = s;
            }
            return s;
        }

        public ByteString getMalwareScanAuthCodeBytes() {
            Object ref = this.malwareScanAuthCode_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.malwareScanAuthCode_ = b;
            return b;
        }

        public boolean hasTemplateVersionSet() {
            return (this.bitField0_ & 4) == 4;
        }

        public String getTemplateVersionSet() {
            Object ref = this.templateVersionSet_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.templateVersionSet_ = s;
            }
            return s;
        }

        public ByteString getTemplateVersionSetBytes() {
            Object ref = this.templateVersionSet_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.templateVersionSet_ = b;
            return b;
        }

        public boolean hasVersion() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getVersion() {
            Object ref = this.version_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.version_ = s;
            }
            return s;
        }

        public ByteString getVersionBytes() {
            Object ref = this.version_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.version_ = b;
            return b;
        }

        public boolean hasLiveJsCacheOption() {
            return (this.bitField0_ & 16) == 16;
        }

        public CacheOption getLiveJsCacheOption() {
            return this.liveJsCacheOption_;
        }

        public boolean hasReportingSampleRate() {
            return (this.bitField0_ & 32) == 32;
        }

        public float getReportingSampleRate() {
            return this.reportingSampleRate_;
        }

        public boolean hasEnableAutoEventTracking() {
            return (this.bitField0_ & 64) == 64;
        }

        public boolean getEnableAutoEventTracking() {
            return this.enableAutoEventTracking_;
        }

        public List<String> getUsageContextList() {
            return this.usageContext_;
        }

        public int getUsageContextCount() {
            return this.usageContext_.size();
        }

        public String getUsageContext(int index) {
            return (String) this.usageContext_.get(index);
        }

        public ByteString getUsageContextBytes(int index) {
            return this.usageContext_.getByteString(index);
        }

        public boolean hasResourceFormatVersion() {
            return (this.bitField0_ & 128) == 128;
        }

        public int getResourceFormatVersion() {
            return this.resourceFormatVersion_;
        }

        private void initFields() {
            this.key_ = LazyStringArrayList.EMPTY;
            this.value_ = Collections.emptyList();
            this.property_ = Collections.emptyList();
            this.macro_ = Collections.emptyList();
            this.tag_ = Collections.emptyList();
            this.predicate_ = Collections.emptyList();
            this.rule_ = Collections.emptyList();
            this.previewAuthCode_ = "";
            this.malwareScanAuthCode_ = "";
            this.templateVersionSet_ = GreeDefs.BARCODE;
            this.version_ = "";
            this.liveJsCacheOption_ = CacheOption.getDefaultInstance();
            this.reportingSampleRate_ = 0.0f;
            this.enableAutoEventTracking_ = false;
            this.usageContext_ = LazyStringArrayList.EMPTY;
            this.resourceFormatVersion_ = 0;
        }

        public final boolean isInitialized() {
            boolean z = true;
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                if (isInitialized != 1) {
                    z = false;
                }
                return z;
            }
            for (int i = 0; i < getValueCount(); i++) {
                if (!getValue(i).isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            for (int i2 = 0; i2 < getPropertyCount(); i2++) {
                if (!getProperty(i2).isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            for (int i3 = 0; i3 < getMacroCount(); i3++) {
                if (!getMacro(i3).isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            for (int i4 = 0; i4 < getTagCount(); i4++) {
                if (!getTag(i4).isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            for (int i5 = 0; i5 < getPredicateCount(); i5++) {
                if (!getPredicate(i5).isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.key_.size(); i++) {
                output.writeBytes(1, this.key_.getByteString(i));
            }
            for (int i2 = 0; i2 < this.value_.size(); i2++) {
                output.writeMessage(2, this.value_.get(i2));
            }
            for (int i3 = 0; i3 < this.property_.size(); i3++) {
                output.writeMessage(3, this.property_.get(i3));
            }
            for (int i4 = 0; i4 < this.macro_.size(); i4++) {
                output.writeMessage(4, this.macro_.get(i4));
            }
            for (int i5 = 0; i5 < this.tag_.size(); i5++) {
                output.writeMessage(5, this.tag_.get(i5));
            }
            for (int i6 = 0; i6 < this.predicate_.size(); i6++) {
                output.writeMessage(6, this.predicate_.get(i6));
            }
            for (int i7 = 0; i7 < this.rule_.size(); i7++) {
                output.writeMessage(7, this.rule_.get(i7));
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(9, getPreviewAuthCodeBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(10, getMalwareScanAuthCodeBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeBytes(12, getTemplateVersionSetBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeBytes(13, getVersionBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeMessage(14, this.liveJsCacheOption_);
            }
            if ((this.bitField0_ & 32) == 32) {
                output.writeFloat(15, this.reportingSampleRate_);
            }
            for (int i8 = 0; i8 < this.usageContext_.size(); i8++) {
                output.writeBytes(16, this.usageContext_.getByteString(i8));
            }
            if ((this.bitField0_ & 128) == 128) {
                output.writeInt32(17, this.resourceFormatVersion_);
            }
            if ((this.bitField0_ & 64) == 64) {
                output.writeBool(18, this.enableAutoEventTracking_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int dataSize = 0;
            for (int i = 0; i < this.key_.size(); i++) {
                dataSize += CodedOutputStream.computeBytesSizeNoTag(this.key_.getByteString(i));
            }
            int size2 = 0 + dataSize + (getKeyList().size() * 1);
            for (int i2 = 0; i2 < this.value_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(2, this.value_.get(i2));
            }
            for (int i3 = 0; i3 < this.property_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.property_.get(i3));
            }
            for (int i4 = 0; i4 < this.macro_.size(); i4++) {
                size2 += CodedOutputStream.computeMessageSize(4, this.macro_.get(i4));
            }
            for (int i5 = 0; i5 < this.tag_.size(); i5++) {
                size2 += CodedOutputStream.computeMessageSize(5, this.tag_.get(i5));
            }
            for (int i6 = 0; i6 < this.predicate_.size(); i6++) {
                size2 += CodedOutputStream.computeMessageSize(6, this.predicate_.get(i6));
            }
            for (int i7 = 0; i7 < this.rule_.size(); i7++) {
                size2 += CodedOutputStream.computeMessageSize(7, this.rule_.get(i7));
            }
            if ((this.bitField0_ & 1) == 1) {
                size2 += CodedOutputStream.computeBytesSize(9, getPreviewAuthCodeBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(10, getMalwareScanAuthCodeBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeBytesSize(12, getTemplateVersionSetBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                size2 += CodedOutputStream.computeBytesSize(13, getVersionBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                size2 += CodedOutputStream.computeMessageSize(14, this.liveJsCacheOption_);
            }
            if ((this.bitField0_ & 32) == 32) {
                size2 += CodedOutputStream.computeFloatSize(15, this.reportingSampleRate_);
            }
            int dataSize2 = 0;
            for (int i8 = 0; i8 < this.usageContext_.size(); i8++) {
                dataSize2 += CodedOutputStream.computeBytesSizeNoTag(this.usageContext_.getByteString(i8));
            }
            int size3 = size2 + dataSize2 + (getUsageContextList().size() * 2);
            if ((this.bitField0_ & 128) == 128) {
                size3 += CodedOutputStream.computeInt32Size(17, this.resourceFormatVersion_);
            }
            if ((this.bitField0_ & 64) == 64) {
                size3 += CodedOutputStream.computeBoolSize(18, this.enableAutoEventTracking_);
            }
            int size4 = size3 + this.unknownFields.size();
            this.memoizedSerializedSize = size4;
            return size4;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            boolean result;
            boolean result2;
            boolean result3;
            boolean result4;
            boolean result5;
            boolean result6;
            boolean result7;
            boolean result8;
            boolean result9;
            boolean result10;
            boolean result11;
            boolean result12;
            boolean result13;
            boolean result14;
            boolean result15;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Resource)) {
                return super.equals(obj);
            }
            Resource other = (Resource) obj;
            if (!(1 != 0 && getKeyList().equals(other.getKeyList())) || !getValueList().equals(other.getValueList())) {
                result = false;
            } else {
                result = true;
            }
            if (!result || !getPropertyList().equals(other.getPropertyList())) {
                result2 = false;
            } else {
                result2 = true;
            }
            if (!result2 || !getMacroList().equals(other.getMacroList())) {
                result3 = false;
            } else {
                result3 = true;
            }
            if (!result3 || !getTagList().equals(other.getTagList())) {
                result4 = false;
            } else {
                result4 = true;
            }
            if (!result4 || !getPredicateList().equals(other.getPredicateList())) {
                result5 = false;
            } else {
                result5 = true;
            }
            if (!result5 || !getRuleList().equals(other.getRuleList())) {
                result6 = false;
            } else {
                result6 = true;
            }
            if (!result6 || hasPreviewAuthCode() != other.hasPreviewAuthCode()) {
                result7 = false;
            } else {
                result7 = true;
            }
            if (hasPreviewAuthCode()) {
                if (!result7 || !getPreviewAuthCode().equals(other.getPreviewAuthCode())) {
                    result7 = false;
                } else {
                    result7 = true;
                }
            }
            if (!result7 || hasMalwareScanAuthCode() != other.hasMalwareScanAuthCode()) {
                result8 = false;
            } else {
                result8 = true;
            }
            if (hasMalwareScanAuthCode()) {
                if (!result8 || !getMalwareScanAuthCode().equals(other.getMalwareScanAuthCode())) {
                    result8 = false;
                } else {
                    result8 = true;
                }
            }
            if (!result8 || hasTemplateVersionSet() != other.hasTemplateVersionSet()) {
                result9 = false;
            } else {
                result9 = true;
            }
            if (hasTemplateVersionSet()) {
                if (!result9 || !getTemplateVersionSet().equals(other.getTemplateVersionSet())) {
                    result9 = false;
                } else {
                    result9 = true;
                }
            }
            if (!result9 || hasVersion() != other.hasVersion()) {
                result10 = false;
            } else {
                result10 = true;
            }
            if (hasVersion()) {
                if (!result10 || !getVersion().equals(other.getVersion())) {
                    result10 = false;
                } else {
                    result10 = true;
                }
            }
            if (!result10 || hasLiveJsCacheOption() != other.hasLiveJsCacheOption()) {
                result11 = false;
            } else {
                result11 = true;
            }
            if (hasLiveJsCacheOption()) {
                if (!result11 || !getLiveJsCacheOption().equals(other.getLiveJsCacheOption())) {
                    result11 = false;
                } else {
                    result11 = true;
                }
            }
            if (!result11 || hasReportingSampleRate() != other.hasReportingSampleRate()) {
                result12 = false;
            } else {
                result12 = true;
            }
            if (hasReportingSampleRate()) {
                if (!result12 || Float.floatToIntBits(getReportingSampleRate()) != Float.floatToIntBits(other.getReportingSampleRate())) {
                    result12 = false;
                } else {
                    result12 = true;
                }
            }
            if (!result12 || hasEnableAutoEventTracking() != other.hasEnableAutoEventTracking()) {
                result13 = false;
            } else {
                result13 = true;
            }
            if (hasEnableAutoEventTracking()) {
                if (!result13 || getEnableAutoEventTracking() != other.getEnableAutoEventTracking()) {
                    result13 = false;
                } else {
                    result13 = true;
                }
            }
            if (!result13 || !getUsageContextList().equals(other.getUsageContextList())) {
                result14 = false;
            } else {
                result14 = true;
            }
            if (!result14 || hasResourceFormatVersion() != other.hasResourceFormatVersion()) {
                result15 = false;
            } else {
                result15 = true;
            }
            if (hasResourceFormatVersion()) {
                if (!result15 || getResourceFormatVersion() != other.getResourceFormatVersion()) {
                    result15 = false;
                } else {
                    result15 = true;
                }
            }
            return result15;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Resource.class.hashCode() + 779;
            if (getKeyCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getKeyList().hashCode();
            }
            if (getValueCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getValueList().hashCode();
            }
            if (getPropertyCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getPropertyList().hashCode();
            }
            if (getMacroCount() > 0) {
                hash = (((hash * 37) + 4) * 53) + getMacroList().hashCode();
            }
            if (getTagCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getTagList().hashCode();
            }
            if (getPredicateCount() > 0) {
                hash = (((hash * 37) + 6) * 53) + getPredicateList().hashCode();
            }
            if (getRuleCount() > 0) {
                hash = (((hash * 37) + 7) * 53) + getRuleList().hashCode();
            }
            if (hasPreviewAuthCode()) {
                hash = (((hash * 37) + 9) * 53) + getPreviewAuthCode().hashCode();
            }
            if (hasMalwareScanAuthCode()) {
                hash = (((hash * 37) + 10) * 53) + getMalwareScanAuthCode().hashCode();
            }
            if (hasTemplateVersionSet()) {
                hash = (((hash * 37) + 12) * 53) + getTemplateVersionSet().hashCode();
            }
            if (hasVersion()) {
                hash = (((hash * 37) + 13) * 53) + getVersion().hashCode();
            }
            if (hasLiveJsCacheOption()) {
                hash = (((hash * 37) + 14) * 53) + getLiveJsCacheOption().hashCode();
            }
            if (hasReportingSampleRate()) {
                hash = (((hash * 37) + 15) * 53) + Float.floatToIntBits(getReportingSampleRate());
            }
            if (hasEnableAutoEventTracking()) {
                hash = (((hash * 37) + 18) * 53) + Internal.hashBoolean(getEnableAutoEventTracking());
            }
            if (getUsageContextCount() > 0) {
                hash = (((hash * 37) + 16) * 53) + getUsageContextList().hashCode();
            }
            if (hasResourceFormatVersion()) {
                hash = (((hash * 37) + 17) * 53) + getResourceFormatVersion();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Resource");
            }
            return mutableDefault;
        }

        public static Resource parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Resource parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Resource parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Resource parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Resource parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Resource parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Resource parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Resource parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Resource parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Resource parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Resource prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Resource, Builder> implements ResourceOrBuilder {
            private int bitField0_;
            private boolean enableAutoEventTracking_;
            private LazyStringList key_ = LazyStringArrayList.EMPTY;
            private CacheOption liveJsCacheOption_ = CacheOption.getDefaultInstance();
            private List<FunctionCall> macro_ = Collections.emptyList();
            private Object malwareScanAuthCode_ = "";
            private List<FunctionCall> predicate_ = Collections.emptyList();
            private Object previewAuthCode_ = "";
            private List<Property> property_ = Collections.emptyList();
            private float reportingSampleRate_;
            private int resourceFormatVersion_;
            private List<Rule> rule_ = Collections.emptyList();
            private List<FunctionCall> tag_ = Collections.emptyList();
            private Object templateVersionSet_ = GreeDefs.BARCODE;
            private LazyStringList usageContext_ = LazyStringArrayList.EMPTY;
            private List<TypeSystem.Value> value_ = Collections.emptyList();
            private Object version_ = "";

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
                this.key_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                this.value_ = Collections.emptyList();
                this.bitField0_ &= -3;
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.macro_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.tag_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.predicate_ = Collections.emptyList();
                this.bitField0_ &= -33;
                this.rule_ = Collections.emptyList();
                this.bitField0_ &= -65;
                this.previewAuthCode_ = "";
                this.bitField0_ &= -129;
                this.malwareScanAuthCode_ = "";
                this.bitField0_ &= -257;
                this.templateVersionSet_ = GreeDefs.BARCODE;
                this.bitField0_ &= -513;
                this.version_ = "";
                this.bitField0_ &= -1025;
                this.liveJsCacheOption_ = CacheOption.getDefaultInstance();
                this.bitField0_ &= -2049;
                this.reportingSampleRate_ = 0.0f;
                this.bitField0_ &= -4097;
                this.enableAutoEventTracking_ = false;
                this.bitField0_ &= -8193;
                this.usageContext_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -16385;
                this.resourceFormatVersion_ = 0;
                this.bitField0_ &= -32769;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public Resource getDefaultInstanceForType() {
                return Resource.getDefaultInstance();
            }

            public Resource build() {
                Resource result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public Resource buildPartial() {
                Resource result = new Resource((GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.key_ = new UnmodifiableLazyStringList(this.key_);
                    this.bitField0_ &= -2;
                }
                LazyStringList unused = result.key_ = this.key_;
                if ((this.bitField0_ & 2) == 2) {
                    this.value_ = Collections.unmodifiableList(this.value_);
                    this.bitField0_ &= -3;
                }
                List unused2 = result.value_ = this.value_;
                if ((this.bitField0_ & 4) == 4) {
                    this.property_ = Collections.unmodifiableList(this.property_);
                    this.bitField0_ &= -5;
                }
                List unused3 = result.property_ = this.property_;
                if ((this.bitField0_ & 8) == 8) {
                    this.macro_ = Collections.unmodifiableList(this.macro_);
                    this.bitField0_ &= -9;
                }
                List unused4 = result.macro_ = this.macro_;
                if ((this.bitField0_ & 16) == 16) {
                    this.tag_ = Collections.unmodifiableList(this.tag_);
                    this.bitField0_ &= -17;
                }
                List unused5 = result.tag_ = this.tag_;
                if ((this.bitField0_ & 32) == 32) {
                    this.predicate_ = Collections.unmodifiableList(this.predicate_);
                    this.bitField0_ &= -33;
                }
                List unused6 = result.predicate_ = this.predicate_;
                if ((this.bitField0_ & 64) == 64) {
                    this.rule_ = Collections.unmodifiableList(this.rule_);
                    this.bitField0_ &= -65;
                }
                List unused7 = result.rule_ = this.rule_;
                if ((from_bitField0_ & 128) == 128) {
                    to_bitField0_ = 0 | 1;
                }
                Object unused8 = result.previewAuthCode_ = this.previewAuthCode_;
                if ((from_bitField0_ & 256) == 256) {
                    to_bitField0_ |= 2;
                }
                Object unused9 = result.malwareScanAuthCode_ = this.malwareScanAuthCode_;
                if ((from_bitField0_ & 512) == 512) {
                    to_bitField0_ |= 4;
                }
                Object unused10 = result.templateVersionSet_ = this.templateVersionSet_;
                if ((from_bitField0_ & 1024) == 1024) {
                    to_bitField0_ |= 8;
                }
                Object unused11 = result.version_ = this.version_;
                if ((from_bitField0_ & 2048) == 2048) {
                    to_bitField0_ |= 16;
                }
                CacheOption unused12 = result.liveJsCacheOption_ = this.liveJsCacheOption_;
                if ((from_bitField0_ & 4096) == 4096) {
                    to_bitField0_ |= 32;
                }
                float unused13 = result.reportingSampleRate_ = this.reportingSampleRate_;
                if ((from_bitField0_ & 8192) == 8192) {
                    to_bitField0_ |= 64;
                }
                boolean unused14 = result.enableAutoEventTracking_ = this.enableAutoEventTracking_;
                if ((this.bitField0_ & 16384) == 16384) {
                    this.usageContext_ = new UnmodifiableLazyStringList(this.usageContext_);
                    this.bitField0_ &= -16385;
                }
                LazyStringList unused15 = result.usageContext_ = this.usageContext_;
                if ((from_bitField0_ & 32768) == 32768) {
                    to_bitField0_ |= 128;
                }
                int unused16 = result.resourceFormatVersion_ = this.resourceFormatVersion_;
                int unused17 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(Resource other) {
                if (other != Resource.getDefaultInstance()) {
                    if (!other.key_.isEmpty()) {
                        if (this.key_.isEmpty()) {
                            this.key_ = other.key_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureKeyIsMutable();
                            this.key_.addAll(other.key_);
                        }
                    }
                    if (!other.value_.isEmpty()) {
                        if (this.value_.isEmpty()) {
                            this.value_ = other.value_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureValueIsMutable();
                            this.value_.addAll(other.value_);
                        }
                    }
                    if (!other.property_.isEmpty()) {
                        if (this.property_.isEmpty()) {
                            this.property_ = other.property_;
                            this.bitField0_ &= -5;
                        } else {
                            ensurePropertyIsMutable();
                            this.property_.addAll(other.property_);
                        }
                    }
                    if (!other.macro_.isEmpty()) {
                        if (this.macro_.isEmpty()) {
                            this.macro_ = other.macro_;
                            this.bitField0_ &= -9;
                        } else {
                            ensureMacroIsMutable();
                            this.macro_.addAll(other.macro_);
                        }
                    }
                    if (!other.tag_.isEmpty()) {
                        if (this.tag_.isEmpty()) {
                            this.tag_ = other.tag_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureTagIsMutable();
                            this.tag_.addAll(other.tag_);
                        }
                    }
                    if (!other.predicate_.isEmpty()) {
                        if (this.predicate_.isEmpty()) {
                            this.predicate_ = other.predicate_;
                            this.bitField0_ &= -33;
                        } else {
                            ensurePredicateIsMutable();
                            this.predicate_.addAll(other.predicate_);
                        }
                    }
                    if (!other.rule_.isEmpty()) {
                        if (this.rule_.isEmpty()) {
                            this.rule_ = other.rule_;
                            this.bitField0_ &= -65;
                        } else {
                            ensureRuleIsMutable();
                            this.rule_.addAll(other.rule_);
                        }
                    }
                    if (other.hasPreviewAuthCode()) {
                        this.bitField0_ |= 128;
                        this.previewAuthCode_ = other.previewAuthCode_;
                    }
                    if (other.hasMalwareScanAuthCode()) {
                        this.bitField0_ |= 256;
                        this.malwareScanAuthCode_ = other.malwareScanAuthCode_;
                    }
                    if (other.hasTemplateVersionSet()) {
                        this.bitField0_ |= 512;
                        this.templateVersionSet_ = other.templateVersionSet_;
                    }
                    if (other.hasVersion()) {
                        this.bitField0_ |= 1024;
                        this.version_ = other.version_;
                    }
                    if (other.hasLiveJsCacheOption()) {
                        mergeLiveJsCacheOption(other.getLiveJsCacheOption());
                    }
                    if (other.hasReportingSampleRate()) {
                        setReportingSampleRate(other.getReportingSampleRate());
                    }
                    if (other.hasEnableAutoEventTracking()) {
                        setEnableAutoEventTracking(other.getEnableAutoEventTracking());
                    }
                    if (!other.usageContext_.isEmpty()) {
                        if (this.usageContext_.isEmpty()) {
                            this.usageContext_ = other.usageContext_;
                            this.bitField0_ &= -16385;
                        } else {
                            ensureUsageContextIsMutable();
                            this.usageContext_.addAll(other.usageContext_);
                        }
                    }
                    if (other.hasResourceFormatVersion()) {
                        setResourceFormatVersion(other.getResourceFormatVersion());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getValueCount(); i++) {
                    if (!getValue(i).isInitialized()) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getPropertyCount(); i2++) {
                    if (!getProperty(i2).isInitialized()) {
                        return false;
                    }
                }
                for (int i3 = 0; i3 < getMacroCount(); i3++) {
                    if (!getMacro(i3).isInitialized()) {
                        return false;
                    }
                }
                for (int i4 = 0; i4 < getTagCount(); i4++) {
                    if (!getTag(i4).isInitialized()) {
                        return false;
                    }
                }
                for (int i5 = 0; i5 < getPredicateCount(); i5++) {
                    if (!getPredicate(i5).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    Resource parsedMessage = Resource.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    Resource parsedMessage2 = (Resource) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((Resource) null);
                    }
                    throw th;
                }
            }

            private void ensureKeyIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.key_ = new LazyStringArrayList(this.key_);
                    this.bitField0_ |= 1;
                }
            }

            public List<String> getKeyList() {
                return Collections.unmodifiableList(this.key_);
            }

            public int getKeyCount() {
                return this.key_.size();
            }

            public String getKey(int index) {
                return (String) this.key_.get(index);
            }

            public ByteString getKeyBytes(int index) {
                return this.key_.getByteString(index);
            }

            public Builder setKey(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureKeyIsMutable();
                this.key_.set(index, value);
                return this;
            }

            public Builder addKey(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureKeyIsMutable();
                this.key_.add(value);
                return this;
            }

            public Builder addAllKey(Iterable<String> values) {
                ensureKeyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.key_);
                return this;
            }

            public Builder clearKey() {
                this.key_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            public Builder addKeyBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureKeyIsMutable();
                this.key_.add(value);
                return this;
            }

            private void ensureValueIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.value_ = new ArrayList(this.value_);
                    this.bitField0_ |= 2;
                }
            }

            public List<TypeSystem.Value> getValueList() {
                return Collections.unmodifiableList(this.value_);
            }

            public int getValueCount() {
                return this.value_.size();
            }

            public TypeSystem.Value getValue(int index) {
                return this.value_.get(index);
            }

            public Builder setValue(int index, TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.set(index, value);
                return this;
            }

            public Builder setValue(int index, TypeSystem.Value.Builder builderForValue) {
                ensureValueIsMutable();
                this.value_.set(index, builderForValue.build());
                return this;
            }

            public Builder addValue(TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.add(value);
                return this;
            }

            public Builder addValue(int index, TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.add(index, value);
                return this;
            }

            public Builder addValue(TypeSystem.Value.Builder builderForValue) {
                ensureValueIsMutable();
                this.value_.add(builderForValue.build());
                return this;
            }

            public Builder addValue(int index, TypeSystem.Value.Builder builderForValue) {
                ensureValueIsMutable();
                this.value_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllValue(Iterable<? extends TypeSystem.Value> values) {
                ensureValueIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.value_);
                return this;
            }

            public Builder clearValue() {
                this.value_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            public Builder removeValue(int index) {
                ensureValueIsMutable();
                this.value_.remove(index);
                return this;
            }

            private void ensurePropertyIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.property_ = new ArrayList(this.property_);
                    this.bitField0_ |= 4;
                }
            }

            public List<Property> getPropertyList() {
                return Collections.unmodifiableList(this.property_);
            }

            public int getPropertyCount() {
                return this.property_.size();
            }

            public Property getProperty(int index) {
                return this.property_.get(index);
            }

            public Builder setProperty(int index, Property value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertyIsMutable();
                this.property_.set(index, value);
                return this;
            }

            public Builder setProperty(int index, Property.Builder builderForValue) {
                ensurePropertyIsMutable();
                this.property_.set(index, builderForValue.build());
                return this;
            }

            public Builder addProperty(Property value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertyIsMutable();
                this.property_.add(value);
                return this;
            }

            public Builder addProperty(int index, Property value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertyIsMutable();
                this.property_.add(index, value);
                return this;
            }

            public Builder addProperty(Property.Builder builderForValue) {
                ensurePropertyIsMutable();
                this.property_.add(builderForValue.build());
                return this;
            }

            public Builder addProperty(int index, Property.Builder builderForValue) {
                ensurePropertyIsMutable();
                this.property_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllProperty(Iterable<? extends Property> values) {
                ensurePropertyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.property_);
                return this;
            }

            public Builder clearProperty() {
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            public Builder removeProperty(int index) {
                ensurePropertyIsMutable();
                this.property_.remove(index);
                return this;
            }

            private void ensureMacroIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.macro_ = new ArrayList(this.macro_);
                    this.bitField0_ |= 8;
                }
            }

            public List<FunctionCall> getMacroList() {
                return Collections.unmodifiableList(this.macro_);
            }

            public int getMacroCount() {
                return this.macro_.size();
            }

            public FunctionCall getMacro(int index) {
                return this.macro_.get(index);
            }

            public Builder setMacro(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMacroIsMutable();
                this.macro_.set(index, value);
                return this;
            }

            public Builder setMacro(int index, FunctionCall.Builder builderForValue) {
                ensureMacroIsMutable();
                this.macro_.set(index, builderForValue.build());
                return this;
            }

            public Builder addMacro(FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMacroIsMutable();
                this.macro_.add(value);
                return this;
            }

            public Builder addMacro(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMacroIsMutable();
                this.macro_.add(index, value);
                return this;
            }

            public Builder addMacro(FunctionCall.Builder builderForValue) {
                ensureMacroIsMutable();
                this.macro_.add(builderForValue.build());
                return this;
            }

            public Builder addMacro(int index, FunctionCall.Builder builderForValue) {
                ensureMacroIsMutable();
                this.macro_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllMacro(Iterable<? extends FunctionCall> values) {
                ensureMacroIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.macro_);
                return this;
            }

            public Builder clearMacro() {
                this.macro_ = Collections.emptyList();
                this.bitField0_ &= -9;
                return this;
            }

            public Builder removeMacro(int index) {
                ensureMacroIsMutable();
                this.macro_.remove(index);
                return this;
            }

            private void ensureTagIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.tag_ = new ArrayList(this.tag_);
                    this.bitField0_ |= 16;
                }
            }

            public List<FunctionCall> getTagList() {
                return Collections.unmodifiableList(this.tag_);
            }

            public int getTagCount() {
                return this.tag_.size();
            }

            public FunctionCall getTag(int index) {
                return this.tag_.get(index);
            }

            public Builder setTag(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTagIsMutable();
                this.tag_.set(index, value);
                return this;
            }

            public Builder setTag(int index, FunctionCall.Builder builderForValue) {
                ensureTagIsMutable();
                this.tag_.set(index, builderForValue.build());
                return this;
            }

            public Builder addTag(FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTagIsMutable();
                this.tag_.add(value);
                return this;
            }

            public Builder addTag(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTagIsMutable();
                this.tag_.add(index, value);
                return this;
            }

            public Builder addTag(FunctionCall.Builder builderForValue) {
                ensureTagIsMutable();
                this.tag_.add(builderForValue.build());
                return this;
            }

            public Builder addTag(int index, FunctionCall.Builder builderForValue) {
                ensureTagIsMutable();
                this.tag_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllTag(Iterable<? extends FunctionCall> values) {
                ensureTagIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.tag_);
                return this;
            }

            public Builder clearTag() {
                this.tag_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            public Builder removeTag(int index) {
                ensureTagIsMutable();
                this.tag_.remove(index);
                return this;
            }

            private void ensurePredicateIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.predicate_ = new ArrayList(this.predicate_);
                    this.bitField0_ |= 32;
                }
            }

            public List<FunctionCall> getPredicateList() {
                return Collections.unmodifiableList(this.predicate_);
            }

            public int getPredicateCount() {
                return this.predicate_.size();
            }

            public FunctionCall getPredicate(int index) {
                return this.predicate_.get(index);
            }

            public Builder setPredicate(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePredicateIsMutable();
                this.predicate_.set(index, value);
                return this;
            }

            public Builder setPredicate(int index, FunctionCall.Builder builderForValue) {
                ensurePredicateIsMutable();
                this.predicate_.set(index, builderForValue.build());
                return this;
            }

            public Builder addPredicate(FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePredicateIsMutable();
                this.predicate_.add(value);
                return this;
            }

            public Builder addPredicate(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePredicateIsMutable();
                this.predicate_.add(index, value);
                return this;
            }

            public Builder addPredicate(FunctionCall.Builder builderForValue) {
                ensurePredicateIsMutable();
                this.predicate_.add(builderForValue.build());
                return this;
            }

            public Builder addPredicate(int index, FunctionCall.Builder builderForValue) {
                ensurePredicateIsMutable();
                this.predicate_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllPredicate(Iterable<? extends FunctionCall> values) {
                ensurePredicateIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.predicate_);
                return this;
            }

            public Builder clearPredicate() {
                this.predicate_ = Collections.emptyList();
                this.bitField0_ &= -33;
                return this;
            }

            public Builder removePredicate(int index) {
                ensurePredicateIsMutable();
                this.predicate_.remove(index);
                return this;
            }

            private void ensureRuleIsMutable() {
                if ((this.bitField0_ & 64) != 64) {
                    this.rule_ = new ArrayList(this.rule_);
                    this.bitField0_ |= 64;
                }
            }

            public List<Rule> getRuleList() {
                return Collections.unmodifiableList(this.rule_);
            }

            public int getRuleCount() {
                return this.rule_.size();
            }

            public Rule getRule(int index) {
                return this.rule_.get(index);
            }

            public Builder setRule(int index, Rule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRuleIsMutable();
                this.rule_.set(index, value);
                return this;
            }

            public Builder setRule(int index, Rule.Builder builderForValue) {
                ensureRuleIsMutable();
                this.rule_.set(index, builderForValue.build());
                return this;
            }

            public Builder addRule(Rule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRuleIsMutable();
                this.rule_.add(value);
                return this;
            }

            public Builder addRule(int index, Rule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRuleIsMutable();
                this.rule_.add(index, value);
                return this;
            }

            public Builder addRule(Rule.Builder builderForValue) {
                ensureRuleIsMutable();
                this.rule_.add(builderForValue.build());
                return this;
            }

            public Builder addRule(int index, Rule.Builder builderForValue) {
                ensureRuleIsMutable();
                this.rule_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllRule(Iterable<? extends Rule> values) {
                ensureRuleIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.rule_);
                return this;
            }

            public Builder clearRule() {
                this.rule_ = Collections.emptyList();
                this.bitField0_ &= -65;
                return this;
            }

            public Builder removeRule(int index) {
                ensureRuleIsMutable();
                this.rule_.remove(index);
                return this;
            }

            public boolean hasPreviewAuthCode() {
                return (this.bitField0_ & 128) == 128;
            }

            public String getPreviewAuthCode() {
                Object ref = this.previewAuthCode_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.previewAuthCode_ = s;
                return s;
            }

            public ByteString getPreviewAuthCodeBytes() {
                Object ref = this.previewAuthCode_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.previewAuthCode_ = b;
                return b;
            }

            public Builder setPreviewAuthCode(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 128;
                this.previewAuthCode_ = value;
                return this;
            }

            public Builder clearPreviewAuthCode() {
                this.bitField0_ &= -129;
                this.previewAuthCode_ = Resource.getDefaultInstance().getPreviewAuthCode();
                return this;
            }

            public Builder setPreviewAuthCodeBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 128;
                this.previewAuthCode_ = value;
                return this;
            }

            public boolean hasMalwareScanAuthCode() {
                return (this.bitField0_ & 256) == 256;
            }

            public String getMalwareScanAuthCode() {
                Object ref = this.malwareScanAuthCode_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.malwareScanAuthCode_ = s;
                return s;
            }

            public ByteString getMalwareScanAuthCodeBytes() {
                Object ref = this.malwareScanAuthCode_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.malwareScanAuthCode_ = b;
                return b;
            }

            public Builder setMalwareScanAuthCode(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 256;
                this.malwareScanAuthCode_ = value;
                return this;
            }

            public Builder clearMalwareScanAuthCode() {
                this.bitField0_ &= -257;
                this.malwareScanAuthCode_ = Resource.getDefaultInstance().getMalwareScanAuthCode();
                return this;
            }

            public Builder setMalwareScanAuthCodeBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 256;
                this.malwareScanAuthCode_ = value;
                return this;
            }

            public boolean hasTemplateVersionSet() {
                return (this.bitField0_ & 512) == 512;
            }

            public String getTemplateVersionSet() {
                Object ref = this.templateVersionSet_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.templateVersionSet_ = s;
                return s;
            }

            public ByteString getTemplateVersionSetBytes() {
                Object ref = this.templateVersionSet_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.templateVersionSet_ = b;
                return b;
            }

            public Builder setTemplateVersionSet(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 512;
                this.templateVersionSet_ = value;
                return this;
            }

            public Builder clearTemplateVersionSet() {
                this.bitField0_ &= -513;
                this.templateVersionSet_ = Resource.getDefaultInstance().getTemplateVersionSet();
                return this;
            }

            public Builder setTemplateVersionSetBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 512;
                this.templateVersionSet_ = value;
                return this;
            }

            public boolean hasVersion() {
                return (this.bitField0_ & 1024) == 1024;
            }

            public String getVersion() {
                Object ref = this.version_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.version_ = s;
                return s;
            }

            public ByteString getVersionBytes() {
                Object ref = this.version_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.version_ = b;
                return b;
            }

            public Builder setVersion(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1024;
                this.version_ = value;
                return this;
            }

            public Builder clearVersion() {
                this.bitField0_ &= -1025;
                this.version_ = Resource.getDefaultInstance().getVersion();
                return this;
            }

            public Builder setVersionBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1024;
                this.version_ = value;
                return this;
            }

            public boolean hasLiveJsCacheOption() {
                return (this.bitField0_ & 2048) == 2048;
            }

            public CacheOption getLiveJsCacheOption() {
                return this.liveJsCacheOption_;
            }

            public Builder setLiveJsCacheOption(CacheOption value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.liveJsCacheOption_ = value;
                this.bitField0_ |= 2048;
                return this;
            }

            public Builder setLiveJsCacheOption(CacheOption.Builder builderForValue) {
                this.liveJsCacheOption_ = builderForValue.build();
                this.bitField0_ |= 2048;
                return this;
            }

            public Builder mergeLiveJsCacheOption(CacheOption value) {
                if ((this.bitField0_ & 2048) != 2048 || this.liveJsCacheOption_ == CacheOption.getDefaultInstance()) {
                    this.liveJsCacheOption_ = value;
                } else {
                    this.liveJsCacheOption_ = CacheOption.newBuilder(this.liveJsCacheOption_).mergeFrom(value).buildPartial();
                }
                this.bitField0_ |= 2048;
                return this;
            }

            public Builder clearLiveJsCacheOption() {
                this.liveJsCacheOption_ = CacheOption.getDefaultInstance();
                this.bitField0_ &= -2049;
                return this;
            }

            public boolean hasReportingSampleRate() {
                return (this.bitField0_ & 4096) == 4096;
            }

            public float getReportingSampleRate() {
                return this.reportingSampleRate_;
            }

            public Builder setReportingSampleRate(float value) {
                this.bitField0_ |= 4096;
                this.reportingSampleRate_ = value;
                return this;
            }

            public Builder clearReportingSampleRate() {
                this.bitField0_ &= -4097;
                this.reportingSampleRate_ = 0.0f;
                return this;
            }

            public boolean hasEnableAutoEventTracking() {
                return (this.bitField0_ & 8192) == 8192;
            }

            public boolean getEnableAutoEventTracking() {
                return this.enableAutoEventTracking_;
            }

            public Builder setEnableAutoEventTracking(boolean value) {
                this.bitField0_ |= 8192;
                this.enableAutoEventTracking_ = value;
                return this;
            }

            public Builder clearEnableAutoEventTracking() {
                this.bitField0_ &= -8193;
                this.enableAutoEventTracking_ = false;
                return this;
            }

            private void ensureUsageContextIsMutable() {
                if ((this.bitField0_ & 16384) != 16384) {
                    this.usageContext_ = new LazyStringArrayList(this.usageContext_);
                    this.bitField0_ |= 16384;
                }
            }

            public List<String> getUsageContextList() {
                return Collections.unmodifiableList(this.usageContext_);
            }

            public int getUsageContextCount() {
                return this.usageContext_.size();
            }

            public String getUsageContext(int index) {
                return (String) this.usageContext_.get(index);
            }

            public ByteString getUsageContextBytes(int index) {
                return this.usageContext_.getByteString(index);
            }

            public Builder setUsageContext(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsageContextIsMutable();
                this.usageContext_.set(index, value);
                return this;
            }

            public Builder addUsageContext(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsageContextIsMutable();
                this.usageContext_.add(value);
                return this;
            }

            public Builder addAllUsageContext(Iterable<String> values) {
                ensureUsageContextIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.usageContext_);
                return this;
            }

            public Builder clearUsageContext() {
                this.usageContext_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -16385;
                return this;
            }

            public Builder addUsageContextBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsageContextIsMutable();
                this.usageContext_.add(value);
                return this;
            }

            public boolean hasResourceFormatVersion() {
                return (this.bitField0_ & 32768) == 32768;
            }

            public int getResourceFormatVersion() {
                return this.resourceFormatVersion_;
            }

            public Builder setResourceFormatVersion(int value) {
                this.bitField0_ |= 32768;
                this.resourceFormatVersion_ = value;
                return this;
            }

            public Builder clearResourceFormatVersion() {
                this.bitField0_ &= -32769;
                this.resourceFormatVersion_ = 0;
                return this;
            }
        }
    }

    public static final class OptionalResource extends GeneratedMessageLite implements OptionalResourceOrBuilder {
        public static Parser<OptionalResource> PARSER = new AbstractParser<OptionalResource>() {
            public OptionalResource parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new OptionalResource(input, extensionRegistry);
            }
        };
        public static final int RESOURCE_FIELD_NUMBER = 2;
        private static final OptionalResource defaultInstance = new OptionalResource(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Resource resource_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;

        private OptionalResource(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private OptionalResource(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static OptionalResource getDefaultInstance() {
            return defaultInstance;
        }

        public OptionalResource getDefaultInstanceForType() {
            return defaultInstance;
        }

        private OptionalResource(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                        case 18:
                            Resource.Builder subBuilder = (this.bitField0_ & 1) == 1 ? this.resource_.toBuilder() : null;
                            this.resource_ = (Resource) input.readMessage(Resource.PARSER, extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.resource_);
                                this.resource_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 1;
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

        public Parser<OptionalResource> getParserForType() {
            return PARSER;
        }

        public boolean hasResource() {
            return (this.bitField0_ & 1) == 1;
        }

        public Resource getResource() {
            return this.resource_;
        }

        private void initFields() {
            this.resource_ = Resource.getDefaultInstance();
        }

        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                if (isInitialized == 1) {
                    return true;
                }
                return false;
            } else if (!hasResource() || getResource().isInitialized()) {
                this.memoizedIsInitialized = 1;
                return true;
            } else {
                this.memoizedIsInitialized = 0;
                return false;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
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
                size2 = 0 + CodedOutputStream.computeMessageSize(2, this.resource_);
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
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof OptionalResource)) {
                return super.equals(obj);
            }
            OptionalResource other = (OptionalResource) obj;
            boolean result = 1 != 0 && hasResource() == other.hasResource();
            if (hasResource()) {
                result = result && getResource().equals(other.getResource());
            }
            return result;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = OptionalResource.class.hashCode() + 779;
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
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$OptionalResource");
            }
            return mutableDefault;
        }

        public static OptionalResource parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static OptionalResource parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static OptionalResource parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static OptionalResource parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static OptionalResource parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static OptionalResource parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static OptionalResource parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static OptionalResource parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static OptionalResource parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static OptionalResource parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(OptionalResource prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<OptionalResource, Builder> implements OptionalResourceOrBuilder {
            private int bitField0_;
            private Resource resource_ = Resource.getDefaultInstance();

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
                this.resource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public OptionalResource getDefaultInstanceForType() {
                return OptionalResource.getDefaultInstance();
            }

            public OptionalResource build() {
                OptionalResource result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public OptionalResource buildPartial() {
                OptionalResource result = new OptionalResource((GeneratedMessageLite.Builder) this);
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                Resource unused = result.resource_ = this.resource_;
                int unused2 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(OptionalResource other) {
                if (other != OptionalResource.getDefaultInstance()) {
                    if (other.hasResource()) {
                        mergeResource(other.getResource());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                if (!hasResource() || getResource().isInitialized()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    OptionalResource parsedMessage = OptionalResource.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    OptionalResource parsedMessage2 = (OptionalResource) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((OptionalResource) null);
                    }
                    throw th;
                }
            }

            public boolean hasResource() {
                return (this.bitField0_ & 1) == 1;
            }

            public Resource getResource() {
                return this.resource_;
            }

            public Builder setResource(Resource value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.resource_ = value;
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setResource(Resource.Builder builderForValue) {
                this.resource_ = builderForValue.build();
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeResource(Resource value) {
                if ((this.bitField0_ & 1) != 1 || this.resource_ == Resource.getDefaultInstance()) {
                    this.resource_ = value;
                } else {
                    this.resource_ = Resource.newBuilder(this.resource_).mergeFrom(value).buildPartial();
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearResource() {
                this.resource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }
        }
    }
}
