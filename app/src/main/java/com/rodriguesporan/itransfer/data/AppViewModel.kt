package com.rodriguesporan.itransfer.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.Barcode
import com.rodriguesporan.itransfer.network.TransactionDatabaseService

class AppViewModel: ViewModel() {

    private val transactionDatabaseService = TransactionDatabaseService()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _transactions = MutableLiveData<MutableList<Transaction>>()
    val transactions: LiveData<MutableList<Transaction>> get() = _transactions

    private val _workflowState = MutableLiveData<WorkflowState>()
    val workflowState: LiveData<WorkflowState> get() = _workflowState

    private val _detectedBarcode = MutableLiveData<Barcode>()
    val detectedBarcode: LiveData<Barcode> get() = _detectedBarcode

    init {
        getTransactions()
    }

    /**
     * State set of the application workflow.
     */
    enum class WorkflowState {
        NOT_STARTED,
        DETECTING,
        DETECTED,
        SEARCHING,
        CONFIRMED,
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun setWorkflowState(workflowState: WorkflowState) {
        _workflowState.value = workflowState
    }

    fun setDetectedBarcode(detectedBarcode: Barcode) {
        _detectedBarcode.value = detectedBarcode
    }

    fun addTransaction(transaction: Transaction) {
        _transactions.value?.add(transaction)
    }

    fun addTransactions(transactions: MutableList<Transaction>) {
        _transactions.value?.addAll(transactions)
    }

    private fun getTransactions() {
        _transactions.value = mutableListOf()
    }
}