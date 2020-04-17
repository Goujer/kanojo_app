package jp.live2d.base;

import java.util.ArrayList;
import jp.live2d.Live2D;
import jp.live2d.ModelContext;
import jp.live2d.id.BaseDataID;
import jp.live2d.io.BReader;
import jp.live2d.param.PivotManager;
import jp.live2d.util.UtDebug;
import jp.live2d.util.UtMath;

public class BDAffine extends IBaseData {
    static boolean[] tmp_ba_1 = new boolean[1];
    static float[] tmp_fa_1_1 = new float[2];
    static float[] tmp_fa_1_2 = new float[2];
    static float[] tmp_fa_1_3 = new float[2];
    static float[] tmp_fa_2_1 = new float[2];
    static float[] tmp_fa_2_2 = new float[2];
    static float[] tmp_fa_2_3 = new float[2];
    ArrayList<AffineEnt> affines = null;
    AffineEnt interpolatedAffine = null;
    PivotManager pivotManager = null;
    int tmpBaseDataIndex = -2;
    AffineEnt transformedAffine = null;

    public void initDirect() {
        this.pivotManager = new PivotManager();
        this.pivotManager.initDirect();
        this.affines = new ArrayList<>();
    }

    public void readV2(BReader br) throws Exception {
        super.readV2(br);
        this.pivotManager = (PivotManager) br.readObject();
        this.affines = (ArrayList) br.readObject();
    }

    public void init() {
        setDirty(false);
        this.interpolatedAffine = new AffineEnt();
        if (needTransform()) {
            this.transformedAffine = new AffineEnt();
        }
    }

