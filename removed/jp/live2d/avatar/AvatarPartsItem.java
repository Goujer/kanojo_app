package jp.live2d.avatar;

import java.util.ArrayList;
import jp.live2d.base.IBaseData;
import jp.live2d.draw.IDrawData;
import jp.live2d.id.PartsDataID;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;
import jp.live2d.model.PartsData;

public class AvatarPartsItem implements ISerializableV2 {
    static int INSTANCE_COUNT = 0;
    ArrayList<IBaseData> baseDataList = null;
    ArrayList<IDrawData> drawDataList = null;
    PartsDataID partsID = null;

    public AvatarPartsItem() {
        INSTANCE_COUNT++;
    }

    public ArrayList<IBaseData> getBaseDataList() {
        return this.baseDataList;
    }

    public ArrayList<IDrawData> getDrawDataList() {
        return this.drawDataList;
    }

    public void readV2(BReader br) throws Exception {
        this.partsID = (PartsDataID) br.readObject();
        this.drawDataList = (ArrayList) br.readObject();
        this.baseDataList = (ArrayList) br.readObject();
    }

    public void replacePartsData(PartsData parts) {
        parts.setBaseData(this.baseDataList);
        parts.setDrawData(this.drawDataList);
        this.baseDataList = null;
        this.drawDataList = null;
    }
}
