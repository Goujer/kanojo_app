package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;

public class Kanojo implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<Kanojo> CREATOR = new Parcelable.Creator<Kanojo>() {
        public Kanojo createFromParcel(Parcel in) {
            return new Kanojo(in, (Kanojo) null);
        }

        public Kanojo[] newArray(int size) {
            return new Kanojo[size];
        }
    };
    public static final int RELATION_FRIEND = 3;
    public static final int RELATION_KANOJO = 2;
    public static final int RELATION_OTHER = 1;
    public static final String TAG = "Kanojo";
    private int accessory_type;
    private String advertising_banner_url;
    private String avatar_background_image_url;
    private String barcode;
    private int birth_day;
    private int birth_month;
    private int birth_year;
    private int body_type;
    private float brow_position;
    private int brow_type;
    private int clothes_type;
    private int consumption;
    private int ear_type;
    private int emotion_status;
    private int eye_color;
    private float eye_position;
    private int eye_type;
    private int face_type;
    private int follower_count;
    private int fringe_type;
    private Location geo;
    private int glasses_type;
    private int hair_color;
    private int hair_type;
    private int id;
    private boolean in_room;
    private int like_rate;
    private String location;
    private int love_gauge;
    private int mascot_enabled;
    private float mouth_position;
    private int mouth_type;
    private String name;
    private String nationality;
    private int nose_type;
    private boolean on_advertising;
    private int possession;
    private int race_type;
    private int recognition;
    private int relation_status;
    private int sexual;
    private int skin_color;
    private String source;
    private int spot_type;
    private String status;
    private boolean voted_like;

    public Kanojo() {
        this.geo = GeoUtil.doublesToGeo(0, 0);
    }

    public Kanojo(Barcode in) {
        this();
        this.barcode = in.getBarcode();
        this.race_type = in.getRace_type();
        this.eye_type = in.getEye_type();
        this.nose_type = in.getNose_type();
        this.mouth_type = in.getMouth_type();
        this.face_type = in.getFace_type();
        this.brow_type = in.getBrow_type();
        this.fringe_type = in.getFringe_type();
        this.hair_type = in.getHair_type();
        this.accessory_type = in.getAccessory_type();
        this.spot_type = in.getSpot_type();
        this.glasses_type = in.getGlasses_type();
        this.body_type = in.getBody_type();
        this.clothes_type = in.getClothes_type();
        this.ear_type = in.getEar_type();
        this.eye_position = in.getEye_position();
        this.brow_position = in.getBrow_position();
        this.mouth_position = in.getMouth_position();
        this.skin_color = in.getSkin_color();
        this.hair_color = in.getHair_color();
        this.eye_color = in.getEye_color();
        this.possession = in.getPossession();
        this.consumption = in.getConsumption();
        this.recognition = in.getRecognition();
        this.sexual = in.getSexual();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.barcode);
        dest.writeDouble(GeoUtil.getLatitudeE6(this.geo));
        dest.writeDouble(GeoUtil.getLongitudeE6(this.geo));
        dest.writeString(this.location);
        dest.writeInt(this.birth_year);
        dest.writeInt(this.birth_month);
        dest.writeInt(this.birth_day);
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
        dest.writeInt(this.love_gauge);
        dest.writeInt(this.follower_count);
        dest.writeString(this.source);
        dest.writeString(this.nationality);
        dest.writeInt(this.relation_status);
        dest.writeBooleanArray(new boolean[]{this.voted_like, this.in_room, this.on_advertising});
        dest.writeInt(this.like_rate);
        dest.writeString(this.status);
        dest.writeString(this.avatar_background_image_url);
        dest.writeString(this.advertising_banner_url);
        dest.writeInt(this.emotion_status);
        dest.writeInt(this.mascot_enabled);
    }

    private Kanojo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.barcode = in.readString();
        this.geo = GeoUtil.doublesToGeo(in.readDouble(), in.readDouble());
        this.location = in.readString();
        this.birth_year = in.readInt();
        this.birth_month = in.readInt();
        this.birth_day = in.readInt();
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
        this.love_gauge = in.readInt();
        this.follower_count = in.readInt();
        this.source = in.readString();
        this.nationality = in.readString();
        this.relation_status = in.readInt();
        boolean[] b = new boolean[3];
        in.readBooleanArray(b);
        this.voted_like = b[0];
        this.in_room = b[1];
        this.on_advertising = b[2];
        this.like_rate = in.readInt();
        this.status = in.readString();
        this.avatar_background_image_url = in.readString();
        this.advertising_banner_url = in.readString();
        this.emotion_status = in.readInt();
        this.mascot_enabled = in.readInt();
    }

	private Kanojo(Parcel parcel, Kanojo kanojo) {
        this(parcel);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode2) {
        this.barcode = barcode2;
    }

    public Location getGeo() {
        return this.geo;
    }

    public void setGeo(Location geo2) {
        this.geo = geo2;
    }

    public void setGeo(String geo2) {
        setGeo(GeoUtil.stringToGeo(geo2));
    }

    public String getGeoString() {
        return GeoUtil.geoToString(this.geo);
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public int getBirth_year() {
        return this.birth_year;
    }

    public void setBirth_year(int birthYear) {
        this.birth_year = birthYear;
    }

    int getBirth_month() {
        return this.birth_month;
    }

    public void setBirth_month(int birthMonth) {
        this.birth_month = birthMonth;
    }

    int getBirth_day() {
        return this.birth_day;
    }

    public void setBirth_day(int birthDay) {
        this.birth_day = birthDay;
    }

    public String getProfile_image_icon_url() {
		return "/profile_images/kanojo/" + id + "/icon.png";
	}

	public String getProfile_image_iconv1_url() {
		return "/profile_images/kanojo/" + id + "/iconv1.png";
	}

	public String getProfile_image_url() {
		return "/profile_images/kanojo/" + id + "/full.png";
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

    public int getLove_gauge() {
        return this.love_gauge;
    }

    public void setLove_gauge(int loveGauge) {
        this.love_gauge = loveGauge;
    }

    int getFollower_count() {
        return this.follower_count;
    }

    public void setFollower_count(int followerCount) {
        this.follower_count = followerCount;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality2) {
        this.nationality = nationality2;
    }

    public int getRelation_status() {
        return this.relation_status;
    }

    public void setRelation_status(int relationStatus) {
        this.relation_status = relationStatus;
    }

    public boolean isVoted_like() {
        return this.voted_like;
    }

    public void setVoted_like(boolean votedLike) {
        this.voted_like = votedLike;
    }

    public boolean isIn_room() {
        return this.in_room;
    }

    public void setIn_room(boolean inRoom) {
        this.in_room = inRoom;
    }

    public int getLike_rate() {
        return this.like_rate;
    }

    public void setLike_rate(int likeRate) {
        this.like_rate = likeRate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getAvatar_background_image_url() {
        return this.avatar_background_image_url;
    }

    public void setAvatar_background_image_url(String avatarBackgroundImageUrl) {
        this.avatar_background_image_url = avatarBackgroundImageUrl;
    }

    public boolean isOn_advertising() {
        return this.on_advertising;
    }

    public void setOn_advertising(boolean onAdvertising) {
        this.on_advertising = onAdvertising;
    }

    public String getAdvertising_banner_url() {
        return this.advertising_banner_url;
    }

    public void setAdvertising_banner_url(String advertisingBannerUrl) {
        this.advertising_banner_url = advertisingBannerUrl;
    }

    public int getEmotion_status() {
        return this.emotion_status;
    }

    public void setEmotion_status(int emotionStatus) {
        this.emotion_status = emotionStatus;
    }

    public int getMascotEnable() {
        return this.mascot_enabled;
    }

    public void setMascotEnable(int mascotEnable) {
        this.mascot_enabled = mascotEnable;
    }
}
