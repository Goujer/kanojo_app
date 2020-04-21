package com.google.tagmanager;

final class Base64 {
    private static final byte[] ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9};
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte NEW_LINE = 10;
    private static final byte PADDING_BYTE = 61;
    private static final byte[] WEBSAFE_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] WEBSAFE_DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9};
    private static final byte WHITE_SPACE_ENC = -5;

    private Base64() {
    }

    public static byte[] getAlphabet() {
        return (byte[]) ALPHABET.clone();
    }

    public static byte[] getWebsafeAlphabet() {
        return (byte[]) WEBSAFE_ALPHABET.clone();
    }

    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, byte[] alphabet) {
        int i;
        int i2 = 0;
        if (numSigBytes > 0) {
            i = (source[srcOffset] << 24) >>> 8;
        } else {
            i = 0;
        }
        int i3 = (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | i;
        if (numSigBytes > 2) {
            i2 = (source[srcOffset + 2] << 24) >>> 24;
        }
        int inBuff = i3 | i2;
        switch (numSigBytes) {
            case 1:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = PADDING_BYTE;
                destination[destOffset + 3] = PADDING_BYTE;
                break;
            case 2:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = alphabet[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = PADDING_BYTE;
                break;
            case 3:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = alphabet[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = alphabet[inBuff & 63];
                break;
        }
        return destination;
    }

    @Deprecated
    public static String encode(byte[] source) {
        return encode(source, 0, source.length, ALPHABET, true);
    }

    public static String encode(byte[] source, boolean doPadding) {
        return encode(source, 0, source.length, ALPHABET, doPadding);
    }

    public static String encodeWebSafe(byte[] source, boolean doPadding) {
        return encode(source, 0, source.length, WEBSAFE_ALPHABET, doPadding);
    }

    private static String encode(byte[] source, int off, int len, byte[] alphabet, boolean doPadding) {
        byte[] outBuff = encode(source, off, len, alphabet, Integer.MAX_VALUE);
        int outLen = outBuff.length;
        while (!doPadding && outLen > 0 && outBuff[outLen - 1] == 61) {
            outLen--;
        }
        return new String(outBuff, 0, outLen);
    }

    public static byte[] encode(byte[] source, int off, int len, byte[] alphabet, int maxLineLength) {
        int len43 = ((len + 2) / 3) * 4;
        byte[] outBuff = new byte[((len43 / maxLineLength) + len43)];
        int d = 0;
        int e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        while (d < len2) {
            int inBuff = ((source[d + off] << 24) >>> 8) | ((source[(d + 1) + off] << 24) >>> 16) | ((source[(d + 2) + off] << 24) >>> 24);
            outBuff[e] = alphabet[inBuff >>> 18];
            outBuff[e + 1] = alphabet[(inBuff >>> 12) & 63];
            outBuff[e + 2] = alphabet[(inBuff >>> 6) & 63];
            outBuff[e + 3] = alphabet[inBuff & 63];
            lineLength += 4;
            if (lineLength == maxLineLength) {
                outBuff[e + 4] = NEW_LINE;
                e++;
                lineLength = 0;
            }
            d += 3;
            e += 4;
        }
        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e, alphabet);
            if (lineLength + 4 == maxLineLength) {
                outBuff[e + 4] = NEW_LINE;
                e++;
            }
            int e2 = e + 4;
        }
        return outBuff;
    }

    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, byte[] decodabet) {
        if (source[srcOffset + 2] == 61) {
            destination[destOffset] = (byte) ((((decodabet[source[srcOffset]] << 24) >>> 6) | ((decodabet[source[srcOffset + 1]] << 24) >>> 12)) >>> 16);
            return 1;
        } else if (source[srcOffset + 3] == 61) {
            int outBuff = ((decodabet[source[srcOffset]] << 24) >>> 6) | ((decodabet[source[srcOffset + 1]] << 24) >>> 12) | ((decodabet[source[srcOffset + 2]] << 24) >>> 18);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            int outBuff2 = ((decodabet[source[srcOffset]] << 24) >>> 6) | ((decodabet[source[srcOffset + 1]] << 24) >>> 12) | ((decodabet[source[srcOffset + 2]] << 24) >>> 18) | ((decodabet[source[srcOffset + 3]] << 24) >>> 24);
            destination[destOffset] = (byte) (outBuff2 >> 16);
            destination[destOffset + 1] = (byte) (outBuff2 >> 8);
            destination[destOffset + 2] = (byte) outBuff2;
            return 3;
        }
    }

    public static byte[] decode(String s) throws Base64DecoderException {
        byte[] bytes = s.getBytes();
        return decode(bytes, 0, bytes.length);
    }

    public static byte[] decodeWebSafe(String s) throws Base64DecoderException {
        byte[] bytes = s.getBytes();
        return decodeWebSafe(bytes, 0, bytes.length);
    }

    public static byte[] decode(byte[] source) throws Base64DecoderException {
        return decode(source, 0, source.length);
    }

    public static byte[] decodeWebSafe(byte[] source) throws Base64DecoderException {
        return decodeWebSafe(source, 0, source.length);
    }

    public static byte[] decode(byte[] source, int off, int len) throws Base64DecoderException {
        return decode(source, off, len, DECODABET);
    }

    public static byte[] decodeWebSafe(byte[] source, int off, int len) throws Base64DecoderException {
        return decode(source, off, len, WEBSAFE_DECODABET);
    }

    private static byte[] decode(byte[] source, int off, int len, byte[] decodabet) throws Base64DecoderException {
        byte[] outBuff = new byte[(((len * 3) / 4) + 2)];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int b4Posn = 0;
        boolean paddingByteSeen = false;
        int i = 0;
        while (true) {
            int b4Posn2 = b4Posn;
            if (i < len) {
                byte sbiCrop = (byte) (source[i + off] & Byte.MAX_VALUE);
                byte sbiDecode = decodabet[sbiCrop];
                if (sbiDecode < -5) {
                    throw new Base64DecoderException("Bad Base64 input character at " + i + ": " + source[i + off] + "(decimal)");
                }
                if (sbiDecode < -1) {
                    b4Posn = b4Posn2;
                } else if (sbiCrop == 61) {
                    if (paddingByteSeen) {
                        b4Posn = b4Posn2;
                    } else if (i < 2) {
                        throw new Base64DecoderException("Invalid padding byte found in position " + i);
                    } else {
                        paddingByteSeen = true;
                        byte lastByte = (byte) (source[(len - 1) + off] & Byte.MAX_VALUE);
                        if (lastByte == 61 || lastByte == 10) {
                            b4Posn = b4Posn2;
                        } else {
                            throw new Base64DecoderException("encoded value has invalid trailing byte");
                        }
                    }
                } else if (paddingByteSeen) {
                    throw new Base64DecoderException("Data found after trailing padding byte at index " + i);
                } else {
                    b4Posn = b4Posn2 + 1;
                    b4[b4Posn2] = sbiCrop;
                    if (b4Posn == 4) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);
                        b4Posn = 0;
                    }
                }
                i++;
            } else {
                if (b4Posn2 != 0) {
                    if (b4Posn2 == 1) {
                        throw new Base64DecoderException("single trailing character at offset " + (len - 1));
                    }
                    b4[b4Posn2] = PADDING_BYTE;
                    outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);
                }
                byte[] out = new byte[outBuffPosn];
                System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
                return out;
            }
        }
    }

    public static class Base64DecoderException extends IllegalArgumentException {
        public Base64DecoderException(String s) {
            super(s);
        }
    }
}
