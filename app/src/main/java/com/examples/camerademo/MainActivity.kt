package com.examples.camerademo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity(), IPickResult {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miCamera -> {
                cameraClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cameraClicked() {
        PickImageDialog.build().show(this)
    }

    override fun onPickResult(r: PickResult?) {
        if (r?.error != null) {
            Toast.makeText(this, r.error?.message, Toast.LENGTH_LONG).show()
        }
        val b = setScaledBitmap(r!!.uri!!)
        result_image.setImageBitmap(b)

    }

    @Throws(FileNotFoundException::class)
    private fun getOptions(uri: Uri): BitmapFactory.Options? {
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(this.contentResolver.openInputStream(uri), null, options)
        var w = options.outWidth
        var h = options.outHeight
        var scale = 1
        while (true) {
            if (w / 2 < cardView.width || h / 2 < cardView.height) break
            w /= 2
            h /= 2
            scale *= 2
        }
        options = BitmapFactory.Options()
        options.inSampleSize = scale
        return options
    }

    @Throws(FileNotFoundException::class)
    private fun scaleDown(uri: Uri): Bitmap? {
        return BitmapFactory.decodeStream(
            this.contentResolver.openInputStream(uri),
            null,
            getOptions(uri)
        )
    }

    private fun setScaledBitmap(uri: Uri): Bitmap {
        return scaleDown(uri)!!

    }


}