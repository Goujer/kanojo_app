package jp.live2d.param;

import jp.live2d.id.ParamID;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;

public class ParamPivots implements ISerializableV2 {
    static final int PARAM_INDEX_NOT_INIT = -2;
    int _paramIndex = -2;
    int indexInitVersion = -1;
    ParamID paramID = null;
    int pivotCount = 0;
    float[] pivotValue = null;
    int tmpPivotIndex = 0;
    float tmpT = 0.0f;

    public void readV2(BReader br) throws Exception {
        this.paramID = (ParamID) br.readObject();
        this.pivotCount = br.readInt();
        this.pivotValue = (float[]) br.readObject();
    }

    public int getParamIndex(int initVersion) {
        if (this.indexInitVersion != initVersion) {
            this._paramIndex = -2;
        }
        return this._paramIndex;
    }

    public void setParamIndex_(int index, int initVersion) {
        this._paramIndex = index;
        this.indexInitVersion = initVersion;
    }

    public ParamID getParamID() {
        return this.paramID;
    }

    public void setParamID(ParamID v) {
        this.paramID = v;
    }

    public int getPivotCount() {
        return this.pivotCount;
    }

    public float[] getPivotValue() {
        return this.pivotValue;
    }

    public void setPivotValue(int _pivotCount, float[] _values) {
        this.pivotCount = _pivotCount;
        this.pivotValue = _values;
    }

    public int getTmpPivotIndex() {
        return this.tmpPivotIndex;
    }

    public void setTmpPivotIndex(int v) {
        this.tmpPivotIndex = v;
    }

    public float getTmpT() {
        return this.tmpT;
    }

    public void setTmpT(float t) {
        this.tmpT = t;
    }
}
