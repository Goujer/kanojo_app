package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.Map;

public final class CodaBarReader extends OneDReader {
    static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    private static final String ALPHABET_STRING = "0123456789-$:/.+ABCD";
    static final int[] CHARACTER_ENCODINGS = {3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14};
    private static final int MAX_ACCEPTABLE = 512;
    private static final int MIN_CHARACTER_LENGTH = 3;
    private static final int PADDING = 384;
    private static final char[] STARTEND_ENCODING = {'A', 'B', 'C', 'D'};
    private int counterLength = 0;
    private int[] counters = new int[80];
    private final StringBuilder decodeRowResult = new StringBuilder(20);

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0025 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x002a  */
    public Result decodeRow(int rowNumber, BitArray row, Map<DecodeHintType, ?> map) throws NotFoundException {
        Arrays.fill(this.counters, 0);
        setCounters(row);
        int startOffset = findStartPattern();
        int nextStart = startOffset;
        this.decodeRowResult.setLength(0);
        do {
            int charOffset = toNarrowWidePattern(nextStart);
            if (charOffset != -1) {
                throw NotFoundException.getNotFoundInstance();
            }
            this.decodeRowResult.append((char) charOffset);
            nextStart += 8;
            if ((this.decodeRowResult.length() > 1 && arrayContains(STARTEND_ENCODING, ALPHABET[charOffset])) || nextStart >= this.counterLength) {
                int trailingWhitespace = this.counters[nextStart - 1];
                int lastPatternSize = 0;
            }
            int charOffset2 = toNarrowWidePattern(nextStart);
            if (charOffset2 != -1) {
            }
        } while (nextStart >= this.counterLength);
        int trailingWhitespace2 = this.counters[nextStart - 1];
        int lastPatternSize2 = 0;
        for (int i = -8; i < -1; i++) {
            lastPatternSize2 += this.counters[nextStart + i];
        }
        if (nextStart >= this.counterLength || trailingWhitespace2 >= lastPatternSize2 / 2) {
            validatePattern(startOffset);
            for (int i2 = 0; i2 < this.decodeRowResult.length(); i2++) {
                this.decodeRowResult.setCharAt(i2, ALPHABET[this.decodeRowResult.charAt(i2)]);
            }
            if (!arrayContains(STARTEND_ENCODING, this.decodeRowResult.charAt(0))) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (!arrayContains(STARTEND_ENCODING, this.decodeRowResult.charAt(this.decodeRowResult.length() - 1))) {
                throw NotFoundException.getNotFoundInstance();
            } else if (this.decodeRowResult.length() <= 3) {
                throw NotFoundException.getNotFoundInstance();
            } else {
                this.decodeRowResult.deleteCharAt(this.decodeRowResult.length() - 1);
                this.decodeRowResult.deleteCharAt(0);
                int runningCount = 0;
                for (int i3 = 0; i3 < startOffset; i3++) {
                    runningCount += this.counters[i3];
                }
                float left = (float) runningCount;
                for (int i4 = startOffset; i4 < nextStart - 1; i4++) {
                    runningCount += this.counters[i4];
                }
                return new Result(this.decodeRowResult.toString(), (byte[]) null, new ResultPoint[]{new ResultPoint(left, (float) rowNumber), new ResultPoint((float) runningCount, (float) rowNumber)}, BarcodeFormat.CODABAR);
            }
        } else {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    /* access modifiers changed from: package-private */
    public void validatePattern(int start) throws NotFoundException {
        int[] sizes = {0, 0, 0, 0};
        int[] counts = {0, 0, 0, 0};
        int end = this.decodeRowResult.length() - 1;
        int pos = start;
        int i = 0;
        while (true) {
            int pattern = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i)];
            for (int j = 6; j >= 0; j--) {
                int category = (j & 1) + ((pattern & 1) * 2);
                sizes[category] = sizes[category] + this.counters[pos + j];
                counts[category] = counts[category] + 1;
                pattern >>= 1;
            }
            if (i >= end) {
                break;
            }
            pos += 8;
            i++;
        }
        int[] maxes = new int[4];
        int[] mins = new int[4];
        for (int i2 = 0; i2 < 2; i2++) {
            mins[i2] = 0;
            mins[i2 + 2] = (((sizes[i2] << 8) / counts[i2]) + ((sizes[i2 + 2] << 8) / counts[i2 + 2])) >> 1;
            maxes[i2] = mins[i2 + 2];
            maxes[i2 + 2] = ((sizes[i2 + 2] * 512) + PADDING) / counts[i2 + 2];
        }
        int pos2 = start;
        int i3 = 0;
        loop3:
        while (true) {
            int pattern2 = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i3)];
            int j2 = 6;
            while (j2 >= 0) {
                int category2 = (j2 & 1) + ((pattern2 & 1) * 2);
                int size = this.counters[pos2 + j2] << 8;
                if (size >= mins[category2] && size <= maxes[category2]) {
                    pattern2 >>= 1;
                    j2--;
                }
            }
            if (i3 < end) {
                pos2 += 8;
                i3++;
            } else {
                return;
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private void setCounters(BitArray row) throws NotFoundException {
        this.counterLength = 0;
        int i = row.getNextUnset(0);
        int end = row.getSize();
        if (i >= end) {
            throw NotFoundException.getNotFoundInstance();
        }
        boolean isWhite = true;
        int count = 0;
        while (i < end) {
            if (row.get(i) ^ isWhite) {
                count++;
            } else {
                counterAppend(count);
                count = 1;
                isWhite = !isWhite;
            }
            i++;
        }
        counterAppend(count);
    }

    private void counterAppend(int e) {
        this.counters[this.counterLength] = e;
        this.counterLength++;
        if (this.counterLength >= this.counters.length) {
            int[] temp = new int[(this.counterLength * 2)];
            System.arraycopy(this.counters, 0, temp, 0, this.counterLength);
            this.counters = temp;
        }
    }

    private int findStartPattern() throws NotFoundException {
        for (int i = 1; i < this.counterLength; i += 2) {
            int charOffset = toNarrowWidePattern(i);
            if (charOffset != -1 && arrayContains(STARTEND_ENCODING, ALPHABET[charOffset])) {
                int patternSize = 0;
                for (int j = i; j < i + 7; j++) {
                    patternSize += this.counters[j];
                }
                if (i == 1 || this.counters[i - 1] >= patternSize / 2) {
                    return i;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    static boolean arrayContains(char[] array, char key) {
        if (array != null) {
            for (char c : array) {
                if (c == key) {
                    return true;
                }
            }
        }
        return false;
    }

    private int toNarrowWidePattern(int position) {
        int threshold;
        int end = position + 7;
        if (end >= this.counterLength) {
            return -1;
        }
        int[] theCounters = this.counters;
        int maxBar = 0;
        int minBar = Integer.MAX_VALUE;
        for (int j = position; j < end; j += 2) {
            int currentCounter = theCounters[j];
            if (currentCounter < minBar) {
                minBar = currentCounter;
            }
            if (currentCounter > maxBar) {
                maxBar = currentCounter;
            }
        }
        int thresholdBar = (minBar + maxBar) / 2;
        int maxSpace = 0;
        int minSpace = Integer.MAX_VALUE;
        for (int j2 = position + 1; j2 < end; j2 += 2) {
            int currentCounter2 = theCounters[j2];
            if (currentCounter2 < minSpace) {
                minSpace = currentCounter2;
            }
            if (currentCounter2 > maxSpace) {
                maxSpace = currentCounter2;
            }
        }
        int thresholdSpace = (minSpace + maxSpace) / 2;
        int bitmask = 128;
        int pattern = 0;
        for (int i = 0; i < 7; i++) {
            if ((i & 1) == 0) {
                threshold = thresholdBar;
            } else {
                threshold = thresholdSpace;
            }
            bitmask >>= 1;
            if (theCounters[position + i] > threshold) {
                pattern |= bitmask;
            }
        }
        for (int i2 = 0; i2 < CHARACTER_ENCODINGS.length; i2++) {
            if (CHARACTER_ENCODINGS[i2] == pattern) {
                return i2;
            }
        }
        return -1;
    }
}
