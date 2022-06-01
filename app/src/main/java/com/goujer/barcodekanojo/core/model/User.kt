package com.goujer.barcodekanojo.core.model

import android.os.Parcel
import android.os.Parcelable
import com.goujer.barcodekanojo.core.Password.Companion.saveHashedPassword
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import com.goujer.barcodekanojo.core.Password

class User : BarcodeKanojoModel, Parcelable {
	var birth_day = 0
	var birth_month = 0
	var birth_year = 0
	var currentPassword: Password? = null
	var email: String? = null
	var enemy_count = 0
	private val genderList: Array<String>?
	var generate_count = 0
	var id = 0
	var kanojo_count = 0
	var language: String? = null
	var level = 0
	var money = 0
	var name: String? = null
	var password: Password? = null
	var relation_status = 0
	private val requestList: Array<String>?
	var scan_count = 0
	var sex: String? = null
	var stamina = 0
	var stamina_max = 0
	var tickets = 0
	var wish_count = 0

	override fun describeContents(): Int {
		return 0
	}

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(id)
		dest.writeString(email)
		if (password != null) {
			dest.writeString(password!!.hashedPassword)
			dest.writeString(password!!.mSalt)
		} else {
			dest.writeString("")
			dest.writeString("")
		}
		dest.writeString(name)
		dest.writeString(sex)
		dest.writeString(language)
		dest.writeInt(level)
		dest.writeInt(stamina)
		dest.writeInt(stamina_max)
		dest.writeInt(money)
		dest.writeInt(kanojo_count)
		dest.writeInt(generate_count)
		dest.writeInt(scan_count)
		dest.writeInt(enemy_count)
		dest.writeInt(wish_count)
		dest.writeInt(birth_month)
		dest.writeInt(birth_day)
		dest.writeInt(birth_year)
		dest.writeInt(relation_status)
		dest.writeInt(tickets)
		dest.writeString(currentPassword?.hashedPassword ?: "")
		dest.writeString(currentPassword?.mSalt ?: "")
	}

	private constructor(`in`: Parcel) {
		genderList = arrayOf("男性", "女性", "わからない")
		requestList = arrayOf("male", "female", "not sure")
		id = `in`.readInt()
		email = `in`.readString()
		password = saveHashedPassword(`in`.readString()!!, `in`.readString()!!)
		name = `in`.readString()
		sex = `in`.readString()
		language = `in`.readString()
		level = `in`.readInt()
		stamina = `in`.readInt()
		stamina_max = `in`.readInt()
		money = `in`.readInt()
		kanojo_count = `in`.readInt()
		generate_count = `in`.readInt()
		scan_count = `in`.readInt()
		enemy_count = `in`.readInt()
		wish_count = `in`.readInt()
		val `val` = BooleanArray(2)
		`in`.readBooleanArray(`val`)
		birth_month = `in`.readInt()
		birth_day = `in`.readInt()
		birth_year = `in`.readInt()
		relation_status = `in`.readInt()
		tickets = `in`.readInt()
		currentPassword = saveHashedPassword(`in`.readString()!!, `in`.readString()!!)
	}

	/* synthetic */
	internal constructor(parcel: Parcel, user: User?) : this(parcel)
	constructor() {
		genderList = arrayOf("男性", "女性", "わからない")
		requestList = arrayOf("male", "female", "not sure")
	}

	val profile_image_url: String
		get() = "/profile_images/user/$id.jpg"

	fun setSexFromText(sexText: String) {
		var selected = 0
		if (genderList != null) {
			val size = genderList.size
			for (i in 0 until size) {
				if (sexText == genderList[i]) {
					selected = i
				}
			}
		}
		sex = if (selected < requestList!!.size) {
			requestList[selected]
		} else {
			""
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

	fun setSexFromText(sexText: String, genderList2: Array<String>?) {
		var selected = 2
		if (genderList2 != null) {
			val size = genderList2.size
			for (i in 0 until size) {
				if (sexText == genderList2[i]) {
					selected = i
				}
			}
		}
		sex = if (selected < requestList!!.size) {
			requestList[selected]
		} else {
			""
		}
	}

	fun getSexText(genderList2: Array<String>): String {
		var selected = 2
		if (requestList != null) {
			val size = requestList.size
			for (i in 0 until size) {
				if (sex.equals(requestList[i], ignoreCase = true)) {
					selected = i
				}
			}
		}
		return if (selected >= genderList2.size || selected < 0) {
			""
		} else genderList2[selected]
	}

	val birthText: String
		get() = if (birth_month == 0 || birth_day == 0 || birth_year == 0) {
			""
		} else "$birth_month.$birth_day.$birth_year"

	fun setBirthFromText(birthdate: String) {
		if (birthdate != "") {
			val array = birthdate.split(".").toTypedArray()
			if (array.size == 3) {
				birth_month = array[0].toInt()
				birth_day = array[1].toInt()
				birth_year = array[2].toInt()
			}
		}
	}

	fun setBirth(month: Int, day: Int, year: Int) {
		birth_month = month
		birth_day = day
		birth_year = year
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
			override fun createFromParcel(`in`: Parcel): User {
				return User(`in`, null as User?)
			}

			override fun newArray(size: Int): Array<User?> {
				return arrayOfNulls(size)
			}
		}
		const val TAG = "User"
	}
}