package com.zak.rsisaobatku.util

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
    fun String.withDateFormat(): String {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(this) as Date
        return SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(date)
    }
}