    public void setupInterpolate(ModelContext mdc) throws Exception {
        if (isDirty()) {
            init();
        }
        if (this.pivotManager.checkParamUpdated(mdc)) {
            boolean[] ret_paramOutside = tmp_ba_1;
            ret_paramOutside[0] = false;
            int interpolateCount = this.pivotManager.calcPivotValue(mdc, ret_paramOutside);
            byte[] pivotTableIndices = mdc.getTmpPivotTableIndicesRef();
            float[] tmpT_array = mdc.getTmpT_ArrayRef();
            this.pivotManager.calcPivotIndexies(pivotTableIndices, tmpT_array, interpolateCount);
            if (interpolateCount <= 0) {
                this.interpolatedAffine.init(this.affines.get(pivotTableIndices[0]));
            } else if (interpolateCount == 1) {
                AffineEnt p00 = this.affines.get(pivotTableIndices[0]);
                AffineEnt p01 = this.affines.get(pivotTableIndices[1]);
                float t1 = tmpT_array[0];
                this.interpolatedAffine.originX = p00.originX + ((p01.originX - p00.originX) * t1);
                this.interpolatedAffine.originY = p00.originY + ((p01.originY - p00.originY) * t1);
                this.interpolatedAffine.scaleX = p00.scaleX + ((p01.scaleX - p00.scaleX) * t1);
                this.interpolatedAffine.scaleY = p00.scaleY + ((p01.scaleY - p00.scaleY) * t1);
                this.interpolatedAffine.rotateDeg = p00.rotateDeg + ((p01.rotateDeg - p00.rotateDeg) * t1);
            } else if (interpolateCount == 2) {
                AffineEnt p002 = this.affines.get(pivotTableIndices[0]);
                AffineEnt p012 = this.affines.get(pivotTableIndices[1]);
                AffineEnt p10 = this.affines.get(pivotTableIndices[2]);
                AffineEnt p11 = this.affines.get(pivotTableIndices[3]);
                float t12 = tmpT_array[0];
                float t2 = tmpT_array[1];
                float v1 = p002.originX + ((p012.originX - p002.originX) * t12);
                float v2 = p10.originX + ((p11.originX - p10.originX) * t12);
                this.interpolatedAffine.originX = ((v2 - v1) * t2) + v1;
                float v12 = p002.originY + ((p012.originY - p002.originY) * t12);
                float v22 = p10.originY + ((p11.originY - p10.originY) * t12);
                this.interpolatedAffine.originY = ((v22 - v12) * t2) + v12;
                float v13 = p002.scaleX + ((p012.scaleX - p002.scaleX) * t12);
                float v23 = p10.scaleX + ((p11.scaleX - p10.scaleX) * t12);
                this.interpolatedAffine.scaleX = ((v23 - v13) * t2) + v13;
                float v14 = p002.scaleY + ((p012.scaleY - p002.scaleY) * t12);
                float v24 = p10.scaleY + ((p11.scaleY - p10.scaleY) * t12);
                this.interpolatedAffine.scaleY = ((v24 - v14) * t2) + v14;
                float v15 = p002.rotateDeg + ((p012.rotateDeg - p002.rotateDeg) * t12);
                float v25 = p10.rotateDeg + ((p11.rotateDeg - p10.rotateDeg) * t12);
                this.interpolatedAffine.rotateDeg = ((v25 - v15) * t2) + v15;
            } else if (interpolateCount == 3) {
                AffineEnt p000 = this.affines.get(pivotTableIndices[0]);
                AffineEnt p001 = this.affines.get(pivotTableIndices[1]);
                AffineEnt p010 = this.affines.get(pivotTableIndices[2]);
                AffineEnt p011 = this.affines.get(pivotTableIndices[3]);
                AffineEnt p100 = this.affines.get(pivotTableIndices[4]);
                AffineEnt p101 = this.affines.get(pivotTableIndices[5]);
                AffineEnt p110 = this.affines.get(pivotTableIndices[6]);
                AffineEnt p111 = this.affines.get(pivotTableIndices[7]);
                float t13 = tmpT_array[0];
                float t22 = tmpT_array[1];
                float t3 = tmpT_array[2];
                float v16 = p000.originX + ((p001.originX - p000.originX) * t13);
                float v26 = p010.originX + ((p011.originX - p010.originX) * t13);
                float v3 = p100.originX + ((p101.originX - p100.originX) * t13);
                float v4 = p110.originX + ((p111.originX - p110.originX) * t13);
                this.interpolatedAffine.originX = ((1.0f - t3) * (((v26 - v16) * t22) + v16)) + ((((v4 - v3) * t22) + v3) * t3);
                float v17 = p000.originY + ((p001.originY - p000.originY) * t13);
                float v27 = p010.originY + ((p011.originY - p010.originY) * t13);
                float v32 = p100.originY + ((p101.originY - p100.originY) * t13);
                float v42 = p110.originY + ((p111.originY - p110.originY) * t13);
                this.interpolatedAffine.originY = ((1.0f - t3) * (((v27 - v17) * t22) + v17)) + ((((v42 - v32) * t22) + v32) * t3);
                float v18 = p000.scaleX + ((p001.scaleX - p000.scaleX) * t13);
                float v28 = p010.scaleX + ((p011.scaleX - p010.scaleX) * t13);
                float v33 = p100.scaleX + ((p101.scaleX - p100.scaleX) * t13);
                float v43 = p110.scaleX + ((p111.scaleX - p110.scaleX) * t13);
                this.interpolatedAffine.scaleX = ((1.0f - t3) * (((v28 - v18) * t22) + v18)) + ((((v43 - v33) * t22) + v33) * t3);
                float v19 = p000.scaleY + ((p001.scaleY - p000.scaleY) * t13);
                float v29 = p010.scaleY + ((p011.scaleY - p010.scaleY) * t13);
                float v34 = p100.scaleY + ((p101.scaleY - p100.scaleY) * t13);
                float v44 = p110.scaleY + ((p111.scaleY - p110.scaleY) * t13);
                this.interpolatedAffine.scaleY = ((1.0f - t3) * (((v29 - v19) * t22) + v19)) + ((((v44 - v34) * t22) + v34) * t3);
                float v110 = p000.rotateDeg + ((p001.rotateDeg - p000.rotateDeg) * t13);
                float v210 = p010.rotateDeg + ((p011.rotateDeg - p010.rotateDeg) * t13);
                float v35 = p100.rotateDeg + ((p101.rotateDeg - p100.rotateDeg) * t13);
                float v45 = p110.rotateDeg + ((p111.rotateDeg - p110.rotateDeg) * t13);
                this.interpolatedAffine.rotateDeg = ((1.0f - t3) * (((v210 - v110) * t22) + v110)) + ((((v45 - v35) * t22) + v35) * t3);
            } else if (interpolateCount == 4) {
                AffineEnt p0000 = this.affines.get(pivotTableIndices[0]);
                AffineEnt p0001 = this.affines.get(pivotTableIndices[1]);
                AffineEnt p0010 = this.affines.get(pivotTableIndices[2]);
                AffineEnt p0011 = this.affines.get(pivotTableIndices[3]);
                AffineEnt p0100 = this.affines.get(pivotTableIndices[4]);
                AffineEnt p0101 = this.affines.get(pivotTableIndices[5]);
                AffineEnt p0110 = this.affines.get(pivotTableIndices[6]);
                AffineEnt p0111 = this.affines.get(pivotTableIndices[7]);
                AffineEnt p1000 = this.affines.get(pivotTableIndices[8]);
                AffineEnt p1001 = this.affines.get(pivotTableIndices[9]);
                AffineEnt p1010 = this.affines.get(pivotTableIndices[10]);
                AffineEnt p1011 = this.affines.get(pivotTableIndices[11]);
                AffineEnt p1100 = this.affines.get(pivotTableIndices[12]);
                AffineEnt p1101 = this.affines.get(pivotTableIndices[13]);
                AffineEnt p1110 = this.affines.get(pivotTableIndices[14]);
                AffineEnt p1111 = this.affines.get(pivotTableIndices[15]);
                float t14 = tmpT_array[0];
                float t23 = tmpT_array[1];
                float t32 = tmpT_array[2];
                float t4 = tmpT_array[3];
                float v111 = p0000.originX + ((p0001.originX - p0000.originX) * t14);
                float v211 = p0010.originX + ((p0011.originX - p0010.originX) * t14);
                float v36 = p0100.originX + ((p0101.originX - p0100.originX) * t14);
                float v46 = p0110.originX + ((p0111.originX - p0110.originX) * t14);
                float v5 = p1000.originX + ((p1001.originX - p1000.originX) * t14);
                float v6 = p1010.originX + ((p1011.originX - p1010.originX) * t14);
                float v7 = p1100.originX + ((p1101.originX - p1100.originX) * t14);
                float v8 = p1110.originX + ((p1111.originX - p1110.originX) * t14);
                this.interpolatedAffine.originX = ((1.0f - t4) * (((1.0f - t32) * (((v211 - v111) * t23) + v111)) + ((((v46 - v36) * t23) + v36) * t32))) + ((((1.0f - t32) * (((v6 - v5) * t23) + v5)) + ((((v8 - v7) * t23) + v7) * t32)) * t4);
                float v112 = p0000.originY + ((p0001.originY - p0000.originY) * t14);
                float v212 = p0010.originY + ((p0011.originY - p0010.originY) * t14);
                float v37 = p0100.originY + ((p0101.originY - p0100.originY) * t14);
                float v47 = p0110.originY + ((p0111.originY - p0110.originY) * t14);
                float v52 = p1000.originY + ((p1001.originY - p1000.originY) * t14);
                float v62 = p1010.originY + ((p1011.originY - p1010.originY) * t14);
                float v72 = p1100.originY + ((p1101.originY - p1100.originY) * t14);
                float v82 = p1110.originY + ((p1111.originY - p1110.originY) * t14);
                this.interpolatedAffine.originY = ((1.0f - t4) * (((1.0f - t32) * (((v212 - v112) * t23) + v112)) + ((((v47 - v37) * t23) + v37) * t32))) + ((((1.0f - t32) * (((v62 - v52) * t23) + v52)) + ((((v82 - v72) * t23) + v72) * t32)) * t4);
                float v113 = p0000.scaleX + ((p0001.scaleX - p0000.scaleX) * t14);
                float v213 = p0010.scaleX + ((p0011.scaleX - p0010.scaleX) * t14);
                float v38 = p0100.scaleX + ((p0101.scaleX - p0100.scaleX) * t14);
                float v48 = p0110.scaleX + ((p0111.scaleX - p0110.scaleX) * t14);
                float v53 = p1000.scaleX + ((p1001.scaleX - p1000.scaleX) * t14);
                float v63 = p1010.scaleX + ((p1011.scaleX - p1010.scaleX) * t14);
                float v73 = p1100.scaleX + ((p1101.scaleX - p1100.scaleX) * t14);
                float v83 = p1110.scaleX + ((p1111.scaleX - p1110.scaleX) * t14);
                this.interpolatedAffine.scaleX = ((1.0f - t4) * (((1.0f - t32) * (((v213 - v113) * t23) + v113)) + ((((v48 - v38) * t23) + v38) * t32))) + ((((1.0f - t32) * (((v63 - v53) * t23) + v53)) + ((((v83 - v73) * t23) + v73) * t32)) * t4);
                float v114 = p0000.scaleY + ((p0001.scaleY - p0000.scaleY) * t14);
                float v214 = p0010.scaleY + ((p0011.scaleY - p0010.scaleY) * t14);
                float v39 = p0100.scaleY + ((p0101.scaleY - p0100.scaleY) * t14);
                float v49 = p0110.scaleY + ((p0111.scaleY - p0110.scaleY) * t14);
                float v54 = p1000.scaleY + ((p1001.scaleY - p1000.scaleY) * t14);
                float v64 = p1010.scaleY + ((p1011.scaleY - p1010.scaleY) * t14);
                float v74 = p1100.scaleY + ((p1101.scaleY - p1100.scaleY) * t14);
                float v84 = p1110.scaleY + ((p1111.scaleY - p1110.scaleY) * t14);
                this.interpolatedAffine.scaleY = ((1.0f - t4) * (((1.0f - t32) * (((v214 - v114) * t23) + v114)) + ((((v49 - v39) * t23) + v39) * t32))) + ((((1.0f - t32) * (((v64 - v54) * t23) + v54)) + ((((v84 - v74) * t23) + v74) * t32)) * t4);
                float v115 = p0000.rotateDeg + ((p0001.rotateDeg - p0000.rotateDeg) * t14);
                float v215 = p0010.rotateDeg + ((p0011.rotateDeg - p0010.rotateDeg) * t14);
                float v310 = p0100.rotateDeg + ((p0101.rotateDeg - p0100.rotateDeg) * t14);
                float v410 = p0110.rotateDeg + ((p0111.rotateDeg - p0110.rotateDeg) * t14);
                float v55 = p1000.rotateDeg + ((p1001.rotateDeg - p1000.rotateDeg) * t14);
                float v65 = p1010.rotateDeg + ((p1011.rotateDeg - p1010.rotateDeg) * t14);
                float v75 = p1100.rotateDeg + ((p1101.rotateDeg - p1100.rotateDeg) * t14);
                float v85 = p1110.rotateDeg + ((p1111.rotateDeg - p1110.rotateDeg) * t14);
                this.interpolatedAffine.rotateDeg = ((1.0f - t4) * (((1.0f - t32) * (((v215 - v115) * t23) + v115)) + ((((v410 - v310) * t23) + v310) * t32))) + ((((1.0f - t32) * (((v65 - v55) * t23) + v55)) + ((((v85 - v75) * t23) + v75) * t32)) * t4);
            } else if (Live2D.L2D_VERBOSE) {
                System.out.printf("=================================================\n", new Object[0]);
                System.out.printf("未対応の補間回数 %d \n", new Object[]{Integer.valueOf(interpolateCount)});
                System.out.printf("Live2D.h / PIVOT_TABLE_SIZE を大きくして下さい\n", new Object[0]);
                System.out.printf("=================================================\n", new Object[0]);
            }
        }
    }

