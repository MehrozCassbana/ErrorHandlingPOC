package com.bykea.task.utils.string

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String.decamelize(): String {
    return replace(upperCaseRegex) {
        val (first) = it.destructured
        "-${first.toLowerCase()}"
    }
}

private val upperCaseRegex = "([A-Z])".toRegex()

private val invalidTaskNameCharacters = "[/\\\\:<>\"?*|]".toRegex()

/**
 * Replaces characters which are not allowed in Gradle task names (/, \, :, <, >, ", ?, *, |) with '_'
 */
fun String.asValidTaskName() = replace(invalidTaskNameCharacters, "_")

private val ANSI_COLOR_REGEX = "\\x1b\\[[0-9;]*m".toRegex()

fun String.clearAnsiColor() =
    replace(ANSI_COLOR_REGEX, "")

/**
 * Extension method to get md5 string.
 */
fun String.md5() = encrypt(this, "MD5")

/**
 * Extension method to check if String is Phone Number.
 */
fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Email.
 */
fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Number.
 */
fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

/**
 * Extension method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())

/**
 * Extension method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

/**
 * Extension method to convert byteArray to String.
 */
private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}