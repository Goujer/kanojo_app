package com.goujer.barcodekanojo.core

import android.content.Context
import android.location.Location
import android.util.Log
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList
import com.goujer.barcodekanojo.core.model.User
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import kotlin.Throws
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.Defs
import com.goujer.barcodekanojo.core.model.Kanojo
import com.goujer.barcodekanojo.core.http.HttpApi
import com.goujer.barcodekanojo.preferences.ApplicationSetting
import jp.co.cybird.barcodekanojoForGAM.core.model.Category
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.StringBuilder
import java.util.*

class BarcodeKanojo(context: Context) {
	var settings: ApplicationSetting = ApplicationSetting(context)
		private set
	private var mBCKApi: BarcodeKanojoHttpApi = BarcodeKanojoHttpApi(settings.getServerHttps(), settings.getServerURL(), settings.getServerPort(), Defs.USER_AGENT(), Defs.USER_LANGUAGE())
	private lateinit var mCategories: ModelList<Category>
	private var mPlayLive2d: PlayLive2d? = null
	var user: User? = null
	var isUserLoggedIn = false
		private set

	fun resetUser() {
		user = User()
	}

	fun signup(uuid: String?, name: String?, password: Password, email: String, birth_year: Int, birth_month: Int, birth_day: Int, sex: String?, profile_image_data: File?): Response<BarcodeKanojoModel?> {
		val mEmail = email.lowercase()
		val response = mBCKApi.signup(uuid!!, name, password, mEmail, birth_year, birth_month, birth_day, sex, profile_image_data)
		user = response[User::class.java] as User?
		return response
	}

