package org.apache.james.mime4j.codec;

import java.util.Iterator;
import java.util.NoSuchElementException;

class UnboundedFifoByteBuffer {
    protected byte[] buffer;
    protected int head;
    protected int tail;

    public UnboundedFifoByteBuffer() {
        this(32);
    }

    public UnboundedFifoByteBuffer(int initialSize) {
        if (initialSize <= 0) {
            throw new IllegalArgumentException("The size must be greater than 0");
        }
        this.buffer = new byte[(initialSize + 1)];
        this.head = 0;
        this.tail = 0;
    }

    public int size() {
        if (this.tail < this.head) {
            return (this.buffer.length - this.head) + this.tail;
        }
        return this.tail - this.head;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean add(byte b) {
        if (size() + 1 >= this.buffer.length) {
            byte[] tmp = new byte[(((this.buffer.length - 1) * 2) + 1)];
            int j = 0;
            int i = this.head;
            while (i != this.tail) {
                tmp[j] = this.buffer[i];
                this.buffer[i] = 0;
                j++;
                i++;
                if (i == this.buffer.length) {
                    i = 0;
                }
            }
            this.buffer = tmp;
            this.head = 0;
            this.tail = j;
        }
        this.buffer[this.tail] = b;
        this.tail++;
        if (this.tail < this.buffer.length) {
            return true;
        }
        this.tail = 0;
        return true;
    }

    public byte get() {
        if (!isEmpty()) {
            return this.buffer[this.head];
        }
        throw new IllegalStateException("The buffer is already empty");
    }

    public byte remove() {
        if (isEmpty()) {
            throw new IllegalStateException("The buffer is already empty");
        }
        byte element = this.buffer[this.head];
        this.head++;
        if (this.head >= this.buffer.length) {
            this.head = 0;
        }
        return element;
    }

    /* access modifiers changed from: private */
    public int increment(int index) {
        int index2 = index + 1;
        if (index2 >= this.buffer.length) {
            return 0;
        }
        return index2;
    }

    /* access modifiers changed from: private */
    public int decrement(int index) {
        int index2 = index - 1;
        if (index2 < 0) {
            return this.buffer.length - 1;
        }
        return index2;
    }

    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private int index = UnboundedFifoByteBuffer.this.head;
            private int lastReturnedIndex = -1;

            public boolean hasNext() {
                return this.index != UnboundedFifoByteBuffer.this.tail;
            }

            public Byte next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                this.lastReturnedIndex = this.index;
                this.index = UnboundedFifoByteBuffer.this.increment(this.index);
                return new Byte(UnboundedFifoByteBuffer.this.buffer[this.lastReturnedIndex]);
            }

            public void remove() {
                if (this.lastReturnedIndex == -1) {
                    throw new IllegalStateException();
                } else if (this.lastReturnedIndex == UnboundedFifoByteBuffer.this.head) {
                    UnboundedFifoByteBuffer.this.remove();
                    this.lastReturnedIndex = -1;
                } else {
                    int i = this.lastReturnedIndex + 1;
                    while (i != UnboundedFifoByteBuffer.this.tail) {
                        if (i >= UnboundedFifoByteBuffer.this.buffer.length) {
                            UnboundedFifoByteBuffer.this.buffer[i - 1] = UnboundedFifoByteBuffer.this.buffer[0];
                            i = 0;
                        } else {
                            UnboundedFifoByteBuffer.this.buffer[i - 1] = UnboundedFifoByteBuffer.this.buffer[i];
                            i++;
                        }
                    }
                    this.lastReturnedIndex = -1;
                    UnboundedFifoByteBuffer.this.tail = UnboundedFifoByteBuffer.this.decrement(UnboundedFifoByteBuffer.this.tail);
                    UnboundedFifoByteBuffer.this.buffer[UnboundedFifoByteBuffer.this.tail] = 0;
                    this.index = UnboundedFifoByteBuffer.this.decrement(this.index);
                }
            }
        };
    }
}
