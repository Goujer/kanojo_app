package com.goujer.barcodekanojo.core.model

import android.os.Parcel
import android.os.Parcelable
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel

class User : BarcodeKanojoModel, Parcelable {
	var birth_day = 1
	var birth_month = 1
	var birth_year = 2000
	var enemy_count = 0
	private val genderList = arrayOf("男性", "女性", "わからない")
	var generate_count = 0
	var id = 0
	var kanojo_count = 0
	var language: String? = null
	var level = 0
	var money = 0
	var name: String? = null
	var relation_status = 0
	private val requestList = arrayOf("male", "female", "not sure")
	var scan_count = 0
	var stamina = 0
	var stamina_max = 0
	var tickets = 0
	var wish_count = 0

	override fun describeContents(): Int {
		return 0
	}

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(id)
		dest.writeString(name)
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
	}

	private constructor(parcelIn: Parcel) {
		id = parcelIn.readInt()
		name = parcelIn.readString()
		language = parcelIn.readString()
		level = parcelIn.readInt()
		stamina = parcelIn.readInt()
		stamina_max = parcelIn.readInt()
		money = parcelIn.readInt()
		kanojo_count = parcelIn.readInt()
		generate_count = parcelIn.readInt()
		scan_count = parcelIn.readInt()
		enemy_count = parcelIn.readInt()
		wish_count = parcelIn.readInt()
		val value = BooleanArray(2)
		parcelIn.readBooleanArray(value)
		birth_month = parcelIn.readInt()
		birth_day = parcelIn.readInt()
		birth_year = parcelIn.readInt()
		relation_status = parcelIn.readInt()
		tickets = parcelIn.readInt()
	}

	/* synthetic */
	internal constructor(parcel: Parcel, user: User?) : this(parcel)

	constructor()

	val profile_image_url: String
		get() = "/profile_images/user/$id.jpg"

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
			override fun createFromParcel(parcelIn: Parcel): User {
				return User(parcelIn, null as User?)
			}

			override fun newArray(size: Int): Array<User?> {
				return arrayOfNulls(size)
			}
		}
		const val TAG = "User"
	}
}