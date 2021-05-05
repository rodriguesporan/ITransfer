package com.rodriguesporan.itransfer.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.data.AppViewModel
import com.rodriguesporan.itransfer.data.User

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    private val viewModel: AppViewModel by viewModels()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        setupActionBarWithNavController(navController)

        auth = Firebase.auth
    }


    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            db.collection("users")
                .whereEqualTo("googleUid", currentUser.uid)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val user: User = documents.elementAt(0).toObject(User::class.java)
                        viewModel.setUser(user)
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

    companion object {
        private const val TAG = "ITransfer"
    }
}