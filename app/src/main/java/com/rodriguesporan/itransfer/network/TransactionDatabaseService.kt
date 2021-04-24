package com.rodriguesporan.itransfer.network

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.model.Transaction
import com.rodriguesporan.itransfer.model.User
import java.sql.Timestamp

class TransactionDatabaseService(
        private val transactionRef: DatabaseReference = Firebase.database.reference.child("transactions")
) {

    fun writeNewTransaction(amount: Double, senderId: String, timestamp: Long) {
        val key = transactionRef.push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }

        val newTransaction = Transaction(key, amount, senderId, timestamp = timestamp)
        transactionRef.child("$key").setValue(newTransaction)
    }

    fun readTransactions(userUid: String): List<Transaction>? {
        var transactions: MutableList<Transaction>? = mutableListOf()
        transactionRef.get().addOnSuccessListener { transactions = it.getValue<MutableList<Transaction>>() }

        return transactions
    }

    companion object {
        private const val TAG = "ITransfer"
    }
}