package com.bykea.task.core.base

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher

/**
 * MyService String: Used to name the worker thread, important only for debugging.
 */

open class BaseIntentService : IntentService("MyService"), LifecycleOwner {

    private var mDispatcher: ServiceLifecycleDispatcher = ServiceLifecycleDispatcher(this)


    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }

    override fun onCreate() {
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()

    }

    override fun onBind(intent: Intent): IBinder? {
        mDispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }

    override fun onHandleIntent(intent: Intent?) {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mDispatcher.onServicePreSuperOnStart()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }
}