package com.zima.myxkcdviewer.data.logic.utils

import org.joda.time.MutableDateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

data class CalendarDayFormatter(val day: Int, val month: Int, val myYear: Int) {


    override fun toString(): String {
        val dateWeekday = DateTimeFormat.forPattern("EEEE, ").withLocale(Locale.getDefault())
        val dateMedium = DateTimeFormat.forPattern("d. MMMM YYY").withLocale(Locale.getDefault())
        val comicDate = MutableDateTime.now().apply {
            setDateTime(myYear, month, day, 0, 0, 0, 0)
        }
        return "${comicDate.toString(dateWeekday)} ${comicDate.toString(dateMedium)}"
    }


}