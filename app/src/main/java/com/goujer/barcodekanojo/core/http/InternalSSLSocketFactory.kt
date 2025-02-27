package com.goujer.barcodekanojo.core.http

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

//Credit goes here: https://gist.github.com/Karewan/4b0270755e7053b471fdca4419467216

class InternalSSLSocketFactory(private val mSSLSocketFactory: SSLSocketFactory) : SSLSocketFactory() {
	override fun getDefaultCipherSuites(): Array<String> {
		return mSSLSocketFactory.defaultCipherSuites
	}

	override fun getSupportedCipherSuites(): Array<String> {
		return mSSLSocketFactory.supportedCipherSuites
	}

	@Throws(IOException::class)
	override fun createSocket(): Socket {
		return enableTLSOnSocket(mSSLSocketFactory.createSocket())
	}

	@Throws(IOException::class)
	override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
		return enableTLSOnSocket(mSSLSocketFactory.createSocket(s, host, port, autoClose))
	}

	@Throws(IOException::class, UnknownHostException::class)
	override fun createSocket(host: String, port: Int): Socket {
		return enableTLSOnSocket(mSSLSocketFactory.createSocket(host, port))
	}

	@Throws(IOException::class, UnknownHostException::class)
	override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket {
		return enableTLSOnSocket(mSSLSocketFactory.createSocket(host, port, localHost, localPort))
	}

	@Throws(IOException::class)
	override fun createSocket(host: InetAddress, port: Int): Socket {
		return enableTLSOnSocket(mSSLSocketFactory.createSocket(host, port))
	}

	@Throws(IOException::class)
	override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket {
		return enableTLSOnSocket(mSSLSocketFactory.createSocket(address, port, localAddress, localPort))
	}

	private fun enableTLSOnSocket(socket: Socket): Socket {
		//if(socket instanceof SSLSocket) ((SSLSocket) socket).setEnabledProtocols(new String[] {"TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"});
		if (socket is SSLSocket) socket.enabledProtocols = arrayOf("TLSv1.2", "TLSv1.3")
		return socket
	}
}