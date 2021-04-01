package com.goujer.utils

import java.net.URLEncoder

//Count how many times ch is in s
fun countOccurrences(s: String, ch: Char): Int {
	return s.filter { it == ch }.count()
}

//String that contains hex to byte array
fun decodeHexString(hexString: String): ByteArray {
	require(hexString.length % 2 != 1) { "Invalid hexadecimal String supplied." }
	val bytes = ByteArray(hexString.length / 2)
	var i = 0
	while (i < hexString.length) {
		bytes[i / 2] = hexToByte(hexString.substring(i, i + 2))
		i += 2
	}
	return bytes
}

//String that contains two hex values to byte
private fun hexToByte(hexString: String): Byte {
	val firstDigit = toDigit(hexString[0])
	val secondDigit = toDigit(hexString[1])
	return ((firstDigit shl 4) + secondDigit).toByte()
}

//Character in hex to Int
private fun toDigit(hexChar: Char): Int {
	val digit = Character.digit(hexChar, 16)
	require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
	return digit
}

//Encode String for use in a URL
fun encodeForUrl(input: String): String {
	return URLEncoder.encode(input, "UTF-8").replace("+", "%20").replace("%2F", "/")
}