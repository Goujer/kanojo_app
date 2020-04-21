package org.apache.james.mime4j.codec;

import java.util.Iterator;

public class ByteQueue implements Iterable<Byte> {
    private UnboundedFifoByteBuffer buf;
    private int initialCapacity;

    public ByteQueue() {
        this.initialCapacity = -1;
        this.buf = new UnboundedFifoByteBuffer();
    }

    public ByteQueue(int initialCapacity2) {
        this.initialCapacity = -1;
        this.buf = new UnboundedFifoByteBuffer(initialCapacity2);
        this.initialCapacity = initialCapacity2;
    }

    public void enqueue(byte b) {
        this.buf.add(b);
    }

    public byte dequeue() {
        return this.buf.remove();
    }

    public int count() {
        return this.buf.size();
    }

    public void clear() {
        if (this.initialCapacity != -1) {
            this.buf = new UnboundedFifoByteBuffer(this.initialCapacity);
        } else {
            this.buf = new UnboundedFifoByteBuffer();
        }
    }

    public Iterator<Byte> iterator() {
        return this.buf.iterator();
    }
}
