package com.bykea.task.utils.imagepicker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.bykea.task.R
import com.bykea.task.core.Application.Companion.context
import com.bykea.task.utils.getPathFromInputStreamUri
import com.bykea.task.utils.getRealPathFromURI
import com.bykea.task.utils.showToast
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class ImagePicker(
    private val activity: FragmentActivity?,
    private val imageFileCallback: (File) -> Unit,
    private val startActivity: (Intent, Int) -> Unit
) {

    private val permissions: RxPermissions = RxPermissions(activity!!)

    fun pickImage(): Disposable {
        return permissions
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .subscribe { granted ->
                    if (granted) {
                        openGallery()
                    } else {
                        // Denied permission with ask never again
                        Toast.makeText(
                                activity,
                                context.getString(R.string.need_perm),
                                Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts(
                                        "package",
                                        activity?.packageName,
                                        null
                                )
                        )
                        startActivity.invoke(intent, PERMISSION_REQUEST_CODE)
                    }
                }
    }

    private fun openCameraToTakePicture() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        takePicture.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivity.invoke(takePicture, CAMERA_REQUEST_CODE)
    }


    private fun openGallery() {
        val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        pickPhoto.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        pickPhoto.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivity.invoke(pickPhoto, REQUEST_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CODE -> {
                activity?.let { context ->

                    data?.let { intentData: Intent ->
                        intentData.data?.let { data ->
                            if (context.contentResolver.getType(data)?.toLowerCase(Locale.getDefault())
                                            ?.contains("image") == true
                            ) {
                                val string = getRealPathFromURI(context, data)
                                string?.let {
                                    //this case only occurs when user is trying to save a photo which is not in his own device its either on google photos or other places
                                    imageFileCallback.invoke(File(it))

                                } ?: run {
                                    val path = getPathFromInputStreamUri(context, data)
                                    path?.let {
                                        imageFileCallback.invoke(File(it))

                                    }
                                }
                            } else {
                                context.showToast(context.getString(R.string.pick_an_image))
                            }
                        }
                    } ?: run {
                        context.showToast(context.getString(R.string.pick_an_image))
                    }
                }
            }

            CAMERA_REQUEST_CODE -> {
                data?.let { intentData : Intent ->
                    intentData.extras?.let { it ->
                        val photo = it["data"] as Bitmap
                        val tempUri: Uri = getImageUri(photo)
                        getRealPathFromURI(activity as Context, tempUri)?.let {
                            imageFileCallback.invoke(File(it))
                        }
                    }
                }
            }

            PERMISSION_REQUEST_CODE -> {
                activity?.let {
                    if (ActivityCompat.checkSelfPermission(
                                    it,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        openGallery()
                    }
                }

            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
                activity?.contentResolver,
                inImage,
                "Title",
                null
        )
        return Uri.parse(path)
    }

    fun openCamera() : Disposable{
        return permissions
                .request(
                        Manifest.permission.CAMERA
                )
                .subscribe { granted ->
                    if (granted) {
                        openCameraToTakePicture()
                    } else {
                        // Denied permission with ask never again
                        activity?.showToast(activity.getString(R.string.need_camera))
                        val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts(
                                        "package",
                                        activity?.packageName,
                                        null
                                )
                        )
                        startActivity.invoke(intent, PERMISSION_REQUEST_CODE)
                    }
                }
    }

    companion object {
        const val REQUEST_CODE = 1221
        const val CAMERA_REQUEST_CODE = 2112
        const val PERMISSION_REQUEST_CODE = 2121
    }
}