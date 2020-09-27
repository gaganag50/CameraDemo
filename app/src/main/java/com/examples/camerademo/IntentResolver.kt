package com.examples.camerademo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class IntentResolver(val activity: AppCompatActivity) {

    private var galleryIntent: Intent? = null
    private var cameraIntent: Intent? = null
    private var saveFile: File? = null
    private fun loadSystemPackages(intent: Intent): Intent {
        val resInfo =
            activity.packageManager.queryIntentActivities(intent, PackageManager.MATCH_SYSTEM_ONLY)
        if (!resInfo.isEmpty()) {
            val packageName = resInfo[0].activityInfo.packageName
            intent.setPackage(packageName)
        }
        return intent
    }

    private val isCamerasAvailable: Boolean
        get() {
            return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        }

    private fun getCameraIntent(): Intent {
        if (cameraIntent == null) {
            cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent!!.putExtra(MediaStore.EXTRA_OUTPUT, cameraUriForProvider())
            applyProviderPermission()
        }
        return cameraIntent!!
    }

    fun launchCamera(listener: Fragment) {
        if (getCameraIntent().resolveActivity(activity.packageManager) != null) {
            cameraFile().delete()
            listener.startActivityForResult(loadSystemPackages(getCameraIntent()), REQUESTER)
        }
    }

    /**
     * Granting permissions to write and read for available cameras to file provider.
     */
    private fun applyProviderPermission() {
        val resInfoList = activity.packageManager.queryIntentActivities(
            cameraIntent!!, PackageManager.MATCH_DEFAULT_ONLY
        )
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            activity.grantUriPermission(
                packageName,
                cameraUriForProvider(),
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    private fun cameraFile(): File {
        if (saveFile != null) {
            return saveFile!!
        }
        val directory: File
        val fileName: String
        val applicationInfo = activity.applicationInfo
        val stringId = applicationInfo.labelRes
        val appName =
            if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else activity.getString(
                stringId
            )
        directory = File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), appName)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        fileName =
            "$timeStamp.jpg"

        // File directory = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"teste");
        directory.mkdirs()
        saveFile = File(directory, fileName)
        Log.i("File-PickImage", saveFile!!.absolutePath)
        return saveFile!!
    }


    private val authority: String
        get() = activity.application.packageName + activity.getString(R.string.provider_package)

    private fun cameraUriForProvider(): Uri {
        return try {
            FileProvider.getUriForFile(activity, authority, cameraFile())
        } catch (e: Exception) {
            if (e.message!!.contains("ProviderInfo.loadXmlMetaData")) {
                throw Error(activity.getString(R.string.wrong_authority))
            } else {
                throw e
            }
        }
    }

    private fun getGalleryIntent(): Intent {
        if (galleryIntent == null) {
            galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent!!.type = activity.getString(R.string.image_content_type)

        }
        return galleryIntent!!
    }

    fun launchGallery(listener: Fragment) {
        listener.startActivityForResult(loadSystemPackages(getGalleryIntent()), REQUESTER)
    }


    private val allPermissionsNeeded: Array<String>
        get() = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )



    fun requestCameraPermissions(listener: Fragment): Boolean {
        return requestPermissions(listener, *allPermissionsNeeded)
    }

    fun requestGalleryPermissions(listener: Fragment): Boolean {
        return requestPermissions(listener, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * resquest permission to use camera and write files
     */
    fun requestPermissions(listener: Fragment, vararg permissionsNeeded: String): Boolean {
        val list: MutableList<String> = ArrayList()
        for (permission in permissionsNeeded) if (ContextCompat.checkSelfPermission(
                activity, permission
            ) == PackageManager.PERMISSION_DENIED
        ) list.add(permission)
        if (list.isEmpty()) return true
        listener.requestPermissions(list.toTypedArray(), REQUESTER)
        return false
    }




    companion object {
        const val REQUESTER = 99
//        const val SAVE_FILE_PATH_TAG = "savePath"
    }


}
