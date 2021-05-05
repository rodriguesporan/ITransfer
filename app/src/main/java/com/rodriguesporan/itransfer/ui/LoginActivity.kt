package com.rodriguesporan.itransfer.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.data.User
import com.rodriguesporan.itransfer.databinding.ActivityLoginBinding
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        binding.signInButton.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            db.collection("users")
                .whereEqualTo("googleUid", currentUser.uid)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        val newUserRef: DocumentReference = db.collection("users").document()
                        val user = User(
                            newUserRef.id,
                            currentUser.uid,
                            currentUser.displayName,
                            currentUser.email,
                            currentUser.phoneNumber,
                            currentUser.photoUrl.toString(),
                            1500.0,
                            Date()
                        )

                        newUserRef.set(user)
                            .addOnSuccessListener { startMainActivity() }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error writing user: ", exception)
                                Snackbar.make(
                                    findViewById(R.id.constraint_root_layout),
                                    "Error writing user",
                                    Snackbar.LENGTH_LONG
                                ).setBackgroundTint(resources.getColor(R.color.red_900))
                                    .setTextColor(resources.getColor(R.color.white)).show()
                            }
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting user: ", exception)
                    Snackbar.make(
                        findViewById(R.id.constraint_root_layout),
                        "Error getting user",
                        Snackbar.LENGTH_LONG
                    ).setBackgroundTint(resources.getColor(R.color.red_900))
                        .setTextColor(resources.getColor(R.color.white)).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed: ", e)
                Snackbar.make(
                    findViewById(R.id.constraint_root_layout),
                    "Google sign in failed",
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(resources.getColor(R.color.red_900))
                    .setTextColor(resources.getColor(R.color.white)).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(auth.currentUser)
                } else {
                    Log.w(TAG, "Sign in with credential failed: ", task.exception)
                    Snackbar.make(
                        findViewById(R.id.constraint_root_layout),
                        "Sign in with credential failed",
                        Snackbar.LENGTH_LONG
                    ).setBackgroundTint(resources.getColor(R.color.red_900))
                        .setTextColor(resources.getColor(R.color.white)).show()
                    updateUI(null)
                }
            }
    }

    private fun startMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 2000)
    }

    companion object {
        private const val TAG = "ITransfer"
        private const val RC_SIGN_IN = 9001
    }
}