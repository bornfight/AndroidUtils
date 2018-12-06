package com.bornfight.android.utils

import android.content.Context
import androidx.annotation.PluralsRes
import org.ocpsoft.prettytime.PrettyTime
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ianic on 12/01/17.
 */

object DateTimeUtil {

    //FIXME:
    private val df = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM)
    private val pt = PrettyTime()

    fun getDate(year: Int, month: Int, day: Int): String {
        return String.format(Locale.US, "%d-%02d-%02d",
            year,
            month + 1,
            day)
    }

    fun getDatePretty(year: Int, month: Int, day: Int): String {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DATE, day)
        return df.format(c.time)
    }

    fun getDatePretty(calendar: Calendar): String {
        return df.format(calendar.time)
    }

    fun getDatePretty(date: Date): String {
        return df.format(date)
    }

    fun parseDate(date: String): Date? {
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        try {
            return df.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }

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


}
