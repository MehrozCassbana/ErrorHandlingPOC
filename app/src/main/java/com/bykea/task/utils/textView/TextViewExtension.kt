package com.bykea.task.utils.textView

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat


fun TextView.underline() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    this.paint.isAntiAlias = true

}

fun TextView.deleteLine() {
    this.paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    this.paint.isAntiAlias = true
}

fun TextView.strike() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.bold(text: CharSequence) {
    val mSpannableString = SpannableString(text)
    mSpannableString.setSpan(StyleSpan(Typeface.BOLD), 0, text.length, 0)
    this.text = mSpannableString
}

/**
 * Extension method to set different color for substring TextView.
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    val spannable = SpannableString(text)
    val start = text.indexOf(substring)
    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, color)),
        start,
        start + substring.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = spannable

}

/**
 * Extension method to set font for TextView.
 */
fun TextView.font(font: String) {
    typeface = Typeface.createFromAsset(context.assets, "fonts/$font.ttf")
}

/**
 * Extension method to set a drawable to the left of a TextView.
 */
fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

operator fun TextView.plusAssign(text: String) {
    this.text = text
}

fun TextView.clear() {
    this.text = ""
}

