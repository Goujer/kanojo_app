package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Barcode implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<Barcode> CREATOR = new Parcelable.Creator<Barcode>() {
        public Barcode createFromParcel(Parcel in) {
            return new Barcode(in, (Barcode) null);
        }

        public Barcode[] newArray(int size) {
            return new Barcode[size];
        }
    };
    public static final String TAG = "Barcode";
    private int accessory_type;
    private String barcode;
    private int body_type;
    private float brow_position;
    private int brow_type;
    private int clothes_type;
    private int consumption;
    private int ear_type;
    private int eye_color;
    private float eye_position;
    private int eye_type;
    private int face_type;
    private int fringe_type;
    private int glasses_type;
    private int hair_color;
    private int hair_type;
    private float mouth_position;
    private int mouth_type;
    private int nose_type;
    private int possession;
    private int race_type;
    private int recognition;
    private int sexual;
    private int skin_color;
    private int spot_type;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.barcode);
        dest.writeInt(this.race_type);
        dest.writeInt(this.eye_type);
        dest.writeInt(this.nose_type);
        dest.writeInt(this.mouth_type);
        dest.writeInt(this.face_type);
        dest.writeInt(this.brow_type);
        dest.writeInt(this.fringe_type);
        dest.writeInt(this.hair_type);
        dest.writeInt(this.accessory_type);
        dest.writeInt(this.spot_type);
        dest.writeInt(this.glasses_type);
        dest.writeInt(this.body_type);
        dest.writeInt(this.clothes_type);
        dest.writeInt(this.ear_type);
        dest.writeFloat(this.eye_position);
        dest.writeFloat(this.brow_position);
        dest.writeFloat(this.mouth_position);
        dest.writeInt(this.skin_color);
        dest.writeInt(this.hair_color);
        dest.writeInt(this.eye_color);
        dest.writeInt(this.possession);
        dest.writeInt(this.consumption);
        dest.writeInt(this.recognition);
        dest.writeInt(this.sexual);
    }

    private Barcode(Parcel in) {
        this.barcode = in.readString();
        this.race_type = in.readInt();
        this.eye_type = in.readInt();
        this.nose_type = in.readInt();
        this.mouth_type = in.readInt();
        this.face_type = in.readInt();
        this.brow_type = in.readInt();
        this.fringe_type = in.readInt();
        this.hair_type = in.readInt();
        this.accessory_type = in.readInt();
        this.spot_type = in.readInt();
        this.glasses_type = in.readInt();
        this.body_type = in.readInt();
        this.clothes_type = in.readInt();
        this.ear_type = in.readInt();
        this.eye_position = in.readFloat();
        this.brow_position = in.readFloat();
        this.mouth_position = in.readFloat();
        this.skin_color = in.readInt();
        this.hair_color = in.readInt();
        this.eye_color = in.readInt();
        this.possession = in.readInt();
        this.consumption = in.readInt();
        this.recognition = in.readInt();
        this.sexual = in.readInt();
    }

    /* synthetic */ Barcode(Parcel parcel, Barcode barcode2) {
        this(parcel);
    }

    public Barcode() {
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode2) {
        this.barcode = barcode2;
    }

    public int getRace_type() {
        return this.race_type;
    }

    public void setRace_type(int raceType) {
        this.race_type = raceType;
    }

    public int getEye_type() {
        return this.eye_type;
    }

    public void setEye_type(int eyeType) {
        this.eye_type = eyeType;
    }

    public int getNose_type() {
        return this.nose_type;
    }

    public void setNose_type(int noseType) {
        this.nose_type = noseType;
    }

    public int getMouth_type() {
        return this.mouth_type;
    }

    public void setMouth_type(int mouthType) {
        this.mouth_type = mouthType;
    }

    public int getFace_type() {
        return this.face_type;
    }

    public void setFace_type(int faceType) {
        this.face_type = faceType;
    }

    public int getBrow_type() {
        return this.brow_type;
    }

    public void setBrow_type(int browType) {
        this.brow_type = browType;
    }

    public int getFringe_type() {
        return this.fringe_type;
    }

    public void setFringe_type(int fringeType) {
        this.fringe_type = fringeType;
    }

    public int getHair_type() {
        return this.hair_type;
    }

    public void setHair_type(int hairType) {
        this.hair_type = hairType;
    }

    public int getAccessory_type() {
        return this.accessory_type;
    }

    public void setAccessory_type(int accessoryType) {
        this.accessory_type = accessoryType;
    }

    public int getSpot_type() {
        return this.spot_type;
    }

    public void setSpot_type(int spotType) {
        this.spot_type = spotType;
    }

    public int getGlasses_type() {
        return this.glasses_type;
    }

    public void setGlasses_type(int glassesType) {
        this.glasses_type = glassesType;
    }

    public int getBody_type() {
        return this.body_type;
    }

    public void setBody_type(int bodyType) {
        this.body_type = bodyType;
    }

    public int getClothes_type() {
        return this.clothes_type;
    }

    public void setClothes_type(int clothesType) {
        this.clothes_type = clothesType;
    }

    public int getEar_type() {
        return this.ear_type;
    }

    public void setEar_type(int earType) {
        this.ear_type = earType;
    }

    public float getEye_position() {
        return this.eye_position;
    }

    public void setEye_position(float eyePosition) {
        this.eye_position = eyePosition;
    }

    public float getBrow_position() {
        return this.brow_position;
    }

    public void setBrow_position(float browPosition) {
        this.brow_position = browPosition;
    }

    public float getMouth_position() {
        return this.mouth_position;
    }

    public void setMouth_position(float mouthPosition) {
        this.mouth_position = mouthPosition;
    }

    public int getSkin_color() {
        return this.skin_color;
    }

    public void setSkin_color(int skinColor) {
        this.skin_color = skinColor;
    }

    public int getHair_color() {
        return this.hair_color;
    }

    public void setHair_color(int hairColor) {
        this.hair_color = hairColor;
    }

    public int getEye_color() {
        return this.eye_color;
    }

    public void setEye_color(int eyeColor) {
        this.eye_color = eyeColor;
    }

    public int getPossession() {
        return this.possession;
    }

    public void setPossession(int possession2) {
        this.possession = possession2;
    }

    public int getConsumption() {
        return this.consumption;
    }

    public void setConsumption(int consumption2) {
        this.consumption = consumption2;
    }

    public int getRecognition() {
        return this.recognition;
    }

    public void setRecognition(int recognition2) {
        this.recognition = recognition2;
    }

    public int getSexual() {
        return this.sexual;
    }

    public void setSexual(int sexual2) {
        this.sexual = sexual2;
    }
}
