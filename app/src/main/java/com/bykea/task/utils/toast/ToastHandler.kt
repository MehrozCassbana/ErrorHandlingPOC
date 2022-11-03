package com.bykea.task.utils.toast

import android.os.Handler
import android.os.Looper
import android.os.Message

/** A handler class used to handle the toast message from a Runnable class ( To manage toast on background thread )
 *  Checks if string is from resID or chars passed directly
 * */
class ToastHandler(mainLooper: Looper) : Handler(mainLooper) {
    override fun handleMessage(msg: Message) {
        val info = msg.obj as ToastInfo
        when (msg.what) {
            MSG_POST_RES_ID -> ToastUtil.show(info.resId, info.durationLong, info.mode)
            MSG_POST_CHAR_SEQUENCE -> ToastUtil.show(info.text, info.durationLong, info.mode)
        }
    }

    companion object {
        const val MSG_POST_RES_ID = 1
        const val MSG_POST_CHAR_SEQUENCE = 2
    }
}