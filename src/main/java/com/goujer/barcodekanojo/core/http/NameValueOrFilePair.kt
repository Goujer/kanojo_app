package com.goujer.barcodekanojo.core.http

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.StringBuilder
import java.net.URLEncoder

class NameValueOrFilePair: NameValuePair {
	internal var file: File?
		private set

	constructor(name: String, file: File?): super(name, null) {
		this.file = file
	}

	constructor(name: String, value: String?): super(name, value) {
		file = null
	}
}