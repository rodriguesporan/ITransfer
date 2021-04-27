package com.rodriguesporan.itransfer.network

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.data.User

class UserDatabaseService(
        private val userRef: DatabaseReference = Firebase.database.reference.child("users")
) {

    fun writeNewUser(phone: String, assets: Double, timestamp: Long, firstName: String, lastName: String, QRToken: String) {
        val key = userRef.push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }

        val newUser = User(
                key,
                phone,
                QRToken,
                lastName,
                firstName,
                assets,
                timestamp
        )

        userRef.child("$key").setValue(newUser)
    }

    fun readUser(userUid: String): User? {
        var user: User? = null
        userRef.child(userUid).get().addOnSuccessListener { user = it.getValue(User::class.java) }

        return user
    }

    companion object {
        private const val TAG = "ITransfer"
    }
}