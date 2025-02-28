package com.goujer.barcodekanojo.core.http

import java.net.URLEncoder

open class NameStringPair(name: String, internal val value: String?): NameValuePair(name) {

	override fun toString(): String {
		return URLEncoder.encode(name, "UTF-8")+'='+URLEncoder.encode(value, "UTF-8")
	}

	override fun valueAsBytes(): ByteArray? {
		return value!!.toByteArray(Charsets.UTF_8)
	}

	override fun valueAsString(): String {
		return value?: ""
	}

	override fun emptyValue(): Boolean {
		return (value == null || value == "")
	}
}