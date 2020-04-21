package org.apache.james.mime4j.util;

public final class ByteArrayBuffer implements ByteSequence {
    private byte[] buffer;
    private int len;

    public ByteArrayBuffer(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Buffer capacity may not be negative");
        }
        this.buffer = new byte[capacity];
    }

    public ByteArrayBuffer(byte[] bytes, boolean dontCopy) {
        this(bytes, bytes.length, dontCopy);
    }

    public ByteArrayBuffer(byte[] bytes, int len2, boolean dontCopy) {
        if (bytes == null) {
            throw new IllegalArgumentException();
        } else if (len2 < 0 || len2 > bytes.length) {
            throw new IllegalArgumentException();
        } else {
            if (dontCopy) {
                this.buffer = bytes;
            } else {
                this.buffer = new byte[len2];
                System.arraycopy(bytes, 0, this.buffer, 0, len2);
            }
            this.len = len2;
        }
    }

    private void expand(int newlen) {
        byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }

    public void append(byte[] b, int off, int len2) {
        if (b != null) {
            if (off < 0 || off > b.length || len2 < 0 || off + len2 < 0 || off + len2 > b.length) {
                throw new IndexOutOfBoundsException();
            } else if (len2 != 0) {
                int newlen = this.len + len2;
                if (newlen > this.buffer.length) {
                    expand(newlen);
                }
                System.arraycopy(b, off, this.buffer, this.len, len2);
                this.len = newlen;
            }
        }
    }

    public void append(int b) {
        int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        this.buffer[this.len] = (byte) b;
        this.len = newlen;
    }

    public void clear() {
        this.len = 0;
    }

    public byte[] toByteArray() {
        byte[] b = new byte[this.len];
        if (this.len > 0) {
            System.arraycopy(this.buffer, 0, b, 0, this.len);
        }
        return b;
    }

    public byte byteAt(int i) {
        if (i >= 0 && i < this.len) {
            return this.buffer[i];
        }
        throw new IndexOutOfBoundsException();
    }

    public int capacity() {
        return this.buffer.length;
    }

    public int length() {
        return this.len;
    }

    public byte[] buffer() {
        return this.buffer;
    }

    public int indexOf(byte b) {
        return indexOf(b, 0, this.len);
    }

    public int indexOf(byte b, int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex > this.len) {
            endIndex = this.len;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        for (int i = beginIndex; i < endIndex; i++) {
            if (this.buffer[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public void setLength(int len2) {
        if (len2 < 0 || len2 > this.buffer.length) {
            throw new IndexOutOfBoundsException();
        }
        this.len = len2;
    }

    public boolean isEmpty() {
        return this.len == 0;
    }

    public boolean isFull() {
        return this.len == this.buffer.length;
    }

    public String toString() {
        return new String(toByteArray());
    }
}
