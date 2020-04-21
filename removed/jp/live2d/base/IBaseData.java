package jp.live2d.base;

import jp.live2d.ModelContext;
import jp.live2d.id.BaseDataID;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;

public abstract class IBaseData implements ISerializableV2 {
    public static final int BASE_INDEX_NOT_INIT = -2;
    protected BaseDataID baseDataID = null;
    protected boolean dirty = true;
    protected BaseDataID targetBaseDataID = null;

    public abstract void init();

    public abstract void setupInterpolate(ModelContext modelContext) throws Exception;

    public abstract void setupTransform(ModelContext modelContext) throws Exception;

    public abstract void transformPoints(float[] fArr, float[] fArr2, int i, int i2, int i3) throws Exception;

    public void readV2(BReader br) throws Exception {
        this.baseDataID = (BaseDataID) br.readObject();
        this.targetBaseDataID = (BaseDataID) br.readObject();
    }

    public void setTargetBaseDataID(BaseDataID id) {
        this.targetBaseDataID = id;
    }

    public void setBaseDataID(BaseDataID id) {
        this.baseDataID = id;
    }

    public BaseDataID getTargetBaseDataID() {
        return this.targetBaseDataID;
    }

    public BaseDataID getBaseDataID() {
        return this.baseDataID;
    }

    public boolean needTransform() {
        return (this.targetBaseDataID == null || this.targetBaseDataID == BaseDataID.DST_BASE_ID()) ? false : true;
    }

    /* access modifiers changed from: protected */
    public boolean isDirty() {
        return this.dirty;
    }

    /* access modifiers changed from: protected */
    public void setDirty(boolean f) {
        this.dirty = f;
    }
}
