@file:kotlin.jvm.JvmName("DateTimeUtil")
@file:kotlin.jvm.JvmMultifileClass

package com.bornfight.utils

import android.content.Context
import androidx.annotation.PluralsRes
import org.ocpsoft.prettytime.PrettyTime
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ianic on 12/01/17.
 *
 * Contains utility functions for date String manipulation.
 */

private val pt = PrettyTime()

/**
 * @return Returns year, month & day formatted as "yyyy-MM-dd"
 */
fun formatDate(year: Int, month: Int, day: Int): String {
    return String.format(
        Locale.US, "%d-%02d-%02d",
        year,
        month + 1,
        day
    )
}

/**
 * Parses date string "yyyy-MM-dd" to [Date]
 */
fun parseDate(date: String): Date? {
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return try {
        df.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

/**
 * Uses [SimpleDateFormat.getDateInstance] to format date.
 * @param dateFormat a [DateFormat] format constant. [DateFormat.MEDIUM] by default.
 */
fun getDatePretty(year: Int, month: Int, day: Int, dateFormat: Int = DateFormat.MEDIUM): String {
    val c = Calendar.getInstance()
    c.set(Calendar.YEAR, year)
    c.set(Calendar.MONTH, month)
    c.set(Calendar.DATE, day)
    val df = SimpleDateFormat.getDateInstance(dateFormat)
    return df.format(c.time)
}

fun getDatePretty(calendar: Calendar, dateFormat: Int = DateFormat.MEDIUM): String {
    val df = SimpleDateFormat.getDateInstance(dateFormat)
    return df.format(calendar.time)
}

fun getDatePretty(date: Date, dateFormat: Int = DateFormat.MEDIUM): String {
    val df = SimpleDateFormat.getDateInstance(dateFormat)
    return df.format(date)
}

/**
 * Returns relative date value (1 minute ago, 1 week ago etc.)
 */
fun timeLeftPretty(tillDate: Date, @PluralsRes daysPluralsResId: Int, context: Context): String {
    val calCurr = Calendar.getInstance()
    val day = Calendar.getInstance()
    day.time = tillDate
    if (day.after(calCurr)) {
        var days = day.get(Calendar.DAY_OF_YEAR) - calCurr.get(Calendar.DAY_OF_YEAR)
        if (day.get(Calendar.YEAR) != calCurr.get(Calendar.YEAR)) {
            days += calCurr.getActualMaximum(Calendar.DAY_OF_YEAR)
        }
        return days.toString() + " " + context.resources.getQuantityString(daysPluralsResId, days)
    }
    return pt.format(tillDate)
}
