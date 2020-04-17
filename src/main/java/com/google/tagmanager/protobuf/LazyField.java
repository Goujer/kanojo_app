package com.google.tagmanager.protobuf;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

class LazyField {
    private ByteString bytes;
    private final MessageLite defaultInstance;
    private final ExtensionRegistryLite extensionRegistry;
    private volatile boolean isDirty = false;
    private volatile MessageLite value;

    public LazyField(MessageLite defaultInstance2, ExtensionRegistryLite extensionRegistry2, ByteString bytes2) {
        this.defaultInstance = defaultInstance2;
        this.extensionRegistry = extensionRegistry2;
        this.bytes = bytes2;
    }

    public MessageLite getValue() {
        ensureInitialized();
        return this.value;
    }

    public MessageLite setValue(MessageLite value2) {
        MessageLite originalValue = this.value;
        this.value = value2;
        this.bytes = null;
        this.isDirty = true;
        return originalValue;
    }

    public int getSerializedSize() {
        if (this.isDirty) {
            return this.value.getSerializedSize();
        }
        return this.bytes.size();
    }

    public ByteString toByteString() {
        if (!this.isDirty) {
            return this.bytes;
        }
        synchronized (this) {
            if (!this.isDirty) {
                ByteString byteString = this.bytes;
                return byteString;
            }
            this.bytes = this.value.toByteString();
            this.isDirty = false;
            ByteString byteString2 = this.bytes;
            return byteString2;
        }
    }

    public int hashCode() {
        ensureInitialized();
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        ensureInitialized();
        return this.value.equals(obj);
    }

    public String toString() {
        ensureInitialized();
        return this.value.toString();
    }

    private void ensureInitialized() {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null) {
                    try {
                        if (this.bytes != null) {
                            this.value = (MessageLite) this.defaultInstance.getParserForType().parseFrom(this.bytes, this.extensionRegistry);
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    static class LazyEntry<K> implements Map.Entry<K, Object> {
        private Map.Entry<K, LazyField> entry;

        private LazyEntry(Map.Entry<K, LazyField> entry2) {
            this.entry = entry2;
        }

        public K getKey() {
            return this.entry.getKey();
        }

        public Object getValue() {
            LazyField field = this.entry.getValue();
            if (field == null) {
                return null;
            }
            return field.getValue();
        }

        public LazyField getField() {
            return this.entry.getValue();
        }

        public Object setValue(Object value) {
            if (value instanceof MessageLite) {
                return this.entry.getValue().setValue((MessageLite) value);
            }
            throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
        }
    }

    static class LazyIterator<K> implements Iterator<Map.Entry<K, Object>> {
        private Iterator<Map.Entry<K, Object>> iterator;

        public LazyIterator(Iterator<Map.Entry<K, Object>> iterator2) {
            this.iterator = iterator2;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Map.Entry<K, Object> next() {
            Map.Entry<K, ?> entry = this.iterator.next();
            if (entry.getValue() instanceof LazyField) {
                return new LazyEntry<>(entry);
            }
            return entry;
        }

        public void remove() {
            this.iterator.remove();
        }
    }
}
