package com.examples.android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.examples.android.myDialogAlert.LoadingAlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SingInActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var email: String
    private lateinit var passwordEditText: EditText
    private lateinit var password: String
    private lateinit var singInButton: Button
    private lateinit var singInCheckBox: CheckBox
    private var isRemember: Boolean = false
    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in)

        emailEditText       = findViewById(R.id.sing_in_email)
        passwordEditText    = findViewById(R.id.sing_in_passwords)
        singInButton        = findViewById(R.id.sing_in_button)
        singInCheckBox      = findViewById(R.id.sing_in_checkBox)

        val loadingAlertDialog = LoadingAlertDialog(this)

        val sharedPref = SharedPrefUtils.readSharedPref(this, "LogIn")
        val isSharedRemember = sharedPref.getBoolean("isRemember", false)
        if (isSharedRemember) {
            sharedPref.let {
                emailEditText.setText(it.getString("email", ""))
                passwordEditText.setText(it.getString("password", ""))
            }
        }


        singInButton.setOnClickListener {
            email       = emailEditText.text.toString().trim()
            password    = passwordEditText.text.toString().trim()
            isRemember  = singInCheckBox.isChecked

            if (!isEmailEmpty() && isEmail() && !isPasswordEmpty() && isPassword()) {
                loadingAlertDialog.show()
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            loadingAlertDialog.dismiss()
                            SharedPrefUtils.saveSharedPref(applicationContext, "LogIn",true)
                            if (isRemember) {
                                SharedPrefUtils.saveSharedPref(this, "LogIn").let {edit->
                                    edit.putBoolean("isRemember", true)
                                    edit.putString("email", email)
                                    edit.putString("password", password)
                                    edit.commit()
                                }
                            }
                            startActivity(Intent(this, MainActivity::class.java))
                        }else {
                            loadingAlertDialog.dismiss()
                            Toast.makeText(this, "Kullanıcı adı veya şifre yanlış", Toast.LENGTH_SHORT).show()
                        }
                    }
            }


        }

    }

    private fun isEmailEmpty(): Boolean {
        return if (email.isEmpty()) {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_email).error = "E-mail adresi boş"
            true
        }else {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_email).error = null
            false
        }
    }

    private fun isPasswordEmpty(): Boolean {
        return if (password.isEmpty()) {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_password).error = "Şifre boş"
            true
        }else {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_password).error = null
            false
        }
    }

    private fun isEmail(): Boolean {
        //e-mail kontrolu yapılıyor
        //zaman kalırsa regex ile yap

        return if (!email.contains("@")) {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_email).error = "Hatalı bir mail adresi"
            false
        }else {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_email).error = null
            true
        }


    }

    private fun isPassword(): Boolean {
        return if (password.length < 6) {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_password).error = "Şifre en az 6 karakter olmalı"
            false
        }else {
            findViewById<TextInputLayout>(R.id.sing_in_text_input_layout_password).error = null
            true
        }
    }


}

















