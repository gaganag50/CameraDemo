package com.examples.camerademo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog.*


/**
 * Created by jrvansuita build 01/11/16.
 */
class PickImageDialog : DialogFragment() {
    val DIALOG_FRAGMENT_TAG: String =
        PickImageDialog::class.java.simpleName
    private var resolver: IntentResolver? = null


    private var onPickResult: IPickResult? = null
    private var llButtons: LinearLayout? = null
    private var tvTitle: TextView? = null
    private var tvCamera: TextView? = null
    private var tvGallery: TextView? = null
    private var tvCancel: TextView? = null
    private var tvProgress: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog, container, false)
        onAttaching()
        if (dialog!!.window != null) {
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        onBindViews(view)
        this.resolver = IntentResolver(activity as AppCompatActivity)
        onBindViewListeners()


        return view

    }

    private fun onAttaching() {
        if (onPickResult == null && activity is IPickResult) {
            onPickResult = activity as IPickResult?
        }
    }

    private fun onBindViews(v: View) {
        llButtons = v.findViewById<View>(R.id.buttons_holder) as LinearLayout
        tvTitle = v.findViewById<View>(R.id.title) as TextView
        tvCamera = v.findViewById<View>(R.id.camera) as TextView
        tvGallery = v.findViewById<View>(R.id.gallery) as TextView
        tvCancel = v.findViewById<View>(R.id.cancel) as TextView
        tvProgress = v.findViewById<View>(R.id.loading_text) as TextView
    }

    private fun onBindViewListeners() {
        tvCancel!!.setOnClickListener(listener)
        tvCamera!!.setOnClickListener(listener)
        tvGallery!!.setOnClickListener(listener)
    }

    private val listener: View.OnClickListener = View.OnClickListener { view ->
        if (view.id == R.id.cancel) {
            //onCancelClick()
            dismiss()
        } else {
            if (view.id == R.id.camera) {
                onCameraClick()
            } else if (view.id == R.id.gallery) {
                onGalleryClick()
            }
        }
    }

    fun show(fragmentActivity: FragmentActivity): PickImageDialog {
        return show(fragmentActivity.supportFragmentManager)
    }

    fun show(fragmentManager: FragmentManager?): PickImageDialog {
        fragmentManager?.let { super.show(it, DIALOG_FRAGMENT_TAG) }
        return this
    }

    fun onCameraClick() {
        launchCamera()
    }

    fun onGalleryClick() {
        launchGallery()
    }




    protected fun launchCamera() {
        if (resolver!!.requestCameraPermissions(this)) {
            resolver!!.launchCamera(this)
        }
    }

    protected fun launchGallery() {
        if (resolver!!.requestGalleryPermissions(this)) {
            resolver!!.launchGallery(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentResolver.REQUESTER) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("mainActivity", "onActivityResult: cameraClicked")
            } else {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == IntentResolver.REQUESTER) {
            var granted = true
            for (i in grantResults) {
                granted = granted && i == PackageManager.PERMISSION_GRANTED
            }
            if (granted) {
                // See if the CAMERA permission is among the granted ones
                var cameraIndex = -1
                for (i in permissions.indices) {
                    if (permissions[i] == Manifest.permission.CAMERA) {
                        cameraIndex = i
                        break
                    }
                }
                if (cameraIndex == -1) {
                    launchGallery()
                } else {
                    launchCamera()
                }
            } else {
                dismissAllowingStateLoss()
//                if (grantResults.size > 1) Keep.with(getActivity()).askedForPermission()
            }
        }
    }

    companion object {

        fun build(): PickImageDialog {
            val d = PickImageDialog()
            return d
        }


    }
}


