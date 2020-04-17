package jp.co.cybird.barcodekanojoForGAM.core.model;

public class ActivityModel implements BarcodeKanojoModel {
    public static final String TAG = "ActivityModel";
    private String activity;
    private int activity_type;
    private int created_timestamp;
    private int id;
    private Kanojo kanojo = null;
    private User other_user = null;
    private Scanned scanned = null;
    private User user = null;

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public int getActivity_type() {
        return this.activity_type;
    }

    public void setActivity_type(int activityType) {
        this.activity_type = activityType;
    }

    public String getLeftImgUrl() {
        switch (getActivity_type()) {
            case 2:
                return this.kanojo.getProfile_image_url();
            case 5:
                return this.user.getProfile_image_url();
            case 7:
                return this.other_user.getProfile_image_url();
            case 8:
                return this.user.getProfile_image_url();
            case 9:
                return this.kanojo.getProfile_image_url();
            case 10:
                return this.other_user.getProfile_image_url();
            case 11:
                return this.user.getProfile_image_url();
            default:
                return null;
        }
    }

    public String getRightImgUrl() {
        switch (getActivity_type()) {
            case 2:
                return this.scanned.getProduct_image_url();
            case 5:
                return this.kanojo.getProfile_image_url();
            case 7:
                return this.kanojo.getProfile_image_url();
            case 8:
                return this.kanojo.getProfile_image_url();
            case 9:
                return this.other_user.getProfile_image_url();
            case 10:
                return this.kanojo.getProfile_image_url();
            default:
                return null;
        }
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user2) {
        this.user = user2;
    }

    public Kanojo getKanojo() {
        return this.kanojo;
    }

    public void setKanojo(Kanojo kanojo2) {
        this.kanojo = kanojo2;
    }

    public Scanned getScanned() {
        return this.scanned;
    }

    public void setScanned(Scanned scanned2) {
        this.scanned = scanned2;
    }

    public User getOther_user() {
        return this.other_user;
    }

    public void setOther_user(User otherUser) {
        this.other_user = otherUser;
    }

    public int getCreated_timestamp() {
        return this.created_timestamp;
    }

    public void setCreated_timestamp(int createdTimestamp) {
        this.created_timestamp = createdTimestamp;
    }

    public String getActivity() {
        return this.activity;
    }

    public void setActivity(String activity2) {
        this.activity = activity2;
    }
}