    public void setupTransform(ModelContext mdc) throws Exception {
        if (needTransform()) {
            BaseDataID targetBaseID = getTargetBaseDataID();
            if (this.tmpBaseDataIndex == -2) {
                this.tmpBaseDataIndex = mdc.getBaseIndex(targetBaseID);
            }
            if (this.tmpBaseDataIndex >= 0) {
                IBaseData baseData = mdc.getBaseData(this.tmpBaseDataIndex);
                if (baseData != null) {
                    float[] points = tmp_fa_1_1;
                    points[0] = this.interpolatedAffine.originX;
                    points[1] = this.interpolatedAffine.originY;
                    float[] preDir = tmp_fa_1_2;
                    preDir[0] = 0.0f;
                    preDir[1] = -0.1f;
                    float[] postDir = tmp_fa_1_3;
                    getDirectionOnDst(baseData, points, preDir, postDir);
                    baseData.transformPoints(points, points, 1, 0, 2);
                    this.transformedAffine.originX = points[0];
                    this.transformedAffine.originY = points[1];
                    this.transformedAffine.scaleX = this.interpolatedAffine.scaleX;
                    this.transformedAffine.scaleY = this.interpolatedAffine.scaleY;
                    this.transformedAffine.rotateDeg = this.interpolatedAffine.rotateDeg - (UtMath.RAD_TO_DEG_F * ((float) UtMath.getAngleNotAbs(preDir, postDir)));
                }
            } else if (Live2D.L2D_VERBOSE) {
                UtDebug.error("未対応の座標系 :: %s", targetBaseID);
            }
        }
    }

