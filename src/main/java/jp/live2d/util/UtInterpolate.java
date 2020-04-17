package jp.live2d.util;

import java.util.ArrayList;
import jp.live2d.Live2D;
import jp.live2d.ModelContext;
import jp.live2d.param.PivotManager;

public class UtInterpolate {
    public static int interpolateInt(ModelContext mdc, PivotManager pivotManager, boolean[] ret_paramOutside, int[] pivotInt) throws Exception {
        int interpolateCount = pivotManager.calcPivotValue(mdc, ret_paramOutside);
        byte[] pivotTableIndices = mdc.getTmpPivotTableIndicesRef();
        float[] tmpT_array = mdc.getTmpT_ArrayRef();
        pivotManager.calcPivotIndexies(pivotTableIndices, tmpT_array, interpolateCount);
        if (interpolateCount <= 0) {
            return pivotInt[pivotTableIndices[0]];
        }
        if (interpolateCount == 1) {
            int p00 = pivotInt[pivotTableIndices[0]];
            return (int) (((float) p00) + (((float) (pivotInt[pivotTableIndices[1]] - p00)) * tmpT_array[0]));
        } else if (interpolateCount == 2) {
            int p002 = pivotInt[pivotTableIndices[0]];
            int p01 = pivotInt[pivotTableIndices[1]];
            int p10 = pivotInt[pivotTableIndices[2]];
            int p11 = pivotInt[pivotTableIndices[3]];
            float t1 = tmpT_array[0];
            int p0 = (int) (((float) p002) + (((float) (p01 - p002)) * t1));
            return (int) (((float) p0) + (((float) (((int) (((float) p10) + (((float) (p11 - p10)) * t1))) - p0)) * tmpT_array[1]));
        } else if (interpolateCount == 3) {
            int p000 = pivotInt[pivotTableIndices[0]];
            int p001 = pivotInt[pivotTableIndices[1]];
            int p010 = pivotInt[pivotTableIndices[2]];
            int p011 = pivotInt[pivotTableIndices[3]];
            int p100 = pivotInt[pivotTableIndices[4]];
            int p101 = pivotInt[pivotTableIndices[5]];
            int p110 = pivotInt[pivotTableIndices[6]];
            int p111 = pivotInt[pivotTableIndices[7]];
            float t12 = tmpT_array[0];
            float t2 = tmpT_array[1];
            int p003 = (int) (((float) p000) + (((float) (p001 - p000)) * t12));
            int p102 = (int) (((float) p100) + (((float) (p101 - p100)) * t12));
            int p02 = (int) (((float) p003) + (((float) (((int) (((float) p010) + (((float) (p011 - p010)) * t12))) - p003)) * t2));
            return (int) (((float) p02) + (((float) (((int) (((float) p102) + (((float) (((int) (((float) p110) + (((float) (p111 - p110)) * t12))) - p102)) * t2))) - p02)) * tmpT_array[2]));
        } else if (interpolateCount == 4) {
            int p0000 = pivotInt[pivotTableIndices[0]];
            int p0001 = pivotInt[pivotTableIndices[1]];
            int p0010 = pivotInt[pivotTableIndices[2]];
            int p0011 = pivotInt[pivotTableIndices[3]];
            int p0100 = pivotInt[pivotTableIndices[4]];
            int p0101 = pivotInt[pivotTableIndices[5]];
            int p0110 = pivotInt[pivotTableIndices[6]];
            int p0111 = pivotInt[pivotTableIndices[7]];
            int p1000 = pivotInt[pivotTableIndices[8]];
            int p1001 = pivotInt[pivotTableIndices[9]];
            int p1010 = pivotInt[pivotTableIndices[10]];
            int p1011 = pivotInt[pivotTableIndices[11]];
            int p1100 = pivotInt[pivotTableIndices[12]];
            int p1101 = pivotInt[pivotTableIndices[13]];
            int p1110 = pivotInt[pivotTableIndices[14]];
            int p1111 = pivotInt[pivotTableIndices[15]];
            float t13 = tmpT_array[0];
            float t22 = tmpT_array[1];
            float t3 = tmpT_array[2];
            int p0002 = (int) (((float) p0000) + (((float) (p0001 - p0000)) * t13));
            int p0102 = (int) (((float) p0100) + (((float) (p0101 - p0100)) * t13));
            int p1002 = (int) (((float) p1000) + (((float) (p1001 - p1000)) * t13));
            int p1102 = (int) (((float) p1100) + (((float) (p1101 - p1100)) * t13));
            int p004 = (int) (((float) p0002) + (((float) (((int) (((float) p0010) + (((float) (p0011 - p0010)) * t13))) - p0002)) * t22));
            int p103 = (int) (((float) p1002) + (((float) (((int) (((float) p1010) + (((float) (p1011 - p1010)) * t13))) - p1002)) * t22));
            int p03 = (int) (((float) p004) + (((float) (((int) (((float) p0102) + (((float) (((int) (((float) p0110) + (((float) (p0111 - p0110)) * t13))) - p0102)) * t22))) - p004)) * t3));
            return (int) (((float) p03) + (((float) (((int) (((float) p103) + (((float) (((int) (((float) p1102) + (((float) (((int) (((float) p1110) + (((float) (p1111 - p1110)) * t13))) - p1102)) * t22))) - p103)) * t3))) - p03)) * tmpT_array[3]));
        } else {
            if (Live2D.L2D_VERBOSE) {
                System.out.printf("=================================================\n", new Object[0]);
                System.out.printf("未対応の補間回数 %d \n", new Object[]{Integer.valueOf(interpolateCount)});
                System.out.printf("Live2D.h / PIVOT_TABLE_SIZE を大きくして下さい\n", new Object[0]);
                System.out.printf("=================================================\n", new Object[0]);
            }
            return 0;
        }
    }

