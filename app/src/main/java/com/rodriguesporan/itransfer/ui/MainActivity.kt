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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.data.AppViewModel
import com.rodriguesporan.itransfer.data.Transaction
import com.rodriguesporan.itransfer.data.User

class MainActivity : AppCompatActivity() {

    private val viewModel: AppViewModel by viewModels()
    private lateinit var navController: NavController
    private val userReference: DatabaseReference = Firebase.database.reference.child("users")
    private val transactionReference: DatabaseReference = Firebase.database.reference.child("transactions")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

//        window.decorView.setBackgroundColor(android.R.color.transparent)
        setupActionBarWithNavController(navController)

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        viewModel.setUser(user)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException())
            }
        }
        userReference.child("-MYl-NTXttZTSkYnB8c3").addValueEventListener(userListener)

        val transactionsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val transactions = dataSnapshot.children.mapNotNull { it.getValue(Transaction::class.java) }.toMutableList()
                    viewModel.addTransactions(transactions)
                }
            }

            override fun onCancelled(e: DatabaseError) {
                Log.w(TAG, "loadTransaction:onCancelled", e.toException())
            }
        }
        transactionReference.orderByChild("timestamp").addValueEventListener(transactionsListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "ITransfer"
    }
}