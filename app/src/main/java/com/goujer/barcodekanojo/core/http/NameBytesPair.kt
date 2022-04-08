package com.goujer.barcodekanojo.core.http

import java.net.URLEncoder

class NameBytesPair(name: String, val value: ByteArray?): NameValuePair(name) {
	override fun toString(): String {
		val valueString = StringBuilder()
		if (value != null) {
			for (b in value) {
				valueString.append(String.format("%02X", b))
			}
		}
		return URLEncoder.encode(name, "UTF-8")+'='+ valueString.toString()
	}

	override fun valueAsBytes(): ByteArray {
		val valueString = StringBuilder()
		if (value != null) {
			for (b in value) {
				valueString.append(String.format("%02X", b))
			}
		}
		return valueString.toString().toByteArray(Charsets.UTF_8)
	}

	override fun emptyValue(): Boolean {
		return (value == null || value.isEmpty())
	}
}