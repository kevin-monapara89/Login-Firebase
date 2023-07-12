package com.example.loginfirebase

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.loginfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.forgetpass.setOnClickListener {
            var email = binding.email.text.toString()

            auth.sendPasswordResetEmail(email)
        }
        binding.login.setOnClickListener {

            var email = binding.email.text.toString()
            var password = binding.password.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.e(TAG, "success")
                }
            }.addOnFailureListener{
                Log.e(TAG, "====== ${it.message}")
            }
        }

    }
}