package com.goujer.utils

fun countOccurrences(s: String, ch: Char): Int {
	return s.filter { it == ch }.count()
}