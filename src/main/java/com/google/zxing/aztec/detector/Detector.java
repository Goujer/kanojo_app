package com.google.zxing.aztec.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.aztec.AztecDetectorResult;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.common.detector.WhiteRectangleDetector;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;

public final class Detector {
    private boolean compact;
    private final BitMatrix image;
    private int nbCenterLayers;
    private int nbDataBlocks;
    private int nbLayers;
    private int shift;

    public Detector(BitMatrix image2) {
        this.image = image2;
    }

    public AztecDetectorResult detect() throws NotFoundException {
        Point[] bullEyeCornerPoints = getBullEyeCornerPoints(getMatrixCenter());
        extractParameters(bullEyeCornerPoints);
        ResultPoint[] corners = getMatrixCornerPoints(bullEyeCornerPoints);
        return new AztecDetectorResult(sampleGrid(this.image, corners[this.shift % 4], corners[(this.shift + 3) % 4], corners[(this.shift + 2) % 4], corners[(this.shift + 1) % 4]), corners, this.compact, this.nbDataBlocks, this.nbLayers);
    }

    private void extractParameters(Point[] bullEyeCornerPoints) throws NotFoundException {
        boolean[] parameterData;
        int twoCenterLayers = this.nbCenterLayers * 2;
        boolean[] resab = sampleLine(bullEyeCornerPoints[0], bullEyeCornerPoints[1], twoCenterLayers + 1);
        boolean[] resbc = sampleLine(bullEyeCornerPoints[1], bullEyeCornerPoints[2], twoCenterLayers + 1);
        boolean[] rescd = sampleLine(bullEyeCornerPoints[2], bullEyeCornerPoints[3], twoCenterLayers + 1);
        boolean[] resda = sampleLine(bullEyeCornerPoints[3], bullEyeCornerPoints[0], twoCenterLayers + 1);
        if (resab[0] && resab[twoCenterLayers]) {
            this.shift = 0;
        } else if (resbc[0] && resbc[twoCenterLayers]) {
            this.shift = 1;
        } else if (rescd[0] && rescd[twoCenterLayers]) {
            this.shift = 2;
        } else if (!resda[0] || !resda[twoCenterLayers]) {
            throw NotFoundException.getNotFoundInstance();
        } else {
            this.shift = 3;
        }
        if (this.compact) {
            boolean[] shiftedParameterData = new boolean[28];
            for (int i = 0; i < 7; i++) {
                shiftedParameterData[i] = resab[i + 2];
                shiftedParameterData[i + 7] = resbc[i + 2];
                shiftedParameterData[i + 14] = rescd[i + 2];
                shiftedParameterData[i + 21] = resda[i + 2];
            }
            parameterData = new boolean[28];
            for (int i2 = 0; i2 < 28; i2++) {
                parameterData[i2] = shiftedParameterData[((this.shift * 7) + i2) % 28];
            }
        } else {
            boolean[] shiftedParameterData2 = new boolean[40];
            for (int i3 = 0; i3 < 11; i3++) {
                if (i3 < 5) {
                    shiftedParameterData2[i3] = resab[i3 + 2];
                    shiftedParameterData2[i3 + 10] = resbc[i3 + 2];
                    shiftedParameterData2[i3 + 20] = rescd[i3 + 2];
                    shiftedParameterData2[i3 + 30] = resda[i3 + 2];
                }
                if (i3 > 5) {
                    shiftedParameterData2[i3 - 1] = resab[i3 + 2];
                    shiftedParameterData2[i3 + 9] = resbc[i3 + 2];
                    shiftedParameterData2[i3 + 19] = rescd[i3 + 2];
                    shiftedParameterData2[i3 + 29] = resda[i3 + 2];
                }
            }
            parameterData = new boolean[40];
            for (int i4 = 0; i4 < 40; i4++) {
                parameterData[i4] = shiftedParameterData2[((this.shift * 10) + i4) % 40];
            }
        }
        correctParameterData(parameterData, this.compact);
        getParameters(parameterData);
    }

