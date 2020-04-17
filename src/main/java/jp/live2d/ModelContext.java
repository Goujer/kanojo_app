package jp.live2d;

import java.util.ArrayList;
import jp.live2d.base.IBaseData;
import jp.live2d.draw.IDrawData;
import jp.live2d.graphics.DrawParam;
import jp.live2d.id.BaseDataID;
import jp.live2d.id.ParamID;
import jp.live2d.model.ModelImpl;
import jp.live2d.model.PartsData;
import jp.live2d.param.ParamDefFloat;
import jp.live2d.param.ParamDefSet;

public class ModelContext {
    private static final int DEFAULT_PARAM_COUNT = 32;
    static final boolean DUMP = false;
    static final short NOT_USED_ORDER = -1;
    static final short NO_NEXT = -1;
    static final boolean PARAM_NOT_UPDATED = false;
    static final boolean PARAM_UPDATED = true;
    static boolean VERBOSE = false;
    static final float _PARAM_FLOAT_MAX_ = 1000000.0f;
    static final float _PARAM_FLOAT_MIN_ = -1000000.0f;
    ArrayList<IBaseData> baseDataList = new ArrayList<>();
    ArrayList<IDrawData> drawDataList = new ArrayList<>();
    ParamID[] floatParamIDList = new ParamID[32];
    float[] floatParamList = new float[32];
    float[] floatParamMaxList = new float[32];
    float[] floatParamMinList = new float[32];
    int initVersion = -1;
    ALive2DModel model;
    boolean needSetup = true;
    short[] nextList_drawIndex;
    int nextParamNo = 0;
    short[] orderList_firstDrawIndex;
    short[] orderList_lastDrawIndex;
    float[] savedFloatParamList = new float[32];
    byte[] tmpPivotTableIndices = new byte[65];
    float[] tmpT_array = new float[10];
    boolean[] updatedFloatParamFlags = new boolean[32];

    public ModelContext(ALive2DModel model2) {
        this.model = model2;
    }

    public void init() {
        boolean updated;
        ArrayList<ParamDefFloat> pdefs;
        BaseDataID target;
        this.initVersion++;
        this.baseDataList.clear();
        this.drawDataList.clear();
        ModelImpl modelImpl = this.model.getModelImpl();
        ArrayList<PartsData> parts = modelImpl.getPartsDataList();
        int partsCount = parts.size();
        ArrayList<IBaseData> tmpBaseData = new ArrayList<>();
        for (int i = 0; i < partsCount; i++) {
            PartsData pd = parts.get(i);
            ArrayList<IBaseData> baseList = pd.getBaseData();
            int baseCount = baseList.size();
            for (int j = 0; j < baseCount; j++) {
                tmpBaseData.add(baseList.get(j));
            }
            if (pd.isVisible()) {
                ArrayList<IDrawData> drawList = pd.getDrawData();
                int drawCount = drawList.size();
                for (int j2 = 0; j2 < drawCount; j2++) {
                    this.drawDataList.add(drawList.get(j2));
                }
            }
        }
        int tmpBaseCount = tmpBaseData.size();
        BaseDataID DST_ID = BaseDataID.DST_BASE_ID();
        do {
            updated = false;
            for (int i2 = 0; i2 < tmpBaseCount; i2++) {
                IBaseData bd = tmpBaseData.get(i2);
                if (bd != null && ((target = bd.getTargetBaseDataID()) == null || target == DST_ID || getBaseIndex(target) >= 0)) {
                    this.baseDataList.add(bd);
                    tmpBaseData.set(i2, (Object) null);
                    updated = true;
                }
            }
        } while (updated);
        ParamDefSet paramDef = modelImpl.getParamDefSet();
        if (!(paramDef == null || (pdefs = paramDef.getParamDefFloatList()) == null)) {
            int len = pdefs.size();
            for (int i3 = 0; i3 < len; i3++) {
                ParamDefFloat def = pdefs.get(i3);
                if (def != null) {
                    addFloatParam(def.getParamID(), def.getDefaultValue(), def.getMinValue(), def.getMaxValue());
                }
            }
        }
        this.needSetup = true;
    }

