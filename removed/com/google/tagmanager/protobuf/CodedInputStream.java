// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// https://developers.google.com/protocol-buffers/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.MessageLite;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CodedInputStream {
    private static final int BUFFER_SIZE = 4096;
    private static final int DEFAULT_RECURSION_LIMIT = 64;
    private static final int DEFAULT_SIZE_LIMIT = 67108864;
    /* access modifiers changed from: private */
    public final byte[] buffer;
    private final boolean bufferIsImmutable;
    /* access modifiers changed from: private */
    public int bufferPos;
    private int bufferSize;
    private int bufferSizeAfterLimit;
    private int currentLimit;
    private boolean enableAliasing;
    private final InputStream input;
    private int lastTag;
    private int recursionDepth;
    private int recursionLimit;
    private RefillCallback refillCallback;
    private int sizeLimit;
    private int totalBytesRetired;

    private interface RefillCallback {
        void onRefill();
    }

    public static CodedInputStream newInstance(InputStream input2) {
        return new CodedInputStream(input2);
    }

    public static CodedInputStream newInstance(byte[] buf) {
        return newInstance(buf, 0, buf.length);
    }

    public static CodedInputStream newInstance(byte[] buf, int off, int len) {
        CodedInputStream result = new CodedInputStream(buf, off, len);
        try {
            result.pushLimit(len);
            return result;
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static CodedInputStream newInstance(ByteBuffer buf) {
        if (buf.hasArray()) {
            return newInstance(buf.array(), buf.arrayOffset() + buf.position(), buf.remaining());
        }
        ByteBuffer temp = buf.duplicate();
        byte[] buffer2 = new byte[temp.remaining()];
        temp.get(buffer2);
        return newInstance(buffer2);
    }

    static CodedInputStream newInstance(LiteralByteString byteString) {
        CodedInputStream result = new CodedInputStream(byteString);
        try {
            result.pushLimit(byteString.size());
            return result;
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public int readTag() throws IOException {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readRawVarint32();
        if (WireFormat.getTagFieldNumber(this.lastTag) != 0) {
            return this.lastTag;
        }
        throw InvalidProtocolBufferException.invalidTag();
    }

    public void checkLastTagWas(int value) throws InvalidProtocolBufferException {
        if (this.lastTag != value) {
            throw InvalidProtocolBufferException.invalidEndTag();
        }
    }

    public int getLastTag() {
        return this.lastTag;
    }

    public boolean skipField(int tag) throws IOException {
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                skipRawVarint();
                return true;
            case 1:
                skipRawBytes(8);
                return true;
            case 2:
                skipRawBytes(readRawVarint32());
                return true;
            case 3:
                skipMessage();
                checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4));
                return true;
            case 4:
                return false;
            case 5:
                skipRawBytes(4);
                return true;
            default:
                throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    public boolean skipField(int tag, CodedOutputStream output) throws IOException {
        switch (WireFormat.getTagWireType(tag)) {
            case 0:
                long value = readInt64();
                output.writeRawVarint32(tag);
                output.writeUInt64NoTag(value);
                return true;
            case 1:
                long value2 = readRawLittleEndian64();
                output.writeRawVarint32(tag);
                output.writeFixed64NoTag(value2);
                return true;
            case 2:
                ByteString value3 = readBytes();
                output.writeRawVarint32(tag);
                output.writeBytesNoTag(value3);
                return true;
            case 3:
                output.writeRawVarint32(tag);
                skipMessage(output);
                int endtag = WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4);
                checkLastTagWas(endtag);
                output.writeRawVarint32(endtag);
                return true;
            case 4:
                return false;
            case 5:
                int value4 = readRawLittleEndian32();
                output.writeRawVarint32(tag);
                output.writeFixed32NoTag(value4);
                return true;
            default:
                throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException:
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public void skipMessage() throws java.io.IOException {
        /*
            r2 = this;
        L_0x0000:
            int r0 = r2.readTag()
            if (r0 == 0) goto L_0x000c
            boolean r1 = r2.skipField(r0)
            if (r1 != 0) goto L_0x0000
        L_0x000c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.protobuf.CodedInputStream.skipMessage():void");
    }

    /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException:
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public void skipMessage(com.google.tagmanager.protobuf.CodedOutputStream r3) throws java.io.IOException {
        /*
            r2 = this;
        L_0x0000:
            int r0 = r2.readTag()
            if (r0 == 0) goto L_0x000c
            boolean r1 = r2.skipField(r0, r3)
            if (r1 != 0) goto L_0x0000
        L_0x000c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.protobuf.CodedInputStream.skipMessage(com.google.tagmanager.protobuf.CodedOutputStream):void");
    }

    private class SkippedDataSink implements RefillCallback {
        private ByteArrayOutputStream byteArrayStream;
        private int lastPos;

        private SkippedDataSink() {
            this.lastPos = CodedInputStream.this.bufferPos;
        }

        public void onRefill() {
            if (this.byteArrayStream == null) {
                this.byteArrayStream = new ByteArrayOutputStream();
            }
            this.byteArrayStream.write(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos - this.lastPos);
            this.lastPos = 0;
        }

        /* access modifiers changed from: package-private */
        public ByteBuffer getSkippedData() {
            if (this.byteArrayStream == null) {
                return ByteBuffer.wrap(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos - this.lastPos);
            }
            this.byteArrayStream.write(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos);
            return ByteBuffer.wrap(this.byteArrayStream.toByteArray());
        }
    }

    public void mergeToMessage(MutableMessageLite message) throws IOException {
        SkippedDataSink dataSink = new SkippedDataSink();
        this.refillCallback = dataSink;
        skipMessage();
        this.refillCallback = null;
        ByteBuffer data = dataSink.getSkippedData();
        if (!message.mergeFrom(data.array(), data.position(), data.remaining())) {
            throw InvalidProtocolBufferException.parseFailure();
        }
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readRawLittleEndian64());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readRawLittleEndian32());
    }

    public long readUInt64() throws IOException {
        return readRawVarint64();
    }

    public long readInt64() throws IOException {
        return readRawVarint64();
    }

    public int readInt32() throws IOException {
        return readRawVarint32();
    }

    public long readFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public boolean readBool() throws IOException {
        return readRawVarint32() != 0;
    }

    public String readString() throws IOException {
        int size = readRawVarint32();
        if (size > this.bufferSize - this.bufferPos || size <= 0) {
            return new String(readRawBytes(size), "UTF-8");
        }
        String result = new String(this.buffer, this.bufferPos, size, "UTF-8");
        this.bufferPos += size;
        return result;
    }

    public String readStringRequireUtf8() throws IOException {
        ByteString bs;
        int size = readRawVarint32();
        if (size > this.bufferSize - this.bufferPos || size <= 0) {
            bs = new LiteralByteString(readRawBytes(size));
        } else {
            bs = ByteString.copyFrom(this.buffer, this.bufferPos, size);
            this.bufferPos += size;
        }
        if (bs.isValidUtf8()) {
            return bs.toStringUtf8();
        }
        throw InvalidProtocolBufferException.invalidUtf8();
    }

    public void readGroup(int fieldNumber, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        builder.mergeFrom(this, extensionRegistry);
        checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
        this.recursionDepth--;
    }

    public void readGroup(int fieldNumber, MutableMessageLite message, ExtensionRegistryLite extensionRegistry) throws IOException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        message.mergeFrom(this, extensionRegistry);
        checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
        this.recursionDepth--;
    }

    public <T extends MessageLite> T readGroup(
            final int fieldNumber,
            final Parser<T> parser,
            final ExtensionRegistryLite extensionRegistry)
            throws IOException {
        if (recursionDepth >= recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        ++recursionDepth;
        T result = parser.parsePartialFrom(this, extensionRegistry);
        checkLastTagWas(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_END_GROUP));
        --recursionDepth;
        return result;
    }

    @Deprecated
    public void readUnknownGroup(int fieldNumber, MessageLite.Builder builder) throws IOException {
        readGroup(fieldNumber, builder, (ExtensionRegistryLite) null);
    }

    public void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        int oldLimit = pushLimit(length);
        this.recursionDepth++;
        builder.mergeFrom(this, extensionRegistry);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(oldLimit);
    }

    public void readMessage(MutableMessageLite message, ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        int oldLimit = pushLimit(length);
        this.recursionDepth++;
        message.mergeFrom(this, extensionRegistry);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(oldLimit);
    }

    public <T extends MessageLite> T readMessage(
            final Parser<T> parser, final ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (recursionDepth >= recursionLimit) {
            throw InvalidProtocolBufferException.recursionLimitExceeded();
        }
        final int oldLimit = pushLimit(length);
        ++recursionDepth;
        T result = parser.parsePartialFrom(this, extensionRegistry);
        checkLastTagWas(0);
        --recursionDepth;
        popLimit(oldLimit);
        return result;
    }

    public ByteString readBytes() throws IOException {
        int size = readRawVarint32();
        if (size == 0) {
            return ByteString.EMPTY;
        }
        if (size > this.bufferSize - this.bufferPos || size <= 0) {
            return new LiteralByteString(readRawBytes(size));
        }
        ByteString result = (!this.bufferIsImmutable || !this.enableAliasing) ? ByteString.copyFrom(this.buffer, this.bufferPos, size) : new BoundedByteString(this.buffer, this.bufferPos, size);
        this.bufferPos += size;
        return result;
    }

    public byte[] readByteArray() throws IOException {
        int size = readRawVarint32();
        if (size == 0) {
            return Internal.EMPTY_BYTE_ARRAY;
        }
        if (size > this.bufferSize - this.bufferPos || size <= 0) {
            return readRawBytes(size);
        }
        byte[] copyOfRange = Arrays.copyOfRange(this.buffer, this.bufferPos, this.bufferPos + size);
        this.bufferPos += size;
        return copyOfRange;
    }

    public ByteBuffer readByteBuffer() throws IOException {
        int size = readRawVarint32();
        if (size == 0) {
            return Internal.EMPTY_BYTE_BUFFER;
        }
        if (size > this.bufferSize - this.bufferPos || size <= 0) {
            return ByteBuffer.wrap(readRawBytes(size));
        }
        ByteBuffer result = (this.input != null || this.bufferIsImmutable || !this.enableAliasing) ? ByteBuffer.wrap(Arrays.copyOfRange(this.buffer, this.bufferPos, this.bufferPos + size)) : ByteBuffer.wrap(this.buffer, this.bufferPos, size).slice();
        this.bufferPos += size;
        return result;
    }

    public int readUInt32() throws IOException {
        return readRawVarint32();
    }

    public int readEnum() throws IOException {
        return readRawVarint32();
    }

    public int readSFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public long readSFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readSInt32() throws IOException {
        return decodeZigZag32(readRawVarint32());
    }

    public long readSInt64() throws IOException {
        return decodeZigZag64(readRawVarint64());
    }

    public int readRawVarint32() throws IOException {
        int tmp = readRawByte();
        if (tmp >= 0) {
            return tmp;
        }
        int result = tmp & 127;
        byte tmp2 = readRawByte();
        if (tmp2 >= 0) {
            return result | (tmp2 << 7);
        }
        int result2 = result | ((tmp2 & Byte.MAX_VALUE) << 7);
        byte tmp3 = readRawByte();
        if (tmp3 >= 0) {
            return result2 | (tmp3 << 14);
        }
        int result3 = result2 | ((tmp3 & Byte.MAX_VALUE) << 14);
        byte tmp4 = readRawByte();
        if (tmp4 >= 0) {
            return result3 | (tmp4 << 21);
        }
        byte tmp5 = readRawByte();
        int result4 = result3 | ((tmp4 & Byte.MAX_VALUE) << 21) | (tmp5 << 28);
        if (tmp5 >= 0) {
            return result4;
        }
        for (int i = 0; i < 5; i++) {
            if (readRawByte() >= 0) {
                return result4;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    private void skipRawVarint() throws IOException {
        int i = 0;
        while (i < 10) {
            if (readRawByte() < 0) {
                i++;
            } else {
                return;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    static int readRawVarint32(InputStream input2) throws IOException {
        int firstByte = input2.read();
        if (firstByte != -1) {
            return readRawVarint32(firstByte, input2);
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    public static int readRawVarint32(int firstByte, InputStream input2) throws IOException {
        if ((firstByte & 128) == 0) {
            return firstByte;
        }
        int result = firstByte & 127;
        int offset = 7;
        while (offset < 32) {
            int b = input2.read();
            if (b == -1) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            result |= (b & 127) << offset;
            if ((b & 128) == 0) {
                return result;
            }
            offset += 7;
        }
        while (offset < 64) {
            int b2 = input2.read();
            if (b2 == -1) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if ((b2 & 128) == 0) {
                return result;
            } else {
                offset += 7;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    public long readRawVarint64() throws IOException {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            byte b = readRawByte();
            result |= ((long) (b & Byte.MAX_VALUE)) << shift;
            if ((b & 128) == 0) {
                return result;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    public int readRawLittleEndian32() throws IOException {
        return (readRawByte() & 255) | ((readRawByte() & 255) << 8) | ((readRawByte() & 255) << 16) | ((readRawByte() & 255) << 24);
    }

    public long readRawLittleEndian64() throws IOException {
        return (((long) readRawByte()) & 255) | ((((long) readRawByte()) & 255) << 8) | ((((long) readRawByte()) & 255) << 16) | ((((long) readRawByte()) & 255) << 24) | ((((long) readRawByte()) & 255) << 32) | ((((long) readRawByte()) & 255) << 40) | ((((long) readRawByte()) & 255) << 48) | ((((long) readRawByte()) & 255) << 56);
    }

    public static int decodeZigZag32(int n) {
        return (n >>> 1) ^ (-(n & 1));
    }

    public static long decodeZigZag64(long n) {
        return (n >>> 1) ^ (-(1 & n));
    }

    private CodedInputStream(byte[] buffer2, int off, int len) {
        this.enableAliasing = false;
        this.currentLimit = Integer.MAX_VALUE;
        this.recursionLimit = 64;
        this.sizeLimit = DEFAULT_SIZE_LIMIT;
        this.refillCallback = null;
        this.buffer = buffer2;
        this.bufferSize = off + len;
        this.bufferPos = off;
        this.totalBytesRetired = -off;
        this.input = null;
        this.bufferIsImmutable = false;
    }

    private CodedInputStream(InputStream input2) {
        this.enableAliasing = false;
        this.currentLimit = Integer.MAX_VALUE;
        this.recursionLimit = 64;
        this.sizeLimit = DEFAULT_SIZE_LIMIT;
        this.refillCallback = null;
        this.buffer = new byte[4096];
        this.bufferSize = 0;
        this.bufferPos = 0;
        this.totalBytesRetired = 0;
        this.input = input2;
        this.bufferIsImmutable = false;
    }

    private CodedInputStream(LiteralByteString byteString) {
        this.enableAliasing = false;
        this.currentLimit = Integer.MAX_VALUE;
        this.recursionLimit = 64;
        this.sizeLimit = DEFAULT_SIZE_LIMIT;
        this.refillCallback = null;
        this.buffer = byteString.bytes;
        this.bufferPos = byteString.getOffsetIntoBytes();
        this.bufferSize = byteString.getOffsetIntoBytes() + byteString.size();
        this.totalBytesRetired = -this.bufferPos;
        this.input = null;
        this.bufferIsImmutable = true;
    }

    public void enableAliasing(boolean enabled) {
        this.enableAliasing = enabled;
    }

    public int setRecursionLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Recursion limit cannot be negative: " + limit);
        }
        int oldLimit = this.recursionLimit;
        this.recursionLimit = limit;
        return oldLimit;
    }

    public int setSizeLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Size limit cannot be negative: " + limit);
        }
        int oldLimit = this.sizeLimit;
        this.sizeLimit = limit;
        return oldLimit;
    }

    public void resetSizeCounter() {
        this.totalBytesRetired = -this.bufferPos;
    }

    public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
        if (byteLimit < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        }
        int byteLimit2 = byteLimit + this.totalBytesRetired + this.bufferPos;
        int oldLimit = this.currentLimit;
        if (byteLimit2 > oldLimit) {
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        this.currentLimit = byteLimit2;
        recomputeBufferSizeAfterLimit();
        return oldLimit;
    }

    private void recomputeBufferSizeAfterLimit() {
        this.bufferSize += this.bufferSizeAfterLimit;
        int bufferEnd = this.totalBytesRetired + this.bufferSize;
        if (bufferEnd > this.currentLimit) {
            this.bufferSizeAfterLimit = bufferEnd - this.currentLimit;
            this.bufferSize -= this.bufferSizeAfterLimit;
            return;
        }
        this.bufferSizeAfterLimit = 0;
    }

    public void popLimit(int oldLimit) {
        this.currentLimit = oldLimit;
        recomputeBufferSizeAfterLimit();
    }

    public int getBytesUntilLimit() {
        if (this.currentLimit == Integer.MAX_VALUE) {
            return -1;
        }
        return this.currentLimit - (this.totalBytesRetired + this.bufferPos);
    }

    public boolean isAtEnd() throws IOException {
        return this.bufferPos == this.bufferSize && !refillBuffer(false);
    }

    public int getTotalBytesRead() {
        return this.totalBytesRetired + this.bufferPos;
    }

    private boolean refillBuffer(boolean mustSucceed) throws IOException {
        if (this.bufferPos < this.bufferSize) {
            throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
        } else if (this.totalBytesRetired + this.bufferSize != this.currentLimit) {
            if (this.refillCallback != null) {
                this.refillCallback.onRefill();
            }
            this.totalBytesRetired += this.bufferSize;
            this.bufferPos = 0;
            this.bufferSize = this.input == null ? -1 : this.input.read(this.buffer);
            if (this.bufferSize == 0 || this.bufferSize < -1) {
                throw new IllegalStateException("InputStream#read(byte[]) returned invalid result: " + this.bufferSize + "\nThe InputStream implementation is buggy.");
            } else if (this.bufferSize == -1) {
                this.bufferSize = 0;
                if (!mustSucceed) {
                    return false;
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            } else {
                recomputeBufferSizeAfterLimit();
                int totalBytesRead = this.totalBytesRetired + this.bufferSize + this.bufferSizeAfterLimit;
                if (totalBytesRead <= this.sizeLimit && totalBytesRead >= 0) {
                    return true;
                }
                throw InvalidProtocolBufferException.sizeLimitExceeded();
            }
        } else if (!mustSucceed) {
            return false;
        } else {
            throw InvalidProtocolBufferException.truncatedMessage();
        }
    }

    public byte readRawByte() throws IOException {
        if (this.bufferPos == this.bufferSize) {
            refillBuffer(true);
        }
        byte[] bArr = this.buffer;
        int i = this.bufferPos;
        this.bufferPos = i + 1;
        return bArr[i];
    }

    public byte[] readRawBytes(int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        } else if (this.totalBytesRetired + this.bufferPos + size > this.currentLimit) {
            skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.bufferPos);
            throw InvalidProtocolBufferException.truncatedMessage();
        } else if (size <= this.bufferSize - this.bufferPos) {
            byte[] bytes = new byte[size];
            System.arraycopy(this.buffer, this.bufferPos, bytes, 0, size);
            this.bufferPos += size;
            return bytes;
        } else if (size < 4096) {
            byte[] bytes2 = new byte[size];
            int pos = this.bufferSize - this.bufferPos;
            System.arraycopy(this.buffer, this.bufferPos, bytes2, 0, pos);
            this.bufferPos = this.bufferSize;
            refillBuffer(true);
            while (size - pos > this.bufferSize) {
                System.arraycopy(this.buffer, 0, bytes2, pos, this.bufferSize);
                pos += this.bufferSize;
                this.bufferPos = this.bufferSize;
                refillBuffer(true);
            }
            System.arraycopy(this.buffer, 0, bytes2, pos, size - pos);
            this.bufferPos = size - pos;
            return bytes2;
        } else {
            int originalBufferPos = this.bufferPos;
            int originalBufferSize = this.bufferSize;
            this.totalBytesRetired += this.bufferSize;
            this.bufferPos = 0;
            this.bufferSize = 0;
            int sizeLeft = size - (originalBufferSize - originalBufferPos);
            List<byte[]> chunks = new ArrayList<>();
            while (sizeLeft > 0) {
                byte[] chunk = new byte[Math.min(sizeLeft, 4096)];
                int pos2 = 0;
                while (pos2 < chunk.length) {
                    int n = this.input == null ? -1 : this.input.read(chunk, pos2, chunk.length - pos2);
                    if (n == -1) {
                        throw InvalidProtocolBufferException.truncatedMessage();
                    }
                    this.totalBytesRetired += n;
                    pos2 += n;
                }
                sizeLeft -= chunk.length;
                chunks.add(chunk);
            }
            byte[] bytes3 = new byte[size];
            int pos3 = originalBufferSize - originalBufferPos;
            System.arraycopy(this.buffer, originalBufferPos, bytes3, 0, pos3);
            for (byte[] chunk2 : chunks) {
                System.arraycopy(chunk2, 0, bytes3, pos3, chunk2.length);
                pos3 += chunk2.length;
            }
            return bytes3;
        }
    }

    public void skipRawBytes(int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferException.negativeSize();
        } else if (this.totalBytesRetired + this.bufferPos + size > this.currentLimit) {
            skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.bufferPos);
            throw InvalidProtocolBufferException.truncatedMessage();
        } else if (size <= this.bufferSize - this.bufferPos) {
            this.bufferPos += size;
        } else {
            int pos = this.bufferSize - this.bufferPos;
            this.bufferPos = this.bufferSize;
            refillBuffer(true);
            while (size - pos > this.bufferSize) {
                pos += this.bufferSize;
                this.bufferPos = this.bufferSize;
                refillBuffer(true);
            }
            this.bufferPos = size - pos;
        }
    }
}
