package com.bykea.task.core.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.CallSuper
import androidx.annotation.Nullable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher

abstract class BaseService <DB : ViewDataBinding> : Service(), LifecycleOwner {

    private val mDispatcher = ServiceLifecycleDispatcher(this)


    @CallSuper
    override fun onCreate() {
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    @CallSuper
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        mDispatcher.onServicePreSuperOnBind()
        return null
    }

    override fun getLifecycle() = mDispatcher.lifecycle



}