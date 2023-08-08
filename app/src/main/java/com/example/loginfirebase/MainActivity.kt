package com.example.loginfirebase

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.loginfirebase.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    val Req_Code: Int = 123
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)


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
            }.addOnFailureListener {
                Log.e(TAG, "====== ${it.message}")
            }
        }


            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            auth = FirebaseAuth.getInstance()

            binding.googlesignin.setOnClickListener { view: View? ->
                Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
                signInGoogle()
            }
        }

        private fun signInGoogle() {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Req_Code)
        }

        // onActivityResult() function : this is where
        // we provide the task and data for the Google Account
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == Req_Code) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleResult(task)
            }
        }

        private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
            try {
                val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
                if (account != null) {
                    UpdateUI(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        // this is where we update the UI after Google signin takes place
        private fun UpdateUI(account: GoogleSignInAccount) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "succesfull", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }