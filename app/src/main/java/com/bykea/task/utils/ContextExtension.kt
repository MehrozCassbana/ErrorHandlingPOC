package com.bykea.task.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bykea.task.BuildConfig

/**
 * Extension method to Send SMS for Context.
 */
fun Context.sendSms(number: String, text: String = ""): Boolean {
    return try {
        val intent = Intent(ACTION_VIEW, Uri.parse("sms:$number"))
        intent.putExtra("sms_body", text)
        startActivity(intent)
        true
    } catch (e: Exception) {
        if (BuildConfig.DEBUG)
            LoggerUtil.debug("ContextExtension: sendSms", e.printStackTrace().toString())
        false
    }
}

/**
 * Extension method to make call for Context.
 */
fun Context.makeCall(number: String): Boolean {
    return try {
        val intent = Intent(ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        if (BuildConfig.DEBUG)
            LoggerUtil.debug("ContextExtension: makeCall", e.printStackTrace().toString())
        false
    }
}

/**
 * Extension method to send email for Context.
 */
@SuppressLint("QueryPermissionsNeeded")
fun Context.email(email: String, subject: String = "", text: String = ""): Boolean {
    val intent = Intent(ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(EXTRA_EMAIL, arrayOf(email))
    if (subject.isNotBlank()) intent.putExtra(EXTRA_SUBJECT, subject)
    if (text.isNotBlank()) intent.putExtra(EXTRA_TEXT, text)
    intent.resolveActivity(packageManager)?.let {
        startActivity(intent)
        return true
    }
    return false
}


/**
 * Extension method to share for Context.
 */
fun Context.share(text: String, subject: String = ""): Boolean {
    val intent = Intent()
    intent.type = "text/plain"
    intent.putExtra(EXTRA_SUBJECT, subject)
    intent.putExtra(EXTRA_TEXT, text)
    return try {
        startActivity(createChooser(intent, null))
        true
    } catch (e: ActivityNotFoundException) {
        if (BuildConfig.DEBUG)
            LoggerUtil.debug("ContextExtension: sendSms", e.printStackTrace().toString())
        false
    }
}


/**
 * Extension method to browse for Context.
 */
fun Context.browse(url: String, newTask: Boolean = false): Boolean {
    return try {
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (newTask)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        true
    } catch (e: Exception) {
        if (BuildConfig.DEBUG)
            LoggerUtil.debug("ContextExtension: sendSms", e.printStackTrace().toString())
        false
    }
}


/**
 * Extension method to show notification for Context.
 */
inline fun Context.notification(body: NotificationCompat.Builder.() -> Unit): Notification {
    val builder = NotificationCompat.Builder(this)
    builder.body()
    return builder.build()
}