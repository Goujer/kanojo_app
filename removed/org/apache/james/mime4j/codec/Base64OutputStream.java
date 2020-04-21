package org.apache.james.mime4j.codec;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class Base64OutputStream extends FilterOutputStream {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64OutputStream.class.desiredAssertionStatus());
    private static final Set<Byte> BASE64_CHARS = new HashSet();
    private static final byte BASE64_PAD = 61;
    static final byte[] BASE64_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] CRLF_SEPARATOR = {13, 10};
    private static final int DEFAULT_LINE_LENGTH = 76;
    private static final int ENCODED_BUFFER_SIZE = 2048;
    private static final int MASK_6BITS = 63;
    private boolean closed;
    private int data;
    private final byte[] encoded;
    private final int lineLength;
    private int linePosition;
    private final byte[] lineSeparator;
    private int modulus;
    private int position;
    private final byte[] singleByte;

    static {
        for (byte b : BASE64_TABLE) {
            BASE64_CHARS.add(Byte.valueOf(b));
        }
        BASE64_CHARS.add(Byte.valueOf(BASE64_PAD));
    }

    public Base64OutputStream(OutputStream out) {
        this(out, DEFAULT_LINE_LENGTH, CRLF_SEPARATOR);
    }

    public Base64OutputStream(OutputStream out, int lineLength2) {
        this(out, lineLength2, CRLF_SEPARATOR);
    }

    public Base64OutputStream(OutputStream out, int lineLength2, byte[] lineSeparator2) {
        super(out);
        this.singleByte = new byte[1];
        this.closed = false;
        this.position = 0;
        this.data = 0;
        this.modulus = 0;
        this.linePosition = 0;
        if (out == null) {
            throw new IllegalArgumentException();
        } else if (lineLength2 < 0) {
            throw new IllegalArgumentException();
        } else {
            checkLineSeparator(lineSeparator2);
            this.lineLength = lineLength2;
            this.lineSeparator = new byte[lineSeparator2.length];
            System.arraycopy(lineSeparator2, 0, this.lineSeparator, 0, lineSeparator2.length);
            this.encoded = new byte[2048];
        }
    }

    public final void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        }
        this.singleByte[0] = (byte) b;
        write0(this.singleByte, 0, 1);
    }

    public final void write(byte[] buffer) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (buffer.length != 0) {
            write0(buffer, 0, buffer.length);
        }
    }

    public final void write(byte[] buffer, int offset, int length) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (offset < 0 || length < 0 || offset + length > buffer.length) {
            throw new IndexOutOfBoundsException();
        } else if (length != 0) {
            write0(buffer, offset, offset + length);
        }
    }

    public void flush() throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        }
        flush0();
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            close0();
        }
    }

    private void write0(byte[] buffer, int from, int to) throws IOException {
        for (int i = from; i < to; i++) {
            this.data = (this.data << 8) | (buffer[i] & 255);
            int i2 = this.modulus + 1;
            this.modulus = i2;
            if (i2 == 3) {
                this.modulus = 0;
                if (this.lineLength > 0 && this.linePosition >= this.lineLength) {
                    this.linePosition = 0;
                    if (this.encoded.length - this.position < this.lineSeparator.length) {
                        flush0();
                    }
                    for (byte ls : this.lineSeparator) {
                        byte[] bArr = this.encoded;
                        int i3 = this.position;
                        this.position = i3 + 1;
                        bArr[i3] = ls;
                    }
                }
                if (this.encoded.length - this.position < 4) {
                    flush0();
                }
                byte[] bArr2 = this.encoded;
                int i4 = this.position;
                this.position = i4 + 1;
                bArr2[i4] = BASE64_TABLE[(this.data >> 18) & MASK_6BITS];
                byte[] bArr3 = this.encoded;
                int i5 = this.position;
                this.position = i5 + 1;
                bArr3[i5] = BASE64_TABLE[(this.data >> 12) & MASK_6BITS];
                byte[] bArr4 = this.encoded;
                int i6 = this.position;
                this.position = i6 + 1;
                bArr4[i6] = BASE64_TABLE[(this.data >> 6) & MASK_6BITS];
                byte[] bArr5 = this.encoded;
                int i7 = this.position;
                this.position = i7 + 1;
                bArr5[i7] = BASE64_TABLE[this.data & MASK_6BITS];
                this.linePosition += 4;
            }
        }
    }

    private void flush0() throws IOException {
        if (this.position > 0) {
            this.out.write(this.encoded, 0, this.position);
            this.position = 0;
        }
    }

    private void close0() throws IOException {
        if (this.modulus != 0) {
            writePad();
        }
        if (this.lineLength > 0 && this.linePosition > 0) {
            writeLineSeparator();
        }
        flush0();
    }

    private void writePad() throws IOException {
        if (this.lineLength > 0 && this.linePosition >= this.lineLength) {
            writeLineSeparator();
        }
        if (this.encoded.length - this.position < 4) {
            flush0();
        }
        if (this.modulus == 1) {
            byte[] bArr = this.encoded;
            int i = this.position;
            this.position = i + 1;
            bArr[i] = BASE64_TABLE[(this.data >> 2) & MASK_6BITS];
            byte[] bArr2 = this.encoded;
            int i2 = this.position;
            this.position = i2 + 1;
            bArr2[i2] = BASE64_TABLE[(this.data << 4) & MASK_6BITS];
            byte[] bArr3 = this.encoded;
            int i3 = this.position;
            this.position = i3 + 1;
            bArr3[i3] = BASE64_PAD;
            byte[] bArr4 = this.encoded;
            int i4 = this.position;
            this.position = i4 + 1;
            bArr4[i4] = BASE64_PAD;
        } else if ($assertionsDisabled || this.modulus == 2) {
            byte[] bArr5 = this.encoded;
            int i5 = this.position;
            this.position = i5 + 1;
            bArr5[i5] = BASE64_TABLE[(this.data >> 10) & MASK_6BITS];
            byte[] bArr6 = this.encoded;
            int i6 = this.position;
            this.position = i6 + 1;
            bArr6[i6] = BASE64_TABLE[(this.data >> 4) & MASK_6BITS];
            byte[] bArr7 = this.encoded;
            int i7 = this.position;
            this.position = i7 + 1;
            bArr7[i7] = BASE64_TABLE[(this.data << 2) & MASK_6BITS];
            byte[] bArr8 = this.encoded;
            int i8 = this.position;
            this.position = i8 + 1;
            bArr8[i8] = BASE64_PAD;
        } else {
            throw new AssertionError();
        }
        this.linePosition += 4;
    }

    private void writeLineSeparator() throws IOException {
        this.linePosition = 0;
        if (this.encoded.length - this.position < this.lineSeparator.length) {
            flush0();
        }
        for (byte ls : this.lineSeparator) {
            byte[] bArr = this.encoded;
            int i = this.position;
            this.position = i + 1;
            bArr[i] = ls;
        }
    }

    private void checkLineSeparator(byte[] lineSeparator2) {
        if (lineSeparator2.length > 2048) {
            throw new IllegalArgumentException("line separator length exceeds 2048");
        }
        for (byte b : lineSeparator2) {
            if (BASE64_CHARS.contains(Byte.valueOf(b))) {
                throw new IllegalArgumentException("line separator must not contain base64 character '" + ((char) (b & 255)) + "'");
            }
        }
    }
}
