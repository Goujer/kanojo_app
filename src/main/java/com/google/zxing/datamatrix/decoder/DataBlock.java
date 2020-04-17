package com.google.zxing.datamatrix.decoder;

import com.google.zxing.datamatrix.decoder.Version;

final class DataBlock {
    private final byte[] codewords;
    private final int numDataCodewords;

    private DataBlock(int numDataCodewords2, byte[] codewords2) {
        this.numDataCodewords = numDataCodewords2;
        this.codewords = codewords2;
    }

    static DataBlock[] getDataBlocks(byte[] rawCodewords, Version version) {
        int numLongerBlocks;
        int rawCodewordsOffset;
        int rawCodewordsOffset2;
        int iOffset;
        int rawCodewordsOffset3;
        Version.ECBlocks ecBlocks = version.getECBlocks();
        int totalBlocks = 0;
        Version.ECB[] ecBlockArray = ecBlocks.getECBlocks();
        for (Version.ECB ecBlock : ecBlockArray) {
            totalBlocks += ecBlock.getCount();
        }
        DataBlock[] result = new DataBlock[totalBlocks];
        int numResultBlocks = 0;
        for (Version.ECB ecBlock2 : ecBlockArray) {
            int i = 0;
            while (i < ecBlock2.getCount()) {
                int numDataCodewords2 = ecBlock2.getDataCodewords();
                result[numResultBlocks] = new DataBlock(numDataCodewords2, new byte[(ecBlocks.getECCodewords() + numDataCodewords2)]);
                i++;
                numResultBlocks++;
            }
        }
        int longerBlocksNumDataCodewords = result[0].codewords.length - ecBlocks.getECCodewords();
        int shorterBlocksNumDataCodewords = longerBlocksNumDataCodewords - 1;
        int rawCodewordsOffset4 = 0;
        int i2 = 0;
        while (i2 < shorterBlocksNumDataCodewords) {
            int j = 0;
            while (true) {
                rawCodewordsOffset3 = rawCodewordsOffset4;
                if (j >= numResultBlocks) {
                    break;
                }
                rawCodewordsOffset4 = rawCodewordsOffset3 + 1;
                result[j].codewords[i2] = rawCodewords[rawCodewordsOffset3];
                j++;
            }
            i2++;
            rawCodewordsOffset4 = rawCodewordsOffset3;
        }
        boolean specialVersion = version.getVersionNumber() == 24;
        if (specialVersion) {
            numLongerBlocks = 8;
        } else {
            numLongerBlocks = numResultBlocks;
        }
        int j2 = 0;
        while (true) {
            rawCodewordsOffset = rawCodewordsOffset4;
            if (j2 >= numLongerBlocks) {
                break;
            }
            rawCodewordsOffset4 = rawCodewordsOffset + 1;
            result[j2].codewords[longerBlocksNumDataCodewords - 1] = rawCodewords[rawCodewordsOffset];
            j2++;
        }
        int max = result[0].codewords.length;
        int i3 = longerBlocksNumDataCodewords;
        int rawCodewordsOffset5 = rawCodewordsOffset;
        while (i3 < max) {
            int j3 = 0;
            while (true) {
                rawCodewordsOffset2 = rawCodewordsOffset5;
                if (j3 >= numResultBlocks) {
                    break;
                }
                if (!specialVersion || j3 <= 7) {
                    iOffset = i3;
                } else {
                    iOffset = i3 - 1;
                }
                rawCodewordsOffset5 = rawCodewordsOffset2 + 1;
                result[j3].codewords[iOffset] = rawCodewords[rawCodewordsOffset2];
                j3++;
            }
            i3++;
            rawCodewordsOffset5 = rawCodewordsOffset2;
        }
        if (rawCodewordsOffset5 == rawCodewords.length) {
            return result;
        }
        throw new IllegalArgumentException();
    }

    /* access modifiers changed from: package-private */
    public int getNumDataCodewords() {
        return this.numDataCodewords;
    }

    /* access modifiers changed from: package-private */
    public byte[] getCodewords() {
        return this.codewords;
    }
}
