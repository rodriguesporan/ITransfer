package com.rodriguesporan.itransfer

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.model.AppViewModel
import com.rodriguesporan.itransfer.model.User
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {

//    private val viewModel: AppViewModel by viewModels()

    private lateinit var navController: NavController
//    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        setupActionBarWithNavController(navController)

        /*reference = Firebase.database.reference
        reference.child("users").child("userIdOne").get().addOnSuccessListener {
            val user = it.getValue(User::class.java)
            if (user != null) {
                viewModel.setUser(user)
                Log.i(TAG, "Got value ${viewModel.user.value?.firstName}")
            }
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /*private fun writeNewUser(userId: String, phone: String) {
        val newUser = User(phone
        , assets = 1500.0, timestamp = Timestamp(System.currentTimeMillis()).time, firstName = "André", lastName = "Rodrigues", QRToken = "QRToken"
        )
        reference.child("users").child(userId).setValue(newUser)
    }*/

    companion object {
        private const val TAG = "ITransfer"
    }
}