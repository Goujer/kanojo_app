package jp.live2d.id;

import java.util.HashMap;

public class DrawDataID extends ID {
    static HashMap<String, DrawDataID> id_hashmap = new HashMap<>();

    private DrawDataID() {
    }

    private DrawDataID(String idstr) {
        this.id = idstr;
    }

    public DrawDataID createIDForSerialize() {
        return new DrawDataID();
    }

    static void releaseStored_exe_notForClientCall() {
        id_hashmap.clear();
    }

    public static DrawDataID getID(String tmp_idstr) {
        DrawDataID ret = id_hashmap.get(tmp_idstr);
        if (ret != null) {
            return ret;
        }
        DrawDataID ret2 = new DrawDataID(tmp_idstr);
        id_hashmap.put(tmp_idstr, ret2);
        return ret2;
    }
}
