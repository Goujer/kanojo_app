package jp.live2d.id;

import java.util.HashMap;

public class PartsDataID extends ID {
    static HashMap<String, PartsDataID> id_hashmap = new HashMap<>();

    private PartsDataID() {
    }

    private PartsDataID(String str) {
        this.id = str;
    }

    public PartsDataID createIDForSerialize() {
        return new PartsDataID();
    }

    static void releaseStored_exe_notForClientCall() {
        id_hashmap.clear();
    }

    public static PartsDataID getID(String tmp_idstr) {
        PartsDataID ret = id_hashmap.get(tmp_idstr);
        if (ret != null) {
            return ret;
        }
        PartsDataID ret2 = new PartsDataID(tmp_idstr);
        id_hashmap.put(tmp_idstr, ret2);
        return ret2;
    }
}
