package jp.co.cybird.barcodekanojoForGAM.core;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import jp.co.cybird.app.android.lib.commons.security.popgate.Codec;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.http.HttpApi;
import jp.co.cybird.barcodekanojoForGAM.core.http.NameValueOrFilePair;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.parser.ActivityParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.AlertParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.BarcodeParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.CategoryParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.JSONParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.KanojoItemCategoryParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.KanojoMessageParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.KanojoParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.LoveIncrementParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.MessageParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.ModelListParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.ModelParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.ProductParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.PurchaseItemParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.ResponseParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.ScanHistoryParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.ScannedParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.SearchResultParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.UserParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.WebViewParser;
import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import twitter4j.conf.PropertyConfiguration;

public class BarcodeKanojoHttpApi {
    private static final boolean DEBUG = false;
    private static final String TAG = "BarcodeKanojoHttpApi";
    private static final String URL_API_ACCOUNT_DELETE = "/api/account/delete.json";
    private static final String URL_API_ACCOUNT_SHOW = "/api/account/show.json";
    private static final String URL_API_ACCOUNT_SIGNUP = "/api/account/signup.json";
    private static final String URL_API_ACCOUNT_UPDATE = "/api/account/update.json";
    private static final String URL_API_ACCOUNT_VERIFY = "/api/account/verify.json";
    private static final String URL_API_ACTIVITY_SCANNED_TIMELINE = "/api/activity/scanned_timeline.json";
    private static final String URL_API_ACTIVITY_USER_TIMELINE = "/activity/user_timeline.json";
    private static final String URL_API_BARCODE_DECREASE_GENERATING = "/api/barcode/decrease_generating.json";
    private static final String URL_API_BARCODE_QUERY = "/api/barcode/query.json";
    private static final String URL_API_BARCODE_SCAN = "/api/barcode/scan.json";
    private static final String URL_API_BARCODE_SCAN_AND_GENERATE = "/api/barcode/scan_and_generate.json";
    private static final String URL_API_BARCODE_UPDATE = "/api/barcode/update.json";
    private static final String URL_API_COMMUNICATION_CONFIRM_PURCHASE_ITEM = "/api/google/confirm_purchase_item.json";
    private static final String URL_API_COMMUNICATION_DATE_AND_GIFT_MENU = "/api/communication/date_and_gift_menu.json";
    private static final String URL_API_COMMUNICATION_DATE_MENU = "/api/communication/date_list.json";
    private static final String URL_API_COMMUNICATION_DO_DATE = "/api/communication/do_date.json";
    private static final String URL_API_COMMUNICATION_DO_EXTEND_DATE = "/api/communication/do_extend_date.json";
    private static final String URL_API_COMMUNICATION_DO_EXTEND_GIFT = "/api/communication/do_extend_gift.json";
    private static final String URL_API_COMMUNICATION_DO_GIFT = "/api/communication/do_gift.json";
    private static final String URL_API_COMMUNICATION_GIFT_MENU = "/api/communication/item_list.json";
    private static final String URL_API_COMMUNICATION_HAS_ITEMS = "/api/communication/has_items.json";
    private static final String URL_API_COMMUNICATION_PERMANENT_ITEM_GIFT_MENU = "/api/communication/permanent_items.json";
    private static final String URL_API_COMMUNICATION_PERMANENT_SUB_ITEM_GIFT_MENU = "/api/communication/permanent_sub_item.json";
    private static final String URL_API_COMMUNICATION_PLAY_ON_LIVE2D = "/api/communication/play_on_live2d.json";
    private static final String URL_API_COMMUNICATION_STORE_ITEMS = "/api/communication/store_items.json";
    private static final String URL_API_COMMUNICATION_VERIFY_PURCHASED_ITEM = "/api/google/verify_purchased_item.json";
    private static final String URL_API_FACEBOOK_CONNECT = "/api/account/connect_facebook.json";
    private static final String URL_API_FACEBOOK_DISCONNECT = "/api/account/disconnect_facebook.json";
    private static final String URL_API_KANOJO_LIKE_RANKING = "/api/kanojo/like_rankings.json";
    private static final String URL_API_KANOJO_SHOW = "/api/kanojo/show.json";
    private static final String URL_API_KANOJO_VOTE_LIKE = "/api/kanojo/vote_like.json";
    private static final String URL_API_MESSAGE_DIALOG = "/api/message/dialog.json";
    private static final String URL_API_PAYMENT_ITEM_DETAIL = "/api/payment/item_detail.html";
    private static final String URL_API_PAYMENT_VERIFY = "/api/payment/verify.json";
    private static final String URL_API_REGISTER_TOKEN = "/api/notification/register_token.json";
    private static final String URL_API_RESOURCE_PRODUCT_CATEGORY_LIST = "/api/resource/product_category_list.json";
    private static final String URL_API_SHOPPING_COMPARE_PRICE = "/api/shopping/compare_price.json";
    private static final String URL_API_SHOPPING_GOOD_LIST = "/api/shopping/goods_list.json";
    private static final String URL_API_SHOPPING_VERIFY_TICKET = "/api/shopping/verify_tickets.json";
    private static final String URL_API_SUKIYA_CONNECT = "/api/account/connect_sukiya.json";
    private static final String URL_API_SUKIYA_DISCONNECT = "/api/account/disconnect_sukiya.json";
    private static final String URL_API_TWITTER_CONNECT = "/api/account/connect_twitter.json";
    private static final String URL_API_TWITTER_DISCONNECT = "/api/account/disconnect_twitter.json";
    private static final String URL_API_USER_CURRENT_KANOJOS = "/user/current_kanojos.json";
    private static final String URL_API_USER_FRIEND_KANOJOS = "/api/user/friend_kanojos.json";
    private static final String URL_API_VERIFY_UUID = "/api/account/uuidverify.json";
    private static final String URL_RADAR_WEBVIEW = "/api/webview/chart.json";
    private static final String URL_WEBVIEW = "/api/webview/show.json";
    private final String mApiBaseUrl;
    private HttpApi mHttpApi;
    private final DefaultHttpClient mHttpClient = HttpApi.createHttpClient();

