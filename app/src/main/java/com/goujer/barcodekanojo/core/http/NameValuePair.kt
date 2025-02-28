package com.goujer.barcodekanojo.core.http

abstract class NameValuePair(val name: String) {

	abstract override fun toString(): String

	abstract fun valueAsBytes(): ByteArray?

	abstract fun valueAsString():  String

	abstract fun emptyValue(): Boolean
}