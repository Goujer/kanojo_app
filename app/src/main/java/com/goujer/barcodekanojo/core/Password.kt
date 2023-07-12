package com.goujer.barcodekanojo.core

import android.os.Build
import android.os.Parcel
import android.os.Parcelable

import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale

class Password: Parcelable {
	lateinit var mSalt: String
		private set
	lateinit var hashedPassword: String
		private set

	private constructor(hashedPassword: String) {
		this.hashedPassword = hashedPassword
	}

	private constructor(unhashedPassword: String, salt: String) {
		mSalt = salt
		val rawHashedPassword: ByteArray
		try {
			rawHashedPassword = if (Build.VERSION.SDK_INT >= 20) {
				MessageDigest.getInstance("SHA-512").digest((unhashedPassword + mSalt).toByteArray(StandardCharsets.UTF_8))
			} else {
				MessageDigest.getInstance("SHA-512").digest((unhashedPassword + mSalt).toByteArray(charset("UTF-8")))
			}
		} catch (e: NoSuchAlgorithmException) {
			e.printStackTrace()
			return
		} catch (e: UnsupportedEncodingException) {
			e.printStackTrace()
			return
		}

		val sb = StringBuilder()
		for (b in rawHashedPassword) {
			sb.append(String.format("%02X", b))
		}
		this.hashedPassword = sb.toString().uppercase(Locale.ENGLISH)
	}

	constructor(parcel: Parcel) {
		mSalt = parcel.readString() ?: ""
		hashedPassword = parcel.readString() ?: ""
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(mSalt)
		parcel.writeString(hashedPassword)
	}

	override fun describeContents(): Int {
		return 0
	}

	override fun equals(other: Any?): Boolean {
		return if (other != null && other is Password)
			(this.hashedPassword == other.hashedPassword && this.mSalt == other.mSalt)
		else
			false
	}

	fun isEmpty(): Boolean {
		return (hashedPassword == "" && mSalt == "")
	}

	companion object {
		@JvmField
		val CREATOR : Parcelable.Creator<Password> = object : Parcelable.Creator<Password> {
			override fun createFromParcel(parcel: Parcel): Password {
				return Password(parcel)
			}

			override fun newArray(size: Int): Array<Password?> {
				return arrayOfNulls(size)
			}
		}

		fun hashPassword(unhashedPassword: String, salt: String = "") : Password {
			return Password(unhashedPassword, salt)
		}

		fun saveHashedPassword(hashedPassword: String, salt: String = "") : Password {
			val newPassword = Password(hashedPassword)
			newPassword.mSalt = salt
			return newPassword
		}

		fun emptyPassword() : Password {
			val newPassword = Password("")
			newPassword.mSalt = ""
			return newPassword
		}
	}
}