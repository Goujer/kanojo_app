package com.google.zxing.datamatrix.encoder;

import com.google.zxing.Dimension;
import java.nio.charset.Charset;

final class EncoderContext {
    StringBuilder codewords;
    private Dimension maxSize;
    private Dimension minSize;
    String msg;
    int newEncoding;
    int pos;
    private SymbolShapeHint shape;
    private int skipAtEnd;
    SymbolInfo symbolInfo;

    EncoderContext(String msg2) {
        byte[] msgBinary = msg2.getBytes(Charset.forName("ISO-8859-1"));
        StringBuilder sb = new StringBuilder(msgBinary.length);
        int i = 0;
        int c = msgBinary.length;
        while (i < c) {
            char ch = (char) (msgBinary[i] & 255);
            if (ch != '?' || msg2.charAt(i) == '?') {
                sb.append(ch);
                i++;
            } else {
                throw new IllegalArgumentException("Message contains characters outside ISO-8859-1 encoding.");
            }
        }
        this.msg = sb.toString();
        this.shape = SymbolShapeHint.FORCE_NONE;
        this.codewords = new StringBuilder(msg2.length());
        this.newEncoding = -1;
    }

    public void setSymbolShape(SymbolShapeHint shape2) {
        this.shape = shape2;
    }

    public void setSizeConstraints(Dimension minSize2, Dimension maxSize2) {
        this.minSize = minSize2;
        this.maxSize = maxSize2;
    }

    public String getMessage() {
        return this.msg;
    }

    public void setSkipAtEnd(int count) {
        this.skipAtEnd = count;
    }

    public char getCurrentChar() {
        return this.msg.charAt(this.pos);
    }

    public char getCurrent() {
        return this.msg.charAt(this.pos);
    }

    public void writeCodewords(String codewords2) {
        this.codewords.append(codewords2);
    }

    public void writeCodeword(char codeword) {
        this.codewords.append(codeword);
    }

    public int getCodewordCount() {
        return this.codewords.length();
    }

    public void signalEncoderChange(int encoding) {
        this.newEncoding = encoding;
    }

    public void resetEncoderSignal() {
        this.newEncoding = -1;
    }

    public boolean hasMoreCharacters() {
        return this.pos < getTotalMessageCharCount();
    }

    private int getTotalMessageCharCount() {
        return this.msg.length() - this.skipAtEnd;
    }

    public int getRemainingCharacters() {
        return getTotalMessageCharCount() - this.pos;
    }

    public void updateSymbolInfo() {
        updateSymbolInfo(getCodewordCount());
    }

    public void updateSymbolInfo(int len) {
        if (this.symbolInfo == null || len > this.symbolInfo.dataCapacity) {
            this.symbolInfo = SymbolInfo.lookup(len, this.shape, this.minSize, this.maxSize, true);
        }
    }

    public void resetSymbolInfo() {
        this.symbolInfo = null;
    }
}
