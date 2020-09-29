package com.examples.camerademo

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


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
        Log.d("mainActivity", "onPickResult: ${r?.path}")
        if (r?.error != null) {
            Toast.makeText(this, r.error?.message, Toast.LENGTH_LONG).show()
        }
    }


}