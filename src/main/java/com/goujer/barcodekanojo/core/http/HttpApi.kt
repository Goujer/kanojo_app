package com.goujer.barcodekanojo.core.http

import android.util.Log
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.core.parser.AbstractJSONParser
import jp.co.cybird.barcodekanojoForGAM.core.parser.JSONParser
import jp.co.cybird.barcodekanojoForGAM.core.util.DiskCache
import java.io.DataOutputStream
import java.net.*

class HttpApi(useHttps: Boolean, mApiBaseUrl: String, private var mApiBasePort: Int, clientVersion: String?, clientLanguage: String?) {
	internal var mApiBaseProtocol: String = if (useHttps) "https" else "http"
		private set
	internal var mApiBaseUrl: String = mApiBaseUrl
		private set
	private val mClientVersion = if (clientVersion != null) clientVersion else DEFAULT_CLIENT_VERSION
	private val mClientLanguage = if (clientLanguage != null) clientLanguage else DEFAULT_CLIENT_LANGUAGE

	init {
		CookieHandler.setDefault(CookieManager())
	}

	//TODO Copied and modified from core.http.HttpApi.executeHttpRequest() which JADX did not decompile correctly.
	fun executeHttpRequest(connection: HttpURLConnection, parser: JSONParser<out BarcodeKanojoModel?>): Response<BarcodeKanojoModel?>? {
		connection.connect()
		val statusCode = connection.responseCode
		when (statusCode) {
			HttpURLConnection.HTTP_OK -> {
				try {
					return parser.parse(AbstractJSONParser.createJSONObject(connection.inputStream)) as Response<BarcodeKanojoModel?>?
				} finally {
					connection.disconnect()
				}
			}
			HttpURLConnection.HTTP_BAD_REQUEST -> {
				val message = connection.responseMessage
				connection.disconnect()
				throw BarcodeKanojoException(message)
			}
			HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_NOT_FOUND -> {
				connection.disconnect()
				throw BarcodeKanojoException(connection.responseMessage)
			}
			HttpURLConnection.HTTP_INTERNAL_ERROR -> {
				connection.disconnect()
				throw BarcodeKanojoException("Server is down. Try again later.")
			}
			else -> {
				connection.disconnect()
				throw BarcodeKanojoException("Error connecting to Server: $statusCode. Try again later.")
			}
		}
	}

	fun executeHttpRequest(connection: HttpURLConnection, cache: DiskCache, key: String) {
		connection.connect()
		val statusCode = connection.responseCode
		when (statusCode) {
			HttpURLConnection.HTTP_OK -> {
				try {
					Log.d(TAG, connection.url.file)
					return cache.store(key, connection.inputStream)
				} finally {
					connection.disconnect()
				}
			}
			HttpURLConnection.HTTP_BAD_REQUEST -> {
				val message = connection.responseMessage
				connection.disconnect()
				throw BarcodeKanojoException(message)
			}
			HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_NOT_FOUND -> {
				connection.disconnect()
				throw BarcodeKanojoException(connection.responseMessage)
			}
			HttpURLConnection.HTTP_INTERNAL_ERROR -> {
				connection.disconnect()
				throw BarcodeKanojoException("Server is down. Try again later.")
			}
			else -> {
				connection.disconnect()
				throw BarcodeKanojoException("Error connecting to Server: $statusCode. Try again later.")
			}
		}
	}

	fun createHttpGet(fileIn: String, vararg nameValuePairs: NameValuePair): HttpURLConnection {
		var file: String = fileIn
		val connection: HttpURLConnection
		if (nameValuePairs.isEmpty()) {
			connection = URL(mApiBaseProtocol, mApiBaseUrl, mApiBasePort, file).openConnection() as HttpURLConnection
		} else {
			val parameters = StringBuilder()
			for (pair in stripNulls(*nameValuePairs)) {
				if (parameters.isNotEmpty()) {
					parameters.append('&')
				} else {
					parameters.append('?')
				}
				parameters.append(pair.toString())
			}
			file += parameters.toString()
			connection = URL(mApiBaseProtocol, mApiBaseUrl, mApiBasePort, file).openConnection() as HttpURLConnection
		}
		connection.doInput = true
		connection.requestMethod = "GET"
		connection.setRequestProperty("Accept-Charset", "utf-8")
		connection.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion)
		connection.setRequestProperty(CLIENT_LANGUAGE_HEADER, mClientLanguage)
		return connection
	}

	fun createHttpPost(file: String, vararg nameValuePairs: NameValuePair): HttpURLConnection {
		val connection = URL(mApiBaseProtocol, mApiBaseUrl, mApiBasePort, file).openConnection() as HttpURLConnection
		connection.doOutput = true
		connection.doInput = true
		connection.requestMethod = "POST"
		connection.setRequestProperty("Accept-Charset", "utf-8")
		connection.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion)
		connection.setRequestProperty(CLIENT_LANGUAGE_HEADER, mClientLanguage)
		val parameters = StringBuilder()
		for (pair:NameValuePair in stripNulls(*nameValuePairs)) {
			if (parameters.isNotEmpty()) parameters.append('&')
			parameters.append(pair.toString())
		}
		connection.outputStream.write(parameters.toString().toByteArray(charset("UTF-8")))
		return connection
	}

	fun createHttpMultipartPost(file: String, vararg nameValuePairs: NameValuePair): HttpURLConnection {
		val connection = URL(mApiBaseProtocol, mApiBaseUrl, mApiBasePort, file).openConnection() as HttpURLConnection
		connection.doOutput = true
		connection.doInput = true
		connection.requestMethod = "POST"
		connection.setRequestProperty("Accept-Charset", "utf-8")
		connection.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion)
		connection.setRequestProperty(CLIENT_LANGUAGE_HEADER, mClientLanguage)
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$BOUNDARY")
		try {
			val request = DataOutputStream(connection.outputStream)
			for (param in stripNulls(*nameValuePairs)) {
				if (param is NameFilePair) {
					request.writeBytes("--$BOUNDARY\r\n")
					request.writeBytes("Content-Disposition: form-data; name=\"" + param.name + "\"; filename=\"image.png\"\r\nContent-Type: application/octet-stream\r\n\r\n")
					request.write(param.valueAsBytes())
					request.writeBytes("\r\n")
				} else {
					request.writeBytes("--$BOUNDARY\r\n")
					request.writeBytes("Content-Disposition: form-data; name=\"" + param.name + "\"\r\n\r\n")
					request.write(param.valueAsBytes())
					request.writeBytes("\r\n")
				}
			}
			request.writeBytes("--$BOUNDARY--\r\n")
			request.flush()
			request.close()
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return connection
	}

	private fun stripNulls(vararg nameValuePairs: NameValuePair): List<NameValuePair> {
		val params: MutableList<NameValuePair> = ArrayList()
		for (param in nameValuePairs) {
			if (!param.emptyValue()) {
				params.add(param)
			}
		}
		return params
	}

	companion object {
		private const val CLIENT_LANGUAGE_HEADER = "Accept-Language"
		private const val CLIENT_VERSION_HEADER = "User-Agent"
		private const val DEFAULT_CLIENT_LANGUAGE = "en"
		private const val DEFAULT_CLIENT_VERSION = "jp.co.cybrid.barcodekanojo"
		private const val BOUNDARY = "0xKhTmLbOuNdArY"
		private const val TAG = "HttpApi"
	}
}