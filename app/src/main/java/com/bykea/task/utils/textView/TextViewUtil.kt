package com.bykea.task.utils.textView

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.bykea.task.R

object TextViewUtil {

    /**
     * A class to defines how we want the "Read More" text to be displayed, either depending of no.of lines or on length
     */
    sealed class ReadMoreType {
        object LINE : ReadMoreType()
        object LENGTH : ReadMoreType()
    }

    /** Function to create a textView to be displayed in a compact manner
     * @param type :  Define the type of read more to be displayed
     * @param view : The TextView to be displayed
     * @param text : The text to be displayed
     * @param max : Max lines or Max length to be showed before Read More label
     * @param expendText : Show More label OR any other label
     * @param expendTextColor : Show More label color
     */
    fun setReadMore(
        type: ReadMoreType, view: TextView, text: String,
        max: Int, expendText: String = view.context.getString(R.string.showMoreLabel),
        expendTextColor: Int = Color.parseColor("#9E9E9E")
    ) {
        when (type) {
            ReadMoreType.LINE -> setReadMoreForLine(
                view,
                text,
                max,
                expendText,
                expendTextColor
            )
            ReadMoreType.LENGTH -> setReadMoreForLength(
                view,
                text,
                max,
                expendText,
                expendTextColor
            )
        }
    }

    private fun setReadMoreForLine(
        view: TextView, text: String, maxLine: Int,
        expendText: String,
        expendTextColor: Int
    ) {
        view.text = text
        view.post {
            if (view.lineCount >= maxLine) {
                val lineEndIndex = view.layout.getLineVisibleEnd(maxLine - 1)

                val split = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var splitLength = 0

                var lessText = ""
                for (item in split) {
                    splitLength += item.length + 1
                    if (splitLength >= lineEndIndex) {
                        lessText += if (item.length >= expendText.length) {
                            item.substring(0, item.length - expendText.length) + expendText
                        } else {
                            item + expendText
                        }
                        break
                    }
                    lessText += item + "\n"
                }
                val spannableString = SpannableString(lessText)
                spannableString.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(vew: View) {
                            view.text = text
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = expendTextColor
                        }
                    },
                    spannableString.length - expendText.length,
                    spannableString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                view.text = spannableString
                view.movementMethod = LinkMovementMethod.getInstance()
            } else view.text = text
        }
    }

    private fun setReadMoreForLength(
        view: TextView, text: String, maxLength: Int,
        expendText: String,
        expendTextColor: Int
    ) {
        view.post {
            if (view.length() > maxLength) {
                val lestText = text.substring(0, maxLength) + expendText
                val spannableString = SpannableString(lestText)
                spannableString.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(vew: View) {
                            view.text = text
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = expendTextColor
                        }
                    },
                    spannableString.length - expendText.length,
                    spannableString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                view.text = spannableString
                view.movementMethod = LinkMovementMethod.getInstance()
            } else view.text = text
        }
    }
}