    public boolean update() throws Exception {
        int baseCount = this.baseDataList.size();
        int drawCount = this.drawDataList.size();
        int minOrder = IDrawData.getTotalMinOrder();
        int orderCount = (IDrawData.getTotalMaxOrder() - minOrder) + 1;
        if (this.orderList_firstDrawIndex == null || this.orderList_firstDrawIndex.length < orderCount) {
            this.orderList_firstDrawIndex = new short[orderCount];
            this.orderList_lastDrawIndex = new short[orderCount];
        }
        for (int i = 0; i < orderCount; i++) {
            this.orderList_firstDrawIndex[i] = -1;
            this.orderList_lastDrawIndex[i] = -1;
        }
        if (this.nextList_drawIndex == null || this.nextList_drawIndex.length < drawCount) {
            this.nextList_drawIndex = new short[drawCount];
        }
        for (int i2 = 0; i2 < drawCount; i2++) {
            this.nextList_drawIndex[i2] = -1;
        }
        Exception firstExceptionBase = null;
        for (int j = 0; j < baseCount; j++) {
            IBaseData base = this.baseDataList.get(j);
            try {
                base.setupInterpolate(this);
                base.setupTransform(this);
            } catch (Exception e) {
                if (firstExceptionBase == null) {
                    firstExceptionBase = e;
                }
            }
        }
        if (firstExceptionBase != null && VERBOSE) {
            System.err.printf("%s / firstExceptionBase\t\t\t\t\t@@ModelContext\n", new Object[]{firstExceptionBase.toString()});
        }
        Exception firstExceptionDraw = null;
        for (int drawIndex = 0; drawIndex < drawCount; drawIndex++) {
            IDrawData draw = this.drawDataList.get(drawIndex);
            try {
                draw.setupInterpolate(this);
                draw.setupTransform(this);
                int orderIndex = draw.getDrawOrder() - minOrder;
                short order_drawIndex = this.orderList_lastDrawIndex[orderIndex];
                if (order_drawIndex == -1) {
                    this.orderList_firstDrawIndex[orderIndex] = (short) drawIndex;
                } else {
                    this.nextList_drawIndex[order_drawIndex] = (short) drawIndex;
                }
                this.orderList_lastDrawIndex[orderIndex] = (short) drawIndex;
            } catch (Exception e2) {
                if (firstExceptionDraw == null) {
                    firstExceptionDraw = e2;
                }
            }
        }
        if (firstExceptionDraw != null && VERBOSE) {
            System.err.printf("%s / firstExceptionDraw\t\t\t\t\t@@ModelContext\n", new Object[]{firstExceptionDraw.toString()});
        }
        for (int i3 = this.updatedFloatParamFlags.length - 1; i3 >= 0; i3--) {
            this.updatedFloatParamFlags[i3] = false;
        }
        this.needSetup = false;
        return false;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r0v0, types: [short] */
    public void draw(DrawParam dp) {
        for (int drawIndex : this.orderList_firstDrawIndex) {
            if (drawIndex != -1) {
                while (true) {
                    this.drawDataList.get(drawIndex).draw(dp, this);
                    short nextDrawIndex = this.nextList_drawIndex[drawIndex];
                    if (nextDrawIndex <= drawIndex || nextDrawIndex == -1) {
                        break;
                    }
                    drawIndex = nextDrawIndex;
                }
            }
        }
    }

    public int getParamIndex(ParamID paramID) {
        for (int i = this.floatParamIDList.length - 1; i >= 0; i--) {
            if (this.floatParamIDList[i] == paramID) {
                return i;
            }
        }
        return addFloatParam(paramID, 0.0f, _PARAM_FLOAT_MIN_, _PARAM_FLOAT_MAX_);
    }

    public int getBaseIndex(BaseDataID baseID) {
        for (int i = this.baseDataList.size() - 1; i >= 0; i--) {
            if (this.baseDataList.get(i) != null && this.baseDataList.get(i).getBaseDataID() == baseID) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public float[] copyArray(float[] src, int newSize) {
        float[] ret = new float[newSize];
        System.arraycopy(src, 0, ret, 0, src.length);
        return ret;
    }

    public int addFloatParam(ParamID id, float value, float min, float max) {
        if (this.nextParamNo >= this.floatParamIDList.length) {
            int size = this.floatParamIDList.length;
            ParamID[] tmp = new ParamID[(size * 2)];
            System.arraycopy(this.floatParamIDList, 0, tmp, 0, size);
            this.floatParamIDList = tmp;
            this.floatParamList = copyArray(this.floatParamList, size * 2);
            this.floatParamMinList = copyArray(this.floatParamMinList, size * 2);
            this.floatParamMaxList = copyArray(this.floatParamMaxList, size * 2);
            boolean[] tmpb = new boolean[(size * 2)];
            System.arraycopy(this.updatedFloatParamFlags, 0, tmpb, 0, size);
            this.updatedFloatParamFlags = tmpb;
        }
        this.floatParamIDList[this.nextParamNo] = id;
        this.floatParamList[this.nextParamNo] = value;
        this.floatParamMinList[this.nextParamNo] = min;
        this.floatParamMaxList[this.nextParamNo] = max;
        this.updatedFloatParamFlags[this.nextParamNo] = true;
        int i = this.nextParamNo;
        this.nextParamNo = i + 1;
        return i;
    }

    public void setBaseData(int baseDataIndex, IBaseData baseData) {
        this.baseDataList.set(baseDataIndex, baseData);
    }

    public void setParamFloat(int paramIndex, float value) {
        if (value < this.floatParamMinList[paramIndex]) {
            value = this.floatParamMinList[paramIndex];
        }
        if (value > this.floatParamMaxList[paramIndex]) {
            value = this.floatParamMaxList[paramIndex];
        }
        if (this.floatParamList[paramIndex] != value) {
            this.updatedFloatParamFlags[paramIndex] = true;
            this.floatParamList[paramIndex] = value;
        }
    }

    public void loadParam() {
        int len = this.floatParamList.length;
        if (len > this.savedFloatParamList.length) {
            len = this.savedFloatParamList.length;
        }
        System.arraycopy(this.savedFloatParamList, 0, this.floatParamList, 0, len);
    }

    public void saveParam() {
        int len = this.floatParamList.length;
        if (len > this.savedFloatParamList.length) {
            this.savedFloatParamList = new float[len];
        }
        System.arraycopy(this.floatParamList, 0, this.savedFloatParamList, 0, len);
    }

    public int getInitVersion() {
        return this.initVersion;
    }

    public boolean requireSetup() {
        return this.needSetup;
    }

    public boolean isParamUpdated(int paramIndex) {
        return this.updatedFloatParamFlags[paramIndex];
    }

    public byte[] getTmpPivotTableIndicesRef() {
        return this.tmpPivotTableIndices;
    }

    public float[] getTmpT_ArrayRef() {
        return this.tmpT_array;
    }

    public IBaseData getBaseData(int baseDataIndex) {
        return this.baseDataList.get(baseDataIndex);
    }

    public float getParamFloat(int paramIndex) {
        return this.floatParamList[paramIndex];
    }
}
