package com.bykea.task.utils.date

import android.annotation.SuppressLint
import com.google.gson.internal.bind.util.ISO8601Utils
import com.bykea.task.utils.LoggerUtil
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateUtil {
    private val TAG = DateUtil::class.java.simpleName
    private const val COMPLETE_DATE_PATTERN = "EEE, MMM dd, yyyy"
    private const val YEAR_MONTH_DATE_PATTERN = "yyyy-MM-dd"
    private const val MONTH_DATE_PATTERN = "MMMM d"

    private object TimeMaximum {
        const val SEC = 60
        const val MIN = 60
        const val HOUR = 24
        const val DAY = 30
        const val MONTH = 12
    }

    /**
     * Converting Date in string format to Date object and converting the Current Stamp
     */
    private fun convertToDate(date: String): Date? {
        var parsedate: Date? = null
        val parsePosition = ParsePosition(0)
        try {
            parsedate = ISO8601Utils.parse(date, parsePosition)
            return parsedate
        } catch (e: ParseException) {
            LoggerUtil.debug(TAG.plus(": convertToDate"), e.printStackTrace())
        }
        return parsedate
    }

    /**
     * @return The current date and time in a ISO 8601 compliant format.
     */
    private val currentTimeStamp: String
    get() = ISO8601Utils.format(Date(), true)

    /**
     * This function returns  date in the Mon, Sep 02, 2013 format
     */
    fun formatDate(date: String): String? {
        return try {
            val dateFormat = SimpleDateFormat(COMPLETE_DATE_PATTERN)
            val startDate = convertToDate(date)
            startDate?.let {
                return dateFormat.format(startDate)
            }
        } catch (e: Exception) {
            LoggerUtil.debug(TAG.plus(": formatDate"), e.printStackTrace())
            return ""
        }
    }

    /**
     * This function match the provide date with current date
     *
     * @return true if the provided date is today else false
     */
    fun isDateToday(date: String): Boolean {
        return convertToSimpleDate(date) == convertToSimpleDate(currentTimeStamp)
    }

    /**
     * This function compare the provide date with current date
     *
     * @return true if the provided date is past else false
     */
    fun isPastDate(date: String): Boolean {
        val currentDate = Date()
        val pastDate = convertToDate(date)
        return if (pastDate != null) {
            pastDate < currentDate
        } else false
    }


    /**
     * This function compare the provide date with current date
     *
     * @return true if the provided date is equal to or past the current date
     */
    fun isExpiredDate(date: String): Boolean {
        val currentDate = Date()
        val expireDate = convertToDate(date)
        return if (expireDate != null) {
            currentDate >= expireDate
        } else false
    }

    /**
     * This function compare the provide date with current date
     *
     * @return true if the provided date is due else false
     */
    fun isDueDate(date: String): Boolean {
        val currentDate = Date()
        val dueDate = convertToDate(date)
        return if (dueDate != null) {
            dueDate > currentDate
        } else false
    }

    /**
     * This function returns Simple date in the yyyy-MM-dd format
     */
    fun convertToSimpleDate(date: String): String? {
        return try {
            val dateFormat = SimpleDateFormat(YEAR_MONTH_DATE_PATTERN)
            val startDate = convertToDate(date)
            startDate?.let {
                return dateFormat.format(startDate)
            }
        } catch (e: Exception) {
            LoggerUtil.debug(TAG.plus(": convertToSimpleDate"), e.printStackTrace())
            return ""
        }
    }

    /**
     * Formats a date according to 'MMMM d' format.
     * Example output is 'February 21'.
     *
     * @param millis a point in time in UTC milliseconds
     * @return a string containing the formatted date.
     */
    fun formatMonthDate(millis: Long): String? {
        return try {
            SimpleDateFormat(MONTH_DATE_PATTERN).format(millis)
        } catch (e: IllegalArgumentException) {
            LoggerUtil.debug(TAG.plus(": formatMonthDate"), e.printStackTrace())
            return ""
        }
    }

    /**
     * Calculates the time according to date
     *
     * @return a simplified message that displays time as human understandable string for example : 1 minute ago, minutes ago, time ago
     */
    fun calculateTime(dateString: String, dateFormat: String): String {
        val simpleDateFormat = SimpleDateFormat(dateFormat)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date? = simpleDateFormat.parse(dateString)
        val curTime = System.currentTimeMillis()
        date?.let {
            var msg = ""
            val regTime = date.time
            var diffTime = (curTime - regTime) / 1000
            when {
                diffTime < TimeMaximum.SEC -> {
                    msg = "1 minute ago"
                }
                TimeMaximum.SEC.let { diffTime /= it; diffTime } < TimeMaximum.MIN -> {
                    msg = diffTime.toString() + "minutes ago"
                }
                TimeMaximum.MIN.let { diffTime /= it; diffTime } < TimeMaximum.HOUR -> {
                    msg = diffTime.toString() + "time ago"
                }
                else -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    msg =
                        calendar[Calendar.YEAR].toString() + "." + (calendar[Calendar.MONTH] + 1) + "." + calendar[Calendar.DAY_OF_MONTH] + "  " + (if (calendar[Calendar.HOUR_OF_DAY] < 10) "0" + calendar[Calendar.HOUR_OF_DAY] else calendar[Calendar.HOUR_OF_DAY]) + ":" + if (calendar[Calendar.MINUTE] < 10) "0" + calendar[Calendar.MINUTE] else calendar[Calendar.MINUTE]
                }
            }
            return msg
        }
        return ""
    }
}


