package jp.live2d.param;

import java.util.ArrayList;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;

public class ParamDefSet implements ISerializableV2 {
    ArrayList<ParamDefFloat> paramDefList = null;

    public ArrayList<ParamDefFloat> getParamDefFloatList() {
        return this.paramDefList;
    }

    public void initDirect() {
        this.paramDefList = new ArrayList<>();
    }

    public void readV2(BReader br) throws Exception {
        this.paramDefList = (ArrayList) br.readObject();
    }
}
