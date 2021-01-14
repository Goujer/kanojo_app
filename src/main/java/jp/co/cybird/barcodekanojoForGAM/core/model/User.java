package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import jp.co.cybird.barcodekanojoForGAM.Defs;

public class User implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in, (User) null);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public static final String TAG = "User";
    private int birth_day;
    private int birth_month;
    private int birth_year;
    private String currentPassword;
    private String email;
    private int enemy_count;
    private final String[] genderList;
    private int generate_count;
    private int gree_id;
    private int id;
    private int kanojo_count;
    private String language;
    private int level;
    private int money;
    private String name;
    private String password;
    private String profile_image_url;
    private int relation_status;
    private final String[] requestList;
    private int scan_count;
    private String sex;
    private int stamina;
    private int stamina_max;
    private int tickets;
    private int wish_count;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.gree_id);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.name);
        dest.writeString(this.profile_image_url);
        dest.writeString(this.sex);
        dest.writeString(this.language);
        dest.writeInt(this.level);
        dest.writeInt(this.stamina);
        dest.writeInt(this.stamina_max);
        dest.writeInt(this.money);
        dest.writeInt(this.kanojo_count);
        dest.writeInt(this.generate_count);
        dest.writeInt(this.scan_count);
        dest.writeInt(this.enemy_count);
        dest.writeInt(this.wish_count);
        dest.writeInt(this.birth_month);
        dest.writeInt(this.birth_day);
        dest.writeInt(this.birth_year);
        dest.writeInt(this.relation_status);
        dest.writeInt(this.tickets);
        dest.writeString(this.currentPassword);
    }

    private User(Parcel in) {
        this.genderList = new String[]{"男性", "女性", "わからない"};
        this.requestList = new String[]{"male", "female", "not sure"};
        this.id = in.readInt();
        this.gree_id = in.readInt();
        this.email = in.readString();
        this.password = in.readString();
        this.name = in.readString();
        this.profile_image_url = in.readString();
        this.sex = in.readString();
        this.language = in.readString();
        this.level = in.readInt();
        this.stamina = in.readInt();
        this.stamina_max = in.readInt();
        this.money = in.readInt();
        this.kanojo_count = in.readInt();
        this.generate_count = in.readInt();
        this.scan_count = in.readInt();
        this.enemy_count = in.readInt();
        this.wish_count = in.readInt();
        boolean[] val = new boolean[2];
        in.readBooleanArray(val);
        this.birth_month = in.readInt();
        this.birth_day = in.readInt();
        this.birth_year = in.readInt();
        this.relation_status = in.readInt();
        this.tickets = in.readInt();
        this.currentPassword = in.readString();
    }

    /* synthetic */ User(Parcel parcel, User user) {
        this(parcel);
    }

    public User() {
        this.genderList = new String[]{"男性", "女性", "わからない"};
        this.requestList = new String[]{"male", "female", "not sure"};
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGree_id() {
        return this.gree_id;
    }

    public void setGree_id(int greeId) {
        this.gree_id = greeId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getProfile_image_url() {
        return this.profile_image_url;
    }

    public void setProfile_image_url(String profileImageUrl) {
        this.profile_image_url = profileImageUrl;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex2) {
        this.sex = sex2;
    }

    public void setSexFromText(String sexText) {
        int selected = 0;
        if (this.genderList != null) {
            int size = this.genderList.length;
            for (int i = 0; i < size; i++) {
                if (sexText.equals(this.genderList[i])) {
                    selected = i;
                }
            }
        }
        if (selected < this.requestList.length) {
            setSex(this.requestList[selected]);
        } else {
            setSex("");
        }
    }

    //public String getSexText() {
    //    int selected = 0;
    //    if (this.requestList != null) {
    //        int size = this.requestList.length;
    //        for (int i = 0; i < size; i++) {
    //            if (getSex().equalsIgnoreCase(this.requestList[i])) {
    //                selected = i;
    //            }
    //        }
    //    }
    //    if (selected < this.genderList.length) {
    //        return this.genderList[selected];
    //    }
    //    return "";
    //}

    public void setSexFromText(String sexText, String[] genderList2) {
        int selected = 2;
        if (genderList2 != null) {
            int size = genderList2.length;
            for (int i = 0; i < size; i++) {
                if (sexText.equals(genderList2[i])) {
                    selected = i;
                }
            }
        }
        if (selected < this.requestList.length) {
            setSex(this.requestList[selected]);
        } else {
            setSex("");
        }
    }

    public String getSexText(String[] genderList2) {
        int selected = 2;
        if (this.requestList != null) {
            int size = this.requestList.length;
            for (int i = 0; i < size; i++) {
                if (getSex().equalsIgnoreCase(this.requestList[i])) {
                    selected = i;
                }
            }
        }
        if (selected >= genderList2.length || selected < 0) {
            return "";
        }
        return genderList2[selected];
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language2) {
        this.language = language2;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level2) {
        this.level = level2;
    }

    public int getStamina() {
        return this.stamina;
    }

    public void setStamina(int stamina2) {
        this.stamina = stamina2;
    }

    public int getStamina_max() {
        return this.stamina_max;
    }

    public void setStamina_max(int staminaMax) {
        this.stamina_max = staminaMax;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int money2) {
        this.money = money2;
    }

    public int getKanojo_count() {
        return this.kanojo_count;
    }

    public void setKanojo_count(int kanojoCount) {
        this.kanojo_count = kanojoCount;
    }

    public int getGenerate_count() {
        return this.generate_count;
    }

    public void setGenerate_count(int generateCount) {
        this.generate_count = generateCount;
    }

    public int getScan_count() {
        return this.scan_count;
    }

    public void setScan_count(int scanCount) {
        this.scan_count = scanCount;
    }

    public int getEnemy_count() {
        return this.enemy_count;
    }

    public void setEnemy_count(int enemyCount) {
        this.enemy_count = enemyCount;
    }

    public int getWish_count() {
        return this.wish_count;
    }

    public void setWish_count(int wishCount) {
        this.wish_count = wishCount;
    }

    public String getBirthText() {
        if (this.birth_month == 0 || this.birth_day == 0 || this.birth_year == 0) {
            return "";
        }
        return this.birth_month + "." + this.birth_day + "." + this.birth_year;
    }

    public void setBirthFromText(String MandD) {
        if (!MandD.equals("")) {
            String[] array = MandD.split("\\.");
            if (array.length == 3) {
                this.birth_month = Integer.parseInt(array[0]);
                this.birth_day = Integer.parseInt(array[1]);
                this.birth_year = Integer.parseInt(array[2]);
            }
        }
    }

    public void setBirth(int month, int day, int year) {
    	birth_month = month;
    	birth_day = day;
    	birth_year = year;
	}

    public int getBirth_month() {
        return this.birth_month;
    }

    public void setBirth_month(int birthMonth) {
        this.birth_month = birthMonth;
    }

    public int getBirth_day() {
        return this.birth_day;
    }

    public void setBirth_day(int birthDay) {
        this.birth_day = birthDay;
    }

    public int getRelation_status() {
        return this.relation_status;
    }

    public void setRelation_status(int relationStatus) {
        this.relation_status = relationStatus;
    }

    public int getTickets() {
        return this.tickets;
    }

    public void setTickets(int tickets2) {
        this.tickets = tickets2;
    }

    public int getBirth_year() {
        return this.birth_year;
    }

    public void setBirth_year(int birth_year2) {
        this.birth_year = birth_year2;
    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public void setCurrentPassword(String currentPassword2) {
        this.currentPassword = currentPassword2;
    }
}
