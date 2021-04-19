package com.rodriguesporan.itransfer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        setupActionBarWithNavController(navController)

        // Write a message to the database
        reference = Firebase.database.reference
        writeNewUser("userIdOne", "+5511953158695")
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun writeNewUser(userId: String, phone: String) {
        val newUser = User(phone)
        reference.child("users").child(userId).setValue(newUser)
                .addOnSuccessListener { Log.i(TAG, "Got user ${it.toString()}") }
                .addOnFailureListener { Log.e(TAG, "Something got wrong: ${it.toString()}") }

    }

    companion object {
        private const val TAG = "CameraXBasic"
    }
}