	//Attempt Login
	@Throws(BarcodeKanojoException::class)
	fun verify(uuid: String, email: String, password: Password?): Response<BarcodeKanojoModel?> {
		val mEmail = email.lowercase(Locale.getDefault())
		var response = mBCKApi.verify(uuid, mEmail, password)
		if (response.code != Response.CODE_SUCCESS && email == "" && password == null) {
			response = mBCKApi.verify(uuid, settings.getEmail(), settings.getPassword())
		}
		this.user = response[User::class.java] as User?
		isUserLoggedIn = true
		return response
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun my_current_kanojos(index: Int, limit: Int, search: String): Response<BarcodeKanojoModel?>? {
		return try {
			current_kanojos(user!!.id, index, limit, search)
		} catch (e: Exception) {
			if (Defs.DEBUG) {
				Log.d(TAG, e.toString())
				e.printStackTrace()
			}
			throw BarcodeKanojoException(e.toString())
		}
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	private fun current_kanojos(user_id: Int, index: Int, limit: Int, search: String): Response<BarcodeKanojoModel?> {
		return mBCKApi.current_kanojos(user_id, index, limit, search)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun my_friend_kanojos(index: Int, limit: Int, search: String?): Response<BarcodeKanojoModel?> {
		return mBCKApi.friend_kanojos(user!!.id, index, limit, search)
	}

	//public Response<BarcodeKanojoModel> friend_kanojos(int user_id, int index, int limit, String search) throws IllegalStateException, BarcodeKanojoException, IOException {
	//    return this.mBCKApi.friend_kanojos(user_id, index, limit, search);
	//}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class)
	fun account_show(): Response<BarcodeKanojoModel?>? {
		return try {
			val response = mBCKApi.account_show()
			this.user = response[User::class.java] as User?
			response
		} catch (e: IOException) {
			throw BarcodeKanojoException(e.toString())
		}
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun like_ranking(index: Int, limit: Int): Response<BarcodeKanojoModel?>? {
		return mBCKApi.like_ranking(index, limit)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun show(kanojo_id: Int, screen: Boolean): Response<BarcodeKanojoModel?>? {
		return mBCKApi.show(kanojo_id, screen)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun vote_like(): Response<BarcodeKanojoModel?>? {
		var kanojo: Kanojo
		return if (mPlayLive2d != null && mPlayLive2d!!.kanojo != null) {
			kanojo = mPlayLive2d!!.kanojo!!
			mBCKApi.vote_like(kanojo.id, kanojo.isVoted_like)
		} else {
			null
		}
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun query(barcode: String?, geo: Location?): Response<BarcodeKanojoModel?>? {
		return mBCKApi.query(barcode, geo)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scan(barcode: String?, company_name: String?, product_name: String?, product_category_id: Int, product_comment: String?, product_image_data: File?, product_geo: Location?): Response<BarcodeKanojoModel?>? {
		return mBCKApi.scan(barcode!!, company_name, product_name, product_category_id, product_comment, product_image_data, product_geo)
	}

	//public Response<BarcodeKanojoModel> scan(String barcode, String company_name, String company_name_textid, String product_name, String product_name_textid, int product_category_id, String product_comment, String product_comment_textid, File product_image_data, Location product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
	//    return this.mBCKApi.scan(barcode, company_name, company_name_textid, product_name, product_name_textid, product_category_id, product_comment, product_comment_textid, product_image_data, product_geo);
	//}

	//public Response<BarcodeKanojoModel> scan_and_generate(String barcode, String company_name, String kanojo_name, File kanojo_profile_image_data, String product_name, int product_category_id, String product_comment, File product_image_data, LatLng product_geo) throws IllegalStateException, BarcodeKanojoException, IOException {
	//    return this.mBCKApi.scan_and_generate(barcode, company_name, kanojo_name, kanojo_profile_image_data, product_name, product_category_id, product_comment, product_image_data, product_geo);
	//}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scan_and_generate(barcode: String?, company_name: String?, kanojo_name: String?, kanojo_profile_image_data: File?, product_name: String?, product_category_id: Int, product_comment: String?, product_image_data: File?, product_geo: Location?): Response<BarcodeKanojoModel?>? {
		return mBCKApi.scan_and_generate(barcode, company_name, kanojo_name, kanojo_profile_image_data, product_name, product_category_id, product_comment, product_image_data, product_geo)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun decrease_generating(barcode: String?): Response<BarcodeKanojoModel?>? {
		return mBCKApi.decrease_generating(barcode)
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun update(barcode: String?, company_name: String?, product_name: String?, product_category_id: Int, product_comment: String?, product_image_data: File?, product_geo: Location?): Response<BarcodeKanojoModel?>? {
		return mBCKApi.barcode_update(barcode, company_name, product_name, product_category_id, product_comment, product_image_data, product_geo)
	}

	//public Response<BarcodeKanojoModel> update(String barcode, String company_name, String product_name, int product_category_id, String product_comment, File product_image_data, Location product_geo) throws BarcodeKanojoException, IOException {
	//    return this.mBCKApi.account_update(barcode, company_name, product_name, product_category_id, product_comment, product_image_data, product_geo);
	//}

	//public Response<BarcodeKanojoModel> update(String name, String name_textid, File profile_image_data) throws BarcodeKanojoException, IOException {
	//    return this.mBCKApi.update(name, name_textid, profile_image_data);
	//}

	//public Response<BarcodeKanojoModel> iphone_update(String name, String current_password, String new_password, String email, int birth_month, int birth_day, String sex, String description, File profile_image_data) throws BarcodeKanojoException, IOException {
	//    Response<BarcodeKanojoModel> response = this.mBCKApi.iphone_update(name, current_password, new_password, email, birth_month, birth_day, sex, description, profile_image_data);
	//    User user = (User) response.get(User.class);
	//    if (user != null) {
	//        setUser(user);
	//    }
	//    return response;
	//}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun my_user_timeline(since_id: Int, index: Int, limit: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.user_timeline(user!!.id, since_id, index, limit)
	}

	//public Response<BarcodeKanojoModel> user_timeline(int user_id, int since_id, int index, int limit) throws IllegalStateException, BarcodeKanojoException, IOException {
	//    return this.mBCKApi.user_timeline(user_id, since_id, index, limit);
	//}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun scanned_timeline(barcode: String?, since_id: Int, index: Int, limit: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.scanned_timeline(barcode, since_id, index, limit)
	}

	/**
	 *
	 * @param kanojo_id    id of the kanojo being dated.
	 * @param type_id
	 * @return            response with KanojoItemCategory in it with dates
	 * @throws IllegalStateException
	 * @throws BarcodeKanojoException
	 * @throws IOException
	 */
	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun date_menu(kanojo_id: Int, type_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.date_menu(kanojo_id, type_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun gift_menu(kanojo_id: Int, type_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.gift_menu(kanojo_id, type_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun permanent_item_gift_menu(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.permanent_item_gift_menu(item_class, item_category_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun permanent_sub_item_gift_menu(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.permanent_sub_item_gift_menu(item_class, item_category_id)
	}

	//public Response<BarcodeKanojoModel> date_and_gift_menu(int kanojo_id) throws IllegalStateException, BarcodeKanojoException, IOException {
	//    return this.mBCKApi.date_and_gift_menu(kanojo_id);
	//}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun has_items(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.has_items(item_class, item_category_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun store_items(item_class: Int, item_category_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.store_items(item_class, item_category_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_date(kanojo_id: Int, basic_item_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.do_date(kanojo_id, basic_item_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_extend_date(kanojo_id: Int, extend_item_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.do_extend_date(kanojo_id, extend_item_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_gift(kanojo_id: Int, basic_item_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.do_gift(kanojo_id, basic_item_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_extend_gift(kanojo_id: Int, extend_item_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.do_extend_gift(kanojo_id, extend_item_id)
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun play_on_live2d(): Response<BarcodeKanojoModel?>? {
		if (mPlayLive2d == null) {
			return null
		}
		val kanojo = mPlayLive2d!!.kanojo
		val actions = mPlayLive2d!!.actions
		if (kanojo == null || actions == null) {
			return null
		}
		return if (kanojo.relation_status != Kanojo.RELATION_KANOJO && kanojo.relation_status != Kanojo.RELATION_FRIEND) {
			null
		} else mBCKApi.play_on_live2d(kanojo.id, actions)
	}

	fun setPlayLive2d(kanojo: Kanojo?, actions: IntArray) {
		initPlayLive2d()
		mPlayLive2d!!.kanojo = kanojo
		val s = StringBuilder()
		for (action in actions) {
			s.append(action).append("|")
		}
		mPlayLive2d!!.actions = s.toString()
	}

	private fun initPlayLive2d() {
		if (mPlayLive2d != null) {
			mPlayLive2d!!.actions = null
			mPlayLive2d!!.kanojo = null
		}
		mPlayLive2d = PlayLive2d()
	}

	//public String item_detail(int store_item_id) {
	//    return this.mBCKApi.item_detail(store_item_id);
	//}

	//public Response<BarcodeKanojoModel> payment_verify(String payment_id) throws IllegalStateException, BarcodeKanojoException, IOException {
	//    return this.mBCKApi.payment_verify(payment_id);
	//}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun init_product_category_list() {
		val product_category_list = mBCKApi.product_category_list()
		when (val code = product_category_list.code) {
			Response.CODE_SUCCESS -> {
				mCategories = product_category_list.categoryModelList
				return
			}
			Response.CODE_ERROR_BAD_REQUEST, Response.CODE_ERROR_UNAUTHORIZED, Response.CODE_ERROR_FORBIDDEN, Response.CODE_ERROR_NOT_FOUND, Response.CODE_ERROR_SERVER, Response.CODE_ERROR_SERVICE_UNAVAILABLE ->
				throw BarcodeKanojoException("Error: Code: $code Category list not initialized!")
		}
	}

	@get:Throws(BarcodeKanojoException::class)
	val categoryList: ModelList<Category>
		get() {
			if (mCategories != null && mCategories!!.isNotEmpty()) {
				return mCategories
			}
			throw BarcodeKanojoException("Categories is empty")
		}

	//fun createHttpApi(useHttps: Boolean, domain: String?, port: Int, clientVersion: String?, clientLanguage: String?) {
	//	if (mBCKApi == null) {
	//		mBCKApi = BarcodeKanojoHttpApi(useHttps, domain!!, port, clientVersion, clientLanguage)
	//	} else {
	//		get(useHttps, domain, port, clientVersion, clientLanguage)
	//	}
	//}

	//@Deprecated
	//public static BarcodeKanojoHttpApi createHttpApi(String clientVersion, String clientLanguage) {
	//    return createHttpApi(Defs.URL_BASE(), clientVersion, clientLanguage);
	//}

	internal class PlayLive2d {
		var actions: String? = null
		var kanojo: Kanojo? = null
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun android_delete_account(user_id: Int): Response<BarcodeKanojoModel?> {
		return mBCKApi.account_delete(user_id)
	}

	@Throws(BarcodeKanojoException::class)
	fun android_register_device(uuid: String?, strDeviceToken: String?): Response<BarcodeKanojoModel?>? {
		return try {
			mBCKApi.android_register_device(uuid, strDeviceToken)
		} catch (e: IOException) {
			throw BarcodeKanojoException(e.toString())
		}
	}

	fun getURLWebView(uuid: String?): Response<BarcodeKanojoModel?>? {
		return try {
			mBCKApi.getURLWebView(uuid)
		} catch (e: BarcodeKanojoException) {
			e.printStackTrace()
			null
		} catch (e2: IOException) {
			e2.printStackTrace()
			null
		}
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun show_dialog(): Response<BarcodeKanojoModel?> {
		return mBCKApi.show_dialog()
	}

	@Throws(BarcodeKanojoException::class, IOException::class)
	fun update(name: String?, current_password: Password?, new_password: Password?, email: String?, birth_year: Int, birth_month: Int, birth_day: Int, sex: String?, profile_image_data: File?): Response<BarcodeKanojoModel?> {
		var mEmail = email
		if (mEmail != null) {
			mEmail = mEmail.lowercase(Locale.getDefault())
		}
		val response = mBCKApi.account_update(name, current_password, new_password, mEmail, birth_year, birth_month, birth_day, sex, profile_image_data)
		this.user = response[User::class.java] as User?
		return response
	}

	@Throws(BarcodeKanojoException::class)
	fun android_get_transaction_id(store_item_id: Int): Response<BarcodeKanojoModel?>? {
		return try {
			mBCKApi.android_get_transaction_id(store_item_id)
		} catch (e: IOException) {
			throw BarcodeKanojoException(e.toString())
		}
	}

	@Throws(BarcodeKanojoException::class)
	fun android_verify_purchased(store_item_id: Int, google_transaction_id: Int, receipt_data: String?): Response<BarcodeKanojoModel?>? {
		return try {
			mBCKApi.android_verify_purchased(store_item_id, google_transaction_id, receipt_data!!)
		} catch (e: IOException) {
			throw BarcodeKanojoException(e.toString())
		}
	}

	@Throws(BarcodeKanojoException::class)
	fun android_check_ticket(price: Int, store_item_id: Int): Response<BarcodeKanojoModel?>? {
		return try {
			mBCKApi.android_check_ticket(price, store_item_id)
		} catch (e: IOException) {
			throw BarcodeKanojoException(e.toString())
		}
	}

	@Throws(IllegalStateException::class, BarcodeKanojoException::class, IOException::class)
	fun do_ticket(store_item_id: Int, use_tickets: Int): Response<BarcodeKanojoModel?>? {
		return mBCKApi.do_ticket(store_item_id, use_tickets)
	}

	val httpApi: HttpApi
		get() = mBCKApi.mHttpApi

	companion object {
		private const val TAG = "BarcodeKanojo"
	}
}