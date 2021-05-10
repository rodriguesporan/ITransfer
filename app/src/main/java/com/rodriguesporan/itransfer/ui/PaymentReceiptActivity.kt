package com.rodriguesporan.itransfer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.data.Transaction
import com.rodriguesporan.itransfer.data.User
import com.rodriguesporan.itransfer.databinding.ActivityPaymentReceiptBinding

class PaymentReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentReceiptBinding
    private lateinit var transactionUid: String
    private lateinit var receiverUid: String
    private lateinit var senderUid: String

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            transactionUid = intent.extras!!.get("TRANSACTION_UID").toString()
            receiverUid = intent.extras!!.get("RECEIVER_UID").toString()
            senderUid = intent.extras!!.get("SENDER_UID").toString()
            updateUI()
        }
    }

    private fun updateUI() {
        if (transactionUid != null) {
            binding.transactionUidTextView.text = transactionUid
            binding.receiverUidTextView.text = receiverUid
            binding.senderUidTextView.text = senderUid

            db.collection("transactions")
                    .document(transactionUid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val transaction: Transaction? = documentSnapshot.toObject(Transaction::class.java)
                            binding.dateTextView.text = transaction?.formatCreatedAt()
                            binding.amountTextView.text = resources.getString(R.string.prefix_currency_symbol, transaction?.formatAmount())
                        }
                    }.addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting transaction: ", exception)
                        Snackbar.make(
                                binding.linearRootLayout,
                                "Error getting transaction",
                                Snackbar.LENGTH_LONG
                        ).setBackgroundTint(resources.getColor(R.color.red_900))
                                .setTextColor(resources.getColor(R.color.white)).show()
                    }

            db.collection("users")
                    .document(receiverUid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val user: User? = documentSnapshot.toObject(User::class.java)
                            binding.receiverNameTextView.text = user?.displayName
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

            db.collection("users")
                    .document(senderUid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val user: User? = documentSnapshot.toObject(User::class.java)
                            binding.senderNameTextView.text = user?.displayName
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