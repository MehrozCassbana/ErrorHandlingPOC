package com.bykea.task.utils

import com.bykea.task.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

object LoggerUtil {
    fun debug(s: String?, vararg objects: Any?) {
        Timber.d(s, *objects)
    }

    fun debug(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.d(throwable, s, *objects)
    }

    fun error(s: String?, vararg objects: Any?) {
        Timber.e(s, *objects)
    }

    fun error(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.e(throwable, s, *objects)
    }

    fun info(s: String?, vararg objects: Any?) {
        Timber.i(s, *objects)
    }

    fun info(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.i(throwable, s, *objects)
    }

    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    fun w(s: String?, vararg objects: Any?) {
        Timber.w(s, *objects)
    }

    fun w(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.w(throwable, s, *objects)
    }
}