    public static float interpolateFloat(ModelContext mdc, PivotManager pivotManager, boolean[] ret_paramOutside, float[] pivotFloat) throws Exception {
        int interpolateCount = pivotManager.calcPivotValue(mdc, ret_paramOutside);
        byte[] pivotTableIndices = mdc.getTmpPivotTableIndicesRef();
        float[] tmpT_array = mdc.getTmpT_ArrayRef();
        pivotManager.calcPivotIndexies(pivotTableIndices, tmpT_array, interpolateCount);
        if (interpolateCount <= 0) {
            return pivotFloat[pivotTableIndices[0]];
        }
        if (interpolateCount == 1) {
            float p00 = pivotFloat[pivotTableIndices[0]];
            return ((pivotFloat[pivotTableIndices[1]] - p00) * tmpT_array[0]) + p00;
        } else if (interpolateCount == 2) {
            float p002 = pivotFloat[pivotTableIndices[0]];
            float p01 = pivotFloat[pivotTableIndices[1]];
            float p10 = pivotFloat[pivotTableIndices[2]];
            float p11 = pivotFloat[pivotTableIndices[3]];
            float t1 = tmpT_array[0];
            float t2 = tmpT_array[1];
            return ((1.0f - t2) * (((p01 - p002) * t1) + p002)) + ((((p11 - p10) * t1) + p10) * t2);
        } else if (interpolateCount == 3) {
            float p000 = pivotFloat[pivotTableIndices[0]];
            float p001 = pivotFloat[pivotTableIndices[1]];
            float p010 = pivotFloat[pivotTableIndices[2]];
            float p011 = pivotFloat[pivotTableIndices[3]];
            float p100 = pivotFloat[pivotTableIndices[4]];
            float p101 = pivotFloat[pivotTableIndices[5]];
            float p110 = pivotFloat[pivotTableIndices[6]];
            float p111 = pivotFloat[pivotTableIndices[7]];
            float t12 = tmpT_array[0];
            float t22 = tmpT_array[1];
            float t3 = tmpT_array[2];
            return ((1.0f - t3) * (((1.0f - t22) * (((p001 - p000) * t12) + p000)) + ((((p011 - p010) * t12) + p010) * t22))) + ((((1.0f - t22) * (((p101 - p100) * t12) + p100)) + ((((p111 - p110) * t12) + p110) * t22)) * t3);
        } else if (interpolateCount == 4) {
            float p0000 = pivotFloat[pivotTableIndices[0]];
            float p0001 = pivotFloat[pivotTableIndices[1]];
            float p0010 = pivotFloat[pivotTableIndices[2]];
            float p0011 = pivotFloat[pivotTableIndices[3]];
            float p0100 = pivotFloat[pivotTableIndices[4]];
            float p0101 = pivotFloat[pivotTableIndices[5]];
            float p0110 = pivotFloat[pivotTableIndices[6]];
            float p0111 = pivotFloat[pivotTableIndices[7]];
            float p1000 = pivotFloat[pivotTableIndices[8]];
            float p1001 = pivotFloat[pivotTableIndices[9]];
            float p1010 = pivotFloat[pivotTableIndices[10]];
            float p1011 = pivotFloat[pivotTableIndices[11]];
            float p1100 = pivotFloat[pivotTableIndices[12]];
            float p1101 = pivotFloat[pivotTableIndices[13]];
            float p1110 = pivotFloat[pivotTableIndices[14]];
            float p1111 = pivotFloat[pivotTableIndices[15]];
            float t13 = tmpT_array[0];
            float t23 = tmpT_array[1];
            float t32 = tmpT_array[2];
            float t4 = tmpT_array[3];
            return ((1.0f - t4) * (((1.0f - t32) * (((1.0f - t23) * (((p0001 - p0000) * t13) + p0000)) + ((((p0011 - p0010) * t13) + p0010) * t23))) + ((((1.0f - t23) * (((p0101 - p0100) * t13) + p0100)) + ((((p0111 - p0110) * t13) + p0110) * t23)) * t32))) + ((((1.0f - t32) * (((1.0f - t23) * (((p1001 - p1000) * t13) + p1000)) + ((((p1011 - p1010) * t13) + p1010) * t23))) + ((((1.0f - t23) * (((p1101 - p1100) * t13) + p1100)) + ((((p1111 - p1110) * t13) + p1110) * t23)) * t32)) * t4);
        } else {
            if (Live2D.L2D_VERBOSE) {
                System.err.printf("=================================================\n", new Object[0]);
                System.err.printf("未対応の補間回数 %d \n", new Object[]{Integer.valueOf(interpolateCount)});
                System.err.printf("Live2D.h / PIVOT_TABLE_SIZE を大きくして下さい\n", new Object[0]);
                System.err.printf("=================================================\n", new Object[0]);
            }
            return 0.0f;
        }
    }

