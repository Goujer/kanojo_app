package jp.live2d.model;

import java.util.ArrayList;
import jp.live2d.base.IBaseData;
import jp.live2d.draw.IDrawData;
import jp.live2d.id.PartsDataID;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;

public class PartsData implements ISerializableV2 {
    static int INSTANCE_COUNT = 0;
    ArrayList<IBaseData> baseDataList = null;
    ArrayList<IDrawData> drawDataList = null;
    boolean locked = false;
    PartsDataID partsID = null;
    boolean visible = true;

    public PartsData() {
        INSTANCE_COUNT++;
    }

    /* access modifiers changed from: package-private */
    public void initDirect() {
        this.baseDataList = new ArrayList<>();
        this.drawDataList = new ArrayList<>();
    }

    public void readV2(BReader br) throws Exception {
        this.locked = br.readBit();
        this.visible = br.readBit();
        this.partsID = (PartsDataID) br.readObject();
        this.baseDataList = (ArrayList) br.readObject();
        this.drawDataList = (ArrayList) br.readObject();
    }

    public void addBaseData(IBaseData baseData) {
        if (this.baseDataList == null) {
            throw new RuntimeException("baseDataList not initialized@addBaseData");
        }
        this.baseDataList.add(baseData);
    }

    public void addDrawData(IDrawData drawData) {
        if (this.drawDataList == null) {
            throw new RuntimeException("drawDataList not initialized@addDrawData");
        }
        this.drawDataList.add(drawData);
    }

    public void setBaseData(ArrayList<IBaseData> _baseDataList) {
        this.baseDataList = _baseDataList;
    }

    public void setDrawData(ArrayList<IDrawData> _drawDataList) {
        this.drawDataList = _drawDataList;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setVisible(boolean v) {
        this.visible = v;
    }

    public void setLocked(boolean v) {
        this.locked = v;
    }

    public ArrayList<IBaseData> getBaseData() {
        return this.baseDataList;
    }

    public ArrayList<IDrawData> getDrawData() {
        return this.drawDataList;
    }

    public PartsDataID getPartsID() {
        return this.partsID;
    }

    public void setPartsID(PartsDataID id) {
        this.partsID = id;
    }
}
