package com.google.analytics.midtier.proto.containertag;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MutableTypeSystem {
    private MutableTypeSystem() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class Value extends GeneratedMutableMessageLite.ExtendableMutableMessage<Value> implements MutableMessageLite {
        public static final int BOOLEAN_FIELD_NUMBER = 12;
        public static final int CONTAINS_REFERENCES_FIELD_NUMBER = 9;
        public static final int ESCAPING_FIELD_NUMBER = 10;
        public static final int FUNCTION_ID_FIELD_NUMBER = 7;
        public static final int INTEGER_FIELD_NUMBER = 8;
        public static final int LIST_ITEM_FIELD_NUMBER = 3;
        public static final int MACRO_REFERENCE_FIELD_NUMBER = 6;
        public static final int MAP_KEY_FIELD_NUMBER = 4;
        public static final int MAP_VALUE_FIELD_NUMBER = 5;
        public static final int STRING_FIELD_NUMBER = 2;
        public static final int TEMPLATE_TOKEN_FIELD_NUMBER = 11;
        public static final int TYPE_FIELD_NUMBER = 1;
        private static final Value defaultInstance = new Value(true);
        public static Parser<Value> PARSER = AbstractMutableMessageLite.internalNewParserForType(defaultInstance);
        private static volatile MessageLite immutableDefault = null;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private boolean boolean_;
        private boolean containsReferences_;
        private List<Escaping> escaping_ = null;
        private Object functionId_ = Internal.EMPTY_BYTE_ARRAY;
        private long integer_;
        private List<Value> listItem_ = null;
        private Object macroReference_ = Internal.EMPTY_BYTE_ARRAY;
        private List<Value> mapKey_ = null;
        private List<Value> mapValue_ = null;
        private Object string_ = Internal.EMPTY_BYTE_ARRAY;
        private List<Value> templateToken_ = null;
        private Type type_ = Type.STRING;

        private Value() {
            initFields();
        }

        private Value(boolean noInit) {
        }

        public Value newMessageForType() {
            return new Value();
        }

        public static Value newMessage() {
            return new Value();
        }

        private void initFields() {
            this.type_ = Type.STRING;
        }

        public static Value getDefaultInstance() {
            return defaultInstance;
        }

        public final Value getDefaultInstanceForType() {
            return defaultInstance;
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

        public Value setType(Type value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 1;
            this.type_ = value;
            return this;
        }

        public Value clearType() {
            assertMutable();
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
            byte[] byteArray = (byte[]) ref;
            String s = Internal.toStringUtf8(byteArray);
            if (Internal.isValidUtf8(byteArray)) {
                this.string_ = s;
            }
            return s;
        }

        public byte[] getStringAsBytes() {
            Object ref = this.string_;
            if (!(ref instanceof String)) {
                return (byte[]) ref;
            }
            byte[] byteArray = Internal.toByteArray((String) ref);
            this.string_ = byteArray;
            return byteArray;
        }

        public Value setString(String value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 2;
            this.string_ = value;
            return this;
        }

        public Value setStringAsBytes(byte[] value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 2;
            this.string_ = value;
            return this;
        }

        public Value clearString() {
            assertMutable();
            this.bitField0_ &= -3;
            this.string_ = Internal.EMPTY_BYTE_ARRAY;
            return this;
        }

        private void ensureListItemInitialized() {
            if (this.listItem_ == null) {
                this.listItem_ = new ArrayList();
            }
        }

        public int getListItemCount() {
            if (this.listItem_ == null) {
                return 0;
            }
            return this.listItem_.size();
        }

        public List<Value> getListItemList() {
            if (this.listItem_ == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(this.listItem_);
        }

        public List<Value> getMutableListItemList() {
            assertMutable();
            ensureListItemInitialized();
            return this.listItem_;
        }

        public Value getListItem(int index) {
            return this.listItem_.get(index);
        }

        public Value getMutableListItem(int index) {
            return this.listItem_.get(index);
        }

        public Value addListItem() {
            assertMutable();
            ensureListItemInitialized();
            Value value = newMessage();
            this.listItem_.add(value);
            return value;
        }

        public Value addListItem(Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureListItemInitialized();
            this.listItem_.add(value);
            return this;
        }

        public Value addAllListItem(Iterable<? extends Value> values) {
            assertMutable();
            ensureListItemInitialized();
            AbstractMutableMessageLite.addAll(values, this.listItem_);
            return this;
        }

        public Value setListItem(int index, Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureListItemInitialized();
            this.listItem_.set(index, value);
            return this;
        }

        public Value clearListItem() {
            assertMutable();
            this.listItem_ = null;
            return this;
        }

        private void ensureMapKeyInitialized() {
            if (this.mapKey_ == null) {
                this.mapKey_ = new ArrayList();
            }
        }

        public int getMapKeyCount() {
            if (this.mapKey_ == null) {
                return 0;
            }
            return this.mapKey_.size();
        }

        public List<Value> getMapKeyList() {
            if (this.mapKey_ == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(this.mapKey_);
        }

        public List<Value> getMutableMapKeyList() {
            assertMutable();
            ensureMapKeyInitialized();
            return this.mapKey_;
        }

        public Value getMapKey(int index) {
            return this.mapKey_.get(index);
        }

        public Value getMutableMapKey(int index) {
            return this.mapKey_.get(index);
        }

        public Value addMapKey() {
            assertMutable();
            ensureMapKeyInitialized();
            Value value = newMessage();
            this.mapKey_.add(value);
            return value;
        }

        public Value addMapKey(Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureMapKeyInitialized();
            this.mapKey_.add(value);
            return this;
        }

        public Value addAllMapKey(Iterable<? extends Value> values) {
            assertMutable();
            ensureMapKeyInitialized();
            AbstractMutableMessageLite.addAll(values, this.mapKey_);
            return this;
        }

        public Value setMapKey(int index, Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureMapKeyInitialized();
            this.mapKey_.set(index, value);
            return this;
        }

        public Value clearMapKey() {
            assertMutable();
            this.mapKey_ = null;
            return this;
        }

        private void ensureMapValueInitialized() {
            if (this.mapValue_ == null) {
                this.mapValue_ = new ArrayList();
            }
        }

        public int getMapValueCount() {
            if (this.mapValue_ == null) {
                return 0;
            }
            return this.mapValue_.size();
        }

        public List<Value> getMapValueList() {
            if (this.mapValue_ == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(this.mapValue_);
        }

        public List<Value> getMutableMapValueList() {
            assertMutable();
            ensureMapValueInitialized();
            return this.mapValue_;
        }

        public Value getMapValue(int index) {
            return this.mapValue_.get(index);
        }

        public Value getMutableMapValue(int index) {
            return this.mapValue_.get(index);
        }

        public Value addMapValue() {
            assertMutable();
            ensureMapValueInitialized();
            Value value = newMessage();
            this.mapValue_.add(value);
            return value;
        }

        public Value addMapValue(Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureMapValueInitialized();
            this.mapValue_.add(value);
            return this;
        }

        public Value addAllMapValue(Iterable<? extends Value> values) {
            assertMutable();
            ensureMapValueInitialized();
            AbstractMutableMessageLite.addAll(values, this.mapValue_);
            return this;
        }

        public Value setMapValue(int index, Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureMapValueInitialized();
            this.mapValue_.set(index, value);
            return this;
        }

        public Value clearMapValue() {
            assertMutable();
            this.mapValue_ = null;
            return this;
        }

        public boolean hasMacroReference() {
            return (this.bitField0_ & 4) == 4;
        }

        public String getMacroReference() {
            Object ref = this.macroReference_;
            if (ref instanceof String) {
                return (String) ref;
            }
            byte[] byteArray = (byte[]) ref;
            String s = Internal.toStringUtf8(byteArray);
            if (Internal.isValidUtf8(byteArray)) {
                this.macroReference_ = s;
            }
            return s;
        }

        public byte[] getMacroReferenceAsBytes() {
            Object ref = this.macroReference_;
            if (!(ref instanceof String)) {
                return (byte[]) ref;
            }
            byte[] byteArray = Internal.toByteArray((String) ref);
            this.macroReference_ = byteArray;
            return byteArray;
        }

        public Value setMacroReference(String value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 4;
            this.macroReference_ = value;
            return this;
        }

        public Value setMacroReferenceAsBytes(byte[] value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 4;
            this.macroReference_ = value;
            return this;
        }

        public Value clearMacroReference() {
            assertMutable();
            this.bitField0_ &= -5;
            this.macroReference_ = Internal.EMPTY_BYTE_ARRAY;
            return this;
        }

        public boolean hasFunctionId() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getFunctionId() {
            Object ref = this.functionId_;
            if (ref instanceof String) {
                return (String) ref;
            }
            byte[] byteArray = (byte[]) ref;
            String s = Internal.toStringUtf8(byteArray);
            if (Internal.isValidUtf8(byteArray)) {
                this.functionId_ = s;
            }
            return s;
        }

        public byte[] getFunctionIdAsBytes() {
            Object ref = this.functionId_;
            if (!(ref instanceof String)) {
                return (byte[]) ref;
            }
            byte[] byteArray = Internal.toByteArray((String) ref);
            this.functionId_ = byteArray;
            return byteArray;
        }

        public Value setFunctionId(String value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 8;
            this.functionId_ = value;
            return this;
        }

        public Value setFunctionIdAsBytes(byte[] value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 8;
            this.functionId_ = value;
            return this;
        }

        public Value clearFunctionId() {
            assertMutable();
            this.bitField0_ &= -9;
            this.functionId_ = Internal.EMPTY_BYTE_ARRAY;
            return this;
        }

        public boolean hasInteger() {
            return (this.bitField0_ & 16) == 16;
        }

        public long getInteger() {
            return this.integer_;
        }

        public Value setInteger(long value) {
            assertMutable();
            this.bitField0_ |= 16;
            this.integer_ = value;
            return this;
        }

        public Value clearInteger() {
            assertMutable();
            this.bitField0_ &= -17;
            this.integer_ = 0;
            return this;
        }

        public boolean hasBoolean() {
            return (this.bitField0_ & 32) == 32;
        }

        public boolean getBoolean() {
            return this.boolean_;
        }

        public Value setBoolean(boolean value) {
            assertMutable();
            this.bitField0_ |= 32;
            this.boolean_ = value;
            return this;
        }

        public Value clearBoolean() {
            assertMutable();
            this.bitField0_ &= -33;
            this.boolean_ = false;
            return this;
        }

        private void ensureTemplateTokenInitialized() {
            if (this.templateToken_ == null) {
                this.templateToken_ = new ArrayList();
            }
        }

        public int getTemplateTokenCount() {
            if (this.templateToken_ == null) {
                return 0;
            }
            return this.templateToken_.size();
        }

        public List<Value> getTemplateTokenList() {
            if (this.templateToken_ == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(this.templateToken_);
        }

        public List<Value> getMutableTemplateTokenList() {
            assertMutable();
            ensureTemplateTokenInitialized();
            return this.templateToken_;
        }

        public Value getTemplateToken(int index) {
            return this.templateToken_.get(index);
        }

        public Value getMutableTemplateToken(int index) {
            return this.templateToken_.get(index);
        }

        public Value addTemplateToken() {
            assertMutable();
            ensureTemplateTokenInitialized();
            Value value = newMessage();
            this.templateToken_.add(value);
            return value;
        }

        public Value addTemplateToken(Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureTemplateTokenInitialized();
            this.templateToken_.add(value);
            return this;
        }

        public Value addAllTemplateToken(Iterable<? extends Value> values) {
            assertMutable();
            ensureTemplateTokenInitialized();
            AbstractMutableMessageLite.addAll(values, this.templateToken_);
            return this;
        }

        public Value setTemplateToken(int index, Value value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureTemplateTokenInitialized();
            this.templateToken_.set(index, value);
            return this;
        }

        public Value clearTemplateToken() {
            assertMutable();
            this.templateToken_ = null;
            return this;
        }

        private void ensureEscapingInitialized() {
            if (this.escaping_ == null) {
                this.escaping_ = new ArrayList();
            }
        }

        public List<Escaping> getEscapingList() {
            if (this.escaping_ == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(this.escaping_);
        }

        public List<Escaping> getMutableEscapingList() {
            assertMutable();
            ensureEscapingInitialized();
            return this.escaping_;
        }

        public int getEscapingCount() {
            if (this.escaping_ == null) {
                return 0;
            }
            return this.escaping_.size();
        }

        public Escaping getEscaping(int index) {
            return this.escaping_.get(index);
        }

        public Value setEscaping(int index, Escaping value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureEscapingInitialized();
            this.escaping_.set(index, value);
            return this;
        }

        public Value addEscaping(Escaping value) {
            assertMutable();
            if (value == null) {
                throw new NullPointerException();
            }
            ensureEscapingInitialized();
            this.escaping_.add(value);
            return this;
        }

        public Value addAllEscaping(Iterable<? extends Escaping> values) {
            assertMutable();
            ensureEscapingInitialized();
            AbstractMutableMessageLite.addAll(values, this.escaping_);
            return this;
        }

        public Value clearEscaping() {
            assertMutable();
            this.escaping_ = null;
            return this;
        }

        public boolean hasContainsReferences() {
            return (this.bitField0_ & 64) == 64;
        }

        public boolean getContainsReferences() {
            return this.containsReferences_;
        }

        public Value setContainsReferences(boolean value) {
            assertMutable();
            this.bitField0_ |= 64;
            this.containsReferences_ = value;
            return this;
        }

        public Value clearContainsReferences() {
            assertMutable();
            this.bitField0_ &= -65;
            this.containsReferences_ = false;
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

        public Value clone() {
            return newMessageForType().mergeFrom(this);
        }

        public Value mergeFrom(Value other) {
            if (this == other) {
                throw new IllegalArgumentException("mergeFrom(message) called on the same message.");
            }
            assertMutable();
            if (other != getDefaultInstance()) {
                if (other.hasType()) {
                    setType(other.getType());
                }
                if (other.hasString()) {
                    this.bitField0_ |= 2;
                    if (other.string_ instanceof String) {
                        this.string_ = other.string_;
                    } else {
                        byte[] ba = (byte[]) other.string_;
                        this.string_ = Arrays.copyOf(ba, ba.length);
                    }
                }
                if (other.listItem_ != null && !other.listItem_.isEmpty()) {
                    ensureListItemInitialized();
                    AbstractMutableMessageLite.addAll(other.listItem_, this.listItem_);
                }
                if (other.mapKey_ != null && !other.mapKey_.isEmpty()) {
                    ensureMapKeyInitialized();
                    AbstractMutableMessageLite.addAll(other.mapKey_, this.mapKey_);
                }
                if (other.mapValue_ != null && !other.mapValue_.isEmpty()) {
                    ensureMapValueInitialized();
                    AbstractMutableMessageLite.addAll(other.mapValue_, this.mapValue_);
                }
                if (other.hasMacroReference()) {
                    this.bitField0_ |= 4;
                    if (other.macroReference_ instanceof String) {
                        this.macroReference_ = other.macroReference_;
                    } else {
                        byte[] ba2 = (byte[]) other.macroReference_;
                        this.macroReference_ = Arrays.copyOf(ba2, ba2.length);
                    }
                }
                if (other.hasFunctionId()) {
                    this.bitField0_ |= 8;
                    if (other.functionId_ instanceof String) {
                        this.functionId_ = other.functionId_;
                    } else {
                        byte[] ba3 = (byte[]) other.functionId_;
                        this.functionId_ = Arrays.copyOf(ba3, ba3.length);
                    }
                }
                if (other.hasInteger()) {
                    setInteger(other.getInteger());
                }
                if (other.hasContainsReferences()) {
                    setContainsReferences(other.getContainsReferences());
                }
                if (other.escaping_ != null && !other.escaping_.isEmpty()) {
                    ensureEscapingInitialized();
                    this.escaping_.addAll(other.escaping_);
                }
                if (other.templateToken_ != null && !other.templateToken_.isEmpty()) {
                    ensureTemplateTokenInitialized();
                    AbstractMutableMessageLite.addAll(other.templateToken_, this.templateToken_);
                }
                if (other.hasBoolean()) {
                    setBoolean(other.getBoolean());
                }
                mergeExtensionFields(other);
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
                            this.bitField0_ |= 2;
                            this.string_ = input.readByteArray();
                            break;
                        case 26:
                            input.readMessage((MutableMessageLite) addListItem(), extensionRegistry);
                            break;
                        case 34:
                            input.readMessage((MutableMessageLite) addMapKey(), extensionRegistry);
                            break;
                        case 42:
                            input.readMessage((MutableMessageLite) addMapValue(), extensionRegistry);
                            break;
                        case 50:
                            this.bitField0_ |= 4;
                            this.macroReference_ = input.readByteArray();
                            break;
                        case 58:
                            this.bitField0_ |= 8;
                            this.functionId_ = input.readByteArray();
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
                                if (this.escaping_ == null) {
                                    this.escaping_ = new ArrayList();
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
                                    if (this.escaping_ == null) {
                                        this.escaping_ = new ArrayList();
                                    }
                                    this.escaping_.add(value3);
                                }
                            }
                            input.popLimit(oldLimit);
                            break;
                        case 90:
                            input.readMessage((MutableMessageLite) addTemplateToken(), extensionRegistry);
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
            GeneratedMutableMessageLite.ExtendableMutableMessage.ExtensionWriter newExtensionWriter = newExtensionWriter();
            output.writeEnum(1, this.type_.getNumber());
            if ((this.bitField0_ & 2) == 2) {
                output.writeByteArray(2, getStringAsBytes());
            }
            if (this.listItem_ != null) {
                for (int i = 0; i < this.listItem_.size(); i++) {
                    output.writeMessageWithCachedSizes(3, this.listItem_.get(i));
                }
            }
            if (this.mapKey_ != null) {
                for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                    output.writeMessageWithCachedSizes(4, this.mapKey_.get(i2));
                }
            }
            if (this.mapValue_ != null) {
                for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                    output.writeMessageWithCachedSizes(5, this.mapValue_.get(i3));
                }
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeByteArray(6, getMacroReferenceAsBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeByteArray(7, getFunctionIdAsBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeInt64(8, this.integer_);
            }
            if ((this.bitField0_ & 64) == 64) {
                output.writeBool(9, this.containsReferences_);
            }
            if (this.escaping_ != null) {
                for (int i4 = 0; i4 < this.escaping_.size(); i4++) {
                    output.writeEnum(10, this.escaping_.get(i4).getNumber());
                }
            }
            if (this.templateToken_ != null) {
                for (int i5 = 0; i5 < this.templateToken_.size(); i5++) {
                    output.writeMessageWithCachedSizes(11, this.templateToken_.get(i5));
                }
            }
            if ((this.bitField0_ & 32) == 32) {
                output.writeBool(12, this.boolean_);
            }
            newExtensionWriter.writeUntil(536870912, output);
            output.writeRawBytes(this.unknownFields);
            if (getCachedSize() != output.getTotalBytesWritten() - bytesWrittenBefore) {
                throw new RuntimeException("Serialized size doesn't match cached size. You may forget to call getSerializedSize() or the message is being modified concurrently.");
            }
        }

        public int getSerializedSize() {
            int size = 0 + CodedOutputStream.computeEnumSize(1, this.type_.getNumber());
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeByteArraySize(2, getStringAsBytes());
            }
            if (this.listItem_ != null) {
                for (int i = 0; i < this.listItem_.size(); i++) {
                    size += CodedOutputStream.computeMessageSize(3, this.listItem_.get(i));
                }
            }
            if (this.mapKey_ != null) {
                for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                    size += CodedOutputStream.computeMessageSize(4, this.mapKey_.get(i2));
                }
            }
            if (this.mapValue_ != null) {
                for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                    size += CodedOutputStream.computeMessageSize(5, this.mapValue_.get(i3));
                }
            }
            if ((this.bitField0_ & 4) == 4) {
                size += CodedOutputStream.computeByteArraySize(6, getMacroReferenceAsBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                size += CodedOutputStream.computeByteArraySize(7, getFunctionIdAsBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                size += CodedOutputStream.computeInt64Size(8, this.integer_);
            }
            if ((this.bitField0_ & 32) == 32) {
                size += CodedOutputStream.computeBoolSize(12, this.boolean_);
            }
            if (this.templateToken_ != null) {
                for (int i4 = 0; i4 < this.templateToken_.size(); i4++) {
                    size += CodedOutputStream.computeMessageSize(11, this.templateToken_.get(i4));
                }
            }
            if (this.escaping_ != null && this.escaping_.size() > 0) {
                int dataSize = 0;
                for (int i5 = 0; i5 < this.escaping_.size(); i5++) {
                    dataSize += CodedOutputStream.computeEnumSizeNoTag(this.escaping_.get(i5).getNumber());
                }
                size = size + dataSize + (this.escaping_.size() * 1);
            }
            if ((this.bitField0_ & 64) == 64) {
                size += CodedOutputStream.computeBoolSize(9, this.containsReferences_);
            }
            int size2 = size + extensionsSerializedSize() + this.unknownFields.size();
            this.cachedSize = size2;
            return size2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public Value clear() {
            assertMutable();
            super.clear();
            this.type_ = Type.STRING;
            this.bitField0_ &= -2;
            this.string_ = Internal.EMPTY_BYTE_ARRAY;
            this.bitField0_ &= -3;
            this.listItem_ = null;
            this.mapKey_ = null;
            this.mapValue_ = null;
            this.macroReference_ = Internal.EMPTY_BYTE_ARRAY;
            this.bitField0_ &= -5;
            this.functionId_ = Internal.EMPTY_BYTE_ARRAY;
            this.bitField0_ &= -9;
            this.integer_ = 0;
            this.bitField0_ &= -17;
            this.boolean_ = false;
            this.bitField0_ &= -33;
            this.templateToken_ = null;
            this.escaping_ = null;
            this.containsReferences_ = false;
            this.bitField0_ &= -65;
            return this;
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
            int hash = 41;
            if (hasType()) {
                int hash2 = 1517 + 1;
                hash = 80454 + Internal.hashEnum(getType());
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
            return (hash * 29) + this.unknownFields.hashCode();
        }

        static {
            defaultInstance.initFields();
            defaultInstance.makeImmutable();
        }

        /* access modifiers changed from: protected */
        public MessageLite internalImmutableDefault() {
            if (immutableDefault == null) {
                immutableDefault = internalImmutableDefault("com.google.analytics.midtier.proto.containertag.TypeSystem$Value");
            }
            return immutableDefault;
        }
    }
}
