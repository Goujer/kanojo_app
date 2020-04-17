package com.google.zxing.aztec.encoder;

import android.support.v4.view.MotionEventCompat;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;
import java.lang.reflect.Array;
import java.util.Arrays;

public final class Encoder {
    private static final int[][] CHAR_MAP = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{5, 256}));
    public static final int DEFAULT_EC_PERCENT = 33;
    private static final int[][] LATCH_TABLE = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{6, 6}));
    private static final int[] NB_BITS = new int[33];
    private static final int[] NB_BITS_COMPACT = new int[5];
    private static final int[][] SHIFT_TABLE = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{6, 6}));
    private static final int TABLE_BINARY = 5;
    private static final int TABLE_DIGIT = 2;
    private static final int TABLE_LOWER = 1;
    private static final int TABLE_MIXED = 3;
    private static final int TABLE_PUNCT = 4;
    private static final int TABLE_UPPER = 0;
    private static final int[] WORD_SIZE = {4, 6, 6, 8, 8, 8, 8, 8, 8, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};

    static {
        CHAR_MAP[0][32] = 1;
        for (int c = 65; c <= 90; c++) {
            CHAR_MAP[0][c] = (c - 65) + 2;
        }
        CHAR_MAP[1][32] = 1;
        for (int c2 = 97; c2 <= 122; c2++) {
            CHAR_MAP[1][c2] = (c2 - 97) + 2;
        }
        CHAR_MAP[2][32] = 1;
        for (int c3 = 48; c3 <= 57; c3++) {
            CHAR_MAP[2][c3] = (c3 - 48) + 2;
        }
        CHAR_MAP[2][44] = 12;
        CHAR_MAP[2][46] = 13;
        int[] mixedTable = {0, 32, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 27, 28, 29, 30, 31, 64, 92, 94, 95, 96, 124, 126, 127};
        for (int i = 0; i < mixedTable.length; i++) {
            CHAR_MAP[3][mixedTable[i]] = i;
        }
        int[] punctTable = {0, 13, 0, 0, 0, 0, 33, 39, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 58, 59, 60, 61, 62, 63, 91, 93, 123, 125};
        for (int i2 = 0; i2 < punctTable.length; i2++) {
            if (punctTable[i2] > 0) {
                CHAR_MAP[4][punctTable[i2]] = i2;
            }
        }
        for (int[] table : SHIFT_TABLE) {
            Arrays.fill(table, -1);
        }
        for (int[] table2 : LATCH_TABLE) {
            Arrays.fill(table2, -1);
        }
        SHIFT_TABLE[0][4] = 0;
        LATCH_TABLE[0][1] = 28;
        LATCH_TABLE[0][3] = 29;
        LATCH_TABLE[0][2] = 30;
        SHIFT_TABLE[0][5] = 31;
        SHIFT_TABLE[1][4] = 0;
        SHIFT_TABLE[1][0] = 28;
        LATCH_TABLE[1][3] = 29;
        LATCH_TABLE[1][2] = 30;
        SHIFT_TABLE[1][5] = 31;
        SHIFT_TABLE[3][4] = 0;
        LATCH_TABLE[3][1] = 28;
        LATCH_TABLE[3][0] = 29;
        LATCH_TABLE[3][4] = 30;
        SHIFT_TABLE[3][5] = 31;
        LATCH_TABLE[4][0] = 31;
        SHIFT_TABLE[2][4] = 0;
        LATCH_TABLE[2][0] = 30;
        SHIFT_TABLE[2][0] = 31;
        for (int i3 = 1; i3 < NB_BITS_COMPACT.length; i3++) {
            NB_BITS_COMPACT[i3] = ((i3 * 16) + 88) * i3;
        }
        for (int i4 = 1; i4 < NB_BITS.length; i4++) {
            NB_BITS[i4] = ((i4 * 16) + 112) * i4;
        }
    }

    private Encoder() {
    }

    public static AztecCode encode(byte[] data) {
        return encode(data, 33);
    }

    public static AztecCode encode(byte[] data, int minECCPercent) {
        int layers;
        int matrixSize;
        BitArray bits = highLevelEncode(data);
        int eccBits = ((bits.getSize() * minECCPercent) / 100) + 11;
        int totalSizeBits = bits.getSize() + eccBits;
        int wordSize = 0;
        int totalSymbolBits = 0;
        BitArray stuffedBits = null;
        int layers2 = 1;
        while (layers < NB_BITS_COMPACT.length) {
            if (NB_BITS_COMPACT[layers] >= totalSizeBits) {
                if (wordSize != WORD_SIZE[layers]) {
                    wordSize = WORD_SIZE[layers];
                    stuffedBits = stuffBits(bits, wordSize);
                }
                totalSymbolBits = NB_BITS_COMPACT[layers];
                if (stuffedBits.getSize() + eccBits <= NB_BITS_COMPACT[layers]) {
                    break;
                }
            }
            layers2 = layers + 1;
        }
        boolean compact = true;
        if (layers == NB_BITS_COMPACT.length) {
            compact = false;
            layers = 1;
            while (layers < NB_BITS.length) {
                if (NB_BITS[layers] >= totalSizeBits) {
                    if (wordSize != WORD_SIZE[layers]) {
                        wordSize = WORD_SIZE[layers];
                        stuffedBits = stuffBits(bits, wordSize);
                    }
                    totalSymbolBits = NB_BITS[layers];
                    if (stuffedBits.getSize() + eccBits <= NB_BITS[layers]) {
                        break;
                    }
                }
                layers++;
            }
        }
        if (layers == NB_BITS.length) {
            throw new IllegalArgumentException("Data too large for an Aztec code");
        }
        int messageSizeInWords = ((stuffedBits.getSize() + wordSize) - 1) / wordSize;
        ReedSolomonEncoder reedSolomonEncoder = new ReedSolomonEncoder(getGF(wordSize));
        int totalSizeInFullWords = totalSymbolBits / wordSize;
        int[] messageWords = bitsToWords(stuffedBits, wordSize, totalSizeInFullWords);
        reedSolomonEncoder.encode(messageWords, totalSizeInFullWords - messageSizeInWords);
        BitArray messageBits = new BitArray();
        messageBits.appendBits(0, totalSymbolBits % wordSize);
        for (int messageWord : messageWords) {
            messageBits.appendBits(messageWord, wordSize);
        }
        BitArray modeMessage = generateModeMessage(compact, layers, messageSizeInWords);
        int baseMatrixSize = compact ? (layers * 4) + 11 : (layers * 4) + 14;
        int[] alignmentMap = new int[baseMatrixSize];
        if (compact) {
            matrixSize = baseMatrixSize;
            for (int i = 0; i < alignmentMap.length; i++) {
                alignmentMap[i] = i;
            }
        } else {
            matrixSize = baseMatrixSize + 1 + ((((baseMatrixSize / 2) - 1) / 15) * 2);
            int origCenter = baseMatrixSize / 2;
            int center = matrixSize / 2;
            for (int i2 = 0; i2 < origCenter; i2++) {
                int newOffset = i2 + (i2 / 15);
                alignmentMap[(origCenter - i2) - 1] = (center - newOffset) - 1;
                alignmentMap[origCenter + i2] = center + newOffset + 1;
            }
        }
        BitMatrix matrix = new BitMatrix(matrixSize);
        int rowOffset = 0;
        for (int i3 = 0; i3 < layers; i3++) {
            int rowSize = compact ? ((layers - i3) * 4) + 9 : ((layers - i3) * 4) + 12;
            for (int j = 0; j < rowSize; j++) {
                int columnOffset = j * 2;
                for (int k = 0; k < 2; k++) {
                    if (messageBits.get(rowOffset + columnOffset + k)) {
                        matrix.set(alignmentMap[(i3 * 2) + k], alignmentMap[(i3 * 2) + j]);
                    }
                    if (messageBits.get((rowSize * 2) + rowOffset + columnOffset + k)) {
                        matrix.set(alignmentMap[(i3 * 2) + j], alignmentMap[((baseMatrixSize - 1) - (i3 * 2)) - k]);
                    }
                    if (messageBits.get((rowSize * 4) + rowOffset + columnOffset + k)) {
                        matrix.set(alignmentMap[((baseMatrixSize - 1) - (i3 * 2)) - k], alignmentMap[((baseMatrixSize - 1) - (i3 * 2)) - j]);
                    }
                    if (messageBits.get((rowSize * 6) + rowOffset + columnOffset + k)) {
                        matrix.set(alignmentMap[((baseMatrixSize - 1) - (i3 * 2)) - j], alignmentMap[(i3 * 2) + k]);
                    }
                }
            }
            rowOffset += rowSize * 8;
        }
        drawModeMessage(matrix, compact, matrixSize, modeMessage);
        if (compact) {
            drawBullsEye(matrix, matrixSize / 2, 5);
        } else {
            drawBullsEye(matrix, matrixSize / 2, 7);
            int i4 = 0;
            int j2 = 0;
            while (i4 < (baseMatrixSize / 2) - 1) {
                for (int k2 = (matrixSize / 2) & 1; k2 < matrixSize; k2 += 2) {
                    matrix.set((matrixSize / 2) - j2, k2);
                    matrix.set((matrixSize / 2) + j2, k2);
                    matrix.set(k2, (matrixSize / 2) - j2);
                    matrix.set(k2, (matrixSize / 2) + j2);
                }
                i4 += 15;
                j2 += 16;
            }
        }
        AztecCode aztec = new AztecCode();
        aztec.setCompact(compact);
        aztec.setSize(matrixSize);
        aztec.setLayers(layers);
        aztec.setCodeWords(messageSizeInWords);
        aztec.setMatrix(matrix);
        return aztec;
    }

    static void drawBullsEye(BitMatrix matrix, int center, int size) {
        for (int i = 0; i < size; i += 2) {
            for (int j = center - i; j <= center + i; j++) {
                matrix.set(j, center - i);
                matrix.set(j, center + i);
                matrix.set(center - i, j);
                matrix.set(center + i, j);
            }
        }
        matrix.set(center - size, center - size);
        matrix.set((center - size) + 1, center - size);
        matrix.set(center - size, (center - size) + 1);
        matrix.set(center + size, center - size);
        matrix.set(center + size, (center - size) + 1);
        matrix.set(center + size, (center + size) - 1);
    }

    static BitArray generateModeMessage(boolean compact, int layers, int messageSizeInWords) {
        BitArray modeMessage = new BitArray();
        if (compact) {
            modeMessage.appendBits(layers - 1, 2);
            modeMessage.appendBits(messageSizeInWords - 1, 6);
            return generateCheckWords(modeMessage, 28, 4);
        }
        modeMessage.appendBits(layers - 1, 5);
        modeMessage.appendBits(messageSizeInWords - 1, 11);
        return generateCheckWords(modeMessage, 40, 4);
    }

    static void drawModeMessage(BitMatrix matrix, boolean compact, int matrixSize, BitArray modeMessage) {
        if (compact) {
            for (int i = 0; i < 7; i++) {
                if (modeMessage.get(i)) {
                    matrix.set(((matrixSize / 2) - 3) + i, (matrixSize / 2) - 5);
                }
                if (modeMessage.get(i + 7)) {
                    matrix.set((matrixSize / 2) + 5, ((matrixSize / 2) - 3) + i);
                }
                if (modeMessage.get(20 - i)) {
                    matrix.set(((matrixSize / 2) - 3) + i, (matrixSize / 2) + 5);
                }
                if (modeMessage.get(27 - i)) {
                    matrix.set((matrixSize / 2) - 5, ((matrixSize / 2) - 3) + i);
                }
            }
            return;
        }
        for (int i2 = 0; i2 < 10; i2++) {
            if (modeMessage.get(i2)) {
                matrix.set(((matrixSize / 2) - 5) + i2 + (i2 / 5), (matrixSize / 2) - 7);
            }
            if (modeMessage.get(i2 + 10)) {
                matrix.set((matrixSize / 2) + 7, ((matrixSize / 2) - 5) + i2 + (i2 / 5));
            }
            if (modeMessage.get(29 - i2)) {
                matrix.set(((matrixSize / 2) - 5) + i2 + (i2 / 5), (matrixSize / 2) + 7);
            }
            if (modeMessage.get(39 - i2)) {
                matrix.set((matrixSize / 2) - 7, ((matrixSize / 2) - 5) + i2 + (i2 / 5));
            }
        }
    }

    static BitArray generateCheckWords(BitArray stuffedBits, int totalSymbolBits, int wordSize) {
        int messageSizeInWords = ((stuffedBits.getSize() + wordSize) - 1) / wordSize;
        for (int i = (messageSizeInWords * wordSize) - stuffedBits.getSize(); i > 0; i--) {
            stuffedBits.appendBit(true);
        }
        ReedSolomonEncoder rs = new ReedSolomonEncoder(getGF(wordSize));
        int totalSizeInFullWords = totalSymbolBits / wordSize;
        int[] messageWords = bitsToWords(stuffedBits, wordSize, totalSizeInFullWords);
        rs.encode(messageWords, totalSizeInFullWords - messageSizeInWords);
        BitArray messageBits = new BitArray();
        messageBits.appendBits(0, totalSymbolBits % wordSize);
        for (int messageWord : messageWords) {
            messageBits.appendBits(messageWord, wordSize);
        }
        return messageBits;
    }

    static int[] bitsToWords(BitArray stuffedBits, int wordSize, int totalWords) {
        int[] message = new int[totalWords];
        int n = stuffedBits.getSize() / wordSize;
        for (int i = 0; i < n; i++) {
            int value = 0;
            for (int j = 0; j < wordSize; j++) {
                value |= stuffedBits.get((i * wordSize) + j) ? 1 << ((wordSize - j) - 1) : 0;
            }
            message[i] = value;
        }
        return message;
    }

    static GenericGF getGF(int wordSize) {
        switch (wordSize) {
            case 4:
                return GenericGF.AZTEC_PARAM;
            case 6:
                return GenericGF.AZTEC_DATA_6;
            case 8:
                return GenericGF.AZTEC_DATA_8;
            case 10:
                return GenericGF.AZTEC_DATA_10;
            case 12:
                return GenericGF.AZTEC_DATA_12;
            default:
                return null;
        }
    }

    static BitArray stuffBits(BitArray bits, int wordSize) {
        BitArray out = new BitArray();
        int n = bits.getSize();
        int mask = (1 << wordSize) - 2;
        int i = 0;
        while (i < n) {
            int word = 0;
            for (int j = 0; j < wordSize; j++) {
                if (i + j >= n || bits.get(i + j)) {
                    word |= 1 << ((wordSize - 1) - j);
                }
            }
            if ((word & mask) == mask) {
                out.appendBits(word & mask, wordSize);
                i--;
            } else if ((word & mask) == 0) {
                out.appendBits(word | 1, wordSize);
                i--;
            } else {
                out.appendBits(word, wordSize);
            }
            i += wordSize;
        }
        return out;
    }

    static BitArray highLevelEncode(byte[] data) {
        BitArray bits = new BitArray();
        int mode = 0;
        int[] idx = new int[5];
        int[] idxnext = new int[5];
        int i = 0;
        while (i < data.length) {
            int c = data[i] & MotionEventCompat.ACTION_MASK;
            int next = i < data.length + -1 ? data[i + 1] & MotionEventCompat.ACTION_MASK : 0;
            int punctWord = 0;
            if (c == 13 && next == 10) {
                punctWord = 2;
            } else if (c == 46 && next == 32) {
                punctWord = 3;
            } else if (c == 44 && next == 32) {
                punctWord = 4;
            } else if (c == 58 && next == 32) {
                punctWord = 5;
            }
            if (punctWord > 0) {
                if (mode == 4) {
                    outputWord(bits, 4, punctWord);
                    i++;
                } else if (SHIFT_TABLE[mode][4] >= 0) {
                    outputWord(bits, mode, SHIFT_TABLE[mode][4]);
                    outputWord(bits, 4, punctWord);
                    i++;
                } else if (LATCH_TABLE[mode][4] >= 0) {
                    outputWord(bits, mode, LATCH_TABLE[mode][4]);
                    outputWord(bits, 4, punctWord);
                    mode = 4;
                    i++;
                }
                i++;
            }
            int firstMatch = -1;
            int shiftMode = -1;
            int latchMode = -1;
            for (int j = 0; j < 5; j++) {
                idx[j] = CHAR_MAP[j][c];
                if (idx[j] > 0 && firstMatch < 0) {
                    firstMatch = j;
                }
                if (shiftMode < 0 && idx[j] > 0 && SHIFT_TABLE[mode][j] >= 0) {
                    shiftMode = j;
                }
                idxnext[j] = CHAR_MAP[j][next];
                if (latchMode < 0 && idx[j] > 0 && ((next == 0 || idxnext[j] > 0) && LATCH_TABLE[mode][j] >= 0)) {
                    latchMode = j;
                }
            }
            if (shiftMode < 0 && latchMode < 0) {
                int j2 = 0;
                while (true) {
                    if (j2 < 5) {
                        if (idx[j2] <= 0 || LATCH_TABLE[mode][j2] < 0) {
                            j2++;
                        } else {
                            latchMode = j2;
                        }
                    }
                }
            }
            if (idx[mode] > 0) {
                outputWord(bits, mode, idx[mode]);
            } else if (latchMode >= 0) {
                outputWord(bits, mode, LATCH_TABLE[mode][latchMode]);
                outputWord(bits, latchMode, idx[latchMode]);
                mode = latchMode;
            } else if (shiftMode >= 0) {
                outputWord(bits, mode, SHIFT_TABLE[mode][shiftMode]);
                outputWord(bits, shiftMode, idx[shiftMode]);
            } else {
                if (firstMatch >= 0) {
                    if (mode == 4) {
                        outputWord(bits, 4, LATCH_TABLE[4][0]);
                        mode = 0;
                        i--;
                    } else if (mode == 2) {
                        outputWord(bits, 2, LATCH_TABLE[2][0]);
                        mode = 0;
                        i--;
                    }
                }
                int k = i + 1;
                int lookahead = 0;
                while (true) {
                    if (k < data.length) {
                        int next2 = data[k] & MotionEventCompat.ACTION_MASK;
                        boolean binary = true;
                        int j3 = 0;
                        while (true) {
                            if (j3 < 5) {
                                if (CHAR_MAP[j3][next2] > 0) {
                                    binary = false;
                                } else {
                                    j3++;
                                }
                            }
                        }
                        if (binary) {
                            lookahead = 0;
                        } else if (lookahead >= 1) {
                            k -= lookahead;
                        } else {
                            lookahead++;
                        }
                        k++;
                    }
                }
                int k2 = k - i;
                switch (mode) {
                    case 0:
                    case 1:
                    case 3:
                        outputWord(bits, mode, SHIFT_TABLE[mode][5]);
                        break;
                    case 2:
                        outputWord(bits, mode, LATCH_TABLE[mode][0]);
                        mode = 0;
                        outputWord(bits, 0, SHIFT_TABLE[0][5]);
                        break;
                    case 4:
                        outputWord(bits, mode, LATCH_TABLE[mode][0]);
                        mode = 0;
                        outputWord(bits, 0, SHIFT_TABLE[0][5]);
                        break;
                }
                if (k2 >= 32 && k2 < 63) {
                    k2 = 31;
                }
                if (k2 > 542) {
                    k2 = 542;
                }
                if (k2 < 32) {
                    bits.appendBits(k2, 5);
                } else {
                    bits.appendBits(k2 - 31, 16);
                }
                while (k2 > 0) {
                    bits.appendBits(data[i], 8);
                    k2--;
                    i++;
                }
                i--;
            }
            i++;
        }
        return bits;
    }

    static void outputWord(BitArray bits, int mode, int value) {
        if (mode == 2) {
            bits.appendBits(value, 4);
        } else if (mode < 5) {
            bits.appendBits(value, 5);
        } else {
            bits.appendBits(value, 8);
        }
    }
}
