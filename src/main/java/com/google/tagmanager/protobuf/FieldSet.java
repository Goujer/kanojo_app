package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.LazyField;
import com.google.tagmanager.protobuf.MessageLite;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class FieldSet<FieldDescriptorType extends FieldDescriptorLite<FieldDescriptorType>> {
    private static final FieldSet DEFAULT_INSTANCE = new FieldSet(true);
    private final SmallSortedMap<FieldDescriptorType, Object> fields = SmallSortedMap.newFieldMap(16);
    private boolean hasLazyField = false;
    private boolean isImmutable;

    public interface FieldDescriptorLite<T extends FieldDescriptorLite<T>> extends Comparable<T> {
        Internal.EnumLiteMap<?> getEnumType();

        WireFormat.JavaType getLiteJavaType();

        WireFormat.FieldType getLiteType();

        int getNumber();

        MessageLite.Builder internalMergeFrom(MessageLite.Builder builder, MessageLite messageLite);

        MutableMessageLite internalMergeFrom(MutableMessageLite mutableMessageLite, MutableMessageLite mutableMessageLite2);

        boolean isPacked();

        boolean isRepeated();
    }

    private FieldSet() {
    }

    private FieldSet(boolean dummy) {
        makeImmutable();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> newFieldSet() {
        return new FieldSet<>();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> emptySet() {
        return DEFAULT_INSTANCE;
    }

    public void makeImmutable() {
        if (!this.isImmutable) {
            this.fields.makeImmutable();
            this.isImmutable = true;
        }
    }

    public boolean isImmutable() {
        return this.isImmutable;
    }

    public FieldSet<FieldDescriptorType> clone() {
        FieldSet<FieldDescriptorType> clone = newFieldSet();
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            clone.setField((FieldDescriptorLite) entry.getKey(), entry.getValue());
        }
        for (Map.Entry<FieldDescriptorType, Object> entry2 : this.fields.getOverflowEntries()) {
            clone.setField((FieldDescriptorLite) entry2.getKey(), entry2.getValue());
        }
        clone.hasLazyField = this.hasLazyField;
        return clone;
    }

    private Object convertToImmutable(FieldDescriptorType descriptor, Object value) {
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            if (descriptor.isRepeated()) {
                List<Object> immutableMessages = new ArrayList<>();
                Iterator i$ = ((List) value).iterator();
                while (i$.hasNext()) {
                    immutableMessages.add(((MutableMessageLite) i$.next()).immutableCopy());
                }
                return immutableMessages;
            } else if (value instanceof LazyField) {
                return ((MutableMessageLite) ((LazyField) value).getValue()).immutableCopy();
            } else {
                return ((MutableMessageLite) value).immutableCopy();
            }
        } else if (descriptor.getLiteJavaType() != WireFormat.JavaType.BYTE_STRING) {
            return value;
        } else {
            if (!descriptor.isRepeated()) {
                return ByteString.copyFrom((byte[]) (byte[]) value);
            }
            List<Object> immutableFields = new ArrayList<>();
            for (Object mutableField : (List) value) {
                immutableFields.add(ByteString.copyFrom((byte[]) mutableField));
            }
            return immutableFields;
        }
    }

    private Object convertToMutable(FieldDescriptorType descriptor, Object value) {
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            if (descriptor.isRepeated()) {
                List<Object> mutableMessages = new ArrayList<>();
                Iterator i$ = ((List) value).iterator();
                while (i$.hasNext()) {
                    mutableMessages.add(((MessageLite) i$.next()).mutableCopy());
                }
                return mutableMessages;
            } else if (value instanceof LazyField) {
                return ((LazyField) value).getValue().mutableCopy();
            } else {
                return ((MessageLite) value).mutableCopy();
            }
        } else if (descriptor.getLiteJavaType() != WireFormat.JavaType.BYTE_STRING) {
            return value;
        } else {
            if (!descriptor.isRepeated()) {
                return ((ByteString) value).toByteArray();
            }
            List<Object> mutableFields = new ArrayList<>();
            Iterator i$2 = ((List) value).iterator();
            while (i$2.hasNext()) {
                mutableFields.add(((ByteString) i$2.next()).toByteArray());
            }
            return mutableFields;
        }
    }

    public FieldSet<FieldDescriptorType> cloneWithAllFieldsToImmutable() {
        FieldSet<FieldDescriptorType> clone = newFieldSet();
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            FieldDescriptorType descriptor = (FieldDescriptorLite) entry.getKey();
            clone.setField(descriptor, convertToImmutable(descriptor, entry.getValue()));
        }
        for (Map.Entry<FieldDescriptorType, Object> entry2 : this.fields.getOverflowEntries()) {
            FieldDescriptorType descriptor2 = (FieldDescriptorLite) entry2.getKey();
            clone.setField(descriptor2, convertToImmutable(descriptor2, entry2.getValue()));
        }
        clone.hasLazyField = false;
        return clone;
    }

    public FieldSet<FieldDescriptorType> cloneWithAllFieldsToMutable() {
        FieldSet<FieldDescriptorType> clone = newFieldSet();
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            FieldDescriptorType descriptor = (FieldDescriptorLite) entry.getKey();
            clone.setField(descriptor, convertToMutable(descriptor, entry.getValue()));
        }
        for (Map.Entry<FieldDescriptorType, Object> entry2 : this.fields.getOverflowEntries()) {
            FieldDescriptorType descriptor2 = (FieldDescriptorLite) entry2.getKey();
            clone.setField(descriptor2, convertToMutable(descriptor2, entry2.getValue()));
        }
        clone.hasLazyField = false;
        return clone;
    }

    public void clear() {
        this.fields.clear();
        this.hasLazyField = false;
    }

    public Map<FieldDescriptorType, Object> getAllFields() {
        if (this.hasLazyField) {
            SmallSortedMap<FieldDescriptorType, Object> result = SmallSortedMap.newFieldMap(16);
            for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
                cloneFieldEntry(result, this.fields.getArrayEntryAt(i));
            }
            for (Map.Entry<FieldDescriptorType, Object> entry : this.fields.getOverflowEntries()) {
                cloneFieldEntry(result, entry);
            }
            if (!this.fields.isImmutable()) {
                return result;
            }
            result.makeImmutable();
            return result;
        }
        return this.fields.isImmutable() ? this.fields : Collections.unmodifiableMap(this.fields);
    }

    private void cloneFieldEntry(Map<FieldDescriptorType, Object> map, Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType key = (FieldDescriptorLite) entry.getKey();
        Object value = entry.getValue();
        if (value instanceof LazyField) {
            map.put(key, ((LazyField) value).getValue());
        } else {
            map.put(key, value);
        }
    }

    public Iterator<Map.Entry<FieldDescriptorType, Object>> iterator() {
        if (this.hasLazyField) {
            return new LazyField.LazyIterator(this.fields.entrySet().iterator());
        }
        return this.fields.entrySet().iterator();
    }

    public boolean hasField(FieldDescriptorType descriptor) {
        if (!descriptor.isRepeated()) {
            return this.fields.get(descriptor) != null;
        }
        throw new IllegalArgumentException("hasField() can only be called on non-repeated fields.");
    }

    public Object getField(FieldDescriptorType descriptor) {
        Object o = this.fields.get(descriptor);
        if (o instanceof LazyField) {
            return ((LazyField) o).getValue();
        }
        return o;
    }

    public void setField(FieldDescriptorType descriptor, Object value) {
        if (!descriptor.isRepeated()) {
            verifyType(descriptor.getLiteType(), value);
        } else if (!(value instanceof List)) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        } else {
            List<Object> newList = new ArrayList<>();
            newList.addAll((List) value);
            for (Object element : newList) {
                verifyType(descriptor.getLiteType(), element);
            }
            value = newList;
        }
        if (value instanceof LazyField) {
            this.hasLazyField = true;
        }
        this.fields.put(descriptor, value);
    }

    public void clearField(FieldDescriptorType descriptor) {
        this.fields.remove(descriptor);
        if (this.fields.isEmpty()) {
            this.hasLazyField = false;
        }
    }

    public int getRepeatedFieldCount(FieldDescriptorType descriptor) {
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
        }
        Object value = getField(descriptor);
        if (value == null) {
            return 0;
        }
        return ((List) value).size();
    }

    public Object getRepeatedField(FieldDescriptorType descriptor, int index) {
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
        }
        Object value = getField(descriptor);
        if (value != null) {
            return ((List) value).get(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public void setRepeatedField(FieldDescriptorType descriptor, int index, Object value) {
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
        }
        Object list = getField(descriptor);
        if (list == null) {
            throw new IndexOutOfBoundsException();
        }
        verifyType(descriptor.getLiteType(), value);
        ((List) list).set(index, value);
    }

    public void addRepeatedField(FieldDescriptorType descriptor, Object value) {
        List<Object> list;
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("addRepeatedField() can only be called on repeated fields.");
        }
        verifyType(descriptor.getLiteType(), value);
        Object existingValue = getField(descriptor);
        if (existingValue == null) {
            list = new ArrayList<>();
            this.fields.put(descriptor, list);
        } else {
            list = (List) existingValue;
        }
        list.add(value);
    }

    private static void verifyType(WireFormat.FieldType type, Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        boolean isValid = false;
        switch (type.getJavaType()) {
            case INT:
                isValid = value instanceof Integer;
                break;
            case LONG:
                isValid = value instanceof Long;
                break;
            case FLOAT:
                isValid = value instanceof Float;
                break;
            case DOUBLE:
                isValid = value instanceof Double;
                break;
            case BOOLEAN:
                isValid = value instanceof Boolean;
                break;
            case STRING:
                isValid = value instanceof String;
                break;
            case BYTE_STRING:
                if (!(value instanceof ByteString) && !(value instanceof byte[])) {
                    isValid = false;
                    break;
                } else {
                    isValid = true;
                    break;
                }
                break;
            case ENUM:
                if (!(value instanceof Integer) && !(value instanceof Internal.EnumLite)) {
                    isValid = false;
                    break;
                } else {
                    isValid = true;
                    break;
                }
                break;
            case MESSAGE:
                if (!(value instanceof MessageLite) && !(value instanceof LazyField)) {
                    isValid = false;
                    break;
                } else {
                    isValid = true;
                    break;
                }
                break;
        }
        if (!isValid) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    public boolean isInitialized() {
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            if (!isInitialized(this.fields.getArrayEntryAt(i))) {
                return false;
            }
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.fields.getOverflowEntries()) {
            if (!isInitialized(entry)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInitialized(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType descriptor = (FieldDescriptorLite) entry.getKey();
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            if (descriptor.isRepeated()) {
                for (MessageLite element : (List) entry.getValue()) {
                    if (!element.isInitialized()) {
                        return false;
                    }
                }
            } else {
                Object value = entry.getValue();
                if (value instanceof MessageLite) {
                    if (!((MessageLite) value).isInitialized()) {
                        return false;
                    }
                } else if (value instanceof LazyField) {
                    return true;
                } else {
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
            }
        }
        return true;
    }

    static int getWireFormatForFieldType(WireFormat.FieldType type, boolean isPacked) {
        if (isPacked) {
            return 2;
        }
        return type.getWireType();
    }

    public void mergeFrom(FieldSet<FieldDescriptorType> other) {
        for (int i = 0; i < other.fields.getNumArrayEntries(); i++) {
            mergeFromField(other.fields.getArrayEntryAt(i));
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : other.fields.getOverflowEntries()) {
            mergeFromField(entry);
        }
    }

    private void mergeFromField(Map.Entry<FieldDescriptorType, Object> entry) {
        Object build;
        FieldDescriptorType descriptor = (FieldDescriptorLite) entry.getKey();
        Object otherValue = entry.getValue();
        if (otherValue instanceof LazyField) {
            otherValue = ((LazyField) otherValue).getValue();
        }
        if (descriptor.isRepeated()) {
            Object value = getField(descriptor);
            if (value == null) {
                this.fields.put(descriptor, new ArrayList((List) otherValue));
            } else {
                ((List) value).addAll((List) otherValue);
            }
        } else if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            Object value2 = getField(descriptor);
            if (value2 == null) {
                this.fields.put(descriptor, otherValue);
                return;
            }
            if (value2 instanceof MutableMessageLite) {
                build = descriptor.internalMergeFrom((MutableMessageLite) value2, (MutableMessageLite) otherValue);
            } else {
                build = descriptor.internalMergeFrom(((MessageLite) value2).toBuilder(), (MessageLite) otherValue).build();
            }
            this.fields.put(descriptor, build);
        } else {
            this.fields.put(descriptor, otherValue);
        }
    }

    public static Object readPrimitiveField(CodedInputStream input, WireFormat.FieldType type, boolean checkUtf8) throws IOException {
        switch (type) {
            case DOUBLE:
                return Double.valueOf(input.readDouble());
            case FLOAT:
                return Float.valueOf(input.readFloat());
            case INT64:
                return Long.valueOf(input.readInt64());
            case UINT64:
                return Long.valueOf(input.readUInt64());
            case INT32:
                return Integer.valueOf(input.readInt32());
            case FIXED64:
                return Long.valueOf(input.readFixed64());
            case FIXED32:
                return Integer.valueOf(input.readFixed32());
            case BOOL:
                return Boolean.valueOf(input.readBool());
            case STRING:
                if (checkUtf8) {
                    return input.readStringRequireUtf8();
                }
                return input.readString();
            case BYTES:
                return input.readBytes();
            case UINT32:
                return Integer.valueOf(input.readUInt32());
            case SFIXED32:
                return Integer.valueOf(input.readSFixed32());
            case SFIXED64:
                return Long.valueOf(input.readSFixed64());
            case SINT32:
                return Integer.valueOf(input.readSInt32());
            case SINT64:
                return Long.valueOf(input.readSInt64());
            case GROUP:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle nested groups.");
            case MESSAGE:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle embedded messages.");
            case ENUM:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle enums.");
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static Object readPrimitiveFieldForMutable(CodedInputStream input, WireFormat.FieldType type, boolean checkUtf8) throws IOException {
        if (type == WireFormat.FieldType.BYTES) {
            return input.readByteArray();
        }
        return readPrimitiveField(input, type, checkUtf8);
    }

    public void writeTo(CodedOutputStream output) throws IOException {
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            writeField((FieldDescriptorLite) entry.getKey(), entry.getValue(), output);
        }
        for (Map.Entry<FieldDescriptorType, Object> entry2 : this.fields.getOverflowEntries()) {
            writeField((FieldDescriptorLite) entry2.getKey(), entry2.getValue(), output);
        }
    }

    public void writeMessageSetTo(CodedOutputStream output) throws IOException {
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            writeMessageSetTo(this.fields.getArrayEntryAt(i), output);
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.fields.getOverflowEntries()) {
            writeMessageSetTo(entry, output);
        }
    }

    private void writeMessageSetTo(Map.Entry<FieldDescriptorType, Object> entry, CodedOutputStream output) throws IOException {
        FieldDescriptorType descriptor = (FieldDescriptorLite) entry.getKey();
        if (descriptor.getLiteJavaType() != WireFormat.JavaType.MESSAGE || descriptor.isRepeated() || descriptor.isPacked()) {
            writeField(descriptor, entry.getValue(), output);
        } else {
            output.writeMessageSetExtension(((FieldDescriptorLite) entry.getKey()).getNumber(), (MessageLite) entry.getValue());
        }
    }

    private static void writeElement(CodedOutputStream output, WireFormat.FieldType type, int number, Object value) throws IOException {
        if (type != WireFormat.FieldType.GROUP) {
            output.writeTag(number, getWireFormatForFieldType(type, false));
            writeElementNoTag(output, type, value);
        } else if (Internal.isProto1Group((MessageLite) value)) {
            output.writeTag(number, 3);
            output.writeGroupNoTag((MessageLite) value);
        } else {
            output.writeGroup(number, (MessageLite) value);
        }
    }

    private static void writeElementNoTag(CodedOutputStream output, WireFormat.FieldType type, Object value) throws IOException {
        switch (type) {
            case DOUBLE:
                output.writeDoubleNoTag(((Double) value).doubleValue());
                return;
            case FLOAT:
                output.writeFloatNoTag(((Float) value).floatValue());
                return;
            case INT64:
                output.writeInt64NoTag(((Long) value).longValue());
                return;
            case UINT64:
                output.writeUInt64NoTag(((Long) value).longValue());
                return;
            case INT32:
                output.writeInt32NoTag(((Integer) value).intValue());
                return;
            case FIXED64:
                output.writeFixed64NoTag(((Long) value).longValue());
                return;
            case FIXED32:
                output.writeFixed32NoTag(((Integer) value).intValue());
                return;
            case BOOL:
                output.writeBoolNoTag(((Boolean) value).booleanValue());
                return;
            case STRING:
                output.writeStringNoTag((String) value);
                return;
            case BYTES:
                if (value instanceof ByteString) {
                    output.writeBytesNoTag((ByteString) value);
                    return;
                } else {
                    output.writeByteArrayNoTag((byte[]) value);
                    return;
                }
            case UINT32:
                output.writeUInt32NoTag(((Integer) value).intValue());
                return;
            case SFIXED32:
                output.writeSFixed32NoTag(((Integer) value).intValue());
                return;
            case SFIXED64:
                output.writeSFixed64NoTag(((Long) value).longValue());
                return;
            case SINT32:
                output.writeSInt32NoTag(((Integer) value).intValue());
                return;
            case SINT64:
                output.writeSInt64NoTag(((Long) value).longValue());
                return;
            case GROUP:
                output.writeGroupNoTag((MessageLite) value);
                return;
            case MESSAGE:
                output.writeMessageNoTag((MessageLite) value);
                return;
            case ENUM:
                if (value instanceof Internal.EnumLite) {
                    output.writeEnumNoTag(((Internal.EnumLite) value).getNumber());
                    return;
                } else {
                    output.writeEnumNoTag(((Integer) value).intValue());
                    return;
                }
            default:
                return;
        }
    }

    public static void writeField(FieldDescriptorLite<?> descriptor, Object value, CodedOutputStream output) throws IOException {
        WireFormat.FieldType type = descriptor.getLiteType();
        int number = descriptor.getNumber();
        if (descriptor.isRepeated()) {
            List<?> valueList = (List) value;
            if (descriptor.isPacked()) {
                output.writeTag(number, 2);
                int dataSize = 0;
                for (Object element : valueList) {
                    dataSize += computeElementSizeNoTag(type, element);
                }
                output.writeRawVarint32(dataSize);
                for (Object element2 : valueList) {
                    writeElementNoTag(output, type, element2);
                }
                return;
            }
            for (Object element3 : valueList) {
                writeElement(output, type, number, element3);
            }
        } else if (value instanceof LazyField) {
            writeElement(output, type, number, ((LazyField) value).getValue());
        } else {
            writeElement(output, type, number, value);
        }
    }

    public int getSerializedSize() {
        int size = 0;
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            size += computeFieldSize((FieldDescriptorLite) entry.getKey(), entry.getValue());
        }
        for (Map.Entry<FieldDescriptorType, Object> entry2 : this.fields.getOverflowEntries()) {
            size += computeFieldSize((FieldDescriptorLite) entry2.getKey(), entry2.getValue());
        }
        return size;
    }

    public int getMessageSetSerializedSize() {
        int size = 0;
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            size += getMessageSetSerializedSize(this.fields.getArrayEntryAt(i));
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.fields.getOverflowEntries()) {
            size += getMessageSetSerializedSize(entry);
        }
        return size;
    }

    private int getMessageSetSerializedSize(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType descriptor = (FieldDescriptorLite) entry.getKey();
        Object value = entry.getValue();
        if (descriptor.getLiteJavaType() != WireFormat.JavaType.MESSAGE || descriptor.isRepeated() || descriptor.isPacked()) {
            return computeFieldSize(descriptor, value);
        }
        if (value instanceof LazyField) {
            return CodedOutputStream.computeLazyFieldMessageSetExtensionSize(((FieldDescriptorLite) entry.getKey()).getNumber(), (LazyField) value);
        }
        return CodedOutputStream.computeMessageSetExtensionSize(((FieldDescriptorLite) entry.getKey()).getNumber(), (MessageLite) value);
    }

    private static int computeElementSize(WireFormat.FieldType type, int number, Object value) {
        int tagSize = CodedOutputStream.computeTagSize(number);
        if (type == WireFormat.FieldType.GROUP && !Internal.isProto1Group((MessageLite) value)) {
            tagSize *= 2;
        }
        return computeElementSizeNoTag(type, value) + tagSize;
    }

    private static int computeElementSizeNoTag(WireFormat.FieldType type, Object value) {
        switch (type) {
            case DOUBLE:
                return CodedOutputStream.computeDoubleSizeNoTag(((Double) value).doubleValue());
            case FLOAT:
                return CodedOutputStream.computeFloatSizeNoTag(((Float) value).floatValue());
            case INT64:
                return CodedOutputStream.computeInt64SizeNoTag(((Long) value).longValue());
            case UINT64:
                return CodedOutputStream.computeUInt64SizeNoTag(((Long) value).longValue());
            case INT32:
                return CodedOutputStream.computeInt32SizeNoTag(((Integer) value).intValue());
            case FIXED64:
                return CodedOutputStream.computeFixed64SizeNoTag(((Long) value).longValue());
            case FIXED32:
                return CodedOutputStream.computeFixed32SizeNoTag(((Integer) value).intValue());
            case BOOL:
                return CodedOutputStream.computeBoolSizeNoTag(((Boolean) value).booleanValue());
            case STRING:
                return CodedOutputStream.computeStringSizeNoTag((String) value);
            case BYTES:
                if (value instanceof ByteString) {
                    return CodedOutputStream.computeBytesSizeNoTag((ByteString) value);
                }
                return CodedOutputStream.computeByteArraySizeNoTag((byte[]) value);
            case UINT32:
                return CodedOutputStream.computeUInt32SizeNoTag(((Integer) value).intValue());
            case SFIXED32:
                return CodedOutputStream.computeSFixed32SizeNoTag(((Integer) value).intValue());
            case SFIXED64:
                return CodedOutputStream.computeSFixed64SizeNoTag(((Long) value).longValue());
            case SINT32:
                return CodedOutputStream.computeSInt32SizeNoTag(((Integer) value).intValue());
            case SINT64:
                return CodedOutputStream.computeSInt64SizeNoTag(((Long) value).longValue());
            case GROUP:
                return CodedOutputStream.computeGroupSizeNoTag((MessageLite) value);
            case MESSAGE:
                if (value instanceof LazyField) {
                    return CodedOutputStream.computeLazyFieldSizeNoTag((LazyField) value);
                }
                return CodedOutputStream.computeMessageSizeNoTag((MessageLite) value);
            case ENUM:
                if (value instanceof Internal.EnumLite) {
                    return CodedOutputStream.computeEnumSizeNoTag(((Internal.EnumLite) value).getNumber());
                }
                return CodedOutputStream.computeEnumSizeNoTag(((Integer) value).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static int computeFieldSize(FieldDescriptorLite<?> descriptor, Object value) {
        WireFormat.FieldType type = descriptor.getLiteType();
        int number = descriptor.getNumber();
        if (!descriptor.isRepeated()) {
            return computeElementSize(type, number, value);
        }
        if (descriptor.isPacked()) {
            int dataSize = 0;
            for (Object element : (List) value) {
                dataSize += computeElementSizeNoTag(type, element);
            }
            return CodedOutputStream.computeTagSize(number) + dataSize + CodedOutputStream.computeRawVarint32Size(dataSize);
        }
        int size = 0;
        for (Object element2 : (List) value) {
            size += computeElementSize(type, number, element2);
        }
        return size;
    }
}
