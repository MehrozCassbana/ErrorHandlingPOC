package com.bykea.task.utils

import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bykea.task.R


/**
 * Extension method to replace the current Fragment with another.
 */
fun FragmentManager.replaceCurrentFragment(fragment: Fragment) {
    this.beginTransaction().replace(R.id.content, fragment)
        .commit()
}






/**
 * Extension method to display notification text for Fragment.
 */
fun Fragment.notification(body: NotificationCompat.Builder.() -> Unit) =
    activity?.notification(body)


/**
 * Extension method to share text for Fragment.
 */
fun Fragment.share(text: String, subject: String = "") = activity?.share(text, subject)


/**
 * Extension method to browse url text for Fragment.
 */
fun Fragment.browse(url: String, newTask: Boolean = false) = activity?.browse(url, newTask)


/**
 * Extension method to send email for Fragment.
 */
fun Fragment.email(email: String, subject: String = "", text: String = "") =
    activity?.email(email, subject, text)

/**
 * Extension method to make call for Fragment.
 */
fun Fragment.makeCall(number: String) = activity?.makeCall(number)


/**
 * Extension method to send sms for Fragment.
 */
fun Fragment.sendSms(number: String, text: String = "") = activity?.sendSms(number, text)


/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}