    public BarcodeKanojoHttpApi(String url, String clientVersion, String clientLanguage) {
        this.mApiBaseUrl = url;
        this.mHttpApi = new HttpApi(this.mHttpClient, clientVersion, clientLanguage);
    }

    public Response<BarcodeKanojoModel> iphone_signup(String name, String password, String email, int birth_month, int birth_day, String sex, String description, File profile_image_data, String udid) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_ACCOUNT_SIGNUP), new NameValueOrFilePair("name", name), new NameValueOrFilePair((String) "password", password), new NameValueOrFilePair("email", email), new NameValueOrFilePair("birth_month", String.valueOf(birth_month)), new NameValueOrFilePair("birth_day", String.valueOf(birth_day)), new NameValueOrFilePair("sex", sex), new NameValueOrFilePair("description", description), new NameValueOrFilePair("profile_image_data", profile_image_data), new NameValueOrFilePair("uuid", udid));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_signup(int birth_year, int birth_month, int birth_day, String sex, String uuid) throws BarcodeKanojoException, IOException {
        String uuid2 = Codec.encode(uuid);
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_ACCOUNT_SIGNUP), new NameValueOrFilePair("uuid", uuid2));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> verify(int gree_id, String verify_key) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_ACCOUNT_VERIFY), new BasicNameValuePair("gree_id", String.valueOf(gree_id)), new BasicNameValuePair("verify_key", verify_key));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> update(String name, String name_textid, File profile_image_data) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_ACCOUNT_UPDATE), new NameValueOrFilePair("name", name), new NameValueOrFilePair("name_textid", name_textid), new NameValueOrFilePair("profile_image_data", profile_image_data));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> iphone_update(String name, String current_apssword, String new_password, String email, int birth_month, int birth_day, String sex, String description, File profile_image_data) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_ACCOUNT_UPDATE), new NameValueOrFilePair("name", name), new NameValueOrFilePair("current_password", current_apssword), new NameValueOrFilePair("new_password", new_password), new NameValueOrFilePair("email", email), new NameValueOrFilePair("birth_month", String.valueOf(birth_month)), new NameValueOrFilePair("birth_day", String.valueOf(birth_day)), new NameValueOrFilePair("sex", sex), new NameValueOrFilePair("description", description), new NameValueOrFilePair("profile_image_data", profile_image_data));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> update(String name, File profile_image_data) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_ACCOUNT_UPDATE), new NameValueOrFilePair("name", name), new NameValueOrFilePair("profile_image_data", profile_image_data));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> iphone_verify(String email, String password, String udid) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_ACCOUNT_VERIFY), new BasicNameValuePair("email", email), new BasicNameValuePair("password", password), new BasicNameValuePair("udid", udid));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_verify(String udid) throws BarcodeKanojoException, IOException {
        String udid2 = Codec.encode(udid);
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_ACCOUNT_VERIFY), new BasicNameValuePair("uuid", udid2));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> current_kanojos(int user_id, int index, int limit, String search) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpPost = this.mHttpApi.createHttpGet(fullUrl(URL_API_USER_CURRENT_KANOJOS), new BasicNameValuePair("user_id", String.valueOf(user_id)), new BasicNameValuePair("index", String.valueOf(index)), new BasicNameValuePair("limit", String.valueOf(limit)), new BasicNameValuePair("search", search));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new ModelListParser("current_kanojos", new KanojoParser()), new ModelParser("user", new UserParser()), new ModelParser("search_result", new SearchResultParser())));
    }

    public Response<BarcodeKanojoModel> friend_kanojos(int user_id, int index, int limit, String search) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpPost = this.mHttpApi.createHttpGet(fullUrl(URL_API_USER_FRIEND_KANOJOS), new BasicNameValuePair("user_id", String.valueOf(user_id)), new BasicNameValuePair("index", String.valueOf(index)), new BasicNameValuePair("limit", String.valueOf(limit)), new BasicNameValuePair("search", search));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelListParser("friend_kanojos", new KanojoParser()), new ModelParser("user", new UserParser()), new ModelParser("search_result", new SearchResultParser())));
    }

    public Response<BarcodeKanojoModel> like_ranking(int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_KANOJO_LIKE_RANKING), new BasicNameValuePair("index", String.valueOf(index)), new BasicNameValuePair("limit", String.valueOf(limit)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("like_ranking_kanojos", new KanojoParser())));
    }

    public Response<BarcodeKanojoModel> show(int kanojo_id, boolean screen) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpApi httpApi = this.mHttpApi;
        String fullUrl = fullUrl(URL_API_KANOJO_SHOW);
        NameValuePair[] nameValuePairArr = new NameValuePair[2];
        nameValuePairArr[0] = new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id));
        nameValuePairArr[1] = new BasicNameValuePair("screen", screen ? "live2d" : "");
        return (Response) this.mHttpApi.executeHttpRequest(httpApi.createHttpGet(fullUrl, nameValuePairArr), new ResponseParser(new AlertParser(), new MessageParser(MessageModel.NOTIFY_AMENDMENT_INFORMATION), new ModelParser("kanojo", new KanojoParser()), new ModelParser("owner_user", new UserParser()), new ModelParser("product", new ProductParser()), new ModelParser("scanned", new ScannedParser())));
    }

    public Response<BarcodeKanojoModel> vote_like(int kanojo_id, boolean like) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_KANOJO_VOTE_LIKE), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("like", String.valueOf(like)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("kanojo", new KanojoParser())));
    }

    public Response<BarcodeKanojoModel> query(String barcode, LatLng geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_BARCODE_QUERY), new BasicNameValuePair("barcode", barcode), new BasicNameValuePair("geo", GeoUtil.geoToString(geo)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelParser("owner_user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("barcode", new BarcodeParser()), new ModelParser("product", new ProductParser()), new ModelParser("scanned", new ScannedParser()), new ModelParser("scan_history", new ScanHistoryParser()), new MessageParser(MessageModel.NOTIFY_AMENDMENT_INFORMATION, MessageModel.DO_GENERATE_KANOJO, MessageModel.DO_ADD_FRIEND, MessageModel.INFORM_GIRLFRIEND, MessageModel.INFORM_FRIEND)));
    }

    public Response<BarcodeKanojoModel> scan(String barcode, String company_name, String product_name, int product_category_id, String product_comment, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_BARCODE_SCAN), new NameValueOrFilePair("barcode", barcode), new NameValueOrFilePair("company_name", company_name), new NameValueOrFilePair("product_name", product_name), new NameValueOrFilePair("product_category_id", String.valueOf(product_category_id)), new NameValueOrFilePair("product_comment", product_comment), new NameValueOrFilePair("product_image_data", product_image_data), new NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("kanojo", new KanojoParser()), new ModelParser("scan_history", new ScanHistoryParser())));
    }

    public Response<BarcodeKanojoModel> scan(String barcode, String company_name, String comapny_name_textid, String product_name, String product_name_textid, int product_category_id, String product_comment, String product_comment_textid, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_BARCODE_SCAN), new NameValueOrFilePair("barcode", barcode), new NameValueOrFilePair("company_name", company_name), new NameValueOrFilePair("company_name_textid", comapny_name_textid), new NameValueOrFilePair("product_name", product_name), new NameValueOrFilePair("product_name_textid", product_name_textid), new NameValueOrFilePair("product_category_id", String.valueOf(product_category_id)), new NameValueOrFilePair("product_comment", product_comment), new NameValueOrFilePair("product_comment_textid", product_comment_textid), new NameValueOrFilePair("product_image_data", product_image_data), new NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("kanojo", new KanojoParser()), new ModelParser("scan_history", new ScanHistoryParser())));
    }

