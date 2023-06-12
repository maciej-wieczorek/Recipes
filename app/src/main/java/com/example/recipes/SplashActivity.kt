package com.example.recipes

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    private val INTERNET_PERMISSION_REQUEST_CODE = 123 // Use any unique request code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setColorFilter(getColor(R.color.purple_200))
        val rotationAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
        rotationAnimator.duration = 2000
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.start()

        val loadingThread = object : Thread() {
            override fun run() {
                try {
                    if (isFirstLaunch()) {
                        fetchData()
                    }
                    sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    synchronized(this) {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        loadingThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPrefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("FirstLaunch", true).apply()
    }

    private fun isFirstLaunch(): Boolean {
        val sharedPrefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPrefs.getBoolean("FirstLaunch", true)
        if (isFirstLaunch) {
            sharedPrefs.edit().putBoolean("FirstLaunch", false).apply()
        }
        return isFirstLaunch
    }

    private fun fetchData() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET),
                INTERNET_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted
            Recipe.fetchRecipes()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == INTERNET_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Recipe.fetchRecipes()
            } else {
                // Permission denied
            }
        }
    }
}