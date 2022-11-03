package com.bykea.task.core.activitystack

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.bykea.task.core.base.BaseActivity
import java.lang.StringBuilder

const val TAG = "TAG_Stack"

object ActivityStackHandler : Application.ActivityLifecycleCallbacks {

    /**
     * at given time the screens that are in memory will be present in this data structure
     */
    private val activityStack = mutableListOf<String>()

    /**
     * checks if its not present inside our stack it gets added here
     */
    fun addIntoStack(activity: Activity) {
        val name = computeScreenName(activity)
        if(!activityStack.contains(name)){
            activityStack.add(name)
        }
    }

    /**
     * if the current activity is instance of base activity it will have a name
     * if its a third part activity we will use java class simple name
     */
    private fun computeScreenName(activity: Activity): String {
        return if (activity is BaseActivity<*>) {
            activity.screenName()
        } else {
            activity.javaClass.simpleName
        }
    }

    /**
     * checks if activity is present inside the stack removes it
     */
    private fun removeFromStack(activity: Activity) {
        val name = computeScreenName(activity)
        if(activityStack.contains(name)){
            activityStack.remove(name)
        }
    }

    /**
     * we can use this method to fetch currently in memory activities
     */
    fun printStack() : String{
        val stack = StringBuilder("")
        activityStack.forEach {
            stack.append(it)
        }
        return stack.toString()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        addIntoStack(activity)
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        removeFromStack(activity)
    }

}