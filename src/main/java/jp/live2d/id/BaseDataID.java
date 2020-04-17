package jp.live2d.id;

import java.util.HashMap;

public class BaseDataID extends ID {
    static BaseDataID dstBaseId = null;
    static HashMap<String, BaseDataID> id_hashmap = new HashMap<>();

    private BaseDataID() {
    }

    private BaseDataID(String idstr) {
        this.id = idstr;
    }

    public static BaseDataID DST_BASE_ID() {
        if (dstBaseId == null) {
            dstBaseId = getID("DST_BASE");
        }
        return dstBaseId;
    }

    public BaseDataID createIDForSerialize() {
        return new BaseDataID();
    }

    static void releaseStored_exe_notForClientCall() {
        id_hashmap.clear();
        dstBaseId = null;
    }

    public static BaseDataID getID(String tmp_idstr) {
        BaseDataID ret = id_hashmap.get(tmp_idstr);
        if (ret != null) {
            return ret;
        }
        BaseDataID ret2 = new BaseDataID(tmp_idstr);
        id_hashmap.put(tmp_idstr, ret2);
        return ret2;
    }
}
