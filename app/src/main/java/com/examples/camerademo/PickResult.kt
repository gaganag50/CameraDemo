package com.examples.camerademo

import android.graphics.Bitmap
import android.net.Uri

class PickResult {
    var bitmap: Bitmap? = null
    var uri: Uri? = null
        private set
    var path: String? = null
        private set
    var error: Throwable? = null
        private set
    fun setUri(uri: Uri?): PickResult {
        this.uri = uri
        return this
    }

    fun setError(error: Exception?): PickResult {
        this.error = error
        return this
    }

    fun setError(error: Throwable?): PickResult {
        this.error = error
        return this
    }

    fun setPath(path: String?): PickResult {
        this.path = path
        return this
    }


}
