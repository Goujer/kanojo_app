package com.goujer.barcodekanojo.core.model

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil
import jp.co.cybird.barcodekanojoForGAM.core.model.Barcode
import org.osmdroid.util.GeoPoint

class Kanojo : BarcodeKanojoModel, Parcelable {
	var accessory_type = 0
	var avatar_background_image_url: String? = null
	var barcode: String? = null
	var birth_day = 0
	var birth_month = 0
	var birth_year = 0
	var brow_position = 0f
	var brow_type = 0
	var clothes_type = 0
	var ear_type = 0
	var emotion_status = 0
	var eye_color = 0
	var eye_position = 0f
	var eye_type = 0
	var face_type = 0
	var follower_count = 0
	var fringe_type = 0
	var geo: GeoPoint
	var id = 0
	var isIn_room = false
	var like_rate = 0
	var location: String? = null
	var love_gauge = 0
	var mascotEnable = 0
	var mouth_position = 0f
	var mouth_type = 0
	var name: String? = null
	var nationality: String? = null

	var isOn_advertising = false

	var relation_status = 0

	var source: String? = null

	var status: String? = null
	var isVoted_like = false

	//Generated Attributes
	var body_type = 0
	var glasses_type = 0
	var hair_type = 0
	var nose_type = 0
	var race_type = 0 //Unused?
	var spot_type = 0

	var hair_color = 0
	var skin_color = 0

	//Chart Stats
	var flirtable = 0
	var consumption = 0
	var possession = 0
	var recognition = 0
	var sexual = 0

	constructor() {
		geo = GeoPoint(0.0, 0.0)
	}

	constructor(barcodeIn: Barcode) : this() {
		barcode = barcodeIn.barcode
		race_type = barcodeIn.race_type
		eye_type = barcodeIn.eye_type
		nose_type = barcodeIn.nose_type
		mouth_type = barcodeIn.mouth_type
		face_type = barcodeIn.face_type
		brow_type = barcodeIn.brow_type
		fringe_type = barcodeIn.fringe_type
		hair_type = barcodeIn.hair_type
		accessory_type = barcodeIn.accessory_type
		spot_type = barcodeIn.spot_type
		glasses_type = barcodeIn.glasses_type
		body_type = barcodeIn.body_type
		clothes_type = barcodeIn.clothes_type
		ear_type = barcodeIn.ear_type
		eye_position = barcodeIn.eye_position
		brow_position = barcodeIn.brow_position
		mouth_position = barcodeIn.mouth_position
		skin_color = barcodeIn.skin_color
		hair_color = barcodeIn.hair_color
		eye_color = barcodeIn.eye_color

		flirtable = barcodeIn.flirtable
		possession = barcodeIn.possession
		consumption = barcodeIn.consumption
		recognition = barcodeIn.recognition
		sexual = barcodeIn.sexual
	}

	override fun describeContents(): Int {
		return 0
	}

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeInt(id)
		dest.writeString(name)
		dest.writeString(barcode)
		dest.writeDouble(geo.latitude)
		dest.writeDouble(geo.longitude)
		dest.writeString(location)
		dest.writeInt(birth_year)
		dest.writeInt(birth_month)
		dest.writeInt(birth_day)
		dest.writeInt(race_type)
		dest.writeInt(eye_type)
		dest.writeInt(nose_type)
		dest.writeInt(mouth_type)
		dest.writeInt(face_type)
		dest.writeInt(brow_type)
		dest.writeInt(fringe_type)
		dest.writeInt(hair_type)
		dest.writeInt(accessory_type)
		dest.writeInt(spot_type)
		dest.writeInt(glasses_type)
		dest.writeInt(body_type)
		dest.writeInt(clothes_type)
		dest.writeInt(ear_type)
		dest.writeFloat(eye_position)
		dest.writeFloat(brow_position)
		dest.writeFloat(mouth_position)
		dest.writeInt(skin_color)
		dest.writeInt(hair_color)
		dest.writeInt(eye_color)

