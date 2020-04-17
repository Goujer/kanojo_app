package jp.live2d.base;

import java.util.ArrayList;
import jp.live2d.Live2D;
import jp.live2d.ModelContext;
import jp.live2d.id.BaseDataID;
import jp.live2d.io.BReader;
import jp.live2d.param.PivotManager;
import jp.live2d.util.UtDebug;
import jp.live2d.util.UtInterpolate;

public class BDBoxGrid extends IBaseData {
    static boolean[] tmp_ba_1 = new boolean[1];
    int col = 0;
    float[] interpolatedPoints = null;
    PivotManager pivotManager = null;
    ArrayList<float[]> pivotPoints = null;
    int row = 0;
    int tmpBaseDataIndex = -2;
    float[] transformedPoints = null;

    public void initDirect() {
        this.pivotManager = new PivotManager();
        this.pivotManager.initDirect();
    }

    public void readV2(BReader br) throws Exception {
        super.readV2(br);
        this.col = br.readInt();
        this.row = br.readInt();
        this.pivotManager = (PivotManager) br.readObject();
        this.pivotPoints = (ArrayList) br.readObject();
    }

    public void init() {
        setDirty(false);
        int pts = (this.col + 1) * (this.row + 1);
        if (this.interpolatedPoints != null) {
            this.interpolatedPoints = null;
        }
        this.interpolatedPoints = new float[(pts * 2)];
        if (this.transformedPoints != null) {
            this.transformedPoints = null;
        }
        if (needTransform()) {
            this.transformedPoints = new float[(pts * 2)];
        } else {
            this.transformedPoints = null;
        }
    }

    public void setupInterpolate(ModelContext mdc) throws Exception {
        if (isDirty()) {
            init();
        }
        if (this.pivotManager.checkParamUpdated(mdc)) {
            int numPts = getNumPts();
            boolean[] ret_paramOutside = tmp_ba_1;
            ret_paramOutside[0] = false;
            UtInterpolate.interpolatePoints(mdc, this.pivotManager, ret_paramOutside, numPts, this.pivotPoints, this.interpolatedPoints, 0, 2);
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
                    baseData.transformPoints(this.interpolatedPoints, this.transformedPoints, getNumPts(), 0, 2);
                }
            } else if (Live2D.L2D_VERBOSE) {
                UtDebug.error("未対応の座標系 :: %s", targetBaseID);
            }
        }
    }

    public void transformPoints(float[] src, float[] dst, int numPoint, int pt_offset, int pt_step) {
        float tx;
        float ty;
        int pi;
        int _col = this.col;
        int _row = this.row;
        int length = numPoint * pt_step;
        float[] tmpBasePoints = this.transformedPoints != null ? this.transformedPoints : this.interpolatedPoints;
        int index = pt_offset;
        while (index < length) {
            if (Live2D.L2D_RANGE_CHECK_POINT) {
                float x = src[index];
                float y = src[index + 1];
                if (x < 0.0f) {
                    x = 0.0f;
                } else if (x > 1.0f) {
                    x = 1.0f;
                }
                if (y < 0.0f) {
                    y = 0.0f;
                } else if (y > 1.0f) {
                    y = 1.0f;
                }
                float x2 = x * ((float) _col);
                float y2 = y * ((float) _row);
                int xi = (int) x2;
                int yi = (int) y2;
                tx = x2 - ((float) xi);
                ty = y2 - ((float) yi);
                pi = (((_col + 1) * yi) + xi) * 2;
            } else {
                float x3 = src[index] * ((float) _col);
                float y3 = src[index + 1] * ((float) _row);
                tx = x3 - ((float) ((int) x3));
                ty = y3 - ((float) ((int) y3));
                pi = (((int) x3) + (((int) y3) * (_col + 1))) * 2;
            }
            if (tx + ty < 1.0f) {
                float x00 = tmpBasePoints[pi];
                float y00 = tmpBasePoints[pi + 1];
                float x01 = tmpBasePoints[pi + 2];
                float y01 = tmpBasePoints[pi + 3];
                float x10 = tmpBasePoints[((_col + 1) * 2) + pi];
                float y10 = tmpBasePoints[((_col + 1) * 2) + pi + 1];
                dst[index] = ((x01 - x00) * tx) + x00 + ((x10 - x00) * ty);
                dst[index + 1] = ((y01 - y00) * tx) + y00 + ((y10 - y00) * ty);
            } else {
                float x012 = tmpBasePoints[pi + 2];
                float y012 = tmpBasePoints[pi + 3];
                float x102 = tmpBasePoints[((_col + 1) * 2) + pi];
                float y102 = tmpBasePoints[((_col + 1) * 2) + pi + 1];
                float x11 = tmpBasePoints[((_col + 1) * 2) + pi + 2];
                float y11 = tmpBasePoints[((_col + 1) * 2) + pi + 3];
                dst[index] = ((1.0f - tx) * (x102 - x11)) + x11 + ((1.0f - ty) * (x012 - x11));
                dst[index + 1] = ((1.0f - tx) * (y102 - y11)) + y11 + ((1.0f - ty) * (y012 - y11));
            }
            index += pt_step;
        }
    }

    public int getNumPts() {
        return (this.col + 1) * (this.row + 1);
    }
}
