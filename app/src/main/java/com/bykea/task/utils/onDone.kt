package com.bykea.task.utils

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.onDone(callback: () -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_DONE
    maxLines = 1
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            true
        }
        false
    }
}