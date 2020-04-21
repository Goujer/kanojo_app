package jp.live2d.model;

import java.util.ArrayList;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;
import jp.live2d.param.ParamDefSet;

public class ModelImpl implements ISerializableV2 {
    static int INSTANCE_COUNT = 0;
    int canvasHeight = 400;
    int canvasWidth = 400;
    ParamDefSet paramDefSet = null;
    ArrayList<PartsData> partsDataList = null;

    public ModelImpl() {
        INSTANCE_COUNT++;
    }

    public void initDirect() {
        if (this.paramDefSet == null) {
            this.paramDefSet = new ParamDefSet();
        }
        if (this.partsDataList == null) {
            this.partsDataList = new ArrayList<>();
        }
    }

    public float getCanvasWidth() {
        return (float) this.canvasWidth;
    }

    public float getCanvasHeight() {
        return (float) this.canvasHeight;
    }

    public void readV2(BReader br) throws Exception {
        this.paramDefSet = (ParamDefSet) br.readObject();
        this.partsDataList = (ArrayList) br.readObject();
        this.canvasWidth = br.readInt();
        this.canvasHeight = br.readInt();
    }

    public void addPartsData(PartsData parts) {
        this.partsDataList.add(parts);
    }

    public ArrayList<PartsData> getPartsDataList() {
        return this.partsDataList;
    }

    public ParamDefSet getParamDefSet() {
        return this.paramDefSet;
    }
}
