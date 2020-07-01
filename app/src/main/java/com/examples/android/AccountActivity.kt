package com.examples.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)


        findViewById<Button>(R.id.account_exit).setOnClickListener {
            SharedPrefUtils.saveSharedPref(applicationContext, "LogIn", false)
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SplashScreen::class.java)
            startActivity(intent)
        }
    }
}
