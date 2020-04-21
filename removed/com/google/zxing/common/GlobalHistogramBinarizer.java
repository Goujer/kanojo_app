package com.google.zxing.common;

import android.support.v4.view.MotionEventCompat;
import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;

public class GlobalHistogramBinarizer extends Binarizer {
    private static final byte[] EMPTY = new byte[0];
    private static final int LUMINANCE_BITS = 5;
    private static final int LUMINANCE_BUCKETS = 32;
    private static final int LUMINANCE_SHIFT = 3;
    private final int[] buckets = new int[32];
    private byte[] luminances = EMPTY;

    public GlobalHistogramBinarizer(LuminanceSource source) {
        super(source);
    }

    public BitArray getBlackRow(int y, BitArray row) throws NotFoundException {
        LuminanceSource source = getLuminanceSource();
        int width = source.getWidth();
        if (row == null || row.getSize() < width) {
            row = new BitArray(width);
        } else {
            row.clear();
        }
        initArrays(width);
        byte[] localLuminances = source.getRow(y, this.luminances);
        int[] localBuckets = this.buckets;
        for (int x = 0; x < width; x++) {
            int i = (localLuminances[x] & MotionEventCompat.ACTION_MASK) >> 3;
            localBuckets[i] = localBuckets[i] + 1;
        }
        int blackPoint = estimateBlackPoint(localBuckets);
        int left = localLuminances[0] & MotionEventCompat.ACTION_MASK;
        int center = localLuminances[1] & MotionEventCompat.ACTION_MASK;
        for (int x2 = 1; x2 < width - 1; x2++) {
            int right = localLuminances[x2 + 1] & MotionEventCompat.ACTION_MASK;
            if (((((center << 2) - left) - right) >> 1) < blackPoint) {
                row.set(x2);
            }
            left = center;
            center = right;
        }
        return row;
    }

    public BitMatrix getBlackMatrix() throws NotFoundException {
        LuminanceSource source = getLuminanceSource();
        int width = source.getWidth();
        int height = source.getHeight();
        BitMatrix matrix = new BitMatrix(width, height);
        initArrays(width);
        int[] localBuckets = this.buckets;
        for (int y = 1; y < 5; y++) {
            byte[] localLuminances = source.getRow((height * y) / 5, this.luminances);
            int right = (width << 2) / 5;
            for (int x = width / 5; x < right; x++) {
                int i = (localLuminances[x] & MotionEventCompat.ACTION_MASK) >> 3;
                localBuckets[i] = localBuckets[i] + 1;
            }
        }
        int blackPoint = estimateBlackPoint(localBuckets);
        byte[] localLuminances2 = source.getMatrix();
        for (int y2 = 0; y2 < height; y2++) {
            int offset = y2 * width;
            for (int x2 = 0; x2 < width; x2++) {
                if ((localLuminances2[offset + x2] & MotionEventCompat.ACTION_MASK) < blackPoint) {
                    matrix.set(x2, y2);
                }
            }
        }
        return matrix;
    }

    public Binarizer createBinarizer(LuminanceSource source) {
        return new GlobalHistogramBinarizer(source);
    }

    private void initArrays(int luminanceSize) {
        if (this.luminances.length < luminanceSize) {
            this.luminances = new byte[luminanceSize];
        }
        for (int x = 0; x < 32; x++) {
            this.buckets[x] = 0;
        }
    }

    private static int estimateBlackPoint(int[] buckets2) throws NotFoundException {
        int numBuckets = buckets2.length;
        int maxBucketCount = 0;
        int firstPeak = 0;
        int firstPeakSize = 0;
        for (int x = 0; x < numBuckets; x++) {
            if (buckets2[x] > firstPeakSize) {
                firstPeak = x;
                firstPeakSize = buckets2[x];
            }
            if (buckets2[x] > maxBucketCount) {
                maxBucketCount = buckets2[x];
            }
        }
        int secondPeak = 0;
        int secondPeakScore = 0;
        for (int x2 = 0; x2 < numBuckets; x2++) {
            int distanceToBiggest = x2 - firstPeak;
            int score = buckets2[x2] * distanceToBiggest * distanceToBiggest;
            if (score > secondPeakScore) {
                secondPeak = x2;
                secondPeakScore = score;
            }
        }
        if (firstPeak > secondPeak) {
            int temp = firstPeak;
            firstPeak = secondPeak;
            secondPeak = temp;
        }
        if (secondPeak - firstPeak <= (numBuckets >> 4)) {
            throw NotFoundException.getNotFoundInstance();
        }
        int bestValley = secondPeak - 1;
        int bestValleyScore = -1;
        for (int x3 = secondPeak - 1; x3 > firstPeak; x3--) {
            int fromFirst = x3 - firstPeak;
            int score2 = fromFirst * fromFirst * (secondPeak - x3) * (maxBucketCount - buckets2[x3]);
            if (score2 > bestValleyScore) {
                bestValley = x3;
                bestValleyScore = score2;
            }
        }
        return bestValley << 3;
    }
}
