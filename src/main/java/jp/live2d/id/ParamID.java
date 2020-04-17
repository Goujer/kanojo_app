package jp.live2d.id;

import java.util.HashMap;

public class ParamID extends ID {
    static HashMap<String, ParamID> id_hashmap = new HashMap<>();

    private ParamID() {
    }

    private ParamID(String id_str) {
        this.id = id_str;
    }

    public ParamID createIDForSerialize() {
        return new ParamID();
    }

    static void releaseStored_exe_notForClientCall() {
        id_hashmap.clear();
    }

    public static ParamID getID(String tmp_idstr) {
        ParamID ret = id_hashmap.get(tmp_idstr);
        if (ret != null) {
            return ret;
        }
        ParamID ret2 = new ParamID(tmp_idstr);
        id_hashmap.put(tmp_idstr, ret2);
        return ret2;
    }
}
