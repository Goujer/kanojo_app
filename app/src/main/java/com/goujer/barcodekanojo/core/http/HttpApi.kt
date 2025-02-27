package com.goujer.barcodekanojo.core.http

import com.goujer.barcodekanojo.core.cache.ImageDiskCache
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel
import jp.co.cybird.barcodekanojoForGAM.core.model.Response
import jp.co.cybird.barcodekanojoForGAM.core.parser.AbstractJSONParser
import jp.co.cybird.barcodekanojoForGAM.core.parser.JSONParser
import jp.co.cybird.barcodekanojoForGAM.core.parser.ResponseParser
import okhttp3.ConnectionSpec
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.conscrypt.Conscrypt
import java.io.DataOutputStream
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpURLConnection
import java.net.URL
import java.security.Security
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.TrustManager

class HttpApi private constructor(useHttps: Boolean, apiBaseUrl: String, apiBasePort: Int?, clientVersion: String?, clientLanguage: String?) {
	//val client: OkHttpClient

	internal var mApiBaseProtocol: String = if (useHttps) "https" else "http"
	var mApiBaseUrl: String = apiBaseUrl
		internal set
	internal var mApiBasePort: Int = if (apiBasePort != null && apiBasePort > 0) {
		apiBasePort
	} else if (useHttps) {
		443
	} else {
		80
	}
	private val mClientVersion = clientVersion ?: DEFAULT_CLIENT_VERSION
	private var mClientLanguage = clientLanguage ?: DEFAULT_CLIENT_LANGUAGE

	init {
		CookieHandler.setDefault(CookieManager())

		val sslContext = SSLContext.getInstance("TLSv1.2")
		sslContext.init(null, null, null)
		val engine = sslContext.createSSLEngine()

//		val clientBuilder = OkHttpClient.Builder()
//				.readTimeout(10, TimeUnit.SECONDS)
//				.writeTimeout(10, TimeUnit.SECONDS)
//				.connectionSpecs(Collections.singletonList(ConnectionSpec.RESTRICTED_TLS))

		//Conscrypt install
//		val conscrypt = Conscrypt.newProvider()
//
//		Security.insertProviderAt(conscrypt, 1)
//
//		try {
//			val tm = Conscrypt.getDefaultX509TrustManager()
//			val sslContext = SSLContext.getInstance("TLS", conscrypt)
//			sslContext.init(null, arrayOf<TrustManager>(tm), null)
//			clientBuilder.sslSocketFactory(InternalSSLSocketFactory(sslContext.socketFactory), tm)
//		} catch (e: Exception) {
//			e.printStackTrace()
//		}
//
//		client = clientBuilder.build()
	}

