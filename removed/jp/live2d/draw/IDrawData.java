package jp.live2d.draw;

import jp.live2d.Live2D;
import jp.live2d.ModelContext;
import jp.live2d.graphics.DrawParam;
import jp.live2d.id.BaseDataID;
import jp.live2d.id.DrawDataID;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;
import jp.live2d.param.PivotManager;
import jp.live2d.util.UtInterpolate;

public abstract class IDrawData implements ISerializableV2 {
    public static final int BASE_INDEX_NOT_INIT = -2;
    public static final int DEFAULT_ORDER = 500;
    public static final int TYPE_DD_PATH = 3;
    public static final int TYPE_DD_TEXTURE = 2;
    static int totalMaxOrder = 500;
    static int totalMinOrder = 500;
    protected int averageDrawOrder;
    boolean dirty = true;
    DrawDataID drawDataID;
    protected int interpolatedDrawOrder;
    protected float interpolatedOpacity;
    protected boolean[] paramOutside = new boolean[1];
    int[] pivotDrawOrder;
    protected PivotManager pivotManager = null;
    float[] pivotOpacity;
    BaseDataID targetBaseDataID;

    public abstract void draw(DrawParam drawParam, ModelContext modelContext);

    public abstract int getType();

    /* access modifiers changed from: protected */
    public boolean isDirty() {
        return this.dirty;
    }

    /* access modifiers changed from: protected */
    public void setDirty(boolean f) {
        this.dirty = f;
    }

    public void readV2(BReader br) throws Exception {
        this.drawDataID = (DrawDataID) br.readObject();
        this.targetBaseDataID = (BaseDataID) br.readObject();
        this.pivotManager = (PivotManager) br.readObject();
        this.averageDrawOrder = br.readInt();
        this.pivotDrawOrder = br.readArrayInt();
        this.pivotOpacity = br.readArrayFloat();
        setDrawOrder(this.pivotDrawOrder);
    }

    /* access modifiers changed from: package-private */
    public void init() {
    }

    public void setupInterpolate(ModelContext mdc) throws Exception {
        this.paramOutside[0] = false;
        this.interpolatedDrawOrder = UtInterpolate.interpolateInt(mdc, this.pivotManager, this.paramOutside, this.pivotDrawOrder);
        if (Live2D.L2D_OUTSIDE_PARAM_AVAILABLE || !this.paramOutside[0]) {
            this.interpolatedOpacity = UtInterpolate.interpolateFloat(mdc, this.pivotManager, this.paramOutside, this.pivotOpacity);
        }
    }

    public void setupTransform(ModelContext mdc) throws Exception {
    }

    public DrawDataID getDrawDataID() {
        return this.drawDataID;
    }

    public void setDrawDataID(DrawDataID id) {
        this.drawDataID = id;
    }

    public float getOpacity() {
        return this.interpolatedOpacity;
    }

    public int getDrawOrder() {
        return this.interpolatedDrawOrder;
    }

    public void setDrawOrder(int[] orders) {
        for (int i = orders.length - 1; i >= 0; i--) {
            int order = orders[i];
            if (order < totalMinOrder) {
                totalMinOrder = order;
            } else if (order > totalMaxOrder) {
                totalMaxOrder = order;
            }
        }
    }

    public BaseDataID getTargetBaseDataID() {
        return this.targetBaseDataID;
    }

    public void setTargetBaseDataID(BaseDataID id) {
        this.targetBaseDataID = id;
    }

    public boolean needTransform() {
        return (this.targetBaseDataID == null || this.targetBaseDataID == BaseDataID.DST_BASE_ID()) ? false : true;
    }

    public static int getTotalMinOrder() {
        return totalMinOrder;
    }

    public static int getTotalMaxOrder() {
        return totalMaxOrder;
    }
}
