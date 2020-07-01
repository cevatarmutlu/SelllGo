package com.examples.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)


        val intent: Intent = if (SharedPrefUtils.readSharedPref(applicationContext, "LogIn", false)) {
            Intent(this, MainActivity::class.java)
        }else {
            Intent(this, EntryActivity::class.java)
        }
        startActivity(intent)



        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.INTERNET),
                REQUEST_INTERNET_PERMISSION
            )
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE_PERMISSION
            )
        }*/

    }
/*
    companion object {
        private const val REQUEST_INTERNET_PERMISSION = 0
        private const val REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1
    }
*/
}
