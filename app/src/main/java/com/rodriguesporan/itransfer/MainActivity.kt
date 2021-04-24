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
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.model.AppViewModel
import com.rodriguesporan.itransfer.model.Transaction
import com.rodriguesporan.itransfer.model.User
import com.rodriguesporan.itransfer.network.UserDatabaseService
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {

    private val viewModel: AppViewModel by viewModels()
    private lateinit var navController: NavController
//    private val userDatabaseService = UserDatabaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        setupActionBarWithNavController(navController)

        /*userDatabaseService.writeNewUser(
                "+5511953158695",
                assets = 1500.0,
                timestamp = Timestamp(System.currentTimeMillis()).time,
                firstName = "André",
                lastName = "Rodrigues",
                QRToken = "QRToken"
        )
        userDatabaseService.readUser("-MYl-NTXttZTSkYnB8c3")?.let {
            Log.d(TAG, it.phone.toString() )
            viewModel.setUser(it)
        }*/
        Firebase.database.reference
                .child("users")
                .child("-MYl-NTXttZTSkYnB8c3")
                .get()
                .addOnSuccessListener {
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        Log.d(TAG, user.firstName.toString())
                        viewModel.setUser(user)
                    }
                }

        Firebase.database.reference
                .child("transactions")
                .child("-MYl1arFQIbtdrrolCO1")
                .get()
                .addOnSuccessListener {
                    val transaction = it.getValue(Transaction::class.java)
                    if (transaction != null) {
                        Log.d(TAG, transaction.amount.toString())
                        viewModel.setTransactions(mutableListOf(transaction))
                    }
                }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "ITransfer"
    }
}