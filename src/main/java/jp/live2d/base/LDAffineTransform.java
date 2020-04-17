package jp.live2d.base;

import jp.live2d.Live2D;

public class LDAffineTransform {
    private static final int MODE_IDENTITY = 0;
    private static final int MODE_TRANSLATION = 1;
    private static final int MODE_UNKNOWN = -1;
    private static final int STATE_IDENTITY = 0;
    private static final int STATE_SCALE = 2;
    private static final int STATE_SHEAR = 4;
    private static final int STATE_TRANSLATE = 1;
    float m00 = 1.0f;
    float m01 = 0.0f;
    float m02 = 0.0f;
    float m10 = 0.0f;
    float m11 = 1.0f;
    float m12 = 0.0f;
    int mode = 0;
    int state = 0;

    public LDAffineTransform() {
    }

    public LDAffineTransform(float _m00, float _m10, float _m01, float _m11, float _m02, float _m12) {
        this.m00 = _m00;
        this.m10 = _m10;
        this.m01 = _m01;
        this.m11 = _m11;
        this.m02 = _m02;
        this.m12 = _m12;
        update();
    }

    public void transform(float[] srcPts, float[] dstPts, int numPts) {
        switch (this.state) {
            case 0:
                if (srcPts != dstPts || 0 != 0) {
                    System.arraycopy(srcPts, 0, dstPts, 0, numPts * 2);
                    return;
                }
                return;
            case 1:
                float M02 = this.m02;
                float M12 = this.m12;
                int dstOff = 0;
                int srcOff = 0;
                while (true) {
                    numPts--;
                    if (numPts < 0) {
                        int i = dstOff;
                        int i2 = srcOff;
                        return;
                    }
                    int dstOff2 = dstOff + 1;
                    int srcOff2 = srcOff + 1;
                    dstPts[dstOff] = srcPts[srcOff] + M02;
                    dstOff = dstOff2 + 1;
                    srcOff = srcOff2 + 1;
                    dstPts[dstOff2] = srcPts[srcOff2] + M12;
                }
            case 2:
                float M00 = this.m00;
                float M11 = this.m11;
                int dstOff3 = 0;
                int srcOff3 = 0;
                while (true) {
                    numPts--;
                    if (numPts < 0) {
                        int i3 = dstOff3;
                        int i4 = srcOff3;
                        return;
                    }
                    int dstOff4 = dstOff3 + 1;
                    int srcOff4 = srcOff3 + 1;
                    dstPts[dstOff3] = srcPts[srcOff3] * M00;
                    dstOff3 = dstOff4 + 1;
                    srcOff3 = srcOff4 + 1;
                    dstPts[dstOff4] = srcPts[srcOff4] * M11;
                }
            case 3:
                float M002 = this.m00;
                float M022 = this.m02;
                float M112 = this.m11;
                float M122 = this.m12;
                int dstOff5 = 0;
                int srcOff5 = 0;
                while (true) {
                    numPts--;
                    if (numPts < 0) {
                        int i5 = dstOff5;
                        int i6 = srcOff5;
                        return;
                    }
                    int dstOff6 = dstOff5 + 1;
                    int srcOff6 = srcOff5 + 1;
                    dstPts[dstOff5] = (srcPts[srcOff5] * M002) + M022;
                    dstOff5 = dstOff6 + 1;
                    srcOff5 = srcOff6 + 1;
                    dstPts[dstOff6] = (srcPts[srcOff6] * M112) + M122;
                }
            case 4:
                float M01 = this.m01;
                float M10 = this.m10;
                int dstOff7 = 0;
                int srcOff7 = 0;
                while (true) {
                    numPts--;
                    if (numPts < 0) {
                        int i7 = dstOff7;
                        int i8 = srcOff7;
                        return;
                    }
                    int srcOff8 = srcOff7 + 1;
                    float x = srcPts[srcOff7];
                    int dstOff8 = dstOff7 + 1;
                    srcOff7 = srcOff8 + 1;
                    dstPts[dstOff7] = srcPts[srcOff8] * M01;
                    dstOff7 = dstOff8 + 1;
                    dstPts[dstOff8] = M10 * x;
                }
            case 5:
                float M012 = this.m01;
                float M023 = this.m02;
                float M102 = this.m10;
                float M123 = this.m12;
                int dstOff9 = 0;
                int srcOff9 = 0;
                while (true) {
                    numPts--;
                    if (numPts < 0) {
                        int i9 = dstOff9;
                        int i10 = srcOff9;
                        return;
                    }
                    int srcOff10 = srcOff9 + 1;
                    float x2 = srcPts[srcOff9];
                    int dstOff10 = dstOff9 + 1;
                    srcOff9 = srcOff10 + 1;
                    dstPts[dstOff9] = (srcPts[srcOff10] * M012) + M023;
                    dstOff9 = dstOff10 + 1;
                    dstPts[dstOff10] = (M102 * x2) + M123;
                }
            case 6:
                float M003 = this.m00;
                float M013 = this.m01;
                float M103 = this.m10;
                float M113 = this.m11;
                int dstOff11 = 0;
                int srcOff11 = 0;
                while (true) {
                    numPts--;
                    if (numPts < 0) {
                        int i11 = dstOff11;
                        int i12 = srcOff11;
                        return;
                    }
                    int srcOff12 = srcOff11 + 1;
                    float x3 = srcPts[srcOff11];
                    srcOff11 = srcOff12 + 1;
                    float y = srcPts[srcOff12];
                    int dstOff12 = dstOff11 + 1;
                    dstPts[dstOff11] = (M003 * x3) + (M013 * y);
                    dstOff11 = dstOff12 + 1;
                    dstPts[dstOff12] = (M103 * x3) + (M113 * y);
                }
            case 7:
                float M004 = this.m00;
                float M014 = this.m01;
                float M024 = this.m02;
                float M104 = this.m10;
                float M114 = this.m11;
                float M124 = this.m12;
                int dstOff13 = 0;
                int srcOff13 = 0;
                while (true) {
                    numPts--;
                    if (numPts < 0) {
                        int i13 = dstOff13;
                        int i14 = srcOff13;
                        return;
                    }
                    int srcOff14 = srcOff13 + 1;
                    float x4 = srcPts[srcOff13];
                    srcOff13 = srcOff14 + 1;
                    float y2 = srcPts[srcOff14];
                    int dstOff14 = dstOff13 + 1;
                    dstPts[dstOff13] = (M004 * x4) + (M014 * y2) + M024;
                    dstOff13 = dstOff14 + 1;
                    dstPts[dstOff14] = (M104 * x4) + (M114 * y2) + M124;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void update() {
        if (((double) this.m01) == 0.0d && ((double) this.m10) == 0.0d) {
            if (((double) this.m00) == 1.0d && ((double) this.m11) == 1.0d) {
                if (((double) this.m02) == 0.0d && ((double) this.m12) == 0.0d) {
                    this.state = 0;
                    this.mode = 0;
                    return;
                }
                this.state = 1;
                this.mode = 1;
            } else if (((double) this.m02) == 0.0d && ((double) this.m12) == 0.0d) {
                this.state = 2;
                this.mode = -1;
            } else {
                this.state = 3;
                this.mode = -1;
            }
        } else if (((double) this.m00) == 0.0d && ((double) this.m11) == 0.0d) {
            if (((double) this.m02) == 0.0d && ((double) this.m12) == 0.0d) {
                this.state = 4;
                this.mode = -1;
                return;
            }
            this.state = 5;
            this.mode = -1;
        } else if (((double) this.m02) == 0.0d && ((double) this.m12) == 0.0d) {
            this.state = 6;
            this.mode = -1;
        } else {
            this.state = 7;
            this.mode = -1;
        }
    }

    public void factorize(float[] ret) {
        getMatrix(ret);
        float a = ret[0];
        float b = ret[2];
        float c = ret[1];
        float d = ret[3];
        float rt = (float) Math.sqrt((double) ((a * a) + (c * c)));
        float tt = (a * d) - (b * c);
        if (rt != 0.0f) {
            ret[0] = rt;
            ret[1] = tt / rt;
            ret[2] = ((c * d) + (a * b)) / tt;
            ret[3] = (float) Math.atan2((double) c, (double) a);
        } else if (Live2D.L2D_VERBOSE) {
            System.out.printf("-----------------------------", new Object[0]);
            System.out.printf(" factorize() / rt==0", new Object[0]);
            System.out.printf("-----------------------------", new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public void interpolate(LDAffineTransform aa1, LDAffineTransform aa2, float t, LDAffineTransform ret) {
        float[] d1 = new float[6];
        float[] d2 = new float[6];
        aa1.factorize(d1);
        aa2.factorize(d2);
        ret.setFactor(new float[]{d1[0] + ((d2[0] - d1[0]) * t), d1[1] + ((d2[1] - d1[1]) * t), d1[2] + ((d2[2] - d1[2]) * t), d1[3] + ((d2[3] - d1[3]) * t), d1[4] + ((d2[4] - d1[4]) * t), d1[5] + ((d2[5] - d1[5]) * t)});
    }

    public void setFactor(float[] fmat) {
        float cosQ = (float) Math.cos((double) fmat[3]);
        float sinQ = (float) Math.sin((double) fmat[3]);
        this.m00 = fmat[0] * cosQ;
        this.m10 = fmat[0] * sinQ;
        this.m01 = fmat[1] * ((fmat[2] * cosQ) - sinQ);
        this.m11 = fmat[1] * ((fmat[2] * sinQ) + cosQ);
        this.m02 = fmat[4];
        this.m12 = fmat[5];
        update();
    }

    /* access modifiers changed from: package-private */
    public void getMatrix(float[] ret) {
        ret[0] = this.m00;
        ret[1] = this.m10;
        ret[2] = this.m01;
        ret[3] = this.m11;
        ret[4] = this.m02;
        ret[5] = this.m12;
    }
}
