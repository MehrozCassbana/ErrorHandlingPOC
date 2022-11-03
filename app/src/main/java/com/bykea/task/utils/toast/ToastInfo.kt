package com.bykea.task.utils.toast

/**
 * A class that represents toast model
 * resId - represents text id from string.xml file
 * text - text provided directly
 * durationLong - duration for toast
 * mode - Should the toast be a normal one or we need to replace it with another message
 */
class ToastInfo {
    @kotlin.jvm.JvmField
    var resId = 0
    @kotlin.jvm.JvmField
    var text: CharSequence? = null
    @kotlin.jvm.JvmField
    var durationLong = false
    @kotlin.jvm.JvmField
    var mode: ToastUtil.Mode? = null
}