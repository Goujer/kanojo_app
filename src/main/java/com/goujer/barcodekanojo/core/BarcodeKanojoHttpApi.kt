package com.goujer.barcodekanojo.core

import com.google.android.gms.maps.model.LatLng
import com.goujer.barcodekanojo.core.http.HttpApi
import com.goujer.barcodekanojo.core.http.NameValueOrFilePair
import com.goujer.barcodekanojo.core.http.NameValuePair
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.core.model.User
import jp.co.cybird.barcodekanojoForGAM.core.parser.*
import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection

class BarcodeKanojoHttpApi(useHttps: Boolean, mApiBaseUrl: String, mApiBasePort: Int, mClientVersion: String?, mClientLanguage: String?) {
	private var mHttpApi: HttpApi = HttpApi(useHttps, mApiBaseUrl, mApiBasePort, mClientVersion, mClientLanguage)

	fun updateApiBase(useHttps: Boolean, newBaseUrl: String, newBasePort: Int, newClientVersion: String?, newClientLanguage: String?) {
		mHttpApi = HttpApi(useHttps, newBaseUrl, newBasePort, newClientVersion, newClientLanguage)
	}

	fun getApiBaseUrl(): String {
		return mHttpApi.mApiBaseUrl
	}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun iphone_signup(name: String?, password: String?, email: String?, birth_month: Int, birth_day: Int, sex: String?, description: String?, profile_image_data: File?, udid: String?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_SIGNUP,
	//			NameValueOrFilePair("name", name),
	//			NameValueOrFilePair("password", password),
	//			NameValueOrFilePair("email", email),
	//			NameValueOrFilePair("birth_month", birth_month.toString()),
	//			NameValueOrFilePair("birth_day", birth_day.toString()),
	//			NameValueOrFilePair("sex", sex),
	//			NameValueOrFilePair("description", description),
	//			NameValueOrFilePair("profile_image_data", profile_image_data),
	//			NameValueOrFilePair("uuid", udid))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun android_signup(birth_year: Int, birth_month: Int, birth_day: Int, sex: String?, uuid: String?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_SIGNUP,
	//			NameValueOrFilePair("uuid", uuid),
	//			NameValueOrFilePair("birth_month", birth_month.toString()),
	//			NameValueOrFilePair("birth_day", birth_day.toString()),
	//			NameValueOrFilePair("sex", sex))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	fun verify(email: String, password: String, uuid: String): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_ACCOUNT_VERIFY,
				NameValuePair("uuid", uuid),
				NameValuePair("email", email),
				NameValuePair("password", password))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	}

	fun signup(name: String?, password: String?, email: String?, birth_year: Int, birth_month: Int, birth_day: Int, sex: String?, profile_image_data: File?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_SIGNUP,
				NameValueOrFilePair("name", name),
				NameValueOrFilePair("password", password),
				NameValueOrFilePair("email", email),
				NameValueOrFilePair("birth_year", birth_year.toString()),
				NameValueOrFilePair("birth_month", birth_month.toString()),
				NameValueOrFilePair("birth_day", birth_day.toString()),
				NameValueOrFilePair("sex", sex),
				NameValueOrFilePair("profile_image_data", profile_image_data))
		return  mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	}

