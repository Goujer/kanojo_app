package com.goujer.barcodekanojo.core.http

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.StringBuilder
import java.net.URLEncoder

class NameFilePair(name: String, file: File?): NameValuePair(name) {
	val value: ByteArray
	init {
		val output: ArrayList<Byte> = ArrayList();
		if (file != null && file.exists()) {
			val inp: InputStream = FileInputStream(file)
			inp.use { inp ->
				val tmp = ByteArray(4096)
				var l: Int
				while (inp.read(tmp).also { l = it } != -1) {
					for (i in 0 until l) {
						output.add(tmp[i])
					}
				}
			}
		}
		value = output.toByteArray()
	}

	override fun toString(): String {
		val valueString = StringBuilder()
		for (b in value) {
			valueString.append(String.format("%02X", b))
		}
		return URLEncoder.encode(name, "UTF-8")+'='+ valueString.toString()
	}

	override fun valueAsBytes(): ByteArray {
		return value
	}

	override fun emptyValue(): Boolean {
		return value.isEmpty()
	}
}