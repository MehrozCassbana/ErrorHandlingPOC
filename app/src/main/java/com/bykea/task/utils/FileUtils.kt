package com.bykea.task.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.bykea.task.core.Application
import java.io.*

fun getRealPathFromURI(
    context: Context,
    contentUri: Uri
): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        cursor?.getString(column_index!!)
    } catch (e: Exception) {
        Log.i("path", "getRealPathFromURI Exception : $e")
        ""
    } finally {
        cursor?.close()
    }
}


/**
 * this method takes in a uri and makes a copy of that uri and stores it to users app folder, it should only be used when absolute necessary
 * @param context
 * @param uri
 * @return
 */
fun getPathFromInputStreamUri(
    context: Context,
    uri: Uri
): String? {
    var inputStream: InputStream? = null
    var filePath: String? = null
    uri.authority?.let {
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            return createTemporalFileFrom(inputStream)?.path
        } catch (ignored: IOException) {
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return null
}


fun createTemporalFileFrom(inputStream: InputStream?): File? {
    var targetFile: File? = null
    inputStream?.let {
        var read: Int
        val buffer = ByteArray(8 * 1024)
        targetFile = getImageFile()
        val outputStream: OutputStream = FileOutputStream(targetFile)
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
        outputStream.flush()
        try {
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return targetFile
}

fun getImageFile(): File {
    return File(
        createImageDirectory() + "/",
        System.currentTimeMillis().toString() + ".jpg"
    )
}

private fun createImageDirectory(): String? {
    val dir = File(
        Application.context.applicationInfo.dataDir + "/Jeeny/images"
    )
    if (!dir.exists()) dir.mkdirs()
    return dir.absolutePath
}
