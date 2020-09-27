package com.examples.camerademo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        return when (item.itemId) {
            R.id.miCamera -> {
                cameraClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cameraClicked() {
        Toast.makeText(this, "Camera Clicked", Toast.LENGTH_SHORT).show()
    }
}