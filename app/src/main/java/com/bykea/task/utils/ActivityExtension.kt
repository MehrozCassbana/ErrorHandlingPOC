package com.bykea.task.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


/**
 * Extension method to set Status Bar Color and Status Bar Icon Color Type(dark/light)
 */
enum class StatusIconColorType {
    Dark, Light
}

fun Activity.setStatusBarColor(
    color: Int,
    iconColorType: StatusIconColorType = StatusIconColorType.Light
) {
    this.window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        statusBarColor = color
        decorView.systemUiVisibility = when (iconColorType) {
            StatusIconColorType.Dark -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            StatusIconColorType.Light -> 0
        }
    }
}

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
@JvmOverloads
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int, shouldAddToStack: Boolean = true) {
    val add = supportFragmentManager.beginTransaction().replace(frameId, fragment)
    if(shouldAddToStack){
        add.addToBackStack(fragment.tag)
    }
    add.commit()
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.beginTransaction().add(fragment, tag).addToBackStack(fragment.tag)
        .commit()
}

/**
 * Setup actionbar
 */
fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

/**
 * Extension method to get ContentView for ViewGroup.
 */
fun Activity.getContentView(): ViewGroup {
    return this.findViewById(android.R.id.content) as ViewGroup
}


/**
 * Extension method for opening a new activity/screen.
 */
fun <T : AppCompatActivity> AppCompatActivity.openActivity(
    clazz: Class<T>,
    isFinishCurrentActivity: Boolean = true,
    context: Context
) {
    startActivity(Intent(context, clazz))
    if (isFinishCurrentActivity) finish()
}


/**
 * Extension method for opening a new activity by clearing the current task in stack.
 */
fun <T : AppCompatActivity> AppCompatActivity.openActivityWithClearTask(
    clazz: Class<T>,
    isFinishCurrentActivity: Boolean = true,
    context: Context
) {
    val i = Intent(context, clazz)
    i.flags =
        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(i)
    if (isFinishCurrentActivity) finish()
}

/**
 * Extension method for performing fragment transaction
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}


/**
 * Extension method for getting in/out animations for fragment
 */
@AnimRes
fun AppCompatActivity.getFadeInOutAnimation() =
    arrayOf(
        android.R.anim.fade_in,
        android.R.anim.fade_out,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    )


/**
 * Extension method for adding a fragment to stack
 */
fun AppCompatActivity.addFragment(
    containerId: Int,
    fragment: Fragment,
    addToStack: Boolean = true, @AnimRes animRes: Array<Int>? = null
) =
    supportFragmentManager.transact {
        if (animRes != null)
            if (animRes.size == 2)
                setCustomAnimations(animRes[0], animRes[1])
            else
                setCustomAnimations(animRes[0], animRes[1], animRes[2], animRes[3])
        add(containerId, fragment, fragment::class.java.name)
        if (addToStack)
            addToBackStack(fragment::class.java.name)
    }

/**
 * Extension method for replace a fragment with another
 */
fun AppCompatActivity.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    addToStack: Boolean = false, @AnimRes animRes: Array<Int>? = null
) = supportFragmentManager.transact {
    if (animRes != null)
        if (animRes.size == 2)
            setCustomAnimations(animRes[0], animRes[1])
        else
            setCustomAnimations(animRes[0], animRes[1], animRes[2], animRes[3])
    replace(containerId, fragment, fragment::class.java.name)
    if (addToStack)
        addToBackStack(fragment::class.java.name)
}

/**
 * Extension method that removes last fragment entry from stack
 */
fun AppCompatActivity.popStack() = supportFragmentManager.popBackStack()

/**
 * Extension method that clears backStack till the fragment specified
 */
fun AppCompatActivity.popStack(tagName: String) =
    supportFragmentManager.popBackStack(tagName, FragmentManager.POP_BACK_STACK_INCLUSIVE)

/**
 * Extension method that clears all the stack of fragments
 */
fun AppCompatActivity.popAllStack() =
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

/**
 * Extension method that checks if fragment stack exists
 */
fun AppCompatActivity.isStack() = supportFragmentManager.backStackEntryCount > 0

