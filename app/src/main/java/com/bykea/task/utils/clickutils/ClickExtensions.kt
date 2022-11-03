package com.bykea.task.utils.clickutils

import android.view.View

fun View.clicks(onSafeClick: (View) -> Unit) {
    val safeClickListener = SingleShotListner {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

