package com.google.analytics.midtier.proto.containertag;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.tagmanager.protobuf.AbstractMessageLite;
import com.google.tagmanager.protobuf.AbstractParser;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.InvalidProtocolBufferException;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TypeSystem {

    public interface ValueOrBuilder extends GeneratedMessageLite.ExtendableMessageOrBuilder<Value> {
        boolean getBoolean();

        boolean getContainsReferences();

        Value.Escaping getEscaping(int i);

        int getEscapingCount();

        List<Value.Escaping> getEscapingList();

        String getFunctionId();

        ByteString getFunctionIdBytes();

        long getInteger();

        Value getListItem(int i);

        int getListItemCount();

        List<Value> getListItemList();

        String getMacroReference();

        ByteString getMacroReferenceBytes();

        Value getMapKey(int i);

        int getMapKeyCount();

        List<Value> getMapKeyList();

        Value getMapValue(int i);

        int getMapValueCount();

        List<Value> getMapValueList();

        String getString();

        ByteString getStringBytes();

        Value getTemplateToken(int i);

        int getTemplateTokenCount();

        List<Value> getTemplateTokenList();

        Value.Type getType();

        boolean hasBoolean();

        boolean hasContainsReferences();

        boolean hasFunctionId();

        boolean hasInteger();

        boolean hasMacroReference();

        boolean hasString();

        boolean hasType();
    }

    private TypeSystem() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class Value extends GeneratedMessageLite.ExtendableMessage<Value> implements ValueOrBuilder {
        public static final int BOOLEAN_FIELD_NUMBER = 12;
        public static final int CONTAINS_REFERENCES_FIELD_NUMBER = 9;
        public static final int ESCAPING_FIELD_NUMBER = 10;
        public static final int FUNCTION_ID_FIELD_NUMBER = 7;
        public static final int INTEGER_FIELD_NUMBER = 8;
        public static final int LIST_ITEM_FIELD_NUMBER = 3;
        public static final int MACRO_REFERENCE_FIELD_NUMBER = 6;
        public static final int MAP_KEY_FIELD_NUMBER = 4;
        public static final int MAP_VALUE_FIELD_NUMBER = 5;
        public static Parser<Value> PARSER = new AbstractParser<Value>() {
            public Value parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Value(input, extensionRegistry);
            }
        };
        public static final int STRING_FIELD_NUMBER = 2;
        public static final int TEMPLATE_TOKEN_FIELD_NUMBER = 11;
        public static final int TYPE_FIELD_NUMBER = 1;
        private static final Value defaultInstance = new Value(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public boolean boolean_;
        /* access modifiers changed from: private */
        public boolean containsReferences_;
        /* access modifiers changed from: private */
        public List<Escaping> escaping_;
        /* access modifiers changed from: private */
        public Object functionId_;
        /* access modifiers changed from: private */
        public long integer_;
        /* access modifiers changed from: private */
        public List<Value> listItem_;
        /* access modifiers changed from: private */
        public Object macroReference_;
        /* access modifiers changed from: private */
        public List<Value> mapKey_;
        /* access modifiers changed from: private */
        public List<Value> mapValue_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Object string_;
        /* access modifiers changed from: private */
        public List<Value> templateToken_;
        /* access modifiers changed from: private */
        public Type type_;
        /* access modifiers changed from: private */
        public final ByteString unknownFields;

        private Value(GeneratedMessageLite.ExtendableBuilder<Value, ?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Value(boolean noInit) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Value getDefaultInstance() {
            return defaultInstance;
        }

        public Value getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Value(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                            int rawValue = input.readEnum();
                            Type value = Type.valueOf(rawValue);
                            if (value != null) {
                                this.bitField0_ |= 1;
                                this.type_ = value;
                                break;
                            } else {
                                unknownFieldsCodedOutput.writeRawVarint32(tag);
                                unknownFieldsCodedOutput.writeRawVarint32(rawValue);
                                break;
                            }
                        case 18:
                            ByteString bs = input.readBytes();
                            this.bitField0_ |= 2;
                            this.string_ = bs;
                            break;
                        case 26:
                            if ((mutable_bitField0_ & 4) != 4) {
                                this.listItem_ = new ArrayList();
                                mutable_bitField0_ |= 4;
                            }
                            this.listItem_.add(input.readMessage(PARSER, extensionRegistry));
                            break;
                        case 34:
                            if ((mutable_bitField0_ & 8) != 8) {
                                this.mapKey_ = new ArrayList();
                                mutable_bitField0_ |= 8;
                            }
                            this.mapKey_.add(input.readMessage(PARSER, extensionRegistry));
                            break;
                        case 42:
                            if ((mutable_bitField0_ & 16) != 16) {
                                this.mapValue_ = new ArrayList();
                                mutable_bitField0_ |= 16;
                            }
                            this.mapValue_.add(input.readMessage(PARSER, extensionRegistry));
                            break;
                        case 50:
                            ByteString bs2 = input.readBytes();
                            this.bitField0_ |= 4;
                            this.macroReference_ = bs2;
                            break;
                        case 58:
                            ByteString bs3 = input.readBytes();
                            this.bitField0_ |= 8;
                            this.functionId_ = bs3;
                            break;
                        case AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS:
                            this.bitField0_ |= 16;
                            this.integer_ = input.readInt64();
                            break;
                        case 72:
                            this.bitField0_ |= 64;
                            this.containsReferences_ = input.readBool();
                            break;
                        case 80:
                            int rawValue2 = input.readEnum();
                            Escaping value2 = Escaping.valueOf(rawValue2);
                            if (value2 != null) {
                                if ((mutable_bitField0_ & 1024) != 1024) {
                                    this.escaping_ = new ArrayList();
                                    mutable_bitField0_ |= 1024;
                                }
                                this.escaping_.add(value2);
                                break;
                            } else {
                                unknownFieldsCodedOutput.writeRawVarint32(tag);
                                unknownFieldsCodedOutput.writeRawVarint32(rawValue2);
                                break;
                            }
                        case 82:
                            int oldLimit = input.pushLimit(input.readRawVarint32());
                            while (input.getBytesUntilLimit() > 0) {
                                int rawValue3 = input.readEnum();
                                Escaping value3 = Escaping.valueOf(rawValue3);
                                if (value3 == null) {
                                    unknownFieldsCodedOutput.writeRawVarint32(tag);
                                    unknownFieldsCodedOutput.writeRawVarint32(rawValue3);
                                } else {
                                    if ((mutable_bitField0_ & 1024) != 1024) {
                                        this.escaping_ = new ArrayList();
                                        mutable_bitField0_ |= 1024;
                                    }
                                    this.escaping_.add(value3);
                                }
                            }
                            input.popLimit(oldLimit);
                            break;
                        case 90:
                            if ((mutable_bitField0_ & 512) != 512) {
                                this.templateToken_ = new ArrayList();
                                mutable_bitField0_ |= 512;
                            }
                            this.templateToken_.add(input.readMessage(PARSER, extensionRegistry));
                            break;
                        case 96:
                            this.bitField0_ |= 32;
                            this.boolean_ = input.readBool();
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
                    if ((mutable_bitField0_ & 4) == 4) {
                        this.listItem_ = Collections.unmodifiableList(this.listItem_);
                    }
                    if ((mutable_bitField0_ & 8) == 8) {
                        this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                    }
                    if ((mutable_bitField0_ & 16) == 16) {
                        this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                    }
                    if ((mutable_bitField0_ & 1024) == 1024) {
                        this.escaping_ = Collections.unmodifiableList(this.escaping_);
                    }
                    if ((mutable_bitField0_ & 512) == 512) {
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
            if ((mutable_bitField0_ & 4) == 4) {
                this.listItem_ = Collections.unmodifiableList(this.listItem_);
            }
            if ((mutable_bitField0_ & 8) == 8) {
                this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
            }
            if ((mutable_bitField0_ & 16) == 16) {
                this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
            }
            if ((mutable_bitField0_ & 1024) == 1024) {
                this.escaping_ = Collections.unmodifiableList(this.escaping_);
            }
            if ((mutable_bitField0_ & 512) == 512) {
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

        public Parser<Value> getParserForType() {
            return PARSER;
        }

        public enum Type implements Internal.EnumLite {
            STRING(0, 1),
            LIST(1, 2),
            MAP(2, 3),
            MACRO_REFERENCE(3, 4),
            FUNCTION_ID(4, 5),
            INTEGER(5, 6),
            TEMPLATE(6, 7),
            BOOLEAN(7, 8);
            
            public static final int BOOLEAN_VALUE = 8;
            public static final int FUNCTION_ID_VALUE = 5;
            public static final int INTEGER_VALUE = 6;
            public static final int LIST_VALUE = 2;
            public static final int MACRO_REFERENCE_VALUE = 4;
            public static final int MAP_VALUE = 3;
            public static final int STRING_VALUE = 1;
            public static final int TEMPLATE_VALUE = 7;
            private static Internal.EnumLiteMap<Type> internalValueMap;
            private final int value;

            static {
                internalValueMap = new Internal.EnumLiteMap<Type>() {
                    public Type findValueByNumber(int number) {
                        return Type.valueOf(number);
                    }
                };
            }

            public final int getNumber() {
                return this.value;
            }

            public static Type valueOf(int value2) {
                switch (value2) {
                    case 1:
                        return STRING;
                    case 2:
                        return LIST;
                    case 3:
                        return MAP;
                    case 4:
                        return MACRO_REFERENCE;
                    case 5:
                        return FUNCTION_ID;
                    case 6:
                        return INTEGER;
                    case 7:
                        return TEMPLATE;
                    case 8:
                        return BOOLEAN;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<Type> internalGetValueMap() {
                return internalValueMap;
            }

            private Type(int index, int value2) {
                this.value = value2;
            }
        }

        public enum Escaping implements Internal.EnumLite {
            ESCAPE_HTML(0, 1),
            ESCAPE_HTML_RCDATA(1, 2),
            ESCAPE_HTML_ATTRIBUTE(2, 3),
            ESCAPE_HTML_ATTRIBUTE_NOSPACE(3, 4),
            FILTER_HTML_ELEMENT_NAME(4, 5),
            FILTER_HTML_ATTRIBUTES(5, 6),
            ESCAPE_JS_STRING(6, 7),
            ESCAPE_JS_VALUE(7, 8),
            ESCAPE_JS_REGEX(8, 9),
            ESCAPE_CSS_STRING(9, 10),
            FILTER_CSS_VALUE(10, 11),
            ESCAPE_URI(11, 12),
            NORMALIZE_URI(12, 13),
            FILTER_NORMALIZE_URI(13, 14),
            NO_AUTOESCAPE(14, 15),
            TEXT(15, 17),
            CONVERT_JS_VALUE_TO_EXPRESSION(16, 16);
            
            public static final int CONVERT_JS_VALUE_TO_EXPRESSION_VALUE = 16;
            public static final int ESCAPE_CSS_STRING_VALUE = 10;
            public static final int ESCAPE_HTML_ATTRIBUTE_NOSPACE_VALUE = 4;
            public static final int ESCAPE_HTML_ATTRIBUTE_VALUE = 3;
            public static final int ESCAPE_HTML_RCDATA_VALUE = 2;
            public static final int ESCAPE_HTML_VALUE = 1;
            public static final int ESCAPE_JS_REGEX_VALUE = 9;
            public static final int ESCAPE_JS_STRING_VALUE = 7;
            public static final int ESCAPE_JS_VALUE_VALUE = 8;
            public static final int ESCAPE_URI_VALUE = 12;
            public static final int FILTER_CSS_VALUE_VALUE = 11;
            public static final int FILTER_HTML_ATTRIBUTES_VALUE = 6;
            public static final int FILTER_HTML_ELEMENT_NAME_VALUE = 5;
            public static final int FILTER_NORMALIZE_URI_VALUE = 14;
            public static final int NORMALIZE_URI_VALUE = 13;
            public static final int NO_AUTOESCAPE_VALUE = 15;
            public static final int TEXT_VALUE = 17;
            private static Internal.EnumLiteMap<Escaping> internalValueMap;
            private final int value;

            static {
                internalValueMap = new Internal.EnumLiteMap<Escaping>() {
                    public Escaping findValueByNumber(int number) {
                        return Escaping.valueOf(number);
                    }
                };
            }

            public final int getNumber() {
                return this.value;
            }

            public static Escaping valueOf(int value2) {
                switch (value2) {
                    case 1:
                        return ESCAPE_HTML;
                    case 2:
                        return ESCAPE_HTML_RCDATA;
                    case 3:
                        return ESCAPE_HTML_ATTRIBUTE;
                    case 4:
                        return ESCAPE_HTML_ATTRIBUTE_NOSPACE;
                    case 5:
                        return FILTER_HTML_ELEMENT_NAME;
                    case 6:
                        return FILTER_HTML_ATTRIBUTES;
                    case 7:
                        return ESCAPE_JS_STRING;
                    case 8:
                        return ESCAPE_JS_VALUE;
                    case 9:
                        return ESCAPE_JS_REGEX;
                    case 10:
                        return ESCAPE_CSS_STRING;
                    case 11:
                        return FILTER_CSS_VALUE;
                    case 12:
                        return ESCAPE_URI;
                    case 13:
                        return NORMALIZE_URI;
                    case 14:
                        return FILTER_NORMALIZE_URI;
                    case 15:
                        return NO_AUTOESCAPE;
                    case 16:
                        return CONVERT_JS_VALUE_TO_EXPRESSION;
                    case 17:
                        return TEXT;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<Escaping> internalGetValueMap() {
                return internalValueMap;
            }

            private Escaping(int index, int value2) {
                this.value = value2;
            }
        }

        public boolean hasType() {
            return (this.bitField0_ & 1) == 1;
        }

        public Type getType() {
            return this.type_;
        }

        public boolean hasString() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getString() {
            Object ref = this.string_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.string_ = s;
            }
            return s;
        }

        public ByteString getStringBytes() {
            Object ref = this.string_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.string_ = b;
            return b;
        }

        public List<Value> getListItemList() {
            return this.listItem_;
        }

        public List<? extends ValueOrBuilder> getListItemOrBuilderList() {
            return this.listItem_;
        }

        public int getListItemCount() {
            return this.listItem_.size();
        }

        public Value getListItem(int index) {
            return this.listItem_.get(index);
        }

        public ValueOrBuilder getListItemOrBuilder(int index) {
            return this.listItem_.get(index);
        }

        public List<Value> getMapKeyList() {
            return this.mapKey_;
        }

        public List<? extends ValueOrBuilder> getMapKeyOrBuilderList() {
            return this.mapKey_;
        }

        public int getMapKeyCount() {
            return this.mapKey_.size();
        }

        public Value getMapKey(int index) {
            return this.mapKey_.get(index);
        }

        public ValueOrBuilder getMapKeyOrBuilder(int index) {
            return this.mapKey_.get(index);
        }

        public List<Value> getMapValueList() {
            return this.mapValue_;
        }

        public List<? extends ValueOrBuilder> getMapValueOrBuilderList() {
            return this.mapValue_;
        }

        public int getMapValueCount() {
            return this.mapValue_.size();
        }

        public Value getMapValue(int index) {
            return this.mapValue_.get(index);
        }

        public ValueOrBuilder getMapValueOrBuilder(int index) {
            return this.mapValue_.get(index);
        }

        public boolean hasMacroReference() {
            return (this.bitField0_ & 4) == 4;
        }

        public String getMacroReference() {
            Object ref = this.macroReference_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.macroReference_ = s;
            }
            return s;
        }

        public ByteString getMacroReferenceBytes() {
            Object ref = this.macroReference_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.macroReference_ = b;
            return b;
        }

        public boolean hasFunctionId() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getFunctionId() {
            Object ref = this.functionId_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.functionId_ = s;
            }
            return s;
        }

        public ByteString getFunctionIdBytes() {
            Object ref = this.functionId_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.functionId_ = b;
            return b;
        }

        public boolean hasInteger() {
            return (this.bitField0_ & 16) == 16;
        }

        public long getInteger() {
            return this.integer_;
        }

        public boolean hasBoolean() {
            return (this.bitField0_ & 32) == 32;
        }

        public boolean getBoolean() {
            return this.boolean_;
        }

        public List<Value> getTemplateTokenList() {
            return this.templateToken_;
        }

        public List<? extends ValueOrBuilder> getTemplateTokenOrBuilderList() {
            return this.templateToken_;
        }

        public int getTemplateTokenCount() {
            return this.templateToken_.size();
        }

        public Value getTemplateToken(int index) {
            return this.templateToken_.get(index);
        }

        public ValueOrBuilder getTemplateTokenOrBuilder(int index) {
            return this.templateToken_.get(index);
        }

        public List<Escaping> getEscapingList() {
            return this.escaping_;
        }

        public int getEscapingCount() {
            return this.escaping_.size();
        }

        public Escaping getEscaping(int index) {
            return this.escaping_.get(index);
        }

        public boolean hasContainsReferences() {
            return (this.bitField0_ & 64) == 64;
        }

        public boolean getContainsReferences() {
            return this.containsReferences_;
        }

        private void initFields() {
            this.type_ = Type.STRING;
            this.string_ = "";
            this.listItem_ = Collections.emptyList();
            this.mapKey_ = Collections.emptyList();
            this.mapValue_ = Collections.emptyList();
            this.macroReference_ = "";
            this.functionId_ = "";
            this.integer_ = 0;
            this.boolean_ = false;
            this.templateToken_ = Collections.emptyList();
            this.escaping_ = Collections.emptyList();
            this.containsReferences_ = false;
        }

        public final boolean isInitialized() {
            boolean z = true;
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                if (isInitialized != 1) {
                    z = false;
                }
                return z;
            } else if (!hasType()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                for (int i = 0; i < getListItemCount(); i++) {
                    if (!getListItem(i).isInitialized()) {
                        this.memoizedIsInitialized = 0;
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getMapKeyCount(); i2++) {
                    if (!getMapKey(i2).isInitialized()) {
                        this.memoizedIsInitialized = 0;
                        return false;
                    }
                }
                for (int i3 = 0; i3 < getMapValueCount(); i3++) {
                    if (!getMapValue(i3).isInitialized()) {
                        this.memoizedIsInitialized = 0;
                        return false;
                    }
                }
                for (int i4 = 0; i4 < getTemplateTokenCount(); i4++) {
                    if (!getTemplateToken(i4).isInitialized()) {
                        this.memoizedIsInitialized = 0;
                        return false;
                    }
                }
                if (!extensionsAreInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            GeneratedMessageLite.ExtendableMessage<MessageType>.ExtensionWriter newExtensionWriter = newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                output.writeEnum(1, this.type_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(2, getStringBytes());
            }
            for (int i = 0; i < this.listItem_.size(); i++) {
                output.writeMessage(3, this.listItem_.get(i));
            }
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                output.writeMessage(4, this.mapKey_.get(i2));
            }
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                output.writeMessage(5, this.mapValue_.get(i3));
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeBytes(6, getMacroReferenceBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeBytes(7, getFunctionIdBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeInt64(8, this.integer_);
            }
            if ((this.bitField0_ & 64) == 64) {
                output.writeBool(9, this.containsReferences_);
            }
            for (int i4 = 0; i4 < this.escaping_.size(); i4++) {
                output.writeEnum(10, this.escaping_.get(i4).getNumber());
            }
            for (int i5 = 0; i5 < this.templateToken_.size(); i5++) {
                output.writeMessage(11, this.templateToken_.get(i5));
            }
            if ((this.bitField0_ & 32) == 32) {
                output.writeBool(12, this.boolean_);
            }
            newExtensionWriter.writeUntil(536870912, output);
            output.writeRawBytes(this.unknownFields);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.type_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(2, getStringBytes());
            }
            for (int i = 0; i < this.listItem_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.listItem_.get(i));
            }
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(4, this.mapKey_.get(i2));
            }
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(5, this.mapValue_.get(i3));
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeBytesSize(6, getMacroReferenceBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                size2 += CodedOutputStream.computeBytesSize(7, getFunctionIdBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                size2 += CodedOutputStream.computeInt64Size(8, this.integer_);
            }
            if ((this.bitField0_ & 64) == 64) {
                size2 += CodedOutputStream.computeBoolSize(9, this.containsReferences_);
            }
            int dataSize = 0;
            for (int i4 = 0; i4 < this.escaping_.size(); i4++) {
                dataSize += CodedOutputStream.computeEnumSizeNoTag(this.escaping_.get(i4).getNumber());
            }
            int size3 = size2 + dataSize + (this.escaping_.size() * 1);
            for (int i5 = 0; i5 < this.templateToken_.size(); i5++) {
                size3 += CodedOutputStream.computeMessageSize(11, this.templateToken_.get(i5));
            }
            if ((this.bitField0_ & 32) == 32) {
                size3 += CodedOutputStream.computeBoolSize(12, this.boolean_);
            }
            int size4 = size3 + extensionsSerializedSize() + this.unknownFields.size();
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
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Value)) {
                return super.equals(obj);
            }
            Value other = (Value) obj;
            boolean result12 = 1 != 0 && hasType() == other.hasType();
            if (hasType()) {
                result12 = result12 && getType() == other.getType();
            }
            if (!result12 || hasString() != other.hasString()) {
                result = false;
            } else {
                result = true;
            }
            if (hasString()) {
                if (!result || !getString().equals(other.getString())) {
                    result = false;
                } else {
                    result = true;
                }
            }
            if (!result || !getListItemList().equals(other.getListItemList())) {
                result2 = false;
            } else {
                result2 = true;
            }
            if (!result2 || !getMapKeyList().equals(other.getMapKeyList())) {
                result3 = false;
            } else {
                result3 = true;
            }
            if (!result3 || !getMapValueList().equals(other.getMapValueList())) {
                result4 = false;
            } else {
                result4 = true;
            }
            if (!result4 || hasMacroReference() != other.hasMacroReference()) {
                result5 = false;
            } else {
                result5 = true;
            }
            if (hasMacroReference()) {
                if (!result5 || !getMacroReference().equals(other.getMacroReference())) {
                    result5 = false;
                } else {
                    result5 = true;
                }
            }
            if (!result5 || hasFunctionId() != other.hasFunctionId()) {
                result6 = false;
            } else {
                result6 = true;
            }
            if (hasFunctionId()) {
                if (!result6 || !getFunctionId().equals(other.getFunctionId())) {
                    result6 = false;
                } else {
                    result6 = true;
                }
            }
            if (!result6 || hasInteger() != other.hasInteger()) {
                result7 = false;
            } else {
                result7 = true;
            }
            if (hasInteger()) {
                if (!result7 || getInteger() != other.getInteger()) {
                    result7 = false;
                } else {
                    result7 = true;
                }
            }
            if (!result7 || hasBoolean() != other.hasBoolean()) {
                result8 = false;
            } else {
                result8 = true;
            }
            if (hasBoolean()) {
                if (!result8 || getBoolean() != other.getBoolean()) {
                    result8 = false;
                } else {
                    result8 = true;
                }
            }
            if (!result8 || !getTemplateTokenList().equals(other.getTemplateTokenList())) {
                result9 = false;
            } else {
                result9 = true;
            }
            if (!result9 || !getEscapingList().equals(other.getEscapingList())) {
                result10 = false;
            } else {
                result10 = true;
            }
            if (!result10 || hasContainsReferences() != other.hasContainsReferences()) {
                result11 = false;
            } else {
                result11 = true;
            }
            if (hasContainsReferences()) {
                if (!result11 || getContainsReferences() != other.getContainsReferences()) {
                    result11 = false;
                } else {
                    result11 = true;
                }
            }
            return result11;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Value.class.hashCode() + 779;
            if (hasType()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashEnum(getType());
            }
            if (hasString()) {
                hash = (((hash * 37) + 2) * 53) + getString().hashCode();
            }
            if (getListItemCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getListItemList().hashCode();
            }
            if (getMapKeyCount() > 0) {
                hash = (((hash * 37) + 4) * 53) + getMapKeyList().hashCode();
            }
            if (getMapValueCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getMapValueList().hashCode();
            }
            if (hasMacroReference()) {
                hash = (((hash * 37) + 6) * 53) + getMacroReference().hashCode();
            }
            if (hasFunctionId()) {
                hash = (((hash * 37) + 7) * 53) + getFunctionId().hashCode();
            }
            if (hasInteger()) {
                hash = (((hash * 37) + 8) * 53) + Internal.hashLong(getInteger());
            }
            if (hasBoolean()) {
                hash = (((hash * 37) + 12) * 53) + Internal.hashBoolean(getBoolean());
            }
            if (getTemplateTokenCount() > 0) {
                hash = (((hash * 37) + 11) * 53) + getTemplateTokenList().hashCode();
            }
            if (getEscapingCount() > 0) {
                hash = (((hash * 37) + 10) * 53) + Internal.hashEnumList(getEscapingList());
            }
            if (hasContainsReferences()) {
                hash = (((hash * 37) + 9) * 53) + Internal.hashBoolean(getContainsReferences());
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.midtier.proto.containertag.MutableTypeSystem$Value");
            }
            return mutableDefault;
        }

        public static Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Value parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Value parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Value parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Value prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.ExtendableBuilder<Value, Builder> implements ValueOrBuilder {
            private int bitField0_;
            private boolean boolean_;
            private boolean containsReferences_;
            private List<Escaping> escaping_ = Collections.emptyList();
            private Object functionId_ = "";
            private long integer_;
            private List<Value> listItem_ = Collections.emptyList();
            private Object macroReference_ = "";
            private List<Value> mapKey_ = Collections.emptyList();
            private List<Value> mapValue_ = Collections.emptyList();
            private Object string_ = "";
            private List<Value> templateToken_ = Collections.emptyList();
            private Type type_ = Type.STRING;

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
                this.type_ = Type.STRING;
                this.bitField0_ &= -2;
                this.string_ = "";
                this.bitField0_ &= -3;
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.macroReference_ = "";
                this.bitField0_ &= -33;
                this.functionId_ = "";
                this.bitField0_ &= -65;
                this.integer_ = 0;
                this.bitField0_ &= -129;
                this.boolean_ = false;
                this.bitField0_ &= -257;
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -513;
                this.escaping_ = Collections.emptyList();
                this.bitField0_ &= -1025;
                this.containsReferences_ = false;
                this.bitField0_ &= -2049;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public Value getDefaultInstanceForType() {
                return Value.getDefaultInstance();
            }

            public Value build() {
                Value result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException(result);
            }

            public Value buildPartial() {
                Value result = new Value((GeneratedMessageLite.ExtendableBuilder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                Type unused = result.type_ = this.type_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                Object unused2 = result.string_ = this.string_;
                if ((this.bitField0_ & 4) == 4) {
                    this.listItem_ = Collections.unmodifiableList(this.listItem_);
                    this.bitField0_ &= -5;
                }
                List unused3 = result.listItem_ = this.listItem_;
                if ((this.bitField0_ & 8) == 8) {
                    this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                    this.bitField0_ &= -9;
                }
                List unused4 = result.mapKey_ = this.mapKey_;
                if ((this.bitField0_ & 16) == 16) {
                    this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                    this.bitField0_ &= -17;
                }
                List unused5 = result.mapValue_ = this.mapValue_;
                if ((from_bitField0_ & 32) == 32) {
                    to_bitField0_ |= 4;
                }
                Object unused6 = result.macroReference_ = this.macroReference_;
                if ((from_bitField0_ & 64) == 64) {
                    to_bitField0_ |= 8;
                }
                Object unused7 = result.functionId_ = this.functionId_;
                if ((from_bitField0_ & 128) == 128) {
                    to_bitField0_ |= 16;
                }
                long unused8 = result.integer_ = this.integer_;
                if ((from_bitField0_ & 256) == 256) {
                    to_bitField0_ |= 32;
                }
                boolean unused9 = result.boolean_ = this.boolean_;
                if ((this.bitField0_ & 512) == 512) {
                    this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
                    this.bitField0_ &= -513;
                }
                List unused10 = result.templateToken_ = this.templateToken_;
                if ((this.bitField0_ & 1024) == 1024) {
                    this.escaping_ = Collections.unmodifiableList(this.escaping_);
                    this.bitField0_ &= -1025;
                }
                List unused11 = result.escaping_ = this.escaping_;
                if ((from_bitField0_ & 2048) == 2048) {
                    to_bitField0_ |= 64;
                }
                boolean unused12 = result.containsReferences_ = this.containsReferences_;
                int unused13 = result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(Value other) {
                if (other != Value.getDefaultInstance()) {
                    if (other.hasType()) {
                        setType(other.getType());
                    }
                    if (other.hasString()) {
                        this.bitField0_ |= 2;
                        this.string_ = other.string_;
                    }
                    if (!other.listItem_.isEmpty()) {
                        if (this.listItem_.isEmpty()) {
                            this.listItem_ = other.listItem_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureListItemIsMutable();
                            this.listItem_.addAll(other.listItem_);
                        }
                    }
                    if (!other.mapKey_.isEmpty()) {
                        if (this.mapKey_.isEmpty()) {
                            this.mapKey_ = other.mapKey_;
                            this.bitField0_ &= -9;
                        } else {
                            ensureMapKeyIsMutable();
                            this.mapKey_.addAll(other.mapKey_);
                        }
                    }
                    if (!other.mapValue_.isEmpty()) {
                        if (this.mapValue_.isEmpty()) {
                            this.mapValue_ = other.mapValue_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureMapValueIsMutable();
                            this.mapValue_.addAll(other.mapValue_);
                        }
                    }
                    if (other.hasMacroReference()) {
                        this.bitField0_ |= 32;
                        this.macroReference_ = other.macroReference_;
                    }
                    if (other.hasFunctionId()) {
                        this.bitField0_ |= 64;
                        this.functionId_ = other.functionId_;
                    }
                    if (other.hasInteger()) {
                        setInteger(other.getInteger());
                    }
                    if (other.hasBoolean()) {
                        setBoolean(other.getBoolean());
                    }
                    if (!other.templateToken_.isEmpty()) {
                        if (this.templateToken_.isEmpty()) {
                            this.templateToken_ = other.templateToken_;
                            this.bitField0_ &= -513;
                        } else {
                            ensureTemplateTokenIsMutable();
                            this.templateToken_.addAll(other.templateToken_);
                        }
                    }
                    if (!other.escaping_.isEmpty()) {
                        if (this.escaping_.isEmpty()) {
                            this.escaping_ = other.escaping_;
                            this.bitField0_ &= -1025;
                        } else {
                            ensureEscapingIsMutable();
                            this.escaping_.addAll(other.escaping_);
                        }
                    }
                    if (other.hasContainsReferences()) {
                        setContainsReferences(other.getContainsReferences());
                    }
                    mergeExtensionFields(other);
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                if (!hasType()) {
                    return false;
                }
                for (int i = 0; i < getListItemCount(); i++) {
                    if (!getListItem(i).isInitialized()) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getMapKeyCount(); i2++) {
                    if (!getMapKey(i2).isInitialized()) {
                        return false;
                    }
                }
                for (int i3 = 0; i3 < getMapValueCount(); i3++) {
                    if (!getMapValue(i3).isInitialized()) {
                        return false;
                    }
                }
                for (int i4 = 0; i4 < getTemplateTokenCount(); i4++) {
                    if (!getTemplateToken(i4).isInitialized()) {
                        return false;
                    }
                }
                if (extensionsAreInitialized()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    Value parsedMessage = Value.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    Value parsedMessage2 = (Value) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((Value) null);
                    }
                    throw th;
                }
            }

            public boolean hasType() {
                return (this.bitField0_ & 1) == 1;
            }

            public Type getType() {
                return this.type_;
            }

            public Builder setType(Type value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.type_ = value;
                return this;
            }

            public Builder clearType() {
                this.bitField0_ &= -2;
                this.type_ = Type.STRING;
                return this;
            }

            public boolean hasString() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getString() {
                Object ref = this.string_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.string_ = s;
                return s;
            }

            public ByteString getStringBytes() {
                Object ref = this.string_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.string_ = b;
                return b;
            }

            public Builder setString(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.string_ = value;
                return this;
            }

            public Builder clearString() {
                this.bitField0_ &= -3;
                this.string_ = Value.getDefaultInstance().getString();
                return this;
            }

            public Builder setStringBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.string_ = value;
                return this;
            }

            private void ensureListItemIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.listItem_ = new ArrayList(this.listItem_);
                    this.bitField0_ |= 4;
                }
            }

            public List<Value> getListItemList() {
                return Collections.unmodifiableList(this.listItem_);
            }

            public int getListItemCount() {
                return this.listItem_.size();
            }

            public Value getListItem(int index) {
                return this.listItem_.get(index);
            }

            public Builder setListItem(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureListItemIsMutable();
                this.listItem_.set(index, value);
                return this;
            }

            public Builder setListItem(int index, Builder builderForValue) {
                ensureListItemIsMutable();
                this.listItem_.set(index, builderForValue.build());
                return this;
            }

            public Builder addListItem(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureListItemIsMutable();
                this.listItem_.add(value);
                return this;
            }

            public Builder addListItem(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureListItemIsMutable();
                this.listItem_.add(index, value);
                return this;
            }

            public Builder addListItem(Builder builderForValue) {
                ensureListItemIsMutable();
                this.listItem_.add(builderForValue.build());
                return this;
            }

            public Builder addListItem(int index, Builder builderForValue) {
                ensureListItemIsMutable();
                this.listItem_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllListItem(Iterable<? extends Value> values) {
                ensureListItemIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.listItem_);
                return this;
            }

            public Builder clearListItem() {
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            public Builder removeListItem(int index) {
                ensureListItemIsMutable();
                this.listItem_.remove(index);
                return this;
            }

            private void ensureMapKeyIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.mapKey_ = new ArrayList(this.mapKey_);
                    this.bitField0_ |= 8;
                }
            }

            public List<Value> getMapKeyList() {
                return Collections.unmodifiableList(this.mapKey_);
            }

            public int getMapKeyCount() {
                return this.mapKey_.size();
            }

            public Value getMapKey(int index) {
                return this.mapKey_.get(index);
            }

            public Builder setMapKey(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapKeyIsMutable();
                this.mapKey_.set(index, value);
                return this;
            }

            public Builder setMapKey(int index, Builder builderForValue) {
                ensureMapKeyIsMutable();
                this.mapKey_.set(index, builderForValue.build());
                return this;
            }

            public Builder addMapKey(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapKeyIsMutable();
                this.mapKey_.add(value);
                return this;
            }

            public Builder addMapKey(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapKeyIsMutable();
                this.mapKey_.add(index, value);
                return this;
            }

            public Builder addMapKey(Builder builderForValue) {
                ensureMapKeyIsMutable();
                this.mapKey_.add(builderForValue.build());
                return this;
            }

            public Builder addMapKey(int index, Builder builderForValue) {
                ensureMapKeyIsMutable();
                this.mapKey_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllMapKey(Iterable<? extends Value> values) {
                ensureMapKeyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapKey_);
                return this;
            }

            public Builder clearMapKey() {
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -9;
                return this;
            }

            public Builder removeMapKey(int index) {
                ensureMapKeyIsMutable();
                this.mapKey_.remove(index);
                return this;
            }

            private void ensureMapValueIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.mapValue_ = new ArrayList(this.mapValue_);
                    this.bitField0_ |= 16;
                }
            }

            public List<Value> getMapValueList() {
                return Collections.unmodifiableList(this.mapValue_);
            }

            public int getMapValueCount() {
                return this.mapValue_.size();
            }

            public Value getMapValue(int index) {
                return this.mapValue_.get(index);
            }

            public Builder setMapValue(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapValueIsMutable();
                this.mapValue_.set(index, value);
                return this;
            }

            public Builder setMapValue(int index, Builder builderForValue) {
                ensureMapValueIsMutable();
                this.mapValue_.set(index, builderForValue.build());
                return this;
            }

            public Builder addMapValue(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapValueIsMutable();
                this.mapValue_.add(value);
                return this;
            }

            public Builder addMapValue(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapValueIsMutable();
                this.mapValue_.add(index, value);
                return this;
            }

            public Builder addMapValue(Builder builderForValue) {
                ensureMapValueIsMutable();
                this.mapValue_.add(builderForValue.build());
                return this;
            }

            public Builder addMapValue(int index, Builder builderForValue) {
                ensureMapValueIsMutable();
                this.mapValue_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllMapValue(Iterable<? extends Value> values) {
                ensureMapValueIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapValue_);
                return this;
            }

            public Builder clearMapValue() {
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            public Builder removeMapValue(int index) {
                ensureMapValueIsMutable();
                this.mapValue_.remove(index);
                return this;
            }

            public boolean hasMacroReference() {
                return (this.bitField0_ & 32) == 32;
            }

            public String getMacroReference() {
                Object ref = this.macroReference_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.macroReference_ = s;
                return s;
            }

            public ByteString getMacroReferenceBytes() {
                Object ref = this.macroReference_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.macroReference_ = b;
                return b;
            }

            public Builder setMacroReference(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 32;
                this.macroReference_ = value;
                return this;
            }

            public Builder clearMacroReference() {
                this.bitField0_ &= -33;
                this.macroReference_ = Value.getDefaultInstance().getMacroReference();
                return this;
            }

            public Builder setMacroReferenceBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 32;
                this.macroReference_ = value;
                return this;
            }

            public boolean hasFunctionId() {
                return (this.bitField0_ & 64) == 64;
            }

            public String getFunctionId() {
                Object ref = this.functionId_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (!bs.isValidUtf8()) {
                    return s;
                }
                this.functionId_ = s;
                return s;
            }

            public ByteString getFunctionIdBytes() {
                Object ref = this.functionId_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.functionId_ = b;
                return b;
            }

            public Builder setFunctionId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 64;
                this.functionId_ = value;
                return this;
            }

            public Builder clearFunctionId() {
                this.bitField0_ &= -65;
                this.functionId_ = Value.getDefaultInstance().getFunctionId();
                return this;
            }

            public Builder setFunctionIdBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 64;
                this.functionId_ = value;
                return this;
            }

            public boolean hasInteger() {
                return (this.bitField0_ & 128) == 128;
            }

            public long getInteger() {
                return this.integer_;
            }

            public Builder setInteger(long value) {
                this.bitField0_ |= 128;
                this.integer_ = value;
                return this;
            }

            public Builder clearInteger() {
                this.bitField0_ &= -129;
                this.integer_ = 0;
                return this;
            }

            public boolean hasBoolean() {
                return (this.bitField0_ & 256) == 256;
            }

            public boolean getBoolean() {
                return this.boolean_;
            }

            public Builder setBoolean(boolean value) {
                this.bitField0_ |= 256;
                this.boolean_ = value;
                return this;
            }

            public Builder clearBoolean() {
                this.bitField0_ &= -257;
                this.boolean_ = false;
                return this;
            }

            private void ensureTemplateTokenIsMutable() {
                if ((this.bitField0_ & 512) != 512) {
                    this.templateToken_ = new ArrayList(this.templateToken_);
                    this.bitField0_ |= 512;
                }
            }

            public List<Value> getTemplateTokenList() {
                return Collections.unmodifiableList(this.templateToken_);
            }

            public int getTemplateTokenCount() {
                return this.templateToken_.size();
            }

            public Value getTemplateToken(int index) {
                return this.templateToken_.get(index);
            }

            public Builder setTemplateToken(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTemplateTokenIsMutable();
                this.templateToken_.set(index, value);
                return this;
            }

            public Builder setTemplateToken(int index, Builder builderForValue) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.set(index, builderForValue.build());
                return this;
            }

            public Builder addTemplateToken(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(value);
                return this;
            }

            public Builder addTemplateToken(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(index, value);
                return this;
            }

            public Builder addTemplateToken(Builder builderForValue) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(builderForValue.build());
                return this;
            }

            public Builder addTemplateToken(int index, Builder builderForValue) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllTemplateToken(Iterable<? extends Value> values) {
                ensureTemplateTokenIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.templateToken_);
                return this;
            }

            public Builder clearTemplateToken() {
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -513;
                return this;
            }

            public Builder removeTemplateToken(int index) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.remove(index);
                return this;
            }

            private void ensureEscapingIsMutable() {
                if ((this.bitField0_ & 1024) != 1024) {
                    this.escaping_ = new ArrayList(this.escaping_);
                    this.bitField0_ |= 1024;
                }
            }

            public List<Escaping> getEscapingList() {
                return Collections.unmodifiableList(this.escaping_);
            }

            public int getEscapingCount() {
                return this.escaping_.size();
            }

            public Escaping getEscaping(int index) {
                return this.escaping_.get(index);
            }

            public Builder setEscaping(int index, Escaping value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEscapingIsMutable();
                this.escaping_.set(index, value);
                return this;
            }

            public Builder addEscaping(Escaping value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEscapingIsMutable();
                this.escaping_.add(value);
                return this;
            }

            public Builder addAllEscaping(Iterable<? extends Escaping> values) {
                ensureEscapingIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.escaping_);
                return this;
            }

            public Builder clearEscaping() {
                this.escaping_ = Collections.emptyList();
                this.bitField0_ &= -1025;
                return this;
            }

            public boolean hasContainsReferences() {
                return (this.bitField0_ & 2048) == 2048;
            }

            public boolean getContainsReferences() {
                return this.containsReferences_;
            }

            public Builder setContainsReferences(boolean value) {
                this.bitField0_ |= 2048;
                this.containsReferences_ = value;
                return this;
            }

            public Builder clearContainsReferences() {
                this.bitField0_ &= -2049;
                this.containsReferences_ = false;
                return this;
            }
        }
    }
}
