package com.bykea.task.utils

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ValidationUtil @Inject constructor(@ApplicationContext private val context: Context) {

    private fun showToast(message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    private fun isNullOrEmpty(input: String?): Boolean = input == null || input.isEmpty()

    fun isValidUsername(
        context: Context,
        username: String?,
        regex: String = "^[a-zA-Z0-9._-]{3,20}$"
    ): Boolean {
        when {
            isNullOrEmpty(username) -> showToast("Please enter User name first.")
            !Pattern.matches(regex, username) -> showToast(
                "Please enter a valid User name."
            )
            else -> return true
        }
        return false
    }

    fun isValidEmail(context: Context, email: String?): Boolean {
        when {
            isNullOrEmpty(email) -> showToast("Please enter Email first.")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showToast(
                "Please enter a valid Email address."
            )
            else -> return true
        }
        return false
    }

    fun isValidMobile(
        context: Context,
        mobile: String?,
        regex: String = "^[0-9]{10}$"
    ): Boolean {
        when {
            isNullOrEmpty(mobile) -> showToast("Please enter Mobile number first.")
            !Pattern.matches(regex, mobile) -> showToast(
                "Please enter a valid Mobile number."
            )
            else -> return true
        }
        return false
    }

    fun isValidPassword(context: Context, password: String?): Boolean {
        when {
            isNullOrEmpty(password) -> showToast("Please enter Password first.")
            password!!.length < 6 -> showToast(
                "Password length should not be less than 6 characters"
            )
            password.length > 30 -> showToast(
                "Password length should not be greater than 30 characters"
            )
            else -> return true
        }
        return false
    }

    fun validateDigits(testObj: EditText, noOfDigits: Int): Boolean {
        return testObj.text.toString().length == noOfDigits
    }
}