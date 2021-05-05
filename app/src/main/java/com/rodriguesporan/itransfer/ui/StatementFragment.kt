package com.rodriguesporan.itransfer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.adapter.TransactionAdapter
import com.rodriguesporan.itransfer.databinding.FragmentStatementBinding
import com.rodriguesporan.itransfer.data.AppViewModel
import com.rodriguesporan.itransfer.data.Transaction
import com.rodriguesporan.itransfer.data.User

class StatementFragment : Fragment() {

    private lateinit var binding: FragmentStatementBinding

    private val viewModel: AppViewModel by activityViewModels()
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStatementBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@StatementFragment
            appViewModel = viewModel
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = TransactionAdapter()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        updateUI(viewModel.user.value)
    }

    private fun updateUI(user: User?) {
        if (user != null) {
            db.collection("transactions")
                    .whereArrayContains("usersUid", user.uid!!)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val transactions: List<Transaction> = documents.mapNotNull {
                                it.toObject(Transaction::class.java)
                            }
                            viewModel.setTransactions(transactions)
                        }
                    }.addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting transactions: ", exception)
                        Snackbar.make(
                            binding.recyclerView,
                            "Error getting transactions",
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