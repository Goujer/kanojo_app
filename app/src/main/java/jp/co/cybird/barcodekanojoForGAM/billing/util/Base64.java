package jp.co.cybird.barcodekanojoForGAM.billing.util;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final byte[] ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] DECODABET;
    public static final boolean DECODE = false;
    public static final boolean ENCODE = true;
    private static final byte EQUALS_SIGN = 61;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte NEW_LINE = 10;
    private static final byte[] WEBSAFE_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] WEBSAFE_DECODABET;
    private static final byte WHITE_SPACE_ENC = -5;

    static {
        boolean z;
        if (!Base64.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
        byte[] bArr = new byte[128];
        bArr[0] = -9;
        bArr[1] = -9;
        bArr[2] = -9;
        bArr[3] = -9;
        bArr[4] = -9;
        bArr[5] = -9;
        bArr[6] = -9;
        bArr[7] = -9;
        bArr[8] = -9;
        bArr[9] = WHITE_SPACE_ENC;
        bArr[10] = WHITE_SPACE_ENC;
        bArr[11] = -9;
        bArr[12] = -9;
        bArr[13] = WHITE_SPACE_ENC;
        bArr[14] = -9;
        bArr[15] = -9;
        bArr[16] = -9;
        bArr[17] = -9;
        bArr[18] = -9;
        bArr[19] = -9;
        bArr[20] = -9;
        bArr[21] = -9;
        bArr[22] = -9;
        bArr[23] = -9;
        bArr[24] = -9;
        bArr[25] = -9;
        bArr[26] = -9;
        bArr[27] = -9;
        bArr[28] = -9;
        bArr[29] = -9;
        bArr[30] = -9;
        bArr[31] = -9;
        bArr[32] = WHITE_SPACE_ENC;
        bArr[33] = -9;
        bArr[34] = -9;
        bArr[35] = -9;
        bArr[36] = -9;
        bArr[37] = -9;
        bArr[38] = -9;
        bArr[39] = -9;
        bArr[40] = -9;
        bArr[41] = -9;
        bArr[42] = -9;
        bArr[43] = 62;
        bArr[44] = -9;
        bArr[45] = -9;
        bArr[46] = -9;
        bArr[47] = 63;
        bArr[48] = 52;
        bArr[49] = 53;
        bArr[50] = 54;
        bArr[51] = 55;
        bArr[52] = 56;
        bArr[53] = 57;
        bArr[54] = 58;
        bArr[55] = 59;
        bArr[56] = 60;
        bArr[57] = EQUALS_SIGN;
        bArr[58] = -9;
        bArr[59] = -9;
        bArr[60] = -9;
        bArr[61] = EQUALS_SIGN_ENC;
        bArr[62] = -9;
        bArr[63] = -9;
        bArr[64] = -9;
        bArr[66] = 1;
        bArr[67] = 2;
        bArr[68] = 3;
        bArr[69] = 4;
        bArr[70] = 5;
        bArr[71] = 6;
        bArr[72] = 7;
        bArr[73] = 8;
        bArr[74] = 9;
        bArr[75] = NEW_LINE;
        bArr[76] = 11;
        bArr[77] = 12;
        bArr[78] = 13;
        bArr[79] = 14;
        bArr[80] = 15;
        bArr[81] = 16;
        bArr[82] = 17;
        bArr[83] = 18;
        bArr[84] = 19;
        bArr[85] = 20;
        bArr[86] = 21;
        bArr[87] = 22;
        bArr[88] = 23;
        bArr[89] = 24;
        bArr[90] = 25;
        bArr[91] = -9;
        bArr[92] = -9;
        bArr[93] = -9;
        bArr[94] = -9;
        bArr[95] = -9;
        bArr[96] = -9;
        bArr[97] = 26;
        bArr[98] = 27;
        bArr[99] = 28;
        bArr[100] = 29;
        bArr[101] = 30;
        bArr[102] = 31;
        bArr[103] = 32;
        bArr[104] = 33;
        bArr[105] = 34;
        bArr[106] = 35;
        bArr[107] = 36;
        bArr[108] = 37;
        bArr[109] = 38;
        bArr[110] = 39;
        bArr[111] = 40;
        bArr[112] = 41;
        bArr[113] = 42;
        bArr[114] = 43;
        bArr[115] = 44;
        bArr[116] = 45;
        bArr[117] = 46;
        bArr[118] = 47;
        bArr[119] = 48;
        bArr[120] = 49;
        bArr[121] = 50;
        bArr[122] = 51;
        bArr[123] = -9;
        bArr[124] = -9;
        bArr[125] = -9;
        bArr[126] = -9;
        bArr[127] = -9;
        DECODABET = bArr;
        byte[] bArr2 = new byte[128];
        bArr2[0] = -9;
        bArr2[1] = -9;
        bArr2[2] = -9;
        bArr2[3] = -9;
        bArr2[4] = -9;
        bArr2[5] = -9;
        bArr2[6] = -9;
        bArr2[7] = -9;
        bArr2[8] = -9;
        bArr2[9] = WHITE_SPACE_ENC;
        bArr2[10] = WHITE_SPACE_ENC;
        bArr2[11] = -9;
        bArr2[12] = -9;
        bArr2[13] = WHITE_SPACE_ENC;
        bArr2[14] = -9;
        bArr2[15] = -9;
        bArr2[16] = -9;
        bArr2[17] = -9;
        bArr2[18] = -9;
        bArr2[19] = -9;
        bArr2[20] = -9;
        bArr2[21] = -9;
        bArr2[22] = -9;
        bArr2[23] = -9;
        bArr2[24] = -9;
        bArr2[25] = -9;
        bArr2[26] = -9;
        bArr2[27] = -9;
        bArr2[28] = -9;
        bArr2[29] = -9;
        bArr2[30] = -9;
        bArr2[31] = -9;
        bArr2[32] = WHITE_SPACE_ENC;
        bArr2[33] = -9;
        bArr2[34] = -9;
        bArr2[35] = -9;
        bArr2[36] = -9;
        bArr2[37] = -9;
        bArr2[38] = -9;
        bArr2[39] = -9;
        bArr2[40] = -9;
        bArr2[41] = -9;
        bArr2[42] = -9;
        bArr2[43] = -9;
        bArr2[44] = -9;
        bArr2[45] = 62;
        bArr2[46] = -9;
        bArr2[47] = -9;
        bArr2[48] = 52;
        bArr2[49] = 53;
        bArr2[50] = 54;
        bArr2[51] = 55;
        bArr2[52] = 56;
        bArr2[53] = 57;
        bArr2[54] = 58;
        bArr2[55] = 59;
        bArr2[56] = 60;
        bArr2[57] = EQUALS_SIGN;
        bArr2[58] = -9;
        bArr2[59] = -9;
        bArr2[60] = -9;
        bArr2[61] = EQUALS_SIGN_ENC;
        bArr2[62] = -9;
        bArr2[63] = -9;
        bArr2[64] = -9;
        bArr2[66] = 1;
        bArr2[67] = 2;
        bArr2[68] = 3;
        bArr2[69] = 4;
        bArr2[70] = 5;
        bArr2[71] = 6;
        bArr2[72] = 7;
        bArr2[73] = 8;
        bArr2[74] = 9;
        bArr2[75] = NEW_LINE;
        bArr2[76] = 11;
        bArr2[77] = 12;
        bArr2[78] = 13;
        bArr2[79] = 14;
        bArr2[80] = 15;
        bArr2[81] = 16;
        bArr2[82] = 17;
        bArr2[83] = 18;
        bArr2[84] = 19;
        bArr2[85] = 20;
        bArr2[86] = 21;
        bArr2[87] = 22;
        bArr2[88] = 23;
        bArr2[89] = 24;
        bArr2[90] = 25;
        bArr2[91] = -9;
        bArr2[92] = -9;
        bArr2[93] = -9;
        bArr2[94] = -9;
        bArr2[95] = 63;
        bArr2[96] = -9;
        bArr2[97] = 26;
        bArr2[98] = 27;
        bArr2[99] = 28;
        bArr2[100] = 29;
        bArr2[101] = 30;
        bArr2[102] = 31;
        bArr2[103] = 32;
        bArr2[104] = 33;
        bArr2[105] = 34;
        bArr2[106] = 35;
        bArr2[107] = 36;
        bArr2[108] = 37;
        bArr2[109] = 38;
        bArr2[110] = 39;
        bArr2[111] = 40;
        bArr2[112] = 41;
        bArr2[113] = 42;
        bArr2[114] = 43;
        bArr2[115] = 44;
        bArr2[116] = 45;
        bArr2[117] = 46;
        bArr2[118] = 47;
        bArr2[119] = 48;
        bArr2[120] = 49;
        bArr2[121] = 50;
        bArr2[122] = 51;
        bArr2[123] = -9;
        bArr2[124] = -9;
        bArr2[125] = -9;
        bArr2[126] = -9;
        bArr2[127] = -9;
        WEBSAFE_DECODABET = bArr2;
    }

    private Base64() {
    }

    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, byte[] alphabet) {
        int i;
        int i2;
        int i3 = 0;
        if (numSigBytes > 0) {
            i = (source[srcOffset] << 24) >>> 8;
        } else {
            i = 0;
        }
        if (numSigBytes > 1) {
            i2 = (source[srcOffset + 1] << 24) >>> 16;
        } else {
            i2 = 0;
        }
        int i4 = i2 | i;
        if (numSigBytes > 2) {
            i3 = (source[srcOffset + 2] << 24) >>> 24;
        }
        int inBuff = i4 | i3;
        switch (numSigBytes) {
            case 1:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = EQUALS_SIGN;
                destination[destOffset + 3] = EQUALS_SIGN;
                break;
            case 2:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = alphabet[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = EQUALS_SIGN;
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

    public static String encode(byte[] source) {
        return encode(source, 0, source.length, ALPHABET, true);
    }

    public static String encodeWebSafe(byte[] source, boolean doPadding) {
        return encode(source, 0, source.length, WEBSAFE_ALPHABET, doPadding);
    }

    public static String encode(byte[] source, int off, int len, byte[] alphabet, boolean doPadding) {
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
            e += 4;
        }
        if ($assertionsDisabled || e == outBuff.length) {
            return outBuff;
        }
        throw new AssertionError();
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

    public static byte[] decode(byte[] source, int off, int len, byte[] decodabet) throws Base64DecoderException {
        int b4Posn;
        byte[] outBuff = new byte[(((len * 3) / 4) + 2)];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int b4Posn2 = 0;
        int i = 0;
        while (true) {
            b4Posn = b4Posn2;
            if (i >= len) {
                break;
            }
            byte sbiCrop = (byte) (source[i + off] & Byte.MAX_VALUE);
            byte sbiDecode = decodabet[sbiCrop];
            if (sbiDecode >= -5) {
                if (sbiDecode < -1) {
                    b4Posn2 = b4Posn;
                } else if (sbiCrop == 61) {
                    int bytesLeft = len - i;
                    byte lastByte = (byte) (source[(len - 1) + off] & Byte.MAX_VALUE);
                    if (b4Posn == 0 || b4Posn == 1) {
                        throw new Base64DecoderException("invalid padding byte '=' at byte offset " + i);
                    } else if ((b4Posn == 3 && bytesLeft > 2) || (b4Posn == 4 && bytesLeft > 1)) {
                        throw new Base64DecoderException("padding byte '=' falsely signals end of encoded value at offset " + i);
                    } else if (lastByte != 61 && lastByte != 10) {
                        throw new Base64DecoderException("encoded value has invalid trailing byte");
                    }
                } else {
                    b4Posn2 = b4Posn + 1;
                    b4[b4Posn] = sbiCrop;
                    if (b4Posn2 == 4) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);
                        b4Posn2 = 0;
                    }
                }
                i++;
            } else {
                throw new Base64DecoderException("Bad Base64 input character at " + i + ": " + source[i + off] + "(decimal)");
            }
        }
        if (b4Posn == 0) {
        } else if (b4Posn == 1) {
            throw new Base64DecoderException("single trailing character at offset " + (len - 1));
        } else {
            int i2 = b4Posn + 1;
            b4[b4Posn] = EQUALS_SIGN;
            outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);
        }
        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }
}
