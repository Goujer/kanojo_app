package jp.co.cybird.barcodekanojoForGAM.core;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Category;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;

public class BarcodeKanojo {
    private static final String TAG = "BarcodeKanojo";
    private BarcodeKanojoHttpApi mBCKApi;
    private ModelList<Category> mCategories;
    private PlayLive2d mPlayLive2d;
    private User mUser;

    public BarcodeKanojo(BarcodeKanojoHttpApi barcodeKanojoHttpApi) {
        this.mBCKApi = barcodeKanojoHttpApi;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public User getUser() {
        return this.mUser;
    }

    public void resetUser() {
        this.mUser = new User();
    }

    public Response<BarcodeKanojoModel> iphone_signup(String name, String password, String email, int birth_month, int birth_day, String sex, String description, File profile_image_data, String udid) throws BarcodeKanojoException, IOException {
        Response<BarcodeKanojoModel> response = this.mBCKApi.iphone_signup(name, password, email, birth_month, birth_day, sex, description, profile_image_data, udid);
        User user = (User) response.get(User.class);
        if (user != null) {
            setUser(user);
        }
        return response;
    }

    public Response<BarcodeKanojoModel> android_signup(int birth_year, int birth_month, int birth_day, String sex, String udid) throws BarcodeKanojoException, IOException {
        Response<BarcodeKanojoModel> response = this.mBCKApi.android_signup(birth_year, birth_month, birth_day, sex, udid);
        User user = (User) response.get(User.class);
        if (user != null) {
            setUser(user);
        }
        return response;
    }

    public Response<BarcodeKanojoModel> update(String name, File profile_image_data) throws BarcodeKanojoException, IOException {
        return this.mBCKApi.update(name, profile_image_data);
    }

    public Response<BarcodeKanojoModel> update(String name, String name_textid, File profile_image_data) throws BarcodeKanojoException, IOException {
        return this.mBCKApi.update(name, name_textid, profile_image_data);
    }

//    public Response<BarcodeKanojoModel> iphone_update(String name, String current_password, String new_password, String email, int birth_month, int birth_day, String sex, String description, File profile_image_data) throws BarcodeKanojoException, IOException {
//        Response<BarcodeKanojoModel> response = this.mBCKApi.iphone_update(name, current_password, new_password, email, birth_month, birth_day, sex, description, profile_image_data);
//        User user = (User) response.get(User.class);
//        if (user != null) {
//            setUser(user);
//        }
//        return response;
//    }

//    public Response<BarcodeKanojoModel> verify(int gree_id, String verify_key) throws BarcodeKanojoException {
//        try {
//            Response<BarcodeKanojoModel> response = this.mBCKApi.verify(gree_id, verify_key);
//            setUser((User) response.get(User.class));
//            if (this.mUser == null) {
//            }
//            return response;
//        } catch (IOException e) {
//            throw new BarcodeKanojoException(e.toString());
//        }
//    }

    public Response<BarcodeKanojoModel> iphone_verify(String email, String password, String udid) throws BarcodeKanojoException {
        try {
            Response<BarcodeKanojoModel> response = this.mBCKApi.iphone_verify(email, password, udid);
            User user = (User) response.get(User.class);
            if (user == null) {
                throw new BarcodeKanojoException("user not found");
            }
            setUser(user);
            return response;
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_verify(String udid) throws BarcodeKanojoException {
        try {
            Response<BarcodeKanojoModel> response = this.mBCKApi.android_verify(udid);
            User user = (User) response.get(User.class);
            if (user == null) {
                throw new BarcodeKanojoException("user not found");
            }
            setUser(user);
            return response;
        } catch (IOException e) {
        	e.printStackTrace();
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_register_fb(String facebookid, String facebookToken) throws BarcodeKanojoException {
        try {
            Response<BarcodeKanojoModel> response = this.mBCKApi.android_register_fb(facebookid, facebookToken);
            if (response.get(User.class) != null) {
                return response;
            }
            throw new BarcodeKanojoException("user not found");
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_disconnect_fb() throws BarcodeKanojoException {
        try {
            Response<BarcodeKanojoModel> response = this.mBCKApi.android_disconnect_fb();
            if (response.get(User.class) != null) {
                return response;
            }
            throw new BarcodeKanojoException("user not found");
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_register_twitter(String access_token, String access_secret) throws BarcodeKanojoException {
        try {
            Response<BarcodeKanojoModel> response = this.mBCKApi.android_register_twitter(access_token, access_secret);
            if (response.get(User.class) != null) {
                return response;
            }
            throw new BarcodeKanojoException("user not found");
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_disconnect_twitter() throws BarcodeKanojoException {
        try {
            Response<BarcodeKanojoModel> response = this.mBCKApi.android_disconnect_twitter();
            if (response.get(User.class) != null) {
                return response;
            }
            throw new BarcodeKanojoException("user not found");
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> my_current_kanojos(int index, int limit, String search) throws IllegalStateException, BarcodeKanojoException, IOException {
        return current_kanojos(this.mUser.getId(), index, limit, search);
    }

    private Response<BarcodeKanojoModel> current_kanojos(int user_id, int index, int limit, String search) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.current_kanojos(user_id, index, limit, search);
    }

    public Response<BarcodeKanojoModel> my_friend_kanojos(int index, int limit, String search) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.friend_kanojos(this.mUser.getId(), index, limit, search);
    }

//    public Response<BarcodeKanojoModel> friend_kanojos(int user_id, int index, int limit, String search) throws IllegalStateException, BarcodeKanojoException, IOException {
//        return this.mBCKApi.friend_kanojos(user_id, index, limit, search);
//    }

    public Response<BarcodeKanojoModel> account_show() throws IllegalStateException, BarcodeKanojoException {
        try {
            Response<BarcodeKanojoModel> response = this.mBCKApi.account_show();
            User user = (User) response.get(User.class);
            if (user != null) {
                setUser(user);
            }
            return response;
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> like_ranking(int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.like_ranking(index, limit);
    }

    public Response<BarcodeKanojoModel> show(int kanojo_id, boolean screen) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.show(kanojo_id, screen);
    }

    public Response<BarcodeKanojoModel> vote_like() throws IllegalStateException, BarcodeKanojoException, IOException {
        Kanojo kanojo;
        if (this.mPlayLive2d == null || (kanojo = this.mPlayLive2d.kanojo) == null) {
            return null;
        }
        return this.mBCKApi.vote_like(kanojo.getId(), kanojo.isVoted_like());
    }

    public Response<BarcodeKanojoModel> query(String barcode, LatLng geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.query(barcode, geo);
    }

    public Response<BarcodeKanojoModel> scan(String barcode, String company_name, String product_name, int product_category_id, String product_comment, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.scan(barcode, company_name, product_name, product_category_id, product_comment, product_image_data, product_geo);
    }

    public Response<BarcodeKanojoModel> scan(String barcode, String company_name, String company_name_textid, String product_name, String product_name_textid, int product_category_id, String product_comment, String product_comment_textid, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.scan(barcode, company_name, company_name_textid, product_name, product_name_textid, product_category_id, product_comment, product_comment_textid, product_image_data, product_geo);
    }

//    public Response<BarcodeKanojoModel> scan_and_generate(String barcode, String company_name, String kanojo_name, File kanojo_profile_image_data, String product_name, int product_category_id, String product_comment, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
//        return this.mBCKApi.scan_and_generate(barcode, company_name, kanojo_name, kanojo_profile_image_data, product_name, product_category_id, product_comment, product_image_data, product_geo);
//    }

    public Response<BarcodeKanojoModel> scan_and_generate(String barcode, String company_name, String company_name_textid, String kanojo_name, String kanojo_name_textid, File kanojo_profile_image_data, String product_name, String product_name_textid, int product_category_id, String product_comment, String product_comment_textid, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.scan_and_generate(barcode, company_name, company_name_textid, kanojo_name, kanojo_name_textid, kanojo_profile_image_data, product_name, product_name_textid, product_category_id, product_comment, product_comment_textid, product_image_data, product_geo);
    }

    public Response<BarcodeKanojoModel> decrease_generating(String barcode) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.decrease_generating(barcode);
    }

    public Response<BarcodeKanojoModel> update(String barcode, String company_name, String product_name, int product_category_id, String product_comment, File product_image_data, LatLng product_geo) throws BarcodeKanojoException, IOException {
        return this.mBCKApi.update(barcode, company_name, product_name, product_category_id, product_comment, product_image_data, product_geo);
    }

    public Response<BarcodeKanojoModel> update(String barcode, String company_name, String company_name_textid, String product_name, String product_name_textid, int product_category_id, String product_comment, String product_comment_textid, File product_image_data, LatLng product_geo) throws BarcodeKanojoException, IOException {
        return this.mBCKApi.update(barcode, company_name, company_name_textid, product_name, product_name_textid, product_category_id, product_comment, product_comment_textid, product_image_data, product_geo);
    }

    public Response<BarcodeKanojoModel> my_user_timeline(int since_id, int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.user_timeline(this.mUser.getId(), since_id, index, limit);
    }

//    public Response<BarcodeKanojoModel> user_timeline(int user_id, int since_id, int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
//        return this.mBCKApi.user_timeline(user_id, since_id, index, limit);
//    }

    public Response<BarcodeKanojoModel> scanned_timeline(String barcode, int since_id, int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.scanned_timeline(barcode, since_id, index, limit);
    }

    public Response<BarcodeKanojoModel> date_menu(int kanojo_id, int type_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.date_menu(kanojo_id, type_id);
    }

    public Response<BarcodeKanojoModel> gift_menu(int kanojo_id, int type_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.gift_menu(kanojo_id, type_id);
    }

    public Response<BarcodeKanojoModel> permanent_item_gift_menu(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.permanent_item_gift_menu(item_class, item_category_id);
    }

    public Response<BarcodeKanojoModel> permanent_sub_item_gift_menu(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.permanent_sub_item_gift_menu(item_class, item_category_id);
    }

//    public Response<BarcodeKanojoModel> date_and_gift_menu(int kanojo_id) throws IllegalStateException, BarcodeKanojoException, IOException {
//        return this.mBCKApi.date_and_gift_menu(kanojo_id);
//    }

    public Response<BarcodeKanojoModel> has_items(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.has_items(item_class, item_category_id);
    }

    public Response<BarcodeKanojoModel> store_items(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.store_items(item_class, item_category_id);
    }

    public Response<BarcodeKanojoModel> do_date(int kanojo_id, int basic_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.do_date(kanojo_id, basic_item_id);
    }

    public Response<BarcodeKanojoModel> do_extend_date(int kanojo_id, int extend_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.do_extend_date(kanojo_id, extend_item_id);
    }

    public Response<BarcodeKanojoModel> do_gift(int kanojo_id, int basic_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.do_gift(kanojo_id, basic_item_id);
    }

    public Response<BarcodeKanojoModel> do_extend_gift(int kanojo_id, int extend_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.do_extend_gift(kanojo_id, extend_item_id);
    }

    public Response<BarcodeKanojoModel> play_on_live2d() throws IllegalStateException, BarcodeKanojoException, IOException {
        if (this.mPlayLive2d == null) {
            return null;
        }
        Kanojo kanojo = this.mPlayLive2d.kanojo;
        if (kanojo.getRelation_status() != 2 && kanojo.getRelation_status() != 3) {
            return null;
        }
        String actions = this.mPlayLive2d.actions;
        if (kanojo == null || actions == null) {
            return null;
        }
        return this.mBCKApi.play_on_live2d(kanojo.getId(), actions);
    }

    public void setPlayLive2d(Kanojo kanojo, int[] actions) {
        initPlayLive2d();
        this.mPlayLive2d.kanojo = kanojo;
        String s = "";
        for (int i = 0; i < actions.length; i++) {
            s = s + actions[i] + "|";
        }
        this.mPlayLive2d.actions = s;
    }

    private void initPlayLive2d() {
        if (this.mPlayLive2d != null) {
            this.mPlayLive2d.actions = null;
            this.mPlayLive2d.kanojo = null;
        }
        this.mPlayLive2d = new PlayLive2d();
    }

//    public String item_detail(int store_item_id) {
//        return this.mBCKApi.item_detail(store_item_id);
//    }

//    public Response<BarcodeKanojoModel> payment_verify(String payment_id) throws IllegalStateException, BarcodeKanojoException, IOException {
//        return this.mBCKApi.payment_verify(payment_id);
//    }

    public void init_product_category_list() throws BarcodeKanojoException, IOException {
        Response<BarcodeKanojoModel> product_category_list = this.mBCKApi.product_category_list();
        if (product_category_list == null) {
            throw new BarcodeKanojoException("Error: Category list not found");
        }
        int code = product_category_list.getCode();
        switch (code) {
            case 200:
                this.mCategories = product_category_list.getCategoryModelList();
                return;
            case 400:
            case 401:
            case 403:
            case 404:
            case 500:
            case 503:
                throw new BarcodeKanojoException("Error: Code: " + code + " Category list not initialized!");
            default:

        }
    }

    public ModelList<Category> getCategoryList() throws BarcodeKanojoException {
        if (this.mCategories != null && !this.mCategories.isEmpty()) {
            return this.mCategories;
        }
        throw new BarcodeKanojoException("Categories is empty");
    }

    private static BarcodeKanojoHttpApi createHttpApi(String domain, String clientVersion, String clientLanguage) {
        return new BarcodeKanojoHttpApi(domain, clientVersion, clientLanguage);
    }

    public static BarcodeKanojoHttpApi createHttpApi(String clientVersion, String clientLanguage) {
        return createHttpApi(Defs.URL_BASE(), clientVersion, clientLanguage);
    }

    static class PlayLive2d {
        String actions;
        Kanojo kanojo;

        PlayLive2d() {
        }
    }

    public Response<BarcodeKanojoModel> android_delete_account(int user_id) throws BarcodeKanojoException, IOException {
        return this.mBCKApi.delete(user_id);
    }

    public Response<BarcodeKanojoModel> android_register_device(String uuid, String strDeviceToken) throws BarcodeKanojoException {
        try {
            return this.mBCKApi.android_register_device(uuid, strDeviceToken);
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> getURLWebView(String uuid) {
        try {
            return this.mBCKApi.getURLWebView(uuid);
        } catch (BarcodeKanojoException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public Response<BarcodeKanojoModel> getURLRadarWebView(int kanojo_id) {
        try {
            return this.mBCKApi.getURLRadarWebView(kanojo_id);
        } catch (BarcodeKanojoException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public Response<BarcodeKanojoModel> show_dialog() throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.show_dialog();
    }

    public Response<BarcodeKanojoModel> android_uuid_verify(String email, String password, String uuid) throws IllegalStateException, BarcodeKanojoException, IOException {
        try {
            return this.mBCKApi.android_uuid_verify(email, password, uuid);
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_update(String name, String current_password, String new_password, String email, int birth_month, int birth_day, int birth_year, String sex, String description, File profile_image_data) throws BarcodeKanojoException, IOException {
        Response<BarcodeKanojoModel> response = this.mBCKApi.android_update(name, current_password, new_password, email, birth_month, birth_day, birth_year, sex, description, profile_image_data);
        User user = (User) response.get(User.class);
        if (user != null) {
            setUser(user);
        }
        return response;
    }

    public Response<BarcodeKanojoModel> android_get_transaction_id(int store_item_id) throws BarcodeKanojoException {
        try {
            return this.mBCKApi.android_get_transaction_id(store_item_id);
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_verify_purchased(int store_item_id, int google_transaction_id, String receipt_data) throws BarcodeKanojoException {
        try {
            return this.mBCKApi.android_verify_purchased(store_item_id, google_transaction_id, receipt_data);
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> android_check_ticket(int price, int store_item_id) throws BarcodeKanojoException {
        try {
            return this.mBCKApi.android_check_ticket(price, store_item_id);
        } catch (IOException e) {
            throw new BarcodeKanojoException(e.toString());
        }
    }

    public Response<BarcodeKanojoModel> do_ticket(int store_item_id, int use_tickets) throws IllegalStateException, BarcodeKanojoException, IOException {
        return this.mBCKApi.do_ticket(store_item_id, use_tickets);
    }
}
