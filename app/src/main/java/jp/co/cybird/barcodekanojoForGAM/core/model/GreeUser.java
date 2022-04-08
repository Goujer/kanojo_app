package jp.co.cybird.barcodekanojoForGAM.core.model;

import jp.co.cybird.barcodekanojoForGAM.core.util.Base64Util;

public class GreeUser {
    private String NickName = "";
    private String encriptedId = "";
    private int id = 0;

    public GreeUser(int greeid) {
        this.id = greeid;
        this.encriptedId = Base64Util.encryptUserId(greeid);
    }

    public int getId() {
        return this.id;
    }

    public String getEncriptedId() {
        return this.encriptedId;
    }

    public String getNickName() {
        return this.NickName;
    }

    public void setNickName(String nickName) {
        this.NickName = nickName;
    }
}
