package jp.live2d.draw;

import java.util.ArrayList;
import java.util.HashMap;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoResource;
import jp.live2d.ModelContext;
import jp.live2d.base.IBaseData;
import jp.live2d.error.Live2DException;
import jp.live2d.graphics.DrawParam;
import jp.live2d.id.BaseDataID;
import jp.live2d.io.BReader;
import jp.live2d.param.PivotManager;
import jp.live2d.util.UtDebug;
import jp.live2d.util.UtInterpolate;

public class DDTexture extends IDrawData {
    static int INSTANCE_COUNT = 0;
    static boolean[] tmp_ba_1 = new boolean[1];
    short[] indexArray;
    float[] interpolatedPoints;
    int numPolygons = 0;
    int numPts = 0;
    HashMap<String, Object> optionData = null;
    int optionFlag;
    ArrayList<float[]> pivotPoints;
    int textureNo = -1;
    int tmpBaseDataIndex = -2;
    float[] transformedPoints;
    float[] uvmap;

    public DDTexture() {
        INSTANCE_COUNT++;
    }

    public void setTextureNo(int no) {
        this.textureNo = no;
    }

    public int getTextureNo() {
        return this.textureNo;
    }

    public float[] getUvMap() {
        return this.uvmap;
    }

    public int getOptionFlag() {
        return this.optionFlag;
    }

    public int getNumPoints() {
        return this.numPts;
    }

    public int getType() {
        return 2;
    }

    public void initDirect() {
        this.pivotManager = new PivotManager();
        this.pivotManager.initDirect();
    }

    public void readV2(BReader br) throws Exception {
        super.readV2(br);
        this.textureNo = br.readInt();
        this.numPts = br.readInt();
        this.numPolygons = br.readInt();
        int[] indexInt = (int[]) br.readObject();
        this.indexArray = new short[(this.numPolygons * 3)];
        for (int i = (this.numPolygons * 3) - 1; i >= 0; i--) {
            this.indexArray[i] = (short) indexInt[i];
        }
        this.pivotPoints = (ArrayList) br.readObject();
        this.uvmap = (float[]) br.readObject();
        if (br.getFormatVersion() >= 8) {
            this.optionFlag = br.readInt();
            if ((this.optionFlag & 1) != 0) {
                int tmp_v = br.readInt();
                if (this.optionData == null) {
                    this.optionData = new HashMap<>();
                }
                this.optionData.put(KanojoResource.DDTEXTURE_OPTION_BK_OPTION_COLOR, new Integer(tmp_v));
                return;
            }
            return;
        }
        this.optionFlag = 0;
    }

    /* access modifiers changed from: package-private */
    public void init() {
        float[] fArr = null;
        super.init();
        setDirty(false);
        int ptArraySize = this.numPts * 2;
        boolean useTransform = needTransform();
        if (this.interpolatedPoints != null) {
            this.interpolatedPoints = null;
        }
        this.interpolatedPoints = new float[ptArraySize];
        if (this.transformedPoints != null) {
            this.transformedPoints = null;
        }
        if (useTransform) {
            fArr = new float[ptArraySize];
        }
        this.transformedPoints = fArr;
        switch (1) {
            case 2:
                for (int i = this.numPts - 1; i >= 0; i--) {
                    int uvi = i << 1;
                    int index = i * 2;
                    float u = this.uvmap[uvi];
                    float v = this.uvmap[uvi + 1];
                    this.interpolatedPoints[index] = u;
                    this.interpolatedPoints[index + 1] = v;
                    this.interpolatedPoints[index + 4] = 0.0f;
                    if (useTransform) {
                        this.transformedPoints[index] = u;
                        this.transformedPoints[index + 1] = v;
                        this.transformedPoints[index + 4] = 0.0f;
                    }
                }
                return;
            default:
                return;
        }
    }

    public void setupInterpolate(ModelContext mdc) throws Exception {
        if (isDirty()) {
            init();
        }
        if (this.pivotManager.checkParamUpdated(mdc)) {
            super.setupInterpolate(mdc);
            if (!this.paramOutside[0]) {
                boolean[] ret_paramOutside = tmp_ba_1;
                ret_paramOutside[0] = false;
                UtInterpolate.interpolatePoints(mdc, this.pivotManager, ret_paramOutside, this.numPts, this.pivotPoints, this.interpolatedPoints, 0, 2);
            }
        }
    }

    public void setupTransform(ModelContext mdc) throws Exception {
        try {
            if (!this.paramOutside[0]) {
                super.setupTransform(mdc);
                if (needTransform()) {
                    BaseDataID targetBaseID = getTargetBaseDataID();
                    if (this.tmpBaseDataIndex == -2) {
                        this.tmpBaseDataIndex = mdc.getBaseIndex(targetBaseID);
                    }
                    if (this.tmpBaseDataIndex < 0) {
                        UtDebug.error("未対応の座標系 :: %s", targetBaseID);
                        return;
                    }
                    IBaseData baseData = mdc.getBaseData(this.tmpBaseDataIndex);
                    if (baseData != null) {
                        baseData.transformPoints(this.interpolatedPoints, this.transformedPoints, this.numPts, 0, 2);
                    }
                }
            }
        } catch (Live2DException e) {
            throw e;
        } catch (Exception e2) {
            throw new Live2DException(e2, getDrawDataID().toString(), "DDTexture/catch@setupTransform");
        }
    }

    public void draw(DrawParam dp, ModelContext mdc) {
        if (!this.paramOutside[0]) {
            int tmpTextureNo = this.textureNo;
            if (tmpTextureNo < 0) {
                tmpTextureNo = 1;
            }
            dp.drawTexture(tmpTextureNo, this.numPolygons * 3, this.indexArray, this.transformedPoints != null ? this.transformedPoints : this.interpolatedPoints, this.uvmap, getOpacity());
        }
    }

    public void dump() {
        System.out.printf("  texNo( %d ) , numPts( %d ) , numPolygons( %d ) \n", new Object[]{Integer.valueOf(this.textureNo), Integer.valueOf(this.numPts), Integer.valueOf(this.numPolygons)});
        System.out.printf("  index array = { ", new Object[0]);
        for (int i = 0; i < this.indexArray.length; i++) {
            System.out.printf("%5d ,", new Object[]{Short.valueOf(this.indexArray[i])});
        }
        System.out.printf("\n  pivot points", new Object[0]);
        for (int i2 = 0; i2 < this.pivotPoints.size(); i2++) {
            System.out.printf("\n    points[%d] = ", new Object[]{Integer.valueOf(i2)});
            float[] ff = this.pivotPoints.get(i2);
            for (int j = 0; j < ff.length; j++) {
                System.out.printf("%6.2f, ", new Object[]{Float.valueOf(ff[j])});
            }
        }
        System.out.printf("\n", new Object[0]);
    }

    public Object getOptionData(String key) {
        if (this.optionData == null) {
            return null;
        }
        return this.optionData.get(key);
    }
}