    public static void interpolatePoints(ModelContext mdc, PivotManager pivotManager, boolean[] ret_paramOutside, int numPts, ArrayList<float[]> pivotPoints, float[] dst_points, int pt_offset, int pt_step) throws Exception {
        int interpolateCount = pivotManager.calcPivotValue(mdc, ret_paramOutside);
        byte[] pivotTableIndices = mdc.getTmpPivotTableIndicesRef();
        float[] tmpT_array = mdc.getTmpT_ArrayRef();
        pivotManager.calcPivotIndexies(pivotTableIndices, tmpT_array, interpolateCount);
        int length = numPts * 2;
        int di = pt_offset;
        if (interpolateCount <= 0) {
            float[] p00 = pivotPoints.get(pivotTableIndices[0]);
            if (pt_step == 2 && pt_offset == 0) {
                System.arraycopy(p00, 0, dst_points, 0, length);
                return;
            }
            int i = 0;
            while (i < length) {
                int i2 = i + 1;
                dst_points[di] = p00[i];
                i = i2 + 1;
                dst_points[di + 1] = p00[i2];
                di += pt_step;
            }
        } else if (interpolateCount == 1) {
            float[] p002 = pivotPoints.get(pivotTableIndices[0]);
            float[] p01 = pivotPoints.get(pivotTableIndices[1]);
            float t1 = tmpT_array[0];
            float t1_1 = 1.0f - t1;
            int i3 = 0;
            while (i3 < length) {
                dst_points[di] = (p002[i3] * t1_1) + (p01[i3] * t1);
                int i4 = i3 + 1;
                dst_points[di + 1] = (p002[i4] * t1_1) + (p01[i4] * t1);
                i3 = i4 + 1;
                di += pt_step;
            }
        } else if (interpolateCount == 2) {
            float[] p003 = pivotPoints.get(pivotTableIndices[0]);
            float[] p012 = pivotPoints.get(pivotTableIndices[1]);
            float[] p10 = pivotPoints.get(pivotTableIndices[2]);
            float[] p11 = pivotPoints.get(pivotTableIndices[3]);
            float t12 = tmpT_array[0];
            float t2 = tmpT_array[1];
            float t1_12 = 1.0f - t12;
            float t2_1 = 1.0f - t2;
            int i5 = 0;
            while (i5 < length) {
                dst_points[di] = (((p003[i5] * t1_12) + (p012[i5] * t12)) * t2_1) + (((p10[i5] * t1_12) + (p11[i5] * t12)) * t2);
                int i6 = i5 + 1;
                dst_points[di + 1] = (((p003[i6] * t1_12) + (p012[i6] * t12)) * t2_1) + (((p10[i6] * t1_12) + (p11[i6] * t12)) * t2);
                i5 = i6 + 1;
                di += pt_step;
            }
        } else if (interpolateCount == 3) {
            float[] p000 = pivotPoints.get(pivotTableIndices[0]);
            float[] p001 = pivotPoints.get(pivotTableIndices[1]);
            float[] p010 = pivotPoints.get(pivotTableIndices[2]);
            float[] p011 = pivotPoints.get(pivotTableIndices[3]);
            float[] p100 = pivotPoints.get(pivotTableIndices[4]);
            float[] p101 = pivotPoints.get(pivotTableIndices[5]);
            float[] p110 = pivotPoints.get(pivotTableIndices[6]);
            float[] p111 = pivotPoints.get(pivotTableIndices[7]);
            float t13 = tmpT_array[0];
            float t22 = tmpT_array[1];
            float t3 = tmpT_array[2];
            float t1_13 = 1.0f - t13;
            float t2_12 = 1.0f - t22;
            float t3_1 = 1.0f - t3;
            int i7 = 0;
            while (i7 < length) {
                dst_points[di] = (((((p000[i7] * t1_13) + (p001[i7] * t13)) * t2_12) + (((p010[i7] * t1_13) + (p011[i7] * t13)) * t22)) * t3_1) + (((((p100[i7] * t1_13) + (p101[i7] * t13)) * t2_12) + (((p110[i7] * t1_13) + (p111[i7] * t13)) * t22)) * t3);
                int i8 = i7 + 1;
                dst_points[di + 1] = (((((p000[i8] * t1_13) + (p001[i8] * t13)) * t2_12) + (((p010[i8] * t1_13) + (p011[i8] * t13)) * t22)) * t3_1) + (((((p100[i8] * t1_13) + (p101[i8] * t13)) * t2_12) + (((p110[i8] * t1_13) + (p111[i8] * t13)) * t22)) * t3);
                i7 = i8 + 1;
                di += pt_step;
            }
        } else if (interpolateCount == 4) {
            float[] p0000 = pivotPoints.get(pivotTableIndices[0]);
            float[] p0001 = pivotPoints.get(pivotTableIndices[1]);
            float[] p0010 = pivotPoints.get(pivotTableIndices[2]);
            float[] p0011 = pivotPoints.get(pivotTableIndices[3]);
            float[] p0100 = pivotPoints.get(pivotTableIndices[4]);
            float[] p0101 = pivotPoints.get(pivotTableIndices[5]);
            float[] p0110 = pivotPoints.get(pivotTableIndices[6]);
            float[] p0111 = pivotPoints.get(pivotTableIndices[7]);
            float[] p1000 = pivotPoints.get(pivotTableIndices[8]);
            float[] p1001 = pivotPoints.get(pivotTableIndices[9]);
            float[] p1010 = pivotPoints.get(pivotTableIndices[10]);
            float[] p1011 = pivotPoints.get(pivotTableIndices[11]);
            float[] p1100 = pivotPoints.get(pivotTableIndices[12]);
            float[] p1101 = pivotPoints.get(pivotTableIndices[13]);
            float[] p1110 = pivotPoints.get(pivotTableIndices[14]);
            float[] p1111 = pivotPoints.get(pivotTableIndices[15]);
            float t14 = tmpT_array[0];
            float t23 = tmpT_array[1];
            float t32 = tmpT_array[2];
            float t4 = tmpT_array[3];
            float t1_14 = 1.0f - t14;
            float t2_13 = 1.0f - t23;
            float t3_12 = 1.0f - t32;
            float t4_1 = 1.0f - t4;
            int i9 = 0;
            while (i9 < length) {
                dst_points[di] = (((((((p0000[i9] * t1_14) + (p0001[i9] * t14)) * t2_13) + (((p0010[i9] * t1_14) + (p0011[i9] * t14)) * t23)) * t3_12) + (((((p0100[i9] * t1_14) + (p0101[i9] * t14)) * t2_13) + (((p0110[i9] * t1_14) + (p0111[i9] * t14)) * t23)) * t32)) * t4_1) + (((((((p1000[i9] * t1_14) + (p1001[i9] * t14)) * t2_13) + (((p1010[i9] * t1_14) + (p1011[i9] * t14)) * t23)) * t3_12) + (((((p1100[i9] * t1_14) + (p1101[i9] * t14)) * t2_13) + (((p1110[i9] * t1_14) + (p1111[i9] * t14)) * t23)) * t32)) * t4);
                int i10 = i9 + 1;
                dst_points[di + 1] = (((((((p0000[i10] * t1_14) + (p0001[i10] * t14)) * t2_13) + (((p0010[i10] * t1_14) + (p0011[i10] * t14)) * t23)) * t3_12) + (((((p0100[i10] * t1_14) + (p0101[i10] * t14)) * t2_13) + (((p0110[i10] * t1_14) + (p0111[i10] * t14)) * t23)) * t32)) * t4_1) + (((((((p1000[i10] * t1_14) + (p1001[i10] * t14)) * t2_13) + (((p1010[i10] * t1_14) + (p1011[i10] * t14)) * t23)) * t3_12) + (((((p1100[i10] * t1_14) + (p1101[i10] * t14)) * t2_13) + (((p1110[i10] * t1_14) + (p1111[i10] * t14)) * t23)) * t32)) * t4);
                i9 = i10 + 1;
                di += pt_step;
            }
        } else if (Live2D.L2D_VERBOSE) {
            System.out.printf("=================================================\n", new Object[0]);
            System.out.printf("未対応の補間回数 %d \n", new Object[]{Integer.valueOf(interpolateCount)});
            System.out.printf("Live2D.h / PIVOT_TABLE_SIZE を大きくして下さい\n", new Object[0]);
            System.out.printf("=================================================\n", new Object[0]);
        }
    }
}
