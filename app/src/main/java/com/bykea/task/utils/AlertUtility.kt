package com.bykea.task.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.bykea.task.R


fun showError(context: Context, errorMessage: String) {
    AlertDialog.Builder(context)
        .setTitle(R.string.alert)
        .setCancelable(false)
        .setMessage(errorMessage)
        .setPositiveButton(
            R.string.ok
        ) { dialog, _ -> dialog.dismiss() }
        .create().show()
}