    public void transformPoints(float[] src, float[] dst, int numPoint, int pt_offset, int pt_step) {
        AffineEnt ent;
        if (this.transformedAffine != null) {
            ent = this.transformedAffine;
        } else {
            ent = this.interpolatedAffine;
        }
        float sinQ = (float) Math.sin((double) (UtMath.DEG_TO_RAD_F * ent.rotateDeg));
        float cosQ = (float) Math.cos((double) (UtMath.DEG_TO_RAD_F * ent.rotateDeg));
        float m00 = cosQ * ent.scaleX;
        float m01 = (-sinQ) * ent.scaleY;
        float m10 = sinQ * ent.scaleX;
        float m11 = cosQ * ent.scaleY;
        float tx = ent.originX;
        float ty = ent.originY;
        int length = numPoint * pt_step;
        int index = pt_offset;
        while (index < length) {
            float x = src[index];
            float y = src[index + 1];
            dst[index] = (m00 * x) + (m01 * y) + tx;
            dst[index + 1] = (m10 * x) + (m11 * y) + ty;
            index += pt_step;
        }
    }

    /* access modifiers changed from: package-private */
    public void getDirectionOnDst(IBaseData targetToDst, float[] srcOrigin, float[] srcDir, float[] retDir) throws Exception {
        float[] dstOrigin = tmp_fa_2_1;
        tmp_fa_2_1[0] = srcOrigin[0];
        tmp_fa_2_1[1] = srcOrigin[1];
        targetToDst.transformPoints(dstOrigin, dstOrigin, 1, 0, 2);
        float[] tmpv = tmp_fa_2_2;
        float[] dirv = tmp_fa_2_3;
        float scale = 1.0f;
        int i = 0;
        while (i < 10) {
            dirv[0] = srcOrigin[0] + (srcDir[0] * scale);
            dirv[1] = srcOrigin[1] + (srcDir[1] * scale);
            targetToDst.transformPoints(dirv, tmpv, 1, 0, 2);
            tmpv[0] = tmpv[0] - dstOrigin[0];
            tmpv[1] = tmpv[1] - dstOrigin[1];
            if (tmpv[0] == 0.0f && tmpv[1] == 0.0f) {
                dirv[0] = srcOrigin[0] - (srcDir[0] * scale);
                dirv[1] = srcOrigin[1] - (srcDir[1] * scale);
                targetToDst.transformPoints(dirv, tmpv, 1, 0, 2);
                tmpv[0] = tmpv[0] - dstOrigin[0];
                tmpv[1] = tmpv[1] - dstOrigin[1];
                if (tmpv[0] == 0.0f && tmpv[1] == 0.0f) {
                    scale = (float) (((double) scale) * 0.1d);
                    i++;
                } else {
                    tmpv[0] = -tmpv[0];
                    tmpv[0] = -tmpv[0];
                    retDir[0] = tmpv[0];
                    retDir[1] = tmpv[1];
                    return;
                }
            } else {
                retDir[0] = tmpv[0];
                retDir[1] = tmpv[1];
                return;
            }
        }
        if (Live2D.L2D_VERBOSE) {
            System.out.printf("failed to transform BDAffine＼n", new Object[0]);
        }
    }
}
