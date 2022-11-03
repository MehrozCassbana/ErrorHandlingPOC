package com.bykea.task.core.activitystack

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.annotation.Nullable
import com.bykea.task.utils.LoggerUtil
import java.lang.Thread.UncaughtExceptionHandler
import java.lang.ref.WeakReference

const val LAST_ACTIVITY = "Last_activity"

/**
 * We can use this class to log exceptions with data that can actually help us
 * todo we have to figure our who can we improve this
 */
class GlobalExceptionHandler private constructor(val application: Application,
                                                 private var dfltExceptionHandler: UncaughtExceptionHandler? = null) : UncaughtExceptionHandler {

    private var activityRef = WeakReference<Activity?>(null)


    @Nullable
    private var lastActivityFullName: String? = null

    override fun uncaughtException(thread: Thread, ex: Throwable) {

        lastActivityFullName?.let {
            LoggerUtil.debug(LAST_ACTIVITY, it)
        }

        dfltExceptionHandler?.uncaughtException(thread, ex)
    }


    companion object {
        fun init(application: Application) {
            val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
            val handler = GlobalExceptionHandler(application, previousHandler)
            Thread.setDefaultUncaughtExceptionHandler(handler)
        }
    }

    init {

        // If mDfltExceptionHandler is not null, initialization is already done.
        // Don't do it twice to avoid losing the original handler.
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                activityRef = WeakReference(activity)
                lastActivityFullName = activity.javaClass.name
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}