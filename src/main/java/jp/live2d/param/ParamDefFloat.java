package jp.live2d.param;

import jp.live2d.id.ParamID;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;

public class ParamDefFloat implements ISerializableV2 {
    float defaultValue;
    float maxValue;
    float minValue;
    ParamID paramID;

    public void readV2(BReader br) throws Exception {
        this.minValue = br.readFloat();
        this.maxValue = br.readFloat();
        this.defaultValue = br.readFloat();
        this.paramID = (ParamID) br.readObject();
    }

    public float getMinValue() {
        return this.minValue;
    }

    public float getMaxValue() {
        return this.maxValue;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public ParamID getParamID() {
        return this.paramID;
    }
}
