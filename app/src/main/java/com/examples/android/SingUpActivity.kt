package com.examples.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.examples.android.firabaseDataClass.DataClassUser
import com.examples.android.myDialogAlert.LoadingAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SingUpActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var name: String
    private lateinit var surnameEditText: EditText
    private lateinit var surname: String
    private lateinit var emailEditText: EditText
    private lateinit var email: String
    private lateinit var passwordEditText: EditText
    private lateinit var password: String
    private lateinit var companyNameEditText: EditText
    private lateinit var companyName: String
    private lateinit var singUpButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        nameEditText            = findViewById(R.id.sing_up_name)
        surnameEditText         = findViewById(R.id.sing_up_surname)
        emailEditText           = findViewById(R.id.sing_up_email)
        passwordEditText        = findViewById(R.id.sing_up_password)
        companyNameEditText     = findViewById(R.id.sing_up_company_name)
        singUpButton            = findViewById(R.id.sing_up_button)
        mAuth                   = FirebaseAuth.getInstance()
        db                      = FirebaseFirestore.getInstance()

        val loadingAlertDialog = LoadingAlertDialog(this)

        singUpButton.setOnClickListener {
            name            = nameEditText.text.toString()
            surname         = surnameEditText.text.toString()
            email           = emailEditText.text.toString()
            password        = passwordEditText.text.toString()
            companyName     = companyNameEditText.text.toString()



            if (
                !isEmptyInputs() &&
                        isEmailAndPasswordCorrect(email, password)
                    ) {
                loadingAlertDialog.show()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            addUserInfo()
                        }else {
                            loadingAlertDialog.dismiss()
                            Toast.makeText(this, "Kayıt sırasında bir hata oluştu", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }


    }

    private fun addUserInfo() {
        //Kullanıcı hakkında Firestore'a eklenecek veriler
        val user = DataClassUser(name, surname, companyName, listOf())

        //Ekleme işlemi
        db.collection("users")
            .document(mAuth.uid.toString())
            .set(user)
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Kayıt sırasında bir hata oluştu",
                    Toast.LENGTH_SHORT
                ).show()
                throw e
            }.addOnSuccessListener {
                SharedPrefUtils.saveSharedPref(applicationContext, "LogIn", false)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }



    private fun isEmail(email: String): Boolean {
        //e-mail kontrolu yapılıyor
        //zaman kalırsa regex ile yap

        if (!email.contains("@")) {
            Toast.makeText(this, "Geçerli bir e-mail adresi giriniz", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isPassword(password: String): Boolean {
        if (password.length < 6) {
            Toast.makeText(this, "Şifre en az 6 karakter olmalı", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isEmailAndPasswordCorrect(email: String, password: String): Boolean {
        if (!isEmail(email)) {
            return false
        }
        if (!isPassword(password)) {
            return false
        }

        return true
    }

    private fun isEmptyInputs(): Boolean {

        val inputs = mapOf(
            "İsim" to name,
            "Soyisim" to surname,
            "E-mail" to email,
            "Şifre" to password,
            "Şirket Adı" to companyName
        )

        inputs.forEach {input ->
            input.value.ifEmpty {
                Toast.makeText(this, "Gerekli alanlar boş bırakılamaz: ${input.key}", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }
}
