package com.goujer.utils


import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

fun getPartOfDay(): Int {
	val calendar = GregorianCalendar.getInstance()
	calendar.time = Date()
	val hour = calendar.get(Calendar.HOUR_OF_DAY)
	return (((hour + 3) % 24 ) / 6)     // 0 - night(9PM-3AM), 1 - morning(3AM-9AM), 2 - day(9AM-3PM), 3 - evening(3PM-9PM)
}

//Don't worry it is floored at the end, not rounded