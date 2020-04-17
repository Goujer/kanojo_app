package jp.co.cybird.barcodekanojoForGAM.core.model;

import java.io.File;
import java.util.HashMap;

public class FaceBookProfile extends HashMap<String, String> implements BarcodeKanojoModel {
    public static final String TAG = "FaceBookError";
    private static final long serialVersionUID = 4819493080923777193L;
    private String birthday;
    private String code = "";
    private String gender = "";
    private String id = "";
    private String message = null;
    private String name = "";
    private File photo = null;
    private String subCode = "";
    private String type = "";
    private String url = "";

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getSubCode() {
        return this.subCode;
    }

    public void setSubCode(String subCode2) {
        this.subCode = subCode2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender2) {
        this.gender = gender2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday2) {
        this.birthday = birthday2;
    }

    public File getPhoto() {
        return this.photo;
    }

    public void setPhoto(File photo2) {
        this.photo = photo2;
    }
}
