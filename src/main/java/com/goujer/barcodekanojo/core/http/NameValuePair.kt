package com.goujer.barcodekanojo.core.http

import java.net.URLEncoder

open class NameValuePair(internal val name: String, internal val value: String?) {

	fun encode(): String {
		return URLEncoder.encode(name, "UTF-8")+'='+URLEncoder.encode(value, "UTF-8")
	}
}