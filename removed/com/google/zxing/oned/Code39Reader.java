package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.Map;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;
import twitter4j.internal.http.HttpResponseCode;

public final class Code39Reader extends OneDReader {
    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";
    private static final int ASTERISK_ENCODING = CHARACTER_ENCODINGS[39];
    static final int[] CHARACTER_ENCODINGS = {52, 289, 97, 352, 49, HttpResponseCode.NOT_MODIFIED, 112, 37, 292, 100, 265, 73, 328, 25, 280, 88, 13, 268, 76, 28, 259, 67, 322, 19, 274, 82, 7, 262, 70, 22, 385, 193, 448, 145, 400, 208, 133, 388, 196, 148, 168, 162, 138, 42};
    private final int[] counters;
    private final StringBuilder decodeRowResult;
    private final boolean extendedMode;
    private final boolean usingCheckDigit;

    public Code39Reader() {
        this(false);
    }

    public Code39Reader(boolean usingCheckDigit2) {
        this(usingCheckDigit2, false);
    }

    public Code39Reader(boolean usingCheckDigit2, boolean extendedMode2) {
        this.usingCheckDigit = usingCheckDigit2;
        this.extendedMode = extendedMode2;
        this.decodeRowResult = new StringBuilder(20);
        this.counters = new int[9];
    }

    public Result decodeRow(int rowNumber, BitArray row, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        char decodedChar;
        int lastStart;
        String resultString;
        int[] theCounters = this.counters;
        Arrays.fill(theCounters, 0);
        StringBuilder result = this.decodeRowResult;
        result.setLength(0);
        int[] start = findAsteriskPattern(row, theCounters);
        int nextStart = row.getNextSet(start[1]);
        int end = row.getSize();
        do {
            recordPattern(row, nextStart, theCounters);
            int pattern = toNarrowWidePattern(theCounters);
            if (pattern < 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            decodedChar = patternToChar(pattern);
            result.append(decodedChar);
            lastStart = nextStart;
            for (int counter : theCounters) {
                nextStart += counter;
            }
            nextStart = row.getNextSet(nextStart);
        } while (decodedChar != '*');
        result.setLength(result.length() - 1);
        int lastPatternSize = 0;
        for (int counter2 : theCounters) {
            lastPatternSize += counter2;
        }
        int whiteSpaceAfterEnd = (nextStart - lastStart) - lastPatternSize;
        if (nextStart == end || (whiteSpaceAfterEnd >> 1) >= lastPatternSize) {
            if (this.usingCheckDigit) {
                int max = result.length() - 1;
                int total = 0;
                for (int i = 0; i < max; i++) {
                    total += ALPHABET_STRING.indexOf(this.decodeRowResult.charAt(i));
                }
                if (result.charAt(max) != ALPHABET[total % 43]) {
                    throw ChecksumException.getChecksumInstance();
                }
                result.setLength(max);
            }
            if (result.length() == 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (this.extendedMode) {
                resultString = decodeExtended(result);
            } else {
                resultString = result.toString();
            }
            return new Result(resultString, (byte[]) null, new ResultPoint[]{new ResultPoint(((float) (start[1] + start[0])) / 2.0f, (float) rowNumber), new ResultPoint(((float) (nextStart + lastStart)) / 2.0f, (float) rowNumber)}, BarcodeFormat.CODE_39);
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int[] findAsteriskPattern(BitArray row, int[] counters2) throws NotFoundException {
        int width = row.getSize();
        int rowOffset = row.getNextSet(0);
        int counterPosition = 0;
        int patternStart = rowOffset;
        boolean isWhite = false;
        int patternLength = counters2.length;
        for (int i = rowOffset; i < width; i++) {
            if (row.get(i) ^ isWhite) {
                counters2[counterPosition] = counters2[counterPosition] + 1;
            } else {
                if (counterPosition != patternLength - 1) {
                    counterPosition++;
                } else if (toNarrowWidePattern(counters2) != ASTERISK_ENCODING || !row.isRange(Math.max(0, patternStart - ((i - patternStart) >> 1)), patternStart, false)) {
                    patternStart += counters2[0] + counters2[1];
                    System.arraycopy(counters2, 2, counters2, 0, patternLength - 2);
                    counters2[patternLength - 2] = 0;
                    counters2[patternLength - 1] = 0;
                    counterPosition--;
                } else {
                    return new int[]{patternStart, i};
                }
                counters2[counterPosition] = 1;
                if (!isWhite) {
                    isWhite = true;
                } else {
                    isWhite = false;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int toNarrowWidePattern(int[] counters2) {
        int wideCounters;
        int numCounters = counters2.length;
        int maxNarrowCounter = 0;
        do {
            int minCounter = Integer.MAX_VALUE;
            for (int counter : counters2) {
                if (counter < minCounter && counter > maxNarrowCounter) {
                    minCounter = counter;
                }
            }
            maxNarrowCounter = minCounter;
            wideCounters = 0;
            int totalWideCountersWidth = 0;
            int pattern = 0;
            for (int i = 0; i < numCounters; i++) {
                int counter2 = counters2[i];
                if (counter2 > maxNarrowCounter) {
                    pattern |= 1 << ((numCounters - 1) - i);
                    wideCounters++;
                    totalWideCountersWidth += counter2;
                }
            }
            if (wideCounters == 3) {
                for (int i2 = 0; i2 < numCounters && wideCounters > 0; i2++) {
                    int counter3 = counters2[i2];
                    if (counter3 > maxNarrowCounter) {
                        wideCounters--;
                        if ((counter3 << 1) >= totalWideCountersWidth) {
                            return -1;
                        }
                    }
                }
                return pattern;
            }
        } while (wideCounters > 3);
        return -1;
    }

    private static char patternToChar(int pattern) throws NotFoundException {
        for (int i = 0; i < CHARACTER_ENCODINGS.length; i++) {
            if (CHARACTER_ENCODINGS[i] == pattern) {
                return ALPHABET[i];
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static String decodeExtended(CharSequence encoded) throws FormatException {
        int length = encoded.length();
        StringBuilder decoded = new StringBuilder(length);
        int i = 0;
        while (i < length) {
            char c = encoded.charAt(i);
            if (c == '+' || c == '$' || c == '%' || c == '/') {
                char next = encoded.charAt(i + 1);
                char decodedChar = 0;
                switch (c) {
                    case DateTimeParserConstants.WS /*36*/:
                        if (next >= 'A' && next <= 'Z') {
                            decodedChar = (char) (next - '@');
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    case '%':
                        if (next < 'A' || next > 'E') {
                            if (next >= 'F' && next <= 'W') {
                                decodedChar = (char) (next - 11);
                                break;
                            } else {
                                throw FormatException.getFormatInstance();
                            }
                        } else {
                            decodedChar = (char) (next - '&');
                            break;
                        }
                        break;
                    case '+':
                        if (next >= 'A' && next <= 'Z') {
                            decodedChar = (char) (next + ' ');
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    case DateTimeParserConstants.QUOTEDPAIR /*47*/:
                        if (next >= 'A' && next <= 'O') {
                            decodedChar = (char) (next - ' ');
                            break;
                        } else if (next == 'Z') {
                            decodedChar = ':';
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                }
                decoded.append(decodedChar);
                i++;
            } else {
                decoded.append(c);
            }
            i++;
        }
        return decoded.toString();
    }
}
