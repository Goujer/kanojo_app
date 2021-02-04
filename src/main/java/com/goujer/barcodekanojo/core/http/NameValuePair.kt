package com.goujer.barcodekanojo.core.http

abstract class NameValuePair(internal val name: String) {
	abstract override fun toString(): String

	abstract fun valueAsBytes(): ByteArray?

	abstract fun emptyValue(): Boolean
}