	//TODO Copied and modified from core.http.HttpApi.executeHttpRequest() which JADX did not decompile correctly.
	fun executeHttpRequest(connection: HttpURLConnection, parser: ResponseParser): Response<BarcodeKanojoModel?> {
		connection.connect()
		when (val statusCode = connection.responseCode) {
			HttpURLConnection.HTTP_OK -> {
				try {
					return parser.parse(AbstractJSONParser.createJSONObject(connection.inputStream)) as Response<BarcodeKanojoModel?>
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

	//fun executeOKHttpRequest(request: Request, parser: ResponseParser): Response<BarcodeKanojoModel?> {
	//	val response = client.newCall(request).execute()
	//	when (val statusCode = response.code()) {
	//		Response.CODE_SUCCESS -> {
	//			try {
	//				return parser.parse(AbstractJSONParser.createJSONObject(connection.inputStream)) as Response<BarcodeKanojoModel?>
	//			} finally {
	//				connection.disconnect()
	//			}
	//		}
	//		Response.CODE_ERROR_BAD_REQUEST -> {
	//			val message = connection.responseMessage
	//			connection.disconnect()
	//			throw BarcodeKanojoException(message)
	//		}
	//		Response.CODE_ERROR_UNAUTHORIZED, Response.CODE_ERROR_NOT_FOUND -> {
	//			connection.disconnect()
	//			throw BarcodeKanojoException(connection.responseMessage)
	//		}
	//		Response.CODE_ERROR_SERVER -> {
	//			connection.disconnect()
	//			throw BarcodeKanojoException("Server is down. Try again later.")
	//		}
	//		else -> {
	//			connection.disconnect()
	//			throw BarcodeKanojoException("Error connecting to Server: $statusCode. Try again later.")
	//		}
	//	}
	//}

	fun executeHttpRequest(connection: HttpURLConnection, cache: ImageDiskCache, key: String) {
		connection.connect()
		val statusCode = connection.responseCode
		when (statusCode) {
			HttpURLConnection.HTTP_OK -> {
				try {
					return cache.put(key, connection.inputStream)
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

	//fun createOkHttpGet(fileIn: String, vararg nameValuePairs: NameValuePair): Request {
	//	val urlBuilder = HttpUrl.Builder()
	//			.scheme(mApiBaseProtocol)
	//			.host(mApiBaseUrl)
	//			.port(mApiBasePort)
	//			.addPathSegment(fileIn)
//
	//	for (pair in stripNulls(*nameValuePairs)) {
	//		urlBuilder.addQueryParameter(pair.name, pair.valueAsString())
	//	}
//
//
//
//
	//	//Old System
	//	var file: String = fileIn
	//	val connection: HttpURLConnection
	//	if (nameValuePairs.isEmpty()) {
	//		connection = URL(mApiBaseProtocol, mApiBaseUrl, mApiBasePort, file).openConnection() as HttpURLConnection
	//	} else {
	//		val parameters = StringBuilder()
	//		for (pair in stripNulls(*nameValuePairs)) {
	//			if (parameters.isNotEmpty()) {
	//				parameters.append('&')
	//			} else {
	//				parameters.append('?')
	//			}
	//			parameters.append(pair.toString())
	//		}
	//		file += parameters.toString()
	//		connection = URL(mApiBaseProtocol, mApiBaseUrl, mApiBasePort, file).openConnection() as HttpURLConnection
	//	}
	//	connection.doInput = true
	//	connection.requestMethod = "GET"
	//	connection.setRequestProperty("Accept-Charset", "utf-8")
	//	connection.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion)
	//	connection.setRequestProperty(CLIENT_LANGUAGE_HEADER, mClientLanguage)
	//	return connection
	//}

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
		try {
			connection.outputStream.write(parameters.toString().toByteArray(charset("UTF-8")))
		} catch (e: SSLHandshakeException) {
			e.printStackTrace()
			throw BarcodeKanojoException("Error with connection")    //TODO Ensure this gets delivered right anc consider making this a proper string.
		}

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
			request.use {
				for (param in stripNulls(*nameValuePairs)) {
					if (param is NameFilePair) {
						it.writeBytes("--$BOUNDARY\r\n")
						it.writeBytes("Content-Disposition: form-data; name=\"" + param.name + "\"; filename=\"image.png\"\r\nContent-Type: application/octet-stream\r\n\r\n")
						it.write(param.valueAsBytes())
						it.writeBytes("\r\n")
					} else {
						it.writeBytes("--$BOUNDARY\r\n")
						it.writeBytes("Content-Disposition: form-data; name=\"" + param.name + "\"\r\n\r\n")
						it.write(param.valueAsBytes())
						it.writeBytes("\r\n")
					}
				}
				it.writeBytes("--$BOUNDARY--\r\n")
				it.flush()
			}
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

		private lateinit var mHttpApi: HttpApi

		fun get(useHttps: Boolean? = null, apiBaseUrl: String? = null, apiBasePort: Int? = null, clientVersion: String? = null, clientLanguage: String? = null): HttpApi {
			if (!this::mHttpApi.isInitialized && useHttps != null && apiBaseUrl != null) {
				mHttpApi = HttpApi(useHttps, apiBaseUrl, apiBasePort, clientVersion, clientLanguage)
				return mHttpApi
			} else {
				mHttpApi.mApiBaseProtocol = if (useHttps != null) {
					if (useHttps) "https" else "http"
				} else {
					mHttpApi.mApiBaseProtocol
				}
				mHttpApi.mApiBaseUrl = apiBaseUrl ?: mHttpApi.mApiBaseUrl
				mHttpApi.mApiBasePort = if (apiBasePort != null && apiBasePort > 0) {
					apiBasePort
				} else if (useHttps != null) {
					if (useHttps) 443 else 80
				} else {
					mHttpApi.mApiBasePort
				}
				mHttpApi.mClientLanguage = clientLanguage ?: mHttpApi.mClientLanguage
			}
			return mHttpApi
		}

		fun get(): HttpApi {
			return if (this::mHttpApi.isInitialized) {
				mHttpApi
			} else {
				throw BarcodeKanojoException("HttpApi is not initialized.")
			}
		}
	}
}