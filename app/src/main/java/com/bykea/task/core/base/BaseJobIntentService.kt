package com.bykea.task.core.base

import androidx.core.app.JobIntentService
import android.content.Intent
import android.util.Log

abstract class BaseJobIntentService : JobIntentService() {

    private val TAG = "BaseJobIntentService"

    override fun onCreate() {
        super.onCreate()
        onServiceCreate()
        Log.d(TAG, "onCreate")
    }


    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork")
        onHandleWork()
    }

    override fun onDestroy() {
        super.onDestroy()
        onServiceDestroyed()
        Log.d(TAG, "onDestroy")
    }

    override fun onStopCurrentWork(): Boolean {
        Log.d(TAG, "onStopCurrentWork")
        stopCurrentWork()
        return super.onStopCurrentWork()
    }


    abstract fun stopCurrentWork()
    abstract fun onHandleWork()
    abstract fun onServiceCreate()
    abstract fun onServiceDestroyed()

}