    private ResultPoint[] getMatrixCornerPoints(Point[] bullEyeCornerPoints) throws NotFoundException {
        float ratio = ((float) (((this.nbLayers > 4 ? 1 : 0) + (this.nbLayers * 2)) + ((this.nbLayers - 4) / 8))) / (2.0f * ((float) this.nbCenterLayers));
        int dx = bullEyeCornerPoints[0].getX() - bullEyeCornerPoints[2].getX();
        int dx2 = dx + (dx > 0 ? 1 : -1);
        int dy = bullEyeCornerPoints[0].getY() - bullEyeCornerPoints[2].getY();
        int dy2 = dy + (dy > 0 ? 1 : -1);
        int targetcx = MathUtils.round(((float) bullEyeCornerPoints[2].getX()) - (((float) dx2) * ratio));
        int targetcy = MathUtils.round(((float) bullEyeCornerPoints[2].getY()) - (((float) dy2) * ratio));
        int targetax = MathUtils.round(((float) bullEyeCornerPoints[0].getX()) + (((float) dx2) * ratio));
        int targetay = MathUtils.round(((float) bullEyeCornerPoints[0].getY()) + (((float) dy2) * ratio));
        int dx3 = bullEyeCornerPoints[1].getX() - bullEyeCornerPoints[3].getX();
        int dx4 = dx3 + (dx3 > 0 ? 1 : -1);
        int dy3 = bullEyeCornerPoints[1].getY() - bullEyeCornerPoints[3].getY();
        int dy4 = dy3 + (dy3 > 0 ? 1 : -1);
        int targetdx = MathUtils.round(((float) bullEyeCornerPoints[3].getX()) - (((float) dx4) * ratio));
        int targetdy = MathUtils.round(((float) bullEyeCornerPoints[3].getY()) - (((float) dy4) * ratio));
        int targetbx = MathUtils.round(((float) bullEyeCornerPoints[1].getX()) + (((float) dx4) * ratio));
        int targetby = MathUtils.round(((float) bullEyeCornerPoints[1].getY()) + (((float) dy4) * ratio));
        if (!isValid(targetax, targetay) || !isValid(targetbx, targetby) || !isValid(targetcx, targetcy) || !isValid(targetdx, targetdy)) {
            throw NotFoundException.getNotFoundInstance();
        }
        return new ResultPoint[]{new ResultPoint((float) targetax, (float) targetay), new ResultPoint((float) targetbx, (float) targetby), new ResultPoint((float) targetcx, (float) targetcy), new ResultPoint((float) targetdx, (float) targetdy)};
    }