//    Response<BarcodeKanojoModel> scan_and_generate(String barcode, String company_name, String kanojo_name, File kanojo_profile_image_data, String product_name, int product_category_id, String product_comment, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
//        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_BARCODE_SCAN_AND_GENERATE), new NameValueOrFilePair("barcode", barcode), new NameValueOrFilePair("company_name", company_name), new NameValueOrFilePair("kanojo_name", kanojo_name), new NameValueOrFilePair("kanojo_profile_image_data", kanojo_profile_image_data), new NameValueOrFilePair("product_name", product_name), new NameValueOrFilePair("product_category_id", String.valueOf(product_category_id)), new NameValueOrFilePair("product_comment", product_comment), new NameValueOrFilePair("product_image_data", product_image_data), new NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)));
//        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("scan_history", new ScanHistoryParser())));
//    }

    Response<BarcodeKanojoModel> scan_and_generate(String barcode, String company_name, String company_name_textid, String kanojo_name, String kanojo_name_textid, File kanojo_profile_image_data, String product_name, String product_name_textid, int product_category_id, String product_comment, String product_comment_textid, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_BARCODE_SCAN_AND_GENERATE), new NameValueOrFilePair("barcode", barcode), new NameValueOrFilePair("company_name", company_name), new NameValueOrFilePair("company_name_textid", company_name_textid), new NameValueOrFilePair("kanojo_name", kanojo_name), new NameValueOrFilePair("kanojo_name_textid", kanojo_name_textid), new NameValueOrFilePair("kanojo_profile_image_data", kanojo_profile_image_data), new NameValueOrFilePair("product_name", product_name), new NameValueOrFilePair("product_name_textid", product_name_textid), new NameValueOrFilePair("product_category_id", String.valueOf(product_category_id)), new NameValueOrFilePair("product_comment", product_comment), new NameValueOrFilePair("product_comment_textid", product_comment_textid), new NameValueOrFilePair("product_image_data", product_image_data), new NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("scan_history", new ScanHistoryParser())));
    }

    public Response<BarcodeKanojoModel> decrease_generating(String barcode) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_BARCODE_DECREASE_GENERATING), new BasicNameValuePair("barcode", barcode));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser()), new ModelParser("product", new ProductParser())));
    }

    public Response<BarcodeKanojoModel> update(String barcode, String company_name, String product_name, int product_category_id, String product_comment, File product_image_data, LatLng product_geo) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_BARCODE_UPDATE), new NameValueOrFilePair("barcode", barcode), new NameValueOrFilePair("company_name", company_name), new NameValueOrFilePair("product_name", product_name), new NameValueOrFilePair("product_category_id", String.valueOf(product_category_id)), new NameValueOrFilePair("product_comment", product_comment), new NameValueOrFilePair("product_image_data", product_image_data), new NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser()));
    }

    public Response<BarcodeKanojoModel> update(String barcode, String company_name, String company_name_textid, String product_name, String product_name_textid, int product_category_id, String product_comment, String product_comment_textid, File product_image_data, LatLng product_geo) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_BARCODE_UPDATE), new NameValueOrFilePair("barcode", barcode), new NameValueOrFilePair("company_name", company_name), new NameValueOrFilePair("company_name_textid", company_name_textid), new NameValueOrFilePair("product_name", product_name), new NameValueOrFilePair("product_name_textid", product_name_textid), new NameValueOrFilePair("product_category_id", String.valueOf(product_category_id)), new NameValueOrFilePair("product_comment", product_comment), new NameValueOrFilePair("product_comment_textid", product_comment_textid), new NameValueOrFilePair("product_image_data", product_image_data), new NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser()));
    }

    public Response<BarcodeKanojoModel> gift_menu(int kanojo_id, int type_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_GIFT_MENU), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("type_id", String.valueOf(type_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("item_categories", new KanojoItemCategoryParser(1))));
    }

    public Response<BarcodeKanojoModel> permanent_item_gift_menu(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_PERMANENT_ITEM_GIFT_MENU), new BasicNameValuePair("item_class", String.valueOf(item_class)), new BasicNameValuePair("item_category_id", String.valueOf(item_category_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("item_categories", new KanojoItemCategoryParser(item_class))));
    }

    public Response<BarcodeKanojoModel> permanent_sub_item_gift_menu(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_PERMANENT_SUB_ITEM_GIFT_MENU), new BasicNameValuePair("item_class", String.valueOf(item_class)), new BasicNameValuePair("item_category_id", String.valueOf(item_category_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("item_categories", new KanojoItemCategoryParser(item_class))));
    }

    public Response<BarcodeKanojoModel> date_and_gift_menu(int kanojo_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_DATE_AND_GIFT_MENU), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("item_categories", new KanojoItemCategoryParser(1))));
    }

    public Response<BarcodeKanojoModel> date_menu(int kanojo_id, int type_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_DATE_MENU), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("type_id", String.valueOf(type_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("item_categories", new KanojoItemCategoryParser(2))));
    }

    public Response<BarcodeKanojoModel> has_items(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_HAS_ITEMS), new BasicNameValuePair("item_class", String.valueOf(item_class)), new BasicNameValuePair("item_category_id", String.valueOf(item_category_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("item_categories", new KanojoItemCategoryParser(item_class))));
    }

    public Response<BarcodeKanojoModel> store_items(int item_class, int item_category_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_STORE_ITEMS), new BasicNameValuePair("item_class", String.valueOf(item_class)), new BasicNameValuePair("item_category_id", String.valueOf(item_category_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("item_categories", new KanojoItemCategoryParser(item_class))));
    }

    public Response<BarcodeKanojoModel> do_date(int kanojo_id, int basic_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_COMMUNICATION_DO_DATE), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("basic_item_id", String.valueOf(basic_item_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("self_user", new UserParser()), new ModelParser("owner_user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("love_increment", new LoveIncrementParser())));
    }

    public Response<BarcodeKanojoModel> do_extend_date(int kanojo_id, int extend_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_COMMUNICATION_DO_EXTEND_DATE), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("extend_item_id", String.valueOf(extend_item_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("self_user", new UserParser()), new ModelParser("owner_user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("love_increment", new LoveIncrementParser())));
    }

    public Response<BarcodeKanojoModel> do_gift(int kanojo_id, int basic_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_COMMUNICATION_DO_GIFT), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("basic_item_id", String.valueOf(basic_item_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("self_user", new UserParser()), new ModelParser("owner_user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("love_increment", new LoveIncrementParser())));
    }

    public Response<BarcodeKanojoModel> do_extend_gift(int kanojo_id, int extend_item_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_COMMUNICATION_DO_EXTEND_GIFT), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("extend_item_id", String.valueOf(extend_item_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("self_user", new UserParser()), new ModelParser("owner_user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("love_increment", new LoveIncrementParser())));
    }

    public Response<BarcodeKanojoModel> play_on_live2d(int kanojo_id, String actions) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_COMMUNICATION_PLAY_ON_LIVE2D), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)), new BasicNameValuePair("actions", actions));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("self_user", new UserParser()), new ModelParser("owner_user", new UserParser()), new ModelParser("kanojo", new KanojoParser()), new ModelParser("love_increment", new LoveIncrementParser())));
    }

    public Response<BarcodeKanojoModel> user_timeline(int user_id, int since_id, int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_ACTIVITY_USER_TIMELINE), new BasicNameValuePair("user_id", String.valueOf(user_id)), new BasicNameValuePair("since_id", String.valueOf(since_id)), new BasicNameValuePair("index", String.valueOf(index)), new BasicNameValuePair("limit", String.valueOf(limit)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("activities", new ActivityParser())));
    }

    public Response<BarcodeKanojoModel> scanned_timeline(String barcode, int since_id, int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_ACTIVITY_SCANNED_TIMELINE), new BasicNameValuePair("barcode", barcode), new BasicNameValuePair("since_id", String.valueOf(since_id)), new BasicNameValuePair("index", String.valueOf(index)), new BasicNameValuePair("limit", String.valueOf(limit)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelListParser("activities", new ActivityParser())));
    }

    public String item_detail(int store_item_id) {
        return String.valueOf(fullUrl(URL_API_PAYMENT_ITEM_DETAIL)) + "?" + new BasicNameValuePair("store_item_id", String.valueOf(store_item_id));
    }

    public Response<BarcodeKanojoModel> payment_verify(String payment_id) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_PAYMENT_VERIFY), new BasicNameValuePair("payment_id", payment_id));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser()));
    }

    public Response<BarcodeKanojoModel> product_category_list() throws BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_RESOURCE_PRODUCT_CATEGORY_LIST));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new CategoryParser()));
    }

    private String fullUrl(String url) {
        return this.mApiBaseUrl + url;
    }

    public Response<BarcodeKanojoModel> getURLWebView(String uuid) throws BarcodeKanojoException, IOException {
        String uuid2 = Codec.encode(uuid);
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_WEBVIEW), new BasicNameValuePair("uuid", uuid2));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new WebViewParser()));
    }

    public Response<BarcodeKanojoModel> getURLRadarWebView(int kanojo_id) throws BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_RADAR_WEBVIEW), new BasicNameValuePair("kanojo_id", String.valueOf(kanojo_id)));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new WebViewParser()));
    }

    public Response<BarcodeKanojoModel> delete(int user_id) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_ACCOUNT_DELETE), new NameValueOrFilePair("user_id", new StringBuilder().append(user_id).toString()));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> account_show() throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_ACCOUNT_SHOW), new NameValuePair[0]);
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_register_fb(String facebookid, String facebookToken) throws BarcodeKanojoException, IOException {
        String facebookid2 = Codec.encode(facebookid);
        String facebookToken2 = Codec.encode(facebookToken);
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_FACEBOOK_CONNECT), new BasicNameValuePair("FACEBOOK_ID", facebookid2), new BasicNameValuePair("FACEBOOK_TOKEN", facebookToken2));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_disconnect_fb() throws BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_FACEBOOK_DISCONNECT), new NameValuePair[0]);
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_register_twitter(String access_token, String access_secret) throws BarcodeKanojoException, IOException {
        String access_token2 = Codec.encode(access_token);
        String access_secret2 = Codec.encode(access_secret);
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_TWITTER_CONNECT), new BasicNameValuePair("access_token", access_token2), new BasicNameValuePair("access_secret", access_secret2));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_disconnect_twitter() throws BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_TWITTER_DISCONNECT), new NameValuePair[0]);
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_register_device(String uuid, String device_token) throws BarcodeKanojoException, IOException {
        String uuid2 = Codec.encode(uuid);
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_REGISTER_TOKEN), new NameValueOrFilePair("uuid", uuid2), new NameValueOrFilePair((String) Preferences.PREFERENCE_DEVICE_TOKEN, device_token));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> show_dialog() throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_MESSAGE_DIALOG), new NameValuePair[0]);
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new KanojoMessageParser()));
    }

    public Response<BarcodeKanojoModel> android_uuid_verify(String email, String password, String uuid) throws IllegalStateException, BarcodeKanojoException, IOException {
        String uuid2 = Codec.encode(uuid);
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_VERIFY_UUID), new BasicNameValuePair("email", email), new BasicNameValuePair("password", password), new BasicNameValuePair("uuid", uuid2));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_update(String name, String current_apssword, String new_password, String email, int birth_month, int birth_day, int birth_year, String sex, String description, File profile_image_data) throws BarcodeKanojoException, IOException {
        NameValueOrFilePair[] value = new NameValueOrFilePair[10];
        if (name == null || name.equals("")) {
            value[0] = null;
        } else {
            value[0] = new NameValueOrFilePair("name", name);
        }
        if (current_apssword == null || current_apssword.equals("")) {
            value[1] = null;
        } else {
            value[1] = new NameValueOrFilePair("current_apssword", current_apssword);
        }
        if (new_password == null || new_password.equals("")) {
            value[2] = null;
        } else {
            value[2] = new NameValueOrFilePair("new_password", new_password);
        }
        if (email == null || email.length() == 0) {
            value[3] = null;
        } else {
            value[3] = new NameValueOrFilePair("email", email);
        }
        if (birth_month == 0) {
            value[4] = null;
        } else {
            value[4] = new NameValueOrFilePair("birth_month", String.valueOf(birth_month));
        }
        if (birth_day == 0) {
            value[5] = null;
        } else {
            value[5] = new NameValueOrFilePair("birth_day", String.valueOf(birth_day));
        }
        if (birth_year == 0) {
            value[6] = null;
        } else {
            value[6] = new NameValueOrFilePair("birth_year", String.valueOf(birth_year));
        }
        if (sex == null) {
            value[7] = null;
        } else {
            value[7] = new NameValueOrFilePair("sex", String.valueOf(sex));
        }
        if (description == null || description.equals("")) {
            value[8] = null;
        } else {
            value[8] = new NameValueOrFilePair("description", String.valueOf(description));
        }
        if (profile_image_data == null) {
            value[9] = null;
        } else {
            value[9] = new NameValueOrFilePair("profile_image_data", profile_image_data);
        }
        HttpPost httpPost = this.mHttpApi.createHttpMultipartPost(fullUrl(URL_API_ACCOUNT_UPDATE), value);
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser(), new ModelParser("user", new UserParser())));
    }

    public Response<BarcodeKanojoModel> android_get_transaction_id(int store_item_id) throws BarcodeKanojoException, IOException {
        HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(URL_API_COMMUNICATION_CONFIRM_PURCHASE_ITEM), new BasicNameValuePair("store_item_id", new StringBuilder(String.valueOf(store_item_id)).toString()));
        return (Response) this.mHttpApi.executeHttpRequest(httpGet, new ResponseParser(new PurchaseItemParser()));
    }

    public Response<BarcodeKanojoModel> android_verify_purchased(int store_item_id, int google_transaction_id, String receipt_data) throws BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_COMMUNICATION_VERIFY_PURCHASED_ITEM), new BasicNameValuePair("store_item_id", new StringBuilder(String.valueOf(store_item_id)).toString()), new BasicNameValuePair("google_transaction_id", new StringBuilder(String.valueOf(google_transaction_id)).toString()), new BasicNameValuePair("receipt_data", new StringBuilder(String.valueOf(receipt_data)).toString()));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser()));
    }

    public Response<BarcodeKanojoModel> android_check_ticket(int price, int store_item_id) throws BarcodeKanojoException, IOException {
        return (Response) this.mHttpApi.executeHttpRequest(this.mHttpApi.createHttpPost(fullUrl(URL_API_SHOPPING_COMPARE_PRICE), new BasicNameValuePair("price", new StringBuilder(String.valueOf(price)).toString()), new BasicNameValuePair("store_item_id", new StringBuilder(String.valueOf(store_item_id)).toString())), new ResponseParser(new JSONParser[0]));
    }

    public Response<BarcodeKanojoModel> do_ticket(int store_item_id, int use_tickets) throws IllegalStateException, BarcodeKanojoException, IOException {
        HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(URL_API_SHOPPING_VERIFY_TICKET), new BasicNameValuePair("store_item_id", String.valueOf(store_item_id)), new BasicNameValuePair("use_tickets", String.valueOf(use_tickets)));
        return (Response) this.mHttpApi.executeHttpRequest(httpPost, new ResponseParser(new AlertParser()));
    }
}
