package com.google.zxing.datamatrix.encoder;

import android.support.v4.view.MotionEventCompat;

final class EdifactEncoder implements Encoder {
    EdifactEncoder() {
    }

    public int getEncodingMode() {
        return 4;
    }

    public void encode(EncoderContext context) {
        StringBuilder buffer = new StringBuilder();
        while (true) {
            if (!context.hasMoreCharacters()) {
                break;
            }
            encodeChar(context.getCurrentChar(), buffer);
            context.pos++;
            if (buffer.length() >= 4) {
                context.writeCodewords(encodeToCodewords(buffer, 0));
                buffer.delete(0, 4);
                if (HighLevelEncoder.lookAheadTest(context.msg, context.pos, getEncodingMode()) != getEncodingMode()) {
                    context.signalEncoderChange(0);
                    break;
                }
            }
        }
        buffer.append(31);
        handleEOD(context, buffer);
    }

    private static void handleEOD(EncoderContext context, CharSequence buffer) {
        boolean endOfSymbolReached;
        boolean restInAscii = true;
        try {
            int count = buffer.length();
            if (count != 0) {
                if (count == 1) {
                    context.updateSymbolInfo();
                    int available = context.symbolInfo.dataCapacity - context.getCodewordCount();
                    if (context.getRemainingCharacters() == 0 && available <= 2) {
                        context.signalEncoderChange(0);
                        return;
                    }
                }
                if (count > 4) {
                    throw new IllegalStateException("Count must not exceed 4");
                }
                int restChars = count - 1;
                String encoded = encodeToCodewords(buffer, 0);
                if (!context.hasMoreCharacters()) {
                    endOfSymbolReached = true;
                } else {
                    endOfSymbolReached = false;
                }
                if (!endOfSymbolReached || restChars > 2) {
                    restInAscii = false;
                }
                if (restChars <= 2) {
                    context.updateSymbolInfo(context.getCodewordCount() + restChars);
                    if (context.symbolInfo.dataCapacity - context.getCodewordCount() >= 3) {
                        restInAscii = false;
                        context.updateSymbolInfo(context.getCodewordCount() + encoded.length());
                    }
                }
                if (restInAscii) {
                    context.resetSymbolInfo();
                    context.pos -= restChars;
                } else {
                    context.writeCodewords(encoded);
                }
                context.signalEncoderChange(0);
            }
        } finally {
            context.signalEncoderChange(0);
        }
    }

    private static void encodeChar(char c, StringBuilder sb) {
        if (c >= ' ' && c <= '?') {
            sb.append(c);
        } else if (c < '@' || c > '^') {
            HighLevelEncoder.illegalCharacter(c);
        } else {
            sb.append((char) (c - '@'));
        }
    }

    private static String encodeToCodewords(CharSequence sb, int startPos) {
        char c2;
        char c3;
        char c4 = 0;
        int len = sb.length() - startPos;
        if (len == 0) {
            throw new IllegalStateException("StringBuilder must not be empty");
        }
        char c1 = sb.charAt(startPos);
        if (len >= 2) {
            c2 = sb.charAt(startPos + 1);
        } else {
            c2 = 0;
        }
        if (len >= 3) {
            c3 = sb.charAt(startPos + 2);
        } else {
            c3 = 0;
        }
        if (len >= 4) {
            c4 = sb.charAt(startPos + 3);
        }
        int v = (c1 << 18) + (c2 << 12) + (c3 << 6) + c4;
        char cw1 = (char) ((v >> 16) & MotionEventCompat.ACTION_MASK);
        char cw2 = (char) ((v >> 8) & MotionEventCompat.ACTION_MASK);
        char cw3 = (char) (v & MotionEventCompat.ACTION_MASK);
        StringBuilder res = new StringBuilder(3);
        res.append(cw1);
        if (len >= 2) {
            res.append(cw2);
        }
        if (len >= 3) {
            res.append(cw3);
        }
        return res.toString();
    }
}
