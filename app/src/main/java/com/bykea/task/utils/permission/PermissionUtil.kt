import com.bykea.task.utils.permission.PermissionCallback


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.*


object PermissionUtil {

    private val TAG = PermissionUtil::class.java.simpleName
    private const val REQUEST_CODE: Int = 2020
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var permissions: Array<String>? = null
    private var mPermissionCallback: PermissionCallback? = null
    private var showRational: Boolean = false


    fun init(context: Context) {
        if (context is Activity)
            activity = context
        else if (context is Fragment)
            fragment = context
        permissions = emptyArray()
    }

    fun permissions() = permissions ?: arrayOf()

    private fun checkIfPermissionPresentInAndroidManifest() {
        permissions?.find { !hasPermissionInManifest(permission = it) }?.let {
            throw RuntimeException("Permission ($it) Not Declared in manifest")
        }
    }


    fun request(permissionCallback: PermissionCallback?) {
        if (permissions.isNullOrEmpty()) {
            return
        }
        mPermissionCallback = permissionCallback

        if (!hasPermission()) {
            showRational = permissions?.let { shouldShowRational(it) } == true
            activity?.let {
                permissions?.let { filterNotGrantedPermission(it) }?.let {
                    ActivityCompat.requestPermissions(
                            activity!!,
                            it, REQUEST_CODE
                    )
                }
            } ?: run {
                permissions?.let { filterNotGrantedPermission(it) }?.let {
                    fragment?.requestPermissions(
                            it,
                            REQUEST_CODE
                    )
                }
            }
        } else {
            mPermissionCallback?.onPermissionGranted()
            requestAllCallback()
        }
    }

    private var requestAllCallback: () -> Unit? = fun() {};
    fun requestAll(callback: () -> Unit) {
        requestAllCallback = callback
        request(null)
    }

    private var requestIndividualCallback: (grantedPermission: Array<String>) -> Unit? =
            fun(grantedPermission: Array<String>) {}

    fun requestIndividual(callback: (grantedPermission: Array<String>) -> Unit) {
        requestIndividualCallback = callback
        request(null)
    }

    private var deniedCallback: (isSystem: Boolean) -> Unit? = fun(isSystem: Boolean) {}
    fun denied(callback: (isSystem: Boolean) -> Unit) {
        deniedCallback = callback
    }

    fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE) {
            var denied = false
            val grantedPermissions = ArrayList<String>()
            val deniedPermissions = ArrayList<String>()
            for ((i, grantResult) in grantResults.withIndex()) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    denied = true
                    deniedPermissions.add(permissions[i])
                } else {
                    grantedPermissions.add(permissions[i])
                }
            }

            if (denied) {
                val currentShowRational = shouldShowRational(permissions)
                if (!showRational && !currentShowRational) {
                    mPermissionCallback?.onPermissionDeniedBySystem()
                    deniedCallback(true)
                } else {
                    //Checking if any single individual permission is granted then show user that permission
                    if (grantedPermissions.isNotEmpty()) {
                        mPermissionCallback?.onIndividualPermissionGranted(grantedPermissions.toTypedArray())
                        requestIndividualCallback(grantedPermissions.toTypedArray())
                    }
                    mPermissionCallback?.onPermissionDenied(deniedPermissions.toTypedArray())
                    deniedCallback(false)
                }
            } else {
                Log.i(TAG, "PERMISSION: Permission Granted")
                mPermissionCallback?.onPermissionGranted()
                requestAllCallback()
            }
        }
    }


    private fun <T : Context> getContext(): T {
        return if (activity != null) activity as T else fragment?.context as T
    }

    /**
     * Return list that is not granted and we need to ask for permission
     *
     * @param permissions
     * @return
     */
    private fun filterNotGrantedPermission(permissions: Array<String>): Array<String> {
        return permissions.filter {
            ContextCompat.checkSelfPermission(
                    getContext(),
                    it
            ) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    }

    /**
     * Check permission is there or not
     *
     * @param permissions
     * @return
     */
    fun hasPermission(): Boolean {

        return permissions?.all {
            ContextCompat.checkSelfPermission(getContext(), it) == PackageManager.PERMISSION_GRANTED
        }?:false

    }

    /**
     * Check permission is there or not for group of permissions
     *
     * @param permissions
     * @return
     */
    fun checkSelfPermission(permissions: Array<String>?): Boolean {
        permissions?.let { permissionList ->
            for (permission in permissionList) {
                if (ContextCompat.checkSelfPermission(
                                getContext(),
                                permission
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


    /**
     * Checking if there is need to show rational for group of permissions
     *
     * @param permissions
     * @return
     */
    private fun shouldShowRational(permissions: Array<String>): Boolean {
        var currentShowRational = false
        for (permission in permissions) {
            if (activity != null) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                    currentShowRational = true
                    break
                }
            } else {
                if (fragment?.shouldShowRequestPermissionRationale(permission) == true) {
                    currentShowRational = true
                    break
                }
            }
        }
        return currentShowRational
    }


    private fun hasPermissionInManifest(permission: String): Boolean {
        try {
            val context = activity ?: fragment?.activity
            val info = context?.packageManager?.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
            )
            if (info?.requestedPermissions != null) {
                for (p in info.requestedPermissions) {
                    if (p == permission) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }


    /**
     * Use this function to set multiple permissions at a time
     */
    fun setPermissions(permissions: Array<String>) {
        PermissionUtil.permissions = permissions
        checkIfPermissionPresentInAndroidManifest()
    }


    /**
     * Common permissions that we can access by one call
     */
    fun contactsRead() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_CONTACTS)
            checkIfPermissionPresentInAndroidManifest()
        }

    }

    fun contactsWrite() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.WRITE_CONTACTS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun contactsRW() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.WRITE_CONTACTS)
            permissions = arrayOf(Manifest.permission.READ_CONTACTS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun calendarRead() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_CALENDAR)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun calendarWrite() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.WRITE_CALENDAR)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun calendarRW() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_CALENDAR)
            permissions = arrayOf(Manifest.permission.WRITE_CALENDAR)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun storageRead() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun storageWrite() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun storageRW() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun locationFine() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun locationCoarse() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun locationBoth() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun camera() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.CAMERA)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun microphone() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneReadState() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_PHONE_STATE)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneCall() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.CALL_PHONE)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneReadCallLog() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_CALL_LOG)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneWriteCallLog() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.WRITE_CALL_LOG)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneAddVoiceMail() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.ADD_VOICEMAIL)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneSip() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.USE_SIP)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneOutgoing() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun phoneAll() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_PHONE_STATE)
            permissions = arrayOf(Manifest.permission.CALL_PHONE)
            permissions = arrayOf(Manifest.permission.READ_CALL_LOG)
            permissions = arrayOf(Manifest.permission.WRITE_CALL_LOG)
            permissions = arrayOf(Manifest.permission.ADD_VOICEMAIL)
            permissions = arrayOf(Manifest.permission.USE_SIP)
            permissions = arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun sensors() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.BODY_SENSORS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun smsSend() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.SEND_SMS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun smsReceive() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.RECEIVE_SMS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun smsRead() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.READ_SMS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun smsWap() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.RECEIVE_WAP_PUSH)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun smsMms() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.RECEIVE_MMS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    fun smsAll() {
        permissions?.let {
            permissions = arrayOf(Manifest.permission.SEND_SMS)
            permissions = arrayOf(Manifest.permission.RECEIVE_SMS)
            permissions = arrayOf(Manifest.permission.READ_SMS)
            permissions = arrayOf(Manifest.permission.RECEIVE_WAP_PUSH)
            permissions = arrayOf(Manifest.permission.RECEIVE_MMS)
            checkIfPermissionPresentInAndroidManifest()
        }
    }

    /**
     * Open current application detail activity so user can change permission manually.
     */
    fun openAppDetailsActivity() {
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + getContext<Context>().packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        getContext<Context>().startActivity(i)
    }
}