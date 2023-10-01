package com.goujer.utils


import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

fun getPartOfDay(): Int {
	val calendar = GregorianCalendar.getInstance()
	calendar.time = Date()
	val hour = calendar.get(Calendar.HOUR_OF_DAY)
	return (((hour + 2) % 24 ) / 6)     // 0 - night, 1 - morning, 2 - day, 3 - evening
}