		dest.writeInt(flirtable)
		dest.writeInt(possession)
		dest.writeInt(consumption)
		dest.writeInt(recognition)
		dest.writeInt(sexual)

		dest.writeInt(love_gauge)
		dest.writeInt(follower_count)
		dest.writeString(source)
		dest.writeString(nationality)
		dest.writeInt(relation_status)
		dest.writeBooleanArray(booleanArrayOf(isVoted_like, isIn_room, isOn_advertising))
		dest.writeInt(like_rate)
		dest.writeString(status)
		dest.writeString(avatar_background_image_url)
		dest.writeInt(emotion_status)
		dest.writeInt(mascotEnable)
	}

	private constructor(parcelIn: Parcel) {
		id = parcelIn.readInt()
		name = parcelIn.readString()
		barcode = parcelIn.readString()
		geo = GeoPoint(parcelIn.readDouble(), parcelIn.readDouble())
		location = parcelIn.readString()
		birth_year = parcelIn.readInt()
		birth_month = parcelIn.readInt()
		birth_day = parcelIn.readInt()
		race_type = parcelIn.readInt()
		eye_type = parcelIn.readInt()
		nose_type = parcelIn.readInt()
		mouth_type = parcelIn.readInt()
		face_type = parcelIn.readInt()
		brow_type = parcelIn.readInt()
		fringe_type = parcelIn.readInt()
		hair_type = parcelIn.readInt()
		accessory_type = parcelIn.readInt()
		spot_type = parcelIn.readInt()
		glasses_type = parcelIn.readInt()
		body_type = parcelIn.readInt()
		clothes_type = parcelIn.readInt()
		ear_type = parcelIn.readInt()
		eye_position = parcelIn.readFloat()
		brow_position = parcelIn.readFloat()
		mouth_position = parcelIn.readFloat()
		skin_color = parcelIn.readInt()
		hair_color = parcelIn.readInt()
		eye_color = parcelIn.readInt()

		flirtable = parcelIn.readInt()
		possession = parcelIn.readInt()
		consumption = parcelIn.readInt()
		recognition = parcelIn.readInt()
		sexual = parcelIn.readInt()

		love_gauge = parcelIn.readInt()
		follower_count = parcelIn.readInt()
		source = parcelIn.readString()
		nationality = parcelIn.readString()
		relation_status = parcelIn.readInt()
		val b = BooleanArray(3)
		parcelIn.readBooleanArray(b)
		isVoted_like = b[0]
		isIn_room = b[1]
		isOn_advertising = b[2]
		like_rate = parcelIn.readInt()
		status = parcelIn.readString()
		avatar_background_image_url = parcelIn.readString()
		emotion_status = parcelIn.readInt()
		mascotEnable = parcelIn.readInt()
	}

	private constructor(parcel: Parcel, kanojo: Kanojo?) : this(parcel) {}

	fun setGeo(geo2: String?) {
		geo = GeoPoint.fromDoubleString(geo2, ',')
	}

	val geoString: String
		get() = geo.toDoubleString()

	val profile_image_icon_url: String
		get() = "/profile_images/kanojo/$id/icon.png"
	val profile_image_bust_url: String
		get() = "/profile_images/kanojo/$id/bust.png"
	val profile_image_url: String
		get() = "/profile_images/kanojo/$id/full.png"

	companion object {
		@JvmField val CREATOR: Parcelable.Creator<Kanojo> = object : Parcelable.Creator<Kanojo> {
			override fun createFromParcel(`in`: Parcel): Kanojo {
				return Kanojo(`in`, null)
			}

			override fun newArray(size: Int): Array<Kanojo?> {
				return arrayOfNulls(size)
			}
		}
		const val RELATION_FRIEND = 3
		const val RELATION_KANOJO = 2
		const val RELATION_OTHER = 1
		const val TAG = "Kanojo"
	}
}