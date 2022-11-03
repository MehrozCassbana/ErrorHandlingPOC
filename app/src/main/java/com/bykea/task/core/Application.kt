package com.bykea.task.core

import android.app.Application
import android.content.Context
import com.bykea.task.core.activitystack.ActivityStackHandler
import com.bykea.task.core.activitystack.GlobalExceptionHandler
import com.bykea.task.utils.LoggerUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        LoggerUtil.init()
        initUncaughtExceptionHandler()
        initLoggerUtil()
    }

    private fun initLoggerUtil() {
        LoggerUtil.init()
    }

    private fun initUncaughtExceptionHandler() {
        GlobalExceptionHandler.init(this)
        registerActivityLifecycleCallbacks(ActivityStackHandler)
    }

    companion object {
        lateinit var context: Context
    }
}
