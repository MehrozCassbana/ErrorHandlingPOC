package com.bykea.task.utils.toast

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.widget.Toast

object ToastUtil {

    private const val TAG = "ToastUtil"
    private var initialized = false
    private var sDefaultMode: Mode? = null
    private var sHandler: ToastHandler? = null
    private var sToast: Toast? = null
    private var sDuration = 0
    private var context: Context? = null


    /**
     * Initialize util with the mode from user.
     *
     * @param context The context to use.
     * @param mode    The default display mode tu use. Either[Mode.NORMAL] or [Mode.REPLACEABLE]
     */
    fun initialize(context: Context?, mode: Mode? = Mode.NORMAL) {
        if (initialized) {
            return
        }
        sDefaultMode = mode
        sHandler = ToastHandler(Looper.getMainLooper())
        ToastUtil.context = context
        initialized = true
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId The resource id of the string resource to use.
     */
    fun show(resId: Int) {
        show(context?.getText(resId), false, sDefaultMode)
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId        The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     */
    fun show(resId: Int, durationLong: Boolean) {
        show(context?.getText(resId), durationLong, sDefaultMode)
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId The resource id of the string resource to use.
     * @param mode  The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     */
    fun show(resId: Int, mode: Mode?) {
        show(context?.getText(resId), false, mode)
    }

    /**
     * Show a toast.
     *
     * @param text The text to show.
     * @param mode The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     */
    fun show(text: CharSequence?, mode: Mode?) {
        show(text, false, mode)
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId        resId The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     */
    fun show(resId: Int, durationLong: Boolean, mode: Mode?) {
        show(context?.getText(resId), durationLong, mode)
    }
    /**
     * Show a toast.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     */
    /**
     * Show a toast.
     *
     * @param text The text to show.
     */
    /**
     * Show a toast.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     */
    @SuppressLint("ShowToast")
    @JvmOverloads
    fun show(text: CharSequence?, durationLong: Boolean = false, mode: Mode? = sDefaultMode) {
        val duration = if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        if (mode != Mode.REPLACEABLE) {
            Toast.makeText(context, text, duration).show()
            return
        }

        if (sToast == null || sDuration != duration) {
            sDuration = duration
            sToast = Toast.makeText(context, text, duration)
        } else {
            sToast?.setText(text)
        }
        sToast?.show()
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId The resource id of the string resource to use.
     * @see .show
     */
    fun postShow(resId: Int) {
        val info = ToastInfo()
        info.resId = resId
        info.durationLong = false
        sHandler?.obtainMessage(ToastHandler.MSG_POST_RES_ID, info)?.sendToTarget()
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text The text to show.
     * @see .show
     */
    fun postShow(text: CharSequence?) {
        val info = ToastInfo()
        info.text = text
        info.durationLong = false
        sHandler?.obtainMessage(ToastHandler.MSG_POST_CHAR_SEQUENCE, info)?.sendToTarget()
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId        The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     * @see .show
     */
    fun postShow(resId: Int, durationLong: Boolean) {
        val info = ToastInfo()
        info.resId = resId
        info.durationLong = durationLong
        sHandler?.obtainMessage(ToastHandler.MSG_POST_RES_ID, info)?.sendToTarget()
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     * @see .show
     */
    fun postShow(text: CharSequence?, durationLong: Boolean) {
        val info = ToastInfo()
        info.text = text
        info.durationLong = durationLong
        sHandler?.obtainMessage(ToastHandler.MSG_POST_CHAR_SEQUENCE, info)?.sendToTarget()
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId The resource id of the string resource to use.
     * @param mode  The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     * @see .show
     */
    fun postShow(resId: Int, mode: Mode?) {
        val info = ToastInfo()
        info.resId = resId
        info.mode = mode
        sHandler?.obtainMessage(ToastHandler.MSG_POST_RES_ID, info)?.sendToTarget()
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text The text to show.
     * @param mode The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     * @see .show
     */
    fun postShow(text: CharSequence?, mode: Mode?) {
        val info = ToastInfo()
        info.text = text
        info.mode = mode
        sHandler?.obtainMessage(ToastHandler.MSG_POST_CHAR_SEQUENCE, info)?.sendToTarget()
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId        resId The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     * @see .show
     */
    fun postShow(resId: Int, durationLong: Boolean, mode: Mode?) {
        val info = ToastInfo()
        info.resId = resId
        info.durationLong = durationLong
        info.mode = mode
        sHandler?.obtainMessage(ToastHandler.MSG_POST_RES_ID, info)?.sendToTarget()
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either [Mode.NORMAL] or [Mode.REPLACEABLE]
     * @see .show
     */
    fun postShow(text: CharSequence?, durationLong: Boolean, mode: Mode?) {
        val info = ToastInfo()
        info.text = text
        info.durationLong = durationLong
        info.mode = mode
        sHandler?.obtainMessage(ToastHandler.MSG_POST_CHAR_SEQUENCE, info)?.sendToTarget()
    }

    /**
     * Display mode
     */
    enum class Mode {
        /**
         * Show as a normal toast. This mode could be user-definable.  This is the default.
         */
        NORMAL,

        /**
         * When the toast is shown to the user , the text will be replaced if call the show() method again.  This mode could be user-definable.
         */
        REPLACEABLE
    }
}