    private static void correctParameterData(boolean[] parameterData, boolean compact2) throws NotFoundException {
        int numCodewords;
        int numDataCodewords;
        if (compact2) {
            numCodewords = 7;
            numDataCodewords = 2;
        } else {
            numCodewords = 10;
            numDataCodewords = 4;
        }
        int numECCodewords = numCodewords - numDataCodewords;
        int[] parameterWords = new int[numCodewords];
        for (int i = 0; i < numCodewords; i++) {
            int flag = 1;
            for (int j = 1; j <= 4; j++) {
                if (parameterData[((4 * i) + 4) - j]) {
                    parameterWords[i] = parameterWords[i] + flag;
                }
                flag <<= 1;
            }
        }
        try {
            new ReedSolomonDecoder(GenericGF.AZTEC_PARAM).decode(parameterWords, numECCodewords);
            for (int i2 = 0; i2 < numDataCodewords; i2++) {
                int flag2 = 1;
                for (int j2 = 1; j2 <= 4; j2++) {
                    parameterData[((i2 * 4) + 4) - j2] = (parameterWords[i2] & flag2) == flag2;
                    flag2 <<= 1;
                }
            }
        } catch (ReedSolomonException e) {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    private Point[] getBullEyeCornerPoints(Point pCenter) throws NotFoundException {
        Point pina = pCenter;
        Point pinb = pCenter;
        Point pinc = pCenter;
        Point pind = pCenter;
        boolean color = true;
        this.nbCenterLayers = 1;
        while (this.nbCenterLayers < 9) {
            Point pouta = getFirstDifferent(pina, color, 1, -1);
            Point poutb = getFirstDifferent(pinb, color, 1, 1);
            Point poutc = getFirstDifferent(pinc, color, -1, 1);
            Point poutd = getFirstDifferent(pind, color, -1, -1);
            if (this.nbCenterLayers > 2) {
                float q = (distance(poutd, pouta) * ((float) this.nbCenterLayers)) / (distance(pind, pina) * ((float) (this.nbCenterLayers + 2)));
                if (((double) q) >= 0.75d) {
                    if (((double) q) <= 1.25d) {
                        if (!isWhiteOrBlackRectangle(pouta, poutb, poutc, poutd)) {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            pina = pouta;
            pinb = poutb;
            pinc = poutc;
            pind = poutd;
            if (!color) {
                color = true;
            } else {
                color = false;
            }
            this.nbCenterLayers++;
        }
        if (this.nbCenterLayers == 5 || this.nbCenterLayers == 7) {
            this.compact = this.nbCenterLayers == 5;
            float ratio = 1.5f / ((float) ((this.nbCenterLayers * 2) - 3));
            int dx = pina.getX() - pinc.getX();
            int dy = pina.getY() - pinc.getY();
            int targetcx = MathUtils.round(((float) pinc.getX()) - (((float) dx) * ratio));
            int targetcy = MathUtils.round(((float) pinc.getY()) - (((float) dy) * ratio));
            int targetax = MathUtils.round(((float) pina.getX()) + (((float) dx) * ratio));
            int targetay = MathUtils.round(((float) pina.getY()) + (((float) dy) * ratio));
            int dx2 = pinb.getX() - pind.getX();
            int dy2 = pinb.getY() - pind.getY();
            int targetdx = MathUtils.round(((float) pind.getX()) - (((float) dx2) * ratio));
            int targetdy = MathUtils.round(((float) pind.getY()) - (((float) dy2) * ratio));
            int targetbx = MathUtils.round(((float) pinb.getX()) + (((float) dx2) * ratio));
            int targetby = MathUtils.round(((float) pinb.getY()) + (((float) dy2) * ratio));
            if (!isValid(targetax, targetay) || !isValid(targetbx, targetby) || !isValid(targetcx, targetcy) || !isValid(targetdx, targetdy)) {
                throw NotFoundException.getNotFoundInstance();
            }
            return new Point[]{new Point(targetax, targetay), new Point(targetbx, targetby), new Point(targetcx, targetcy), new Point(targetdx, targetdy)};
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private Point getMatrixCenter() {
        ResultPoint pointA;
        ResultPoint pointB;
        ResultPoint pointC;
        ResultPoint pointD;
        ResultPoint pointA2;
        ResultPoint pointB2;
        ResultPoint pointC2;
        ResultPoint pointD2;
        try {
            ResultPoint[] cornerPoints = new WhiteRectangleDetector(this.image).detect();
            pointA = cornerPoints[0];
            pointB = cornerPoints[1];
            pointC = cornerPoints[2];
            pointD = cornerPoints[3];
        } catch (NotFoundException e) {
            int cx = this.image.getWidth() / 2;
            int cy = this.image.getHeight() / 2;
            pointA = getFirstDifferent(new Point(cx + 7, cy - 7), false, 1, -1).toResultPoint();
            pointB = getFirstDifferent(new Point(cx + 7, cy + 7), false, 1, 1).toResultPoint();
            pointC = getFirstDifferent(new Point(cx - 7, cy + 7), false, -1, 1).toResultPoint();
            pointD = getFirstDifferent(new Point(cx - 7, cy - 7), false, -1, -1).toResultPoint();
        }
        int cx2 = MathUtils.round((((pointA.getX() + pointD.getX()) + pointB.getX()) + pointC.getX()) / 4.0f);
        int cy2 = MathUtils.round((((pointA.getY() + pointD.getY()) + pointB.getY()) + pointC.getY()) / 4.0f);
        try {
            ResultPoint[] cornerPoints2 = new WhiteRectangleDetector(this.image, 15, cx2, cy2).detect();
            pointA2 = cornerPoints2[0];
            pointB2 = cornerPoints2[1];
            pointC2 = cornerPoints2[2];
            pointD2 = cornerPoints2[3];
        } catch (NotFoundException e2) {
            pointA2 = getFirstDifferent(new Point(cx2 + 7, cy2 - 7), false, 1, -1).toResultPoint();
            pointB2 = getFirstDifferent(new Point(cx2 + 7, cy2 + 7), false, 1, 1).toResultPoint();
            pointC2 = getFirstDifferent(new Point(cx2 - 7, cy2 + 7), false, -1, 1).toResultPoint();
            pointD2 = getFirstDifferent(new Point(cx2 - 7, cy2 - 7), false, -1, -1).toResultPoint();
        }
        return new Point(MathUtils.round((((pointA2.getX() + pointD2.getX()) + pointB2.getX()) + pointC2.getX()) / 4.0f), MathUtils.round((((pointA2.getY() + pointD2.getY()) + pointB2.getY()) + pointC2.getY()) / 4.0f));
    }

    private BitMatrix sampleGrid(BitMatrix image2, ResultPoint topLeft, ResultPoint bottomLeft, ResultPoint bottomRight, ResultPoint topRight) throws NotFoundException {
        int dimension;
        if (this.compact) {
            dimension = (this.nbLayers * 4) + 11;
        } else if (this.nbLayers <= 4) {
            dimension = (this.nbLayers * 4) + 15;
        } else {
            dimension = (this.nbLayers * 4) + ((((this.nbLayers - 4) / 8) + 1) * 2) + 15;
        }
        return GridSampler.getInstance().sampleGrid(image2, dimension, dimension, 0.5f, 0.5f, ((float) dimension) - 0.5f, 0.5f, ((float) dimension) - 0.5f, ((float) dimension) - 0.5f, 0.5f, ((float) dimension) - 0.5f, topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY());
    }

    private void getParameters(boolean[] parameterData) {
        int nbBitsForNbLayers;
        int nbBitsForNbDatablocks;
        if (this.compact) {
            nbBitsForNbLayers = 2;
            nbBitsForNbDatablocks = 6;
        } else {
            nbBitsForNbLayers = 5;
            nbBitsForNbDatablocks = 11;
        }
        for (int i = 0; i < nbBitsForNbLayers; i++) {
            this.nbLayers <<= 1;
            if (parameterData[i]) {
                this.nbLayers++;
            }
        }
        for (int i2 = nbBitsForNbLayers; i2 < nbBitsForNbLayers + nbBitsForNbDatablocks; i2++) {
            this.nbDataBlocks <<= 1;
            if (parameterData[i2]) {
                this.nbDataBlocks++;
            }
        }
        this.nbLayers++;
        this.nbDataBlocks++;
    }

    private boolean[] sampleLine(Point p1, Point p2, int size) {
        boolean[] res = new boolean[size];
        float d = distance(p1, p2);
        float moduleSize = d / ((float) (size - 1));
        float dx = (((float) (p2.getX() - p1.getX())) * moduleSize) / d;
        float dy = (((float) (p2.getY() - p1.getY())) * moduleSize) / d;
        float px = (float) p1.getX();
        float py = (float) p1.getY();
        for (int i = 0; i < size; i++) {
            res[i] = this.image.get(MathUtils.round(px), MathUtils.round(py));
            px += dx;
            py += dy;
        }
        return res;
    }

    private boolean isWhiteOrBlackRectangle(Point p1, Point p2, Point p3, Point p4) {
        Point p12 = new Point(p1.getX() - 3, p1.getY() + 3);
        Point p22 = new Point(p2.getX() - 3, p2.getY() - 3);
        Point p32 = new Point(p3.getX() + 3, p3.getY() - 3);
        Point p42 = new Point(p4.getX() + 3, p4.getY() + 3);
        int cInit = getColor(p42, p12);
        if (cInit != 0 && getColor(p12, p22) == cInit && getColor(p22, p32) == cInit && getColor(p32, p42) == cInit) {
            return true;
        }
        return false;
    }

    private int getColor(Point p1, Point p2) {
        float d = distance(p1, p2);
        float dx = ((float) (p2.getX() - p1.getX())) / d;
        float dy = ((float) (p2.getY() - p1.getY())) / d;
        int error = 0;
        float px = (float) p1.getX();
        float py = (float) p1.getY();
        boolean colorModel = this.image.get(p1.getX(), p1.getY());
        for (int i = 0; ((float) i) < d; i++) {
            px += dx;
            py += dy;
            if (this.image.get(MathUtils.round(px), MathUtils.round(py)) != colorModel) {
                error++;
            }
        }
        float errRatio = ((float) error) / d;
        if (errRatio > 0.1f && errRatio < 0.9f) {
            return 0;
        }
        return ((errRatio > 0.1f ? 1 : (errRatio == 0.1f ? 0 : -1)) <= 0) == colorModel ? 1 : -1;
    }

    private Point getFirstDifferent(Point init, boolean color, int dx, int dy) {
        int x = init.getX() + dx;
        int y = init.getY() + dy;
        while (isValid(x, y) && this.image.get(x, y) == color) {
            x += dx;
            y += dy;
        }
        int x2 = x - dx;
        int y2 = y - dy;
        while (isValid(x2, y2) && this.image.get(x2, y2) == color) {
            x2 += dx;
        }
        int x3 = x2 - dx;
        while (isValid(x3, y2) && this.image.get(x3, y2) == color) {
            y2 += dy;
        }
        return new Point(x3, y2 - dy);
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < this.image.getWidth() && y > 0 && y < this.image.getHeight();
    }

    private static float distance(Point a, Point b) {
        return MathUtils.distance(a.getX(), a.getY(), b.getX(), b.getY());
    }

    static final class Point {
        private final int x;
        private final int y;

        /* access modifiers changed from: package-private */
        public ResultPoint toResultPoint() {
            return new ResultPoint((float) getX(), (float) getY());
        }

        Point(int x2, int y2) {
            this.x = x2;
            this.y = y2;
        }

        /* access modifiers changed from: package-private */
        public int getX() {
            return this.x;
        }

        /* access modifiers changed from: package-private */
        public int getY() {
            return this.y;
        }
    }
}
