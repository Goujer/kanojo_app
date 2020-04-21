package org.apache.james.mime4j.codec;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Base64InputStream extends InputStream {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64InputStream.class.desiredAssertionStatus());
    private static final int[] BASE64_DECODE = new int[256];
    private static final byte BASE64_PAD = 61;
    private static final int ENCODED_BUFFER_SIZE = 1536;
    private static final int EOF = -1;
    private static Log log = LogFactory.getLog(Base64InputStream.class);
    private boolean closed;
    private final byte[] encoded;
    private boolean eof;
    private final InputStream in;
    private int position;
    private final ByteQueue q;
    private final byte[] singleByte;
    private int size;
    private boolean strict;

    static {
        for (int i = 0; i < 256; i++) {
            BASE64_DECODE[i] = -1;
        }
        for (int i2 = 0; i2 < Base64OutputStream.BASE64_TABLE.length; i2++) {
            BASE64_DECODE[Base64OutputStream.BASE64_TABLE[i2] & 255] = i2;
        }
    }

    public Base64InputStream(InputStream in2) {
        this(in2, false);
    }

    public Base64InputStream(InputStream in2, boolean strict2) {
        this.singleByte = new byte[1];
        this.closed = false;
        this.encoded = new byte[ENCODED_BUFFER_SIZE];
        this.position = 0;
        this.size = 0;
        this.q = new ByteQueue();
        if (in2 == null) {
            throw new IllegalArgumentException();
        }
        this.in = in2;
        this.strict = strict2;
    }

    public int read() throws IOException {
        int bytes;
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        }
        do {
            bytes = read0(this.singleByte, 0, 1);
            if (bytes == -1) {
                return -1;
            }
        } while (bytes != 1);
        return this.singleByte[0] & 255;
    }

    public int read(byte[] buffer) throws IOException {
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (buffer.length == 0) {
            return 0;
        } else {
            return read0(buffer, 0, buffer.length);
        }
    }

    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (this.closed) {
            throw new IOException("Base64InputStream has been closed");
        } else if (buffer == null) {
            throw new NullPointerException();
        } else if (offset < 0 || length < 0 || offset + length > buffer.length) {
            throw new IndexOutOfBoundsException();
        } else if (length == 0) {
            return 0;
        } else {
            return read0(buffer, offset, offset + length);
        }
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
        }
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 185 */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x008e, code lost:
        r10 = BASE64_DECODE[r15];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0092, code lost:
        if (r10 >= 0) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x009c, code lost:
        if (r17.position >= r17.size) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00a0, code lost:
        if (r5 >= r20) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00a2, code lost:
        r1 = r17.encoded;
        r4 = r17.position;
        r17.position = r4 + 1;
        r15 = r1[r4] & android.support.v4.view.MotionEventCompat.ACTION_MASK;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b6, code lost:
        if (r15 != 61) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00b8, code lost:
        r1 = decodePad(r2, r3, r18, r5, r20) - r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c6, code lost:
        r2 = (r2 << 6) | r10;
        r3 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00cd, code lost:
        if (r3 != 4) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00cf, code lost:
        r3 = 0;
        r7 = (byte) (r2 >>> 16);
        r8 = (byte) (r2 >>> 8);
        r9 = (byte) r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00d9, code lost:
        if (r5 >= (r20 - 2)) goto L_0x00e9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00db, code lost:
        r11 = r5 + 1;
        r18[r5] = r7;
        r5 = r11 + 1;
        r18[r11] = r8;
        r18[r5] = r9;
        r5 = r5 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00eb, code lost:
        if (r5 >= (r20 - 1)) goto L_0x010a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ed, code lost:
        r11 = r5 + 1;
        r18[r5] = r7;
        r5 = r11 + 1;
        r18[r11] = r8;
        r17.q.enqueue(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00fe, code lost:
        if ($assertionsDisabled != false) goto L_0x0138;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0102, code lost:
        if (r5 == r20) goto L_0x0138;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0109, code lost:
        throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x010c, code lost:
        if (r5 >= r20) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x010e, code lost:
        r18[r5] = r7;
        r17.q.enqueue(r8);
        r17.q.enqueue(r9);
        r5 = r5 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0122, code lost:
        r17.q.enqueue(r7);
        r17.q.enqueue(r8);
        r17.q.enqueue(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0138, code lost:
        r1 = r20 - r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0036, code lost:
        continue;
     */
    private int read0(byte[] buffer, int from, int to) throws IOException {
        int index;
        int i;
        int index2 = from;
        int qCount = this.q.count();
        while (true) {
            int qCount2 = qCount;
            index = index2;
            qCount = qCount2 - 1;
            if (qCount2 > 0 && index < to) {
                index2 = index + 1;
                buffer[index] = this.q.dequeue();
            }
        }
        if (this.eof) {
            if (index == from) {
                i = -1;
            } else {
                i = index - from;
            }
            int i2 = index;
        } else {
            int data = 0;
            int sextets = 0;
            int index3 = index;
            loop1:
            while (true) {
                if (index3 < to) {
                    while (true) {
                        if (this.position != this.size) {
                            break;
                        }
                        int n = this.in.read(this.encoded, 0, this.encoded.length);
                        if (n == -1) {
                            this.eof = true;
                            if (sextets != 0) {
                                handleUnexpectedEof(sextets);
                            }
                            i = index3 == from ? -1 : index3 - from;
                        } else if (n > 0) {
                            this.position = 0;
                            this.size = n;
                        } else if (!$assertionsDisabled && n != 0) {
                            throw new AssertionError();
                        }
                    }
                } else if (!$assertionsDisabled && sextets != 0) {
                    throw new AssertionError();
                } else if ($assertionsDisabled || index3 == to) {
                    i = to - from;
                } else {
                    throw new AssertionError();
                }
            }
        }
        return i;
    }

    private int decodePad(int data, int sextets, byte[] buffer, int index, int end) throws IOException {
        this.eof = true;
        if (sextets == 2) {
            byte b = (byte) (data >>> 4);
            if (index < end) {
                buffer[index] = b;
                return index + 1;
            }
            this.q.enqueue(b);
            return index;
        } else if (sextets == 3) {
            byte b1 = (byte) (data >>> 10);
            byte b2 = (byte) ((data >>> 2) & MotionEventCompat.ACTION_MASK);
            if (index < end - 1) {
                int index2 = index + 1;
                buffer[index] = b1;
                int index3 = index2 + 1;
                buffer[index2] = b2;
                return index3;
            } else if (index < end) {
                buffer[index] = b1;
                this.q.enqueue(b2);
                return index + 1;
            } else {
                this.q.enqueue(b1);
                this.q.enqueue(b2);
                return index;
            }
        } else {
            handleUnexpecedPad(sextets);
            return index;
        }
    }

    private void handleUnexpectedEof(int sextets) throws IOException {
        if (this.strict) {
            throw new IOException("unexpected end of file");
        }
        log.warn("unexpected end of file; dropping " + sextets + " sextet(s)");
    }

    private void handleUnexpecedPad(int sextets) throws IOException {
        if (this.strict) {
            throw new IOException("unexpected padding character");
        }
        log.warn("unexpected padding character; dropping " + sextets + " sextet(s)");
    }
}
