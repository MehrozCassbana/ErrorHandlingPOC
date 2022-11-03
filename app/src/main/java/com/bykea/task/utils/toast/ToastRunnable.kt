package com.bykea.task.utils.toast

import com.bykea.task.utils.toast.ToastUtil.postShow

/** A class that throws a string message to a thread class( In this case our ToastHandler ) to handle message on background thread
 * @param message - The string message to show
 * @param mMode -  Represents toast mode either Normal OR Replaceable
 */
class ToastRunnable(
    private val message: String,
    private val mMode: ToastUtil.Mode
) : Runnable {
    override fun run() {
        postShow(message, mMode)
    }
}