//	@Throws(BarcodeKanojoException::class, IOException::class)
//	fun verify(gree_id: Int, verify_key: String?): Response<BarcodeKanojoModel?>? {
//		val connection = mHttpApi.createHttpPost(URL_API_ACCOUNT_VERIFY, NameValuePair("gree_id", gree_id.toString()), NameValuePair("verify_key", verify_key))
//		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
//	}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun iphone_verify(email: String?, password: String?, udid: String?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpPost(URL_API_ACCOUNT_VERIFY, NameValuePair("email", email), NameValuePair("password", password), NameValuePair("udid", udid))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun android_verify(udid: String?): Response<BarcodeKanojoModel?>? {
	//	val connection: HttpURLConnection = mHttpApi.createHttpPost(URL_API_ACCOUNT_VERIFY, NameValuePair("uuid", udid))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//fun update(name: String?, profile_image_data: File?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_UPDATE,
	//			NameValueOrFilePair("name", name),
	//			NameValueOrFilePair("profile_image_data", profile_image_data))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun update(name: String?, name_textid: String?, profile_image_data: File?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_UPDATE, NameValueOrFilePair("name", name), NameValueOrFilePair("name_textid", name_textid), NameValueOrFilePair("profile_image_data", profile_image_data))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun iphone_update(name: String?, current_apssword: String?, new_password: String?, email: String?, birth_month: Int, birth_day: Int, sex: String?, description: String?, profile_image_data: File?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_UPDATE, NameValueOrFilePair("name", name), NameValueOrFilePair("current_password", current_apssword), NameValueOrFilePair("new_password", new_password), NameValueOrFilePair("email", email), NameValueOrFilePair("birth_month", birth_month.toString()), NameValueOrFilePair("birth_day", birth_day.toString()), NameValueOrFilePair("sex", sex), NameValueOrFilePair("description", description), NameValueOrFilePair("profile_image_data", profile_image_data))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun update(name: String?, profile_image_data: File?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_UPDATE, NameValueOrFilePair("name", name), NameValueOrFilePair("profile_image_data", profile_image_data))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun current_kanojos(user_id: Int, index: Int, limit: Int, search: String?): Response<BarcodeKanojoModel?>? {
		val httpPost: HttpURLConnection = mHttpApi.createHttpGet(URL_API_USER_CURRENT_KANOJOS, NameValuePair("user_id", user_id.toString()), NameValuePair("index", index.toString()), NameValuePair("limit", limit.toString()), NameValuePair("search", search))
		return mHttpApi.executeHttpRequest(httpPost, ResponseParser(ModelListParser("current_kanojos", KanojoParser()), ModelParser("user", UserParser()), ModelParser("search_result", SearchResultParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun friend_kanojos(user_id: Int, index: Int, limit: Int, search: String?): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpGet(URL_API_USER_FRIEND_KANOJOS, NameValuePair("user_id", user_id.toString()), NameValuePair("index", index.toString()), NameValuePair("limit", limit.toString()), NameValuePair("search", search))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("friend_kanojos", KanojoParser()), ModelParser("user", UserParser()), ModelParser("search_result", SearchResultParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun like_ranking(index: Int, limit: Int): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpGet(URL_API_KANOJO_LIKE_RANKING, NameValuePair("index", index.toString()), NameValuePair("limit", limit.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("like_ranking_kanojos", KanojoParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun show(kanojo_id: Int, screen: Boolean): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpGet(URL_API_KANOJO_SHOW,
				NameValuePair("kanojo_id", kanojo_id.toString()),
				NameValuePair("screen", if (screen) "live2d" else ""))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), MessageParser(MessageModel.NOTIFY_AMENDMENT_INFORMATION), ModelParser("kanojo", KanojoParser()), ModelParser("owner_user", UserParser()), ModelParser("product", ProductParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun vote_like(kanojo_id: Int, like: Boolean): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_KANOJO_VOTE_LIKE,
				NameValuePair("kanojo_id", kanojo_id.toString()),
				NameValuePair("like", like.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("kanojo", KanojoParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun query(barcode: String?, geo: LatLng?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_BARCODE_QUERY, NameValuePair("barcode", barcode), NameValuePair("geo", GeoUtil.geoToString(geo)))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("owner_user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("barcode", BarcodeParser()), ModelParser("product", ProductParser()), ModelParser("scan_history", ScanHistoryParser()), MessageParser(MessageModel.NOTIFY_AMENDMENT_INFORMATION, MessageModel.DO_GENERATE_KANOJO, MessageModel.DO_ADD_FRIEND, MessageModel.INFORM_GIRLFRIEND, MessageModel.INFORM_FRIEND)))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scan(barcode: String?, company_name: String?, product_name: String?, product_category_id: Int, product_comment: String?, product_image_data: File?, product_geo: LatLng?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_BARCODE_SCAN, NameValueOrFilePair("barcode", barcode), NameValueOrFilePair("company_name", company_name), NameValueOrFilePair("product_name", product_name), NameValueOrFilePair("product_category_id", product_category_id.toString()), NameValueOrFilePair("product_comment", product_comment), NameValueOrFilePair("product_image_data", product_image_data), NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("kanojo", KanojoParser()), ModelParser("scan_history", ScanHistoryParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scan(barcode: String?, company_name: String?, comapny_name_textid: String?, product_name: String?, product_name_textid: String?, product_category_id: Int, product_comment: String?, product_comment_textid: String?, product_image_data: File?, product_geo: LatLng?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_BARCODE_SCAN, NameValueOrFilePair("barcode", barcode), NameValueOrFilePair("company_name", company_name), NameValueOrFilePair("company_name_textid", comapny_name_textid), NameValueOrFilePair("product_name", product_name), NameValueOrFilePair("product_name_textid", product_name_textid), NameValueOrFilePair("product_category_id", product_category_id.toString()), NameValueOrFilePair("product_comment", product_comment), NameValueOrFilePair("product_comment_textid", product_comment_textid), NameValueOrFilePair("product_image_data", product_image_data), NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("kanojo", KanojoParser()), ModelParser("scan_history", ScanHistoryParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scan_and_generate(barcode: String?, company_name: String?, kanojo_name: String?, kanojo_profile_image_data: File?, product_name: String?, product_category_id: Int, product_comment: String?, product_image_data: File?, product_geo: LatLng?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_BARCODE_SCAN_AND_GENERATE,
				NameValueOrFilePair("barcode", barcode),
				NameValueOrFilePair("company_name", company_name),
				NameValueOrFilePair("kanojo_name", kanojo_name),
				NameValueOrFilePair("kanojo_profile_image_data", kanojo_profile_image_data),
				NameValueOrFilePair("product_name", product_name),
				NameValueOrFilePair("product_category_id", product_category_id.toString()),
				NameValueOrFilePair("product_comment", product_comment),
				NameValueOrFilePair("product_image_data", product_image_data),
				NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("scan_history", ScanHistoryParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scan_and_generate(barcode: String?, company_name: String?, company_name_textid: String?, kanojo_name: String?, kanojo_name_textid: String?, kanojo_profile_image_data: File?, product_name: String?, product_name_textid: String?, product_category_id: Int, product_comment: String?, product_comment_textid: String?, product_image_data: File?, product_geo: LatLng?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_BARCODE_SCAN_AND_GENERATE,
				NameValueOrFilePair("barcode", barcode),
				NameValueOrFilePair("company_name", company_name),
				NameValueOrFilePair("company_name_textid", company_name_textid),
				NameValueOrFilePair("kanojo_name", kanojo_name),
				NameValueOrFilePair("kanojo_name_textid", kanojo_name_textid),
				NameValueOrFilePair("kanojo_profile_image_data", kanojo_profile_image_data),
				NameValueOrFilePair("product_name", product_name),
				NameValueOrFilePair("product_name_textid", product_name_textid),
				NameValueOrFilePair("product_category_id", product_category_id.toString()),
				NameValueOrFilePair("product_comment", product_comment),
				NameValueOrFilePair("product_comment_textid", product_comment_textid),
				NameValueOrFilePair("product_image_data", product_image_data),
				NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("scan_history", ScanHistoryParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun decrease_generating(barcode: String?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_BARCODE_DECREASE_GENERATING, NameValuePair("barcode", barcode))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser()), ModelParser("product", ProductParser())))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun update(barcode: String?, company_name: String?, product_name: String?, product_category_id: Int, product_comment: String?, product_image_data: File?, product_geo: LatLng?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_BARCODE_UPDATE,
				NameValueOrFilePair("barcode", barcode),
				NameValueOrFilePair("company_name", company_name),
				NameValueOrFilePair("product_name", product_name),
				NameValueOrFilePair("product_category_id", product_category_id.toString()),
				NameValueOrFilePair("product_comment", product_comment),
				NameValueOrFilePair("product_image_data", product_image_data),
				NameValueOrFilePair("product_geo", GeoUtil.geoToString(product_geo)))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser()))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun gift_menu(kanojo_id: Int, type_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_GIFT_MENU, NameValuePair("kanojo_id", kanojo_id.toString()), NameValuePair("type_id", type_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("item_categories", KanojoItemCategoryParser(1))))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun permanent_item_gift_menu(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_PERMANENT_ITEM_GIFT_MENU, NameValuePair("item_class", item_class.toString()), NameValuePair("item_category_id", item_category_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("item_categories", KanojoItemCategoryParser(item_class))))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun permanent_sub_item_gift_menu(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_PERMANENT_SUB_ITEM_GIFT_MENU, NameValuePair("item_class", item_class.toString()), NameValuePair("item_category_id", item_category_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("item_categories", KanojoItemCategoryParser(item_class))))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun date_and_gift_menu(kanojo_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_DATE_AND_GIFT_MENU, NameValuePair("kanojo_id", kanojo_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("item_categories", KanojoItemCategoryParser(1))))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun date_menu(kanojo_id: Int, type_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_DATE_MENU, NameValuePair("kanojo_id", kanojo_id.toString()), NameValuePair("type_id", type_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("item_categories", KanojoItemCategoryParser(2))))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun has_items(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_HAS_ITEMS, NameValuePair("item_class", item_class.toString()), NameValuePair("item_category_id", item_category_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("item_categories", KanojoItemCategoryParser(item_class))))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun store_items(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_STORE_ITEMS, NameValuePair("item_class", item_class.toString()), NameValuePair("item_category_id", item_category_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("item_categories", KanojoItemCategoryParser(item_class))))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_date(kanojo_id: Int, basic_item_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_COMMUNICATION_DO_DATE, NameValuePair("kanojo_id", kanojo_id.toString()), NameValuePair("basic_item_id", basic_item_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("self_user", UserParser()), ModelParser("owner_user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("love_increment", LoveIncrementParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_extend_date(kanojo_id: Int, extend_item_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_COMMUNICATION_DO_EXTEND_DATE, NameValuePair("kanojo_id", kanojo_id.toString()), NameValuePair("extend_item_id", extend_item_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("self_user", UserParser()), ModelParser("owner_user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("love_increment", LoveIncrementParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_gift(kanojo_id: Int, basic_item_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_COMMUNICATION_DO_GIFT, NameValuePair("kanojo_id", kanojo_id.toString()), NameValuePair("basic_item_id", basic_item_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("self_user", UserParser()), ModelParser("owner_user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("love_increment", LoveIncrementParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_extend_gift(kanojo_id: Int, extend_item_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_COMMUNICATION_DO_EXTEND_GIFT, NameValuePair("kanojo_id", kanojo_id.toString()), NameValuePair("extend_item_id", extend_item_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("self_user", UserParser()), ModelParser("owner_user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("love_increment", LoveIncrementParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun play_on_live2d(kanojo_id: Int, actions: String?): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpPost(URL_API_COMMUNICATION_PLAY_ON_LIVE2D, NameValuePair("kanojo_id", kanojo_id.toString()), NameValuePair("actions", actions))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("self_user", UserParser()), ModelParser("owner_user", UserParser()), ModelParser("kanojo", KanojoParser()), ModelParser("love_increment", LoveIncrementParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun user_timeline(user_id: Int, since_id: Int, index: Int, limit: Int): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpGet(URL_API_ACTIVITY_USER_TIMELINE, NameValuePair("user_id", user_id.toString()), NameValuePair("since_id", since_id.toString()), NameValuePair("index", index.toString()), NameValuePair("limit", limit.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("activities", ActivityParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scanned_timeline(barcode: String?, since_id: Int, index: Int, limit: Int): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpGet(URL_API_ACTIVITY_SCANNED_TIMELINE, NameValuePair("barcode", barcode), NameValuePair("since_id", since_id.toString()), NameValuePair("index", index.toString()), NameValuePair("limit", limit.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelListParser("activities", ActivityParser())))
	}

	//fun item_detail(store_item_id: Int): String? {
	//	return URL_API_PAYMENT_ITEM_DETAIL + "?" + NameValuePair("store_item_id", store_item_id.toString())
	//}

	//@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	//fun payment_verify(payment_id: String?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpGet(URL_API_PAYMENT_VERIFY, NameValuePair("payment_id", payment_id))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser()))
	//}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun product_category_list(): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpGet(URL_API_RESOURCE_PRODUCT_CATEGORY_LIST)
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), CategoryParser()))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun getURLWebView(uuid: String?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_WEBVIEW, NameValuePair("uuid", uuid))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), WebViewParser()))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun getURLRadarWebView(kanojo_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_RADAR_WEBVIEW, NameValuePair("kanojo_id", kanojo_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), WebViewParser()))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun delete(user_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_ACCOUNT_DELETE, NameValuePair("user_id", user_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun account_show(): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_ACCOUNT_SHOW)
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun android_register_fb(facebookid: String?, facebookToken: String?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpPost(URL_API_FACEBOOK_CONNECT, NameValuePair("FACEBOOK_ID", facebookid), NameValuePair("FACEBOOK_TOKEN", facebookToken))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun android_disconnect_fb(): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpGet(URL_API_FACEBOOK_DISCONNECT)
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun android_register_twitter(access_token: String?, access_secret: String?): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpPost(URL_API_TWITTER_CONNECT, NameValuePair("access_token", access_token), NameValuePair("access_secret", access_secret))
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	//@Throws(BarcodeKanojoException::class, IOException::class)
	//fun android_disconnect_twitter(): Response<BarcodeKanojoModel?>? {
	//	val connection = mHttpApi.createHttpGet(URL_API_TWITTER_DISCONNECT)
	//	return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	//}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun android_register_device(uuid: String?, device_token: String?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_REGISTER_TOKEN, NameValueOrFilePair("uuid", uuid), NameValueOrFilePair(Preferences.PREFERENCE_DEVICE_TOKEN, device_token))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun show_dialog(): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_MESSAGE_DIALOG)
		return mHttpApi.executeHttpRequest(connection, ResponseParser(KanojoMessageParser()))
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun android_uuid_verify(email: String?, password: String?, uuid: String?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpPost(URL_API_VERIFY_UUID, NameValuePair("email", email), NameValuePair("password", password), NameValuePair("uuid", uuid))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun update(name: String?, current_password: String?, new_password: String?, email: String?, birth_year: Int, birth_month: Int, birth_day: Int, sex: String?, profile_image_data: File?): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpMultipartPost(URL_API_ACCOUNT_UPDATE,
				NameValueOrFilePair("name", name),
				NameValueOrFilePair("current_password", current_password),
				NameValueOrFilePair("new_password", new_password),
				NameValueOrFilePair("email", email),
				NameValueOrFilePair("birth_month", birth_month.toString()),
				NameValueOrFilePair("birth_day", birth_day.toString()),
				NameValueOrFilePair("birth_year", birth_year.toString()),
				NameValueOrFilePair("sex", sex),
				NameValueOrFilePair("profile_image_data", profile_image_data))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser(), ModelParser("user", UserParser())))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun android_get_transaction_id(store_item_id: Int): Response<BarcodeKanojoModel?>? {
		val connection = mHttpApi.createHttpGet(URL_API_COMMUNICATION_CONFIRM_PURCHASE_ITEM, NameValuePair("store_item_id", store_item_id.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(PurchaseItemParser()))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun android_verify_purchased(store_item_id: Int, google_transaction_id: Int, receipt_data: String): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpPost(URL_API_COMMUNICATION_VERIFY_PURCHASED_ITEM, NameValuePair("store_item_id", store_item_id.toString()), NameValuePair("google_transaction_id", google_transaction_id.toString()), NameValuePair("receipt_data", receipt_data))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser()))
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun android_check_ticket(price: Int, store_item_id: Int): Response<BarcodeKanojoModel?>? {
		return mHttpApi.executeHttpRequest(mHttpApi.createHttpPost(URL_API_SHOPPING_COMPARE_PRICE, NameValuePair("price", price.toString()), NameValuePair("store_item_id", store_item_id.toString())), ResponseParser())
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_ticket(store_item_id: Int, use_tickets: Int): Response<BarcodeKanojoModel?>? {
		val connection: HttpURLConnection = mHttpApi.createHttpPost(URL_API_SHOPPING_VERIFY_TICKET, NameValuePair("store_item_id", store_item_id.toString()), NameValuePair("use_tickets", use_tickets.toString()))
		return mHttpApi.executeHttpRequest(connection, ResponseParser(AlertParser()))
	}

	companion object {
		private const val TAG = "BarcodeKanojoHttpApi"
		private const val URL_API_ACCOUNT_DELETE = "/api/account/delete.json"
		private const val URL_API_ACCOUNT_SHOW = "/api/account/show.json"
		private const val URL_API_ACCOUNT_SIGNUP = "/api/account/signup.json"
		private const val URL_API_ACCOUNT_UPDATE = "/api/account/update.json"
		private const val URL_API_ACCOUNT_VERIFY = "/api/account/verify.json"
		private const val URL_API_ACTIVITY_SCANNED_TIMELINE = "/api/activity/scanned_timeline.json"
		private const val URL_API_ACTIVITY_USER_TIMELINE = "/activity/user_timeline.json"
		private const val URL_API_BARCODE_DECREASE_GENERATING = "/api/barcode/decrease_generating.json"
		private const val URL_API_BARCODE_QUERY = "/api/barcode/query.json"
		private const val URL_API_BARCODE_SCAN = "/api/barcode/scan.json"
		private const val URL_API_BARCODE_SCAN_AND_GENERATE = "/api/barcode/scan_and_generate.json"
		private const val URL_API_BARCODE_UPDATE = "/api/barcode/update.json"
		private const val URL_API_COMMUNICATION_CONFIRM_PURCHASE_ITEM = "/api/google/confirm_purchase_item.json"
		private const val URL_API_COMMUNICATION_DATE_AND_GIFT_MENU = "/api/communication/date_and_gift_menu.json"
		private const val URL_API_COMMUNICATION_DATE_MENU = "/api/communication/date_list.json"
		private const val URL_API_COMMUNICATION_DO_DATE = "/api/communication/do_date.json"
		private const val URL_API_COMMUNICATION_DO_EXTEND_DATE = "/api/communication/do_extend_date.json"
		private const val URL_API_COMMUNICATION_DO_EXTEND_GIFT = "/api/communication/do_extend_gift.json"
		private const val URL_API_COMMUNICATION_DO_GIFT = "/api/communication/do_gift.json"
		private const val URL_API_COMMUNICATION_GIFT_MENU = "/api/communication/item_list.json"
		private const val URL_API_COMMUNICATION_HAS_ITEMS = "/api/communication/has_items.json"
		private const val URL_API_COMMUNICATION_PERMANENT_ITEM_GIFT_MENU = "/api/communication/permanent_items.json"
		private const val URL_API_COMMUNICATION_PERMANENT_SUB_ITEM_GIFT_MENU = "/api/communication/permanent_sub_item.json"
		private const val URL_API_COMMUNICATION_PLAY_ON_LIVE2D = "/api/communication/play_on_live2d.json"
		private const val URL_API_COMMUNICATION_STORE_ITEMS = "/api/communication/store_items.json"
		private const val URL_API_COMMUNICATION_VERIFY_PURCHASED_ITEM = "/api/google/verify_purchased_item.json"
		private const val URL_API_FACEBOOK_CONNECT = "/api/account/connect_facebook.json"
		private const val URL_API_FACEBOOK_DISCONNECT = "/api/account/disconnect_facebook.json"
		private const val URL_API_KANOJO_LIKE_RANKING = "/api/kanojo/like_rankings.json"
		private const val URL_API_KANOJO_SHOW = "/api/kanojo/show.json"
		private const val URL_API_KANOJO_VOTE_LIKE = "/api/kanojo/vote_like.json"
		private const val URL_API_MESSAGE_DIALOG = "/api/message/dialog.json"
		private const val URL_API_PAYMENT_ITEM_DETAIL = "/api/payment/item_detail.html"
		private const val URL_API_PAYMENT_VERIFY = "/api/payment/verify.json"
		private const val URL_API_REGISTER_TOKEN = "/api/notification/register_token.json"
		private const val URL_API_RESOURCE_PRODUCT_CATEGORY_LIST = "/api/resource/product_category_list.json"
		private const val URL_API_SHOPPING_COMPARE_PRICE = "/api/shopping/compare_price.json"
		private const val URL_API_SHOPPING_GOOD_LIST = "/api/shopping/goods_list.json"
		private const val URL_API_SHOPPING_VERIFY_TICKET = "/api/shopping/verify_tickets.json"
		private const val URL_API_SUKIYA_CONNECT = "/api/account/connect_sukiya.json"
		private const val URL_API_SUKIYA_DISCONNECT = "/api/account/disconnect_sukiya.json"
		private const val URL_API_TWITTER_CONNECT = "/api/account/connect_twitter.json"
		private const val URL_API_TWITTER_DISCONNECT = "/api/account/disconnect_twitter.json"
		private const val URL_API_USER_CURRENT_KANOJOS = "/user/current_kanojos.json"
		private const val URL_API_USER_FRIEND_KANOJOS = "/api/user/friend_kanojos.json"
		private const val URL_API_VERIFY_UUID = "/api/account/uuidverify.json"
		private const val URL_RADAR_WEBVIEW = "/api/webview/chart.json"
		private const val URL_WEBVIEW = "/api/webview/show.json"
	}
}