package jp.co.cybird.barcodekanojoForGAM.core.model;

import com.goujer.barcodekanojo.core.model.Kanojo;
import com.goujer.barcodekanojo.core.model.User;

public class ActivityModel implements BarcodeKanojoModel {
	private static final int ACTIVITY_SCAN = 1;
	private static final int ACTIVITY_GENERATED = 2;
	private static final int ACTIVITY_ME_ADD_FRIEND = 5;
	private static final int ACTIVITY_APPROACH_KANOJO = 7;
	private static final int ACTIVITY_ME_STOLE_KANOJO = 8;
	private static final int ACTIVITY_MY_KANOJO_STOLEN = 9;
	private static final int ACTIVITY_MY_KANOJO_ADDED_TO_FRIENDS = 10;
	private static final int ACTIVITY_BECOME_NEW_LEVEL = 11;
	private static final int ACTIVITY_MARRIED = 15;
	private static final int ACTIVITY_JOINED = 101;
	private static final int ACTIVITY_BREAKUP = 102;
	private static final int ACTIVITY_ADD_AS_ENEMY = 103;


	public static final String TAG = "ActivityModel";
    private String activity;
    private int activity_type;
    private int created_timestamp;
    private int id;
    private Kanojo kanojo = null;
    private User other_user = null;
    private Product product = null;
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
            case ACTIVITY_GENERATED:
                return this.kanojo.getProfile_image_icon_url();
            case ACTIVITY_ME_ADD_FRIEND:
                return this.user.getProfile_image_url();
            case ACTIVITY_APPROACH_KANOJO:
                return this.other_user.getProfile_image_url();
            case ACTIVITY_ME_STOLE_KANOJO:
                return this.user.getProfile_image_url();
            case ACTIVITY_MY_KANOJO_STOLEN:
                return this.kanojo.getProfile_image_icon_url();
            case ACTIVITY_MY_KANOJO_ADDED_TO_FRIENDS:
                return this.other_user.getProfile_image_url();
            case ACTIVITY_BECOME_NEW_LEVEL:
                return this.user.getProfile_image_url();
            default:
                return null;
        }
    }

    public String getRightImgUrl() {
    	try {
			switch (getActivity_type()) {
				case ACTIVITY_GENERATED:
					return this.product.getProduct_image_url();
				case ACTIVITY_ME_ADD_FRIEND:
				case ACTIVITY_APPROACH_KANOJO:
				case ACTIVITY_ME_STOLE_KANOJO:
					return this.kanojo.getProfile_image_icon_url();
				case ACTIVITY_MY_KANOJO_STOLEN:
					return this.other_user.getProfile_image_url();
				case ACTIVITY_MY_KANOJO_ADDED_TO_FRIENDS:
					return this.kanojo.getProfile_image_icon_url();
				default:
					return null;
			}
		} catch (NullPointerException e) {
    		e.printStackTrace();
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

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    User getOther_user() {
        return this.other_user;
    }

    public void setOther_user(User otherUser) {
        this.other_user = otherUser;
    }

    int getCreated_timestamp() {
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
