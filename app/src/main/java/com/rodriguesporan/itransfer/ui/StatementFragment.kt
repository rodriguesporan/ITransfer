package com.rodriguesporan.itransfer.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentChange
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

    private fun updateUI(currentUser: User?) {
        if (currentUser != null) {
            db.collection("transactions")
                    .whereArrayContains("usersUid", currentUser.uid!!)
                    .addSnapshotListener { documents, e ->
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        if (documents != null) {
                            Toast.makeText(context, "Reading transactions collection", Toast.LENGTH_SHORT).show()
                            /*documents.filter {
                                it != null &&
                            }*/
                            val transactions: List<Transaction> = documents
                                    .mapNotNull { queryDocumentSnapshot ->
                                        queryDocumentSnapshot.toObject(Transaction::class.java).also {
                                            it.setCurrentUserUid(currentUser.uid!!)
                                        }
                                    }

                            /*for (dc in documents!!.documentChanges) {
                                if (dc.type == DocumentChange.Type.ADDED) {
                                    Log.d(TAG, "New city: ${dc.document.data}")
                                }
                                when (dc.type) {
                                    DocumentChange.Type.ADDED -> Log.d(TAG, "New city: ${dc.document.data}")
                                    DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified city: ${dc.document.data}")
                                    DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed city: ${dc.document.data}")
                                }
                            }*/

                            viewModel.setTransactions(transactions)
                        }
                    }
        }
    }

    companion object {
        private const val TAG = "ITransfer"
    }
}