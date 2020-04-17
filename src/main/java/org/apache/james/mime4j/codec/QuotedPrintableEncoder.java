package org.apache.james.mime4j.codec;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class QuotedPrintableEncoder {
    private static final byte CR = 13;
    private static final byte EQUALS = 61;
    private static final byte[] HEX_DIGITS = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
    private static final byte LF = 10;
    private static final byte QUOTED_PRINTABLE_LAST_PLAIN = 126;
    private static final int QUOTED_PRINTABLE_MAX_LINE_LENGTH = 76;
    private static final int QUOTED_PRINTABLE_OCTETS_PER_ESCAPE = 3;
    private static final byte SPACE = 32;
    private static final byte TAB = 9;
    private final boolean binary;
    private final byte[] inBuffer;
    private int nextSoftBreak = 77;
    private OutputStream out = null;
    private final byte[] outBuffer;
    private int outputIndex = 0;
    private boolean pendingCR;
    private boolean pendingSpace;
    private boolean pendingTab;

    public QuotedPrintableEncoder(int bufferSize, boolean binary2) {
        this.inBuffer = new byte[bufferSize];
        this.outBuffer = new byte[(bufferSize * 3)];
        this.binary = binary2;
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
    }

    /* access modifiers changed from: package-private */
    public void initEncoding(OutputStream out2) {
        this.out = out2;
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
        this.nextSoftBreak = 77;
    }

    /* access modifiers changed from: package-private */
    public void encodeChunk(byte[] buffer, int off, int len) throws IOException {
        for (int inputIndex = off; inputIndex < len + off; inputIndex++) {
            encode(buffer[inputIndex]);
        }
    }

    /* access modifiers changed from: package-private */
    public void completeEncoding() throws IOException {
        writePending();
        flushOutput();
    }

    public void encode(InputStream in, OutputStream out2) throws IOException {
        initEncoding(out2);
        while (true) {
            int inputLength = in.read(this.inBuffer);
            if (inputLength > -1) {
                encodeChunk(this.inBuffer, 0, inputLength);
            } else {
                completeEncoding();
                return;
            }
        }
    }

    private void writePending() throws IOException {
        if (this.pendingSpace) {
            plain(SPACE);
        } else if (this.pendingTab) {
            plain(TAB);
        } else if (this.pendingCR) {
            plain(CR);
        }
        clearPending();
    }

    private void clearPending() throws IOException {
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
    }

    private void encode(byte next) throws IOException {
        if (next == 10) {
            if (this.binary) {
                writePending();
                escape(next);
            } else if (this.pendingCR) {
                if (this.pendingSpace) {
                    escape(SPACE);
                } else if (this.pendingTab) {
                    escape(TAB);
                }
                lineBreak();
                clearPending();
            } else {
                writePending();
                plain(next);
            }
        } else if (next != 13) {
            writePending();
            if (next == 32) {
                if (this.binary) {
                    escape(next);
                } else {
                    this.pendingSpace = true;
                }
            } else if (next == 9) {
                if (this.binary) {
                    escape(next);
                } else {
                    this.pendingTab = true;
                }
            } else if (next < 32) {
                escape(next);
            } else if (next > 126) {
                escape(next);
            } else if (next == 61) {
                escape(next);
            } else {
                plain(next);
            }
        } else if (this.binary) {
            escape(next);
        } else {
            this.pendingCR = true;
        }
    }

    private void plain(byte next) throws IOException {
        int i = this.nextSoftBreak - 1;
        this.nextSoftBreak = i;
        if (i <= 1) {
            softBreak();
        }
        write(next);
    }

    private void escape(byte next) throws IOException {
        int i = this.nextSoftBreak - 1;
        this.nextSoftBreak = i;
        if (i <= 3) {
            softBreak();
        }
        int nextUnsigned = next & MotionEventCompat.ACTION_MASK;
        write(EQUALS);
        this.nextSoftBreak--;
        write(HEX_DIGITS[nextUnsigned >> 4]);
        this.nextSoftBreak--;
        write(HEX_DIGITS[nextUnsigned % 16]);
    }

    private void write(byte next) throws IOException {
        byte[] bArr = this.outBuffer;
        int i = this.outputIndex;
        this.outputIndex = i + 1;
        bArr[i] = next;
        if (this.outputIndex >= this.outBuffer.length) {
            flushOutput();
        }
    }

    private void softBreak() throws IOException {
        write(EQUALS);
        lineBreak();
    }

    private void lineBreak() throws IOException {
        write(CR);
        write(LF);
        this.nextSoftBreak = QUOTED_PRINTABLE_MAX_LINE_LENGTH;
    }

    /* access modifiers changed from: package-private */
    public void flushOutput() throws IOException {
        if (this.outputIndex < this.outBuffer.length) {
            this.out.write(this.outBuffer, 0, this.outputIndex);
        } else {
            this.out.write(this.outBuffer);
        }
        this.outputIndex = 0;
    }
}
